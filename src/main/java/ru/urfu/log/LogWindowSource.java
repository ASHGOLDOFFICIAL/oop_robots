package ru.urfu.log;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import org.slf4j.LoggerFactory;
import ru.urfu.structures.SynchronizedCircularQueue;


/**
 * <p>Источник логов для {@link ru.urfu.gui.LogWindow}.</p>
 */
public final class LogWindowSource {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(LogWindowSource.class);

    private final Queue<LogEntry> messages;
    private final Set<LogChangeListener> listeners;

    private volatile LogChangeListener[] activeListeners;

    /**
     * <p>Конструктор.</p>
     *
     * @param capacity максимальное количество хранимых логов.
     */
    public LogWindowSource(int capacity) {
        messages = new SynchronizedCircularQueue<>(capacity);
        listeners = Collections.newSetFromMap(new WeakHashMap<>());
    }

    /**
     * <p>Подписывает слушателя на уведомления об обновлениях логов.</p>
     *
     * @param listener слушатель событий
     */
    public void registerListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
            log.trace("Listener {}[{}] registered.", listener.getClass().getSimpleName(), listener.hashCode());
        }
    }

    /**
     * <p>Отписывает слушателя от уведомлений об обновлениях логов.</p>
     *
     * @param listener слушатель событий
     */
    public void unregisterListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
            log.trace("Listener {}[{}] unregistered.", listener.getClass().getSimpleName(), listener.hashCode());
        }
    }

    /**
     * <p>Добавляет новую запись.</p>
     *
     * @param logLevel   уровень лога.
     * @param strMessage сообщения лога.
     */
    public void append(LogLevel logLevel, String strMessage) {
        final LogEntry entry = new LogEntry(logLevel, strMessage);
        messages.add(entry);
        log.debug("Current size is {}", messages.size());

        if (this.activeListeners == null) {
            synchronized (listeners) {
                if (this.activeListeners == null) {
                    this.activeListeners = listeners.toArray(new LogChangeListener[0]);
                }
            }
        }
        for (final LogChangeListener listener : this.activeListeners) {
            listener.onLogChanged();
        }
    }

    /**
     * <p>Возвращает все сообщения.</p>
     *
     * @return все сообщения.
     */
    public Iterable<LogEntry> all() {
        return List.copyOf(messages);
    }
}
