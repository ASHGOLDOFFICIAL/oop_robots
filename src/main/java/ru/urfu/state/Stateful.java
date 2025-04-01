package ru.urfu.state;

/**
 * <p>Обозначает, что для данного класса
 * необходимо хранить состояние.</p>
 */
public interface Stateful {
    /**
     * <p>Имя для службы контроля состояний.</p>
     *
     * @return имя
     */
    String getNameForStateService();
}
