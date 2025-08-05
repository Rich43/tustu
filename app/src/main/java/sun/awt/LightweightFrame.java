package sun.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MenuBar;
import java.awt.MenuComponent;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.peer.FramePeer;
import java.util.List;

/* loaded from: rt.jar:sun/awt/LightweightFrame.class */
public abstract class LightweightFrame extends Frame {
    private int hostX;
    private int hostY;
    private int hostW;
    private int hostH;

    public abstract void grabFocus();

    public abstract void ungrabFocus();

    public abstract int getScaleFactor();

    public abstract void notifyDisplayChanged(int i2);

    public abstract <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> cls, DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener);

    public abstract DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException;

    public abstract void addDropTarget(DropTarget dropTarget);

    public abstract void removeDropTarget(DropTarget dropTarget);

    public LightweightFrame() {
        setUndecorated(true);
        setResizable(true);
        setEnabled(true);
    }

    @Override // java.awt.Component
    public final Container getParent() {
        return null;
    }

    @Override // java.awt.Component
    public Graphics getGraphics() {
        return null;
    }

    @Override // java.awt.Frame
    public final boolean isResizable() {
        return true;
    }

    @Override // java.awt.Frame
    public final void setTitle(String str) {
    }

    @Override // java.awt.Frame, java.awt.Window
    public final void setIconImage(Image image) {
    }

    @Override // java.awt.Window
    public final void setIconImages(List<? extends Image> list) {
    }

    @Override // java.awt.Frame
    public final void setMenuBar(MenuBar menuBar) {
    }

    @Override // java.awt.Frame
    public final void setResizable(boolean z2) {
    }

    @Override // java.awt.Frame, java.awt.Component, java.awt.MenuContainer
    public final void remove(MenuComponent menuComponent) {
    }

    @Override // java.awt.Window
    public final void toFront() {
    }

    @Override // java.awt.Window
    public final void toBack() {
    }

    @Override // java.awt.Frame, java.awt.Window, java.awt.Container, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (getPeer() == null) {
                try {
                    setPeer(((SunToolkit) Toolkit.getDefaultToolkit()).createLightweightFrame(this));
                } catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
            super.addNotify();
        }
    }

    private void setPeer(FramePeer framePeer) {
        AWTAccessor.getComponentAccessor().setPeer(this, framePeer);
    }

    public void emulateActivation(boolean z2) {
        ((FramePeer) getPeer()).emulateActivation(z2);
    }

    public Rectangle getHostBounds() {
        if (this.hostX == 0 && this.hostY == 0 && this.hostW == 0 && this.hostH == 0) {
            return getBounds();
        }
        return new Rectangle(this.hostX, this.hostY, this.hostW, this.hostH);
    }

    public void setHostBounds(int i2, int i3, int i4, int i5) {
        this.hostX = i2;
        this.hostY = i3;
        this.hostW = i4;
        this.hostH = i5;
    }
}
