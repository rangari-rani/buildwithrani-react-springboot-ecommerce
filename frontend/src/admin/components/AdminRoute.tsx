import type { JSX } from "react";
import { Navigate } from "react-router-dom";
import { isAdminLoggedIn } from "../utils/adminAuth";

const AdminRoute = ({ children }: { children: JSX.Element }) => {
  return isAdminLoggedIn()
    ? children
    : <Navigate to="/admin/login" replace />;
};

export default AdminRoute;
