package com.sun.javafx.application;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.jmx.MXExtension;
import com.sun.javafx.runtime.SystemProperties;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.stage.Stage;

/* loaded from: jfxrt.jar:com/sun/javafx/application/LauncherImpl.class */
public class LauncherImpl {
    public static final String LAUNCH_MODE_CLASS = "LM_CLASS";
    public static final String LAUNCH_MODE_JAR = "LM_JAR";
    private static final boolean trace = false;
    private static final String MF_MAIN_CLASS = "Main-Class";
    private static final String MF_JAVAFX_MAIN = "JavaFX-Application-Class";
    private static final String MF_JAVAFX_PRELOADER = "JavaFX-Preloader-Class";
    private static final String MF_JAVAFX_CLASS_PATH = "JavaFX-Class-Path";
    private static final String MF_JAVAFX_ARGUMENT_PREFIX = "JavaFX-Argument-";
    private static final String MF_JAVAFX_PARAMETER_NAME_PREFIX = "JavaFX-Parameter-Name-";
    private static final String MF_JAVAFX_PARAMETER_VALUE_PREFIX = "JavaFX-Parameter-Value-";
    private static final boolean simulateSlowProgress = false;
    private static boolean verbose = false;
    private static AtomicBoolean launchCalled = new AtomicBoolean(false);
    private static final AtomicBoolean toolkitStarted = new AtomicBoolean(false);
    private static volatile RuntimeException launchException = null;
    private static Preloader currentPreloader = null;
    private static Class<? extends Preloader> savedPreloaderClass = null;
    private static ClassLoader savedMainCcl = null;
    private static volatile boolean error = false;
    private static volatile Throwable pConstructorError = null;
    private static volatile Throwable pInitError = null;
    private static volatile Throwable pStartError = null;
    private static volatile Throwable pStopError = null;
    private static volatile Throwable constructorError = null;
    private static volatile Throwable initError = null;
    private static volatile Throwable startError = null;
    private static volatile Throwable stopError = null;

    /* JADX WARN: Multi-variable type inference failed */
    public static void launchApplication(Class<? extends Application> appClass, String[] args) {
        String preloaderByProperty;
        Class preloaderClass = savedPreloaderClass;
        if (preloaderClass == null && (preloaderByProperty = (String) AccessController.doPrivileged(() -> {
            return System.getProperty("javafx.preloader");
        })) != null) {
            try {
                preloaderClass = Class.forName(preloaderByProperty, false, appClass.getClassLoader());
            } catch (Exception e2) {
                System.err.printf("Could not load preloader class '" + preloaderByProperty + "', continuing without preloader.", new Object[0]);
                e2.printStackTrace();
            }
        }
        launchApplication(appClass, (Class<? extends Preloader>) preloaderClass, args);
    }

    public static void launchApplication(Class<? extends Application> appClass, Class<? extends Preloader> preloaderClass, String[] args) {
        if (launchCalled.getAndSet(true)) {
            throw new IllegalStateException("Application launch must not be called more than once");
        }
        if (!Application.class.isAssignableFrom(appClass)) {
            throw new IllegalArgumentException("Error: " + appClass.getName() + " is not a subclass of javafx.application.Application");
        }
        if (preloaderClass != null && !Preloader.class.isAssignableFrom(preloaderClass)) {
            throw new IllegalArgumentException("Error: " + preloaderClass.getName() + " is not a subclass of javafx.application.Preloader");
        }
        CountDownLatch launchLatch = new CountDownLatch(1);
        Thread launcherThread = new Thread(() -> {
            try {
                try {
                    try {
                        launchApplication1(appClass, preloaderClass, args);
                        launchLatch.countDown();
                    } catch (RuntimeException rte) {
                        launchException = rte;
                        launchLatch.countDown();
                    } catch (Exception ex) {
                        launchException = new RuntimeException("Application launch exception", ex);
                        launchLatch.countDown();
                    }
                } catch (Error err) {
                    launchException = new RuntimeException("Application launch error", err);
                    launchLatch.countDown();
                }
            } catch (Throwable th) {
                launchLatch.countDown();
                throw th;
            }
        });
        launcherThread.setName("JavaFX-Launcher");
        launcherThread.start();
        try {
            launchLatch.await();
            if (launchException != null) {
                throw launchException;
            }
        } catch (InterruptedException ex) {
            throw new RuntimeException("Unexpected exception: ", ex);
        }
    }

    public static void launchApplication(String launchName, String launchMode, String[] args) {
        if (verbose) {
            System.err.println("Java 8 launchApplication method: launchMode=" + launchMode);
        }
        String mainClassName = null;
        String preloaderClassName = null;
        String[] appArgs = args;
        ClassLoader appLoader = null;
        verbose = Boolean.getBoolean("javafx.verbose");
        if (launchMode.equals(LAUNCH_MODE_JAR)) {
            Attributes jarAttrs = getJarAttributes(launchName);
            if (jarAttrs == null) {
                abort(null, "Can't get manifest attributes from jar", new Object[0]);
            }
            String fxClassPath = jarAttrs.getValue(MF_JAVAFX_CLASS_PATH);
            if (fxClassPath != null && fxClassPath.trim().length() != 0) {
                if (verbose) {
                    System.err.println("WARNING: Application jar uses deprecated JavaFX-Class-Path attribute. Please use Class-Path instead.");
                }
                appLoader = setupJavaFXClassLoader(new File(launchName), fxClassPath);
            }
            if (args.length == 0) {
                appArgs = getAppArguments(jarAttrs);
            }
            String mainClassName2 = jarAttrs.getValue(MF_JAVAFX_MAIN);
            if (mainClassName2 == null) {
                mainClassName2 = jarAttrs.getValue(MF_MAIN_CLASS);
                if (mainClassName2 == null) {
                    abort(null, "JavaFX jar manifest requires a valid JavaFX-Appliation-Class or Main-Class entry", new Object[0]);
                }
            }
            mainClassName = mainClassName2.trim();
            preloaderClassName = jarAttrs.getValue(MF_JAVAFX_PRELOADER);
            if (preloaderClassName != null) {
                preloaderClassName = preloaderClassName.trim();
            }
        } else if (launchMode.equals(LAUNCH_MODE_CLASS)) {
            mainClassName = launchName;
            preloaderClassName = System.getProperty("javafx.preloader");
        } else {
            abort(new IllegalArgumentException("The launchMode argument must be one of LM_CLASS or LM_JAR"), "Invalid launch mode: %1$s", launchMode);
        }
        if (mainClassName == null) {
            abort(null, "No main JavaFX class to launch", new Object[0]);
        }
        if (appLoader != null) {
            try {
                Class<?> launcherClass = appLoader.loadClass(LauncherImpl.class.getName());
                Method lawa = launcherClass.getMethod("launchApplicationWithArgs", String.class, String.class, new String[0].getClass());
                Thread.currentThread().setContextClassLoader(appLoader);
                lawa.invoke(null, mainClassName, preloaderClassName, appArgs);
                return;
            } catch (Exception e2) {
                abort(e2, "Exception while launching application", new Object[0]);
                return;
            }
        }
        launchApplicationWithArgs(mainClassName, preloaderClassName, appArgs);
    }

    public static void launchApplicationWithArgs(String mainClassName, String preloaderClassName, String[] args) {
        try {
            startToolkit();
        } catch (InterruptedException ex) {
            abort(ex, "Toolkit initialization error", mainClassName);
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        AtomicReference<Class<?>> tmpClassRef = new AtomicReference<>();
        AtomicReference<Class<? extends Preloader>> preClassRef = new AtomicReference<>();
        PlatformImpl.runAndWait(() -> {
            Class<?> clz = null;
            try {
                clz = Class.forName(mainClassName, true, loader);
            } catch (ClassNotFoundException cnfe) {
                abort(cnfe, "Missing JavaFX application class %1$s", mainClassName);
            }
            tmpClassRef.set(clz);
            if (preloaderClassName != null) {
                try {
                    clz = Class.forName(preloaderClassName, true, loader);
                } catch (ClassNotFoundException cnfe2) {
                    abort(cnfe2, "Missing JavaFX preloader class %1$s", preloaderClassName);
                }
                if (!Preloader.class.isAssignableFrom(clz)) {
                    abort(null, "JavaFX preloader class %1$s does not extend javafx.application.Preloader", clz.getName());
                }
                preClassRef.set(clz.asSubclass(Preloader.class));
            }
        });
        Class<? extends Preloader> preClass = preClassRef.get();
        Class<?> tempAppClass = tmpClassRef.get();
        savedPreloaderClass = preClass;
        try {
            Method mainMethod = tempAppClass.getMethod("main", new String[0].getClass());
            if (verbose) {
                System.err.println("Calling main(String[]) method");
            }
            savedMainCcl = Thread.currentThread().getContextClassLoader();
            mainMethod.invoke(null, args);
        } catch (IllegalAccessException | NoSuchMethodException ex2) {
            savedPreloaderClass = null;
            if (!Application.class.isAssignableFrom(tempAppClass)) {
                abort(ex2, "JavaFX application class %1$s does not extend javafx.application.Application", tempAppClass.getName());
            }
            Class clsAsSubclass = tempAppClass.asSubclass(Application.class);
            if (verbose) {
                System.err.println("Launching application directly");
            }
            launchApplication((Class<? extends Application>) clsAsSubclass, preClass, args);
        } catch (InvocationTargetException ex3) {
            ex3.printStackTrace();
            abort(null, "Exception running application %1$s", tempAppClass.getName());
        }
    }

    private static URL fileToURL(File file) throws IOException {
        return file.getCanonicalFile().toURI().toURL();
    }

    private static ClassLoader setupJavaFXClassLoader(File appJar, String fxClassPath) {
        try {
            File baseDir = appJar.getParentFile();
            ArrayList jcpList = new ArrayList();
            String cp = fxClassPath;
            if (cp != null) {
                while (true) {
                    if (cp.length() <= 0) {
                        break;
                    }
                    int pathSepIdx = cp.indexOf(" ");
                    if (pathSepIdx < 0) {
                        String pathElem = cp;
                        File f2 = baseDir == null ? new File(pathElem) : new File(baseDir, pathElem);
                        if (f2.exists()) {
                            jcpList.add(fileToURL(f2));
                        } else if (verbose) {
                            System.err.println("Class Path entry \"" + pathElem + "\" does not exist, ignoring");
                        }
                    } else {
                        if (pathSepIdx > 0) {
                            String pathElem2 = cp.substring(0, pathSepIdx);
                            File f3 = baseDir == null ? new File(pathElem2) : new File(baseDir, pathElem2);
                            if (f3.exists()) {
                                jcpList.add(fileToURL(f3));
                            } else if (verbose) {
                                System.err.println("Class Path entry \"" + pathElem2 + "\" does not exist, ignoring");
                            }
                        }
                        cp = cp.substring(pathSepIdx + 1);
                    }
                }
            }
            if (!jcpList.isEmpty()) {
                ArrayList<URL> urlList = new ArrayList<>();
                String cp2 = System.getProperty("java.class.path");
                if (cp2 != null) {
                    while (true) {
                        if (cp2.length() <= 0) {
                            break;
                        }
                        int pathSepIdx2 = cp2.indexOf(File.pathSeparatorChar);
                        if (pathSepIdx2 < 0) {
                            String pathElem3 = cp2;
                            urlList.add(fileToURL(new File(pathElem3)));
                            break;
                        }
                        if (pathSepIdx2 > 0) {
                            String pathElem4 = cp2.substring(0, pathSepIdx2);
                            urlList.add(fileToURL(new File(pathElem4)));
                        }
                        cp2 = cp2.substring(pathSepIdx2 + 1);
                    }
                }
                URL jfxRtURL = LauncherImpl.class.getProtectionDomain().getCodeSource().getLocation();
                urlList.add(jfxRtURL);
                urlList.addAll(jcpList);
                URL[] urls = (URL[]) urlList.toArray(new URL[0]);
                if (verbose) {
                    System.err.println("===== URL list");
                    for (URL url : urls) {
                        System.err.println("" + ((Object) url));
                    }
                    System.err.println("=====");
                }
                return new URLClassLoader(urls, (ClassLoader) null);
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    private static String decodeBase64(String inp) throws IOException {
        return new String(Base64.getDecoder().decode(inp));
    }

    private static String[] getAppArguments(Attributes attrs) {
        List args = new LinkedList();
        for (int idx = 1; attrs.getValue(MF_JAVAFX_ARGUMENT_PREFIX + idx) != null; idx++) {
            try {
                args.add(decodeBase64(attrs.getValue(MF_JAVAFX_ARGUMENT_PREFIX + idx)));
            } catch (IOException ioe) {
                if (verbose) {
                    System.err.println("Failed to extract application parameters");
                }
                ioe.printStackTrace();
            }
        }
        for (int idx2 = 1; attrs.getValue(MF_JAVAFX_PARAMETER_NAME_PREFIX + idx2) != null; idx2++) {
            String k2 = decodeBase64(attrs.getValue(MF_JAVAFX_PARAMETER_NAME_PREFIX + idx2));
            String v2 = null;
            if (attrs.getValue(MF_JAVAFX_PARAMETER_VALUE_PREFIX + idx2) != null) {
                v2 = decodeBase64(attrs.getValue(MF_JAVAFX_PARAMETER_VALUE_PREFIX + idx2));
            }
            args.add("--" + k2 + "=" + (v2 != null ? v2 : ""));
        }
        return (String[]) args.toArray(new String[0]);
    }

    private static void abort(Throwable cause, String fmt, Object... args) {
        String msg = String.format(fmt, args);
        if (msg != null) {
            System.err.println(msg);
        }
        System.exit(1);
    }

    private static Attributes getJarAttributes(String jarPath) {
        JarFile jarFile = null;
        try {
            try {
                jarFile = new JarFile(jarPath);
                Manifest manifest = jarFile.getManifest();
                if (manifest == null) {
                    abort(null, "No manifest in jar file %1$s", jarPath);
                }
                Attributes mainAttributes = manifest.getMainAttributes();
                try {
                    jarFile.close();
                } catch (IOException e2) {
                }
                return mainAttributes;
            } catch (IOException ioe) {
                abort(ioe, "Error launching jar file %1%s", jarPath);
                try {
                    jarFile.close();
                    return null;
                } catch (IOException e3) {
                    return null;
                }
            }
        } catch (Throwable th) {
            try {
                jarFile.close();
            } catch (IOException e4) {
            }
            throw th;
        }
    }

    private static void startToolkit() throws InterruptedException {
        if (toolkitStarted.getAndSet(true)) {
            return;
        }
        if (SystemProperties.isDebug()) {
            MXExtension.initializeIfAvailable();
        }
        CountDownLatch startupLatch = new CountDownLatch(1);
        PlatformImpl.startup(() -> {
            startupLatch.countDown();
        });
        startupLatch.await();
    }

    private static void launchApplication1(Class<? extends Application> appClass, Class<? extends Preloader> preloaderClass, String[] args) throws Exception {
        ClassLoader ccl;
        startToolkit();
        if (savedMainCcl != null && (ccl = Thread.currentThread().getContextClassLoader()) != null && ccl != savedMainCcl) {
            PlatformImpl.runLater(() -> {
                Thread.currentThread().setContextClassLoader(ccl);
            });
        }
        final AtomicBoolean pStartCalled = new AtomicBoolean(false);
        final AtomicBoolean startCalled = new AtomicBoolean(false);
        final AtomicBoolean exitCalled = new AtomicBoolean(false);
        new AtomicBoolean(false);
        final CountDownLatch shutdownLatch = new CountDownLatch(1);
        final CountDownLatch pShutdownLatch = new CountDownLatch(1);
        PlatformImpl.FinishListener listener = new PlatformImpl.FinishListener() { // from class: com.sun.javafx.application.LauncherImpl.1
            @Override // com.sun.javafx.application.PlatformImpl.FinishListener
            public void idle(boolean implicitExit) {
                if (!implicitExit) {
                    return;
                }
                if (startCalled.get()) {
                    shutdownLatch.countDown();
                } else if (pStartCalled.get()) {
                    pShutdownLatch.countDown();
                }
            }

            @Override // com.sun.javafx.application.PlatformImpl.FinishListener
            public void exitCalled() {
                exitCalled.set(true);
                shutdownLatch.countDown();
            }
        };
        PlatformImpl.addListener(listener);
        try {
            AtomicReference<Preloader> pldr = new AtomicReference<>();
            if (preloaderClass != null) {
                PlatformImpl.runAndWait(() -> {
                    try {
                        Constructor<? extends Preloader> c2 = preloaderClass.getConstructor(new Class[0]);
                        pldr.set(c2.newInstance(new Object[0]));
                        ParametersImpl.registerParameters((Application) pldr.get(), new ParametersImpl(args));
                    } catch (Throwable t2) {
                        System.err.println("Exception in Preloader constructor");
                        pConstructorError = t2;
                        error = true;
                    }
                });
            }
            currentPreloader = pldr.get();
            if (currentPreloader != null && !error && !exitCalled.get()) {
                try {
                    currentPreloader.init();
                } catch (Throwable t2) {
                    System.err.println("Exception in Preloader init method");
                    pInitError = t2;
                    error = true;
                }
            }
            if (currentPreloader != null && !error && !exitCalled.get()) {
                PlatformImpl.runAndWait(() -> {
                    try {
                        pStartCalled.set(true);
                        Stage primaryStage = new Stage();
                        primaryStage.impl_setPrimary(true);
                        currentPreloader.start(primaryStage);
                    } catch (Throwable t3) {
                        System.err.println("Exception in Preloader start method");
                        pStartError = t3;
                        error = true;
                    }
                });
                if (!error && !exitCalled.get()) {
                    notifyProgress(currentPreloader, 0.0d);
                }
            }
            AtomicReference<Application> app = new AtomicReference<>();
            if (!error && !exitCalled.get()) {
                if (currentPreloader != null) {
                    notifyProgress(currentPreloader, 1.0d);
                    notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_LOAD, null);
                }
                PlatformImpl.runAndWait(() -> {
                    try {
                        Constructor<? extends Application> c2 = appClass.getConstructor(new Class[0]);
                        app.set(c2.newInstance(new Object[0]));
                        ParametersImpl.registerParameters((Application) app.get(), new ParametersImpl(args));
                        PlatformImpl.setApplicationName(appClass);
                    } catch (Throwable t3) {
                        System.err.println("Exception in Application constructor");
                        constructorError = t3;
                        error = true;
                    }
                });
            }
            Application theApp = app.get();
            if (!error && !exitCalled.get()) {
                if (currentPreloader != null) {
                    notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_INIT, theApp);
                }
                try {
                    theApp.init();
                } catch (Throwable t3) {
                    System.err.println("Exception in Application init method");
                    initError = t3;
                    error = true;
                }
            }
            if (!error && !exitCalled.get()) {
                if (currentPreloader != null) {
                    notifyStateChange(currentPreloader, Preloader.StateChangeNotification.Type.BEFORE_START, theApp);
                }
                PlatformImpl.runAndWait(() -> {
                    try {
                        startCalled.set(true);
                        Stage primaryStage = new Stage();
                        primaryStage.impl_setPrimary(true);
                        theApp.start(primaryStage);
                    } catch (Throwable t4) {
                        System.err.println("Exception in Application start method");
                        startError = t4;
                        error = true;
                    }
                });
            }
            if (!error) {
                shutdownLatch.await();
            }
            if (startCalled.get()) {
                PlatformImpl.runAndWait(() -> {
                    try {
                        theApp.stop();
                    } catch (Throwable t4) {
                        System.err.println("Exception in Application stop method");
                        stopError = t4;
                        error = true;
                    }
                });
            }
            if (error) {
                if (pConstructorError != null) {
                    throw new RuntimeException("Unable to construct Preloader instance: " + ((Object) appClass), pConstructorError);
                }
                if (pInitError != null) {
                    throw new RuntimeException("Exception in Preloader init method", pInitError);
                }
                if (pStartError != null) {
                    throw new RuntimeException("Exception in Preloader start method", pStartError);
                }
                if (pStopError != null) {
                    throw new RuntimeException("Exception in Preloader stop method", pStopError);
                }
                if (constructorError != null) {
                    String msg = "Unable to construct Application instance: " + ((Object) appClass);
                    if (!notifyError(msg, constructorError)) {
                        throw new RuntimeException(msg, constructorError);
                    }
                } else if (initError != null) {
                    if (!notifyError("Exception in Application init method", initError)) {
                        throw new RuntimeException("Exception in Application init method", initError);
                    }
                } else if (startError != null) {
                    if (!notifyError("Exception in Application start method", startError)) {
                        throw new RuntimeException("Exception in Application start method", startError);
                    }
                } else if (stopError != null && !notifyError("Exception in Application stop method", stopError)) {
                    throw new RuntimeException("Exception in Application stop method", stopError);
                }
            }
        } finally {
            PlatformImpl.removeListener(listener);
            PlatformImpl.tkExit();
        }
    }

    private static void notifyStateChange(Preloader preloader, Preloader.StateChangeNotification.Type type, Application app) {
        PlatformImpl.runAndWait(() -> {
            preloader.handleStateChangeNotification(new Preloader.StateChangeNotification(type, app));
        });
    }

    private static void notifyProgress(Preloader preloader, double d2) {
        PlatformImpl.runAndWait(() -> {
            preloader.handleProgressNotification(new Preloader.ProgressNotification(d2));
        });
    }

    private static boolean notifyError(String msg, Throwable constructorError2) {
        AtomicBoolean result = new AtomicBoolean(false);
        PlatformImpl.runAndWait(() -> {
            if (currentPreloader != null) {
                try {
                    Preloader.ErrorNotification evt = new Preloader.ErrorNotification(null, msg, constructorError2);
                    boolean rval = currentPreloader.handleErrorNotification(evt);
                    result.set(rval);
                } catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }
        });
        return result.get();
    }

    private static void notifyCurrentPreloader(Preloader.PreloaderNotification pe) {
        PlatformImpl.runAndWait(() -> {
            if (currentPreloader != null) {
                currentPreloader.handleApplicationNotification(pe);
            }
        });
    }

    public static void notifyPreloader(Application app, Preloader.PreloaderNotification info) {
        if (launchCalled.get()) {
            notifyCurrentPreloader(info);
        }
    }

    private LauncherImpl() {
        throw new InternalError();
    }
}
