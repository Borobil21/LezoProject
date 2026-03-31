package eus.ehu.businesslogic;

import eus.ehu.data_access.DbAccessManager;
import eus.ehu.domain.Sale;
import eus.ehu.domain.User;
import eus.ehu.domain.Vehicle;

public class BlManager implements BlInterface {

    private final DbAccessManager dbManager;

    public BlManager() {
        this.dbManager = new DbAccessManager();
    }

    public BlManager(DbAccessManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void storeUser(User user) {
        dbManager.storeUser(user);
    }

    @Override
    public void addSaleToUser(Integer userId, Integer saleId) {
        dbManager.addSaleToUser(userId, saleId);
    }

    @Override
    public void addSaleToUser(Integer userId, Sale sale) {
        dbManager.addSaleToUser(userId, sale.getId());
    }

    @Override
    public void addSaleToUser(User user, Integer saleId) {
        dbManager.addSaleToUser(user.getId(), saleId);
    }

    @Override
    public void addSaleToUser(User user, Sale sale) {
        dbManager.addSaleToUser(user.getId(), sale.getId());
    }

    @Override
    public void storeSale(Sale sale) {
        dbManager.storeSale(sale);
    }

    @Override
    public void storeVehicle(Vehicle vehicle) {
        dbManager.storeVehicle(vehicle);
    }

    @Override
    public void addVehicleToSale(Integer saleId, String licensePlate) {
        dbManager.addVehicleToSale(saleId, licensePlate);
    }

    @Override
    public void addVehicleToSale(Integer saleId, Vehicle vehicle) {
        dbManager.addVehicleToSale(saleId, vehicle.getLicensePlate());
    }

    @Override
    public void addVehicleToSale(Sale sale, String licensePlate) {
        dbManager.addVehicleToSale(sale.getId(), licensePlate);
    }

    @Override
    public void addVehicleToSale(Sale sale, Vehicle vehicle) {
        dbManager.addVehicleToSale(sale.getId(), vehicle.getLicensePlate());
    }

    @Override
    public Sale getSaleById(Integer saleId) {
        return dbManager.getSaleById(saleId);
    }

    @Override
    public User getUserById(Integer userId) {
        return dbManager.getUserById(userId);
    }

    @Override
    public Vehicle getVehicleByLicensePlate(String licensePlate) {
        return dbManager.getVehicleByLicensePlate(licensePlate);
    }

    @Override
    public void deleteSale(Integer saleId) {
        dbManager.deleteSale(saleId);
    }

    @Override
    public void deleteSale(Sale sale) {
        dbManager.deleteSale(sale.getId());
    }

    @Override
    public void deleteUser(Integer userId) {
        dbManager.deleteUser(userId);
    }

    @Override
    public void deleteUser(User user) {
        dbManager.deleteUser(user.getId());
    }

    @Override
    public void deleteVehicle(String licensePlate) {
        dbManager.deleteVehicle(licensePlate);
    }

    @Override
    public void deleteVehicle(Vehicle vehicle) {
        dbManager.deleteVehicle(vehicle.getLicensePlate());
    }

    @Override
    public boolean register(String name, String email, String password, String phone, String location) {
        // Verify if the email is already in use
        if (dbManager.getUserByEmail(email) != null) {
            System.out.println("El registro falló: el email ya está en uso."); 
            //or trigger a message in the UI TO DO
            return false;
        }
        // If not, create a new user and store it in the database
        User newUser = new User(name, email, password, phone, location);
        try {
            dbManager.storeUser(newUser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
