import AuthLayout from "../../components/auth/AuthLayout";
import LoginForm from "../../components/auth/LoginForm";


export default function LoginPage() {
  return (
    <AuthLayout
      title="Welcome Back"
      subtitle="Log in to your account to continue shopping"
      footerText="Don’t have an account?"
      footerLinkText="Sign up"
      footerLinkTo="/signup"
    >
      <LoginForm />
    </AuthLayout>
  );
}

