import { Routes, Route, Link } from "react-router-dom";
import LoginPage from "./pages/auth/login";
import SignupPage from "./pages/auth/signup";
import Footer from "./components/Footer";

export default function App() {
  return (
    <div className="min-h-screen bg-white flex flex-col">

      {/* ==== NAVBAR ==== */}
      <nav className="w-full border-b border-teal-200 bg-white py-4 px-6 flex items-center justify-between">
        {/* Logo / Brand */}
        <Link to="/" className="text-xl font-semibold text-teal-700">
          Wellness Cart
        </Link>

        {/* Right buttons */}
        <div className="flex items-center gap-3">
          <Link
            to="/login"
            className="px-4 py-2 rounded-lg border border-teal-500 text-teal-600 font-medium hover:bg-teal-50 transition"
          >
            Login
          </Link>

          <Link
            to="/signup"
            className="px-4 py-2 rounded-lg bg-teal-600 text-white font-medium hover:bg-teal-700 transition"
          >
            Signup
          </Link>
        </div>
      </nav>

      {/* ==== ROUTES ==== */}
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />

        {/* Home page if needed */}
        <Route
          path="/"
          element={
            <div className="p-6 text-center">
              <h1 className="text-3xl font-bold text-gray-800">
                Welcome to Wellness Cart 🌿
              </h1>
              <p className="text-gray-600 mt-2">
                The best eco-friendly ecommerce platform.
              </p>
            </div>
          }
        />
      </Routes>
  <main className="grow">
        {/* your routes/components */}
      </main>
      <Footer/>
    </div>

  );
}
