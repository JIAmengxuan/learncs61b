/**
 * Created by John on 2017/10/6.
 */
public class LinkedListDeque<Item> implements Deque<Item>{
    private LLDNode sentinel;
    private int size;

    private class LLDNode {
        private Item it;
        private LLDNode next, pre;

        public LLDNode() {
            it = null;
            next = this;
            pre = this;
        }

        public LLDNode(Item i, LLDNode n, LLDNode p) {
            it = i;
            next = n;
            pre = p;
        }

        private Item helpRecursive(int index) {
            LLDNode p = this.next;
            if(index == 0)
                return p.it;
            else {
                index--;
                return p.helpRecursive(index);
            }
        }
    }

    /*Zero arguments constructor*/
    public LinkedListDeque(){
        sentinel = new LLDNode();
        size = 0;
    }

    public LinkedListDeque(Item i) {
        sentinel = new LLDNode();
        sentinel.next = new LLDNode(i, sentinel, sentinel);
        size = 1;
    }
    /* Adds an item to the front of the Deque.*/
    @Override
    public void addFirst(Item i) {
        LLDNode Temp = new LLDNode(i, sentinel.next, sentinel);
        Temp.next.pre = Temp;
        sentinel.next = Temp;
        size ++;
    }

    /* Adds an item to the back of the Deque.*/
    @Override
    public void addLast(Item i) {
        LLDNode Temp = new LLDNode(i, sentinel, sentinel.pre);
        Temp.pre.next = Temp;
        sentinel.pre = Temp;
        size ++;
    }

    /*Returns true if deque is empty, false otherwise.*/
    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    /*Returns the number of items in the Deque.*/
    @Override
    public int size() {
        return size;
    }

    /*Prints the items in the Deque from first to last, separated by a space.*/
    @Override
    public void printDeque() {
        if(!isEmpty()) {
            LLDNode p = sentinel.next;
            while(p != sentinel) {
                System.out.print(p.it + " ");
                p = p.next;
            }
        }
        else System.out.println("Empty Deque");
    }

    /*Removes and returns the item at the front of the Deque. If no such item exists, returns null.*/
    @Override
    public Item removeFirst() {
        if(!isEmpty()) {
            LLDNode p = sentinel.next;
            sentinel.next = p.next;
            p.next.pre = sentinel;
            size--;
            return p.it;
        }
        else return null;
    }

    /*Removes and returns the item at the back of the Deque. If no such item exists, returns null.*/
    @Override
    public Item removeLast() {
        if(!isEmpty()) {
            LLDNode p = sentinel.pre;
            sentinel.pre = p.pre;
            p.pre.next = sentinel;
            size--;
            return p.it;
        }
        else return null;

    }

    /*Gets the item at the given index, where 0 is the front, 1 is the next item,
      and so forth. If no such item exists, returns null. Must not alter the deque!*/
    @Override
    public Item get(int index) {
        if(index >= size)
            return null;
        else {
            LLDNode p = sentinel.next;
            while(index != 0) {
                p = p.next;
                index --;
            }
            return p.it;
        }
    }
    @Override
    public String toString() {
        String s = "";
        for (LLDNode p = sentinel.next;p != sentinel; p = p.next) {
            s = s + p.it;
        }
        return s;
    }
    /*Gets the item at the given index, where 0 is the front, 1 is the next item,
      and so forth. If no such item exists, returns null. Must not alter the deque!*/
    public Item getRecursive(int index) {
        if(index >= size)
            return null;
        else return this.sentinel.helpRecursive(index);
    }
}
