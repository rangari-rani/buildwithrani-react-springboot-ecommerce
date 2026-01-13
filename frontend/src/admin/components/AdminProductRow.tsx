import React, { useEffect, useState } from "react";
import { FiEdit2 } from "react-icons/fi";
import type { Product } from "../../products/services/productsData";
import { useNavigate } from "react-router-dom";
import {
  updateFeaturedStatus,
  updateProductStatus,
} from "../services/adminProductApi";

interface AdminProductRowProps {
  product: Product;
  onRefresh: () => void;
}

const AdminProductRow: React.FC<AdminProductRowProps> = ({
  product,
  onRefresh,
}) => {
  const navigate = useNavigate();

  // 🔥 LOCAL STATE FOR OPTIMISTIC UI
  const [isFeatured, setIsFeatured] = useState<boolean>(product.featured);

  // 🔁 SYNC WHEN PARENT DATA CHANGES
  useEffect(() => {
    setIsFeatured(product.featured);
  }, [product.featured]);

  const handleEdit = () => {
    navigate(`/admin/products/${product.id}/edit`);
  };

  // ⭐ FEATURED TOGGLE (FIXED)
  const handleFeaturedToggle = async () => {
    const nextValue = !isFeatured;

    // optimistic update
    setIsFeatured(nextValue);

    try {
      await updateFeaturedStatus(product.id, nextValue);
      onRefresh(); // sync with backend
    } catch (error) {
      // rollback on failure
      setIsFeatured(product.featured);
      console.error("Failed to update featured status", error);
      alert("Failed to update featured status");
    }
  };

  const handleStatusToggle = async () => {
    try {
      await updateProductStatus(
        product.id,
        product.status === "ACTIVE" ? "INACTIVE" : "ACTIVE"
      );
      onRefresh();
    } catch (error) {
      console.error("Failed to update product status", error);
      alert("Failed to update product status");
    }
  };

  return (
    <tr className="border-t border-gray-100 hover:bg-gray-50 transition">

      {/* Image */}
      <td className="px-4 py-3">
        <div className="w-12 h-12 bg-gray-100 rounded overflow-hidden flex items-center justify-center">
          {product.imageUrl ? (
            <img
              src={product.imageUrl}
              alt={product.name}
              className="w-full h-full object-cover"
            />
          ) : (
            <span className="text-xs text-gray-400">No image</span>
          )}
        </div>
      </td>

      {/* Name */}
      <td className="px-4 py-3">
        <p className="text-sm font-medium text-gray-900 line-clamp-1">
          {product.name}
        </p>
        <p className="text-xs text-gray-500">{product.status}</p>
      </td>

      {/* Price */}
      <td className="px-4 py-3">
        <p className="text-sm text-gray-800 font-medium">
          ₹{product.price}
        </p>
      </td>

      {/* Featured */}
      <td className="px-4 py-3 text-center">
        <button
          onClick={handleFeaturedToggle}
          className={`
            text-xs font-medium px-2 py-0.5 rounded cursor-pointer
            ${
              isFeatured
                ? "text-green-700 bg-green-100"
                : "text-gray-600 bg-gray-100"
            }
          `}
        >
          {isFeatured ? "Yes" : "No"}
        </button>
      </td>

      {/* Actions */}
      <td className="px-4 py-3 text-right">
        <div className="inline-flex items-center gap-3">

          {/* Edit */}
          <button
            onClick={handleEdit}
            className="text-gray-500 hover:text-blue-600 transition cursor-pointer"
            title="Edit product"
          >
            <FiEdit2 size={16} />
          </button>

          {/* Activate / Deactivate */}
          <button
            onClick={handleStatusToggle}
            className={`
              text-xs font-medium px-2 py-1 rounded cursor-pointer
              ${
                product.status === "ACTIVE"
                  ? "text-red-600 bg-red-100"
                  : "text-green-700 bg-green-100"
              }
            `}
          >
            {product.status === "ACTIVE" ? "Deactivate" : "Activate"}
          </button>

        </div>
      </td>

    </tr>
  );
};

export default AdminProductRow;
