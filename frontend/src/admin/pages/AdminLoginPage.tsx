import { useState } from "react";
import { FiHome } from "react-icons/fi";
import { Link, useNavigate } from "react-router-dom";
import axiosInstance from "../../api/axiosInstance";

const AdminLoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      // 🧹 Clear any old/invalid token before login
      localStorage.removeItem("token");

      const res = await axiosInstance.post("/auth/login", {
        email,
        password,
      });

      // 🔐 Store JWT (single source of truth)
      localStorage.setItem("token", res.data.token);

      navigate("/admin/products");
    } catch (error) {
      alert("Invalid credentials");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col bg-gray-50 px-4">
      {/* 🔙 Back to Home */}
      <div className="py-4">
        <Link
          to="/"
          className="flex items-center gap-2 text-sm font-semibold text-green-700"
        >
          <FiHome />
          Back to Home
        </Link>
      </div>

      {/* Login Form */}
      <div className="flex flex-1 items-center justify-center">
        <form
          onSubmit={handleLogin}
          className="bg-white p-6 rounded-lg shadow max-w-sm w-full space-y-4"
        >
          <h1 className="text-xl font-semibold text-center">Admin Login</h1>

          <input
            type="email"
            placeholder="admin@test.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full border px-3 py-2 rounded"
            required
          />

          <input
            type="password"
            placeholder="admin123"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full border px-3 py-2 rounded"
            required
          />

          <button
            type="submit"
            disabled={loading}
            className="
              w-full bg-green-600 text-white py-2 rounded
              hover:bg-green-700 transition disabled:opacity-50
            "
          >
            {loading ? "Logging in..." : "Login"}
          </button>

          <p className="text-xs text-center text-gray-400">
            Demo credentials for portfolio only
          </p>
        </form>
      </div>
    </div>
  );
};

export default AdminLoginPage;
