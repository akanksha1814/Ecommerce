import React, { useState, useEffect } from 'react';

// Your Google Client ID from the .env file
// Switched to process.env for broader compatibility (e.g., with Create React App)
//const GOOGLE_CLIENT_ID = import.meta.env.VITE_APP_GOOGLE_CLIENT_ID;
const GOOGLE_CLIENT_ID = "1065495912274-ogja54ifkasp4qeu53icbcd6l07cuvsm.apps.googleusercontent.com";

// The Redirect URI you configured in Google Cloud & your backend
const REDIRECT_URI = 'http://localhost:3001'; // Your React app's URL

const AuthPage = () => {
    // Attempt to get user data from localStorage on initial load
    const [token, setToken] = useState(localStorage.getItem('jwt'));
    const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')));
    const [error, setError] = useState(null);

    // This effect runs once when the component loads to handle the Google redirect
    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');
        console.log(code);

        // If a Google auth code is in the URL and we don't have a token yet,
        // exchange it for a JWT from our backend.
        if (code && !token) {
            exchangeCodeForJwt(code);
        }
    }, [token]); // The dependency array ensures this runs only when the token state changes

    const exchangeCodeForJwt = async (code) => {
        try {
            // Note: This relies on the "proxy" in package.json or CORS on the backend
            console.log("calling /auth/google")
            const response = await fetch('/auth/google', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ code }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                console.log(errorData.message + "error pakda gya");
                throw new Error(errorData.message || 'Failed to authenticate with backend.');
                // throw new Error('pakda gya error');

            }
            console.log("succesfully get the data from backend");

            const data = await response.json();
            
            // Store token and user info in localStorage for session persistence
            localStorage.setItem('jwt', data.jwt);
            const userData = { email: data.email, customerId: data.customerId };
            localStorage.setItem('user', JSON.stringify(userData));

            // Update component state to re-render the UI
            setToken(data.jwt);
            setUser(userData);
            setError(null); // Clear any previous errors

            // Clean the URL by removing the "code" parameter so it doesn't get resent
            window.history.replaceState({}, '', window.location.pathname);

        } catch (err) {
            console.log(err.message);
            setError(err.message);
        }
    };

    // Redirects the user to Google's consent screen
    const handleGoogleLogin = () => {
        const scope = 'https://www.googleapis.com/auth/userinfo.email';
        const authUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${GOOGLE_CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code&scope=${scope}&access_type=offline`;
        
        window.location.href = authUrl;
    };

    // Clears local storage and state to log the user out
    const handleLogout = () => {
        localStorage.removeItem('jwt');
        localStorage.removeItem('user');
        setToken(null);
        setUser(null);
    };
    
    // CSS styles are embedded directly to resolve file import issues.
    const Style = () => (
        <style>{`
            .auth-container {
                background-color: #f7fafc;
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            }
            .login-card {
                background-color: white;
                box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
                border-radius: 0.5rem;
                padding: 2rem;
                width: 100%;
                max-width: 28rem;
                text-align: center;
            }
            @media (min-width: 768px) {
                .login-card { padding: 3rem; }
            }
            .welcome-heading {
                font-size: 1.875rem;
                font-weight: 700;
                color: #2d3748;
                margin-bottom: 0.5rem;
            }
            .welcome-subheading {
                color: #718096;
                margin-bottom: 2rem;
            }
            .error-message {
                background-color: #fee2e2;
                color: #b91c1c;
                padding: 0.75rem;
                border-radius: 0.375rem;
                margin-bottom: 1.5rem;
                text-align: left;
            }
            .user-info-box {
                text-align: left;
                background-color: #f9fafb;
                padding: 1.5rem;
                border-radius: 0.5rem;
                border: 1px solid #e5e7eb;
            }
            .user-info-box h2 {
                font-size: 1.25rem;
                font-weight: 600;
                color: #374151;
            }
            .user-info-box p { color: #4b5563; word-break: break-all; }
            .user-info-box p:first-of-type { margin-top: 0.5rem; }
            .user-info-box p:last-of-type { margin-top: 0.25rem; }
            .btn {
                width: 100%;
                font-weight: 600;
                font-size: 1rem;
                padding: 0.75rem 1rem;
                border-radius: 0.5rem;
                transition: all 0.2s ease-in-out;
                cursor: pointer;
                border: none;
            }
            .google-btn {
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: white;
                border: 1px solid #d1d5db;
                color: #374151;
                box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
            }
            .google-btn:hover { background-color: #f9fafb; }
            .google-btn svg { width: 1.5rem; height: 1.5rem; margin-right: 0.75rem; }
            .logout-btn { margin-top: 1.5rem; background-color: #ef4444; color: white; }
            .logout-btn:hover { background-color: #dc2626; }
        `}</style>
    );

    // A simple component for the Google Icon SVG
    const GoogleIcon = () => (
        <svg viewBox="0 0 48 48">
            <path fill="#4285F4" d="M24 9.5c3.9 0 6.9 1.6 9 3.6l6.4-6.4C34.6 2.7 29.8 0 24 0 14.9 0 7.3 5.4 3 12.9l7.9 6.2C12.8 13.3 18 9.5 24 9.5z"></path>
            <path fill="#34A853" d="M46.2 25.4c0-1.7-.2-3.4-.5-5H24v9.5h12.5c-.5 3.1-2.2 5.7-4.8 7.5l7.9 6.2c4.6-4.2 7.3-10.4 7.3-17.7z"></path>
            <path fill="#FBBC05" d="M11 28.5c-.5-1.5-.8-3.1-.8-4.8s.3-3.3.8-4.8l-7.9-6.2C.9 16.4 0 20.1 0 24s.9 7.6 2.9 10.7l8.1-6.2z"></path>
            <path fill="#EA4335" d="M24 48c5.8 0 10.6-1.9 14.2-5.2l-7.9-6.2c-1.9 1.3-4.4 2-7.3 2-6 0-11.2-3.8-13.1-9.1l-7.9 6.2C7.3 42.6 14.9 48 24 48z"></path>
            <path fill="none" d="M0 0h48v48H0z"></path>
        </svg>
    );

    return (
        <div className="auth-container">
            <Style />
            <div className="login-card">
                <h1 className="welcome-heading">Welcome</h1>
                <p className="welcome-subheading">Sign in to manage your account</p>

                {error && <p className="error-message">Error: {error}</p>}
                
                {token && user ? (
                    <div className="user-info-box">
                        <h2>You are logged in as:</h2>
                        <p><strong>Email:</strong> {user.email}</p>
                        <p><strong>Customer ID:</strong> {user.customerId}</p>
                        <button 
                            onClick={handleLogout} 
                            className="btn logout-btn"
                        >
                            Log Out
                        </button>
                    </div>
                ) : (
                    <button 
                        onClick={handleGoogleLogin}
                        className="btn google-btn"
                    >
                        <GoogleIcon />
                        Sign in with Google
                    </button>
                )}
            </div>
        </div>
    );
};

export default AuthPage;

