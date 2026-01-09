import React from "react";
import CartItem from "./CartItem";
import type { Product } from "../../products/services/productsData";

export interface CartItemType {
  product: Product;
  quantity: number;
}

interface CartListProps {
  items: CartItemType[];
}

const CartList: React.FC<CartListProps> = ({ items }) => {
  return (
    <div className="space-y-4">
      {items.map((item) => (
        <CartItem
          key={item.product.id}
          item={item}
        />
      ))}
    </div>
  );
};

export default CartList;
