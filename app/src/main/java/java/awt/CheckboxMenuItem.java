package java.awt;

import java.awt.MenuItem;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.peer.CheckboxMenuItemPeer;
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

/* loaded from: rt.jar:java/awt/CheckboxMenuItem.class */
public class CheckboxMenuItem extends MenuItem implements ItemSelectable, Accessible {
    boolean state;
    transient ItemListener itemListener;
    private static final String base = "chkmenuitem";
    private static int nameCounter;
    private static final long serialVersionUID = 6190621106981774043L;
    private int checkboxMenuItemSerializedDataVersion;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setCheckboxMenuItemAccessor(new AWTAccessor.CheckboxMenuItemAccessor() { // from class: java.awt.CheckboxMenuItem.1
            @Override // sun.awt.AWTAccessor.CheckboxMenuItemAccessor
            public boolean getState(CheckboxMenuItem checkboxMenuItem) {
                return checkboxMenuItem.state;
            }
        });
        nameCounter = 0;
    }

    public CheckboxMenuItem() throws HeadlessException {
        this("", false);
    }

    public CheckboxMenuItem(String str) throws HeadlessException {
        this(str, false);
    }

    public CheckboxMenuItem(String str, boolean z2) throws HeadlessException {
        super(str);
        this.state = false;
        this.checkboxMenuItemSerializedDataVersion = 1;
        this.state = z2;
    }

    @Override // java.awt.MenuItem, java.awt.MenuComponent
    String constructComponentName() {
        String string;
        synchronized (CheckboxMenuItem.class) {
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
                this.peer = Toolkit.getDefaultToolkit().createCheckboxMenuItem(this);
            }
            super.addNotify();
        }
    }

    public boolean getState() {
        return this.state;
    }

    public synchronized void setState(boolean z2) {
        this.state = z2;
        CheckboxMenuItemPeer checkboxMenuItemPeer = (CheckboxMenuItemPeer) this.peer;
        if (checkboxMenuItemPeer != null) {
            checkboxMenuItemPeer.setState(z2);
        }
    }

    @Override // java.awt.ItemSelectable
    public synchronized Object[] getSelectedObjects() {
        if (this.state) {
            return new Object[]{this.label};
        }
        return null;
    }

    @Override // java.awt.ItemSelectable
    public synchronized void addItemListener(ItemListener itemListener) {
        if (itemListener == null) {
            return;
        }
        this.itemListener = AWTEventMulticaster.add(this.itemListener, itemListener);
        this.newEventsOnly = true;
    }

    @Override // java.awt.ItemSelectable
    public synchronized void removeItemListener(ItemListener itemListener) {
        if (itemListener == null) {
            return;
        }
        this.itemListener = AWTEventMulticaster.remove(this.itemListener, itemListener);
    }

    public synchronized ItemListener[] getItemListeners() {
        return (ItemListener[]) getListeners(ItemListener.class);
    }

    @Override // java.awt.MenuItem
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        if (cls != ItemListener.class) {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(this.itemListener, cls);
    }

    @Override // java.awt.MenuItem, java.awt.MenuComponent
    boolean eventEnabled(AWTEvent aWTEvent) {
        if (aWTEvent.id != 701) {
            return super.eventEnabled(aWTEvent);
        }
        if ((this.eventMask & 512) != 0 || this.itemListener != null) {
            return true;
        }
        return false;
    }

    @Override // java.awt.MenuItem, java.awt.MenuComponent
    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof ItemEvent) {
            processItemEvent((ItemEvent) aWTEvent);
        } else {
            super.processEvent(aWTEvent);
        }
    }

    protected void processItemEvent(ItemEvent itemEvent) {
        ItemListener itemListener = this.itemListener;
        if (itemListener != null) {
            itemListener.itemStateChanged(itemEvent);
        }
    }

    @Override // java.awt.MenuItem
    void doMenuEvent(long j2, int i2) {
        setState(!this.state);
        Toolkit.getEventQueue().postEvent(new ItemEvent(this, 701, getLabel(), this.state ? 1 : 2));
    }

    @Override // java.awt.MenuItem, java.awt.MenuComponent
    public String paramString() {
        return super.paramString() + ",state=" + this.state;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, "itemL", this.itemListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        while (true) {
            Object object = objectInputStream.readObject();
            if (null != object) {
                if ("itemL" == ((String) object).intern()) {
                    addItemListener((ItemListener) objectInputStream.readObject());
                } else {
                    objectInputStream.readObject();
                }
            } else {
                return;
            }
        }
    }

    @Override // java.awt.MenuItem, java.awt.MenuComponent, javax.accessibility.Accessible
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTCheckboxMenuItem();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/CheckboxMenuItem$AccessibleAWTCheckboxMenuItem.class */
    protected class AccessibleAWTCheckboxMenuItem extends MenuItem.AccessibleAWTMenuItem implements AccessibleAction, AccessibleValue {
        private static final long serialVersionUID = -1122642964303476L;

        protected AccessibleAWTCheckboxMenuItem() {
            super();
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, javax.accessibility.AccessibleContext
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, javax.accessibility.AccessibleAction
        public int getAccessibleActionCount() {
            return 0;
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, javax.accessibility.AccessibleAction
        public String getAccessibleActionDescription(int i2) {
            return null;
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, javax.accessibility.AccessibleAction
        public boolean doAccessibleAction(int i2) {
            return false;
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            return null;
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            return false;
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return null;
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            return null;
        }

        @Override // java.awt.MenuItem.AccessibleAWTMenuItem, java.awt.MenuComponent.AccessibleAWTMenuComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.CHECK_BOX;
        }
    }
}
