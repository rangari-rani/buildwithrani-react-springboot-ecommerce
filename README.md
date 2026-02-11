# Wellness Cart — Role-Based E-Commerce System

Backend-focused e-commerce system built with Java, Spring Boot, and MySQL, with a React frontend.

This project demonstrates domain-driven backend design, including:

- State-driven Order lifecycle management
- Transactionally consistent Cart aggregate with one-way Cart → Order transition
- JWT-based authentication with role-based access (USER / ADMIN)
- Centralized exception handling and enforced business rules
- Admin-controlled product availability

The React client acts as a consumer of backend state; lifecycle rules and permissions are enforced server-side.

---

## ⚙️ Tech Stack

- ☕ [**Java + Spring Boot**](https://spring.io/projects/spring-boot) – backend framework for domain-driven REST APIs and order workflows  
- 🔐 **Spring Security + JWT** – stateless authentication and role-based access control  
- 🗄️ [**MySQL**](https://www.mysql.com/) – relational database for transactional persistence  
- ⚛️ [**React.js (Vite)**](https://vitejs.dev/) – frontend client for consuming backend APIs  


---

## 📸 Screenshots

![Product](https://github.com/rangari-rani/buildwithrani-react-springboot-ecommerce/blob/27dd92048f5bdcf3dda0a6a7bd93b4a2c907c45e/frontend/public/listproduct.png)
_Admin Product_
![Cart Page](https://github.com/rangari-rani/buildwithrani-react-springboot-ecommerce/blob/27dd92048f5bdcf3dda0a6a7bd93b4a2c907c45e/frontend/public/listcart.png)
_Cart Page_
![Order](https://github.com/rangari-rani/buildwithrani-react-springboot-ecommerce/blob/27dd92048f5bdcf3dda0a6a7bd93b4a2c907c45e/frontend/public/listorder.png)
_Admin Order_

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
├─ service/           # business logic
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
git clone https://github.com/rangari-rani/buildwithrani-react-springboot-ecommerce.git
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
