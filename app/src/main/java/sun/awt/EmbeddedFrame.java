package sun.awt;

import java.applet.Applet;
import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.MenuBar;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.peer.ComponentPeer;
import java.awt.peer.FramePeer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/* loaded from: rt.jar:sun/awt/EmbeddedFrame.class */
public abstract class EmbeddedFrame extends Frame implements KeyEventDispatcher, PropertyChangeListener {
    private boolean isCursorAllowed;
    private boolean supportsXEmbed;
    private KeyboardFocusManager appletKFM;
    private static final long serialVersionUID = 2967042741780317130L;
    protected static final boolean FORWARD = true;
    protected static final boolean BACKWARD = false;

    public abstract void registerAccelerator(AWTKeyStroke aWTKeyStroke);

    public abstract void unregisterAccelerator(AWTKeyStroke aWTKeyStroke);

    public boolean supportsXEmbed() {
        return this.supportsXEmbed && SunToolkit.needsXEmbed();
    }

    protected EmbeddedFrame(boolean z2) {
        this(0L, z2);
    }

    protected EmbeddedFrame() {
        this(0L);
    }

    @Deprecated
    protected EmbeddedFrame(int i2) {
        this(i2);
    }

    protected EmbeddedFrame(long j2) {
        this(j2, false);
    }

    protected EmbeddedFrame(long j2, boolean z2) {
        this.isCursorAllowed = true;
        this.supportsXEmbed = false;
        this.supportsXEmbed = z2;
        registerListeners();
    }

    @Override // java.awt.Component
    public Container getParent() {
        return null;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (!propertyChangeEvent.getPropertyName().equals("managingFocus") || propertyChangeEvent.getNewValue() == Boolean.TRUE) {
            return;
        }
        removeTraversingOutListeners((KeyboardFocusManager) propertyChangeEvent.getSource());
        this.appletKFM = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        if (isVisible()) {
            addTraversingOutListeners(this.appletKFM);
        }
    }

    private void addTraversingOutListeners(KeyboardFocusManager keyboardFocusManager) {
        keyboardFocusManager.addKeyEventDispatcher(this);
        keyboardFocusManager.addPropertyChangeListener("managingFocus", this);
    }

    private void removeTraversingOutListeners(KeyboardFocusManager keyboardFocusManager) {
        keyboardFocusManager.removeKeyEventDispatcher(this);
        keyboardFocusManager.removePropertyChangeListener("managingFocus", this);
    }

    public void registerListeners() {
        if (this.appletKFM != null) {
            removeTraversingOutListeners(this.appletKFM);
        }
        this.appletKFM = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        if (isVisible()) {
            addTraversingOutListeners(this.appletKFM);
        }
    }

    @Override // java.awt.Window, java.awt.Component
    public void show() {
        if (this.appletKFM != null) {
            addTraversingOutListeners(this.appletKFM);
        }
        super.show();
    }

    @Override // java.awt.Window, java.awt.Component
    public void hide() {
        if (this.appletKFM != null) {
            removeTraversingOutListeners(this.appletKFM);
        }
        super.hide();
    }

    @Override // java.awt.KeyEventDispatcher
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        Component lastComponent;
        if (this != AWTAccessor.getKeyboardFocusManagerAccessor().getCurrentFocusCycleRoot() || keyEvent.getID() == 400 || !getFocusTraversalKeysEnabled() || keyEvent.isConsumed()) {
            return false;
        }
        AWTKeyStroke aWTKeyStrokeForEvent = AWTKeyStroke.getAWTKeyStrokeForEvent(keyEvent);
        Component component = keyEvent.getComponent();
        if (getFocusTraversalKeys(0).contains(aWTKeyStrokeForEvent) && ((component == (lastComponent = getFocusTraversalPolicy().getLastComponent(this)) || lastComponent == null) && traverseOut(true))) {
            keyEvent.consume();
            return true;
        }
        if (getFocusTraversalKeys(1).contains(aWTKeyStrokeForEvent)) {
            Component firstComponent = getFocusTraversalPolicy().getFirstComponent(this);
            if ((component == firstComponent || firstComponent == null) && traverseOut(false)) {
                keyEvent.consume();
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean traverseIn(boolean z2) {
        Component lastComponent;
        if (z2) {
            lastComponent = getFocusTraversalPolicy().getFirstComponent(this);
        } else {
            lastComponent = getFocusTraversalPolicy().getLastComponent(this);
        }
        if (lastComponent != null) {
            AWTAccessor.getKeyboardFocusManagerAccessor().setMostRecentFocusOwner(this, lastComponent);
            synthesizeWindowActivation(true);
        }
        return null != lastComponent;
    }

    protected boolean traverseOut(boolean z2) {
        return false;
    }

    @Override // java.awt.Frame
    public void setTitle(String str) {
    }

    @Override // java.awt.Frame, java.awt.Window
    public void setIconImage(Image image) {
    }

    @Override // java.awt.Window
    public void setIconImages(List<? extends Image> list) {
    }

    @Override // java.awt.Frame
    public void setMenuBar(MenuBar menuBar) {
    }

    @Override // java.awt.Frame
    public void setResizable(boolean z2) {
    }

    @Override // java.awt.Frame, java.awt.Component, java.awt.MenuContainer
    public void remove(MenuComponent menuComponent) {
    }

    @Override // java.awt.Frame
    public boolean isResizable() {
        return true;
    }

    @Override // java.awt.Frame, java.awt.Window, java.awt.Container, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (getPeer() == null) {
                setPeer(new NullEmbeddedFramePeer());
            }
            super.addNotify();
        }
    }

    public void setCursorAllowed(boolean z2) {
        this.isCursorAllowed = z2;
        getPeer().updateCursorImmediately();
    }

    public boolean isCursorAllowed() {
        return this.isCursorAllowed;
    }

    @Override // java.awt.Component
    public Cursor getCursor() {
        if (this.isCursorAllowed) {
            return super.getCursor();
        }
        return Cursor.getPredefinedCursor(0);
    }

    protected void setPeer(ComponentPeer componentPeer) {
        AWTAccessor.getComponentAccessor().setPeer(this, componentPeer);
    }

    public void synthesizeWindowActivation(boolean z2) {
    }

    protected void setLocationPrivate(int i2, int i3) {
        Dimension size = getSize();
        setBoundsPrivate(i2, i3, size.width, size.height);
    }

    protected Point getLocationPrivate() {
        Rectangle boundsPrivate = getBoundsPrivate();
        return new Point(boundsPrivate.f12372x, boundsPrivate.f12373y);
    }

    protected void setBoundsPrivate(int i2, int i3, int i4, int i5) {
        FramePeer framePeer = (FramePeer) getPeer();
        if (framePeer != null) {
            framePeer.setBoundsPrivate(i2, i3, i4, i5);
        }
    }

    protected Rectangle getBoundsPrivate() {
        FramePeer framePeer = (FramePeer) getPeer();
        if (framePeer != null) {
            return framePeer.getBoundsPrivate();
        }
        return getBounds();
    }

    @Override // java.awt.Window
    public void toFront() {
    }

    @Override // java.awt.Window
    public void toBack() {
    }

    public static Applet getAppletIfAncestorOf(Component component) {
        Container parent = component.getParent();
        Applet applet = null;
        while (parent != null && !(parent instanceof EmbeddedFrame)) {
            if (parent instanceof Applet) {
                applet = (Applet) parent;
            }
            parent = parent.getParent();
        }
        if (parent == null) {
            return null;
        }
        return applet;
    }

    public void notifyModalBlocked(Dialog dialog, boolean z2) {
    }

    /* loaded from: rt.jar:sun/awt/EmbeddedFrame$NullEmbeddedFramePeer.class */
    private static class NullEmbeddedFramePeer extends NullComponentPeer implements FramePeer {
        private NullEmbeddedFramePeer() {
        }

        @Override // java.awt.peer.FramePeer
        public void setTitle(String str) {
        }

        public void setIconImage(Image image) {
        }

        @Override // java.awt.peer.WindowPeer
        public void updateIconImages() {
        }

        @Override // java.awt.peer.FramePeer
        public void setMenuBar(MenuBar menuBar) {
        }

        @Override // java.awt.peer.FramePeer
        public void setResizable(boolean z2) {
        }

        @Override // java.awt.peer.FramePeer
        public void setState(int i2) {
        }

        @Override // java.awt.peer.FramePeer
        public int getState() {
            return 0;
        }

        @Override // java.awt.peer.FramePeer
        public void setMaximizedBounds(Rectangle rectangle) {
        }

        @Override // java.awt.peer.WindowPeer
        public void toFront() {
        }

        @Override // java.awt.peer.WindowPeer
        public void toBack() {
        }

        @Override // java.awt.peer.WindowPeer
        public void updateFocusableWindowState() {
        }

        public void updateAlwaysOnTop() {
        }

        @Override // java.awt.peer.WindowPeer
        public void updateAlwaysOnTopState() {
        }

        public Component getGlobalHeavyweightFocusOwner() {
            return null;
        }

        @Override // java.awt.peer.FramePeer
        public void setBoundsPrivate(int i2, int i3, int i4, int i5) {
            setBounds(i2, i3, i4, i5, 3);
        }

        @Override // java.awt.peer.FramePeer
        public Rectangle getBoundsPrivate() {
            return getBounds();
        }

        @Override // java.awt.peer.WindowPeer
        public void setModalBlocked(Dialog dialog, boolean z2) {
        }

        public void restack() {
            throw new UnsupportedOperationException();
        }

        public boolean isRestackSupported() {
            return false;
        }

        public boolean requestWindowFocus() {
            return false;
        }

        @Override // java.awt.peer.WindowPeer
        public void updateMinimumSize() {
        }

        @Override // java.awt.peer.WindowPeer
        public void setOpacity(float f2) {
        }

        @Override // java.awt.peer.WindowPeer
        public void setOpaque(boolean z2) {
        }

        @Override // java.awt.peer.WindowPeer
        public void updateWindow() {
        }

        @Override // java.awt.peer.WindowPeer
        public void repositionSecurityWarning() {
        }

        @Override // java.awt.peer.FramePeer
        public void emulateActivation(boolean z2) {
        }
    }
}
