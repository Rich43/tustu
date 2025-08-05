package sun.awt.windows;

import java.awt.Dimension;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.peer.ScrollbarPeer;

/* loaded from: rt.jar:sun/awt/windows/WScrollbarPeer.class */
final class WScrollbarPeer extends WComponentPeer implements ScrollbarPeer {
    private boolean dragInProgress;

    static native int getScrollbarSize(int i2);

    @Override // java.awt.peer.ScrollbarPeer
    public native void setValues(int i2, int i3, int i4, int i5);

    @Override // java.awt.peer.ScrollbarPeer
    public native void setLineIncrement(int i2);

    @Override // java.awt.peer.ScrollbarPeer
    public native void setPageIncrement(int i2);

    @Override // sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        if (((Scrollbar) this.target).getOrientation() == 1) {
            return new Dimension(getScrollbarSize(1), 50);
        }
        return new Dimension(50, getScrollbarSize(0));
    }

    WScrollbarPeer(Scrollbar scrollbar) {
        super(scrollbar);
        this.dragInProgress = false;
    }

    @Override // sun.awt.windows.WComponentPeer
    void initialize() {
        Scrollbar scrollbar = (Scrollbar) this.target;
        setValues(scrollbar.getValue(), scrollbar.getVisibleAmount(), scrollbar.getMinimum(), scrollbar.getMaximum());
        super.initialize();
    }

    private void postAdjustmentEvent(final int i2, final int i3, final boolean z2) {
        final Scrollbar scrollbar = (Scrollbar) this.target;
        WToolkit.executeOnEventHandlerThread(scrollbar, new Runnable() { // from class: sun.awt.windows.WScrollbarPeer.1
            @Override // java.lang.Runnable
            public void run() {
                scrollbar.setValueIsAdjusting(z2);
                scrollbar.setValue(i3);
                WScrollbarPeer.this.postEvent(new AdjustmentEvent(scrollbar, 601, i2, i3, z2));
            }
        });
    }

    void lineUp(int i2) {
        postAdjustmentEvent(2, i2, false);
    }

    void lineDown(int i2) {
        postAdjustmentEvent(1, i2, false);
    }

    void pageUp(int i2) {
        postAdjustmentEvent(3, i2, false);
    }

    void pageDown(int i2) {
        postAdjustmentEvent(4, i2, false);
    }

    void warp(int i2) {
        postAdjustmentEvent(5, i2, false);
    }

    void drag(int i2) {
        if (!this.dragInProgress) {
            this.dragInProgress = true;
        }
        postAdjustmentEvent(5, i2, true);
    }

    void dragEnd(final int i2) {
        final Scrollbar scrollbar = (Scrollbar) this.target;
        if (!this.dragInProgress) {
            return;
        }
        this.dragInProgress = false;
        WToolkit.executeOnEventHandlerThread(scrollbar, new Runnable() { // from class: sun.awt.windows.WScrollbarPeer.2
            @Override // java.lang.Runnable
            public void run() {
                scrollbar.setValueIsAdjusting(false);
                WScrollbarPeer.this.postEvent(new AdjustmentEvent(scrollbar, 601, 5, i2, false));
            }
        });
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean shouldClearRectBeforePaint() {
        return false;
    }
}
