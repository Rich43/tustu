package java.awt;

import java.awt.MenuComponent;
import java.awt.event.KeyEvent;
import java.awt.peer.MenuBarPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:java/awt/MenuBar.class */
public class MenuBar extends MenuComponent implements MenuContainer, Accessible {
    Menu helpMenu;
    private static final String base = "menubar";
    private static int nameCounter;
    private static final long serialVersionUID = -4930327919388951260L;
    Vector<Menu> menus = new Vector<>();
    private int menuBarSerializedDataVersion = 1;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setMenuBarAccessor(new AWTAccessor.MenuBarAccessor() { // from class: java.awt.MenuBar.1
            @Override // sun.awt.AWTAccessor.MenuBarAccessor
            public Menu getHelpMenu(MenuBar menuBar) {
                return menuBar.helpMenu;
            }

            @Override // sun.awt.AWTAccessor.MenuBarAccessor
            public Vector<Menu> getMenus(MenuBar menuBar) {
                return menuBar.menus;
            }
        });
        nameCounter = 0;
    }

    @Override // java.awt.MenuComponent
    String constructComponentName() {
        String string;
        synchronized (MenuBar.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                this.peer = Toolkit.getDefaultToolkit().createMenuBar(this);
            }
            int menuCount = getMenuCount();
            for (int i2 = 0; i2 < menuCount; i2++) {
                getMenu(i2).addNotify();
            }
        }
    }

    @Override // java.awt.MenuComponent
    public void removeNotify() {
        synchronized (getTreeLock()) {
            int menuCount = getMenuCount();
            for (int i2 = 0; i2 < menuCount; i2++) {
                getMenu(i2).removeNotify();
            }
            super.removeNotify();
        }
    }

    public Menu getHelpMenu() {
        return this.helpMenu;
    }

    public void setHelpMenu(Menu menu) {
        synchronized (getTreeLock()) {
            if (this.helpMenu == menu) {
                return;
            }
            if (this.helpMenu != null) {
                remove(this.helpMenu);
            }
            this.helpMenu = menu;
            if (menu != null) {
                if (menu.parent != this) {
                    add(menu);
                }
                menu.isHelpMenu = true;
                menu.parent = this;
                MenuBarPeer menuBarPeer = (MenuBarPeer) this.peer;
                if (menuBarPeer != null) {
                    if (menu.peer == null) {
                        menu.addNotify();
                    }
                    menuBarPeer.addHelpMenu(menu);
                }
            }
        }
    }

    public Menu add(Menu menu) {
        synchronized (getTreeLock()) {
            if (menu.parent != null) {
                menu.parent.remove(menu);
            }
            menu.parent = this;
            MenuBarPeer menuBarPeer = (MenuBarPeer) this.peer;
            if (menuBarPeer != null) {
                if (menu.peer == null) {
                    menu.addNotify();
                }
                this.menus.addElement(menu);
                menuBarPeer.addMenu(menu);
            } else {
                this.menus.addElement(menu);
            }
        }
        return menu;
    }

    public void remove(int i2) {
        synchronized (getTreeLock()) {
            Menu menu = getMenu(i2);
            this.menus.removeElementAt(i2);
            MenuBarPeer menuBarPeer = (MenuBarPeer) this.peer;
            if (menuBarPeer != null) {
                menuBarPeer.delMenu(i2);
                menu.removeNotify();
                menu.parent = null;
            }
            if (this.helpMenu == menu) {
                this.helpMenu = null;
                menu.isHelpMenu = false;
            }
        }
    }

    @Override // java.awt.MenuContainer
    public void remove(MenuComponent menuComponent) {
        synchronized (getTreeLock()) {
            int iIndexOf = this.menus.indexOf(menuComponent);
            if (iIndexOf >= 0) {
                remove(iIndexOf);
            }
        }
    }

    public int getMenuCount() {
        return countMenus();
    }

    @Deprecated
    public int countMenus() {
        return getMenuCountImpl();
    }

    final int getMenuCountImpl() {
        return this.menus.size();
    }

    public Menu getMenu(int i2) {
        return getMenuImpl(i2);
    }

    final Menu getMenuImpl(int i2) {
        return this.menus.elementAt(i2);
    }

    public synchronized Enumeration<MenuShortcut> shortcuts() {
        Vector vector = new Vector();
        int menuCount = getMenuCount();
        for (int i2 = 0; i2 < menuCount; i2++) {
            Enumeration<MenuShortcut> enumerationShortcuts = getMenu(i2).shortcuts();
            while (enumerationShortcuts.hasMoreElements()) {
                vector.addElement(enumerationShortcuts.nextElement());
            }
        }
        return vector.elements();
    }

    public MenuItem getShortcutMenuItem(MenuShortcut menuShortcut) {
        int menuCount = getMenuCount();
        for (int i2 = 0; i2 < menuCount; i2++) {
            MenuItem shortcutMenuItem = getMenu(i2).getShortcutMenuItem(menuShortcut);
            if (shortcutMenuItem != null) {
                return shortcutMenuItem;
            }
        }
        return null;
    }

    boolean handleShortcut(KeyEvent keyEvent) throws HeadlessException {
        int id = keyEvent.getID();
        if (id != 401 && id != 402) {
            return false;
        }
        if ((keyEvent.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) == 0) {
            return false;
        }
        int menuCount = getMenuCount();
        for (int i2 = 0; i2 < menuCount; i2++) {
            if (getMenu(i2).handleShortcut(keyEvent)) {
                return true;
            }
        }
        return false;
    }

    public void deleteShortcut(MenuShortcut menuShortcut) {
        int menuCount = getMenuCount();
        for (int i2 = 0; i2 < menuCount; i2++) {
            getMenu(i2).deleteShortcut(menuShortcut);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, HeadlessException {
        objectInputStream.defaultReadObject();
        for (int i2 = 0; i2 < this.menus.size(); i2++) {
            this.menus.elementAt(i2).parent = this;
        }
    }

    @Override // java.awt.MenuComponent, javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTMenuBar();
        }
        return this.accessibleContext;
    }

    @Override // java.awt.MenuComponent
    int getAccessibleChildIndex(MenuComponent menuComponent) {
        return this.menus.indexOf(menuComponent);
    }

    /* loaded from: rt.jar:java/awt/MenuBar$AccessibleAWTMenuBar.class */
    protected class AccessibleAWTMenuBar extends MenuComponent.AccessibleAWTMenuComponent {
        private static final long serialVersionUID = -8577604491830083815L;

        protected AccessibleAWTMenuBar() {
            super();
        }

        @Override // java.awt.MenuComponent.AccessibleAWTMenuComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.MENU_BAR;
        }
    }
}
