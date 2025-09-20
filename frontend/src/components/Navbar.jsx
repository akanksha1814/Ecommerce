import React from 'react';
import { Link } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav style={{
      padding: '15px',
      backgroundColor: '#003b6f',
      color: 'white',
      display: 'flex',
      justifyContent: 'space-between',
      alignItems: 'center'
    }}>
      <div>
        <Link to="/" style={{ color: 'white', textDecoration: 'none', fontSize: '22px' }}>
          IshikaKart 🛒
        </Link>
      </div>
      <div style={{ display: 'flex', gap: '20px' }}>
        <Link to="/products" style={{ color: 'white' }}>Products</Link>
        <Link to="/login" style={{ color: 'white' }}>Login</Link>
        <Link to="/register" style={{ color: 'white' }}>Register</Link>
        <Link to="/orders" style={{ color: 'white' }}>Orders</Link>
      </div>
    </nav>
  );
};

export default Navbar;
