package eus.ehu.init;

import com.opencsv.CSVReader;
import eus.ehu.businesslogic.BlManager;
import eus.ehu.domain.ListingStatus;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.User;
import eus.ehu.domain.Vehicle;

import java.io.InputStreamReader;
import java.util.UUID;

public class DefaultUserInitializer {

    public static void main(String[] args) {
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
                bl.storeUser(defaultUser);
            }

            User fetchedUser = bl.getUserByEmail("default@gmail.com");
            
            if (fetchedUser != null) {
                try (CSVReader reader = new CSVReader(new InputStreamReader(
                        DefaultUserInitializer.class.getResourceAsStream("/vehicles.csv")))) {
                    
                    String[] line;
                    reader.readNext(); 

                    while ((line = reader.readNext()) != null) {
                        String brand = line[0].trim();
                        String model = line[1].trim();
                        int year = Integer.parseInt(line[2].trim());
                        int mileage = Integer.parseInt(line[3].trim());
                        int horsepower = Integer.parseInt(line[4].trim());
                        double price = Double.parseDouble(line[5].trim());
                        
                        String motorRaw = line[6].trim().toUpperCase();
                        MotorType motor = MotorType.valueOf(motorRaw);

                        String formattedBrand = brand.toLowerCase().replace(" ", "-");
                        String expectedImagePath = "/images/cars/" + formattedBrand + ".jpg";
                        String finalImagePath;

                        if (DefaultUserInitializer.class.getResource(expectedImagePath) != null) {
                            finalImagePath = expectedImagePath;
                        } else {
                            finalImagePath = "/images/cars/default_car.jpg";
                        }

                        String licensePlate = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                        Sale sale = new Sale.Builder()
                                .description(brand + " " + model)
                                .status(ListingStatus.ACTIVE)
                                .user(fetchedUser)
                                .fixedPrice(price)
                                .build();

                        Vehicle vehicle = new Vehicle.Builder(licensePlate)
                                .make(brand)
                                .model(model)
                                .year(year)
                                .mileage(mileage)
                                .horsepower(horsepower)
                                .motorType(motor) 
                                .imagePath(finalImagePath)
                                .listing(sale)
                                .build();

                        sale.setVehicle(vehicle);
                        bl.storeSale(sale);
                    }
                    System.out.println("Vehicles stored successfully from CSV.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bl != null) bl.close();
        }
    }
}