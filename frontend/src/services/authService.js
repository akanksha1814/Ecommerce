import axios from "axios";

const API_BASE = "http://localhost:8080";

export const getUser = () => {
  return axios.get(`${API_BASE}/auth/user`, { withCredentials: true });
};
