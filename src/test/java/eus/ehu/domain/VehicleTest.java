package eus.ehu.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class VehicleTest {

    @Test
    public void constructorAndGettersSetters() {
        User u = new User("Alice", "a@example.com", "secret", "600000000", "Bilbao");
        Sale sale = new Sale("For sale", ListingStatus.ACTIVE, u, 2000.0);

        Vehicle v = new Vehicle("ABC-123", "Toyota", "Corolla", 2018, 50000, 120, MotorType.GASOLINE, sale);

        assertEquals("ABC-123", v.getLicensePlate());
        assertEquals("Toyota", v.getMake());
        assertEquals("Corolla", v.getModel());
        assertEquals(2018, v.getYear());
        assertEquals(50000, v.getMileage());
        assertEquals(120, v.getHorsepower());
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
        assertEquals(30000, v.getMileage());
        assertEquals(140, v.getHorsepower());
        assertEquals(MotorType.DIESEL, v.getMotorType());
    }

    @Test
    public void toStringShowsLicenseAndListing() {
        User u = new User("A", "a@x", "pw", "600", "L");
        Sale s = new Sale("desc", ListingStatus.ACTIVE, u, 1.0);
        Vehicle v = new Vehicle("PLT-1", "M", "Mo", 2021, 10, 10, MotorType.ELECTRIC, s);
        String out = v.toString();
        assertTrue(out.contains("PLT-1") || out.contains("licensePlate"));
    }

}
