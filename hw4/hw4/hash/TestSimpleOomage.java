package hw4.hash;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


public class TestSimpleOomage {

    @Test
    public void testHashCodeDeterministic() {
        SimpleOomage so = SimpleOomage.randomSimpleOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    @Test
    public void testHashCodePerfect() {
        /* TODO: Write a test that ensures the hashCode is perfect,
          meaning no two SimpleOomages should EVER have the same
          hashCode!
         */
        for(int i = 0; i < 1000; i++) {
            SimpleOomage so = SimpleOomage.randomSimpleOomage();
            //so1 should always equals so
            SimpleOomage so1 = new SimpleOomage(so.red, so.green, so.blue);
            //so2 should never equals so
            int red2 = so.red + 5 > 225 ? so.red - 5 : so.red + 5;
            int green2 = so.green + 15 > 225 ? so.green - 15 : so.green + 15;
            int blue2 = so.blue + 25 > 225 ? so.blue - 25 : so.blue + 25;
            SimpleOomage so2 = new SimpleOomage(red2, green2, blue2);
            //so3 is a random SimpleOomgage instance.
            SimpleOomage so3 = SimpleOomage.randomSimpleOomage();

            assertEquals(so.hashCode(), so1.hashCode());
            assertNotEquals(so.hashCode(), so2.hashCode());

            if(so.equals(so3)) {
                assertEquals(so.hashCode(), so3.hashCode());
            } else {
                assertNotEquals(so3.hashCode(), so.hashCode());
            }
        }
    }

    @Test
    public void testEquals() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        SimpleOomage ooB = new SimpleOomage(50, 50, 50);
        assertEquals(ooA, ooA2);
        assertNotEquals(ooA, ooB);
        assertNotEquals(ooA2, ooB);
        assertNotEquals(ooA, "ketchup");
    }

    @Test
    public void testHashCodeAndEqualsConsistency() {
        SimpleOomage ooA = new SimpleOomage(5, 10, 20);
        SimpleOomage ooA2 = new SimpleOomage(5, 10, 20);
        HashSet<SimpleOomage> hashSet = new HashSet<>();
        hashSet.add(ooA);
        assertTrue(hashSet.contains(ooA2));
    }

    /* TODO: Once you've finished haveNiceHashCodeSpread,
    in OomageTestUtility, uncomment this test. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(SimpleOomage.randomSimpleOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestSimpleOomage.class);
    }
}
