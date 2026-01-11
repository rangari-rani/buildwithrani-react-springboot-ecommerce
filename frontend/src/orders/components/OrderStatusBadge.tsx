import React from "react";

interface OrderStatusBadgeProps {
  status: "PLACED" | "DELIVERED" | "CANCELLED";
}

const statusStyles: Record<
  OrderStatusBadgeProps["status"],
  { label: string; className: string }
> = {
  PLACED: {
    label: "Placed",
    className: "bg-yellow-100 text-yellow-700",
  },
  DELIVERED: {
    label: "Delivered",
    className: "bg-green-100 text-green-700",
  },
  CANCELLED: {
    label: "Cancelled",
    className: "bg-red-100 text-red-700",
  },
};

const OrderStatusBadge: React.FC<OrderStatusBadgeProps> = ({ status }) => {
  const config = statusStyles[status];

  return (
    <span
      className={`
        inline-flex items-center
        px-2.5 py-1
        rounded-full
        text-xs font-medium
        ${config.className}
      `}
    >
      {config.label}
    </span>
  );
};

export default OrderStatusBadge;
