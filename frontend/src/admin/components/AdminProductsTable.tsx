import React from "react";
import type { Product } from "../../products/services/productsData";
import AdminProductRow from "./AdminProductRow";

interface AdminProductsTableProps {
  products: Product[];
}

const AdminProductsTable: React.FC<AdminProductsTableProps> = ({ products }) => {
  if (!products || products.length === 0) {
    return (
      <div className="text-center text-gray-500 py-12">
        No products found.
      </div>
    );
  }

  return (
    <div className="overflow-x-auto border border-gray-200 rounded-lg bg-white">
      <table className="min-w-full border-collapse">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-4 py-3 text-left text-xs font-semibold text-gray-600">
              Image
            </th>
            <th className="px-4 py-3 text-left text-xs font-semibold text-gray-600">
              Name
            </th>
            <th className="px-4 py-3 text-left text-xs font-semibold text-gray-600">
              Price
            </th>
            <th className="px-4 py-3 text-center text-xs font-semibold text-gray-600">
              Featured
            </th>
            <th className="px-4 py-3 text-right text-xs font-semibold text-gray-600">
              Actions
            </th>
          </tr>
        </thead>

        <tbody>
          {products.map((product) => (
            <AdminProductRow
              key={product.id}
              product={product}
            />
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminProductsTable;
