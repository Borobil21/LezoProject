package eus.ehu.init;

import com.opencsv.CSVReader;
import eus.ehu.domain.Vehicle;
import eus.ehu.domain.Sale;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.User;
import eus.ehu.data_access.DbAccessManager;

import java.io.FileReader;

public class DatabaseSeeder {

    public void initDatabaseFromCSV() {
        DbAccessManager dbManager = DbAccessManager.getInstance();

        // 1. Verificamos si ya hay coches para no duplicar la base de datos
        if (!dbManager.getAllVehicles().isEmpty()) {
            System.out.println("La base de datos ya tiene datos. Omitiendo Seeder.");
            return;
        }
        
        // Recomendación: Recuperar un usuario "Admin" o "Concesionario" al que asignarle estos coches
        // User adminUser = dbManager.getUserByEmail("admin@pickauto.com");

        String rutaCSV = "src/main/resources/dataset_coches.csv";

        try (CSVReader reader = new CSVReader(new FileReader(rutaCSV))) {
            String[] linea;
            reader.readNext(); // Saltamos la cabecera del CSV

            while ((linea = reader.readNext()) != null) {
                // Mapeo básico desde el CSV (ajustar los índices según vuestro archivo)
                String licensePlate = linea[0]; // Usad un generador aleatorio si el CSV no tiene matrículas
                String brand = linea[1].trim();
                String model = linea[2];
                int year = Integer.parseInt(linea[3]);
                int mileage = Integer.parseInt(linea[4]);
                int horsepower = Integer.parseInt(linea[5]);
                double price = Double.parseDouble(linea[6]); // Asumiendo que tenéis precio en el CSV

                // 2. Creamos el Vehículo usando vuestro fantástico patrón Builder
                Vehicle vehicle = new Vehicle.Builder(licensePlate)
                        .make(brand)
                        .model(model)
                        .year(year)
                        .mileage(mileage)
                        .horsepower(horsepower)
                        .motorType(MotorType.GASOLINE) // Aquí podríais hacer un if según el CSV
                        .build();

                // 3. Estrategia 1: Asignamos la foto local según la marca
                switch (brand.toLowerCase()) {
                    case "tesla":
                        vehicle.setImagePath("/images/cars/tesla.jpg");
                        break;
                    case "bmw":
                        vehicle.setImagePath("/images/cars/bmw.jpg");
                        break;
                    case "mercedes-benz":
                        vehicle.setImagePath("/images/cars/mercedes.jpg");
                        break;
                    case "porsche":
                        vehicle.setImagePath("/images/cars/porsche.jpg");
                        break;
                    default:
                        vehicle.setImagePath("/images/cars/default_car.jpg");
                        break;
                }

                // 4. Creamos el Anuncio (Sale) usando el Builder y lo vinculamos al vehículo
                Sale sale = new Sale.Builder()
                    .fixedPrice(price)
                    .build();
                // sale.setLocation("Bilbao"); // Rellenar campos necesarios de Sale
                
                // Usamos vuestro propio método para vincularlos y guardarlos
                dbManager.storeSale(sale);
                dbManager.storeVehicle(vehicle);
                dbManager.addVehicleToSale(sale, vehicle); 
                
                // Si la relación requiere un usuario para no fallar:
                // dbManager.addSaleToUser(adminUser, sale);
            }
            
            System.out.println("¡Coches importados con éxito a PickAuto!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}