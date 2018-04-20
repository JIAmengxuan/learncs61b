/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {
    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("C:\\Users\\John\\IdeaProjects\\cs61b\\learncs61b\\learncs61b\\proj1b\\words.txt");

        while (!in.isEmpty()) {
            String word = in.readString();
            //OffByOne ofb1 = new OffByOne();
            OffByN ofb5 = new OffByN(5);
            if (Palindrome.isPalindrome(word, ofb5)) {
                System.out.println(word);
            }
        }
    }
}


