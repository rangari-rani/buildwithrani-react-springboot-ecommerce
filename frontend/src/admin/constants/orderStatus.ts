export const ORDER_STATUSES = [
  "PLACED",
  "CONFIRMED",
  "SHIPPED",
  "DELIVERED",
  "CANCELLED",
] as const;

export type OrderStatus = typeof ORDER_STATUSES[number];
