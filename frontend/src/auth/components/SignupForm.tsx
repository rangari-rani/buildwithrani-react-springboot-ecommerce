import { FiEye, FiEyeOff } from "react-icons/fi";
import { usePasswordToggle } from "../hooks/usePasswordToggle";
import { useState } from "react";
import { signupApi } from "../services/authApi";
import { useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import toast from "react-hot-toast";

export default function SignupForm() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [passwordValue, setPasswordValue] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
const location = useLocation();
const redirectTo =
  (location.state as { redirectTo?: string })?.redirectTo || "/";
  const password = usePasswordToggle();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await signupApi({
        fullName,
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

      toast.success("Signup successful 🎉");
      navigate(redirectTo, { replace: true });

    } catch {
      toast.error("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className="space-y-4" onSubmit={handleSubmit}>
      {/* Full Name */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Full Name
        </label>
        <input
          type="text"
          placeholder="John Doe"
          value={fullName}
          onChange={(e) => setFullName(e.target.value)}
          className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
          required
        />
      </div>

      {/* Email */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Email
        </label>
        <input
          type="email"
          placeholder="user@demo.com"
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
            type={password.type}
            placeholder="Password123!"
            value={passwordValue}
            onChange={(e) => setPasswordValue(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none pr-10"
            required
          />

          <button
            type="button"
            onClick={password.toggle}
            className="absolute inset-y-0 right-3 flex items-center text-gray-500"
          >
            {password.visible ? <FiEyeOff size={18} /> : <FiEye size={18} />}
          </button>
        </div>
      </div>

      {error && <p className="text-sm text-red-600">{error}</p>}

      {/* Submit */}
      <button
        type="submit"
        disabled={loading}
        className="w-full bg-green-600 text-white py-2.5 rounded-lg font-medium hover:bg-green-700 transition disabled:opacity-60"
      >
        {loading ? "Creating account..." : "Create Account"}
      </button>
    </form>
  );
}
