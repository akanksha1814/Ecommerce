import { useState, useEffect } from 'react';
import { Container, Row, Col, Button, Card } from 'react-bootstrap';
import { useLocation, useParams, useNavigate } from 'react-router-dom';
import { productService } from '../api/productService';
import { cartService } from '../api/cartService';
import { useAuth } from '../context/AuthContext';
import LoadingSpinner from '../components/LoadingSpinner';

// CSS Styles as a string
// CSS Styles as a string - CORRECTED VERSION
const productsStyles = `
body {
    background-color: #000 !important;
    color: #fff !important;
}

.products-container {
    padding-bottom: 2rem;
    background-color: #000;
    color: #fff;
}

.category-section {
    margin-bottom: 3rem;
    text-align: center;
}

.category-title {
    color: #fff;
    font-weight: bold;
    margin-bottom: 1.5rem;
    padding-bottom: 0.5rem;
    font-size: 1.8rem;
    text-transform: uppercase;
    letter-spacing: 1px;
}

.scroll-container {
    display: flex;
    overflow-x: auto;
    gap: 1.5rem;
    padding: 1rem 0.5rem;
    scrollbar-width: none; /* Firefox */
    -ms-overflow-style: none; /* IE and Edge */
    justify-content: flex-start; /* Changed from center to flex-start */
}

.scroll-container::-webkit-scrollbar {
    display: none; /* Chrome, Safari and Opera */
}

.scroll-card {
    min-width: 280px;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
    cursor: pointer;
    background-color: #1a1a1a;
    border: 1px solid #333;
    color: #fff;
    border-radius: 8px;
    margin: 0 auto;
}

.scroll-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 20px rgba(255, 255, 255, 0.1);
}

.product-card {
    height: 100%;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
    cursor: pointer;
    border: 1px solid #333;
    border-radius: 10px;
    overflow: hidden;
    background-color: #1a1a1a;
    color: #fff;
    margin: 0 auto;
}

.product-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 20px rgba(255, 255, 255, 0.1);
}

.product-image-container {
    height: 200px;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #000;
    padding: 1rem;
}

.product-image {
    object-fit: contain;
    height: 100%;
    width: auto;
    max-width: 100%;
}

.card-body {
    background-color: #1a1a1a;
    color: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
}

.card-title {
    color: #fff;
    font-size: 1.1rem;
    margin-bottom: 0.5rem;
}

.text-muted {
    color: #aaa !important;
}

.card-price {
    font-size: 1.2rem;
    font-weight: bold;
    margin: 0.5rem 0;
}

.btn-add-to-cart {
    width: 100%;
    margin-top: 0.5rem;
    background-color: #333;
    border: none;
}

.btn-add-to-cart:hover {
    background-color: #444;
}

@media (max-width: 768px) {
    .scroll-card {
        min-width: 250px;
    }
    
    .category-title {
        font-size: 1.5rem;
    }
    
    /* Removed the justify-content: flex-start; from here as it's now in the base class */
}
`;

// Sample product data (same as before)
const sampleProducts = {
    mobiles: [
        { id: 1, name: 'iPhone 15 Pro', price: 129999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNZV9feoQNodsu0Cnb07yLZAfcaDZhBkHggF95swsVe1l5AdEcnw0CZeffueoY62AA69M&usqp=CAU', description: 'Latest iPhone with A17 Pro chip', category: 'Mobile' },
        { id: 2, name: 'Samsung Galaxy S23', price: 84999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQeaYtsUeMSAxTofh9Oc0JdslrGI4nl2Efmrw&s', description: 'Powerful Android flagship', category: 'Mobile' },
        { id: 3, name: 'Google Pixel 8', price: 75999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeuto4PdqCHyslN0O-8rP87v4vtLGR0fIIEg&s', description: 'Best camera phone', category: 'Mobile' },
        { id: 4, name: 'OnePlus 11', price: 56999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT24iEG8GboeDwt03j16GlbYfEC-eBNh49Wov2ETF261UeDLqn4GpYjURvJdJlFJyqAhoM&usqp=CAU', description: 'Fast charging flagship', category: 'Mobile' },
        { id: 5, name: 'Xiaomi 13 Pro', price: 79999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkeJJNMAzb0D71PcaPWeY3pdpAA4cBN8eJ25TIxrnk97DI4a7s2DJDzje4WjfEZIKSmUw&usqp=CAU', description: 'Excellent value flagship', category: 'Mobile' }
    ],
    laptops: [
        { id: 6, name: 'MacBook Pro 16"', price: 229999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSSe93S_LTHnRW0ff6OtknvzO2aheKisfsH9g&s', description: 'Powerful laptop for professionals', category: 'Laptop' },
        { id: 7, name: 'Dell XPS 15', price: 159999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS1xKUhmlvA_TBPyyxqr7PAdeYuPby3uI0s2Q&s', description: 'Premium Windows laptop', category: 'Laptop' },
        { id: 8, name: 'HP Spectre x360', price: 144999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTv24VuXU2ugAte5kKza9GPSVfYCEvWe0_u9g&s', description: 'Convertible premium laptop', category: 'Laptop' },
        { id: 9, name: 'Lenovo Yoga 9i', price: 134999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSz700w9BK_GorIJq0-m6lhadkHeetTQyiOmw&s', description: 'Versatile 2-in-1 laptop', category: 'Laptop' },
        { id: 10, name: 'Asus ROG Zephyrus', price: 189999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAAZwSNNwzAzPNPQlpbX-iiC14IsekhJotEA&s', description: 'Gaming powerhouse', category: 'Laptop' }
    ],
    headphones: [
        { id: 11, name: 'Sony WH-1000XM5', price: 28999, img: 'https://www.sony.co.in/image/6145c1d32e6ac8e63a46c912dc33c5bb?fmt=pjpeg&wid=330&bgcolor=FFFFFF&bgc=FFFFFF', description: 'Industry leading noise cancellation', category: 'Headphones' },
        { id: 12, name: 'AirPods Max', price: 59900, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRbemRbhwKJry3jx_tHCS-4uRRwdv5MGT7BVw&s', description: 'Premium Apple headphones', category: 'Headphones' },
        { id: 13, name: 'Bose QuietComfort 45', price: 28990, img: 'https://m.media-amazon.com/images/I/51HHABMPoVL._UF1000,1000_QL80_.jpg', description: 'Comfortable noise cancelling', category: 'Headphones' },
        { id: 14, name: 'Sennheiser Momentum 4', price: 31990, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTTBCETwg7QRdnb9hEzGzCi7-s1izhfjlxAyA&s', description: 'Superior sound quality', category: 'Headphones' },
        { id: 15, name: 'JBL Tour One M2', price: 24999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcx-CX-e_iZECs8n7IVIAvD9RsnPV3IXp0cA&s', description: 'Great battery life', category: 'Headphones' }
    ],
    televisions: [
        { id: 16, name: 'Samsung QLED 4K', price: 94999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsP6w5P0r_mcPCtCxxjDnxVGyKSGHk23hRpQ&s', description: 'Vibrant colors and deep blacks', category: 'Television' },
        { id: 17, name: 'LG OLED C3', price: 129999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSE9f34ns0CgU7CLcLfLxvj5kweoB_dg2HqPw&s', description: 'Great value TV', category: 'Television' },
        { id: 18, name: 'Sony Bravia XR', price: 159999, img: 'https://img-prd-pim.poorvika.com/cdn-cgi/image/width=500,height=500,quality=75/product/Sony-bravia-xr-4k-ultra-hd-smart-android-led-tv-x70l-43-inch-Front-View.png', description: 'Cognitive Processor XR', category: 'Television' },
        { id: 19, name: 'OnePlus Q2 Pro', price: 69999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSnJWE31xSE2A1HD9oycPUZGiFbQUNzVtmQw&s', description: 'Great value TV', category: 'Television' },
        { id: 20, name: 'Xiaomi QLED', price: 59999, img: 'https://i02.appmifile.com/122_operator_in/23/04/2021/1da9de5f68cb8a1108c3ac1dc3100255.jpg', description: 'Affordable premium TV', category: 'Television' }
    ],
    washingMachines: [
        { id: 21, name: 'Samsung Front Load', price: 44999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-JRu47O4HFZ8pwzaH21QEtvpfUnNsi50Z-g&s', description: 'EcoBubble technology', category: 'Washing Machine' },
        { id: 22, name: 'LG Inverter Wash', price: 38999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8udy_k-ct7V82WZ2vQEu84TYOJ7iRvRmGwA&s', description: 'Direct Drive motor', category: 'Washing Machine' },
        { id: 23, name: 'Whirlpool Supreme', price: 32999, img: 'https://darlingretail.com/cdn/shop/products/1_bab1c1b9-87f2-476d-b8e4-ca0e1433e841.jpg?v=1755761714', description: '6th Sense Technology', category: 'Washing Machine' },
        { id: 24, name: 'IFB Senator', price: 36999, img: 'https://ifbdm.ifbappliances.com/adobe/assets/urn:aaid:aem:39531b4b-162a-4726-a02a-1dadcb1e3002/as/8903287031847-.webp?quality=60', description: 'Aqua Energie feature', category: 'Washing Machine' },
        { id: 25, name: 'Bosch Serie 6', price: 47999, img: 'https://media.tatacroma.com/Croma%20Assets/Large%20Appliances/Dishwashers/Images/309172_0_zdlv1s.png', description: 'VarioDrum technology', category: 'Washing Machine' }
    ],
    accessories: [
        { id: 26, name: 'Apple Watch Ultra', price: 89900, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcREZm1_DCpggXektd_xca7IwoSoTuUOmQ_8ag&s', description: 'Rugged smartwatch', category: 'Accessory' },
        { id: 27, name: 'iPad Pro 12.9', price: 109999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTIPfTF971Nh2g38_0FmN5fZF3ii4lEvoxiiA&s', description: 'Powerful tablet', category: 'Accessory' },
        { id: 28, name: 'Samsung Galaxy Tab', price: 59999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ5Ol9iLYszCtcYIcDHF2EMeNs-E9gPSSnvTQ&s', description: 'Android tablet', category: 'Accessory' },
        { id: 29, name: 'Logitech MX Keys', price: 8999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQeGPnnZWgckW4O63evcDmflQ3mQspKJkByTA&s', description: 'Premium keyboard', category: 'Accessory' },
        { id: 30, name: 'Razer DeathAdder', price: 4999, img: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSVrNUPFVlaJ3YXmlSC1uLsKECr3x48np9zFw&s', description: 'Gaming mouse', category: 'Accessory' }
    ]
};

export default function Products() {
    const [products, setProducts] = useState([]);
    const [filteredProducts, setFilteredProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const { user } = useAuth();
    const location = useLocation();
    const { id } = useParams();
    const navigate = useNavigate();

    // Add CSS to the document head
    useEffect(() => {
        const styleElement = document.createElement('style');
        styleElement.textContent = productsStyles;
        document.head.appendChild(styleElement);

        // Set body background to black
        document.body.style.backgroundColor = '#000';
        document.body.style.color = '#fff';

        return () => {
            document.head.removeChild(styleElement);
            // Reset body styles when component unmounts
            document.body.style.backgroundColor = '';
            document.body.style.color = '';
        };
    }, []);

    useEffect(() => {
        const loadProducts = async () => {
            try {
                // In a real app, you would fetch from your API
                // const data = await productService.getAllProducts();

                // For demo, using sample data
                const allProducts = Object.values(sampleProducts).flat();
                setProducts(allProducts);

                // Check if we're filtering by category from URL query params
                const searchParams = new URLSearchParams(location.search);
                const category = searchParams.get('category');

                if (category) {
                    // Filter products by category
                    const filtered = allProducts.filter(p =>
                        p.category.toLowerCase() === category.toLowerCase()
                    );
                    setFilteredProducts(filtered);
                } else if (id) {
                    // Show single product if ID is in URL
                    const product = allProducts.find(p => p.id === parseInt(id));
                    setFilteredProducts(product ? [product] : []);
                } else {
                    // Show all products if no filter
                    setFilteredProducts(allProducts);
                }
            } catch (err) {
                console.error("Error loading products:", err);
            } finally {
                setLoading(false);
            }
        };
        loadProducts();
    }, [location.search, id]);

    // Add to cart function
    const addToCart = async (productId) => {
        if (!user) {
            alert("Please login to add items to cart");
            return;
        }
        try {
            await cartService.addToCart(user.id, productId, 1);
            alert("Product added to cart!");
        } catch (err) {
            console.error("Add to cart failed:", err.response?.data || err.message);
            alert("Failed to add product to cart");
        }
    };

    // Handle product click
    const handleProductClick = (productId) => {
        navigate(`/products/${productId}`);
    };

    if (loading) return <LoadingSpinner />;

    // If viewing a single product
    if (id && filteredProducts.length === 1) {
        const product = filteredProducts[0];
        return (
            <Container className="my-5">
                <Row>
                    <Col md={6}>
                        <img
                            src={product.img}
                            alt={product.name}
                            style={{ width: '100%', borderRadius: '10px' }}
                        />
                    </Col>
                    <Col md={6}>
                        <h2 style={{color: '#fff'}}>{product.name}</h2>
                        <p className="text-muted">{product.category}</p>
                        <h3 className="my-3" style={{color: '#fff'}}>₹{product.price.toLocaleString()}</h3>
                        <p style={{color: '#fff'}}>{product.description}</p>
                        <Button
                            variant="primary"
                            size="lg"
                            onClick={() => addToCart(product.id)}
                        >
                            Add to Cart
                        </Button>
                    </Col>
                </Row>
            </Container>
        );
    }

    // If filtering by category
    const searchParams = new URLSearchParams(location.search);
    const category = searchParams.get('category');

    if (category) {
        return (
            <Container>
                <h2 className="my-4" style={{color: '#fff', textAlign: 'center'}}>{category}</h2>
                <Row>
                    {filteredProducts.length > 0 ? (
                        filteredProducts.map(product => (
                            <Col key={product.id} md={4} className="mb-4">
                                <Card
                                    className="h-100 product-card"
                                    onClick={() => handleProductClick(product.id)}
                                >
                                    <div className="product-image-container">
                                        <Card.Img
                                            variant="top"
                                            src={product.img}
                                            className="product-image"
                                        />
                                    </div>
                                    <Card.Body className="d-flex flex-column">
                                        <Card.Title>{product.name}</Card.Title>
                                        <Card.Text className="text-muted">{product.description}</Card.Text>
                                        <div className="mt-auto text-center">
                                            <Card.Text className="card-price mb-2">₹{product.price.toLocaleString()}</Card.Text>
                                            <Button
                                                className="btn-add-to-cart"
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    addToCart(product.id);
                                                }}
                                            >
                                                Add to Cart
                                            </Button>
                                        </div>
                                    </Card.Body>
                                </Card>
                            </Col>
                        ))
                    ) : (
                        <p>No products found in this category</p>
                    )}
                </Row>
            </Container>
        );
    }

    // Default view - all products in horizontal scrolling categories
    return (
        <Container className="products-container">
            <h2 className="my-4" style={{color: '#fff', textAlign: 'center'}}>Our Products</h2>

            {/* Mobiles Section */}
            <div className="category-section">
                <h3 className="category-title">Mobiles</h3>
                <div className="scroll-container">
                    {sampleProducts.mobiles.map(product => (
                        <Card
                            key={product.id}
                            className="product-card scroll-card"
                            onClick={() => handleProductClick(product.id)}
                        >
                            <div className="product-image-container">
                                <Card.Img
                                    variant="top"
                                    src={product.img}
                                    className="product-image"
                                />
                            </div>
                            <Card.Body className="d-flex flex-column">
                                <Card.Title>{product.name}</Card.Title>
                                <Card.Text className="card-price">₹{product.price.toLocaleString()}</Card.Text>
                                <Button
                                    className="btn-add-to-cart"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        addToCart(product.id);
                                    }}
                                >
                                    Add to Cart
                                </Button>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </div>

            {/* Laptops Section */}
            <div className="category-section">
                <h3 className="category-title">Laptops</h3>
                <div className="scroll-container">
                    {sampleProducts.laptops.map(product => (
                        <Card
                            key={product.id}
                            className="product-card scroll-card"
                            onClick={() => handleProductClick(product.id)}
                        >
                            <div className="product-image-container">
                                <Card.Img
                                    variant="top"
                                    src={product.img}
                                    className="product-image"
                                />
                            </div>
                            <Card.Body className="d-flex flex-column">
                                <Card.Title>{product.name}</Card.Title>
                                <Card.Text className="card-price">₹{product.price.toLocaleString()}</Card.Text>
                                <Button
                                    className="btn-add-to-cart"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        addToCart(product.id);
                                    }}
                                >
                                    Add to Cart
                                </Button>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </div>

            {/* Headphones Section */}
            <div className="category-section">
                <h3 className="category-title">Headphones</h3>
                <div className="scroll-container">
                    {sampleProducts.headphones.map(product => (
                        <Card
                            key={product.id}
                            className="product-card scroll-card"
                            onClick={() => handleProductClick(product.id)}
                        >
                            <div className="product-image-container">
                                <Card.Img
                                    variant="top"
                                    src={product.img}
                                    className="product-image"
                                />
                            </div>
                            <Card.Body className="d-flex flex-column">
                                <Card.Title>{product.name}</Card.Title>
                                <Card.Text className="card-price">₹{product.price.toLocaleString()}</Card.Text>
                                <Button
                                    className="btn-add-to-cart"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        addToCart(product.id);
                                    }}
                                >
                                    Add to Cart
                                </Button>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </div>

            {/* Televisions Section */}
            <div className="category-section">
                <h3 className="category-title">Televisions</h3>
                <div className="scroll-container">
                    {sampleProducts.televisions.map(product => (
                        <Card
                            key={product.id}
                            className="product-card scroll-card"
                            onClick={() => handleProductClick(product.id)}
                        >
                            <div className="product-image-container">
                                <Card.Img
                                    variant="top"
                                    src={product.img}
                                    className="product-image"
                                />
                            </div>
                            <Card.Body className="d-flex flex-column">
                                <Card.Title>{product.name}</Card.Title>
                                <Card.Text className="card-price">₹{product.price.toLocaleString()}</Card.Text>
                                <Button
                                    className="btn-add-to-cart"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        addToCart(product.id);
                                    }}
                                >
                                    Add to Cart
                                </Button>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </div>

            {/* Washing Machines Section */}
            <div className="category-section">
                <h3 className="category-title">Washing Machines</h3>
                <div className="scroll-container">
                    {sampleProducts.washingMachines.map(product => (
                        <Card
                            key={product.id}
                            className="product-card scroll-card"
                            onClick={() => handleProductClick(product.id)}
                        >
                            <div className="product-image-container">
                                <Card.Img
                                    variant="top"
                                    src={product.img}
                                    className="product-image"
                                />
                            </div>
                            <Card.Body className="d-flex flex-column">
                                <Card.Title>{product.name}</Card.Title>
                                <Card.Text className="card-price">₹{product.price.toLocaleString()}</Card.Text>
                                <Button
                                    className="btn-add-to-cart"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        addToCart(product.id);
                                    }}
                                >
                                    Add to Cart
                                </Button>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </div>

            {/* Accessories Section */}
            <div className="category-section">
                <h3 className="category-title">Accessories</h3>
                <div className="scroll-container">
                    {sampleProducts.accessories.map(product => (
                        <Card
                            key={product.id}
                            className="product-card scroll-card"
                            onClick={() => handleProductClick(product.id)}
                        >
                            <div className="product-image-container">
                                <Card.Img
                                    variant="top"
                                    src={product.img}
                                    className="product-image"
                                />
                            </div>
                            <Card.Body className="d-flex flex-column">
                                <Card.Title>{product.name}</Card.Title>
                                <Card.Text className="card-price">₹{product.price.toLocaleString()}</Card.Text>
                                <Button
                                    className="btn-add-to-cart"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        addToCart(product.id);
                                    }}
                                >
                                    Add to Cart
                                </Button>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </div>
        </Container>
    );
}