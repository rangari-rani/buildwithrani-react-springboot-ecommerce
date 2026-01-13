import React, { useEffect, useState } from "react";
import FeaturedCard from "./FeaturedCard";
import { fetchFeaturedProducts } from "../../../products/services/productsApi";
import type { Product } from "../../../products/services/productsData";

const FeaturedSlider: React.FC = () => {
  const [featuredProducts, setFeaturedProducts] = useState<Product[]>([]);

  useEffect(() => {
    const loadFeaturedProducts = async () => {
      try {
        const products = await fetchFeaturedProducts();
        setFeaturedProducts(products);
      } catch (error) {
        console.error("Failed to load featured products", error);
      }
    };

    loadFeaturedProducts();
  }, []);

  if (featuredProducts.length === 0) {
    return null;
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
