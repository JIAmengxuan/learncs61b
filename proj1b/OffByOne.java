/**
 * Created by John on 2017/10/30.
 */
public class OffByOne implements CharacterComparator {
    @Override
    public boolean equalChars(char x, char y) {
        return (Math.abs(x - y) == 1);
    }

}
