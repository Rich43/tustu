package jdk.internal.org.objectweb.asm;

import com.sun.org.apache.xml.internal.security.utils.Constants;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/ClassWriter.class */
public class ClassWriter extends ClassVisitor {
    public static final int COMPUTE_MAXS = 1;
    public static final int COMPUTE_FRAMES = 2;
    static final int ACC_SYNTHETIC_ATTRIBUTE = 262144;
    static final int TO_ACC_SYNTHETIC = 64;
    static final int NOARG_INSN = 0;
    static final int SBYTE_INSN = 1;
    static final int SHORT_INSN = 2;
    static final int VAR_INSN = 3;
    static final int IMPLVAR_INSN = 4;
    static final int TYPE_INSN = 5;
    static final int FIELDORMETH_INSN = 6;
    static final int ITFMETH_INSN = 7;
    static final int INDYMETH_INSN = 8;
    static final int LABEL_INSN = 9;
    static final int LABELW_INSN = 10;
    static final int LDC_INSN = 11;
    static final int LDCW_INSN = 12;
    static final int IINC_INSN = 13;
    static final int TABL_INSN = 14;
    static final int LOOK_INSN = 15;
    static final int MANA_INSN = 16;
    static final int WIDE_INSN = 17;
    static final byte[] TYPE;
    static final int CLASS = 7;
    static final int FIELD = 9;
    static final int METH = 10;
    static final int IMETH = 11;
    static final int STR = 8;
    static final int INT = 3;
    static final int FLOAT = 4;
    static final int LONG = 5;
    static final int DOUBLE = 6;
    static final int NAME_TYPE = 12;
    static final int UTF8 = 1;
    static final int MTYPE = 16;
    static final int HANDLE = 15;
    static final int INDY = 18;
    static final int HANDLE_BASE = 20;
    static final int TYPE_NORMAL = 30;
    static final int TYPE_UNINIT = 31;
    static final int TYPE_MERGED = 32;
    static final int BSM = 33;
    ClassReader cr;
    int version;
    int index;
    final ByteVector pool;
    Item[] items;
    int threshold;
    final Item key;
    final Item key2;
    final Item key3;
    final Item key4;
    Item[] typeTable;
    private short typeCount;
    private int access;
    private int name;
    String thisName;
    private int signature;
    private int superName;
    private int interfaceCount;
    private int[] interfaces;
    private int sourceFile;
    private ByteVector sourceDebug;
    private int enclosingMethodOwner;
    private int enclosingMethod;
    private AnnotationWriter anns;
    private AnnotationWriter ianns;
    private AnnotationWriter tanns;
    private AnnotationWriter itanns;
    private Attribute attrs;
    private int innerClassesCount;
    private ByteVector innerClasses;
    int bootstrapMethodsCount;
    ByteVector bootstrapMethods;
    FieldWriter firstField;
    FieldWriter lastField;
    MethodWriter firstMethod;
    MethodWriter lastMethod;
    private boolean computeMaxs;
    private boolean computeFrames;
    boolean invalidFrames;

    static {
        byte[] bArr = new byte[220];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) ("AAAAAAAAAAAAAAAABCLMMDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJDOPAAAAAAGGGGGGGHIFBFAAFFAARQJJKKJJJJJJJJJJJJJJJJJJ".charAt(i2) - 'A');
        }
        TYPE = bArr;
    }

    public ClassWriter(int i2) {
        super(Opcodes.ASM5);
        this.index = 1;
        this.pool = new ByteVector();
        this.items = new Item[256];
        this.threshold = (int) (0.75d * this.items.length);
        this.key = new Item();
        this.key2 = new Item();
        this.key3 = new Item();
        this.key4 = new Item();
        this.computeMaxs = (i2 & 1) != 0;
        this.computeFrames = (i2 & 2) != 0;
    }

    public ClassWriter(ClassReader classReader, int i2) {
        this(i2);
        classReader.copyPool(this);
        this.cr = classReader;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        this.version = i2;
        this.access = i3;
        this.name = newClass(str);
        this.thisName = str;
        if (str2 != null) {
            this.signature = newUTF8(str2);
        }
        this.superName = str3 == null ? 0 : newClass(str3);
        if (strArr != null && strArr.length > 0) {
            this.interfaceCount = strArr.length;
            this.interfaces = new int[this.interfaceCount];
            for (int i4 = 0; i4 < this.interfaceCount; i4++) {
                this.interfaces[i4] = newClass(strArr[i4]);
            }
        }
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final void visitSource(String str, String str2) {
        if (str != null) {
            this.sourceFile = newUTF8(str);
        }
        if (str2 != null) {
            this.sourceDebug = new ByteVector().encodeUTF8(str2, 0, Integer.MAX_VALUE);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final void visitOuterClass(String str, String str2, String str3) {
        this.enclosingMethodOwner = newClass(str);
        if (str2 != null && str3 != null) {
            this.enclosingMethod = newNameType(str2, str3);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final AnnotationVisitor visitAnnotation(String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, 2);
        if (z2) {
            annotationWriter.next = this.anns;
            this.anns = annotationWriter;
        } else {
            annotationWriter.next = this.ianns;
            this.ianns = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget(i2, typePath, byteVector);
        byteVector.putShort(newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this, true, byteVector, byteVector, byteVector.length - 2);
        if (z2) {
            annotationWriter.next = this.tanns;
            this.tanns = annotationWriter;
        } else {
            annotationWriter.next = this.itanns;
            this.itanns = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final void visitAttribute(Attribute attribute) {
        attribute.next = this.attrs;
        this.attrs = attribute;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final void visitInnerClass(String str, String str2, String str3, int i2) {
        if (this.innerClasses == null) {
            this.innerClasses = new ByteVector();
        }
        Item itemNewClassItem = newClassItem(str);
        if (itemNewClassItem.intVal == 0) {
            this.innerClassesCount++;
            this.innerClasses.putShort(itemNewClassItem.index);
            this.innerClasses.putShort(str2 == null ? 0 : newClass(str2));
            this.innerClasses.putShort(str3 == null ? 0 : newUTF8(str3));
            this.innerClasses.putShort(i2);
            itemNewClassItem.intVal = this.innerClassesCount;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final FieldVisitor visitField(int i2, String str, String str2, String str3, Object obj) {
        return new FieldWriter(this, i2, str, str2, str3, obj);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        return new MethodWriter(this, i2, str, str2, str3, strArr, this.computeMaxs, this.computeFrames);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public final void visitEnd() {
    }

    public byte[] toByteArray() {
        if (this.index > 65535) {
            throw new RuntimeException("Class file too large!");
        }
        int size = 24 + (2 * this.interfaceCount);
        int i2 = 0;
        FieldWriter fieldWriter = this.firstField;
        while (true) {
            FieldWriter fieldWriter2 = fieldWriter;
            if (fieldWriter2 == null) {
                break;
            }
            i2++;
            size += fieldWriter2.getSize();
            fieldWriter = (FieldWriter) fieldWriter2.fv;
        }
        int i3 = 0;
        MethodWriter methodWriter = this.firstMethod;
        while (true) {
            MethodWriter methodWriter2 = methodWriter;
            if (methodWriter2 == null) {
                break;
            }
            i3++;
            size += methodWriter2.getSize();
            methodWriter = (MethodWriter) methodWriter2.mv;
        }
        int count = 0;
        if (this.bootstrapMethods != null) {
            count = 0 + 1;
            size += 8 + this.bootstrapMethods.length;
            newUTF8("BootstrapMethods");
        }
        if (this.signature != 0) {
            count++;
            size += 8;
            newUTF8(Constants._TAG_SIGNATURE);
        }
        if (this.sourceFile != 0) {
            count++;
            size += 8;
            newUTF8("SourceFile");
        }
        if (this.sourceDebug != null) {
            count++;
            size += this.sourceDebug.length + 6;
            newUTF8("SourceDebugExtension");
        }
        if (this.enclosingMethodOwner != 0) {
            count++;
            size += 10;
            newUTF8("EnclosingMethod");
        }
        if ((this.access & 131072) != 0) {
            count++;
            size += 6;
            newUTF8("Deprecated");
        }
        if ((this.access & 4096) != 0 && ((this.version & 65535) < 49 || (this.access & 262144) != 0)) {
            count++;
            size += 6;
            newUTF8("Synthetic");
        }
        if (this.innerClasses != null) {
            count++;
            size += 8 + this.innerClasses.length;
            newUTF8("InnerClasses");
        }
        if (this.anns != null) {
            count++;
            size += 8 + this.anns.getSize();
            newUTF8("RuntimeVisibleAnnotations");
        }
        if (this.ianns != null) {
            count++;
            size += 8 + this.ianns.getSize();
            newUTF8("RuntimeInvisibleAnnotations");
        }
        if (this.tanns != null) {
            count++;
            size += 8 + this.tanns.getSize();
            newUTF8("RuntimeVisibleTypeAnnotations");
        }
        if (this.itanns != null) {
            count++;
            size += 8 + this.itanns.getSize();
            newUTF8("RuntimeInvisibleTypeAnnotations");
        }
        if (this.attrs != null) {
            count += this.attrs.getCount();
            size += this.attrs.getSize(this, null, 0, -1, -1);
        }
        ByteVector byteVector = new ByteVector(size + this.pool.length);
        byteVector.putInt(com.sun.java.util.jar.pack.Constants.JAVA_MAGIC).putInt(this.version);
        byteVector.putShort(this.index).putByteArray(this.pool.data, 0, this.pool.length);
        byteVector.putShort(this.access & ((393216 | ((this.access & 262144) / 64)) ^ (-1))).putShort(this.name).putShort(this.superName);
        byteVector.putShort(this.interfaceCount);
        for (int i4 = 0; i4 < this.interfaceCount; i4++) {
            byteVector.putShort(this.interfaces[i4]);
        }
        byteVector.putShort(i2);
        FieldWriter fieldWriter3 = this.firstField;
        while (true) {
            FieldWriter fieldWriter4 = fieldWriter3;
            if (fieldWriter4 == null) {
                break;
            }
            fieldWriter4.put(byteVector);
            fieldWriter3 = (FieldWriter) fieldWriter4.fv;
        }
        byteVector.putShort(i3);
        MethodWriter methodWriter3 = this.firstMethod;
        while (true) {
            MethodWriter methodWriter4 = methodWriter3;
            if (methodWriter4 == null) {
                break;
            }
            methodWriter4.put(byteVector);
            methodWriter3 = (MethodWriter) methodWriter4.mv;
        }
        byteVector.putShort(count);
        if (this.bootstrapMethods != null) {
            byteVector.putShort(newUTF8("BootstrapMethods"));
            byteVector.putInt(this.bootstrapMethods.length + 2).putShort(this.bootstrapMethodsCount);
            byteVector.putByteArray(this.bootstrapMethods.data, 0, this.bootstrapMethods.length);
        }
        if (this.signature != 0) {
            byteVector.putShort(newUTF8(Constants._TAG_SIGNATURE)).putInt(2).putShort(this.signature);
        }
        if (this.sourceFile != 0) {
            byteVector.putShort(newUTF8("SourceFile")).putInt(2).putShort(this.sourceFile);
        }
        if (this.sourceDebug != null) {
            int i5 = this.sourceDebug.length;
            byteVector.putShort(newUTF8("SourceDebugExtension")).putInt(i5);
            byteVector.putByteArray(this.sourceDebug.data, 0, i5);
        }
        if (this.enclosingMethodOwner != 0) {
            byteVector.putShort(newUTF8("EnclosingMethod")).putInt(4);
            byteVector.putShort(this.enclosingMethodOwner).putShort(this.enclosingMethod);
        }
        if ((this.access & 131072) != 0) {
            byteVector.putShort(newUTF8("Deprecated")).putInt(0);
        }
        if ((this.access & 4096) != 0 && ((this.version & 65535) < 49 || (this.access & 262144) != 0)) {
            byteVector.putShort(newUTF8("Synthetic")).putInt(0);
        }
        if (this.innerClasses != null) {
            byteVector.putShort(newUTF8("InnerClasses"));
            byteVector.putInt(this.innerClasses.length + 2).putShort(this.innerClassesCount);
            byteVector.putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
        }
        if (this.anns != null) {
            byteVector.putShort(newUTF8("RuntimeVisibleAnnotations"));
            this.anns.put(byteVector);
        }
        if (this.ianns != null) {
            byteVector.putShort(newUTF8("RuntimeInvisibleAnnotations"));
            this.ianns.put(byteVector);
        }
        if (this.tanns != null) {
            byteVector.putShort(newUTF8("RuntimeVisibleTypeAnnotations"));
            this.tanns.put(byteVector);
        }
        if (this.itanns != null) {
            byteVector.putShort(newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.itanns.put(byteVector);
        }
        if (this.attrs != null) {
            this.attrs.put(this, null, 0, -1, -1, byteVector);
        }
        if (this.invalidFrames) {
            this.anns = null;
            this.ianns = null;
            this.attrs = null;
            this.innerClassesCount = 0;
            this.innerClasses = null;
            this.bootstrapMethodsCount = 0;
            this.bootstrapMethods = null;
            this.firstField = null;
            this.lastField = null;
            this.firstMethod = null;
            this.lastMethod = null;
            this.computeMaxs = false;
            this.computeFrames = true;
            this.invalidFrames = false;
            new ClassReader(byteVector.data).accept(this, 4);
            return toByteArray();
        }
        return byteVector.data;
    }

    Item newConstItem(Object obj) {
        if (obj instanceof Integer) {
            return newInteger(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return newInteger(((Byte) obj).intValue());
        }
        if (obj instanceof Character) {
            return newInteger(((Character) obj).charValue());
        }
        if (obj instanceof Short) {
            return newInteger(((Short) obj).intValue());
        }
        if (obj instanceof Boolean) {
            return newInteger(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        if (obj instanceof Float) {
            return newFloat(((Float) obj).floatValue());
        }
        if (obj instanceof Long) {
            return newLong(((Long) obj).longValue());
        }
        if (obj instanceof Double) {
            return newDouble(((Double) obj).doubleValue());
        }
        if (obj instanceof String) {
            return newString((String) obj);
        }
        if (obj instanceof Type) {
            Type type = (Type) obj;
            int sort = type.getSort();
            if (sort == 10) {
                return newClassItem(type.getInternalName());
            }
            if (sort == 11) {
                return newMethodTypeItem(type.getDescriptor());
            }
            return newClassItem(type.getDescriptor());
        }
        if (obj instanceof Handle) {
            Handle handle = (Handle) obj;
            return newHandleItem(handle.tag, handle.owner, handle.name, handle.desc);
        }
        throw new IllegalArgumentException("value " + obj);
    }

    public int newConst(Object obj) {
        return newConstItem(obj).index;
    }

    public int newUTF8(String str) {
        this.key.set(1, str, null, null);
        Item item = get(this.key);
        if (item == null) {
            this.pool.putByte(1).putUTF8(str);
            int i2 = this.index;
            this.index = i2 + 1;
            item = new Item(i2, this.key);
            put(item);
        }
        return item.index;
    }

    Item newClassItem(String str) {
        this.key2.set(7, str, null, null);
        Item item = get(this.key2);
        if (item == null) {
            this.pool.put12(7, newUTF8(str));
            int i2 = this.index;
            this.index = i2 + 1;
            item = new Item(i2, this.key2);
            put(item);
        }
        return item;
    }

    public int newClass(String str) {
        return newClassItem(str).index;
    }

    Item newMethodTypeItem(String str) {
        this.key2.set(16, str, null, null);
        Item item = get(this.key2);
        if (item == null) {
            this.pool.put12(16, newUTF8(str));
            int i2 = this.index;
            this.index = i2 + 1;
            item = new Item(i2, this.key2);
            put(item);
        }
        return item;
    }

    public int newMethodType(String str) {
        return newMethodTypeItem(str).index;
    }

    Item newHandleItem(int i2, String str, String str2, String str3) {
        this.key4.set(20 + i2, str, str2, str3);
        Item item = get(this.key4);
        if (item == null) {
            if (i2 <= 4) {
                put112(15, i2, newField(str, str2, str3));
            } else {
                put112(15, i2, newMethod(str, str2, str3, i2 == 9));
            }
            int i3 = this.index;
            this.index = i3 + 1;
            item = new Item(i3, this.key4);
            put(item);
        }
        return item;
    }

    public int newHandle(int i2, String str, String str2, String str3) {
        return newHandleItem(i2, str, str2, str3).index;
    }

    Item newInvokeDynamicItem(String str, String str2, Handle handle, Object... objArr) {
        Item item;
        int i2;
        ByteVector byteVector = this.bootstrapMethods;
        if (byteVector == null) {
            ByteVector byteVector2 = new ByteVector();
            this.bootstrapMethods = byteVector2;
            byteVector = byteVector2;
        }
        int i3 = byteVector.length;
        int iHashCode = handle.hashCode();
        byteVector.putShort(newHandle(handle.tag, handle.owner, handle.name, handle.desc));
        int length = objArr.length;
        byteVector.putShort(length);
        for (Object obj : objArr) {
            iHashCode ^= obj.hashCode();
            byteVector.putShort(newConst(obj));
        }
        byte[] bArr = byteVector.data;
        int i4 = (2 + length) << 1;
        int i5 = iHashCode & Integer.MAX_VALUE;
        Item item2 = this.items[i5 % this.items.length];
        loop1: while (true) {
            item = item2;
            if (item == null) {
                break;
            }
            if (item.type != 33 || item.hashCode != i5) {
                item2 = item.next;
            } else {
                int i6 = item.intVal;
                for (int i7 = 0; i7 < i4; i7++) {
                    if (bArr[i3 + i7] != bArr[i6 + i7]) {
                        item2 = item.next;
                    }
                }
                break loop1;
            }
        }
        if (item != null) {
            i2 = item.index;
            byteVector.length = i3;
        } else {
            int i8 = this.bootstrapMethodsCount;
            this.bootstrapMethodsCount = i8 + 1;
            i2 = i8;
            Item item3 = new Item(i2);
            item3.set(i3, i5);
            put(item3);
        }
        this.key3.set(str, str2, i2);
        Item item4 = get(this.key3);
        if (item4 == null) {
            put122(18, i2, newNameType(str, str2));
            int i9 = this.index;
            this.index = i9 + 1;
            item4 = new Item(i9, this.key3);
            put(item4);
        }
        return item4;
    }

    public int newInvokeDynamic(String str, String str2, Handle handle, Object... objArr) {
        return newInvokeDynamicItem(str, str2, handle, objArr).index;
    }

    Item newFieldItem(String str, String str2, String str3) {
        this.key3.set(9, str, str2, str3);
        Item item = get(this.key3);
        if (item == null) {
            put122(9, newClass(str), newNameType(str2, str3));
            int i2 = this.index;
            this.index = i2 + 1;
            item = new Item(i2, this.key3);
            put(item);
        }
        return item;
    }

    public int newField(String str, String str2, String str3) {
        return newFieldItem(str, str2, str3).index;
    }

    Item newMethodItem(String str, String str2, String str3, boolean z2) {
        int i2 = z2 ? 11 : 10;
        this.key3.set(i2, str, str2, str3);
        Item item = get(this.key3);
        if (item == null) {
            put122(i2, newClass(str), newNameType(str2, str3));
            int i3 = this.index;
            this.index = i3 + 1;
            item = new Item(i3, this.key3);
            put(item);
        }
        return item;
    }

    public int newMethod(String str, String str2, String str3, boolean z2) {
        return newMethodItem(str, str2, str3, z2).index;
    }

    Item newInteger(int i2) {
        this.key.set(i2);
        Item item = get(this.key);
        if (item == null) {
            this.pool.putByte(3).putInt(i2);
            int i3 = this.index;
            this.index = i3 + 1;
            item = new Item(i3, this.key);
            put(item);
        }
        return item;
    }

    Item newFloat(float f2) {
        this.key.set(f2);
        Item item = get(this.key);
        if (item == null) {
            this.pool.putByte(4).putInt(this.key.intVal);
            int i2 = this.index;
            this.index = i2 + 1;
            item = new Item(i2, this.key);
            put(item);
        }
        return item;
    }

    Item newLong(long j2) {
        this.key.set(j2);
        Item item = get(this.key);
        if (item == null) {
            this.pool.putByte(5).putLong(j2);
            item = new Item(this.index, this.key);
            this.index += 2;
            put(item);
        }
        return item;
    }

    Item newDouble(double d2) {
        this.key.set(d2);
        Item item = get(this.key);
        if (item == null) {
            this.pool.putByte(6).putLong(this.key.longVal);
            item = new Item(this.index, this.key);
            this.index += 2;
            put(item);
        }
        return item;
    }

    private Item newString(String str) {
        this.key2.set(8, str, null, null);
        Item item = get(this.key2);
        if (item == null) {
            this.pool.put12(8, newUTF8(str));
            int i2 = this.index;
            this.index = i2 + 1;
            item = new Item(i2, this.key2);
            put(item);
        }
        return item;
    }

    public int newNameType(String str, String str2) {
        return newNameTypeItem(str, str2).index;
    }

    Item newNameTypeItem(String str, String str2) {
        this.key2.set(12, str, str2, null);
        Item item = get(this.key2);
        if (item == null) {
            put122(12, newUTF8(str), newUTF8(str2));
            int i2 = this.index;
            this.index = i2 + 1;
            item = new Item(i2, this.key2);
            put(item);
        }
        return item;
    }

    int addType(String str) {
        this.key.set(30, str, null, null);
        Item itemAddType = get(this.key);
        if (itemAddType == null) {
            itemAddType = addType(this.key);
        }
        return itemAddType.index;
    }

    int addUninitializedType(String str, int i2) {
        this.key.type = 31;
        this.key.intVal = i2;
        this.key.strVal1 = str;
        this.key.hashCode = Integer.MAX_VALUE & (31 + str.hashCode() + i2);
        Item itemAddType = get(this.key);
        if (itemAddType == null) {
            itemAddType = addType(this.key);
        }
        return itemAddType.index;
    }

    private Item addType(Item item) {
        this.typeCount = (short) (this.typeCount + 1);
        Item item2 = new Item(this.typeCount, this.key);
        put(item2);
        if (this.typeTable == null) {
            this.typeTable = new Item[16];
        }
        if (this.typeCount == this.typeTable.length) {
            Item[] itemArr = new Item[2 * this.typeTable.length];
            System.arraycopy(this.typeTable, 0, itemArr, 0, this.typeTable.length);
            this.typeTable = itemArr;
        }
        this.typeTable[this.typeCount] = item2;
        return item2;
    }

    int getMergedType(int i2, int i3) {
        this.key2.type = 32;
        this.key2.longVal = i2 | (i3 << 32);
        this.key2.hashCode = Integer.MAX_VALUE & (32 + i2 + i3);
        Item item = get(this.key2);
        if (item == null) {
            this.key2.intVal = addType(getCommonSuperClass(this.typeTable[i2].strVal1, this.typeTable[i3].strVal1));
            item = new Item(0, this.key2);
            put(item);
        }
        return item.intVal;
    }

    protected String getCommonSuperClass(String str, String str2) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            Class<?> cls = Class.forName(str.replace('/', '.'), false, classLoader);
            Class<?> cls2 = Class.forName(str2.replace('/', '.'), false, classLoader);
            if (cls.isAssignableFrom(cls2)) {
                return str;
            }
            if (cls2.isAssignableFrom(cls)) {
                return str2;
            }
            if (cls.isInterface() || cls2.isInterface()) {
                return "java/lang/Object";
            }
            do {
                cls = cls.getSuperclass();
            } while (!cls.isAssignableFrom(cls2));
            return cls.getName().replace('.', '/');
        } catch (Exception e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    private Item get(Item item) {
        Item item2;
        Item item3 = this.items[item.hashCode % this.items.length];
        while (true) {
            item2 = item3;
            if (item2 == null || (item2.type == item.type && item.isEqualTo(item2))) {
                break;
            }
            item3 = item2.next;
        }
        return item2;
    }

    private void put(Item item) {
        if (this.index + this.typeCount > this.threshold) {
            int length = this.items.length;
            int i2 = (length * 2) + 1;
            Item[] itemArr = new Item[i2];
            for (int i3 = length - 1; i3 >= 0; i3--) {
                Item item2 = this.items[i3];
                while (true) {
                    Item item3 = item2;
                    if (item3 != null) {
                        int length2 = item3.hashCode % itemArr.length;
                        Item item4 = item3.next;
                        item3.next = itemArr[length2];
                        itemArr[length2] = item3;
                        item2 = item4;
                    }
                }
            }
            this.items = itemArr;
            this.threshold = (int) (i2 * 0.75d);
        }
        int length3 = item.hashCode % this.items.length;
        item.next = this.items[length3];
        this.items[length3] = item;
    }

    private void put122(int i2, int i3, int i4) {
        this.pool.put12(i2, i3).putShort(i4);
    }

    private void put112(int i2, int i3, int i4) {
        this.pool.put11(i2, i3).putShort(i4);
    }
}
