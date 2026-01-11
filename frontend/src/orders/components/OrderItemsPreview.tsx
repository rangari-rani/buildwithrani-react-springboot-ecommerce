import React from "react";
import type { OrderItem } from "../services/ordersData";

interface OrderItemsPreviewProps {
  items: OrderItem[];
}

const MAX_PREVIEW_ITEMS = 3;

const OrderItemsPreview: React.FC<OrderItemsPreviewProps> = ({ items }) => {
  const previewItems = items.slice(0, MAX_PREVIEW_ITEMS);
  const remainingCount = items.length - previewItems.length;

  return (
    <div className="flex items-center gap-2 mt-3">

      {/* Product images */}
      {previewItems.map((item) => (
        <div
          key={item.product.id}
          className="w-12 h-12 rounded-md overflow-hidden border border-gray-200 bg-gray-50 shrink-0"
        >
          <img
            src={item.product.image}
            alt={item.product.name}
            className="w-full h-full object-cover"
          />
        </div>
      ))}

      {/* +X more */}
      {remainingCount > 0 && (
        <div className="
          w-12 h-12
          flex items-center justify-center
          rounded-md
          border border-gray-200
          bg-gray-100
          text-xs
          font-medium
          text-gray-600
        ">
          +{remainingCount}
        </div>
      )}

    </div>
  );
};

export default OrderItemsPreview;
