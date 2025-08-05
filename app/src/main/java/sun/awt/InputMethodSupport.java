package sun.awt;

import java.awt.AWTException;
import java.awt.Window;
import java.awt.im.spi.InputMethodDescriptor;
import java.util.Locale;
import sun.awt.im.InputContext;

/* loaded from: rt.jar:sun/awt/InputMethodSupport.class */
public interface InputMethodSupport {
    InputMethodDescriptor getInputMethodAdapterDescriptor() throws AWTException;

    Window createInputMethodWindow(String str, InputContext inputContext);

    boolean enableInputMethodsForTextComponent();

    Locale getDefaultKeyboardLocale();
}
