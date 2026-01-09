import React from "react";
import { Link } from "react-router-dom";
import { FiShoppingCart } from "react-icons/fi";

const EmptyCart: React.FC = () => {
  return (
    <div className="flex flex-col items-center justify-center py-16 text-center">

      {/* Icon */}
      <div className="w-16 h-16 flex items-center justify-center rounded-full bg-gray-100 mb-4">
        <FiShoppingCart size={28} className="text-gray-400" />
      </div>

      {/* Text */}
      <h2 className="text-lg font-semibold text-gray-900">
        Your cart is empty
      </h2>
      <p className="text-sm text-gray-500 mt-2 max-w-xs">
        Looks like you haven’t added anything to your cart yet.
      </p>

      {/* CTA */}
      <Link
        to="/products"
        className="
          mt-6
          inline-flex items-center justify-center
          px-5 py-2.5
          bg-green-600
          text-white
          text-sm font-medium
          rounded-lg
          hover:bg-green-700
          transition
        "
      >
        Browse Products
      </Link>

    </div>
  );
};

export default EmptyCart;
