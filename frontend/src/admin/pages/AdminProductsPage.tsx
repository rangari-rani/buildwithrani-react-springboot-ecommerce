import React, { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import AdminProductsTable from "../components/AdminProductsTable";
import type { Product } from "../../products/services/productsData";
import { fetchAdminProducts } from "../services/adminProductApi";

const AdminProductsPage: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
const location = useLocation();

  const fetchProducts = async () => {
    try {
      const data = await fetchAdminProducts();
      setProducts(data);
    } catch (error) {
      console.error("Failed to fetch products", error);
      alert("Failed to load products");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, [location.pathname]);

  if (loading) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-10 text-center text-gray-500">
        Loading products...
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4">

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
      <AdminProductsTable
        products={products}
        onRefresh={fetchProducts}
      />

    </div>
  );
};

export default AdminProductsPage;
