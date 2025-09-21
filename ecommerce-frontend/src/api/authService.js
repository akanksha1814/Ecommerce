import api from './api';

export const authService = {
    login: async (credentials) => {
        const response = await api.post('/customers/login', credentials);
        return response.data;
    },

    signup: async (userData) => {
        const response = await api.post('/customers/signup', userData);
        return response.data;
    },

    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },
};