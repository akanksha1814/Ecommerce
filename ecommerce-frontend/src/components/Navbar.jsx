import { Navbar, Nav, Container, Form, FormControl, Button, NavDropdown, Offcanvas } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FaUserCircle, FaShoppingCart, FaSearch } from 'react-icons/fa';

export default function Navigation() {
    const { user, logout } = useAuth();

    const handleLogout = () => {
        logout();
    };

    return (
        <Navbar bg="dark" variant="dark" expand="lg" sticky="top" className="py-2">
            <Container fluid className="d-flex align-items-center justify-content-between px-3">

                {/* Brand */}
                <Navbar.Brand as={Link} to="/" className="fw-bold fs-4 mb-0">
                    E-Com
                </Navbar.Brand>

                {/* Toggle for menu */}
                <Navbar.Toggle aria-controls="offcanvasNavbar-expand-lg" />

                {/* Offcanvas menu */}
                <Navbar.Offcanvas
                    id="offcanvasNavbar-expand-lg"
                    aria-labelledby="offcanvasNavbarLabel-expand-lg"
                    placement="start"
                >
                    <Offcanvas.Header closeButton closeVariant="white">
                        <Offcanvas.Title id="offcanvasNavbarLabel-expand-lg" className="text-white">
                            Menu
                        </Offcanvas.Title>
                    </Offcanvas.Header>
                    <Offcanvas.Body className="bg-dark text-white">
                        <Nav className="flex-grow-1 pe-3">
                            <Nav.Link as={Link} to="/products" className="text-white">Products</Nav.Link>
                            <Nav.Link as={Link} to="/categories" className="text-white">Categories</Nav.Link>
                            <Nav.Link as={Link} to="/offers" className="text-white">Offers</Nav.Link>
                            <Nav.Link as={Link} to="/latest" className="text-white">Latest Launches</Nav.Link>
                        </Nav>
                    </Offcanvas.Body>
                </Navbar.Offcanvas>

                {/* Search bar in middle */}
                <div className="flex-grow-1 d-flex justify-content-center">
                    <Form className="d-flex w-100" style={{ maxWidth: '500px' }}>
                        <FormControl
                            type="search"
                            placeholder="What are you looking for?"
                            className="me-2 rounded-pill"
                            style={{ paddingLeft: '15px' }}
                        />
                        <Button variant="light" className="rounded-pill px-3">
                            <FaSearch />
                        </Button>
                    </Form>
                </div>

                {/* Right side icons */}
                <Nav className="d-flex align-items-center ms-3">
                    <NavDropdown
                        title={<FaUserCircle size={25} color="white"/>}
                        id="user-nav-dropdown"
                        align="end"
                    >
                        {user ? (
                            <>
                                <NavDropdown.Item as={Link} to="/profile">My Profile</NavDropdown.Item>
                                <NavDropdown.Item as={Link} to="/orders">My Orders</NavDropdown.Item>
                                <NavDropdown.Item>Wishlist</NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item onClick={handleLogout}>Logout</NavDropdown.Item>
                            </>
                        ) : (
                            <>
                                <NavDropdown.Item as={Link} to="/login">Login</NavDropdown.Item>
                                <NavDropdown.Item as={Link} to="/signup">Signup</NavDropdown.Item>
                            </>
                        )}
                    </NavDropdown>

                    <Nav.Link as={Link} to="/cart" className="ms-3">
                        <FaShoppingCart size={25} color="white"/>
                    </Nav.Link>
                </Nav>
            </Container>
        </Navbar>
    );
}
