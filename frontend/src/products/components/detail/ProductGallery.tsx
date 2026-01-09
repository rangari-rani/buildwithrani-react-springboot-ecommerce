import React from "react";
import type { Product } from "../../services/productsData";


interface ProductGalleryProps {
  product: Product;
}

const ProductGallery: React.FC<ProductGalleryProps> = ({ product }) => {
  const { image, name } = product;

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
    "
  >
    <img
      src={image}
      alt={name}
      className="w-full h-full object-cover"
    />
  </div>
</div>

  );
};

export default ProductGallery;
