package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.gui.MainFrame;
import ru.urfu.i18n.I18n;
import ru.urfu.i18n.I18nManager;

/**
 * <p>Меню для управления модами.</p>
 */
public final class ModMenuProvider implements MenuElementProvider {
    private final Logger log = LoggerFactory.getLogger(ModMenuProvider.class);
    private final I18n i18n = I18nManager.getInstance().getI18n();

    @Override
    public Component provide(MainFrame frame) {
        final JMenu menu = new JMenu(i18n.tr("Mods"));

        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                i18n.tr("Manage mods."));

        menu.add(createModLoadItem(frame));
        return menu;
    }

    /**
     * <p>Создаёт элемент меню для загрузки модов.</p>
     *
     * @param frame окно.
     * @return элемент меню.
     */
    private JMenuItem createModLoadItem(MainFrame frame) {
        final JMenuItem item = new JMenuItem(i18n.tr("Load"), KeyEvent.VK_S);
        item.addActionListener((event) -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(
                    new FileNameExtensionFilter("JAR files", "jar"));

            final int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile();
                log.debug("Mod {} was chosen", selectedFile.getAbsolutePath());
                frame.handleModFile(selectedFile);
            }
        });
        return item;
    }
}
