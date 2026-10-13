package com.farmassist.config;

import com.farmassist.entity.*;
import com.farmassist.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FarmerProfileRepository farmerProfileRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final ServiceItemRepository itemRepository;
    private final ServiceRequestRepository requestRepository;
    private final RequestStatusHistoryRepository historyRepository;
    private final PaymentRecordRepository paymentRepository;
    private final ServiceReviewRepository reviewRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           FarmerProfileRepository farmerProfileRepository,
                           ProviderProfileRepository providerProfileRepository,
                           ServiceCategoryRepository categoryRepository,
                           ServiceItemRepository itemRepository,
                           ServiceRequestRepository requestRepository,
                           RequestStatusHistoryRepository historyRepository,
                           PaymentRecordRepository paymentRepository,
                           ServiceReviewRepository reviewRepository,
                           NotificationRepository notificationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.farmerProfileRepository = farmerProfileRepository;
        this.providerProfileRepository = providerProfileRepository;
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.requestRepository = requestRepository;
        this.historyRepository = historyRepository;
        this.paymentRepository = paymentRepository;
        this.reviewRepository = reviewRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Already initialized
        }

        System.out.println("Initializing FarmAssist database with initial seed data...");

        // 1. Admin User
        User admin = new User("FarmAssist Admin", "admin@farmassist.com", passwordEncoder.encode("admin123"), "+91 98765 00001", Role.ROLE_ADMIN);
        admin.setAddress("Krishi Bhavan, Ministry of Agriculture");
        admin.setDistrict("Central Delhi");
        admin.setState("Delhi");
        admin.setPincode("110001");
        userRepository.save(admin);

        // 2. Farmers
        User f1 = new User("Ramesh Patel", "ramesh.patel@kisan.in", passwordEncoder.encode("farmer123"), "+91 98765 43210", Role.ROLE_FARMER);
        f1.setAddress("Plot 42, Green Valley Farm, Village Rampur");
        f1.setDistrict("Ludhiana");
        f1.setState("Punjab");
        f1.setPincode("141001");
        userRepository.save(f1);

        FarmerProfile fp1 = new FarmerProfile(f1, "Plot 42, Rampur Sector 3", "Rampur", "Ludhiana", "Punjab", 12.5, "Wheat, Mustard, Basmati Rice");
        fp1.setSoilType("Alluvial Loam");
        fp1.setIrrigationSource("Canal & Deep Tube Well");
        fp1.setKisanCardNumber("KCC-PB-2024-9982");
        fp1.setAdditionalDetails("Requires heavy machinery before sowing season and drone spray during pest bloom.");
        farmerProfileRepository.save(fp1);

        User f2 = new User("Sunita Devi", "sunita.devi@kisan.in", passwordEncoder.encode("farmer123"), "+91 98765 43211", Role.ROLE_FARMER);
        f2.setAddress("House 18, Kisan Basti, Village Nilokheri");
        f2.setDistrict("Karnal");
        f2.setState("Haryana");
        f2.setPincode("132117");
        userRepository.save(f2);

        FarmerProfile fp2 = new FarmerProfile(f2, "Nilokheri North Farm", "Nilokheri", "Karnal", "Haryana", 8.0, "Paddy, Sugarcane, Vegetables");
        fp2.setSoilType("Clay Loam");
        fp2.setIrrigationSource("Submersible Solar Pump");
        fp2.setKisanCardNumber("KCC-HR-2025-4421");
        farmerProfileRepository.save(fp2);

        User f3 = new User("Balwinder Singh", "balwinder.singh@kisan.in", passwordEncoder.encode("farmer123"), "+91 98765 43212", Role.ROLE_FARMER);
        f3.setAddress("Khasra 91, Majitha Road");
        f3.setDistrict("Amritsar");
        f3.setState("Punjab");
        f3.setPincode("143001");
        userRepository.save(f3);

        FarmerProfile fp3 = new FarmerProfile(f3, "Majitha Agro Acreage", "Majitha", "Amritsar", "Punjab", 25.0, "Cotton, Wheat, Maize");
        fp3.setSoilType("Sandy Loam");
        fp3.setIrrigationSource("Borewell Canal Grid");
        farmerProfileRepository.save(fp3);

        // 3. Service Providers
        User p1 = new User("Kisan Agro Services", "kisan.agro@services.in", passwordEncoder.encode("provider123"), "+91 98123 45670", Role.ROLE_PROVIDER);
        p1.setAddress("Industrial Area B, GT Road");
        p1.setDistrict("Ludhiana");
        p1.setState("Punjab");
        p1.setPincode("141003");
        userRepository.save(p1);

        ProviderProfile pp1 = new ProviderProfile(p1, "Kisan Agro Heavy Machinery & Harvesters", "Gurpreet Singh", "MACHINERY,HARVESTING,TRANSPORT", "Ludhiana, Jalandhar, Patiala", 9);
        pp1.setEquipmentInventorySummary("4x John Deere 55HP Tractors, 2x Rotavators, 2x Combine Harvesters, 1x Laser Leveler");
        pp1.setGstOrLicenseNumber("03AACCK1294F1Z3");
        pp1.setRatingAvg(4.9);
        pp1.setRatingCount(28);
        providerProfileRepository.save(pp1);

        User p2 = new User("GreenField Agri Solutions", "greenfield@services.in", passwordEncoder.encode("provider123"), "+91 98123 45671", Role.ROLE_PROVIDER);
        p2.setAddress("Agri Hub, Sector 12");
        p2.setDistrict("Karnal");
        p2.setState("Haryana");
        p2.setPincode("132001");
        userRepository.save(p2);

        ProviderProfile pp2 = new ProviderProfile(p2, "GreenField Smart Irrigation & Drone Tech", "Amit Sharma", "IRRIGATION,DRONE_SPRAY,SOIL_TESTING", "Karnal, Kurukshetra, Panipat", 6);
        pp2.setEquipmentInventorySummary("3x DJI Agras T40 Spray Drones, Drip Line Installation Kits, Portable Soil NPK Spectrometer");
        pp2.setRatingAvg(4.7);
        pp2.setRatingCount(19);
        providerProfileRepository.save(pp2);

        User p3 = new User("Bharat Krishi Transport", "bharat.logistics@services.in", passwordEncoder.encode("provider123"), "+91 98123 45672", Role.ROLE_PROVIDER);
        p3.setAddress("Transport Nagar, NH 44");
        p3.setDistrict("Ambala");
        p3.setState("Haryana");
        p3.setPincode("134003");
        userRepository.save(p3);

        ProviderProfile pp3 = new ProviderProfile(p3, "Bharat Krishi Transport & Mandi Logistics", "Harish Verma", "TRANSPORT,COLD_STORAGE", "Ambala, Ludhiana, Chandigarh, Karnal", 12);
        pp3.setEquipmentInventorySummary("8x Eicher 5-Ton Trucks, 4x Ashok Leyland 10-Ton Grain Trailers, 2x Reefer Cold Vans");
        pp3.setRatingAvg(4.8);
        pp3.setRatingCount(34);
        providerProfileRepository.save(pp3);

        User p4 = new User("Shrestha Bio-Organics", "shrestha.bio@services.in", passwordEncoder.encode("provider123"), "+91 98123 45673", Role.ROLE_PROVIDER);
        p4.setAddress("Bio-Tech Center, Pune Agro Zone");
        p4.setDistrict("Pune");
        p4.setState("Maharashtra");
        p4.setPincode("411005");
        userRepository.save(p4);

        ProviderProfile pp4 = new ProviderProfile(p4, "Shrestha Bio-Fertilizers & Soil Health Lab", "Dr. Nitin Kulkarni", "SEEDS_FERTILIZER,SOIL_TESTING", "Pune, Solapur, Nashik, Ludhiana", 8);
        pp4.setEquipmentInventorySummary("Govt-Certified Soil Testing Laboratory, Organic Compost Supply Depot, High-yield Seed stocks");
        pp4.setRatingAvg(4.9);
        pp4.setRatingCount(15);
        providerProfileRepository.save(pp4);

        // 4. Service Categories
        ServiceCategory catMachinery = categoryRepository.save(new ServiceCategory(
                "Tractor & Heavy Machinery", "MACHINERY",
                "Advanced tractors, rotavators, deep ploughs, disc harrows, and laser land leveling for optimal field prep.",
                "Tractor", 1
        ));

        ServiceCategory catIrrigation = categoryRepository.save(new ServiceCategory(
                "Irrigation & Water Management", "IRRIGATION",
                "Precision drip irrigation setup, sprinkler networks, solar water pump servicing, and canal connection piping.",
                "Droplets", 2
        ));

        ServiceCategory catTransport = categoryRepository.save(new ServiceCategory(
                "Crop Transport & Logistics", "TRANSPORT",
                "On-demand farm-to-mandi transport, heavy grain haulage, multi-ton trailers, and cold-chain refrigerated vans.",
                "Truck", 3
        ));

        ServiceCategory catSeeds = categoryRepository.save(new ServiceCategory(
                "Seeds & Fertilizer Supply", "SEEDS_FERTILIZER",
                "Certified hybrid seeds, organic vermicompost, bio-fertilizers, and bulk micronutrient delivery.",
                "Sprout", 4
        ));

        ServiceCategory catSoil = categoryRepository.save(new ServiceCategory(
                "Soil Testing & Agronomy", "SOIL_TESTING",
                "Comprehensive 14-parameter soil health card, NPK & pH testing, moisture profiling, and agronomist consultation.",
                "FlaskConical", 5
        ));

        ServiceCategory catDrone = categoryRepository.save(new ServiceCategory(
                "Drone Spraying & Crop Health", "DRONE_SPRAY",
                "Ultra-fast AI drone spray for pesticide and nano urea foliar fertilizer covering 1 acre in 7 minutes.",
                "Plane", 6
        ));

        ServiceCategory catHarvesting = categoryRepository.save(new ServiceCategory(
                "Harvesting & Threshing", "HARVESTING",
                "Tracked combine harvesters for paddy and wheat, crop cutting, multi-crop threshers, and straw reapers.",
                "Wheat", 7
        ));

        ServiceCategory catStorage = categoryRepository.save(new ServiceCategory(
                "Storage & Warehousing", "COLD_STORAGE",
                "Secure moisture-controlled warehouse bays, silos, and cold storage preservation for perishables.",
                "Warehouse", 8
        ));

        // 5. Service Items
        ServiceItem item1 = itemRepository.save(new ServiceItem(
                catMachinery, "Heavy Duty Tractor Ploughing & Tillage",
                "55 HP 4WD Tractor with 3-bottom MB Plough. Deep tillage breaking hardpan soil.",
                1200.0, UnitType.PER_ACRE, "per Acre", "https://images.unsplash.com/photo-1592878904946-b3cd8ae243d0?w=600&auto=format&fit=crop&q=60"
        ));

        ServiceItem item2 = itemRepository.save(new ServiceItem(
                catMachinery, "Laser Land Leveler Service",
                "Precision GNSS Laser Guided Leveler ensures 100% flat plot, saving up to 30% water and improving yield.",
                1500.0, UnitType.PER_ACRE, "per Acre", "https://images.unsplash.com/photo-1594771804886-a933bb2d609b?w=600&auto=format&fit=crop&q=60"
        ));

        ServiceItem item3 = itemRepository.save(new ServiceItem(
                catIrrigation, "Drip Irrigation Installation & Maintenance",
                "Complete layout design, inline drip lateral installation, sub-main piping, and sand/disc filter testing.",
                3500.0, UnitType.PER_ACRE, "per Acre", "https://images.unsplash.com/photo-1589923188900-85dae523342b?w=600&auto=format&fit=crop&q=60"
        ));

        ServiceItem item4 = itemRepository.save(new ServiceItem(
                catDrone, "Autonomous Agro Drone Spraying (Nano Urea / Pesticide)",
                "DGCA certified drone pilot with 16L spray tank. High-efficiency uniform mist covering 1 acre in under 8 mins.",
                450.0, UnitType.PER_ACRE, "per Acre", "https://images.unsplash.com/photo-1527018606416-a60f532132bc?w=600&auto=format&fit=crop&q=60"
        ));

        ServiceItem item5 = itemRepository.save(new ServiceItem(
                catTransport, "Farm to Mandi 5-Ton Grain Transport",
                "Medium-duty freight carrier with loading ramp and waterproof tarpaulins directly from farm gate to local Mandi.",
                2200.0, UnitType.FIXED, "per Trip (up to 30km)", "https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?w=600&auto=format&fit=crop&q=60"
        ));

        ServiceItem item6 = itemRepository.save(new ServiceItem(
                catSoil, "Comprehensive Soil Health Card & NPK Analysis",
                "Doorstep soil sample collection, laboratory test of Nitrogen, Phosphorus, Potassium, Organic Carbon, pH & EC.",
                800.0, UnitType.PER_VISIT, "per Sample Report", "https://images.unsplash.com/photo-1628352081506-83c43123ed6d?w=600&auto=format&fit=crop&q=60"
        ));

        ServiceItem item7 = itemRepository.save(new ServiceItem(
                catHarvesting, "Combine Harvester with Straw Collector",
                "Full-track self-propelled combine harvester for wheat & paddy. Minimizes grain loss to < 1.5%.",
                2400.0, UnitType.PER_ACRE, "per Acre", "https://images.unsplash.com/photo-1500937386664-56d1dfef3854?w=600&auto=format&fit=crop&q=60"
        ));

        ServiceItem item8 = itemRepository.save(new ServiceItem(
                catSeeds, "Premium Certified Hybrid Wheat Seeds (HD-3086)",
                "High yield, rust resistant certified seed bags (40kg bag). High germination rate > 95%.",
                1600.0, UnitType.PER_KG, "per 40kg Bag", "https://images.unsplash.com/photo-1574943320219-553eb213f72d?w=600&auto=format&fit=crop&q=60"
        ));

        // 6. Sample Requests
        // Request 1: In Progress
        ServiceRequest req1 = new ServiceRequest();
        req1.setTrackingNumber("FA-2026-1042");
        req1.setFarmer(f1);
        req1.setServiceItem(item1);
        req1.setServiceCategory(catMachinery);
        req1.setAssignedProvider(p1);
        req1.setRequestedDate(LocalDate.now().plusDays(1));
        req1.setPreferredTimeSlot("Morning (07:00 - 11:00)");
        req1.setStatus(RequestStatus.IN_PROGRESS);
        req1.setUrgencyLevel(UrgencyLevel.HIGH);
        req1.setFarmLocation("Plot 42, North Field, Rampur");
        req1.setVillageDistrict("Ludhiana, Punjab");
        req1.setLandAreaOrQuantity(10.0);
        req1.setEstimatedCost(12000.0);
        req1.setFinalCost(12000.0);
        req1.setNotes("Require deep ploughing before pre-monsoon rain. Soil is somewhat dry.");
        req1.setExecutionNotes("Tractor 55HP dispatched. Operator Baljit is on site. 6 acres completed so far.");
        req1.setPaymentStatus(PaymentStatus.PENDING);
        ServiceRequest savedReq1 = requestRepository.save(req1);

        historyRepository.save(new RequestStatusHistory(savedReq1, RequestStatus.PENDING, "Request submitted by farmer Ramesh Patel", f1));
        historyRepository.save(new RequestStatusHistory(savedReq1, RequestStatus.ASSIGNED, "Assigned to Kisan Agro Services", admin));
        historyRepository.save(new RequestStatusHistory(savedReq1, RequestStatus.IN_PROGRESS, "Tractor dispatched to Rampur field. Work commenced.", p1));

        // Request 2: Completed with Payment & Review
        ServiceRequest req2 = new ServiceRequest();
        req2.setTrackingNumber("FA-2026-0984");
        req2.setFarmer(f1);
        req2.setServiceItem(item4);
        req2.setServiceCategory(catDrone);
        req2.setAssignedProvider(p2);
        req2.setRequestedDate(LocalDate.now().minusDays(5));
        req2.setPreferredTimeSlot("Morning (08:00 - 12:00)");
        req2.setStatus(RequestStatus.COMPLETED);
        req2.setUrgencyLevel(UrgencyLevel.NORMAL);
        req2.setFarmLocation("Plot 42, South Acreage, Rampur");
        req2.setVillageDistrict("Ludhiana, Punjab");
        req2.setLandAreaOrQuantity(8.0);
        req2.setEstimatedCost(3600.0);
        req2.setFinalCost(3600.0);
        req2.setNotes("Nano urea foliar spray over 8 acres of wheat crop.");
        req2.setExecutionNotes("Completed drone flight in 65 minutes. 100% uniform canopy coverage.");
        req2.setPaymentStatus(PaymentStatus.COMPLETED);
        req2.setCompletedAt(LocalDateTime.now().minusDays(5));
        ServiceRequest savedReq2 = requestRepository.save(req2);

        historyRepository.save(new RequestStatusHistory(savedReq2, RequestStatus.PENDING, "Request submitted", f1));
        historyRepository.save(new RequestStatusHistory(savedReq2, RequestStatus.ACCEPTED, "Claimed by GreenField Agri Solutions", p2));
        historyRepository.save(new RequestStatusHistory(savedReq2, RequestStatus.IN_PROGRESS, "Drone pilot deployed on field", p2));
        historyRepository.save(new RequestStatusHistory(savedReq2, RequestStatus.COMPLETED, "Spraying completed successfully", p2));

        // Payment for Req 2
        PaymentRecord pay2 = new PaymentRecord(savedReq2, 3600.0, PaymentMethod.UPI_QR, PaymentStatus.COMPLETED, "UPI20269871124", "INV-2026-10492");
        paymentRepository.save(pay2);

        // Review for Req 2
        ServiceReview rev2 = new ServiceReview(savedReq2, f1, p2, 5, "Outstanding drone spray service! Saved me two full days of manual labor. Highly recommended.");
        reviewRepository.save(rev2);

        // Request 3: Pending in available pool
        ServiceRequest req3 = new ServiceRequest();
        req3.setTrackingNumber("FA-2026-1158");
        req3.setFarmer(f2);
        req3.setServiceItem(item5);
        req3.setServiceCategory(catTransport);
        req3.setRequestedDate(LocalDate.now().plusDays(2));
        req3.setPreferredTimeSlot("Morning (06:00 - 10:00)");
        req3.setStatus(RequestStatus.PENDING);
        req3.setUrgencyLevel(UrgencyLevel.EMERGENCY);
        req3.setFarmLocation("Nilokheri North Farm Gate");
        req3.setVillageDistrict("Karnal, Haryana");
        req3.setLandAreaOrQuantity(1.0);
        req3.setEstimatedCost(2200.0);
        req3.setFinalCost(2200.0);
        req3.setNotes("Need urgent transport for 50 quintals paddy harvest to Karnal Grain Mandi before forecast rain.");
        req3.setPaymentStatus(PaymentStatus.PENDING);
        ServiceRequest savedReq3 = requestRepository.save(req3);
        historyRepository.save(new RequestStatusHistory(savedReq3, RequestStatus.PENDING, "Urgent request submitted by Sunita Devi", f2));

        // Notifications
        notificationRepository.save(new Notification(f1, "Request In Progress (FA-2026-1042)", "Kisan Agro has arrived on site and started tractor tillage.", "REQUEST_UPDATE", "/requests/" + savedReq1.getId()));
        notificationRepository.save(new Notification(p1, "New Job In Progress (FA-2026-1042)", "You are working on Ramesh Patel's 10 acres tillage.", "ASSIGNMENT", "/provider/requests/" + savedReq1.getId()));
        notificationRepository.save(new Notification(admin, "New Emergency Request (FA-2026-1158)", "Farmer Sunita Devi requested emergency crop transport in Karnal.", "SYSTEM", "/admin/requests"));

        System.out.println("FarmAssist sample data successfully seeded!");
    }
}
