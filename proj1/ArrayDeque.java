/**
 * Created by John on 2017/10/7.
 */
public class ArrayDeque<Item> {
    private Item [] AList;
    private int size;
    private int head, tail;

    /*Zero arguments constructor*/
    public ArrayDeque(){
        AList = (Item []) new Object[8] ;
        size = 0;
        head = tail = 0;
    }

    public ArrayDeque(Item i) {
        AList = (Item []) new Object[8];
        AList[0] = i;
        head = tail = 0;
        size = 1;
    }

    /*Returns true if deque is empty, false otherwise.*/
    public boolean isEmpty() {
        return (size == 0);
    }

    private boolean isOversize() {
        return (size >= AList.length);
    }

    private boolean isWastememory() {
        return (AList.length> 16 && (double)(size - 1)/AList.length < 0.25);
    }

    private void resize(int newsize) {
        Item[] newAList = (Item []) new Object[newsize];
        if(head <= tail)
            System.arraycopy(AList, head, newAList, 0, size);
        else {
            System.arraycopy(AList, head, newAList, 0, AList.length - head);
            System.arraycopy(AList, 0, newAList, AList.length - head, tail+1);
        }
        AList = newAList;
        head = 0;
        tail = size - 1;
    }

    /* Adds an item to the front of the Deque.*/
    public void addFirst(Item i) {
        if(isOversize()) {
            this.resize(AList.length*2);
        }
        if (head != 0) {
            head --;
            AList[head] = i;
        }
        else {
            if(isEmpty()) {
                AList[head] = i;
            }
            else {
                head = AList.length - 1;
                AList[head] = i;
            }
        }
        size ++;
    }

    /* Adds an item to the back of the Deque.*/
    public void addLast(Item i) {
        if(isOversize()) {
            this.resize(AList.length*2);
        }
        if (tail != AList.length - 1) {
            if(isEmpty()) {
                AList[head] = i;
            }
            else {
                tail++;
                AList[tail] = i;
            }
        }
        else {
            tail = 0;
            AList[tail] = i;
        }
        size ++;
    }

    /*Returns the number of items in the Deque.*/
    public int size() {
        return size;
    }

    /*Prints the items in the Deque from first to last, separated by a space.*/
    public void printDeque() {
        if(!isEmpty()) {
            if (head < tail) {
                for (int i = head; i <= tail; i++) {
                    System.out.print(AList[i] + " ");
                }
            } else if (head == tail)
                System.out.print(AList[head]);
            else {
                for (int i = head; i <= AList.length - 1; i++) {
                    if(AList[i] == null) continue;
                    System.out.print(AList[i] + " ");
                }
                for (int i = 0; i <= tail; i++) {
                    if(AList[i] == null) continue;
                    System.out.print(AList[i] + " ");
                }
            }
            System.out.println();
        }
        else System.out.println("Empty Deque");
    }

    /*Removes and returns the item at the front of the Deque. If no such item exists, returns null.*/
    public Item removeFirst() {
        if(!isEmpty()) {
            if(isWastememory())
                this.resize(AList.length/2);
            Item temp = AList[head];
            AList[head] = null;
            size --;
            head = (isEmpty() || head == AList.length - 1)? 0: (head + 1);
            if(size == 0) tail = 0;
            return temp;
        }
        else return null;
    }

    /*Removes and returns the item at the back of the Deque. If no such item exists, returns null.*/
    public Item removeLast() {
        if(!isEmpty()) {
            if(isWastememory())
                this.resize(AList.length/2);
            Item temp = AList[tail];
            AList[tail] = null;
            size --;
            if(isEmpty()) {
                head = 0;
                tail = 0;
            }
            else {
                tail = (tail == 0)? AList.length - 1 : (tail - 1);
            }
            return temp;
        }
        else return null;
    }

    /*Gets the item at the given index, where 0 is the front, 1 is the next item,
      and so forth. If no such item exists, returns null. Must not alter the deque!*/
    public Item get(int index) {
        if(index >= size)
            return null;
        else
            return AList[head+ index];
    }
}
