package com.intel.bluetooth;

import com.intel.bluetooth.BluetoothStack;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BlueCoveImpl.class */
public class BlueCoveImpl {
    public static final String BLUETOOTH_API_VERSION = "1.1.1";
    public static final String OBEX_API_VERSION = "1.1.1";
    public static final int versionMajor1 = 2;
    public static final int versionMajor2 = 1;
    public static final int versionMinor = 1;
    public static final int versionBuild = 0;
    public static final String versionSufix = "-SNAPSHOT";
    public static final String version = new StringBuffer().append(String.valueOf(2)).append(".").append(String.valueOf(1)).append(".").append(String.valueOf(1)).append(versionSufix).toString();
    public static final int nativeLibraryVersionExpected = 2010100;
    public static final String STACK_WINSOCK = "winsock";
    public static final String STACK_WIDCOMM = "widcomm";
    public static final String STACK_BLUESOLEIL = "bluesoleil";
    public static final String STACK_TOSHIBA = "toshiba";
    public static final String STACK_BLUEZ = "bluez";
    public static final String STACK_BLUEZ_DBUS = "bluez-dbus";
    public static final String STACK_OSX = "mac";
    public static final String STACK_EMULATOR = "emulator";
    private static final boolean oneDLLbuild = false;
    public static final String NATIVE_LIB_MS = "intelbth";
    public static final String NATIVE_LIB_WIDCOMM = "bluecove";
    public static final String NATIVE_LIB_TOSHIBA = "bluecove";
    public static final String NATIVE_LIB_BLUEZ = "bluecove";
    public static final String NATIVE_LIB_OSX = "bluecove";
    public static final String NATIVE_LIB_BLUESOLEIL = "intelbth";
    static final int BLUECOVE_STACK_DETECT_MICROSOFT = 1;
    static final int BLUECOVE_STACK_DETECT_WIDCOMM = 2;
    static final int BLUECOVE_STACK_DETECT_BLUESOLEIL = 4;
    static final int BLUECOVE_STACK_DETECT_TOSHIBA = 8;
    static final int BLUECOVE_STACK_DETECT_OSX = 16;
    public static final int BLUECOVE_STACK_DETECT_BLUEZ = 32;
    public static final int BLUECOVE_STACK_DETECT_EMULATOR = 64;
    public static final int BLUECOVE_STACK_DETECT_BLUEZ_DBUS = 128;
    static final String TRUE = "true";
    static final String FALSE = "false";
    private static final String FQCN;
    private static final Vector fqcnSet;
    private Object accessControlContext;
    private static ShutdownHookThread shutdownHookRegistered;
    private static BlueCoveImpl instance;
    private static BluetoothStackHolder singleStack;
    private static ThreadLocalWrapper threadStack;
    private static BluetoothStackHolder threadStackIDDefault;
    private static Hashtable resourceConfigProperties;
    private static Hashtable stacks;
    private static Vector initializationProperties;
    static Class class$com$intel$bluetooth$BlueCoveImpl;
    static Class class$com$intel$bluetooth$DebugLog;

    static {
        Class clsClass$;
        if (class$com$intel$bluetooth$BlueCoveImpl == null) {
            clsClass$ = class$("com.intel.bluetooth.BlueCoveImpl");
            class$com$intel$bluetooth$BlueCoveImpl = clsClass$;
        } else {
            clsClass$ = class$com$intel$bluetooth$BlueCoveImpl;
        }
        FQCN = clsClass$.getName();
        fqcnSet = new Vector();
        resourceConfigProperties = new Hashtable();
        stacks = new Hashtable();
        initializationProperties = new Vector();
        fqcnSet.addElement(FQCN);
        for (int i2 = 0; i2 < BlueCoveConfigProperties.INITIALIZATION_PROPERTIES.length; i2++) {
            initializationProperties.addElement(BlueCoveConfigProperties.INITIALIZATION_PROPERTIES[i2]);
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BlueCoveImpl$BluetoothStackHolder.class */
    private static class BluetoothStackHolder {
        private BluetoothStack bluetoothStack;
        Hashtable configProperties;

        private BluetoothStackHolder() {
            this.configProperties = new Hashtable();
        }

        BluetoothStackHolder(AnonymousClass1 x0) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static BluetoothStack getBluetoothStack() throws BluetoothStateException {
            return BlueCoveImpl.instance().getBluetoothStack();
        }

        public String toString() {
            if (this.bluetoothStack == null) {
                return "not initialized";
            }
            return this.bluetoothStack.toString();
        }
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BlueCoveImpl$AsynchronousShutdownThread.class */
    private class AsynchronousShutdownThread extends Thread {
        final Object monitor;
        int shutdownStart;
        private final BlueCoveImpl this$0;

        AsynchronousShutdownThread(BlueCoveImpl blueCoveImpl) {
            super("BluecoveAsynchronousShutdownThread");
            this.this$0 = blueCoveImpl;
            this.monitor = new Object();
            this.shutdownStart = 0;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            synchronized (this.monitor) {
                while (this.shutdownStart == 0) {
                    try {
                        this.monitor.wait();
                    } catch (InterruptedException e2) {
                        return;
                    }
                }
            }
            if (this.shutdownStart != -1) {
                if (!BlueCoveImpl.stacks.isEmpty()) {
                    Enumeration en = BlueCoveImpl.stacks.elements();
                    while (en.hasMoreElements()) {
                        BluetoothStackHolder s2 = (BluetoothStackHolder) en.nextElement2();
                        if (s2.bluetoothStack != null) {
                            try {
                                s2.bluetoothStack.destroy();
                                s2.bluetoothStack = null;
                            } catch (Throwable th) {
                                s2.bluetoothStack = null;
                                throw th;
                            }
                        }
                    }
                    BlueCoveImpl.stacks.clear();
                    System.out.println("BlueCove stack shutdown completed");
                }
                synchronized (this.monitor) {
                    this.monitor.notifyAll();
                }
            }
        }

        void deRegister() {
            this.shutdownStart = -1;
            synchronized (this.monitor) {
                this.monitor.notifyAll();
            }
        }
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BlueCoveImpl$ShutdownHookThread.class */
    private class ShutdownHookThread extends Thread {
        AsynchronousShutdownThread shutdownHookThread;
        private final BlueCoveImpl this$0;

        ShutdownHookThread(BlueCoveImpl blueCoveImpl, AsynchronousShutdownThread shutdownHookThread) {
            super("BluecoveShutdownHookThread");
            this.this$0 = blueCoveImpl;
            this.shutdownHookThread = shutdownHookThread;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Object monitor = this.shutdownHookThread.monitor;
            synchronized (monitor) {
                this.shutdownHookThread.shutdownStart = 1;
                monitor.notifyAll();
                if (!BlueCoveImpl.stacks.isEmpty()) {
                    try {
                        monitor.wait(7000L);
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }

        void deRegister() {
            ShutdownHookThread unused = BlueCoveImpl.shutdownHookRegistered = null;
            UtilsJavaSE.runtimeRemoveShutdownHook(this);
            this.shutdownHookThread.deRegister();
        }
    }

    public static synchronized BlueCoveImpl instance() {
        if (instance == null) {
            instance = new BlueCoveImpl();
        }
        return instance;
    }

    private BlueCoveImpl() {
        try {
            this.accessControlContext = AccessController.getContext();
        } catch (Throwable th) {
        }
        DebugLog.isDebugEnabled();
        copySystemProperties(null);
    }

    static int getNativeLibraryVersion() {
        return nativeLibraryVersionExpected;
    }

    private synchronized void createShutdownHook() {
        if (shutdownHookRegistered != null) {
            return;
        }
        AsynchronousShutdownThread shutdownHookThread = new AsynchronousShutdownThread(this);
        ShutdownHookThread shutdownHookThread2 = new ShutdownHookThread(this, shutdownHookThread);
        shutdownHookRegistered = shutdownHookThread2;
        if (UtilsJavaSE.runtimeAddShutdownHook(shutdownHookThread2)) {
            UtilsJavaSE.threadSetDaemon(shutdownHookThread);
            shutdownHookThread.start();
        }
    }

    private int getStackId(String stack) {
        if (STACK_WIDCOMM.equalsIgnoreCase(stack)) {
            return 2;
        }
        if (STACK_BLUESOLEIL.equalsIgnoreCase(stack)) {
            return 4;
        }
        if (STACK_TOSHIBA.equalsIgnoreCase(stack)) {
            return 8;
        }
        if (STACK_WINSOCK.equalsIgnoreCase(stack)) {
            return 1;
        }
        if (STACK_BLUEZ.equalsIgnoreCase(stack)) {
            return 32;
        }
        if (STACK_BLUEZ_DBUS.equalsIgnoreCase(stack)) {
            return 128;
        }
        if (STACK_WINSOCK.equalsIgnoreCase(stack)) {
            return 16;
        }
        if (STACK_EMULATOR.equalsIgnoreCase(stack)) {
            return 64;
        }
        return 0;
    }

    private Class loadStackClass(String classPropertyName, String classNamesDefault) throws BluetoothStateException, NoSuchElementException {
        String classNames = getConfigProperty(classPropertyName);
        if (classNames == null) {
            classNames = classNamesDefault;
        }
        UtilsStringTokenizer tok = new UtilsStringTokenizer(classNames, CallSiteDescriptor.OPERATOR_DELIMITER);
        while (tok.hasMoreTokens()) {
            String className = tok.nextToken();
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e2) {
                DebugLog.error(className, e2);
            }
        }
        throw new BluetoothStateException(new StringBuffer().append("BlueCove ").append(classNames).append(" not available").toString());
    }

    private BluetoothStack newStackInstance(Class ctackClass) throws BluetoothStateException {
        String className = ctackClass.getName();
        try {
            return (BluetoothStack) ctackClass.newInstance();
        } catch (IllegalAccessException e2) {
            DebugLog.error(className, e2);
            throw new BluetoothStateException(new StringBuffer().append("BlueCove ").append(className).append(" can't instantiate").toString());
        } catch (InstantiationException e3) {
            DebugLog.error(className, e3);
            throw new BluetoothStateException(new StringBuffer().append("BlueCove ").append(className).append(" can't instantiate").toString());
        }
    }

    private BluetoothStack loadStack(String classPropertyName, String classNameDefault) throws BluetoothStateException {
        return newStackInstance(loadStackClass(classPropertyName, classNameDefault));
    }

    static void loadNativeLibraries(BluetoothStack stack) throws BluetoothStateException {
        try {
            if (UtilsJavaSE.canCallNotLoadedNativeMethod) {
                if (stack.isNativeCodeLoaded()) {
                    return;
                }
            }
        } catch (Error e2) {
        }
        BluetoothStack.LibraryInformation[] libs = stack.requireNativeLibraries();
        if (libs == null || libs.length == 0) {
            return;
        }
        for (int i2 = 0; i2 < libs.length; i2++) {
            Class c2 = libs[i2].stackClass;
            if (c2 == null) {
                c2 = stack.getClass();
            }
            if (!NativeLibLoader.isAvailable(libs[i2].libraryName, c2, libs[i2].required)) {
                if (libs[i2].required) {
                    throw new BluetoothStateException(new StringBuffer().append("BlueCove library ").append(libs[i2].libraryName).append(" not available;").append(NativeLibLoader.getLoadErrors(libs[i2].libraryName)).toString());
                }
                DebugLog.debug(new StringBuffer().append("library ").append(libs[i2].libraryName).append(" not available").toString());
            }
        }
    }

    private static boolean isNativeLibrariesAvailable(BluetoothStack stack) {
        try {
            if (UtilsJavaSE.canCallNotLoadedNativeMethod) {
                return stack.isNativeCodeLoaded();
            }
        } catch (Error e2) {
        }
        BluetoothStack.LibraryInformation[] libs = stack.requireNativeLibraries();
        if (libs == null || libs.length == 0) {
            return true;
        }
        for (int i2 = 0; i2 < libs.length; i2++) {
            Class c2 = libs[i2].stackClass;
            if (c2 == null) {
                c2 = stack.getClass();
            }
            if (!NativeLibLoader.isAvailable(libs[i2].libraryName, c2)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BluetoothStack detectStack() throws BluetoothStateException, NoSuchElementException {
        BluetoothStack detectorStack;
        Class clsClass$;
        String stackFirstDetector = getConfigProperty(BlueCoveConfigProperties.PROPERTY_STACK_FIRST);
        String stackSelected = getConfigProperty("bluecove.stack");
        if (stackFirstDetector == null) {
            stackFirstDetector = stackSelected;
        }
        if (STACK_EMULATOR.equals(stackSelected)) {
            detectorStack = loadStack(BlueCoveConfigProperties.PROPERTY_EMULATOR_CLASS, "com.intel.bluetooth.BluetoothEmulator");
        } else {
            switch (NativeLibLoader.getOS()) {
                case 1:
                case 5:
                    Class stackClass = loadStackClass(BlueCoveConfigProperties.PROPERTY_BLUEZ_CLASS, "com.intel.bluetooth.BluetoothStackBlueZ|com.intel.bluetooth.BluetoothStackBlueZDBus");
                    detectorStack = newStackInstance(stackClass);
                    loadNativeLibraries(detectorStack);
                    stackSelected = detectorStack.getStackID();
                    break;
                case 2:
                case 3:
                    detectorStack = createDetectorOnWindows(stackFirstDetector);
                    if (DebugLog.isDebugEnabled()) {
                        if (class$com$intel$bluetooth$DebugLog == null) {
                            clsClass$ = class$("com.intel.bluetooth.DebugLog");
                            class$com$intel$bluetooth$DebugLog = clsClass$;
                        } else {
                            clsClass$ = class$com$intel$bluetooth$DebugLog;
                        }
                        detectorStack.enableNativeDebug(clsClass$, true);
                        break;
                    }
                    break;
                case 4:
                    detectorStack = new BluetoothStackOSX();
                    loadNativeLibraries(detectorStack);
                    stackSelected = detectorStack.getStackID();
                    break;
                default:
                    throw new BluetoothStateException("BlueCove not available");
            }
        }
        int libraryVersion = detectorStack.getLibraryVersion();
        if (2010100 != libraryVersion) {
            DebugLog.fatal(new StringBuffer().append("BlueCove native library version mismatch ").append(libraryVersion).append(" expected ").append(nativeLibraryVersionExpected).toString());
            throw new BluetoothStateException("BlueCove native library version mismatch");
        }
        if (stackSelected == null) {
            int aval = detectorStack.detectBluetoothStack();
            DebugLog.debug("BluetoothStack detected", aval);
            int detectorID = getStackId(detectorStack.getStackID());
            if ((aval & detectorID) != 0) {
                stackSelected = detectorStack.getStackID();
            } else if ((aval & 1) != 0) {
                stackSelected = STACK_WINSOCK;
            } else if ((aval & 2) != 0) {
                stackSelected = STACK_WIDCOMM;
            } else if ((aval & 4) != 0) {
                stackSelected = STACK_BLUESOLEIL;
            } else if ((aval & 8) != 0) {
                stackSelected = STACK_TOSHIBA;
            } else if ((aval & 16) != 0) {
                stackSelected = STACK_OSX;
            } else {
                DebugLog.fatal("BluetoothStack not detected");
                throw new BluetoothStateException("BluetoothStack not detected");
            }
        } else {
            DebugLog.debug("BluetoothStack selected", stackSelected);
        }
        BluetoothStack stack = setBluetoothStack(stackSelected, detectorStack);
        String stackSelected2 = stack.getStackID();
        copySystemProperties(stack);
        if (!stackSelected2.equals(STACK_EMULATOR)) {
            System.out.println(new StringBuffer().append("BlueCove version ").append(version).append(" on ").append(stackSelected2).toString());
        }
        return stack;
    }

    public static Vector getLocalDevicesID() throws BluetoothStateException {
        Vector v2 = new Vector();
        String ids = BluetoothStackHolder.getBluetoothStack().getLocalDeviceProperty(BlueCoveLocalDeviceProperties.LOCAL_DEVICE_DEVICES_LIST);
        if (ids != null) {
            UtilsStringTokenizer tok = new UtilsStringTokenizer(ids, ",");
            while (tok.hasMoreTokens()) {
                v2.addElement(tok.nextToken());
            }
        }
        return v2;
    }

    public static synchronized void useThreadLocalBluetoothStack() {
        BluetoothStackHolder s2;
        if (threadStack == null) {
            threadStack = new ThreadLocalWrapper();
        }
        BluetoothStackHolder s3 = (BluetoothStackHolder) threadStack.get();
        if (s3 == null) {
            if (singleStack != null) {
                s2 = singleStack;
                singleStack = null;
            } else {
                s2 = new BluetoothStackHolder(null);
            }
            threadStack.set(s2);
        }
    }

    public static synchronized Object getThreadBluetoothStackID() throws BluetoothStateException {
        useThreadLocalBluetoothStack();
        BluetoothStackHolder.getBluetoothStack();
        return threadStack.get();
    }

    public static synchronized Object getCurrentThreadBluetoothStackID() {
        if (threadStack == null) {
            return null;
        }
        return threadStack.get();
    }

    public static synchronized void setThreadBluetoothStackID(Object stackID) {
        if (stackID != null && !(stackID instanceof BluetoothStackHolder)) {
            throw new IllegalArgumentException("stackID is not valid");
        }
        if (threadStack == null) {
            throw new IllegalArgumentException("ThreadLocal configuration is not initialized");
        }
        threadStack.set(stackID);
    }

    public static synchronized void releaseThreadBluetoothStack() {
        if (threadStack == null) {
            throw new IllegalArgumentException("ThreadLocal configuration is not initialized");
        }
        threadStack.set(null);
    }

    public static synchronized void setDefaultThreadBluetoothStackID(Object stackID) {
        if (stackID != null && !(stackID instanceof BluetoothStackHolder)) {
            throw new IllegalArgumentException("stackID is not valid");
        }
        if (threadStack == null) {
            throw new IllegalArgumentException("ThreadLocal configuration is not initialized");
        }
        threadStackIDDefault = (BluetoothStackHolder) stackID;
    }

    static synchronized void setThreadBluetoothStack(BluetoothStack bluetoothStack) {
        if (threadStack == null) {
            return;
        }
        BluetoothStackHolder s2 = (BluetoothStackHolder) threadStack.get();
        if (s2 != null && s2.bluetoothStack == bluetoothStack) {
            return;
        }
        BluetoothStackHolder sh = (BluetoothStackHolder) stacks.get(bluetoothStack);
        if (sh == null) {
            throw new RuntimeException("ThreadLocal not found for BluetoothStack");
        }
        threadStack.set(sh);
    }

    public static synchronized void shutdownThreadBluetoothStack() {
        BluetoothStackHolder s2;
        if (threadStack == null || (s2 = (BluetoothStackHolder) threadStack.get()) == null) {
            return;
        }
        if (threadStackIDDefault == s2) {
            threadStackIDDefault = null;
        }
        s2.configProperties.clear();
        if (s2.bluetoothStack != null) {
            BluetoothConnectionNotifierBase.shutdownConnections(s2.bluetoothStack);
            RemoteDeviceHelper.shutdownConnections(s2.bluetoothStack);
            s2.bluetoothStack.destroy();
            stacks.remove(s2.bluetoothStack);
            s2.bluetoothStack = null;
        }
    }

    public static synchronized void shutdown() {
        Enumeration en = stacks.elements();
        while (en.hasMoreElements()) {
            BluetoothStackHolder s2 = (BluetoothStackHolder) en.nextElement2();
            s2.configProperties.clear();
            if (s2.bluetoothStack != null) {
                BluetoothConnectionNotifierBase.shutdownConnections(s2.bluetoothStack);
                RemoteDeviceHelper.shutdownConnections(s2.bluetoothStack);
                try {
                    s2.bluetoothStack.destroy();
                    s2.bluetoothStack = null;
                } catch (Throwable th) {
                    s2.bluetoothStack = null;
                    throw th;
                }
            }
        }
        stacks.clear();
        singleStack = null;
        threadStackIDDefault = null;
        if (shutdownHookRegistered != null) {
            shutdownHookRegistered.deRegister();
        }
        clearSystemProperties();
    }

    public static void setConfigProperty(String name, String value) {
        if (name == null) {
            throw new NullPointerException("key is null");
        }
        BluetoothStackHolder sh = currentStackHolder(true);
        if (sh.bluetoothStack != null && initializationProperties.contains(name)) {
            throw new IllegalArgumentException("BlueCove Stack already initialized");
        }
        if (value == null) {
            sh.configProperties.remove(name);
        } else {
            sh.configProperties.put(name, value);
        }
    }

    public static String getConfigProperty(String key) {
        Class clsClass$;
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        String value = null;
        BluetoothStackHolder sh = currentStackHolder(false);
        if (sh != null) {
            value = (String) sh.configProperties.get(key);
        }
        if (value == null) {
            try {
                value = System.getProperty(key);
            } catch (SecurityException e2) {
            }
        }
        if (value == null) {
            synchronized (resourceConfigProperties) {
                Object casheValue = resourceConfigProperties.get(key);
                if (casheValue != null) {
                    if (casheValue instanceof String) {
                        value = (String) casheValue;
                    }
                } else {
                    if (class$com$intel$bluetooth$BlueCoveImpl == null) {
                        clsClass$ = class$("com.intel.bluetooth.BlueCoveImpl");
                        class$com$intel$bluetooth$BlueCoveImpl = clsClass$;
                    } else {
                        clsClass$ = class$com$intel$bluetooth$BlueCoveImpl;
                    }
                    value = Utils.getResourceProperty(clsClass$, key);
                    if (value == null) {
                        resourceConfigProperties.put(key, new Object());
                    } else {
                        resourceConfigProperties.put(key, value);
                    }
                }
            }
        }
        return value;
    }

    public static boolean getConfigProperty(String key, boolean defaultValue) {
        String value = getConfigProperty(key);
        if (value != null) {
            return "true".equals(value) || "1".equals(value);
        }
        return defaultValue;
    }

    static int getConfigProperty(String key, int defaultValue) {
        String value = getConfigProperty(key);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    static String[] getSystemPropertiesList() {
        String[] p2 = {BluetoothConsts.PROPERTY_BLUETOOTH_MASTER_SWITCH, BluetoothConsts.PROPERTY_BLUETOOTH_SD_ATTR_RETRIEVABLE_MAX, BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_DEVICES_MAX, BluetoothConsts.PROPERTY_BLUETOOTH_L2CAP_RECEIVEMTU_MAX, BluetoothConsts.PROPERTY_BLUETOOTH_SD_TRANS_MAX, BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_INQUIRY_SCAN, BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_PAGE_SCAN, BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_INQUIRY, BluetoothConsts.PROPERTY_BLUETOOTH_CONNECTED_PAGE};
        return p2;
    }

    static void clearSystemProperties() {
        UtilsJavaSE.setSystemProperty(BluetoothConsts.PROPERTY_BLUETOOTH_API_VERSION, null);
        UtilsJavaSE.setSystemProperty(BluetoothConsts.PROPERTY_OBEX_API_VERSION, null);
        String[] property = getSystemPropertiesList();
        for (String str : property) {
            UtilsJavaSE.setSystemProperty(str, null);
        }
    }

    void copySystemProperties(BluetoothStack bluetoothStack) {
        UtilsJavaSE.setSystemProperty(BluetoothConsts.PROPERTY_BLUETOOTH_API_VERSION, "1.1.1");
        UtilsJavaSE.setSystemProperty(BluetoothConsts.PROPERTY_OBEX_API_VERSION, "1.1.1");
        if (bluetoothStack != null) {
            String[] property = getSystemPropertiesList();
            for (int i2 = 0; i2 < property.length; i2++) {
                UtilsJavaSE.setSystemProperty(property[i2], bluetoothStack.getLocalDeviceProperty(property[i2]));
            }
        }
    }

    public String getLocalDeviceFeature(int featureID) throws BluetoothStateException {
        return (BluetoothStackHolder.getBluetoothStack().getFeatureSet() & featureID) != 0 ? "true" : "false";
    }

    private BluetoothStack createDetectorOnWindows(String stackFirst) throws BluetoothStateException {
        if (stackFirst != null) {
            DebugLog.debug("detector stack", stackFirst);
            if (STACK_WIDCOMM.equalsIgnoreCase(stackFirst)) {
                BluetoothStack detectorStack = new BluetoothStackWIDCOMM();
                if (isNativeLibrariesAvailable(detectorStack)) {
                    return detectorStack;
                }
            } else if (STACK_BLUESOLEIL.equalsIgnoreCase(stackFirst)) {
                BluetoothStack detectorStack2 = new BluetoothStackBlueSoleil();
                if (isNativeLibrariesAvailable(detectorStack2)) {
                    return detectorStack2;
                }
            } else if (STACK_WINSOCK.equalsIgnoreCase(stackFirst)) {
                BluetoothStack detectorStack3 = new BluetoothStackMicrosoft();
                if (isNativeLibrariesAvailable(detectorStack3)) {
                    return detectorStack3;
                }
            } else if (STACK_TOSHIBA.equalsIgnoreCase(stackFirst)) {
                BluetoothStack detectorStack4 = new BluetoothStackToshiba();
                if (isNativeLibrariesAvailable(detectorStack4)) {
                    return detectorStack4;
                }
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Invalid BlueCove detector stack [").append(stackFirst).append("]").toString());
            }
        }
        BluetoothStack stack = new BluetoothStackMicrosoft();
        if (isNativeLibrariesAvailable(stack)) {
            return stack;
        }
        BluetoothStack stack2 = new BluetoothStackWIDCOMM();
        if (isNativeLibrariesAvailable(stack2)) {
            return stack2;
        }
        throw new BluetoothStateException("BlueCove libraries not available");
    }

    public String setBluetoothStack(String stack) throws BluetoothStateException {
        return setBluetoothStack(stack, null).getStackID();
    }

    private synchronized BluetoothStack setBluetoothStack(String stack, BluetoothStack detectorStack) throws BluetoothStateException {
        BluetoothStackHolder s2;
        BluetoothStack newStack;
        Class clsClass$;
        if (singleStack != null) {
            if (singleStack.bluetoothStack != null) {
                singleStack.bluetoothStack.destroy();
                stacks.remove(singleStack.bluetoothStack);
                singleStack.bluetoothStack = null;
            }
        } else if (threadStack != null && (s2 = (BluetoothStackHolder) threadStack.get()) != null && s2.bluetoothStack != null) {
            s2.bluetoothStack.destroy();
            stacks.remove(s2.bluetoothStack);
            s2.bluetoothStack = null;
        }
        if (detectorStack != null && detectorStack.getStackID().equalsIgnoreCase(stack)) {
            newStack = detectorStack;
        } else if (STACK_WIDCOMM.equalsIgnoreCase(stack)) {
            newStack = new BluetoothStackWIDCOMM();
        } else if (STACK_BLUESOLEIL.equalsIgnoreCase(stack)) {
            newStack = new BluetoothStackBlueSoleil();
        } else if (STACK_TOSHIBA.equalsIgnoreCase(stack)) {
            newStack = new BluetoothStackToshiba();
        } else {
            newStack = new BluetoothStackMicrosoft();
        }
        loadNativeLibraries(newStack);
        int libraryVersion = newStack.getLibraryVersion();
        if (2010100 != libraryVersion) {
            DebugLog.fatal(new StringBuffer().append("BlueCove native library version mismatch ").append(libraryVersion).append(" expected ").append(nativeLibraryVersionExpected).toString());
            throw new BluetoothStateException("BlueCove native library version mismatch");
        }
        if (DebugLog.isDebugEnabled()) {
            BluetoothStack bluetoothStack = newStack;
            if (class$com$intel$bluetooth$DebugLog == null) {
                clsClass$ = class$("com.intel.bluetooth.DebugLog");
                class$com$intel$bluetooth$DebugLog = clsClass$;
            } else {
                clsClass$ = class$com$intel$bluetooth$DebugLog;
            }
            bluetoothStack.enableNativeDebug(clsClass$, true);
        }
        newStack.initialize();
        createShutdownHook();
        BluetoothStackHolder sh = currentStackHolder(true);
        sh.bluetoothStack = newStack;
        stacks.put(newStack, sh);
        if (threadStack != null) {
            threadStack.set(sh);
        }
        return newStack;
    }

    public void enableNativeDebug(boolean on) {
        Class clsClass$;
        BluetoothStackHolder s2 = currentStackHolder(false);
        if (s2 != null && s2.bluetoothStack != null) {
            BluetoothStack bluetoothStack = s2.bluetoothStack;
            if (class$com$intel$bluetooth$DebugLog == null) {
                clsClass$ = class$("com.intel.bluetooth.DebugLog");
                class$com$intel$bluetooth$DebugLog = clsClass$;
            } else {
                clsClass$ = class$com$intel$bluetooth$DebugLog;
            }
            bluetoothStack.enableNativeDebug(clsClass$, on);
        }
    }

    private static BluetoothStackHolder currentStackHolder(boolean create) {
        if (threadStack != null) {
            BluetoothStackHolder s2 = (BluetoothStackHolder) threadStack.get();
            if (s2 == null && threadStackIDDefault != null) {
                return threadStackIDDefault;
            }
            if (s2 == null && create) {
                s2 = new BluetoothStackHolder(null);
                threadStack.set(s2);
            }
            return s2;
        }
        if (singleStack == null && create) {
            singleStack = new BluetoothStackHolder(null);
        }
        return singleStack;
    }

    public synchronized BluetoothStack getBluetoothStack() throws BluetoothStateException {
        BluetoothStack stack;
        Utils.isLegalAPICall(fqcnSet);
        BluetoothStackHolder sh = currentStackHolder(false);
        if (sh != null && sh.bluetoothStack != null) {
            return sh.bluetoothStack;
        }
        if (sh == null && threadStack != null) {
            throw new BluetoothStateException("No BluetoothStack or Adapter for current thread");
        }
        if (this.accessControlContext == null) {
            stack = detectStack();
        } else {
            stack = detectStackPrivileged();
        }
        return stack;
    }

    private BluetoothStack detectStackPrivileged() throws BluetoothStateException {
        try {
            return (BluetoothStack) AccessController.doPrivileged(new PrivilegedExceptionAction(this) { // from class: com.intel.bluetooth.BlueCoveImpl.1
                private final BlueCoveImpl this$0;

                {
                    this.this$0 = this;
                }

                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws BluetoothStateException {
                    return this.this$0.detectStack();
                }
            }, (AccessControlContext) this.accessControlContext);
        } catch (PrivilegedActionException e2) {
            Throwable cause = UtilsJavaSE.getCause(e2);
            if (cause instanceof BluetoothStateException) {
                throw ((BluetoothStateException) cause);
            }
            throw ((BluetoothStateException) UtilsJavaSE.initCause(new BluetoothStateException(e2.getMessage()), cause));
        }
    }
}
