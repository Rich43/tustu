package sun.awt.im;

import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;

/* compiled from: InputMethodPopupMenu.java */
/* loaded from: rt.jar:sun/awt/im/AWTInputMethodPopupMenu.class */
class AWTInputMethodPopupMenu extends InputMethodPopupMenu {
    static PopupMenu delegate = null;

    AWTInputMethodPopupMenu(String str) {
        synchronized (this) {
            if (delegate == null) {
                delegate = new PopupMenu(str);
            }
        }
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void show(Component component, int i2, int i3) {
        delegate.show(component, i2, i3);
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void removeAll() {
        delegate.removeAll();
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void addSeparator() {
        delegate.addSeparator();
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void addToComponent(Component component) {
        component.add(delegate);
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    Object createSubmenu(String str) {
        return new Menu(str);
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void add(Object obj) {
        delegate.add((MenuItem) obj);
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void addMenuItem(String str, String str2, String str3) {
        addMenuItem(delegate, str, str2, str3);
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void addMenuItem(Object obj, String str, String str2, String str3) {
        MenuItem menuItem;
        if (isSelected(str2, str3)) {
            menuItem = new CheckboxMenuItem(str, true);
        } else {
            menuItem = new MenuItem(str);
        }
        menuItem.setActionCommand(str2);
        menuItem.addActionListener(this);
        menuItem.setEnabled(str2 != null);
        ((Menu) obj).add(menuItem);
    }
}
