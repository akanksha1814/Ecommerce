// src/components/CustomerList.jsx
import { useEffect, useState } from "react";
import { getCustomers, createCustomer, updateCustomer, deleteCustomer } from "../api/customers";

function CustomerList() {
    const [customers, setCustomers] = useState([]);
    const [form, setForm] = useState({ name: "", email: "" });
    const [editingId, setEditingId] = useState(null);

    useEffect(() => {
        fetchCustomers();
    }, []);

    const fetchCustomers = async () => {
        const data = await getCustomers();
        setCustomers(data);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (editingId) {
            await updateCustomer(editingId, form);
        } else {
            await createCustomer(form);
        }
        setForm({ name: "", email: "" });
        setEditingId(null);
        fetchCustomers();
    };

    const handleEdit = (customer) => {
        setForm({ name: customer.name, email: customer.email });
        setEditingId(customer.id);
    };

    const handleDelete = async (id) => {
        await deleteCustomer(id);
        fetchCustomers();
    };

    return (
        <div>
            <h2>Customers</h2>
            <form onSubmit={handleSubmit}>
                <input
                    placeholder="Name"
                    value={form.name}
                    onChange={(e) => setForm({ ...form, name: e.target.value })}
                    required
                />
                <input
                    placeholder="Email"
                    value={form.email}
                    onChange={(e) => setForm({ ...form, email: e.target.value })}
                    required
                />
                <button type="submit">{editingId ? "Update" : "Add"} Customer</button>
            </form>

            <ul>
                {customers.map((c) => (
                    <li key={c.id}>
                        {c.name} ({c.email}){" "}
                        <button onClick={() => handleEdit(c)}>Edit</button>
                        <button onClick={() => handleDelete(c.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default CustomerList;
