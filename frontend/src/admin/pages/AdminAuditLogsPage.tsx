// src/admin/pages/AdminAuditLogsPage.tsx

import React, { useEffect, useState } from "react";
import AdminAuditLogsTable from "../components/AdminAuditLogsTable";
import { adminAuditApi } from "../services/adminAuditApi";
import type { AuditLog, EntityType } from "../types/audit";
import { useSearchParams } from "react-router-dom";

const AdminAuditLogsPage: React.FC = () => {
  const [logs, setLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(false);

  const [entityType, setEntityType] = useState<EntityType | "">("");
  const [entityId, setEntityId] = useState<string>("");
  const [searchParams] = useSearchParams();

  const fetchLogs = async (
    type: EntityType | "",
    id: string
  ) => {
    setLoading(true);
    try {
      const params: any = {};

      if (type) params.entityType = type;
      if (id) params.entityId = Number(id);

      const data = await adminAuditApi.fetchAuditLogs(
        Object.keys(params).length ? params : undefined
      );

      setLogs(data);
    } catch (error) {
      console.error("Failed to fetch audit logs", error);
      setLogs([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const urlEntityType = searchParams.get("entityType") as EntityType | null;
    const urlEntityId = searchParams.get("entityId");

    if (urlEntityType) {
      setEntityType(urlEntityType);
    }

    if (urlEntityId) {
      setEntityId(urlEntityId);
    }

    // fetch logs using URL params if present
    fetchLogs(
      urlEntityType ?? "",
      urlEntityId ?? ""
    );

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);


  const handleFilterSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    fetchLogs(entityType, entityId);

  };

  const handleClearFilters = () => {
    setEntityType("");
    setEntityId("");
    fetchLogs("", "");

  };

  return (
    <div className="p-6">
      <h1 className="text-xl font-semibold mb-4">Audit Logs</h1>

      {/* Filters */}
      <form
        onSubmit={handleFilterSubmit}
        className="flex flex-wrap items-end gap-4 mb-6"
      >
        <div>
          <label className="block text-sm font-medium mb-1">
            Entity Type
          </label>
          <select
            value={entityType}
            onChange={(e) =>
              setEntityType(e.target.value as EntityType | "")
            }
            className="border rounded px-3 py-2 text-sm"
          >
            <option value="">All</option>
            <option value="ORDER">Order</option>
            <option value="PRODUCT">Product</option>
            <option value="CART">Cart</option>
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">
            Entity ID
          </label>
          <input
            type="number"
            value={entityId}
            onChange={(e) => setEntityId(e.target.value)}
            placeholder="e.g. 16"
            className="border rounded px-3 py-2 text-sm"
          />
        </div>

        <div className="flex gap-2">
          <button
            type="submit"
            className="bg-black text-white px-4 py-2 rounded text-sm"
          >
            Apply
          </button>

          <button
            type="button"
            onClick={handleClearFilters}
            className="border px-4 py-2 rounded text-sm"
          >
            Clear
          </button>
        </div>
      </form>

      {/* Table */}
      <AdminAuditLogsTable logs={logs} loading={loading} />
    </div>
  );
};

export default AdminAuditLogsPage;
