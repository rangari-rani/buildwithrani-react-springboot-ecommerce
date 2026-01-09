import React from "react";
import { FiShoppingCart } from "react-icons/fi";
import type { Product } from "../../../products/services/productsData";
import { Link } from "react-router-dom";
import { useCart } from "../../../cart/context/CartContext";

interface NewArrivalCardProps {
  product: Product;
}
const NewArrivalCard: React.FC<NewArrivalCardProps> = ({ product }) => {
  const { id, image, name, price } = product;
const { addToCart } = useCart();
  return (
    <div className="min-w-65 max-w-65 bg-white rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition">

      {/* Image */}
      <Link to={`/products/${id}`}>
        <div className="relative w-full h-56 overflow-hidden rounded-t-xl bg-gray-50">
          <img
            src={image}
            alt={name}
            className="w-full h-full object-cover transition-transform duration-300 hover:scale-105"
          />

          {/* Badge */}
          <span className="absolute top-2 left-2 bg-blue-50 text-blue-700 text-xs font-medium px-2 py-0.5 rounded">
            🆕 New
          </span>
        </div>
      </Link>

      {/* Content */}
      <div className="p-4">
        <Link to={`/products/${id}`}>
          <h3 className="text-sm font-semibold text-gray-900 line-clamp-2 mb-3 hover:underline">
            {name}
          </h3>
        </Link>

        {/* Price + Cart */}
        <div className="flex items-center justify-between">
          <p className="text-sm font-semibold text-gray-800">
            ₹{price}
          </p>

          <button
            className="
              w-8 h-8
              flex items-center justify-center
              rounded-full
              border border-gray-300
              text-gray-600
              hover:bg-gray-100
              transition
            "
            onClick={() => addToCart(product, 1)}
          >
            <FiShoppingCart size={14} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default NewArrivalCard;
