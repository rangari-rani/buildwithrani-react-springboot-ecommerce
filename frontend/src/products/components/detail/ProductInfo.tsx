import React from "react";
import type { Product } from "../../services/productsData";
import RatingStars from "../../../shared/RatingStars";

interface ProductInfoProps {
  product: Product;
}

const ProductInfo: React.FC<ProductInfoProps> = ({ product }) => {
  const {
    name,
    description,
    price,
    discountPercentage,
    averageRating,
    status,
  } = product;

  const hasDiscount =
    discountPercentage !== undefined && discountPercentage > 0;

  const discountedPrice = hasDiscount
    ? Math.round(price - (price * discountPercentage) / 100)
    : price;

  return (
    <div className="w-full">

      {/* Product Name */}
      <h1 className="text-2xl font-bold text-gray-900 mb-3">
        {name}
      </h1>

      {/* Rating */}
      {typeof averageRating === "number" && (
        <div className="flex items-center gap-2 mb-3">
          <RatingStars rating={averageRating} />
          <span className="text-sm text-gray-500">
            {averageRating.toFixed(1)} rating
          </span>
        </div>
      )}

      {/* Price */}
      <div className="flex items-center gap-3 mb-4">
        <p className="text-2xl font-semibold text-green-700">
          ₹{discountedPrice}
        </p>

        {hasDiscount && (
          <>
            <p className="text-lg text-gray-400 line-through">
              ₹{price}
            </p>
            <span className="text-sm font-medium text-red-600">
              {discountPercentage}% OFF
            </span>
          </>
        )}
      </div>

      {/* Description */}
      <p className="text-sm text-gray-600 leading-relaxed mb-6">
        {description}
      </p>

      {/* Meta info */}
      <div className="space-y-2 text-sm text-gray-500">
        <p>
          <span className="font-medium text-gray-700">
            Availability:
          </span>{" "}
          {status === "ACTIVE" ? "In Stock" : "Unavailable"}
        </p>
      </div>

    </div>
  );
};

export default ProductInfo;
