package ru.urfu.collections;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>Circular queue with fixed size.</p>
 *
 * <p>Does not allow null elements.</p>
 *
 * <p>If collection is full, then when adding a new element,
 * oldest one will be overwritten.</p>
 *
 * @param <E> the type of elements held in this сollection
 */
public class SynchronizedLimitedQueue<E> extends AbstractQueue<E> {
    private final int capacity;
    private int size = 0;
    private Node start = null;
    private Node end = null;

    /**
     * <p>Constructor.</p>
     *
     * @param capacity queue capacity
     */
    public SynchronizedLimitedQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
        this.capacity = capacity;
    }

    @Override
    public synchronized Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public synchronized int size() {
        return size;
    }

    @Override
    public synchronized boolean offer(E e) {
        if (e == null) {
            return false;
        }

        if (size == capacity) {
            start = start.next;
            --size;
        }

        final Node node = new Node(null, e);

        if (size == 0) {
            start = node;
            end = node;
        } else {
            end.next = node;
            end = end.next;
        }

        ++size;
        return true;
    }

    @Override
    public synchronized E poll() {
        if (size == 0) {
            return null;
        }
        final Node head = start;
        if (head == end) {
            end = null;
        }

        start = start.next;
        --size;
        return head.el;
    }

    @Override
    public synchronized E peek() {
        if (size == 0) {
            return null;
        }
        return start.el;
    }

    /**
     * <p>Iterable containing some amount of elements of the collection.</p>
     *
     * <p>If start index is lesser than zero or bigger than collection size,
     * empty iterable will be returned.</p>
     *
     * <p>Start index of zero corresponds to the oldest entry.</p>
     *
     * @param startFrom start index
     * @param count     number of elements
     * @return iterable containing specified amount of elements
     *         of the collection starting with given index
     */
    public synchronized Iterable<E> range(int startFrom, int count) {
        final int needed = Math.min(size - startFrom, count);
        if (startFrom < 0 || startFrom >= size || needed <= 0) {
            return Collections.emptyList();
        }

        Node current = start;
        for (int i = 0; i < startFrom; ++i) {
            if (current == null) {
                return Collections.emptyList();
            }
            current = current.next;
        }

        final List<E> elements = new ArrayList<>(needed);
        for (int i = 0; i < needed; ++i) {
            elements.add(current.el);
            current = current.next;
        }
        return elements;
    }

    /**
     * <p>List node, for inner use only.</p>
     */
    private class Node {
        E el;

        Node next;

        /**
         * <p>Constructor.</p>
         *
         * @param next reference to the next node
         * @param el   node content
         */
        private Node(Node next, E el) {
            this.next = next;
            this.el = el;
        }

    }

    /**
     * <p>Iterator for {@link SynchronizedLimitedQueue}.</p>
     */
    private class Itr implements Iterator<E> {
        private Node cursor;
        private int remaining;

        /**
         * <p>Constructor.</p>
         */
        private Itr() {
            cursor = start;
            remaining = size();
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            final Node head = cursor;
            cursor = cursor.next;
            --remaining;
            return head.el;
        }
    }
}
