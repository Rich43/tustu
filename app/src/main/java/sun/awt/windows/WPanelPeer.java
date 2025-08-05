package sun.awt.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.peer.PanelPeer;
import sun.awt.SunGraphicsCallback;

/* loaded from: rt.jar:sun/awt/windows/WPanelPeer.class */
class WPanelPeer extends WCanvasPeer implements PanelPeer {
    Insets insets_;

    private static native void initIDs();

    @Override // sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void paint(Graphics graphics) {
        super.paint(graphics);
        SunGraphicsCallback.PaintHeavyweightComponentsCallback.getInstance().runComponents(((Container) this.target).getComponents(), graphics, 3);
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void print(Graphics graphics) {
        super.print(graphics);
        SunGraphicsCallback.PrintHeavyweightComponentsCallback.getInstance().runComponents(((Container) this.target).getComponents(), graphics, 3);
    }

    @Override // java.awt.peer.ContainerPeer
    public Insets getInsets() {
        return this.insets_;
    }

    static {
        initIDs();
    }

    WPanelPeer(Component component) {
        super(component);
    }

    @Override // sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void initialize() {
        super.initialize();
        this.insets_ = new Insets(0, 0, 0, 0);
        if (((Component) this.target).getBackground() == null) {
            Color defaultColor = WColor.getDefaultColor(1);
            ((Component) this.target).setBackground(defaultColor);
            setBackground(defaultColor);
        }
        if (((Component) this.target).getForeground() == null) {
            Color defaultColor2 = WColor.getDefaultColor(2);
            ((Component) this.target).setForeground(defaultColor2);
            setForeground(defaultColor2);
        }
    }

    public Insets insets() {
        return getInsets();
    }
}
