package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.Locale;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.gui.MainFrame;
import ru.urfu.i18n.I18n;
import ru.urfu.i18n.I18nManager;

/**
 * <p>Создаёт меню для смены языка.</p>
 */
final class LanguageMenuProvider implements MenuElementProvider {
    private final Logger log = LoggerFactory.getLogger(LanguageMenuProvider.class);
    private final I18n i18n = I18nManager.getInstance().getI18n();

    @Override
    public Component provide(MainFrame frame) {
        final JMenu menu = new JMenu(i18n.tr("Language"));

        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                i18n.tr("Change application language."));

        menu.add(createSwitchToLanguageItem("English", Locale.ENGLISH));
        menu.add(createSwitchToLanguageItem("Русский", Locale.of("ru")));
        return menu;
    }

    /**
     * <p>Создаёт кнопку для смены языка.</p>
     *
     * @param languageName название языка для отображения.
     * @param locale       локаль, соответсвующая языку.
     * @return кнопка.
     */
    private JMenuItem createSwitchToLanguageItem(String languageName, Locale locale) {
        final JMenuItem menuItem = new JMenuItem(languageName, KeyEvent.VK_S);
        menuItem.addActionListener((event) -> {
            Locale.setDefault(locale);
            I18nManager.getInstance().setDefaultLocale(locale);
            log.debug("Locale changed to {}", Locale.getDefault());
        });
        return menuItem;
    }
}
