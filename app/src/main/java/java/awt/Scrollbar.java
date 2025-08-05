package java.awt;

import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.peer.ScrollbarPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleValue;

/* loaded from: rt.jar:java/awt/Scrollbar.class */
public class Scrollbar extends Component implements Adjustable, Accessible {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    int value;
    int maximum;
    int minimum;
    int visibleAmount;
    int orientation;
    int lineIncrement;
    int pageIncrement;
    transient boolean isAdjusting;
    transient AdjustmentListener adjustmentListener;
    private static final String base = "scrollbar";
    private static int nameCounter = 0;
    private static final long serialVersionUID = 8451667562882310543L;
    private int scrollbarSerializedDataVersion;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    public Scrollbar() throws HeadlessException {
        this(1, 0, 10, 0, 100);
    }

    public Scrollbar(int i2) throws HeadlessException {
        this(i2, 0, 10, 0, 100);
    }

    public Scrollbar(int i2, int i3, int i4, int i5, int i6) throws HeadlessException {
        this.lineIncrement = 1;
        this.pageIncrement = 10;
        this.scrollbarSerializedDataVersion = 1;
        GraphicsEnvironment.checkHeadless();
        switch (i2) {
            case 0:
            case 1:
                this.orientation = i2;
                setValues(i3, i4, i5, i6);
                return;
            default:
                throw new IllegalArgumentException("illegal scrollbar orientation");
        }
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Scrollbar.class) {
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
                this.peer = getToolkit().createScrollbar(this);
            }
            super.addNotify();
        }
    }

    @Override // java.awt.Adjustable
    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i2) {
        synchronized (getTreeLock()) {
            if (i2 == this.orientation) {
                return;
            }
            switch (i2) {
                case 0:
                case 1:
                    this.orientation = i2;
                    if (this.peer != null) {
                        removeNotify();
                        addNotify();
                        invalidate();
                    }
                    if (this.accessibleContext != null) {
                        this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, i2 == 1 ? AccessibleState.HORIZONTAL : AccessibleState.VERTICAL, i2 == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL);
                        return;
                    }
                    return;
                default:
                    throw new IllegalArgumentException("illegal scrollbar orientation");
            }
        }
    }

    @Override // java.awt.Adjustable
    public int getValue() {
        return this.value;
    }

    @Override // java.awt.Adjustable
    public void setValue(int i2) {
        setValues(i2, this.visibleAmount, this.minimum, this.maximum);
    }

    @Override // java.awt.Adjustable
    public int getMinimum() {
        return this.minimum;
    }

    @Override // java.awt.Adjustable
    public void setMinimum(int i2) {
        setValues(this.value, this.visibleAmount, i2, this.maximum);
    }

    @Override // java.awt.Adjustable
    public int getMaximum() {
        return this.maximum;
    }

    @Override // java.awt.Adjustable
    public void setMaximum(int i2) {
        if (i2 == Integer.MIN_VALUE) {
            i2 = -2147483647;
        }
        if (this.minimum >= i2) {
            this.minimum = i2 - 1;
        }
        setValues(this.value, this.visibleAmount, this.minimum, i2);
    }

    @Override // java.awt.Adjustable
    public int getVisibleAmount() {
        return getVisible();
    }

    @Deprecated
    public int getVisible() {
        return this.visibleAmount;
    }

    @Override // java.awt.Adjustable
    public void setVisibleAmount(int i2) {
        setValues(this.value, i2, this.minimum, this.maximum);
    }

    @Override // java.awt.Adjustable
    public void setUnitIncrement(int i2) {
        setLineIncrement(i2);
    }

    @Deprecated
    public synchronized void setLineIncrement(int i2) {
        int i3 = i2 < 1 ? 1 : i2;
        if (this.lineIncrement == i3) {
            return;
        }
        this.lineIncrement = i3;
        ScrollbarPeer scrollbarPeer = (ScrollbarPeer) this.peer;
        if (scrollbarPeer != null) {
            scrollbarPeer.setLineIncrement(this.lineIncrement);
        }
    }

    @Override // java.awt.Adjustable
    public int getUnitIncrement() {
        return getLineIncrement();
    }

    @Deprecated
    public int getLineIncrement() {
        return this.lineIncrement;
    }

    @Override // java.awt.Adjustable
    public void setBlockIncrement(int i2) {
        setPageIncrement(i2);
    }

    @Deprecated
    public synchronized void setPageIncrement(int i2) {
        int i3 = i2 < 1 ? 1 : i2;
        if (this.pageIncrement == i3) {
            return;
        }
        this.pageIncrement = i3;
        ScrollbarPeer scrollbarPeer = (ScrollbarPeer) this.peer;
        if (scrollbarPeer != null) {
            scrollbarPeer.setPageIncrement(this.pageIncrement);
        }
    }

    @Override // java.awt.Adjustable
    public int getBlockIncrement() {
        return getPageIncrement();
    }

    @Deprecated
    public int getPageIncrement() {
        return this.pageIncrement;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x002a A[Catch: all -> 0x009a, TryCatch #0 {, blocks: (B:9:0x0014, B:10:0x0019, B:12:0x002a, B:13:0x0036, B:15:0x003d, B:22:0x004f, B:24:0x0057, B:25:0x005c, B:27:0x0085, B:29:0x0096), top: B:42:0x0014 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x003d A[Catch: all -> 0x009a, TryCatch #0 {, blocks: (B:9:0x0014, B:10:0x0019, B:12:0x002a, B:13:0x0036, B:15:0x003d, B:22:0x004f, B:24:0x0057, B:25:0x005c, B:27:0x0085, B:29:0x0096), top: B:42:0x0014 }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0057 A[Catch: all -> 0x009a, TryCatch #0 {, blocks: (B:9:0x0014, B:10:0x0019, B:12:0x002a, B:13:0x0036, B:15:0x003d, B:22:0x004f, B:24:0x0057, B:25:0x005c, B:27:0x0085, B:29:0x0096), top: B:42:0x0014 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0085 A[Catch: all -> 0x009a, TryCatch #0 {, blocks: (B:9:0x0014, B:10:0x0019, B:12:0x002a, B:13:0x0036, B:15:0x003d, B:22:0x004f, B:24:0x0057, B:25:0x005c, B:27:0x0085, B:29:0x0096), top: B:42:0x0014 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setValues(int r7, int r8, int r9, int r10) {
        /*
            Method dump skipped, instructions count: 194
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.Scrollbar.setValues(int, int, int, int):void");
    }

    public boolean getValueIsAdjusting() {
        return this.isAdjusting;
    }

    public void setValueIsAdjusting(boolean z2) {
        boolean z3;
        synchronized (this) {
            z3 = this.isAdjusting;
            this.isAdjusting = z2;
        }
        if (z3 != z2 && this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, z3 ? AccessibleState.BUSY : null, z2 ? AccessibleState.BUSY : null);
        }
    }

    @Override // java.awt.Adjustable
    public synchronized void addAdjustmentListener(AdjustmentListener adjustmentListener) {
        if (adjustmentListener == null) {
            return;
        }
        this.adjustmentListener = AWTEventMulticaster.add(this.adjustmentListener, adjustmentListener);
        this.newEventsOnly = true;
    }

    @Override // java.awt.Adjustable
    public synchronized void removeAdjustmentListener(AdjustmentListener adjustmentListener) {
        if (adjustmentListener == null) {
            return;
        }
        this.adjustmentListener = AWTEventMulticaster.remove(this.adjustmentListener, adjustmentListener);
    }

    public synchronized AdjustmentListener[] getAdjustmentListeners() {
        return (AdjustmentListener[]) getListeners(AdjustmentListener.class);
    }

    @Override // java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        if (cls != AdjustmentListener.class) {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(this.adjustmentListener, cls);
    }

    @Override // java.awt.Component
    boolean eventEnabled(AWTEvent aWTEvent) {
        if (aWTEvent.id != 601) {
            return super.eventEnabled(aWTEvent);
        }
        if ((this.eventMask & 256) != 0 || this.adjustmentListener != null) {
            return true;
        }
        return false;
    }

    @Override // java.awt.Component
    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof AdjustmentEvent) {
            processAdjustmentEvent((AdjustmentEvent) aWTEvent);
        } else {
            super.processEvent(aWTEvent);
        }
    }

    protected void processAdjustmentEvent(AdjustmentEvent adjustmentEvent) {
        AdjustmentListener adjustmentListener = this.adjustmentListener;
        if (adjustmentListener != null) {
            adjustmentListener.adjustmentValueChanged(adjustmentEvent);
        }
    }

    @Override // java.awt.Component
    protected String paramString() {
        return super.paramString() + ",val=" + this.value + ",vis=" + this.visibleAmount + ",min=" + this.minimum + ",max=" + this.maximum + (this.orientation == 1 ? ",vert" : ",horz") + ",isAdjusting=" + this.isAdjusting;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, "adjustmentL", this.adjustmentListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
        objectInputStream.defaultReadObject();
        while (true) {
            Object object = objectInputStream.readObject();
            if (null != object) {
                if ("adjustmentL" == ((String) object).intern()) {
                    addAdjustmentListener((AdjustmentListener) objectInputStream.readObject());
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
            this.accessibleContext = new AccessibleAWTScrollBar();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/Scrollbar$AccessibleAWTScrollBar.class */
    protected class AccessibleAWTScrollBar extends Component.AccessibleAWTComponent implements AccessibleValue {
        private static final long serialVersionUID = -344337268523697807L;

        protected AccessibleAWTScrollBar() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (Scrollbar.this.getValueIsAdjusting()) {
                accessibleStateSet.add(AccessibleState.BUSY);
            }
            if (Scrollbar.this.getOrientation() == 1) {
                accessibleStateSet.add(AccessibleState.VERTICAL);
            } else {
                accessibleStateSet.add(AccessibleState.HORIZONTAL);
            }
            return accessibleStateSet;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SCROLL_BAR;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            return Integer.valueOf(Scrollbar.this.getValue());
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            if (number instanceof Integer) {
                Scrollbar.this.setValue(number.intValue());
                return true;
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return Integer.valueOf(Scrollbar.this.getMinimum());
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            return Integer.valueOf(Scrollbar.this.getMaximum());
        }
    }
}
