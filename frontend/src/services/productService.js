import axios from 'axios';

const API_BASE = 'http://localhost:8080';

export async function getAllProducts(token) {
  const resp = await axios.get(`${API_BASE}/api/products`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return resp.data;
}

export async function createProduct(product, token) {
  const resp = await axios.post(`${API_BASE}/api/products`, product, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return resp.data;
}

export async function updateProduct(id, product, token) {
  const resp = await axios.put(`${API_BASE}/api/products/${id}`, product, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return resp.data;
}

export async function getProductById(id, token) {
  const resp = await axios.get(`${API_BASE}/api/products/${id}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return resp.data;
}
