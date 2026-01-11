import React from "react";
import { FiStar } from "react-icons/fi";

interface RatingStarsProps {
  rating: number; // 0–5
}

const RatingStars: React.FC<RatingStarsProps> = ({ rating }) => {
  const fullStars = Math.floor(rating);
  const hasHalfStar = rating % 1 >= 0.5;
  const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

  return (
    <div className="flex items-center gap-0.5 text-yellow-500">
      {Array.from({ length: fullStars }).map((_, i) => (
        <FiStar key={`full-${i}`} fill="currentColor" />
      ))}

      {hasHalfStar && (
        <FiStar
          style={{ clipPath: "inset(0 50% 0 0)" }}
          fill="currentColor"
        />
      )}

      {Array.from({ length: emptyStars }).map((_, i) => (
        <FiStar key={`empty-${i}`} className="text-gray-300" />
      ))}
    </div>
  );
};

export default RatingStars;
