package ru.urfu.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.LoggerFactory;


@SuppressWarnings({"MissingJavadocMethod", "MissingJavadocType"})
public class LogWindowSource {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(LogWindowSource.class);
    private final int iQueueLength;
    private List<LogEntry> messages;
    private final List<LogChangeListener> listeners;
    private volatile LogChangeListener[] activeListeners;

    public LogWindowSource(int iQueueLength) {
        this.iQueueLength = iQueueLength;
        messages = new ArrayList<>(iQueueLength);
        listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
            activeListeners = null;
            log.debug("Listener registered. Its hashcode is {}", listener.hashCode());
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
            activeListeners = null;
            log.debug("Listener unregistered: Its hashcode is {}", listener.hashCode());
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        final LogEntry entry = new LogEntry(logLevel, strMessage);

        if (size() >= iQueueLength) {
            this.messages = range(1, iQueueLength);
        }
        messages.add(entry);
        log.debug("Current size is {}", size());

        if (this.activeListeners == null) {
            synchronized (listeners) {
                if (this.activeListeners == null) {
                    this.activeListeners = listeners.toArray(new LogChangeListener[0]);
                }
            }
        }
        for (LogChangeListener listener : this.activeListeners) {
            listener.onLogChanged();
        }
    }

    public int size() {
        return messages.size();
    }

    public List<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= messages.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, messages.size());
        return messages.subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return List.copyOf(messages);
    }
}
