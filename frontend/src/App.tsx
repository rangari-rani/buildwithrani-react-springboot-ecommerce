import { Routes, Route } from "react-router-dom";

import Footer from "./shared/Footer";
import SignupPage from "./auth/pages/signup";
import LoginPage from "./auth/pages/login";
import Home from "./homepage/pages/Home";
import ProductsList from "./products/pages/ProductsList";
import ProductDetail from "./products/pages/ProductDetail";
import AdminProductsPage from "./admin/pages/AdminProductsPage";
import AddProductPage from "./admin/pages/AddProductPage";
import EditProductPage from "./admin/pages/EditProductPage";
import AdminLayout from "./admin/layout/AdminLayout";
import PublicLayout from "./shared/PublicLayout";
import ScrollToTop from "./shared/ScrollToTop";
import CartPage from "./cart/pages/CartPage";
import OrderSuccess from "./orders/pages/OrderSuccess";

export default function App() {
  return (

    <div className="min-h-screen flex flex-col">
       <ScrollToTop />
     <Routes>

      {/* Public layout */}
      <Route element={<PublicLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/products" element={<ProductsList />} />
        <Route path="/products/:id" element={<ProductDetail />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/order-success" element={<OrderSuccess />} />

      </Route>

      {/* Admin layout (NO navbar) */}
      <Route path="/admin" element={<AdminLayout />}>
        <Route path="products" element={<AdminProductsPage />} />
        <Route path="products/new" element={<AddProductPage />} />
        <Route path="products/:id/edit" element={<EditProductPage />} />
      </Route>

    </Routes>

    <Footer />
    </div>
  );
}
