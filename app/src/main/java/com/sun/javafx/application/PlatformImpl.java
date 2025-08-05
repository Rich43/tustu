package com.sun.javafx.application;

import com.sun.glass.ui.Application;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.runtime.SystemProperties;
import com.sun.javafx.tk.TKListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AllPermission;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.ConditionalFeature;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javax.print.ServiceUIFactory;
import org.icepdf.core.pobjects.graphics.Separation;

/* loaded from: jfxrt.jar:com/sun/javafx/application/PlatformImpl.class */
public class PlatformImpl {
    private static boolean contextual2DNavigation;
    private static Boolean isGraphicsSupported;
    private static Boolean isControlsSupported;
    private static Boolean isMediaSupported;
    private static Boolean isWebSupported;
    private static Boolean isSWTSupported;
    private static Boolean isSwingSupported;
    private static Boolean isFXMLSupported;
    private static Boolean hasTwoLevelFocus;
    private static Boolean hasVirtualKeyboard;
    private static Boolean hasTouch;
    private static Boolean hasMultiTouch;
    private static Boolean hasPointer;
    private static String accessibilityTheme;
    private static AtomicBoolean initialized = new AtomicBoolean(false);
    private static AtomicBoolean platformExit = new AtomicBoolean(false);
    private static AtomicBoolean toolkitExit = new AtomicBoolean(false);
    private static CountDownLatch startupLatch = new CountDownLatch(1);
    private static AtomicBoolean listenersRegistered = new AtomicBoolean(false);
    private static TKListener toolkitListener = null;
    private static volatile boolean implicitExit = true;
    private static boolean taskbarApplication = true;
    private static AtomicInteger pendingRunnables = new AtomicInteger(0);
    private static AtomicInteger numWindows = new AtomicInteger(0);
    private static volatile boolean firstWindowShown = false;
    private static volatile boolean lastWindowClosed = false;
    private static AtomicBoolean reallyIdle = new AtomicBoolean(false);
    private static Set<FinishListener> finishListeners = new CopyOnWriteArraySet();
    private static final Object runLaterLock = new Object();
    private static boolean isThreadMerged = false;
    private static BooleanProperty accessibilityActive = new SimpleBooleanProperty();
    private static final CountDownLatch platformExitLatch = new CountDownLatch(1);
    private static boolean isModena = false;
    private static boolean isCaspian = false;

    /* loaded from: jfxrt.jar:com/sun/javafx/application/PlatformImpl$FinishListener.class */
    public interface FinishListener {
        void idle(boolean z2);

        void exitCalled();
    }

    public static void setTaskbarApplication(boolean taskbarApplication2) {
        taskbarApplication = taskbarApplication2;
    }

    public static boolean isTaskbarApplication() {
        return taskbarApplication;
    }

    public static void setApplicationName(Class appClass) {
        runLater(() -> {
            Application.GetApplication().setName(appClass.getName());
        });
    }

    public static boolean isContextual2DNavigation() {
        return contextual2DNavigation;
    }

    public static void startup(Runnable r2) {
        if (platformExit.get()) {
            throw new IllegalStateException("Platform.exit has been called");
        }
        if (initialized.getAndSet(true)) {
            runLater(r2);
            return;
        }
        AccessController.doPrivileged(() -> {
            contextual2DNavigation = Boolean.getBoolean("com.sun.javafx.isContextual2DNavigation");
            String s2 = System.getProperty("com.sun.javafx.twoLevelFocus");
            if (s2 != null) {
                hasTwoLevelFocus = Boolean.valueOf(s2);
            }
            String s3 = System.getProperty("com.sun.javafx.virtualKeyboard");
            if (s3 != null) {
                if (s3.equalsIgnoreCase(Separation.COLORANT_NONE)) {
                    hasVirtualKeyboard = false;
                } else if (s3.equalsIgnoreCase("javafx") || s3.equalsIgnoreCase("native")) {
                    hasVirtualKeyboard = true;
                }
            }
            String s4 = System.getProperty("com.sun.javafx.touch");
            if (s4 != null) {
                hasTouch = Boolean.valueOf(s4);
            }
            String s5 = System.getProperty("com.sun.javafx.multiTouch");
            if (s5 != null) {
                hasMultiTouch = Boolean.valueOf(s5);
            }
            String s6 = System.getProperty("com.sun.javafx.pointer");
            if (s6 != null) {
                hasPointer = Boolean.valueOf(s6);
            }
            String s7 = System.getProperty("javafx.embed.singleThread");
            if (s7 != null) {
                isThreadMerged = Boolean.valueOf(s7).booleanValue();
                return null;
            }
            return null;
        });
        if (!taskbarApplication) {
            AccessController.doPrivileged(() -> {
                System.setProperty("glass.taskbarApplication", "false");
                return null;
            });
        }
        toolkitListener = new TKListener() { // from class: com.sun.javafx.application.PlatformImpl.1
            @Override // com.sun.javafx.tk.TKListener
            public void changedTopLevelWindows(List<TKStage> windows) {
                PlatformImpl.numWindows.set(windows.size());
                PlatformImpl.checkIdle();
            }

            @Override // com.sun.javafx.tk.TKListener
            public void exitedLastNestedLoop() {
                PlatformImpl.checkIdle();
            }
        };
        Toolkit.getToolkit().addTkListener(toolkitListener);
        Toolkit.getToolkit().startup(() -> {
            startupLatch.countDown();
            r2.run();
        });
        if (isThreadMerged) {
            installFwEventQueue();
        }
    }

    private static void installFwEventQueue() {
        invokeSwingFXUtilsMethod("installFwEventQueue");
    }

    private static void removeFwEventQueue() {
        invokeSwingFXUtilsMethod("removeFwEventQueue");
    }

    private static void invokeSwingFXUtilsMethod(String methodName) {
        try {
            Class swingFXUtilsClass = Class.forName("javafx.embed.swing.SwingFXUtils");
            Method installFwEventQueue = swingFXUtilsClass.getDeclaredMethod(methodName, new Class[0]);
            AccessController.doPrivileged(() -> {
                installFwEventQueue.setAccessible(true);
                return null;
            });
            waitForStart();
            installFwEventQueue.invoke(null, new Object[0]);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException e2) {
            throw new RuntimeException("Property javafx.embed.singleThread is not supported");
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    private static void waitForStart() {
        if (startupLatch.getCount() > 0) {
            try {
                startupLatch.await();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean isFxApplicationThread() {
        return Toolkit.getToolkit().isFxUserThread();
    }

    public static void runLater(Runnable r2) {
        runLater(r2, false);
    }

    private static void runLater(Runnable r2, boolean exiting) {
        if (!initialized.get()) {
            throw new IllegalStateException("Toolkit not initialized");
        }
        pendingRunnables.incrementAndGet();
        waitForStart();
        if (SystemProperties.isDebug()) {
            Toolkit.getToolkit().pauseCurrentThread();
        }
        synchronized (runLaterLock) {
            if (!exiting) {
                if (toolkitExit.get()) {
                    pendingRunnables.decrementAndGet();
                    return;
                }
            }
            AccessControlContext acc = AccessController.getContext();
            Toolkit.getToolkit().defer(() -> {
                try {
                    AccessController.doPrivileged(() -> {
                        r2.run();
                        return null;
                    }, acc);
                    pendingRunnables.decrementAndGet();
                    checkIdle();
                } catch (Throwable th) {
                    pendingRunnables.decrementAndGet();
                    checkIdle();
                    throw th;
                }
            });
        }
    }

    public static void runAndWait(Runnable r2) {
        runAndWait(r2, false);
    }

    private static void runAndWait(Runnable r2, boolean exiting) {
        if (SystemProperties.isDebug()) {
            Toolkit.getToolkit().pauseCurrentThread();
        }
        if (isFxApplicationThread()) {
            try {
                r2.run();
                return;
            } catch (Throwable t2) {
                System.err.println("Exception in runnable");
                t2.printStackTrace();
                return;
            }
        }
        CountDownLatch doneLatch = new CountDownLatch(1);
        runLater(() -> {
            try {
                r2.run();
            } finally {
                doneLatch.countDown();
            }
        }, exiting);
        if (!exiting && toolkitExit.get()) {
            throw new IllegalStateException("Toolkit has exited");
        }
        try {
            doneLatch.await();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void setImplicitExit(boolean implicitExit2) {
        implicitExit = implicitExit2;
        checkIdle();
    }

    public static boolean isImplicitExit() {
        return implicitExit;
    }

    public static void addListener(FinishListener l2) {
        listenersRegistered.set(true);
        finishListeners.add(l2);
    }

    public static void removeListener(FinishListener l2) {
        finishListeners.remove(l2);
        listenersRegistered.set(!finishListeners.isEmpty());
        if (!listenersRegistered.get()) {
            checkIdle();
        }
    }

    private static void notifyFinishListeners(boolean exitCalled) {
        if (listenersRegistered.get()) {
            for (FinishListener l2 : finishListeners) {
                if (exitCalled) {
                    l2.exitCalled();
                } else {
                    l2.idle(implicitExit);
                }
            }
            return;
        }
        if (implicitExit || platformExit.get()) {
            tkExit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkIdle() {
        if (!initialized.get()) {
            return;
        }
        if (!isFxApplicationThread()) {
            runLater(() -> {
            });
            return;
        }
        boolean doNotify = false;
        synchronized (PlatformImpl.class) {
            int numWin = numWindows.get();
            if (numWin > 0) {
                firstWindowShown = true;
                lastWindowClosed = false;
                reallyIdle.set(false);
            } else if (numWin == 0 && firstWindowShown) {
                lastWindowClosed = true;
            }
            if (lastWindowClosed && pendingRunnables.get() == 0 && (toolkitExit.get() || !Toolkit.getToolkit().isNestedLoopRunning())) {
                if (reallyIdle.getAndSet(true)) {
                    doNotify = true;
                    lastWindowClosed = false;
                } else {
                    runLater(() -> {
                    });
                }
            }
        }
        if (doNotify) {
            notifyFinishListeners(false);
        }
    }

    static CountDownLatch test_getPlatformExitLatch() {
        return platformExitLatch;
    }

    public static void tkExit() {
        if (!toolkitExit.getAndSet(true) && initialized.get()) {
            runAndWait(() -> {
                Toolkit.getToolkit().exit();
            }, true);
            if (isThreadMerged) {
                removeFwEventQueue();
            }
            Toolkit.getToolkit().removeTkListener(toolkitListener);
            toolkitListener = null;
            platformExitLatch.countDown();
        }
    }

    public static BooleanProperty accessibilityActiveProperty() {
        return accessibilityActive;
    }

    public static void exit() {
        platformExit.set(true);
        notifyFinishListeners(true);
    }

    private static Boolean checkForClass(String classname) {
        try {
            Class.forName(classname, false, PlatformImpl.class.getClassLoader());
            return Boolean.TRUE;
        } catch (ClassNotFoundException e2) {
            return Boolean.FALSE;
        }
    }

    public static boolean isSupported(ConditionalFeature feature) {
        boolean supported = isSupportedImpl(feature);
        if (supported && feature == ConditionalFeature.TRANSPARENT_WINDOW) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                try {
                    securityManager.checkPermission(new AllPermission());
                    return true;
                } catch (SecurityException e2) {
                    return false;
                }
            }
            return true;
        }
        return supported;
    }

    public static void setDefaultPlatformUserAgentStylesheet() {
        setPlatformUserAgentStylesheet(javafx.application.Application.STYLESHEET_MODENA);
    }

    public static boolean isModena() {
        return isModena;
    }

    public static boolean isCaspian() {
        return isCaspian;
    }

    public static void setPlatformUserAgentStylesheet(String stylesheetUrl) {
        if (isFxApplicationThread()) {
            _setPlatformUserAgentStylesheet(stylesheetUrl);
        } else {
            runLater(() -> {
                _setPlatformUserAgentStylesheet(stylesheetUrl);
            });
        }
    }

    public static boolean setAccessibilityTheme(String platformTheme) {
        if (accessibilityTheme != null) {
            StyleManager.getInstance().removeUserAgentStylesheet(accessibilityTheme);
            accessibilityTheme = null;
        }
        _setAccessibilityTheme(platformTheme);
        if (accessibilityTheme != null) {
            StyleManager.getInstance().addUserAgentStylesheet(accessibilityTheme);
            return true;
        }
        return false;
    }

    private static void _setAccessibilityTheme(String platformTheme) {
        String userTheme = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("com.sun.javafx.highContrastTheme");
        });
        if (isCaspian()) {
            if (platformTheme != null || userTheme != null) {
                accessibilityTheme = "com/sun/javafx/scene/control/skin/caspian/highcontrast.css";
                return;
            }
        }
        if (isModena()) {
            if (userTheme != null) {
                switch (userTheme.toUpperCase()) {
                    case "BLACKONWHITE":
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/blackOnWhite.css";
                        break;
                    case "WHITEONBLACK":
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/whiteOnBlack.css";
                        break;
                    case "YELLOWONBLACK":
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/yellowOnBlack.css";
                        break;
                }
            }
            if (platformTheme != null) {
                switch (platformTheme) {
                    case "High Contrast White":
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/blackOnWhite.css";
                        break;
                    case "High Contrast Black":
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/whiteOnBlack.css";
                        break;
                    case "High Contrast #1":
                    case "High Contrast #2":
                        accessibilityTheme = "com/sun/javafx/scene/control/skin/modena/yellowOnBlack.css";
                        break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void _setPlatformUserAgentStylesheet(String stylesheetUrl) {
        isCaspian = false;
        isModena = false;
        String overrideStylesheetUrl = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("javafx.userAgentStylesheetUrl");
        });
        if (overrideStylesheetUrl != null) {
            stylesheetUrl = overrideStylesheetUrl;
        }
        List<String> uaStylesheets = new ArrayList<>();
        if (javafx.application.Application.STYLESHEET_CASPIAN.equalsIgnoreCase(stylesheetUrl)) {
            isCaspian = true;
            uaStylesheets.add("com/sun/javafx/scene/control/skin/caspian/caspian.css");
            if (isSupported(ConditionalFeature.INPUT_TOUCH)) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/caspian/embedded.css");
                if (Utils.isQVGAScreen()) {
                    uaStylesheets.add("com/sun/javafx/scene/control/skin/caspian/embedded-qvga.css");
                }
                if (PlatformUtil.isAndroid()) {
                    uaStylesheets.add("com/sun/javafx/scene/control/skin/caspian/android.css");
                }
            }
            if (isSupported(ConditionalFeature.TWO_LEVEL_FOCUS)) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/caspian/two-level-focus.css");
            }
            if (isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/caspian/fxvk.css");
            }
            if (!isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/caspian/caspian-no-transparency.css");
            }
        } else if (javafx.application.Application.STYLESHEET_MODENA.equalsIgnoreCase(stylesheetUrl)) {
            isModena = true;
            uaStylesheets.add("com/sun/javafx/scene/control/skin/modena/modena.css");
            if (isSupported(ConditionalFeature.INPUT_TOUCH)) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/modena/touch.css");
            }
            if (PlatformUtil.isEmbedded()) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/modena/modena-embedded-performance.css");
            }
            if (PlatformUtil.isAndroid()) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/modena/android.css");
            }
            if (isSupported(ConditionalFeature.TWO_LEVEL_FOCUS)) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/modena/two-level-focus.css");
            }
            if (isSupported(ConditionalFeature.VIRTUAL_KEYBOARD)) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/caspian/fxvk.css");
            }
            if (!isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
                uaStylesheets.add("com/sun/javafx/scene/control/skin/modena/modena-no-transparency.css");
            }
        } else {
            uaStylesheets.add(stylesheetUrl);
        }
        _setAccessibilityTheme(Toolkit.getToolkit().getThemeName());
        if (accessibilityTheme != null) {
            uaStylesheets.add(accessibilityTheme);
        }
        AccessController.doPrivileged(() -> {
            StyleManager.getInstance().setUserAgentStylesheets(uaStylesheets);
            return null;
        });
    }

    public static void addNoTransparencyStylesheetToScene(Scene scene) {
        if (isCaspian()) {
            AccessController.doPrivileged(() -> {
                StyleManager.getInstance().addUserAgentStylesheet(scene, "com/sun/javafx/scene/control/skin/caspian/caspian-no-transparency.css");
                return null;
            });
        } else if (isModena()) {
            AccessController.doPrivileged(() -> {
                StyleManager.getInstance().addUserAgentStylesheet(scene, "com/sun/javafx/scene/control/skin/modena/modena-no-transparency.css");
                return null;
            });
        }
    }

    private static boolean isSupportedImpl(ConditionalFeature feature) {
        switch (feature) {
            case GRAPHICS:
                if (isGraphicsSupported == null) {
                    isGraphicsSupported = checkForClass("javafx.stage.Stage");
                }
                return isGraphicsSupported.booleanValue();
            case CONTROLS:
                if (isControlsSupported == null) {
                    isControlsSupported = checkForClass("javafx.scene.control.Control");
                }
                return isControlsSupported.booleanValue();
            case MEDIA:
                if (isMediaSupported == null) {
                    isMediaSupported = checkForClass("javafx.scene.media.MediaView");
                    if (isMediaSupported.booleanValue() && PlatformUtil.isEmbedded()) {
                        AccessController.doPrivileged(() -> {
                            String s2 = System.getProperty("com.sun.javafx.experimental.embedded.media", "false");
                            isMediaSupported = Boolean.valueOf(s2);
                            return null;
                        });
                    }
                }
                return isMediaSupported.booleanValue();
            case WEB:
                if (isWebSupported == null) {
                    isWebSupported = checkForClass("javafx.scene.web.WebView");
                    if (isWebSupported.booleanValue() && PlatformUtil.isEmbedded()) {
                        AccessController.doPrivileged(() -> {
                            String s2 = System.getProperty("com.sun.javafx.experimental.embedded.web", "false");
                            isWebSupported = Boolean.valueOf(s2);
                            return null;
                        });
                    }
                }
                return isWebSupported.booleanValue();
            case SWT:
                if (isSWTSupported == null) {
                    isSWTSupported = checkForClass("javafx.embed.swt.FXCanvas");
                }
                return isSWTSupported.booleanValue();
            case SWING:
                if (isSwingSupported == null) {
                    isSwingSupported = Boolean.valueOf(checkForClass(ServiceUIFactory.JCOMPONENT_UI).booleanValue() && checkForClass("javafx.embed.swing.JFXPanel").booleanValue());
                }
                return isSwingSupported.booleanValue();
            case FXML:
                if (isFXMLSupported == null) {
                    isFXMLSupported = Boolean.valueOf(checkForClass("javafx.fxml.FXMLLoader").booleanValue() && checkForClass("javax.xml.stream.XMLInputFactory").booleanValue());
                }
                return isFXMLSupported.booleanValue();
            case TWO_LEVEL_FOCUS:
                if (hasTwoLevelFocus == null) {
                    return Toolkit.getToolkit().isSupported(feature);
                }
                return hasTwoLevelFocus.booleanValue();
            case VIRTUAL_KEYBOARD:
                if (hasVirtualKeyboard == null) {
                    return Toolkit.getToolkit().isSupported(feature);
                }
                return hasVirtualKeyboard.booleanValue();
            case INPUT_TOUCH:
                if (hasTouch == null) {
                    return Toolkit.getToolkit().isSupported(feature);
                }
                return hasTouch.booleanValue();
            case INPUT_MULTITOUCH:
                if (hasMultiTouch == null) {
                    return Toolkit.getToolkit().isSupported(feature);
                }
                return hasMultiTouch.booleanValue();
            case INPUT_POINTER:
                if (hasPointer == null) {
                    return Toolkit.getToolkit().isSupported(feature);
                }
                return hasPointer.booleanValue();
            default:
                return Toolkit.getToolkit().isSupported(feature);
        }
    }
}
