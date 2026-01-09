import React from "react";
import { FiShoppingCart } from "react-icons/fi";
import { Link } from "react-router-dom";
import type { Product } from "../../services/productsData";
import { useCart } from "../../../cart/context/CartContext";

interface ProductCardProps {
  product: Product;
}

const ProductCard: React.FC<ProductCardProps> = ({ product }) => {
  const { id, image, name, price } = product;
const { addToCart } = useCart();

  return (
    <div className="bg-white rounded-lg border border-gray-100 hover:shadow-sm transition">

      {/* Image */}
      <Link to={`/products/${id}`}>
        <div className="w-full h-56 bg-gray-50 overflow-hidden rounded-t-lg">
          <img
            src={image}
            alt={name}
            className="w-full h-full object-cover hover:scale-105 transition duration-300"
          />
        </div>
      </Link>

      {/* Content */}
      <div className="p-4">
        <Link to={`/products/${id}`}>
          <h3 className="text-sm font-medium text-gray-900 line-clamp-2 mb-2">
            {name}
          </h3>
        </Link>

        <div className="flex items-center justify-between">
          <p className="text-gray-900 font-semibold">
            ₹{price}
          </p>

         <button
  className="
    w-9 h-9
    flex items-center justify-center
    rounded-full
    border border-gray-300
    text-gray-600
    hover:bg-gray-100
    transition
    cursor-pointer
  "
  onClick={() => addToCart(product, 1)}
  title="Add to cart"
>
  <FiShoppingCart size={16} />
</button>

        </div>
      </div>
    </div>
  );
};

export default ProductCard;
