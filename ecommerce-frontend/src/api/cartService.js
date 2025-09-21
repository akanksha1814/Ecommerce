import api from './api';

export const cartService = {
    getCart: async (customerId) => {
        const response = await api.get(`/carts/${customerId}`);
        return response.data;
    },

    addToCart: async (customerId, productId, quantity) => {
        const response = await api.post(`/carts/${customerId}/add/${productId}?quantity=${quantity}`);
        return response.data;
    },

    removeFromCart: async (customerId, productId) => {
        const response = await api.delete(`/carts/${customerId}/remove/${productId}`);
        return response.data;
    },

    clearCart: async (customerId) => {
        const response = await api.delete(`/carts/${customerId}/clear`);
        return response.data;
    },
};