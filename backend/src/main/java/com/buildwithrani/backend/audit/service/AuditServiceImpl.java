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

    @Override
    public void logAction(
            Long actorId,
            ActorRole actorRole,
            String action,
            String entityType,
            Long entityId
    ) {
        AuditLog auditLog = new AuditLog(
                actorId,
                actorRole,
                action,
                entityType,
                entityId
        );

        auditLogRepository.save(auditLog);
    }
}
