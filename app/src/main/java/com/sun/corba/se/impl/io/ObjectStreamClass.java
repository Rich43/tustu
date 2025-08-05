package com.sun.corba.se.impl.io;

import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.xpath.internal.compiler.OpCodes;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import org.icepdf.core.util.PdfOps;
import org.omg.CORBA.ValueMember;
import sun.corba.Bridge;
import sun.misc.SharedSecrets;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    */
/* loaded from: rt.jar:com/sun/corba/se/impl/io/ObjectStreamClass.class */
public class ObjectStreamClass implements Serializable {
    private static final boolean DEBUG_SVUID = false;
    public static final long kDefaultUID = -1;
    private boolean isEnum;
    public static final int CLASS_MASK = 1553;
    public static final int FIELD_MASK = 223;
    public static final int METHOD_MASK = 3391;
    private String name;
    private ObjectStreamClass superclass;
    private boolean serializable;
    private boolean externalizable;
    private ObjectStreamField[] fields;
    private Class<?> ofClass;
    boolean forProxyClass;
    private long suid;
    private String suidStr;
    private long actualSuid;
    private String actualSuidStr;
    int primBytes;
    int objFields;
    private boolean initialized;
    private Object lock;
    private boolean hasExternalizableBlockData;
    Method writeObjectMethod;
    Method readObjectMethod;
    private transient Method writeReplaceObjectMethod;
    private transient Method readResolveObjectMethod;
    private Constructor<?> cons;
    private transient ProtectionDomain[] domains;
    private String rmiiiopOptionalDataRepId;
    private ObjectStreamClass localClassDesc;
    private static final long serialVersionUID = -6120832682080437368L;
    private static Object[] noArgsList = new Object[0];
    private static Class<?>[] noTypesList = new Class[0];
    private static final Bridge bridge = (Bridge) AccessController.doPrivileged(new PrivilegedAction<Bridge>() { // from class: com.sun.corba.se.impl.io.ObjectStreamClass.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Bridge run2() {
            return Bridge.get();
        }
    });
    private static final PersistentFieldsValue persistentFieldsValue = new PersistentFieldsValue();
    private static ObjectStreamClassEntry[] descriptorFor = new ObjectStreamClassEntry[61];
    private static Method hasStaticInitializerMethod = null;
    public static final ObjectStreamField[] NO_FIELDS = new ObjectStreamField[0];
    private static Comparator compareClassByName = new CompareClassByName();
    private static final Comparator compareObjStrFieldsByName = new CompareObjStrFieldsByName();
    private static Comparator compareMemberByName = new CompareMemberByName();

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$202(com.sun.corba.se.impl.io.ObjectStreamClass r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.suid = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.io.ObjectStreamClass.access$202(com.sun.corba.se.impl.io.ObjectStreamClass, long):long");
    }

    static {
    }

    static final ObjectStreamClass lookup(Class<?> cls) {
        ObjectStreamClass objectStreamClassLookupInternal = lookupInternal(cls);
        if (objectStreamClassLookupInternal.isSerializable() || objectStreamClassLookupInternal.isExternalizable()) {
            return objectStreamClassLookupInternal;
        }
        return null;
    }

    static ObjectStreamClass lookupInternal(Class<?> cls) {
        ObjectStreamClass objectStreamClassFindDescriptorFor;
        Class<? super Object> superclass;
        synchronized (descriptorFor) {
            objectStreamClassFindDescriptorFor = findDescriptorFor(cls);
            if (objectStreamClassFindDescriptorFor == null) {
                boolean zIsAssignableFrom = Serializable.class.isAssignableFrom(cls);
                ObjectStreamClass objectStreamClassLookup = null;
                if (zIsAssignableFrom && (superclass = cls.getSuperclass()) != null) {
                    objectStreamClassLookup = lookup(superclass);
                }
                boolean z2 = false;
                if (zIsAssignableFrom) {
                    z2 = (objectStreamClassLookup != null && objectStreamClassLookup.isExternalizable()) || Externalizable.class.isAssignableFrom(cls);
                    if (z2) {
                        zIsAssignableFrom = false;
                    }
                }
                objectStreamClassFindDescriptorFor = new ObjectStreamClass(cls, objectStreamClassLookup, zIsAssignableFrom, z2);
            }
            objectStreamClassFindDescriptorFor.init();
        }
        return objectStreamClassFindDescriptorFor;
    }

    public final String getName() {
        return this.name;
    }

    public static final long getSerialVersionUID(Class<?> cls) {
        ObjectStreamClass objectStreamClassLookup = lookup(cls);
        if (objectStreamClassLookup != null) {
            return objectStreamClassLookup.getSerialVersionUID();
        }
        return 0L;
    }

    public final long getSerialVersionUID() {
        return this.suid;
    }

    public final String getSerialVersionUIDStr() {
        if (this.suidStr == null) {
            this.suidStr = Long.toHexString(this.suid).toUpperCase();
        }
        return this.suidStr;
    }

    public static final long getActualSerialVersionUID(Class<?> cls) {
        ObjectStreamClass objectStreamClassLookup = lookup(cls);
        if (objectStreamClassLookup != null) {
            return objectStreamClassLookup.getActualSerialVersionUID();
        }
        return 0L;
    }

    public final long getActualSerialVersionUID() {
        return this.actualSuid;
    }

    public final String getActualSerialVersionUIDStr() {
        if (this.actualSuidStr == null) {
            this.actualSuidStr = Long.toHexString(this.actualSuid).toUpperCase();
        }
        return this.actualSuidStr;
    }

    public final Class<?> forClass() {
        return this.ofClass;
    }

    public ObjectStreamField[] getFields() {
        if (this.fields.length > 0) {
            ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[this.fields.length];
            System.arraycopy(this.fields, 0, objectStreamFieldArr, 0, this.fields.length);
            return objectStreamFieldArr;
        }
        return this.fields;
    }

    public boolean hasField(ValueMember valueMember) {
        for (int i2 = 0; i2 < this.fields.length; i2++) {
            try {
                if (this.fields[i2].getName().equals(valueMember.name) && this.fields[i2].getSignature().equals(ValueUtility.getSignature(valueMember))) {
                    return true;
                }
            } catch (Exception e2) {
                return false;
            }
        }
        return false;
    }

    final ObjectStreamField[] getFieldsNoCopy() {
        return this.fields;
    }

    public final ObjectStreamField getField(String str) {
        for (int length = this.fields.length - 1; length >= 0; length--) {
            if (str.equals(this.fields[length].getName())) {
                return this.fields[length];
            }
        }
        return null;
    }

    public Serializable writeReplace(Serializable serializable) {
        if (this.writeReplaceObjectMethod != null) {
            try {
                return (Serializable) this.writeReplaceObjectMethod.invoke(serializable, noArgsList);
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        }
        return serializable;
    }

    public Object readResolve(Object obj) {
        if (this.readResolveObjectMethod != null) {
            try {
                return this.readResolveObjectMethod.invoke(obj, noArgsList);
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        }
        return obj;
    }

    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.name);
        stringBuffer.append(": static final long serialVersionUID = ");
        stringBuffer.append(Long.toString(this.suid));
        stringBuffer.append("L;");
        return stringBuffer.toString();
    }

    private ObjectStreamClass(Class<?> cls, ObjectStreamClass objectStreamClass, boolean z2, boolean z3) {
        this.suid = -1L;
        this.suidStr = null;
        this.actualSuid = -1L;
        this.actualSuidStr = null;
        this.initialized = false;
        this.lock = new Object();
        this.rmiiiopOptionalDataRepId = null;
        this.ofClass = cls;
        if (Proxy.isProxyClass(cls)) {
            this.forProxyClass = true;
        }
        this.name = cls.getName();
        this.isEnum = Enum.class.isAssignableFrom(cls);
        this.superclass = objectStreamClass;
        this.serializable = z2;
        if (!this.forProxyClass) {
            this.externalizable = z3;
        }
        insertDescriptorFor(this);
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/ObjectStreamClass$PersistentFieldsValue.class */
    private static final class PersistentFieldsValue extends ClassValue<ObjectStreamField[]> {
        @Override // java.lang.ClassValue
        protected /* bridge */ /* synthetic */ ObjectStreamField[] computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        PersistentFieldsValue() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ClassValue
        protected ObjectStreamField[] computeValue(Class<?> cls) {
            try {
                Field declaredField = cls.getDeclaredField("serialPersistentFields");
                int modifiers = declaredField.getModifiers();
                if (Modifier.isPrivate(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                    declaredField.setAccessible(true);
                    return translateFields((java.io.ObjectStreamField[]) declaredField.get(cls));
                }
                return null;
            } catch (ClassCastException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException e2) {
                return null;
            }
        }

        private static ObjectStreamField[] translateFields(java.io.ObjectStreamField[] objectStreamFieldArr) {
            ObjectStreamField[] objectStreamFieldArr2 = new ObjectStreamField[objectStreamFieldArr.length];
            for (int i2 = 0; i2 < objectStreamFieldArr.length; i2++) {
                objectStreamFieldArr2[i2] = new ObjectStreamField(objectStreamFieldArr[i2].getName(), objectStreamFieldArr[i2].getType());
            }
            return objectStreamFieldArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ProtectionDomain noPermissionsDomain() {
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

    private void init() {
        synchronized (this.lock) {
            if (this.initialized) {
                return;
            }
            final Class<?> cls = this.ofClass;
            if (!this.serializable || this.externalizable || this.forProxyClass || this.name.equals("java.lang.String")) {
                this.fields = NO_FIELDS;
            } else if (this.serializable) {
                AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.io.ObjectStreamClass.2
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() throws SecurityException {
                        ObjectStreamClass.this.fields = ObjectStreamClass.persistentFieldsValue.get(cls);
                        if (ObjectStreamClass.this.fields != null) {
                            for (int length = ObjectStreamClass.this.fields.length - 1; length >= 0; length--) {
                                try {
                                    Field declaredField = cls.getDeclaredField(ObjectStreamClass.this.fields[length].getName());
                                    if (ObjectStreamClass.this.fields[length].getType() == declaredField.getType()) {
                                        declaredField.setAccessible(true);
                                        ObjectStreamClass.this.fields[length].setField(declaredField);
                                    }
                                } catch (NoSuchFieldException e2) {
                                }
                            }
                            return null;
                        }
                        Field[] declaredFields = cls.getDeclaredFields();
                        int i2 = 0;
                        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[declaredFields.length];
                        for (Field field : declaredFields) {
                            int modifiers = field.getModifiers();
                            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                                field.setAccessible(true);
                                int i3 = i2;
                                i2++;
                                objectStreamFieldArr[i3] = new ObjectStreamField(field);
                            }
                        }
                        ObjectStreamClass.this.fields = new ObjectStreamField[i2];
                        System.arraycopy(objectStreamFieldArr, 0, ObjectStreamClass.this.fields, 0, i2);
                        return null;
                    }
                });
                if (this.fields.length > 1) {
                    Arrays.sort(this.fields);
                }
                computeFieldInfo();
            }
            if (isNonSerializable() || this.isEnum) {
                this.suid = 0L;
            } else {
                AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.io.ObjectStreamClass.3
                    /* JADX WARN: Failed to check method for inline after forced processcom.sun.corba.se.impl.io.ObjectStreamClass.access$202(com.sun.corba.se.impl.io.ObjectStreamClass, long):long */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() throws SecurityException {
                        if (ObjectStreamClass.this.forProxyClass) {
                            ObjectStreamClass.access$202(ObjectStreamClass.this, 0L);
                        } else {
                            try {
                                Field declaredField = cls.getDeclaredField("serialVersionUID");
                                int modifiers = declaredField.getModifiers();
                                if (!Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers)) {
                                    ObjectStreamClass.access$202(ObjectStreamClass.this, ObjectStreamClass._computeSerialVersionUID(cls));
                                } else {
                                    declaredField.setAccessible(true);
                                    ObjectStreamClass.access$202(ObjectStreamClass.this, declaredField.getLong(cls));
                                }
                            } catch (IllegalAccessException e2) {
                                ObjectStreamClass.access$202(ObjectStreamClass.this, ObjectStreamClass._computeSerialVersionUID(cls));
                            } catch (NoSuchFieldException e3) {
                                ObjectStreamClass.access$202(ObjectStreamClass.this, ObjectStreamClass._computeSerialVersionUID(cls));
                            }
                        }
                        ObjectStreamClass.this.writeReplaceObjectMethod = ObjectStreamClass.getInheritableMethod(cls, "writeReplace", ObjectStreamClass.noTypesList, Object.class);
                        ObjectStreamClass.this.readResolveObjectMethod = ObjectStreamClass.getInheritableMethod(cls, "readResolve", ObjectStreamClass.noTypesList, Object.class);
                        ObjectStreamClass.this.domains = new ProtectionDomain[]{ObjectStreamClass.this.noPermissionsDomain()};
                        if (ObjectStreamClass.this.externalizable) {
                            ObjectStreamClass.this.cons = ObjectStreamClass.getExternalizableConstructor(cls);
                        } else {
                            ObjectStreamClass.this.cons = ObjectStreamClass.getSerializableConstructor(cls);
                        }
                        ObjectStreamClass.this.domains = ObjectStreamClass.this.getProtectionDomains(ObjectStreamClass.this.cons, cls);
                        if (ObjectStreamClass.this.serializable && !ObjectStreamClass.this.forProxyClass) {
                            ObjectStreamClass.this.writeObjectMethod = ObjectStreamClass.getPrivateMethod(cls, "writeObject", new Class[]{ObjectOutputStream.class}, Void.TYPE);
                            ObjectStreamClass.this.readObjectMethod = ObjectStreamClass.getPrivateMethod(cls, "readObject", new Class[]{ObjectInputStream.class}, Void.TYPE);
                            return null;
                        }
                        return null;
                    }
                });
            }
            this.actualSuid = computeStructuralUID(this, cls);
            if (hasWriteObject()) {
                this.rmiiiopOptionalDataRepId = computeRMIIIOPOptionalDataRepId();
            }
            this.initialized = true;
        }
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

    private String computeRMIIIOPOptionalDataRepId() {
        StringBuffer stringBuffer = new StringBuffer("RMI:org.omg.custom.");
        stringBuffer.append(RepositoryId.convertToISOLatin1(getName()));
        stringBuffer.append(':');
        stringBuffer.append(getActualSerialVersionUIDStr());
        stringBuffer.append(':');
        stringBuffer.append(getSerialVersionUIDStr());
        return stringBuffer.toString();
    }

    public final String getRMIIIOPOptionalDataRepId() {
        return this.rmiiiopOptionalDataRepId;
    }

    ObjectStreamClass(String str, long j2) {
        this.suid = -1L;
        this.suidStr = null;
        this.actualSuid = -1L;
        this.actualSuidStr = null;
        this.initialized = false;
        this.lock = new Object();
        this.rmiiiopOptionalDataRepId = null;
        this.name = str;
        this.suid = j2;
        this.superclass = null;
    }

    final void setClass(Class<?> cls) throws InvalidClassException {
        if (cls == null) {
            this.localClassDesc = null;
            this.ofClass = null;
            computeFieldInfo();
            return;
        }
        this.localClassDesc = lookupInternal(cls);
        if (this.localClassDesc == null) {
            throw new InvalidClassException(cls.getName(), "Local class not compatible");
        }
        if (this.suid != this.localClassDesc.suid) {
            boolean z2 = isNonSerializable() || this.localClassDesc.isNonSerializable();
            if (!(cls.isArray() && !cls.getName().equals(this.name)) && !z2) {
                throw new InvalidClassException(cls.getName(), "Local class not compatible: stream classdesc serialVersionUID=" + this.suid + " local class serialVersionUID=" + this.localClassDesc.suid);
            }
        }
        if (!compareClassNames(this.name, cls.getName(), '.')) {
            throw new InvalidClassException(cls.getName(), "Incompatible local class name. Expected class name compatible with " + this.name);
        }
        if (this.serializable != this.localClassDesc.serializable || this.externalizable != this.localClassDesc.externalizable || (!this.serializable && !this.externalizable)) {
            throw new InvalidClassException(cls.getName(), "Serialization incompatible with Externalization");
        }
        ObjectStreamField[] objectStreamFieldArr = this.localClassDesc.fields;
        ObjectStreamField[] objectStreamFieldArr2 = this.fields;
        int i2 = 0;
        for (int i3 = 0; i3 < objectStreamFieldArr2.length; i3++) {
            int i4 = i2;
            while (true) {
                if (i4 >= objectStreamFieldArr.length) {
                    break;
                }
                if (!objectStreamFieldArr2[i3].getName().equals(objectStreamFieldArr[i4].getName())) {
                    i4++;
                } else {
                    if (objectStreamFieldArr2[i3].isPrimitive() && !objectStreamFieldArr2[i3].typeEquals(objectStreamFieldArr[i4])) {
                        throw new InvalidClassException(cls.getName(), "The type of field " + objectStreamFieldArr2[i3].getName() + " of class " + this.name + " is incompatible.");
                    }
                    i2 = i4;
                    objectStreamFieldArr2[i3].setField(objectStreamFieldArr[i2].getField());
                }
            }
        }
        computeFieldInfo();
        this.ofClass = cls;
        this.readObjectMethod = this.localClassDesc.readObjectMethod;
        this.readResolveObjectMethod = this.localClassDesc.readResolveObjectMethod;
    }

    static boolean compareClassNames(String str, String str2, char c2) {
        int iLastIndexOf = str.lastIndexOf(c2);
        if (iLastIndexOf < 0) {
            iLastIndexOf = 0;
        }
        int iLastIndexOf2 = str2.lastIndexOf(c2);
        if (iLastIndexOf2 < 0) {
            iLastIndexOf2 = 0;
        }
        return str.regionMatches(false, iLastIndexOf, str2, iLastIndexOf2, str.length() - iLastIndexOf);
    }

    final boolean typeEquals(ObjectStreamClass objectStreamClass) {
        return this.suid == objectStreamClass.suid && compareClassNames(this.name, objectStreamClass.name, '.');
    }

    final void setSuperclass(ObjectStreamClass objectStreamClass) {
        this.superclass = objectStreamClass;
    }

    final ObjectStreamClass getSuperclass() {
        return this.superclass;
    }

    final boolean hasReadObject() {
        return this.readObjectMethod != null;
    }

    final boolean hasWriteObject() {
        return this.writeObjectMethod != null;
    }

    final boolean isCustomMarshaled() {
        return hasWriteObject() || isExternalizable() || (this.superclass != null && this.superclass.isCustomMarshaled());
    }

    boolean hasExternalizableBlockDataMode() {
        return this.hasExternalizableBlockData;
    }

    Object newInstance() throws UnsupportedOperationException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if (!this.initialized) {
            throw new InternalError("Unexpected call when not initialized");
        }
        if (this.cons != null) {
            try {
                if (this.domains == null || this.domains.length == 0) {
                    return this.cons.newInstance(new Object[0]);
                }
                try {
                    return SharedSecrets.getJavaSecurityAccess().doIntersectionPrivilege(new PrivilegedAction() { // from class: com.sun.corba.se.impl.io.ObjectStreamClass.4
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Object run2() {
                            try {
                                return ObjectStreamClass.this.cons.newInstance(new Object[0]);
                            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e2) {
                                throw new UndeclaredThrowableException(e2);
                            }
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
                InternalError internalError = new InternalError();
                internalError.initCause(e3);
                throw internalError;
            }
        }
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Constructor getExternalizableConstructor(Class<?> cls) throws SecurityException {
        try {
            Constructor<?> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            declaredConstructor.setAccessible(true);
            if ((declaredConstructor.getModifiers() & 1) != 0) {
                return declaredConstructor;
            }
            return null;
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Constructor getSerializableConstructor(Class<?> cls) throws SecurityException {
        Class<?> cls2 = cls;
        while (Serializable.class.isAssignableFrom(cls2)) {
            Class<? super Object> superclass = cls2.getSuperclass();
            cls2 = superclass;
            if (superclass == null) {
                return null;
            }
        }
        try {
            Constructor<?> declaredConstructor = cls2.getDeclaredConstructor(new Class[0]);
            int modifiers = declaredConstructor.getModifiers();
            if ((modifiers & 2) == 0) {
                if ((modifiers & 5) == 0 && !packageEquals(cls, cls2)) {
                    return null;
                }
                Constructor constructorNewConstructorForSerialization = bridge.newConstructorForSerialization(cls, declaredConstructor);
                constructorNewConstructorForSerialization.setAccessible(true);
                return constructorNewConstructorForSerialization;
            }
            return null;
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    final ObjectStreamClass localClassDescriptor() {
        return this.localClassDesc;
    }

    boolean isSerializable() {
        return this.serializable;
    }

    boolean isExternalizable() {
        return this.externalizable;
    }

    boolean isNonSerializable() {
        return (this.externalizable || this.serializable) ? false : true;
    }

    private void computeFieldInfo() {
        this.primBytes = 0;
        this.objFields = 0;
        for (int i2 = 0; i2 < this.fields.length; i2++) {
            switch (this.fields[i2].getTypeCode()) {
                case 'B':
                case 'Z':
                    this.primBytes++;
                    break;
                case 'C':
                case 'S':
                    this.primBytes += 2;
                    break;
                case 'D':
                case 'J':
                    this.primBytes += 8;
                    break;
                case 'F':
                case 'I':
                    this.primBytes += 4;
                    break;
                case 'L':
                case '[':
                    this.objFields++;
                    break;
            }
        }
    }

    private static void msg(String str) {
        System.out.println(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long _computeSerialVersionUID(Class<?> cls) throws SecurityException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        long j2 = 0;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(byteArrayOutputStream, messageDigest));
            dataOutputStream.writeUTF(cls.getName());
            int modifiers = cls.getModifiers() & CLASS_MASK;
            Method[] declaredMethods = cls.getDeclaredMethods();
            if ((modifiers & 512) != 0) {
                modifiers &= -1025;
                if (declaredMethods.length > 0) {
                    modifiers |= 1024;
                }
            }
            dataOutputStream.writeInt(modifiers & CLASS_MASK);
            if (!cls.isArray()) {
                Class<?>[] interfaces = cls.getInterfaces();
                Arrays.sort(interfaces, compareClassByName);
                for (Class<?> cls2 : interfaces) {
                    dataOutputStream.writeUTF(cls2.getName());
                }
            }
            Field[] declaredFields = cls.getDeclaredFields();
            Arrays.sort(declaredFields, compareMemberByName);
            for (Field field : declaredFields) {
                int modifiers2 = field.getModifiers();
                if (!Modifier.isPrivate(modifiers2) || (!Modifier.isTransient(modifiers2) && !Modifier.isStatic(modifiers2))) {
                    dataOutputStream.writeUTF(field.getName());
                    dataOutputStream.writeInt(modifiers2 & 223);
                    dataOutputStream.writeUTF(getSignature(field.getType()));
                }
            }
            if (hasStaticInitializer(cls)) {
                dataOutputStream.writeUTF(Constants.STATIC_INITIALIZER_NAME);
                dataOutputStream.writeInt(8);
                dataOutputStream.writeUTF("()V");
            }
            for (MethodSignature methodSignature : MethodSignature.removePrivateAndSort(cls.getDeclaredConstructors())) {
                String strReplace = methodSignature.signature.replace('/', '.');
                dataOutputStream.writeUTF(Constants.CONSTRUCTOR_NAME);
                dataOutputStream.writeInt(methodSignature.member.getModifiers() & METHOD_MASK);
                dataOutputStream.writeUTF(strReplace);
            }
            for (MethodSignature methodSignature2 : MethodSignature.removePrivateAndSort(declaredMethods)) {
                String strReplace2 = methodSignature2.signature.replace('/', '.');
                dataOutputStream.writeUTF(methodSignature2.member.getName());
                dataOutputStream.writeInt(methodSignature2.member.getModifiers() & METHOD_MASK);
                dataOutputStream.writeUTF(strReplace2);
            }
            dataOutputStream.flush();
            for (int i2 = 0; i2 < Math.min(8, messageDigest.digest().length); i2++) {
                j2 += (r0[i2] & 255) << (i2 * 8);
            }
        } catch (IOException e2) {
            j2 = -1;
        } catch (NoSuchAlgorithmException e3) {
            SecurityException securityException = new SecurityException();
            securityException.initCause(e3);
            throw securityException;
        }
        return j2;
    }

    private static long computeStructuralUID(ObjectStreamClass objectStreamClass, Class<?> cls) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        long j2 = 0;
        try {
        } catch (IOException e2) {
            j2 = -1;
        } catch (NoSuchAlgorithmException e3) {
            SecurityException securityException = new SecurityException();
            securityException.initCause(e3);
            throw securityException;
        }
        if (!Serializable.class.isAssignableFrom(cls) || cls.isInterface()) {
            return 0L;
        }
        if (Externalizable.class.isAssignableFrom(cls)) {
            return 1L;
        }
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(byteArrayOutputStream, messageDigest));
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass != null) {
            dataOutputStream.writeLong(computeStructuralUID(lookup(superclass), superclass));
        }
        if (objectStreamClass.hasWriteObject()) {
            dataOutputStream.writeInt(2);
        } else {
            dataOutputStream.writeInt(1);
        }
        ObjectStreamField[] fields = objectStreamClass.getFields();
        if (fields.length > 1) {
            Arrays.sort(fields, compareObjStrFieldsByName);
        }
        for (int i2 = 0; i2 < fields.length; i2++) {
            dataOutputStream.writeUTF(fields[i2].getName());
            dataOutputStream.writeUTF(fields[i2].getSignature());
        }
        dataOutputStream.flush();
        for (int i3 = 0; i3 < Math.min(8, messageDigest.digest().length); i3++) {
            j2 += (r0[i3] & 255) << (i3 * 8);
        }
        return j2;
    }

    static String getSignature(Class<?> cls) {
        String string = null;
        if (cls.isArray()) {
            Class<?> componentType = cls;
            int i2 = 0;
            while (componentType.isArray()) {
                i2++;
                componentType = componentType.getComponentType();
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (int i3 = 0; i3 < i2; i3++) {
                stringBuffer.append("[");
            }
            stringBuffer.append(getSignature(componentType));
            string = stringBuffer.toString();
        } else if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                string = "I";
            } else if (cls == Byte.TYPE) {
                string = PdfOps.B_TOKEN;
            } else if (cls == Long.TYPE) {
                string = "J";
            } else if (cls == Float.TYPE) {
                string = PdfOps.F_TOKEN;
            } else if (cls == Double.TYPE) {
                string = PdfOps.D_TOKEN;
            } else if (cls == Short.TYPE) {
                string = PdfOps.S_TOKEN;
            } else if (cls == Character.TYPE) {
                string = "C";
            } else if (cls == Boolean.TYPE) {
                string = com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG;
            } else if (cls == Void.TYPE) {
                string = "V";
            }
        } else {
            string = "L" + cls.getName().replace('.', '/') + ";";
        }
        return string;
    }

    static String getSignature(Method method) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for (Class<?> cls : method.getParameterTypes()) {
            stringBuffer.append(getSignature(cls));
        }
        stringBuffer.append(")");
        stringBuffer.append(getSignature(method.getReturnType()));
        return stringBuffer.toString();
    }

    static String getSignature(Constructor constructor) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for (Class<?> cls : constructor.getParameterTypes()) {
            stringBuffer.append(getSignature(cls));
        }
        stringBuffer.append(")V");
        return stringBuffer.toString();
    }

    private static ObjectStreamClass findDescriptorFor(Class<?> cls) {
        ObjectStreamClassEntry objectStreamClassEntry;
        int iHashCode = (cls.hashCode() & Integer.MAX_VALUE) % descriptorFor.length;
        while (true) {
            ObjectStreamClassEntry objectStreamClassEntry2 = descriptorFor[iHashCode];
            objectStreamClassEntry = objectStreamClassEntry2;
            if (objectStreamClassEntry2 == null || objectStreamClassEntry.get() != null) {
                break;
            }
            descriptorFor[iHashCode] = objectStreamClassEntry.next;
        }
        ObjectStreamClassEntry objectStreamClassEntry3 = objectStreamClassEntry;
        while (objectStreamClassEntry != null) {
            ObjectStreamClass objectStreamClass = (ObjectStreamClass) objectStreamClassEntry.get();
            if (objectStreamClass == null) {
                objectStreamClassEntry3.next = objectStreamClassEntry.next;
            } else {
                if (objectStreamClass.ofClass == cls) {
                    return objectStreamClass;
                }
                objectStreamClassEntry3 = objectStreamClassEntry;
            }
            objectStreamClassEntry = objectStreamClassEntry.next;
        }
        return null;
    }

    private static void insertDescriptorFor(ObjectStreamClass objectStreamClass) {
        if (findDescriptorFor(objectStreamClass.ofClass) != null) {
            return;
        }
        int iHashCode = (objectStreamClass.ofClass.hashCode() & Integer.MAX_VALUE) % descriptorFor.length;
        ObjectStreamClassEntry objectStreamClassEntry = new ObjectStreamClassEntry(objectStreamClass);
        objectStreamClassEntry.next = descriptorFor[iHashCode];
        descriptorFor[iHashCode] = objectStreamClassEntry;
    }

    private static Field[] getDeclaredFields(final Class<?> cls) {
        return (Field[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.io.ObjectStreamClass.5
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return cls.getDeclaredFields();
            }
        });
    }

    private static boolean hasStaticInitializer(Class<?> cls) {
        if (hasStaticInitializerMethod == null) {
            Class cls2 = null;
            if (0 == 0) {
                cls2 = java.io.ObjectStreamClass.class;
            }
            try {
                hasStaticInitializerMethod = cls2.getDeclaredMethod("hasStaticInitializer", Class.class);
            } catch (NoSuchMethodException e2) {
            }
            if (hasStaticInitializerMethod == null) {
                throw new InternalError("Can't find hasStaticInitializer method on " + cls2.getName());
            }
            hasStaticInitializerMethod.setAccessible(true);
        }
        try {
            return ((Boolean) hasStaticInitializerMethod.invoke(null, cls)).booleanValue();
        } catch (Exception e3) {
            InternalError internalError = new InternalError("Error invoking hasStaticInitializer");
            internalError.initCause(e3);
            throw internalError;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/ObjectStreamClass$ObjectStreamClassEntry.class */
    private static class ObjectStreamClassEntry {
        ObjectStreamClassEntry next;

        /* renamed from: c, reason: collision with root package name */
        private ObjectStreamClass f11801c;

        ObjectStreamClassEntry(ObjectStreamClass objectStreamClass) {
            this.f11801c = objectStreamClass;
        }

        public Object get() {
            return this.f11801c;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/ObjectStreamClass$CompareClassByName.class */
    private static class CompareClassByName implements Comparator {
        private CompareClassByName() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            return ((Class) obj).getName().compareTo(((Class) obj2).getName());
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/ObjectStreamClass$CompareObjStrFieldsByName.class */
    private static class CompareObjStrFieldsByName implements Comparator {
        private CompareObjStrFieldsByName() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            return ((ObjectStreamField) obj).getName().compareTo(((ObjectStreamField) obj2).getName());
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/ObjectStreamClass$CompareMemberByName.class */
    private static class CompareMemberByName implements Comparator {
        private CompareMemberByName() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            String name = ((Member) obj).getName();
            String name2 = ((Member) obj2).getName();
            if (obj instanceof Method) {
                name = name + ObjectStreamClass.getSignature((Method) obj);
                name2 = name2 + ObjectStreamClass.getSignature((Method) obj2);
            } else if (obj instanceof Constructor) {
                name = name + ObjectStreamClass.getSignature((Constructor) obj);
                name2 = name2 + ObjectStreamClass.getSignature((Constructor) obj2);
            }
            return name.compareTo(name2);
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/ObjectStreamClass$MethodSignature.class */
    private static class MethodSignature implements Comparator {
        Member member;
        String signature;

        static MethodSignature[] removePrivateAndSort(Member[] memberArr) {
            int i2 = 0;
            for (Member member : memberArr) {
                if (!Modifier.isPrivate(member.getModifiers())) {
                    i2++;
                }
            }
            MethodSignature[] methodSignatureArr = new MethodSignature[i2];
            int i3 = 0;
            for (int i4 = 0; i4 < memberArr.length; i4++) {
                if (!Modifier.isPrivate(memberArr[i4].getModifiers())) {
                    methodSignatureArr[i3] = new MethodSignature(memberArr[i4]);
                    i3++;
                }
            }
            if (i3 > 0) {
                Arrays.sort(methodSignatureArr, methodSignatureArr[0]);
            }
            return methodSignatureArr;
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            int iCompareTo;
            if (obj == obj2) {
                return 0;
            }
            MethodSignature methodSignature = (MethodSignature) obj;
            MethodSignature methodSignature2 = (MethodSignature) obj2;
            if (isConstructor()) {
                iCompareTo = methodSignature.signature.compareTo(methodSignature2.signature);
            } else {
                iCompareTo = methodSignature.member.getName().compareTo(methodSignature2.member.getName());
                if (iCompareTo == 0) {
                    iCompareTo = methodSignature.signature.compareTo(methodSignature2.signature);
                }
            }
            return iCompareTo;
        }

        private final boolean isConstructor() {
            return this.member instanceof Constructor;
        }

        private MethodSignature(Member member) {
            this.member = member;
            if (isConstructor()) {
                this.signature = ObjectStreamClass.getSignature((Constructor) member);
            } else {
                this.signature = ObjectStreamClass.getSignature((Method) member);
            }
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

    private static boolean packageEquals(Class<?> cls, Class<?> cls2) {
        Package r0 = cls.getPackage();
        Package r02 = cls2.getPackage();
        return r0 == r02 || (r0 != null && r0.equals(r02));
    }
}
