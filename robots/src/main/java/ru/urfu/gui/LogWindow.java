package ru.urfu.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.TextArea;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import ru.urfu.i18n.I18n;
import ru.urfu.i18n.I18nManager;
import ru.urfu.i18n.LocaleChangeListener;
import ru.urfu.i18n.LocaleChangedEvent;
import ru.urfu.log.LogChangeListener;
import ru.urfu.log.LogEntry;
import ru.urfu.log.LogWindowSource;
import ru.urfu.state.Stateful;

/**
 * <p>Окно с логами.</p>
 */
public final class LogWindow extends JInternalFrame
        implements LogChangeListener, LocaleChangeListener, Stateful {
    private final static int CONTENT_WIDTH = 200;
    private final static int CONTENT_HEIGHT = 500;

    private final LogWindowSource logSource;
    private final TextArea logContent;

    /**
     * <p>Конструктор.</p>
     *
     * @param logSource источник логов.
     */
    public LogWindow(LogWindowSource logSource) {
        super(null, true, true, true, true);

        this.logSource = logSource;
        this.logSource.registerListener(this);
        this.logContent = new TextArea("");
        this.logContent.setSize(CONTENT_WIDTH, CONTENT_HEIGHT);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        I18nManager.getInstance().addWeakLocaleChangeListener(this);

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);

        setLocaleDependantProperties();

        final int width = 300;
        final int height = 800;
        setPreferredSize(new Dimension(width, height));

        final int x = 10;
        final int y = 10;
        setLocation(x, y);
        pack();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void onLocaleChanged(LocaleChangedEvent event) {
        setLocaleDependantProperties();
    }

    /**
     * <p>Устанавливает свойства и поля,
     * зависящие от текущей локали.</p>
     */
    private void setLocaleDependantProperties() {
        final I18n i18n = I18nManager.getInstance().getI18n();
        setTitle(i18n.tr("Logs"));
        updateLogContent();
    }

    /**
     * <p>Обновляет содержимое окна.</p>
     */
    private void updateLogContent() {
        final StringBuilder content = new StringBuilder();
        for (final LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }

    @Override
    public String getNameForStateService() {
        return "LogWindow";
    }
}
