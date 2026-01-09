import { FaGithub, FaLinkedin } from "react-icons/fa";
import { Link } from "react-router-dom";

const Footer = () => {
  return (
    <footer className="border-t border-gray-200 bg-white py-6 mt-10">
      <div className="container mx-auto flex flex-wrap justify-center items-center gap-4 px-4 text-center">
        <p className="text-sm text-gray-600">
          © {new Date().getFullYear()}{" "}
            <span className="font-semibold text-[#10B981]">Rani Rangari</span>
        </p>

        <div className="flex items-center gap-4">
          <a
            href="https://github.com/rangari-rani"
            target="_blank"
            rel="noopener noreferrer"
            className="text-gray-500 hover:text-[#10B981] transition-colors"
            aria-label="GitHub"
          >
            <FaGithub size={18} />
          </a>
          <a
            href="https://linkedin.com/in/rani-rangari"
            target="_blank"
            rel="noopener noreferrer"
            className="text-gray-500 hover:text-[#10B981] transition-colors"
            aria-label="LinkedIn"
          >
            <FaLinkedin size={18} />
          </a>
        </div>

<div className="flex flex-wrap items-center justify-center gap-2 text-center">
  <a
    href="https://buildwithrani.com"
    target="_blank"
    rel="noopener noreferrer"
    className="text-sm font-medium text-teal-500 hover:text-[#10B981] transition-colors"
  >
    buildwithrani.com
  </a>

  <span className="text-gray-300">•</span>

  <Link
    to="/admin/products"
    className="text-sm font-medium text-gray-400 hover:text-gray-600 underline-offset-2 hover:underline"
  >
    Admin
  </Link>
</div>


      </div>
    </footer>
  );
};

export default Footer;
