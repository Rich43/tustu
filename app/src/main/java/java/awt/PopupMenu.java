package java.awt;

import java.awt.Menu;
import java.awt.peer.PopupMenuPeer;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:java/awt/PopupMenu.class */
public class PopupMenu extends Menu {
    private static final String base = "popup";
    static int nameCounter = 0;
    transient boolean isTrayIconPopup;
    private static final long serialVersionUID = -4620452533522760060L;

    static {
        AWTAccessor.setPopupMenuAccessor(new AWTAccessor.PopupMenuAccessor() { // from class: java.awt.PopupMenu.1
            @Override // sun.awt.AWTAccessor.PopupMenuAccessor
            public boolean isTrayIconPopup(PopupMenu popupMenu) {
                return popupMenu.isTrayIconPopup;
            }
        });
    }

    public PopupMenu() throws HeadlessException {
        this("");
    }

    public PopupMenu(String str) throws HeadlessException {
        super(str);
        this.isTrayIconPopup = false;
    }

    @Override // java.awt.MenuComponent
    public MenuContainer getParent() {
        if (this.isTrayIconPopup) {
            return null;
        }
        return super.getParent();
    }

    @Override // java.awt.Menu, java.awt.MenuItem, java.awt.MenuComponent
    String constructComponentName() {
        String string;
        synchronized (PopupMenu.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    @Override // java.awt.Menu, java.awt.MenuItem
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.parent != null && !(this.parent instanceof Component)) {
                super.addNotify();
            } else {
                if (this.peer == null) {
                    this.peer = Toolkit.getDefaultToolkit().createPopupMenu(this);
                }
                int itemCount = getItemCount();
                for (int i2 = 0; i2 < itemCount; i2++) {
                    MenuItem item = getItem(i2);
                    item.parent = this;
                    item.addNotify();
                }
            }
        }
    }

    public void show(Component component, int i2, int i3) {
        MenuContainer menuContainer = this.parent;
        if (menuContainer == null) {
            throw new NullPointerException("parent is null");
        }
        if (!(menuContainer instanceof Component)) {
            throw new IllegalArgumentException("PopupMenus with non-Component parents cannot be shown");
        }
        Component component2 = (Component) menuContainer;
        if (component2 != component) {
            if (component2 instanceof Container) {
                if (!((Container) component2).isAncestorOf(component)) {
                    throw new IllegalArgumentException("origin not in parent's hierarchy");
                }
            } else {
                throw new IllegalArgumentException("origin not in parent's hierarchy");
            }
        }
        if (component2.getPeer() == null || !component2.isShowing()) {
            throw new RuntimeException("parent not showing on screen");
        }
        if (this.peer == null) {
            addNotify();
        }
        synchronized (getTreeLock()) {
            if (this.peer != null) {
                ((PopupMenuPeer) this.peer).show(new Event(component, 0L, 501, i2, i3, 0, 0));
            }
        }
    }

    @Override // java.awt.Menu, java.awt.MenuItem, java.awt.MenuComponent, javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTPopupMenu();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/PopupMenu$AccessibleAWTPopupMenu.class */
    protected class AccessibleAWTPopupMenu extends Menu.AccessibleAWTMenu {
        private static final long serialVersionUID = -4282044795947239955L;

        protected AccessibleAWTPopupMenu() {
            super();
        }

        @Override // java.awt.Menu.AccessibleAWTMenu, java.awt.MenuItem.AccessibleAWTMenuItem, java.awt.MenuComponent.AccessibleAWTMenuComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.POPUP_MENU;
        }
    }
}
