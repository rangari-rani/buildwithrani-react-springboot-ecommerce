import { FiShoppingCart, FiUser, FiLogOut, FiPackage } from "react-icons/fi";
import { Link } from "react-router-dom";
import { useCart } from "../../cart/context/CartContext";
import { useAuth } from "../../auth/context/AuthContext";
import { useState } from "react";

export default function Navbar() {
  const { items } = useCart();
  const { isAuthenticated, logout } = useAuth();
  const [open, setOpen] = useState(false);

  const cartCount = items.reduce(
    (sum, item) => sum + item.quantity,
    0
  );

  const displayCount = cartCount > 99 ? "99+" : cartCount;

  return (
    <header className="fixed top-0 left-0 w-full bg-white shadow-sm z-50">
      <nav className="max-w-350 mx-auto px-6 py-3 flex items-center justify-between">
        {/* Logo */}
        <Link to="/">
          <h1 className="text-xl sm:text-2xl font-semibold text-teal-600 tracking-wide">
            Wellness Cart 🌿
          </h1>
        </Link>

        {/* Right actions */}
        <div className="flex items-center gap-3 relative">
          {/* USER DROPDOWN */}
          {isAuthenticated ? (
            <div className="relative">
              <button
                onClick={() => setOpen((o) => !o)}
                className="inline-flex items-center justify-center w-9 h-9 rounded-full hover:bg-gray-100 transition"
                aria-label="Account"
              >
                <FiUser size={20} className="text-gray-700" />
              </button>

              {/* Dropdown */}
              {open && (
                <div
                  className="
                    absolute right-0 mt-2 w-44
                    bg-white border border-gray-200 rounded-lg
                    shadow-lg z-50
                  "
                >
                  <Link
                    to="/orders"
                    onClick={() => setOpen(false)}
                    className="flex items-center gap-2 px-4 py-2 text-sm text-gray-700 hover:bg-gray-50"
                  >
                    <FiPackage size={16} />
                    My Orders
                  </Link>

                  <button
                    onClick={() => {
                      logout();
                      setOpen(false);
                    }}
                    className="w-full flex items-center gap-2 px-4 py-2 text-sm text-red-600 hover:bg-gray-50"
                  >
                    <FiLogOut size={16} />
                    Logout
                  </button>
                </div>
              )}
            </div>
          ) : (
            <>
              {/* Login (Mobile icon) */}
              <Link
                to="/login"
                className="md:hidden inline-flex items-center justify-center w-9 h-9 rounded-full hover:bg-gray-100 transition"
                aria-label="Login"
              >
                <FiUser size={20} className="text-gray-700" />
              </Link>

              {/* Login Button (Desktop) */}
              <Link
                to="/login"
                className="hidden md:block text-sm font-medium border border-teal-500 px-4 py-2 rounded-md bg-teal-500 text-white hover:bg-teal-600 transition"
              >
                Login
              </Link>
            </>
          )}

          {/* CART */}
          <Link
            to="/cart"
            className="relative inline-flex items-center justify-center w-9 h-9 rounded-full hover:bg-gray-100 transition"
            aria-label="Cart"
          >
            <FiShoppingCart size={20} className="text-gray-700" />

            {cartCount > 0 && (
              <span
                className="
                  absolute -top-1 -right-1
                  min-w-4.5 h-4.5
                  px-1
                  flex items-center justify-center
                  text-xs font-semibold
                  bg-green-600 text-white
                  rounded-full
                "
              >
                {displayCount}
              </span>
            )}
          </Link>
        </div>
      </nav>
    </header>
  );
}
