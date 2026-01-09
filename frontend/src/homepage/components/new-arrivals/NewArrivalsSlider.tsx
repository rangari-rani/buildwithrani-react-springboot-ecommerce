import React from "react";
import NewArrivalCard from "./NewArrivalCard";
import { products } from "../../../products/services/productsData";
import { isNewArrival } from "../../../products/utils/productUtils";

const NewArrivalsSlider: React.FC = () => {
  const newArrivals = products.filter((product) =>
    isNewArrival(product.createdAt)
  );

  return (
    <div className="flex gap-4 overflow-x-auto scrollbar-hide py-2 sm:px-12 px-4">
      {newArrivals.map((product) => (
        <NewArrivalCard key={product.id} product={product} />
      ))}
    </div>
  );
};

export default NewArrivalsSlider;
