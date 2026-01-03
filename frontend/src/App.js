// Main application component. Composes the CustomerTable and BulkUpload components.
import React, { useState, useEffect } from 'react';
import CustomerTable from './components/CustomerTable';
import BulkUpload from './components/BulkUpload';
import CustomerModal from './components/CustomerModal';
import api from './api';

export default function App() {
  // customers holds the list fetched from backend
  const [customers, setCustomers] = useState([]);
  // selected customer for modal
  const [selected, setSelected] = useState(null);
  // refresh toggle to refetch after bulk upload
  const [refreshToggle, setRefreshToggle] = useState(false);

  // pagination & sorting state (server-driven)
  const [page, setPage] = useState(0); // 0-based
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [sort, setSort] = useState({ key: 'name', dir: 'asc' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // fetch customers
  useEffect(() => {
    let cancelled = false;
    async function fetchCustomers() {
      setLoading(true);
      setError(null);
      try {
        // build sort param as 'field,dir'
        const sortParam = sort?.key ? `${sort.key},${sort.dir}` : undefined;
        const res = await api.getPage({ page, size, sort: sortParam });
        if (cancelled) return;
        const data = res?.data;
        // Spring Page<T> typically has: content, totalPages, totalElements, number
        let list = [];
        if (Array.isArray(data)) list = data;
        else if (data && Array.isArray(data.content)) list = data.content;
        else if (data && Array.isArray(data.customers)) list = data.customers;
        else list = data || [];

        setCustomers(list);
        setTotalPages((data && typeof data.totalPages === 'number') ? data.totalPages : Math.max(1, Math.ceil((data.totalElements || list.length) / size)));
        setTotalElements((data && typeof data.totalElements === 'number') ? data.totalElements : list.length);
      } catch (err) {
        console.error('Failed to fetch customers', err);
        setError('Failed to load customers');
        setCustomers([]);
        setTotalPages(1);
        setTotalElements(0);
      } finally {
        if (!cancelled) setLoading(false);
      }
    }
    fetchCustomers();
    return () => { cancelled = true; };
  }, [page, size, sort, refreshToggle]);

  // called after successful upload to refresh list
  function handleUploadSuccess() {
    // keep current page/sort and trigger refresh
    setRefreshToggle(t => !t);
  }

  function handlePageChange(newPage) {
    // newPage expected 0-based
    setPage(newPage);
  }

  function handleSortChange(key) {
    setPage(0); // reset to first page when sorting changes
    setSort(prev => ({ key, dir: prev.key === key && prev.dir === 'asc' ? 'desc' : 'asc' }));
  }

  return (
    <div className="app-container">
      <div className="app-header">
        <div>
          <h1>Customer Management</h1>
          <div className="brand-sub">Manage your customers — create, upload, and review</div>
        </div>
        <div>
          <button className="btn secondary" onClick={() => { setPage(0); setSize(10); setSort({ key: 'name', dir: 'asc' }); }}>Reset View</button>
        </div>
      </div>

      <div className="card">
        <BulkUpload onSuccess={handleUploadSuccess} />
      </div>

      <div className="card">
        <CustomerTable
          customers={customers}
          page={page}
          size={size}
          totalPages={totalPages}
          totalElements={totalElements}
          onPageChange={handlePageChange}
          onSortChange={handleSortChange}
          sort={sort}
          loading={loading}
          error={error}
          onRowClick={setSelected}
        />
      </div>

      {selected && (
        <CustomerModal customer={selected} onClose={() => setSelected(null)} />
      )}
    </div>
  );
}
