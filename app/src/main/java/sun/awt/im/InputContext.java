package sun.awt.im;

import java.awt.AWTEvent;
import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.im.spi.InputMethod;
import java.lang.Character;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import sun.awt.SunToolkit;
import sun.util.locale.LanguageTag;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/im/InputContext.class */
public class InputContext extends java.awt.im.InputContext implements ComponentListener, WindowListener {
    private InputMethodLocator inputMethodLocator;
    private InputMethod inputMethod;
    private boolean inputMethodCreationFailed;
    private HashMap<InputMethodLocator, InputMethod> usedInputMethods;
    private Component currentClientComponent;
    private Component awtFocussedComponent;
    private boolean isInputMethodActive;
    private static InputContext inputMethodWindowContext;
    private Window clientWindowListened;
    private HashMap<InputMethod, Boolean> perInputMethodState;
    private static AWTKeyStroke inputMethodSelectionKey;
    private static final String inputMethodSelectionKeyPath = "/java/awt/im/selectionKey";
    private static final String inputMethodSelectionKeyCodeName = "keyCode";
    private static final String inputMethodSelectionKeyModifiersName = "modifiers";
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.im.InputContext");
    private static InputMethod previousInputMethod = null;
    private static boolean inputMethodSelectionKeyInitialized = false;
    private Character.Subset[] characterSubsets = null;
    private boolean compositionAreaHidden = false;
    private boolean clientWindowNotificationEnabled = false;
    private Rectangle clientWindowLocation = null;

    protected InputContext() {
        InputMethodManager inputMethodManager = InputMethodManager.getInstance();
        synchronized (InputContext.class) {
            if (!inputMethodSelectionKeyInitialized) {
                inputMethodSelectionKeyInitialized = true;
                if (inputMethodManager.hasMultipleInputMethods()) {
                    initializeInputMethodSelectionKey();
                }
            }
        }
        selectInputMethod(inputMethodManager.getDefaultKeyboardLocale());
    }

    @Override // java.awt.im.InputContext
    public synchronized boolean selectInputMethod(Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }
        if (this.inputMethod != null) {
            if (this.inputMethod.setLocale(locale)) {
                return true;
            }
        } else if (this.inputMethodLocator != null && this.inputMethodLocator.isLocaleAvailable(locale)) {
            this.inputMethodLocator = this.inputMethodLocator.deriveLocator(locale);
            return true;
        }
        InputMethodLocator inputMethodLocatorFindInputMethod = InputMethodManager.getInstance().findInputMethod(locale);
        if (inputMethodLocatorFindInputMethod != null) {
            changeInputMethod(inputMethodLocatorFindInputMethod);
            return true;
        }
        if (this.inputMethod == null && this.inputMethodLocator != null) {
            this.inputMethod = getInputMethod();
            if (this.inputMethod != null) {
                return this.inputMethod.setLocale(locale);
            }
            return false;
        }
        return false;
    }

    @Override // java.awt.im.InputContext
    public Locale getLocale() {
        if (this.inputMethod != null) {
            return this.inputMethod.getLocale();
        }
        if (this.inputMethodLocator != null) {
            return this.inputMethodLocator.getLocale();
        }
        return null;
    }

    @Override // java.awt.im.InputContext
    public void setCharacterSubsets(Character.Subset[] subsetArr) {
        if (subsetArr == null) {
            this.characterSubsets = null;
        } else {
            this.characterSubsets = new Character.Subset[subsetArr.length];
            System.arraycopy(subsetArr, 0, this.characterSubsets, 0, this.characterSubsets.length);
        }
        if (this.inputMethod != null) {
            this.inputMethod.setCharacterSubsets(subsetArr);
        }
    }

    @Override // java.awt.im.InputContext
    public synchronized void reconvert() {
        InputMethod inputMethod = getInputMethod();
        if (inputMethod == null) {
            throw new UnsupportedOperationException();
        }
        inputMethod.reconvert();
    }

    @Override // java.awt.im.InputContext
    public void dispatchEvent(AWTEvent aWTEvent) {
        Component oppositeComponent;
        if (aWTEvent instanceof InputMethodEvent) {
            return;
        }
        if ((aWTEvent instanceof FocusEvent) && (oppositeComponent = ((FocusEvent) aWTEvent).getOppositeComponent()) != null && (getComponentWindow(oppositeComponent) instanceof InputMethodWindow) && oppositeComponent.getInputContext() == this) {
            return;
        }
        InputMethod inputMethod = getInputMethod();
        switch (aWTEvent.getID()) {
            case 401:
                if (checkInputMethodSelectionKey((KeyEvent) aWTEvent)) {
                    InputMethodManager.getInstance().notifyChangeRequestByHotKey((Component) aWTEvent.getSource());
                    return;
                }
                break;
            case 1004:
                focusGained((Component) aWTEvent.getSource());
                return;
            case 1005:
                focusLost((Component) aWTEvent.getSource(), ((FocusEvent) aWTEvent).isTemporary());
                return;
        }
        if (inputMethod != null && (aWTEvent instanceof InputEvent)) {
            inputMethod.dispatchEvent(aWTEvent);
        }
    }

    private void focusGained(Component component) {
        synchronized (component.getTreeLock()) {
            synchronized (this) {
                if (!"sun.awt.im.CompositionArea".equals(component.getClass().getName()) && !(getComponentWindow(component) instanceof InputMethodWindow)) {
                    if (!component.isDisplayable()) {
                        return;
                    }
                    if (this.inputMethod != null && this.currentClientComponent != null && this.currentClientComponent != component) {
                        if (!this.isInputMethodActive) {
                            activateInputMethod(false);
                        }
                        endComposition();
                        deactivateInputMethod(false);
                    }
                    this.currentClientComponent = component;
                }
                this.awtFocussedComponent = component;
                if (this.inputMethod instanceof InputMethodAdapter) {
                    ((InputMethodAdapter) this.inputMethod).setAWTFocussedComponent(component);
                }
                if (!this.isInputMethodActive) {
                    activateInputMethod(true);
                }
                InputMethodContext inputMethodContext = (InputMethodContext) this;
                if (!inputMethodContext.isCompositionAreaVisible()) {
                    if (component.getInputMethodRequests() != null && inputMethodContext.useBelowTheSpotInput()) {
                        inputMethodContext.setCompositionAreaUndecorated(true);
                    } else {
                        inputMethodContext.setCompositionAreaUndecorated(false);
                    }
                }
                if (this.compositionAreaHidden) {
                    ((InputMethodContext) this).setCompositionAreaVisible(true);
                    this.compositionAreaHidden = false;
                }
            }
        }
    }

    private void activateInputMethod(boolean z2) {
        Boolean boolRemove;
        if (inputMethodWindowContext != null && inputMethodWindowContext != this && inputMethodWindowContext.inputMethodLocator != null && !inputMethodWindowContext.inputMethodLocator.sameInputMethod(this.inputMethodLocator) && inputMethodWindowContext.inputMethod != null) {
            inputMethodWindowContext.inputMethod.hideWindows();
        }
        inputMethodWindowContext = this;
        if (this.inputMethod != null) {
            if (previousInputMethod != this.inputMethod && (previousInputMethod instanceof InputMethodAdapter)) {
                ((InputMethodAdapter) previousInputMethod).stopListening();
            }
            previousInputMethod = null;
            if (log.isLoggable(PlatformLogger.Level.FINE)) {
                log.fine("Current client component " + ((Object) this.currentClientComponent));
            }
            if (this.inputMethod instanceof InputMethodAdapter) {
                ((InputMethodAdapter) this.inputMethod).setClientComponent(this.currentClientComponent);
            }
            this.inputMethod.activate();
            this.isInputMethodActive = true;
            if (this.perInputMethodState != null && (boolRemove = this.perInputMethodState.remove(this.inputMethod)) != null) {
                this.clientWindowNotificationEnabled = boolRemove.booleanValue();
            }
            if (this.clientWindowNotificationEnabled) {
                if (!addedClientWindowListeners()) {
                    addClientWindowListeners();
                }
                synchronized (this) {
                    if (this.clientWindowListened != null) {
                        notifyClientWindowChange(this.clientWindowListened);
                    }
                }
            } else if (addedClientWindowListeners()) {
                removeClientWindowListeners();
            }
        }
        InputMethodManager.getInstance().setInputContext(this);
        ((InputMethodContext) this).grabCompositionArea(z2);
    }

    static Window getComponentWindow(Component component) {
        while (component != null) {
            if (component instanceof Window) {
                return (Window) component;
            }
            component = component.getParent();
        }
        return null;
    }

    private void focusLost(Component component, boolean z2) {
        synchronized (component.getTreeLock()) {
            synchronized (this) {
                if (this.isInputMethodActive) {
                    deactivateInputMethod(z2);
                }
                this.awtFocussedComponent = null;
                if (this.inputMethod instanceof InputMethodAdapter) {
                    ((InputMethodAdapter) this.inputMethod).setAWTFocussedComponent(null);
                }
                InputMethodContext inputMethodContext = (InputMethodContext) this;
                if (inputMethodContext.isCompositionAreaVisible()) {
                    inputMethodContext.setCompositionAreaVisible(false);
                    this.compositionAreaHidden = true;
                }
            }
        }
    }

    private boolean checkInputMethodSelectionKey(KeyEvent keyEvent) {
        if (inputMethodSelectionKey != null) {
            return inputMethodSelectionKey.equals(AWTKeyStroke.getAWTKeyStrokeForEvent(keyEvent));
        }
        return false;
    }

    private void deactivateInputMethod(boolean z2) {
        InputMethodManager.getInstance().setInputContext(null);
        if (this.inputMethod != null) {
            this.isInputMethodActive = false;
            this.inputMethod.deactivate(z2);
            previousInputMethod = this.inputMethod;
        }
    }

    synchronized void changeInputMethod(InputMethodLocator inputMethodLocator) {
        if (this.inputMethodLocator == null) {
            this.inputMethodLocator = inputMethodLocator;
            this.inputMethodCreationFailed = false;
            return;
        }
        if (this.inputMethodLocator.sameInputMethod(inputMethodLocator)) {
            Locale locale = inputMethodLocator.getLocale();
            if (locale != null && this.inputMethodLocator.getLocale() != locale) {
                if (this.inputMethod != null) {
                    this.inputMethod.setLocale(locale);
                }
                this.inputMethodLocator = inputMethodLocator;
                return;
            }
            return;
        }
        Locale locale2 = this.inputMethodLocator.getLocale();
        boolean z2 = this.isInputMethodActive;
        boolean z3 = false;
        boolean zIsCompositionEnabled = false;
        if (this.inputMethod != null) {
            try {
                zIsCompositionEnabled = this.inputMethod.isCompositionEnabled();
                z3 = true;
            } catch (UnsupportedOperationException e2) {
            }
            if (this.currentClientComponent != null) {
                if (!this.isInputMethodActive) {
                    activateInputMethod(false);
                }
                endComposition();
                deactivateInputMethod(false);
                if (this.inputMethod instanceof InputMethodAdapter) {
                    ((InputMethodAdapter) this.inputMethod).setClientComponent(null);
                }
            }
            locale2 = this.inputMethod.getLocale();
            if (this.usedInputMethods == null) {
                this.usedInputMethods = new HashMap<>(5);
            }
            if (this.perInputMethodState == null) {
                this.perInputMethodState = new HashMap<>(5);
            }
            this.usedInputMethods.put(this.inputMethodLocator.deriveLocator(null), this.inputMethod);
            this.perInputMethodState.put(this.inputMethod, Boolean.valueOf(this.clientWindowNotificationEnabled));
            enableClientWindowNotification(this.inputMethod, false);
            if (this == inputMethodWindowContext) {
                this.inputMethod.hideWindows();
                inputMethodWindowContext = null;
            }
            this.inputMethodLocator = null;
            this.inputMethod = null;
            this.inputMethodCreationFailed = false;
        }
        if (inputMethodLocator.getLocale() == null && locale2 != null && inputMethodLocator.isLocaleAvailable(locale2)) {
            inputMethodLocator = inputMethodLocator.deriveLocator(locale2);
        }
        this.inputMethodLocator = inputMethodLocator;
        this.inputMethodCreationFailed = false;
        if (z2) {
            this.inputMethod = getInputMethodInstance();
            if (this.inputMethod instanceof InputMethodAdapter) {
                ((InputMethodAdapter) this.inputMethod).setAWTFocussedComponent(this.awtFocussedComponent);
            }
            activateInputMethod(true);
        }
        if (z3) {
            this.inputMethod = getInputMethod();
            if (this.inputMethod != null) {
                try {
                    this.inputMethod.setCompositionEnabled(zIsCompositionEnabled);
                } catch (UnsupportedOperationException e3) {
                }
            }
        }
    }

    Component getClientComponent() {
        return this.currentClientComponent;
    }

    @Override // java.awt.im.InputContext
    public synchronized void removeNotify(Component component) {
        if (component == null) {
            throw new NullPointerException();
        }
        if (this.inputMethod == null) {
            if (component == this.currentClientComponent) {
                this.currentClientComponent = null;
                return;
            }
            return;
        }
        if (component == this.awtFocussedComponent) {
            focusLost(component, false);
        }
        if (component == this.currentClientComponent) {
            if (this.isInputMethodActive) {
                deactivateInputMethod(false);
            }
            this.inputMethod.removeNotify();
            if (this.clientWindowNotificationEnabled && addedClientWindowListeners()) {
                removeClientWindowListeners();
            }
            this.currentClientComponent = null;
            if (this.inputMethod instanceof InputMethodAdapter) {
                ((InputMethodAdapter) this.inputMethod).setClientComponent(null);
            }
            if (EventQueue.isDispatchThread()) {
                ((InputMethodContext) this).releaseCompositionArea();
            } else {
                EventQueue.invokeLater(new Runnable() { // from class: sun.awt.im.InputContext.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ((InputMethodContext) InputContext.this).releaseCompositionArea();
                    }
                });
            }
        }
    }

    @Override // java.awt.im.InputContext
    public synchronized void dispose() {
        if (this.currentClientComponent != null) {
            throw new IllegalStateException("Can't dispose InputContext while it's active");
        }
        if (this.inputMethod != null) {
            if (this == inputMethodWindowContext) {
                this.inputMethod.hideWindows();
                inputMethodWindowContext = null;
            }
            if (this.inputMethod == previousInputMethod) {
                previousInputMethod = null;
            }
            if (this.clientWindowNotificationEnabled) {
                if (addedClientWindowListeners()) {
                    removeClientWindowListeners();
                }
                this.clientWindowNotificationEnabled = false;
            }
            this.inputMethod.dispose();
            if (this.clientWindowNotificationEnabled) {
                enableClientWindowNotification(this.inputMethod, false);
            }
            this.inputMethod = null;
        }
        this.inputMethodLocator = null;
        if (this.usedInputMethods != null && !this.usedInputMethods.isEmpty()) {
            Iterator<InputMethod> it = this.usedInputMethods.values().iterator();
            this.usedInputMethods = null;
            while (it.hasNext()) {
                it.next().dispose();
            }
        }
        this.clientWindowNotificationEnabled = false;
        this.clientWindowListened = null;
        this.perInputMethodState = null;
    }

    @Override // java.awt.im.InputContext
    public synchronized Object getInputMethodControlObject() {
        InputMethod inputMethod = getInputMethod();
        if (inputMethod != null) {
            return inputMethod.getControlObject();
        }
        return null;
    }

    @Override // java.awt.im.InputContext
    public void setCompositionEnabled(boolean z2) {
        InputMethod inputMethod = getInputMethod();
        if (inputMethod == null) {
            throw new UnsupportedOperationException();
        }
        inputMethod.setCompositionEnabled(z2);
    }

    @Override // java.awt.im.InputContext
    public boolean isCompositionEnabled() {
        InputMethod inputMethod = getInputMethod();
        if (inputMethod == null) {
            throw new UnsupportedOperationException();
        }
        return inputMethod.isCompositionEnabled();
    }

    public String getInputMethodInfo() {
        InputMethod inputMethod = getInputMethod();
        if (inputMethod == null) {
            throw new UnsupportedOperationException("Null input method");
        }
        String inputMethodDisplayName = null;
        if (inputMethod instanceof InputMethodAdapter) {
            inputMethodDisplayName = ((InputMethodAdapter) inputMethod).getNativeInputMethodInfo();
        }
        if (inputMethodDisplayName == null && this.inputMethodLocator != null) {
            inputMethodDisplayName = this.inputMethodLocator.getDescriptor().getInputMethodDisplayName(getLocale(), SunToolkit.getStartupLocale());
        }
        if (inputMethodDisplayName != null && !inputMethodDisplayName.equals("")) {
            return inputMethodDisplayName;
        }
        return inputMethod.toString() + LanguageTag.SEP + inputMethod.getLocale().toString();
    }

    public void disableNativeIM() {
        InputMethod inputMethod = getInputMethod();
        if (inputMethod != null && (inputMethod instanceof InputMethodAdapter)) {
            ((InputMethodAdapter) inputMethod).stopListening();
        }
    }

    private synchronized InputMethod getInputMethod() {
        if (this.inputMethod != null) {
            return this.inputMethod;
        }
        if (this.inputMethodCreationFailed) {
            return null;
        }
        this.inputMethod = getInputMethodInstance();
        return this.inputMethod;
    }

    private InputMethod getInputMethodInstance() {
        InputMethodLocator inputMethodLocator = this.inputMethodLocator;
        if (inputMethodLocator == null) {
            this.inputMethodCreationFailed = true;
            return null;
        }
        Locale locale = inputMethodLocator.getLocale();
        InputMethod inputMethodCreateInputMethod = null;
        if (this.usedInputMethods != null) {
            inputMethodCreateInputMethod = this.usedInputMethods.remove(inputMethodLocator.deriveLocator(null));
            if (inputMethodCreateInputMethod != null) {
                if (locale != null) {
                    inputMethodCreateInputMethod.setLocale(locale);
                }
                inputMethodCreateInputMethod.setCharacterSubsets(this.characterSubsets);
                Boolean boolRemove = this.perInputMethodState.remove(inputMethodCreateInputMethod);
                if (boolRemove != null) {
                    enableClientWindowNotification(inputMethodCreateInputMethod, boolRemove.booleanValue());
                }
                ((InputMethodContext) this).setInputMethodSupportsBelowTheSpot(!(inputMethodCreateInputMethod instanceof InputMethodAdapter) || ((InputMethodAdapter) inputMethodCreateInputMethod).supportsBelowTheSpot());
                return inputMethodCreateInputMethod;
            }
        }
        try {
            inputMethodCreateInputMethod = inputMethodLocator.getDescriptor().createInputMethod();
            if (locale != null) {
                inputMethodCreateInputMethod.setLocale(locale);
            }
            inputMethodCreateInputMethod.setInputMethodContext((InputMethodContext) this);
            inputMethodCreateInputMethod.setCharacterSubsets(this.characterSubsets);
        } catch (Exception e2) {
            logCreationFailed(e2);
            this.inputMethodCreationFailed = true;
            if (inputMethodCreateInputMethod != null) {
                inputMethodCreateInputMethod = null;
            }
        } catch (LinkageError e3) {
            logCreationFailed(e3);
            this.inputMethodCreationFailed = true;
        }
        ((InputMethodContext) this).setInputMethodSupportsBelowTheSpot(!(inputMethodCreateInputMethod instanceof InputMethodAdapter) || ((InputMethodAdapter) inputMethodCreateInputMethod).supportsBelowTheSpot());
        return inputMethodCreateInputMethod;
    }

    private void logCreationFailed(Throwable th) {
        PlatformLogger logger = PlatformLogger.getLogger("sun.awt.im");
        if (logger.isLoggable(PlatformLogger.Level.CONFIG)) {
            logger.config(new MessageFormat(Toolkit.getProperty("AWT.InputMethodCreationFailed", "Could not create {0}. Reason: {1}")).format(new Object[]{this.inputMethodLocator.getDescriptor().getInputMethodDisplayName(null, Locale.getDefault()), th.getLocalizedMessage()}));
        }
    }

    InputMethodLocator getInputMethodLocator() {
        if (this.inputMethod != null) {
            return this.inputMethodLocator.deriveLocator(this.inputMethod.getLocale());
        }
        return this.inputMethodLocator;
    }

    @Override // java.awt.im.InputContext
    public synchronized void endComposition() {
        if (this.inputMethod != null) {
            this.inputMethod.endComposition();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void enableClientWindowNotification(InputMethod inputMethod, boolean z2) {
        if (inputMethod != this.inputMethod) {
            if (this.perInputMethodState == null) {
                this.perInputMethodState = new HashMap<>(5);
            }
            this.perInputMethodState.put(inputMethod, Boolean.valueOf(z2));
            return;
        }
        if (this.clientWindowNotificationEnabled != z2) {
            this.clientWindowLocation = null;
            this.clientWindowNotificationEnabled = z2;
        }
        if (this.clientWindowNotificationEnabled) {
            if (!addedClientWindowListeners()) {
                addClientWindowListeners();
            }
            if (this.clientWindowListened != null) {
                this.clientWindowLocation = null;
                notifyClientWindowChange(this.clientWindowListened);
                return;
            }
            return;
        }
        if (addedClientWindowListeners()) {
            removeClientWindowListeners();
        }
    }

    private synchronized void notifyClientWindowChange(Window window) {
        if (this.inputMethod == null) {
            return;
        }
        if (!window.isVisible() || ((window instanceof Frame) && ((Frame) window).getState() == 1)) {
            this.clientWindowLocation = null;
            this.inputMethod.notifyClientWindowChange(null);
            return;
        }
        Rectangle bounds = window.getBounds();
        if (this.clientWindowLocation == null || !this.clientWindowLocation.equals(bounds)) {
            this.clientWindowLocation = bounds;
            this.inputMethod.notifyClientWindowChange(this.clientWindowLocation);
        }
    }

    private synchronized void addClientWindowListeners() {
        Window componentWindow;
        Component clientComponent = getClientComponent();
        if (clientComponent == null || (componentWindow = getComponentWindow(clientComponent)) == null) {
            return;
        }
        componentWindow.addComponentListener(this);
        componentWindow.addWindowListener(this);
        this.clientWindowListened = componentWindow;
    }

    private synchronized void removeClientWindowListeners() {
        this.clientWindowListened.removeComponentListener(this);
        this.clientWindowListened.removeWindowListener(this);
        this.clientWindowListened = null;
    }

    private boolean addedClientWindowListeners() {
        return this.clientWindowListened != null;
    }

    @Override // java.awt.event.ComponentListener
    public void componentResized(ComponentEvent componentEvent) {
        notifyClientWindowChange((Window) componentEvent.getComponent());
    }

    @Override // java.awt.event.ComponentListener
    public void componentMoved(ComponentEvent componentEvent) {
        notifyClientWindowChange((Window) componentEvent.getComponent());
    }

    @Override // java.awt.event.ComponentListener
    public void componentShown(ComponentEvent componentEvent) {
        notifyClientWindowChange((Window) componentEvent.getComponent());
    }

    @Override // java.awt.event.ComponentListener
    public void componentHidden(ComponentEvent componentEvent) {
        notifyClientWindowChange((Window) componentEvent.getComponent());
    }

    @Override // java.awt.event.WindowListener
    public void windowOpened(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowClosed(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowIconified(WindowEvent windowEvent) {
        notifyClientWindowChange(windowEvent.getWindow());
    }

    @Override // java.awt.event.WindowListener
    public void windowDeiconified(WindowEvent windowEvent) {
        notifyClientWindowChange(windowEvent.getWindow());
    }

    @Override // java.awt.event.WindowListener
    public void windowActivated(WindowEvent windowEvent) {
    }

    @Override // java.awt.event.WindowListener
    public void windowDeactivated(WindowEvent windowEvent) {
    }

    private void initializeInputMethodSelectionKey() {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.awt.im.InputContext.2
            @Override // java.security.PrivilegedAction
            public Object run() {
                AWTKeyStroke unused = InputContext.inputMethodSelectionKey = InputContext.this.getInputMethodSelectionKeyStroke(Preferences.userRoot());
                if (InputContext.inputMethodSelectionKey == null) {
                    AWTKeyStroke unused2 = InputContext.inputMethodSelectionKey = InputContext.this.getInputMethodSelectionKeyStroke(Preferences.systemRoot());
                    return null;
                }
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AWTKeyStroke getInputMethodSelectionKeyStroke(Preferences preferences) {
        Preferences preferencesNode;
        int i2;
        try {
            if (preferences.nodeExists(inputMethodSelectionKeyPath) && (i2 = (preferencesNode = preferences.node(inputMethodSelectionKeyPath)).getInt(inputMethodSelectionKeyCodeName, 0)) != 0) {
                return AWTKeyStroke.getAWTKeyStroke(i2, preferencesNode.getInt(inputMethodSelectionKeyModifiersName, 0));
            }
            return null;
        } catch (BackingStoreException e2) {
            return null;
        }
    }
}
