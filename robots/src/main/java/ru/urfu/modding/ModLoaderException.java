package ru.urfu.modding;

/**
 * <p>Загрузка мода не удалась.</p>
 */
public class ModLoaderException extends RuntimeException {
    /**
     * <p>Конструктор.</p>
     *
     * @param message сообщение.
     */
    public ModLoaderException(String message) {
        super(message);
    }
}
