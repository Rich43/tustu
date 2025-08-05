package sun.awt.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Event;
import java.awt.Font;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;
import java.awt.peer.ComponentPeer;
import java.awt.peer.DialogPeer;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import sun.awt.AWTAccessor;
import sun.awt.CausedFocusEvent;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/awt/windows/WPrintDialogPeer.class */
class WPrintDialogPeer extends WWindowPeer implements DialogPeer {
    private WComponentPeer parent;
    private Vector<WWindowPeer> blockedWindows;

    /* JADX INFO: Access modifiers changed from: private */
    public native boolean _show();

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public native void toFront();

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public native void toBack();

    private static native void initIDs();

    static {
        initIDs();
    }

    WPrintDialogPeer(WPrintDialog wPrintDialog) {
        super(wPrintDialog);
        this.blockedWindows = new Vector<>();
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void create(WComponentPeer wComponentPeer) {
        this.parent = wComponentPeer;
    }

    @Override // sun.awt.windows.WComponentPeer
    protected void checkCreation() {
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer, sun.awt.windows.WObjectPeer
    protected void disposeImpl() {
        WToolkit.targetDisposedPeer(this.target, this);
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer
    public void show() {
        new Thread(new Runnable() { // from class: sun.awt.windows.WPrintDialogPeer.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ((WPrintDialog) WPrintDialogPeer.this.target).setRetVal(WPrintDialogPeer.this._show());
                } catch (Exception e2) {
                }
                ((WPrintDialog) WPrintDialogPeer.this.target).setVisible(false);
            }
        }).start();
    }

    synchronized void setHWnd(long j2) {
        this.hwnd = j2;
        Iterator<WWindowPeer> it = this.blockedWindows.iterator();
        while (it.hasNext()) {
            WWindowPeer next = it.next();
            if (j2 != 0) {
                next.modalDisable((Dialog) this.target, j2);
            } else {
                next.modalEnable((Dialog) this.target);
            }
        }
    }

    synchronized void blockWindow(WWindowPeer wWindowPeer) {
        this.blockedWindows.add(wWindowPeer);
        if (this.hwnd != 0) {
            wWindowPeer.modalDisable((Dialog) this.target, this.hwnd);
        }
    }

    synchronized void unblockWindow(WWindowPeer wWindowPeer) {
        this.blockedWindows.remove(wWindowPeer);
        if (this.hwnd != 0) {
            wWindowPeer.modalEnable((Dialog) this.target);
        }
    }

    @Override // java.awt.peer.DialogPeer
    public void blockWindows(List<Window> list) {
        Iterator<Window> it = list.iterator();
        while (it.hasNext()) {
            WWindowPeer wWindowPeer = (WWindowPeer) AWTAccessor.getComponentAccessor().getPeer(it.next());
            if (wWindowPeer != null) {
                blockWindow(wWindowPeer);
            }
        }
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WPanelPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void initialize() {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void updateAlwaysOnTopState() {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.DialogPeer
    public void setResizable(boolean z2) {
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer
    void hide() {
    }

    @Override // sun.awt.windows.WComponentPeer
    void enable() {
    }

    @Override // sun.awt.windows.WComponentPeer
    void disable() {
    }

    @Override // sun.awt.windows.WComponentPeer
    public void reshape(int i2, int i3, int i4, int i5) {
    }

    public boolean handleEvent(Event event) {
        return false;
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setForeground(Color color) {
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setBackground(Color color) {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setFont(Font font) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void updateMinimumSize() {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void updateIconImages() {
    }

    public boolean requestFocus(boolean z2, boolean z3) {
        return false;
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public boolean requestFocus(Component component, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause) {
        return false;
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void updateFocusableWindowState() {
    }

    @Override // sun.awt.windows.WComponentPeer
    void start() {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ContainerPeer
    public void beginValidate() {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ContainerPeer
    public void endValidate() {
    }

    void invalidate(int i2, int i3, int i4, int i5) {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.dnd.peer.DropTargetPeer
    public void addDropTarget(DropTarget dropTarget) {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.dnd.peer.DropTargetPeer
    public void removeDropTarget(DropTarget dropTarget) {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setZOrder(ComponentPeer componentPeer) {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void applyShape(Region region) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void setOpacity(float f2) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void setOpaque(boolean z2) {
    }

    public void updateWindow(BufferedImage bufferedImage) {
    }

    @Override // sun.awt.windows.WComponentPeer
    public void createScreenSurface(boolean z2) {
    }

    @Override // sun.awt.windows.WComponentPeer
    public void replaceSurfaceData() {
    }
}
