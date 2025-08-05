package java.awt;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.peer.ScrollPanePeer;
import java.io.Serializable;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:java/awt/ScrollPaneAdjustable.class */
public class ScrollPaneAdjustable implements Adjustable, Serializable {
    private ScrollPane sp;
    private int orientation;
    private int value;
    private int minimum;
    private int maximum;
    private int visibleAmount;
    private transient boolean isAdjusting;
    private int unitIncrement = 1;
    private int blockIncrement = 1;
    private AdjustmentListener adjustmentListener;
    private static final String SCROLLPANE_ONLY = "Can be set by scrollpane only";
    private static final long serialVersionUID = -3359745691033257079L;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setScrollPaneAdjustableAccessor(new AWTAccessor.ScrollPaneAdjustableAccessor() { // from class: java.awt.ScrollPaneAdjustable.1
            @Override // sun.awt.AWTAccessor.ScrollPaneAdjustableAccessor
            public void setTypedValue(ScrollPaneAdjustable scrollPaneAdjustable, int i2, int i3) {
                scrollPaneAdjustable.setTypedValue(i2, i3);
            }
        });
    }

    ScrollPaneAdjustable(ScrollPane scrollPane, AdjustmentListener adjustmentListener, int i2) {
        this.sp = scrollPane;
        this.orientation = i2;
        addAdjustmentListener(adjustmentListener);
    }

    void setSpan(int i2, int i3, int i4) {
        this.minimum = i2;
        this.maximum = Math.max(i3, this.minimum + 1);
        this.visibleAmount = Math.min(i4, this.maximum - this.minimum);
        this.visibleAmount = Math.max(this.visibleAmount, 1);
        this.blockIncrement = Math.max((int) (i4 * 0.9d), 1);
        setValue(this.value);
    }

    @Override // java.awt.Adjustable
    public int getOrientation() {
        return this.orientation;
    }

    @Override // java.awt.Adjustable
    public void setMinimum(int i2) {
        throw new AWTError(SCROLLPANE_ONLY);
    }

    @Override // java.awt.Adjustable
    public int getMinimum() {
        return 0;
    }

    @Override // java.awt.Adjustable
    public void setMaximum(int i2) {
        throw new AWTError(SCROLLPANE_ONLY);
    }

    @Override // java.awt.Adjustable
    public int getMaximum() {
        return this.maximum;
    }

    @Override // java.awt.Adjustable
    public synchronized void setUnitIncrement(int i2) {
        if (i2 != this.unitIncrement) {
            this.unitIncrement = i2;
            if (this.sp.peer != null) {
                ((ScrollPanePeer) this.sp.peer).setUnitIncrement(this, i2);
            }
        }
    }

    @Override // java.awt.Adjustable
    public int getUnitIncrement() {
        return this.unitIncrement;
    }

    @Override // java.awt.Adjustable
    public synchronized void setBlockIncrement(int i2) {
        this.blockIncrement = i2;
    }

    @Override // java.awt.Adjustable
    public int getBlockIncrement() {
        return this.blockIncrement;
    }

    @Override // java.awt.Adjustable
    public void setVisibleAmount(int i2) {
        throw new AWTError(SCROLLPANE_ONLY);
    }

    @Override // java.awt.Adjustable
    public int getVisibleAmount() {
        return this.visibleAmount;
    }

    public void setValueIsAdjusting(boolean z2) {
        if (this.isAdjusting != z2) {
            this.isAdjusting = z2;
            this.adjustmentListener.adjustmentValueChanged(new AdjustmentEvent(this, 601, 5, this.value, z2));
        }
    }

    public boolean getValueIsAdjusting() {
        return this.isAdjusting;
    }

    @Override // java.awt.Adjustable
    public void setValue(int i2) {
        setTypedValue(i2, 5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTypedValue(int i2, int i3) {
        int iMin = Math.min(Math.max(i2, this.minimum), this.maximum - this.visibleAmount);
        if (iMin != this.value) {
            this.value = iMin;
            this.adjustmentListener.adjustmentValueChanged(new AdjustmentEvent(this, 601, i3, this.value, this.isAdjusting));
        }
    }

    @Override // java.awt.Adjustable
    public int getValue() {
        return this.value;
    }

    @Override // java.awt.Adjustable
    public synchronized void addAdjustmentListener(AdjustmentListener adjustmentListener) {
        if (adjustmentListener == null) {
            return;
        }
        this.adjustmentListener = AWTEventMulticaster.add(this.adjustmentListener, adjustmentListener);
    }

    @Override // java.awt.Adjustable
    public synchronized void removeAdjustmentListener(AdjustmentListener adjustmentListener) {
        if (adjustmentListener == null) {
            return;
        }
        this.adjustmentListener = AWTEventMulticaster.remove(this.adjustmentListener, adjustmentListener);
    }

    public synchronized AdjustmentListener[] getAdjustmentListeners() {
        return (AdjustmentListener[]) AWTEventMulticaster.getListeners(this.adjustmentListener, AdjustmentListener.class);
    }

    public String toString() {
        return getClass().getName() + "[" + paramString() + "]";
    }

    public String paramString() {
        return (this.orientation == 1 ? "vertical," : "horizontal,") + "[0.." + this.maximum + "],val=" + this.value + ",vis=" + this.visibleAmount + ",unit=" + this.unitIncrement + ",block=" + this.blockIncrement + ",isAdjusting=" + this.isAdjusting;
    }
}
