package eus.ehu.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class SaleTest {

    @Test
    public void salePropertiesAndAccessors() {
    User u = new User.Builder()
        .name("Alice")
        .email("a@example.com")
        .password("secret")
        .phone("600000000")
        .location("Bilbao")
        .build();
    Sale s = new Sale.Builder()
        .description("Old car")
        .status(ListingStatus.ACTIVE)
        .user(u)
        .fixedPrice(4500.5)
        .build();

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
        User u = new User.Builder()
            .name("A")
            .email("a@x")
            .password("pw")
            .phone("600")
            .location("L")
            .build();
        Sale s = new Sale.Builder()
            .description("X")
            .status(ListingStatus.ACTIVE)
            .user(u)
            .fixedPrice(1000.0)
            .build();
        String out = s.toString();
        assertTrue(out.contains("Sale{") || out.contains("price="));
    }

}
