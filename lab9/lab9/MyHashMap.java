package lab9;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int INIT_LOADFACTOR = 4;
    private static final int INIT_SIZE = 10;
    private int size;
    private double loadFactor, curLoadFactor;
    private HashSet<K> keys;
    private ArrayList[] table;

    private class Node {
        private K key;
        private V value;

        private Node(K k, V v) {
            key = k;
            value = v;
        }

        private V getValue() {
            return value;
        }

        private K getKey() {
            return key;
        }

        private void setValue(V v) {
            value = v;
        }
    }


    public MyHashMap(){
        size = INIT_SIZE;
        loadFactor = INIT_LOADFACTOR;
        curLoadFactor = 0;
        table = new ArrayList[size];
        for(int i = 0; i < size; i ++) {
            table[i] = new ArrayList<Node>();
        }
        keys = new HashSet<>();
    }

    public MyHashMap(int initialSize) {
        size = initialSize;
        loadFactor = INIT_LOADFACTOR;
        curLoadFactor = 0;
        table = new ArrayList[size];
        for(int i = 0; i < size; i ++) {
            table[i] = new ArrayList<Node>();
        }
        keys = new HashSet<>();
    }

    public MyHashMap(int initialSize, double loadFactor) {
        size = initialSize;
        this.loadFactor = loadFactor;
        curLoadFactor = 0;
        table = new ArrayList[size];
        for(int i = 0; i < size; i ++) {
            table[i] = new ArrayList<Node>();
        }
        keys = new HashSet<>();
    }

    // hash value between 0 and size-1
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % size;
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        keys.clear();
        for(ArrayList eachList : table) {
            eachList.clear();
        }
        curLoadFactor = 0;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) throws RuntimeException {
        if(key == null)
            throw new RuntimeException("null key!");
        return keys.contains(key);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) throws RuntimeException {
        V result = null;
        if(containsKey(key)) {
            ArrayList<Node> groupOfValue = table[hash(key)];
            for(Node eachNode : groupOfValue) {
                if(eachNode.getKey().equals(key)) {
                    result = eachNode.getValue();
                    break;
                }
            }
        }
        return result;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return keys.size();
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int index = hash(key);
        ArrayList<Node> listToAdd = table[index];
        if(containsKey(key)) {
            for(Node n : listToAdd) {
                if(n.getKey().equals(key)) {
                    n.setValue(value);
                    break;
                }
            }
        } else {
            listToAdd.add(new Node(key, value));
            keys.add(key);
        }
        curLoadFactor = size() / size;
        if(curLoadFactor >= loadFactor) {
            resize(2);
        }

    }

    /* Resize the size of MyHashMap. */
    private void resize(int multiple) {
        ArrayList<Node> temp = new ArrayList<>();

        for(Iterator hashMapItor = this.iterator(); hashMapItor.hasNext();) {
            K key = ((K) hashMapItor.next());
            Node nodeToAdd = new Node(key, get(key));
            temp.add(nodeToAdd);
        }
        clear();

        size = size * multiple;
        table = new ArrayList[size];
        for(int i = 0; i < size; i ++) {
            table[i] = new ArrayList<Node>();
        }
        for(Node n : temp) {
           put(n.getKey(), n.getValue());
        }
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keys;
    }


    @Override
    public Iterator<K> iterator() {
        return keys.iterator();
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if(containsKey(key)) {
            keys.remove(key);
            int index = hash(key);
            ArrayList<Node> groupToRemove = table[index];
            int indexOfRemoveNode = 0;
            for(Node eachNode : groupToRemove) {
                if(eachNode.getKey().equals(key))
                    return groupToRemove.remove(indexOfRemoveNode).getValue();
                indexOfRemoveNode ++;
            }
            curLoadFactor = size() / size;
        }
        return null;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        return remove(key);
    }
}
