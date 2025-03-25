package ru.urfu.collections;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

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
        if (startFrom < 0 || startFrom >= size || count == 0) {
            return Collections.emptyList();
        }

        final int remaining = size - startFrom;
        final int realCount = Math.min(count, remaining);
        final int fromIndex = (start + startFrom) % size;
        final int toIndex = (fromIndex + realCount) % size;

        if (toIndex <= fromIndex) {
            final E[] firstPart = Arrays.copyOfRange(elements, fromIndex, size);
            final E[] secondPart = Arrays.copyOfRange(elements, 0, toIndex);
            return Stream.concat(Arrays.stream(firstPart), Arrays.stream(secondPart)).toList();
        }

        return Arrays.asList(Arrays.copyOfRange(elements, fromIndex, toIndex));
    }

    /**
     * <p>Iterator for {@link SynchronizedCircularQueue}.</p>
     */
    private class SynchronizedCircularQueueIterator implements Iterator<E> {
        private final E[] iteratorElements = Arrays.copyOf(elements, elements.length);
        private int cursor;
        private int remaining;

        /**
         * <p>Constructor.</p>
         */
        SynchronizedCircularQueueIterator() {
            remaining = size();
            cursor = start;
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
