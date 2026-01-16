import axiosInstance from "../../api/axiosInstance";

/**
 * Types that match backend CartResponse DTO
 */
export interface CartItem {
  productId: number;
  name: string;
  imageUrl: string;
  price: number;
  quantity: number;
}

export interface CartResponse {
  items: CartItem[];
}

/**
 * Get current user's cart
 * GET /api/cart
 */
export const getCart = async (): Promise<CartResponse> => {
  const response = await axiosInstance.get<CartResponse>("/cart");
  return response.data;
};

/**
 * Add product to cart
 * POST /api/cart/add
 */
export const addToCart = async (
  productId: number,
  quantity: number
): Promise<CartResponse> => {
  const response = await axiosInstance.post<CartResponse>("/cart/add", {
    productId,
    quantity,
  });
  return response.data;
};

/**
 * Update cart item quantity
 * PUT /api/cart/update
 */
export const updateCartItem = async (
  productId: number,
  quantity: number
): Promise<CartResponse> => {
  const response = await axiosInstance.put<CartResponse>("/cart/update", {
    productId,
    quantity,
  });
  return response.data;
};

/**
 * Remove item from cart
 * DELETE /api/cart/remove/{productId}
 */
export const removeCartItem = async (productId: number): Promise<void> => {
  await axiosInstance.delete(`/cart/remove/${productId}`);
};

/**
 * Clear entire cart
 * DELETE /api/cart/clear
 */
export const clearCart = async (): Promise<void> => {
  await axiosInstance.delete("/cart/clear");
};
