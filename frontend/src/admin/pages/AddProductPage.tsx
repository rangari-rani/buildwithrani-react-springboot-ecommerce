import React from "react";
import { Link, useNavigate } from "react-router-dom";
import ProductForm, {
  type ProductFormData,
} from "../components/ProductForm";

const AddProductPage: React.FC = () => {
  const navigate = useNavigate();

  const handleSubmit = (data: ProductFormData) => {
    // UI-first: no API yet
    console.log("Create product", data);

    // After successful submit → go back to admin list
    navigate("/admin/products");
  };

  const handleCancel = () => {
    navigate("/admin/products");
  };

  return (
    <div className="max-w-7xl mx-auto px-4 py-0">
      <Link
        to="/admin/products"
        className="md:hidden text-green-600 font-medium  cursor-pointer "
      >
        ← Back to products
      </Link>
      {/* Page Header */}
      <div className="mb-8 mt-4">
        <h1 className="text-2xl text-center font-bold text-gray-900">
          Add Product
        </h1>
        <p className="text-sm text-center text-gray-500 mt-1">
          Admin – Create a new product
        </p>
      </div>

      {/* Centered Form */}
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
