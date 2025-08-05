package java.awt;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.ButtonPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleValue;

/* loaded from: rt.jar:java/awt/Button.class */
public class Button extends Component implements Accessible {
    String label;
    String actionCommand;
    transient ActionListener actionListener;
    private static final String base = "button";
    private static int nameCounter = 0;
    private static final long serialVersionUID = -8774683716313001058L;
    private int buttonSerializedDataVersion;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    public Button() throws HeadlessException {
        this("");
    }

    public Button(String str) throws HeadlessException {
        this.buttonSerializedDataVersion = 1;
        GraphicsEnvironment.checkHeadless();
        this.label = str;
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Button.class) {
            StringBuilder sbAppend = new StringBuilder().append("button");
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
                this.peer = getToolkit().createButton(this);
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
                ButtonPeer buttonPeer = (ButtonPeer) this.peer;
                if (buttonPeer != null) {
                    buttonPeer.setLabel(str);
                }
                z2 = true;
            }
        }
        if (z2) {
            invalidateIfValid();
        }
    }

    public void setActionCommand(String str) {
        this.actionCommand = str;
    }

    public String getActionCommand() {
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

    @Override // java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        if (cls != ActionListener.class) {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(this.actionListener, cls);
    }

    @Override // java.awt.Component
    boolean eventEnabled(AWTEvent aWTEvent) {
        if (aWTEvent.id != 1001) {
            return super.eventEnabled(aWTEvent);
        }
        if ((this.eventMask & 128) != 0 || this.actionListener != null) {
            return true;
        }
        return false;
    }

    @Override // java.awt.Component
    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof ActionEvent) {
            processActionEvent((ActionEvent) aWTEvent);
        } else {
            super.processEvent(aWTEvent);
        }
    }

    protected void processActionEvent(ActionEvent actionEvent) {
        ActionListener actionListener = this.actionListener;
        if (actionListener != null) {
            actionListener.actionPerformed(actionEvent);
        }
    }

    @Override // java.awt.Component
    protected String paramString() {
        return super.paramString() + ",label=" + this.label;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, "actionL", this.actionListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
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

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTButton();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/Button$AccessibleAWTButton.class */
    protected class AccessibleAWTButton extends Component.AccessibleAWTComponent implements AccessibleAction, AccessibleValue {
        private static final long serialVersionUID = -5932203980244017102L;

        protected AccessibleAWTButton() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            if (this.accessibleName != null) {
                return this.accessibleName;
            }
            if (Button.this.getLabel() == null) {
                return super.getAccessibleName();
            }
            return Button.this.getLabel();
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
            return 1;
        }

        @Override // javax.accessibility.AccessibleAction
        public String getAccessibleActionDescription(int i2) {
            if (i2 == 0) {
                return "click";
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleAction
        public boolean doAccessibleAction(int i2) {
            if (i2 == 0) {
                Toolkit.getEventQueue().postEvent(new ActionEvent(Button.this, 1001, Button.this.getActionCommand()));
                return true;
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            return 0;
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            return false;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return 0;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            return 0;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PUSH_BUTTON;
        }
    }
}
