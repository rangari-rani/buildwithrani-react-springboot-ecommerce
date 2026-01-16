import React from "react";
import { Link } from "react-router-dom";
import type { OrderResponse } from "../services/ordersData";
import OrderStatusBadge from "./OrderStatusBadge";
import OrderItemsPreview from "./OrderItemsPreview";

interface OrderCardProps {
  order: OrderResponse;
}

const OrderCard: React.FC<OrderCardProps> = ({ order }) => {
  const {
    orderId,
    items,
    totalAmount,
    orderStatus,
    createdAt,
  } = order;

  const formattedDate = new Date(createdAt).toLocaleDateString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });

  return (
    <Link
      to={`/orders/${orderId}`}
      className="
        block
        bg-white
        border border-gray-200
        rounded-lg
        p-4
        hover:shadow-sm
        transition
      "
    >
      {/* Top row */}
      <div className="flex items-center justify-between gap-4 mb-3">
        <div>
          <p className="text-sm font-semibold text-gray-900">
            Order #{orderId}
          </p>
          <p className="text-xs text-gray-500 mt-0.5">
            Placed on {formattedDate}
          </p>
        </div>

        <OrderStatusBadge status={orderStatus} />
      </div>

      {/* Items preview */}
      <OrderItemsPreview items={items} />

      {/* Footer */}
      <div className="mt-4 flex items-center justify-between">
        <p className="text-sm text-gray-600">
          {items.length} item{items.length > 1 ? "s" : ""}
        </p>

        <p className="text-sm font-semibold text-gray-900">
          ₹{totalAmount}
        </p>
      </div>
    </Link>
  );
};

export default OrderCard;
