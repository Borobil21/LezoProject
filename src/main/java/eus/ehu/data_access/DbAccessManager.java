package eus.ehu.data_access;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.User;
import eus.ehu.domain.Vehicle;

public class DbAccessManager {
    protected EntityManager db;
    protected EntityManagerFactory emf;

    public DbAccessManager() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        try {
            emf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        db = emf.createEntityManager();
        System.out.println("DataBase opened");
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        System.out.println("DataBase is closed");
    }

    public void storeUser(User user) {
        db.getTransaction().begin();
        db.persist(user);
        db.getTransaction().commit();
        System.out.println("User stored: " + user.getId());
    }

    public void addSaleToUser(Integer userId, Integer saleId) {
        User user = db.find(User.class, userId);
        Sale sale = db.find(Sale.class, saleId);

        addSaleToUser(user, sale);
    }

    public void addSaleToUser(User user, Sale sale) {
        db.getTransaction().begin();
        user.getPublications().add(sale);
        sale.setUser(user);
        db.merge(sale);
        db.merge(user);
        db.getTransaction().commit();
        System.out.println("Sale added to user: " + user.getId());
    }

    public void storeSale(Sale sale) {
        db.getTransaction().begin();
        db.persist(sale);
        db.getTransaction().commit();
        System.out.println("Sale created: " + sale.getId());
    }

    public void storeVehicle(Vehicle vehicle) {
        db.getTransaction().begin();
        db.persist(vehicle);
        db.getTransaction().commit();
        System.out.println("Vehicle stored: " + vehicle.getLicensePlate());
    }

    public void addVehicleToSale(Integer saleId, String licensePlate) {
        Sale sale = db.find(Sale.class, saleId);
        Vehicle vehicle = db.find(Vehicle.class, licensePlate);

        addVehicleToSale(sale, vehicle);
    }

    public void addVehicleToSale(Sale sale, Vehicle vehicle) {
        db.getTransaction().begin();
        sale.setVehicle(vehicle);
        vehicle.setListing(sale);
        db.merge(vehicle);
        db.merge(sale);
        db.getTransaction().commit();
        System.out.println("Vehicle added to sale: " + sale.getId());
    }

    public Sale getSaleById(Integer saleId) {
        return db.find(Sale.class, saleId);
    }

    public User getUserById(Integer userId) {
        return db.find(User.class, userId);
    }

    public User getUserByEmail(String email) {
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
        return db.find(Vehicle.class, licensePlate);
    }

    public List<User> getAllUsers() {
        return db.createQuery("SELECT u FROM User u ORDER BY u.id", User.class).getResultList();
    }

    public List<Sale> getAllSales() {
        return db.createQuery("SELECT s FROM Sale s ORDER BY s.id", Sale.class).getResultList();
    }

    public List<Vehicle> getAllVehicles() {
        return db.createQuery("SELECT v FROM Vehicle v ORDER BY v.licensePlate", Vehicle.class).getResultList();
    }

    public void deleteSale(Integer saleId) {
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
        Vehicle vehicle = db.find(Vehicle.class, licensePlate);
        if (vehicle != null) {
            db.getTransaction().begin();

            if (vehicle.getListing() != null) {
                vehicle.getListing().setVehicle(null);
                vehicle.setListing(null);
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
}
