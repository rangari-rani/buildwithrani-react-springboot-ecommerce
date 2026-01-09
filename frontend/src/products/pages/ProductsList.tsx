import React from "react";
import ProductsGrid from "../components/list/ProductsGrid";
import { products } from "../services/productsData";

const ProductsList: React.FC = () => {
  return (
    <div className="max-w-7xl mx-auto px-4 py-20">

      {/* Page Heading */}
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">
          All Products
        </h1>
        <p className="text-sm text-gray-600 mt-1">
          Browse our complete collection
        </p>
      </div>

      {/* Products Grid */}
      <ProductsGrid products={products} />

    </div>
  );
};

export default ProductsList;
