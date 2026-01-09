import React from "react";
import { FiPlus, FiMinus, FiTrash2 } from "react-icons/fi";
import type { CartItemType } from "./CartList";
import { useCart } from "../context/CartContext";

interface CartItemProps {
  item: CartItemType;
}

const CartItem: React.FC<CartItemProps> = ({ item }) => {
  const { product, quantity } = item;
  const { image, name, price } = product;
const { updateQuantity, removeFromCart } = useCart();

const increment = () =>
  updateQuantity(product.id, quantity + 1);

const decrement = () =>
  updateQuantity(product.id, quantity - 1);

const removeItem = () =>
  removeFromCart(product.id);


  return (
    <div className="flex gap-4 p-4 bg-white border border-gray-200 rounded-lg">

      {/* Image */}
      <div className="w-20 h-20 bg-gray-50 rounded-md overflow-hidden shrink-0">
        <img
          src={image}
          alt={name}
          className="w-full h-full object-cover"
        />
      </div>

      {/* Content */}
      <div className="flex-1">

        {/* Name + Remove */}
        <div className="flex items-start justify-between gap-4">
          <h3 className="text-sm font-medium text-gray-900 line-clamp-2">
            {name}
          </h3>

          <button
            onClick={removeItem}
            className="text-gray-400 hover:text-red-600 transition cursor-pointer"
            title="Remove item"
          >
            <FiTrash2 size={16} />
          </button>
        </div>

        {/* Price + Quantity */}
        <div className="flex items-center justify-between mt-4">

          <p className="text-sm font-semibold text-gray-900">
            ₹{price}
          </p>

          {/* Quantity selector */}
          <div className="flex items-center border border-gray-300 rounded-lg overflow-hidden">
            <button
              onClick={decrement}
              className="
                w-8 h-8
                flex items-center justify-center
                text-gray-600
                hover:bg-gray-100
                transition
              "
            >
              <FiMinus size={12} />
            </button>

            <div className="w-10 h-8 flex items-center justify-center text-sm font-medium text-gray-900">
              {quantity}
            </div>

            <button
              onClick={increment}
              className="
                w-8 h-8
                flex items-center justify-center
                text-gray-600
                hover:bg-gray-100
                transition
              "
            >
              <FiPlus size={12} />
            </button>
          </div>

        </div>
      </div>
    </div>
  );
};

export default CartItem;
