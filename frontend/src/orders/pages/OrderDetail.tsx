import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getOrderById, type OrderResponse } from "../services/ordersData";
import OrderStatusBadge from "../components/OrderStatusBadge";

const OrderDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const orderId = Number(id);

  const [order, setOrder] = useState<OrderResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchOrder = async () => {
      try {
        const data = await getOrderById(orderId);
        setOrder(data);
      } finally {
        setLoading(false);
      }
    };

    if (!isNaN(orderId)) fetchOrder();
    else setLoading(false);
  }, [orderId]);

  if (loading) {
    return (
      <div className="max-w-5xl mx-auto px-4 py-20 text-center text-gray-500">
        Loading order details...
      </div>
    );
  }

  if (!order) {
    return (
      <div className="max-w-5xl mx-auto px-4 py-20 text-center">
        <h2 className="text-lg font-semibold">Order not found</h2>
        <Link to="/orders" className="text-green-600 hover:underline mt-3 block">
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
    <div className="max-w-5xl mx-auto px-4 py-22 space-y-8">

      {/* HEADER */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Order #{order.orderId}
          </h1>
          <p className="text-sm text-gray-500 mt-1">
            Placed on {formattedDate}
          </p>
        </div>

        <OrderStatusBadge status={order.orderStatus} />
      </div>

      {/* ITEMS */}
      <div className="bg-white border border-gray-200 rounded-xl divide-y">
        {order.items.map((item) => (
          <div
            key={item.productId}
            className="flex items-center gap-4 p-5"
          >
            {/* Product avatar */}
            <div className="
              w-14 h-14
              rounded-lg
              bg-green-50
              text-green-700
              font-bold
              flex items-center justify-center
              text-lg
              shrink-0
            ">
              {item.productName.charAt(0)}
            </div>

            {/* Details */}
            <div className="flex-1">
              <p className="font-medium text-gray-900">
                {item.productName}
              </p>
              <p className="text-sm text-gray-500 mt-1">
                Quantity: {item.quantity}
              </p>
              <p className="text-xs text-gray-400 mt-0.5">
                ₹{item.priceAtPurchase} × {item.quantity}
              </p>
            </div>

            {/* Price */}
            <div className="text-right">
              <p className="font-semibold text-gray-900">
                ₹{item.totalPrice}
              </p>
            </div>
          </div>
        ))}
      </div>

      {/* SUMMARY */}
      <div className="bg-white border border-gray-200 rounded-xl p-5 space-y-3 max-w-md ml-auto">
        <div className="flex justify-between text-sm">
          <span className="text-gray-600">Items</span>
          <span>{order.items.length}</span>
        </div>

        <div className="flex justify-between text-base font-semibold">
          <span>Total Amount</span>
          <span>₹{order.totalAmount}</span>
        </div>
      </div>

      {/* BACK */}
      <Link
        to="/orders"
        className="inline-block text-sm text-gray-500 hover:text-gray-700 hover:underline"
      >
        ← Back to Orders
      </Link>
    </div>
  );
};

export default OrderDetail;
