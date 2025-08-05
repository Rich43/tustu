package java.io;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.icepdf.core.util.PdfOps;
import sun.misc.SharedSecrets;
import sun.misc.Unsafe;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.ReflectionFactory;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/io/ObjectStreamClass.class */
public class ObjectStreamClass implements Serializable {
    public static final ObjectStreamField[] NO_FIELDS;
    private static final long serialVersionUID = -6120832682080437368L;
    private static final ObjectStreamField[] serialPersistentFields;
    private static boolean disableSerialConstructorChecks;
    private static final ReflectionFactory reflFactory;
    private Class<?> cl;
    private String name;
    private volatile Long suid;
    private boolean isProxy;
    private boolean isEnum;
    private boolean serializable;
    private boolean externalizable;
    private boolean hasWriteObjectData;
    private boolean hasBlockExternalData = true;
    private ClassNotFoundException resolveEx;
    private ExceptionInfo deserializeEx;
    private ExceptionInfo serializeEx;
    private ExceptionInfo defaultSerializeEx;
    private ObjectStreamField[] fields;
    private int primDataSize;
    private int numObjFields;
    private FieldReflector fieldRefl;
    private volatile ClassDataSlot[] dataLayout;
    private Constructor<?> cons;
    private ProtectionDomain[] domains;
    private Method writeObjectMethod;
    private Method readObjectMethod;
    private Method readObjectNoDataMethod;
    private Method writeReplaceMethod;
    private Method readResolveMethod;
    private ObjectStreamClass localDesc;
    private ObjectStreamClass superDesc;
    private boolean initialized;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native void initNative();

    private static native boolean hasStaticInitializer(Class<?> cls);

    static {
        $assertionsDisabled = !ObjectStreamClass.class.desiredAssertionStatus();
        NO_FIELDS = new ObjectStreamField[0];
        serialPersistentFields = NO_FIELDS;
        disableSerialConstructorChecks = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.io.ObjectStreamClass.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() {
                return "true".equals(System.getProperty("jdk.disableSerialConstructorChecks")) ? Boolean.TRUE : Boolean.FALSE;
            }
        })).booleanValue();
        reflFactory = (ReflectionFactory) AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());
        initNative();
    }

    /* loaded from: rt.jar:java/io/ObjectStreamClass$Caches.class */
    private static class Caches {
        static final ConcurrentMap<WeakClassKey, Reference<?>> localDescs = new ConcurrentHashMap();
        static final ConcurrentMap<FieldReflectorKey, Reference<?>> reflectors = new ConcurrentHashMap();
        private static final ReferenceQueue<Class<?>> localDescsQueue = new ReferenceQueue<>();
        private static final ReferenceQueue<Class<?>> reflectorsQueue = new ReferenceQueue<>();

        private Caches() {
        }
    }

    /* loaded from: rt.jar:java/io/ObjectStreamClass$ExceptionInfo.class */
    private static class ExceptionInfo {
        private final String className;
        private final String message;

        ExceptionInfo(String str, String str2) {
            this.className = str;
            this.message = str2;
        }

        InvalidClassException newInvalidClassException() {
            return new InvalidClassException(this.className, this.message);
        }
    }

    public static ObjectStreamClass lookup(Class<?> cls) {
        return lookup(cls, false);
    }

    public static ObjectStreamClass lookupAny(Class<?> cls) {
        return lookup(cls, true);
    }

    public String getName() {
        return this.name;
    }

    public long getSerialVersionUID() {
        if (this.suid == null) {
            this.suid = (Long) AccessController.doPrivileged(new PrivilegedAction<Long>() { // from class: java.io.ObjectStreamClass.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Long run() {
                    return Long.valueOf(ObjectStreamClass.computeDefaultSUID(ObjectStreamClass.this.cl));
                }
            });
        }
        return this.suid.longValue();
    }

    @CallerSensitive
    public Class<?> forClass() {
        if (this.cl == null) {
            return null;
        }
        requireInitialized();
        if (System.getSecurityManager() != null && ReflectUtil.needsPackageAccessCheck(Reflection.getCallerClass().getClassLoader(), this.cl.getClassLoader())) {
            ReflectUtil.checkPackageAccess(this.cl);
        }
        return this.cl;
    }

    public ObjectStreamField[] getFields() {
        return getFields(true);
    }

    public ObjectStreamField getField(String str) {
        return getField(str, null);
    }

    public String toString() {
        return this.name + ": static final long serialVersionUID = " + getSerialVersionUID() + "L;";
    }

    static ObjectStreamClass lookup(Class<?> cls, boolean z2) {
        if (z2 || Serializable.class.isAssignableFrom(cls)) {
            processQueue(Caches.localDescsQueue, Caches.localDescs);
            WeakClassKey weakClassKey = new WeakClassKey(cls, Caches.localDescsQueue);
            Reference<?> referencePutIfAbsent = Caches.localDescs.get(weakClassKey);
            Object objectStreamClass = null;
            if (referencePutIfAbsent != null) {
                objectStreamClass = referencePutIfAbsent.get();
            }
            EntryFuture entryFuture = null;
            if (objectStreamClass == null) {
                EntryFuture entryFuture2 = new EntryFuture();
                SoftReference softReference = new SoftReference(entryFuture2);
                do {
                    if (referencePutIfAbsent != null) {
                        Caches.localDescs.remove(weakClassKey, referencePutIfAbsent);
                    }
                    referencePutIfAbsent = Caches.localDescs.putIfAbsent(weakClassKey, softReference);
                    if (referencePutIfAbsent != null) {
                        objectStreamClass = referencePutIfAbsent.get();
                    }
                    if (referencePutIfAbsent == null) {
                        break;
                    }
                } while (objectStreamClass == null);
                if (objectStreamClass == null) {
                    entryFuture = entryFuture2;
                }
            }
            if (objectStreamClass instanceof ObjectStreamClass) {
                return (ObjectStreamClass) objectStreamClass;
            }
            if (objectStreamClass instanceof EntryFuture) {
                entryFuture = (EntryFuture) objectStreamClass;
                if (entryFuture.getOwner() == Thread.currentThread()) {
                    objectStreamClass = null;
                } else {
                    objectStreamClass = entryFuture.get();
                }
            }
            if (objectStreamClass == null) {
                try {
                    objectStreamClass = new ObjectStreamClass(cls);
                } catch (Throwable th) {
                    objectStreamClass = th;
                }
                if (entryFuture.set(objectStreamClass)) {
                    Caches.localDescs.put(weakClassKey, new SoftReference(objectStreamClass));
                } else {
                    objectStreamClass = entryFuture.get();
                }
            }
            if (objectStreamClass instanceof ObjectStreamClass) {
                return (ObjectStreamClass) objectStreamClass;
            }
            if (objectStreamClass instanceof RuntimeException) {
                throw ((RuntimeException) objectStreamClass);
            }
            if (objectStreamClass instanceof Error) {
                throw ((Error) objectStreamClass);
            }
            throw new InternalError("unexpected entry: " + objectStreamClass);
        }
        return null;
    }

    /* loaded from: rt.jar:java/io/ObjectStreamClass$EntryFuture.class */
    private static class EntryFuture {
        private static final Object unset = new Object();
        private final Thread owner;
        private Object entry;

        private EntryFuture() {
            this.owner = Thread.currentThread();
            this.entry = unset;
        }

        synchronized boolean set(Object obj) {
            if (this.entry != unset) {
                return false;
            }
            this.entry = obj;
            notifyAll();
            return true;
        }

        synchronized Object get() {
            boolean z2 = false;
            while (this.entry == unset) {
                try {
                    wait();
                } catch (InterruptedException e2) {
                    z2 = true;
                }
            }
            if (z2) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.io.ObjectStreamClass.EntryFuture.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Void run() {
                        Thread.currentThread().interrupt();
                        return null;
                    }
                });
            }
            return this.entry;
        }

        Thread getOwner() {
            return this.owner;
        }
    }

    private ObjectStreamClass(final Class<?> cls) {
        this.cl = cls;
        this.name = cls.getName();
        this.isProxy = Proxy.isProxyClass(cls);
        this.isEnum = Enum.class.isAssignableFrom(cls);
        this.serializable = Serializable.class.isAssignableFrom(cls);
        this.externalizable = Externalizable.class.isAssignableFrom(cls);
        Class<? super Object> superclass = cls.getSuperclass();
        this.superDesc = superclass != null ? lookup(superclass, false) : null;
        this.localDesc = this;
        if (this.serializable) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.io.ObjectStreamClass.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Void run() {
                    if (ObjectStreamClass.this.isEnum) {
                        ObjectStreamClass.this.suid = 0L;
                        ObjectStreamClass.this.fields = ObjectStreamClass.NO_FIELDS;
                        return null;
                    }
                    if (cls.isArray()) {
                        ObjectStreamClass.this.fields = ObjectStreamClass.NO_FIELDS;
                        return null;
                    }
                    ObjectStreamClass.this.suid = ObjectStreamClass.getDeclaredSUID(cls);
                    try {
                        ObjectStreamClass.this.fields = ObjectStreamClass.getSerialFields(cls);
                        ObjectStreamClass.this.computeFieldOffsets();
                    } catch (InvalidClassException e2) {
                        ObjectStreamClass.this.serializeEx = ObjectStreamClass.this.deserializeEx = new ExceptionInfo(e2.classname, e2.getMessage());
                        ObjectStreamClass.this.fields = ObjectStreamClass.NO_FIELDS;
                    }
                    if (ObjectStreamClass.this.externalizable) {
                        ObjectStreamClass.this.cons = ObjectStreamClass.getExternalizableConstructor(cls);
                    } else {
                        ObjectStreamClass.this.cons = ObjectStreamClass.getSerializableConstructor(cls);
                        ObjectStreamClass.this.writeObjectMethod = ObjectStreamClass.getPrivateMethod(cls, "writeObject", new Class[]{ObjectOutputStream.class}, Void.TYPE);
                        ObjectStreamClass.this.readObjectMethod = ObjectStreamClass.getPrivateMethod(cls, "readObject", new Class[]{ObjectInputStream.class}, Void.TYPE);
                        ObjectStreamClass.this.readObjectNoDataMethod = ObjectStreamClass.getPrivateMethod(cls, "readObjectNoData", null, Void.TYPE);
                        ObjectStreamClass.this.hasWriteObjectData = ObjectStreamClass.this.writeObjectMethod != null;
                    }
                    ObjectStreamClass.this.domains = ObjectStreamClass.this.getProtectionDomains(ObjectStreamClass.this.cons, cls);
                    ObjectStreamClass.this.writeReplaceMethod = ObjectStreamClass.getInheritableMethod(cls, "writeReplace", null, Object.class);
                    ObjectStreamClass.this.readResolveMethod = ObjectStreamClass.getInheritableMethod(cls, "readResolve", null, Object.class);
                    return null;
                }
            });
        } else {
            this.suid = 0L;
            this.fields = NO_FIELDS;
        }
        try {
            this.fieldRefl = getReflector(this.fields, this);
            if (this.deserializeEx == null) {
                if (this.isEnum) {
                    this.deserializeEx = new ExceptionInfo(this.name, "enum type");
                } else if (this.cons == null) {
                    this.deserializeEx = new ExceptionInfo(this.name, "no valid constructor");
                }
            }
            for (int i2 = 0; i2 < this.fields.length; i2++) {
                if (this.fields[i2].getField() == null) {
                    this.defaultSerializeEx = new ExceptionInfo(this.name, "unmatched serializable field(s) declared");
                }
            }
            this.initialized = true;
        } catch (InvalidClassException e2) {
            throw new InternalError(e2);
        }
    }

    ObjectStreamClass() {
    }

    private ProtectionDomain noPermissionsDomain() {
        Permissions permissions = new Permissions();
        permissions.setReadOnly();
        return new ProtectionDomain(null, permissions);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ProtectionDomain[] getProtectionDomains(Constructor<?> constructor, Class<?> cls) {
        ProtectionDomain[] protectionDomainArr = null;
        if (constructor != null && cls.getClassLoader() != null && System.getSecurityManager() != null) {
            Class<?> superclass = cls;
            Class<?> declaringClass = constructor.getDeclaringClass();
            HashSet hashSet = null;
            while (true) {
                if (superclass == declaringClass) {
                    break;
                }
                ProtectionDomain protectionDomain = superclass.getProtectionDomain();
                if (protectionDomain != null) {
                    if (hashSet == null) {
                        hashSet = new HashSet();
                    }
                    hashSet.add(protectionDomain);
                }
                superclass = superclass.getSuperclass();
                if (superclass == null) {
                    if (hashSet == null) {
                        hashSet = new HashSet();
                    } else {
                        hashSet.clear();
                    }
                    hashSet.add(noPermissionsDomain());
                }
            }
            if (hashSet != null) {
                protectionDomainArr = (ProtectionDomain[]) hashSet.toArray(new ProtectionDomain[0]);
            }
        }
        return protectionDomainArr;
    }

    void initProxy(Class<?> cls, ClassNotFoundException classNotFoundException, ObjectStreamClass objectStreamClass) throws InvalidClassException {
        ObjectStreamClass objectStreamClassLookup = null;
        if (cls != null) {
            objectStreamClassLookup = lookup(cls, true);
            if (!objectStreamClassLookup.isProxy) {
                throw new InvalidClassException("cannot bind proxy descriptor to a non-proxy class");
            }
        }
        this.cl = cls;
        this.resolveEx = classNotFoundException;
        this.superDesc = objectStreamClass;
        this.isProxy = true;
        this.serializable = true;
        this.suid = 0L;
        this.fields = NO_FIELDS;
        if (objectStreamClassLookup != null) {
            this.localDesc = objectStreamClassLookup;
            this.name = this.localDesc.name;
            this.externalizable = this.localDesc.externalizable;
            this.writeReplaceMethod = this.localDesc.writeReplaceMethod;
            this.readResolveMethod = this.localDesc.readResolveMethod;
            this.deserializeEx = this.localDesc.deserializeEx;
            this.domains = this.localDesc.domains;
            this.cons = this.localDesc.cons;
        }
        this.fieldRefl = getReflector(this.fields, this.localDesc);
        this.initialized = true;
    }

    void initNonProxy(ObjectStreamClass objectStreamClass, Class<?> cls, ClassNotFoundException classNotFoundException, ObjectStreamClass objectStreamClass2) throws InvalidClassException {
        long jLongValue = Long.valueOf(objectStreamClass.getSerialVersionUID()).longValue();
        ObjectStreamClass objectStreamClassLookup = null;
        if (cls != null) {
            objectStreamClassLookup = lookup(cls, true);
            if (objectStreamClassLookup.isProxy) {
                throw new InvalidClassException("cannot bind non-proxy descriptor to a proxy class");
            }
            if (objectStreamClass.isEnum != objectStreamClassLookup.isEnum) {
                throw new InvalidClassException(objectStreamClass.isEnum ? "cannot bind enum descriptor to a non-enum class" : "cannot bind non-enum descriptor to an enum class");
            }
            if (objectStreamClass.serializable == objectStreamClassLookup.serializable && !cls.isArray() && jLongValue != objectStreamClassLookup.getSerialVersionUID()) {
                throw new InvalidClassException(objectStreamClassLookup.name, "local class incompatible: stream classdesc serialVersionUID = " + jLongValue + ", local class serialVersionUID = " + objectStreamClassLookup.getSerialVersionUID());
            }
            if (!classNamesEqual(objectStreamClass.name, objectStreamClassLookup.name)) {
                throw new InvalidClassException(objectStreamClassLookup.name, "local class name incompatible with stream class name \"" + objectStreamClass.name + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            if (!objectStreamClass.isEnum) {
                if (objectStreamClass.serializable == objectStreamClassLookup.serializable && objectStreamClass.externalizable != objectStreamClassLookup.externalizable) {
                    throw new InvalidClassException(objectStreamClassLookup.name, "Serializable incompatible with Externalizable");
                }
                if (objectStreamClass.serializable != objectStreamClassLookup.serializable || objectStreamClass.externalizable != objectStreamClassLookup.externalizable || (!objectStreamClass.serializable && !objectStreamClass.externalizable)) {
                    this.deserializeEx = new ExceptionInfo(objectStreamClassLookup.name, "class invalid for deserialization");
                }
            }
        }
        this.cl = cls;
        this.resolveEx = classNotFoundException;
        this.superDesc = objectStreamClass2;
        this.name = objectStreamClass.name;
        this.suid = Long.valueOf(jLongValue);
        this.isProxy = false;
        this.isEnum = objectStreamClass.isEnum;
        this.serializable = objectStreamClass.serializable;
        this.externalizable = objectStreamClass.externalizable;
        this.hasBlockExternalData = objectStreamClass.hasBlockExternalData;
        this.hasWriteObjectData = objectStreamClass.hasWriteObjectData;
        this.fields = objectStreamClass.fields;
        this.primDataSize = objectStreamClass.primDataSize;
        this.numObjFields = objectStreamClass.numObjFields;
        if (objectStreamClassLookup != null) {
            this.localDesc = objectStreamClassLookup;
            this.writeObjectMethod = this.localDesc.writeObjectMethod;
            this.readObjectMethod = this.localDesc.readObjectMethod;
            this.readObjectNoDataMethod = this.localDesc.readObjectNoDataMethod;
            this.writeReplaceMethod = this.localDesc.writeReplaceMethod;
            this.readResolveMethod = this.localDesc.readResolveMethod;
            if (this.deserializeEx == null) {
                this.deserializeEx = this.localDesc.deserializeEx;
            }
            this.domains = this.localDesc.domains;
            this.cons = this.localDesc.cons;
        }
        this.fieldRefl = getReflector(this.fields, this.localDesc);
        this.fields = this.fieldRefl.getFields();
        this.initialized = true;
    }

    void readNonProxy(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.name = objectInputStream.readUTF();
        this.suid = Long.valueOf(objectInputStream.readLong());
        this.isProxy = false;
        byte b2 = objectInputStream.readByte();
        this.hasWriteObjectData = (b2 & 1) != 0;
        this.hasBlockExternalData = (b2 & 8) != 0;
        this.externalizable = (b2 & 4) != 0;
        boolean z2 = (b2 & 2) != 0;
        if (this.externalizable && z2) {
            throw new InvalidClassException(this.name, "serializable and externalizable flags conflict");
        }
        this.serializable = this.externalizable || z2;
        this.isEnum = (b2 & 16) != 0;
        if (this.isEnum && this.suid.longValue() != 0) {
            throw new InvalidClassException(this.name, "enum descriptor has non-zero serialVersionUID: " + ((Object) this.suid));
        }
        int i2 = objectInputStream.readShort();
        if (this.isEnum && i2 != 0) {
            throw new InvalidClassException(this.name, "enum descriptor has non-zero field count: " + i2);
        }
        this.fields = i2 > 0 ? new ObjectStreamField[i2] : NO_FIELDS;
        for (int i3 = 0; i3 < i2; i3++) {
            char c2 = (char) objectInputStream.readByte();
            String utf = objectInputStream.readUTF();
            try {
                this.fields[i3] = new ObjectStreamField(utf, (c2 == 'L' || c2 == '[') ? objectInputStream.readTypeString() : new String(new char[]{c2}), false);
            } catch (RuntimeException e2) {
                throw ((IOException) new InvalidClassException(this.name, "invalid descriptor for field " + utf).initCause(e2));
            }
        }
        computeFieldOffsets();
    }

    void writeNonProxy(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeUTF(this.name);
        objectOutputStream.writeLong(getSerialVersionUID());
        byte b2 = 0;
        if (this.externalizable) {
            b2 = (byte) (0 | 4);
            if (objectOutputStream.getProtocolVersion() != 1) {
                b2 = (byte) (b2 | 8);
            }
        } else if (this.serializable) {
            b2 = (byte) (0 | 2);
        }
        if (this.hasWriteObjectData) {
            b2 = (byte) (b2 | 1);
        }
        if (this.isEnum) {
            b2 = (byte) (b2 | 16);
        }
        objectOutputStream.writeByte(b2);
        objectOutputStream.writeShort(this.fields.length);
        for (int i2 = 0; i2 < this.fields.length; i2++) {
            ObjectStreamField objectStreamField = this.fields[i2];
            objectOutputStream.writeByte(objectStreamField.getTypeCode());
            objectOutputStream.writeUTF(objectStreamField.getName());
            if (!objectStreamField.isPrimitive()) {
                objectOutputStream.writeTypeString(objectStreamField.getTypeString());
            }
        }
    }

    ClassNotFoundException getResolveException() {
        return this.resolveEx;
    }

    private final void requireInitialized() {
        if (!this.initialized) {
            throw new InternalError("Unexpected call when not initialized");
        }
    }

    final void checkInitialized() throws InvalidClassException {
        if (!this.initialized) {
            throw new InvalidClassException("Class descriptor should be initialized");
        }
    }

    void checkDeserialize() throws InvalidClassException {
        requireInitialized();
        if (this.deserializeEx != null) {
            throw this.deserializeEx.newInvalidClassException();
        }
    }

    void checkSerialize() throws InvalidClassException {
        requireInitialized();
        if (this.serializeEx != null) {
            throw this.serializeEx.newInvalidClassException();
        }
    }

    void checkDefaultSerialize() throws InvalidClassException {
        requireInitialized();
        if (this.defaultSerializeEx != null) {
            throw this.defaultSerializeEx.newInvalidClassException();
        }
    }

    ObjectStreamClass getSuperDesc() {
        requireInitialized();
        return this.superDesc;
    }

    ObjectStreamClass getLocalDesc() {
        requireInitialized();
        return this.localDesc;
    }

    ObjectStreamField[] getFields(boolean z2) {
        return z2 ? (ObjectStreamField[]) this.fields.clone() : this.fields;
    }

    ObjectStreamField getField(String str, Class<?> cls) {
        for (int i2 = 0; i2 < this.fields.length; i2++) {
            ObjectStreamField objectStreamField = this.fields[i2];
            if (objectStreamField.getName().equals(str)) {
                if (cls == null || (cls == Object.class && !objectStreamField.isPrimitive())) {
                    return objectStreamField;
                }
                Class<?> type = objectStreamField.getType();
                if (type != null && cls.isAssignableFrom(type)) {
                    return objectStreamField;
                }
            }
        }
        return null;
    }

    boolean isProxy() {
        requireInitialized();
        return this.isProxy;
    }

    boolean isEnum() {
        requireInitialized();
        return this.isEnum;
    }

    boolean isExternalizable() {
        requireInitialized();
        return this.externalizable;
    }

    boolean isSerializable() {
        requireInitialized();
        return this.serializable;
    }

    boolean hasBlockExternalData() {
        requireInitialized();
        return this.hasBlockExternalData;
    }

    boolean hasWriteObjectData() {
        requireInitialized();
        return this.hasWriteObjectData;
    }

    boolean isInstantiable() {
        requireInitialized();
        return this.cons != null;
    }

    boolean hasWriteObjectMethod() {
        requireInitialized();
        return this.writeObjectMethod != null;
    }

    boolean hasReadObjectMethod() {
        requireInitialized();
        return this.readObjectMethod != null;
    }

    boolean hasReadObjectNoDataMethod() {
        requireInitialized();
        return this.readObjectNoDataMethod != null;
    }

    boolean hasWriteReplaceMethod() {
        requireInitialized();
        return this.writeReplaceMethod != null;
    }

    boolean hasReadResolveMethod() {
        requireInitialized();
        return this.readResolveMethod != null;
    }

    Object newInstance() throws UnsupportedOperationException, IllegalAccessException, InstantiationException, InvocationTargetException {
        requireInitialized();
        if (this.cons != null) {
            try {
                if (this.domains == null || this.domains.length == 0) {
                    return this.cons.newInstance(new Object[0]);
                }
                try {
                    return SharedSecrets.getJavaSecurityAccess().doIntersectionPrivilege(() -> {
                        try {
                            return this.cons.newInstance(new Object[0]);
                        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e2) {
                            throw new UndeclaredThrowableException(e2);
                        }
                    }, AccessController.getContext(), new AccessControlContext(this.domains));
                } catch (UndeclaredThrowableException e2) {
                    Throwable cause = e2.getCause();
                    if (cause instanceof InstantiationException) {
                        throw ((InstantiationException) cause);
                    }
                    if (cause instanceof InvocationTargetException) {
                        throw ((InvocationTargetException) cause);
                    }
                    if (cause instanceof IllegalAccessException) {
                        throw ((IllegalAccessException) cause);
                    }
                    throw e2;
                }
            } catch (IllegalAccessException e3) {
                throw new InternalError(e3);
            } catch (InstantiationError e4) {
                throw ((InstantiationException) new InstantiationException().initCause(e4));
            }
        }
        throw new UnsupportedOperationException();
    }

    void invokeWriteObject(Object obj, ObjectOutputStream objectOutputStream) throws UnsupportedOperationException, IOException, IllegalArgumentException {
        requireInitialized();
        if (this.writeObjectMethod != null) {
            try {
                this.writeObjectMethod.invoke(obj, objectOutputStream);
                return;
            } catch (IllegalAccessException e2) {
                throw new InternalError(e2);
            } catch (InvocationTargetException e3) {
                Throwable targetException = e3.getTargetException();
                if (targetException instanceof IOException) {
                    throw ((IOException) targetException);
                }
                throwMiscException(targetException);
                return;
            }
        }
        throw new UnsupportedOperationException();
    }

    void invokeReadObject(Object obj, ObjectInputStream objectInputStream) throws UnsupportedOperationException, ClassNotFoundException, IOException, IllegalArgumentException {
        requireInitialized();
        if (this.readObjectMethod != null) {
            try {
                this.readObjectMethod.invoke(obj, objectInputStream);
                return;
            } catch (IllegalAccessException e2) {
                throw new InternalError(e2);
            } catch (InvocationTargetException e3) {
                Throwable targetException = e3.getTargetException();
                if (targetException instanceof ClassNotFoundException) {
                    throw ((ClassNotFoundException) targetException);
                }
                if (targetException instanceof IOException) {
                    throw ((IOException) targetException);
                }
                throwMiscException(targetException);
                return;
            }
        }
        throw new UnsupportedOperationException();
    }

    void invokeReadObjectNoData(Object obj) throws UnsupportedOperationException, IOException {
        requireInitialized();
        if (this.readObjectNoDataMethod != null) {
            try {
                this.readObjectNoDataMethod.invoke(obj, (Object[]) null);
                return;
            } catch (IllegalAccessException e2) {
                throw new InternalError(e2);
            } catch (InvocationTargetException e3) {
                Throwable targetException = e3.getTargetException();
                if (targetException instanceof ObjectStreamException) {
                    throw ((ObjectStreamException) targetException);
                }
                throwMiscException(targetException);
                return;
            }
        }
        throw new UnsupportedOperationException();
    }

    Object invokeWriteReplace(Object obj) throws UnsupportedOperationException, IOException {
        requireInitialized();
        if (this.writeReplaceMethod != null) {
            try {
                return this.writeReplaceMethod.invoke(obj, (Object[]) null);
            } catch (IllegalAccessException e2) {
                throw new InternalError(e2);
            } catch (InvocationTargetException e3) {
                Throwable targetException = e3.getTargetException();
                if (targetException instanceof ObjectStreamException) {
                    throw ((ObjectStreamException) targetException);
                }
                throwMiscException(targetException);
                throw new InternalError(targetException);
            }
        }
        throw new UnsupportedOperationException();
    }

    Object invokeReadResolve(Object obj) throws UnsupportedOperationException, IOException {
        requireInitialized();
        if (this.readResolveMethod != null) {
            try {
                return this.readResolveMethod.invoke(obj, (Object[]) null);
            } catch (IllegalAccessException e2) {
                throw new InternalError(e2);
            } catch (InvocationTargetException e3) {
                Throwable targetException = e3.getTargetException();
                if (targetException instanceof ObjectStreamException) {
                    throw ((ObjectStreamException) targetException);
                }
                throwMiscException(targetException);
                throw new InternalError(targetException);
            }
        }
        throw new UnsupportedOperationException();
    }

    /* loaded from: rt.jar:java/io/ObjectStreamClass$ClassDataSlot.class */
    static class ClassDataSlot {
        final ObjectStreamClass desc;
        final boolean hasData;

        ClassDataSlot(ObjectStreamClass objectStreamClass, boolean z2) {
            this.desc = objectStreamClass;
            this.hasData = z2;
        }
    }

    ClassDataSlot[] getClassDataLayout() throws InvalidClassException {
        if (this.dataLayout == null) {
            this.dataLayout = getClassDataLayout0();
        }
        return this.dataLayout;
    }

    private ClassDataSlot[] getClassDataLayout0() throws InvalidClassException {
        Class<?> cls;
        ArrayList arrayList = new ArrayList();
        Class<?> superclass = this.cl;
        Class<?> superclass2 = this.cl;
        while (true) {
            cls = superclass2;
            if (cls == null || !Serializable.class.isAssignableFrom(cls)) {
                break;
            }
            superclass2 = cls.getSuperclass();
        }
        HashSet hashSet = new HashSet(3);
        ObjectStreamClass objectStreamClass = this;
        while (true) {
            ObjectStreamClass objectStreamClass2 = objectStreamClass;
            if (objectStreamClass2 != null) {
                if (hashSet.contains(objectStreamClass2.name)) {
                    throw new InvalidClassException("Circular reference.");
                }
                hashSet.add(objectStreamClass2.name);
                String name = objectStreamClass2.cl != null ? objectStreamClass2.cl.getName() : objectStreamClass2.name;
                Class<?> cls2 = null;
                Class<?> superclass3 = superclass;
                while (true) {
                    Class<?> cls3 = superclass3;
                    if (cls3 == cls) {
                        break;
                    }
                    if (!name.equals(cls3.getName())) {
                        superclass3 = cls3.getSuperclass();
                    } else {
                        cls2 = cls3;
                        break;
                    }
                }
                if (cls2 != null) {
                    Class<?> superclass4 = superclass;
                    while (true) {
                        Class<?> cls4 = superclass4;
                        if (cls4 == cls2) {
                            break;
                        }
                        arrayList.add(new ClassDataSlot(lookup(cls4, true), false));
                        superclass4 = cls4.getSuperclass();
                    }
                    superclass = cls2.getSuperclass();
                }
                arrayList.add(new ClassDataSlot(objectStreamClass2.getVariantFor(cls2), true));
                objectStreamClass = objectStreamClass2.superDesc;
            } else {
                Class<?> superclass5 = superclass;
                while (true) {
                    Class<?> cls5 = superclass5;
                    if (cls5 != cls) {
                        arrayList.add(new ClassDataSlot(lookup(cls5, true), false));
                        superclass5 = cls5.getSuperclass();
                    } else {
                        Collections.reverse(arrayList);
                        return (ClassDataSlot[]) arrayList.toArray(new ClassDataSlot[arrayList.size()]);
                    }
                }
            }
        }
    }

    int getPrimDataSize() {
        return this.primDataSize;
    }

    int getNumObjFields() {
        return this.numObjFields;
    }

    void getPrimFieldValues(Object obj, byte[] bArr) {
        this.fieldRefl.getPrimFieldValues(obj, bArr);
    }

    void setPrimFieldValues(Object obj, byte[] bArr) {
        this.fieldRefl.setPrimFieldValues(obj, bArr);
    }

    void getObjFieldValues(Object obj, Object[] objArr) {
        this.fieldRefl.getObjFieldValues(obj, objArr);
    }

    void setObjFieldValues(Object obj, Object[] objArr) {
        this.fieldRefl.setObjFieldValues(obj, objArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void computeFieldOffsets() throws InvalidClassException {
        this.primDataSize = 0;
        this.numObjFields = 0;
        int i2 = -1;
        for (int i3 = 0; i3 < this.fields.length; i3++) {
            ObjectStreamField objectStreamField = this.fields[i3];
            switch (objectStreamField.getTypeCode()) {
                case 'B':
                case 'Z':
                    int i4 = this.primDataSize;
                    this.primDataSize = i4 + 1;
                    objectStreamField.setOffset(i4);
                    break;
                case 'C':
                case 'S':
                    objectStreamField.setOffset(this.primDataSize);
                    this.primDataSize += 2;
                    break;
                case 'D':
                case 'J':
                    objectStreamField.setOffset(this.primDataSize);
                    this.primDataSize += 8;
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    throw new InternalError();
                case 'F':
                case 'I':
                    objectStreamField.setOffset(this.primDataSize);
                    this.primDataSize += 4;
                    break;
                case 'L':
                case '[':
                    int i5 = this.numObjFields;
                    this.numObjFields = i5 + 1;
                    objectStreamField.setOffset(i5);
                    if (i2 == -1) {
                        i2 = i3;
                        break;
                    } else {
                        break;
                    }
            }
        }
        if (i2 != -1 && i2 + this.numObjFields != this.fields.length) {
            throw new InvalidClassException(this.name, "illegal field order");
        }
    }

    private ObjectStreamClass getVariantFor(Class<?> cls) throws InvalidClassException {
        if (this.cl == cls) {
            return this;
        }
        ObjectStreamClass objectStreamClass = new ObjectStreamClass();
        if (this.isProxy) {
            objectStreamClass.initProxy(cls, null, this.superDesc);
        } else {
            objectStreamClass.initNonProxy(this, cls, null, this.superDesc);
        }
        return objectStreamClass;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Constructor<?> getExternalizableConstructor(Class<?> cls) throws SecurityException {
        try {
            Constructor<?> declaredConstructor = cls.getDeclaredConstructor((Class[]) null);
            declaredConstructor.setAccessible(true);
            if ((declaredConstructor.getModifiers() & 1) != 0) {
                return declaredConstructor;
            }
            return null;
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    private static boolean superHasAccessibleConstructor(Class<?> cls) throws SecurityException {
        Class<? super Object> superclass = cls.getSuperclass();
        if (!$assertionsDisabled && !Serializable.class.isAssignableFrom(cls)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && superclass == null) {
            throw new AssertionError();
        }
        if (packageEquals(cls, superclass)) {
            for (Constructor<?> constructor : superclass.getDeclaredConstructors()) {
                if ((constructor.getModifiers() & 2) == 0) {
                    return true;
                }
            }
            return false;
        }
        if ((superclass.getModifiers() & 5) == 0) {
            return false;
        }
        for (Constructor<?> constructor2 : superclass.getDeclaredConstructors()) {
            if ((constructor2.getModifiers() & 5) != 0) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Constructor<?> getSerializableConstructor(Class<?> cls) throws SecurityException {
        Class<?> cls2 = cls;
        while (Serializable.class.isAssignableFrom(cls2)) {
            Class<?> cls3 = cls2;
            Class<? super Object> superclass = cls2.getSuperclass();
            cls2 = superclass;
            if (superclass != null) {
                if (!disableSerialConstructorChecks && !superHasAccessibleConstructor(cls3)) {
                    return null;
                }
            } else {
                return null;
            }
        }
        try {
            Constructor<?> declaredConstructor = cls2.getDeclaredConstructor((Class[]) null);
            int modifiers = declaredConstructor.getModifiers();
            if ((modifiers & 2) == 0) {
                if ((modifiers & 5) == 0 && !packageEquals(cls, cls2)) {
                    return null;
                }
                Constructor<?> constructorNewConstructorForSerialization = reflFactory.newConstructorForSerialization(cls, declaredConstructor);
                constructorNewConstructorForSerialization.setAccessible(true);
                return constructorNewConstructorForSerialization;
            }
            return null;
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Method getInheritableMethod(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2) throws SecurityException {
        Class<?> cls3;
        Method declaredMethod = null;
        Class<?> superclass = cls;
        while (true) {
            cls3 = superclass;
            if (cls3 == null) {
                break;
            }
            try {
                declaredMethod = cls3.getDeclaredMethod(str, clsArr);
                break;
            } catch (NoSuchMethodException e2) {
                superclass = cls3.getSuperclass();
            }
        }
        if (declaredMethod == null || declaredMethod.getReturnType() != cls2) {
            return null;
        }
        declaredMethod.setAccessible(true);
        int modifiers = declaredMethod.getModifiers();
        if ((modifiers & OpCodes.NODETYPE_PI) != 0) {
            return null;
        }
        if ((modifiers & 5) != 0) {
            return declaredMethod;
        }
        if ((modifiers & 2) != 0) {
            if (cls == cls3) {
                return declaredMethod;
            }
            return null;
        }
        if (packageEquals(cls, cls3)) {
            return declaredMethod;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Method getPrivateMethod(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2) throws SecurityException {
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
            declaredMethod.setAccessible(true);
            int modifiers = declaredMethod.getModifiers();
            if (declaredMethod.getReturnType() == cls2 && (modifiers & 8) == 0) {
                if ((modifiers & 2) != 0) {
                    return declaredMethod;
                }
            }
            return null;
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    private static boolean packageEquals(Class<?> cls, Class<?> cls2) {
        return cls.getClassLoader() == cls2.getClassLoader() && getPackageName(cls).equals(getPackageName(cls2));
    }

    private static String getPackageName(Class<?> cls) {
        String name = cls.getName();
        int iLastIndexOf = name.lastIndexOf(91);
        if (iLastIndexOf >= 0) {
            name = name.substring(iLastIndexOf + 2);
        }
        int iLastIndexOf2 = name.lastIndexOf(46);
        return iLastIndexOf2 >= 0 ? name.substring(0, iLastIndexOf2) : "";
    }

    private static boolean classNamesEqual(String str, String str2) {
        return str.substring(str.lastIndexOf(46) + 1).equals(str2.substring(str2.lastIndexOf(46) + 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getClassSignature(Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        while (cls.isArray()) {
            sb.append('[');
            cls = cls.getComponentType();
        }
        if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                sb.append('I');
            } else if (cls == Byte.TYPE) {
                sb.append('B');
            } else if (cls == Long.TYPE) {
                sb.append('J');
            } else if (cls == Float.TYPE) {
                sb.append('F');
            } else if (cls == Double.TYPE) {
                sb.append('D');
            } else if (cls == Short.TYPE) {
                sb.append('S');
            } else if (cls == Character.TYPE) {
                sb.append('C');
            } else if (cls == Boolean.TYPE) {
                sb.append('Z');
            } else if (cls == Void.TYPE) {
                sb.append('V');
            } else {
                throw new InternalError();
            }
        } else {
            sb.append('L' + cls.getName().replace('.', '/') + ';');
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getMethodSignature(Class<?>[] clsArr, Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (Class<?> cls2 : clsArr) {
            sb.append(getClassSignature(cls2));
        }
        sb.append(')');
        sb.append(getClassSignature(cls));
        return sb.toString();
    }

    private static void throwMiscException(Throwable th) throws IOException {
        if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        }
        if (th instanceof Error) {
            throw ((Error) th);
        }
        IOException iOException = new IOException("unexpected exception type");
        iOException.initCause(th);
        throw iOException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ObjectStreamField[] getSerialFields(Class<?> cls) throws InvalidClassException, SecurityException {
        ObjectStreamField[] defaultSerialFields;
        if (Serializable.class.isAssignableFrom(cls) && !Externalizable.class.isAssignableFrom(cls) && !Proxy.isProxyClass(cls) && !cls.isInterface()) {
            ObjectStreamField[] declaredSerialFields = getDeclaredSerialFields(cls);
            defaultSerialFields = declaredSerialFields;
            if (declaredSerialFields == null) {
                defaultSerialFields = getDefaultSerialFields(cls);
            }
            Arrays.sort(defaultSerialFields);
        } else {
            defaultSerialFields = NO_FIELDS;
        }
        return defaultSerialFields;
    }

    private static ObjectStreamField[] getDeclaredSerialFields(Class<?> cls) throws InvalidClassException, SecurityException {
        ObjectStreamField[] objectStreamFieldArr = null;
        try {
            Field declaredField = cls.getDeclaredField("serialPersistentFields");
            if ((declaredField.getModifiers() & 26) == 26) {
                declaredField.setAccessible(true);
                objectStreamFieldArr = (ObjectStreamField[]) declaredField.get(null);
            }
        } catch (Exception e2) {
        }
        if (objectStreamFieldArr == null) {
            return null;
        }
        if (objectStreamFieldArr.length == 0) {
            return NO_FIELDS;
        }
        ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[objectStreamFieldArr.length];
        HashSet hashSet = new HashSet(objectStreamFieldArr.length);
        for (int i2 = 0; i2 < objectStreamFieldArr.length; i2++) {
            ObjectStreamField objectStreamField = objectStreamFieldArr[i2];
            String name = objectStreamField.getName();
            if (hashSet.contains(name)) {
                throw new InvalidClassException("multiple serializable fields named " + name);
            }
            hashSet.add(name);
            try {
                Field declaredField2 = cls.getDeclaredField(name);
                if (declaredField2.getType() == objectStreamField.getType() && (declaredField2.getModifiers() & 8) == 0) {
                    objectStreamFieldArr2[i2] = new ObjectStreamField(declaredField2, objectStreamField.isUnshared(), true);
                }
            } catch (NoSuchFieldException e3) {
            }
            if (objectStreamFieldArr2[i2] == null) {
                objectStreamFieldArr2[i2] = new ObjectStreamField(name, objectStreamField.getType(), objectStreamField.isUnshared());
            }
        }
        return objectStreamFieldArr2;
    }

    private static ObjectStreamField[] getDefaultSerialFields(Class<?> cls) throws SecurityException {
        Field[] declaredFields = cls.getDeclaredFields();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < declaredFields.length; i2++) {
            if ((declaredFields[i2].getModifiers() & 136) == 0) {
                arrayList.add(new ObjectStreamField(declaredFields[i2], false, true));
            }
        }
        int size = arrayList.size();
        return size == 0 ? NO_FIELDS : (ObjectStreamField[]) arrayList.toArray(new ObjectStreamField[size]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Long getDeclaredSUID(Class<?> cls) {
        try {
            Field declaredField = cls.getDeclaredField("serialVersionUID");
            if ((declaredField.getModifiers() & 24) == 24) {
                declaredField.setAccessible(true);
                return Long.valueOf(declaredField.getLong(null));
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long computeDefaultSUID(Class<?> cls) throws SecurityException {
        if (!Serializable.class.isAssignableFrom(cls) || Proxy.isProxyClass(cls)) {
            return 0L;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF(cls.getName());
            int modifiers = cls.getModifiers() & com.sun.corba.se.impl.io.ObjectStreamClass.CLASS_MASK;
            Method[] declaredMethods = cls.getDeclaredMethods();
            if ((modifiers & 512) != 0) {
                modifiers = declaredMethods.length > 0 ? modifiers | 1024 : modifiers & (-1025);
            }
            dataOutputStream.writeInt(modifiers);
            if (!cls.isArray()) {
                Class<?>[] interfaces = cls.getInterfaces();
                String[] strArr = new String[interfaces.length];
                for (int i2 = 0; i2 < interfaces.length; i2++) {
                    strArr[i2] = interfaces[i2].getName();
                }
                Arrays.sort(strArr);
                for (String str : strArr) {
                    dataOutputStream.writeUTF(str);
                }
            }
            Field[] declaredFields = cls.getDeclaredFields();
            MemberSignature[] memberSignatureArr = new MemberSignature[declaredFields.length];
            for (int i3 = 0; i3 < declaredFields.length; i3++) {
                memberSignatureArr[i3] = new MemberSignature(declaredFields[i3]);
            }
            Arrays.sort(memberSignatureArr, new Comparator<MemberSignature>() { // from class: java.io.ObjectStreamClass.4
                @Override // java.util.Comparator
                public int compare(MemberSignature memberSignature, MemberSignature memberSignature2) {
                    return memberSignature.name.compareTo(memberSignature2.name);
                }
            });
            for (MemberSignature memberSignature : memberSignatureArr) {
                int modifiers2 = memberSignature.member.getModifiers() & 223;
                if ((modifiers2 & 2) == 0 || (modifiers2 & 136) == 0) {
                    dataOutputStream.writeUTF(memberSignature.name);
                    dataOutputStream.writeInt(modifiers2);
                    dataOutputStream.writeUTF(memberSignature.signature);
                }
            }
            if (hasStaticInitializer(cls)) {
                dataOutputStream.writeUTF(Constants.STATIC_INITIALIZER_NAME);
                dataOutputStream.writeInt(8);
                dataOutputStream.writeUTF("()V");
            }
            Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
            MemberSignature[] memberSignatureArr2 = new MemberSignature[declaredConstructors.length];
            for (int i4 = 0; i4 < declaredConstructors.length; i4++) {
                memberSignatureArr2[i4] = new MemberSignature(declaredConstructors[i4]);
            }
            Arrays.sort(memberSignatureArr2, new Comparator<MemberSignature>() { // from class: java.io.ObjectStreamClass.5
                @Override // java.util.Comparator
                public int compare(MemberSignature memberSignature2, MemberSignature memberSignature3) {
                    return memberSignature2.signature.compareTo(memberSignature3.signature);
                }
            });
            for (MemberSignature memberSignature2 : memberSignatureArr2) {
                int modifiers3 = memberSignature2.member.getModifiers() & com.sun.corba.se.impl.io.ObjectStreamClass.METHOD_MASK;
                if ((modifiers3 & 2) == 0) {
                    dataOutputStream.writeUTF(Constants.CONSTRUCTOR_NAME);
                    dataOutputStream.writeInt(modifiers3);
                    dataOutputStream.writeUTF(memberSignature2.signature.replace('/', '.'));
                }
            }
            MemberSignature[] memberSignatureArr3 = new MemberSignature[declaredMethods.length];
            for (int i5 = 0; i5 < declaredMethods.length; i5++) {
                memberSignatureArr3[i5] = new MemberSignature(declaredMethods[i5]);
            }
            Arrays.sort(memberSignatureArr3, new Comparator<MemberSignature>() { // from class: java.io.ObjectStreamClass.6
                @Override // java.util.Comparator
                public int compare(MemberSignature memberSignature3, MemberSignature memberSignature4) {
                    int iCompareTo = memberSignature3.name.compareTo(memberSignature4.name);
                    if (iCompareTo == 0) {
                        iCompareTo = memberSignature3.signature.compareTo(memberSignature4.signature);
                    }
                    return iCompareTo;
                }
            });
            for (MemberSignature memberSignature3 : memberSignatureArr3) {
                int modifiers4 = memberSignature3.member.getModifiers() & com.sun.corba.se.impl.io.ObjectStreamClass.METHOD_MASK;
                if ((modifiers4 & 2) == 0) {
                    dataOutputStream.writeUTF(memberSignature3.name);
                    dataOutputStream.writeInt(modifiers4);
                    dataOutputStream.writeUTF(memberSignature3.signature.replace('/', '.'));
                }
            }
            dataOutputStream.flush();
            long j2 = 0;
            for (int iMin = Math.min(MessageDigest.getInstance("SHA").digest(byteArrayOutputStream.toByteArray()).length, 8) - 1; iMin >= 0; iMin--) {
                j2 = (j2 << 8) | (r0[iMin] & 255);
            }
            return j2;
        } catch (IOException e2) {
            throw new InternalError(e2);
        } catch (NoSuchAlgorithmException e3) {
            throw new SecurityException(e3.getMessage());
        }
    }

    /* loaded from: rt.jar:java/io/ObjectStreamClass$MemberSignature.class */
    private static class MemberSignature {
        public final Member member;
        public final String name;
        public final String signature;

        public MemberSignature(Field field) {
            this.member = field;
            this.name = field.getName();
            this.signature = ObjectStreamClass.getClassSignature(field.getType());
        }

        public MemberSignature(Constructor<?> constructor) {
            this.member = constructor;
            this.name = constructor.getName();
            this.signature = ObjectStreamClass.getMethodSignature(constructor.getParameterTypes(), Void.TYPE);
        }

        public MemberSignature(Method method) {
            this.member = method;
            this.name = method.getName();
            this.signature = ObjectStreamClass.getMethodSignature(method.getParameterTypes(), method.getReturnType());
        }
    }

    /* loaded from: rt.jar:java/io/ObjectStreamClass$FieldReflector.class */
    private static class FieldReflector {
        private static final Unsafe unsafe = Unsafe.getUnsafe();
        private final ObjectStreamField[] fields;
        private final int numPrimFields;
        private final long[] readKeys;
        private final long[] writeKeys;
        private final int[] offsets;
        private final char[] typeCodes;
        private final Class<?>[] types;

        FieldReflector(ObjectStreamField[] objectStreamFieldArr) {
            this.fields = objectStreamFieldArr;
            int length = objectStreamFieldArr.length;
            this.readKeys = new long[length];
            this.writeKeys = new long[length];
            this.offsets = new int[length];
            this.typeCodes = new char[length];
            ArrayList arrayList = new ArrayList();
            HashSet hashSet = new HashSet();
            for (int i2 = 0; i2 < length; i2++) {
                ObjectStreamField objectStreamField = objectStreamFieldArr[i2];
                Field field = objectStreamField.getField();
                long jObjectFieldOffset = field != null ? unsafe.objectFieldOffset(field) : -1L;
                this.readKeys[i2] = jObjectFieldOffset;
                this.writeKeys[i2] = hashSet.add(Long.valueOf(jObjectFieldOffset)) ? jObjectFieldOffset : -1L;
                this.offsets[i2] = objectStreamField.getOffset();
                this.typeCodes[i2] = objectStreamField.getTypeCode();
                if (!objectStreamField.isPrimitive()) {
                    arrayList.add(field != null ? field.getType() : null);
                }
            }
            this.types = (Class[]) arrayList.toArray(new Class[arrayList.size()]);
            this.numPrimFields = length - this.types.length;
        }

        ObjectStreamField[] getFields() {
            return this.fields;
        }

        void getPrimFieldValues(Object obj, byte[] bArr) {
            if (obj == null) {
                throw new NullPointerException();
            }
            for (int i2 = 0; i2 < this.numPrimFields; i2++) {
                long j2 = this.readKeys[i2];
                int i3 = this.offsets[i2];
                switch (this.typeCodes[i2]) {
                    case 'B':
                        bArr[i3] = unsafe.getByte(obj, j2);
                        break;
                    case 'C':
                        Bits.putChar(bArr, i3, unsafe.getChar(obj, j2));
                        break;
                    case 'D':
                        Bits.putDouble(bArr, i3, unsafe.getDouble(obj, j2));
                        break;
                    case 'E':
                    case 'G':
                    case 'H':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    default:
                        throw new InternalError();
                    case 'F':
                        Bits.putFloat(bArr, i3, unsafe.getFloat(obj, j2));
                        break;
                    case 'I':
                        Bits.putInt(bArr, i3, unsafe.getInt(obj, j2));
                        break;
                    case 'J':
                        Bits.putLong(bArr, i3, unsafe.getLong(obj, j2));
                        break;
                    case 'S':
                        Bits.putShort(bArr, i3, unsafe.getShort(obj, j2));
                        break;
                    case 'Z':
                        Bits.putBoolean(bArr, i3, unsafe.getBoolean(obj, j2));
                        break;
                }
            }
        }

        void setPrimFieldValues(Object obj, byte[] bArr) {
            if (obj == null) {
                throw new NullPointerException();
            }
            for (int i2 = 0; i2 < this.numPrimFields; i2++) {
                long j2 = this.writeKeys[i2];
                if (j2 != -1) {
                    int i3 = this.offsets[i2];
                    switch (this.typeCodes[i2]) {
                        case 'B':
                            unsafe.putByte(obj, j2, bArr[i3]);
                            break;
                        case 'C':
                            unsafe.putChar(obj, j2, Bits.getChar(bArr, i3));
                            break;
                        case 'D':
                            unsafe.putDouble(obj, j2, Bits.getDouble(bArr, i3));
                            break;
                        case 'E':
                        case 'G':
                        case 'H':
                        case 'K':
                        case 'L':
                        case 'M':
                        case 'N':
                        case 'O':
                        case 'P':
                        case 'Q':
                        case 'R':
                        case 'T':
                        case 'U':
                        case 'V':
                        case 'W':
                        case 'X':
                        case 'Y':
                        default:
                            throw new InternalError();
                        case 'F':
                            unsafe.putFloat(obj, j2, Bits.getFloat(bArr, i3));
                            break;
                        case 'I':
                            unsafe.putInt(obj, j2, Bits.getInt(bArr, i3));
                            break;
                        case 'J':
                            unsafe.putLong(obj, j2, Bits.getLong(bArr, i3));
                            break;
                        case 'S':
                            unsafe.putShort(obj, j2, Bits.getShort(bArr, i3));
                            break;
                        case 'Z':
                            unsafe.putBoolean(obj, j2, Bits.getBoolean(bArr, i3));
                            break;
                    }
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:9:0x001a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void getObjFieldValues(java.lang.Object r8, java.lang.Object[] r9) {
            /*
                r7 = this;
                r0 = r8
                if (r0 != 0) goto Lc
                java.lang.NullPointerException r0 = new java.lang.NullPointerException
                r1 = r0
                r1.<init>()
                throw r0
            Lc:
                r0 = r7
                int r0 = r0.numPrimFields
                r10 = r0
            L11:
                r0 = r10
                r1 = r7
                java.io.ObjectStreamField[] r1 = r1.fields
                int r1 = r1.length
                if (r0 >= r1) goto L62
                r0 = r7
                char[] r0 = r0.typeCodes
                r1 = r10
                char r0 = r0[r1]
                switch(r0) {
                    case 76: goto L3c;
                    case 91: goto L3c;
                    default: goto L54;
                }
            L3c:
                r0 = r9
                r1 = r7
                int[] r1 = r1.offsets
                r2 = r10
                r1 = r1[r2]
                sun.misc.Unsafe r2 = java.io.ObjectStreamClass.FieldReflector.unsafe
                r3 = r8
                r4 = r7
                long[] r4 = r4.readKeys
                r5 = r10
                r4 = r4[r5]
                java.lang.Object r2 = r2.getObject(r3, r4)
                r0[r1] = r2
                goto L5c
            L54:
                java.lang.InternalError r0 = new java.lang.InternalError
                r1 = r0
                r1.<init>()
                throw r0
            L5c:
                int r10 = r10 + 1
                goto L11
            L62:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.io.ObjectStreamClass.FieldReflector.getObjFieldValues(java.lang.Object, java.lang.Object[]):void");
        }

        void setObjFieldValues(Object obj, Object[] objArr) {
            if (obj == null) {
                throw new NullPointerException();
            }
            for (int i2 = this.numPrimFields; i2 < this.fields.length; i2++) {
                long j2 = this.writeKeys[i2];
                if (j2 != -1) {
                    switch (this.typeCodes[i2]) {
                        case 'L':
                        case '[':
                            Object obj2 = objArr[this.offsets[i2]];
                            if (obj2 != null && !this.types[i2 - this.numPrimFields].isInstance(obj2)) {
                                Field field = this.fields[i2].getField();
                                throw new ClassCastException("cannot assign instance of " + obj2.getClass().getName() + " to field " + field.getDeclaringClass().getName() + "." + field.getName() + " of type " + field.getType().getName() + " in instance of " + obj.getClass().getName());
                            }
                            unsafe.putObject(obj, j2, obj2);
                            break;
                        default:
                            throw new InternalError();
                    }
                }
            }
        }
    }

    private static FieldReflector getReflector(ObjectStreamField[] objectStreamFieldArr, ObjectStreamClass objectStreamClass) throws InvalidClassException {
        Class<?> cls = (objectStreamClass == null || objectStreamFieldArr.length <= 0) ? null : objectStreamClass.cl;
        processQueue(Caches.reflectorsQueue, Caches.reflectors);
        FieldReflectorKey fieldReflectorKey = new FieldReflectorKey(cls, objectStreamFieldArr, Caches.reflectorsQueue);
        Reference<?> referencePutIfAbsent = Caches.reflectors.get(fieldReflectorKey);
        Object fieldReflector = null;
        if (referencePutIfAbsent != null) {
            fieldReflector = referencePutIfAbsent.get();
        }
        EntryFuture entryFuture = null;
        if (fieldReflector == null) {
            EntryFuture entryFuture2 = new EntryFuture();
            SoftReference softReference = new SoftReference(entryFuture2);
            do {
                if (referencePutIfAbsent != null) {
                    Caches.reflectors.remove(fieldReflectorKey, referencePutIfAbsent);
                }
                referencePutIfAbsent = Caches.reflectors.putIfAbsent(fieldReflectorKey, softReference);
                if (referencePutIfAbsent != null) {
                    fieldReflector = referencePutIfAbsent.get();
                }
                if (referencePutIfAbsent == null) {
                    break;
                }
            } while (fieldReflector == null);
            if (fieldReflector == null) {
                entryFuture = entryFuture2;
            }
        }
        if (fieldReflector instanceof FieldReflector) {
            return (FieldReflector) fieldReflector;
        }
        if (fieldReflector instanceof EntryFuture) {
            fieldReflector = ((EntryFuture) fieldReflector).get();
        } else if (fieldReflector == null) {
            try {
                fieldReflector = new FieldReflector(matchFields(objectStreamFieldArr, objectStreamClass));
            } catch (Throwable th) {
                fieldReflector = th;
            }
            entryFuture.set(fieldReflector);
            Caches.reflectors.put(fieldReflectorKey, new SoftReference(fieldReflector));
        }
        if (fieldReflector instanceof FieldReflector) {
            return (FieldReflector) fieldReflector;
        }
        if (fieldReflector instanceof InvalidClassException) {
            throw ((InvalidClassException) fieldReflector);
        }
        if (fieldReflector instanceof RuntimeException) {
            throw ((RuntimeException) fieldReflector);
        }
        if (fieldReflector instanceof Error) {
            throw ((Error) fieldReflector);
        }
        throw new InternalError("unexpected entry: " + fieldReflector);
    }

    /* loaded from: rt.jar:java/io/ObjectStreamClass$FieldReflectorKey.class */
    private static class FieldReflectorKey extends WeakReference<Class<?>> {
        private final String[] sigs;
        private final int hash;
        private final boolean nullClass;

        FieldReflectorKey(Class<?> cls, ObjectStreamField[] objectStreamFieldArr, ReferenceQueue<Class<?>> referenceQueue) {
            super(cls, referenceQueue);
            this.nullClass = cls == null;
            this.sigs = new String[2 * objectStreamFieldArr.length];
            int i2 = 0;
            for (ObjectStreamField objectStreamField : objectStreamFieldArr) {
                int i3 = i2;
                int i4 = i2 + 1;
                this.sigs[i3] = objectStreamField.getName();
                i2 = i4 + 1;
                this.sigs[i4] = objectStreamField.getSignature();
            }
            this.hash = System.identityHashCode(cls) + Arrays.hashCode(this.sigs);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            Class<?> cls;
            if (obj == this) {
                return true;
            }
            if (obj instanceof FieldReflectorKey) {
                FieldReflectorKey fieldReflectorKey = (FieldReflectorKey) obj;
                if (!this.nullClass ? !((cls = get()) == null || cls != fieldReflectorKey.get()) : fieldReflectorKey.nullClass) {
                    if (Arrays.equals(this.sigs, fieldReflectorKey.sigs)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
    }

    private static ObjectStreamField[] matchFields(ObjectStreamField[] objectStreamFieldArr, ObjectStreamClass objectStreamClass) throws InvalidClassException {
        ObjectStreamField[] objectStreamFieldArr2 = objectStreamClass != null ? objectStreamClass.fields : NO_FIELDS;
        ObjectStreamField[] objectStreamFieldArr3 = new ObjectStreamField[objectStreamFieldArr.length];
        for (int i2 = 0; i2 < objectStreamFieldArr.length; i2++) {
            ObjectStreamField objectStreamField = objectStreamFieldArr[i2];
            ObjectStreamField objectStreamField2 = null;
            for (ObjectStreamField objectStreamField3 : objectStreamFieldArr2) {
                if (objectStreamField.getName().equals(objectStreamField3.getName())) {
                    if ((objectStreamField.isPrimitive() || objectStreamField3.isPrimitive()) && objectStreamField.getTypeCode() != objectStreamField3.getTypeCode()) {
                        throw new InvalidClassException(objectStreamClass.name, "incompatible types for field " + objectStreamField.getName());
                    }
                    if (objectStreamField3.getField() != null) {
                        objectStreamField2 = new ObjectStreamField(objectStreamField3.getField(), objectStreamField3.isUnshared(), false);
                    } else {
                        objectStreamField2 = new ObjectStreamField(objectStreamField3.getName(), objectStreamField3.getSignature(), objectStreamField3.isUnshared());
                    }
                }
            }
            if (objectStreamField2 == null) {
                objectStreamField2 = new ObjectStreamField(objectStreamField.getName(), objectStreamField.getSignature(), false);
            }
            objectStreamField2.setOffset(objectStreamField.getOffset());
            objectStreamFieldArr3[i2] = objectStreamField2;
        }
        return objectStreamFieldArr3;
    }

    static void processQueue(ReferenceQueue<Class<?>> referenceQueue, ConcurrentMap<? extends WeakReference<Class<?>>, ?> concurrentMap) {
        while (true) {
            Reference<? extends Class<?>> referencePoll = referenceQueue.poll();
            if (referencePoll != null) {
                concurrentMap.remove(referencePoll);
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/io/ObjectStreamClass$WeakClassKey.class */
    static class WeakClassKey extends WeakReference<Class<?>> {
        private final int hash;

        WeakClassKey(Class<?> cls, ReferenceQueue<Class<?>> referenceQueue) {
            super(cls, referenceQueue);
            this.hash = System.identityHashCode(cls);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            Class<?> cls;
            if (obj == this) {
                return true;
            }
            return (obj instanceof WeakClassKey) && (cls = get()) != null && cls == ((WeakClassKey) obj).get();
        }
    }
}
