package eus.ehu.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class SaleTest {

    @Test
    public void salePropertiesAndAccessors() {
        User u = new User("Alice", "a@example.com", "secret", "600000000", "Bilbao");
        Sale s = new Sale("Old car", ListingStatus.ACTIVE, u, 4500.5);

        assertEquals("Old car", s.getDescription());
        assertEquals(ListingStatus.ACTIVE, s.getStatus());
        assertSame(u, s.getUser());
        assertEquals(4500.5, s.getFixedPrice(), 0.0001);

        s.setFixedPrice(5000.0);
        assertEquals(5000.0, s.getFixedPrice(), 0.0001);

        s.setDescription("New desc");
        assertEquals("New desc", s.getDescription());

        s.setStatus(ListingStatus.SOLD);
        assertEquals(ListingStatus.SOLD, s.getStatus());
    }

    @Test
    public void toStringIncludesKeyParts() {
        User u = new User("A", "a@x", "pw", "600", "L");
        Sale s = new Sale("X", ListingStatus.ACTIVE, u, 1000.0);
        String out = s.toString();
        assertTrue(out.contains("Sale{") || out.contains("fixedPrice"));
    }

}
