import api from "./api";

export const login = (data: {
  email: string;
  password: string;
}) => api.post("/api/auth/login", data);

export const signup = (data: {
  fullName: string;
  email: string;
  password: string;
}) => api.post("/api/auth/signup", data);

