package java.awt;

import java.awt.TextComponent;
import java.awt.peer.TextAreaPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: rt.jar:java/awt/TextArea.class */
public class TextArea extends TextComponent {
    int rows;
    int columns;
    private static final String base = "text";
    private static int nameCounter = 0;
    public static final int SCROLLBARS_BOTH = 0;
    public static final int SCROLLBARS_VERTICAL_ONLY = 1;
    public static final int SCROLLBARS_HORIZONTAL_ONLY = 2;
    public static final int SCROLLBARS_NONE = 3;
    private int scrollbarVisibility;
    private static Set<AWTKeyStroke> forwardTraversalKeys;
    private static Set<AWTKeyStroke> backwardTraversalKeys;
    private static final long serialVersionUID = 3692302836626095722L;
    private int textAreaSerializedDataVersion;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        forwardTraversalKeys = KeyboardFocusManager.initFocusTraversalKeysSet("ctrl TAB", new HashSet());
        backwardTraversalKeys = KeyboardFocusManager.initFocusTraversalKeysSet("ctrl shift TAB", new HashSet());
    }

    public TextArea() throws HeadlessException {
        this("", 0, 0, 0);
    }

    public TextArea(String str) throws HeadlessException {
        this(str, 0, 0, 0);
    }

    public TextArea(int i2, int i3) throws HeadlessException {
        this("", i2, i3, 0);
    }

    public TextArea(String str, int i2, int i3) throws HeadlessException {
        this(str, i2, i3, 0);
    }

    public TextArea(String str, int i2, int i3, int i4) throws HeadlessException {
        super(str);
        this.textAreaSerializedDataVersion = 2;
        this.rows = i2 >= 0 ? i2 : 0;
        this.columns = i3 >= 0 ? i3 : 0;
        if (i4 >= 0 && i4 <= 3) {
            this.scrollbarVisibility = i4;
        } else {
            this.scrollbarVisibility = 0;
        }
        setFocusTraversalKeys(0, forwardTraversalKeys);
        setFocusTraversalKeys(1, backwardTraversalKeys);
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (TextArea.class) {
            StringBuilder sbAppend = new StringBuilder().append("text");
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
                this.peer = getToolkit().createTextArea(this);
            }
            super.addNotify();
        }
    }

    public void insert(String str, int i2) {
        insertText(str, i2);
    }

    @Deprecated
    public synchronized void insertText(String str, int i2) {
        TextAreaPeer textAreaPeer = (TextAreaPeer) this.peer;
        if (textAreaPeer != null) {
            textAreaPeer.insert(str, i2);
        }
        this.text = this.text.substring(0, i2) + str + this.text.substring(i2);
    }

    public void append(String str) {
        appendText(str);
    }

    @Deprecated
    public synchronized void appendText(String str) {
        insertText(str, getText().length());
    }

    public void replaceRange(String str, int i2, int i3) {
        replaceText(str, i2, i3);
    }

    @Deprecated
    public synchronized void replaceText(String str, int i2, int i3) {
        TextAreaPeer textAreaPeer = (TextAreaPeer) this.peer;
        if (textAreaPeer != null) {
            textAreaPeer.replaceRange(str, i2, i3);
        }
        this.text = this.text.substring(0, i2) + str + this.text.substring(i3);
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int i2) {
        int i3 = this.rows;
        if (i2 < 0) {
            throw new IllegalArgumentException("rows less than zero.");
        }
        if (i2 != i3) {
            this.rows = i2;
            invalidate();
        }
    }

    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int i2) {
        int i3 = this.columns;
        if (i2 < 0) {
            throw new IllegalArgumentException("columns less than zero.");
        }
        if (i2 != i3) {
            this.columns = i2;
            invalidate();
        }
    }

    public int getScrollbarVisibility() {
        return this.scrollbarVisibility;
    }

    public Dimension getPreferredSize(int i2, int i3) {
        return preferredSize(i2, i3);
    }

    @Deprecated
    public Dimension preferredSize(int i2, int i3) {
        Dimension dimensionPreferredSize;
        synchronized (getTreeLock()) {
            TextAreaPeer textAreaPeer = (TextAreaPeer) this.peer;
            if (textAreaPeer != null) {
                dimensionPreferredSize = textAreaPeer.getPreferredSize(i2, i3);
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
            if (this.rows > 0 && this.columns > 0) {
                dimensionPreferredSize = preferredSize(this.rows, this.columns);
            } else {
                dimensionPreferredSize = super.preferredSize();
            }
        }
        return dimensionPreferredSize;
    }

    public Dimension getMinimumSize(int i2, int i3) {
        return minimumSize(i2, i3);
    }

    @Deprecated
    public Dimension minimumSize(int i2, int i3) {
        Dimension dimensionMinimumSize;
        synchronized (getTreeLock()) {
            TextAreaPeer textAreaPeer = (TextAreaPeer) this.peer;
            if (textAreaPeer != null) {
                dimensionMinimumSize = textAreaPeer.getMinimumSize(i2, i3);
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
            if (this.rows > 0 && this.columns > 0) {
                dimensionMinimumSize = minimumSize(this.rows, this.columns);
            } else {
                dimensionMinimumSize = super.minimumSize();
            }
        }
        return dimensionMinimumSize;
    }

    @Override // java.awt.TextComponent, java.awt.Component
    protected String paramString() {
        String str;
        switch (this.scrollbarVisibility) {
            case 0:
                str = "both";
                break;
            case 1:
                str = "vertical-only";
                break;
            case 2:
                str = "horizontal-only";
                break;
            case 3:
                str = Separation.COLORANT_NONE;
                break;
            default:
                str = "invalid display policy";
                break;
        }
        return super.paramString() + ",rows=" + this.rows + ",columns=" + this.columns + ",scrollbarVisibility=" + str;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, HeadlessException {
        objectInputStream.defaultReadObject();
        if (this.columns < 0) {
            this.columns = 0;
        }
        if (this.rows < 0) {
            this.rows = 0;
        }
        if (this.scrollbarVisibility < 0 || this.scrollbarVisibility > 3) {
            this.scrollbarVisibility = 0;
        }
        if (this.textAreaSerializedDataVersion < 2) {
            setFocusTraversalKeys(0, forwardTraversalKeys);
            setFocusTraversalKeys(1, backwardTraversalKeys);
        }
    }

    @Override // java.awt.TextComponent, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTTextArea();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/TextArea$AccessibleAWTTextArea.class */
    protected class AccessibleAWTTextArea extends TextComponent.AccessibleAWTTextComponent {
        private static final long serialVersionUID = 3472827823632144419L;

        protected AccessibleAWTTextArea() {
            super();
        }

        @Override // java.awt.TextComponent.AccessibleAWTTextComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            accessibleStateSet.add(AccessibleState.MULTI_LINE);
            return accessibleStateSet;
        }
    }
}
