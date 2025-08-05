package sun.awt.im;

import java.awt.Component;
import java.util.Locale;

/* loaded from: rt.jar:sun/awt/im/InputMethodManager.class */
public abstract class InputMethodManager {
    private static final String threadName = "AWT-InputMethodManager";
    private static final Object LOCK = new Object();
    private static InputMethodManager inputMethodManager;

    public abstract String getTriggerMenuString();

    public abstract void notifyChangeRequest(Component component);

    public abstract void notifyChangeRequestByHotKey(Component component);

    abstract void setInputContext(InputContext inputContext);

    abstract InputMethodLocator findInputMethod(Locale locale);

    abstract Locale getDefaultKeyboardLocale();

    abstract boolean hasMultipleInputMethods();

    public static final InputMethodManager getInstance() {
        if (inputMethodManager != null) {
            return inputMethodManager;
        }
        synchronized (LOCK) {
            if (inputMethodManager == null) {
                ExecutableInputMethodManager executableInputMethodManager = new ExecutableInputMethodManager();
                if (executableInputMethodManager.hasMultipleInputMethods()) {
                    executableInputMethodManager.initialize();
                    Thread thread = new Thread(executableInputMethodManager, threadName);
                    thread.setDaemon(true);
                    thread.setPriority(6);
                    thread.start();
                }
                inputMethodManager = executableInputMethodManager;
            }
        }
        return inputMethodManager;
    }
}
