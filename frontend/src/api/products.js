// src/api/products.js
import api from "./api";

// Get all products
export const getProducts = async () => {
    const response = await api.get("/products");
    return response.data;
};

// Create product
export const createProduct = async (product) => {
    const response = await api.post("/products", product);
    return response.data;
};

// Update product
export const updateProduct = async (id, product) => {
    const response = await api.put(`/products/${id}`, product);
    return response.data;
};

// Delete product
export const deleteProduct = async (id) => {
    await api.delete(`/products/${id}`);
};
