import type { OrderStatus } from "../../admin/constants/orderStatus";
import axiosInstance from "../../api/axiosInstance";

/* ========= Types (local to service) ========= */

export interface OrderItem {
  productId: number;
  productName: string;
  priceAtPurchase: number;
  quantity: number;
  totalPrice: number;
}

export interface OrderResponse {
  orderId: number;
  totalAmount: number;
  orderStatus: OrderStatus;
  paymentStatus: "PENDING" | "PAID";
  createdAt: string;
  items: OrderItem[];
}

/* ========= API calls ========= */

/**
 * Place order from cart
 * POST /api/orders
 */
export const placeOrder = async (): Promise<OrderResponse> => {
  const response = await axiosInstance.post<OrderResponse>("/orders");
  return response.data;
};

/**
 * Get logged-in user's orders
 * GET /api/orders
 */
export const getMyOrders = async (): Promise<OrderResponse[]> => {
  const response = await axiosInstance.get<OrderResponse[]>("/orders");
  return response.data;
};

/**
 * Get single order by ID
 * GET /api/orders/{orderId}
 */
export const getOrderById = async (
  orderId: number
): Promise<OrderResponse> => {
  const response = await axiosInstance.get<OrderResponse>(
    `/orders/${orderId}`
  );
  return response.data;
};

/* ========= ADMIN APIs (optional UI later) ========= */

/**
 * Admin: get all orders
 * GET /api/admin/orders
 */
export const getAllOrdersAdmin = async (): Promise<OrderResponse[]> => {
  const response = await axiosInstance.get<OrderResponse[]>(
    "/admin/orders"
  );
  return response.data;
};

/**
 * Admin: update order status
 * PUT /api/admin/orders/{id}/status
 */
export const updateOrderStatusAdmin = async (
  orderId: number,
  orderStatus: string
): Promise<OrderResponse> => {
  const response = await axiosInstance.put<OrderResponse>(
    `/admin/orders/${orderId}/status`,
    { orderStatus }
  );
  return response.data;
};


export const cancelOrder = async (orderId: number) => {
  const res = await axiosInstance.patch(
    `/orders/${orderId}/cancel`
  );
  return res.data;
};