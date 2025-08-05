package java.awt.im.spi;

import java.awt.AWTEvent;
import java.awt.Rectangle;
import java.lang.Character;
import java.util.Locale;

/* loaded from: rt.jar:java/awt/im/spi/InputMethod.class */
public interface InputMethod {
    void setInputMethodContext(InputMethodContext inputMethodContext);

    boolean setLocale(Locale locale);

    Locale getLocale();

    void setCharacterSubsets(Character.Subset[] subsetArr);

    void setCompositionEnabled(boolean z2);

    boolean isCompositionEnabled();

    void reconvert();

    void dispatchEvent(AWTEvent aWTEvent);

    void notifyClientWindowChange(Rectangle rectangle);

    void activate();

    void deactivate(boolean z2);

    void hideWindows();

    void removeNotify();

    void endComposition();

    void dispose();

    Object getControlObject();
}
