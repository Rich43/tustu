package sun.awt.windows;

import java.awt.TextComponent;
import java.awt.event.TextEvent;
import java.awt.peer.TextComponentPeer;

/* loaded from: rt.jar:sun/awt/windows/WTextComponentPeer.class */
abstract class WTextComponentPeer extends WComponentPeer implements TextComponentPeer {
    @Override // java.awt.peer.TextComponentPeer
    public native String getText();

    @Override // java.awt.peer.TextComponentPeer
    public native void setText(String str);

    @Override // java.awt.peer.TextComponentPeer
    public native int getSelectionStart();

    @Override // java.awt.peer.TextComponentPeer
    public native int getSelectionEnd();

    @Override // java.awt.peer.TextComponentPeer
    public native void select(int i2, int i3);

    native void enableEditing(boolean z2);

    private static native void initIDs();

    static {
        initIDs();
    }

    @Override // java.awt.peer.TextComponentPeer
    public void setEditable(boolean z2) {
        enableEditing(z2);
        setBackground(((TextComponent) this.target).getBackground());
    }

    WTextComponentPeer(TextComponent textComponent) {
        super(textComponent);
    }

    @Override // sun.awt.windows.WComponentPeer
    void initialize() {
        TextComponent textComponent = (TextComponent) this.target;
        String text = textComponent.getText();
        if (text != null) {
            setText(text);
        }
        select(textComponent.getSelectionStart(), textComponent.getSelectionEnd());
        setEditable(textComponent.isEditable());
        super.initialize();
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public boolean isFocusable() {
        return true;
    }

    @Override // java.awt.peer.TextComponentPeer
    public void setCaretPosition(int i2) {
        select(i2, i2);
    }

    @Override // java.awt.peer.TextComponentPeer
    public int getCaretPosition() {
        return getSelectionStart();
    }

    public void valueChanged() {
        postEvent(new TextEvent(this.target, 900));
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean shouldClearRectBeforePaint() {
        return false;
    }
}
