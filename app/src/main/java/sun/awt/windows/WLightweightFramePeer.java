package sun.awt.windows;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.MenuBar;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentEvent;
import sun.awt.LightweightFrame;
import sun.awt.OverrideNativeWindowHandle;
import sun.swing.JLightweightFrame;
import sun.swing.SwingAccessor;

/* loaded from: rt.jar:sun/awt/windows/WLightweightFramePeer.class */
public class WLightweightFramePeer extends WFramePeer implements OverrideNativeWindowHandle {
    private native void overrideNativeHandle(long j2);

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public /* bridge */ /* synthetic */ void emulateActivation(boolean z2) {
        super.emulateActivation(z2);
    }

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public /* bridge */ /* synthetic */ Rectangle getBoundsPrivate() {
        return super.getBoundsPrivate();
    }

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public /* bridge */ /* synthetic */ void setBoundsPrivate(int i2, int i3, int i4, int i5) {
        super.setBoundsPrivate(i2, i3, i4, i5);
    }

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public /* bridge */ /* synthetic */ void setMenuBar(MenuBar menuBar) {
        super.setMenuBar(menuBar);
    }

    @Override // sun.awt.windows.WFramePeer, sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public /* bridge */ /* synthetic */ Dimension getMinimumSize() {
        return super.getMinimumSize();
    }

    @Override // sun.awt.windows.WFramePeer, sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public /* bridge */ /* synthetic */ boolean updateGraphicsData(GraphicsConfiguration graphicsConfiguration) {
        return super.updateGraphicsData(graphicsConfiguration);
    }

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public /* bridge */ /* synthetic */ void setMaximizedBounds(Rectangle rectangle) {
        super.setMaximizedBounds(rectangle);
    }

    @Override // sun.awt.windows.WFramePeer
    public /* bridge */ /* synthetic */ int getExtendedState() {
        return super.getExtendedState();
    }

    @Override // sun.awt.windows.WFramePeer
    public /* bridge */ /* synthetic */ void setExtendedState(int i2) {
        super.setExtendedState(i2);
    }

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public /* bridge */ /* synthetic */ int getState() {
        return super.getState();
    }

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public /* bridge */ /* synthetic */ void setState(int i2) {
        super.setState(i2);
    }

    public WLightweightFramePeer(LightweightFrame lightweightFrame) {
        super(lightweightFrame);
    }

    private LightweightFrame getLwTarget() {
        return (LightweightFrame) this.target;
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Graphics getGraphics() {
        return getLwTarget().getGraphics();
    }

    @Override // sun.awt.OverrideNativeWindowHandle
    public void overrideWindowHandle(long j2) {
        overrideNativeHandle(j2);
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer
    public void show() {
        super.show();
        postEvent(new ComponentEvent((Component) getTarget(), 102));
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer
    public void hide() {
        super.hide();
        postEvent(new ComponentEvent((Component) getTarget(), 103));
    }

    @Override // sun.awt.windows.WFramePeer, sun.awt.windows.WComponentPeer
    public void reshape(int i2, int i3, int i4, int i5) {
        super.reshape(i2, i3, i4, i5);
        postEvent(new ComponentEvent((Component) getTarget(), 100));
        postEvent(new ComponentEvent((Component) getTarget(), 101));
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void handleEvent(AWTEvent aWTEvent) {
        if (aWTEvent.getID() == 501) {
            emulateActivation(true);
        }
        super.handleEvent(aWTEvent);
    }

    @Override // sun.awt.windows.WWindowPeer
    public void grab() {
        getLwTarget().grabFocus();
    }

    @Override // sun.awt.windows.WWindowPeer
    public void ungrab() {
        getLwTarget().ungrabFocus();
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void updateCursorImmediately() {
        SwingAccessor.getJLightweightFrameAccessor().updateCursor((JLightweightFrame) getLwTarget());
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean isLightweightFramePeer() {
        return true;
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.dnd.peer.DropTargetPeer
    public void addDropTarget(DropTarget dropTarget) {
        getLwTarget().addDropTarget(dropTarget);
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.dnd.peer.DropTargetPeer
    public void removeDropTarget(DropTarget dropTarget) {
        getLwTarget().removeDropTarget(dropTarget);
    }
}
