import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import { adminLogout } from "../utils/adminAuth";
import { FiHome, FiLogOut, FiActivity } from "react-icons/fi";


const AdminLayout = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    adminLogout();
    navigate("/admin/login", { replace: true });
  };

  return (
    <div className="min-h-screen flex flex-col">

      {/* 🔝 TOP BAR (VISIBLE ON ALL SCREENS) */}
      <header className="flex items-center justify-between px-4 py-3 border-b bg-white">
        <Link
          to="/"
          className="flex items-center gap-2 text-sm font-semibold text-green-700"
        >
          <FiHome />
          Back to Home
        </Link>

        <button
          onClick={handleLogout}
          className="flex items-center gap-2 text-sm font-medium text-red-600 hover:text-red-700 cursor-pointer"
        >
          <FiLogOut />
          Logout
        </button>
      </header>

      {/* BODY */}
      <div className="flex flex-1">

        {/* Sidebar (desktop only) */}
        <aside className="hidden md:flex w-56 border-r border-gray-200 bg-white flex-col">
          <div className="p-4">
            <h2 className="text-sm text-green-700 font-bold uppercase">
              Wellness Cart
            </h2>

            <nav className="space-y-4 mt-6">
              <NavLink
                to="/admin/products"
                className={({ isActive }) =>
                  `block px-3 py-2 rounded-md text-sm font-medium ${isActive
                    ? "bg-green-50 text-green-700"
                    : "text-gray-600 hover:bg-green-50"
                  }`
                }
              >
                Products
              </NavLink>
              <NavLink
                to="/admin/orders"
                className={({ isActive }) =>
                  `block px-3 py-2 rounded-md text-sm font-medium ${isActive
                    ? "bg-green-50 text-green-700"
                    : "text-gray-600 hover:bg-green-50"
                  }`
                }
              >
                Orders
              </NavLink>
              <NavLink
                to="/admin/audit-logs"
                className={({ isActive }) =>
                  `block px-3 py-2 rounded-md text-sm font-medium ${isActive
                    ? "bg-green-50 text-green-700"
                    : "text-gray-600 hover:bg-green-50"
                  }`
                }
              >
                <div className="flex items-center gap-2">
                  <FiActivity />
                  Audit Logs
                </div>
              </NavLink>

            </nav>
          </div>
        </aside>

        {/* Content */}
        <main className="flex-1 bg-gray-50 px-4 py-8">
          <Outlet />
        </main>

      </div>
    </div>
  );
};

export default AdminLayout;
