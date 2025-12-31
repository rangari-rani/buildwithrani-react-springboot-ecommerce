# Wellness Cart — E-commerce User Flow 

A React, Spring Boot & MySQL project that demonstrates real-world e-commerce user flows such as product browsing, cart and checkout experience.

Built with a focus on clean UI architecture, reusable components, and scalable state management.

---

## ⚙️ Tech Stack

- ⚛️ [**React.js (Vite)**](https://vitejs.dev/) – modern frontend build tool for fast development  
- 🌱 [**Spring Boot**](https://spring.io/projects/spring-boot) – framework for building production-ready REST APIs  
- 🗄️ [**MySQL**](https://www.mysql.com/) – relational database for persistent data storage  

---

## ✨ Features

- User authentication (signup & login)
- JWT-based authentication flow
- Secure password hashing (BCrypt)
- Frontend–backend integration
- Global success/error feedback (toasts)
- Stateless backend configuration

---

## 📁 Project Structure

### React

```tsx
src/
├─ components/      # Feature-based UI components  
├─ pages/           # Route-level pages
├─ hooks/           # Custom hooks
├─ context/         # Auth context
├─ services/        # API & axios setup
├─ data/            # Static product & category data
├─ utils/           # Shared utilities
└─ assets/          # Static assets
```

### Spring Boot

```text
src/main/java/com/buildwithrani/ecommerce
├─ controller/        # REST API endpoints
├─ service/           # logic
├─ dto/               # Request/response objects
├─ model/             # JPA entities
├─ repository/        # Database access
├─ security/          # JWT utilities
├─ config/            # Application configuration
└─ EcommerceApplication.java
```

---

## 🔧 Setup Instructions - React

### 1. Clone the repository

```bash
git clone https://github.com/rangari-rani/buildwithrani-react-springboot-ecommerce.git
cd frontend
```

### 2. Install dependencies

```bash
npm install
```

### 3. Start the development server

```bash
npm run dev
```
> App runs at:
🌐 http://localhost:5173

---

## 🔧 Setup Instructions - Spring Boot

### 1. Clone the repository

```bash
git clone https://github.com/rangari-rani/buildwithrani-react-springboot-ecommerce-api.git
cd backend
```

### 2. Configure local environment
Create:

```text
src/main/resources/application-local.properties
```

Example:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/buildwithrani_ecommerce
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

jwt.secret=YOUR_SECRET_KEY
jwt.expiration=86400000
```

### 3. Start the development server
 Navigate to: `src/main/java/com/buildwithrani/ecommerce/EcommerceApplication.java`  
 Click the **Run ▶️ button**  
> App runs at:
🌐 http://localhost:8080

---

## ✨ Part of BuildWithRani

This project is part of the **BuildWithRani** learning series.

📖 Implementation details:  [buildwithrani.com](https://buildwithrani.com)

---

## 📬 Contact

Connect with me on **[LinkedIn – Rani Rangari](https://linkedin.com/in/rani-rangari)**  

⭐ If you found this project helpful, consider giving it a star!
