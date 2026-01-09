import React from "react";
import FeaturedSlider from "./FeaturedSlider";

const FeaturedSection: React.FC = () => {
  return (
    <section className="mt-12">
      
      {/* Header */}
      <div className="flex items-center justify-between mb-4 sm:px-12 px-4">
        <h2 className="text-xl font-bold text-gray-900">
          Featured Products
        </h2>
      </div>
      {/* Slider */}
      <FeaturedSlider />

    </section>
  );
};

export default FeaturedSection;
