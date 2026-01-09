import { Link, NavLink, Outlet } from "react-router-dom";

const AdminLayout = () => {
  return (
    <div className="min-h-screen md:flex pt-4">

      {/* Sidebar (desktop only) */}
      <aside className="hidden md:block w-56 border-r border-gray-200 bg-white">
        <div className="p-4">
          
          <Link to="/" className="text-sm text-green-700 font-bold  uppercase ">
            Wellness Cart 
          </Link>

          <nav className="space-y-4">
            <NavLink
              to="/admin/products"
              className={({ isActive }) =>
                `block px-3 py-2 mt-4 rounded-md text-sm font-medium ${
                  isActive
                    ? "bg-green-50 text-green-700"
                    : "text-gray-600 hover:bg-green-50"
                }`
              }
            >
              Products
            </NavLink>
        
            
          </nav>
        </div>
      </aside>

      {/* Content */}
      <main className="flex-1 bg-gray-50 px-4 py-8">
        <Outlet />
      </main>

    </div>
  );
};

export default AdminLayout;
