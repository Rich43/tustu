package sun.awt.windows;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.MenuBar;
import java.awt.Rectangle;
import java.awt.peer.FramePeer;
import java.security.AccessController;
import sun.awt.AWTAccessor;
import sun.awt.im.InputMethodManager;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/awt/windows/WFramePeer.class */
class WFramePeer extends WWindowPeer implements FramePeer {
    private static final boolean keepOnMinimize;

    private static native void initIDs();

    public native void setState(int i2);

    public native int getState();

    private native void setMaximizedBounds(int i2, int i3, int i4, int i5);

    private native void clearMaximizedBounds();

    private native void setMenuBar0(WMenuBarPeer wMenuBarPeer);

    native void createAwtFrame(WComponentPeer wComponentPeer);

    private static native int getSysMenuHeight();

    native void pSetIMMOption(String str);

    private native void synthesizeWmActivate(boolean z2);

    static {
        initIDs();
        keepOnMinimize = "true".equals(AccessController.doPrivileged(new GetPropertyAction("sun.awt.keepWorkingSetOnMinimize")));
    }

    public void setExtendedState(int i2) {
        AWTAccessor.getFrameAccessor().setExtendedState((Frame) this.target, i2);
    }

    public int getExtendedState() {
        return AWTAccessor.getFrameAccessor().getExtendedState((Frame) this.target);
    }

    public void setMaximizedBounds(Rectangle rectangle) {
        if (rectangle == null) {
            clearMaximizedBounds();
            return;
        }
        Rectangle rectangle2 = (Rectangle) rectangle.clone();
        adjustMaximizedBounds(rectangle2);
        setMaximizedBounds(rectangle2.f12372x, rectangle2.f12373y, rectangle2.width, rectangle2.height);
    }

    private void adjustMaximizedBounds(Rectangle rectangle) {
        GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
        GraphicsConfiguration defaultConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        if (graphicsConfiguration != null && graphicsConfiguration != defaultConfiguration) {
            Rectangle bounds = graphicsConfiguration.getBounds();
            Rectangle bounds2 = defaultConfiguration.getBounds();
            if (bounds.width - bounds2.width > 0 || bounds.height - bounds2.height > 0) {
                rectangle.width -= bounds.width - bounds2.width;
                rectangle.height -= bounds.height - bounds2.height;
            }
        }
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public boolean updateGraphicsData(GraphicsConfiguration graphicsConfiguration) {
        boolean zUpdateGraphicsData = super.updateGraphicsData(graphicsConfiguration);
        Rectangle maximizedBounds = AWTAccessor.getFrameAccessor().getMaximizedBounds((Frame) this.target);
        if (maximizedBounds != null) {
            setMaximizedBounds(maximizedBounds);
        }
        return zUpdateGraphicsData;
    }

    @Override // sun.awt.windows.WWindowPeer
    boolean isTargetUndecorated() {
        return ((Frame) this.target).isUndecorated();
    }

    @Override // sun.awt.windows.WComponentPeer
    public void reshape(int i2, int i3, int i4, int i5) {
        if (((Frame) this.target).isUndecorated()) {
            super.reshape(i2, i3, i4, i5);
        } else {
            reshapeFrame(i2, i3, i4, i5);
        }
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        Dimension dimension = new Dimension();
        if (!((Frame) this.target).isUndecorated()) {
            dimension.setSize(getSysMinWidth(), getSysMinHeight());
        }
        if (((Frame) this.target).getMenuBar() != null) {
            dimension.height += getSysMenuHeight();
        }
        return dimension;
    }

    public void setMenuBar(MenuBar menuBar) {
        WMenuBarPeer wMenuBarPeer = (WMenuBarPeer) WToolkit.targetToPeer(menuBar);
        if (wMenuBarPeer != null) {
            if (wMenuBarPeer.framePeer != this) {
                menuBar.removeNotify();
                menuBar.addNotify();
                wMenuBarPeer = (WMenuBarPeer) WToolkit.targetToPeer(menuBar);
                if (wMenuBarPeer != null && wMenuBarPeer.framePeer != this) {
                    throw new IllegalStateException("Wrong parent peer");
                }
            }
            if (wMenuBarPeer != null) {
                addChildPeer(wMenuBarPeer);
            }
        }
        setMenuBar0(wMenuBarPeer);
        updateInsets(this.insets_);
    }

    WFramePeer(Frame frame) {
        super(frame);
        String triggerMenuString = InputMethodManager.getInstance().getTriggerMenuString();
        if (triggerMenuString != null) {
            pSetIMMOption(triggerMenuString);
        }
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void create(WComponentPeer wComponentPeer) {
        preCreate(wComponentPeer);
        createAwtFrame(wComponentPeer);
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WPanelPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void initialize() {
        super.initialize();
        Frame frame = (Frame) this.target;
        if (frame.getTitle() != null) {
            setTitle(frame.getTitle());
        }
        setResizable(frame.isResizable());
        setState(frame.getExtendedState());
    }

    void notifyIMMOptionChange() {
        InputMethodManager.getInstance().notifyChangeRequest((Component) this.target);
    }

    public void setBoundsPrivate(int i2, int i3, int i4, int i5) {
        setBounds(i2, i3, i4, i5, 3);
    }

    public Rectangle getBoundsPrivate() {
        return getBounds();
    }

    public void emulateActivation(boolean z2) {
        synthesizeWmActivate(z2);
    }
}
