// src/admin/types/order.ts

export type OrderStatus =
  | "CREATED"
  | "PAID"
  | "PACKED"
  | "SHIPPED"
  | "DELIVERED"
  | "CANCELLED";

export type PaymentStatus = "PENDING" | "PAID";

export interface OrderItem {
  productId: number;
  productName: string;
  priceAtPurchase: number;
  quantity: number;
  totalPrice: number;
}

export interface Order {
  orderId: number;
  totalAmount: number;
  orderStatus: OrderStatus;
  paymentStatus: PaymentStatus;
  createdAt: string;
  items: OrderItem[];
}
