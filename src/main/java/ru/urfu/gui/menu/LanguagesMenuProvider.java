package ru.urfu.gui.menu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.Locale;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import org.xnap.commons.i18n.I18nManager;
import ru.urfu.gui.MainFrame;

final class LanguagesMenuProvider implements MenuElementProvider {
    private final Logger log = LoggerFactory.getLogger(LanguagesMenuProvider.class);
    private final I18n i18n = I18nFactory.getI18n(getClass());

    @Override
    public Component provide(MainFrame frame) {
        final JMenu menu = new JMenu(i18n.tr("Languages"));

        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                i18n.tr("Change application language."));

        menu.add(createSwitchToLanguageItem("English", Locale.ENGLISH));
        menu.add(createSwitchToLanguageItem("Русский", Locale.of("ru")));
        return menu;
    }

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
