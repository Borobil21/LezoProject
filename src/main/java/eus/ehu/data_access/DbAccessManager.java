package eus.ehu.data_access;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.User;
import eus.ehu.domain.Vehicle;

public class DbAccessManager {
    private static final String DB_URL_PROPERTY = "lezo.db.url";
    private static final String DB_URL_ENV = "LEZO_DB_URL";

    protected EntityManager db;
    protected EntityManagerFactory emf;
    private boolean available;
    private static DbAccessManager instance;

    private DbAccessManager() {
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml");

        String configuredUrl = resolveDbUrl();
        if (configuredUrl != null && !configuredUrl.isBlank()) {
            registryBuilder.applySetting("hibernate.connection.url", configuredUrl);
        }

        final StandardServiceRegistry registry = registryBuilder.build();
        available = false;
        try {
            emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            emf = null;
            StandardServiceRegistryBuilder.destroy(registry);
            System.err.println("Database bootstrap failed: " + e.getMessage());
            e.printStackTrace();
        }

        if (emf != null) {
            try {
                db = emf.createEntityManager();
                available = true;
                System.out.println("DataBase opened");
            } catch (Exception e) {
                db = null;
                available = false;
                System.err.println("Unable to create EntityManager: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String resolveDbUrl() {
        String systemValue = System.getProperty(DB_URL_PROPERTY);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String envValue = System.getenv(DB_URL_ENV);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        return null;
    }

    public static synchronized DbAccessManager getInstance() {
        if (instance == null) {
            instance = new DbAccessManager();
        }
        return instance;
    }

    public boolean isAvailable() {
        return available && db != null;
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        available = false;
        System.out.println("DataBase is closed");
    }

    public void storeUser(User user) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        db.getTransaction().begin();
        db.persist(user);
        db.getTransaction().commit();
        System.out.println("User stored: " + user.getId());
    }

    public void addSaleToUser(Integer userId, Integer saleId) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        User user = db.find(User.class, userId);
        Sale sale = db.find(Sale.class, saleId);

        addSaleToUser(user, sale);
    }

    public void addSaleToUser(User user, Sale sale) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        db.getTransaction().begin();
        user.getPublications().add(sale);
        sale.setUser(user);
        db.merge(sale);
        db.merge(user);
        db.getTransaction().commit();
        System.out.println("Sale added to user: " + user.getId());
    }

    public void storeSale(Sale sale) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        db.getTransaction().begin();
        db.persist(sale);
        db.getTransaction().commit();
        System.out.println("Sale created: " + sale.getId());
    }

    public void storeVehicle(Vehicle vehicle) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        db.getTransaction().begin();
        db.persist(vehicle);
        db.getTransaction().commit();
        System.out.println("Vehicle stored: " + vehicle.getLicensePlate());
    }

    public void addVehicleToSale(Integer saleId, String licensePlate) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        Sale sale = db.find(Sale.class, saleId);
        Vehicle vehicle = db.find(Vehicle.class, licensePlate);

        addVehicleToSale(sale, vehicle);
    }

    public void addVehicleToSale(Sale sale, Vehicle vehicle) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        db.getTransaction().begin();
        sale.setVehicle(vehicle);
        vehicle.setListing(sale);
        db.merge(vehicle);
        db.merge(sale);
        db.getTransaction().commit();
        System.out.println("Vehicle added to sale: " + sale.getId());
    }

    public Sale getSaleById(Integer saleId) {
        if (!isAvailable()) {
            return null;
        }
        return db.find(Sale.class, saleId);
    }

    public User getUserById(Integer userId) {
        if (!isAvailable()) {
            return null;
        }
        return db.find(User.class, userId);
    }

    public User getUserByEmail(String email) {
        if (!isAvailable()) {
            return null;
        }
        try {
            return db.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        if (!isAvailable()) {
            return null;
        }
        return db.find(Vehicle.class, licensePlate);
    }

    public Vehicle getVehicleBySaleId(Integer saleId) {
        if (!isAvailable()) {
            return null;
        }
        try {
            return db.createQuery("SELECT v FROM Vehicle v JOIN v.listing s WHERE s.id = :saleId", Vehicle.class)
                    .setParameter("saleId", saleId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> getAllUsers() {
        if (!isAvailable()) {
            return new ArrayList<>();
        }
        return db.createQuery("SELECT u FROM User u ORDER BY u.id", User.class).getResultList();
    }

    public List<Sale> getAllSales() {
        if (!isAvailable()) {
            return new ArrayList<>();
        }
        return db.createQuery("SELECT s FROM Sale s ORDER BY s.id", Sale.class).getResultList();
    }

    public List<Vehicle> getAllVehicles() {
        if (!isAvailable()) {
            return new ArrayList<>();
        }
        return db.createQuery("SELECT v FROM Vehicle v ORDER BY v.licensePlate", Vehicle.class).getResultList();
    }

    public void deleteSale(Integer saleId) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        Sale sale = db.find(Sale.class, saleId);
        if (sale != null) {
            db.getTransaction().begin();

            if (sale.getUser() != null && sale.getUser().getPublications() != null) {
                sale.getUser().getPublications().remove(sale);
                sale.setUser(null);
            }

            db.remove(sale);
            db.getTransaction().commit();
            System.out.println("Sale deleted: " + saleId);
        } else {
            System.out.println("Sale not found: " + saleId);
        }
    }

    public void deleteUser(Integer userId) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        User user = db.find(User.class, userId);
        if (user != null) {
            db.getTransaction().begin();
            db.remove(user);
            db.getTransaction().commit();
            System.out.println("User deleted: " + userId);
        } else {
            System.out.println("User not found: " + userId);
        }
    }

    public void deleteVehicle(String licensePlate) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        Vehicle vehicle = db.find(Vehicle.class, licensePlate);
        if (vehicle != null) {
            db.getTransaction().begin();

            if (vehicle.getListing() != null) {
                vehicle.getListing().setVehicle(null);
            }

            db.remove(vehicle);
            db.getTransaction().commit();
            System.out.println("Vehicle deleted: " + licensePlate);
        } else {
            System.out.println("Vehicle not found: " + licensePlate);
        }
    }

    public List<Sale> getFilteredSales(String location, Double minPrice, Double maxPrice,
            List<String> makes, String model,
            Integer minYear, Integer maxYear,
            Integer minMileage, Integer maxMileage,
            MotorType motorType) {

        if (!isAvailable()) {
            return new ArrayList<>();
        }

        String queryStr = "SELECT s FROM Sale s JOIN s.vehicle v WHERE 1=1";

        if (location != null && !location.isEmpty())
            queryStr += " AND s.location = :location";
        if (minPrice != null)
            queryStr += " AND s.fixedPrice >= :minPrice";
        if (maxPrice != null)
            queryStr += " AND s.fixedPrice <= :maxPrice";
        if (makes != null && !makes.isEmpty())
            queryStr += " AND v.make IN :makes";
        if (model != null && !model.isEmpty())
            queryStr += " AND v.model = :model";
        if (minYear != null)
            queryStr += " AND v.year >= :minYear";
        if (maxYear != null)
            queryStr += " AND v.year <= :maxYear";
        if (minMileage != null)
            queryStr += " AND v.mileage >= :minMileage";
        if (maxMileage != null)
            queryStr += " AND v.mileage <= :maxMileage";
        if (motorType != null)
            queryStr += " AND v.motorType = :motorType";

        TypedQuery<Sale> query = db.createQuery(queryStr, Sale.class);

        if (location != null && !location.isEmpty())
            query.setParameter("location", location);
        if (minPrice != null)
            query.setParameter("minPrice", minPrice);
        if (maxPrice != null)
            query.setParameter("maxPrice", maxPrice);
        if (makes != null && !makes.isEmpty())
            query.setParameter("makes", makes);
        if (model != null && !model.isEmpty())
            query.setParameter("model", model);
        if (minYear != null)
            query.setParameter("minYear", minYear);
        if (maxYear != null)
            query.setParameter("maxYear", maxYear);
        if (minMileage != null)
            query.setParameter("minMileage", minMileage);
        if (maxMileage != null)
            query.setParameter("maxMileage", maxMileage);
        if (motorType != null)
            query.setParameter("motorType", motorType);

        return query.getResultList();
    }

    // for wishlist
    public void addVehicleToWishlist(Integer userId, String licensePlate) {
        if (!isAvailable()) {
            throw new IllegalStateException("Database not available");
        }
        try {
            db.getTransaction().begin();
            User user = db.find(User.class, userId);
            Vehicle vehicle = db.find(Vehicle.class, licensePlate);
            
            if (user != null && vehicle != null) {
                if (!user.getWishlist().contains(vehicle)) {
                    user.getWishlist().add(vehicle);
                    db.merge(user);
                    System.out.println("Coche " + licensePlate + " añadido a la wishlist del usuario " + userId);
                }
            }
            db.getTransaction().commit();
        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            System.err.println("Error al añadir a la wishlist:");
            e.printStackTrace();
        }
    }

    public List<Vehicle> getUserWishlist(Integer userId) {
        if (!isAvailable()) {
            return new ArrayList<>();
        }
        try {
            db.clear(); 
            
            return db.createQuery("SELECT v FROM User u JOIN u.wishlist v WHERE u.id = :userId", Vehicle.class)
                     .setParameter("userId", userId)
                     .getResultList();
        } catch (Exception e) {
            System.err.println("Error al obtener la wishlist:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
