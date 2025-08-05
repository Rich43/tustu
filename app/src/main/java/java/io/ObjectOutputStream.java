package java.io;

import java.io.ObjectStreamClass;
import java.lang.ref.ReferenceQueue;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.reflect.misc.ReflectUtil;
import sun.security.action.GetBooleanAction;

/* loaded from: rt.jar:java/io/ObjectOutputStream.class */
public class ObjectOutputStream extends OutputStream implements ObjectOutput, ObjectStreamConstants {
    private final BlockDataOutputStream bout;
    private final HandleTable handles;
    private final ReplaceTable subs;
    private int protocol = 2;
    private int depth;
    private byte[] primVals;
    private final boolean enableOverride;
    private boolean enableReplace;
    private SerialCallbackContext curContext;
    private PutFieldImpl curPut;
    private final DebugTraceInfoStack debugInfoStack;
    private static final boolean extendedDebugInfo = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.io.serialization.extendedDebugInfo"))).booleanValue();

    /* loaded from: rt.jar:java/io/ObjectOutputStream$PutField.class */
    public static abstract class PutField {
        public abstract void put(String str, boolean z2);

        public abstract void put(String str, byte b2);

        public abstract void put(String str, char c2);

        public abstract void put(String str, short s2);

        public abstract void put(String str, int i2);

        public abstract void put(String str, long j2);

        public abstract void put(String str, float f2);

        public abstract void put(String str, double d2);

        public abstract void put(String str, Object obj);

        @Deprecated
        public abstract void write(ObjectOutput objectOutput) throws IOException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void floatsToBytes(float[] fArr, int i2, byte[] bArr, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void doublesToBytes(double[] dArr, int i2, byte[] bArr, int i3, int i4);

    /* loaded from: rt.jar:java/io/ObjectOutputStream$Caches.class */
    private static class Caches {
        static final ConcurrentMap<ObjectStreamClass.WeakClassKey, Boolean> subclassAudits = new ConcurrentHashMap();
        static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue<>();

        private Caches() {
        }
    }

    public ObjectOutputStream(OutputStream outputStream) throws IOException {
        verifySubclass();
        this.bout = new BlockDataOutputStream(outputStream);
        this.handles = new HandleTable(10, 3.0f);
        this.subs = new ReplaceTable(10, 3.0f);
        this.enableOverride = false;
        writeStreamHeader();
        this.bout.setBlockDataMode(true);
        if (extendedDebugInfo) {
            this.debugInfoStack = new DebugTraceInfoStack();
        } else {
            this.debugInfoStack = null;
        }
    }

    protected ObjectOutputStream() throws IOException, SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
        }
        this.bout = null;
        this.handles = null;
        this.subs = null;
        this.enableOverride = true;
        this.debugInfoStack = null;
    }

    public void useProtocolVersion(int i2) throws IOException {
        if (this.handles.size() != 0) {
            throw new IllegalStateException("stream non-empty");
        }
        switch (i2) {
            case 1:
            case 2:
                this.protocol = i2;
                return;
            default:
                throw new IllegalArgumentException("unknown version: " + i2);
        }
    }

    @Override // java.io.ObjectOutput
    public final void writeObject(Object obj) throws IOException {
        if (this.enableOverride) {
            writeObjectOverride(obj);
            return;
        }
        try {
            writeObject0(obj, false);
        } catch (IOException e2) {
            if (this.depth == 0) {
                writeFatalException(e2);
            }
            throw e2;
        }
    }

    protected void writeObjectOverride(Object obj) throws IOException {
    }

    public void writeUnshared(Object obj) throws IOException {
        try {
            writeObject0(obj, true);
        } catch (IOException e2) {
            if (this.depth == 0) {
                writeFatalException(e2);
            }
            throw e2;
        }
    }

    public void defaultWriteObject() throws IOException {
        SerialCallbackContext serialCallbackContext = this.curContext;
        if (serialCallbackContext == null) {
            throw new NotActiveException("not in call to writeObject");
        }
        Object obj = serialCallbackContext.getObj();
        ObjectStreamClass desc = serialCallbackContext.getDesc();
        this.bout.setBlockDataMode(false);
        defaultWriteFields(obj, desc);
        this.bout.setBlockDataMode(true);
    }

    public PutField putFields() throws IOException {
        if (this.curPut == null) {
            SerialCallbackContext serialCallbackContext = this.curContext;
            if (serialCallbackContext == null) {
                throw new NotActiveException("not in call to writeObject");
            }
            serialCallbackContext.getObj();
            this.curPut = new PutFieldImpl(serialCallbackContext.getDesc());
        }
        return this.curPut;
    }

    public void writeFields() throws IOException {
        if (this.curPut == null) {
            throw new NotActiveException("no current PutField object");
        }
        this.bout.setBlockDataMode(false);
        this.curPut.writeFields();
        this.bout.setBlockDataMode(true);
    }

    public void reset() throws IOException {
        if (this.depth != 0) {
            throw new IOException("stream active");
        }
        this.bout.setBlockDataMode(false);
        this.bout.writeByte(121);
        clear();
        this.bout.setBlockDataMode(true);
    }

    protected void annotateClass(Class<?> cls) throws IOException {
    }

    protected void annotateProxyClass(Class<?> cls) throws IOException {
    }

    protected Object replaceObject(Object obj) throws IOException {
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean enableReplaceObject(boolean z2) throws SecurityException {
        SecurityManager securityManager;
        if (z2 == this.enableReplace) {
            return z2;
        }
        if (z2 && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPermission(SUBSTITUTION_PERMISSION);
        }
        this.enableReplace = z2;
        return !this.enableReplace;
    }

    protected void writeStreamHeader() throws IOException {
        this.bout.writeShort(ObjectStreamConstants.STREAM_MAGIC);
        this.bout.writeShort(5);
    }

    protected void writeClassDescriptor(ObjectStreamClass objectStreamClass) throws IOException {
        objectStreamClass.writeNonProxy(this);
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        this.bout.write(i2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        this.bout.write(bArr, 0, bArr.length, false);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (bArr == null) {
            throw new NullPointerException();
        }
        int i4 = i2 + i3;
        if (i2 < 0 || i3 < 0 || i4 > bArr.length || i4 < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.bout.write(bArr, i2, i3, false);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.bout.flush();
    }

    protected void drain() throws IOException {
        this.bout.drain();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        clear();
        this.bout.close();
    }

    public void writeBoolean(boolean z2) throws IOException {
        this.bout.writeBoolean(z2);
    }

    public void writeByte(int i2) throws IOException {
        this.bout.writeByte(i2);
    }

    public void writeShort(int i2) throws IOException {
        this.bout.writeShort(i2);
    }

    public void writeChar(int i2) throws IOException {
        this.bout.writeChar(i2);
    }

    public void writeInt(int i2) throws IOException {
        this.bout.writeInt(i2);
    }

    public void writeLong(long j2) throws IOException {
        this.bout.writeLong(j2);
    }

    public void writeFloat(float f2) throws IOException {
        this.bout.writeFloat(f2);
    }

    public void writeDouble(double d2) throws IOException {
        this.bout.writeDouble(d2);
    }

    public void writeBytes(String str) throws IOException {
        this.bout.writeBytes(str);
    }

    public void writeChars(String str) throws IOException {
        this.bout.writeChars(str);
    }

    public void writeUTF(String str) throws IOException {
        this.bout.writeUTF(str);
    }

    int getProtocolVersion() {
        return this.protocol;
    }

    void writeTypeString(String str) throws IOException {
        if (str == null) {
            writeNull();
            return;
        }
        int iLookup = this.handles.lookup(str);
        if (iLookup != -1) {
            writeHandle(iLookup);
        } else {
            writeString(str, false);
        }
    }

    private void verifySubclass() {
        SecurityManager securityManager;
        Class<?> cls = getClass();
        if (cls == ObjectOutputStream.class || (securityManager = System.getSecurityManager()) == null) {
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
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.io.ObjectOutputStream.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() throws SecurityException {
                Class superclass = cls;
                while (true) {
                    Class cls2 = superclass;
                    if (cls2 != ObjectOutputStream.class) {
                        try {
                            cls2.getDeclaredMethod("writeUnshared", Object.class);
                            return Boolean.FALSE;
                        } catch (NoSuchMethodException e2) {
                            try {
                                cls2.getDeclaredMethod("putFields", (Class[]) null);
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
        this.subs.clear();
        this.handles.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeObject0(Object obj, boolean z2) throws IOException {
        ObjectStreamClass objectStreamClassLookup;
        int iLookup;
        Class<?> cls;
        int iLookup2;
        boolean blockDataMode = this.bout.setBlockDataMode(false);
        this.depth++;
        try {
            Object objLookup = this.subs.lookup(obj);
            Object obj2 = objLookup;
            if (objLookup == null) {
                writeNull();
                this.depth--;
                this.bout.setBlockDataMode(blockDataMode);
                return;
            }
            if (!z2 && (iLookup2 = this.handles.lookup(obj2)) != -1) {
                writeHandle(iLookup2);
                this.depth--;
                this.bout.setBlockDataMode(blockDataMode);
                return;
            }
            if (obj2 instanceof Class) {
                writeClass((Class) obj2, z2);
                this.depth--;
                this.bout.setBlockDataMode(blockDataMode);
                return;
            }
            if (obj2 instanceof ObjectStreamClass) {
                writeClassDesc((ObjectStreamClass) obj2, z2);
                this.depth--;
                this.bout.setBlockDataMode(blockDataMode);
                return;
            }
            Class<?> cls2 = obj2.getClass();
            while (true) {
                objectStreamClassLookup = ObjectStreamClass.lookup(cls2, true);
                if (!objectStreamClassLookup.hasWriteReplaceMethod()) {
                    break;
                }
                Object objInvokeWriteReplace = objectStreamClassLookup.invokeWriteReplace(obj2);
                obj2 = objInvokeWriteReplace;
                if (objInvokeWriteReplace == null || (cls = obj2.getClass()) == cls2) {
                    break;
                } else {
                    cls2 = cls;
                }
            }
            if (this.enableReplace) {
                Object objReplaceObject = replaceObject(obj2);
                if (objReplaceObject != obj2 && objReplaceObject != null) {
                    cls2 = objReplaceObject.getClass();
                    objectStreamClassLookup = ObjectStreamClass.lookup(cls2, true);
                }
                obj2 = objReplaceObject;
            }
            if (obj2 != obj2) {
                this.subs.assign(obj2, obj2);
                if (obj2 == null) {
                    writeNull();
                    this.depth--;
                    this.bout.setBlockDataMode(blockDataMode);
                    return;
                }
                if (!z2 && (iLookup = this.handles.lookup(obj2)) != -1) {
                    writeHandle(iLookup);
                    this.depth--;
                    this.bout.setBlockDataMode(blockDataMode);
                    return;
                } else if (obj2 instanceof Class) {
                    writeClass(obj2, z2);
                    this.depth--;
                    this.bout.setBlockDataMode(blockDataMode);
                    return;
                } else if (obj2 instanceof ObjectStreamClass) {
                    writeClassDesc(obj2, z2);
                    this.depth--;
                    this.bout.setBlockDataMode(blockDataMode);
                    return;
                }
            }
            if (obj2 instanceof String) {
                writeString(obj2, z2);
            } else if (cls2.isArray()) {
                writeArray(obj2, objectStreamClassLookup, z2);
            } else if (obj2 instanceof Enum) {
                writeEnum(obj2, objectStreamClassLookup, z2);
            } else {
                if (!(obj2 instanceof Serializable)) {
                    if (!extendedDebugInfo) {
                        throw new NotSerializableException(cls2.getName());
                    }
                    throw new NotSerializableException(cls2.getName() + "\n" + this.debugInfoStack.toString());
                }
                writeOrdinaryObject(obj2, objectStreamClassLookup, z2);
            }
        } finally {
            this.depth--;
            this.bout.setBlockDataMode(blockDataMode);
        }
    }

    private void writeNull() throws IOException {
        this.bout.writeByte(112);
    }

    private void writeHandle(int i2) throws IOException {
        this.bout.writeByte(113);
        this.bout.writeInt(ObjectStreamConstants.baseWireHandle + i2);
    }

    private void writeClass(Class<?> cls, boolean z2) throws IOException {
        this.bout.writeByte(118);
        writeClassDesc(ObjectStreamClass.lookup(cls, true), false);
        this.handles.assign(z2 ? null : cls);
    }

    private void writeClassDesc(ObjectStreamClass objectStreamClass, boolean z2) throws IOException {
        int iLookup;
        if (objectStreamClass == null) {
            writeNull();
            return;
        }
        if (!z2 && (iLookup = this.handles.lookup(objectStreamClass)) != -1) {
            writeHandle(iLookup);
        } else if (objectStreamClass.isProxy()) {
            writeProxyDesc(objectStreamClass, z2);
        } else {
            writeNonProxyDesc(objectStreamClass, z2);
        }
    }

    private boolean isCustomSubclass() {
        return getClass().getClassLoader() != ObjectOutputStream.class.getClassLoader();
    }

    private void writeProxyDesc(ObjectStreamClass objectStreamClass, boolean z2) throws IOException {
        this.bout.writeByte(125);
        this.handles.assign(z2 ? null : objectStreamClass);
        Class<?> clsForClass = objectStreamClass.forClass();
        Class<?>[] interfaces = clsForClass.getInterfaces();
        this.bout.writeInt(interfaces.length);
        for (Class<?> cls : interfaces) {
            this.bout.writeUTF(cls.getName());
        }
        this.bout.setBlockDataMode(true);
        if (clsForClass != null && isCustomSubclass()) {
            ReflectUtil.checkPackageAccess(clsForClass);
        }
        annotateProxyClass(clsForClass);
        this.bout.setBlockDataMode(false);
        this.bout.writeByte(120);
        writeClassDesc(objectStreamClass.getSuperDesc(), false);
    }

    private void writeNonProxyDesc(ObjectStreamClass objectStreamClass, boolean z2) throws IOException {
        this.bout.writeByte(114);
        this.handles.assign(z2 ? null : objectStreamClass);
        if (this.protocol == 1) {
            objectStreamClass.writeNonProxy(this);
        } else {
            writeClassDescriptor(objectStreamClass);
        }
        Class<?> clsForClass = objectStreamClass.forClass();
        this.bout.setBlockDataMode(true);
        if (clsForClass != null && isCustomSubclass()) {
            ReflectUtil.checkPackageAccess(clsForClass);
        }
        annotateClass(clsForClass);
        this.bout.setBlockDataMode(false);
        this.bout.writeByte(120);
        writeClassDesc(objectStreamClass.getSuperDesc(), false);
    }

    private void writeString(String str, boolean z2) throws IOException {
        this.handles.assign(z2 ? null : str);
        long uTFLength = this.bout.getUTFLength(str);
        if (uTFLength <= 65535) {
            this.bout.writeByte(116);
            this.bout.writeUTF(str, uTFLength);
        } else {
            this.bout.writeByte(124);
            this.bout.writeLongUTF(str, uTFLength);
        }
    }

    private void writeArray(Object obj, ObjectStreamClass objectStreamClass, boolean z2) throws IOException {
        this.bout.writeByte(117);
        writeClassDesc(objectStreamClass, false);
        this.handles.assign(z2 ? null : obj);
        Class<?> componentType = objectStreamClass.forClass().getComponentType();
        if (componentType.isPrimitive()) {
            if (componentType == Integer.TYPE) {
                int[] iArr = (int[]) obj;
                this.bout.writeInt(iArr.length);
                this.bout.writeInts(iArr, 0, iArr.length);
                return;
            }
            if (componentType == Byte.TYPE) {
                byte[] bArr = (byte[]) obj;
                this.bout.writeInt(bArr.length);
                this.bout.write(bArr, 0, bArr.length, true);
                return;
            }
            if (componentType == Long.TYPE) {
                long[] jArr = (long[]) obj;
                this.bout.writeInt(jArr.length);
                this.bout.writeLongs(jArr, 0, jArr.length);
                return;
            }
            if (componentType == Float.TYPE) {
                float[] fArr = (float[]) obj;
                this.bout.writeInt(fArr.length);
                this.bout.writeFloats(fArr, 0, fArr.length);
                return;
            }
            if (componentType == Double.TYPE) {
                double[] dArr = (double[]) obj;
                this.bout.writeInt(dArr.length);
                this.bout.writeDoubles(dArr, 0, dArr.length);
                return;
            }
            if (componentType == Short.TYPE) {
                short[] sArr = (short[]) obj;
                this.bout.writeInt(sArr.length);
                this.bout.writeShorts(sArr, 0, sArr.length);
                return;
            } else if (componentType == Character.TYPE) {
                char[] cArr = (char[]) obj;
                this.bout.writeInt(cArr.length);
                this.bout.writeChars(cArr, 0, cArr.length);
                return;
            } else {
                if (componentType == Boolean.TYPE) {
                    boolean[] zArr = (boolean[]) obj;
                    this.bout.writeInt(zArr.length);
                    this.bout.writeBooleans(zArr, 0, zArr.length);
                    return;
                }
                throw new InternalError();
            }
        }
        Object[] objArr = (Object[]) obj;
        int length = objArr.length;
        this.bout.writeInt(length);
        if (extendedDebugInfo) {
            this.debugInfoStack.push("array (class \"" + obj.getClass().getName() + "\", size: " + length + ")");
        }
        for (int i2 = 0; i2 < length; i2++) {
            try {
                if (extendedDebugInfo) {
                    this.debugInfoStack.push("element of array (index: " + i2 + ")");
                }
                try {
                    writeObject0(objArr[i2], false);
                    if (extendedDebugInfo) {
                        this.debugInfoStack.pop();
                    }
                } finally {
                    if (extendedDebugInfo) {
                        this.debugInfoStack.pop();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        if (extendedDebugInfo) {
            this.debugInfoStack.pop();
        }
    }

    private void writeEnum(Enum<?> r5, ObjectStreamClass objectStreamClass, boolean z2) throws IOException {
        this.bout.writeByte(126);
        ObjectStreamClass superDesc = objectStreamClass.getSuperDesc();
        writeClassDesc(superDesc.forClass() == Enum.class ? objectStreamClass : superDesc, false);
        this.handles.assign(z2 ? null : r5);
        writeString(r5.name(), false);
    }

    private void writeOrdinaryObject(Object obj, ObjectStreamClass objectStreamClass, boolean z2) throws IOException {
        if (extendedDebugInfo) {
            this.debugInfoStack.push((this.depth == 1 ? "root " : "") + "object (class \"" + obj.getClass().getName() + "\", " + obj.toString() + ")");
        }
        try {
            objectStreamClass.checkSerialize();
            this.bout.writeByte(115);
            writeClassDesc(objectStreamClass, false);
            this.handles.assign(z2 ? null : obj);
            if (objectStreamClass.isExternalizable() && !objectStreamClass.isProxy()) {
                writeExternalData((Externalizable) obj);
            } else {
                writeSerialData(obj, objectStreamClass);
            }
            if (extendedDebugInfo) {
                this.debugInfoStack.pop();
            }
        } catch (Throwable th) {
            if (extendedDebugInfo) {
                this.debugInfoStack.pop();
            }
            throw th;
        }
    }

    private void writeExternalData(Externalizable externalizable) throws IOException {
        PutFieldImpl putFieldImpl = this.curPut;
        this.curPut = null;
        if (extendedDebugInfo) {
            this.debugInfoStack.push("writeExternal data");
        }
        SerialCallbackContext serialCallbackContext = this.curContext;
        try {
            this.curContext = null;
            if (this.protocol == 1) {
                externalizable.writeExternal(this);
            } else {
                this.bout.setBlockDataMode(true);
                externalizable.writeExternal(this);
                this.bout.setBlockDataMode(false);
                this.bout.writeByte(120);
            }
            this.curPut = putFieldImpl;
        } finally {
            this.curContext = serialCallbackContext;
            if (extendedDebugInfo) {
                this.debugInfoStack.pop();
            }
        }
    }

    private void writeSerialData(Object obj, ObjectStreamClass objectStreamClass) throws IOException {
        for (ObjectStreamClass.ClassDataSlot classDataSlot : objectStreamClass.getClassDataLayout()) {
            ObjectStreamClass objectStreamClass2 = classDataSlot.desc;
            if (objectStreamClass2.hasWriteObjectMethod()) {
                PutFieldImpl putFieldImpl = this.curPut;
                this.curPut = null;
                SerialCallbackContext serialCallbackContext = this.curContext;
                if (extendedDebugInfo) {
                    this.debugInfoStack.push("custom writeObject data (class \"" + objectStreamClass2.getName() + "\")");
                }
                try {
                    this.curContext = new SerialCallbackContext(obj, objectStreamClass2);
                    this.bout.setBlockDataMode(true);
                    objectStreamClass2.invokeWriteObject(obj, this);
                    this.bout.setBlockDataMode(false);
                    this.bout.writeByte(120);
                    this.curContext.setUsed();
                    this.curContext = serialCallbackContext;
                    if (extendedDebugInfo) {
                        this.debugInfoStack.pop();
                    }
                    this.curPut = putFieldImpl;
                } catch (Throwable th) {
                    this.curContext.setUsed();
                    this.curContext = serialCallbackContext;
                    if (extendedDebugInfo) {
                        this.debugInfoStack.pop();
                    }
                    throw th;
                }
            } else {
                defaultWriteFields(obj, objectStreamClass2);
            }
        }
    }

    private void defaultWriteFields(Object obj, ObjectStreamClass objectStreamClass) throws IOException {
        Class<?> clsForClass = objectStreamClass.forClass();
        if (clsForClass != null && obj != null && !clsForClass.isInstance(obj)) {
            throw new ClassCastException();
        }
        objectStreamClass.checkDefaultSerialize();
        int primDataSize = objectStreamClass.getPrimDataSize();
        if (this.primVals == null || this.primVals.length < primDataSize) {
            this.primVals = new byte[primDataSize];
        }
        objectStreamClass.getPrimFieldValues(obj, this.primVals);
        this.bout.write(this.primVals, 0, primDataSize, false);
        ObjectStreamField[] fields = objectStreamClass.getFields(false);
        Object[] objArr = new Object[objectStreamClass.getNumObjFields()];
        int length = fields.length - objArr.length;
        objectStreamClass.getObjFieldValues(obj, objArr);
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (extendedDebugInfo) {
                this.debugInfoStack.push("field (class \"" + objectStreamClass.getName() + "\", name: \"" + fields[length + i2].getName() + "\", type: \"" + ((Object) fields[length + i2].getType()) + "\")");
            }
            try {
                writeObject0(objArr[i2], fields[length + i2].isUnshared());
                if (extendedDebugInfo) {
                    this.debugInfoStack.pop();
                }
            } catch (Throwable th) {
                if (extendedDebugInfo) {
                    this.debugInfoStack.pop();
                }
                throw th;
            }
        }
    }

    private void writeFatalException(IOException iOException) throws IOException {
        clear();
        boolean blockDataMode = this.bout.setBlockDataMode(false);
        try {
            this.bout.writeByte(123);
            writeObject0(iOException, false);
            clear();
        } finally {
            this.bout.setBlockDataMode(blockDataMode);
        }
    }

    /* loaded from: rt.jar:java/io/ObjectOutputStream$PutFieldImpl.class */
    private class PutFieldImpl extends PutField {
        private final ObjectStreamClass desc;
        private final byte[] primVals;
        private final Object[] objVals;

        PutFieldImpl(ObjectStreamClass objectStreamClass) {
            this.desc = objectStreamClass;
            this.primVals = new byte[objectStreamClass.getPrimDataSize()];
            this.objVals = new Object[objectStreamClass.getNumObjFields()];
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, boolean z2) {
            Bits.putBoolean(this.primVals, getFieldOffset(str, Boolean.TYPE), z2);
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, byte b2) {
            this.primVals[getFieldOffset(str, Byte.TYPE)] = b2;
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, char c2) {
            Bits.putChar(this.primVals, getFieldOffset(str, Character.TYPE), c2);
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, short s2) {
            Bits.putShort(this.primVals, getFieldOffset(str, Short.TYPE), s2);
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, int i2) {
            Bits.putInt(this.primVals, getFieldOffset(str, Integer.TYPE), i2);
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, float f2) {
            Bits.putFloat(this.primVals, getFieldOffset(str, Float.TYPE), f2);
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, long j2) {
            Bits.putLong(this.primVals, getFieldOffset(str, Long.TYPE), j2);
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, double d2) {
            Bits.putDouble(this.primVals, getFieldOffset(str, Double.TYPE), d2);
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void put(String str, Object obj) {
            this.objVals[getFieldOffset(str, Object.class)] = obj;
        }

        @Override // java.io.ObjectOutputStream.PutField
        public void write(ObjectOutput objectOutput) throws IOException {
            if (ObjectOutputStream.this != objectOutput) {
                throw new IllegalArgumentException("wrong stream");
            }
            objectOutput.write(this.primVals, 0, this.primVals.length);
            ObjectStreamField[] fields = this.desc.getFields(false);
            int length = fields.length - this.objVals.length;
            for (int i2 = 0; i2 < this.objVals.length; i2++) {
                if (fields[length + i2].isUnshared()) {
                    throw new IOException("cannot write unshared object");
                }
                objectOutput.writeObject(this.objVals[i2]);
            }
        }

        void writeFields() throws IOException {
            ObjectOutputStream.this.bout.write(this.primVals, 0, this.primVals.length, false);
            ObjectStreamField[] fields = this.desc.getFields(false);
            int length = fields.length - this.objVals.length;
            for (int i2 = 0; i2 < this.objVals.length; i2++) {
                if (ObjectOutputStream.extendedDebugInfo) {
                    ObjectOutputStream.this.debugInfoStack.push("field (class \"" + this.desc.getName() + "\", name: \"" + fields[length + i2].getName() + "\", type: \"" + ((Object) fields[length + i2].getType()) + "\")");
                }
                try {
                    ObjectOutputStream.this.writeObject0(this.objVals[i2], fields[length + i2].isUnshared());
                    if (ObjectOutputStream.extendedDebugInfo) {
                        ObjectOutputStream.this.debugInfoStack.pop();
                    }
                } catch (Throwable th) {
                    if (ObjectOutputStream.extendedDebugInfo) {
                        ObjectOutputStream.this.debugInfoStack.pop();
                    }
                    throw th;
                }
            }
        }

        private int getFieldOffset(String str, Class<?> cls) {
            ObjectStreamField field = this.desc.getField(str, cls);
            if (field == null) {
                throw new IllegalArgumentException("no such field " + str + " with type " + ((Object) cls));
            }
            return field.getOffset();
        }
    }

    /* loaded from: rt.jar:java/io/ObjectOutputStream$BlockDataOutputStream.class */
    private static class BlockDataOutputStream extends OutputStream implements DataOutput {
        private static final int MAX_BLOCK_SIZE = 1024;
        private static final int MAX_HEADER_SIZE = 5;
        private static final int CHAR_BUF_SIZE = 256;
        private final OutputStream out;
        private final byte[] buf = new byte[1024];
        private final byte[] hbuf = new byte[5];
        private final char[] cbuf = new char[256];
        private boolean blkmode = false;
        private int pos = 0;
        private final DataOutputStream dout = new DataOutputStream(this);

        BlockDataOutputStream(OutputStream outputStream) {
            this.out = outputStream;
        }

        boolean setBlockDataMode(boolean z2) throws IOException {
            if (this.blkmode == z2) {
                return this.blkmode;
            }
            drain();
            this.blkmode = z2;
            return !this.blkmode;
        }

        boolean getBlockDataMode() {
            return this.blkmode;
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            if (this.pos >= 1024) {
                drain();
            }
            byte[] bArr = this.buf;
            int i3 = this.pos;
            this.pos = i3 + 1;
            bArr[i3] = (byte) i2;
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length, false);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            write(bArr, i2, i3, false);
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
            drain();
            this.out.flush();
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            flush();
            this.out.close();
        }

        void write(byte[] bArr, int i2, int i3, boolean z2) throws IOException {
            if (!z2 && !this.blkmode) {
                drain();
                this.out.write(bArr, i2, i3);
                return;
            }
            while (i3 > 0) {
                if (this.pos >= 1024) {
                    drain();
                }
                if (i3 >= 1024 && !z2 && this.pos == 0) {
                    writeBlockHeader(1024);
                    this.out.write(bArr, i2, 1024);
                    i2 += 1024;
                    i3 -= 1024;
                } else {
                    int iMin = Math.min(i3, 1024 - this.pos);
                    System.arraycopy(bArr, i2, this.buf, this.pos, iMin);
                    this.pos += iMin;
                    i2 += iMin;
                    i3 -= iMin;
                }
            }
        }

        void drain() throws IOException {
            if (this.pos == 0) {
                return;
            }
            if (this.blkmode) {
                writeBlockHeader(this.pos);
            }
            this.out.write(this.buf, 0, this.pos);
            this.pos = 0;
        }

        private void writeBlockHeader(int i2) throws IOException {
            if (i2 <= 255) {
                this.hbuf[0] = 119;
                this.hbuf[1] = (byte) i2;
                this.out.write(this.hbuf, 0, 2);
            } else {
                this.hbuf[0] = 122;
                Bits.putInt(this.hbuf, 1, i2);
                this.out.write(this.hbuf, 0, 5);
            }
        }

        @Override // java.io.DataOutput
        public void writeBoolean(boolean z2) throws IOException {
            if (this.pos >= 1024) {
                drain();
            }
            byte[] bArr = this.buf;
            int i2 = this.pos;
            this.pos = i2 + 1;
            Bits.putBoolean(bArr, i2, z2);
        }

        @Override // java.io.DataOutput
        public void writeByte(int i2) throws IOException {
            if (this.pos >= 1024) {
                drain();
            }
            byte[] bArr = this.buf;
            int i3 = this.pos;
            this.pos = i3 + 1;
            bArr[i3] = (byte) i2;
        }

        @Override // java.io.DataOutput
        public void writeChar(int i2) throws IOException {
            if (this.pos + 2 <= 1024) {
                Bits.putChar(this.buf, this.pos, (char) i2);
                this.pos += 2;
            } else {
                this.dout.writeChar(i2);
            }
        }

        @Override // java.io.DataOutput
        public void writeShort(int i2) throws IOException {
            if (this.pos + 2 <= 1024) {
                Bits.putShort(this.buf, this.pos, (short) i2);
                this.pos += 2;
            } else {
                this.dout.writeShort(i2);
            }
        }

        @Override // java.io.DataOutput
        public void writeInt(int i2) throws IOException {
            if (this.pos + 4 <= 1024) {
                Bits.putInt(this.buf, this.pos, i2);
                this.pos += 4;
            } else {
                this.dout.writeInt(i2);
            }
        }

        @Override // java.io.DataOutput
        public void writeFloat(float f2) throws IOException {
            if (this.pos + 4 <= 1024) {
                Bits.putFloat(this.buf, this.pos, f2);
                this.pos += 4;
            } else {
                this.dout.writeFloat(f2);
            }
        }

        @Override // java.io.DataOutput
        public void writeLong(long j2) throws IOException {
            if (this.pos + 8 <= 1024) {
                Bits.putLong(this.buf, this.pos, j2);
                this.pos += 8;
            } else {
                this.dout.writeLong(j2);
            }
        }

        @Override // java.io.DataOutput
        public void writeDouble(double d2) throws IOException {
            if (this.pos + 8 <= 1024) {
                Bits.putDouble(this.buf, this.pos, d2);
                this.pos += 8;
            } else {
                this.dout.writeDouble(d2);
            }
        }

        @Override // java.io.DataOutput
        public void writeBytes(String str) throws IOException {
            int length = str.length();
            int i2 = 0;
            int iMin = 0;
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 < length) {
                    if (i2 >= iMin) {
                        i2 = 0;
                        iMin = Math.min(length - i4, 256);
                        str.getChars(i4, i4 + iMin, this.cbuf, 0);
                    }
                    if (this.pos >= 1024) {
                        drain();
                    }
                    int iMin2 = Math.min(iMin - i2, 1024 - this.pos);
                    int i5 = this.pos + iMin2;
                    while (this.pos < i5) {
                        byte[] bArr = this.buf;
                        int i6 = this.pos;
                        this.pos = i6 + 1;
                        int i7 = i2;
                        i2++;
                        bArr[i6] = (byte) this.cbuf[i7];
                    }
                    i3 = i4 + iMin2;
                } else {
                    return;
                }
            }
        }

        @Override // java.io.DataOutput
        public void writeChars(String str) throws IOException {
            int length = str.length();
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < length) {
                    int iMin = Math.min(length - i3, 256);
                    str.getChars(i3, i3 + iMin, this.cbuf, 0);
                    writeChars(this.cbuf, 0, iMin);
                    i2 = i3 + iMin;
                } else {
                    return;
                }
            }
        }

        @Override // java.io.DataOutput
        public void writeUTF(String str) throws IOException {
            writeUTF(str, getUTFLength(str));
        }

        void writeBooleans(boolean[] zArr, int i2, int i3) throws IOException {
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (this.pos >= 1024) {
                    drain();
                }
                int iMin = Math.min(i4, i2 + (1024 - this.pos));
                while (i2 < iMin) {
                    byte[] bArr = this.buf;
                    int i5 = this.pos;
                    this.pos = i5 + 1;
                    int i6 = i2;
                    i2++;
                    Bits.putBoolean(bArr, i5, zArr[i6]);
                }
            }
        }

        void writeChars(char[] cArr, int i2, int i3) throws IOException {
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (this.pos <= 1022) {
                    int iMin = Math.min(i4, i2 + ((1024 - this.pos) >> 1));
                    while (i2 < iMin) {
                        int i5 = i2;
                        i2++;
                        Bits.putChar(this.buf, this.pos, cArr[i5]);
                        this.pos += 2;
                    }
                } else {
                    int i6 = i2;
                    i2++;
                    this.dout.writeChar(cArr[i6]);
                }
            }
        }

        void writeShorts(short[] sArr, int i2, int i3) throws IOException {
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (this.pos <= 1022) {
                    int iMin = Math.min(i4, i2 + ((1024 - this.pos) >> 1));
                    while (i2 < iMin) {
                        int i5 = i2;
                        i2++;
                        Bits.putShort(this.buf, this.pos, sArr[i5]);
                        this.pos += 2;
                    }
                } else {
                    int i6 = i2;
                    i2++;
                    this.dout.writeShort(sArr[i6]);
                }
            }
        }

        void writeInts(int[] iArr, int i2, int i3) throws IOException {
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (this.pos <= 1020) {
                    int iMin = Math.min(i4, i2 + ((1024 - this.pos) >> 2));
                    while (i2 < iMin) {
                        int i5 = i2;
                        i2++;
                        Bits.putInt(this.buf, this.pos, iArr[i5]);
                        this.pos += 4;
                    }
                } else {
                    int i6 = i2;
                    i2++;
                    this.dout.writeInt(iArr[i6]);
                }
            }
        }

        void writeFloats(float[] fArr, int i2, int i3) throws IOException {
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (this.pos <= 1020) {
                    int iMin = Math.min(i4 - i2, (1024 - this.pos) >> 2);
                    ObjectOutputStream.floatsToBytes(fArr, i2, this.buf, this.pos, iMin);
                    i2 += iMin;
                    this.pos += iMin << 2;
                } else {
                    int i5 = i2;
                    i2++;
                    this.dout.writeFloat(fArr[i5]);
                }
            }
        }

        void writeLongs(long[] jArr, int i2, int i3) throws IOException {
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (this.pos <= 1016) {
                    int iMin = Math.min(i4, i2 + ((1024 - this.pos) >> 3));
                    while (i2 < iMin) {
                        int i5 = i2;
                        i2++;
                        Bits.putLong(this.buf, this.pos, jArr[i5]);
                        this.pos += 8;
                    }
                } else {
                    int i6 = i2;
                    i2++;
                    this.dout.writeLong(jArr[i6]);
                }
            }
        }

        void writeDoubles(double[] dArr, int i2, int i3) throws IOException {
            int i4 = i2 + i3;
            while (i2 < i4) {
                if (this.pos <= 1016) {
                    int iMin = Math.min(i4 - i2, (1024 - this.pos) >> 3);
                    ObjectOutputStream.doublesToBytes(dArr, i2, this.buf, this.pos, iMin);
                    i2 += iMin;
                    this.pos += iMin << 3;
                } else {
                    int i5 = i2;
                    i2++;
                    this.dout.writeDouble(dArr[i5]);
                }
            }
        }

        long getUTFLength(String str) {
            long j2;
            long j3;
            int length = str.length();
            long j4 = 0;
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < length) {
                    int iMin = Math.min(length - i3, 256);
                    str.getChars(i3, i3 + iMin, this.cbuf, 0);
                    for (int i4 = 0; i4 < iMin; i4++) {
                        char c2 = this.cbuf[i4];
                        if (c2 >= 1 && c2 <= 127) {
                            j2 = j4;
                            j3 = 1;
                        } else if (c2 > 2047) {
                            j2 = j4;
                            j3 = 3;
                        } else {
                            j2 = j4;
                            j3 = 2;
                        }
                        j4 = j2 + j3;
                    }
                    i2 = i3 + iMin;
                } else {
                    return j4;
                }
            }
        }

        void writeUTF(String str, long j2) throws IOException {
            if (j2 > 65535) {
                throw new UTFDataFormatException();
            }
            writeShort((int) j2);
            if (j2 == str.length()) {
                writeBytes(str);
            } else {
                writeUTFBody(str);
            }
        }

        void writeLongUTF(String str) throws IOException {
            writeLongUTF(str, getUTFLength(str));
        }

        void writeLongUTF(String str, long j2) throws IOException {
            writeLong(j2);
            if (j2 == str.length()) {
                writeBytes(str);
            } else {
                writeUTFBody(str);
            }
        }

        private void writeUTFBody(String str) throws IOException {
            int length = str.length();
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < length) {
                    int iMin = Math.min(length - i3, 256);
                    str.getChars(i3, i3 + iMin, this.cbuf, 0);
                    for (int i4 = 0; i4 < iMin; i4++) {
                        char c2 = this.cbuf[i4];
                        if (this.pos <= 1021) {
                            if (c2 <= 127 && c2 != 0) {
                                byte[] bArr = this.buf;
                                int i5 = this.pos;
                                this.pos = i5 + 1;
                                bArr[i5] = (byte) c2;
                            } else if (c2 > 2047) {
                                this.buf[this.pos + 2] = (byte) (128 | ((c2 >> 0) & 63));
                                this.buf[this.pos + 1] = (byte) (128 | ((c2 >> 6) & 63));
                                this.buf[this.pos + 0] = (byte) (224 | ((c2 >> '\f') & 15));
                                this.pos += 3;
                            } else {
                                this.buf[this.pos + 1] = (byte) (128 | ((c2 >> 0) & 63));
                                this.buf[this.pos + 0] = (byte) (192 | ((c2 >> 6) & 31));
                                this.pos += 2;
                            }
                        } else if (c2 <= 127 && c2 != 0) {
                            write(c2);
                        } else if (c2 > 2047) {
                            write(224 | ((c2 >> '\f') & 15));
                            write(128 | ((c2 >> 6) & 63));
                            write(128 | ((c2 >> 0) & 63));
                        } else {
                            write(192 | ((c2 >> 6) & 31));
                            write(128 | ((c2 >> 0) & 63));
                        }
                    }
                    i2 = i3 + iMin;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: rt.jar:java/io/ObjectOutputStream$HandleTable.class */
    private static class HandleTable {
        private int size;
        private int threshold;
        private final float loadFactor;
        private int[] spine;
        private int[] next;
        private Object[] objs;

        HandleTable(int i2, float f2) {
            this.loadFactor = f2;
            this.spine = new int[i2];
            this.next = new int[i2];
            this.objs = new Object[i2];
            this.threshold = (int) (i2 * f2);
            clear();
        }

        int assign(Object obj) {
            if (this.size >= this.next.length) {
                growEntries();
            }
            if (this.size >= this.threshold) {
                growSpine();
            }
            insert(obj, this.size);
            int i2 = this.size;
            this.size = i2 + 1;
            return i2;
        }

        int lookup(Object obj) {
            if (this.size == 0) {
                return -1;
            }
            int i2 = this.spine[hash(obj) % this.spine.length];
            while (true) {
                int i3 = i2;
                if (i3 >= 0) {
                    if (this.objs[i3] != obj) {
                        i2 = this.next[i3];
                    } else {
                        return i3;
                    }
                } else {
                    return -1;
                }
            }
        }

        void clear() {
            Arrays.fill(this.spine, -1);
            Arrays.fill(this.objs, 0, this.size, (Object) null);
            this.size = 0;
        }

        int size() {
            return this.size;
        }

        private void insert(Object obj, int i2) {
            int iHash = hash(obj) % this.spine.length;
            this.objs[i2] = obj;
            this.next[i2] = this.spine[iHash];
            this.spine[iHash] = i2;
        }

        private void growSpine() {
            this.spine = new int[(this.spine.length << 1) + 1];
            this.threshold = (int) (this.spine.length * this.loadFactor);
            Arrays.fill(this.spine, -1);
            for (int i2 = 0; i2 < this.size; i2++) {
                insert(this.objs[i2], i2);
            }
        }

        private void growEntries() {
            int length = (this.next.length << 1) + 1;
            int[] iArr = new int[length];
            System.arraycopy(this.next, 0, iArr, 0, this.size);
            this.next = iArr;
            Object[] objArr = new Object[length];
            System.arraycopy(this.objs, 0, objArr, 0, this.size);
            this.objs = objArr;
        }

        private int hash(Object obj) {
            return System.identityHashCode(obj) & Integer.MAX_VALUE;
        }
    }

    /* loaded from: rt.jar:java/io/ObjectOutputStream$ReplaceTable.class */
    private static class ReplaceTable {
        private final HandleTable htab;
        private Object[] reps;

        ReplaceTable(int i2, float f2) {
            this.htab = new HandleTable(i2, f2);
            this.reps = new Object[i2];
        }

        void assign(Object obj, Object obj2) {
            int iAssign = this.htab.assign(obj);
            while (iAssign >= this.reps.length) {
                grow();
            }
            this.reps[iAssign] = obj2;
        }

        Object lookup(Object obj) {
            int iLookup = this.htab.lookup(obj);
            return iLookup >= 0 ? this.reps[iLookup] : obj;
        }

        void clear() {
            Arrays.fill(this.reps, 0, this.htab.size(), (Object) null);
            this.htab.clear();
        }

        int size() {
            return this.htab.size();
        }

        private void grow() {
            Object[] objArr = new Object[(this.reps.length << 1) + 1];
            System.arraycopy(this.reps, 0, objArr, 0, this.reps.length);
            this.reps = objArr;
        }
    }

    /* loaded from: rt.jar:java/io/ObjectOutputStream$DebugTraceInfoStack.class */
    private static class DebugTraceInfoStack {
        private final List<String> stack = new ArrayList();

        DebugTraceInfoStack() {
        }

        void clear() {
            this.stack.clear();
        }

        void pop() {
            this.stack.remove(this.stack.size() - 1);
        }

        void push(String str) {
            this.stack.add("\t- " + str);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (!this.stack.isEmpty()) {
                int size = this.stack.size();
                while (size > 0) {
                    sb.append(this.stack.get(size - 1) + (size != 1 ? "\n" : ""));
                    size--;
                }
            }
            return sb.toString();
        }
    }
}
