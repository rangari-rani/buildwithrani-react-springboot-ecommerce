import React, { useState } from "react";
import { FiShoppingCart, FiMinus, FiPlus } from "react-icons/fi";
import type { Product } from "../../services/productsData";
import { useCart } from "../../../cart/context/CartContext";
import { useAuth } from "../../../auth/context/AuthContext";
import toast from "react-hot-toast";
import { useNavigate } from "react-router-dom";

interface AddToCartSectionProps {
  product: Product;
}

const AddToCartSection: React.FC<AddToCartSectionProps> = ({ product }) => {
  const [quantity, setQuantity] = useState<number>(1);
  const { addItem } = useCart();
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const increment = () =>
    setQuantity((q) =>
      Math.min(q + 1, product.stock)
    );

  const decrement = () =>
    setQuantity((q) => (q > 1 ? q - 1 : 1));

  const isOutOfStock =
    product.status !== "ACTIVE" || product.stock <= 0;

  const handleAddToCart = async () => {
    if (isOutOfStock) return;

    if (quantity > product.stock) {
      toast.error("Selected quantity exceeds available stock");
      return;
    }

    if (!isAuthenticated) {
      toast.error("Please login to add items to cart");
      navigate("/login", {
        state: { redirectTo: "/cart" },
      });
      return;
    }

    await addItem(product.id, quantity);
    toast.success("Item added to cart");
    setQuantity(1);
  };


  return (
    <div className="w-full mt-8 border-t border-gray-100 pt-6">

      {product.stock > 0 && product.stock <= 5 && (
        <p className="text-sm text-orange-500 mb-4">
          Only {product.stock} left in stock
        </p>
      )}

      {product.stock === 0 && (
        <p className="text-sm text-red-500 mb-4">
          Out of stock
        </p>
      )}

      {/* Quantity selector */}
      {!isOutOfStock && (
        <div className="flex items-center gap-4 mb-6">
          <span className="text-sm font-medium text-gray-700">
            Quantity
          </span>

          <div className="flex items-center border border-gray-300 rounded-lg overflow-hidden">
            <button
              onClick={decrement}
              className="w-10 h-10 md:w-8 md:h-8 flex items-center justify-center text-gray-600 hover:bg-gray-100 transition"
            >
              <FiMinus size={12} />
            </button>

            <div className="w-12 h-10 md:w-10 md:h-8 flex items-center justify-center text-gray-900 font-medium">
              {quantity}
            </div>

            <button
              onClick={increment}
              disabled={quantity >= product.stock}
              className={`
    w-10 h-10 md:w-8 md:h-8 flex items-center justify-center
    transition
    ${quantity >= product.stock
                  ? "text-gray-300 cursor-not-allowed"
                  : "text-gray-600 hover:bg-gray-100"}
  `}
            >
              <FiPlus size={12} />
            </button>
          </div>
        </div>
      )}
      {/* Add to cart button */}
      <button
        onClick={handleAddToCart}
        disabled={isOutOfStock}
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
          disabled:bg-gray-300
          disabled:cursor-not-allowed
        "
      >
        <FiShoppingCart size={18} />
        {isOutOfStock ? "Out of Stock" : "Add to Cart"}
      </button>
    </div>
  );
};

export default AddToCartSection;
