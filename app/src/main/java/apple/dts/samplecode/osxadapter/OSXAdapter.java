package apple.dts.samplecode.osxadapter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: icepdf-viewer.jar:apple/dts/samplecode/osxadapter/OSXAdapter.class */
public class OSXAdapter implements InvocationHandler {
    private static final Logger logger = Logger.getLogger(OSXAdapter.class.toString());
    protected Object targetObject;
    protected Method targetMethod;
    protected String proxySignature;
    static Object macOSXApplication;

    public static void setQuitHandler(Object target, Method quitHandler) {
        setHandler(new OSXAdapter("handleQuit", target, quitHandler));
    }

    public static void setAboutHandler(Object target, Method aboutHandler) {
        boolean enableAboutMenu = (target == null || aboutHandler == null) ? false : true;
        if (enableAboutMenu) {
            setHandler(new OSXAdapter("handleAbout", target, aboutHandler));
        }
        try {
            Method enableAboutMethod = macOSXApplication.getClass().getDeclaredMethod("setEnabledAboutMenu", Boolean.TYPE);
            enableAboutMethod.invoke(macOSXApplication, Boolean.valueOf(enableAboutMenu));
        } catch (Exception ex) {
            logger.log(Level.FINE, "OSXAdapter could not access the About Menu", (Throwable) ex);
        }
    }

    public static void setPreferencesHandler(Object target, Method prefsHandler) {
        boolean enablePrefsMenu = (target == null || prefsHandler == null) ? false : true;
        if (enablePrefsMenu) {
            setHandler(new OSXAdapter("handlePreferences", target, prefsHandler));
        }
        try {
            Method enablePrefsMethod = macOSXApplication.getClass().getDeclaredMethod("setEnabledPreferencesMenu", Boolean.TYPE);
            enablePrefsMethod.invoke(macOSXApplication, Boolean.valueOf(enablePrefsMenu));
        } catch (Exception ex) {
            logger.log(Level.FINE, "OSXAdapter could not access the About Menu", (Throwable) ex);
        }
    }

    public static void setFileHandler(Object target, Method fileHandler) {
        setHandler(new OSXAdapter("handleOpenFile", target, fileHandler) { // from class: apple.dts.samplecode.osxadapter.OSXAdapter.1
            @Override // apple.dts.samplecode.osxadapter.OSXAdapter
            public boolean callTarget(Object appleEvent) {
                if (appleEvent != null) {
                    try {
                        Method getFilenameMethod = appleEvent.getClass().getDeclaredMethod("getFilename", (Class[]) null);
                        String filename = (String) getFilenameMethod.invoke(appleEvent, (Object[]) null);
                        this.targetMethod.invoke(this.targetObject, filename);
                        return true;
                    } catch (Exception e2) {
                        return true;
                    }
                }
                return true;
            }
        });
    }

    public static void setHandler(OSXAdapter adapter) {
        try {
            Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
            if (macOSXApplication == null) {
                macOSXApplication = applicationClass.getConstructor((Class[]) null).newInstance((Object[]) null);
            }
            Class applicationListenerClass = Class.forName("com.apple.eawt.ApplicationListener");
            Method addListenerMethod = applicationClass.getDeclaredMethod("addApplicationListener", applicationListenerClass);
            Object osxAdapterProxy = Proxy.newProxyInstance(OSXAdapter.class.getClassLoader(), new Class[]{applicationListenerClass}, adapter);
            addListenerMethod.invoke(macOSXApplication, osxAdapterProxy);
        } catch (ClassNotFoundException cnfe) {
            System.err.println("This version of Mac OS X does not support the Apple EAWT.  ApplicationEvent handling has been disabled (" + ((Object) cnfe) + ")");
        } catch (Exception ex) {
            logger.log(Level.FINE, "Mac OS X Adapter could not talk to EAWT", (Throwable) ex);
        }
    }

    protected OSXAdapter(String proxySignature, Object target, Method handler) {
        this.proxySignature = proxySignature;
        this.targetObject = target;
        this.targetMethod = handler;
    }

    public boolean callTarget(Object appleEvent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object result = this.targetMethod.invoke(this.targetObject, (Object[]) null);
        if (result == null) {
            return true;
        }
        return Boolean.valueOf(result.toString()).booleanValue();
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isCorrectMethod(method, args)) {
            boolean handled = callTarget(args[0]);
            setApplicationEventHandled(args[0], handled);
            return null;
        }
        return null;
    }

    protected boolean isCorrectMethod(Method method, Object[] args) {
        return this.targetMethod != null && this.proxySignature.equals(method.getName()) && args.length == 1;
    }

    protected void setApplicationEventHandled(Object event, boolean handled) {
        if (event != null) {
            try {
                Method setHandledMethod = event.getClass().getDeclaredMethod("setHandled", Boolean.TYPE);
                setHandledMethod.invoke(event, Boolean.valueOf(handled));
            } catch (Exception ex) {
                logger.log(Level.FINE, "OSXAdapter was unable to handle an ApplicationEvent", (Throwable) ex);
            }
        }
    }
}
