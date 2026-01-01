import AuthLayout from "../components/AuthLayout";
import SignupForm from "../components/SignupForm";


export default function SignupPage() {
  return (
    <AuthLayout
      title="Create Your Account"
      subtitle="Join us and start your wellness journey today"
      footerText="Already have an account?"
      footerLinkText="Login"
      footerLinkTo="/login"
    >
      <SignupForm />
    </AuthLayout>
  );
}
