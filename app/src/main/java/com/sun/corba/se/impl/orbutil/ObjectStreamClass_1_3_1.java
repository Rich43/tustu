package com.sun.corba.se.impl.orbutil;

import com.sun.corba.se.impl.io.ObjectStreamClass;
import com.sun.corba.se.impl.io.ValueUtility;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import org.icepdf.core.util.PdfOps;
import org.omg.CORBA.ValueMember;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectStreamClass_1_3_1.class */
public class ObjectStreamClass_1_3_1 implements Serializable {
    public static final long kDefaultUID = -1;
    private static Hashtable translatedFields;
    private String name;
    private ObjectStreamClass_1_3_1 superclass;
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
    private Object lock;
    private boolean hasWriteObjectMethod;
    private boolean hasExternalizableBlockData;
    Method writeObjectMethod;
    Method readObjectMethod;
    private transient Method writeReplaceObjectMethod;
    private transient Method readResolveObjectMethod;
    private ObjectStreamClass_1_3_1 localClassDesc;
    private static final long serialVersionUID = -6120832682080437368L;
    private static Object[] noArgsList = new Object[0];
    private static Class<?>[] noTypesList = new Class[0];
    private static ObjectStreamClassEntry[] descriptorFor = new ObjectStreamClassEntry[61];
    public static final ObjectStreamField[] NO_FIELDS = new ObjectStreamField[0];
    private static Comparator compareClassByName = new CompareClassByName();
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
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ long access$202(com.sun.corba.se.impl.orbutil.ObjectStreamClass_1_3_1 r6, long r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.suid = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.orbutil.ObjectStreamClass_1_3_1.access$202(com.sun.corba.se.impl.orbutil.ObjectStreamClass_1_3_1, long):long");
    }

    static {
    }

    static final ObjectStreamClass_1_3_1 lookup(Class<?> cls) {
        ObjectStreamClass_1_3_1 objectStreamClass_1_3_1LookupInternal = lookupInternal(cls);
        if (objectStreamClass_1_3_1LookupInternal.isSerializable() || objectStreamClass_1_3_1LookupInternal.isExternalizable()) {
            return objectStreamClass_1_3_1LookupInternal;
        }
        return null;
    }

    static ObjectStreamClass_1_3_1 lookupInternal(Class<?> cls) {
        Class<? super Object> superclass;
        synchronized (descriptorFor) {
            ObjectStreamClass_1_3_1 objectStreamClass_1_3_1FindDescriptorFor = findDescriptorFor(cls);
            if (objectStreamClass_1_3_1FindDescriptorFor != null) {
                return objectStreamClass_1_3_1FindDescriptorFor;
            }
            boolean zIsAssignableFrom = Serializable.class.isAssignableFrom(cls);
            ObjectStreamClass_1_3_1 objectStreamClass_1_3_1Lookup = null;
            if (zIsAssignableFrom && (superclass = cls.getSuperclass()) != null) {
                objectStreamClass_1_3_1Lookup = lookup(superclass);
            }
            boolean z2 = false;
            if (zIsAssignableFrom) {
                z2 = (objectStreamClass_1_3_1Lookup != null && objectStreamClass_1_3_1Lookup.isExternalizable()) || Externalizable.class.isAssignableFrom(cls);
                if (z2) {
                    zIsAssignableFrom = false;
                }
            }
            ObjectStreamClass_1_3_1 objectStreamClass_1_3_1 = new ObjectStreamClass_1_3_1(cls, objectStreamClass_1_3_1Lookup, zIsAssignableFrom, z2);
            objectStreamClass_1_3_1.init();
            return objectStreamClass_1_3_1;
        }
    }

    public final String getName() {
        return this.name;
    }

    public static final long getSerialVersionUID(Class<?> cls) {
        ObjectStreamClass_1_3_1 objectStreamClass_1_3_1Lookup = lookup(cls);
        if (objectStreamClass_1_3_1Lookup != null) {
            return objectStreamClass_1_3_1Lookup.getSerialVersionUID();
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
        ObjectStreamClass_1_3_1 objectStreamClass_1_3_1Lookup = lookup(cls);
        if (objectStreamClass_1_3_1Lookup != null) {
            return objectStreamClass_1_3_1Lookup.getActualSerialVersionUID();
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
            if (this.fields[i2].getName().equals(valueMember.name) && this.fields[i2].getSignature().equals(ValueUtility.getSignature(valueMember))) {
                return true;
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
                throw new RuntimeException(th.getMessage());
            }
        }
        return serializable;
    }

    public Object readResolve(Object obj) {
        if (this.readResolveObjectMethod != null) {
            try {
                return this.readResolveObjectMethod.invoke(obj, noArgsList);
            } catch (Throwable th) {
                throw new RuntimeException(th.getMessage());
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

    private ObjectStreamClass_1_3_1(Class<?> cls, ObjectStreamClass_1_3_1 objectStreamClass_1_3_1, boolean z2, boolean z3) {
        this.suid = -1L;
        this.suidStr = null;
        this.actualSuid = -1L;
        this.actualSuidStr = null;
        this.lock = new Object();
        this.ofClass = cls;
        if (Proxy.isProxyClass(cls)) {
            this.forProxyClass = true;
        }
        this.name = cls.getName();
        this.superclass = objectStreamClass_1_3_1;
        this.serializable = z2;
        if (!this.forProxyClass) {
            this.externalizable = z3;
        }
        insertDescriptorFor(this);
    }

    private void init() {
        synchronized (this.lock) {
            final Class<?> cls = this.ofClass;
            if (this.fields != null) {
                return;
            }
            if (!this.serializable || this.externalizable || this.forProxyClass || this.name.equals("java.lang.String")) {
                this.fields = NO_FIELDS;
            } else if (this.serializable) {
                AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.orbutil.ObjectStreamClass_1_3_1.1
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() throws SecurityException {
                        try {
                            Field declaredField = cls.getDeclaredField("serialPersistentFields");
                            declaredField.setAccessible(true);
                            int modifiers = declaredField.getModifiers();
                            if (Modifier.isPrivate(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                                ObjectStreamClass_1_3_1.this.fields = (ObjectStreamField[]) ObjectStreamClass_1_3_1.translateFields((Object[]) declaredField.get(cls));
                            }
                        } catch (ClassCastException e2) {
                            ObjectStreamClass_1_3_1.this.fields = null;
                        } catch (IllegalAccessException e3) {
                            ObjectStreamClass_1_3_1.this.fields = null;
                        } catch (IllegalArgumentException e4) {
                            ObjectStreamClass_1_3_1.this.fields = null;
                        } catch (NoSuchFieldException e5) {
                            ObjectStreamClass_1_3_1.this.fields = null;
                        }
                        if (ObjectStreamClass_1_3_1.this.fields != null) {
                            for (int length = ObjectStreamClass_1_3_1.this.fields.length - 1; length >= 0; length--) {
                                try {
                                    Field declaredField2 = cls.getDeclaredField(ObjectStreamClass_1_3_1.this.fields[length].getName());
                                    if (ObjectStreamClass_1_3_1.this.fields[length].getType() == declaredField2.getType()) {
                                        ObjectStreamClass_1_3_1.this.fields[length].setField(declaredField2);
                                    }
                                } catch (NoSuchFieldException e6) {
                                }
                            }
                            return null;
                        }
                        Field[] declaredFields = cls.getDeclaredFields();
                        int i2 = 0;
                        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[declaredFields.length];
                        for (int i3 = 0; i3 < declaredFields.length; i3++) {
                            int modifiers2 = declaredFields[i3].getModifiers();
                            if (!Modifier.isStatic(modifiers2) && !Modifier.isTransient(modifiers2)) {
                                int i4 = i2;
                                i2++;
                                objectStreamFieldArr[i4] = new ObjectStreamField(declaredFields[i3]);
                            }
                        }
                        ObjectStreamClass_1_3_1.this.fields = new ObjectStreamField[i2];
                        System.arraycopy(objectStreamFieldArr, 0, ObjectStreamClass_1_3_1.this.fields, 0, i2);
                        return null;
                    }
                });
                if (this.fields.length > 1) {
                    Arrays.sort(this.fields);
                }
                computeFieldInfo();
            }
            if (isNonSerializable()) {
                this.suid = 0L;
            } else {
                AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.orbutil.ObjectStreamClass_1_3_1.2
                    /* JADX WARN: Failed to check method for inline after forced processcom.sun.corba.se.impl.orbutil.ObjectStreamClass_1_3_1.access$202(com.sun.corba.se.impl.orbutil.ObjectStreamClass_1_3_1, long):long */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() throws SecurityException {
                        if (ObjectStreamClass_1_3_1.this.forProxyClass) {
                            ObjectStreamClass_1_3_1.access$202(ObjectStreamClass_1_3_1.this, 0L);
                        } else {
                            try {
                                Field declaredField = cls.getDeclaredField("serialVersionUID");
                                int modifiers = declaredField.getModifiers();
                                if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                                    declaredField.setAccessible(true);
                                    ObjectStreamClass_1_3_1.access$202(ObjectStreamClass_1_3_1.this, declaredField.getLong(cls));
                                } else {
                                    ObjectStreamClass_1_3_1.access$202(ObjectStreamClass_1_3_1.this, ObjectStreamClass.getSerialVersionUID(cls));
                                }
                            } catch (IllegalAccessException e2) {
                                ObjectStreamClass_1_3_1.access$202(ObjectStreamClass_1_3_1.this, ObjectStreamClass.getSerialVersionUID(cls));
                            } catch (NoSuchFieldException e3) {
                                ObjectStreamClass_1_3_1.access$202(ObjectStreamClass_1_3_1.this, ObjectStreamClass.getSerialVersionUID(cls));
                            }
                        }
                        try {
                            ObjectStreamClass_1_3_1.this.writeReplaceObjectMethod = cls.getDeclaredMethod("writeReplace", ObjectStreamClass_1_3_1.noTypesList);
                            if (Modifier.isStatic(ObjectStreamClass_1_3_1.this.writeReplaceObjectMethod.getModifiers())) {
                                ObjectStreamClass_1_3_1.this.writeReplaceObjectMethod = null;
                            } else {
                                ObjectStreamClass_1_3_1.this.writeReplaceObjectMethod.setAccessible(true);
                            }
                        } catch (NoSuchMethodException e4) {
                        }
                        try {
                            ObjectStreamClass_1_3_1.this.readResolveObjectMethod = cls.getDeclaredMethod("readResolve", ObjectStreamClass_1_3_1.noTypesList);
                            if (Modifier.isStatic(ObjectStreamClass_1_3_1.this.readResolveObjectMethod.getModifiers())) {
                                ObjectStreamClass_1_3_1.this.readResolveObjectMethod = null;
                            } else {
                                ObjectStreamClass_1_3_1.this.readResolveObjectMethod.setAccessible(true);
                            }
                        } catch (NoSuchMethodException e5) {
                        }
                        if (ObjectStreamClass_1_3_1.this.serializable && !ObjectStreamClass_1_3_1.this.forProxyClass) {
                            try {
                                ObjectStreamClass_1_3_1.this.writeObjectMethod = cls.getDeclaredMethod("writeObject", ObjectOutputStream.class);
                                ObjectStreamClass_1_3_1.this.hasWriteObjectMethod = true;
                                int modifiers2 = ObjectStreamClass_1_3_1.this.writeObjectMethod.getModifiers();
                                if (!Modifier.isPrivate(modifiers2) || Modifier.isStatic(modifiers2)) {
                                    ObjectStreamClass_1_3_1.this.writeObjectMethod = null;
                                    ObjectStreamClass_1_3_1.this.hasWriteObjectMethod = false;
                                }
                            } catch (NoSuchMethodException e6) {
                            }
                            try {
                                ObjectStreamClass_1_3_1.this.readObjectMethod = cls.getDeclaredMethod("readObject", ObjectInputStream.class);
                                int modifiers3 = ObjectStreamClass_1_3_1.this.readObjectMethod.getModifiers();
                                if (!Modifier.isPrivate(modifiers3) || Modifier.isStatic(modifiers3)) {
                                    ObjectStreamClass_1_3_1.this.readObjectMethod = null;
                                }
                                return null;
                            } catch (NoSuchMethodException e7) {
                                return null;
                            }
                        }
                        return null;
                    }
                });
            }
            this.actualSuid = computeStructuralUID(this, cls);
        }
    }

    ObjectStreamClass_1_3_1(String str, long j2) {
        this.suid = -1L;
        this.suidStr = null;
        this.actualSuid = -1L;
        this.actualSuidStr = null;
        this.lock = new Object();
        this.name = str;
        this.suid = j2;
        this.superclass = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object[] translateFields(Object[] objArr) throws NoSuchFieldException {
        try {
            java.io.ObjectStreamField[] objectStreamFieldArr = (java.io.ObjectStreamField[]) objArr;
            if (translatedFields == null) {
                translatedFields = new Hashtable();
            }
            Object[] objArr2 = (Object[]) translatedFields.get(objectStreamFieldArr);
            if (objArr2 != null) {
                return objArr2;
            }
            Object[] objArr3 = (Object[]) Array.newInstance((Class<?>) ObjectStreamField.class, objArr.length);
            Object[] objArr4 = new Object[2];
            Constructor declaredConstructor = ObjectStreamField.class.getDeclaredConstructor(String.class, Class.class);
            for (int length = objectStreamFieldArr.length - 1; length >= 0; length--) {
                objArr4[0] = objectStreamFieldArr[length].getName();
                objArr4[1] = objectStreamFieldArr[length].getType();
                objArr3[length] = declaredConstructor.newInstance(objArr4);
            }
            translatedFields.put(objectStreamFieldArr, objArr3);
            return objArr3;
        } catch (Throwable th) {
            throw new NoSuchFieldException();
        }
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

    final boolean typeEquals(ObjectStreamClass_1_3_1 objectStreamClass_1_3_1) {
        return this.suid == objectStreamClass_1_3_1.suid && compareClassNames(this.name, objectStreamClass_1_3_1.name, '.');
    }

    final void setSuperclass(ObjectStreamClass_1_3_1 objectStreamClass_1_3_1) {
        this.superclass = objectStreamClass_1_3_1;
    }

    final ObjectStreamClass_1_3_1 getSuperclass() {
        return this.superclass;
    }

    final boolean hasWriteObject() {
        return this.hasWriteObjectMethod;
    }

    final boolean isCustomMarshaled() {
        return hasWriteObject() || isExternalizable();
    }

    boolean hasExternalizableBlockDataMode() {
        return this.hasExternalizableBlockData;
    }

    final ObjectStreamClass_1_3_1 localClassDescriptor() {
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

    private static long computeStructuralUID(ObjectStreamClass_1_3_1 objectStreamClass_1_3_1, Class<?> cls) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        long j2 = 0;
        try {
        } catch (IOException e2) {
            j2 = -1;
        } catch (NoSuchAlgorithmException e3) {
            throw new SecurityException(e3.getMessage());
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
        if (objectStreamClass_1_3_1.hasWriteObject()) {
            dataOutputStream.writeInt(2);
        } else {
            dataOutputStream.writeInt(1);
        }
        ObjectStreamField[] fields = objectStreamClass_1_3_1.getFields();
        int i2 = 0;
        for (ObjectStreamField objectStreamField : fields) {
            if (objectStreamField.getField() != null) {
                i2++;
            }
        }
        Field[] fieldArr = new Field[i2];
        int i3 = 0;
        for (int i4 = 0; i4 < fields.length; i4++) {
            if (fields[i4].getField() != null) {
                int i5 = i3;
                i3++;
                fieldArr[i5] = fields[i4].getField();
            }
        }
        if (fieldArr.length > 1) {
            Arrays.sort(fieldArr, compareMemberByName);
        }
        for (Field field : fieldArr) {
            field.getModifiers();
            dataOutputStream.writeUTF(field.getName());
            dataOutputStream.writeUTF(getSignature(field.getType()));
        }
        dataOutputStream.flush();
        for (int i6 = 0; i6 < Math.min(8, messageDigest.digest().length); i6++) {
            j2 += (r0[i6] & 255) << (i6 * 8);
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
                string = Constants.HASIDCALL_INDEX_SIG;
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

    private static ObjectStreamClass_1_3_1 findDescriptorFor(Class<?> cls) {
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
            ObjectStreamClass_1_3_1 objectStreamClass_1_3_1 = (ObjectStreamClass_1_3_1) objectStreamClassEntry.get();
            if (objectStreamClass_1_3_1 == null) {
                objectStreamClassEntry3.next = objectStreamClassEntry.next;
            } else {
                if (objectStreamClass_1_3_1.ofClass == cls) {
                    return objectStreamClass_1_3_1;
                }
                objectStreamClassEntry3 = objectStreamClassEntry;
            }
            objectStreamClassEntry = objectStreamClassEntry.next;
        }
        return null;
    }

    private static void insertDescriptorFor(ObjectStreamClass_1_3_1 objectStreamClass_1_3_1) {
        if (findDescriptorFor(objectStreamClass_1_3_1.ofClass) != null) {
            return;
        }
        int iHashCode = (objectStreamClass_1_3_1.ofClass.hashCode() & Integer.MAX_VALUE) % descriptorFor.length;
        ObjectStreamClassEntry objectStreamClassEntry = new ObjectStreamClassEntry(objectStreamClass_1_3_1);
        objectStreamClassEntry.next = descriptorFor[iHashCode];
        descriptorFor[iHashCode] = objectStreamClassEntry;
    }

    private static Field[] getDeclaredFields(final Class cls) {
        return (Field[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.orbutil.ObjectStreamClass_1_3_1.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return cls.getDeclaredFields();
            }
        });
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectStreamClass_1_3_1$ObjectStreamClassEntry.class */
    private static class ObjectStreamClassEntry {
        ObjectStreamClassEntry next;

        /* renamed from: c, reason: collision with root package name */
        private ObjectStreamClass_1_3_1 f11802c;

        ObjectStreamClassEntry(ObjectStreamClass_1_3_1 objectStreamClass_1_3_1) {
            this.f11802c = objectStreamClass_1_3_1;
        }

        public Object get() {
            return this.f11802c;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectStreamClass_1_3_1$CompareClassByName.class */
    private static class CompareClassByName implements Comparator {
        private CompareClassByName() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            return ((Class) obj).getName().compareTo(((Class) obj2).getName());
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectStreamClass_1_3_1$CompareMemberByName.class */
    private static class CompareMemberByName implements Comparator {
        private CompareMemberByName() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            String name = ((Member) obj).getName();
            String name2 = ((Member) obj2).getName();
            if (obj instanceof Method) {
                name = name + ObjectStreamClass_1_3_1.getSignature((Method) obj);
                name2 = name2 + ObjectStreamClass_1_3_1.getSignature((Method) obj2);
            } else if (obj instanceof Constructor) {
                name = name + ObjectStreamClass_1_3_1.getSignature((Constructor) obj);
                name2 = name2 + ObjectStreamClass_1_3_1.getSignature((Constructor) obj2);
            }
            return name.compareTo(name2);
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectStreamClass_1_3_1$MethodSignature.class */
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
                this.signature = ObjectStreamClass_1_3_1.getSignature((Constructor) member);
            } else {
                this.signature = ObjectStreamClass_1_3_1.getSignature((Method) member);
            }
        }
    }
}
