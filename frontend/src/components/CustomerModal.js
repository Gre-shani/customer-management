// CustomerModal: shows detailed information about a customer (mobiles, addresses, families)
import React from 'react';

export default function CustomerModal({ customer, onClose }) {
  if (!customer) return null;

  // helper renderer for lists
  const renderList = (title, list) => (
    <div className="section">
      <h4>{title}</h4>
      {(!list || list.length === 0) ? <div className="empty">(none)</div> : (
        <ul>
          {list.map((it, idx) => <li key={idx}>{JSON.stringify(it)}</li>)}
        </ul>
      )}
    </div>
  );

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div>
            <h2>{customer.name}</h2>
            <div className="col-muted">ID: {customer.id}</div>
          </div>
          <div>
            <button className="close" onClick={onClose}>Close</button>
          </div>
        </div>

        <div style={{ marginTop: 8 }}>
          <div><strong>DOB:</strong> {customer.dob}</div>
          <div><strong>NIC:</strong> {customer.nic}</div>
        </div>

        {renderList('Mobiles', customer.mobiles)}
        {renderList('Addresses', customer.addresses || customer.customerAddresses || [])}
        {renderList('Families', customer.families || [])}
      </div>
    </div>
  );
}
