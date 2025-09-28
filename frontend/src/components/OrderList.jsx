// src/components/OrderList.jsx
import { useEffect, useState } from "react";
import { getOrders, createOrder, updateOrder, deleteOrder } from "../api/orders";

function OrderList() {
    const [orders, setOrders] = useState([]);
    const [form, setForm] = useState({ productId: "", quantity: "" });
    const [editingId, setEditingId] = useState(null);

    useEffect(() => {
        fetchOrders();
    }, []);

    const fetchOrders = async () => {
        const data = await getOrders();
        setOrders(data);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (editingId) {
            await updateOrder(editingId, form);
        } else {
            await createOrder(form);
        }
        setForm({ productId: "", quantity: "" });
        setEditingId(null);
        fetchOrders();
    };

    const handleEdit = (order) => {
        setForm({ productId: order.productId, quantity: order.quantity });
        setEditingId(order.id);
    };

    const handleDelete = async (id) => {
        await deleteOrder(id);
        fetchOrders();
    };

    return (
        <div>
            <h2>Orders</h2>
            <form onSubmit={handleSubmit}>
                <input
                    placeholder="Product ID"
                    value={form.productId}
                    onChange={(e) => setForm({ ...form, productId: e.target.value })}
                    required
                />
                <input
                    placeholder="Quantity"
                    value={form.quantity}
                    onChange={(e) => setForm({ ...form, quantity: e.target.value })}
                    required
                />
                <button type="submit">{editingId ? "Update" : "Add"} Order</button>
            </form>

            <ul>
                {orders.map((o) => (
                    <li key={o.id}>
                        Product: {o.productId}, Qty: {o.quantity}{" "}
                        <button onClick={() => handleEdit(o)}>Edit</button>
                        <button onClick={() => handleDelete(o.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default OrderList;
