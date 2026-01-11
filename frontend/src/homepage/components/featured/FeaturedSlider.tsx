import React, { useEffect, useState } from "react";
import FeaturedCard from "./FeaturedCard";
import { fetchProducts } from "../../../products/services/productsApi";
import type { Product } from "../../../products/services/productsData";

const FeaturedSlider: React.FC = () => {
  const [featuredProducts, setFeaturedProducts] = useState<Product[]>([]);

  useEffect(() => {
    const loadFeaturedProducts = async () => {
      try {
        const products = await fetchProducts();
        const filtered = products.filter(
          (product) => product.isFeatured
        );
        setFeaturedProducts(filtered);
      } catch (error) {
        console.error("Failed to load featured products", error);
      }
    };

    loadFeaturedProducts();
  }, []);

  if (featuredProducts.length === 0) {
    return null; // hide section if no featured products
  }

  return (
    <div className="flex gap-4 overflow-x-auto scrollbar-hide py-2 sm:px-12 px-4">
      {featuredProducts.map((product) => (
        <FeaturedCard key={product.id} product={product} />
      ))}
    </div>
  );
};

export default FeaturedSlider;
