package sun.awt.im;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.InvocationEvent;
import java.awt.im.spi.InputMethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.Vector;
import java.util.prefs.Preferences;
import sun.awt.AppContext;
import sun.awt.InputMethodSupport;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:sun/awt/im/ExecutableInputMethodManager.class */
class ExecutableInputMethodManager extends InputMethodManager implements Runnable {
    private InputContext currentInputContext;
    private String triggerMenuString;
    private InputMethodPopupMenu selectionMenu;
    private static String selectInputMethodMenuTitle;
    private InputMethodLocator hostAdapterLocator;
    private int javaInputMethodCount;
    private Vector<InputMethodLocator> javaInputMethodLocatorList;
    private Component requestComponent;
    private InputContext requestInputContext;
    private static final String preferredIMNode = "/sun/awt/im/preferredInputMethod";
    private static final String descriptorKey = "descriptor";
    private Hashtable<String, InputMethodLocator> preferredLocatorCache = new Hashtable<>();
    private Preferences userRoot;

    ExecutableInputMethodManager() {
        InputMethodDescriptor inputMethodAdapterDescriptor;
        Object defaultToolkit = Toolkit.getDefaultToolkit();
        try {
            if ((defaultToolkit instanceof InputMethodSupport) && (inputMethodAdapterDescriptor = ((InputMethodSupport) defaultToolkit).getInputMethodAdapterDescriptor()) != null) {
                this.hostAdapterLocator = new InputMethodLocator(inputMethodAdapterDescriptor, null, null);
            }
        } catch (AWTException e2) {
        }
        this.javaInputMethodLocatorList = new Vector<>();
        initializeInputMethodLocatorList();
    }

    synchronized void initialize() {
        selectInputMethodMenuTitle = Toolkit.getProperty("AWT.InputMethodSelectionMenu", "Select Input Method");
        this.triggerMenuString = selectInputMethodMenuTitle;
    }

    @Override // java.lang.Runnable
    public void run() {
        while (!hasMultipleInputMethods()) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e2) {
            }
        }
        while (true) {
            waitForChangeRequest();
            initializeInputMethodLocatorList();
            try {
                if (this.requestComponent != null) {
                    showInputMethodMenuOnRequesterEDT(this.requestComponent);
                } else {
                    EventQueue.invokeAndWait(new Runnable() { // from class: sun.awt.im.ExecutableInputMethodManager.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ExecutableInputMethodManager.this.showInputMethodMenu();
                        }
                    });
                }
            } catch (InterruptedException e3) {
            } catch (InvocationTargetException e4) {
            }
        }
    }

    private void showInputMethodMenuOnRequesterEDT(Component component) throws InterruptedException, InvocationTargetException {
        if (component == null) {
            return;
        }
        Object obj = new Object() { // from class: sun.awt.im.ExecutableInputMethodManager.1AWTInvocationLock
        };
        InvocationEvent invocationEvent = new InvocationEvent((Object) component, new Runnable() { // from class: sun.awt.im.ExecutableInputMethodManager.2
            @Override // java.lang.Runnable
            public void run() {
                ExecutableInputMethodManager.this.showInputMethodMenu();
            }
        }, obj, true);
        AppContext appContextTargetToAppContext = SunToolkit.targetToAppContext(component);
        synchronized (obj) {
            SunToolkit.postEvent(appContextTargetToAppContext, invocationEvent);
            while (!invocationEvent.isDispatched()) {
                obj.wait();
            }
        }
        Throwable throwable = invocationEvent.getThrowable();
        if (throwable != null) {
            throw new InvocationTargetException(throwable);
        }
    }

    @Override // sun.awt.im.InputMethodManager
    void setInputContext(InputContext inputContext) {
        if (this.currentInputContext == null || inputContext != null) {
        }
        this.currentInputContext = inputContext;
    }

    @Override // sun.awt.im.InputMethodManager
    public synchronized void notifyChangeRequest(Component component) {
        if ((!(component instanceof Frame) && !(component instanceof Dialog)) || this.requestComponent != null) {
            return;
        }
        this.requestComponent = component;
        notify();
    }

    @Override // sun.awt.im.InputMethodManager
    public synchronized void notifyChangeRequestByHotKey(Component component) {
        while (!(component instanceof Frame) && !(component instanceof Dialog)) {
            if (component == null) {
                return;
            } else {
                component = component.getParent();
            }
        }
        notifyChangeRequest(component);
    }

    @Override // sun.awt.im.InputMethodManager
    public String getTriggerMenuString() {
        return this.triggerMenuString;
    }

    @Override // sun.awt.im.InputMethodManager
    boolean hasMultipleInputMethods() {
        return (this.hostAdapterLocator != null && this.javaInputMethodCount > 0) || this.javaInputMethodCount > 1;
    }

    private synchronized void waitForChangeRequest() {
        while (this.requestComponent == null) {
            try {
                wait();
            } catch (InterruptedException e2) {
                return;
            }
        }
    }

    private void initializeInputMethodLocatorList() {
        synchronized (this.javaInputMethodLocatorList) {
            this.javaInputMethodLocatorList.clear();
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: sun.awt.im.ExecutableInputMethodManager.3
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() {
                        Iterator it = ServiceLoader.loadInstalled(InputMethodDescriptor.class).iterator();
                        while (it.hasNext()) {
                            InputMethodDescriptor inputMethodDescriptor = (InputMethodDescriptor) it.next();
                            ExecutableInputMethodManager.this.javaInputMethodLocatorList.add(new InputMethodLocator(inputMethodDescriptor, inputMethodDescriptor.getClass().getClassLoader(), null));
                        }
                        return null;
                    }
                });
            } catch (PrivilegedActionException e2) {
                e2.printStackTrace();
            }
            this.javaInputMethodCount = this.javaInputMethodLocatorList.size();
        }
        if (hasMultipleInputMethods()) {
            if (this.userRoot == null) {
                this.userRoot = getUserRoot();
                return;
            }
            return;
        }
        this.triggerMenuString = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showInputMethodMenu() {
        if (!hasMultipleInputMethods()) {
            this.requestComponent = null;
            return;
        }
        this.selectionMenu = InputMethodPopupMenu.getInstance(this.requestComponent, selectInputMethodMenuTitle);
        this.selectionMenu.removeAll();
        String currentSelection = getCurrentSelection();
        if (this.hostAdapterLocator != null) {
            this.selectionMenu.addOneInputMethodToMenu(this.hostAdapterLocator, currentSelection);
            this.selectionMenu.addSeparator();
        }
        for (int i2 = 0; i2 < this.javaInputMethodLocatorList.size(); i2++) {
            this.selectionMenu.addOneInputMethodToMenu(this.javaInputMethodLocatorList.get(i2), currentSelection);
        }
        synchronized (this) {
            this.selectionMenu.addToComponent(this.requestComponent);
            this.requestInputContext = this.currentInputContext;
            this.selectionMenu.show(this.requestComponent, 60, 80);
            this.requestComponent = null;
        }
    }

    private String getCurrentSelection() {
        InputMethodLocator inputMethodLocator;
        InputContext inputContext = this.currentInputContext;
        if (inputContext != null && (inputMethodLocator = inputContext.getInputMethodLocator()) != null) {
            return inputMethodLocator.getActionCommandString();
        }
        return null;
    }

    synchronized void changeInputMethod(String str) {
        String strSubstring;
        InputMethodLocator inputMethodLocatorDeriveLocator = null;
        String strSubstring2 = str;
        String strSubstring3 = null;
        int iIndexOf = str.indexOf(10);
        if (iIndexOf != -1) {
            strSubstring3 = str.substring(iIndexOf + 1);
            strSubstring2 = str.substring(0, iIndexOf);
        }
        if (this.hostAdapterLocator.getActionCommandString().equals(strSubstring2)) {
            inputMethodLocatorDeriveLocator = this.hostAdapterLocator;
        } else {
            int i2 = 0;
            while (true) {
                if (i2 >= this.javaInputMethodLocatorList.size()) {
                    break;
                }
                InputMethodLocator inputMethodLocator = this.javaInputMethodLocatorList.get(i2);
                if (!inputMethodLocator.getActionCommandString().equals(strSubstring2)) {
                    i2++;
                } else {
                    inputMethodLocatorDeriveLocator = inputMethodLocator;
                    break;
                }
            }
        }
        if (inputMethodLocatorDeriveLocator != null && strSubstring3 != null) {
            String strSubstring4 = "";
            String strSubstring5 = "";
            int iIndexOf2 = strSubstring3.indexOf(95);
            if (iIndexOf2 == -1) {
                strSubstring = strSubstring3;
            } else {
                strSubstring = strSubstring3.substring(0, iIndexOf2);
                int i3 = iIndexOf2 + 1;
                int iIndexOf3 = strSubstring3.indexOf(95, i3);
                if (iIndexOf3 == -1) {
                    strSubstring4 = strSubstring3.substring(i3);
                } else {
                    strSubstring4 = strSubstring3.substring(i3, iIndexOf3);
                    strSubstring5 = strSubstring3.substring(iIndexOf3 + 1);
                }
            }
            inputMethodLocatorDeriveLocator = inputMethodLocatorDeriveLocator.deriveLocator(new Locale(strSubstring, strSubstring4, strSubstring5));
        }
        if (inputMethodLocatorDeriveLocator != null && this.requestInputContext != null) {
            this.requestInputContext.changeInputMethod(inputMethodLocatorDeriveLocator);
            this.requestInputContext = null;
            putPreferredInputMethod(inputMethodLocatorDeriveLocator);
        }
    }

    @Override // sun.awt.im.InputMethodManager
    InputMethodLocator findInputMethod(Locale locale) {
        InputMethodLocator preferredInputMethod = getPreferredInputMethod(locale);
        if (preferredInputMethod != null) {
            return preferredInputMethod;
        }
        if (this.hostAdapterLocator != null && this.hostAdapterLocator.isLocaleAvailable(locale)) {
            return this.hostAdapterLocator.deriveLocator(locale);
        }
        initializeInputMethodLocatorList();
        for (int i2 = 0; i2 < this.javaInputMethodLocatorList.size(); i2++) {
            InputMethodLocator inputMethodLocator = this.javaInputMethodLocatorList.get(i2);
            if (inputMethodLocator.isLocaleAvailable(locale)) {
                return inputMethodLocator.deriveLocator(locale);
            }
        }
        return null;
    }

    @Override // sun.awt.im.InputMethodManager
    Locale getDefaultKeyboardLocale() {
        Object defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof InputMethodSupport) {
            return ((InputMethodSupport) defaultToolkit).getDefaultKeyboardLocale();
        }
        return Locale.getDefault();
    }

    private synchronized InputMethodLocator getPreferredInputMethod(Locale locale) {
        if (!hasMultipleInputMethods()) {
            return null;
        }
        InputMethodLocator inputMethodLocatorDeriveLocator = this.preferredLocatorCache.get(locale.toString().intern());
        if (inputMethodLocatorDeriveLocator != null) {
            return inputMethodLocatorDeriveLocator;
        }
        String strFindPreferredInputMethodNode = findPreferredInputMethodNode(locale);
        String preferredInputMethod = readPreferredInputMethod(strFindPreferredInputMethodNode);
        if (preferredInputMethod != null) {
            if (this.hostAdapterLocator != null && this.hostAdapterLocator.getDescriptor().getClass().getName().equals(preferredInputMethod)) {
                Locale advertisedLocale = getAdvertisedLocale(this.hostAdapterLocator, locale);
                if (advertisedLocale != null) {
                    inputMethodLocatorDeriveLocator = this.hostAdapterLocator.deriveLocator(advertisedLocale);
                    this.preferredLocatorCache.put(locale.toString().intern(), inputMethodLocatorDeriveLocator);
                }
                return inputMethodLocatorDeriveLocator;
            }
            for (int i2 = 0; i2 < this.javaInputMethodLocatorList.size(); i2++) {
                InputMethodLocator inputMethodLocator = this.javaInputMethodLocatorList.get(i2);
                if (inputMethodLocator.getDescriptor().getClass().getName().equals(preferredInputMethod)) {
                    Locale advertisedLocale2 = getAdvertisedLocale(inputMethodLocator, locale);
                    if (advertisedLocale2 != null) {
                        inputMethodLocatorDeriveLocator = inputMethodLocator.deriveLocator(advertisedLocale2);
                        this.preferredLocatorCache.put(locale.toString().intern(), inputMethodLocatorDeriveLocator);
                    }
                    return inputMethodLocatorDeriveLocator;
                }
            }
            writePreferredInputMethod(strFindPreferredInputMethodNode, null);
            return null;
        }
        return null;
    }

    private String findPreferredInputMethodNode(Locale locale) {
        if (this.userRoot == null) {
            return null;
        }
        String strSubstring = "/sun/awt/im/preferredInputMethod/" + createLocalePath(locale);
        while (true) {
            String str = strSubstring;
            if (!str.equals(preferredIMNode)) {
                if (this.userRoot.nodeExists(str) && readPreferredInputMethod(str) != null) {
                    return str;
                }
                strSubstring = str.substring(0, str.lastIndexOf(47));
            } else {
                return null;
            }
        }
    }

    private String readPreferredInputMethod(String str) {
        if (this.userRoot == null || str == null) {
            return null;
        }
        return this.userRoot.node(str).get(descriptorKey, null);
    }

    private synchronized void putPreferredInputMethod(InputMethodLocator inputMethodLocator) {
        InputMethodDescriptor descriptor = inputMethodLocator.getDescriptor();
        Locale locale = inputMethodLocator.getLocale();
        if (locale == null) {
            try {
                Locale[] availableLocales = descriptor.getAvailableLocales();
                if (availableLocales.length == 1) {
                    locale = availableLocales[0];
                } else {
                    return;
                }
            } catch (AWTException e2) {
                return;
            }
        }
        if (locale.equals(Locale.JAPAN)) {
            locale = Locale.JAPANESE;
        }
        if (locale.equals(Locale.KOREA)) {
            locale = Locale.KOREAN;
        }
        if (locale.equals(new Locale("th", "TH"))) {
            locale = new Locale("th");
        }
        writePreferredInputMethod("/sun/awt/im/preferredInputMethod/" + createLocalePath(locale), descriptor.getClass().getName());
        this.preferredLocatorCache.put(locale.toString().intern(), inputMethodLocator.deriveLocator(locale));
    }

    private String createLocalePath(Locale locale) {
        String str;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        if (!variant.equals("")) {
            str = "_" + language + "/_" + country + "/_" + variant;
        } else if (!country.equals("")) {
            str = "_" + language + "/_" + country;
        } else {
            str = "_" + language;
        }
        return str;
    }

    private void writePreferredInputMethod(String str, String str2) {
        if (this.userRoot != null) {
            Preferences preferencesNode = this.userRoot.node(str);
            if (str2 != null) {
                preferencesNode.put(descriptorKey, str2);
            } else {
                preferencesNode.remove(descriptorKey);
            }
        }
    }

    private Preferences getUserRoot() {
        return (Preferences) AccessController.doPrivileged(new PrivilegedAction<Preferences>() { // from class: sun.awt.im.ExecutableInputMethodManager.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Preferences run2() {
                return Preferences.userRoot();
            }
        });
    }

    private Locale getAdvertisedLocale(InputMethodLocator inputMethodLocator, Locale locale) {
        Locale locale2 = null;
        if (inputMethodLocator.isLocaleAvailable(locale)) {
            locale2 = locale;
        } else if (locale.getLanguage().equals("ja")) {
            if (inputMethodLocator.isLocaleAvailable(Locale.JAPAN)) {
                locale2 = Locale.JAPAN;
            } else if (inputMethodLocator.isLocaleAvailable(Locale.JAPANESE)) {
                locale2 = Locale.JAPANESE;
            }
        } else if (locale.getLanguage().equals("ko")) {
            if (inputMethodLocator.isLocaleAvailable(Locale.KOREA)) {
                locale2 = Locale.KOREA;
            } else if (inputMethodLocator.isLocaleAvailable(Locale.KOREAN)) {
                locale2 = Locale.KOREAN;
            }
        } else if (locale.getLanguage().equals("th")) {
            if (inputMethodLocator.isLocaleAvailable(new Locale("th", "TH"))) {
                locale2 = new Locale("th", "TH");
            } else if (inputMethodLocator.isLocaleAvailable(new Locale("th"))) {
                locale2 = new Locale("th");
            }
        }
        return locale2;
    }
}
