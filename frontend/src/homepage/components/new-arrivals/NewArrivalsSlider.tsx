import React, { useEffect, useState } from "react";
import NewArrivalCard from "./NewArrivalCard";
import { fetchProducts } from "../../../products/services/productsApi";
import type { Product } from "../../../products/services/productsData";
import { isNewArrival } from "../../../products/utils/productUtils";

const NewArrivalsSlider: React.FC = () => {
  const [newArrivals, setNewArrivals] = useState<Product[]>([]);

  useEffect(() => {
    const loadNewArrivals = async () => {
      try {
        const products = await fetchProducts();

        const filtered = products.filter(
  (product) =>
    product.createdAt && isNewArrival(product.createdAt)
);


        setNewArrivals(filtered);
      } catch (error) {
        console.error("Failed to load new arrivals", error);
      }
    };

    loadNewArrivals();
  }, []);

  if (newArrivals.length === 0) {
    return null; // no new arrivals → hide section
  }

  return (
    <div className="flex gap-4 overflow-x-auto scrollbar-hide py-2 sm:px-12 px-4">
      {newArrivals.map((product) => (
        <NewArrivalCard key={product.id} product={product} />
      ))}
    </div>
  );
};

export default NewArrivalsSlider;
