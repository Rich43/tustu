package java.awt.im;

import java.awt.AWTEvent;
import java.awt.Component;
import java.beans.Transient;
import java.lang.Character;
import java.util.Locale;
import sun.awt.im.InputMethodContext;

/* loaded from: rt.jar:java/awt/im/InputContext.class */
public class InputContext {
    protected InputContext() {
    }

    public static InputContext getInstance() {
        return new InputMethodContext();
    }

    public boolean selectInputMethod(Locale locale) {
        return false;
    }

    public Locale getLocale() {
        return null;
    }

    public void setCharacterSubsets(Character.Subset[] subsetArr) {
    }

    public void setCompositionEnabled(boolean z2) {
    }

    @Transient
    public boolean isCompositionEnabled() {
        return false;
    }

    public void reconvert() {
    }

    public void dispatchEvent(AWTEvent aWTEvent) {
    }

    public void removeNotify(Component component) {
    }

    public void endComposition() {
    }

    public void dispose() {
    }

    public Object getInputMethodControlObject() {
        return null;
    }
}
