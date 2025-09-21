import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { cartService } from '../api/cartService';
import { useAuth } from '../context/AuthContext';
import LoadingSpinner from '../components/LoadingSpinner';

export default function Cart() {
    const { user } = useAuth();
    const [cart, setCart] = useState([]);
    const [loading, setLoading] = useState(true);

    // Load cart on mount
    useEffect(() => {
        if (!user) return;

        const loadCart = async () => {
            try {
                const data = await cartService.getCart(user.id);
                setCart(data.items || []); // assuming backend returns { items: [...] }
            } catch (err) {
                console.error("Error loading cart:", err);
            } finally {
                setLoading(false);
            }
        };

        loadCart();
    }, [user]);

    const removeItem = async (productId) => {
        try {
            await cartService.removeFromCart(user.id, productId);
            setCart((prev) => prev.filter(item => item.product.id !== productId));
        } catch (err) {
            console.error("Error removing item:", err);
        }
    };

    if (loading) return <LoadingSpinner />;

    return (
        <Container className="mt-4">
            <h2>My Cart</h2>
            {cart.length === 0 ? (
                <p>Your cart is empty</p>
            ) : (
                <Row>
                    {cart.map((item) => (
                        <Col key={item.product.id} md={4} className="mb-4">
                            <Card>
                                <Card.Img
                                    variant="top"
                                    src={item.product.imageUrl || 'https://via.placeholder.com/300x200'}
                                    style={{ height: '200px', objectFit: 'cover' }}
                                />
                                <Card.Body>
                                    <Card.Title>{item.product.name}</Card.Title>
                                    <Card.Text>Qty: {item.quantity}</Card.Text>
                                    <Card.Text className="fw-bold">₹{item.product.price}</Card.Text>
                                    <Button
                                        variant="danger"
                                        onClick={() => removeItem(item.product.id)}
                                    >
                                        Remove
                                    </Button>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            )}
        </Container>
    );
}
