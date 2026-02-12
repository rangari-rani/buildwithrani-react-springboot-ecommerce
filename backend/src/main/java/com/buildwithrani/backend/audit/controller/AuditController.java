package com.buildwithrani.backend.audit.controller;

import com.buildwithrani.backend.audit.entity.AuditLog;
import com.buildwithrani.backend.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public List<AuditLog> getAuditLogs(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) Long entityId
    ) {

        if (entityType != null && entityId != null) {
            return auditLogRepository
                    .findByEntityTypeAndEntityIdOrderByTimestampDesc(
                            entityType,
                            entityId
                    );
        }

        if (entityType != null) {
            return auditLogRepository.findByEntityTypeOrderByTimestampDesc(entityType);
        }


        return auditLogRepository.findAllByOrderByTimestampDesc();
    }
}

