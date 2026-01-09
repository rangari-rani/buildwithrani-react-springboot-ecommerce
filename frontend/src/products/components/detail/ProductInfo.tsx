import React from "react";
import type { Product } from "../../services/productsData";

interface ProductInfoProps {
  product: Product;
}

const ProductInfo: React.FC<ProductInfoProps> = ({ product }) => {
  const { name, price } = product;

  return (
    <div className="w-full">

      {/* Product Name */}
      <h1 className="text-2xl font-bold text-gray-900 mb-3">
        {name}
      </h1>

      {/* Price */}
      <p className="text-2xl font-semibold text-green-700 mb-4">
        ₹{price}
      </p>

      {/* Description */}
      <p className="text-sm text-gray-600 leading-relaxed mb-6">
        This is a premium quality product made with natural ingredients.
        Designed to give you the best results while being gentle on your skin.
        Perfect for daily use and suitable for all skin types.
      </p>

      {/* Meta info (optional, UI-only for now) */}
      <div className="space-y-2 text-sm text-gray-500">
        <p>
          <span className="font-medium text-gray-700">Category:</span>{" "}
          Skincare
        </p>
        <p>
          <span className="font-medium text-gray-700">Availability:</span>{" "}
          In Stock
        </p>
      </div>

    </div>
  );
};

export default ProductInfo;
