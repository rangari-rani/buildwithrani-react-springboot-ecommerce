import axiosInstance from "../../api/axiosInstance";

export interface CreateProductPayload {
  name: string;
  description: string;
  price: number;
  discountPercentage?: number;
  isFeatured: boolean;
}

// CREATE PRODUCT
export const createProduct = async (data: CreateProductPayload) => {
  const response = await axiosInstance.post(
    "/admin/products",
    data
  );
  return response.data;
};

// UPDATE PRODUCT

export const updateProduct = async (
  productId: number,
  data: {
    name: string;
    description: string;
    price: number;
    discountPercentage?: number;
    isFeatured: boolean;
  }
) => {
  await axiosInstance.put(`/admin/products/${productId}`, data);
};

// UPDATE STATUS
export const updateProductStatus = async (
  productId: number,
  status: "ACTIVE" | "INACTIVE"
) => {
  await axiosInstance.patch(
    `/admin/products/${productId}/status`,
    null,
    {
      params: { status },
    }
  );
};

// TOGGLE FEATURED
export const updateFeaturedStatus = async (
  productId: number,
  isFeatured: boolean
) => {
  await axiosInstance.patch(
    `/admin/products/${productId}/featured`,
    null,
    {
      params: { isFeatured },
    }
  );
};
