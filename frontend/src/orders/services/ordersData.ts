import type { Product } from "../../products/services/productsData";

/**
 * Order item = product + quantity
 */
export interface OrderItem {
  product: Product;
  quantity: number;
}

/**
 * Order model
 */
export interface Order {
  id: number;
  items: OrderItem[];
  totalAmount: number;
  status: "PLACED" | "DELIVERED" | "CANCELLED";
  createdAt: string; // ISO date string
}

/**
 * Mock orders (UI-first)
 * Will be replaced by Spring Boot API later
 */
export const orders: Order[] = [
  {
    id: 1001,
    items: [
      {
        product: {
          id: 1,
          name: "Herbal Face Wash with Neem",
          price: 299,
          image:
            "https://images.unsplash.com/photo-1556228724-4d8b57b6a1b4",
          isFeatured: true,
          createdAt: "2026-01-02T10:30:00",
        },
        quantity: 2,
      },
      {
        product: {
          id: 2,
          name: "Organic Aloe Vera Gel",
          price: 249,
          image:
            "https://images.unsplash.com/photo-1611930022073-b7a4ba5fcccd",
          isFeatured: false,
          createdAt: "2026-01-06T09:15:00",
        },
        quantity: 1,
      },
    ],
    totalAmount: 847,
    status: "DELIVERED",
    createdAt: "2026-02-01T14:20:00",
  },
  {
    id: 1002,
    items: [
      {
        product: {
          id: 3,
          name: "Ayurvedic Hair Oil",
          price: 399,
          image:
            "https://images.unsplash.com/photo-1600185365483-26d7a4cc7519",
          isFeatured: false,
          createdAt: "2026-01-08T11:00:00",
        },
        quantity: 1,
      },
    ],
    totalAmount: 399,
    status: "PLACED",
    createdAt: "2026-02-10T09:45:00",
  },
];
