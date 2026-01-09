import React from "react";
import { useParams, Link } from "react-router-dom";

import { products } from "../services/productsData";
import ProductGallery from "../components/detail/ProductGallery";
import ProductInfo from "../components/detail/ProductInfo";
import AddToCartSection from "../components/detail/AddToCartSection";

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();

  const product = products.find(
    (p) => p.id === Number(id)
  );

  if (!product) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-16 text-center">
        <h2 className="text-xl font-semibold text-gray-900 mb-2">
          Product not found
        </h2>
        <p className="text-gray-600 mb-6">
          The product you are looking for does not exist.
        </p>
        <Link
          to="/products"
          className="text-green-600 font-medium hover:underline"
        >
          ← Back to products
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-10">

      {/* Back link */}
      <div className="mb-6 py-8">
        <Link
          to="/products"
          className="text-sm text-gray-600 hover:text-gray-900"
        >
          ← Back to products
        </Link>
      </div>

      {/* Main layout */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-10">

        {/* Left: Gallery */}
        <ProductGallery product={product} />

        {/* Right: Info + Cart */}
        <div>
          <ProductInfo product={product} />
          <AddToCartSection product={product} />
        </div>

      </div>
    </div>
  );
};

export default ProductDetail;
