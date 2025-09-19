// src/components/FilterSort.jsx
import React, { useState } from 'react';

const FilterSort = ({ onFilterChange, onSortChange }) => {
  const [brand, setBrand] = useState('');
  const [sortOption, setSortOption] = useState('');

  const handleFilter = (e) => {
    const b = e.target.value;
    setBrand(b);
    onFilterChange(b);
  };

  const handleSort = (e) => {
    const s = e.target.value;
    setSortOption(s);
    onSortChange(s);
  };

  return (
    <div style={{ margin: '20px 0' }}>
      <select value={brand} onChange={handleFilter}>
        <option value="">All Brands</option>
        <option value="BrandA">BrandA</option>
        <option value="BrandB">BrandB</option>
        {/* add dynamically if you have the brands list */}
      </select>

      <select value={sortOption} onChange={handleSort} style={{ marginLeft: '20px' }}>
        <option value="">Sort By</option>
        <option value="priceAsc">Price: Low to High</option>
        <option value="priceDesc">Price: High to Low</option>
        <option value="discount">Discount</option>
      </select>
    </div>
  );
};

export default FilterSort;
