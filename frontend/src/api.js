// Simple API helper using axios.
// It points to the Spring Boot backend at http://localhost:8081/api/customers
import axios from 'axios';

const BASE = 'http://localhost:8081/api/customers';

export default {
  // GET /api/customers
  getAll() {
    return axios.get(BASE);
  },
  // GET pageable endpoint: accepts page (0-based), size, sort (e.g. name,asc)
  getPage(params) {
    // params: { page, size, sort } where sort can be 'field,asc' or array
    return axios.get(BASE, { params });
  },
  // POST /api/customers/bulk/upload - expects form-data with file
  uploadBulk(formData) {
    return axios.post(`${BASE}/bulk/upload`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  }
};
