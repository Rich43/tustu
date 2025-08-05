package java.awt.im.spi;

import java.awt.Window;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodRequests;
import java.text.AttributedCharacterIterator;
import javax.swing.JFrame;

/* loaded from: rt.jar:java/awt/im/spi/InputMethodContext.class */
public interface InputMethodContext extends InputMethodRequests {
    void dispatchInputMethodEvent(int i2, AttributedCharacterIterator attributedCharacterIterator, int i3, TextHitInfo textHitInfo, TextHitInfo textHitInfo2);

    Window createInputMethodWindow(String str, boolean z2);

    JFrame createInputMethodJFrame(String str, boolean z2);

    void enableClientWindowNotification(InputMethod inputMethod, boolean z2);
}
