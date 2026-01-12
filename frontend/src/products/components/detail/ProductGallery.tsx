import React from "react";
import type { Product } from "../../services/productsData";

interface ProductGalleryProps {
  product: Product;
}

const ProductGallery: React.FC<ProductGalleryProps> = ({ product }) => {
  const { imageUrl, name } = product;

  return (
    <div className="w-full">
      <div
        className="
          w-full
          aspect-square
          md:aspect-auto
          md:h-105
          bg-gray-50
          rounded-lg
          overflow-hidden
          border border-gray-100
          flex items-center justify-center
        "
      >
        {imageUrl ? (
          <img
            src={imageUrl}
            alt={name}
            className="w-full h-full object-cover"
          />
        ) : (
          <span className="text-sm text-gray-400">
            No image available
          </span>
        )}
      </div>
    </div>
  );
};

export default ProductGallery;
