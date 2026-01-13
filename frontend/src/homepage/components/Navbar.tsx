import { FiShoppingCart, FiUser } from "react-icons/fi";
import { Link } from "react-router-dom";
import { useCart } from "../../cart/context/CartContext";

export default function Navbar() {
  const { items } = useCart();

  const cartCount = items.reduce(
    (sum, item) => sum + item.quantity,
    0
  );

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
        <div className="flex items-center gap-3">

          {/* Login (Mobile icon) */}
          <Link
            to="/login"
            className="md:hidden relative inline-flex items-center justify-center w-9 h-9 rounded-full hover:bg-gray-100 transition"
            aria-label="Login"
          >
            <FiUser size={20} className="text-gray-700" />
          </Link>

          {/* Cart */}
          <Link
            to="/cart"
            className="relative inline-flex items-center justify-center w-9 h-9 rounded-full hover:bg-gray-100 transition"
            aria-label="Cart"
          >
            <FiShoppingCart size={20} className="text-gray-700" />

            {/* Badge */}
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
                {cartCount}
              </span>
            )}
          </Link>

          {/* Login Button (Desktop) */}
          <Link
            to="/login"
            className="hidden md:block text-sm font-medium border border-teal-500 px-4 py-2 rounded-md bg-teal-500 text-white hover:bg-teal-600 transition"
          >
            Login
          </Link>

        </div>
      </nav>
    </header>
  );
}
