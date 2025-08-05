package java.awt;

import java.awt.TextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.TextFieldPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;

/* loaded from: rt.jar:java/awt/TextField.class */
public class TextField extends TextComponent {
    int columns;
    char echoChar;
    transient ActionListener actionListener;
    private static final String base = "textfield";
    private static int nameCounter = 0;
    private static final long serialVersionUID = -2966288784432217853L;
    private int textFieldSerializedDataVersion;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    public TextField() throws HeadlessException {
        this("", 0);
    }

    public TextField(String str) throws HeadlessException {
        this(str, str != null ? str.length() : 0);
    }

    public TextField(int i2) throws HeadlessException {
        this("", i2);
    }

    public TextField(String str, int i2) throws HeadlessException {
        super(str);
        this.textFieldSerializedDataVersion = 1;
        this.columns = i2 >= 0 ? i2 : 0;
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (TextField.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    @Override // java.awt.TextComponent, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                this.peer = getToolkit().createTextField(this);
            }
            super.addNotify();
        }
    }

    public char getEchoChar() {
        return this.echoChar;
    }

    public void setEchoChar(char c2) {
        setEchoCharacter(c2);
    }

    @Deprecated
    public synchronized void setEchoCharacter(char c2) {
        if (this.echoChar != c2) {
            this.echoChar = c2;
            TextFieldPeer textFieldPeer = (TextFieldPeer) this.peer;
            if (textFieldPeer != null) {
                textFieldPeer.setEchoChar(c2);
            }
        }
    }

    @Override // java.awt.TextComponent
    public void setText(String str) {
        super.setText(str);
        invalidateIfValid();
    }

    public boolean echoCharIsSet() {
        return this.echoChar != 0;
    }

    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int i2) {
        int i3;
        synchronized (this) {
            i3 = this.columns;
            if (i2 < 0) {
                throw new IllegalArgumentException("columns less than zero.");
            }
            if (i2 != i3) {
                this.columns = i2;
            }
        }
        if (i2 != i3) {
            invalidate();
        }
    }

    public Dimension getPreferredSize(int i2) {
        return preferredSize(i2);
    }

    @Deprecated
    public Dimension preferredSize(int i2) {
        Dimension dimensionPreferredSize;
        synchronized (getTreeLock()) {
            TextFieldPeer textFieldPeer = (TextFieldPeer) this.peer;
            if (textFieldPeer != null) {
                dimensionPreferredSize = textFieldPeer.getPreferredSize(i2);
            } else {
                dimensionPreferredSize = super.preferredSize();
            }
        }
        return dimensionPreferredSize;
    }

    @Override // java.awt.Component
    public Dimension getPreferredSize() {
        return preferredSize();
    }

    @Override // java.awt.Component
    @Deprecated
    public Dimension preferredSize() {
        Dimension dimensionPreferredSize;
        synchronized (getTreeLock()) {
            if (this.columns > 0) {
                dimensionPreferredSize = preferredSize(this.columns);
            } else {
                dimensionPreferredSize = super.preferredSize();
            }
        }
        return dimensionPreferredSize;
    }

    public Dimension getMinimumSize(int i2) {
        return minimumSize(i2);
    }

    @Deprecated
    public Dimension minimumSize(int i2) {
        Dimension dimensionMinimumSize;
        synchronized (getTreeLock()) {
            TextFieldPeer textFieldPeer = (TextFieldPeer) this.peer;
            if (textFieldPeer != null) {
                dimensionMinimumSize = textFieldPeer.getMinimumSize(i2);
            } else {
                dimensionMinimumSize = super.minimumSize();
            }
        }
        return dimensionMinimumSize;
    }

    @Override // java.awt.Component
    public Dimension getMinimumSize() {
        return minimumSize();
    }

    @Override // java.awt.Component
    @Deprecated
    public Dimension minimumSize() {
        Dimension dimensionMinimumSize;
        synchronized (getTreeLock()) {
            if (this.columns > 0) {
                dimensionMinimumSize = minimumSize(this.columns);
            } else {
                dimensionMinimumSize = super.minimumSize();
            }
        }
        return dimensionMinimumSize;
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

    @Override // java.awt.TextComponent, java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        if (cls != ActionListener.class) {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(this.actionListener, cls);
    }

    @Override // java.awt.TextComponent, java.awt.Component
    boolean eventEnabled(AWTEvent aWTEvent) {
        if (aWTEvent.id != 1001) {
            return super.eventEnabled(aWTEvent);
        }
        if ((this.eventMask & 128) != 0 || this.actionListener != null) {
            return true;
        }
        return false;
    }

    @Override // java.awt.TextComponent, java.awt.Component
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

    @Override // java.awt.TextComponent, java.awt.Component
    protected String paramString() {
        String strParamString = super.paramString();
        if (this.echoChar != 0) {
            strParamString = strParamString + ",echo=" + this.echoChar;
        }
        return strParamString;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, "actionL", this.actionListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, HeadlessException {
        objectInputStream.defaultReadObject();
        if (this.columns < 0) {
            this.columns = 0;
        }
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

    @Override // java.awt.TextComponent, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTTextField();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/TextField$AccessibleAWTTextField.class */
    protected class AccessibleAWTTextField extends TextComponent.AccessibleAWTTextComponent {
        private static final long serialVersionUID = 6219164359235943158L;

        protected AccessibleAWTTextField() {
            super();
        }

        @Override // java.awt.TextComponent.AccessibleAWTTextComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            accessibleStateSet.add(AccessibleState.SINGLE_LINE);
            return accessibleStateSet;
        }
    }
}
