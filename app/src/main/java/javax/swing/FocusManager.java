package javax.swing;

import java.awt.DefaultFocusTraversalPolicy;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.KeyboardFocusManager;

/* loaded from: rt.jar:javax/swing/FocusManager.class */
public abstract class FocusManager extends DefaultKeyboardFocusManager {
    public static final String FOCUS_MANAGER_CLASS_PROPERTY = "FocusManagerClassName";
    private static boolean enabled = true;

    public static FocusManager getCurrentManager() {
        KeyboardFocusManager currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        if (currentKeyboardFocusManager instanceof FocusManager) {
            return (FocusManager) currentKeyboardFocusManager;
        }
        return new DelegatingDefaultFocusManager(currentKeyboardFocusManager);
    }

    public static void setCurrentManager(FocusManager focusManager) throws SecurityException {
        KeyboardFocusManager.setCurrentKeyboardFocusManager(focusManager instanceof DelegatingDefaultFocusManager ? ((DelegatingDefaultFocusManager) focusManager).getDelegate() : focusManager);
    }

    @Deprecated
    public static void disableSwingFocusManager() {
        if (enabled) {
            enabled = false;
            KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(new DefaultFocusTraversalPolicy());
        }
    }

    @Deprecated
    public static boolean isFocusManagerEnabled() {
        return enabled;
    }
}
