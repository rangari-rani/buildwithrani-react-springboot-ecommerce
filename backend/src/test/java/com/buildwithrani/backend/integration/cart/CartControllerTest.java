package com.buildwithrani.backend.integration.cart;

import com.buildwithrani.backend.cart.dto.AddToCartRequest;
import com.buildwithrani.backend.cart.dto.CartResponse;
import com.buildwithrani.backend.cart.dto.UpdateCartItemRequest;
import com.buildwithrani.backend.cart.service.CartService;
import com.buildwithrani.backend.common.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(roles = "USER")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnUserCart() throws Exception {

        CartResponse response = new CartResponse(List.of());

        when(cartService.getCart()).thenReturn(response);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk());

        verify(cartService).getCart();
    }

    @Test
    void shouldAddProductToCart() throws Exception {

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        when(cartService.addToCart(any(AddToCartRequest.class)))
                .thenReturn(new CartResponse(List.of()));

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(cartService).addToCart(any(AddToCartRequest.class));
    }

    @Test
    void shouldUpdateCartItemQuantity() throws Exception {

        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setProductId(1L);
        request.setQuantity(5);

        when(cartService.updateCartItem(any(UpdateCartItemRequest.class)))
                .thenReturn(new CartResponse(List.of()));

        mockMvc.perform(put("/api/cart/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(cartService).updateCartItem(any(UpdateCartItemRequest.class));
    }

    @Test
    void shouldRemoveItemFromCart() throws Exception {

        mockMvc.perform(delete("/api/cart/remove/{productId}", 1L))
                .andExpect(status().isNoContent());

        verify(cartService).removeItem(1L);
    }

    @Test
    void shouldClearCart() throws Exception {

        mockMvc.perform(delete("/api/cart/clear"))
                .andExpect(status().isNoContent());

        verify(cartService).clearCart();
    }

    @Test
    void shouldReturnBadRequestWhenAddToCartRequestIsInvalid() throws Exception {

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(null);   // invalid
        request.setQuantity(0);       // invalid

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(cartService, never()).addToCart(any());
    }

    @Test
    void shouldReturnEmptyCartWhenNoItemsExist() throws Exception {

        CartResponse response = new CartResponse(List.of());

        when(cartService.getCart()).thenReturn(response);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk());

        verify(cartService).getCart();
    }

    @Test
    void shouldReturnNotFoundWhenProductNotInCart() throws Exception {

        doThrow(new ResourceNotFoundException("Product not found"))
                .when(cartService).removeItem(1L);

        mockMvc.perform(delete("/api/cart/remove/{productId}", 1L))
                .andExpect(status().isNotFound());
    }
}
