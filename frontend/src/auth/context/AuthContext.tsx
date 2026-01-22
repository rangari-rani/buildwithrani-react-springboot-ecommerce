import { createContext, useContext, useEffect, useState } from "react";
import api from "../services/api";

interface AuthUser {
  email: string;
}

interface AuthContextType {
  user: AuthUser | null;
  login: (token: string, email: string) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<AuthUser | null>(null);

  // 🔁 Restore auth on refresh
  useEffect(() => {
    const token = localStorage.getItem("token");
    const email = localStorage.getItem("email");

    if (token && email) {
      setUser({ email });
    }
  }, []);

  const login = (token: string, email: string) => {
    localStorage.setItem("token", token);
    localStorage.setItem("email", email);
    setUser({ email });
  };

const logout = async () => {
  try {
    await api.post("/api/auth/logout");
  } catch (err) {
    // Even if backend logout fails, clear client state
  } finally {
    localStorage.removeItem("token");
    localStorage.removeItem("email");
    setUser(null);
  }
};



  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
        isAuthenticated: !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return ctx;
};
