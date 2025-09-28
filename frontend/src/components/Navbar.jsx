// src/components/Navbar.jsx
import { Link } from "react-router-dom";

function Navbar() {
    return (
        <nav style={{ padding: "10px", background: "#f0f0f0" }}>
            <Link to="/" style={{ marginRight: "15px" }}>Home</Link>
            <Link to="/products" style={{ marginRight: "15px" }}>Products</Link>
            <Link to="/orders" style={{ marginRight: "15px" }}>Orders</Link>
            <Link to="/customers">Customers</Link>
        </nav>
    );
}

export default Navbar;
