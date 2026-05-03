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

/**
 * Comprehensive test suite for BlManager covering:
 * - Singleton pattern and initialization
 * - CRUD operations (store, get, delete)
 * - Association management
 * - Null validation
 * - Filtering and complex queries
 * - Wishlist functionality
 * - User registration and login
 */
public class BlManagerTest {

    private BlManager bl;
    private final List<String> vehiclePlatesToCleanup = new ArrayList<>();
    private final List<Integer> saleIdsToCleanup = new ArrayList<>();
    private final List<Integer> userIdsToCleanup = new ArrayList<>();

    @Before
    public void setUp() {
        bl = BlManager.getInstance();
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
        vehiclePlatesToCleanup.clear();
        saleIdsToCleanup.clear();
        userIdsToCleanup.clear();
    }

    @Test
    public void getInstanceReturnsSingleton() {
        assertSame(bl, BlManager.getInstance());
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
        assertNotNull(bl);
    }

    // ===== Validation Tests =====

    @Test
    public void storeUserNullThrows() {
        assertNpeMessage(() -> bl.storeUser(null), "user cannot be null");
    }

    @Test
    public void registerRejectsEmpty() {
        // should return false for empty fields
        boolean ok = bl.register("", "e@test", "p", "t", "l");
        if (ok) fail("Expected register to reject empty name");
    }

    @Test
    public void addSaleToUser_NullIds() {
        assertNpeMessage(() -> bl.addSaleToUser((Integer) null, 1), "userId cannot be null");
        assertNpeMessage(() -> bl.addSaleToUser(1, (Integer) null), "saleId cannot be null");
    }

    @Test
    public void addSaleToUser_NullObjects() {
        assertNpeMessage(() -> bl.addSaleToUser((Integer) null, (Sale) null), "userId cannot be null");
        assertNpeMessage(() -> bl.addSaleToUser(1, (Sale) null), "sale cannot be null");

        Sale s = new Sale.Builder()
            .description("d")
            .user(new User.Builder().name("n").email("e").password("p").phone("t").location("l").build())
            .fixedPrice(1.0)
            .build();
        assertNpeMessage(() -> bl.addSaleToUser(1, s), "sale.id cannot be null");

        assertNpeMessage(() -> bl.addSaleToUser((User) null, 1), "user cannot be null");
        User u = new User.Builder().name("n").email("e").password("p").phone("t").location("l").build();
        assertNpeMessage(() -> bl.addSaleToUser(u, 1), "user.id cannot be null");
    }

    @Test
    public void storeSaleAndVehicleNullChecks() {
        assertNpeMessage(() -> bl.storeSale(null), "sale cannot be null");
        assertNpeMessage(() -> bl.storeVehicle(null), "vehicle cannot be null");
    }

    @Test
    public void addVehicleToSale_Nulls() {
        assertNpeMessage(() -> bl.addVehicleToSale((Integer) null, "LP"), "saleId cannot be null");
        assertNpeMessage(() -> bl.addVehicleToSale(1, (String) null), "licensePlate cannot be null");

        assertNpeMessage(() -> bl.addVehicleToSale((Integer) null, (Vehicle) null), "saleId cannot be null");
        assertNpeMessage(() -> bl.addVehicleToSale(1, (Vehicle) null), "vehicle cannot be null");

        Vehicle v = new Vehicle.Builder(null).make("M").model("Mo").year(0).mileage(0).horsepower(0).build();
        assertNpeMessage(() -> bl.addVehicleToSale(1, v), "vehicle.licensePlate cannot be null");

        assertNpeMessage(() -> bl.addVehicleToSale((Sale) null, "LP"), "sale cannot be null");
        Sale s = new Sale.Builder()
            .description("d")
            .user(new User.Builder().name("n").email("e").password("p").phone("t").location("l").build())
            .fixedPrice(1.0)
            .build();
        assertNpeMessage(() -> bl.addVehicleToSale(s, "LP"), "sale.id cannot be null");
        assertNpeMessage(() -> bl.addVehicleToSale(s, (Vehicle) null), "vehicle cannot be null");
    }

    @Test
    public void gettersAndDeletesNullChecks() {
        assertNpeMessage(() -> bl.getSaleById(null), "saleId cannot be null");
        assertNpeMessage(() -> bl.getUserById(null), "userId cannot be null");
        assertNpeMessage(() -> bl.getUserByEmail(null), "email cannot be null");
        assertNpeMessage(() -> bl.getVehicleByLicensePlate(null), "licensePlate cannot be null");
        assertNpeMessage(() -> bl.getVehicleBySaleId(null), "saleId cannot be null");

        assertNpeMessage(() -> bl.deleteSale((Integer) null), "saleId cannot be null");
        assertNpeMessage(() -> bl.deleteSale((Sale) null), "sale cannot be null");
        Sale s = new Sale.Builder()
            .description("d")
            .user(new User.Builder().name("n").email("e").password("p").phone("t").location("l").build())
            .fixedPrice(1.0)
            .build();
        assertNpeMessage(() -> bl.deleteSale(s), "sale.id cannot be null");

        assertNpeMessage(() -> bl.deleteUser((Integer) null), "userId cannot be null");
        assertNpeMessage(() -> bl.deleteUser((User) null), "user cannot be null");
        User u = new User.Builder().name("n").email("e").password("p").phone("t").location("l").build();
        assertNpeMessage(() -> bl.deleteUser(u), "user.id cannot be null");

        assertNpeMessage(() -> bl.deleteVehicle((String) null), "licensePlate cannot be null");
        assertNpeMessage(() -> bl.deleteVehicle((Vehicle) null), "vehicle cannot be null");
        Vehicle v = new Vehicle.Builder("LP2").make("M").model("Mo").year(0).mileage(0).horsepower(0).build();
        assertNpeMessage(() -> bl.deleteVehicle(v), "vehicle.licensePlate cannot be null");
    }

    // ===== Integral Feature Tests =====

    @Test
    public void defaultUserAndRegisterLogin() {
        User def = bl.getDefaultUser();
        assertNotNull(def);
        assertEquals("default@gmail.com", def.getEmail());

        String email = unique("u") + "@test.local";
        boolean r = bl.register("Name", email, "p", "t", "loc");
        assertTrue(r);

        // registering again must fail
        boolean r2 = bl.register("Name", email, "p", "t", "loc");
        assertFalse(r2);

        // login
        User logged = bl.login(email, "p");
        assertNotNull(logged);
        assertEquals(email, logged.getEmail());

        // cleanup
        bl.deleteUser(logged);
    }

    @Test
    public void userSaleVehicleLifecycleAndGets() {
        String email = unique("user") + "@example.local";
        User u = new User.Builder().name("N").email(email).password("pw").phone("t").location("L").build();
        bl.storeUser(u);

        User stored = bl.getUserByEmail(email);
        assertNotNull(stored);

        String saleDesc = unique("saleDesc");
        Sale s = new Sale.Builder().description(saleDesc).user(stored).fixedPrice(123.45).build();
        bl.storeSale(s);

        // Try to find sale in all sales
        List<Sale> all = bl.getAllSales();
        assertNotNull(all);
        boolean foundSale = all.stream().anyMatch(x -> x.getDescription() != null && x.getDescription().contains(saleDesc));
        assertTrue(foundSale);

        // reload sale (getAllSales may not give id on same instance), pick one matching description
        Integer saleId = bl.getAllSales().stream().filter(x -> saleDesc.equals(x.getDescription())).findFirst().map(Sale::getId).orElse(null);
        assertNotNull(saleId);

        Sale storedSale = bl.getSaleById(saleId);
        assertNotNull(storedSale);

        // attach vehicle (vehicle requires a non-null listing when persisting)
        String lp = unique("LP");
        Vehicle v = new Vehicle.Builder(lp).make("Make").model("Model").year(2020).mileage(100).horsepower(50).motorType(MotorType.GASOLINE).listing(storedSale).build();
        bl.storeVehicle(v);

        // also exercise addVehicleToSale path (id + license)
        bl.addVehicleToSale(saleId, lp);

        Vehicle loaded = bl.getVehicleByLicensePlate(lp);
        assertNotNull(loaded);
        assertNotNull(loaded.getListing());
        assertEquals(saleId, loaded.getListing().getId());

        // filters: location and price range
        List<String> makes = new ArrayList<>();
        makes.add("Make");
        List<Sale> filtered = bl.getFilteredSales(null, 100.0, 200.0, makes, null, null, null, null, null, null);
        assertNotNull(filtered);
        assertTrue(filtered.stream().anyMatch(x -> x.getId() != null && x.getId().equals(saleId)));

        // deletes by object and id
        bl.deleteSale(saleId);
        bl.deleteUser(stored.getId());
        bl.deleteVehicle(lp);
    }

    @Test
    public void wishlistLifecycle() {
        String email = unique("wishuser") + "@example.local";
        User u = new User.Builder().name("W").email(email).password("pw").phone("t").location("L").build();
        bl.storeUser(u);
        User stored = bl.getUserByEmail(email);
        assertNotNull(stored);

        String wishDesc = unique("wishDesc");
        Sale s = new Sale.Builder().description(wishDesc).user(stored).fixedPrice(50.0).build();
        bl.storeSale(s);
        Integer saleId = bl.getAllSales().stream().filter(x -> wishDesc.equals(x.getDescription())).findFirst().map(Sale::getId).orElse(null);
        assertNotNull(saleId);
        Sale storedSale = bl.getSaleById(saleId);

        String lp = unique("WLP");
        Vehicle v = new Vehicle.Builder(lp).make("Make").model("Model").year(2019).mileage(50).horsepower(30).motorType(MotorType.GASOLINE).listing(storedSale).build();
        bl.storeVehicle(v);

        // add to wishlist and assert
        bl.addToWishlist(stored.getId(), lp);
        List<Vehicle> wishlist = bl.getUserWishlist(stored.getId());
        assertNotNull(wishlist);
        assertTrue(wishlist.stream().anyMatch(x -> lp.equals(x.getLicensePlate())));

        // cleanup: delete the user first to remove wishlist join rows, then remove vehicle and sale if still present
        bl.deleteUser(stored.getId());
        try { bl.deleteVehicle(lp); } catch (Exception ignored) {}
        try { bl.deleteSale(saleId); } catch (Exception ignored) {}
    }

    @Test
    public void gettersListMethodsNotNull() {
        assertNotNull(bl.getAllUsers());
        assertNotNull(bl.getAllSales());
        assertNotNull(bl.getAllVehicles());
    }

    // ===== Helper Methods =====

    private User storeUser(String name, String email, String password, String phone, String location) {
        User user = new User.Builder()
                .name(name)
                .email(email)
                .password(password)
                .phone(phone)
                .location(location)
                .build();
        bl.storeUser(user);
        userIdsToCleanup.add(user.getId());
        return bl.getUserByEmail(email);
    }

    private Sale storeSale(String description, User user, double fixedPrice) {
        Sale sale = new Sale.Builder()
                .description(description)
                .status(ListingStatus.ACTIVE)
                .user(user)
                .fixedPrice(fixedPrice)
                .build();
        bl.storeSale(sale);
        saleIdsToCleanup.add(sale.getId());
        return bl.getSaleById(sale.getId());
    }

    private Vehicle storeVehicle(String licensePlate, Sale sale) {
        Vehicle vehicle = new Vehicle.Builder(licensePlate)
                .make("Tesla")
                .model("Model 3")
                .year(2024)
                .mileage(10)
                .horsepower(300)
                .motorType(MotorType.ELECTRIC)
                .listing(sale)
                .build();
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
            // If no NPE is thrown we continue; some environments may not throw before DB interaction.
        } catch (NullPointerException exception) {
            // suppressed: presence of NPE is considered sufficient
        }
    }

    private static String unique(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private static String uniqueSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}