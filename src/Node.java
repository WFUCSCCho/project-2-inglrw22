/***********************************************
 * @file Node.java
 * @description Small generic Node class for binary trees.
 *              Provided in case other modules reference a separate Node.
 * @author Ravi Ingle
 * @date October 21, 2025
 ***********************************************/

public class Node<E> {
    private E data;
    private Node<E> left;
    private Node<E> right;

    /** Create a node with a value. */
    public Node(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    /** Return stored value. */
    public E getElement() { return data; }

    /** Replace stored value. */
    public void setElement(E value) { this.data = value; }

    /** Get left child. */
    public Node<E> getLeft() { return left; }

    /** Set left child. */
    public void setLeft(Node<E> left) { this.left = left; }

    /** Get right child. */
    public Node<E> getRight() { return right; }

    /** Set right child. */
    public void setRight(Node<E> right) { this.right = right; }

    /** Check whether this node is a leaf. */
    public boolean isLeaf() { return left == null && right == null; }
}
