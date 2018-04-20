/**
 * Created by John on 2017/10/28.
 */
public interface Deque<Item> {
    void addFirst(Item i);

    void addLast(Item i);

    boolean isEmpty();

    int size();

    void printDeque();

    Item removeFirst();

    Item removeLast();

    Item get(int index);
}
