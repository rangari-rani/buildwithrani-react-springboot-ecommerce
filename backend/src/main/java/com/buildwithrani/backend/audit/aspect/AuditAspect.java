package com.buildwithrani.backend.audit.aspect;

import com.buildwithrani.backend.audit.entity.Audit;
import com.buildwithrani.backend.audit.enums.ActorRole;
import com.buildwithrani.backend.audit.service.AuditService;
import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import com.buildwithrani.backend.auth.security.SecurityUtils;
import com.buildwithrani.backend.cart.dto.CartResponse; // Ensure this matches your package
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final UserRepository userRepository;

    @AfterReturning(pointcut = "@annotation(auditAnnotation)", returning = "result")
    public void logAuditActivity(JoinPoint joinPoint, Audit auditAnnotation, Object result) {

        // 1. Resolve Actor Details from Security Context
        String email = SecurityUtils.getCurrentUserEmail();
        Long actorId = null;
        ActorRole actorRole = ActorRole.USER;

        if (email != null) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                actorId = user.getId();
                if (user.getRole() != null && user.getRole().name().equalsIgnoreCase("ADMIN")) {
                    actorRole = ActorRole.ADMIN;
                }
            }
        }

        // 2. Extract Entity ID (Handles result objects and method arguments)
        Long entityId = extractId(result, joinPoint.getArgs());

        // 3. Extract Smart Metadata
        String metadata = extractMetadata(result, joinPoint.getArgs());

        // 4. Execute Logging
        auditService.logAction(
                actorId,
                actorRole,
                auditAnnotation.action(),
                auditAnnotation.entityType(),
                entityId,
                metadata
        );
    }

    private String extractMetadata(Object result, Object[] args) {
        // A. Handle Cart Actions
        if (result instanceof CartResponse) {
            CartResponse cart = (CartResponse) result;
            int itemCount = (cart.getItems() != null) ? cart.getItems().size() : 0;

            if (itemCount == 0) {
                return "User cleared all items from cart";
            }

            // NEW: Try to get Quantity from the Request Object (AddToCartRequest/UpdateCartItemRequest)
            if (args != null && args.length > 0) {
                try {
                    Method getQty = args[0].getClass().getMethod("getQuantity");
                    Object qty = getQty.invoke(args[0]);
                    if (qty != null) {
                        return "Qty: " + qty + " | Items in cart: " + itemCount;
                    }
                } catch (Exception e) {
                    // No getQuantity method found in this specific request object
                }
            }

            return "Items in cart: " + itemCount;
        }

        // B. Handle Order Actions (Capture total price)
        if (result != null && result.getClass().getSimpleName().contains("Order")) {
            try {
                Method getAmount = result.getClass().getMethod("getTotalAmount");
                return "Total: " + getAmount.invoke(result).toString();
            } catch (Exception e) { }
        }

        // C. Handle Status Toggles (Product featured/active)
        if (args != null && args.length >= 2) {
            return "Value: " + args[1].toString();
        }


        return "AOP-tracked action";
    }

    private Long extractId(Object result, Object[] args) {
        // 1. Try to get ID from the result (Works for OrderResponse/Product)
        if (result != null) {
            String[] methodNames = {"getId", "getOrderId", "getCartId"};
            for (String methodName : methodNames) {
                try {
                    Method method = result.getClass().getMethod(methodName);
                    return (Long) method.invoke(result);
                } catch (Exception e) { }
            }
        }

        // 2. Try to get ID from method arguments (Works for Cart)
        if (args != null && args.length > 0) {
            // Case A: Argument is a simple Long (like removeItem(Long productId))
            if (args[0] instanceof Long) {
                return (Long) args[0];
            }

            // Case B: Argument is a Request Object (like AddToCartRequest)
            try {
                Method getProductId = args[0].getClass().getMethod("getProductId");
                return (Long) getProductId.invoke(args[0]);
            } catch (Exception e) {
                // If it's not a productId, maybe it's an orderId in a request?
            }
        }
        return 0L;
    }
}