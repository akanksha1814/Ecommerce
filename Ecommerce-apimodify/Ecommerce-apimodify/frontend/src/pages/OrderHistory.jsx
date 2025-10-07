import React, { useEffect, useState } from 'react';
import api from '../api/api';

const OrderHistory = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    api.get('/orders')
      .then(res => setOrders(res.data))
      .catch(err => console.log("No orders found"));
  }, []);

  return (
    <div>
      <h2>Order History</h2>
      {orders.map(order => (
        <div key={order.id}>
          <p>Order ID: {order.id}</p>
          <p>Products: {order.productList}</p>
          <p>Total: ₹{order.totalAmount}</p>
          <p>Date: {new Date(order.orderDate).toLocaleDateString()}</p>
        </div>
      ))}
    </div>
  );
};

export default OrderHistory;
