import React from "react";
import ProductCard from "./ProductCard";
import type { Product } from "../../services/productsData";

interface ProductsGridProps {
  products: Product[];
}

const ProductsGrid: React.FC<ProductsGridProps> = ({ products }) => {
  if (!products || products.length === 0) {
    return (
      <div className="text-center text-gray-500 py-18">
        No products found.
      </div>
    );
  }

  return (
    <div
      className="
        grid
        grid-cols-2
        sm:grid-cols-3
        lg:grid-cols-4
        gap-6
      "
    >
      {products.map((product) => (
        <ProductCard
          key={product.id}
          product={product}
        />
      ))}
    </div>
  );
};

export default ProductsGrid;
