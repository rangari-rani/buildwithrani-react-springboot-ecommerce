package com.buildwithrani.backend.audit.service;

import com.buildwithrani.backend.audit.entity.AuditLog;
import com.buildwithrani.backend.audit.enums.ActorRole;
import com.buildwithrani.backend.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    // Overloaded method for simple logging (Backwards compatibility)
    @Override
    public void logAction(Long actorId, ActorRole actorRole, String action, String entityType, Long entityId) {
        this.logAction(actorId, actorRole, action, entityType, entityId, null);
    }

    // New primary method with Metadata support
    @Override
    public void logAction(
            Long actorId,
            ActorRole actorRole,
            String action,
            String entityType,
            Long entityId,
            String metadata
    ) {
        AuditLog auditLog = new AuditLog(
                actorId,
                actorRole,
                action,
                entityType,
                entityId,
                metadata // Now capturing the "Why" and "How"
        );

        auditLogRepository.save(auditLog);
    }
}