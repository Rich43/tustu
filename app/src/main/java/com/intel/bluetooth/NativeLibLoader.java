package com.intel.bluetooth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import javafx.fxml.FXMLLoader;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/NativeLibLoader.class */
public abstract class NativeLibLoader {
    static final int OS_UNSUPPORTED = -1;
    static final int OS_LINUX = 1;
    static final int OS_WINDOWS = 2;
    static final int OS_WINDOWS_CE = 3;
    static final int OS_MAC_OS_X = 4;
    static final int OS_ANDROID = 5;
    private static int os = 0;
    private static Hashtable libsState = new Hashtable();
    private static Object bluecoveDllDir = null;
    static Class class$com$intel$bluetooth$NativeLibLoader;

    /* renamed from: com.intel.bluetooth.NativeLibLoader$1, reason: invalid class name */
    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/NativeLibLoader$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/NativeLibLoader$LibState.class */
    private static class LibState {
        boolean triedToLoadAlredy;
        boolean libraryAvailable;
        StringBuffer loadErrors;

        private LibState() {
            this.triedToLoadAlredy = false;
            this.libraryAvailable = false;
            this.loadErrors = new StringBuffer();
        }

        LibState(AnonymousClass1 x0) {
            this();
        }
    }

    private NativeLibLoader() {
    }

    static int getOS() {
        if (os != 0) {
            return os;
        }
        String sysName = System.getProperty("os.name");
        if (sysName == null) {
            DebugLog.fatal("Native Library not available on unknown platform");
            os = -1;
        } else {
            String sysName2 = sysName.toLowerCase();
            if (sysName2.indexOf("windows") != -1) {
                if (sysName2.indexOf("ce") != -1) {
                    os = 3;
                } else {
                    os = 2;
                }
            } else if (sysName2.indexOf("mac os x") != -1) {
                os = 4;
            } else if (sysName2.indexOf("linux") != -1) {
                String javaRuntimeName = System.getProperty("java.runtime.name");
                if (javaRuntimeName != null && javaRuntimeName.toLowerCase().indexOf("android runtime") != -1) {
                    os = 5;
                } else {
                    os = 1;
                }
            } else {
                DebugLog.fatal(new StringBuffer().append("Native Library not available on platform ").append(sysName2).toString());
                os = -1;
            }
        }
        return os;
    }

    static boolean isAvailable(String name) {
        return isAvailable(name, null);
    }

    static String getLoadErrors(String name) {
        LibState state = (LibState) libsState.get(name);
        if (state == null || state.loadErrors == null) {
            return "";
        }
        return state.loadErrors.toString();
    }

    static boolean isAvailable(String name, Class stackClass) {
        return isAvailable(name, stackClass, true);
    }

    static boolean isAvailable(String name, Class stackClass, boolean requiredLibrary) throws NoSuchElementException {
        String sysArch;
        String libFileName;
        LibState state = (LibState) libsState.get(name);
        if (state == null) {
            state = new LibState(null);
            libsState.put(name, state);
        }
        if (state.triedToLoadAlredy) {
            return state.libraryAvailable;
        }
        state.loadErrors = new StringBuffer();
        String libName = name;
        String libFileName2 = libName;
        String sysName = System.getProperty("os.name");
        String sysArch2 = System.getProperty("os.arch");
        if (sysArch2 != null) {
            sysArch = sysArch2.toLowerCase();
        } else {
            sysArch = "";
        }
        switch (getOS()) {
            case -1:
                state.loadErrors.append(new StringBuffer().append("Native Library ").append(name).append(" not available on [").append(sysName).append("] platform").toString());
                DebugLog.fatal(new StringBuffer().append("Native Library ").append(name).append(" not available on [").append(sysName).append("] platform").toString());
                state.triedToLoadAlredy = true;
                state.libraryAvailable = false;
                return state.libraryAvailable;
            case 0:
            default:
                state.loadErrors.append(new StringBuffer().append("Native Library ").append(name).append(" not available on [").append(sysName).append("] platform").toString());
                DebugLog.fatal(new StringBuffer().append("Native Library ").append(name).append(" not available on platform ").append(sysName).toString());
                state.triedToLoadAlredy = true;
                state.libraryAvailable = false;
                return state.libraryAvailable;
            case 1:
                if (sysArch.indexOf("i386") == -1 && sysArch.length() != 0) {
                    if (sysArch.indexOf("amd64") != -1 || sysArch.indexOf("x86_64") != -1) {
                        libName = new StringBuffer().append(libName).append("_x64").toString();
                    } else if (sysArch.indexOf("x86") == -1) {
                        libName = new StringBuffer().append(libName).append("_").append(sysArch).toString();
                    }
                }
                libFileName = new StringBuffer().append("lib").append(libName).append(".so").toString();
                break;
            case 2:
                if (sysArch.indexOf("amd64") != -1 || sysArch.indexOf("x86_64") != -1) {
                    libName = new StringBuffer().append(libName).append("_x64").toString();
                    libFileName2 = libName;
                }
                libFileName = new StringBuffer().append(libFileName2).append(".dll").toString();
                break;
            case 3:
                libName = new StringBuffer().append(libName).append("_ce").toString();
                libFileName = new StringBuffer().append(libName).append(".dll").toString();
                break;
            case 4:
                libFileName = new StringBuffer().append("lib").append(libFileName2).append(".jnilib").toString();
                break;
            case 5:
                libFileName = new StringBuffer().append("lib").append(libFileName2).append(".so").toString();
                break;
        }
        String path = System.getProperty(BlueCoveConfigProperties.PROPERTY_NATIVE_PATH);
        if (path != null && !UtilsJavaSE.ibmJ9midp) {
            state.libraryAvailable = tryloadPath(path, libFileName, state.loadErrors);
        }
        boolean useResource = true;
        String d2 = System.getProperty(BlueCoveConfigProperties.PROPERTY_NATIVE_RESOURCE);
        if ((d2 != null && d2.equalsIgnoreCase("false")) || getOS() == 5) {
            useResource = false;
        }
        if (!state.libraryAvailable && useResource && !UtilsJavaSE.ibmJ9midp) {
            state.libraryAvailable = loadAsSystemResource(libFileName, stackClass, state.loadErrors);
        }
        if (!state.libraryAvailable && getOS() == 1 && !UtilsJavaSE.ibmJ9midp) {
            state.libraryAvailable = tryloadPath(createLinuxPackagePath(sysArch), libFileName, state.loadErrors);
        }
        if (!state.libraryAvailable) {
            if (!UtilsJavaSE.ibmJ9midp) {
                state.libraryAvailable = tryload(libName, state.loadErrors);
            } else {
                state.libraryAvailable = tryloadIBMj9MIDP(libName);
            }
        }
        if (!state.libraryAvailable) {
            if (requiredLibrary) {
                System.err.println(new StringBuffer().append("Native Library ").append(libName).append(" not available").toString());
            }
            DebugLog.debug("java.library.path", System.getProperty("java.library.path"));
        }
        state.triedToLoadAlredy = true;
        return state.libraryAvailable;
    }

    private static String createLinuxPackagePath(String sysArch) {
        if (sysArch.indexOf("64") != -1) {
            return new StringBuffer().append("/usr/lib64/bluecove/").append(BlueCoveImpl.version).toString();
        }
        return new StringBuffer().append("/usr/lib/bluecove/").append(BlueCoveImpl.version).toString();
    }

    private static boolean tryload(String name, StringBuffer loadErrors) {
        try {
            System.loadLibrary(name);
            DebugLog.debug("Library loaded", name);
            return true;
        } catch (Throwable e2) {
            DebugLog.error(new StringBuffer().append("Library ").append(name).append(" not loaded ").toString(), e2);
            loadErrors.append("\nload [").append(name).append("] ").append(e2.getMessage());
            return false;
        }
    }

    private static boolean tryloadIBMj9MIDP(String name) {
        try {
            IBMJ9Helper.loadLibrary(name);
            DebugLog.debug("Library loaded", name);
            return true;
        } catch (Throwable e2) {
            DebugLog.error(new StringBuffer().append("Library ").append(name).append(" not loaded ").toString(), e2);
            return false;
        }
    }

    private static boolean tryloadPath(String path, String name, StringBuffer loadErrors) throws NoSuchElementException {
        UtilsStringTokenizer tok = new UtilsStringTokenizer(path, File.pathSeparator);
        while (tok.hasMoreTokens()) {
            String dirPath = tok.nextToken();
            File dir = new File(dirPath);
            if (dir.isDirectory() && tryloadFile(dir, name, loadErrors)) {
                return true;
            }
        }
        return false;
    }

    private static boolean tryloadFile(File path, String name, StringBuffer loadErrors) {
        File f2 = new File(path, name);
        if (!f2.canRead()) {
            DebugLog.debug(new StringBuffer().append("Native Library ").append(f2.getAbsolutePath()).append(" not found").toString());
            return false;
        }
        try {
            System.load(f2.getAbsolutePath());
            DebugLog.debug("Library loaded", f2.getAbsolutePath());
            return true;
        } catch (Throwable e2) {
            DebugLog.error(new StringBuffer().append("Can't load library from path ").append((Object) path).toString(), e2);
            loadErrors.append("\nload [").append(f2.getAbsolutePath()).append("] ").append(e2.getMessage());
            return false;
        }
    }

    private static boolean tryloadPathIBMj9MIDP(String path, String name) {
        try {
            IBMJ9Helper.loadLibrary(new StringBuffer().append(path).append(FXMLLoader.ESCAPE_PREFIX).append(name).toString());
            DebugLog.debug("Library loaded", new StringBuffer().append(path).append(FXMLLoader.ESCAPE_PREFIX).append(name).toString());
            return true;
        } catch (Throwable e2) {
            DebugLog.error(new StringBuffer().append("Can't load library from path ").append(path).append(FXMLLoader.ESCAPE_PREFIX).append(name).toString(), e2);
            return false;
        }
    }

    private static boolean loadAsSystemResource(String libFileName, Class stackClass, StringBuffer loadErrors) {
        InputStream is;
        Class clsClass$;
        ClassLoader clo = null;
        try {
            try {
                if (stackClass != null) {
                    clo = stackClass.getClassLoader();
                    DebugLog.debug("Use stack ClassLoader");
                } else {
                    if (class$com$intel$bluetooth$NativeLibLoader == null) {
                        clsClass$ = class$("com.intel.bluetooth.NativeLibLoader");
                        class$com$intel$bluetooth$NativeLibLoader = clsClass$;
                    } else {
                        clsClass$ = class$com$intel$bluetooth$NativeLibLoader;
                    }
                    clo = clsClass$.getClassLoader();
                }
            } catch (Throwable th) {
            }
            if (clo == null) {
                DebugLog.debug("Use System ClassLoader");
                is = ClassLoader.getSystemResourceAsStream(libFileName);
            } else {
                is = clo.getResourceAsStream(libFileName);
            }
            if (is == null) {
                DebugLog.error(new StringBuffer().append("Native Library ").append(libFileName).append(" is not a Resource !").toString());
                loadErrors.append("\nresource not found ").append(libFileName);
                return false;
            }
            File fd = makeTempName(libFileName);
            try {
                if (!copy2File(is, fd)) {
                    loadErrors.append("\ncan't create temp file");
                    return false;
                }
                try {
                    is.close();
                } catch (IOException e2) {
                }
                try {
                    fd.deleteOnExit();
                } catch (Throwable th2) {
                }
                try {
                    System.load(fd.getAbsolutePath());
                    DebugLog.debug("Library loaded from", fd);
                    return true;
                } catch (Throwable e3) {
                    DebugLog.fatal("Can't load library file ", e3);
                    loadErrors.append("\nload resource [").append(fd.getAbsolutePath()).append("] ").append(e3.getMessage());
                    File debugFileCreated = new File(fd.getAbsolutePath());
                    if (debugFileCreated.canRead()) {
                        return false;
                    }
                    DebugLog.fatal(new StringBuffer().append("File ").append(fd.getAbsolutePath()).append(" magicaly disappeared").toString());
                    return false;
                }
            } finally {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        } catch (Throwable th3) {
            DebugLog.error(new StringBuffer().append("Native Library ").append(libFileName).append(" is not a Resource !").toString());
            loadErrors.append("\nresource not found ").append(libFileName);
            return false;
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    private static boolean copy2File(InputStream is, File fd) {
        FileOutputStream fos = null;
        try {
            try {
                fos = new FileOutputStream(fd);
                byte[] b2 = new byte[1000];
                while (true) {
                    int len = is.read(b2);
                    if (len < 0) {
                        break;
                    }
                    fos.write(b2, 0, len);
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e2) {
                    }
                }
                return true;
            } catch (Throwable th) {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e3) {
                    }
                }
                throw th;
            }
        } catch (Throwable e4) {
            DebugLog.debug("Can't create temp file ", e4);
            System.err.println(new StringBuffer().append("Can't create temp file ").append(fd.getAbsolutePath()).toString());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e5) {
                }
            }
            return false;
        }
    }

    private static File makeTempName(String libFileName) {
        if (bluecoveDllDir != null) {
            File f2 = new File((File) bluecoveDllDir, libFileName);
            DebugLog.debug("tmp file", f2.getAbsolutePath());
            return f2;
        }
        String tmpDir = System.getProperty("java.io.tmpdir");
        if (tmpDir == null || tmpDir.length() == 0) {
            tmpDir = "temp";
        }
        String uname = System.getProperty("user.name");
        int count = 0;
        File dir = null;
        while (count <= 10) {
            int i2 = count;
            count++;
            dir = new File(tmpDir, new StringBuffer().append("bluecove_").append(uname).append("_").append(i2).toString());
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    try {
                        File[] files = dir.listFiles();
                        for (File file : files) {
                            if (!file.delete()) {
                                break;
                            }
                        }
                    } catch (Throwable th) {
                    }
                } else {
                    continue;
                }
            }
            if (!dir.exists() && !dir.mkdirs()) {
                DebugLog.debug("Can't create temporary dir ", dir.getAbsolutePath());
            } else {
                try {
                    dir.deleteOnExit();
                } catch (Throwable th2) {
                }
                File fd = new File(dir, libFileName);
                if (!fd.exists() || fd.delete()) {
                    try {
                    } catch (IOException e2) {
                        DebugLog.debug("Can't create file in temporary dir ", fd.getAbsolutePath());
                    } catch (Throwable th3) {
                    }
                    if (!fd.createNewFile()) {
                        DebugLog.debug("Can't create file in temporary dir ", fd.getAbsolutePath());
                    } else {
                        bluecoveDllDir = dir;
                        DebugLog.debug("set dll dir", dir.getAbsolutePath());
                        return fd;
                    }
                }
            }
        }
        DebugLog.debug(new StringBuffer().append("Can't create temporary dir ").append(dir.getAbsolutePath()).toString());
        return new File(tmpDir, libFileName);
    }
}
