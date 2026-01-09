import React, { useMemo } from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

interface HeroSliderProps {
  images: string[];
}

const HeroSlider: React.FC<HeroSliderProps> = ({ images }) => {
  const settings = useMemo(() => ({
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
    autoplay: true,
    autoplaySpeed: 3000,
  }), []);

  return (
    <Slider {...settings} className="w-full max-w-lg h-50 md:h-75">
      {images.map((img, idx) => (
        <div key={idx} className="flex items-center justify-center">
          <img
            src={img}
            alt={`Slide ${idx + 1}`}
            className="w-full h-full object-cover rounded-lg"
            loading="lazy"
          />
        </div>
      ))}
    </Slider>
  );
};

export default HeroSlider;
