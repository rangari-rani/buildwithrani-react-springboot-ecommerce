package com.buildwithrani.backend.audit.entity;

import com.buildwithrani.backend.audit.enums.ActorRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long actorId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String entityType; // e.g., "PRODUCT", "ORDER"

    @Column(nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActorRole actorRole;

    /**
     * Store extra details here like "Price changed from 10 to 20"
     * or "User IP: 192.168.1.1". Using TEXT to allow long JSON strings.
     */
    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    // Updated constructor to include metadata
    public AuditLog(
            Long actorId,
            ActorRole actorRole,
            String action,
            String entityType,
            Long entityId,
            String metadata
    ) {
        this.actorId = actorId;
        this.actorRole = actorRole;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.metadata = metadata;
    }
}
