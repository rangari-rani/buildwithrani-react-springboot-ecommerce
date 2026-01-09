import React, { useState } from "react";
import type { Product } from "../../products/services/productsData";

interface ProductFormProps {
  initialData?: Product;      // present when editing
  onSubmit: (data: ProductFormData) => void;
  onCancel: () => void;
}

export interface ProductFormData {
  name: string;
  price: number;
  image: string;
  isFeatured: boolean;
}

const ProductForm: React.FC<ProductFormProps> = ({
  initialData,
  onSubmit,
  onCancel,
}) => {
  const [name, setName] = useState(initialData?.name ?? "");
  const [price, setPrice] = useState<number>(initialData?.price ?? 0);
  const [image, setImage] = useState(initialData?.image ?? "");
  const [isFeatured, setIsFeatured] = useState<boolean>(
    initialData?.isFeatured ?? false
  );

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    onSubmit({
      name,
      price,
      image,
      isFeatured,
    });
  };

  return (
    <div className="bg-white border border-gray-200 rounded-lg p-6 max-w-xl w-full">

      <h2 className="text-lg font-semibold text-gray-900 mb-6">
        {initialData ? "Edit Product" : "Add Product"}
      </h2>

      <form onSubmit={handleSubmit} className="space-y-5">

        {/* Product Name */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Product Name
          </label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        {/* Price */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Price (₹)
          </label>
          <input
            type="number"
            value={price}
            min={0}
            onChange={(e) => setPrice(Number(e.target.value))}
            required
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        {/* Image URL */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Image URL
          </label>
          <input
            type="url"
            value={image}
            onChange={(e) => setImage(e.target.value)}
            required
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-500"
          />
        </div>

        {/* Featured */}
        <div className="flex items-center gap-2">
          <input
            type="checkbox"
            checked={isFeatured}
            onChange={(e) => setIsFeatured(e.target.checked)}
            className="h-4 w-4 text-green-600 border-gray-300 rounded"
          />
          <span className="text-sm text-gray-700">
            Mark as Featured
          </span>
        </div>

        {/* Actions */}
        <div className="flex items-center justify-end gap-3 pt-4">
          <button
            type="button"
            onClick={onCancel}
            className="px-4 py-2 text-sm text-gray-600 hover:text-gray-900  cursor-pointer"
          >
            Cancel
          </button>

          <button
            type="submit"
            className="
             cursor-pointer
              px-5 py-2
              bg-green-600
              text-white
              text-sm font-medium
              rounded-lg
              hover:bg-green-700
              transition
            "
          >
            {initialData ? "Update Product" : "Add Product"}
          </button>
        </div>

      </form>
    </div>
  );
};

export default ProductForm;
