import axiosInstance from "../../api/axiosInstance";
import type { OrderResponse } from "../../orders/services/ordersData";
import type { OrderStatus } from "../constants/orderStatus";

/**
 * GET /api/admin/orders
 */
export const getAllOrders = async (): Promise<OrderResponse[]> => {
  const res = await axiosInstance.get<OrderResponse[]>("/admin/orders");
  return res.data;
};

/**
 * PUT /api/admin/orders/{id}/status
 */
export const updateOrderStatus = async (
  orderId: number,
  orderStatus: OrderStatus
): Promise<void> => {
  await axiosInstance.put(
    `/admin/orders/${orderId}/status`,
    { orderStatus }
  );
};

