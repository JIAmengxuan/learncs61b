/**
 * Created by John on 2017/10/28.
 */
public class Palindrome {
    public static Deque<Character> wordToDeque(String word) {
        Deque<Character> wordChar = new LinkedListDeque<Character>();
        char[] arr = word.toCharArray();
        for (Character cha : arr) {
            wordChar.addLast(cha);
        }
        return wordChar;
    }

    public static boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordChar = Palindrome.wordToDeque(word);
        if(wordChar.size() == 1) return false;

        if(wordChar.size() == 0) return true;
        if(wordChar.size() == 3)
            return cc.equalChars(wordChar.removeLast(), wordChar.removeFirst());

        if(cc.equalChars(wordChar.removeLast(), wordChar.removeFirst())) {
            return Palindrome.isPalindrome(wordChar.toString(), cc);
        }
        return false;
    }

    public static boolean isPalindrome(String word){
        Deque<Character> wordChar = Palindrome.wordToDeque(word);

        if(wordChar.size() <= 1) return true;

        if(wordChar.removeLast().equals(wordChar.removeFirst())) {
            return Palindrome.isPalindrome(wordChar.toString());
        }
        return false;
    }
}
