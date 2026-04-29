package eus.ehu.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class EnumsTest {

    @Test
    public void listingStatusValues() {
        ListingStatus[] vals = ListingStatus.values();
        assertTrue(vals.length >= 3);
        assertEquals(ListingStatus.ACTIVE, ListingStatus.valueOf("ACTIVE"));
        assertEquals(ListingStatus.SOLD, ListingStatus.valueOf("SOLD"));
    }

    @Test
    public void motorTypeValues() {
        MotorType[] vals = MotorType.values();
        assertTrue(vals.length >= 3);
        assertEquals(MotorType.GASOLINE, MotorType.valueOf("GASOLINE"));
        assertEquals(MotorType.ELECTRIC, MotorType.valueOf("ELECTRIC"));
    }

}
