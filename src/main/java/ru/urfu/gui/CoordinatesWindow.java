package ru.urfu.gui;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import org.xnap.commons.i18n.I18nManager;
import org.xnap.commons.i18n.LocaleChangeEvent;
import org.xnap.commons.i18n.LocaleChangeListener;
import ru.urfu.core.GameModel;
import ru.urfu.core.RobotPosition;

/**
 * <p>Окно с координатами робота.</p>
 */
public final class CoordinatesWindow extends JInternalFrame
        implements PropertyChangeListener, LocaleChangeListener, Stateful {
    private final static String TEXT_TEMPLATE = "x: %f, y: %f";

    private final GameModel model;
    private final JLabel label;

    /**
     * <p>Конструктор.</p>
     *
     * @param model модель игры
     */
    public CoordinatesWindow(GameModel model) {
        super(null, true, true, true, true);

        this.model = model;
        this.model.registerListener(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        I18nManager.getInstance().addLocaleChangeListener(this);

        this.label = new JLabel(getTextForLabel(this.model.getRobotPosition()), SwingConstants.CENTER);
        getContentPane().add(label);

        setLocaleDependantProperties();

        final int width = 200;
        final int height = 75;
        setPreferredSize(new Dimension(width, height));

        final int x = 100;
        final int y = 100;
        setLocation(x, y);
        pack();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final RobotPosition position = this.model.getRobotPosition();
        this.label.setText(getTextForLabel(position));
    }

    @Override
    public void localeChanged(LocaleChangeEvent event) {
        setLocaleDependantProperties();
    }

    /**
     * <p>Создаёт строку для отображения окном.</p>
     *
     * @param position позиция робота.
     * @return созданную строку.
     */
    private String getTextForLabel(RobotPosition position) {
        return TEXT_TEMPLATE.formatted(position.positionX(), position.positionY());
    }

    /**
     * <p>Устанавливает свойства и поля,
     * зависящие от текущей локали.</p>
     */
    private void setLocaleDependantProperties() {
        final I18n i18n = I18nFactory.getI18n(CoordinatesWindow.class);
        setTitle(i18n.tr("Coordinates"));
    }
}
