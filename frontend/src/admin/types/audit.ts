// src/admin/types/audit.ts

export type ActorRole = "ADMIN" | "USER";

export type EntityType = "ORDER" | "PRODUCT";

export interface AuditLog {
  id: number;
  actorId: number | null;
  actorRole: ActorRole;
  action: string;
  entityType: EntityType;
  entityId: number;
  timestamp: string; // ISO string from backend
}
