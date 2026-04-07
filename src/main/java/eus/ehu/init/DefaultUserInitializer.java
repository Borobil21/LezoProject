package eus.ehu.init;

import eus.ehu.businesslogic.BlManager;
import eus.ehu.domain.ListingStatus;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.User;
import eus.ehu.domain.Vehicle;

public class DefaultUserInitializer {

    public static void main(String[] args) {
        System.out.println("=== Initializing Default User and Mock Cars ===\n");
        BlManager bl = null;

        try {
            bl = new BlManager();
            User defaultUser = bl.getUserByEmail("default@gmail.com");

            if (defaultUser == null) {
                defaultUser = new User("Default User", "default@gmail.com", "1234", "999999999", "Lezo");
                System.out.println("Storing default user: " + defaultUser.getName());
                bl.storeUser(defaultUser);
            } else {
                System.out.println("Default user already exists: " + defaultUser.getEmail());
            }

            User fetchedUser = bl.getUserByEmail("default@gmail.com");
            if (fetchedUser != null) {
                System.out.println("User verified in database: " + fetchedUser.getEmail());
                
                
                System.out.println("Generating test vehicle fleet.");

                
                Sale sale1 = new Sale("Tesla Model 3", ListingStatus.ACTIVE, fetchedUser, 45000.0);
                Vehicle v1 = new Vehicle("1111-AAA", "Tesla", "Model 3", 2025, 50, 350, MotorType.ELECTRIC, sale1);
                sale1.setVehicle(v1);

                
                Sale sale2 = new Sale("BMW Serie 3", ListingStatus.ACTIVE, fetchedUser, 32000.0);
                Vehicle v2 = new Vehicle("2222-BBB", "BMW", "Serie 3", 2022, 45000, 190, MotorType.GASOLINE, sale2);
                sale2.setVehicle(v2);

                
                Sale sale3 = new Sale("Mercedes C Class", ListingStatus.ACTIVE, fetchedUser, 15000.0);
                Vehicle v3 = new Vehicle("3333-CCC", "Mercedes-Benz", "Clase C", 2015, 120000, 170, MotorType.DIESEL, sale3);
                sale3.setVehicle(v3);

                
                Sale sale4 = new Sale("Porsche 911", ListingStatus.ACTIVE, fetchedUser, 65000.0);
                Vehicle v4 = new Vehicle("4444-DDD", "Porsche", "911 Carrera", 2004, 180000, 320, MotorType.GASOLINE, sale4);
                sale4.setVehicle(v4);

                
                Sale sale5 = new Sale("Audi A3", ListingStatus.ACTIVE, fetchedUser, 8500.0);
                Vehicle v5 = new Vehicle("5555-EEE", "Audi", "A3", 2009, 210000, 105, MotorType.DIESEL, sale5);
                sale5.setVehicle(v5);


                bl.storeSale(sale1);
                bl.storeSale(sale2);
                bl.storeSale(sale3);
                bl.storeSale(sale4);
                bl.storeSale(sale5);

                System.out.println("Vehicles stored successfully.\n");
              
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