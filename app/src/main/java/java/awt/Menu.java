package java.awt;

import java.awt.MenuItem;
import java.awt.event.KeyEvent;
import java.awt.peer.MenuPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import sun.awt.AWTAccessor;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/awt/Menu.class */
public class Menu extends MenuItem implements MenuContainer, Accessible {
    Vector<MenuComponent> items;
    boolean tearOff;
    boolean isHelpMenu;
    private static final String base = "menu";
    private static int nameCounter;
    private static final long serialVersionUID = -8809584163345499784L;
    private int menuSerializedDataVersion;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setMenuAccessor(new AWTAccessor.MenuAccessor() { // from class: java.awt.Menu.1
            @Override // sun.awt.AWTAccessor.MenuAccessor
            public Vector<MenuComponent> getItems(Menu menu) {
                return menu.items;
            }
        });
        nameCounter = 0;
    }

    public Menu() throws HeadlessException {
        this("", false);
    }

    public Menu(String str) throws HeadlessException {
        this(str, false);
    }

    public Menu(String str, boolean z2) throws HeadlessException {
        super(str);
        this.items = new Vector<>();
        this.menuSerializedDataVersion = 1;
        this.tearOff = z2;
    }

    @Override // java.awt.MenuItem, java.awt.MenuComponent
    String constructComponentName() {
        String string;
        synchronized (Menu.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    @Override // java.awt.MenuItem
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                this.peer = Toolkit.getDefaultToolkit().createMenu(this);
            }
            int itemCount = getItemCount();
            for (int i2 = 0; i2 < itemCount; i2++) {
                MenuItem item = getItem(i2);
                item.parent = this;
                item.addNotify();
            }
        }
    }

    @Override // java.awt.MenuComponent
    public void removeNotify() {
        synchronized (getTreeLock()) {
            int itemCount = getItemCount();
            for (int i2 = 0; i2 < itemCount; i2++) {
                getItem(i2).removeNotify();
            }
            super.removeNotify();
        }
    }

    public boolean isTearOff() {
        return this.tearOff;
    }

    public int getItemCount() {
        return countItems();
    }

    @Deprecated
    public int countItems() {
        return countItemsImpl();
    }

    final int countItemsImpl() {
        return this.items.size();
    }

    public MenuItem getItem(int i2) {
        return getItemImpl(i2);
    }

    final MenuItem getItemImpl(int i2) {
        return (MenuItem) this.items.elementAt(i2);
    }

    public MenuItem add(MenuItem menuItem) {
        synchronized (getTreeLock()) {
            if (menuItem.parent != null) {
                menuItem.parent.remove(menuItem);
            }
            this.items.addElement(menuItem);
            menuItem.parent = this;
            MenuPeer menuPeer = (MenuPeer) this.peer;
            if (menuPeer != null) {
                menuItem.addNotify();
                menuPeer.addItem(menuItem);
            }
        }
        return menuItem;
    }

    public void add(String str) {
        add(new MenuItem(str));
    }

    public void insert(MenuItem menuItem, int i2) {
        synchronized (getTreeLock()) {
            if (i2 < 0) {
                throw new IllegalArgumentException("index less than zero.");
            }
            int itemCount = getItemCount();
            Vector vector = new Vector();
            for (int i3 = i2; i3 < itemCount; i3++) {
                vector.addElement(getItem(i2));
                remove(i2);
            }
            add(menuItem);
            for (int i4 = 0; i4 < vector.size(); i4++) {
                add((MenuItem) vector.elementAt(i4));
            }
        }
    }

    public void insert(String str, int i2) {
        insert(new MenuItem(str), i2);
    }

    public void addSeparator() {
        add(LanguageTag.SEP);
    }

    public void insertSeparator(int i2) {
        synchronized (getTreeLock()) {
            if (i2 < 0) {
                throw new IllegalArgumentException("index less than zero.");
            }
            int itemCount = getItemCount();
            Vector vector = new Vector();
            for (int i3 = i2; i3 < itemCount; i3++) {
                vector.addElement(getItem(i2));
                remove(i2);
            }
            addSeparator();
            for (int i4 = 0; i4 < vector.size(); i4++) {
                add((MenuItem) vector.elementAt(i4));
            }
        }
    }

    public void remove(int i2) {
        synchronized (getTreeLock()) {
            MenuItem item = getItem(i2);
            this.items.removeElementAt(i2);
            MenuPeer menuPeer = (MenuPeer) this.peer;
            if (menuPeer != null) {
                menuPeer.delItem(i2);
                item.removeNotify();
                item.parent = null;
            }
        }
    }

    @Override // java.awt.MenuContainer
    public void remove(MenuComponent menuComponent) {
        synchronized (getTreeLock()) {
            int iIndexOf = this.items.indexOf(menuComponent);
            if (iIndexOf >= 0) {
                remove(iIndexOf);
            }
        }
    }

    public void removeAll() {
        synchronized (getTreeLock()) {
            for (int itemCount = getItemCount() - 1; itemCount >= 0; itemCount--) {
                remove(itemCount);
            }
        }
    }

    @Override // java.awt.MenuItem
    boolean handleShortcut(KeyEvent keyEvent) {
        int itemCount = getItemCount();
        for (int i2 = 0; i2 < itemCount; i2++) {
            if (getItem(i2).handleShortcut(keyEvent)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.awt.MenuItem
    MenuItem getShortcutMenuItem(MenuShortcut menuShortcut) {
        int itemCount = getItemCount();
        for (int i2 = 0; i2 < itemCount; i2++) {
            MenuItem shortcutMenuItem = getItem(i2).getShortcutMenuItem(menuShortcut);
            if (shortcutMenuItem != null) {
                return shortcutMenuItem;
            }
        }
        return null;
    }

    synchronized Enumeration<MenuShortcut> shortcuts() {
        Vector vector = new Vector();
        int itemCount = getItemCount();
        for (int i2 = 0; i2 < itemCount; i2++) {
            MenuItem item = getItem(i2);
            if (item instanceof Menu) {
                Enumeration<MenuShortcut> enumerationShortcuts = ((Menu) item).shortcuts();
                while (enumerationShortcuts.hasMoreElements()) {
                    vector.addElement(enumerationShortcuts.nextElement());
                }
            } else {
                MenuShortcut shortcut = item.getShortcut();
                if (shortcut != null) {
                    vector.addElement(shortcut);
                }
            }
        }
        return vector.elements();
    }

    @Override // java.awt.MenuItem
    void deleteShortcut(MenuShortcut menuShortcut) {
        int itemCount = getItemCount();
        for (int i2 = 0; i2 < itemCount; i2++) {
            getItem(i2).deleteShortcut(menuShortcut);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, HeadlessException {
        objectInputStream.defaultReadObject();
        for (int i2 = 0; i2 < this.items.size(); i2++) {
            ((MenuItem) this.items.elementAt(i2)).parent = this;
        }
    }

    @Override // java.awt.MenuItem, java.awt.MenuComponent
    public String paramString() {
        return super.paramString() + (",tearOff=" + this.tearOff + ",isHelpMenu=" + this.isHelpMenu);
    }

    @Override // java.awt.MenuItem, java.awt.MenuComponent, javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTMenu();
        }
        return this.accessibleContext;
    }

    @Override // java.awt.MenuComponent
    int getAccessibleChildIndex(MenuComponent menuComponent) {
        return this.items.indexOf(menuComponent);
    }

    /* loaded from: rt.jar:java/awt/Menu$AccessibleAWTMenu.class */
    protected class AccessibleAWTMenu extends MenuItem.AccessibleAWTMenuItem {
        private static final long serialVersionUID = 5228160894980069094L;

        protected AccessibleAWTMenu() {
            super();
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, java.awt.MenuComponent.AccessibleAWTMenuComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.MENU;
        }
    }
}
