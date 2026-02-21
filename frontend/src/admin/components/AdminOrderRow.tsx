import { useState } from "react";
import type { OrderResponse } from "../../orders/services/ordersData";
import OrderStatusBadge from "../../orders/components/OrderStatusBadge";
import { updateOrderStatus } from "../services/adminOrdersApi";
import {
  ORDER_STATUS_TRANSITIONS,
  type OrderStatus,
} from "../constants/orderStatus";
import { Link } from "react-router-dom";


const AdminOrderRow = ({ order }: { order: OrderResponse }) => {
const [status, setStatus] = useState<OrderStatus>(
  order.orderStatus as OrderStatus
);

  const [loading, setLoading] = useState(false);

const handleStatusChange = async (
  e: React.ChangeEvent<HTMLSelectElement>
) => {
  const newStatus = e.target.value as OrderStatus;

  if (newStatus === status) return;

  try {
    setLoading(true);

    await updateOrderStatus(order.orderId, newStatus);

    // Optimistic UI update
    setStatus(newStatus);

  } catch (error) {
    console.error("Failed to update order status", error);
    alert("Failed to update order status");
  } finally {
    setLoading(false);
  }
};

  return (
    <tr className="border-t">
      <td className="px-4 py-3 font-medium">
        #{order.orderId}
      </td>

      <td className="px-4 py-3">
        {new Date(order.createdAt).toLocaleDateString()}
      </td>

      <td className="px-4 py-3 flex items-center gap-2">
        <OrderStatusBadge status={status} />

      <select
  value={status}
  onChange={handleStatusChange}
  disabled={loading || ORDER_STATUS_TRANSITIONS[status].length === 0}
  className="
    text-xs
    border border-gray-300
    rounded-md
    px-2 py-1
    bg-white
    focus:outline-none
    focus:ring-1
    focus:ring-green-500
    disabled:opacity-50
    disabled:cursor-not-allowed
  "
>
  <option value={status}>{status}</option>

  {ORDER_STATUS_TRANSITIONS[status].map((nextStatus) => (
    <option key={nextStatus} value={nextStatus}>
      {nextStatus}
    </option>
  ))}
</select>

      </td>

      <td className="px-4 py-3 text-right font-semibold">
        ₹{order.totalAmount}
      </td>
      <td className="px-4 py-3 text-right">
  <Link
    to={`/admin/audit-logs?entityType=ORDER&entityId=${order.orderId}`}
    className="text-xs text-green-700 hover:underline font-medium"
  >
    Audit Logs
  </Link>
</td>

    </tr>
  );
};

export default AdminOrderRow;
