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
    await createProduct({
      name: data.name,
      description: data.description,
      price: data.price,
      discountPercentage: data.discountPercentage,
      isFeatured: data.featured,
    });

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
