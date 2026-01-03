// BulkUpload: simple file input and submit to POST /api/customers/bulk/upload
import React, { useState } from 'react';
import api from '../api';

export default function BulkUpload({ onSuccess }) {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    if (!file) {
      setMessage({ type: 'error', text: 'Please select a file.' });
      return;
    }

    const form = new FormData();
    form.append('file', file);
    try {
      const res = await api.uploadBulk(form);
      setMessage({ type: 'success', text: res.data?.message || 'Upload successful' });
      setFile(null);
      if (onSuccess) onSuccess();
    } catch (err) {
      const text = err?.response?.data?.message || err.message || 'Upload failed';
      setMessage({ type: 'error', text });
    }
  }

  return (
    <div className="upload">
      <form onSubmit={handleSubmit}>
        <label style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
          <span className="col-muted">Bulk Upload</span>
          <input type="file" accept=".xls,.xlsx" onChange={e => setFile(e.target.files[0])} />
        </label>
        <div style={{ marginLeft: 'auto' }}>
          <button className="btn" type="submit">Upload</button>
        </div>
      </form>
      {message && <div className={`msg ${message.type}`}>{message.text}</div>}
    </div>
  );
}
