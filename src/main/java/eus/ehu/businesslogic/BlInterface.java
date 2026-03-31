package eus.ehu.businesslogic;

import eus.ehu.domain.Sale;
import eus.ehu.domain.User;
import eus.ehu.domain.Vehicle;

public interface BlInterface {

	void storeUser(User user);

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

	Vehicle getVehicleByLicensePlate(String licensePlate);

	void deleteSale(Integer saleId);

	void deleteSale(Sale sale);

	void deleteUser(Integer userId);

	void deleteUser(User user);

	void deleteVehicle(String licensePlate);

	void deleteVehicle(Vehicle vehicle);

}
