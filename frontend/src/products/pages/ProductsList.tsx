import React, { useEffect, useState } from "react";
import ProductsGrid from "../components/list/ProductsGrid";
import { fetchProducts } from "../services/productsApi";
import type { Product } from "../services/productsData";

const ProductsList: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadProducts = async () => {
      try {
        const data = await fetchProducts();
        setProducts(data);
      } catch (error) {
        console.error("Failed to fetch products", error);
      } finally {
        setLoading(false);
      }
    };

    loadProducts();
  }, []);

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-20 text-center text-gray-500">
        Loading products...
      </div>
    );
  }

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
