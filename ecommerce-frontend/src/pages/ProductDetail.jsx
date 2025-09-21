import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Row, Col, Image, Button, Spinner } from 'react-bootstrap';
import { productService } from '../api/productService';
import { cartService } from '../api/cartService';
import { useAuth } from '../context/AuthContext';
import LoadingSpinner from '../components/LoadingSpinner';

export default function ProductDetail() {
    const { id } = useParams();
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const { user } = useAuth();

    useEffect(() => {
        const loadProduct = async () => {
            try {
                const data = await productService.getProductById(id);
                setProduct(data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        loadProduct();
    }, [id]);

    const addToCart = async () => {
        if (!user) return alert('Please login to add items to cart');
        try {
            await cartService.addToCart(user.id, id, 1);
            alert('Product added to cart!');
        } catch (err) {
            console.error(err);
            alert('Failed to add product');
        }
    };

    if (loading) return <LoadingSpinner />;
    if (!product) return <Container className="mt-5">Product not found</Container>;

    return (
        <Container className="mt-4">
            <Row>
                <Col md={6}>
                    <Image src={product.imageUrl || 'https://via.placeholder.com/400x300'} fluid />
                </Col>
                <Col md={6}>
                    <h2>{product.name}</h2>
                    <p>{product.description}</p>
                    <h4 className="text-primary">₹{product.price}</h4>
                    <Button variant="primary" onClick={addToCart}>Add to Cart</Button>
                </Col>
            </Row>
        </Container>
    );
}
