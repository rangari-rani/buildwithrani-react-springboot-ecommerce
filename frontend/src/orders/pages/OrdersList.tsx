import React from "react";
import OrderCard from "../components/OrderCard";
import { orders } from "../services/ordersData";

const OrdersList: React.FC = () => {
  const isEmpty = orders.length === 0;

  return (
    <div className="max-w-7xl mx-auto px-4 py-18">

      {/* Page header */}
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-900">
          My Orders
        </h1>
        <p className="text-sm text-gray-500 mt-1">
          View your past orders and their status
        </p>
      </div>

      {/* Empty state */}
      {isEmpty ? (
        <div className="py-16 text-center">
          <p className="text-gray-600">
            You haven’t placed any orders yet.
          </p>
        </div>
      ) : (
        <div className="space-y-4">
          {orders.map((order) => (
            <OrderCard key={order.id} order={order} />
          ))}
        </div>
      )}

    </div>
  );
};

export default OrdersList;
