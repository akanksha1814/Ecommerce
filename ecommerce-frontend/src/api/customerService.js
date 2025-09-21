import api from './api';

export const customerService = {
    signup: async (customerData) => {
        const response = await api.post('/customers/signup', customerData);
        return response.data;
    },

    login: async (loginData) => {
        const response = await api.post('/customers/login', loginData);
        return response.data;
    },

    getCustomer: async (id) => {
        const response = await api.get(`/customers/${id}`);
        return response.data;
    },

    updateCustomer: async (id, customerData) => {
        const response = await api.put(`/customers/${id}`, customerData);
        return response.data;
    },

    getAllCustomers: async () => {
        const response = await api.get('/customers');
        return response.data;
    }
};
