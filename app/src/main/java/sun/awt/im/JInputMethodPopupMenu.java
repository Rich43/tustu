package sun.awt.im;

import java.awt.Component;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/* compiled from: InputMethodPopupMenu.java */
/* loaded from: rt.jar:sun/awt/im/JInputMethodPopupMenu.class */
class JInputMethodPopupMenu extends InputMethodPopupMenu {
    static JPopupMenu delegate = null;

    JInputMethodPopupMenu(String str) {
        synchronized (this) {
            if (delegate == null) {
                delegate = new JPopupMenu(str);
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
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    Object createSubmenu(String str) {
        return new JMenu(str);
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void add(Object obj) {
        delegate.add((JMenuItem) obj);
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void addMenuItem(String str, String str2, String str3) {
        addMenuItem(delegate, str, str2, str3);
    }

    @Override // sun.awt.im.InputMethodPopupMenu
    void addMenuItem(Object obj, String str, String str2, String str3) {
        JMenuItem jMenuItem;
        if (isSelected(str2, str3)) {
            jMenuItem = new JCheckBoxMenuItem(str, true);
        } else {
            jMenuItem = new JMenuItem(str);
        }
        jMenuItem.setActionCommand(str2);
        jMenuItem.addActionListener(this);
        jMenuItem.setEnabled(str2 != null);
        if (obj instanceof JMenu) {
            ((JMenu) obj).add(jMenuItem);
        } else {
            ((JPopupMenu) obj).add(jMenuItem);
        }
    }
}
