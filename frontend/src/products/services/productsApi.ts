import axiosInstance from "../../api/axiosInstance";
import type { Product } from "./productsData";

/**
 * Fetch all ACTIVE products
 */
export const fetchProducts = async (): Promise<Product[]> => {
  const response = await axiosInstance.get("/products");
  return response.data;
};

/**
 * Fetch single product
 */
export const fetchProductById = async (
  productId: number
): Promise<Product> => {
  const response = await axiosInstance.get(`/products/${productId}`);
  return response.data;
};

/**
 * Featured = client side filter
 */
export const fetchFeaturedProducts = async (): Promise<Product[]> => {
  const products = await fetchProducts();
  return products.filter((p) => p.featured);
};

/**
 * New arrivals = latest products
 */
export const fetchNewArrivals = async (): Promise<Product[]> => {
  const products = await fetchProducts();
  return products.slice(0, 4);
};