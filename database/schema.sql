-- =========================================================================
-- FarmAssist - Farmer Service and Agricultural Request Management System
-- Database Schema (MySQL 8.0+)
-- =========================================================================

CREATE DATABASE IF NOT EXISTS farmassist_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE farmassist_db;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(30) NOT NULL, -- ROLE_FARMER, ROLE_PROVIDER, ROLE_ADMIN
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, PENDING_APPROVAL
    address VARCHAR(255),
    district VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(20),
    avatar_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Farmer Profiles Table
CREATE TABLE IF NOT EXISTS farmer_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    farm_location VARCHAR(255),
    village VARCHAR(100),
    taluk_or_block VARCHAR(100),
    district VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(20),
    land_area_acres DOUBLE,
    primary_crops VARCHAR(255),
    soil_type VARCHAR(100),
    irrigation_source VARCHAR(100),
    kisan_card_number VARCHAR(50),
    additional_details VARCHAR(500),
    CONSTRAINT fk_farmer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 3. Provider Profiles Table
CREATE TABLE IF NOT EXISTS provider_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    agency_name VARCHAR(150),
    contact_person VARCHAR(100),
    contact_phone VARCHAR(20),
    service_categories VARCHAR(255), -- e.g. "MACHINERY,IRRIGATION,TRANSPORT"
    coverage_districts VARCHAR(255),
    experience_years INT DEFAULT 0,
    rating_avg DOUBLE DEFAULT 0.0,
    rating_count INT DEFAULT 0,
    is_verified BOOLEAN DEFAULT FALSE,
    availability_status VARCHAR(30) DEFAULT 'AVAILABLE', -- AVAILABLE, BUSY, OFFLINE
    gst_or_license_number VARCHAR(100),
    equipment_inventory_summary VARCHAR(500),
    CONSTRAINT fk_provider_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. Service Categories Table
CREATE TABLE IF NOT EXISTS service_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500),
    icon VARCHAR(100),
    banner_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 5. Service Items Table
CREATE TABLE IF NOT EXISTS service_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    base_price DOUBLE NOT NULL,
    unit_type VARCHAR(30) NOT NULL, -- PER_ACRE, PER_HOUR, PER_DAY, PER_KG, FIXED
    unit_label VARCHAR(50),
    image_url VARCHAR(500),
    is_available BOOLEAN DEFAULT TRUE,
    estimated_duration_hours INT,
    specification_json VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_category FOREIGN KEY (category_id) REFERENCES service_categories(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6. Service Requests Table
CREATE TABLE IF NOT EXISTS service_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tracking_number VARCHAR(30) NOT NULL UNIQUE,
    farmer_id BIGINT NOT NULL,
    service_item_id BIGINT NOT NULL,
    service_category_id BIGINT NOT NULL,
    provider_id BIGINT,
    requested_date DATE NOT NULL,
    preferred_time_slot VARCHAR(50),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- PENDING, UNDER_REVIEW, ACCEPTED, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED, REJECTED
    urgency_level VARCHAR(30) NOT NULL DEFAULT 'NORMAL', -- LOW, NORMAL, HIGH, EMERGENCY
    farm_location VARCHAR(255),
    village_district VARCHAR(150),
    land_area_or_quantity DOUBLE NOT NULL,
    notes VARCHAR(1000),
    estimated_cost DOUBLE,
    final_cost DOUBLE,
    payment_status VARCHAR(30) DEFAULT 'PENDING', -- PENDING, COMPLETED, FAILED, WAIVED
    execution_notes VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    CONSTRAINT fk_request_farmer FOREIGN KEY (farmer_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_request_item FOREIGN KEY (service_item_id) REFERENCES service_items(id),
    CONSTRAINT fk_request_category FOREIGN KEY (service_category_id) REFERENCES service_categories(id),
    CONSTRAINT fk_request_provider FOREIGN KEY (provider_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 7. Request Status History Table
CREATE TABLE IF NOT EXISTS request_status_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    comment VARCHAR(500),
    updated_by_user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_request FOREIGN KEY (request_id) REFERENCES service_requests(id) ON DELETE CASCADE,
    CONSTRAINT fk_history_user FOREIGN KEY (updated_by_user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 8. Payment Records Table
CREATE TABLE IF NOT EXISTS payment_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    payment_method VARCHAR(30) NOT NULL DEFAULT 'CASH_ON_DELIVERY', -- UPI_QR, CASH_ON_DELIVERY, BANK_TRANSFER, ONLINE_CARD
    payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- PENDING, COMPLETED, FAILED, REFUNDED
    transaction_ref VARCHAR(100),
    invoice_number VARCHAR(50),
    notes VARCHAR(500),
    paid_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_request FOREIGN KEY (request_id) REFERENCES service_requests(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 9. Service Reviews Table
CREATE TABLE IF NOT EXISTS service_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id BIGINT NOT NULL UNIQUE,
    farmer_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comments VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_request FOREIGN KEY (request_id) REFERENCES service_requests(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_farmer FOREIGN KEY (farmer_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_provider FOREIGN KEY (provider_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 10. Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message VARCHAR(500) NOT NULL,
    type VARCHAR(50),
    is_read BOOLEAN DEFAULT FALSE,
    link_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Indexes for high-speed querying
CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_role ON users(role);
CREATE INDEX idx_request_tracking ON service_requests(tracking_number);
CREATE INDEX idx_request_status ON service_requests(status);
CREATE INDEX idx_request_farmer ON service_requests(farmer_id);
CREATE INDEX idx_request_provider ON service_requests(provider_id);
