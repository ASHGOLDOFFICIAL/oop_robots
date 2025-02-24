package ru.urfu.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.log.LogChangeListener;
import ru.urfu.log.LogEntry;
import ru.urfu.log.LogWindowSource;

/**
 * <p>Окно с логами.</p>
 */
public final class LogWindow extends JInternalFrame implements LogChangeListener {
    private final static int LOG_CONTENT_WIDTH = 200;
    private final static int LOG_CONTENT_HEIGHT = 500;

    private final I18n i18n = I18nFactory.getI18n(LogWindow.class);
    private final LogWindowSource logSource;
    private final TextArea logContent;

    /**
     * <p>Конструктор.</p>
     *
     * @param logSource источник логов.
     */
    public LogWindow(LogWindowSource logSource) {
        super(null, true, true, true, true);
        setTitle(i18n.tr("Logs"));

        this.logSource = logSource;
        this.logSource.registerListener(this);
        logContent = new TextArea("");
        logContent.setSize(LOG_CONTENT_WIDTH, LOG_CONTENT_HEIGHT);

        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                logSource.unregisterListener(LogWindow.this);
            }
        });

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    @SuppressWarnings("MissingJavadocMethod")
    private void updateLogContent() {
        final StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
