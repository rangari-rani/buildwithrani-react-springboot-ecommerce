import React from "react";
import { FiShoppingCart } from "react-icons/fi";
import type { Product } from "../../../products/services/productsData";
import { Link } from "react-router-dom";
import { useCart } from "../../../cart/context/CartContext";

interface FeaturedCardProps {
  product: Product;
}

const FeaturedCard: React.FC<FeaturedCardProps> = ({ product }) => {
  const { id, imageUrl, name, price } = product;
const { addItem } = useCart();

  return (
    <div className="min-w-65 max-w-65 bg-white rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition">

      {/* Image */}
      <Link to={`/products/${id}`}>
        <div className="relative w-full h-56 overflow-hidden rounded-t-xl bg-gray-50">
          <img
            src={imageUrl}
            alt={name}
            className="w-full h-full object-cover transition-transform duration-300 hover:scale-105"
          />

          {/* Badge */}
          <span className="absolute top-3 left-3 bg-green-600 text-white text-xs font-medium px-2 py-1 rounded-md">
            ⭐ Featured
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
          <p className="text-green-700 font-bold">
            ₹{price}
          </p>

          <button
            className="
              flex items-center justify-center
              w-9 h-9
              rounded-full
              bg-green-600
              text-white
              hover:bg-green-700
              transition
            "
           onClick={() => addItem(product.id, 1)}
          >
            <FiShoppingCart size={18} />
          </button>
        </div>
      </div>
    </div>
  );
};


export default FeaturedCard;
