package eus.ehu.domain;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class UserTest {

    @Test
    public void constructorAndGettersSetters() {
        User u = new User.Builder()
                .name("Alice")
                .email("a@example.com")
                .password("secret")
                .phone("600000000")
                .location("Bilbao")
                .build();

        assertEquals("Alice", u.getName());
        assertEquals("a@example.com", u.getEmail());
        assertEquals("secret", u.getPassword());
        assertEquals("600000000", u.getPhone());
        assertEquals("Bilbao", u.getLocation());

        List<Listing> pubs = u.getPublications();
        assertNotNull(pubs);
        assertEquals(0, pubs.size());

        u.setName("Bob");
        u.setEmail("b@example.com");
        u.setPassword("pw");
        u.setPhone("611111111");
        u.setLocation("Donostia");

        assertEquals("Bob", u.getName());
        assertEquals("b@example.com", u.getEmail());
        assertEquals("pw", u.getPassword());
        assertEquals("611111111", u.getPhone());
        assertEquals("Donostia", u.getLocation());
    }

    @Test
    public void publicationsCanBeManaged() {
        User u = new User.Builder()
            .name("Alice")
            .email("a@example.com")
            .password("secret")
            .phone("600000000")
            .location("Bilbao")
            .build();
        Sale s = new Sale.Builder()
            .description("Nice car")
            .status(ListingStatus.ACTIVE)
            .user(u)
            .fixedPrice(12345.0)
            .build();

        u.getPublications().add(s);

        assertEquals(1, u.getPublications().size());
        assertSame(s, u.getPublications().get(0));
        assertEquals(u, s.getUser());
    }

    @Test
    public void toStringContainsFields() {
        User u = new User.Builder()
                .name("Alice")
                .email("a@example.com")
                .password("secret")
                .phone("600000000")
                .location("Bilbao")
                .build();
        String s = u.toString();
        assertTrue(s.contains("name='Alice'") || s.contains("name=Alice"));
        assertTrue(s.contains("email='a@example.com'") || s.contains("email=a@example.com"));
    }

}
