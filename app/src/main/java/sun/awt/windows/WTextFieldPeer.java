package sun.awt.windows;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.im.InputMethodRequests;
import java.awt.peer.TextFieldPeer;

/* loaded from: rt.jar:sun/awt/windows/WTextFieldPeer.class */
final class WTextFieldPeer extends WTextComponentPeer implements TextFieldPeer {
    @Override // java.awt.peer.TextFieldPeer
    public native void setEchoChar(char c2);

    @Override // sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        FontMetrics fontMetrics = getFontMetrics(((TextField) this.target).getFont());
        return new Dimension(fontMetrics.stringWidth(getText()) + 24, fontMetrics.getHeight() + 8);
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean handleJavaKeyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getID()) {
            case 400:
                if (keyEvent.getKeyChar() == '\n' && !keyEvent.isAltDown() && !keyEvent.isControlDown()) {
                    postEvent(new ActionEvent(this.target, 1001, getText(), keyEvent.getWhen(), keyEvent.getModifiers()));
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    @Override // java.awt.peer.TextFieldPeer
    public Dimension getPreferredSize(int i2) {
        return getMinimumSize(i2);
    }

    @Override // java.awt.peer.TextFieldPeer
    public Dimension getMinimumSize(int i2) {
        FontMetrics fontMetrics = getFontMetrics(((TextField) this.target).getFont());
        return new Dimension((fontMetrics.charWidth('0') * i2) + 24, fontMetrics.getHeight() + 8);
    }

    @Override // java.awt.peer.TextComponentPeer
    public InputMethodRequests getInputMethodRequests() {
        return null;
    }

    WTextFieldPeer(TextField textField) {
        super(textField);
    }

    @Override // sun.awt.windows.WTextComponentPeer, sun.awt.windows.WComponentPeer
    void initialize() {
        TextField textField = (TextField) this.target;
        if (textField.echoCharIsSet()) {
            setEchoChar(textField.getEchoChar());
        }
        super.initialize();
    }
}
