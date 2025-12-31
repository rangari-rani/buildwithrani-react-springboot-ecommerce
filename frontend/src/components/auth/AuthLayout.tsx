import React from "react";
import { Link } from "react-router-dom";

interface AuthLayoutProps {
  title: string;
  subtitle?: string;
  children: React.ReactNode;
  footerText?: string;
  footerLinkText?: string;
  footerLinkTo?: string;
}


const AuthLayout: React.FC<AuthLayoutProps> = ({
  title,
  subtitle,
  children,
  footerText,
  footerLinkText,
  footerLinkTo,
}) => {
  return (
    <div className="h-screen overflow-hidden flex flex-col md:flex-row">
      
      {/* Left Section */}
      <div className="hidden md:flex md:w-1/2 bg-green-50 items-center justify-center relative overflow-hidden">
        <div className="relative w-full h-full bg-linear-to-br from-green-50 via-white to-green-100 rounded-r-2xl overflow-hidden flex items-center justify-center">
          <div className="absolute w-64 h-64 bg-green-200 rounded-full blur-3xl opacity-50 top-10 left-10"></div>
          <div className="absolute w-48 h-48 bg-emerald-300 rounded-full blur-2xl opacity-40 bottom-16 right-12"></div>

          <div className="relative z-10 text-center">
            <h2 className="text-2xl font-bold text-green-700">Welcome Back!</h2>
            <p className="text-gray-600 text-sm mt-2 max-w-xs mx-auto">
              Join our wellness community and unlock your next favorite products 🌿
            </p>
          </div>
        </div>

        <div className="absolute bottom-6 text-center text-green-700 text-sm">
          <p>Check out our top trending products 🌿</p>
        </div>
      </div>

      {/* Right Section */}
      <div className="w-full md:w-1/2 flex items-center justify-center p-6 bg-white">
        <div className="w-full max-w-md">
          <div className="mb-8 text-center">
            <h1 className="text-3xl font-bold text-gray-900">{title}</h1>
            {subtitle && (
              <p className="text-gray-600 text-sm mt-2">{subtitle}</p>
            )}
          </div>

          <div className="bg-white rounded-xl shadow-md p-6 border border-gray-100">
            {children}
          </div>

          {footerText && footerLinkTo && footerLinkText && (
            <p className="text-sm text-center text-gray-700 mt-4">
              {footerText}{" "}
              <Link
                to={footerLinkTo}
                className="text-green-600 font-medium hover:underline"
              >
                {footerLinkText}
              </Link>
            </p>
          )}
        </div>
      </div>

    </div>
  );
};

export default AuthLayout;
