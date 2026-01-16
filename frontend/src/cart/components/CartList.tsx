import React from "react";
import CartItem from "./CartItem";
import type { CartItem as CartItemModel } from "../services/cartService";

interface CartListProps {
  items: CartItemModel[];
}

const CartList: React.FC<CartListProps> = ({ items }) => {
  return (
    <div className="space-y-4">
      {items.map((item) => (
        <CartItem
          key={item.productId}
          item={item}
        />
      ))}
    </div>
  );
};

export default CartList;
