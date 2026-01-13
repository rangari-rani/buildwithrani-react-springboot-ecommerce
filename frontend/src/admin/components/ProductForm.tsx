import React, { useEffect, useState } from "react";
import type { Product } from "../../products/services/productsData";

interface ProductFormProps {
  initialData?: Product;
  onSubmit: (data: ProductFormData) => void;
  onCancel: () => void;
}

export interface ProductFormData {
  name: string;
  description: string;
  price: number;
  discountPercentage?: number;
  featured: boolean;
  image?: File | null;
}

const ProductForm: React.FC<ProductFormProps> = ({
  initialData,
  onSubmit,
  onCancel,
}) => {
  const [name, setName] = useState(initialData?.name ?? "");
  const [description, setDescription] = useState(
    initialData?.description ?? ""
  );
  const [price, setPrice] = useState<number>(initialData?.price ?? 0);
  const [discountPercentage, setDiscountPercentage] = useState<number>(
    initialData?.discountPercentage ?? 0
  );
  const [isFeatured, setIsFeatured] = useState<boolean>(
    initialData?.featured ?? false
  );
  const [image, setImage] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(
    initialData?.imageUrl ?? null
  );

  useEffect(() => {
    if (!initialData) return;

    setName(initialData.name ?? "");
    setDescription(initialData.description ?? "");
    setPrice(initialData.price ?? 0);
    setDiscountPercentage(initialData.discountPercentage ?? 0);
    setIsFeatured(initialData.featured ?? false);
  }, [initialData]);

  useEffect(() => {
  return () => {
    if (preview && preview.startsWith("blob:")) {
      URL.revokeObjectURL(preview);
    }
  };
}, [preview]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    onSubmit({
      name,
      description,
      price,
      discountPercentage:
        discountPercentage > 0 ? discountPercentage : undefined,
      featured: isFeatured,
      image,
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
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-green-500"
          />
        </div>

        {/* Description */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Description
          </label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            rows={4}
            required
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-green-500"
          />
        </div>

        {/* Price */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Price (₹)
          </label>
          <input
            type="text"
            inputMode="numeric"
            pattern="[0-9]*"
            value={price === 0 ? "" : price}
            onChange={(e) => {
              const value = e.target.value.replace(/\D/g, "");
              setPrice(value ? Number(value) : 0);
            }}
            placeholder="e.g. 1299"
            required
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-green-500"
          />
        </div>


        {/* Discount */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Discount (%) <span className="text-gray-400">(optional)</span>
          </label>
          <input
            type="number"
            min={0}
            max={90}
            value={discountPercentage}
            onChange={(e) =>
              setDiscountPercentage(Number(e.target.value))
            }
            className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-green-500"
          />
        </div>

        {/* Featured */}
        <div className="flex items-center gap-2">
          <input
            type="checkbox"
            checked={isFeatured}
            onChange={(e) => setIsFeatured(e.target.checked)}
            className="h-4 w-4 text-green-600 rounded"
          />
          <span className="text-sm text-gray-700">
            Mark as Featured
          </span>
        </div>
        {/* Product Image */}
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Product Image
          </label>

          {preview && (
            <img
              src={preview}
              alt="Preview"
              className="w-32 h-32 object-cover rounded mb-3 border"
            />
          )}

          <input
            type="file"
            accept="image/*"
            onChange={(e) => {
              const file = e.target.files?.[0];
              if (!file) return;

              setImage(file);
              setPreview(URL.createObjectURL(file));
            }}
            className="block w-full text-sm text-gray-600"
          />
        </div>

        {/* Actions */}
        <div className="flex justify-end gap-3 pt-4">
          <button
            type="button"
            onClick={onCancel}
            className="text-sm text-gray-600 hover:text-gray-900"
          >
            Cancel
          </button>

          <button
            type="submit"
            className="px-5 py-2 bg-green-600 text-white text-sm font-medium rounded-lg hover:bg-green-700"
          >
            {initialData ? "Update Product" : "Add Product"}
          </button>
        </div>

      </form>
    </div>
  );
};

export default ProductForm;
