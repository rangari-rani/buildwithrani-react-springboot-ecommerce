import React, { useState } from "react";
import { FiShoppingCart, FiMinus, FiPlus } from "react-icons/fi";
import type { Product } from "../../services/productsData";
import { useCart } from "../../../cart/context/CartContext";

interface AddToCartSectionProps {
  product: Product;
}

const AddToCartSection: React.FC<AddToCartSectionProps> = ({ product }) => {
  const [quantity, setQuantity] = useState<number>(1);

  const increment = () => setQuantity((q) => q + 1);
  const decrement = () =>
    setQuantity((q) => (q > 1 ? q - 1 : 1));

  const { addToCart } = useCart();

  const handleAddToCart = () => {
    addToCart(product, quantity);
  };

  return (
    <div className="w-full mt-8 border-t border-gray-100 pt-6">

      <div className="flex items-center gap-4 mb-6">
        <span className="text-sm font-medium text-gray-700">
          Quantity
        </span>

        <div className="flex items-center border border-gray-300 rounded-lg overflow-hidden">
          <button
            onClick={decrement}
            className="
        w-10 h-10
        md:w-8 md:h-8
        flex items-center justify-center
        text-gray-600
        hover:bg-gray-100
        transition
      "
          >
            <FiMinus size={12} />
          </button>

          <div
            className="
        w-12 h-10
        md:w-10 md:h-8
        flex items-center justify-center
        text-gray-900 font-medium
      "
          >
            {quantity}
          </div>

          <button
            onClick={increment}
            className="
        w-10 h-10
        md:w-8 md:h-8
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


      {/* Add to cart button */}
      <button
        onClick={handleAddToCart}
        className="
    w-full
    md:w-auto
    md:min-w-50
    flex items-center justify-center gap-2
    bg-green-600
    text-white
    font-medium
    py-3
    px-6
    rounded-lg
    hover:bg-green-700
    transition
  "
      >

        <FiShoppingCart size={18} />
        Add to Cart
      </button>

    </div>
  );
};

export default AddToCartSection;
