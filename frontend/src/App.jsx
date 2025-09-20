// import React from 'react';
// import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
// import Login from './pages/Login';
// import Register from './pages/Register';
// import ProductList from './pages/ProductList';
// import AddProduct from './pages/AddProduct';
// import OrderHistory from './pages/OrderHistory';
// import ProtectedRoute from './components/ProtectedRoute';

// function App() {
//   return (
//     <BrowserRouter>
//       <nav>
//         <Link to="/">Products</Link> | <Link to="/add">Add Product</Link> | <Link to="/orders">My Orders</Link> | <Link to="/login">Login</Link> | <Link to="/register">Register</Link>
//       </nav>

//       <Routes>
//         <Route path="/" element={<ProductList />} />
//         <Route path="/add" element={<ProtectedRoute><AddProduct /></ProtectedRoute>} />
//         <Route path="/orders" element={<ProtectedRoute><OrderHistory /></ProtectedRoute>} />
//         <Route path="/login" element={<Login />} />
//         <Route path="/register" element={<Register />} />
//       </Routes>
//     </BrowserRouter>
//   );
// }

// export default App;

// src/App.jsx
// import React from 'react';
// import { BrowserRouter, Routes, Route } from 'react-router-dom';
// import Navbar from './components/Navbar';
// import HeroSlider from './components/HeroSlider';
// import Footer from './components/Footer';
// import Home from './pages/Home';
// import ProductList from './pages/ProductList';
// import ProductDetail from './pages/ProductDetail';
// import Login from './pages/Login';
// import Register from './pages/Register';
// import OrderHistory from './pages/OrderHistory';
// import ProtectedRoute from './components/ProtectedRoute';
// import AddProduct from './pages/AddProduct';

// function App() {
//   return (
    
//     <BrowserRouter>
//       <Navbar />
//       <HeroSlider />
//       <Routes>
//         <Route path="/" element={<Home />} />
//         <Route path="/products" element={<ProductList />} />
//         <Route path="/category/:categoryName" element={<ProductList />} />
//         <Route path="/search" element={<ProductList />} />
//         <Route path="/product/:id" element={<ProductDetail />} />
//         <Route path="/add" element={<ProtectedRoute><AddProduct /></ProtectedRoute>} />
//         <Route path="/orders" element={<ProtectedRoute><OrderHistory /></ProtectedRoute>} />
//         <Route path="/login" element={<Login />} />
//         <Route path="/register" element={<Register />} />
//       </Routes>
//       <Footer />
//     </BrowserRouter>
//   );
// }

// export default App;

import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import HeroSlider from './components/HeroSlider';
import ProductList from './pages/ProductList';
import ProductDetail from './pages/ProductDetail';
import Login from './pages/Login';
import Register from './pages/Register';

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <HeroSlider />
      <div style={{ padding: '20px' }}>
        <Routes>
          <Route path="/" element={<ProductList />} />
          <Route path="/products" element={<ProductList />} />
          <Route path="/product/:id" element={<ProductDetail />} />
        <Route path="*" element={<h1 style={{ textAlign: 'center', marginTop: '50px' }}>Page not found ❌</h1>} />
         <Route path="/login" element={<Login />} />
  <Route path="/register" element={<Register />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;

