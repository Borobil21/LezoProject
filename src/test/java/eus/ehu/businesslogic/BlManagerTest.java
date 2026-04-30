package eus.ehu.businesslogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eus.ehu.domain.ListingStatus;
import eus.ehu.domain.MotorType;
import eus.ehu.domain.Sale;
import eus.ehu.domain.User;
import eus.ehu.domain.Vehicle;

public class BlManagerTest {

    private BlManager bl;
    private final List<String> vehiclePlatesToCleanup = new ArrayList<>();
    private final List<Integer> saleIdsToCleanup = new ArrayList<>();
    private final List<Integer> userIdsToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        bl = new BlManager();
        bl.getDefaultUser();
    }

    @After
    public void tearDown() {
        for (String licensePlate : vehiclePlatesToCleanup) {
            tryDeleteVehicle(licensePlate);
        }
        for (Integer saleId : saleIdsToCleanup) {
            tryDeleteSale(saleId);
        }
        for (Integer userId : userIdsToCleanup) {
            tryDeleteUser(userId);
        }
        if (bl != null) {
            bl.close();
        }
        vehiclePlatesToCleanup.clear();
        saleIdsToCleanup.clear();
        userIdsToCleanup.clear();
    }

    @Test
    public void constructorRejectsNullDbManager() {
        assertNpeMessage(() -> new BlManager(null), "dbManager cannot be null");
    }

    @Test
    public void getDefaultUserReturnsPersistedUserAndCachesIt() {
        User first = bl.getDefaultUser();
        User second = bl.getDefaultUser();

        assertNotNull(first);
        assertEquals("default@gmail.com", first.getEmail());
        assertSame(first, second);
    }

    @Test
    public void registerStoresAndLetsYouLogin() {
        String suffix = uniqueSuffix();
        String email = "bl-register-" + suffix + "@test.com";

        assertTrue(bl.register("Register User", email, "secret", "123456", "Lezo"));

        User registered = bl.getUserByEmail(email);
        assertNotNull(registered);
        assertEquals(email, registered.getEmail());
        assertEquals("Lezo", registered.getLocation());
        assertNotNull(registered.getId());
        userIdsToCleanup.add(registered.getId());

        assertNotNull(bl.login(email, "secret"));
        assertNull(bl.login(email, "wrong"));
        assertNull(bl.login("", "secret"));
    }

    @Test
    public void storeAndLookupMethodsWorkAgainstDatabase() {
        User user = storeUser("Lookup User", "lookup-" + uniqueSuffix() + "@test.com", "secret", "123", "Lezo");
        Sale sale = storeSale("Lookup sale", user, 12000.0);
        Vehicle vehicle = storeVehicle("LP-" + uniqueSuffix(), sale);

        assertEquals(user.getId(), bl.getUserById(user.getId()).getId());
        assertEquals(user.getEmail(), bl.getUserByEmail(user.getEmail()).getEmail());
        assertEquals(sale.getId(), bl.getSaleById(sale.getId()).getId());
        assertEquals(vehicle.getLicensePlate(), bl.getVehicleByLicensePlate(vehicle.getLicensePlate()).getLicensePlate());
        assertEquals(vehicle.getLicensePlate(), bl.getVehicleBySaleId(sale.getId()).getLicensePlate());
    }

    @Test
    public void associationMethodsUpdateRelationsInDatabase() {
        User seller = storeUser("Seller", "seller-" + uniqueSuffix() + "@test.com", "secret", "123", "Lezo");
        Sale sale = storeSale("Association sale", bl.getDefaultUser(), 18000.0);
        Vehicle vehicle = storeVehicle("ASSOC-" + uniqueSuffix(), sale);

        bl.addSaleToUser(seller.getId(), sale.getId());
        bl.addSaleToUser(seller.getId(), sale);
        bl.addSaleToUser(seller, sale.getId());
        bl.addSaleToUser(seller, sale);

        bl.addVehicleToSale(sale.getId(), vehicle.getLicensePlate());
        bl.addVehicleToSale(sale.getId(), vehicle);
        bl.addVehicleToSale(sale, vehicle.getLicensePlate());
        bl.addVehicleToSale(sale, vehicle);

        Sale updatedSale = bl.getSaleById(sale.getId());
        assertNotNull(updatedSale.getUser());
        assertEquals(seller.getEmail(), updatedSale.getUser().getEmail());
        assertNotNull(updatedSale.getVehicle());
        assertEquals(vehicle.getLicensePlate(), updatedSale.getVehicle().getLicensePlate());
        assertEquals(vehicle.getLicensePlate(), bl.getVehicleBySaleId(sale.getId()).getLicensePlate());
    }

    @Test
    public void getAllMethodsIncludeInsertedEntities() {
        User user = storeUser("All User", "all-" + uniqueSuffix() + "@test.com", "secret", "123", "Lezo");
        Sale sale = storeSale("All sale", user, 14500.0);
        Vehicle vehicle = storeVehicle("ALL-" + uniqueSuffix(), sale);

        assertTrue(bl.getAllUsers().stream().anyMatch(item -> user.getEmail().equals(item.getEmail())));
        assertTrue(bl.getAllSales().stream().anyMatch(item -> sale.getId().equals(item.getId())));
        assertTrue(bl.getAllVehicles().stream().anyMatch(item -> vehicle.getLicensePlate().equals(item.getLicensePlate())));
    }

    @Test
    public void deleteMethodsRemovePersistedEntities() {
        User userById = storeUser("Delete User 1", "delete1-" + uniqueSuffix() + "@test.com", "secret", "123", "Lezo");
        Sale saleById = storeSale("Delete sale 1", userById, 15000.0);
        Vehicle vehicleByPlate = storeVehicle("DEL-PLATE-" + uniqueSuffix(), saleById);

        bl.deleteVehicle(vehicleByPlate.getLicensePlate());
        assertNull(bl.getVehicleByLicensePlate(vehicleByPlate.getLicensePlate()));

        bl.deleteSale(saleById.getId());
        assertNull(bl.getSaleById(saleById.getId()));

        bl.deleteUser(userById.getId());
        assertNull(bl.getUserByEmail(userById.getEmail()));

        User userByObject = storeUser("Delete User 2", "delete2-" + uniqueSuffix() + "@test.com", "secret", "123", "Lezo");
        Sale saleByObject = storeSale("Delete sale 2", userByObject, 15500.0);
        Vehicle vehicleByObject = storeVehicle("DEL-OBJ-" + uniqueSuffix(), saleByObject);

        bl.deleteVehicle(vehicleByObject);
        assertNull(bl.getVehicleByLicensePlate(vehicleByObject.getLicensePlate()));

        bl.deleteSale(saleByObject);
        assertNull(bl.getSaleById(saleByObject.getId()));

        bl.deleteUser(userByObject);
        assertNull(bl.getUserByEmail(userByObject.getEmail()));
    }

    @Test
    public void getFilteredSalesForwardsRealFilters() {
        Sale sale = storeSale("Filter sale", bl.getDefaultUser(), 22222.0);
        Vehicle vehicle = storeVehicle("FILTER-" + uniqueSuffix(), sale);
        bl.addVehicleToSale(sale, vehicle);

        List<Sale> filtered = bl.getFilteredSales(
                null,
                20000.0,
                23000.0,
                List.of("Tesla"),
                null,
                2020,
                2025,
                0,
                100000,
                MotorType.ELECTRIC);

        assertTrue(filtered.stream().anyMatch(item -> sale.getId().equals(item.getId())));
    }

    @Test
    public void closeDelegatesToDatabase() {
        bl.close();
        bl = null;
    }

    private User storeUser(String name, String email, String password, String phone, String location) {
        User user = new User(name, email, password, phone, location);
        bl.storeUser(user);
        userIdsToCleanup.add(user.getId());
        return bl.getUserByEmail(email);
    }

    private Sale storeSale(String description, User user, double fixedPrice) {
        Sale sale = new Sale(description, ListingStatus.ACTIVE, user, fixedPrice);
        bl.storeSale(sale);
        saleIdsToCleanup.add(sale.getId());
        return bl.getSaleById(sale.getId());
    }

    private Vehicle storeVehicle(String licensePlate, Sale sale) {
        Vehicle vehicle = new Vehicle(licensePlate, "Tesla", "Model 3", 2024, 10, 300, MotorType.ELECTRIC, sale);
        bl.storeVehicle(vehicle);
        vehiclePlatesToCleanup.add(vehicle.getLicensePlate());
        return bl.getVehicleByLicensePlate(licensePlate);
    }

    private void tryDeleteVehicle(String licensePlate) {
        try {
            Vehicle vehicle = bl.getVehicleByLicensePlate(licensePlate);
            if (vehicle != null) {
                bl.deleteVehicle(vehicle);
            }
        } catch (Exception ignored) {
        }
    }

    private void tryDeleteSale(Integer saleId) {
        try {
            Sale sale = bl.getSaleById(saleId);
            if (sale != null) {
                bl.deleteSale(sale);
            }
        } catch (Exception ignored) {
        }
    }

    private void tryDeleteUser(Integer userId) {
        try {
            User user = bl.getUserById(userId);
            if (user != null) {
                bl.deleteUser(user);
            }
        } catch (Exception ignored) {
        }
    }

    private static void assertNpeMessage(Runnable action, String expectedMessage) {
        try {
            action.run();
            fail("Expected NullPointerException");
        } catch (NullPointerException exception) {
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    private static String uniqueSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}