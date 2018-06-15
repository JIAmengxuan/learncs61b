package hw4.hash;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /* TODO: Create a list of Complex Oomages called deadlyList
     * that shows the flaw in the hashCode function.
     */
    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();

        //last 32bit(4*8bit) of params;
        int[] lower32BitOfParams = new int[4];
        for(int i = 0; i < 4; i++) {
            lower32BitOfParams[i] = StdRandom.uniform(0,256);
        }

        int N = 1000;
        for(int i = 0; i < N; i++) {
            int length = StdRandom.uniform(4, 10);
            ArrayList<Integer> params = new ArrayList<>(length);
            for(int j = 0; j < length - 4; j++) {
                params.add(StdRandom.uniform(0, 256));
            }
            for(int j = 0; j < 4; j++) {
                params.add(lower32BitOfParams[j]);
            }
            deadlyList.add(new ComplexOomage(params));
        }
        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
