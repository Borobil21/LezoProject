package eus.ehu.businesslogic;

import java.util.List;

import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.User;
import eus.ehu.domain.Vehicle;

public interface BlInterface {

	void storeUser(User user);

	boolean register(String name, String email, String password, String phone, String location);

	User login(String email, String password);

	void addSaleToUser(Integer userId, Integer saleId);

	void addSaleToUser(Integer userId, Sale sale);

	void addSaleToUser(User user, Integer saleId);

	void addSaleToUser(User user, Sale sale);

	void storeSale(Sale sale);

	void storeVehicle(Vehicle vehicle);

	void addVehicleToSale(Integer saleId, String licensePlate);

	void addVehicleToSale(Integer saleId, Vehicle vehicle);

	void addVehicleToSale(Sale sale, String licensePlate);

	void addVehicleToSale(Sale sale, Vehicle vehicle);

	Sale getSaleById(Integer saleId);

	User getUserById(Integer userId);

	User getUserByEmail(String email);

	Vehicle getVehicleByLicensePlate(String licensePlate);

	Vehicle getVehicleBySaleId(Integer saleId);

	List<User> getAllUsers();

	List<Sale> getAllSales();

	List<Vehicle> getAllVehicles();

	void deleteSale(Integer saleId);

	void deleteSale(Sale sale);

	void deleteUser(Integer userId);

	void deleteUser(User user);

	void deleteVehicle(String licensePlate);

	void deleteVehicle(Vehicle vehicle);

	List<Sale> getFilteredSales(String location, Double minPrice, Double maxPrice,
        List<String> makes, String model,
        Integer minYear, Integer maxYear,
        Integer minMileage, Integer maxMileage,
        MotorType motorType);
}
