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
import org.xnap.commons.i18n.I18nManager;
import org.xnap.commons.i18n.LocaleChangeEvent;
import org.xnap.commons.i18n.LocaleChangeListener;
import ru.urfu.log.LogChangeListener;
import ru.urfu.log.LogEntry;
import ru.urfu.log.LogWindowSource;

/**
 * <p>Окно с логами.</p>
 */
public final class LogWindow extends JInternalFrame implements LogChangeListener, LocaleChangeListener {
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
        super(null, true, true, true, true);

        this.logSource = logSource;
        this.logSource.registerListener(this);
        logContent = new TextArea("");
        logContent.setSize(CONTENT_WIDTH, CONTENT_HEIGHT);

        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                logSource.unregisterListener(LogWindow.this);
            }
        });

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        setLocation(WINDOW_LOCATION_X, WINDOW_LOCATION_Y);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        I18nManager.getInstance().addLocaleChangeListener(this);
        setLocaleSpecificProperties();
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

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void localeChanged(LocaleChangeEvent event) {
        setLocaleSpecificProperties();
    }

    /**
     * <p>Устанавливает свойства и поля,
     * зависящие от текущей Locale.</p>
     */
    private void setLocaleSpecificProperties() {
        final I18n i18n = I18nFactory.getI18n(LogWindow.class);
        setTitle(i18n.tr("Logs"));
        updateLogContent();
    }
}
