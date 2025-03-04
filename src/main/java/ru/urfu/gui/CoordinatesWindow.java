package ru.urfu.gui;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import ru.urfu.config.Configuration;
import ru.urfu.core.GameModel;
import ru.urfu.core.GameModelChangeListener;
import ru.urfu.core.RobotPosition;

/**
 * <p>Окно с координатами робота.</p>
 */
public final class CoordinatesWindow extends CustomInternalFrame implements GameModelChangeListener {
    private final static String TEXT_TEMPLATE = "x: %f, y: %f";

    private final GameModel model;
    private final JLabel label;

    /**
     * <p>Конструктор.</p>
     *
     * @param config конфигурация
     * @param model  модель игры
     */
    public CoordinatesWindow(Configuration config, GameModel model) {
        super(config);

        this.model = model;
        this.model.registerListener(this);

        this.label = new JLabel(getTextForLabel(this.model.getRobotPosition()), SwingConstants.CENTER);
        getContentPane().add(label);

        setLocaleDependantProperties();
    }

    @Override
    protected Dimension defaultSize() {
        final int width = 200;
        final int height = 75;
        return new Dimension(width, height);
    }

    @Override
    protected void onDispose() {
        this.model.registerListener(this);
    }

    @Override
    protected void setLocaleDependantProperties() {
        final I18n i18n = I18nFactory.getI18n(CoordinatesWindow.class);
        setTitle(i18n.tr("Coordinates"));
    }

    @Override
    public void onModelChange() {
        final RobotPosition position = this.model.getRobotPosition();
        this.label.setText(getTextForLabel(position));
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
}
