// // import React, { useEffect, useState } from 'react';
// // import api from '../api/api';

// // const ProductList = () => {
// //   const [products, setProducts] = useState([]);

// //   useEffect(() => {
// //     api.get('/products').then((res) => setProducts(res.data));
// //   }, []);

// //   return (
// //     <div>
// //       <h2>All Products</h2>
// //       {products.map(p => (
// //         <div key={p.id}>
// //           <h4>{p.name}</h4>
// //           <p>{p.description}</p>
// //           <p>Price: ₹{p.price}</p>
// //         </div>
// //       ))}
// //     </div>
// //   );
// // };

// // export default ProductList;

// // src/pages/ProductList.jsx
// import React, { useState, useEffect } from 'react';
// import api from '../api/api';
// import ProductCard from '../components/ProductCard';
// import FilterSort from '../components/FilterSort';

// const ProductList = () => {
//   const [products, setProducts] = useState([]);
//   const [filterBrand, setFilterBrand] = useState('');
//   const [sortOption, setSortOption] = useState('');
//   const [page, setPage] = useState(1);
//   const [pageSize] = useState(10); // show 10 per page

//   useEffect(() => {
//     fetchProducts();
//   }, [filterBrand, sortOption, page]);

//   const fetchProducts = async () => {
//     let url = `/products?page=${page}&size=${pageSize}`;

//     if (filterBrand) url += `&brand=${filterBrand}`;
//     if (sortOption) url += `&sort=${sortOption}`;

//     const res = await api.get(url);
//     setProducts(res.data);  // adjust if backend wraps paging
//   };

//   const onFilterChange = (brand) => {
//     setFilterBrand(brand);
//     setPage(1); // reset page
//   };

//   const onSortChange = (sort) => {
//     setSortOption(sort);
//     setPage(1);
//   };

//   const nextPage = () => setPage(prev => prev + 1);
//   const prevPage = () => setPage(prev => Math.max(prev - 1, 1));

//   return (
//     <div>
//       <FilterSort onFilterChange={onFilterChange} onSortChange={onSortChange} />
//       <div style={{ display: 'flex', flexWrap: 'wrap', gap: '20px' }}>
//         {products.map(p => <ProductCard key={p.id} product={p} />)}
//       </div>
//       <div style={{ marginTop: '20px' }}>
//         <button onClick={prevPage} disabled={page === 1}>Previous</button>
//         <span style={{ margin: '0 10px' }}>Page {page}</span>
//         <button onClick={nextPage}>Next</button>
//       </div>
//     </div>
//   );
// };

// export default ProductList;

import React, { useEffect, useState } from 'react';
import ProductCard from '../components/ProductCard';

const ProductList = () => {
  const [products, setProducts] = useState([]);

  // Dummy data (until backend connection)
  useEffect(() => {
    setProducts([
      {
        id: 1,
        name: 'OnePlus Nord CE 3 Lite',
        price: 17999,
        originalPrice: 20999,
        imageUrl: 'https://m.media-amazon.com/images/I/41zSmQwV+1L._SX300_SY300_QL70_FMwebp_.jpg'
      },
      {
        id: 2,
        name: 'Samsung Galaxy M14 5G',
        price: 11490,
        originalPrice: 14990,
        imageUrl: 'https://m.media-amazon.com/images/I/81ZSn2rk9WL._SX679_.jpg'
      },
      {
        id: 3,
        name: 'boAt Airdopes 161 Earbuds',
        price: 999,
        originalPrice: 2490,
        imageUrl: 'https://m.media-amazon.com/images/I/61oT6fDdLZL._SX679_.jpg'
      },
      {
        id: 4,
        name: 'Apple MacBook Air M1',
        price: 69999,
        originalPrice: 99900,
        imageUrl: 'https://m.media-amazon.com/images/I/71vFKBpKakL._SX679_.jpg'
      }
    ]);
  }, []);

  return (
    <div>
      <h2 style={{ marginBottom: '20px' }}>Latest Products</h2>
      <div style={{ display: 'flex', flexWrap: 'wrap', gap: '20px' }}>
        {products.map(product => (
          <ProductCard key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
};

export default ProductList;
