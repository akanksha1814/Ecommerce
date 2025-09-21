import { useState, useEffect } from 'react';
import { Container, Card, Row, Col } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';
import { orderService } from '../api/orderService';
import LoadingSpinner from '../components/LoadingSpinner';

export default function Orders() {
    const { user } = useAuth();
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadOrders = async () => {
            try {
                const data = await orderService.getOrdersByCustomer(user.id);
                setOrders(data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        loadOrders();
    }, [user]);

    if (loading) return <LoadingSpinner />;

    return (
        <Container className="mt-4">
            <h2>My Orders</h2>
            {orders.length === 0 ? (
                <p>No orders yet</p>
            ) : (
                <Row>
                    {orders.map((order) => (
                        <Col key={order.id} md={4} className="mb-4">
                            <Card>
                                <Card.Body>
                                    <Card.Title>Order #{order.id}</Card.Title>
                                    <Card.Text>Total: ₹{order.total}</Card.Text>
                                    <Card.Text>Status: {order.status}</Card.Text>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            )}
        </Container>
    );
}
