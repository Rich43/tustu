package java.awt;

import java.awt.Component;
import java.awt.peer.LabelPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JSplitPane;

/* loaded from: rt.jar:java/awt/Label.class */
public class Label extends Component implements Accessible {
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    String text;
    int alignment;
    private static final String base = "label";
    private static int nameCounter;
    private static final long serialVersionUID = 3094126758329070636L;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        nameCounter = 0;
    }

    public Label() throws HeadlessException {
        this("", 0);
    }

    public Label(String str) throws HeadlessException {
        this(str, 0);
    }

    public Label(String str, int i2) throws HeadlessException {
        this.alignment = 0;
        GraphicsEnvironment.checkHeadless();
        this.text = str;
        setAlignment(i2);
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
        objectInputStream.defaultReadObject();
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Label.class) {
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
                this.peer = getToolkit().createLabel(this);
            }
            super.addNotify();
        }
    }

    public int getAlignment() {
        return this.alignment;
    }

    public synchronized void setAlignment(int i2) {
        switch (i2) {
            case 0:
            case 1:
            case 2:
                this.alignment = i2;
                LabelPeer labelPeer = (LabelPeer) this.peer;
                if (labelPeer != null) {
                    labelPeer.setAlignment(i2);
                    return;
                }
                return;
            default:
                throw new IllegalArgumentException("improper alignment: " + i2);
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        boolean z2 = false;
        synchronized (this) {
            if (str != this.text && (this.text == null || !this.text.equals(str))) {
                this.text = str;
                LabelPeer labelPeer = (LabelPeer) this.peer;
                if (labelPeer != null) {
                    labelPeer.setText(str);
                }
                z2 = true;
            }
        }
        if (z2) {
            invalidateIfValid();
        }
    }

    @Override // java.awt.Component
    protected String paramString() {
        String str = "";
        switch (this.alignment) {
            case 0:
                str = JSplitPane.LEFT;
                break;
            case 1:
                str = "center";
                break;
            case 2:
                str = JSplitPane.RIGHT;
                break;
        }
        return super.paramString() + ",align=" + str + ",text=" + this.text;
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTLabel();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/Label$AccessibleAWTLabel.class */
    protected class AccessibleAWTLabel extends Component.AccessibleAWTComponent {
        private static final long serialVersionUID = -3568967560160480438L;

        public AccessibleAWTLabel() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            if (this.accessibleName != null) {
                return this.accessibleName;
            }
            if (Label.this.getText() == null) {
                return super.getAccessibleName();
            }
            return Label.this.getText();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.LABEL;
        }
    }
}
