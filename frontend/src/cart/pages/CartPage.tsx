import React from "react";
import CartList from "../components/CartList";
import CartSummary from "../components/CartSummary";
import EmptyCart from "../components/EmptyCart";
import { useCart } from "../context/CartContext";

const CartPage: React.FC = () => {
  const { items, loading } = useCart();

  const isEmpty = items.length === 0;

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-6 mt-12">
          Shopping Cart
        </h1>
        <p className="text-gray-500">Loading cart...</p>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {/* Page Title */}
      <h1 className="text-2xl font-bold text-gray-900 mb-6 mt-12">
        Shopping Cart
      </h1>

      {isEmpty ? (
        <EmptyCart />
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Cart items */}
          <div className="lg:col-span-2">
            <CartList items={items} />
          </div>

          {/* Cart summary */}
          <div>
            <CartSummary items={items} />
          </div>
        </div>
      )}
    </div>
  );
};

export default CartPage;
