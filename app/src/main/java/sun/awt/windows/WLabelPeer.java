package sun.awt.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Label;
import java.awt.peer.LabelPeer;

/* loaded from: rt.jar:sun/awt/windows/WLabelPeer.class */
final class WLabelPeer extends WComponentPeer implements LabelPeer {
    native void lazyPaint();

    @Override // java.awt.peer.LabelPeer
    public native void setText(String str);

    @Override // java.awt.peer.LabelPeer
    public native void setAlignment(int i2);

    @Override // sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        FontMetrics fontMetrics = getFontMetrics(((Label) this.target).getFont());
        String text = ((Label) this.target).getText();
        if (text == null) {
            text = "";
        }
        return new Dimension(fontMetrics.stringWidth(text) + 14, fontMetrics.getHeight() + 8);
    }

    @Override // sun.awt.windows.WComponentPeer
    synchronized void start() {
        super.start();
        lazyPaint();
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean shouldClearRectBeforePaint() {
        return false;
    }

    WLabelPeer(Label label) {
        super(label);
    }

    @Override // sun.awt.windows.WComponentPeer
    void initialize() {
        Label label = (Label) this.target;
        String text = label.getText();
        if (text != null) {
            setText(text);
        }
        int alignment = label.getAlignment();
        if (alignment != 0) {
            setAlignment(alignment);
        }
        Color background = ((Component) this.target).getBackground();
        if (background != null) {
            setBackground(background);
        }
        super.initialize();
    }
}
