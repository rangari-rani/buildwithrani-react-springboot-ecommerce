import React from "react";
import { FiEdit2, FiTrash2 } from "react-icons/fi";
import type { Product } from "../../products/services/productsData";
import { useNavigate } from "react-router-dom";

interface AdminProductRowProps {
  product: Product;
}

const AdminProductRow: React.FC<AdminProductRowProps> = ({ product }) => {
  const { image, name, price, isFeatured } = product;
const navigate = useNavigate();
const handleEdit = () => {
  navigate(`/admin/products/${product.id}/edit`);
};

  const handleDelete = () => {
    console.log("Delete product", product.id);
  };


  return (
    <tr className="border-t border-gray-100 hover:bg-gray-50 transition">

      {/* Image */}
      <td className="px-4 py-3">
        <div className="w-12 h-12 bg-gray-100 rounded overflow-hidden">
          <img
            src={image}
            alt={name}
            className="w-full h-full object-cover"
          />
        </div>
      </td>

      {/* Name */}
      <td className="px-4 py-3">
        <p className="text-sm font-medium text-gray-900 line-clamp-1">
          {name}
        </p>
      </td>

      {/* Price */}
      <td className="px-4 py-3">
        <p className="text-sm text-gray-800 font-medium">
          ₹{price}
        </p>
      </td>

      {/* Featured */}
      <td className="px-4 py-3 text-center">
        {isFeatured ? (
          <span className="inline-block text-xs font-medium text-green-700 bg-green-100 px-2 py-0.5 rounded">
            Yes
          </span>
        ) : (
          <span className="inline-block text-xs text-gray-500">
            No
          </span>
        )}
      </td>

      {/* Actions */}
      <td className="px-4 py-3 text-right">
        <div className="inline-flex items-center gap-3">
          <button
            onClick={handleEdit}
            className="text-gray-500 hover:text-blue-600 transition  cursor-pointer"
            title="Edit product"
          >
            <FiEdit2 size={16} />
          </button>

          <button
            onClick={handleDelete}
            className="text-gray-500 hover:text-red-600 transition  cursor-pointer"
            title="Delete product"
          >
            <FiTrash2 size={16} />
          </button>
        </div>
      </td>

    </tr>
  );
};

export default AdminProductRow;
