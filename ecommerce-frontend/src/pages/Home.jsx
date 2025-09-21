import { useState, useRef, useEffect } from 'react';
import { Container, Card, Carousel } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

// Dummy Data
const slides = [
    'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2025/Sanity/HP/Sep/21092025/HP_Rotating_Mac_21Sep2025_wJcNKE2DR.jpg?updatedAt=1758432535326',
    'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2025/HP%20Rotating%20Banners/Sept/21092025/Desktop/HP_Rotating_OneplusNeckbands_21Sep2025_HNz1i6bMs.jpg?updatedAt=1758368755984',
    'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2025/Sanity/HP/Sep/21092025/HP_Rotating_Watch_21Sep2025_-CzQNoEfj.jpg?updatedAt=1758432524590'
];

const categories = [
    { name: 'Mobiles', img: 'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2024/HP%20Category%20Ic0on/Homepage%20Cat%20Icons-Desktop/Mobile_sdtrdf.png?tr=w-480', id: 1 },
    { name: 'TVs', img: 'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2024/HP%20Category%20Ic0on/Homepage%20Cat%20Icons-Desktop/TV_vdemgc.png?tr=w-480', id: 2 },
    { name: 'Headphones', img: 'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2024/HP%20Category%20Ic0on/Homepage%20Cat%20Icons-Desktop/Head_set_xjj934.png?tr=w-480', id: 3 },
    { name: 'Laptops', img: 'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2024/HP%20Category%20Ic0on/Homepage%20Cat%20Icons-Desktop/Laptops_pzewpv.png?tr=w-480', id: 4 },
    { name: 'Accessories', img: 'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2024/HP%20Category%20Ic0on/Homepage%20Cat%20Icons-Desktop/Accessories_kefony.png?tr=w-480', id: 5 },
    { name: 'Washing Machines', img: 'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2024/HP%20Category%20Ic0on/Homepage%20Cat%20Icons-Desktop/Washing_machines_izyrnd.png?tr=w-480', id: 6 },
    { name: 'Gadgets', img: 'https://media-ik.croma.com/prod/https://media.tatacroma.com/Croma%20Assets/CMS/LP%20Page%20Banners/2024/HP%20Category%20Ic0on/Homepage%20Cat%20Icons-Desktop/Kitchen_Appliances_yhzevo.png?tr=w-480', id: 7 }
];

const products = [
    { id: 1, name: 'Smartphone XYZ', price: 29999, img: 'https://images-cdn.ubuy.co.in/688e8abbd8891194fe08ec1b-clearance-smart-appliances-7-3-inch.jpg', description: 'Latest Smartphone with amazing features.' },
    { id: 2, name: 'Laptop ABC', price: 49999, img: 'https://benstore.com.ph/34276-medium_default/microsoft-surface-laptop-15-touch-snapdragon-x-elite-integrated-qualcomm-adreno-16gb-lpddr5x-1tb-ssd-.jpg', description: 'High performance laptop for work and gaming.' },
    { id: 3, name: 'Wireless Headphones', price: 2999, img: 'https://m.media-amazon.com/images/I/61mOR017+fL._UF894,1000_QL80_.jpg', description: 'Noise-cancelling, wireless and stylish.' },
    { id: 4, name: 'Smartwatch', price: 9999, img: 'https://m.media-amazon.com/images/I/61pIzNaNRWL._UF1000,1000_QL80_.jpg', description: 'Track your fitness and notifications.' },
    { id: 5, name: 'Camera', price: 24999, img: 'https://m.media-amazon.com/images/I/81WtQ64-SOL._UF1000,1000_QL80_.jpg', description: 'Capture your moments in high quality.' }
];

export default function Home() {
    const [index, setIndex] = useState(0);
    const navigate = useNavigate();
    const { user } = useAuth();

    const handleSelect = (selectedIndex) => setIndex(selectedIndex);

    // Refs for drag scroll
    const categoryRef = useRef(null);
    const latestRef = useRef(null);
    const offersRef = useRef(null);

    useEffect(() => {
        const handleDragScroll = (ref) => {
            let isDown = false;
            let startX;
            let scrollLeft;

            const slider = ref.current;
            if (!slider) return;

            const mouseDown = (e) => {
                isDown = true;
                slider.classList.add('active');
                startX = e.pageX - slider.offsetLeft;
                scrollLeft = slider.scrollLeft;
            };

            const mouseLeave = () => (isDown = false);
            const mouseUp = () => (isDown = false);
            const mouseMove = (e) => {
                if (!isDown) return;
                e.preventDefault();
                const x = e.pageX - slider.offsetLeft;
                const walk = (x - startX) * 2; // scroll-fast
                slider.scrollLeft = scrollLeft - walk;
            };

            slider.addEventListener('mousedown', mouseDown);
            slider.addEventListener('mouseleave', mouseLeave);
            slider.addEventListener('mouseup', mouseUp);
            slider.addEventListener('mousemove', mouseMove);

            // Cleanup
            return () => {
                slider.removeEventListener('mousedown', mouseDown);
                slider.removeEventListener('mouseleave', mouseLeave);
                slider.removeEventListener('mouseup', mouseUp);
                slider.removeEventListener('mousemove', mouseMove);
            };
        };

        if (categoryRef.current) handleDragScroll(categoryRef);
        if (latestRef.current) handleDragScroll(latestRef);
        if (offersRef.current) handleDragScroll(offersRef);
    }, []);

    // Function to navigate to product details page
    const handleProductClick = (productId) => {
        navigate(`/products/${productId}`);
    };

    return (
        <div style={{ backgroundColor: '#000', color: 'white' }}>
            {/* Hero Carousel - Reduced gap with navbar */}
            <Carousel
                activeIndex={index}
                onSelect={handleSelect}
                interval={3000}
                fade
                className="m-0 p-0"
                style={{ marginTop: '-1px' }}
            >
                {slides.map((slide, idx) => (
                    <Carousel.Item key={idx}>
                        <img
                            className="d-block w-100"
                            src={slide}
                            alt={`Slide ${idx + 1}`}
                            style={{ height: '450px', objectFit: 'cover' }}
                        />
                    </Carousel.Item>
                ))}
            </Carousel>

            {/* Categories - Removed heading and centered */}
            <Container fluid className="my-5 px-4">
                <div
                    ref={categoryRef}
                    style={{
                        display: 'flex',
                        overflowX: 'auto',
                        cursor: 'grab',
                        gap: '1rem',
                        paddingBottom: '10px',
                        justifyContent: 'center',
                        scrollbarWidth: 'none', // Hide scrollbar for Firefox
                        msOverflowStyle: 'none', // Hide scrollbar for IE/Edge
                    }}
                    className="hide-scrollbar" // Custom class for Webkit browsers
                >
                    {categories.map((cat) => (
                        <Card
                            key={cat.id}
                            style={{
                                backgroundColor: 'black',
                                color: 'white',
                                minWidth: '150px',
                                cursor: 'pointer',
                                border: 'none',
                                borderRadius: '5px'
                            }}
                            className="text-center p-2 flex-shrink-0 category-card"
                            onClick={() => navigate(`/products?category=${cat.name}`)}
                        >
                            <Card.Img
                                variant="top"
                                src={cat.img}
                                style={{
                                    height: '100px',
                                    width: '100px', // Fixed width to make it square
                                    objectFit: '', // Changed to cover to fill the space
                                    padding: '5px',
                                    backgroundColor: 'black',
                                    borderRadius: '0px',
                                    margin: '0 auto' // Center the image
                                }}
                            />
                            <Card.Body className="p-2">
                                <Card.Title style={{ fontSize: '0.9rem', margin: 0 }}>{cat.name}</Card.Title>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </Container>

            {/* Latest Launches - Centered with white heading */}
            <Container fluid className="my-5 px-4">
                <div className="text-center mb-4">
                    <h3 className="mb-0" style={{
                        color: 'white',
                        fontWeight: 'bold',
                        fontSize: '1.8rem',
                        textTransform: 'uppercase',
                        letterSpacing: '1px'
                    }}>
                        Latest Launches
                    </h3>
                    <div style={{
                        height: '3px',
                        width: '60px',
                        backgroundColor: 'white',
                        margin: '10px auto 0',
                        borderRadius: '2px'
                    }}></div>
                </div>
                <div
                    ref={latestRef}
                    style={{
                        display: 'flex',
                        overflowX: 'auto',
                        cursor: 'grab',
                        gap: '1rem',
                        paddingBottom: '10px',
                        scrollbarWidth: 'none',
                        msOverflowStyle: 'none',
                        justifyContent: 'center'
                    }}
                    className="hide-scrollbar"
                >
                    {products.slice(0, 3).map((prod) => (
                        <Card
                            key={prod.id}
                            style={{
                                backgroundColor: '#222',
                                color: 'white',
                                width: '280px',
                                flexShrink: 0,
                                border: 'none',
                                borderRadius: '12px',
                                cursor: 'pointer',
                                overflow: 'hidden'
                            }}
                            onClick={() => handleProductClick(prod.id)}
                        >
                            <div style={{
                                height: '200px',
                                overflow: 'hidden',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                backgroundColor: 'white'
                            }}>
                                <img
                                    src={prod.img}
                                    alt={prod.name}
                                    style={{
                                        width: '100%',
                                        height: '100%',
                                        objectFit: 'contain',
                                        padding: '10px'
                                    }}
                                />
                            </div>
                            <Card.Body className="d-flex flex-column">
                                <Card.Title className="mb-1">{prod.name}</Card.Title>
                                <Card.Text className="mb-2 fw-bold">₹{prod.price.toLocaleString()}</Card.Text>
                                <Card.Text className="mb-2 small" style={{ minHeight: '40px' }}>{prod.description}</Card.Text>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </Container>

            {/* Exciting Offers - Centered with white heading */}
            <Container fluid className="my-5 px-4">
                <div className="text-center mb-4">
                    <h3 className="mb-0" style={{
                        color: 'white',
                        fontWeight: 'bold',
                        fontSize: '1.8rem',
                        textTransform: 'uppercase',
                        letterSpacing: '1px'
                    }}>
                        Exciting Offers
                    </h3>
                    <div style={{
                        height: '3px',
                        width: '60px',
                        backgroundColor: 'white',
                        margin: '10px auto 0',
                        borderRadius: '2px'
                    }}></div>
                </div>
                <div
                    ref={offersRef}
                    style={{
                        display: 'flex',
                        overflowX: 'auto',
                        cursor: 'grab',
                        gap: '1rem',
                        paddingBottom: '10px',
                        scrollbarWidth: 'none',
                        msOverflowStyle: 'none',
                        justifyContent: 'center'
                    }}
                    className="hide-scrollbar"
                >
                    {products.map((prod) => (
                        <Card
                            key={prod.id}
                            style={{
                                backgroundColor: '#222',
                                color: 'white',
                                width: '280px',
                                flexShrink: 0,
                                border: 'none',
                                borderRadius: '12px',
                                cursor: 'pointer',
                                overflow: 'hidden'
                            }}
                            onClick={() => handleProductClick(prod.id)}
                        >
                            <div style={{
                                height: '200px',
                                overflow: 'hidden',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                backgroundColor: 'white'
                            }}>
                                <img
                                    src={prod.img}
                                    alt={prod.name}
                                    style={{
                                        width: '100%',
                                        height: '100%',
                                        objectFit: 'contain',
                                        padding: '10px'
                                    }}
                                />
                            </div>
                            <Card.Body className="d-flex flex-column">
                                <Card.Title className="mb-1">{prod.name}</Card.Title>
                                <Card.Text className="mb-2 fw-bold">₹{prod.price.toLocaleString()}</Card.Text>
                                <Card.Text className="mb-2 small" style={{ minHeight: '40px' }}>{prod.description}</Card.Text>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </Container>

            {/* CSS for hiding scrollbar in Webkit browsers */}
            <style>
                {`
                    .hide-scrollbar::-webkit-scrollbar {
                        display: none;
                    }
                    .category-card:hover {
                        transform: translateY(-5px);
                        transition: transform 0.2s ease;
                    }
                    .card:hover {
                        transform: scale(1.02);
                        transition: transform 0.2s ease;
                    }
                `}
            </style>
        </div>
    );
}