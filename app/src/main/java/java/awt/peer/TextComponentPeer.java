package java.awt.peer;

import java.awt.im.InputMethodRequests;

/* loaded from: rt.jar:java/awt/peer/TextComponentPeer.class */
public interface TextComponentPeer extends ComponentPeer {
    void setEditable(boolean z2);

    String getText();

    void setText(String str);

    int getSelectionStart();

    int getSelectionEnd();

    void select(int i2, int i3);

    void setCaretPosition(int i2);

    int getCaretPosition();

    InputMethodRequests getInputMethodRequests();
}
