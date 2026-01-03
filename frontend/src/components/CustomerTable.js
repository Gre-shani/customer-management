// CustomerTable: displays list, provides client-side pagination and sorting.
import React from 'react';

// Controlled table component. Expects server-provided page data and callbacks.
export default function CustomerTable({
  customers = [],
  page = 0,
  size = 10,
  totalPages = 1,
  totalElements = 0,
  onPageChange,
  onSortChange,
  sort = { key: 'name', dir: 'asc' },
  onRowClick,
  loading = false,
  error = null
}) {
  // Helper to render sort indicator
  const renderSort = (key) => {
    if (!sort || sort.key !== key) return null;
    return sort.dir === 'asc' ? ' ▲' : ' ▼';
  };

  const gotoFirst = () => onPageChange && onPageChange(0);
  const gotoPrev = () => onPageChange && onPageChange(Math.max(0, page - 1));
  const gotoNext = () => onPageChange && onPageChange(Math.min(totalPages - 1, page + 1));
  const gotoLast = () => onPageChange && onPageChange(Math.max(0, totalPages - 1));

  return (
    <div className="table-container">
      {loading && <div className="loading">Loading...</div>}
      {error && <div className="msg error">{error}</div>}

      <div className="table-responsive">
        <table>
          <thead>
            <tr>
              <th onClick={() => onSortChange && onSortChange('id')}>ID{renderSort('id')}</th>
              <th onClick={() => onSortChange && onSortChange('name')}>Name{renderSort('name')}</th>
              <th onClick={() => onSortChange && onSortChange('dob')}>Date of Birth{renderSort('dob')}</th>
              <th>NIC</th>
              <th>Mobiles</th>
              <th>Families</th>
            </tr>
          </thead>
          <tbody>
            {customers.length === 0 && !loading && (
              <tr><td colSpan={6} style={{ textAlign: 'center' }}>No customers</td></tr>
            )}
            {customers.map(c => (
              <tr key={c.id || `${c.name}-${Math.random()}`} onClick={() => onRowClick && onRowClick(c)}>
                <td>{c.id}</td>
                <td>{c.name}</td>
                <td>{c.dob || ''}</td>
                <td>{c.nic || ''}</td>
                <td>{(c.mobiles || []).length}</td>
                <td>{(c.families || []).length}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="pagination">
        <button onClick={gotoFirst} disabled={page <= 0}>First</button>
        <button onClick={gotoPrev} disabled={page <= 0}>Prev</button>
        <span>Page {page + 1} / {totalPages}</span>
        <button onClick={gotoNext} disabled={page >= totalPages - 1}>Next</button>
        <button onClick={gotoLast} disabled={page >= totalPages - 1}>Last</button>
      </div>
    </div>
  );
}
