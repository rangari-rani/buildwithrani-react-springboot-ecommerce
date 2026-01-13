import React from "react";
import { Link, useNavigate } from "react-router-dom";
import ProductForm, {
  type ProductFormData,
} from "../components/ProductForm";
import { createProduct } from "../services/adminProductApi";

const AddProductPage: React.FC = () => {
  const navigate = useNavigate();

const handleSubmit = async (data: ProductFormData) => {
  try {
    const formData = new FormData();

    formData.append("name", data.name);
    formData.append("description", data.description);
    formData.append("price", String(data.price));

    if (data.discountPercentage !== undefined) {
      formData.append(
        "discountPercentage",
        String(data.discountPercentage)
      );
    }

    formData.append("featured", String(data.featured));

    if (data.image) {
      formData.append("image", data.image);
    }

    await createProduct(formData);

    navigate("/admin/products");
  } catch (error) {
    console.error("Failed to create product", error);
    alert("Failed to create product");
  }
};

  const handleCancel = () => {
    navigate("/admin/products");
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-0">
      <Link
        to="/admin/products"
        className="md:hidden text-green-600 font-medium cursor-pointer"
      >
        ← Back to products
      </Link>

      <div className="mb-8 mt-4">
        <h1 className="text-2xl text-center font-bold text-gray-900">
          Add Product
        </h1>
        <p className="text-sm text-center text-gray-500 mt-1">
          Admin – Create a new product
        </p>
      </div>

      <div className="flex justify-center">
        <ProductForm
          onSubmit={handleSubmit}
          onCancel={handleCancel}
        />
      </div>
    </div>
  );
};

export default AddProductPage;
