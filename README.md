# 🌾 FarmAssist – Farmer Service & Agricultural Request Management System

**FarmAssist** is a modern, enterprise-grade digital platform engineered to bridge the gap between farmers and certified agricultural service providers (tractors, heavy tillage machinery, precision drip irrigation, drone aerial spraying, Mandi crop transport, and soil testing laboratories).

---

## 🚀 Key Features

### 👨‍🌾 1. Farmer Portal
- **Agricultural Services Catalog**: Browse 8+ categories of standardized farming services with transparent pricing per acre/unit.
- **Dynamic Cost Estimator**: Automatic calculation of estimated costs based on land size (acres) or product quantity before booking.
- **3-Step Service Booking Wizard**: Select machinery, define schedule date and time slot, specify farm plot location, and assign urgency level (`LOW`, `NORMAL`, `HIGH`, `EMERGENCY`).
- **Live Status & Lifecycle Tracking**: Track service requests across all states: `PENDING` ➔ `ASSIGNED` ➔ `IN_PROGRESS` (Dispatched / On Field) ➔ `COMPLETED`.
- **Payment & Invoicing**: Settle dues via simulated UPI QR code, Cash on Delivery, Bank Transfer, or Debit/Credit Card with instant downloadable invoices.
- **Ratings & Reviews**: Rate completed jobs from 1 to 5 stars with written feedback.
- **Farm Profile Management**: Maintain farm plot addresses, land area in acres, crop rotation schedules, soil types, and Kisan Credit Card (KCC) details.

### 🚜 2. Service Provider & Vendor Portal
- **Available Job Pool**: Real-time regional request feed allowing providers to claim and accept unassigned farmer requests with one click.
- **Active Job Dispatch Board**: Step-by-step field status progression (`Accepted`, `Dispatched to Field`, `Work in Progress`, `Completed`).
- **On-Site Execution Logging**: Record equipment serial numbers, operator details, hours spent, and field condition notes.
- **Earnings & Reviews Overview**: Track total revenue, completed job count, and average customer satisfaction rating.
- **Agency Profile & Capacity**: Configure operating districts, fleet inventory summary (tractors, drones, sprayers), and live availability toggle (`AVAILABLE`, `BUSY`, `OFFLINE`).

### 🛡️ 3. Administrator Control Center
- **Executive Analytics & KPI Dashboard**: Real-time counters for Total Farmers, Active Providers, Total Requests, and Total System Volume.
- **Interactive Recharts Visualizations**:
  - *Monthly Request Volume & Revenue Trends* (Bar & Area Charts)
  - *Requests by Agricultural Service Category* (Donut / Pie Chart)
  - *Request Status Distribution*
- **Master Dispatch Center**: Filter all state-wide requests, manually assign/reassign certified providers, or override job status.
- **Catalog & Tariff Management**: Full CRUD on Agricultural Service Categories and Service Items, including unit pricing, specifications, and availability toggles.
- **User & Vendor Registry**: View and audit all registered farmers and service providers, with 1-click vendor verification badges.

### 🔍 4. Public Tracking Portal
- Instant lookup by Request Tracking Number (e.g., `FA-2026-1042`) showing live status and full audit timeline history without requiring login.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Backend** | Java 17+, Spring Boot 3.x / 4.x, Spring Data JPA, Spring Security, Hibernate, JJWT (Stateless Auth), Maven |
| **Frontend** | React 18, Vite, Tailwind CSS, Lucide React Icons, React Router v6, Recharts, Axios |
| **Database** | MySQL 8.0+ / H2 In-Memory (Zero-setup test mode with MySQL compatibility) |
| **Architecture** | RESTful API, Role-Based Access Control (RBAC), DTO Pattern, Service Layer Architecture |

---

## 👥 Pre-Configured Demo Accounts

For immediate testing, use the floating **Interactive Demo Switcher** in the UI or log in with these credentials:

| Role | Email | Password | Details |
|---|---|---|---|
| **Farmer** | `ramesh.patel@kisan.in` | `farmer123` | Ramesh Patel (12.5 Acres, Punjab) |
| **Farmer** | `sunita.devi@kisan.in` | `farmer123` | Sunita Devi (8 Acres, Haryana) |
| **Service Provider** | `kisan.agro@services.in` | `provider123` | Kisan Agro Machinery & Harvesters |
| **Service Provider** | `greenfield@services.in` | `provider123` | GreenField Drone & Irrigation Tech |
| **Administrator** | `admin@farmassist.com` | `admin123` | Central Agricultural Administrator |

---

## 🏁 How to Run the Project

### Option A: One-Click Quick Launch (Windows)
Double-click `start-all.bat` or run:
```powershell
.\start-all.bat
```
This automatically launches both the Spring Boot backend on port `8080` and the React frontend on port `5173`.

### Option B: Running Services Individually

#### 1. Backend (Spring Boot)
```powershell
cd backend
.\mvnw.cmd spring-boot:run
```
- **Backend API**: `http://localhost:8080/api`
- **H2 Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:farmassist_db`, User: `sa`, Password: *blank*)

#### 2. Frontend (React + Vite)
```powershell
cd frontend
npm.cmd run dev
```
- **Web Application**: `http://localhost:5173`

---

## 🗄️ Database Setup (Optional MySQL Server)

By default, the backend runs with an in-memory database with pre-seeded sample data. To connect to an external MySQL server:
1. Ensure MySQL is running on `localhost:3306`.
2. Import `database/schema.sql` and `database/sample_data.sql`:
   ```sql
   SOURCE c:/Users/chand/Downloads/FarmAssist/database/schema.sql;
   SOURCE c:/Users/chand/Downloads/FarmAssist/database/sample_data.sql;
   ```
3. Run backend with MySQL profile:
   ```powershell
   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

---

## 📡 REST API Endpoint Reference

### Authentication (`/api/auth`)
- `POST /api/auth/register` - Register new Farmer or Service Provider
- `POST /api/auth/login` - Authenticate and obtain JWT token
- `GET /api/auth/me` - Get current user profile

### Agricultural Catalog (`/api/catalog`)
- `GET /api/catalog/categories` - List active categories with service items
- `GET /api/catalog/categories/all` - List all categories (Admin)
- `POST /api/catalog/categories` - Create service category (Admin)
- `GET /api/catalog/items` - List available service items
- `POST /api/catalog/items` - Add new service item (Admin)
- `PUT /api/catalog/items/{id}` - Update service item (Admin)
- `DELETE /api/catalog/items/{id}` - Remove service item (Admin)

### Service Requests (`/api/requests`)
- `POST /api/requests` - Submit new agricultural service request
- `GET /api/requests/my` - Fetch user's requests (Farmer / Provider)
- `GET /api/requests/available-pool` - List unassigned requests for providers
- `GET /api/requests/{id}` - Get request details with audit history
- `GET /api/requests/track/{trackingNumber}` - Public tracking by Tracking ID
- `POST /api/requests/{id}/claim` - Provider claims request from pool
- `POST /api/requests/{id}/assign` - Admin assigns provider
- `PATCH /api/requests/{id}/status` - Update execution status & notes
- `POST /api/requests/{id}/cancel` - Cancel request

### Payments & Invoicing (`/api/payments`)
- `POST /api/payments/process` - Record payment transaction & generate invoice
- `GET /api/payments/request/{requestId}` - Get invoice & payment receipt
- `GET /api/payments/all` - View all payment records (Admin)

### Reviews & Ratings (`/api/reviews`)
- `POST /api/reviews` - Submit 1-5 star review for completed request
- `GET /api/reviews/provider/{providerId}` - Get provider's customer reviews

### Dashboard & Analytics (`/api/dashboard`)
- `GET /api/dashboard/stats` - Role-aware dashboard KPI metrics
- `GET /api/dashboard/admin-summary` - Comprehensive administrative metrics & trends

---

## 📂 Project Directory Structure

```
FarmAssist/
├── backend/                             # Java Spring Boot Application
│   ├── src/main/java/com/farmassist/
│   │   ├── config/                      # SecurityConfig, JwtTokenProvider, DataInitializer
│   │   ├── controller/                  # REST Controllers (Auth, Requests, Catalog, Admin, etc.)
│   │   ├── dto/                         # Request / Response Transfer Objects
│   │   ├── entity/                      # JPA Entities (User, ServiceRequest, ServiceItem, etc.)
│   │   ├── repository/                  # Spring Data JPA Repositories
│   │   ├── service/                     # Core Business Logic & Orchestration
│   │   └── FarmAssistApplication.java  # Application Entry Point
│   ├── src/main/resources/
│   │   ├── application.properties       # Default Application Configuration
│   │   └── application-mysql.properties # MySQL Profile Settings
│   └── pom.xml                          # Maven Dependencies & Build Configuration
│
├── frontend/                            # React.js SPA (Vite + Tailwind CSS)
│   ├── src/
│   │   ├── api/apiClient.js             # Axios Interceptor with JWT Auth
│   │   ├── components/common/           # Navbar, Sidebar, Footer, Modals, Badges
│   │   ├── context/                     # AuthContext, NotificationContext
│   │   ├── pages/                       # Portals for Farmer, Provider, and Admin
│   │   ├── App.jsx                      # Router & Protected Route Guards
│   │   └── main.jsx                     # React Entry Point
│   ├── package.json
│   ├── vite.config.js
│   └── tailwind.config.js
│
├── database/                            # Database Scripts
│   ├── schema.sql                       # Complete MySQL DDL Schema
│   └── sample_data.sql                  # Seed Data with realistic agricultural requests
│
├── start-backend.bat                    # Backend Startup Script
├── start-frontend.bat                   # Frontend Startup Script
├── start-all.bat                        # Full Stack One-Click Launcher
└── README.md                            # Comprehensive System Documentation
```
