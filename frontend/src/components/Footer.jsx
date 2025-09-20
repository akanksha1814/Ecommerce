// src/components/Footer.jsx
import React from 'react';
import { Link } from 'react-router-dom';

const Footer = () => (
  <footer style={{ background: '#f5f5f5', padding: '20px 10px', marginTop: '40px' }}>
    <div style={{ display: 'flex', justifyContent: 'space-around' }}>
      <div>
        <h4>About Us</h4>
        <Link to="/about">About</Link><br/>
        <Link to="/faq">FAQ</Link><br/>
        <Link to="/contact">Contact</Link>
      </div>
      <div>
        <h4>Support</h4>
        <Link to="/terms">Terms & Conditions</Link><br/>
        <Link to="/privacy">Privacy Policy</Link>
      </div>
      <div>
        <h4>Account</h4>
        <Link to="/login">Login</Link><br/>
        <Link to="/orders">My Orders</Link><br/>
        <Link to="/profile">Profile</Link>
      </div>
    </div>
    <div style={{ textAlign: 'center', marginTop: '20px' }}>
      © {new Date().getFullYear()} YourStoreName. All rights reserved.
    </div>
  </footer>
);

export default Footer;
