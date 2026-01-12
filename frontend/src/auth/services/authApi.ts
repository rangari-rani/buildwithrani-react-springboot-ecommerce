import api from "./api";

export const loginApi = async (data: {
  email: string;
  password: string;
}) => {
  const response = await api.post("/api/auth/login", data);
  return response.data; // { success, email, token }
};

export const signupApi = async (data: {
  fullName: string;
  email: string;
  password: string;
}) => {
  const response = await api.post("/api/auth/signup", data);
  return response.data;
};
