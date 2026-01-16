import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import type { CartItem as CartItemModel } from "../services/cartService";

interface CartSummaryProps {
  items: CartItemModel[];
}

const CartSummary: React.FC<CartSummaryProps> = ({ items }) => {
  const navigate = useNavigate();
  const { clear } = useCart();

  const subtotal = items.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0
  );

  const totalItems = items.reduce(
    (sum, item) => sum + item.quantity,
    0
  );

  const handleCheckout = async () => {
    if (items.length === 0) return;

    await clear();
    navigate("/order-success");
  };

  return (
    <div className="bg-white border border-gray-200 rounded-lg p-5 sticky top-24">
      <h2 className="text-lg font-semibold text-gray-900 mb-4">
        Order Summary
      </h2>

      {/* Summary rows */}
      <div className="space-y-3 text-sm text-gray-700">
        <div className="flex justify-between">
          <span>Items ({totalItems})</span>
          <span>₹{subtotal}</span>
        </div>

        <div className="flex justify-between">
          <span>Shipping</span>
          <span className="text-green-600">Free</span>
        </div>

        <div className="border-t border-gray-200 pt-3 flex justify-between font-semibold text-gray-900">
          <span>Total</span>
          <span>₹{subtotal}</span>
        </div>
      </div>

      {/* Checkout CTA */}
      <button
        onClick={handleCheckout}
        className="
          w-full
          mt-6
          bg-green-600
          text-white
          py-3
          rounded-lg
          font-medium
          hover:bg-green-700
          transition
        "
      >
        Proceed to Checkout
      </button>

      {/* Continue shopping */}
      <div className="mt-4 text-center">
        <Link
          to="/products"
          className="text-sm text-gray-500 hover:text-gray-700 underline-offset-2 hover:underline"
        >
          Continue shopping
        </Link>
      </div>
    </div>
  );
};

export default CartSummary;
