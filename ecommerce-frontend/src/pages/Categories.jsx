import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Spinner } from 'react-bootstrap';
import { categoryService } from '../api/categoryService';
import LoadingSpinner from '../components/LoadingSpinner';

export default function Categories() {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadCategories = async () => {
            try {
                const data = await categoryService.getAllCategories();
                setCategories(data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        loadCategories();
    }, []);

    if (loading) return <LoadingSpinner />;

    return (
        <Container className="mt-4">
            <h2 className="mb-4">Categories</h2>
            <Row>
                {categories.map((category) => (
                    <Col key={category.id} md={4} className="mb-4">
                        <Card>
                            <Card.Body>
                                <Card.Title>{category.name}</Card.Title>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>
        </Container>
    );
}
