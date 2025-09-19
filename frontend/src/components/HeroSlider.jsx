// // src/components/HeroSlider.jsx
// import React from 'react';

// const slides = [
//   { image: 'https://via.placeholder.com/1200x400?text=Big+Sale+1', caption: 'Big Sale This Week!' },
//   { image: 'https://via.placeholder.com/1200x400?text=New+Arrivals', caption: 'Check New Arrivals' },
//   { image: 'https://via.placeholder.com/1200x400?text=Discount Offers', caption: 'Up to 50% Off' },
// ];

// const HeroSlider = () => {
//   const [current, setCurrent] = React.useState(0);

//   React.useEffect(() => {
//     const interval = setInterval(() => {
//       setCurrent(prev => (prev + 1) % slides.length);
//     }, 3000);
//     return () => clearInterval(interval);
//   }, []);

//   return (
//     <div style={{ position: 'relative', width: '100%', height: '400px', overflow: 'hidden' }}>
//       {slides.map((slide, idx) => (
//         <div
//           key={idx}
//           style={{
//             backgroundImage: `url(${slide.image})`,
//             backgroundSize: 'cover',
//             backgroundPosition: 'center',
//             position: 'absolute',
//             width: '100%',
//             height: '100%',
//             transition: 'opacity 0.5s ease-in-out',
//             opacity: idx === current ? 1 : 0,
//           }}
//         >
//           <h2 style={{
//             color: 'white',
//             position: 'absolute',
//             bottom: '50px',
//             left: '50px',
//             fontSize: '2rem',
//             textShadow: '2px 2px 4px rgba(0,0,0,0.6)'
//           }}>
//             {slide.caption}
//           </h2>
//         </div>
//       ))}
//     </div>
//   );
// };

// export default HeroSlider;


import React, { useState, useEffect } from 'react';

const slides = [
  {
    image: 'https://via.placeholder.com/1200x400?text=Big+Sale+Up+To+70%25+Off',
    caption: 'Big Sale! Up to 70% OFF 🔥',
  },
  {
    image: 'https://via.placeholder.com/1200x400?text=New+Gadgets+Arrived',
    caption: 'New Gadgets Just Landed 🚀',
  },
  {
    image: 'https://via.placeholder.com/1200x400?text=Festive+Offers',
    caption: 'Festive Offers Await! 🎉',
  },
];

const HeroSlider = () => {
  const [currentSlide, setCurrentSlide] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentSlide((prevSlide) =>
        prevSlide === slides.length - 1 ? 0 : prevSlide + 1
      );
    }, 3000); // change every 3 seconds

    return () => clearInterval(interval);
  }, []);

  return (
    <div style={{ position: 'relative', width: '100%', height: '400px', overflow: 'hidden' }}>
      {slides.map((slide, index) => (
        <div
          key={index}
          style={{
            backgroundImage: `url(${slide.image})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            width: '100%',
            height: '100%',
            position: 'absolute',
            top: 0,
            left: 0,
            opacity: index === currentSlide ? 1 : 0,
            transition: 'opacity 0.8s ease-in-out',
          }}
        >
          <div style={{
            position: 'absolute',
            bottom: '40px',
            left: '40px',
            color: 'white',
            fontSize: '2rem',
            background: 'rgba(0,0,0,0.4)',
            padding: '10px 20px',
            borderRadius: '8px'
          }}>
            {slide.caption}
          </div>
        </div>
      ))}
    </div>
  );
};

export default HeroSlider;
