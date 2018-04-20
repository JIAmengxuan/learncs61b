/**
 * Created by John on 2017/10/30.
 */
public class OffByN implements CharacterComparator {
    private Integer n;

    /**Constructor*/
    public OffByN(Integer N) {
        n = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return (Math.abs(x - y) == n);
    }
}
