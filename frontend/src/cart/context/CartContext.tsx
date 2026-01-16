import {
  createContext,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";
import {
  getCart,
  addToCart,
  updateCartItem,
  removeCartItem,
  clearCart,
  type CartItem,
} from "../services/cartService";
import { useAuth } from "../../auth/context/AuthContext";

interface CartContextType {
  items: CartItem[];
  loading: boolean;
  refreshCart: () => Promise<void>;
  addItem: (productId: number, quantity?: number) => Promise<void>;
  updateItem: (productId: number, quantity: number) => Promise<void>;
  removeItem: (productId: number) => Promise<void>;
  clear: () => Promise<void>;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: ReactNode }) => {
  const [items, setItems] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const { isAuthenticated } = useAuth();

  /**
   * Fetch cart from backend (only place where loading=true is needed)
   */
  const refreshCart = async () => {
    try {
      setLoading(true);
      const data = await getCart();
      setItems(data.items);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Add product to cart (optimistic UX)
   */
  const addItem = async (productId: number, quantity = 1) => {
    const data = await addToCart(productId, quantity);
    setItems(data.items);
  };

  /**
   * Update quantity (optimistic UX)
   */
  const updateItem = async (productId: number, quantity: number) => {
    const data = await updateCartItem(productId, quantity);
    setItems(data.items);
  };

  /**
   * Remove item (optimistic UX)
   */
  const removeItem = async (productId: number) => {
    await removeCartItem(productId);
    setItems((prev) =>
      prev.filter((item) => item.productId !== productId)
    );
  };

  /**
   * Clear entire cart (destructive → keep loading)
   */
  const clear = async () => {
    try {
      setLoading(true);
      await clearCart();
      setItems([]);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Load cart only when user is authenticated
   */
  useEffect(() => {
    if (isAuthenticated) {
      refreshCart();
    } else {
      setItems([]);
    }
  }, [isAuthenticated]);

  return (
    <CartContext.Provider
      value={{
        items,
        loading,
        refreshCart,
        addItem,
        updateItem,
        removeItem,
        clear,
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

/**
 * Custom hook for consuming cart context
 */
export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error("useCart must be used within a CartProvider");
  }
  return context;
};
