import React from "react";
import { FiShoppingCart } from "react-icons/fi";
import { Link } from "react-router-dom";
import type { Product } from "../../services/productsData";
import { useCart } from "../../../cart/context/CartContext";
import RatingStars from "../../../shared/RatingStars";
import { useAuth } from "../../../auth/context/AuthContext";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

interface ProductCardProps {
  product: Product;
}

const ProductCard: React.FC<ProductCardProps> = ({ product }) => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const {
    id,
    imageUrl,
    name,
    price,
    discountPercentage,
    stock,
  } = product;

  const { addItem } = useCart();
  const isOutOfStock = stock <= 0;
  const hasDiscount =
    discountPercentage !== undefined && discountPercentage > 0;

  const discountedPrice = hasDiscount
    ? Math.round(price - (price * discountPercentage) / 100)
    : price;

  const handleQuickAdd = async () => {
    if (isOutOfStock) {
      toast.error("Product is out of stock");
      return;
    }

    if (!isAuthenticated) {
      toast.error("Please login to add items to cart");
      navigate("/login", {
        state: { redirectTo: "/cart" },
      });
      return;
    }

    await addItem(product.id, 1);
    toast.success("Item added to cart");
  };

  return (
    <div className="bg-white rounded-lg border border-gray-100 hover:shadow-sm transition">

      {/* Image */}
      <Link to={`/products/${id}`}>
        <div className="relative w-full h-56 bg-gray-50 overflow-hidden rounded-t-lg">

          {/* Discount badge */}
          {hasDiscount && (
            <span className="absolute top-2 left-2 z-10 bg-red-500 text-white text-xs font-semibold px-2 py-1 rounded">
              {discountPercentage}% OFF
            </span>
          )}

          {imageUrl ? (
            <img
              src={imageUrl}
              alt={name}
              className="w-full h-full object-cover hover:scale-105 transition duration-300"
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center text-sm text-gray-400">
              No image
            </div>
          )}
        </div>
      </Link>

      {/* Content */}
      <div className="p-4">
        <Link to={`/products/${id}`}>
          <h3 className="text-sm font-medium text-gray-900 line-clamp-2 mb-2">
            {name}
          </h3>
        </Link>

        {typeof product.averageRating === "number" && (
          <div className="mb-2 flex items-center gap-1">
            <RatingStars rating={product.averageRating} />
            <span className="text-xs text-gray-500">
              {product.averageRating.toFixed(1)}
            </span>
          </div>
        )}

        <div className="flex items-center justify-between">

          {/* Price */}
          <div className="flex items-center gap-2">
            <p className="text-gray-900 font-semibold">
              ₹{discountedPrice}
            </p>

            {hasDiscount && (
              <p className="text-sm text-gray-400 line-through">
                ₹{price}
              </p>
            )}
          </div>


          {/* Add to cart */}
          <button
            disabled={isOutOfStock}
            className={`
    w-9 h-9
    flex items-center justify-center
    rounded-full
    border
    transition
    ${isOutOfStock
                ? "bg-gray-200 text-gray-400 cursor-not-allowed border-gray-200"
                : "border-gray-300 text-gray-600 hover:bg-gray-100 cursor-pointer"}
  `}
            onClick={handleQuickAdd}
            title={isOutOfStock ? "Out of stock" : "Add to cart"}
          >
            <FiShoppingCart size={16} />
          </button>

        </div>
        {isOutOfStock && (
          <p className="text-xs text-red-500 mt-2">
            Out of stock
          </p>
        )}

        {stock > 0 && stock <= 5 && (
          <p className="text-xs text-orange-500 mt-2">
            Only {stock} left
          </p>
        )}
      </div>
    </div>
  );
};

export default ProductCard;
