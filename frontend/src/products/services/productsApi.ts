import axiosInstance from "../../api/axiosInstance";
import type { Product } from "./productsData";

/**
 * Fetch all ACTIVE products
 * Backend: GET /api/products
 */
export const fetchProducts = async (): Promise<Product[]> => {
  const response = await axiosInstance.get("/products");
  return response.data;
};

/**
 * Fetch single product by ID
 * Backend: GET /api/products/{id}
 */
export const fetchProductById = async (
  productId: number
): Promise<Product> => {
  const response = await axiosInstance.get(`/products/${productId}`);
  return response.data;
};
