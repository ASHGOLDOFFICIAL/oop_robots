package ru.urfu.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import javax.swing.JPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.log.LogChangeListener;
import ru.urfu.log.LogEntry;
import ru.urfu.log.LogWindowSource;
import ru.urfu.log.Logger;

/**
 * <p>Окно с логами.</p>
 */
public final class LogWindow extends CustomInternalFrame implements LogChangeListener {
    private final static int WINDOW_WIDTH = 300;
    private final static int WINDOW_HEIGHT = 800;
    private final static int WINDOW_LOCATION_X = 10;
    private final static int WINDOW_LOCATION_Y = 10;
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
        this.logSource = logSource;
        this.logSource.registerListener(this);
        logContent = new TextArea("");
        logContent.setSize(CONTENT_WIDTH, CONTENT_HEIGHT);

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        setLocation(WINDOW_LOCATION_X, WINDOW_LOCATION_Y);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocaleDependantProperties();

        final I18n i18n = I18nFactory.getI18n(getClass());
        Logger.debug(i18n.tr("Протокол работает"));
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    protected void onDispose() {
        logSource.unregisterListener(LogWindow.this);
    }

    @Override
    protected void setLocaleDependantProperties() {
        final I18n i18n = I18nFactory.getI18n(LogWindow.class);
        setTitle(i18n.tr("Logs"));
        updateLogContent();
    }

    @SuppressWarnings("MissingJavadocMethod")
    private void updateLogContent() {
        final StringBuilder content = new StringBuilder();
        for (final LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }
}
