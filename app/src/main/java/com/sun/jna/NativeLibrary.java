package com.sun.jna;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/NativeLibrary.class */
public class NativeLibrary {
    private long handle;
    private final String libraryName;
    private final String libraryPath;
    private final Map functions = new HashMap();
    final int callFlags;
    final Map options;
    private static final Map libraries = new HashMap();
    private static final Map searchPaths = Collections.synchronizedMap(new HashMap());
    private static final List librarySearchPath = new LinkedList();
    static Class class$com$sun$jna$LastErrorException;

    static {
        if (Native.POINTER_SIZE == 0) {
            throw new Error("Native library not initialized");
        }
        String webstartPath = Native.getWebStartLibraryPath("jnidispatch");
        if (webstartPath != null) {
            librarySearchPath.add(webstartPath);
        }
        if (System.getProperty("jna.platform.library.path") == null && !Platform.isWindows()) {
            String platformPath = "";
            String sep = "";
            String archPath = "";
            if (Platform.isLinux() || Platform.isSolaris() || Platform.isFreeBSD()) {
                archPath = new StringBuffer().append(Platform.isSolaris() ? "/" : "").append(Pointer.SIZE * 8).toString();
            }
            String[] paths = {new StringBuffer().append("/usr/lib").append(archPath).toString(), new StringBuffer().append("/lib").append(archPath).toString(), "/usr/lib", "/lib"};
            if (Platform.isLinux() && Pointer.SIZE == 8) {
                paths = new String[]{new StringBuffer().append("/usr/lib").append(archPath).toString(), new StringBuffer().append("/lib").append(archPath).toString()};
            }
            for (int i2 = 0; i2 < paths.length; i2++) {
                File dir = new File(paths[i2]);
                if (dir.exists() && dir.isDirectory()) {
                    platformPath = new StringBuffer().append(platformPath).append(sep).append(paths[i2]).toString();
                    sep = File.pathSeparator;
                }
            }
            if (!"".equals(platformPath)) {
                System.setProperty("jna.platform.library.path", platformPath);
            }
        }
        librarySearchPath.addAll(initPaths("jna.platform.library.path"));
    }

    private static String functionKey(String name, int flags) {
        return new StringBuffer().append(name).append(CallSiteDescriptor.OPERATOR_DELIMITER).append(flags).toString();
    }

    private NativeLibrary(String libraryName, String libraryPath, long handle, Map options) {
        this.libraryName = getLibraryName(libraryName);
        this.libraryPath = libraryPath;
        this.handle = handle;
        Object option = options.get(Library.OPTION_CALLING_CONVENTION);
        int callingConvention = option instanceof Integer ? ((Integer) option).intValue() : 0;
        this.callFlags = callingConvention;
        this.options = options;
        if (Platform.isWindows() && "kernel32".equals(this.libraryName.toLowerCase())) {
            synchronized (this.functions) {
                Function f2 = new Function(this, this, "GetLastError", 1) { // from class: com.sun.jna.NativeLibrary.1
                    private final NativeLibrary this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // com.sun.jna.Function
                    Object invoke(Object[] args, Class returnType, boolean b2) {
                        return new Integer(Native.getLastError());
                    }
                };
                this.functions.put(functionKey("GetLastError", this.callFlags), f2);
            }
        }
    }

    private static NativeLibrary loadLibrary(String libraryName, Map options) {
        List searchPath = new LinkedList();
        String webstartPath = Native.getWebStartLibraryPath(libraryName);
        if (webstartPath != null) {
            searchPath.add(webstartPath);
        }
        List customPaths = (List) searchPaths.get(libraryName);
        if (customPaths != null) {
            synchronized (customPaths) {
                searchPath.addAll(0, customPaths);
            }
        }
        searchPath.addAll(initPaths("jna.library.path"));
        String libraryPath = findLibraryPath(libraryName, searchPath);
        long handle = 0;
        try {
            handle = Native.open(libraryPath);
        } catch (UnsatisfiedLinkError e2) {
            searchPath.addAll(librarySearchPath);
        }
        if (handle == 0) {
            try {
                libraryPath = findLibraryPath(libraryName, searchPath);
                handle = Native.open(libraryPath);
            } catch (UnsatisfiedLinkError e3) {
                e = e3;
                if (Platform.isLinux()) {
                    libraryPath = matchLibrary(libraryName, searchPath);
                    if (libraryPath != null) {
                        try {
                            handle = Native.open(libraryPath);
                        } catch (UnsatisfiedLinkError e22) {
                            e = e22;
                        }
                    }
                } else if (Platform.isMac() && !libraryName.endsWith(".dylib")) {
                    libraryPath = new StringBuffer().append("/System/Library/Frameworks/").append(libraryName).append(".framework/").append(libraryName).toString();
                    if (new File(libraryPath).exists()) {
                        try {
                            handle = Native.open(libraryPath);
                        } catch (UnsatisfiedLinkError e23) {
                            e = e23;
                        }
                    }
                } else if (Platform.isWindows()) {
                    libraryPath = findLibraryPath(new StringBuffer().append("lib").append(libraryName).toString(), searchPath);
                    try {
                        handle = Native.open(libraryPath);
                    } catch (UnsatisfiedLinkError e24) {
                        e = e24;
                    }
                }
                if (handle == 0) {
                    throw new UnsatisfiedLinkError(new StringBuffer().append("Unable to load library '").append(libraryName).append("': ").append(e.getMessage()).toString());
                }
            }
        }
        return new NativeLibrary(libraryName, libraryPath, handle, options);
    }

    private String getLibraryName(String libraryName) {
        String simplified = libraryName;
        String template = mapLibraryName("---");
        int prefixEnd = template.indexOf("---");
        if (prefixEnd > 0 && simplified.startsWith(template.substring(0, prefixEnd))) {
            simplified = simplified.substring(prefixEnd);
        }
        String suffix = template.substring(prefixEnd + "---".length());
        int suffixStart = simplified.indexOf(suffix);
        if (suffixStart != -1) {
            simplified = simplified.substring(0, suffixStart);
        }
        return simplified;
    }

    public static final NativeLibrary getInstance(String libraryName) {
        return getInstance(libraryName, Collections.EMPTY_MAP);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final NativeLibrary getInstance(String libraryName, Map options) {
        NativeLibrary nativeLibrary;
        Map options2 = new HashMap(options);
        if (options2.get(Library.OPTION_CALLING_CONVENTION) == null) {
            options2.put(Library.OPTION_CALLING_CONVENTION, new Integer(0));
        }
        if (Platform.isLinux() && PdfOps.c_TOKEN.equals(libraryName)) {
            libraryName = null;
        }
        synchronized (libraries) {
            WeakReference ref = (WeakReference) libraries.get(new StringBuffer().append(libraryName).append((Object) options2).toString());
            NativeLibrary library = ref != null ? (NativeLibrary) ref.get() : null;
            if (library == null) {
                if (libraryName == null) {
                    library = new NativeLibrary("<process>", null, Native.open(null), options2);
                } else {
                    library = loadLibrary(libraryName, options2);
                }
                WeakReference ref2 = new WeakReference(library);
                libraries.put(new StringBuffer().append(library.getName()).append((Object) options2).toString(), ref2);
                File file = library.getFile();
                if (file != null) {
                    libraries.put(new StringBuffer().append(file.getAbsolutePath()).append((Object) options2).toString(), ref2);
                    libraries.put(new StringBuffer().append(file.getName()).append((Object) options2).toString(), ref2);
                }
            }
            nativeLibrary = library;
        }
        return nativeLibrary;
    }

    public static final synchronized NativeLibrary getProcess() {
        return getInstance(null);
    }

    public static final synchronized NativeLibrary getProcess(Map options) {
        return getInstance(null, options);
    }

    public static final void addSearchPath(String libraryName, String path) {
        synchronized (searchPaths) {
            List customPaths = (List) searchPaths.get(libraryName);
            if (customPaths == null) {
                customPaths = Collections.synchronizedList(new LinkedList());
                searchPaths.put(libraryName, customPaths);
            }
            customPaths.add(path);
        }
    }

    public Function getFunction(String functionName) {
        return getFunction(functionName, this.callFlags);
    }

    Function getFunction(String name, Method method) throws Throwable {
        Class clsClass$;
        int flags = this.callFlags;
        Class[] etypes = method.getExceptionTypes();
        for (Class cls : etypes) {
            if (class$com$sun$jna$LastErrorException == null) {
                clsClass$ = class$("com.sun.jna.LastErrorException");
                class$com$sun$jna$LastErrorException = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$LastErrorException;
            }
            if (clsClass$.isAssignableFrom(cls)) {
                flags |= 4;
            }
        }
        return getFunction(name, flags);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public Function getFunction(String functionName, int callFlags) {
        Function function;
        if (functionName == null) {
            throw new NullPointerException("Function name may not be null");
        }
        synchronized (this.functions) {
            String key = functionKey(functionName, callFlags);
            Function function2 = (Function) this.functions.get(key);
            if (function2 == null) {
                function2 = new Function(this, functionName, callFlags);
                this.functions.put(key, function2);
            }
            function = function2;
        }
        return function;
    }

    public Map getOptions() {
        return this.options;
    }

    public Pointer getGlobalVariableAddress(String symbolName) {
        try {
            return new Pointer(getSymbolAddress(symbolName));
        } catch (UnsatisfiedLinkError e2) {
            throw new UnsatisfiedLinkError(new StringBuffer().append("Error looking up '").append(symbolName).append("': ").append(e2.getMessage()).toString());
        }
    }

    long getSymbolAddress(String name) {
        if (this.handle == 0) {
            throw new UnsatisfiedLinkError("Library has been unloaded");
        }
        return Native.findSymbol(this.handle, name);
    }

    public String toString() {
        return new StringBuffer().append("Native Library <").append(this.libraryPath).append("@").append(this.handle).append(">").toString();
    }

    public String getName() {
        return this.libraryName;
    }

    public File getFile() {
        if (this.libraryPath == null) {
            return null;
        }
        return new File(this.libraryPath);
    }

    protected void finalize() {
        dispose();
    }

    static void disposeAll() {
        Set<Reference> values;
        synchronized (libraries) {
            values = new HashSet(libraries.values());
        }
        for (Reference ref : values) {
            NativeLibrary lib = (NativeLibrary) ref.get();
            if (lib != null) {
                lib.dispose();
            }
        }
    }

    public void dispose() {
        synchronized (libraries) {
            libraries.remove(new StringBuffer().append(getName()).append((Object) this.options).toString());
            File file = getFile();
            if (file != null) {
                libraries.remove(new StringBuffer().append(file.getAbsolutePath()).append((Object) this.options).toString());
                libraries.remove(new StringBuffer().append(file.getName()).append((Object) this.options).toString());
            }
        }
        synchronized (this) {
            if (this.handle != 0) {
                Native.close(this.handle);
                this.handle = 0L;
            }
        }
    }

    private static List initPaths(String key) {
        String value = System.getProperty(key, "");
        if ("".equals(value)) {
            return Collections.EMPTY_LIST;
        }
        StringTokenizer st = new StringTokenizer(value, File.pathSeparator);
        List list = new ArrayList();
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            if (!"".equals(path)) {
                list.add(path);
            }
        }
        return list;
    }

    private static String findLibraryPath(String libName, List searchPath) {
        if (new File(libName).isAbsolute()) {
            return libName;
        }
        String name = mapLibraryName(libName);
        Iterator it = searchPath.iterator();
        while (it.hasNext()) {
            String path = (String) it.next();
            File file = new File(path, name);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            if (Platform.isMac() && name.endsWith(".dylib")) {
                File file2 = new File(path, new StringBuffer().append(name.substring(0, name.lastIndexOf(".dylib"))).append(".jnilib").toString());
                if (file2.exists()) {
                    return file2.getAbsolutePath();
                }
            }
        }
        return name;
    }

    private static String mapLibraryName(String libName) {
        if (Platform.isMac()) {
            if (libName.startsWith("lib") && (libName.endsWith(".dylib") || libName.endsWith(".jnilib"))) {
                return libName;
            }
            String name = System.mapLibraryName(libName);
            if (name.endsWith(".jnilib")) {
                return new StringBuffer().append(name.substring(0, name.lastIndexOf(".jnilib"))).append(".dylib").toString();
            }
            return name;
        }
        if (Platform.isLinux()) {
            if (isVersionedName(libName) || libName.endsWith(".so")) {
                return libName;
            }
        } else if (Platform.isWindows() && (libName.endsWith(".drv") || libName.endsWith(".dll"))) {
            return libName;
        }
        return System.mapLibraryName(libName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isVersionedName(String name) {
        int so;
        if (name.startsWith("lib") && (so = name.lastIndexOf(".so.")) != -1 && so + 4 < name.length()) {
            for (int i2 = so + 4; i2 < name.length(); i2++) {
                char ch = name.charAt(i2);
                if (!Character.isDigit(ch) && ch != '.') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    static String matchLibrary(String libName, List searchPath) {
        File lib = new File(libName);
        if (lib.isAbsolute()) {
            searchPath = Arrays.asList(lib.getParent());
        }
        FilenameFilter filter = new FilenameFilter(libName) { // from class: com.sun.jna.NativeLibrary.2
            private final String val$libName;

            {
                this.val$libName = libName;
            }

            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String filename) {
                return (filename.startsWith(new StringBuffer().append("lib").append(this.val$libName).append(".so").toString()) || (filename.startsWith(new StringBuffer().append(this.val$libName).append(".so").toString()) && this.val$libName.startsWith("lib"))) && NativeLibrary.isVersionedName(filename);
            }
        };
        List matches = new LinkedList();
        Iterator it = searchPath.iterator();
        while (it.hasNext()) {
            File[] files = new File((String) it.next()).listFiles(filter);
            if (files != null && files.length > 0) {
                matches.addAll(Arrays.asList(files));
            }
        }
        double bestVersion = -1.0d;
        String bestMatch = null;
        Iterator it2 = matches.iterator();
        while (it2.hasNext()) {
            String path = ((File) it2.next()).getAbsolutePath();
            String ver = path.substring(path.lastIndexOf(".so.") + 4);
            double version = parseVersion(ver);
            if (version > bestVersion) {
                bestVersion = version;
                bestMatch = path;
            }
        }
        return bestMatch;
    }

    static double parseVersion(String ver) {
        String num;
        double v2 = 0.0d;
        double divisor = 1.0d;
        int dot = ver.indexOf(".");
        while (ver != null) {
            if (dot != -1) {
                num = ver.substring(0, dot);
                ver = ver.substring(dot + 1);
                dot = ver.indexOf(".");
            } else {
                num = ver;
                ver = null;
            }
            try {
                v2 += Integer.parseInt(num) / divisor;
                divisor *= 100.0d;
            } catch (NumberFormatException e2) {
                return 0.0d;
            }
        }
        return v2;
    }
}
