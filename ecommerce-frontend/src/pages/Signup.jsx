import { useState } from 'react';
import { Container, Form, Button, Card, Alert } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

export default function Signup() {
    const [userData, setUserData] = useState({
        name: '',
        email: '',
        password: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const { signup } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setUserData({ ...userData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        const result = await signup(userData);

        if (result.success) {
            alert('Signup successful! Please login.');
            navigate('/login');
        } else {
            setError(result.message);
        }

        setLoading(false);
    };

    return (
        <Container className="mt-5">
            <Card className="mx-auto" style={{ maxWidth: '500px' }}>
                <Card.Body>
                    <h2 className="text-center mb-4">Sign Up</h2>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Name</Form.Label>
                            <Form.Control type="text" name="name" value={userData.name} onChange={handleChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" name="email" value={userData.email} onChange={handleChange} required />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" name="password" value={userData.password} onChange={handleChange} required />
                        </Form.Group>
                        <Button type="submit" variant="primary" className="w-100" disabled={loading}>
                            {loading ? 'Signing up...' : 'Sign Up'}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </Container>
    );
}
