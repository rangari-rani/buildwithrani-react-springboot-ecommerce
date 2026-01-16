import type { OrderResponse } from "../../orders/services/ordersData";
import AdminOrderRow from "./AdminOrderRow";

interface Props {
  orders: OrderResponse[];
}

const AdminOrdersTable = ({ orders }: Props) => {
  return (
    <div className="bg-white border rounded-lg overflow-hidden">
      <table className="w-full text-sm">
        <thead className="bg-gray-50 text-gray-600">
          <tr>
            <th className="px-4 py-3 text-left">Order ID</th>
            <th className="px-4 py-3 text-left">Date</th>
            <th className="px-4 py-3 text-left">Status</th>
            <th className="px-4 py-3 text-right">Total</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <AdminOrderRow key={order.orderId} order={order} />
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminOrdersTable;
