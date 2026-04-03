package eus.ehu.businesslogic;

import java.util.List;

import eus.ehu.data_access.DbAccessManager;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.User;
import eus.ehu.domain.Vehicle;

public class BlManager implements BlInterface {

    private final DbAccessManager dbManager;
    private User defaultUser;

    public User getDefaultUser() {
        if (defaultUser == null) {
            defaultUser = loadOrCreateDefaultUser();
        }
        return defaultUser;
    }

    public BlManager() {
        this.dbManager = new DbAccessManager();
    }

    public BlManager(DbAccessManager dbManager) {
        if (dbManager == null) {
            throw new NullPointerException("dbManager cannot be null");
        }
        this.dbManager = dbManager;
    }

    private User loadOrCreateDefaultUser() {
        User user = dbManager.getUserByEmail("default@gmail.com");
        if (user != null) {
            return user;
        }

        User defaultUser = new User("Default User", "default@gmail.com", "1234", "999999999", "Lezo");
        storeUser(defaultUser);
        return dbManager.getUserByEmail("default@gmail.com");
    }

    @Override
    public void storeUser(User user) {
        if (user == null) {
            throw new NullPointerException("user cannot be null");
        }
        dbManager.storeUser(user);
    }

    @Override
    public boolean register(String name, String email, String password, String phone, String location) {
        if (name == null || email == null || password == null || phone == null || location == null) {
            throw new NullPointerException("registration fields cannot be null");
        }
        if (dbManager.getUserByEmail(email) != null) {
            return false;
        }

        storeUser(new User(name, email, password, phone, location));
        return true;
    }

    @Override
    public User login(String email, String password) {
        if (email == null || password == null) {
            throw new NullPointerException("email and password cannot be null");
        }

        User user = dbManager.getUserByEmail(email);
        if (user == null) {
            return null;
        }
        return password.equals(user.getPassword()) ? user : null;
    }

    @Override
    public void addSaleToUser(Integer userId, Integer saleId) {
        if (userId == null) {
            throw new NullPointerException("userId cannot be null");
        }
        if (saleId == null) {
            throw new NullPointerException("saleId cannot be null");
        }
        dbManager.addSaleToUser(userId, saleId);
    }

    @Override
    public void addSaleToUser(Integer userId, Sale sale) {
        if (userId == null) {
            throw new NullPointerException("userId cannot be null");
        }
        if (sale == null) {
            throw new NullPointerException("sale cannot be null");
        }
        if (sale.getId() == null) {
            throw new NullPointerException("sale.id cannot be null");
        }
        dbManager.addSaleToUser(userId, sale.getId());
    }

    @Override
    public void addSaleToUser(User user, Integer saleId) {
        if (user == null) {
            throw new NullPointerException("user cannot be null");
        }
        if (user.getId() == null) {
            throw new NullPointerException("user.id cannot be null");
        }
        if (saleId == null) {
            throw new NullPointerException("saleId cannot be null");
        }
        dbManager.addSaleToUser(user.getId(), saleId);
    }

    @Override
    public void addSaleToUser(User user, Sale sale) {
        if (user == null) {
            throw new NullPointerException("user cannot be null");
        }
        if (user.getId() == null) {
            throw new NullPointerException("user.id cannot be null");
        }
        if (sale == null) {
            throw new NullPointerException("sale cannot be null");
        }
        if (sale.getId() == null) {
            throw new NullPointerException("sale.id cannot be null");
        }
        dbManager.addSaleToUser(user.getId(), sale.getId());
    }

    @Override
    public void storeSale(Sale sale) {
        if (sale == null) {
            throw new NullPointerException("sale cannot be null");
        }
        dbManager.storeSale(sale);
    }

    @Override
    public void storeVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new NullPointerException("vehicle cannot be null");
        }
        dbManager.storeVehicle(vehicle);
    }

    @Override
    public void addVehicleToSale(Integer saleId, String licensePlate) {
        if (saleId == null) {
            throw new NullPointerException("saleId cannot be null");
        }
        if (licensePlate == null) {
            throw new NullPointerException("licensePlate cannot be null");
        }
        dbManager.addVehicleToSale(saleId, licensePlate);
    }

    @Override
    public void addVehicleToSale(Integer saleId, Vehicle vehicle) {
        if (saleId == null) {
            throw new NullPointerException("saleId cannot be null");
        }
        if (vehicle == null) {
            throw new NullPointerException("vehicle cannot be null");
        }
        if (vehicle.getLicensePlate() == null) {
            throw new NullPointerException("vehicle.licensePlate cannot be null");
        }
        dbManager.addVehicleToSale(saleId, vehicle.getLicensePlate());
    }

    @Override
    public void addVehicleToSale(Sale sale, String licensePlate) {
        if (sale == null) {
            throw new NullPointerException("sale cannot be null");
        }
        if (sale.getId() == null) {
            throw new NullPointerException("sale.id cannot be null");
        }
        if (licensePlate == null) {
            throw new NullPointerException("licensePlate cannot be null");
        }
        dbManager.addVehicleToSale(sale.getId(), licensePlate);
    }

    @Override
    public void addVehicleToSale(Sale sale, Vehicle vehicle) {
        if (sale == null) {
            throw new NullPointerException("sale cannot be null");
        }
        if (sale.getId() == null) {
            throw new NullPointerException("sale.id cannot be null");
        }
        if (vehicle == null) {
            throw new NullPointerException("vehicle cannot be null");
        }
        if (vehicle.getLicensePlate() == null) {
            throw new NullPointerException("vehicle.licensePlate cannot be null");
        }
        dbManager.addVehicleToSale(sale.getId(), vehicle.getLicensePlate());
    }

    @Override
    public Sale getSaleById(Integer saleId) {
        if (saleId == null) {
            throw new NullPointerException("saleId cannot be null");
        }
        return dbManager.getSaleById(saleId);
    }

    @Override
    public User getUserById(Integer userId) {
        if (userId == null) {
            throw new NullPointerException("userId cannot be null");
        }
        return dbManager.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        if (email == null) {
            throw new NullPointerException("email cannot be null");
        }
        return dbManager.getUserByEmail(email);
    }

    @Override
    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        if (licensePlate == null) {
            throw new NullPointerException("licensePlate cannot be null");
        }
        return dbManager.getVehicleByLicensePlate(licensePlate);
    }

    @Override
    public List<User> getAllUsers() {
        return dbManager.getAllUsers();
    }

    @Override
    public List<Sale> getAllSales() {
        return dbManager.getAllSales();
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return dbManager.getAllVehicles();
    }

    @Override
    public void deleteSale(Integer saleId) {
        if (saleId == null) {
            throw new NullPointerException("saleId cannot be null");
        }
        dbManager.deleteSale(saleId);
    }

    @Override
    public void deleteSale(Sale sale) {
        if (sale == null) {
            throw new NullPointerException("sale cannot be null");
        }
        if (sale.getId() == null) {
            throw new NullPointerException("sale.id cannot be null");
        }
        dbManager.deleteSale(sale.getId());
    }

    @Override
    public void deleteUser(Integer userId) {
        if (userId == null) {
            throw new NullPointerException("userId cannot be null");
        }
        dbManager.deleteUser(userId);
    }

    @Override
    public void deleteUser(User user) {
        if (user == null) {
            throw new NullPointerException("user cannot be null");
        }
        if (user.getId() == null) {
            throw new NullPointerException("user.id cannot be null");
        }
        dbManager.deleteUser(user.getId());
    }

    @Override
    public void deleteVehicle(String licensePlate) {
        if (licensePlate == null) {
            throw new NullPointerException("licensePlate cannot be null");
        }
        dbManager.deleteVehicle(licensePlate);
    }

    @Override
    public void deleteVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new NullPointerException("vehicle cannot be null");
        }
        if (vehicle.getLicensePlate() == null) {
            throw new NullPointerException("vehicle.licensePlate cannot be null");
        }
        dbManager.deleteVehicle(vehicle.getLicensePlate());
    }

    @Override
    public List<Sale> getFilteredSales(String location, Double minPrice, Double maxPrice,
            List<String> makes, String model,
            Integer minYear, Integer maxYear,
            Integer minMileage, Integer maxMileage,
            MotorType motorType) {
        return dbManager.getFilteredSales(location, minPrice, maxPrice, makes, model, minYear, maxYear, minMileage, maxMileage, motorType);
    }

    public void close() {
        dbManager.close();
    }
}
