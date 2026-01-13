import axiosInstance from "../../api/axiosInstance";

export interface CreateProductPayload {
  name: string;
  description: string;
  price: number;
  discountPercentage?: number;
  featured: boolean;
}

// CREATE PRODUCT
export const createProduct = (formData: FormData) =>
  axiosInstance.post("/admin/products", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

// UPDATE PRODUCT
export const updateProduct = (id: number, formData: FormData) =>
  axiosInstance.put(`/admin/products/${id}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });


// FETCH ALL PRODUCTS

export const fetchAdminProducts = async () => {
  const response = await axiosInstance.get("/admin/products");
  return response.data;
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
  featured: boolean
) => {
  await axiosInstance.patch(
    `/admin/products/${productId}/featured`,
    null,
    {
      params: { featured },
    }
  );
};
