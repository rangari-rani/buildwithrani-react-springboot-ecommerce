package com.buildwithrani.backend.audit.service;

import com.buildwithrani.backend.audit.enums.ActorRole;

public interface AuditService {

    void logAction(
            Long actorId,
            ActorRole actorRole,
            String action,
            String entityType,
            Long entityId
    );
}
