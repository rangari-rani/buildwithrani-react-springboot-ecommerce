import { useState } from "react";
import type { OrderResponse } from "../../orders/services/ordersData";
import OrderStatusBadge from "../../orders/components/OrderStatusBadge";
import { updateOrderStatus } from "../services/adminOrdersApi";
import { ORDER_STATUSES, type OrderStatus } from "../constants/orderStatus";

const AdminOrderRow = ({ order }: { order: OrderResponse }) => {
  const [status, setStatus] = useState(order.orderStatus);
  const [loading, setLoading] = useState(false);

  const handleStatusChange = async (
    e: React.ChangeEvent<HTMLSelectElement>
  ) => {
    const newStatus = e.target.value as OrderStatus;

    if (newStatus === status) return;

    try {
      setLoading(true);

      const updatedOrder = await updateOrderStatus(
        order.orderId,
        newStatus
      );

      // ✅ backend is source of truth
      setStatus(updatedOrder.orderStatus);
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
          disabled={loading}
          className="
            text-xs
            border border-gray-300
            rounded-md
            px-2 py-1
            bg-white
            focus:outline-none
            focus:ring-1
            focus:ring-green-500
          "
        >
          {ORDER_STATUSES.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </select>
      </td>

      <td className="px-4 py-3 text-right font-semibold">
        ₹{order.totalAmount}
      </td>
    </tr>
  );
};

export default AdminOrderRow;
