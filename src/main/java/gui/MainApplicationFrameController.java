package gui;

import log.Logger;

import javax.swing.*;

public class MainApplicationFrameController {
    public void showExitConfirmationPopUp(MainApplicationFrame frame) {
        final int result = JOptionPane.showOptionDialog(
                null,
                "Are You Sure to Close Application?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);

        Logger.debug("Запрос на выход");

        switch (result) {
            case JOptionPane.YES_OPTION -> {
                Logger.debug("Подтверждение выхода");
                closeWindow();
            }
            case JOptionPane.NO_OPTION -> Logger.debug("Отмена выхода");
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
        }
        catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
