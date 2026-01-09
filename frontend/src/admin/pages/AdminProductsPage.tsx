import React from "react";
import { products } from "../../products/services/productsData";
import AdminProductsTable from "../components/AdminProductsTable";
import { Link } from "react-router-dom";

const AdminProductsPage: React.FC = () => {
  return (
    <div className="max-w-7xl mx-auto px-4 ">

      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-8">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Admin – Products
          </h1>
          <p className="text-sm text-gray-500 mt-1">
            Product management 
          </p>
        </div>

    <Link
  to="/admin/products/new"
  className="
   cursor-pointer
    inline-flex items-center justify-center
    px-4 py-2
    bg-green-600
    text-white
    text-sm font-medium
    rounded-lg
    hover:bg-green-700
    transition
  "
>
  + Add Product
</Link>
      </div>

      {/* Products Table */}
      <AdminProductsTable products={products} />

    </div>
  );
};

export default AdminProductsPage;
