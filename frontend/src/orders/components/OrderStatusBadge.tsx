import React from "react";

interface OrderStatusBadgeProps {
  status: string; // ✅ accept backend string
}

const statusStyles: Record<
  string,
  { label: string; className: string }
> = {
  PLACED: {
    label: "Placed",
    className: "bg-yellow-100 text-yellow-700",
  },
  CONFIRMED: {
    label: "Confirmed",
    className: "bg-blue-100 text-blue-700",
  },
  SHIPPED: {
    label: "Shipped",
    className: "bg-purple-100 text-purple-700",
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
  const config =
    statusStyles[status] ?? {
      label: status,
      className: "bg-gray-100 text-gray-700",
    };

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
