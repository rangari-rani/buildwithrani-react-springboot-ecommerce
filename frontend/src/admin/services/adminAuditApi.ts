// src/admin/services/adminAuditApi.ts

import axiosInstance from "../../api/axiosInstance";
import type { EntityType, AuditLog } from "../types/audit";


interface FetchAuditLogsParams {
  entityType?: EntityType;
  entityId?: number;
}

export const adminAuditApi = {
  /**
   * Fetch audit logs
   *
   * - If no params → fetch all logs
   * - If entityType + entityId → fetch filtered logs
   */
  async fetchAuditLogs(
    params?: FetchAuditLogsParams
  ): Promise<AuditLog[]> {
    const response = await axiosInstance.get<AuditLog[]>(
      "/admin/audit-logs",
      {
        params,
      }
    );

    return response.data;
  },
};
