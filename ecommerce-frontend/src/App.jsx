import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Products from './pages/Products';
import ProductDetail from './pages/ProductDetail';
import Categories from './pages/Categories';
import Cart from './pages/Cart';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Profile from './pages/Profile';
import Orders from './pages/Orders';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
    return (
        <AuthProvider>
            <Router>
                {/* Navbar stays at the top on all pages */}
                <Navbar />

                {/* Main content */}
                <div className="App" style={{ marginTop: '70px' }}>
                    <Routes>
                        {/* Public Routes */}
                        <Route path="/" element={<Home />} />
                        {/* All products */}
                        <Route path="/products" element={<Products />} />
                        {/* Single product details */}
                        <Route path="/products/:id" element={<Products />} />
                        <Route path="/categories" element={<Categories />} />
                        <Route path="/cart" element={<Cart />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/signup" element={<Signup />} />

                        {/* Protected Routes */}
                        <Route
                            path="/profile"
                            element={
                                <ProtectedRoute>
                                    <Profile />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="/orders"
                            element={
                                <ProtectedRoute>
                                    <Orders />
                                </ProtectedRoute>
                            }
                        />

                        {/* Fallback for unmatched routes */}
                        <Route path="*" element={<h2 className="text-center mt-5">Page Not Found</h2>} />
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;
