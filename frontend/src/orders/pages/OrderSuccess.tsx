import React from "react";
import { Link, useLocation, Navigate } from "react-router-dom";
import { FiCheckCircle } from "react-icons/fi";

interface OrderSuccessState {
  orderId: number;
  orderStatus: string;
}

const OrderSuccess: React.FC = () => {
  const location = useLocation();
  const state = location.state as OrderSuccessState | null;

  // Safety: user refreshed or accessed directly
  if (!state) {
    return <Navigate to="/orders" replace />;
  }

  const { orderId, orderStatus } = state;

  return (
    <div className="max-w-3xl mx-auto px-4 py-22 text-center">

      {/* Success icon */}
      <div className="flex justify-center mb-6">
        <FiCheckCircle size={72} className="text-green-600" />
      </div>

      {/* Heading */}
      <h1 className="text-2xl font-bold text-gray-900">
        Order Placed Successfully 🎉
      </h1>

      {/* Order info */}
      <p className="text-gray-700 mt-2">
        Order ID: <span className="font-semibold">#{orderId}</span>
      </p>

      <p className="text-gray-600 mt-1">
        Current Status:{" "}
        <span className="font-medium text-green-600">
          {orderStatus}
        </span>
      </p>

      {/* Message */}
      <p className="text-gray-600 mt-4 max-w-md mx-auto">
        Thank you for your purchase. Your order has been placed successfully
        and is being processed.
      </p>

      {/* Actions */}
      <div className="mt-8 flex flex-col sm:flex-row items-center justify-center gap-4">

        <Link
          to="/products"
          className="
            inline-flex items-center justify-center
            px-6 py-3
            bg-green-600
            text-white
            font-medium
            rounded-lg
            hover:bg-green-700
            transition
          "
        >
          Continue Shopping
        </Link>

        <Link
          to="/orders"
          className="
            inline-flex items-center justify-center
            px-6 py-3
            border border-gray-300
            text-gray-700
            font-medium
            rounded-lg
            hover:bg-gray-50
            transition
          "
        >
          View Orders
        </Link>

        <Link
          to="/"
          className="
            text-sm
            text-gray-500
            hover:text-gray-700
            underline-offset-2
            hover:underline
          "
        >
          Back to Home
        </Link>
      </div>
    </div>
  );
};

export default OrderSuccess;
