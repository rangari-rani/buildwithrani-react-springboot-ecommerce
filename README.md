<div align="center">

# 🛒 Wellness Cart  
### Domain-Driven E-Commerce System  
**Built by Rani Rangari**

A full-stack, Dockerized ecommerce platform built with **Spring Boot + React**, showcasing explicit order lifecycles, role-based security, transactional integrity, and audit logging.

<br/>

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![React](https://img.shields.io/badge/React-Vite-blue)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![JWT](https://img.shields.io/badge/Auth-JWT-purple)

<br/>

<a href="https://buildwithrani.com">📘 BuildWithRani Docs</a>
&nbsp;•&nbsp;
<a href="https://linkedin.com/in/rani-rangari">💼 LinkedIn</a>

</div>

---

## Core Features

- Explicit Order State Machine (CREATED → PAID → PACKED → SHIPPED → DELIVERED)
- Cart aggregate with one-way Cart → Order transition
- JWT-based authentication with role-aware access control (USER / ADMIN)
- Transactional audit logging using Spring AOP
- Admin-controlled product lifecycle (ACTIVE / INACTIVE)
- Dockerized full stack (React + Spring Boot + MySQL)
- Automatically seeded demo data (users, products, orders)
- Clean REST API design ready for OpenAPI / Swagger integration
  
---

## ⚙️ Tech Stack

### Backend
- [**Java 17**](https://www.oracle.com/java/) – core language for backend development  
- [**Spring Boot**](https://spring.io/projects/spring-boot) – framework for building domain-driven REST APIs and workflow orchestration  
- [**Spring Security**](https://spring.io/projects/spring-security) – authentication and authorization layer  
- [**JWT (JSON Web Tokens)**](https://jwt.io/) – stateless token-based authentication  

### Frontend
- [**React**](https://react.dev/) – component-based UI framework  
- [**Vite**](https://vitejs.dev/) – modern frontend build tool and dev server  

### Database
- [**MySQL 8**](https://www.mysql.com/) – relational database for transactional persistence  

### Infrastructure
- [**Docker**](https://www.docker.com/) – containerization platform  
- [**Docker Compose**](https://docs.docker.com/compose/) – multi-container orchestration  
- [**Nginx**](https://nginx.org/) – production-grade static frontend server and SPA routing  

---

## 🐳 Quick Start (Recommended)

Run the entire stack with one command:

```bash
docker compose up --build
```

After startup:

- **Frontend** → http://localhost:3000
- **Backend** → http://localhost:8080

To reset database and reseed demo data:

```bash
docker compose down -v
docker compose up --build
```

---

## 🔐 Demo Credentials

```bash
ADMIN  
email: admin@demo.com  
password: Password123!

USER  
email: user@demo.com  
password: Password123!
```

## 🏛 Architecture Highlights

- Stateless REST APIs
- Domain-enforced lifecycle invariants
- Role-based authorization at API boundary
- Containerized multi-service deployment
- Healthcheck-based startup orchestration

---

## 📸 Screenshots

![Audit](https://github.com/rangari-rani/buildwithrani-react-springboot-ecommerce/blob/25f9dedd42809e989f8d3c79c6d73c204c06cd31/frontend/public/audit.png)

![List Order](https://github.com/rangari-rani/buildwithrani-react-springboot-ecommerce/blob/1446b4d38f40087f47083fcf5e815c11e1b3493b/frontend/public/listorder.png)

![List Product](https://github.com/rangari-rani/buildwithrani-react-springboot-ecommerce/blob/1446b4d38f40087f47083fcf5e815c11e1b3493b/frontend/public/listproduct.png)

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
src/main/java/com/buildwithrani/backend
├─ audit/
├─ auth/
├─ cart/
├─ order/
├─ product/
├─ common/
└─ BackendApplication.java
```

---

## 🛠 Local Development (Without Docker)

 **Setup Instructions - React**

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
 Navigate to: `src/main/java/com/buildwithrani/backend/BackendApplication.java`    
 Click the **Run ▶️ button**  
> App runs at:
🌐 http://localhost:8080

---

## 👩‍💻 Author

Rani Rangari  
https://linkedin.com/in/rani-rangari  
https://buildwithrani.com
