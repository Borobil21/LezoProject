package eus.ehu.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class VehicleTest {

    @Test
    public void constructorAndGettersSetters() {
    User u = new User.Builder()
        .name("Alice")
        .email("a@example.com")
        .password("secret")
        .phone("600000000")
        .location("Bilbao")
        .build();
    Sale sale = new Sale.Builder()
        .description("For sale")
        .status(ListingStatus.ACTIVE)
        .user(u)
        .fixedPrice(2000.0)
        .build();

    Vehicle v = new Vehicle.Builder("ABC-123")
        .make("Toyota")
        .model("Corolla")
        .year(2018)
        .mileage(50000)
        .horsepower(120)
        .motorType(MotorType.GASOLINE)
        .listing(sale)
        .build();

        assertEquals("ABC-123", v.getLicensePlate());
        assertEquals("Toyota", v.getMake());
        assertEquals("Corolla", v.getModel());
        assertEquals(2018, v.getYear());
        assertEquals(Integer.valueOf(50000), v.getMileage());
        assertEquals(Integer.valueOf(120), v.getHorsepower());
        assertEquals(MotorType.GASOLINE, v.getMotorType());
        assertSame(sale, v.getListing());

        v.setMake("Honda");
        v.setModel("Civic");
        v.setYear(2020);
        v.setMileage(30000);
        v.setHorsepower(140);
        v.setMotorType(MotorType.DIESEL);

        assertEquals("Honda", v.getMake());
        assertEquals("Civic", v.getModel());
        assertEquals(2020, v.getYear());
        assertEquals(Integer.valueOf(30000), v.getMileage());
        assertEquals(Integer.valueOf(140), v.getHorsepower());
        assertEquals(MotorType.DIESEL, v.getMotorType());
    }

    @Test
    public void toStringShowsLicenseAndListing() {
        User u = new User.Builder()
            .name("A")
            .email("a@x")
            .password("pw")
            .phone("600")
            .location("L")
            .build();
        Sale s = new Sale.Builder()
            .description("desc")
            .status(ListingStatus.ACTIVE)
            .user(u)
            .fixedPrice(1.0)
            .build();
        Vehicle v = new Vehicle.Builder("PLT-1")
            .make("M")
            .model("Mo")
            .year(2021)
            .mileage(10)
            .horsepower(10)
            .motorType(MotorType.ELECTRIC)
            .listing(s)
            .build();
        String out = v.toString();
        assertTrue(out.contains("PLT-1") || out.contains("licensePlate"));
    }

}
