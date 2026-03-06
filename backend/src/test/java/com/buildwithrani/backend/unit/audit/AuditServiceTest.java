package com.buildwithrani.backend.unit.audit;

import static org.mockito.Mockito.*;

import com.buildwithrani.backend.audit.entity.AuditLog;
import com.buildwithrani.backend.audit.enums.ActorRole;
import com.buildwithrani.backend.audit.repository.AuditLogRepository;


import com.buildwithrani.backend.audit.service.AuditServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AuditServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    public AuditServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLogActionWithMetadata() {

        auditService.logAction(
                1L,
                ActorRole.ADMIN,
                "UPDATE_PRODUCT",
                "PRODUCT",
                10L,
                "Price changed"
        );

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void shouldLogActionWithoutMetadata() {

        auditService.logAction(
                1L,
                ActorRole.ADMIN,
                "DELETE_ORDER",
                "ORDER",
                5L
        );

        verify(auditLogRepository).save(any(AuditLog.class));
    }
}