// src/api/api.js
import axios from "axios";

// Base configuration for API
const api = axios.create({
    baseURL: "http://localhost:8080/api", // change if different
    headers: {
        "Content-Type": "application/json",
    },
});

export default api;
