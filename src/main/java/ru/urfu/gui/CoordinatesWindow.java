package ru.urfu.gui;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import ru.urfu.core.GameModel;
import ru.urfu.core.RobotInfo;
import ru.urfu.i18n.I18n;
import ru.urfu.i18n.I18nManager;
import ru.urfu.i18n.LocaleChangeListener;
import ru.urfu.i18n.LocaleChangedEvent;
import ru.urfu.state.Stateful;
import ru.urfu.utils.Vector2;

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
        I18nManager.getInstance().addWeakLocaleChangeListener(this);

        this.label = new JLabel(getTextForLabel(this.model.getRobotInfo()), SwingConstants.CENTER);
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
        final RobotInfo position = this.model.getRobotInfo();
        this.label.setText(getTextForLabel(position));
    }

    @Override
    public void onLocaleChanged(LocaleChangedEvent event) {
        setLocaleDependantProperties();
    }

    /**
     * <p>Создаёт строку для отображения окном.</p>
     *
     * @param position позиция робота.
     * @return созданную строку.
     */
    private String getTextForLabel(RobotInfo position) {
        final Vector2 pos = position.position();
        return TEXT_TEMPLATE.formatted(pos.x(), pos.y());
    }

    /**
     * <p>Устанавливает свойства и поля,
     * зависящие от текущей локали.</p>
     */
    private void setLocaleDependantProperties() {
        final I18n i18n = I18nManager.getInstance().getI18n();
        setTitle(i18n.tr("Coordinates"));
    }

    @Override
    public String getNameForStateService() {
        return "CoordinatesWindow";
    }
}
