/***********************************************
 * @file BST.java
 * @description Generic binary search tree with in-order iterator.
 *              Provides insert, contains, clear, size, and iteration.
 * @author Ravi Ingle
 * @date October 21, 2025
 ***********************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayDeque;
import java.util.Deque;

public class BST<T extends Comparable<? super T>> implements Iterable<T> {

    /**
     * Internal node class used by this BST.
     * Kept private to allow a different external Node class if desired.
     */
    private static class TreeNode<U> {
        U value;
        TreeNode<U> left, right;

        TreeNode(U v) { value = v; left = right = null; }
    }

    private TreeNode<T> root;
    private int count;

    /** Construct an empty BST. */
    public BST() {
        root = null;
        count = 0;
    }

    /**
     * Insert an element into the BST. Duplicate elements are ignored.
     *
     * @param item element to insert
     */
    public void insert(T item) {
        root = insertNode(root, item);
    }

    /**
     * Recursive helper to insert into subtree rooted at node.
     *
     * @param node subtree root
     * @param item value to insert
     * @return new subtree root
     */
    private TreeNode<T> insertNode(TreeNode<T> node, T item) {
        if (node == null) {
            count++;
            return new TreeNode<>(item);
        }
        int cmp = item.compareTo(node.value);
        if (cmp < 0) node.left = insertNode(node.left, item);
        else if (cmp > 0) node.right = insertNode(node.right, item);
        // if equal, do nothing (no duplicates)
        return node;
    }

    /**
     * Check whether the tree contains a given key.
     *
     * @param key element to search for
     * @return true if present, false otherwise
     */
    public boolean contains(T key) {
        TreeNode<T> cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.value);
            if (cmp == 0) return true;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return false;
    }

    /** Remove all nodes from the tree. */
    public void clear() {
        root = null;
        count = 0;
    }

    /** Return number of elements currently stored. */
    public int size() {
        return count;
    }

    /**
     * Provide an in-order iterator over the tree's elements.
     *
     * @return iterator that returns elements in ascending order
     */
    @Override
    public Iterator<T> iterator() {
        return new InOrderIterator<>(root);
    }

    /**
     * Stack-based in-order iterator to avoid recursion.
     */
    private static class InOrderIterator<U> implements Iterator<U> {
        private final Deque<TreeNode<U>> stack = new ArrayDeque<>();

        InOrderIterator(TreeNode<U> root) {
            pushLeft(root);
        }

        private void pushLeft(TreeNode<U> node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public U next() {
            if (!hasNext()) throw new NoSuchElementException();
            TreeNode<U> node = stack.pop();
            U val = node.value;
            if (node.right != null) pushLeft(node.right);
            return val;
        }
    }
}

