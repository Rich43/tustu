package java.awt;

import java.awt.MenuComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.peer.MenuItemPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleValue;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:java/awt/MenuItem.class */
public class MenuItem extends MenuComponent implements Accessible {
    boolean enabled;
    String label;
    String actionCommand;
    long eventMask;
    transient ActionListener actionListener;
    private MenuShortcut shortcut;
    private static final String base = "menuitem";
    private static int nameCounter;
    private static final long serialVersionUID = -21757335363267194L;
    private int menuItemSerializedDataVersion;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setMenuItemAccessor(new AWTAccessor.MenuItemAccessor() { // from class: java.awt.MenuItem.1
            @Override // sun.awt.AWTAccessor.MenuItemAccessor
            public boolean isEnabled(MenuItem menuItem) {
                return menuItem.enabled;
            }

            @Override // sun.awt.AWTAccessor.MenuItemAccessor
            public String getLabel(MenuItem menuItem) {
                return menuItem.label;
            }

            @Override // sun.awt.AWTAccessor.MenuItemAccessor
            public MenuShortcut getShortcut(MenuItem menuItem) {
                return menuItem.shortcut;
            }

            @Override // sun.awt.AWTAccessor.MenuItemAccessor
            public String getActionCommandImpl(MenuItem menuItem) {
                return menuItem.getActionCommandImpl();
            }

            @Override // sun.awt.AWTAccessor.MenuItemAccessor
            public boolean isItemEnabled(MenuItem menuItem) {
                return menuItem.isItemEnabled();
            }
        });
        nameCounter = 0;
    }

    public MenuItem() throws HeadlessException {
        this("", null);
    }

    public MenuItem(String str) throws HeadlessException {
        this(str, null);
    }

    public MenuItem(String str, MenuShortcut menuShortcut) throws HeadlessException {
        this.enabled = true;
        this.shortcut = null;
        this.menuItemSerializedDataVersion = 1;
        this.label = str;
        this.shortcut = menuShortcut;
    }

    @Override // java.awt.MenuComponent
    String constructComponentName() {
        String string;
        synchronized (MenuItem.class) {
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
                this.peer = Toolkit.getDefaultToolkit().createMenuItem(this);
            }
        }
    }

    public String getLabel() {
        return this.label;
    }

    public synchronized void setLabel(String str) {
        this.label = str;
        MenuItemPeer menuItemPeer = (MenuItemPeer) this.peer;
        if (menuItemPeer != null) {
            menuItemPeer.setLabel(str);
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public synchronized void setEnabled(boolean z2) {
        enable(z2);
    }

    @Deprecated
    public synchronized void enable() {
        this.enabled = true;
        MenuItemPeer menuItemPeer = (MenuItemPeer) this.peer;
        if (menuItemPeer != null) {
            menuItemPeer.setEnabled(true);
        }
    }

    @Deprecated
    public void enable(boolean z2) {
        if (z2) {
            enable();
        } else {
            disable();
        }
    }

    @Deprecated
    public synchronized void disable() {
        this.enabled = false;
        MenuItemPeer menuItemPeer = (MenuItemPeer) this.peer;
        if (menuItemPeer != null) {
            menuItemPeer.setEnabled(false);
        }
    }

    public MenuShortcut getShortcut() {
        return this.shortcut;
    }

    public void setShortcut(MenuShortcut menuShortcut) {
        this.shortcut = menuShortcut;
        MenuItemPeer menuItemPeer = (MenuItemPeer) this.peer;
        if (menuItemPeer != null) {
            menuItemPeer.setLabel(this.label);
        }
    }

    public void deleteShortcut() {
        this.shortcut = null;
        MenuItemPeer menuItemPeer = (MenuItemPeer) this.peer;
        if (menuItemPeer != null) {
            menuItemPeer.setLabel(this.label);
        }
    }

    void deleteShortcut(MenuShortcut menuShortcut) {
        if (menuShortcut.equals(this.shortcut)) {
            this.shortcut = null;
            MenuItemPeer menuItemPeer = (MenuItemPeer) this.peer;
            if (menuItemPeer != null) {
                menuItemPeer.setLabel(this.label);
            }
        }
    }

    void doMenuEvent(long j2, int i2) {
        Toolkit.getEventQueue().postEvent(new ActionEvent(this, 1001, getActionCommand(), j2, i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isItemEnabled() {
        if (!isEnabled()) {
            return false;
        }
        MenuContainer parent_NoClientCode = getParent_NoClientCode();
        while (parent_NoClientCode instanceof Menu) {
            Menu menu = (Menu) parent_NoClientCode;
            if (!menu.isEnabled()) {
                return false;
            }
            parent_NoClientCode = menu.getParent_NoClientCode();
            if (parent_NoClientCode == null) {
                return true;
            }
        }
        return true;
    }

    boolean handleShortcut(KeyEvent keyEvent) {
        MenuShortcut menuShortcut = new MenuShortcut(keyEvent.getKeyCode(), (keyEvent.getModifiers() & 1) > 0);
        MenuShortcut menuShortcut2 = new MenuShortcut(keyEvent.getExtendedKeyCode(), (keyEvent.getModifiers() & 1) > 0);
        if ((menuShortcut.equals(this.shortcut) || menuShortcut2.equals(this.shortcut)) && isItemEnabled()) {
            if (keyEvent.getID() == 401) {
                doMenuEvent(keyEvent.getWhen(), keyEvent.getModifiers());
                return true;
            }
            return true;
        }
        return false;
    }

    MenuItem getShortcutMenuItem(MenuShortcut menuShortcut) {
        if (menuShortcut.equals(this.shortcut)) {
            return this;
        }
        return null;
    }

    protected final void enableEvents(long j2) {
        this.eventMask |= j2;
        this.newEventsOnly = true;
    }

    protected final void disableEvents(long j2) {
        this.eventMask &= j2 ^ (-1);
    }

    public void setActionCommand(String str) {
        this.actionCommand = str;
    }

    public String getActionCommand() {
        return getActionCommandImpl();
    }

    final String getActionCommandImpl() {
        return this.actionCommand == null ? this.label : this.actionCommand;
    }

    public synchronized void addActionListener(ActionListener actionListener) {
        if (actionListener == null) {
            return;
        }
        this.actionListener = AWTEventMulticaster.add(this.actionListener, actionListener);
        this.newEventsOnly = true;
    }

    public synchronized void removeActionListener(ActionListener actionListener) {
        if (actionListener == null) {
            return;
        }
        this.actionListener = AWTEventMulticaster.remove(this.actionListener, actionListener);
    }

    public synchronized ActionListener[] getActionListeners() {
        return (ActionListener[]) getListeners(ActionListener.class);
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        ActionListener actionListener = null;
        if (cls == ActionListener.class) {
            actionListener = this.actionListener;
        }
        return (T[]) AWTEventMulticaster.getListeners(actionListener, cls);
    }

    @Override // java.awt.MenuComponent
    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof ActionEvent) {
            processActionEvent((ActionEvent) aWTEvent);
        }
    }

    @Override // java.awt.MenuComponent
    boolean eventEnabled(AWTEvent aWTEvent) {
        if (aWTEvent.id != 1001) {
            return super.eventEnabled(aWTEvent);
        }
        if ((this.eventMask & 128) != 0 || this.actionListener != null) {
            return true;
        }
        return false;
    }

    protected void processActionEvent(ActionEvent actionEvent) {
        ActionListener actionListener = this.actionListener;
        if (actionListener != null) {
            actionListener.actionPerformed(actionEvent);
        }
    }

    @Override // java.awt.MenuComponent
    public String paramString() {
        String str = ",label=" + this.label;
        if (this.shortcut != null) {
            str = str + ",shortcut=" + ((Object) this.shortcut);
        }
        return super.paramString() + str;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, "actionL", this.actionListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, HeadlessException {
        objectInputStream.defaultReadObject();
        while (true) {
            Object object = objectInputStream.readObject();
            if (null != object) {
                if ("actionL" == ((String) object).intern()) {
                    addActionListener((ActionListener) objectInputStream.readObject());
                } else {
                    objectInputStream.readObject();
                }
            } else {
                return;
            }
        }
    }

    @Override // java.awt.MenuComponent, javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTMenuItem();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/MenuItem$AccessibleAWTMenuItem.class */
    protected class AccessibleAWTMenuItem extends MenuComponent.AccessibleAWTMenuComponent implements AccessibleAction, AccessibleValue {
        private static final long serialVersionUID = -217847831945965825L;

        protected AccessibleAWTMenuItem() {
            super();
        }

        @Override // java.awt.MenuComponent.AccessibleAWTMenuComponent, javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            if (this.accessibleName != null) {
                return this.accessibleName;
            }
            if (MenuItem.this.getLabel() == null) {
                return super.getAccessibleName();
            }
            return MenuItem.this.getLabel();
        }

        @Override // java.awt.MenuComponent.AccessibleAWTMenuComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.MENU_ITEM;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        public int getAccessibleActionCount() {
            return 1;
        }

        public String getAccessibleActionDescription(int i2) {
            if (i2 == 0) {
                return "click";
            }
            return null;
        }

        public boolean doAccessibleAction(int i2) {
            if (i2 == 0) {
                Toolkit.getEventQueue().postEvent(new ActionEvent(MenuItem.this, 1001, MenuItem.this.getActionCommand(), EventQueue.getMostRecentEventTime(), 0));
                return true;
            }
            return false;
        }

        public Number getCurrentAccessibleValue() {
            return 0;
        }

        public boolean setCurrentAccessibleValue(Number number) {
            return false;
        }

        public Number getMinimumAccessibleValue() {
            return 0;
        }

        public Number getMaximumAccessibleValue() {
            return 0;
        }
    }
}
