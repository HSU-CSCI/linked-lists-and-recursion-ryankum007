package edu.hsutx;

/**
 * The {@code CircularLL} class implements a circularly linked list where each element points
 * to the next in sequence, and the last element points back to the first, forming a circle.
 * This class provides basic operations such as adding elements to the front or end, removing
 * elements, rotating the list, and checking the first element.
 *
 * @param <E> the type of elements in this list
 */
public class CircularLL<E> {
    private Node<E> tail = null;
    private Node<E> head = null;
    private int size=0;

    /**
     * A static nested class representing a node in the circular linked list.
     *
     * @param <E> the type of elements in this node
     */
    private static class Node<E> {
        private E data;
        private Node<E> next;
        public Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
        public E getData() {return this.data;}
        public Node<E> getNext() {return this.next;}
        public void setData(E data) {this.data = data;}
        public void setNext(Node<E> next) {this.next = next;}
    }

    /**
     * Constructs an empty circularly linked list.
     */
    public CircularLL() { }

    /**
     * Checks if the list is empty.
     *
     * @return {@code true} if the list is empty, otherwise {@code false}
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds an element to the front of the list. If the list is empty, the new node
     * points to itself. Otherwise, the new node is inserted before the current first node.
     *
     * @param e the element to add
     */

    public void addFirst(E e) {
    // if list is empty, make tail point to a new node
    if (isEmpty()) {
        tail = new Node<>(e, null);
        tail.next = tail; // only node, so point to itself
    } else {
        // get current head (node after tail)
        Node<E> head = tail.next;

        // make a new node that points to the old head
        Node<E> n = new Node<>(e, head);

        // link tail to the new node, so it becomes the new head
        tail.next = n;
    }

    // increase list size
    size++;
}


    /**
     * Adds an element to the end of the list by reusing the {@code addFirst} method
     * and updating the tail to point to the new last element.
     *
     * @param e the element to add
     */
    
    public void addLast(E e) {
    // if list is empty, just use addFirst (makes a single node that points to itself)
    if (isEmpty()) {
        addFirst(e);
    } else {
        // first add new element at the front (head position)
        addFirst(e);

        // then move tail forward, so the new node becomes the tail (last element)
        tail = tail.next;
    }
}


    /**
     * Removes and returns the first element of the list. If the list becomes empty after
     * removal, the tail is set to {@code null}.
     *
     * @return the element removed, or {@code null} if the list was empty
     */
    public E removeFirst() {
        if (isEmpty()) return null;
        Node<E> head = tail.getNext();
        if (head == tail) tail = null;
        else tail.setNext(head.getNext());
        size--;
        return head.getData();
    }

    /**
     * Rotates the list by advancing the tail reference to the next node, effectively
     * shifting the head of the list to the next element.
     */
    public void rotate() {
        if (tail != null) {
            tail = tail.next; // tail+=1, head becomes next element
        }
    }

    /**
     * Returns the first element of the list without removing it.
     *
     * @return the first element of the list, or {@code null} if the list is empty
     */
    public E first() {
        if (isEmpty()) return null;
        return tail.getNext().getData();
    }

}
