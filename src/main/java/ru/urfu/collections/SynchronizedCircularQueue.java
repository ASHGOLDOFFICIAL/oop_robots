package ru.urfu.collections;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Circular queue with fixed size.</p>
 *
 * <p>Does not allow null elements.</p>
 *
 * <p>If collection is full, then when adding a new element,
 * oldest one will be overwritten.</p>
 *
 * @param <E> the type of elements held in this deque
 */
public class SynchronizedCircularQueue<E> extends AbstractQueue<E> {
    private final E[] elements;
    private final int capacity;

    private int size = 0;
    private int start = 0;
    private int end = 0;

    /**
     * <p>Constructor.</p>
     *
     * @param capacity queue capacity
     */
    @SuppressWarnings("unchecked")
    public SynchronizedCircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
        this.capacity = capacity;
        this.elements = (E[]) new Object[capacity];
    }

    @Override
    public synchronized Iterator<E> iterator() {
        return new SynchronizedCircularQueueIterator();
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

        if (size == elements.length) {
            start = (start + 1) % capacity;
            --size;
        }
        elements[end] = e;
        end = (end + 1) % capacity;
        ++size;
        return true;
    }

    @Override
    public synchronized E poll() {
        if (size == 0) {
            return null;
        }
        final E head = peek();
        elements[start] = null;
        start = (start + 1) % capacity;
        --size;
        return head;
    }

    @Override
    public synchronized E peek() {
        return elements[start];
    }

    /**
     * <p>Iterator for {@link SynchronizedCircularQueue}.</p>
     */
    private class SynchronizedCircularQueueIterator implements Iterator<E> {
        private final E[] iteratorElements;
        private int cursor;
        private int remaining = size();

        /**
         * <p>Constructor.</p>
         */
        SynchronizedCircularQueueIterator() {
            cursor = start;
            iteratorElements = Arrays.copyOf(elements, elements.length);
        }

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public E next() {
            if (remaining <= 0) {
                throw new NoSuchElementException();
            }
            final E e = iteratorElements[cursor];
            cursor = (cursor + 1) % capacity;
            remaining--;
            return e;
        }
    }
}
