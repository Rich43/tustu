package java.awt;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.peer.CheckboxPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleValue;

/* loaded from: rt.jar:java/awt/Checkbox.class */
public class Checkbox extends Component implements ItemSelectable, Accessible {
    String label;
    boolean state;
    CheckboxGroup group;
    transient ItemListener itemListener;
    private static final String base = "checkbox";
    private static int nameCounter;
    private static final long serialVersionUID = 7270714317450821763L;
    private int checkboxSerializedDataVersion;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        nameCounter = 0;
    }

    void setStateInternal(boolean z2) {
        this.state = z2;
        CheckboxPeer checkboxPeer = (CheckboxPeer) this.peer;
        if (checkboxPeer != null) {
            checkboxPeer.setState(z2);
        }
    }

    public Checkbox() throws HeadlessException {
        this("", false, (CheckboxGroup) null);
    }

    public Checkbox(String str) throws HeadlessException {
        this(str, false, (CheckboxGroup) null);
    }

    public Checkbox(String str, boolean z2) throws HeadlessException {
        this(str, z2, (CheckboxGroup) null);
    }

    public Checkbox(String str, boolean z2, CheckboxGroup checkboxGroup) throws HeadlessException {
        this.checkboxSerializedDataVersion = 1;
        GraphicsEnvironment.checkHeadless();
        this.label = str;
        this.state = z2;
        this.group = checkboxGroup;
        if (z2 && checkboxGroup != null) {
            checkboxGroup.setSelectedCheckbox(this);
        }
    }

    public Checkbox(String str, CheckboxGroup checkboxGroup, boolean z2) throws HeadlessException {
        this(str, z2, checkboxGroup);
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Checkbox.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    @Override // java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                this.peer = getToolkit().createCheckbox(this);
            }
            super.addNotify();
        }
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        boolean z2 = false;
        synchronized (this) {
            if (str != this.label && (this.label == null || !this.label.equals(str))) {
                this.label = str;
                CheckboxPeer checkboxPeer = (CheckboxPeer) this.peer;
                if (checkboxPeer != null) {
                    checkboxPeer.setLabel(str);
                }
                z2 = true;
            }
        }
        if (z2) {
            invalidateIfValid();
        }
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean z2) {
        CheckboxGroup checkboxGroup = this.group;
        if (checkboxGroup != null) {
            if (z2) {
                checkboxGroup.setSelectedCheckbox(this);
            } else if (checkboxGroup.getSelectedCheckbox() == this) {
                z2 = true;
            }
        }
        setStateInternal(z2);
    }

    @Override // java.awt.ItemSelectable
    public Object[] getSelectedObjects() {
        if (this.state) {
            return new Object[]{this.label};
        }
        return null;
    }

    public CheckboxGroup getCheckboxGroup() {
        return this.group;
    }

    public void setCheckboxGroup(CheckboxGroup checkboxGroup) {
        CheckboxGroup checkboxGroup2;
        boolean state;
        if (this.group == checkboxGroup) {
            return;
        }
        synchronized (this) {
            checkboxGroup2 = this.group;
            state = getState();
            this.group = checkboxGroup;
            CheckboxPeer checkboxPeer = (CheckboxPeer) this.peer;
            if (checkboxPeer != null) {
                checkboxPeer.setCheckboxGroup(checkboxGroup);
            }
            if (this.group != null && getState()) {
                if (this.group.getSelectedCheckbox() != null) {
                    setState(false);
                } else {
                    this.group.setSelectedCheckbox(this);
                }
            }
        }
        if (checkboxGroup2 != null && state) {
            checkboxGroup2.setSelectedCheckbox(null);
        }
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

    @Override // java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        if (cls != ItemListener.class) {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(this.itemListener, cls);
    }

    @Override // java.awt.Component
    boolean eventEnabled(AWTEvent aWTEvent) {
        if (aWTEvent.id != 701) {
            return super.eventEnabled(aWTEvent);
        }
        if ((this.eventMask & 512) != 0 || this.itemListener != null) {
            return true;
        }
        return false;
    }

    @Override // java.awt.Component
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

    @Override // java.awt.Component
    protected String paramString() {
        String strParamString = super.paramString();
        String str = this.label;
        if (str != null) {
            strParamString = strParamString + ",label=" + str;
        }
        return strParamString + ",state=" + this.state;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, "itemL", this.itemListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
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

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTCheckbox();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/Checkbox$AccessibleAWTCheckbox.class */
    protected class AccessibleAWTCheckbox extends Component.AccessibleAWTComponent implements ItemListener, AccessibleAction, AccessibleValue {
        private static final long serialVersionUID = 7881579233144754107L;

        public AccessibleAWTCheckbox() {
            super();
            Checkbox.this.addItemListener(this);
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
            Checkbox checkbox = (Checkbox) itemEvent.getSource();
            if (Checkbox.this.accessibleContext != null) {
                if (checkbox.getState()) {
                    Checkbox.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.CHECKED);
                } else {
                    Checkbox.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.CHECKED, null);
                }
            }
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // javax.accessibility.AccessibleAction
        public int getAccessibleActionCount() {
            return 0;
        }

        @Override // javax.accessibility.AccessibleAction
        public String getAccessibleActionDescription(int i2) {
            return null;
        }

        @Override // javax.accessibility.AccessibleAction
        public boolean doAccessibleAction(int i2) {
            return false;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            return null;
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            return false;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return null;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            return null;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.CHECK_BOX;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (Checkbox.this.getState()) {
                accessibleStateSet.add(AccessibleState.CHECKED);
            }
            return accessibleStateSet;
        }
    }
}
