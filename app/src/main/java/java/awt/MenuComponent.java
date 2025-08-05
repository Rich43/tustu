package java.awt;

import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.peer.MenuComponentPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.Locale;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleStateSet;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;

/* loaded from: rt.jar:java/awt/MenuComponent.class */
public abstract class MenuComponent implements Serializable {
    transient MenuComponentPeer peer;
    transient MenuContainer parent;
    transient AppContext appContext;
    volatile Font font;
    private String name;
    static final String actionListenerK = "actionL";
    static final String itemListenerK = "itemL";
    private static final long serialVersionUID = -4536902356223894379L;
    private boolean nameExplicitlySet = false;
    boolean newEventsOnly = false;
    private volatile transient AccessControlContext acc = AccessController.getContext();
    AccessibleContext accessibleContext = null;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setMenuComponentAccessor(new AWTAccessor.MenuComponentAccessor() { // from class: java.awt.MenuComponent.1
            @Override // sun.awt.AWTAccessor.MenuComponentAccessor
            public AppContext getAppContext(MenuComponent menuComponent) {
                return menuComponent.appContext;
            }

            @Override // sun.awt.AWTAccessor.MenuComponentAccessor
            public void setAppContext(MenuComponent menuComponent, AppContext appContext) {
                menuComponent.appContext = appContext;
            }

            @Override // sun.awt.AWTAccessor.MenuComponentAccessor
            public MenuContainer getParent(MenuComponent menuComponent) {
                return menuComponent.parent;
            }

            @Override // sun.awt.AWTAccessor.MenuComponentAccessor
            public Font getFont_NoClientCode(MenuComponent menuComponent) {
                return menuComponent.getFont_NoClientCode();
            }

            @Override // sun.awt.AWTAccessor.MenuComponentAccessor
            public <T extends MenuComponentPeer> T getPeer(MenuComponent menuComponent) {
                return (T) menuComponent.peer;
            }
        });
    }

    final AccessControlContext getAccessControlContext() {
        if (this.acc == null) {
            throw new SecurityException("MenuComponent is missing AccessControlContext");
        }
        return this.acc;
    }

    public MenuComponent() throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        this.appContext = AppContext.getAppContext();
    }

    String constructComponentName() {
        return null;
    }

    public String getName() {
        if (this.name == null && !this.nameExplicitlySet) {
            synchronized (this) {
                if (this.name == null && !this.nameExplicitlySet) {
                    this.name = constructComponentName();
                }
            }
        }
        return this.name;
    }

    public void setName(String str) {
        synchronized (this) {
            this.name = str;
            this.nameExplicitlySet = true;
        }
    }

    public MenuContainer getParent() {
        return getParent_NoClientCode();
    }

    final MenuContainer getParent_NoClientCode() {
        return this.parent;
    }

    @Deprecated
    public MenuComponentPeer getPeer() {
        return this.peer;
    }

    public Font getFont() {
        Font font = this.font;
        if (font != null) {
            return font;
        }
        MenuContainer menuContainer = this.parent;
        if (menuContainer != null) {
            return menuContainer.getFont();
        }
        return null;
    }

    final Font getFont_NoClientCode() {
        Font font_NoClientCode = this.font;
        if (font_NoClientCode != null) {
            return font_NoClientCode;
        }
        Object obj = this.parent;
        if (obj != null) {
            if (obj instanceof Component) {
                font_NoClientCode = ((Component) obj).getFont_NoClientCode();
            } else if (obj instanceof MenuComponent) {
                font_NoClientCode = ((MenuComponent) obj).getFont_NoClientCode();
            }
        }
        return font_NoClientCode;
    }

    public void setFont(Font font) {
        synchronized (getTreeLock()) {
            this.font = font;
            MenuComponentPeer menuComponentPeer = this.peer;
            if (menuComponentPeer != null) {
                menuComponentPeer.setFont(font);
            }
        }
    }

    public void removeNotify() {
        synchronized (getTreeLock()) {
            MenuComponentPeer menuComponentPeer = this.peer;
            if (menuComponentPeer != null) {
                Toolkit.getEventQueue().removeSourceEvents(this, true);
                this.peer = null;
                menuComponentPeer.dispose();
            }
        }
    }

    @Deprecated
    public boolean postEvent(Event event) {
        MenuContainer menuContainer = this.parent;
        if (menuContainer != null) {
            menuContainer.postEvent(event);
            return false;
        }
        return false;
    }

    public final void dispatchEvent(AWTEvent aWTEvent) {
        dispatchEventImpl(aWTEvent);
    }

    void dispatchEventImpl(AWTEvent aWTEvent) {
        EventQueue.setCurrentEventAndMostRecentTime(aWTEvent);
        Toolkit.getDefaultToolkit().notifyAWTEventListeners(aWTEvent);
        if (this.newEventsOnly || (this.parent != null && (this.parent instanceof MenuComponent) && ((MenuComponent) this.parent).newEventsOnly)) {
            if (eventEnabled(aWTEvent)) {
                processEvent(aWTEvent);
                return;
            } else {
                if ((aWTEvent instanceof ActionEvent) && this.parent != null) {
                    aWTEvent.setSource(this.parent);
                    ((MenuComponent) this.parent).dispatchEvent(aWTEvent);
                    return;
                }
                return;
            }
        }
        Event eventConvertToOld = aWTEvent.convertToOld();
        if (eventConvertToOld != null) {
            postEvent(eventConvertToOld);
        }
    }

    boolean eventEnabled(AWTEvent aWTEvent) {
        return false;
    }

    protected void processEvent(AWTEvent aWTEvent) {
    }

    protected String paramString() {
        String name = getName();
        return name != null ? name : "";
    }

    public String toString() {
        return getClass().getName() + "[" + paramString() + "]";
    }

    protected final Object getTreeLock() {
        return Component.LOCK;
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
        this.acc = AccessController.getContext();
        objectInputStream.defaultReadObject();
        this.appContext = AppContext.getAppContext();
    }

    public AccessibleContext getAccessibleContext() {
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/MenuComponent$AccessibleAWTMenuComponent.class */
    protected abstract class AccessibleAWTMenuComponent extends AccessibleContext implements Serializable, AccessibleComponent, AccessibleSelection {
        private static final long serialVersionUID = -4269533416223798698L;

        protected AccessibleAWTMenuComponent() {
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            return this.accessibleName;
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleDescription() {
            return this.accessibleDescription;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.AWT_COMPONENT;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            return MenuComponent.this.getAccessibleStateSet();
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleParent() {
            if (this.accessibleParent != null) {
                return this.accessibleParent;
            }
            MenuContainer parent = MenuComponent.this.getParent();
            if (parent instanceof Accessible) {
                return (Accessible) parent;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleIndexInParent() {
            return MenuComponent.this.getAccessibleIndexInParent();
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return 0;
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public Locale getLocale() {
            MenuContainer parent = MenuComponent.this.getParent();
            if (parent instanceof Component) {
                return ((Component) parent).getLocale();
            }
            return Locale.getDefault();
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleComponent getAccessibleComponent() {
            return this;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getBackground() {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBackground(Color color) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getForeground() {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setForeground(Color color) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Cursor getCursor() {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setCursor(Cursor cursor) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Font getFont() {
            return MenuComponent.this.getFont();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setFont(Font font) {
            MenuComponent.this.setFont(font);
        }

        @Override // javax.accessibility.AccessibleComponent
        public FontMetrics getFontMetrics(Font font) {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isEnabled() {
            return true;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setEnabled(boolean z2) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isVisible() {
            return true;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setVisible(boolean z2) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isShowing() {
            return true;
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean contains(Point point) {
            return false;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocationOnScreen() {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocation() {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setLocation(Point point) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Rectangle getBounds() {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBounds(Rectangle rectangle) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Dimension getSize() {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setSize(Dimension dimension) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isFocusTraversable() {
            return true;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void requestFocus() {
        }

        @Override // javax.accessibility.AccessibleComponent
        public void addFocusListener(FocusListener focusListener) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public void removeFocusListener(FocusListener focusListener) {
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            return 0;
        }

        @Override // javax.accessibility.AccessibleSelection
        public Accessible getAccessibleSelection(int i2) {
            return null;
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            return false;
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
        }
    }

    int getAccessibleIndexInParent() {
        Object obj = this.parent;
        if (!(obj instanceof MenuComponent)) {
            return -1;
        }
        return ((MenuComponent) obj).getAccessibleChildIndex(this);
    }

    int getAccessibleChildIndex(MenuComponent menuComponent) {
        return -1;
    }

    AccessibleStateSet getAccessibleStateSet() {
        return new AccessibleStateSet();
    }
}
