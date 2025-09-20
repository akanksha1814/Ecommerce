// // src/components/ProductCard.jsx
// import React from 'react';
// import { Link } from 'react-router-dom';

// const ProductCard = ({ product }) => {
//   // assume product has: id, name, price, originalPrice (if discount), imageUrl
//   const { id, name, price, originalPrice, imageUrl } = product;
//   const discount = originalPrice && originalPrice > price 
//     ? Math.round(((originalPrice - price) / originalPrice) * 100)
//     : 0;

//   return (
//     <div style={{ border: '1px solid #ddd', borderRadius: '5px', padding: '10px', width: '250px' }}>
//       <Link to={`/product/${id}`}>
//         <img src={imageUrl || 'https://via.placeholder.com/200'} alt={name} style={{ width: '100%', height: '200px', objectFit: 'cover' }} />
//       </Link>
//       <h3>{name}</h3>
//       <div>
//         {discount > 0 && <span style={{ textDecoration: 'line-through', color: '#888', marginRight: '10px' }}>₹{originalPrice}</span>}
//         <span style={{ fontWeight: 'bold' }}>₹{price}</span>
//         {discount > 0 && <span style={{ color: 'green', marginLeft: '10px' }}>{discount}% OFF</span>}
//       </div>
//       <button style={{ marginTop: '10px' }}>Add to Cart</button>
//     </div>
//   );
// };

// export default ProductCard;

import React from 'react';
import { Link } from 'react-router-dom';

const ProductCard = ({ product }) => {
  const { id, name, price, originalPrice, imageUrl } = product;
  const discount = originalPrice && originalPrice > price
    ? Math.round(((originalPrice - price) / originalPrice) * 100)
    : 0;

  return (
    <div style={{
      border: '1px solid #ccc',
      borderRadius: '8px',
      padding: '10px',
      width: '230px',
      boxShadow: '0 2px 5px rgba(0,0,0,0.1)'
    }}>
      <Link to={`/product/${id}`}>
        <img
          src={imageUrl || 'https://via.placeholder.com/200x200'}
          alt={name}
          style={{ width: '100%', height: '200px', objectFit: 'cover' }}
        />
      </Link>
      <h3 style={{ fontSize: '16px', margin: '10px 0' }}>{name}</h3>
      <div>
        <span style={{ fontWeight: 'bold', fontSize: '18px' }}>₹{price}</span>
        {discount > 0 && (
          <>
            <span style={{ marginLeft: '8px', textDecoration: 'line-through', color: '#999' }}>
              ₹{originalPrice}
            </span>
            <span style={{ color: 'green', marginLeft: '8px' }}>{discount}% OFF</span>
          </>
        )}
      </div>
      <button style={{
        marginTop: '10px',
        padding: '8px 12px',
        backgroundColor: '#007bff',
        color: 'white',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer'
      }}>
        Add to Cart
      </button>
    </div>
  );
};

export default ProductCard;
