import React from "react";
import HeroSlider from "./HeroSlider";
import HeroBadges from "./HeroBadges";
import { DEFAULT_HERO_CONTENT, HERO_IMAGES } from "./heroData";
import { Link } from "react-router-dom";

interface HeroProps {
  headline?: string;
  subtext?: string;
  ctaText?: string;
}

const Hero: React.FC<HeroProps> = ({
  headline = DEFAULT_HERO_CONTENT.headline,
  subtext = DEFAULT_HERO_CONTENT.subtext,
  ctaText = DEFAULT_HERO_CONTENT.ctaText,
}) => {
  return (
    <section className="py-8">
      <div className="mt-14 max-w-350 mx-auto px-6 rounded-xl shadow-sm flex flex-col md:flex-row items-center justify-between py-4 md:py-12 bg-linear-to-r from-green-100 via-green-200 to-green-300">
        {/* Text Section */}
        <div className="md:w-1/2 w-full text-center md:text-left mb-10 md:mb-0 text-gray-800">
          <span className="inline-block bg-green-200 text-green-800 px-3 py-1 rounded-full text-sm font-semibold mb-2">
            🔥 Hot promotions
          </span>

          <h1 className="text-3xl sm:text-4xl md:text-5xl font-bold mb-4 leading-tight">
            {headline}
          </h1>

          <p className="text-base sm:text-lg mb-6">{subtext}</p>

          <Link to="/products" className="bg-green-600 text-white px-4 py-2 text-lg shadow-lg hover:scale-105 transition-transform">
            {ctaText}
          </Link>

          <HeroBadges />
        </div>
{/* Slider */}
<div className="md:w-1/2 w-full flex justify-center">
  <div className="w-full max-w-150">
    <HeroSlider images={HERO_IMAGES} />
  </div>
</div>

      </div>
    </section>
  );
};

export default Hero;
