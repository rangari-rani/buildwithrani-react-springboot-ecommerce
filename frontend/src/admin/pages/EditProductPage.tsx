import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";

import ProductForm, {
  type ProductFormData,
} from "../components/ProductForm";

import { fetchProductById } from "../../products/services/productsApi";
import { updateProduct } from "../services/adminProductApi";
import type { Product } from "../../products/services/productsData";

const EditProductPage: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();

  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!id) return;

    const loadProduct = async () => {
      try {
        const data = await fetchProductById(Number(id));
        setProduct(data);
      } catch (error) {
        console.error("Failed to load product", error);
        setProduct(null);
      } finally {
        setLoading(false);
      }
    };

    loadProduct();
  }, [id]);

  const handleSubmit = async (data: ProductFormData) => {
    if (!product) return;

    try {
      await updateProduct(product.id, {
        name: data.name,
        description: data.description,
        price: data.price,
        discountPercentage: data.discountPercentage,
        isFeatured: data.featured,
      });

      navigate("/admin/products");
    } catch (error) {
      console.error("Failed to update product", error);
      alert("Failed to update product");
    }
  };

  const handleCancel = () => {
    navigate("/admin/products");
  };

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-16 text-center text-gray-500">
        Loading product...
      </div>
    );
  }

  if (!product) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-16 text-center">
        <h2 className="text-xl font-semibold text-gray-900 mb-2">
          Product not found
        </h2>
        <p className="text-gray-600 mb-6">
          The product you are trying to edit does not exist.
        </p>
        <Link
          to="/admin/products"
          className="text-green-600 font-medium hover:underline"
        >
          ← Back to products
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-0">

      <Link
        to="/admin/products"
        className="md:hidden text-green-600 font-medium cursor-pointer"
      >
        ← Back to products
      </Link>

      {/* Page Header */}
      <div className="mb-8 mt-4">
        <h1 className="text-2xl text-center font-bold text-gray-900">
          Edit Product
        </h1>
        <p className="text-sm text-center text-gray-500 mt-1">
          Admin – Update product details
        </p>
      </div>

      {/* Product Form */}
      <div className="flex justify-center">
        <ProductForm
          initialData={product}
          onSubmit={handleSubmit}
          onCancel={handleCancel}
        />
      </div>

    </div>
  );
};

export default EditProductPage;
