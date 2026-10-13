-- =========================================================================
-- FarmAssist - Sample Seed Data Script (MySQL 8.0+)
-- Passwords below are BCrypt hashed for 'admin123', 'farmer123', 'provider123'
-- =========================================================================

USE farmassist_db;

-- 1. Users
-- Password hashes:
-- admin123    -> $2a$10$wT3WfJvG7q8sQp2LzXm6AeW7sC4k8r8m4o2P1v9e7r4q3e5t6y7u
-- farmer123   -> $2a$10$eK6/4Tz2X5Y8zK9.bL3C7uX8lJ4o8e5n2k1p9r6q4t3w2v1y0z8a
-- provider123 -> $2a$10$kL8/5Tz3Y6Z9aL0.cM4D8vY9mK5p9f6o3l2q0s7r5u4x3w2z1a9b

INSERT INTO users (id, name, email, password, phone, role, status, address, district, state, pincode) VALUES
(1, 'FarmAssist Admin', 'admin@farmassist.com', '$2a$10$wT3WfJvG7q8sQp2LzXm6AeW7sC4k8r8m4o2P1v9e7r4q3e5t6y7u', '+91 98765 00001', 'ROLE_ADMIN', 'ACTIVE', 'Krishi Bhavan, Dept of Agriculture', 'Central Delhi', 'Delhi', '110001'),
(2, 'Ramesh Patel', 'ramesh.patel@kisan.in', '$2a$10$eK6/4Tz2X5Y8zK9.bL3C7uX8lJ4o8e5n2k1p9r6q4t3w2v1y0z8a', '+91 98765 43210', 'ROLE_FARMER', 'ACTIVE', 'Plot 42, Green Valley Farm, Village Rampur', 'Ludhiana', 'Punjab', '141001'),
(3, 'Sunita Devi', 'sunita.devi@kisan.in', '$2a$10$eK6/4Tz2X5Y8zK9.bL3C7uX8lJ4o8e5n2k1p9r6q4t3w2v1y0z8a', '+91 98765 43211', 'ROLE_FARMER', 'ACTIVE', 'House 18, Kisan Basti, Village Nilokheri', 'Karnal', 'Haryana', '132117'),
(4, 'Balwinder Singh', 'balwinder.singh@kisan.in', '$2a$10$eK6/4Tz2X5Y8zK9.bL3C7uX8lJ4o8e5n2k1p9r6q4t3w2v1y0z8a', '+91 98765 43212', 'ROLE_FARMER', 'ACTIVE', 'Khasra 91, Majitha Road', 'Amritsar', 'Punjab', '143001'),
(5, 'Kisan Agro Services', 'kisan.agro@services.in', '$2a$10$kL8/5Tz3Y6Z9aL0.cM4D8vY9mK5p9f6o3l2q0s7r5u4x3w2z1a9b', '+91 98123 45670', 'ROLE_PROVIDER', 'ACTIVE', 'Industrial Area B, GT Road', 'Ludhiana', 'Punjab', '141003'),
(6, 'GreenField Agri Solutions', 'greenfield@services.in', '$2a$10$kL8/5Tz3Y6Z9aL0.cM4D8vY9mK5p9f6o3l2q0s7r5u4x3w2z1a9b', '+91 98123 45671', 'ROLE_PROVIDER', 'ACTIVE', 'Agri Hub, Sector 12', 'Karnal', 'Haryana', '132001'),
(7, 'Bharat Krishi Transport', 'bharat.logistics@services.in', '$2a$10$kL8/5Tz3Y6Z9aL0.cM4D8vY9mK5p9f6o3l2q0s7r5u4x3w2z1a9b', '+91 98123 45672', 'ROLE_PROVIDER', 'ACTIVE', 'Transport Nagar, NH 44', 'Ambala', 'Haryana', '134003'),
(8, 'Shrestha Bio-Organics', 'shrestha.bio@services.in', '$2a$10$kL8/5Tz3Y6Z9aL0.cM4D8vY9mK5p9f6o3l2q0s7r5u4x3w2z1a9b', '+91 98123 45673', 'ROLE_PROVIDER', 'ACTIVE', 'Bio-Tech Center, Pune Agro Zone', 'Pune', 'Maharashtra', '411005');

-- 2. Farmer Profiles
INSERT INTO farmer_profiles (user_id, farm_location, village, district, state, pincode, land_area_acres, primaryCrops, soil_type, irrigation_source, kisan_card_number, additional_details) VALUES
(2, 'Plot 42, Rampur Sector 3', 'Rampur', 'Ludhiana', 'Punjab', '141001', 12.5, 'Wheat, Mustard, Basmati Rice', 'Alluvial Loam', 'Canal & Deep Tube Well', 'KCC-PB-2024-9982', 'Requires machinery before sowing season'),
(3, 'Nilokheri North Farm', 'Nilokheri', 'Karnal', 'Haryana', '132117', 8.0, 'Paddy, Sugarcane, Vegetables', 'Clay Loam', 'Submersible Solar Pump', 'KCC-HR-2025-4421', 'Needs organic inputs'),
(4, 'Majitha Agro Acreage', 'Majitha', 'Amritsar', 'Punjab', '143001', 25.0, 'Cotton, Wheat, Maize', 'Sandy Loam', 'Borewell Canal Grid', 'KCC-PB-2023-1102', 'High acreage farm');

-- 3. Provider Profiles
INSERT INTO provider_profiles (user_id, agency_name, contact_person, contact_phone, service_categories, coverage_districts, experience_years, rating_avg, rating_count, is_verified, availability_status, gst_or_license_number, equipment_inventory_summary) VALUES
(5, 'Kisan Agro Heavy Machinery & Harvesters', 'Gurpreet Singh', '+91 98123 45670', 'MACHINERY,HARVESTING,TRANSPORT', 'Ludhiana, Jalandhar, Patiala', 9, 4.9, 28, TRUE, 'AVAILABLE', '03AACCK1294F1Z3', '4x John Deere 55HP Tractors, 2x Rotavators, 2x Combine Harvesters'),
(6, 'GreenField Smart Irrigation & Drone Tech', 'Amit Sharma', '+91 98123 45671', 'IRRIGATION,DRONE_SPRAY,SOIL_TESTING', 'Karnal, Kurukshetra, Panipat', 6, 4.7, 19, TRUE, 'AVAILABLE', '06AAACG9921D1ZA', '3x DJI Agras T40 Spray Drones, Drip Line Installation Kits'),
(7, 'Bharat Krishi Transport & Mandi Logistics', 'Harish Verma', '+91 98123 45672', 'TRANSPORT,COLD_STORAGE', 'Ambala, Ludhiana, Chandigarh, Karnal', 12, 4.8, 34, TRUE, 'AVAILABLE', '06AABCB4812E1Z8', '8x Eicher 5-Ton Trucks, 4x Ashok Leyland 10-Ton Trailers'),
(8, 'Shrestha Bio-Fertilizers & Soil Health Lab', 'Dr. Nitin Kulkarni', '+91 98123 45673', 'SEEDS_FERTILIZER,SOIL_TESTING', 'Pune, Solapur, Nashik, Ludhiana', 8, 4.9, 15, TRUE, 'AVAILABLE', '27AACCS8812K1Z9', 'Govt-Certified Soil Testing Laboratory, Organic Compost Depot');

-- 4. Service Categories
INSERT INTO service_categories (id, name, code, description, icon, is_active, display_order) VALUES
(1, 'Tractor & Heavy Machinery', 'MACHINERY', 'Advanced tractors, rotavators, deep ploughs, disc harrows, and laser land leveling for optimal field prep.', 'Tractor', TRUE, 1),
(2, 'Irrigation & Water Management', 'IRRIGATION', 'Precision drip irrigation setup, sprinkler networks, solar water pump servicing, and canal connection piping.', 'Droplets', TRUE, 2),
(3, 'Crop Transport & Logistics', 'TRANSPORT', 'On-demand farm-to-mandi transport, heavy grain haulage, multi-ton trailers, and cold-chain refrigerated vans.', 'Truck', TRUE, 3),
(4, 'Seeds & Fertilizer Supply', 'SEEDS_FERTILIZER', 'Certified hybrid seeds, organic vermicompost, bio-fertilizers, and bulk micronutrient delivery.', 'Sprout', TRUE, 4),
(5, 'Soil Testing & Agronomy', 'SOIL_TESTING', 'Comprehensive 14-parameter soil health card, NPK & pH testing, moisture profiling, and agronomist consultation.', 'FlaskConical', TRUE, 5),
(6, 'Drone Spraying & Crop Health', 'DRONE_SPRAY', 'Ultra-fast AI drone spray for pesticide and nano urea foliar fertilizer covering 1 acre in 7 minutes.', 'Plane', TRUE, 6),
(7, 'Harvesting & Threshing', 'HARVESTING', 'Tracked combine harvesters for paddy and wheat, crop cutting, multi-crop threshers, and straw reapers.', 'Wheat', TRUE, 7),
(8, 'Storage & Warehousing', 'COLD_STORAGE', 'Secure moisture-controlled warehouse bays, silos, and cold storage preservation for perishables.', 'Warehouse', TRUE, 8);

-- 5. Service Items
INSERT INTO service_items (id, category_id, name, description, base_price, unit_type, unit_label, image_url, is_available, estimated_duration_hours) VALUES
(1, 1, 'Heavy Duty Tractor Ploughing & Tillage', '55 HP 4WD Tractor with 3-bottom MB Plough. Deep tillage breaking hardpan soil.', 1200.0, 'PER_ACRE', 'per Acre', 'https://images.unsplash.com/photo-1592878904946-b3cd8ae243d0?w=600&auto=format&fit=crop&q=60', TRUE, 3),
(2, 1, 'Laser Land Leveler Service', 'Precision GNSS Laser Guided Leveler ensures 100% flat plot, saving up to 30% water and improving yield.', 1500.0, 'PER_ACRE', 'per Acre', 'https://images.unsplash.com/photo-1594771804886-a933bb2d609b?w=600&auto=format&fit=crop&q=60', TRUE, 4),
(3, 2, 'Drip Irrigation Installation & Maintenance', 'Complete layout design, inline drip lateral installation, sub-main piping, and sand/disc filter testing.', 3500.0, 'PER_ACRE', 'per Acre', 'https://images.unsplash.com/photo-1589923188900-85dae523342b?w=600&auto=format&fit=crop&q=60', TRUE, 8),
(4, 6, 'Autonomous Agro Drone Spraying (Nano Urea / Pesticide)', 'DGCA certified drone pilot with 16L spray tank. High-efficiency uniform mist covering 1 acre in under 8 mins.', 450.0, 'PER_ACRE', 'per Acre', 'https://images.unsplash.com/photo-1527018606416-a60f532132bc?w=600&auto=format&fit=crop&q=60', TRUE, 2),
(5, 3, 'Farm to Mandi 5-Ton Grain Transport', 'Medium-duty freight carrier with loading ramp and waterproof tarpaulins directly from farm gate to local Mandi.', 2200.0, 'FIXED', 'per Trip (up to 30km)', 'https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?w=600&auto=format&fit=crop&q=60', TRUE, 4),
(6, 5, 'Comprehensive Soil Health Card & NPK Analysis', 'Doorstep soil sample collection, laboratory test of Nitrogen, Phosphorus, Potassium, Organic Carbon, pH & EC.', 800.0, 'PER_VISIT', 'per Sample Report', 'https://images.unsplash.com/photo-1628352081506-83c43123ed6d?w=600&auto=format&fit=crop&q=60', TRUE, 24),
(7, 7, 'Combine Harvester with Straw Collector', 'Full-track self-propelled combine harvester for wheat & paddy. Minimizes grain loss to < 1.5%.', 2400.0, 'PER_ACRE', 'per Acre', 'https://images.unsplash.com/photo-1500937386664-56d1dfef3854?w=600&auto=format&fit=crop&q=60', TRUE, 5),
(8, 4, 'Premium Certified Hybrid Wheat Seeds (HD-3086)', 'High yield, rust resistant certified seed bags (40kg bag). High germination rate > 95%.', 1600.0, 'PER_KG', 'per 40kg Bag', 'https://images.unsplash.com/photo-1574943320219-553eb213f72d?w=600&auto=format&fit=crop&q=60', TRUE, 1);

-- 6. Service Requests
INSERT INTO service_requests (id, tracking_number, farmer_id, service_item_id, service_category_id, provider_id, requested_date, preferred_time_slot, status, urgency_level, farm_location, village_district, land_area_or_quantity, notes, estimated_cost, final_cost, payment_status, execution_notes) VALUES
(1, 'FA-2026-1042', 2, 1, 1, 5, CURDATE() + INTERVAL 1 DAY, 'Morning (07:00 - 11:00)', 'IN_PROGRESS', 'HIGH', 'Plot 42, North Field, Rampur', 'Ludhiana, Punjab', 10.0, 'Require deep ploughing before pre-monsoon rain.', 12000.0, 12000.0, 'PENDING', 'Tractor 55HP dispatched. Operator Baljit is on site. 6 acres completed.'),
(2, 'FA-2026-0984', 2, 4, 6, 6, CURDATE() - INTERVAL 5 DAY, 'Morning (08:00 - 12:00)', 'COMPLETED', 'NORMAL', 'Plot 42, South Acreage, Rampur', 'Ludhiana, Punjab', 8.0, 'Nano urea foliar spray over 8 acres of wheat crop.', 3600.0, 3600.0, 'COMPLETED', 'Completed drone flight in 65 minutes. 100% uniform canopy coverage.'),
(3, 'FA-2026-1158', 3, 5, 3, NULL, CURDATE() + INTERVAL 2 DAY, 'Morning (06:00 - 10:00)', 'PENDING', 'EMERGENCY', 'Nilokheri North Farm Gate', 'Karnal, Haryana', 1.0, 'Need urgent transport for 50 quintals paddy harvest to Karnal Grain Mandi.', 2200.0, 2200.0, 'PENDING', NULL);

-- 7. Request Status History
INSERT INTO request_status_history (request_id, status, comment, updated_by_user_id) VALUES
(1, 'PENDING', 'Request submitted by farmer Ramesh Patel', 2),
(1, 'ASSIGNED', 'Assigned to Kisan Agro Services', 1),
(1, 'IN_PROGRESS', 'Tractor dispatched to Rampur field. Work commenced.', 5),
(2, 'PENDING', 'Request submitted', 2),
(2, 'ACCEPTED', 'Claimed by GreenField Agri Solutions', 6),
(2, 'IN_PROGRESS', 'Drone pilot deployed on field', 6),
(2, 'COMPLETED', 'Spraying completed successfully', 6),
(3, 'PENDING', 'Urgent request submitted by Sunita Devi', 3);

-- 8. Payment Records
INSERT INTO payment_records (request_id, amount, payment_method, payment_status, transaction_ref, invoice_number, notes, paid_at) VALUES
(2, 3600.0, 'UPI_QR', 'COMPLETED', 'UPI20269871124', 'INV-2026-10492', 'Paid via UPI QR on completion', NOW() - INTERVAL 5 DAY);

-- 9. Service Reviews
INSERT INTO service_reviews (request_id, farmer_id, provider_id, rating, comments) VALUES
(2, 2, 6, 5, 'Outstanding drone spray service! Saved me two full days of manual labor. Highly recommended.');
