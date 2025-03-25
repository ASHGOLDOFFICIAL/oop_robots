package ru.urfu.collections;

import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты для {@link SynchronizedCircularQueue}.
 */
@SuppressWarnings("MagicNumber")
class SynchronizedCircularQueueTest {
    /**
     * <p>Проверяем, что:</p>
     * <ul>
     *     <li>Коллекция отказывается от предложения null.</li>
     *     <li>Содержимое и размер не меняется после попытки.</li>
     * </ul>
     */
    @Test
    @DisplayName("Добавление null не проходит")
    void testNullAddition() {
        final Queue<Integer> queue = new SynchronizedCircularQueue<>(3);
        Assertions.assertFalse(queue.offer(null));
        Assertions.assertIterableEquals(List.of(), queue);
        Assertions.assertEquals(0, queue.size());
    }

    /**
     * <p>Проверяем, что при добавлении элементов
     * корректно меняется размер и содержимое коллекции.</p>
     */
    @Test
    @DisplayName("Обычное добавление в коллекцию")
    void testNormalAddition() {
        final Queue<Integer> queue = new SynchronizedCircularQueue<>(3);

        queue.offer(1);
        queue.offer(2);
        Assertions.assertEquals(2, queue.size());
        Assertions.assertIterableEquals(List.of(1, 2), queue);

        queue.offer(3);
        queue.offer(4);
        Assertions.assertEquals(3, queue.size());
        Assertions.assertIterableEquals(List.of(2, 3, 4), queue);
    }

    /**
     * <p>Проверяем, что:</p>
     * <ul>
     *     <li>{@link SynchronizedCircularQueue#poll} на пустой коллекции возвращает null;</li>
     *     <li>{@link SynchronizedCircularQueue#poll} возвращает корректный элемент;</li>
     *     <li>{@link SynchronizedCircularQueue#poll} на непустой коллекции удаляет элемент из коллекции.</li>
     * </ul>
     */
    @Test
    @DisplayName("Обычное удаление из коллекции")
    void testRemoval() {
        final Queue<Integer> queue = new SynchronizedCircularQueue<>(3);
        Assertions.assertNull(queue.poll());

        queue.add(5);
        Assertions.assertEquals(5, queue.poll());
        Assertions.assertEquals(0, queue.size());
        Assertions.assertIterableEquals(List.of(), queue);

        Assertions.assertNull(queue.poll());
    }

    /**
     * <p>Проверяем, что:</p>
     * <ul>
     *     <li>{@link SynchronizedCircularQueue#peek} возвращает корректный элемент
     *     и не меняет содержимое коллекции и её размер.</li>
     * </ul>
     */
    @Test
    @DisplayName("peek")
    void testPeek() {
        final Queue<Integer> queue = new SynchronizedCircularQueue<>(3);
        queue.add(1);
        Assertions.assertEquals(1, queue.peek());
        Assertions.assertEquals(1, queue.size());
        Assertions.assertIterableEquals(List.of(1), queue);
    }

    /**
     * <p>Добавляем 8 элементов в очередь.
     * Она должна содержать элементы: 5, 6, 7.</p>
     *
     * <p>Берём 3 элемента с 1-го. Ожидаем: 5, 6, 7</p>
     *
     * <p>Берём 2 элемента со 2-го. Ожидаем: 6, 7</p>
     *
     * <p>Берём 3 элемента со 2-го. Ожидаем: 6, 7</p>
     */
    @Test
    @DisplayName("range")
    void testRange() {
        final SynchronizedCircularQueue<Integer> queue = new SynchronizedCircularQueue<>(3);
        for (int i = 0; i < 8; ++i) {
            queue.add(i);
        }

        Assertions.assertIterableEquals(List.of(5, 6, 7), queue.range(0, 3));
        Assertions.assertIterableEquals(List.of(6, 7), queue.range(1, 2));
        Assertions.assertIterableEquals(List.of(6, 7), queue.range(1, 3));
    }
}
