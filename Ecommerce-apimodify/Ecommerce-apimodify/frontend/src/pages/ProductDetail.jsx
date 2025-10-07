import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const dummyProducts = [
  {
    id: 1,
    name: 'OnePlus Nord CE 3 Lite 5G',
    price: 17999,
    originalPrice: 20999,
    description: 'Experience fast performance with Snapdragon 695 and a 108MP camera. Comes with a 67W SUPERVOOC charger.',
    imageUrl: 'https://m.media-amazon.com/images/I/41zSmQwV+1L._SX300_SY300_QL70_FMwebp_.jpg'
  },
  {
    id: 2,
    name: 'Samsung Galaxy M14 5G',
    price: 11490,
    originalPrice: 14990,
    description: 'Comes with a 6000mAh battery, triple camera, and smooth 90Hz display.',
    imageUrl: 'https://m.media-amazon.com/images/I/81ZSn2rk9WL._SX679_.jpg'
  },
  {
    id: 3,
    name: 'boAt Airdopes 161 Bluetooth Earbuds',
    price: 999,
    originalPrice: 2490,
    description: 'Enjoy 40 hours of playback, fast charging, and immersive sound.',
    imageUrl: 'https://m.media-amazon.com/images/I/61oT6fDdLZL._SX679_.jpg'
  },
  {
    id: 4,
    name: 'Apple MacBook Air (M1, 2020)',
    price: 69999,
    originalPrice: 99900,
    description: 'Powerful M1 chip with 8-core CPU and 7-core GPU, 13.3-inch Retina display.',
    imageUrl: 'https://m.media-amazon.com/images/I/71vFKBpKakL._SX679_.jpg'
  }
];

const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);

  useEffect(() => {
    const foundProduct = dummyProducts.find(p => p.id === parseInt(id));
    setProduct(foundProduct);
  }, [id]);

  if (!product) return <p>Product not found.</p>;

  const discount = product.originalPrice > product.price
    ? Math.round(((product.originalPrice - product.price) / product.originalPrice) * 100)
    : 0;

  return (
    <div style={{
      display: 'flex',
      gap: '40px',
      padding: '40px',
      maxWidth: '1000px',
      margin: 'auto',
      border: '1px solid #eee',
      borderRadius: '10px',
      boxShadow: '0 0 10px rgba(0,0,0,0.1)'
    }}>
      <div>
        <img
          src={product.imageUrl}
          alt={product.name}
          style={{ width: '400px', height: '400px', objectFit: 'contain', borderRadius: '8px' }}
        />
      </div>

      <div>
        <h2 style={{ marginBottom: '10px' }}>{product.name}</h2>
        <div style={{ fontSize: '24px', fontWeight: 'bold' }}>₹{product.price}</div>

        {discount > 0 && (
          <p style={{ fontSize: '16px', color: '#666' }}>
            <span style={{ textDecoration: 'line-through' }}>₹{product.originalPrice}</span>
            <span style={{ color: 'green', marginLeft: '10px' }}>{discount}% OFF</span>
          </p>
        )}

        <p style={{ marginTop: '20px', fontSize: '16px', lineHeight: '1.6' }}>
          {product.description}
        </p>

        <button style={{
          marginTop: '30px',
          padding: '12px 25px',
          fontSize: '16px',
          backgroundColor: '#007bff',
          color: 'white',
          border: 'none',
          borderRadius: '5px',
          cursor: 'pointer'
        }}>
          Add to Cart
        </button>
      </div>
    </div>
  );
};

export default ProductDetail;
