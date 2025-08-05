package com.sun.jna;

import com.sun.jmx.defaults.ServiceName;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Structure;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import sun.java2d.marlin.MarlinConst;
import sun.util.locale.LanguageTag;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/Native.class */
public final class Native {
    private static final String VERSION = "3.3.0";
    private static boolean unpacked;
    public static final int POINTER_SIZE;
    public static final int LONG_SIZE;
    public static final int WCHAR_SIZE;
    public static final int SIZE_T_SIZE;
    private static final int TYPE_VOIDP = 0;
    private static final int TYPE_LONG = 1;
    private static final int TYPE_WCHAR_T = 2;
    private static final int TYPE_SIZE_T = 3;
    private static final Object finalizer;
    private static final ThreadLocal lastError;
    private static Map registeredClasses;
    private static Map registeredLibraries;
    private static Object unloader;
    private static final int CVT_UNSUPPORTED = -1;
    private static final int CVT_DEFAULT = 0;
    private static final int CVT_POINTER = 1;
    private static final int CVT_STRING = 2;
    private static final int CVT_STRUCTURE = 3;
    private static final int CVT_STRUCTURE_BYVAL = 4;
    private static final int CVT_BUFFER = 5;
    private static final int CVT_ARRAY_BYTE = 6;
    private static final int CVT_ARRAY_SHORT = 7;
    private static final int CVT_ARRAY_CHAR = 8;
    private static final int CVT_ARRAY_INT = 9;
    private static final int CVT_ARRAY_LONG = 10;
    private static final int CVT_ARRAY_FLOAT = 11;
    private static final int CVT_ARRAY_DOUBLE = 12;
    private static final int CVT_ARRAY_BOOLEAN = 13;
    private static final int CVT_BOOLEAN = 14;
    private static final int CVT_CALLBACK = 15;
    private static final int CVT_FLOAT = 16;
    private static final int CVT_NATIVE_MAPPED = 17;
    private static final int CVT_WSTRING = 18;
    private static final int CVT_INTEGER_TYPE = 19;
    private static final int CVT_POINTER_TYPE = 20;
    private static final int CVT_TYPE_MAPPER = 21;
    static Class class$com$sun$jna$Native;
    static Class class$java$lang$ClassLoader;
    static Class class$com$sun$jna$Library;
    static Class class$com$sun$jna$Callback;
    static Class class$com$sun$jna$TypeMapper;
    static Class class$java$lang$String;
    static Class class$com$sun$jna$Structure;
    static Class class$com$sun$jna$Structure$ByReference;
    static Class class$com$sun$jna$NativeMapped;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Short;
    static Class class$java$lang$Character;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$com$sun$jna$Structure$ByValue;
    static Class class$com$sun$jna$Pointer;
    static Class class$java$nio$Buffer;
    static Class class$com$sun$jna$WString;
    static Class class$java$lang$Void;
    static Class class$com$sun$jna$IntegerType;
    static Class class$com$sun$jna$PointerType;
    static Class class$com$sun$jna$LastErrorException;
    private static String nativeLibraryPath = null;
    private static Map typeMappers = new WeakHashMap();
    private static Map alignments = new WeakHashMap();
    private static Map options = new WeakHashMap();
    private static Map libraries = new WeakHashMap();
    private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler() { // from class: com.sun.jna.Native.1
        @Override // com.sun.jna.Callback.UncaughtExceptionHandler
        public void uncaughtException(Callback c2, Throwable e2) {
            System.err.println(new StringBuffer().append("JNA: Callback ").append((Object) c2).append(" threw the following exception:").toString());
            e2.printStackTrace();
        }
    };
    private static Callback.UncaughtExceptionHandler callbackExceptionHandler = DEFAULT_HANDLER;

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Native$ffi_callback.class */
    public interface ffi_callback {
        void invoke(long j2, long j3, long j4);
    }

    private static native void initIDs();

    public static native synchronized void setProtected(boolean z2);

    public static native synchronized boolean isProtected();

    public static native synchronized void setPreserveLastError(boolean z2);

    public static native synchronized boolean getPreserveLastError();

    private static native long getWindowHandle0(Component component);

    private static native long _getDirectBufferPointer(Buffer buffer);

    private static native int sizeof(int i2);

    private static native String getNativeVersion();

    private static native String getAPIChecksum();

    public static native void setLastError(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void unregister(Class cls, long[] jArr);

    private static native long registerMethod(Class cls, String str, String str2, int[] iArr, long[] jArr, long[] jArr2, int i2, long j2, long j3, Class cls2, long j4, int i3, boolean z2, ToNativeConverter[] toNativeConverterArr, FromNativeConverter fromNativeConverter);

    public static native long ffi_prep_cif(int i2, int i3, long j2, long j3);

    public static native void ffi_call(long j2, long j3, long j4, long j5);

    public static native long ffi_prep_closure(long j2, ffi_callback ffi_callbackVar);

    public static native void ffi_free_closure(long j2);

    static native int initialize_ffi_type(long j2);

    static native synchronized void freeNativeCallback(long j2);

    static native synchronized long createNativeCallback(Callback callback, Method method, Class[] clsArr, Class cls, int i2, boolean z2);

    static native int invokeInt(long j2, int i2, Object[] objArr);

    static native long invokeLong(long j2, int i2, Object[] objArr);

    static native void invokeVoid(long j2, int i2, Object[] objArr);

    static native float invokeFloat(long j2, int i2, Object[] objArr);

    static native double invokeDouble(long j2, int i2, Object[] objArr);

    static native long invokePointer(long j2, int i2, Object[] objArr);

    private static native void invokeStructure(long j2, int i2, Object[] objArr, long j3, long j4);

    static native Object invokeObject(long j2, int i2, Object[] objArr);

    static native long open(String str);

    static native void close(long j2);

    static native long findSymbol(long j2, String str);

    static native long indexOf(long j2, byte b2);

    static native void read(long j2, byte[] bArr, int i2, int i3);

    static native void read(long j2, short[] sArr, int i2, int i3);

    static native void read(long j2, char[] cArr, int i2, int i3);

    static native void read(long j2, int[] iArr, int i2, int i3);

    static native void read(long j2, long[] jArr, int i2, int i3);

    static native void read(long j2, float[] fArr, int i2, int i3);

    static native void read(long j2, double[] dArr, int i2, int i3);

    static native void write(long j2, byte[] bArr, int i2, int i3);

    static native void write(long j2, short[] sArr, int i2, int i3);

    static native void write(long j2, char[] cArr, int i2, int i3);

    static native void write(long j2, int[] iArr, int i2, int i3);

    static native void write(long j2, long[] jArr, int i2, int i3);

    static native void write(long j2, float[] fArr, int i2, int i3);

    static native void write(long j2, double[] dArr, int i2, int i3);

    static native byte getByte(long j2);

    static native char getChar(long j2);

    static native short getShort(long j2);

    static native int getInt(long j2);

    static native long getLong(long j2);

    static native float getFloat(long j2);

    static native double getDouble(long j2);

    private static native long _getPointer(long j2);

    static native String getString(long j2, boolean z2);

    static native void setMemory(long j2, long j3, byte b2);

    static native void setByte(long j2, byte b2);

    static native void setShort(long j2, short s2);

    static native void setChar(long j2, char c2);

    static native void setInt(long j2, int i2);

    static native void setLong(long j2, long j3);

    static native void setFloat(long j2, float f2);

    static native void setDouble(long j2, double d2);

    static native void setPointer(long j2, long j3);

    static native void setString(long j2, String str, boolean z2);

    public static native long malloc(long j2);

    public static native void free(long j2);

    public static native ByteBuffer getDirectByteBuffer(long j2, long j3);

    static {
        loadNativeLibrary();
        POINTER_SIZE = sizeof(0);
        LONG_SIZE = sizeof(1);
        WCHAR_SIZE = sizeof(2);
        SIZE_T_SIZE = sizeof(3);
        initIDs();
        if (Boolean.getBoolean("jna.protected")) {
            setProtected(true);
        }
        finalizer = new Object() { // from class: com.sun.jna.Native.2
            protected void finalize() throws Throwable {
                Native.dispose();
            }
        };
        lastError = new ThreadLocal() { // from class: com.sun.jna.Native.3
            @Override // java.lang.ThreadLocal
            protected synchronized Object initialValue() {
                return new Integer(0);
            }
        };
        registeredClasses = new HashMap();
        registeredLibraries = new HashMap();
        unloader = new Object() { // from class: com.sun.jna.Native.7
            protected void finalize() {
                synchronized (Native.registeredClasses) {
                    Iterator i2 = Native.registeredClasses.entrySet().iterator();
                    while (i2.hasNext()) {
                        Map.Entry e2 = (Map.Entry) i2.next();
                        Native.unregister((Class) e2.getKey(), (long[]) e2.getValue());
                        i2.remove();
                    }
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dispose() throws Throwable {
        NativeLibrary.disposeAll();
        deleteNativeLibrary();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean deleteNativeLibrary() throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        String path = nativeLibraryPath;
        if (path == null || !unpacked) {
            return true;
        }
        File flib = new File(path);
        if (flib.delete()) {
            nativeLibraryPath = null;
            unpacked = false;
            return true;
        }
        try {
            if (class$com$sun$jna$Native == null) {
                clsClass$ = class$("com.sun.jna.Native");
                class$com$sun$jna$Native = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$Native;
            }
            ClassLoader cl = clsClass$.getClassLoader();
            if (class$java$lang$ClassLoader == null) {
                clsClass$2 = class$("java.lang.ClassLoader");
                class$java$lang$ClassLoader = clsClass$2;
            } else {
                clsClass$2 = class$java$lang$ClassLoader;
            }
            Field f2 = clsClass$2.getDeclaredField("nativeLibraries");
            f2.setAccessible(true);
            List libs = (List) f2.get(cl);
            for (Object lib : libs) {
                Field f3 = lib.getClass().getDeclaredField("name");
                f3.setAccessible(true);
                String name = (String) f3.get(lib);
                if (name.equals(path) || name.indexOf(path) != -1 || name.equals(flib.getCanonicalPath())) {
                    Method m2 = lib.getClass().getDeclaredMethod("finalize", new Class[0]);
                    m2.setAccessible(true);
                    m2.invoke(lib, new Object[0]);
                    nativeLibraryPath = null;
                    if (unpacked && flib.exists()) {
                        if (flib.delete()) {
                            unpacked = false;
                            return true;
                        }
                        return false;
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private Native() {
    }

    public static long getWindowID(Window w2) throws HeadlessException {
        return getComponentID(w2);
    }

    public static long getComponentID(Component c2) throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException("No native windows when headless");
        }
        if (c2.isLightweight()) {
            throw new IllegalArgumentException("Component must be heavyweight");
        }
        if (!c2.isDisplayable()) {
            throw new IllegalStateException("Component must be displayable");
        }
        if (Platform.isX11() && System.getProperty("java.version").startsWith(ServiceName.JMX_SPEC_VERSION) && !c2.isVisible()) {
            throw new IllegalStateException("Component must be visible");
        }
        return getWindowHandle0(c2);
    }

    public static Pointer getWindowPointer(Window w2) throws HeadlessException {
        return getComponentPointer(w2);
    }

    public static Pointer getComponentPointer(Component c2) throws HeadlessException {
        return new Pointer(getComponentID(c2));
    }

    public static Pointer getDirectBufferPointer(Buffer b2) {
        long peer = _getDirectBufferPointer(b2);
        if (peer == 0) {
            return null;
        }
        return new Pointer(peer);
    }

    public static String toString(byte[] buf) {
        return toString(buf, System.getProperty("jna.encoding"));
    }

    public static String toString(byte[] buf, String encoding) {
        String s2 = null;
        if (encoding != null) {
            try {
                s2 = new String(buf, encoding);
            } catch (UnsupportedEncodingException e2) {
            }
        }
        if (s2 == null) {
            s2 = new String(buf);
        }
        int term = s2.indexOf(0);
        if (term != -1) {
            s2 = s2.substring(0, term);
        }
        return s2;
    }

    public static String toString(char[] buf) {
        String s2 = new String(buf);
        int term = s2.indexOf(0);
        if (term != -1) {
            s2 = s2.substring(0, term);
        }
        return s2;
    }

    public static Object loadLibrary(Class interfaceClass) {
        return loadLibrary((String) null, interfaceClass);
    }

    public static Object loadLibrary(Class interfaceClass, Map options2) {
        return loadLibrary(null, interfaceClass, options2);
    }

    public static Object loadLibrary(String name, Class interfaceClass) {
        return loadLibrary(name, interfaceClass, Collections.EMPTY_MAP);
    }

    public static Object loadLibrary(String name, Class interfaceClass, Map options2) {
        Library.Handler handler = new Library.Handler(name, interfaceClass, options2);
        ClassLoader loader = interfaceClass.getClassLoader();
        Library proxy = (Library) Proxy.newProxyInstance(loader, new Class[]{interfaceClass}, handler);
        cacheOptions(interfaceClass, options2, proxy);
        return proxy;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0033, code lost:
    
        com.sun.jna.Native.libraries.put(r7, new java.lang.ref.WeakReference(r0.get(null)));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void loadLibraryInstance(java.lang.Class r7) {
        /*
            r0 = r7
            if (r0 == 0) goto L7f
            java.util.Map r0 = com.sun.jna.Native.libraries
            r1 = r7
            boolean r0 = r0.containsKey(r1)
            if (r0 != 0) goto L7f
            r0 = r7
            java.lang.reflect.Field[] r0 = r0.getFields()     // Catch: java.lang.Exception -> L55
            r8 = r0
            r0 = 0
            r9 = r0
        L17:
            r0 = r9
            r1 = r8
            int r1 = r1.length     // Catch: java.lang.Exception -> L55
            if (r0 >= r1) goto L52
            r0 = r8
            r1 = r9
            r0 = r0[r1]     // Catch: java.lang.Exception -> L55
            r10 = r0
            r0 = r10
            java.lang.Class r0 = r0.getType()     // Catch: java.lang.Exception -> L55
            r1 = r7
            if (r0 != r1) goto L4c
            r0 = r10
            int r0 = r0.getModifiers()     // Catch: java.lang.Exception -> L55
            boolean r0 = java.lang.reflect.Modifier.isStatic(r0)     // Catch: java.lang.Exception -> L55
            if (r0 == 0) goto L4c
            java.util.Map r0 = com.sun.jna.Native.libraries     // Catch: java.lang.Exception -> L55
            r1 = r7
            java.lang.ref.WeakReference r2 = new java.lang.ref.WeakReference     // Catch: java.lang.Exception -> L55
            r3 = r2
            r4 = r10
            r5 = 0
            java.lang.Object r4 = r4.get(r5)     // Catch: java.lang.Exception -> L55
            r3.<init>(r4)     // Catch: java.lang.Exception -> L55
            java.lang.Object r0 = r0.put(r1, r2)     // Catch: java.lang.Exception -> L55
            goto L52
        L4c:
            int r9 = r9 + 1
            goto L17
        L52:
            goto L7f
        L55:
            r8 = move-exception
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "Could not access instance of "
            java.lang.StringBuffer r2 = r2.append(r3)
            r3 = r7
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.String r3 = " ("
            java.lang.StringBuffer r2 = r2.append(r3)
            r3 = r8
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.String r3 = ")"
            java.lang.StringBuffer r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L7f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Native.loadLibraryInstance(java.lang.Class):void");
    }

    static Class findEnclosingLibraryClass(Class cls) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        if (cls == null) {
            return null;
        }
        synchronized (libraries) {
            if (options.containsKey(cls)) {
                return cls;
            }
            if (class$com$sun$jna$Library == null) {
                clsClass$ = class$("com.sun.jna.Library");
                class$com$sun$jna$Library = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$Library;
            }
            if (clsClass$.isAssignableFrom(cls)) {
                return cls;
            }
            if (class$com$sun$jna$Callback == null) {
                clsClass$2 = class$("com.sun.jna.Callback");
                class$com$sun$jna$Callback = clsClass$2;
            } else {
                clsClass$2 = class$com$sun$jna$Callback;
            }
            if (clsClass$2.isAssignableFrom(cls)) {
                cls = CallbackReference.findCallbackClass(cls);
            }
            Class declaring = cls.getDeclaringClass();
            Class fromDeclaring = findEnclosingLibraryClass(declaring);
            if (fromDeclaring != null) {
                return fromDeclaring;
            }
            return findEnclosingLibraryClass(cls.getSuperclass());
        }
    }

    public static Map getLibraryOptions(Class type) {
        Map map;
        synchronized (libraries) {
            Class interfaceClass = findEnclosingLibraryClass(type);
            if (interfaceClass != null) {
                loadLibraryInstance(interfaceClass);
            } else {
                interfaceClass = type;
            }
            if (!options.containsKey(interfaceClass)) {
                try {
                    Field field = interfaceClass.getField("OPTIONS");
                    field.setAccessible(true);
                    options.put(interfaceClass, field.get(null));
                } catch (NoSuchFieldException e2) {
                } catch (Exception e3) {
                    throw new IllegalArgumentException(new StringBuffer().append("OPTIONS must be a public field of type java.util.Map (").append((Object) e3).append("): ").append((Object) interfaceClass).toString());
                }
            }
            map = (Map) options.get(interfaceClass);
        }
        return map;
    }

    public static TypeMapper getTypeMapper(Class cls) {
        Class clsClass$;
        TypeMapper typeMapper;
        synchronized (libraries) {
            Class interfaceClass = findEnclosingLibraryClass(cls);
            if (interfaceClass != null) {
                loadLibraryInstance(interfaceClass);
            } else {
                interfaceClass = cls;
            }
            if (!typeMappers.containsKey(interfaceClass)) {
                try {
                    Field field = interfaceClass.getField("TYPE_MAPPER");
                    field.setAccessible(true);
                    typeMappers.put(interfaceClass, field.get(null));
                } catch (NoSuchFieldException e2) {
                    Map options2 = getLibraryOptions(cls);
                    if (options2 != null && options2.containsKey(Library.OPTION_TYPE_MAPPER)) {
                        typeMappers.put(interfaceClass, options2.get(Library.OPTION_TYPE_MAPPER));
                    }
                } catch (Exception e3) {
                    StringBuffer stringBufferAppend = new StringBuffer().append("TYPE_MAPPER must be a public field of type ");
                    if (class$com$sun$jna$TypeMapper == null) {
                        clsClass$ = class$("com.sun.jna.TypeMapper");
                        class$com$sun$jna$TypeMapper = clsClass$;
                    } else {
                        clsClass$ = class$com$sun$jna$TypeMapper;
                    }
                    throw new IllegalArgumentException(stringBufferAppend.append(clsClass$.getName()).append(" (").append((Object) e3).append("): ").append((Object) interfaceClass).toString());
                }
            }
            typeMapper = (TypeMapper) typeMappers.get(interfaceClass);
        }
        return typeMapper;
    }

    public static int getStructureAlignment(Class cls) {
        int iIntValue;
        synchronized (libraries) {
            Class interfaceClass = findEnclosingLibraryClass(cls);
            if (interfaceClass != null) {
                loadLibraryInstance(interfaceClass);
            } else {
                interfaceClass = cls;
            }
            if (!alignments.containsKey(interfaceClass)) {
                try {
                    Field field = interfaceClass.getField("STRUCTURE_ALIGNMENT");
                    field.setAccessible(true);
                    alignments.put(interfaceClass, field.get(null));
                } catch (NoSuchFieldException e2) {
                    Map options2 = getLibraryOptions(interfaceClass);
                    if (options2 != null && options2.containsKey(Library.OPTION_STRUCTURE_ALIGNMENT)) {
                        alignments.put(interfaceClass, options2.get(Library.OPTION_STRUCTURE_ALIGNMENT));
                    }
                } catch (Exception e3) {
                    throw new IllegalArgumentException(new StringBuffer().append("STRUCTURE_ALIGNMENT must be a public field of type int (").append((Object) e3).append("): ").append((Object) interfaceClass).toString());
                }
            }
            Integer value = (Integer) alignments.get(interfaceClass);
            iIntValue = value != null ? value.intValue() : 0;
        }
        return iIntValue;
    }

    static byte[] getBytes(String s2) {
        try {
            return getBytes(s2, System.getProperty("jna.encoding"));
        } catch (UnsupportedEncodingException e2) {
            return s2.getBytes();
        }
    }

    static byte[] getBytes(String s2, String encoding) throws UnsupportedEncodingException {
        if (encoding != null) {
            return s2.getBytes(encoding);
        }
        return s2.getBytes();
    }

    public static byte[] toByteArray(String s2) {
        byte[] bytes = getBytes(s2);
        byte[] buf = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
        return buf;
    }

    public static byte[] toByteArray(String s2, String encoding) throws UnsupportedEncodingException {
        byte[] bytes = getBytes(s2, encoding);
        byte[] buf = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
        return buf;
    }

    public static char[] toCharArray(String s2) {
        char[] chars = s2.toCharArray();
        char[] buf = new char[chars.length + 1];
        System.arraycopy(chars, 0, buf, 0, chars.length);
        return buf;
    }

    static String getNativeLibraryResourcePath(int osType, String arch, String name) {
        String osPrefix;
        String arch2 = arch.toLowerCase();
        switch (osType) {
            case 0:
                osPrefix = "darwin";
                break;
            case 1:
                if ("x86".equals(arch2)) {
                    arch2 = "i386";
                } else if ("x86_64".equals(arch2)) {
                    arch2 = "amd64";
                }
                osPrefix = new StringBuffer().append("linux-").append(arch2).toString();
                break;
            case 2:
                if ("i386".equals(arch2)) {
                    arch2 = "x86";
                }
                osPrefix = new StringBuffer().append("win32-").append(arch2).toString();
                break;
            case 3:
                osPrefix = new StringBuffer().append("sunos-").append(arch2).toString();
                break;
            default:
                String osPrefix2 = name.toLowerCase();
                if ("x86".equals(arch2)) {
                    arch2 = "i386";
                }
                if ("x86_64".equals(arch2)) {
                    arch2 = "amd64";
                }
                if ("powerpc".equals(arch2)) {
                    arch2 = "ppc";
                }
                int space = osPrefix2.indexOf(" ");
                if (space != -1) {
                    osPrefix2 = osPrefix2.substring(0, space);
                }
                osPrefix = new StringBuffer().append(osPrefix2).append(LanguageTag.SEP).append(arch2).toString();
                break;
        }
        return new StringBuffer().append("/com/sun/jna/").append(osPrefix).toString();
    }

    private static void loadNativeLibrary() throws Throwable {
        String orig;
        String ext;
        String bootPath = System.getProperty("jna.boot.library.path");
        if (bootPath != null) {
            String[] dirs = bootPath.split(File.pathSeparator);
            for (String str : dirs) {
                String path = new File(new File(str), System.mapLibraryName("jnidispatch")).getAbsolutePath();
                try {
                    System.load(path);
                    nativeLibraryPath = path;
                    return;
                } catch (UnsatisfiedLinkError e2) {
                    if (Platform.isMac()) {
                        if (path.endsWith("dylib")) {
                            orig = "dylib";
                            ext = "jnilib";
                        } else {
                            orig = "jnilib";
                            ext = "dylib";
                        }
                        try {
                            String path2 = new StringBuffer().append(path.substring(0, path.lastIndexOf(orig))).append(ext).toString();
                            System.load(path2);
                            nativeLibraryPath = path2;
                            return;
                        } catch (UnsatisfiedLinkError e3) {
                        }
                    }
                }
            }
        }
        try {
            System.loadLibrary("jnidispatch");
            nativeLibraryPath = "jnidispatch";
        } catch (UnsatisfiedLinkError e4) {
            loadNativeLibraryFromJar();
        }
    }

    private static void loadNativeLibraryFromJar() throws Throwable {
        Class clsClass$;
        File lib;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        String libname = System.mapLibraryName("jnidispatch");
        String arch = System.getProperty("os.arch");
        String name = System.getProperty("os.name");
        String resourceName = new StringBuffer().append(getNativeLibraryResourcePath(Platform.getOSType(), arch, name)).append("/").append(libname).toString();
        if (class$com$sun$jna$Native == null) {
            clsClass$ = class$("com.sun.jna.Native");
            class$com$sun$jna$Native = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Native;
        }
        URL url = clsClass$.getResource(resourceName);
        if (url == null && Platform.isMac() && resourceName.endsWith(".dylib")) {
            resourceName = new StringBuffer().append(resourceName.substring(0, resourceName.lastIndexOf(".dylib"))).append(".jnilib").toString();
            if (class$com$sun$jna$Native == null) {
                clsClass$4 = class$("com.sun.jna.Native");
                class$com$sun$jna$Native = clsClass$4;
            } else {
                clsClass$4 = class$com$sun$jna$Native;
            }
            url = clsClass$4.getResource(resourceName);
        }
        if (url == null) {
            throw new UnsatisfiedLinkError(new StringBuffer().append("jnidispatch (").append(resourceName).append(") not found in resource path").toString());
        }
        if (url.getProtocol().toLowerCase().equals(DeploymentDescriptorParser.ATTR_FILE)) {
            try {
                lib = new File(new URI(url.toString()));
            } catch (URISyntaxException e2) {
                lib = new File(url.getPath());
            }
            if (!lib.exists()) {
                throw new Error(new StringBuffer().append("File URL ").append((Object) url).append(" could not be properly decoded").toString());
            }
        } else {
            if (class$com$sun$jna$Native == null) {
                clsClass$2 = class$("com.sun.jna.Native");
                class$com$sun$jna$Native = clsClass$2;
            } else {
                clsClass$2 = class$com$sun$jna$Native;
            }
            InputStream is = clsClass$2.getResourceAsStream(resourceName);
            if (is == null) {
                throw new Error("Can't obtain jnidispatch InputStream");
            }
            FileOutputStream fos = null;
            try {
                try {
                    lib = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null);
                    lib.deleteOnExit();
                    if (class$com$sun$jna$Native == null) {
                        clsClass$3 = class$("com.sun.jna.Native");
                        class$com$sun$jna$Native = clsClass$3;
                    } else {
                        clsClass$3 = class$com$sun$jna$Native;
                    }
                    ClassLoader cl = clsClass$3.getClassLoader();
                    if (Platform.deleteNativeLibraryAfterVMExit() && (cl == null || cl.equals(ClassLoader.getSystemClassLoader()))) {
                        Runtime.getRuntime().addShutdownHook(new DeleteNativeLibrary(lib));
                    }
                    fos = new FileOutputStream(lib);
                    byte[] buf = new byte[1024];
                    while (true) {
                        int count = is.read(buf, 0, buf.length);
                        if (count <= 0) {
                            break;
                        } else {
                            fos.write(buf, 0, count);
                        }
                    }
                    try {
                        is.close();
                    } catch (IOException e3) {
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e4) {
                        }
                    }
                    unpacked = true;
                } catch (IOException e5) {
                    throw new Error(new StringBuffer().append("Failed to create temporary file for jnidispatch library: ").append((Object) e5).toString());
                }
            } catch (Throwable th) {
                try {
                    is.close();
                } catch (IOException e6) {
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e7) {
                    }
                }
                throw th;
            }
        }
        System.load(lib.getAbsolutePath());
        nativeLibraryPath = lib.getAbsolutePath();
    }

    public static int getLastError() {
        return ((Integer) lastError.get()).intValue();
    }

    static void updateLastError(int e2) {
        lastError.set(new Integer(e2));
    }

    public static Library synchronizedLibrary(Library library) throws IllegalArgumentException {
        Class cls = library.getClass();
        if (!Proxy.isProxyClass(cls)) {
            throw new IllegalArgumentException("Library must be a proxy class");
        }
        InvocationHandler ih = Proxy.getInvocationHandler(library);
        if (!(ih instanceof Library.Handler)) {
            throw new IllegalArgumentException(new StringBuffer().append("Unrecognized proxy handler: ").append((Object) ih).toString());
        }
        Library.Handler handler = (Library.Handler) ih;
        InvocationHandler newHandler = new InvocationHandler(handler, library) { // from class: com.sun.jna.Native.4
            private final Library.Handler val$handler;
            private final Library val$library;

            {
                this.val$handler = handler;
                this.val$library = library;
            }

            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object objInvoke;
                synchronized (this.val$handler.getNativeLibrary()) {
                    objInvoke = this.val$handler.invoke(this.val$library, method, args);
                }
                return objInvoke;
            }
        };
        return (Library) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), newHandler);
    }

    public static String getWebStartLibraryPath(String libName) {
        Class clsClass$;
        if (System.getProperty("javawebstart.version") == null) {
            return null;
        }
        try {
            if (class$com$sun$jna$Native == null) {
                clsClass$ = class$("com.sun.jna.Native");
                class$com$sun$jna$Native = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$Native;
            }
            ClassLoader cl = clsClass$.getClassLoader();
            Method m2 = (Method) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.jna.Native.5
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() throws Throwable {
                    Class clsClass$2;
                    Class<?> clsClass$3;
                    try {
                        if (Native.class$java$lang$ClassLoader == null) {
                            clsClass$2 = Native.class$("java.lang.ClassLoader");
                            Native.class$java$lang$ClassLoader = clsClass$2;
                        } else {
                            clsClass$2 = Native.class$java$lang$ClassLoader;
                        }
                        Class<?>[] clsArr = new Class[1];
                        if (Native.class$java$lang$String == null) {
                            clsClass$3 = Native.class$("java.lang.String");
                            Native.class$java$lang$String = clsClass$3;
                        } else {
                            clsClass$3 = Native.class$java$lang$String;
                        }
                        clsArr[0] = clsClass$3;
                        Method m3 = clsClass$2.getDeclaredMethod("findLibrary", clsArr);
                        m3.setAccessible(true);
                        return m3;
                    } catch (Exception e2) {
                        return null;
                    }
                }
            });
            String libpath = (String) m2.invoke(cl, libName);
            if (libpath != null) {
                return new File(libpath).getParent();
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Native$DeleteNativeLibrary.class */
    public static class DeleteNativeLibrary extends Thread {
        private final File file;

        public DeleteNativeLibrary(File file) {
            this.file = file;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (!Native.deleteNativeLibrary()) {
                try {
                    Runtime.getRuntime().exec(new String[]{new StringBuffer().append(System.getProperty("java.home")).append("/bin/java").toString(), "-cp", System.getProperty("java.class.path"), getClass().getName(), this.file.getAbsolutePath()});
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }

        public static void main(String[] args) {
            if (args.length == 1) {
                File file = new File(args[0]);
                if (file.exists()) {
                    long start = System.currentTimeMillis();
                    while (true) {
                        if (file.delete() || !file.exists()) {
                            break;
                        }
                        try {
                            Thread.sleep(10L);
                        } catch (InterruptedException e2) {
                        }
                        if (System.currentTimeMillis() - start > MarlinConst.statDump) {
                            System.err.println(new StringBuffer().append("Could not remove temp file: ").append(file.getAbsolutePath()).toString());
                            break;
                        }
                    }
                }
            }
            System.exit(0);
        }
    }

    public static int getNativeSize(Class type, Object value) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        if (type.isArray()) {
            int len = Array.getLength(value);
            if (len > 0) {
                Object o2 = Array.get(value, 0);
                return len * getNativeSize(type.getComponentType(), o2);
            }
            throw new IllegalArgumentException(new StringBuffer().append("Arrays of length zero not allowed: ").append((Object) type).toString());
        }
        if (class$com$sun$jna$Structure == null) {
            clsClass$ = class$("com.sun.jna.Structure");
            class$com$sun$jna$Structure = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Structure;
        }
        if (clsClass$.isAssignableFrom(type)) {
            if (class$com$sun$jna$Structure$ByReference == null) {
                clsClass$2 = class$("com.sun.jna.Structure$ByReference");
                class$com$sun$jna$Structure$ByReference = clsClass$2;
            } else {
                clsClass$2 = class$com$sun$jna$Structure$ByReference;
            }
            if (!clsClass$2.isAssignableFrom(type)) {
                if (value == null) {
                    value = Structure.newInstance(type);
                }
                return ((Structure) value).size();
            }
        }
        try {
            return getNativeSize(type);
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException(new StringBuffer().append("The type \"").append(type.getName()).append("\" is not supported: ").append(e2.getMessage()).toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int getNativeSize(Class cls) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class clsClass$10;
        Class clsClass$11;
        Class cls6;
        if (class$com$sun$jna$NativeMapped == null) {
            clsClass$ = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$NativeMapped;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            cls = NativeMappedConverter.getInstance(cls).nativeType();
        }
        if (cls == Boolean.TYPE) {
            return 4;
        }
        Class cls7 = cls;
        if (class$java$lang$Boolean == null) {
            clsClass$2 = class$(Constants.BOOLEAN_CLASS);
            class$java$lang$Boolean = clsClass$2;
        } else {
            clsClass$2 = class$java$lang$Boolean;
        }
        if (cls7 == clsClass$2) {
            return 4;
        }
        if (cls == Byte.TYPE) {
            return 1;
        }
        Class cls8 = cls;
        if (class$java$lang$Byte == null) {
            clsClass$3 = class$("java.lang.Byte");
            class$java$lang$Byte = clsClass$3;
        } else {
            clsClass$3 = class$java$lang$Byte;
        }
        if (cls8 == clsClass$3) {
            return 1;
        }
        if (cls == Short.TYPE) {
            return 2;
        }
        Class cls9 = cls;
        if (class$java$lang$Short == null) {
            clsClass$4 = class$("java.lang.Short");
            class$java$lang$Short = clsClass$4;
        } else {
            clsClass$4 = class$java$lang$Short;
        }
        if (cls9 == clsClass$4) {
            return 2;
        }
        if (cls != Character.TYPE) {
            Class cls10 = cls;
            if (class$java$lang$Character == null) {
                clsClass$5 = class$("java.lang.Character");
                class$java$lang$Character = clsClass$5;
            } else {
                clsClass$5 = class$java$lang$Character;
            }
            if (cls10 != clsClass$5) {
                if (cls == Integer.TYPE) {
                    return 4;
                }
                Class cls11 = cls;
                if (class$java$lang$Integer == null) {
                    clsClass$6 = class$(Constants.INTEGER_CLASS);
                    class$java$lang$Integer = clsClass$6;
                } else {
                    clsClass$6 = class$java$lang$Integer;
                }
                if (cls11 == clsClass$6) {
                    return 4;
                }
                if (cls == Long.TYPE) {
                    return 8;
                }
                Class cls12 = cls;
                if (class$java$lang$Long == null) {
                    clsClass$7 = class$("java.lang.Long");
                    class$java$lang$Long = clsClass$7;
                } else {
                    clsClass$7 = class$java$lang$Long;
                }
                if (cls12 == clsClass$7) {
                    return 8;
                }
                if (cls == Float.TYPE) {
                    return 4;
                }
                Class cls13 = cls;
                if (class$java$lang$Float == null) {
                    clsClass$8 = class$("java.lang.Float");
                    class$java$lang$Float = clsClass$8;
                } else {
                    clsClass$8 = class$java$lang$Float;
                }
                if (cls13 == clsClass$8) {
                    return 4;
                }
                if (cls == Double.TYPE) {
                    return 8;
                }
                Class cls14 = cls;
                if (class$java$lang$Double == null) {
                    clsClass$9 = class$(Constants.DOUBLE_CLASS);
                    class$java$lang$Double = clsClass$9;
                } else {
                    clsClass$9 = class$java$lang$Double;
                }
                if (cls14 == clsClass$9) {
                    return 8;
                }
                if (class$com$sun$jna$Structure == null) {
                    Class clsClass$12 = class$("com.sun.jna.Structure");
                    class$com$sun$jna$Structure = clsClass$12;
                    cls2 = clsClass$12;
                } else {
                    cls2 = class$com$sun$jna$Structure;
                }
                if (cls2.isAssignableFrom(cls)) {
                    if (class$com$sun$jna$Structure$ByValue == null) {
                        Class clsClass$13 = class$("com.sun.jna.Structure$ByValue");
                        class$com$sun$jna$Structure$ByValue = clsClass$13;
                        cls6 = clsClass$13;
                    } else {
                        cls6 = class$com$sun$jna$Structure$ByValue;
                    }
                    if (cls6.isAssignableFrom(cls)) {
                        return Structure.newInstance(cls).size();
                    }
                    return POINTER_SIZE;
                }
                if (class$com$sun$jna$Pointer == null) {
                    Class clsClass$14 = class$("com.sun.jna.Pointer");
                    class$com$sun$jna$Pointer = clsClass$14;
                    cls3 = clsClass$14;
                } else {
                    cls3 = class$com$sun$jna$Pointer;
                }
                if (!cls3.isAssignableFrom(cls)) {
                    if (class$java$nio$Buffer == null) {
                        Class clsClass$15 = class$("java.nio.Buffer");
                        class$java$nio$Buffer = clsClass$15;
                        cls4 = clsClass$15;
                    } else {
                        cls4 = class$java$nio$Buffer;
                    }
                    if (!cls4.isAssignableFrom(cls)) {
                        if (class$com$sun$jna$Callback == null) {
                            Class clsClass$16 = class$("com.sun.jna.Callback");
                            class$com$sun$jna$Callback = clsClass$16;
                            cls5 = clsClass$16;
                        } else {
                            cls5 = class$com$sun$jna$Callback;
                        }
                        if (!cls5.isAssignableFrom(cls)) {
                            if (class$java$lang$String == null) {
                                clsClass$10 = class$("java.lang.String");
                                class$java$lang$String = clsClass$10;
                            } else {
                                clsClass$10 = class$java$lang$String;
                            }
                            if (clsClass$10 != cls) {
                                if (class$com$sun$jna$WString == null) {
                                    clsClass$11 = class$("com.sun.jna.WString");
                                    class$com$sun$jna$WString = clsClass$11;
                                } else {
                                    clsClass$11 = class$com$sun$jna$WString;
                                }
                                if (clsClass$11 != cls) {
                                    throw new IllegalArgumentException(new StringBuffer().append("Native size for type \"").append(cls.getName()).append("\" is unknown").toString());
                                }
                            }
                        }
                    }
                }
                return POINTER_SIZE;
            }
        }
        return WCHAR_SIZE;
    }

    public static boolean isSupportedNativeType(Class cls) throws Throwable {
        Class clsClass$;
        if (class$com$sun$jna$Structure == null) {
            clsClass$ = class$("com.sun.jna.Structure");
            class$com$sun$jna$Structure = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Structure;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            return true;
        }
        try {
            return getNativeSize(cls) != 0;
        } catch (IllegalArgumentException e2) {
            return false;
        }
    }

    public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler eh) {
        callbackExceptionHandler = eh == null ? DEFAULT_HANDLER : eh;
    }

    public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
        return callbackExceptionHandler;
    }

    public static void register(String libName) throws Throwable {
        register(getNativeClass(getCallingClass()), NativeLibrary.getInstance(libName));
    }

    public static void register(NativeLibrary lib) throws Throwable {
        register(getNativeClass(getCallingClass()), lib);
    }

    static Class getNativeClass(Class cls) throws SecurityException {
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if ((method.getModifiers() & 256) != 0) {
                return cls;
            }
        }
        int idx = cls.getName().lastIndexOf(FXMLLoader.EXPRESSION_PREFIX);
        if (idx != -1) {
            String name = cls.getName().substring(0, idx);
            try {
                return getNativeClass(Class.forName(name, true, cls.getClassLoader()));
            } catch (ClassNotFoundException e2) {
            }
        }
        throw new IllegalArgumentException(new StringBuffer().append("Can't determine class with native methods from the current context (").append((Object) cls).append(")").toString());
    }

    static Class getCallingClass() {
        Class[] context = new SecurityManager() { // from class: com.sun.jna.Native.6
            @Override // java.lang.SecurityManager
            public Class[] getClassContext() {
                return super.getClassContext();
            }
        }.getClassContext();
        if (context.length < 4) {
            throw new IllegalStateException("This method must be called from the static initializer of a class");
        }
        return context[3];
    }

    public static void unregister() {
        unregister(getNativeClass(getCallingClass()));
    }

    public static void unregister(Class cls) {
        synchronized (registeredClasses) {
            if (registeredClasses.containsKey(cls)) {
                unregister(cls, (long[]) registeredClasses.get(cls));
                registeredClasses.remove(cls);
                registeredLibraries.remove(cls);
            }
        }
    }

    private static String getSignature(Class cls) {
        if (cls.isArray()) {
            return new StringBuffer().append("[").append(getSignature(cls.getComponentType())).toString();
        }
        if (cls.isPrimitive()) {
            if (cls == Void.TYPE) {
                return "V";
            }
            if (cls == Boolean.TYPE) {
                return Constants.HASIDCALL_INDEX_SIG;
            }
            if (cls == Byte.TYPE) {
                return PdfOps.B_TOKEN;
            }
            if (cls == Short.TYPE) {
                return PdfOps.S_TOKEN;
            }
            if (cls == Character.TYPE) {
                return "C";
            }
            if (cls == Integer.TYPE) {
                return "I";
            }
            if (cls == Long.TYPE) {
                return "J";
            }
            if (cls == Float.TYPE) {
                return PdfOps.F_TOKEN;
            }
            if (cls == Double.TYPE) {
                return PdfOps.D_TOKEN;
            }
        }
        return new StringBuffer().append("L").append(replace(".", "/", cls.getName())).append(";").toString();
    }

    static String replace(String s1, String s2, String str) {
        StringBuffer buf = new StringBuffer();
        while (true) {
            int idx = str.indexOf(s1);
            if (idx == -1) {
                buf.append(str);
                return buf.toString();
            }
            buf.append(str.substring(0, idx));
            buf.append(s2);
            str = str.substring(idx + s1.length());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static int getConversion(Class cls, TypeMapper typeMapper) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class cls2;
        Class clsClass$10;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        Class cls10;
        if (class$java$lang$Boolean == null) {
            clsClass$ = class$(Constants.BOOLEAN_CLASS);
            class$java$lang$Boolean = clsClass$;
        } else {
            clsClass$ = class$java$lang$Boolean;
        }
        if (cls == clsClass$) {
            cls = Boolean.TYPE;
        } else {
            if (class$java$lang$Byte == null) {
                clsClass$2 = class$("java.lang.Byte");
                class$java$lang$Byte = clsClass$2;
            } else {
                clsClass$2 = class$java$lang$Byte;
            }
            if (cls == clsClass$2) {
                cls = Byte.TYPE;
            } else {
                if (class$java$lang$Short == null) {
                    clsClass$3 = class$("java.lang.Short");
                    class$java$lang$Short = clsClass$3;
                } else {
                    clsClass$3 = class$java$lang$Short;
                }
                if (cls == clsClass$3) {
                    cls = Short.TYPE;
                } else {
                    if (class$java$lang$Character == null) {
                        clsClass$4 = class$("java.lang.Character");
                        class$java$lang$Character = clsClass$4;
                    } else {
                        clsClass$4 = class$java$lang$Character;
                    }
                    if (cls == clsClass$4) {
                        cls = Character.TYPE;
                    } else {
                        if (class$java$lang$Integer == null) {
                            clsClass$5 = class$(Constants.INTEGER_CLASS);
                            class$java$lang$Integer = clsClass$5;
                        } else {
                            clsClass$5 = class$java$lang$Integer;
                        }
                        if (cls == clsClass$5) {
                            cls = Integer.TYPE;
                        } else {
                            if (class$java$lang$Long == null) {
                                clsClass$6 = class$("java.lang.Long");
                                class$java$lang$Long = clsClass$6;
                            } else {
                                clsClass$6 = class$java$lang$Long;
                            }
                            if (cls == clsClass$6) {
                                cls = Long.TYPE;
                            } else {
                                if (class$java$lang$Float == null) {
                                    clsClass$7 = class$("java.lang.Float");
                                    class$java$lang$Float = clsClass$7;
                                } else {
                                    clsClass$7 = class$java$lang$Float;
                                }
                                if (cls == clsClass$7) {
                                    cls = Float.TYPE;
                                } else {
                                    if (class$java$lang$Double == null) {
                                        clsClass$8 = class$(Constants.DOUBLE_CLASS);
                                        class$java$lang$Double = clsClass$8;
                                    } else {
                                        clsClass$8 = class$java$lang$Double;
                                    }
                                    if (cls == clsClass$8) {
                                        cls = Double.TYPE;
                                    } else {
                                        if (class$java$lang$Void == null) {
                                            clsClass$9 = class$("java.lang.Void");
                                            class$java$lang$Void = clsClass$9;
                                        } else {
                                            clsClass$9 = class$java$lang$Void;
                                        }
                                        if (cls == clsClass$9) {
                                            cls = Void.TYPE;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (typeMapper != null && (typeMapper.getFromNativeConverter(cls) != null || typeMapper.getToNativeConverter(cls) != null)) {
            return 21;
        }
        if (class$com$sun$jna$Pointer == null) {
            Class clsClass$11 = class$("com.sun.jna.Pointer");
            class$com$sun$jna$Pointer = clsClass$11;
            cls2 = clsClass$11;
        } else {
            cls2 = class$com$sun$jna$Pointer;
        }
        if (cls2.isAssignableFrom(cls)) {
            return 1;
        }
        if (class$java$lang$String == null) {
            clsClass$10 = class$("java.lang.String");
            class$java$lang$String = clsClass$10;
        } else {
            clsClass$10 = class$java$lang$String;
        }
        if (clsClass$10 == cls) {
            return 2;
        }
        if (class$com$sun$jna$WString == null) {
            Class clsClass$12 = class$("com.sun.jna.WString");
            class$com$sun$jna$WString = clsClass$12;
            cls3 = clsClass$12;
        } else {
            cls3 = class$com$sun$jna$WString;
        }
        if (cls3.isAssignableFrom(cls)) {
            return 18;
        }
        if (class$java$nio$Buffer == null) {
            Class clsClass$13 = class$("java.nio.Buffer");
            class$java$nio$Buffer = clsClass$13;
            cls4 = clsClass$13;
        } else {
            cls4 = class$java$nio$Buffer;
        }
        if (cls4.isAssignableFrom(cls)) {
            return 5;
        }
        if (class$com$sun$jna$Structure == null) {
            Class clsClass$14 = class$("com.sun.jna.Structure");
            class$com$sun$jna$Structure = clsClass$14;
            cls5 = clsClass$14;
        } else {
            cls5 = class$com$sun$jna$Structure;
        }
        if (cls5.isAssignableFrom(cls)) {
            if (class$com$sun$jna$Structure$ByValue == null) {
                Class clsClass$15 = class$("com.sun.jna.Structure$ByValue");
                class$com$sun$jna$Structure$ByValue = clsClass$15;
                cls10 = clsClass$15;
            } else {
                cls10 = class$com$sun$jna$Structure$ByValue;
            }
            if (cls10.isAssignableFrom(cls)) {
                return 4;
            }
            return 3;
        }
        if (cls.isArray()) {
            switch (cls.getName().charAt(1)) {
                case 'B':
                    return 6;
                case 'C':
                    return 8;
                case 'D':
                    return 12;
                case 'F':
                    return 11;
                case 'I':
                    return 9;
                case 'J':
                    return 10;
                case 'S':
                    return 7;
                case 'Z':
                    return 13;
            }
        }
        if (cls.isPrimitive()) {
            return cls == Boolean.TYPE ? 14 : 0;
        }
        if (class$com$sun$jna$Callback == null) {
            Class clsClass$16 = class$("com.sun.jna.Callback");
            class$com$sun$jna$Callback = clsClass$16;
            cls6 = clsClass$16;
        } else {
            cls6 = class$com$sun$jna$Callback;
        }
        if (cls6.isAssignableFrom(cls)) {
            return 15;
        }
        if (class$com$sun$jna$IntegerType == null) {
            Class clsClass$17 = class$("com.sun.jna.IntegerType");
            class$com$sun$jna$IntegerType = clsClass$17;
            cls7 = clsClass$17;
        } else {
            cls7 = class$com$sun$jna$IntegerType;
        }
        if (cls7.isAssignableFrom(cls)) {
            return 19;
        }
        if (class$com$sun$jna$PointerType == null) {
            Class clsClass$18 = class$("com.sun.jna.PointerType");
            class$com$sun$jna$PointerType = clsClass$18;
            cls8 = clsClass$18;
        } else {
            cls8 = class$com$sun$jna$PointerType;
        }
        if (cls8.isAssignableFrom(cls)) {
            return 20;
        }
        if (class$com$sun$jna$NativeMapped == null) {
            Class clsClass$19 = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$19;
            cls9 = clsClass$19;
        } else {
            cls9 = class$com$sun$jna$NativeMapped;
        }
        if (cls9.isAssignableFrom(cls)) {
            return 17;
        }
        return -1;
    }

    public static void register(Class cls, NativeLibrary lib) throws Throwable {
        long rtype;
        long closure_rtype;
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Method[] methods = cls.getDeclaredMethods();
        List mlist = new ArrayList();
        TypeMapper mapper = (TypeMapper) lib.getOptions().get(Library.OPTION_TYPE_MAPPER);
        for (int i2 = 0; i2 < methods.length; i2++) {
            if ((methods[i2].getModifiers() & 256) != 0) {
                mlist.add(methods[i2]);
            }
        }
        long[] handles = new long[mlist.size()];
        for (int i3 = 0; i3 < handles.length; i3++) {
            Method method = (Method) mlist.get(i3);
            String sig = "(";
            Class rclass = method.getReturnType();
            Class[] ptypes = method.getParameterTypes();
            long[] atypes = new long[ptypes.length];
            long[] closure_atypes = new long[ptypes.length];
            int[] cvt = new int[ptypes.length];
            ToNativeConverter[] toNative = new ToNativeConverter[ptypes.length];
            FromNativeConverter fromNative = null;
            int rcvt = getConversion(rclass, mapper);
            boolean throwLastError = false;
            switch (rcvt) {
                case -1:
                    throw new IllegalArgumentException(new StringBuffer().append((Object) rclass).append(" is not a supported return type (in method ").append(method.getName()).append(" in ").append((Object) cls).append(")").toString());
                case 0:
                case 1:
                case 2:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 18:
                default:
                    long j2 = Structure.FFIType.get(rclass).peer;
                    rtype = j2;
                    closure_rtype = j2;
                    break;
                case 3:
                    if (class$com$sun$jna$Pointer == null) {
                        clsClass$2 = class$("com.sun.jna.Pointer");
                        class$com$sun$jna$Pointer = clsClass$2;
                    } else {
                        clsClass$2 = class$com$sun$jna$Pointer;
                    }
                    long j3 = Structure.FFIType.get(clsClass$2).peer;
                    rtype = j3;
                    closure_rtype = j3;
                    break;
                case 4:
                    if (class$com$sun$jna$Pointer == null) {
                        clsClass$ = class$("com.sun.jna.Pointer");
                        class$com$sun$jna$Pointer = clsClass$;
                    } else {
                        clsClass$ = class$com$sun$jna$Pointer;
                    }
                    closure_rtype = Structure.FFIType.get(clsClass$).peer;
                    rtype = Structure.FFIType.get(rclass).peer;
                    break;
                case 17:
                case 19:
                case 20:
                    if (class$com$sun$jna$Pointer == null) {
                        clsClass$3 = class$("com.sun.jna.Pointer");
                        class$com$sun$jna$Pointer = clsClass$3;
                    } else {
                        clsClass$3 = class$com$sun$jna$Pointer;
                    }
                    closure_rtype = Structure.FFIType.get(clsClass$3).peer;
                    rtype = Structure.FFIType.get(NativeMappedConverter.getInstance(rclass).nativeType()).peer;
                    break;
                case 21:
                    fromNative = mapper.getFromNativeConverter(rclass);
                    closure_rtype = Structure.FFIType.get(rclass).peer;
                    rtype = Structure.FFIType.get(fromNative.nativeType()).peer;
                    break;
            }
            for (int t2 = 0; t2 < ptypes.length; t2++) {
                Class type = ptypes[t2];
                sig = new StringBuffer().append(sig).append(getSignature(type)).toString();
                cvt[t2] = getConversion(type, mapper);
                if (cvt[t2] == -1) {
                    throw new IllegalArgumentException(new StringBuffer().append((Object) type).append(" is not a supported argument type (in method ").append(method.getName()).append(" in ").append((Object) cls).append(")").toString());
                }
                if (cvt[t2] == 17 || cvt[t2] == 19) {
                    type = NativeMappedConverter.getInstance(type).nativeType();
                } else if (cvt[t2] == 21) {
                    toNative[t2] = mapper.getToNativeConverter(type);
                }
                switch (cvt[t2]) {
                    case 0:
                        long j4 = Structure.FFIType.get(type).peer;
                        atypes[t2] = j4;
                        closure_atypes[t2] = j4;
                        break;
                    case 4:
                    case 17:
                    case 19:
                    case 20:
                        atypes[t2] = Structure.FFIType.get(type).peer;
                        int i4 = t2;
                        if (class$com$sun$jna$Pointer == null) {
                            clsClass$7 = class$("com.sun.jna.Pointer");
                            class$com$sun$jna$Pointer = clsClass$7;
                        } else {
                            clsClass$7 = class$com$sun$jna$Pointer;
                        }
                        closure_atypes[i4] = Structure.FFIType.get(clsClass$7).peer;
                        break;
                    case 21:
                        if (type.isPrimitive()) {
                            closure_atypes[t2] = Structure.FFIType.get(type).peer;
                        } else {
                            int i5 = t2;
                            if (class$com$sun$jna$Pointer == null) {
                                clsClass$6 = class$("com.sun.jna.Pointer");
                                class$com$sun$jna$Pointer = clsClass$6;
                            } else {
                                clsClass$6 = class$com$sun$jna$Pointer;
                            }
                            closure_atypes[i5] = Structure.FFIType.get(clsClass$6).peer;
                        }
                        atypes[t2] = Structure.FFIType.get(toNative[t2].nativeType()).peer;
                        break;
                    default:
                        int i6 = t2;
                        int i7 = t2;
                        if (class$com$sun$jna$Pointer == null) {
                            clsClass$5 = class$("com.sun.jna.Pointer");
                            class$com$sun$jna$Pointer = clsClass$5;
                        } else {
                            clsClass$5 = class$com$sun$jna$Pointer;
                        }
                        long j5 = Structure.FFIType.get(clsClass$5).peer;
                        atypes[i7] = j5;
                        closure_atypes[i6] = j5;
                        break;
                }
            }
            String sig2 = new StringBuffer().append(new StringBuffer().append(sig).append(")").toString()).append(getSignature(rclass)).toString();
            Class[] etypes = method.getExceptionTypes();
            int e2 = 0;
            while (true) {
                if (e2 < etypes.length) {
                    if (class$com$sun$jna$LastErrorException == null) {
                        clsClass$4 = class$("com.sun.jna.LastErrorException");
                        class$com$sun$jna$LastErrorException = clsClass$4;
                    } else {
                        clsClass$4 = class$com$sun$jna$LastErrorException;
                    }
                    if (!clsClass$4.isAssignableFrom(etypes[e2])) {
                        e2++;
                    } else {
                        throwLastError = true;
                    }
                }
            }
            String name = method.getName();
            FunctionMapper fmapper = (FunctionMapper) lib.getOptions().get(Library.OPTION_FUNCTION_MAPPER);
            if (fmapper != null) {
                name = fmapper.getFunctionName(lib, method);
            }
            Function f2 = lib.getFunction(name, method);
            try {
                handles[i3] = registerMethod(cls, method.getName(), sig2, cvt, closure_atypes, atypes, rcvt, closure_rtype, rtype, rclass, f2.peer, f2.getCallingConvention(), throwLastError, toNative, fromNative);
            } catch (NoSuchMethodError e3) {
                throw new UnsatisfiedLinkError(new StringBuffer().append("No method ").append(method.getName()).append(" with signature ").append(sig2).append(" in ").append((Object) cls).toString());
            }
        }
        synchronized (registeredClasses) {
            registeredClasses.put(cls, handles);
            registeredLibraries.put(cls, lib);
        }
        cacheOptions(cls, lib.getOptions(), null);
    }

    private static void cacheOptions(Class cls, Map libOptions, Object proxy) {
        Class clsClass$;
        Class clsClass$2;
        synchronized (libraries) {
            if (!libOptions.isEmpty()) {
                options.put(cls, libOptions);
            }
            if (libOptions.containsKey(Library.OPTION_TYPE_MAPPER)) {
                typeMappers.put(cls, libOptions.get(Library.OPTION_TYPE_MAPPER));
            }
            if (libOptions.containsKey(Library.OPTION_STRUCTURE_ALIGNMENT)) {
                alignments.put(cls, libOptions.get(Library.OPTION_STRUCTURE_ALIGNMENT));
            }
            if (proxy != null) {
                libraries.put(cls, new WeakReference(proxy));
            }
            if (!cls.isInterface()) {
                if (class$com$sun$jna$Library == null) {
                    clsClass$ = class$("com.sun.jna.Library");
                    class$com$sun$jna$Library = clsClass$;
                } else {
                    clsClass$ = class$com$sun$jna$Library;
                }
                if (clsClass$.isAssignableFrom(cls)) {
                    Class[] ifaces = cls.getInterfaces();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= ifaces.length) {
                            break;
                        }
                        if (class$com$sun$jna$Library == null) {
                            clsClass$2 = class$("com.sun.jna.Library");
                            class$com$sun$jna$Library = clsClass$2;
                        } else {
                            clsClass$2 = class$com$sun$jna$Library;
                        }
                        if (!clsClass$2.isAssignableFrom(ifaces[i2])) {
                            i2++;
                        } else {
                            cacheOptions(ifaces[i2], libOptions, proxy);
                            break;
                        }
                    }
                }
            }
        }
    }

    private static NativeMapped fromNative(Class cls, Object value) {
        return (NativeMapped) NativeMappedConverter.getInstance(cls).fromNative(value, new FromNativeContext(cls));
    }

    private static Class nativeType(Class cls) {
        return NativeMappedConverter.getInstance(cls).nativeType();
    }

    private static Object toNative(ToNativeConverter cvt, Object o2) {
        return cvt.toNative(o2, new ToNativeContext());
    }

    private static Object fromNative(FromNativeConverter cvt, Object o2, Class cls) {
        return cvt.fromNative(o2, new FromNativeContext(cls));
    }

    public static void main(String[] args) throws Throwable {
        Class clsClass$;
        if (class$com$sun$jna$Native == null) {
            clsClass$ = class$("com.sun.jna.Native");
            class$com$sun$jna$Native = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Native;
        }
        Package pkg = clsClass$.getPackage();
        String title = pkg != null ? pkg.getSpecificationTitle() : "Java Native Access (JNA)";
        if (title == null) {
            title = "Java Native Access (JNA)";
        }
        String version = pkg != null ? pkg.getSpecificationVersion() : VERSION;
        if (version == null) {
            version = VERSION;
        }
        System.out.println(new StringBuffer().append(title).append(" API Version ").append(version).toString());
        String version2 = pkg != null ? pkg.getImplementationVersion() : "3.3.0 (package information missing)";
        if (version2 == null) {
            version2 = "3.3.0 (package information missing)";
        }
        System.out.println(new StringBuffer().append("Version: ").append(version2).toString());
        System.out.println(new StringBuffer().append(" Native: ").append(getNativeVersion()).append(" (").append(getAPIChecksum()).append(")").toString());
        System.exit(0);
    }

    static Structure invokeStructure(long fp, int callFlags, Object[] args, Structure s2) {
        invokeStructure(fp, callFlags, args, s2.getPointer().peer, s2.getTypeInfo().peer);
        return s2;
    }

    static Pointer getPointer(long addr) {
        long peer = _getPointer(addr);
        if (peer == 0) {
            return null;
        }
        return new Pointer(peer);
    }
}
