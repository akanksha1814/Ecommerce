import React, { useState } from 'react';
import api from '../api/api';

const AddProduct = () => {
  const [product, setProduct] = useState({ name: '', price: '', description: '', quantity: '' });

  const handleSubmit = async (e) => {
    e.preventDefault();
    await api.post('/products', product);
    alert('Product added');
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Add Product</h2>
      <input placeholder="Name" onChange={(e) => setProduct({ ...product, name: e.target.value })} />
      <input placeholder="Price" onChange={(e) => setProduct({ ...product, price: e.target.value })} />
      <input placeholder="Description" onChange={(e) => setProduct({ ...product, description: e.target.value })} />
      <input placeholder="Quantity" onChange={(e) => setProduct({ ...product, quantity: e.target.value })} />
      <button type="submit">Add</button>
    </form>
  );
};

export default AddProduct;
