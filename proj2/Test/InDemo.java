package Test;

/**
 * Created by John on 2017/11/27.
 */
import edu.princeton.cs.introcs.In;
import java.util.Scanner;

public class InDemo {

    public static void testIn() {
        In in = new In("C:\\Users\\John\\IdeaProjects\\cs61b\\learncs61b\\learncs61b\\proj2\\examples\\" + "teams" + ".tbl");
        String Line = in.readLine();
        Scanner theRestLines = new Scanner(in.readAll()).useDelimiter("\\s*,\\s*|\\s*\\n");
        while(theRestLines.hasNext()) {
            System.out.println(theRestLines.next());
        }
    }

    public static void main (String[] args) {
        InDemo.testIn();
    }
}
