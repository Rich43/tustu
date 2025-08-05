package sun.awt.windows;

import java.awt.Adjustable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.ScrollPaneAdjustable;
import java.awt.peer.ScrollPanePeer;
import sun.awt.AWTAccessor;
import sun.awt.PeerEvent;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/windows/WScrollPanePeer.class */
final class WScrollPanePeer extends WPanelPeer implements ScrollPanePeer {
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.windows.WScrollPanePeer");
    int scrollbarWidth;
    int scrollbarHeight;
    int prevx;
    int prevy;

    static native void initIDs();

    @Override // sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    native int getOffset(int i2);

    private native void setInsets();

    @Override // java.awt.peer.ScrollPanePeer
    public native synchronized void setScrollPosition(int i2, int i3);

    private native int _getHScrollbarHeight();

    private native int _getVScrollbarWidth();

    native synchronized void setSpans(int i2, int i3, int i4, int i5);

    static {
        initIDs();
    }

    WScrollPanePeer(Component component) {
        super(component);
        this.scrollbarWidth = _getVScrollbarWidth();
        this.scrollbarHeight = _getHScrollbarHeight();
    }

    @Override // sun.awt.windows.WPanelPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void initialize() {
        super.initialize();
        setInsets();
        Insets insets = getInsets();
        setScrollPosition(-insets.left, -insets.top);
    }

    @Override // java.awt.peer.ScrollPanePeer
    public void setUnitIncrement(Adjustable adjustable, int i2) {
    }

    @Override // sun.awt.windows.WPanelPeer
    public Insets insets() {
        return getInsets();
    }

    @Override // java.awt.peer.ScrollPanePeer
    public int getHScrollbarHeight() {
        return this.scrollbarHeight;
    }

    @Override // java.awt.peer.ScrollPanePeer
    public int getVScrollbarWidth() {
        return this.scrollbarWidth;
    }

    public Point getScrollOffset() {
        return new Point(getOffset(0), getOffset(1));
    }

    @Override // java.awt.peer.ScrollPanePeer
    public void childResized(int i2, int i3) {
        Dimension size = ((ScrollPane) this.target).getSize();
        setSpans(size.width, size.height, i2, i3);
        setInsets();
    }

    @Override // java.awt.peer.ScrollPanePeer
    public void setValue(Adjustable adjustable, int i2) {
        Component scrollChild = getScrollChild();
        if (scrollChild == null) {
        }
        Point location = scrollChild.getLocation();
        switch (adjustable.getOrientation()) {
            case 0:
                setScrollPosition(i2, -location.f12371y);
                break;
            case 1:
                setScrollPosition(-location.f12370x, i2);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Component getScrollChild() {
        Component component = null;
        try {
            component = ((ScrollPane) this.target).getComponent(0);
        } catch (ArrayIndexOutOfBoundsException e2) {
        }
        return component;
    }

    private void postScrollEvent(int i2, int i3, int i4, boolean z2) {
        WToolkit.executeOnEventHandlerThread(new ScrollEvent(this.target, new Adjustor(i2, i3, i4, z2)));
    }

    /* loaded from: rt.jar:sun/awt/windows/WScrollPanePeer$ScrollEvent.class */
    class ScrollEvent extends PeerEvent {
        ScrollEvent(Object obj, Runnable runnable) {
            super(obj, runnable, 0L);
        }

        @Override // sun.awt.PeerEvent
        public PeerEvent coalesceEvents(PeerEvent peerEvent) {
            if (WScrollPanePeer.log.isLoggable(PlatformLogger.Level.FINEST)) {
                WScrollPanePeer.log.finest("ScrollEvent coalesced: " + ((Object) peerEvent));
            }
            if (peerEvent instanceof ScrollEvent) {
                return peerEvent;
            }
            return null;
        }
    }

    /* loaded from: rt.jar:sun/awt/windows/WScrollPanePeer$Adjustor.class */
    class Adjustor implements Runnable {
        int orient;
        int type;
        int pos;
        boolean isAdjusting;

        Adjustor(int i2, int i3, int i4, boolean z2) {
            this.orient = i2;
            this.type = i3;
            this.pos = i4;
            this.isAdjusting = z2;
        }

        @Override // java.lang.Runnable
        public void run() {
            int blockIncrement;
            Component component;
            if (WScrollPanePeer.this.getScrollChild() == null) {
                return;
            }
            ScrollPane scrollPane = (ScrollPane) WScrollPanePeer.this.target;
            ScrollPaneAdjustable scrollPaneAdjustable = null;
            if (this.orient == 1) {
                scrollPaneAdjustable = (ScrollPaneAdjustable) scrollPane.getVAdjustable();
            } else if (this.orient != 0) {
                if (WScrollPanePeer.log.isLoggable(PlatformLogger.Level.FINE)) {
                    WScrollPanePeer.log.fine("Assertion failed: unknown orient");
                }
            } else {
                scrollPaneAdjustable = (ScrollPaneAdjustable) scrollPane.getHAdjustable();
            }
            if (scrollPaneAdjustable == null) {
                return;
            }
            int value = scrollPaneAdjustable.getValue();
            switch (this.type) {
                case 1:
                    blockIncrement = value + scrollPaneAdjustable.getUnitIncrement();
                    break;
                case 2:
                    blockIncrement = value - scrollPaneAdjustable.getUnitIncrement();
                    break;
                case 3:
                    blockIncrement = value - scrollPaneAdjustable.getBlockIncrement();
                    break;
                case 4:
                    blockIncrement = value + scrollPaneAdjustable.getBlockIncrement();
                    break;
                case 5:
                    blockIncrement = this.pos;
                    break;
                default:
                    if (WScrollPanePeer.log.isLoggable(PlatformLogger.Level.FINE)) {
                        WScrollPanePeer.log.fine("Assertion failed: unknown type");
                        return;
                    }
                    return;
            }
            int iMin = Math.min(scrollPaneAdjustable.getMaximum(), Math.max(scrollPaneAdjustable.getMinimum(), blockIncrement));
            scrollPaneAdjustable.setValueIsAdjusting(this.isAdjusting);
            AWTAccessor.getScrollPaneAdjustableAccessor().setTypedValue(scrollPaneAdjustable, iMin, this.type);
            Component scrollChild = WScrollPanePeer.this.getScrollChild();
            while (true) {
                component = scrollChild;
                if (component != null && !(component.getPeer() instanceof WComponentPeer)) {
                    scrollChild = component.getParent();
                }
            }
            if (WScrollPanePeer.log.isLoggable(PlatformLogger.Level.FINE) && component == null) {
                WScrollPanePeer.log.fine("Assertion (hwAncestor != null) failed, couldn't find heavyweight ancestor of scroll pane child");
            }
            ((WComponentPeer) component.getPeer()).paintDamagedAreaImmediately();
        }
    }
}
