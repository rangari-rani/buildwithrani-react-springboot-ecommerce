import React from "react";

interface HeroBadgesProps {
  items?: string[];
}

const HeroBadges: React.FC<HeroBadgesProps> = ({
  items = ["Free Shipping", "Easy Returns"],
}) => {
  return (
    <div className="flex gap-3 mt-4 justify-center md:justify-start">
      {items.map((label, index) => (
        <span
          key={index}
          className="bg-green-200 text-green-800 px-3 py-1 rounded-full text-sm"
        >
          {label}
        </span>
      ))}
    </div>
  );
};

export default HeroBadges;
