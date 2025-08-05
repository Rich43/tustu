package sun.awt.windows;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.MenuBar;
import java.awt.Rectangle;
import sun.awt.EmbeddedFrame;
import sun.awt.Win32GraphicsEnvironment;

/* loaded from: rt.jar:sun/awt/windows/WEmbeddedFramePeer.class */
public class WEmbeddedFramePeer extends WFramePeer {
    @Override // sun.awt.windows.WFramePeer, sun.awt.windows.WWindowPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public native Rectangle getBoundsPrivate();

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public /* bridge */ /* synthetic */ void emulateActivation(boolean z2) {
        super.emulateActivation(z2);
    }

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public /* bridge */ /* synthetic */ void setMenuBar(MenuBar menuBar) {
        super.setMenuBar(menuBar);
    }

    @Override // sun.awt.windows.WFramePeer, sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public /* bridge */ /* synthetic */ Dimension getMinimumSize() {
        return super.getMinimumSize();
    }

    @Override // sun.awt.windows.WFramePeer, sun.awt.windows.WComponentPeer
    public /* bridge */ /* synthetic */ void reshape(int i2, int i3, int i4, int i5) {
        super.reshape(i2, i3, i4, i5);
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

    public WEmbeddedFramePeer(EmbeddedFrame embeddedFrame) {
        super(embeddedFrame);
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WPanelPeer, sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void print(Graphics graphics) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void updateMinimumSize() {
    }

    @Override // sun.awt.windows.WWindowPeer
    public void modalDisable(Dialog dialog, long j2) {
        super.modalDisable(dialog, j2);
        ((EmbeddedFrame) this.target).notifyModalBlocked(dialog, true);
    }

    @Override // sun.awt.windows.WWindowPeer
    public void modalEnable(Dialog dialog) {
        super.modalEnable(dialog);
        ((EmbeddedFrame) this.target).notifyModalBlocked(dialog, false);
    }

    @Override // sun.awt.windows.WFramePeer, java.awt.peer.FramePeer
    public void setBoundsPrivate(int i2, int i3, int i4, int i5) {
        setBounds(i2, i3, i4, i5, 16387);
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean isAccelCapable() {
        return !Win32GraphicsEnvironment.isDWMCompositionEnabled();
    }
}
