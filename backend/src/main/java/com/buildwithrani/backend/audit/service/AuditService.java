package com.buildwithrani.backend.audit.service;

import com.buildwithrani.backend.audit.enums.ActorRole;

public interface AuditService {

    // 1. Simple version (without metadata)
    void logAction(
            Long actorId,
            ActorRole actorRole,
            String action,
            String entityType,
            Long entityId
    );

    // 2. Advanced version (with metadata)
    void logAction(
            Long actorId,
            ActorRole actorRole,
            String action,
            String entityType,
            Long entityId,
            String metadata
    );
}