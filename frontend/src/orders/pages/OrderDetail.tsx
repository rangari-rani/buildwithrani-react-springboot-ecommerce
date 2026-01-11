import React from "react";
import { useParams, Link } from "react-router-dom";
import { orders } from "../services/ordersData";
import OrderStatusBadge from "../components/OrderStatusBadge";

const OrderDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const orderId = Number(id);

  const order = orders.find((o) => o.id === orderId);

  if (!order) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-16 text-center">
        <h2 className="text-lg font-semibold text-gray-900">
          Order not found
        </h2>
        <p className="text-sm text-gray-500 mt-2">
          The order you are looking for does not exist.
        </p>
        <Link
          to="/orders"
          className="mt-4 inline-block text-sm text-green-600 hover:underline"
        >
          Back to Orders
        </Link>
      </div>
    );
  }

  const formattedDate = new Date(order.createdAt).toLocaleDateString("en-IN", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });

  return (
    <div className="max-w-4xl mx-auto px-4 py-22">

      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Order #{order.id}
          </h1>
          <p className="text-sm text-gray-500 mt-1">
            Placed on {formattedDate}
          </p>
        </div>

        <OrderStatusBadge status={order.status} />
      </div>

      {/* Items */}
      <div className="bg-white border border-gray-200 rounded-lg divide-y">
        {order.items.map((item) => (
          <div
            key={item.product.id}
            className="flex items-center gap-4 p-4"
          >
            <div className="w-16 h-16 bg-gray-50 rounded-md overflow-hidden shrink-0">
              <img
                src={item.product.image}
                alt={item.product.name}
                className="w-full h-full object-cover"
              />
            </div>

            <div className="flex-1">
              <p className="text-sm font-medium text-gray-900">
                {item.product.name}
              </p>
              <p className="text-xs text-gray-500 mt-1">
                Quantity: {item.quantity}
              </p>
            </div>

            <p className="text-sm font-semibold text-gray-900">
              ₹{item.product.price * item.quantity}
            </p>
          </div>
        ))}
      </div>

      {/* Summary */}
      <div className="mt-6 bg-white border border-gray-200 rounded-lg p-4">
        <div className="flex items-center justify-between text-sm">
          <span className="text-gray-600">Total Amount</span>
          <span className="font-semibold text-gray-900">
            ₹{order.totalAmount}
          </span>
        </div>
      </div>

      {/* Back */}
      <div className="mt-6">
        <Link
          to="/orders"
          className="text-sm text-gray-500 hover:text-gray-700 underline-offset-2 hover:underline"
        >
          ← Back to Orders
        </Link>
      </div>

    </div>
  );
};

export default OrderDetail;
