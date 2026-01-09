import React from "react";
import FeaturedCard from "./FeaturedCard";
import { products } from "../../../products/services/productsData";

const FeaturedSlider: React.FC = () => {
  const featuredProducts = products.filter(
    (product) => product.isFeatured
  );

  return (
    <div className="flex gap-4 overflow-x-auto scrollbar-hide py-2 sm:px-12 px-4">
      {featuredProducts.map((product) => (
        <FeaturedCard key={product.id} product={product} />
      ))}
    </div>
  );
};

export default FeaturedSlider;
