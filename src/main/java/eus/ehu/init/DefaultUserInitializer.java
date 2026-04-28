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
            bl = BlManager.getInstance();
            User defaultUser = bl.getUserByEmail("default@gmail.com");

            if (defaultUser == null) {
                defaultUser = new User.Builder()
                        .name("Default User")
                        .email("default@gmail.com")
                        .password("1234")
                        .phone("999999999")
                        .location("Lezo")
                        .build();
                System.out.println("Storing default user: " + defaultUser.getName());
                bl.storeUser(defaultUser);
            } else {
                System.out.println("Default user already exists: " + defaultUser.getEmail());
            }

            User fetchedUser = bl.getUserByEmail("default@gmail.com");
            if (fetchedUser != null) {
                System.out.println("User verified in database: " + fetchedUser.getEmail());
                
                
                System.out.println("Generating test vehicle fleet.");

                
                Sale sale1 = new Sale.Builder()
                       .description("Tesla Model 3")
                        .status(ListingStatus.ACTIVE)
                        .user(fetchedUser)
                        .fixedPrice(45000.0)
                        .build();
                Vehicle v1 = new Vehicle.Builder("1111-AAA")
                        .make("Tesla")
                        .model("Model 3")
                        .year(2025)
                        .mileage(50)
                        .horsepower(350)
                        .motorType(MotorType.ELECTRIC)
                        .listing(sale1)
                        .build();
                sale1.setVehicle(v1);

                
                Sale sale2 = new Sale.Builder()
                        .description("BMW Serie 3")
                        .status(ListingStatus.ACTIVE)
                        .user(fetchedUser)
                        .fixedPrice(32000.0)
                        .build();
                Vehicle v2 = new Vehicle.Builder("2222-BBB")
                        .make("BMW")
                        .model("Serie 3")
                        .year(2022)
                        .mileage(45000)
                        .horsepower(190)
                        .motorType(MotorType.GASOLINE)
                        .listing(sale2)
                        .build();
                sale2.setVehicle(v2);

                
                Sale sale3 = new Sale.Builder()
                        .description("Mercedes C Class")
                        .status(ListingStatus.ACTIVE)
                        .user(fetchedUser)
                        .fixedPrice(15000.0)
                        .build();
                Vehicle v3 = new Vehicle.Builder("3333-CCC")
                        .make("Mercedes-Benz")
                        .model("Clase C")
                        .year(2015)
                        .mileage(120000)
                        .horsepower(170)
                        .motorType(MotorType.DIESEL)
                        .listing(sale3)
                        .build();
                sale3.setVehicle(v3);

                
                Sale sale4 = new Sale.Builder()
                        .description("Porsche 911")
                        .status(ListingStatus.ACTIVE)
                        .user(fetchedUser)
                        .fixedPrice(65000.0)
                        .build();
                Vehicle v4 = new Vehicle.Builder("4444-DDD")
                        .make("Porsche")
                        .model("911 Carrera")
                        .year(2004)
                        .horsepower(320)
                        .motorType(MotorType.GASOLINE)
                        .listing(sale4)
                        .build();
                sale4.setVehicle(v4);

                
                Sale sale5 = new Sale.Builder()
                        .description("Audi A3")
                        .status(ListingStatus.ACTIVE)
                        .user(fetchedUser)
                        .fixedPrice(8500.0)
                        .build();
                Vehicle v5 = new Vehicle.Builder("5555-EEE")
                        .make("Audi")
                        .model("A3")
                        .year(2009)
                        .mileage(210000)
                        .horsepower(105)
                        .motorType(MotorType.DIESEL)
                        .listing(sale5)
                        .build();
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