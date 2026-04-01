package eus.ehu.data_access;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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

    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        return db.find(Vehicle.class, licensePlate);
    }

    public void deleteSale(Integer saleId) {
        Sale sale = db.find(Sale.class, saleId);
        if (sale != null) {
            db.getTransaction().begin();

            Vehicle vehicle = sale.getVehicle();
            if (vehicle != null) {
                vehicle.setListing(null);
                sale.setVehicle(null);
                db.remove(vehicle);
                System.out.println("Vehicle deleted with sale: " + vehicle.getLicensePlate());
            }

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

    // Method to get a user by email, used for login and registration checks
    public User getUserByEmail(String email) {
        try {
            return db.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            // If no user is found or there's an error, return null (indicates the email doesn't exist in our db)
            return null;
        }
    }

    //login
    public User login(String email, String password) {
    try {
        // Buscamos un usuario que tenga ese email y esa contraseña exacta
        return db.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :pass", User.class)
                 .setParameter("email", email)
                 .setParameter("pass", password)
                 .getSingleResult();
    } catch (Exception e) {
        // Si no encuentra a nadie (o la contraseña está mal), getSingleResult() lanza una excepción
        return null; 
    }
}
}
