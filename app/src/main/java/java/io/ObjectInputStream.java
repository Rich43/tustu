package java.io;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.ObjectStreamClass;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.misc.JavaOISAccess;
import sun.misc.ObjectInputFilter;
import sun.misc.ObjectStreamClassValidator;
import sun.misc.SharedSecrets;
import sun.misc.VM;
import sun.reflect.misc.ReflectUtil;
import sun.security.action.GetBooleanAction;
import sun.security.action.GetIntegerAction;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/io/ObjectInputStream.class */
public class ObjectInputStream extends InputStream implements ObjectInput, ObjectStreamConstants {
    private static final int NULL_HANDLE = -1;
    private static final Object unsharedMarker = new Object();
    private static final HashMap<String, Class<?>> primClasses = new HashMap<>(8, 1.0f);
    private final BlockDataInputStream bin;
    private final ValidationList vlist;
    private long depth;
    private long totalObjectRefs;
    private boolean closed;
    private final HandleTable handles;
    private int passHandle = -1;
    private boolean defaultDataEnd = false;
    private byte[] primVals;
    private final boolean enableOverride;
    private boolean enableResolve;
    private SerialCallbackContext curContext;
    private ObjectInputFilter serialFilter;
    private volatile ObjectStreamClassValidator validator;

    /* loaded from: rt.jar:java/io/ObjectInputStream$GetField.class */
    public static abstract class GetField {
        public abstract ObjectStreamClass getObjectStreamClass();

        public abstract boolean defaulted(String str) throws IOException;

        public abstract boolean get(String str, boolean z2) throws IOException;

        public abstract byte get(String str, byte b2) throws IOException;

        public abstract char get(String str, char c2) throws IOException;

        public abstract short get(String str, short s2) throws IOException;

        public abstract int get(String str, int i2) throws IOException;

        public abstract long get(String str, long j2) throws IOException;

        public abstract float get(String str, float f2) throws IOException;

        public abstract double get(String str, double d2) throws IOException;

        public abstract Object get(String str, Object obj) throws IOException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void bytesToFloats(byte[] bArr, int i2, float[] fArr, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void bytesToDoubles(byte[] bArr, int i2, double[] dArr, int i3, int i4);

    static {
        primClasses.put("boolean", Boolean.TYPE);
        primClasses.put(SchemaSymbols.ATTVAL_BYTE, Byte.TYPE);
        primClasses.put("char", Character.TYPE);
        primClasses.put(SchemaSymbols.ATTVAL_SHORT, Short.TYPE);
        primClasses.put("int", Integer.TYPE);
        primClasses.put(SchemaSymbols.ATTVAL_LONG, Long.TYPE);
        primClasses.put(SchemaSymbols.ATTVAL_FLOAT, Float.TYPE);
        primClasses.put(SchemaSymbols.ATTVAL_DOUBLE, Double.TYPE);
        primClasses.put("void", Void.TYPE);
        SharedSecrets.setJavaOISAccess(new JavaOISAccess() { // from class: java.io.ObjectInputStream.1
            @Override // sun.misc.JavaOISAccess
            public void setObjectInputFilter(ObjectInputStream objectInputStream, ObjectInputFilter objectInputFilter) {
                objectInputStream.setInternalObjectInputFilter(objectInputFilter);
            }

            @Override // sun.misc.JavaOISAccess
            public ObjectInputFilter getObjectInputFilter(ObjectInputStream objectInputStream) {
                return objectInputStream.getInternalObjectInputFilter();
            }

            @Override // sun.misc.JavaOISAccess
            public void checkArray(ObjectInputStream objectInputStream, Class<?> cls, int i2) throws InvalidClassException {
                objectInputStream.checkArray(cls, i2);
            }
        });
        SharedSecrets.setJavaObjectInputStreamAccess(ObjectInputStream::setValidator);
        SharedSecrets.setJavaObjectInputStreamReadString((v0) -> {
            return v0.readString();
        });
    }

    /* loaded from: rt.jar:java/io/ObjectInputStream$Caches.class */
    private static class Caches {
        static final ConcurrentMap<ObjectStreamClass.WeakClassKey, Boolean> subclassAudits = new ConcurrentHashMap();
        static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue<>();
        static final boolean SET_FILTER_AFTER_READ = privilegedGetProperty("jdk.serialSetFilterAfterRead");
        static final int PROXY_INTERFACE_LIMIT = Math.max(0, Math.min(65535, privilegedGetIntegerProperty("jdk.serialProxyInterfaceLimit", 65535)));

        private Caches() {
        }

        private static boolean privilegedGetProperty(String str) {
            if (System.getSecurityManager() == null) {
                return Boolean.getBoolean(str);
            }
            return ((Boolean) AccessController.doPrivileged(new GetBooleanAction(str))).booleanValue();
        }

        private static int privilegedGetIntegerProperty(String str, int i2) {
            if (System.getSecurityManager() == null) {
                return Integer.getInteger(str, i2).intValue();
            }
            return ((Integer) AccessController.doPrivileged(new GetIntegerAction(str, i2))).intValue();
        }
    }

    /* loaded from: rt.jar:java/io/ObjectInputStream$Logging.class */
    private static class Logging {
        private static final PlatformLogger traceLogger;
        private static final PlatformLogger infoLogger;

        private Logging() {
        }

        static {
            PlatformLogger logger = PlatformLogger.getLogger("java.io.serialization");
            infoLogger = (logger == null || !logger.isLoggable(PlatformLogger.Level.INFO)) ? null : logger;
            traceLogger = (logger == null || !logger.isLoggable(PlatformLogger.Level.FINER)) ? null : logger;
        }
    }

    public ObjectInputStream(InputStream inputStream) throws IOException {
        verifySubclass();
        this.bin = new BlockDataInputStream(inputStream);
        this.handles = new HandleTable(10);
        this.vlist = new ValidationList();
        this.serialFilter = ObjectInputFilter.Config.getSerialFilter();
        this.enableOverride = false;
        readStreamHeader();
        this.bin.setBlockDataMode(true);
    }

    protected ObjectInputStream() throws IOException, SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
        }
        this.bin = null;
        this.handles = null;
        this.vlist = null;
        this.serialFilter = ObjectInputFilter.Config.getSerialFilter();
        this.enableOverride = true;
    }

    @Override // java.io.ObjectInput
    public final Object readObject() throws IOException, ClassNotFoundException {
        return readObject(Object.class);
    }

    private String readString() throws IOException {
        try {
            return (String) readObject(String.class);
        } catch (ClassNotFoundException e2) {
            throw new IllegalStateException(e2);
        }
    }

    private final Object readObject(Class<?> cls) throws IOException, ClassNotFoundException {
        if (this.enableOverride) {
            return readObjectOverride();
        }
        if (cls != Object.class && cls != String.class) {
            throw new AssertionError((Object) "internal error");
        }
        int i2 = this.passHandle;
        try {
            Object object0 = readObject0(cls, false);
            this.handles.markDependency(i2, this.passHandle);
            ClassNotFoundException classNotFoundExceptionLookupException = this.handles.lookupException(this.passHandle);
            if (classNotFoundExceptionLookupException != null) {
                throw classNotFoundExceptionLookupException;
            }
            if (this.depth == 0) {
                this.vlist.doCallbacks();
            }
            return object0;
        } finally {
            this.passHandle = i2;
            if (this.closed && this.depth == 0) {
                clear();
            }
        }
    }

    protected Object readObjectOverride() throws IOException, ClassNotFoundException {
        return null;
    }

    public Object readUnshared() throws IOException, ClassNotFoundException {
        int i2 = this.passHandle;
        try {
            Object object0 = readObject0(Object.class, true);
            this.handles.markDependency(i2, this.passHandle);
            ClassNotFoundException classNotFoundExceptionLookupException = this.handles.lookupException(this.passHandle);
            if (classNotFoundExceptionLookupException != null) {
                throw classNotFoundExceptionLookupException;
            }
            if (this.depth == 0) {
                this.vlist.doCallbacks();
            }
            return object0;
        } finally {
            this.passHandle = i2;
            if (this.closed && this.depth == 0) {
                clear();
            }
        }
    }

    public void defaultReadObject() throws IOException, ClassNotFoundException {
        SerialCallbackContext serialCallbackContext = this.curContext;
        if (serialCallbackContext == null) {
            throw new NotActiveException("not in call to readObject");
        }
        Object obj = serialCallbackContext.getObj();
        ObjectStreamClass desc = serialCallbackContext.getDesc();
        this.bin.setBlockDataMode(false);
        defaultReadFields(obj, desc);
        this.bin.setBlockDataMode(true);
        if (!desc.hasWriteObjectData()) {
            this.defaultDataEnd = true;
        }
        ClassNotFoundException classNotFoundExceptionLookupException = this.handles.lookupException(this.passHandle);
        if (classNotFoundExceptionLookupException != null) {
            throw classNotFoundExceptionLookupException;
        }
    }

    public GetField readFields() throws IOException, ClassNotFoundException {
        SerialCallbackContext serialCallbackContext = this.curContext;
        if (serialCallbackContext == null) {
            throw new NotActiveException("not in call to readObject");
        }
        serialCallbackContext.getObj();
        ObjectStreamClass desc = serialCallbackContext.getDesc();
        this.bin.setBlockDataMode(false);
        GetFieldImpl getFieldImpl = new GetFieldImpl(desc);
        getFieldImpl.readFields();
        this.bin.setBlockDataMode(true);
        if (!desc.hasWriteObjectData()) {
            this.defaultDataEnd = true;
        }
        return getFieldImpl;
    }

    public void registerValidation(ObjectInputValidation objectInputValidation, int i2) throws InvalidObjectException, NotActiveException {
        if (this.depth == 0) {
            throw new NotActiveException("stream inactive");
        }
        this.vlist.register(objectInputValidation, i2);
    }

    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws ClassNotFoundException, IOException {
        String name = objectStreamClass.getName();
        try {
            return Class.forName(name, false, latestUserDefinedLoader());
        } catch (ClassNotFoundException e2) {
            Class<?> cls = primClasses.get(name);
            if (cls != null) {
                return cls;
            }
            throw e2;
        }
    }

    protected Class<?> resolveProxyClass(String[] strArr) throws ClassNotFoundException, IOException {
        ClassLoader classLoaderLatestUserDefinedLoader = latestUserDefinedLoader();
        ClassLoader classLoader = null;
        boolean z2 = false;
        Class[] clsArr = new Class[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            Class<?> cls = Class.forName(strArr[i2], false, classLoaderLatestUserDefinedLoader);
            if ((cls.getModifiers() & 1) == 0) {
                if (z2) {
                    if (classLoader != cls.getClassLoader()) {
                        throw new IllegalAccessError("conflicting non-public interface class loaders");
                    }
                } else {
                    classLoader = cls.getClassLoader();
                    z2 = true;
                }
            }
            clsArr[i2] = cls;
        }
        try {
            return Proxy.getProxyClass(z2 ? classLoader : classLoaderLatestUserDefinedLoader, clsArr);
        } catch (IllegalArgumentException e2) {
            throw new ClassNotFoundException(null, e2);
        }
    }

    protected Object resolveObject(Object obj) throws IOException {
        return obj;
    }

    protected boolean enableResolveObject(boolean z2) throws SecurityException {
        SecurityManager securityManager;
        if (z2 == this.enableResolve) {
            return z2;
        }
        if (z2 && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPermission(SUBSTITUTION_PERMISSION);
        }
        this.enableResolve = z2;
        return !this.enableResolve;
    }

    protected void readStreamHeader() throws IOException {
        short s2 = this.bin.readShort();
        short s3 = this.bin.readShort();
        if (s2 != -21267 || s3 != 5) {
            throw new StreamCorruptedException(String.format("invalid stream header: %04X%04X", Short.valueOf(s2), Short.valueOf(s3)));
        }
    }

    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass objectStreamClass = new ObjectStreamClass();
        objectStreamClass.readNonProxy(this);
        return objectStreamClass;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.bin.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (bArr == null) {
            throw new NullPointerException();
        }
        int i4 = i2 + i3;
        if (i2 < 0 || i3 < 0 || i4 > bArr.length || i4 < 0) {
            throw new IndexOutOfBoundsException();
        }
        return this.bin.read(bArr, i2, i3, false);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.bin.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.closed = true;
        if (this.depth == 0) {
            clear();
        }
        this.bin.close();
    }

    public boolean readBoolean() throws IOException {
        return this.bin.readBoolean();
    }

    public byte readByte() throws IOException {
        return this.bin.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return this.bin.readUnsignedByte();
    }

    public char readChar() throws IOException {
        return this.bin.readChar();
    }

    public short readShort() throws IOException {
        return this.bin.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return this.bin.readUnsignedShort();
    }

    public int readInt() throws IOException {
        return this.bin.readInt();
    }

    public long readLong() throws IOException {
        return this.bin.readLong();
    }

    public float readFloat() throws IOException {
        return this.bin.readFloat();
    }

    public double readDouble() throws IOException {
        return this.bin.readDouble();
    }

    public void readFully(byte[] bArr) throws IOException {
        this.bin.readFully(bArr, 0, bArr.length, false);
    }

    public void readFully(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = i2 + i3;
        if (i2 < 0 || i3 < 0 || i4 > bArr.length || i4 < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.bin.readFully(bArr, i2, i3, false);
    }

    public int skipBytes(int i2) throws IOException {
        return this.bin.skipBytes(i2);
    }

    @Deprecated
    public String readLine() throws IOException {
        return this.bin.readLine();
    }

    public String readUTF() throws IOException {
        return this.bin.readUTF();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ObjectInputFilter getInternalObjectInputFilter() {
        return this.serialFilter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setInternalObjectInputFilter(ObjectInputFilter objectInputFilter) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SerializablePermission("serialFilter"));
        }
        if (this.serialFilter != null && this.serialFilter != ObjectInputFilter.Config.getSerialFilter()) {
            throw new IllegalStateException("filter can not be set more than once");
        }
        if (this.totalObjectRefs > 0 && !Caches.SET_FILTER_AFTER_READ) {
            throw new IllegalStateException("filter can not be set after an object has been read");
        }
        this.serialFilter = objectInputFilter;
    }

    private void filterCheck(Class<?> cls, int i2) throws InvalidClassException {
        ObjectInputFilter.Status statusCheckInput;
        if (this.serialFilter != null) {
            RuntimeException runtimeException = null;
            long bytesRead = this.bin == null ? 0L : this.bin.getBytesRead();
            try {
                statusCheckInput = this.serialFilter.checkInput(new FilterValues(cls, i2, this.totalObjectRefs, this.depth, bytesRead));
            } catch (RuntimeException e2) {
                statusCheckInput = ObjectInputFilter.Status.REJECTED;
                runtimeException = e2;
            }
            if (statusCheckInput == null || statusCheckInput == ObjectInputFilter.Status.REJECTED) {
                if (Logging.infoLogger != null) {
                    Logging.infoLogger.info("ObjectInputFilter {0}: {1}, array length: {2}, nRefs: {3}, depth: {4}, bytes: {5}, ex: {6}", statusCheckInput, cls, Integer.valueOf(i2), Long.valueOf(this.totalObjectRefs), Long.valueOf(this.depth), Long.valueOf(bytesRead), Objects.toString(runtimeException, "n/a"));
                }
                InvalidClassException invalidClassException = new InvalidClassException("filter status: " + ((Object) statusCheckInput));
                invalidClassException.initCause(runtimeException);
                throw invalidClassException;
            }
            if (Logging.traceLogger != null) {
                Logging.traceLogger.finer("ObjectInputFilter {0}: {1}, array length: {2}, nRefs: {3}, depth: {4}, bytes: {5}, ex: {6}", statusCheckInput, cls, Integer.valueOf(i2), Long.valueOf(this.totalObjectRefs), Long.valueOf(this.depth), Long.valueOf(bytesRead), Objects.toString(runtimeException, "n/a"));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkArray(Class<?> cls, int i2) throws InvalidClassException {
        Objects.requireNonNull(cls);
        if (!cls.isArray()) {
            throw new IllegalArgumentException("not an array type");
        }
        if (i2 < 0) {
            throw new NegativeArraySizeException();
        }
        filterCheck(cls, i2);
    }

    private void verifySubclass() {
        SecurityManager securityManager;
        Class<?> cls = getClass();
        if (cls == ObjectInputStream.class || (securityManager = System.getSecurityManager()) == null) {
            return;
        }
        ObjectStreamClass.processQueue(Caches.subclassAuditsQueue, Caches.subclassAudits);
        ObjectStreamClass.WeakClassKey weakClassKey = new ObjectStreamClass.WeakClassKey(cls, Caches.subclassAuditsQueue);
        Boolean boolValueOf = Caches.subclassAudits.get(weakClassKey);
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(auditSubclass(cls));
            Caches.subclassAudits.putIfAbsent(weakClassKey, boolValueOf);
        }
        if (boolValueOf.booleanValue()) {
            return;
        }
        securityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
    }

    private static boolean auditSubclass(final Class<?> cls) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.io.ObjectInputStream.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() throws SecurityException {
                Class superclass = cls;
                while (true) {
                    Class cls2 = superclass;
                    if (cls2 != ObjectInputStream.class) {
                        try {
                            cls2.getDeclaredMethod("readUnshared", (Class[]) null);
                            return Boolean.FALSE;
                        } catch (NoSuchMethodException e2) {
                            try {
                                cls2.getDeclaredMethod("readFields", (Class[]) null);
                                return Boolean.FALSE;
                            } catch (NoSuchMethodException e3) {
                                superclass = cls2.getSuperclass();
                            }
                        }
                    } else {
                        return Boolean.TRUE;
                    }
                }
            }
        })).booleanValue();
    }

    private void clear() {
        this.handles.clear();
        this.vlist.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object readObject0(Class<?> cls, boolean z2) throws IOException {
        byte bPeekByte;
        boolean blockDataMode = this.bin.getBlockDataMode();
        if (blockDataMode) {
            int iCurrentBlockRemaining = this.bin.currentBlockRemaining();
            if (iCurrentBlockRemaining > 0) {
                throw new OptionalDataException(iCurrentBlockRemaining);
            }
            if (this.defaultDataEnd) {
                throw new OptionalDataException(true);
            }
            this.bin.setBlockDataMode(false);
        }
        while (true) {
            bPeekByte = this.bin.peekByte();
            if (bPeekByte != 121) {
                break;
            }
            this.bin.readByte();
            handleReset();
        }
        this.depth++;
        this.totalObjectRefs++;
        try {
            switch (bPeekByte) {
                case 112:
                    Object obj = readNull();
                    this.depth--;
                    this.bin.setBlockDataMode(blockDataMode);
                    return obj;
                case 113:
                    Object objCast = cls.cast(readHandle(z2));
                    this.depth--;
                    this.bin.setBlockDataMode(blockDataMode);
                    return objCast;
                case 114:
                case 125:
                    if (cls == String.class) {
                        throw new ClassCastException("Cannot cast a class to java.lang.String");
                    }
                    ObjectStreamClass classDesc = readClassDesc(z2);
                    this.depth--;
                    this.bin.setBlockDataMode(blockDataMode);
                    return classDesc;
                case 115:
                    if (cls == String.class) {
                        throw new ClassCastException("Cannot cast an object to java.lang.String");
                    }
                    Object objCheckResolve = checkResolve(readOrdinaryObject(z2));
                    this.depth--;
                    this.bin.setBlockDataMode(blockDataMode);
                    return objCheckResolve;
                case 116:
                case 124:
                    Object objCheckResolve2 = checkResolve(readString(z2));
                    this.depth--;
                    this.bin.setBlockDataMode(blockDataMode);
                    return objCheckResolve2;
                case 117:
                    if (cls == String.class) {
                        throw new ClassCastException("Cannot cast an array to java.lang.String");
                    }
                    Object objCheckResolve3 = checkResolve(readArray(z2));
                    this.depth--;
                    this.bin.setBlockDataMode(blockDataMode);
                    return objCheckResolve3;
                case 118:
                    if (cls == String.class) {
                        throw new ClassCastException("Cannot cast a class to java.lang.String");
                    }
                    Class<?> cls2 = readClass(z2);
                    this.depth--;
                    this.bin.setBlockDataMode(blockDataMode);
                    return cls2;
                case 119:
                case 122:
                    if (!blockDataMode) {
                        throw new StreamCorruptedException("unexpected block data");
                    }
                    this.bin.setBlockDataMode(true);
                    this.bin.peek();
                    throw new OptionalDataException(this.bin.currentBlockRemaining());
                case 120:
                    if (blockDataMode) {
                        throw new OptionalDataException(true);
                    }
                    throw new StreamCorruptedException("unexpected end of block data");
                case 121:
                default:
                    throw new StreamCorruptedException(String.format("invalid type code: %02X", Byte.valueOf(bPeekByte)));
                case 123:
                    if (cls == String.class) {
                        throw new ClassCastException("Cannot cast an exception to java.lang.String");
                    }
                    throw new WriteAbortedException("writing aborted", readFatalException());
                case 126:
                    if (cls == String.class) {
                        throw new ClassCastException("Cannot cast an enum to java.lang.String");
                    }
                    Object objCheckResolve4 = checkResolve(readEnum(z2));
                    this.depth--;
                    this.bin.setBlockDataMode(blockDataMode);
                    return objCheckResolve4;
            }
        } catch (Throwable th) {
            this.depth--;
            this.bin.setBlockDataMode(blockDataMode);
            throw th;
        }
    }

    private Object checkResolve(Object obj) throws IOException {
        if (!this.enableResolve || this.handles.lookupException(this.passHandle) != null) {
            return obj;
        }
        Object objResolveObject = resolveObject(obj);
        if (objResolveObject != obj) {
            if (objResolveObject != null) {
                if (objResolveObject.getClass().isArray()) {
                    filterCheck(objResolveObject.getClass(), Array.getLength(objResolveObject));
                } else {
                    filterCheck(objResolveObject.getClass(), -1);
                }
            }
            this.handles.setObject(this.passHandle, objResolveObject);
        }
        return objResolveObject;
    }

    String readTypeString() throws IOException {
        int i2 = this.passHandle;
        try {
            byte bPeekByte = this.bin.peekByte();
            switch (bPeekByte) {
                case 112:
                    String str = (String) readNull();
                    this.passHandle = i2;
                    return str;
                case 113:
                    String str2 = (String) readHandle(false);
                    this.passHandle = i2;
                    return str2;
                case 116:
                case 124:
                    String string = readString(false);
                    this.passHandle = i2;
                    return string;
                default:
                    throw new StreamCorruptedException(String.format("invalid type code: %02X", Byte.valueOf(bPeekByte)));
            }
        } catch (Throwable th) {
            this.passHandle = i2;
            throw th;
        }
    }

    private Object readNull() throws IOException {
        if (this.bin.readByte() != 112) {
            throw new InternalError();
        }
        this.passHandle = -1;
        return null;
    }

    private Object readHandle(boolean z2) throws IOException {
        if (this.bin.readByte() != 113) {
            throw new InternalError();
        }
        this.passHandle = this.bin.readInt() - ObjectStreamConstants.baseWireHandle;
        if (this.passHandle < 0 || this.passHandle >= this.handles.size()) {
            throw new StreamCorruptedException(String.format("invalid handle value: %08X", Integer.valueOf(this.passHandle + ObjectStreamConstants.baseWireHandle)));
        }
        if (z2) {
            throw new InvalidObjectException("cannot read back reference as unshared");
        }
        Object objLookupObject = this.handles.lookupObject(this.passHandle);
        if (objLookupObject == unsharedMarker) {
            throw new InvalidObjectException("cannot read back reference to unshared object");
        }
        filterCheck(null, -1);
        return objLookupObject;
    }

    private Class<?> readClass(boolean z2) throws IOException {
        if (this.bin.readByte() != 118) {
            throw new InternalError();
        }
        ObjectStreamClass classDesc = readClassDesc(false);
        Class<?> clsForClass = classDesc.forClass();
        this.passHandle = this.handles.assign(z2 ? unsharedMarker : clsForClass);
        ClassNotFoundException resolveException = classDesc.getResolveException();
        if (resolveException != null) {
            this.handles.markException(this.passHandle, resolveException);
        }
        this.handles.finish(this.passHandle);
        return clsForClass;
    }

    private ObjectStreamClass readClassDesc(boolean z2) throws IOException {
        ObjectStreamClass nonProxyDesc;
        byte bPeekByte = this.bin.peekByte();
        switch (bPeekByte) {
            case 112:
                nonProxyDesc = (ObjectStreamClass) readNull();
                break;
            case 113:
                nonProxyDesc = (ObjectStreamClass) readHandle(z2);
                nonProxyDesc.checkInitialized();
                break;
            case 114:
                nonProxyDesc = readNonProxyDesc(z2);
                break;
            case 125:
                nonProxyDesc = readProxyDesc(z2);
                break;
            default:
                throw new StreamCorruptedException(String.format("invalid type code: %02X", Byte.valueOf(bPeekByte)));
        }
        if (nonProxyDesc != null) {
            validateDescriptor(nonProxyDesc);
        }
        return nonProxyDesc;
    }

    private boolean isCustomSubclass() {
        return getClass().getClassLoader() != ObjectInputStream.class.getClassLoader();
    }

    private ObjectStreamClass readProxyDesc(boolean z2) throws IOException {
        if (this.bin.readByte() != 125) {
            throw new InternalError();
        }
        ObjectStreamClass objectStreamClass = new ObjectStreamClass();
        int iAssign = this.handles.assign(z2 ? unsharedMarker : objectStreamClass);
        this.passHandle = -1;
        int i2 = this.bin.readInt();
        if (i2 > 65535) {
            throw new InvalidObjectException("interface limit exceeded: " + i2 + ", limit: " + Caches.PROXY_INTERFACE_LIMIT);
        }
        String[] strArr = new String[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            strArr[i3] = this.bin.readUTF();
        }
        if (i2 > Caches.PROXY_INTERFACE_LIMIT) {
            throw new InvalidObjectException("interface limit exceeded: " + i2 + ", limit: " + Caches.PROXY_INTERFACE_LIMIT + VectorFormat.DEFAULT_SEPARATOR + Arrays.toString(strArr));
        }
        Class<?> cls = null;
        ClassNotFoundException classNotFoundException = null;
        this.bin.setBlockDataMode(true);
        try {
            Class<?> clsResolveProxyClass = resolveProxyClass(strArr);
            cls = clsResolveProxyClass;
            if (clsResolveProxyClass == null) {
                classNotFoundException = new ClassNotFoundException("null class");
            } else {
                if (!Proxy.isProxyClass(cls)) {
                    throw new InvalidClassException("Not a proxy");
                }
                ReflectUtil.checkProxyPackageAccess(getClass().getClassLoader(), cls.getInterfaces());
                for (Class<?> cls2 : cls.getInterfaces()) {
                    filterCheck(cls2, -1);
                }
            }
        } catch (ClassNotFoundException e2) {
            classNotFoundException = e2;
        } catch (OutOfMemoryError e3) {
            InvalidObjectException invalidObjectException = new InvalidObjectException("Proxy interface limit exceeded: " + Arrays.toString(strArr));
            invalidObjectException.initCause(e3);
            throw invalidObjectException;
        }
        filterCheck(cls, -1);
        skipCustomData();
        try {
            try {
                this.totalObjectRefs++;
                this.depth++;
                objectStreamClass.initProxy(cls, classNotFoundException, readClassDesc(false));
                this.depth--;
                this.handles.finish(iAssign);
                this.passHandle = iAssign;
                return objectStreamClass;
            } catch (OutOfMemoryError e4) {
                InvalidObjectException invalidObjectException2 = new InvalidObjectException("Proxy interface limit exceeded: " + Arrays.toString(strArr));
                invalidObjectException2.initCause(e4);
                throw invalidObjectException2;
            }
        } catch (Throwable th) {
            this.depth--;
            throw th;
        }
    }

    private ObjectStreamClass readNonProxyDesc(boolean z2) throws IOException {
        if (this.bin.readByte() != 114) {
            throw new InternalError();
        }
        ObjectStreamClass objectStreamClass = new ObjectStreamClass();
        int iAssign = this.handles.assign(z2 ? unsharedMarker : objectStreamClass);
        this.passHandle = -1;
        try {
            ObjectStreamClass classDescriptor = readClassDescriptor();
            Class<?> cls = null;
            ClassNotFoundException classNotFoundException = null;
            this.bin.setBlockDataMode(true);
            boolean zIsCustomSubclass = isCustomSubclass();
            try {
                Class<?> clsResolveClass = resolveClass(classDescriptor);
                cls = clsResolveClass;
                if (clsResolveClass == null) {
                    classNotFoundException = new ClassNotFoundException("null class");
                } else if (zIsCustomSubclass) {
                    ReflectUtil.checkPackageAccess(cls);
                }
            } catch (ClassNotFoundException e2) {
                classNotFoundException = e2;
            }
            filterCheck(cls, -1);
            skipCustomData();
            try {
                this.totalObjectRefs++;
                this.depth++;
                objectStreamClass.initNonProxy(classDescriptor, cls, classNotFoundException, readClassDesc(false));
                if (cls != null) {
                    ObjectStreamClass objectStreamClass2 = null;
                    for (ObjectStreamClass superDesc = objectStreamClass.getSuperDesc(); superDesc != null; superDesc = superDesc.getSuperDesc()) {
                        ObjectStreamClass localDesc = superDesc.getLocalDesc();
                        objectStreamClass2 = localDesc;
                        if (localDesc != null) {
                            break;
                        }
                    }
                    for (ObjectStreamClass superDesc2 = objectStreamClass.getLocalDesc().getSuperDesc(); superDesc2 != null && superDesc2 != objectStreamClass2; superDesc2 = superDesc2.getSuperDesc()) {
                        filterCheck(superDesc2.forClass(), -1);
                    }
                }
                this.handles.finish(iAssign);
                this.passHandle = iAssign;
                return objectStreamClass;
            } finally {
                this.depth--;
            }
        } catch (ClassNotFoundException e3) {
            throw ((IOException) new InvalidClassException("failed to read class descriptor").initCause(e3));
        }
    }

    private String readString(boolean z2) throws IOException {
        String longUTF;
        byte b2 = this.bin.readByte();
        switch (b2) {
            case 116:
                longUTF = this.bin.readUTF();
                break;
            case 124:
                longUTF = this.bin.readLongUTF();
                break;
            default:
                throw new StreamCorruptedException(String.format("invalid type code: %02X", Byte.valueOf(b2)));
        }
        this.passHandle = this.handles.assign(z2 ? unsharedMarker : longUTF);
        this.handles.finish(this.passHandle);
        return longUTF;
    }

    private Object readArray(boolean z2) throws IOException, NegativeArraySizeException {
        if (this.bin.readByte() != 117) {
            throw new InternalError();
        }
        ObjectStreamClass classDesc = readClassDesc(false);
        int i2 = this.bin.readInt();
        filterCheck(classDesc.forClass(), i2);
        Object objNewInstance = null;
        Class<?> componentType = null;
        Class<?> clsForClass = classDesc.forClass();
        if (clsForClass != null) {
            componentType = clsForClass.getComponentType();
            objNewInstance = Array.newInstance(componentType, i2);
        }
        int iAssign = this.handles.assign(z2 ? unsharedMarker : objNewInstance);
        ClassNotFoundException resolveException = classDesc.getResolveException();
        if (resolveException != null) {
            this.handles.markException(iAssign, resolveException);
        }
        if (componentType == null) {
            for (int i3 = 0; i3 < i2; i3++) {
                readObject0(Object.class, false);
            }
        } else if (componentType.isPrimitive()) {
            if (componentType == Integer.TYPE) {
                this.bin.readInts((int[]) objNewInstance, 0, i2);
            } else if (componentType == Byte.TYPE) {
                this.bin.readFully((byte[]) objNewInstance, 0, i2, true);
            } else if (componentType == Long.TYPE) {
                this.bin.readLongs((long[]) objNewInstance, 0, i2);
            } else if (componentType == Float.TYPE) {
                this.bin.readFloats((float[]) objNewInstance, 0, i2);
            } else if (componentType == Double.TYPE) {
                this.bin.readDoubles((double[]) objNewInstance, 0, i2);
            } else if (componentType == Short.TYPE) {
                this.bin.readShorts((short[]) objNewInstance, 0, i2);
            } else if (componentType == Character.TYPE) {
                this.bin.readChars((char[]) objNewInstance, 0, i2);
            } else if (componentType == Boolean.TYPE) {
                this.bin.readBooleans((boolean[]) objNewInstance, 0, i2);
            } else {
                throw new InternalError();
            }
        } else {
            Object[] objArr = (Object[]) objNewInstance;
            for (int i4 = 0; i4 < i2; i4++) {
                objArr[i4] = readObject0(Object.class, false);
                this.handles.markDependency(iAssign, this.passHandle);
            }
        }
        this.handles.finish(iAssign);
        this.passHandle = iAssign;
        return objNewInstance;
    }

    private Enum<?> readEnum(boolean z2) throws IOException {
        if (this.bin.readByte() != 126) {
            throw new InternalError();
        }
        ObjectStreamClass classDesc = readClassDesc(false);
        if (!classDesc.isEnum()) {
            throw new InvalidClassException("non-enum class: " + ((Object) classDesc));
        }
        int iAssign = this.handles.assign(z2 ? unsharedMarker : null);
        ClassNotFoundException resolveException = classDesc.getResolveException();
        if (resolveException != null) {
            this.handles.markException(iAssign, resolveException);
        }
        String string = readString(false);
        Enum<?> enumValueOf = null;
        Class<?> clsForClass = classDesc.forClass();
        if (clsForClass != null) {
            try {
                enumValueOf = Enum.valueOf(clsForClass, string);
                if (!z2) {
                    this.handles.setObject(iAssign, enumValueOf);
                }
            } catch (IllegalArgumentException e2) {
                throw ((IOException) new InvalidObjectException("enum constant " + string + " does not exist in " + ((Object) clsForClass)).initCause(e2));
            }
        }
        this.handles.finish(iAssign);
        this.passHandle = iAssign;
        return enumValueOf;
    }

    private Object readOrdinaryObject(boolean z2) throws UnsupportedOperationException, IOException {
        if (this.bin.readByte() != 115) {
            throw new InternalError();
        }
        ObjectStreamClass classDesc = readClassDesc(false);
        classDesc.checkDeserialize();
        Class<?> clsForClass = classDesc.forClass();
        if (clsForClass == String.class || clsForClass == Class.class || clsForClass == ObjectStreamClass.class) {
            throw new InvalidClassException("invalid class descriptor");
        }
        try {
            Object objNewInstance = classDesc.isInstantiable() ? classDesc.newInstance() : null;
            this.passHandle = this.handles.assign(z2 ? unsharedMarker : objNewInstance);
            ClassNotFoundException resolveException = classDesc.getResolveException();
            if (resolveException != null) {
                this.handles.markException(this.passHandle, resolveException);
            }
            if (classDesc.isExternalizable()) {
                readExternalData((Externalizable) objNewInstance, classDesc);
            } else {
                readSerialData(objNewInstance, classDesc);
            }
            this.handles.finish(this.passHandle);
            if (objNewInstance != null && this.handles.lookupException(this.passHandle) == null && classDesc.hasReadResolveMethod()) {
                Object objInvokeReadResolve = classDesc.invokeReadResolve(objNewInstance);
                if (z2 && objInvokeReadResolve.getClass().isArray()) {
                    objInvokeReadResolve = cloneArray(objInvokeReadResolve);
                }
                if (objInvokeReadResolve != objNewInstance) {
                    if (objInvokeReadResolve != null) {
                        if (objInvokeReadResolve.getClass().isArray()) {
                            filterCheck(objInvokeReadResolve.getClass(), Array.getLength(objInvokeReadResolve));
                        } else {
                            filterCheck(objInvokeReadResolve.getClass(), -1);
                        }
                    }
                    Object obj = objInvokeReadResolve;
                    objNewInstance = obj;
                    this.handles.setObject(this.passHandle, obj);
                }
            }
            return objNewInstance;
        } catch (Exception e2) {
            throw ((IOException) new InvalidClassException(classDesc.forClass().getName(), "unable to create instance").initCause(e2));
        }
    }

    private void readExternalData(Externalizable externalizable, ObjectStreamClass objectStreamClass) throws IOException {
        SerialCallbackContext serialCallbackContext = this.curContext;
        if (serialCallbackContext != null) {
            serialCallbackContext.check();
        }
        this.curContext = null;
        try {
            boolean zHasBlockExternalData = objectStreamClass.hasBlockExternalData();
            if (zHasBlockExternalData) {
                this.bin.setBlockDataMode(true);
            }
            if (externalizable != null) {
                try {
                    externalizable.readExternal(this);
                } catch (ClassNotFoundException e2) {
                    this.handles.markException(this.passHandle, e2);
                }
            }
            if (zHasBlockExternalData) {
                skipCustomData();
            }
        } finally {
            if (serialCallbackContext != null) {
                serialCallbackContext.check();
            }
            this.curContext = serialCallbackContext;
        }
    }

    private void readSerialData(Object obj, ObjectStreamClass objectStreamClass) throws UnsupportedOperationException, IOException {
        ObjectStreamClass.ClassDataSlot[] classDataLayout = objectStreamClass.getClassDataLayout();
        for (int i2 = 0; i2 < classDataLayout.length; i2++) {
            ObjectStreamClass objectStreamClass2 = classDataLayout[i2].desc;
            if (classDataLayout[i2].hasData) {
                if (obj == null || this.handles.lookupException(this.passHandle) != null) {
                    defaultReadFields(null, objectStreamClass2);
                } else if (objectStreamClass2.hasReadObjectMethod()) {
                    ThreadDeath threadDeath = null;
                    boolean z2 = false;
                    SerialCallbackContext serialCallbackContext = this.curContext;
                    if (serialCallbackContext != null) {
                        serialCallbackContext.check();
                    }
                    try {
                        try {
                            this.curContext = new SerialCallbackContext(obj, objectStreamClass2);
                            this.bin.setBlockDataMode(true);
                            objectStreamClass2.invokeReadObject(obj, this);
                            do {
                                try {
                                    this.curContext.setUsed();
                                    if (serialCallbackContext != null) {
                                        serialCallbackContext.check();
                                    }
                                    this.curContext = serialCallbackContext;
                                    z2 = true;
                                } catch (ThreadDeath e2) {
                                    threadDeath = e2;
                                }
                            } while (!z2);
                            if (threadDeath != null) {
                                throw threadDeath;
                            }
                        } catch (ClassNotFoundException e3) {
                            this.handles.markException(this.passHandle, e3);
                            do {
                                try {
                                    this.curContext.setUsed();
                                    if (serialCallbackContext != null) {
                                        serialCallbackContext.check();
                                    }
                                    this.curContext = serialCallbackContext;
                                    z2 = true;
                                } catch (ThreadDeath e4) {
                                    threadDeath = e4;
                                }
                            } while (!z2);
                            if (threadDeath != null) {
                                throw threadDeath;
                            }
                        }
                        this.defaultDataEnd = false;
                    } catch (Throwable th) {
                        do {
                            try {
                                this.curContext.setUsed();
                                if (serialCallbackContext != null) {
                                    serialCallbackContext.check();
                                }
                                this.curContext = serialCallbackContext;
                                z2 = true;
                            } catch (ThreadDeath e5) {
                                threadDeath = e5;
                            }
                        } while (!z2);
                        if (threadDeath != null) {
                            throw threadDeath;
                        }
                        throw th;
                    }
                } else {
                    defaultReadFields(obj, objectStreamClass2);
                }
                if (objectStreamClass2.hasWriteObjectData()) {
                    skipCustomData();
                } else {
                    this.bin.setBlockDataMode(false);
                }
            } else if (obj != null && objectStreamClass2.hasReadObjectNoDataMethod() && this.handles.lookupException(this.passHandle) == null) {
                objectStreamClass2.invokeReadObjectNoData(obj);
            }
        }
    }

    private void skipCustomData() throws IOException {
        int i2 = this.passHandle;
        while (true) {
            if (this.bin.getBlockDataMode()) {
                this.bin.skipBlockData();
                this.bin.setBlockDataMode(false);
            }
            switch (this.bin.peekByte()) {
                case 119:
                case 122:
                    this.bin.setBlockDataMode(true);
                    break;
                case 120:
                    this.bin.readByte();
                    this.passHandle = i2;
                    return;
                case 121:
                default:
                    readObject0(Object.class, false);
                    break;
            }
        }
    }

    private void defaultReadFields(Object obj, ObjectStreamClass objectStreamClass) throws IOException {
        Class<?> clsForClass = objectStreamClass.forClass();
        if (clsForClass != null && obj != null && !clsForClass.isInstance(obj)) {
            throw new ClassCastException();
        }
        int primDataSize = objectStreamClass.getPrimDataSize();
        if (this.primVals == null || this.primVals.length < primDataSize) {
            this.primVals = new byte[primDataSize];
        }
        this.bin.readFully(this.primVals, 0, primDataSize, false);
        if (obj != null) {
            objectStreamClass.setPrimFieldValues(obj, this.primVals);
        }
        int i2 = this.passHandle;
        ObjectStreamField[] fields = objectStreamClass.getFields(false);
        Object[] objArr = new Object[objectStreamClass.getNumObjFields()];
        int length = fields.length - objArr.length;
        for (int i3 = 0; i3 < objArr.length; i3++) {
            ObjectStreamField objectStreamField = fields[length + i3];
            objArr[i3] = readObject0(Object.class, objectStreamField.isUnshared());
            if (objectStreamField.getField() != null) {
                this.handles.markDependency(i2, this.passHandle);
            }
        }
        if (obj != null) {
            objectStreamClass.setObjFieldValues(obj, objArr);
        }
        this.passHandle = i2;
    }

    private IOException readFatalException() throws IOException {
        if (this.bin.readByte() != 123) {
            throw new InternalError();
        }
        clear();
        byte bPeekByte = this.bin.peekByte();
        if (bPeekByte != 115 && bPeekByte != 113) {
            throw new StreamCorruptedException(String.format("invalid type code: %02X", Byte.valueOf(bPeekByte)));
        }
        return (IOException) readObject0(Object.class, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleReset() throws StreamCorruptedException {
        if (this.depth > 0) {
            throw new StreamCorruptedException("unexpected reset; recursion depth: " + this.depth);
        }
        clear();
    }

    private static ClassLoader latestUserDefinedLoader() {
        return VM.latestUserDefinedLoader();
    }

    /* loaded from: rt.jar:java/io/ObjectInputStream$GetFieldImpl.class */
    private class GetFieldImpl extends GetField {
        private final ObjectStreamClass desc;
        private final byte[] primVals;
        private final Object[] objVals;
        private final int[] objHandles;

        GetFieldImpl(ObjectStreamClass objectStreamClass) {
            this.desc = objectStreamClass;
            this.primVals = new byte[objectStreamClass.getPrimDataSize()];
            this.objVals = new Object[objectStreamClass.getNumObjFields()];
            this.objHandles = new int[this.objVals.length];
        }

        @Override // java.io.ObjectInputStream.GetField
        public ObjectStreamClass getObjectStreamClass() {
            return this.desc;
        }

        @Override // java.io.ObjectInputStream.GetField
        public boolean defaulted(String str) throws IOException {
            return getFieldOffset(str, null) < 0;
        }

        @Override // java.io.ObjectInputStream.GetField
        public boolean get(String str, boolean z2) throws IOException {
            int fieldOffset = getFieldOffset(str, Boolean.TYPE);
            return fieldOffset >= 0 ? Bits.getBoolean(this.primVals, fieldOffset) : z2;
        }

        @Override // java.io.ObjectInputStream.GetField
        public byte get(String str, byte b2) throws IOException {
            int fieldOffset = getFieldOffset(str, Byte.TYPE);
            return fieldOffset >= 0 ? this.primVals[fieldOffset] : b2;
        }

        @Override // java.io.ObjectInputStream.GetField
        public char get(String str, char c2) throws IOException {
            int fieldOffset = getFieldOffset(str, Character.TYPE);
            return fieldOffset >= 0 ? Bits.getChar(this.primVals, fieldOffset) : c2;
        }

        @Override // java.io.ObjectInputStream.GetField
        public short get(String str, short s2) throws IOException {
            int fieldOffset = getFieldOffset(str, Short.TYPE);
            return fieldOffset >= 0 ? Bits.getShort(this.primVals, fieldOffset) : s2;
        }

        @Override // java.io.ObjectInputStream.GetField
        public int get(String str, int i2) throws IOException {
            int fieldOffset = getFieldOffset(str, Integer.TYPE);
            return fieldOffset >= 0 ? Bits.getInt(this.primVals, fieldOffset) : i2;
        }

        @Override // java.io.ObjectInputStream.GetField
        public float get(String str, float f2) throws IOException {
            int fieldOffset = getFieldOffset(str, Float.TYPE);
            return fieldOffset >= 0 ? Bits.getFloat(this.primVals, fieldOffset) : f2;
        }

        @Override // java.io.ObjectInputStream.GetField
        public long get(String str, long j2) throws IOException {
            int fieldOffset = getFieldOffset(str, Long.TYPE);
            return fieldOffset >= 0 ? Bits.getLong(this.primVals, fieldOffset) : j2;
        }

        @Override // java.io.ObjectInputStream.GetField
        public double get(String str, double d2) throws IOException {
            int fieldOffset = getFieldOffset(str, Double.TYPE);
            return fieldOffset >= 0 ? Bits.getDouble(this.primVals, fieldOffset) : d2;
        }

        @Override // java.io.ObjectInputStream.GetField
        public Object get(String str, Object obj) throws IOException {
            int fieldOffset = getFieldOffset(str, Object.class);
            if (fieldOffset >= 0) {
                int i2 = this.objHandles[fieldOffset];
                ObjectInputStream.this.handles.markDependency(ObjectInputStream.this.passHandle, i2);
                if (ObjectInputStream.this.handles.lookupException(i2) == null) {
                    return this.objVals[fieldOffset];
                }
                return null;
            }
            return obj;
        }

        void readFields() throws IOException {
            ObjectInputStream.this.bin.readFully(this.primVals, 0, this.primVals.length, false);
            int i2 = ObjectInputStream.this.passHandle;
            ObjectStreamField[] fields = this.desc.getFields(false);
            int length = fields.length - this.objVals.length;
            for (int i3 = 0; i3 < this.objVals.length; i3++) {
                this.objVals[i3] = ObjectInputStream.this.readObject0(Object.class, fields[length + i3].isUnshared());
                this.objHandles[i3] = ObjectInputStream.this.passHandle;
            }
            ObjectInputStream.this.passHandle = i2;
        }

        private int getFieldOffset(String str, Class<?> cls) {
            ObjectStreamField field = this.desc.getField(str, cls);
            if (field != null) {
                return field.getOffset();
            }
            if (this.desc.getLocalDesc().getField(str, cls) != null) {
                return -1;
            }
            throw new IllegalArgumentException("no such field " + str + " with type " + ((Object) cls));
        }
    }

    /* loaded from: rt.jar:java/io/ObjectInputStream$ValidationList.class */
    private static class ValidationList {
        private Callback list;

        /* loaded from: rt.jar:java/io/ObjectInputStream$ValidationList$Callback.class */
        private static class Callback {
            final ObjectInputValidation obj;
            final int priority;
            Callback next;
            final AccessControlContext acc;

            Callback(ObjectInputValidation objectInputValidation, int i2, Callback callback, AccessControlContext accessControlContext) {
                this.obj = objectInputValidation;
                this.priority = i2;
                this.next = callback;
                this.acc = accessControlContext;
            }
        }

        ValidationList() {
        }

        void register(ObjectInputValidation objectInputValidation, int i2) throws InvalidObjectException {
            Callback callback;
            if (objectInputValidation == null) {
                throw new InvalidObjectException("null callback");
            }
            Callback callback2 = null;
            Callback callback3 = this.list;
            while (true) {
                callback = callback3;
                if (callback == null || i2 >= callback.priority) {
                    break;
                }
                callback2 = callback;
                callback3 = callback.next;
            }
            AccessControlContext context = AccessController.getContext();
            if (callback2 != null) {
                callback2.next = new Callback(objectInputValidation, i2, callback, context);
            } else {
                this.list = new Callback(objectInputValidation, i2, this.list, context);
            }
        }

        void doCallbacks() throws InvalidObjectException {
            while (this.list != null) {
                try {
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.io.ObjectInputStream.ValidationList.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Void run() throws InvalidObjectException {
                            ValidationList.this.list.obj.validateObject();
                            return null;
                        }
                    }, this.list.acc);
                    this.list = this.list.next;
                } catch (PrivilegedActionException e2) {
                    this.list = null;
                    throw ((InvalidObjectException) e2.getException());
                }
            }
        }

        public void clear() {
            this.list = null;
        }
    }

    /* loaded from: rt.jar:java/io/ObjectInputStream$FilterValues.class */
    static class FilterValues implements ObjectInputFilter.FilterInfo {
        final Class<?> clazz;
        final long arrayLength;
        final long totalObjectRefs;
        final long depth;
        final long streamBytes;

        public FilterValues(Class<?> cls, long j2, long j3, long j4, long j5) {
            this.clazz = cls;
            this.arrayLength = j2;
            this.totalObjectRefs = j3;
            this.depth = j4;
            this.streamBytes = j5;
        }

        @Override // sun.misc.ObjectInputFilter.FilterInfo
        public Class<?> serialClass() {
            return this.clazz;
        }

        @Override // sun.misc.ObjectInputFilter.FilterInfo
        public long arrayLength() {
            return this.arrayLength;
        }

        @Override // sun.misc.ObjectInputFilter.FilterInfo
        public long references() {
            return this.totalObjectRefs;
        }

        @Override // sun.misc.ObjectInputFilter.FilterInfo
        public long depth() {
            return this.depth;
        }

        @Override // sun.misc.ObjectInputFilter.FilterInfo
        public long streamBytes() {
            return this.streamBytes;
        }
    }

    /* loaded from: rt.jar:java/io/ObjectInputStream$PeekInputStream.class */
    private static class PeekInputStream extends InputStream {
        private final InputStream in;
        private int peekb = -1;
        private long totalBytesRead = 0;

        PeekInputStream(InputStream inputStream) {
            this.in = inputStream;
        }

        int peek() throws IOException {
            if (this.peekb >= 0) {
                return this.peekb;
            }
            this.peekb = this.in.read();
            this.totalBytesRead += this.peekb >= 0 ? 1L : 0L;
            return this.peekb;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.peekb >= 0) {
                int i2 = this.peekb;
                this.peekb = -1;
                return i2;
            }
            int i3 = this.in.read();
            this.totalBytesRead += i3 >= 0 ? 1L : 0L;
            return i3;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            if (i3 == 0) {
                return 0;
            }
            if (this.peekb < 0) {
                int i4 = this.in.read(bArr, i2, i3);
                this.totalBytesRead += i4 >= 0 ? i4 : 0L;
                return i4;
            }
            bArr[i2] = (byte) this.peekb;
            this.peekb = -1;
            int i5 = this.in.read(bArr, i2 + 1, i3 - 1);
            this.totalBytesRead += i5 >= 0 ? i5 : 0L;
            if (i5 >= 0) {
                return i5 + 1;
            }
            return 1;
        }

        void readFully(byte[] bArr, int i2, int i3) throws IOException {
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 < i3) {
                    int i6 = read(bArr, i2 + i5, i3 - i5);
                    if (i6 < 0) {
                        throw new EOFException();
                    }
                    i4 = i5 + i6;
                } else {
                    return;
                }
            }
        }

        @Override // java.io.InputStream
        public long skip(long j2) throws IOException {
            if (j2 <= 0) {
                return 0L;
            }
            int i2 = 0;
            if (this.peekb >= 0) {
                this.peekb = -1;
                i2 = 0 + 1;
                j2--;
            }
            long jSkip = i2 + this.in.skip(j2);
            this.totalBytesRead += jSkip;
            return jSkip;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.in.available() + (this.peekb >= 0 ? 1 : 0);
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.in.close();
        }

        public long getBytesRead() {
            return this.totalBytesRead;
        }
    }

    /* loaded from: rt.jar:java/io/ObjectInputStream$BlockDataInputStream.class */
    private class BlockDataInputStream extends InputStream implements DataInput {
        private static final int MAX_BLOCK_SIZE = 1024;
        private static final int MAX_HEADER_SIZE = 5;
        private static final int CHAR_BUF_SIZE = 256;
        private static final int HEADER_BLOCKED = -2;
        private final PeekInputStream in;
        private final byte[] buf = new byte[1024];
        private final byte[] hbuf = new byte[5];
        private final char[] cbuf = new char[256];
        private boolean blkmode = false;
        private int pos = 0;
        private int end = -1;
        private int unread = 0;
        private final DataInputStream din = new DataInputStream(this);

        BlockDataInputStream(InputStream inputStream) {
            this.in = new PeekInputStream(inputStream);
        }

        boolean setBlockDataMode(boolean z2) throws IOException {
            if (this.blkmode == z2) {
                return this.blkmode;
            }
            if (z2) {
                this.pos = 0;
                this.end = 0;
                this.unread = 0;
            } else if (this.pos < this.end) {
                throw new IllegalStateException("unread block data");
            }
            this.blkmode = z2;
            return !this.blkmode;
        }

        boolean getBlockDataMode() {
            return this.blkmode;
        }

        void skipBlockData() throws IOException {
            if (!this.blkmode) {
                throw new IllegalStateException("not in block data mode");
            }
            while (this.end >= 0) {
                refill();
            }
        }

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:200)
            	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:61)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:101)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeEndlessLoop(LoopRegionMaker.java:281)
            	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:64)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:101)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
            */
        private int readBlockHeader(boolean r9) throws java.io.IOException {
            /*
                Method dump skipped, instructions count: 249
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.io.ObjectInputStream.BlockDataInputStream.readBlockHeader(boolean):int");
        }

        private void refill() throws IOException {
            do {
                try {
                    this.pos = 0;
                    if (this.unread > 0) {
                        int i2 = this.in.read(this.buf, 0, Math.min(this.unread, 1024));
                        if (i2 >= 0) {
                            this.end = i2;
                            this.unread -= i2;
                        } else {
                            throw new StreamCorruptedException("unexpected EOF in middle of data block");
                        }
                    } else {
                        int blockHeader = readBlockHeader(true);
                        if (blockHeader >= 0) {
                            this.end = 0;
                            this.unread = blockHeader;
                        } else {
                            this.end = -1;
                            this.unread = 0;
                        }
                    }
                } catch (IOException e2) {
                    this.pos = 0;
                    this.end = -1;
                    this.unread = 0;
                    throw e2;
                }
            } while (this.pos == this.end);
        }

        int currentBlockRemaining() {
            if (this.blkmode) {
                if (this.end >= 0) {
                    return (this.end - this.pos) + this.unread;
                }
                return 0;
            }
            throw new IllegalStateException();
        }

        int peek() throws IOException {
            if (this.blkmode) {
                if (this.pos == this.end) {
                    refill();
                }
                if (this.end >= 0) {
                    return this.buf[this.pos] & 255;
                }
                return -1;
            }
            return this.in.peek();
        }

        byte peekByte() throws IOException {
            int iPeek = peek();
            if (iPeek < 0) {
                throw new EOFException();
            }
            return (byte) iPeek;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.blkmode) {
                if (this.pos == this.end) {
                    refill();
                }
                if (this.end < 0) {
                    return -1;
                }
                byte[] bArr = this.buf;
                int i2 = this.pos;
                this.pos = i2 + 1;
                return bArr[i2] & 255;
            }
            return this.in.read();
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            return read(bArr, i2, i3, false);
        }

        @Override // java.io.InputStream
        public long skip(long j2) throws IOException {
            long j3 = j2;
            while (j3 > 0) {
                if (this.blkmode) {
                    if (this.pos == this.end) {
                        refill();
                    }
                    if (this.end < 0) {
                        break;
                    }
                    int iMin = (int) Math.min(j3, this.end - this.pos);
                    j3 -= iMin;
                    this.pos += iMin;
                } else {
                    int i2 = this.in.read(this.buf, 0, (int) Math.min(j3, 1024L));
                    if (i2 < 0) {
                        break;
                    }
                    j3 -= i2;
                }
            }
            return j2 - j3;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            int blockHeader;
            if (this.blkmode) {
                if (this.pos == this.end && this.unread == 0) {
                    do {
                        blockHeader = readBlockHeader(false);
                    } while (blockHeader == 0);
                    switch (blockHeader) {
                        case -2:
                            break;
                        case -1:
                            this.pos = 0;
                            this.end = -1;
                            break;
                        default:
                            this.pos = 0;
                            this.end = 0;
                            this.unread = blockHeader;
                            break;
                    }
                }
                int iMin = this.unread > 0 ? Math.min(this.in.available(), this.unread) : 0;
                if (this.end >= 0) {
                    return (this.end - this.pos) + iMin;
                }
                return 0;
            }
            return this.in.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.blkmode) {
                this.pos = 0;
                this.end = -1;
                this.unread = 0;
            }
            this.in.close();
        }

        int read(byte[] bArr, int i2, int i3, boolean z2) throws IOException {
            if (i3 == 0) {
                return 0;
            }
            if (this.blkmode) {
                if (this.pos == this.end) {
                    refill();
                }
                if (this.end < 0) {
                    return -1;
                }
                int iMin = Math.min(i3, this.end - this.pos);
                System.arraycopy(this.buf, this.pos, bArr, i2, iMin);
                this.pos += iMin;
                return iMin;
            }
            if (z2) {
                int i4 = this.in.read(this.buf, 0, Math.min(i3, 1024));
                if (i4 > 0) {
                    System.arraycopy(this.buf, 0, bArr, i2, i4);
                }
                return i4;
            }
            return this.in.read(bArr, i2, i3);
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr) throws IOException {
            readFully(bArr, 0, bArr.length, false);
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr, int i2, int i3) throws IOException {
            readFully(bArr, i2, i3, false);
        }

        public void readFully(byte[] bArr, int i2, int i3, boolean z2) throws IOException {
            while (i3 > 0) {
                int i4 = read(bArr, i2, i3, z2);
                if (i4 < 0) {
                    throw new EOFException();
                }
                i2 += i4;
                i3 -= i4;
            }
        }

        @Override // java.io.DataInput
        public int skipBytes(int i2) throws IOException {
            return this.din.skipBytes(i2);
        }

        @Override // java.io.DataInput
        public boolean readBoolean() throws IOException {
            int i2 = read();
            if (i2 < 0) {
                throw new EOFException();
            }
            return i2 != 0;
        }

        @Override // java.io.DataInput
        public byte readByte() throws IOException {
            int i2 = read();
            if (i2 < 0) {
                throw new EOFException();
            }
            return (byte) i2;
        }

        @Override // java.io.DataInput
        public int readUnsignedByte() throws IOException {
            int i2 = read();
            if (i2 < 0) {
                throw new EOFException();
            }
            return i2;
        }

        @Override // java.io.DataInput
        public char readChar() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.in.readFully(this.buf, 0, 2);
            } else if (this.end - this.pos < 2) {
                return this.din.readChar();
            }
            char c2 = Bits.getChar(this.buf, this.pos);
            this.pos += 2;
            return c2;
        }

        @Override // java.io.DataInput
        public short readShort() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.in.readFully(this.buf, 0, 2);
            } else if (this.end - this.pos < 2) {
                return this.din.readShort();
            }
            short s2 = Bits.getShort(this.buf, this.pos);
            this.pos += 2;
            return s2;
        }

        @Override // java.io.DataInput
        public int readUnsignedShort() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.in.readFully(this.buf, 0, 2);
            } else if (this.end - this.pos < 2) {
                return this.din.readUnsignedShort();
            }
            int i2 = Bits.getShort(this.buf, this.pos) & 65535;
            this.pos += 2;
            return i2;
        }

        @Override // java.io.DataInput
        public int readInt() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.in.readFully(this.buf, 0, 4);
            } else if (this.end - this.pos < 4) {
                return this.din.readInt();
            }
            int i2 = Bits.getInt(this.buf, this.pos);
            this.pos += 4;
            return i2;
        }

        @Override // java.io.DataInput
        public float readFloat() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.in.readFully(this.buf, 0, 4);
            } else if (this.end - this.pos < 4) {
                return this.din.readFloat();
            }
            float f2 = Bits.getFloat(this.buf, this.pos);
            this.pos += 4;
            return f2;
        }

        @Override // java.io.DataInput
        public long readLong() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.in.readFully(this.buf, 0, 8);
            } else if (this.end - this.pos < 8) {
                return this.din.readLong();
            }
            long j2 = Bits.getLong(this.buf, this.pos);
            this.pos += 8;
            return j2;
        }

        @Override // java.io.DataInput
        public double readDouble() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.in.readFully(this.buf, 0, 8);
            } else if (this.end - this.pos < 8) {
                return this.din.readDouble();
            }
            double d2 = Bits.getDouble(this.buf, this.pos);
            this.pos += 8;
            return d2;
        }

        @Override // java.io.DataInput
        public String readUTF() throws IOException {
            return readUTFBody(readUnsignedShort());
        }

        @Override // java.io.DataInput
        public String readLine() throws IOException {
            return this.din.readLine();
        }

        void readBooleans(boolean[] zArr, int i2, int i3) throws IOException {
            int iMin;
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (!this.blkmode) {
                    int iMin2 = Math.min(i4 - i2, 1024);
                    this.in.readFully(this.buf, 0, iMin2);
                    iMin = i2 + iMin2;
                    this.pos = 0;
                } else if (this.end - this.pos < 1) {
                    int i5 = i2;
                    i2++;
                    zArr[i5] = this.din.readBoolean();
                } else {
                    iMin = Math.min(i4, (i2 + this.end) - this.pos);
                }
                while (i2 < iMin) {
                    int i6 = i2;
                    i2++;
                    byte[] bArr = this.buf;
                    int i7 = this.pos;
                    this.pos = i7 + 1;
                    zArr[i6] = Bits.getBoolean(bArr, i7);
                }
            }
        }

        void readChars(char[] cArr, int i2, int i3) throws IOException {
            int iMin;
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (!this.blkmode) {
                    int iMin2 = Math.min(i4 - i2, 512);
                    this.in.readFully(this.buf, 0, iMin2 << 1);
                    iMin = i2 + iMin2;
                    this.pos = 0;
                } else if (this.end - this.pos < 2) {
                    int i5 = i2;
                    i2++;
                    cArr[i5] = this.din.readChar();
                } else {
                    iMin = Math.min(i4, i2 + ((this.end - this.pos) >> 1));
                }
                while (i2 < iMin) {
                    int i6 = i2;
                    i2++;
                    cArr[i6] = Bits.getChar(this.buf, this.pos);
                    this.pos += 2;
                }
            }
        }

        void readShorts(short[] sArr, int i2, int i3) throws IOException {
            int iMin;
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (!this.blkmode) {
                    int iMin2 = Math.min(i4 - i2, 512);
                    this.in.readFully(this.buf, 0, iMin2 << 1);
                    iMin = i2 + iMin2;
                    this.pos = 0;
                } else if (this.end - this.pos < 2) {
                    int i5 = i2;
                    i2++;
                    sArr[i5] = this.din.readShort();
                } else {
                    iMin = Math.min(i4, i2 + ((this.end - this.pos) >> 1));
                }
                while (i2 < iMin) {
                    int i6 = i2;
                    i2++;
                    sArr[i6] = Bits.getShort(this.buf, this.pos);
                    this.pos += 2;
                }
            }
        }

        void readInts(int[] iArr, int i2, int i3) throws IOException {
            int iMin;
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (!this.blkmode) {
                    int iMin2 = Math.min(i4 - i2, 256);
                    this.in.readFully(this.buf, 0, iMin2 << 2);
                    iMin = i2 + iMin2;
                    this.pos = 0;
                } else if (this.end - this.pos < 4) {
                    int i5 = i2;
                    i2++;
                    iArr[i5] = this.din.readInt();
                } else {
                    iMin = Math.min(i4, i2 + ((this.end - this.pos) >> 2));
                }
                while (i2 < iMin) {
                    int i6 = i2;
                    i2++;
                    iArr[i6] = Bits.getInt(this.buf, this.pos);
                    this.pos += 4;
                }
            }
        }

        void readFloats(float[] fArr, int i2, int i3) throws IOException {
            int iMin;
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (!this.blkmode) {
                    iMin = Math.min(i4 - i2, 256);
                    this.in.readFully(this.buf, 0, iMin << 2);
                    this.pos = 0;
                } else if (this.end - this.pos < 4) {
                    int i5 = i2;
                    i2++;
                    fArr[i5] = this.din.readFloat();
                } else {
                    iMin = Math.min(i4 - i2, (this.end - this.pos) >> 2);
                }
                ObjectInputStream.bytesToFloats(this.buf, this.pos, fArr, i2, iMin);
                i2 += iMin;
                this.pos += iMin << 2;
            }
        }

        void readLongs(long[] jArr, int i2, int i3) throws IOException {
            int iMin;
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (!this.blkmode) {
                    int iMin2 = Math.min(i4 - i2, 128);
                    this.in.readFully(this.buf, 0, iMin2 << 3);
                    iMin = i2 + iMin2;
                    this.pos = 0;
                } else if (this.end - this.pos < 8) {
                    int i5 = i2;
                    i2++;
                    jArr[i5] = this.din.readLong();
                } else {
                    iMin = Math.min(i4, i2 + ((this.end - this.pos) >> 3));
                }
                while (i2 < iMin) {
                    int i6 = i2;
                    i2++;
                    jArr[i6] = Bits.getLong(this.buf, this.pos);
                    this.pos += 8;
                }
            }
        }

        void readDoubles(double[] dArr, int i2, int i3) throws IOException {
            int iMin;
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (!this.blkmode) {
                    iMin = Math.min(i4 - i2, 128);
                    this.in.readFully(this.buf, 0, iMin << 3);
                    this.pos = 0;
                } else if (this.end - this.pos < 8) {
                    int i5 = i2;
                    i2++;
                    dArr[i5] = this.din.readDouble();
                } else {
                    iMin = Math.min(i4 - i2, (this.end - this.pos) >> 3);
                }
                ObjectInputStream.bytesToDoubles(this.buf, this.pos, dArr, i2, iMin);
                i2 += iMin;
                this.pos += iMin << 3;
            }
        }

        String readLongUTF() throws IOException {
            return readUTFBody(readLong());
        }

        private String readUTFBody(long j2) throws IOException {
            StringBuilder sb;
            if (j2 > 0 && j2 < 2147483647L) {
                sb = new StringBuilder(Math.min((int) j2, 65535));
            } else {
                sb = new StringBuilder();
            }
            if (!this.blkmode) {
                this.pos = 0;
                this.end = 0;
            }
            while (j2 > 0) {
                int i2 = this.end - this.pos;
                if (i2 >= 3 || i2 == j2) {
                    j2 -= readUTFSpan(sb, j2);
                } else if (this.blkmode) {
                    j2 -= readUTFChar(sb, j2);
                } else {
                    if (i2 > 0) {
                        System.arraycopy(this.buf, this.pos, this.buf, 0, i2);
                    }
                    this.pos = 0;
                    this.end = (int) Math.min(1024L, j2);
                    this.in.readFully(this.buf, i2, this.end - i2);
                }
            }
            return sb.toString();
        }

        private long readUTFSpan(StringBuilder sb, long j2) throws IOException {
            int i2 = 0;
            int i3 = this.pos;
            int iMin = Math.min(this.end - this.pos, 256);
            int i4 = this.pos + (j2 > ((long) iMin) ? iMin - 2 : (int) j2);
            while (this.pos < i4) {
                try {
                    byte[] bArr = this.buf;
                    int i5 = this.pos;
                    this.pos = i5 + 1;
                    int i6 = bArr[i5] & 255;
                    switch (i6 >> 4) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            int i7 = i2;
                            i2++;
                            this.cbuf[i7] = (char) i6;
                            break;
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        default:
                            throw new UTFDataFormatException();
                        case 12:
                        case 13:
                            byte[] bArr2 = this.buf;
                            int i8 = this.pos;
                            this.pos = i8 + 1;
                            byte b2 = bArr2[i8];
                            if ((b2 & 192) != 128) {
                                throw new UTFDataFormatException();
                            }
                            int i9 = i2;
                            i2++;
                            this.cbuf[i9] = (char) (((i6 & 31) << 6) | ((b2 & 63) << 0));
                            break;
                        case 14:
                            byte b3 = this.buf[this.pos + 1];
                            byte b4 = this.buf[this.pos + 0];
                            this.pos += 2;
                            if ((b4 & 192) != 128 || (b3 & 192) != 128) {
                                throw new UTFDataFormatException();
                            }
                            int i10 = i2;
                            i2++;
                            this.cbuf[i10] = (char) (((i6 & 15) << 12) | ((b4 & 63) << 6) | ((b3 & 63) << 0));
                            break;
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException e2) {
                    if (1 != 0 || this.pos - i3 > j2) {
                        this.pos = i3 + ((int) j2);
                        throw new UTFDataFormatException();
                    }
                } catch (Throwable th) {
                    if (0 != 0 || this.pos - i3 > j2) {
                        this.pos = i3 + ((int) j2);
                        throw new UTFDataFormatException();
                    }
                    throw th;
                }
            }
            if (0 != 0 || this.pos - i3 > j2) {
                this.pos = i3 + ((int) j2);
                throw new UTFDataFormatException();
            }
            sb.append(this.cbuf, 0, i2);
            return this.pos - i3;
        }

        private int readUTFChar(StringBuilder sb, long j2) throws IOException {
            int i2 = readByte() & 255;
            switch (i2 >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    sb.append((char) i2);
                    return 1;
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    throw new UTFDataFormatException();
                case 12:
                case 13:
                    if (j2 < 2) {
                        throw new UTFDataFormatException();
                    }
                    byte b2 = readByte();
                    if ((b2 & 192) != 128) {
                        throw new UTFDataFormatException();
                    }
                    sb.append((char) (((i2 & 31) << 6) | ((b2 & 63) << 0)));
                    return 2;
                case 14:
                    if (j2 < 3) {
                        if (j2 == 2) {
                            readByte();
                        }
                        throw new UTFDataFormatException();
                    }
                    byte b3 = readByte();
                    byte b4 = readByte();
                    if ((b3 & 192) != 128 || (b4 & 192) != 128) {
                        throw new UTFDataFormatException();
                    }
                    sb.append((char) (((i2 & 15) << 12) | ((b3 & 63) << 6) | ((b4 & 63) << 0)));
                    return 3;
            }
        }

        long getBytesRead() {
            return this.in.getBytesRead();
        }
    }

    /* loaded from: rt.jar:java/io/ObjectInputStream$HandleTable.class */
    private static class HandleTable {
        private static final byte STATUS_OK = 1;
        private static final byte STATUS_UNKNOWN = 2;
        private static final byte STATUS_EXCEPTION = 3;
        byte[] status;
        Object[] entries;
        HandleList[] deps;
        int lowDep = -1;
        int size = 0;

        HandleTable(int i2) {
            this.status = new byte[i2];
            this.entries = new Object[i2];
            this.deps = new HandleList[i2];
        }

        int assign(Object obj) {
            if (this.size >= this.entries.length) {
                grow();
            }
            this.status[this.size] = 2;
            this.entries[this.size] = obj;
            int i2 = this.size;
            this.size = i2 + 1;
            return i2;
        }

        void markDependency(int i2, int i3) {
            if (i2 == -1 || i3 == -1) {
                return;
            }
            switch (this.status[i2]) {
                case 2:
                    switch (this.status[i3]) {
                        case 1:
                            return;
                        case 2:
                            if (this.deps[i3] == null) {
                                this.deps[i3] = new HandleList();
                            }
                            this.deps[i3].add(i2);
                            if (this.lowDep < 0 || this.lowDep > i3) {
                                this.lowDep = i3;
                                return;
                            }
                            return;
                        case 3:
                            markException(i2, (ClassNotFoundException) this.entries[i3]);
                            return;
                        default:
                            throw new InternalError();
                    }
                case 3:
                    return;
                default:
                    throw new InternalError();
            }
        }

        void markException(int i2, ClassNotFoundException classNotFoundException) {
            switch (this.status[i2]) {
                case 2:
                    this.status[i2] = 3;
                    this.entries[i2] = classNotFoundException;
                    HandleList handleList = this.deps[i2];
                    if (handleList != null) {
                        int size = handleList.size();
                        for (int i3 = 0; i3 < size; i3++) {
                            markException(handleList.get(i3), classNotFoundException);
                        }
                        this.deps[i2] = null;
                        return;
                    }
                    return;
                case 3:
                    return;
                default:
                    throw new InternalError();
            }
        }

        void finish(int i2) {
            int i3;
            if (this.lowDep < 0) {
                i3 = i2 + 1;
            } else if (this.lowDep >= i2) {
                i3 = this.size;
                this.lowDep = -1;
            } else {
                return;
            }
            for (int i4 = i2; i4 < i3; i4++) {
                switch (this.status[i4]) {
                    case 1:
                    case 3:
                        break;
                    case 2:
                        this.status[i4] = 1;
                        this.deps[i4] = null;
                        break;
                    default:
                        throw new InternalError();
                }
            }
        }

        void setObject(int i2, Object obj) {
            switch (this.status[i2]) {
                case 1:
                case 2:
                    this.entries[i2] = obj;
                    return;
                case 3:
                    return;
                default:
                    throw new InternalError();
            }
        }

        Object lookupObject(int i2) {
            if (i2 == -1 || this.status[i2] == 3) {
                return null;
            }
            return this.entries[i2];
        }

        ClassNotFoundException lookupException(int i2) {
            if (i2 == -1 || this.status[i2] != 3) {
                return null;
            }
            return (ClassNotFoundException) this.entries[i2];
        }

        void clear() {
            Arrays.fill(this.status, 0, this.size, (byte) 0);
            Arrays.fill(this.entries, 0, this.size, (Object) null);
            Arrays.fill(this.deps, 0, this.size, (Object) null);
            this.lowDep = -1;
            this.size = 0;
        }

        int size() {
            return this.size;
        }

        private void grow() {
            int length = (this.entries.length << 1) + 1;
            byte[] bArr = new byte[length];
            Object[] objArr = new Object[length];
            HandleList[] handleListArr = new HandleList[length];
            System.arraycopy(this.status, 0, bArr, 0, this.size);
            System.arraycopy(this.entries, 0, objArr, 0, this.size);
            System.arraycopy(this.deps, 0, handleListArr, 0, this.size);
            this.status = bArr;
            this.entries = objArr;
            this.deps = handleListArr;
        }

        /* loaded from: rt.jar:java/io/ObjectInputStream$HandleTable$HandleList.class */
        private static class HandleList {
            private int[] list = new int[4];
            private int size = 0;

            public void add(int i2) {
                if (this.size >= this.list.length) {
                    int[] iArr = new int[this.list.length << 1];
                    System.arraycopy(this.list, 0, iArr, 0, this.list.length);
                    this.list = iArr;
                }
                int[] iArr2 = this.list;
                int i3 = this.size;
                this.size = i3 + 1;
                iArr2[i3] = i2;
            }

            public int get(int i2) {
                if (i2 >= this.size) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                return this.list[i2];
            }

            public int size() {
                return this.size;
            }
        }
    }

    private static Object cloneArray(Object obj) {
        if (obj instanceof Object[]) {
            return ((Object[]) obj).clone();
        }
        if (obj instanceof boolean[]) {
            return ((boolean[]) obj).clone();
        }
        if (obj instanceof byte[]) {
            return ((byte[]) obj).clone();
        }
        if (obj instanceof char[]) {
            return ((char[]) obj).clone();
        }
        if (obj instanceof double[]) {
            return ((double[]) obj).clone();
        }
        if (obj instanceof float[]) {
            return ((float[]) obj).clone();
        }
        if (obj instanceof int[]) {
            return ((int[]) obj).clone();
        }
        if (obj instanceof long[]) {
            return ((long[]) obj).clone();
        }
        if (obj instanceof short[]) {
            return ((short[]) obj).clone();
        }
        throw new AssertionError();
    }

    private void validateDescriptor(ObjectStreamClass objectStreamClass) {
        ObjectStreamClassValidator objectStreamClassValidator = this.validator;
        if (objectStreamClassValidator != null) {
            objectStreamClassValidator.validateDescriptor(objectStreamClass);
        }
    }

    private static void setValidator(ObjectInputStream objectInputStream, ObjectStreamClassValidator objectStreamClassValidator) {
        objectInputStream.validator = objectStreamClassValidator;
    }
}
