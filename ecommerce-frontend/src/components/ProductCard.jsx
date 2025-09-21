import { Card, Button } from 'react-bootstrap';

export default function ProductCard({ product, onAddToCart }) {
    return (
        <Card className="h-100">
            <Card.Img
                variant="top"
                src={product.imageUrl || 'https://via.placeholder.com/300x200'}
                style={{ height: '200px', objectFit: 'cover' }}
            />
            <Card.Body className="d-flex flex-column">
                <Card.Title>{product.name}</Card.Title>
                <Card.Text>{product.description}</Card.Text>
                <Card.Text className="fw-bold">₹{product.price}</Card.Text>
                <Button
                    variant="primary"
                    className="mt-auto"
                    onClick={() => onAddToCart(product.id)}
                >
                    Add to Cart
                </Button>
            </Card.Body>
        </Card>
    );
}
