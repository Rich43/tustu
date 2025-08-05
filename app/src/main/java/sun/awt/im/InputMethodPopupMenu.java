package sun.awt.im;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.im.spi.InputMethodDescriptor;
import java.util.Locale;
import javax.swing.JDialog;
import javax.swing.JFrame;

/* loaded from: rt.jar:sun/awt/im/InputMethodPopupMenu.class */
abstract class InputMethodPopupMenu implements ActionListener {
    abstract void show(Component component, int i2, int i3);

    abstract void removeAll();

    abstract void addSeparator();

    abstract void addToComponent(Component component);

    abstract Object createSubmenu(String str);

    abstract void add(Object obj);

    abstract void addMenuItem(String str, String str2, String str3);

    abstract void addMenuItem(Object obj, String str, String str2, String str3);

    InputMethodPopupMenu() {
    }

    static InputMethodPopupMenu getInstance(Component component, String str) {
        if ((component instanceof JFrame) || (component instanceof JDialog)) {
            return new JInputMethodPopupMenu(str);
        }
        return new AWTInputMethodPopupMenu(str);
    }

    void addOneInputMethodToMenu(InputMethodLocator inputMethodLocator, String str) {
        int length;
        InputMethodDescriptor descriptor = inputMethodLocator.getDescriptor();
        String inputMethodDisplayName = descriptor.getInputMethodDisplayName(null, Locale.getDefault());
        String actionCommandString = inputMethodLocator.getActionCommandString();
        Locale[] availableLocales = null;
        try {
            availableLocales = descriptor.getAvailableLocales();
            length = availableLocales.length;
        } catch (AWTException e2) {
            length = 0;
        }
        if (length == 0) {
            addMenuItem(inputMethodDisplayName, null, str);
            return;
        }
        if (length == 1) {
            if (descriptor.hasDynamicLocaleList()) {
                inputMethodDisplayName = descriptor.getInputMethodDisplayName(availableLocales[0], Locale.getDefault());
                actionCommandString = inputMethodLocator.deriveLocator(availableLocales[0]).getActionCommandString();
            }
            addMenuItem(inputMethodDisplayName, actionCommandString, str);
            return;
        }
        Object objCreateSubmenu = createSubmenu(inputMethodDisplayName);
        add(objCreateSubmenu);
        for (int i2 = 0; i2 < length; i2++) {
            Locale locale = availableLocales[i2];
            addMenuItem(objCreateSubmenu, getLocaleName(locale), inputMethodLocator.deriveLocator(locale).getActionCommandString(), str);
        }
    }

    static boolean isSelected(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        if (str.equals(str2)) {
            return true;
        }
        int iIndexOf = str2.indexOf(10);
        if (iIndexOf != -1 && str2.substring(0, iIndexOf).equals(str)) {
            return true;
        }
        return false;
    }

    String getLocaleName(Locale locale) {
        String string = locale.toString();
        String property = Toolkit.getProperty("AWT.InputMethodLanguage." + string, null);
        if (property == null) {
            property = locale.getDisplayName();
            if (property == null || property.length() == 0) {
                property = string;
            }
        }
        return property;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        ((ExecutableInputMethodManager) InputMethodManager.getInstance()).changeInputMethod(actionEvent.getActionCommand());
    }
}
