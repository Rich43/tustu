package sun.awt.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.peer.CanvasPeer;
import sun.awt.Graphics2Delegate;
import sun.awt.PaintEventDispatcher;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:sun/awt/windows/WCanvasPeer.class */
class WCanvasPeer extends WComponentPeer implements CanvasPeer {
    private boolean eraseBackground;

    @Override // sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    private native void setNativeBackgroundErase(boolean z2, boolean z3);

    WCanvasPeer(Component component) {
        super(component);
    }

    @Override // sun.awt.windows.WComponentPeer
    void initialize() {
        this.eraseBackground = !SunToolkit.getSunAwtNoerasebackground();
        boolean sunAwtErasebackgroundonresize = SunToolkit.getSunAwtErasebackgroundonresize();
        if (!PaintEventDispatcher.getPaintEventDispatcher().shouldDoNativeBackgroundErase((Component) this.target)) {
            this.eraseBackground = false;
        }
        setNativeBackgroundErase(this.eraseBackground, sunAwtErasebackgroundonresize);
        super.initialize();
        Color background = ((Component) this.target).getBackground();
        if (background != null) {
            setBackground(background);
        }
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void paint(Graphics graphics) {
        Dimension size = ((Component) this.target).getSize();
        if ((graphics instanceof Graphics2D) || (graphics instanceof Graphics2Delegate)) {
            graphics.clearRect(0, 0, size.width, size.height);
        } else {
            graphics.setColor(((Component) this.target).getBackground());
            graphics.fillRect(0, 0, size.width, size.height);
            graphics.setColor(((Component) this.target).getForeground());
        }
        super.paint(graphics);
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean shouldClearRectBeforePaint() {
        return this.eraseBackground;
    }

    void disableBackgroundErase() {
        this.eraseBackground = false;
        setNativeBackgroundErase(false, false);
    }

    @Override // java.awt.peer.CanvasPeer
    public GraphicsConfiguration getAppropriateGraphicsConfiguration(GraphicsConfiguration graphicsConfiguration) {
        return graphicsConfiguration;
    }
}
