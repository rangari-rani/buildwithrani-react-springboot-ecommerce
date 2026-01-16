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
    <div className="flex items-center gap-2 mt-3 flex-wrap">

      {/* Product name chips */}
      {previewItems.map((item) => (
        <span
          key={item.productId}
          className="
            px-3 py-1
            rounded-full
            bg-gray-100
            text-xs
            text-gray-700
            font-medium
          "
        >
          {item.productName}
        </span>
      ))}

      {/* +X more */}
      {remainingCount > 0 && (
        <span
          className="
            px-3 py-1
            rounded-full
            bg-gray-200
            text-xs
            font-medium
            text-gray-600
          "
        >
          +{remainingCount} more
        </span>
      )}
    </div>
  );
};

export default OrderItemsPreview;
