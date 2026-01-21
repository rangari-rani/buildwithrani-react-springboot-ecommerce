// src/admin/components/AdminAuditLogsTable.tsx

import React from "react";
import type { AuditLog } from "../types/audit";
import { Link } from "react-router-dom";

interface AdminAuditLogsTableProps {
  logs: AuditLog[];
  loading?: boolean;
}
const roleBadgeClasses: Record<string, string> = {
  ADMIN: "bg-purple-100 text-purple-700",
  USER: "bg-blue-100 text-blue-700",
};

const entityBadgeClasses: Record<string, string> = {
  ORDER: "bg-green-100 text-green-700",
  PRODUCT: "bg-orange-100 text-orange-700",
};

const AdminAuditLogsTable: React.FC<AdminAuditLogsTableProps> = ({
  logs,
  loading = false,
}) => {
  if (loading) {
    return (
      <div className="p-4 text-sm text-gray-500">
        Loading audit logs...
      </div>
    );
  }

  if (!logs.length) {
    return (
      <div className="p-4 text-sm text-gray-500">
        No audit activity recorded yet.
      </div>
    );
  }

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full border border-gray-200 text-sm">
        <thead className="bg-gray-100">
          <tr>
            <th className="px-3 py-2 border">Timestamp</th>
            <th className="px-3 py-2 border">Actor Role</th>
            <th className="px-3 py-2 border">Actor ID</th>
            <th className="px-3 py-2 border">Action</th>
            <th className="px-3 py-2 border">Entity</th>
            <th className="px-3 py-2 border">Entity ID</th>
          </tr>
        </thead>
        <tbody>
          {logs.map((log) => (
            <tr key={log.id} className="hover:bg-gray-50">
              <td className="px-3 py-2 border whitespace-nowrap">
                {new Date(log.timestamp).toLocaleString()}
              </td>

              <td className="px-3 py-2 border">
                <span
                  className={`px-2 py-0.5 rounded text-xs font-semibold ${roleBadgeClasses[log.actorRole] ??
                    "bg-gray-100 text-gray-700"
                    }`}
                >
                  {log.actorRole}
                </span>
              </td>


              <td className="px-3 py-2 border">
                {log.actorId ?? "—"}
              </td>

              <td className="px-3 py-2 border font-mono text-xs bg-gray-50">
                {log.action}
              </td>


              <td className="px-3 py-2 border">
                <span
                  className={`px-2 py-0.5 rounded text-xs font-semibold ${entityBadgeClasses[log.entityType] ??
                    "bg-gray-100 text-gray-700"
                    }`}
                >
                  {log.entityType}
                </span>
              </td>

              <td className="px-3 py-2 border">
                {log.entityType === "ORDER" ? (
                  <Link
                    to={`/admin/orders`}
                    className="text-green-700 hover:underline font-medium"
                  >
                    #{log.entityId}
                  </Link>
                ) : log.entityType === "PRODUCT" ? (
                  <Link
                    to={`/admin/products/${log.entityId}/edit`}
                    className="text-green-700 hover:underline font-medium"
                  >
                    #{log.entityId}
                  </Link>
                ) : (
                  log.entityId
                )}
              </td>

            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminAuditLogsTable;
