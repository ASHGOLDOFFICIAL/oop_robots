package ru.urfu.gui;

import ru.urfu.log.Logger;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.*;

public class MainApplicationFrameController {
    private final I18n i18n = I18nFactory.getI18n(getClass());

    public void showExitConfirmationPopUp() {
        final int result = JOptionPane.showOptionDialog(
                null,
                i18n.tr("Are you sure you want to close application?"),
                i18n.tr("Exit Confirmation"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);

        Logger.debug(i18n.tr("Exit confirmation"));

        switch (result) {
            case JOptionPane.YES_OPTION -> {
                Logger.debug(i18n.tr("Exit is confirmed"));
                closeWindow();
            }
            case JOptionPane.NO_OPTION -> Logger.debug(i18n.tr("Exit is cancelled"));
        }
    }

    private void closeWindow() {
        System.exit(0);
    }

    public void setSystemLookAndFeel(MainApplicationFrame frame) {
        setLookAndFeel(UIManager.getSystemLookAndFeelClassName(), frame);
        frame.invalidate();
    }

    public void setCrossPlatformLookAndFeel(MainApplicationFrame frame) {
        setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName(), frame);
        frame.invalidate();
    }

    private void setLookAndFeel(String className, MainApplicationFrame frame) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
