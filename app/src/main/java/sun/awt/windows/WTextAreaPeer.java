package sun.awt.windows;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.TextArea;
import java.awt.im.InputMethodRequests;
import java.awt.peer.TextAreaPeer;

/* loaded from: rt.jar:sun/awt/windows/WTextAreaPeer.class */
final class WTextAreaPeer extends WTextComponentPeer implements TextAreaPeer {
    @Override // java.awt.peer.TextAreaPeer
    public native void replaceRange(String str, int i2, int i3);

    @Override // sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        return getMinimumSize(10, 60);
    }

    @Override // java.awt.peer.TextAreaPeer
    public void insert(String str, int i2) {
        replaceRange(str, i2, i2);
    }

    @Override // java.awt.peer.TextAreaPeer
    public Dimension getPreferredSize(int i2, int i3) {
        return getMinimumSize(i2, i3);
    }

    @Override // java.awt.peer.TextAreaPeer
    public Dimension getMinimumSize(int i2, int i3) {
        FontMetrics fontMetrics = getFontMetrics(((TextArea) this.target).getFont());
        return new Dimension((fontMetrics.charWidth('0') * i3) + 20, (fontMetrics.getHeight() * i2) + 20);
    }

    @Override // java.awt.peer.TextComponentPeer
    public InputMethodRequests getInputMethodRequests() {
        return null;
    }

    WTextAreaPeer(TextArea textArea) {
        super(textArea);
    }
}
