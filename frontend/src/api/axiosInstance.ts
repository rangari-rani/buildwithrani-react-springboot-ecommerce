import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api",
});

//  REQUEST INTERCEPTOR
axiosInstance.interceptors.request.use(
  (config) => {
    if (
      config.url?.includes("/auth/login") ||
      config.url?.includes("/auth/register")
    ) {
      return config;
    }

    const token = localStorage.getItem("token");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

//  RESPONSE INTERCEPTOR
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;

    //  Concurrency conflict
    if (status === 409) {
      alert(error.response.data?.message || "Conflict occurred. Please retry.");
    }

    //  Unauthorized (token expired)
    if (status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/login";
    }

    //  Forbidden
    if (status === 403) {
      alert("You do not have permission to perform this action.");
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;