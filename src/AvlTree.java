/***********************************************
 * @file AvlTree.java
 * @description Balanced AVL tree implementation.
 *              Supports insert, remove, contains, findMin, findMax,
 *              printTree and checkBalance (for debugging/test).
 * @author Ravi Ingle
 * @date October 21, 2025
 ***********************************************/

public class AvlTree<T extends Comparable<? super T>> {

    /** Node used by AVL tree */
    private static class ANode<U> {
        U value;
        ANode<U> left, right;
        int height;
        ANode(U val) { value = val; left = right = null; height = 0; }
    }

    private ANode<T> root;

    /** Construct empty AVL tree */
    public AvlTree() {
        root = null;
    }

    /**
     * Insert value into AVL tree. Duplicates are ignored.
     *
     * @param x item to insert
     */
    public void insert(T x) {
        root = insertRec(root, x);
    }

    /**
     * Internal recursive insertion that returns new subtree root.
     *
     * @param node subtree root
     * @param x item to insert
     * @return new root of subtree
     */
    private ANode<T> insertRec(ANode<T> node, T x) {
        if (node == null) return new ANode<>(x);

        int cmp = x.compareTo(node.value);
        if (cmp < 0) node.left = insertRec(node.left, x);
        else if (cmp > 0) node.right = insertRec(node.right, x);
        // else duplicate: do nothing

        return rebalance(node);
    }

    /**
     * Remove value from tree (no-op if absent).
     *
     * @param x item to remove
     */
    public void remove(T x) {
        root = removeRec(root, x);
    }

    /**
     * Recursive remove helper.
     *
     * @param node subtree root
     * @param x value to remove
     * @return new subtree root
     */
    private ANode<T> removeRec(ANode<T> node, T x) {
        if (node == null) return null;

        int cmp = x.compareTo(node.value);
        if (cmp < 0) node.left = removeRec(node.left, x);
        else if (cmp > 0) node.right = removeRec(node.right, x);
        else {
            // found node to delete
            if (node.left != null && node.right != null) {
                // two children: replace with smallest from right subtree
                ANode<T> min = findMinNode(node.right);
                node.value = min.value;
                node.right = removeRec(node.right, min.value);
            } else {
                // one or zero children: replace node with child (may be null)
                node = (node.left != null) ? node.left : node.right;
            }
        }
        if (node != null) node = rebalance(node);
        return node;
    }

    /**
     * Check whether the tree contains a value.
     *
     * @param x item to find
     * @return true if present
     */
    public boolean contains(T x) {
        ANode<T> cur = root;
        while (cur != null) {
            int cmp = x.compareTo(cur.value);
            if (cmp == 0) return true;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return false;
    }

    /**
     * Find the minimum value stored in the tree.
     *
     * @return smallest item
     * @throws UnderflowException if empty
     */
    public T findMin() {
        if (root == null) throw new UnderflowException();
        return findMinNode(root).value;
    }

    /** Helper: find node containing min in subtree */
    private ANode<T> findMinNode(ANode<T> node) {
        if (node == null) return null;
        while (node.left != null) node = node.left;
        return node;
    }

    /**
     * Find the maximum value stored in the tree.
     *
     * @return largest item
     * @throws UnderflowException if empty
     */
    public T findMax() {
        if (root == null) throw new UnderflowException();
        return findMaxNode(root).value;
    }

    /** Helper: find node containing max in subtree */
    private ANode<T> findMaxNode(ANode<T> node) {
        if (node == null) return null;
        while (node.right != null) node = node.right;
        return node;
    }

    /** Make tree logically empty. */
    public void makeEmpty() {
        root = null;
    }

    /** Check whether tree is empty. */
    public boolean isEmpty() {
        return root == null;
    }

    /** Print tree items in sorted (in-order) order to stdout. */
    public void printTree() {
        printInOrder(root);
    }

    /** Recursively print subtree in-order. */
    private void printInOrder(ANode<T> node) {
        if (node == null) return;
        printInOrder(node.left);
        System.out.println(node.value);
        printInOrder(node.right);
    }

    /**
     * Rebalance the given subtree root if it violates AVL balance.
     *
     * @param node subtree root
     * @return new subtree root after rotations (or same node)
     */
    private ANode<T> rebalance(ANode<T> node) {
        if (node == null) return null;

        updateHeight(node);
        int balance = height(node.left) - height(node.right);

        // Left heavy
        if (balance > 1) {
            if (height(node.left.left) >= height(node.left.right)) {
                node = rotateRight(node); // single rotation
            } else {
                node.left = rotateLeft(node.left); // double rotation
                node = rotateRight(node);
            }
        }
        // Right heavy
        else if (balance < -1) {
            if (height(node.right.right) >= height(node.right.left)) {
                node = rotateLeft(node); // single rotation
            } else {
                node.right = rotateRight(node.right); // double rotation
                node = rotateLeft(node);
            }
        } else {
            // already balanced
        }

        updateHeight(node);
        return node;
    }

    /** Update node.height from children. */
    private void updateHeight(ANode<T> node) {
        if (node != null) node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    /** Return height for node or -1 if null. */
    private int height(ANode<T> node) {
        return node == null ? -1 : node.height;
    }

    /** Single right rotation (rotate with left child). */
    private ANode<T> rotateRight(ANode<T> k2) {
        ANode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        // update heights bottom-up
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /** Single left rotation (rotate with right child). */
    private ANode<T> rotateLeft(ANode<T> k1) {
        ANode<T> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        // update heights bottom-up
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(k1.height, height(k2.right)) + 1;
        return k2;
    }

    /**
     * For debugging: check balance invariants for entire tree and print OOPS!! if violation.
     * This mirrors the provided TestAvl.checkBalance expectations.
     */
    public void checkBalance() {
        checkBalanceRec(root);
    }

    /** Recursively check subtree heights and invariants. */
    private int checkBalanceRec(ANode<T> node) {
        if (node == null) return -1;
        int hl = checkBalanceRec(node.left);
        int hr = checkBalanceRec(node.right);
        if (Math.abs(height(node.left) - height(node.right)) > 1 ||
                height(node.left) != hl || height(node.right) != hr) {
            System.out.println("OOPS!!");
        }
        return height(node);
    }
}

