import { useEffect, useState } from "react";
import { getAllOrders } from "../services/adminOrdersApi";
import type { OrderResponse } from "../../orders/services/ordersData";
import AdminOrdersTable from "../components/AdminOrdersTable";

const AdminOrdersPage = () => {
  const [orders, setOrders] = useState<OrderResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const data = await getAllOrders();
        setOrders(data);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  if (loading) {
    return <p className="text-sm text-gray-500">Loading orders...</p>;
  }

  return (
    <div>
      <h1 className="text-xl font-bold text-gray-900 mb-6">
        Orders Management
      </h1>

      <AdminOrdersTable orders={orders} />
    </div>
  );
};

export default AdminOrdersPage;
