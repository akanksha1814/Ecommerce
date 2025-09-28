// src/components/ProductList.jsx
import { useEffect, useState } from "react";
import { getProducts, createProduct, updateProduct, deleteProduct } from "../api/products";

function ProductList() {
    const [products, setProducts] = useState([]);
    const [form, setForm] = useState({ name: "", price: "" });
    const [editingId, setEditingId] = useState(null);

    useEffect(() => {
        fetchProducts();
    }, []);

    const fetchProducts = async () => {
        const data = await getProducts();
        setProducts(data);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (editingId) {
            await updateProduct(editingId, form);
        } else {
            await createProduct(form);
        }
        setForm({ name: "", price: "" });
        setEditingId(null);
        fetchProducts();
    };

    const handleEdit = (product) => {
        setForm({ name: product.name, price: product.price });
        setEditingId(product.id);
    };

    const handleDelete = async (id) => {
        await deleteProduct(id);
        fetchProducts();
    };

    return (
        <div>
            <h2>Products</h2>
            <form onSubmit={handleSubmit}>
                <input
                    placeholder="Name"
                    value={form.name}
                    onChange={(e) => setForm({ ...form, name: e.target.value })}
                    required
                />
                <input
                    placeholder="Price"
                    value={form.price}
                    onChange={(e) => setForm({ ...form, price: e.target.value })}
                    required
                />
                <button type="submit">{editingId ? "Update" : "Add"} Product</button>
            </form>

            <ul>
                {products.map((p) => (
                    <li key={p.id}>
                        {p.name} - ${p.price}{" "}
                        <button onClick={() => handleEdit(p)}>Edit</button>
                        <button onClick={() => handleDelete(p.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ProductList;
