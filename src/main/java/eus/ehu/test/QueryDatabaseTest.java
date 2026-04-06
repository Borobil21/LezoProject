package eus.ehu.test;

import eus.ehu.businessLogic.BlManager;
import eus.ehu.domain.Sale;
import eus.ehu.domain.Vehicle;
import eus.ehu.domain.User;
import java.util.List;

public class QueryDatabaseTest {

    public static void main(String[] args) {
        printAllData();
        cleanDatabase();
    }

    public static void cleanDatabase() {
        System.out.println("=== Cleaning Database (keeping Default User) ===\n");
        BlManager bl = null;

        try {
            bl = new BlManager();
            User defaultUser = bl.getDefaultUser();
            String defaultEmail = defaultUser != null ? defaultUser.getEmail() : "default@gmail.com";

            // Delete all sales (which will cascade delete vehicles)
            System.out.println("Deleting all sales...");
            for (int i = 1; i <= 100; i++) {
                Sale sale = bl.getSaleById(i);
                if (sale != null) {
                    bl.deleteSale(i);
                } else {
                    break;
                }
            }
            // Delete all users except the default user (ID 3)
            System.out.println("Deleting all users except default user (" + defaultEmail + ")...");
            for (int i = 1; i <= 100; i++) {
                User user = bl.getUserById(i);
                if (user != null) {
                    if (!defaultEmail.equals(user.getEmail())) {
                        bl.deleteUser(i);
                    }
                } else if (i > 10) { // Stop searching after first gap
                    break;
                }
            }

            System.out.println("✓ Database cleaned successfully!\n");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (bl != null) {
                bl.close();
            }
        }
    }

    public static void printAllData() {
        System.out.println("=== Database Content ===\n");
        BlManager bl = null;

        try {
            bl = new BlManager();

            System.out.println("--- USERS ---");
            List<User> users = bl.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users found");
            } else {
                for (User user : users) {
                    System.out.println("\nUser ID: " + user.getId());
                    System.out.println("Name: " + user.getName());
                    System.out.println("Email: " + user.getEmail());
                    System.out.println("Phone: " + user.getPhone());
                    System.out.println("Location: " + user.getLocation());
                }
            }

            System.out.println("\n--- SALES ---");
            List<Sale> sales = bl.getAllSales();
            if (sales.isEmpty()) {
                System.out.println("No sales found");
            } else {
                for (Sale sale : sales) {
                    System.out.println("\nSale ID: " + sale.getId());
                    System.out.println("Description: " + sale.getDescription());
                    System.out.println("Price: " + sale.getFixedPrice());
                    System.out.println("Status: " + sale.getStatus());
                    if (sale.getUser() != null) {
                        System.out.println("User ID: " + sale.getUser().getId());
                    }
                    if (sale.getVehicle() != null) {
                        System.out.println("Vehicle License Plate: " + sale.getVehicle().getLicensePlate());
                    }
                }
            }

            System.out.println("\n--- VEHICLES ---");
            List<Vehicle> vehicles = bl.getAllVehicles();
            if (vehicles.isEmpty()) {
                System.out.println("No vehicles found");
            } else {
                for (Vehicle vehicle : vehicles) {
                    System.out.println("\nLicense Plate: " + vehicle.getLicensePlate());
                    System.out.println("Make: " + vehicle.getMake());
                    System.out.println("Model: " + vehicle.getModel());
                    System.out.println("Year: " + vehicle.getYear());
                    System.out.println("Mileage: " + vehicle.getMileage());
                    System.out.println("Horsepower: " + vehicle.getHorsepower());
                    System.out.println("Motor Type: " + vehicle.getMotorType());
                    if (vehicle.getListing() != null) {
                        System.out.println("Sale ID: " + vehicle.getListing().getId());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (bl != null) {
                bl.close();
            }
        }
    }

}
