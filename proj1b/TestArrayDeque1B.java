/**
 * Created by John on 2017/10/23.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayDeque1B {
    @Test
    public void randomCall () {
        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ad1 = new ArrayDequeSolution<>();
        Integer tempSad1, tempAd1;
        OperationSequence nameLists = new OperationSequence();
        DequeOperation nameOfEveryStep;

        while (true) {
            int numberBetweenZeroAndTen = StdRandom.uniform(10);
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.25) {
                ad1.addLast(numberBetweenZeroAndTen);
                sad1.addLast(numberBetweenZeroAndTen);
                nameOfEveryStep = new DequeOperation("addLast", numberBetweenZeroAndTen);
                nameLists.addOperation(nameOfEveryStep);
            } else if(numberBetweenZeroAndOne < 0.5) {
                sad1.addFirst(numberBetweenZeroAndTen);
                ad1.addFirst(numberBetweenZeroAndTen);
                nameOfEveryStep = new DequeOperation("addFirst", numberBetweenZeroAndTen);
                nameLists.addOperation(nameOfEveryStep);
            } else if(numberBetweenZeroAndOne < 0.75) {
                if(ad1.isEmpty() || sad1.isEmpty()) continue;
                tempSad1 = sad1.removeFirst();
                tempAd1 = ad1.removeFirst();
                nameOfEveryStep = new DequeOperation("removeFirst");
                nameLists.addOperation(nameOfEveryStep);
                if(!tempAd1.equals(tempSad1)) {
                    assertEquals(nameLists.toString(), tempAd1, tempSad1);
                    break;
                }
            } else {
                if (ad1.isEmpty() || sad1.isEmpty()) continue;
                tempSad1 = sad1.removeLast();
                tempAd1 = ad1.removeLast();
                nameOfEveryStep = new DequeOperation("removeLast");
                nameLists.addOperation(nameOfEveryStep);
                if(!tempAd1.equals(tempSad1)) {
                    assertEquals("\n" + nameLists.toString(), tempAd1, tempSad1);
                    break;
                }
            }
        }
    }
}
