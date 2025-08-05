package java.awt.im.spi;

import java.awt.AWTException;
import java.awt.Image;
import java.util.Locale;

/* loaded from: rt.jar:java/awt/im/spi/InputMethodDescriptor.class */
public interface InputMethodDescriptor {
    Locale[] getAvailableLocales() throws AWTException;

    boolean hasDynamicLocaleList();

    String getInputMethodDisplayName(Locale locale, Locale locale2);

    Image getInputMethodIcon(Locale locale);

    InputMethod createInputMethod() throws Exception;
}
