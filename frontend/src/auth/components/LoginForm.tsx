import { FiEye, FiEyeOff } from "react-icons/fi";
import { usePasswordToggle } from "../hooks/usePasswordToggle";
import { useState } from "react";
import { loginApi } from "../services/authApi";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import toast from "react-hot-toast";

export default function LoginForm() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [email, setEmail] = useState("");
  const [passwordValue, setPasswordValue] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { type, visible, toggle } = usePasswordToggle();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await loginApi({
        email,
        password: passwordValue,
      });

      const { success, message, token, email: userEmail } = response;

      if (!success) {
        setError(message);
        return;
      }

      // ✅ SINGLE SOURCE OF TRUTH
      login(token, userEmail);

      toast.success("Login successful 👋");
      navigate("/admin/products");
    } catch {
      toast.error("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="space-y-4" onSubmit={handleSubmit}>
      {/* Email */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Email
        </label>
        <input
          type="email"
          placeholder="you@example.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
          required
        />
      </div>

      {/* Password */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Password
        </label>

        <div className="relative">
          <input
            type={type}
            placeholder="••••••••"
            value={passwordValue}
            onChange={(e) => setPasswordValue(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none pr-10"
            required
          />

          <button
            type="button"
            onClick={toggle}
            className="absolute inset-y-0 right-3 flex items-center text-gray-500"
          >
            {visible ? <FiEyeOff size={18} /> : <FiEye size={18} />}
          </button>
        </div>
      </div>

      {/* Error */}
      {error && <p className="text-sm text-red-600">{error}</p>}

      {/* Submit */}
      <button
        type="submit"
        disabled={loading}
        className="w-full bg-green-600 text-white py-2.5 rounded-lg font-medium hover:bg-green-700 transition disabled:opacity-60"
      >
        {loading ? "Logging in..." : "Login"}
      </button>
    </form>
  );
}
