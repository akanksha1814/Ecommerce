import { Container, Card } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';

export default function Profile() {
    const { user } = useAuth();

    return (
        <Container className="mt-4">
            <h2>My Profile</h2>
            <Card style={{ maxWidth: '500px' }}>
                <Card.Body>
                    <Card.Title>{user.name}</Card.Title>
                    <Card.Text>Email: {user.email}</Card.Text>
                </Card.Body>
            </Card>
        </Container>
    );
}
