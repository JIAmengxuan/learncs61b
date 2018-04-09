package lab8;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B <K, V>, Iterable<K>{
    private BST rootOfBST;
    private int size = 0;


    private class BST {
        private K key;
        private V value;
        private BST left;
        private BST right;

        BST(K k, V v, BST l, BST r) {
            key = k;
            value = v;
            left = l;
            right = r;
        }

        void BSTPrint() {
            System.out.print("Key: " + key.toString() + "   Value: " + value.toString());
        }
    }

    private BST get(BST bst, K k) {
        if(bst == null) {
            return null;
        }
        int cmp = k.compareTo(bst.key);
        if(cmp == 0) {
            return bst;
        } else if(cmp < 0) {
            return get(bst.left, k);
        } else {
            return get(bst.right, k);
        }
    }

    private BST put(BST bst, K k, V v) {
        if(bst == null)
            return new BST(k, v, null, null);
        int cmp = k.compareTo(bst.key);
        if(cmp == 0) {
            bst.value = v;
        } else if (cmp < 0) {
            bst.left = put(bst.left, k, v);
        } else {
            bst.right = put(bst.right, k, v);
        }
        return bst;
    }

    private void printBST(BST bst) {
        if(bst != null) {
            printBST(bst.left);
            bst.BSTPrint();
            System.out.println();
            printBST(bst.right);
        }
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        size = 0;
        rootOfBST = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return key != null && get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        BST result =  get(rootOfBST, key);
        if(result == null) {
            return null;
        } else {
            return result.value;
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        if(key == null)
            throw new IllegalArgumentException("calls put() with a null key");
        if(!containsKey(key))
            size++;
        rootOfBST = put(rootOfBST, key, value);

    }

    public void printInOrder() {
        System.out.println("Prints out BSTMap in order of increasing Key:");
        printBST(rootOfBST);
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIter();
    }

    private class BSTMapIter implements Iterator<K> {
        private BST cur;

        BSTMapIter() {
            cur = rootOfBST;
        }

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public K next() {
            K ret = cur.key;


            return ret;
        }

    }

    /* 换引用（蠢）：
     * 分两种情况：被删除结点有儿子和没儿子：没儿子直接删除；有儿子则寻找左子树最大节点/右子树最小节点 去替换被删除节点。
     * 采用递归，自根向叶一次用当前节点的左/右子树调用递归，并重赋给当前节点的左/右引用，至此表明该层递归完成，然后返回已被重新赋值的当前节点。
     * 当bst.key == key终止递归（被删除节点为当层递归树的头节点）；分情况讨论。
     * 1.如果没有儿子，那么向上层递归返回null;
     * 2.如果有儿子，如果有左子树，则寻找左子树最大节点，返回最大节点，返回前需进行替换：如果没有左子树，则寻找右子树最小结点，操作类似；
     *   将最大节点的左子树分配给父节点，分两种情况：
     *   ①如果最大节点<其父节点，则说明父节点即为被删除节点，故无需分配，直接将父节点的右子树分配给最大节点的右子树，并返回最大节点；
     *   ②如果最大节点>其父节点，说明被删除节点不是其父节点，那么将最大节点的左子树分配给其父节点的右引用，
     *    再将最大节点替换被删除节点（当前递归的头节点），然后返回。
     */
    private BST helpRemove(BST bst, K key) {
        int cmp = key.compareTo(bst.key);
        if(cmp < 0) {
            bst.left = helpRemove(bst.left, key);
            return bst;
        }
        if(cmp > 0) {
            bst.right = helpRemove(bst.right, key);
            return bst;
        }
        else {
            if(bst.right == null && bst.left == null)
                return null;
            else if(bst.left != null ) {
                BST temp = bst;
                BST max = temp.left;
                while (max.right != null) {
                    temp = max;
                    max = max.right;
                }
                if(temp.equals(bst)) {
                    max.right = temp.right;
                    return max;
                }
                temp.right = max.left;
                max.left = bst.left;
                max.right = bst.right;
                return max;
            } else {
                BST temp = bst.right;
                BST min = temp;
                while (min.left != null) {
                    temp = min;
                    min = min.left;
                }
                if(temp.equals(bst)) {
                    min.left = bst.left;
                    return min;
                }
                temp.left = min.right;
                min.right = bst.right;
                min.left = bst.left;
                return min;
            }
        }
    }
    /* 换值：
     * 分三种情况：被删除结点有两个儿子、只有一个儿子、没儿子：没儿子直接删除；只有一个儿子的将被删除结点引用移到子节点，
     * 有两个儿子则寻找左子树最大节点/右子树最小节点 去替换被删除节点。
     * 采用递归，自根向叶一次用当前节点的左/右子树调用递归，并重赋给当前节点的左/右引用，至此表明该层递归完成，然后返回已被重新赋值的当前节点。
     * 当bst.key == key终止递归（被删除节点为当层递归树的头节点）；分情况讨论。
     * 1.如果没有儿子，那么向上层递归返回null;
     * 2.如果只有一个儿子，则向上层递归返回儿子的引用；
     * 3.如果有两个儿子，如果有左子树，则寻找左子树最大节点，返回最大节点，返回前需进行替换：如果没有左子树，则寻找右子树最小结点，操作类似；
     *   具体操作：找到被删除节点左子树的最大节点，将最大节点的值赋给被删除节点，然后递归左子树删除最大节点helpDelete(head.left, max.key);
     */
    private BST helpDelete (BST head, K key) {
        int cmp = key.compareTo(head.key);
        if(cmp < 0) {
            head.left = helpDelete(head.left, key);
        } else if (cmp > 0) {
            head.right = helpDelete(head.right, key);
        } else {
            if(head.right == null && head.left == null)
                head = null;
            else if(head.left != null && head.right == null)
                head = head.left;
            else if(head.left == null)
                head = head.right;
            else {
                BST max = head.left;
                while(max.right != null) {
                    max = max.right;
                }
                head.key = max.key;
                head.value = max.value;
                head.left = helpDelete(head.left, max.key);
            }
        }
        return head;
    }

    @Override
    public V remove(K key) {
        V temp = get(key);
        //rootOfBST = helpRemove(rootOfBST, key);
        rootOfBST = helpDelete(rootOfBST, key);
        return temp;
    }

    @Override
    public V remove(K key, V value) {
        if(containsKey(key) && value.equals(get(key)))
            return remove(key);
        return null;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        LinkedList<BST> stack = new LinkedList<>();
        BST cur = rootOfBST;

        while(cur != null || !stack.isEmpty()) {
            while(cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            if(!stack.isEmpty()) {
                cur = stack.pop();
                keySet.add(cur.key);
                cur = cur.right;
            }
        }
        return keySet;
    }
}
