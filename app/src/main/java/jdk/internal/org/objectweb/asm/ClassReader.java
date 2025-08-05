package jdk.internal.org.objectweb.asm;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/ClassReader.class */
public class ClassReader {
    static final boolean SIGNATURES = true;
    static final boolean ANNOTATIONS = true;
    static final boolean FRAMES = true;
    static final boolean WRITER = true;
    static final boolean RESIZE = true;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    public static final int EXPAND_FRAMES = 8;

    /* renamed from: b, reason: collision with root package name */
    public final byte[] f12859b;
    private final int[] items;
    private final String[] strings;
    private final int maxStringLength;
    public final int header;

    public ClassReader(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ClassReader(byte[] bArr, int i2, int i3) {
        int unsignedShort;
        this.f12859b = bArr;
        if (readShort(i2 + 6) > 52) {
            throw new IllegalArgumentException();
        }
        this.items = new int[readUnsignedShort(i2 + 8)];
        int length = this.items.length;
        this.strings = new String[length];
        int i4 = 0;
        int i5 = i2 + 10;
        int i6 = 1;
        while (i6 < length) {
            this.items[i6] = i5 + 1;
            switch (bArr[i5]) {
                case 1:
                    unsignedShort = 3 + readUnsignedShort(i5 + 1);
                    if (unsignedShort <= i4) {
                        break;
                    } else {
                        i4 = unsignedShort;
                        break;
                    }
                case 2:
                case 7:
                case 8:
                case 13:
                case 14:
                case 16:
                case 17:
                default:
                    unsignedShort = 3;
                    break;
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                case 18:
                    unsignedShort = 5;
                    break;
                case 5:
                case 6:
                    unsignedShort = 9;
                    i6++;
                    break;
                case 15:
                    unsignedShort = 4;
                    break;
            }
            i5 += unsignedShort;
            i6++;
        }
        this.maxStringLength = i4;
        this.header = i5;
    }

    public int getAccess() {
        return readUnsignedShort(this.header);
    }

    public String getClassName() {
        return readClass(this.header + 2, new char[this.maxStringLength]);
    }

    public String getSuperName() {
        return readClass(this.header + 4, new char[this.maxStringLength]);
    }

    public String[] getInterfaces() {
        int i2 = this.header + 6;
        int unsignedShort = readUnsignedShort(i2);
        String[] strArr = new String[unsignedShort];
        if (unsignedShort > 0) {
            char[] cArr = new char[this.maxStringLength];
            for (int i3 = 0; i3 < unsignedShort; i3++) {
                i2 += 2;
                strArr[i3] = readClass(i2, cArr);
            }
        }
        return strArr;
    }

    void copyPool(ClassWriter classWriter) {
        char[] cArr = new char[this.maxStringLength];
        int length = this.items.length;
        Item[] itemArr = new Item[length];
        int i2 = 1;
        while (i2 < length) {
            int i3 = this.items[i2];
            byte b2 = this.f12859b[i3 - 1];
            Item item = new Item(i2);
            switch (b2) {
                case 1:
                    String str = this.strings[i2];
                    if (str == null) {
                        int i4 = this.items[i2];
                        String utf = readUTF(i4 + 2, readUnsignedShort(i4), cArr);
                        this.strings[i2] = utf;
                        str = utf;
                    }
                    item.set(b2, str, null, null);
                    break;
                case 2:
                case 7:
                case 8:
                case 13:
                case 14:
                case 16:
                case 17:
                default:
                    item.set(b2, readUTF8(i3, cArr), null, null);
                    break;
                case 3:
                    item.set(readInt(i3));
                    break;
                case 4:
                    item.set(Float.intBitsToFloat(readInt(i3)));
                    break;
                case 5:
                    item.set(readLong(i3));
                    i2++;
                    break;
                case 6:
                    item.set(Double.longBitsToDouble(readLong(i3)));
                    i2++;
                    break;
                case 9:
                case 10:
                case 11:
                    int i5 = this.items[readUnsignedShort(i3 + 2)];
                    item.set(b2, readClass(i3, cArr), readUTF8(i5, cArr), readUTF8(i5 + 2, cArr));
                    break;
                case 12:
                    item.set(b2, readUTF8(i3, cArr), readUTF8(i3 + 2, cArr), null);
                    break;
                case 15:
                    int i6 = this.items[readUnsignedShort(i3 + 1)];
                    int i7 = this.items[readUnsignedShort(i6 + 2)];
                    item.set(20 + readByte(i3), readClass(i6, cArr), readUTF8(i7, cArr), readUTF8(i7 + 2, cArr));
                    break;
                case 18:
                    if (classWriter.bootstrapMethods == null) {
                        copyBootstrapMethods(classWriter, itemArr, cArr);
                    }
                    int i8 = this.items[readUnsignedShort(i3 + 2)];
                    item.set(readUTF8(i8, cArr), readUTF8(i8 + 2, cArr), readUnsignedShort(i3));
                    break;
            }
            int length2 = item.hashCode % itemArr.length;
            item.next = itemArr[length2];
            itemArr[length2] = item;
            i2++;
        }
        int i9 = this.items[1] - 1;
        classWriter.pool.putByteArray(this.f12859b, i9, this.header - i9);
        classWriter.items = itemArr;
        classWriter.threshold = (int) (0.75d * length);
        classWriter.index = length;
    }

    private void copyBootstrapMethods(ClassWriter classWriter, Item[] itemArr, char[] cArr) {
        int attributes = getAttributes();
        boolean z2 = false;
        int unsignedShort = readUnsignedShort(attributes);
        while (true) {
            if (unsignedShort <= 0) {
                break;
            }
            if ("BootstrapMethods".equals(readUTF8(attributes + 2, cArr))) {
                z2 = true;
                break;
            } else {
                attributes += 6 + readInt(attributes + 4);
                unsignedShort--;
            }
        }
        if (!z2) {
            return;
        }
        int unsignedShort2 = readUnsignedShort(attributes + 8);
        int i2 = attributes + 10;
        for (int i3 = 0; i3 < unsignedShort2; i3++) {
            int i4 = (i2 - attributes) - 10;
            int iHashCode = readConst(readUnsignedShort(i2), cArr).hashCode();
            for (int unsignedShort3 = readUnsignedShort(i2 + 2); unsignedShort3 > 0; unsignedShort3--) {
                iHashCode ^= readConst(readUnsignedShort(i2 + 4), cArr).hashCode();
                i2 += 2;
            }
            i2 += 4;
            Item item = new Item(i3);
            item.set(i4, iHashCode & Integer.MAX_VALUE);
            int length = item.hashCode % itemArr.length;
            item.next = itemArr[length];
            itemArr[length] = item;
        }
        int i5 = readInt(attributes + 4);
        ByteVector byteVector = new ByteVector(i5 + 62);
        byteVector.putByteArray(this.f12859b, attributes + 10, i5 - 2);
        classWriter.bootstrapMethodsCount = unsignedShort2;
        classWriter.bootstrapMethods = byteVector;
    }

    public ClassReader(InputStream inputStream) throws IOException {
        this(readClass(inputStream, false));
    }

    public ClassReader(String str) throws IOException {
        this(readClass(ClassLoader.getSystemResourceAsStream(str.replace('.', '/') + ".class"), true));
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002c, code lost:
    
        if (r9 >= r8.length) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x002f, code lost:
    
        r0 = new byte[r9];
        java.lang.System.arraycopy(r8, 0, r0, 0, r9);
        r8 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004d, code lost:
    
        return r8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static byte[] readClass(java.io.InputStream r6, boolean r7) throws java.io.IOException {
        /*
            r0 = r6
            if (r0 != 0) goto Le
            java.io.IOException r0 = new java.io.IOException
            r1 = r0
            java.lang.String r2 = "Class not found"
            r1.<init>(r2)
            throw r0
        Le:
            r0 = r6
            int r0 = r0.available()     // Catch: java.lang.Throwable -> L95
            byte[] r0 = new byte[r0]     // Catch: java.lang.Throwable -> L95
            r8 = r0
            r0 = 0
            r9 = r0
        L17:
            r0 = r6
            r1 = r8
            r2 = r9
            r3 = r8
            int r3 = r3.length     // Catch: java.lang.Throwable -> L95
            r4 = r9
            int r3 = r3 - r4
            int r0 = r0.read(r1, r2, r3)     // Catch: java.lang.Throwable -> L95
            r10 = r0
            r0 = r10
            r1 = -1
            if (r0 != r1) goto L4e
            r0 = r9
            r1 = r8
            int r1 = r1.length     // Catch: java.lang.Throwable -> L95
            if (r0 >= r1) goto L40
            r0 = r9
            byte[] r0 = new byte[r0]     // Catch: java.lang.Throwable -> L95
            r11 = r0
            r0 = r8
            r1 = 0
            r2 = r11
            r3 = 0
            r4 = r9
            java.lang.System.arraycopy(r0, r1, r2, r3, r4)     // Catch: java.lang.Throwable -> L95
            r0 = r11
            r8 = r0
        L40:
            r0 = r8
            r11 = r0
            r0 = r7
            if (r0 == 0) goto L4b
            r0 = r6
            r0.close()
        L4b:
            r0 = r11
            return r0
        L4e:
            r0 = r9
            r1 = r10
            int r0 = r0 + r1
            r9 = r0
            r0 = r9
            r1 = r8
            int r1 = r1.length     // Catch: java.lang.Throwable -> L95
            if (r0 != r1) goto L92
            r0 = r6
            int r0 = r0.read()     // Catch: java.lang.Throwable -> L95
            r11 = r0
            r0 = r11
            if (r0 >= 0) goto L72
            r0 = r8
            r12 = r0
            r0 = r7
            if (r0 == 0) goto L6f
            r0 = r6
            r0.close()
        L6f:
            r0 = r12
            return r0
        L72:
            r0 = r8
            int r0 = r0.length     // Catch: java.lang.Throwable -> L95
            r1 = 1000(0x3e8, float:1.401E-42)
            int r0 = r0 + r1
            byte[] r0 = new byte[r0]     // Catch: java.lang.Throwable -> L95
            r12 = r0
            r0 = r8
            r1 = 0
            r2 = r12
            r3 = 0
            r4 = r9
            java.lang.System.arraycopy(r0, r1, r2, r3, r4)     // Catch: java.lang.Throwable -> L95
            r0 = r12
            r1 = r9
            int r9 = r9 + 1
            r2 = r11
            byte r2 = (byte) r2     // Catch: java.lang.Throwable -> L95
            r0[r1] = r2     // Catch: java.lang.Throwable -> L95
            r0 = r12
            r8 = r0
        L92:
            goto L17
        L95:
            r13 = move-exception
            r0 = r7
            if (r0 == 0) goto L9f
            r0 = r6
            r0.close()
        L9f:
            r0 = r13
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.org.objectweb.asm.ClassReader.readClass(java.io.InputStream, boolean):byte[]");
    }

    public void accept(ClassVisitor classVisitor, int i2) {
        accept(classVisitor, new Attribute[0], i2);
    }

    public void accept(ClassVisitor classVisitor, Attribute[] attributeArr, int i2) {
        int i3 = this.header;
        char[] cArr = new char[this.maxStringLength];
        Context context = new Context();
        context.attrs = attributeArr;
        context.flags = i2;
        context.buffer = cArr;
        int unsignedShort = readUnsignedShort(i3);
        String str = readClass(i3 + 2, cArr);
        String str2 = readClass(i3 + 4, cArr);
        String[] strArr = new String[readUnsignedShort(i3 + 6)];
        int i4 = i3 + 8;
        for (int i5 = 0; i5 < strArr.length; i5++) {
            strArr[i5] = readClass(i4, cArr);
            i4 += 2;
        }
        String utf8 = null;
        String utf82 = null;
        String utf = null;
        String str3 = null;
        String utf83 = null;
        String utf84 = null;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        Attribute attribute = null;
        int attributes = getAttributes();
        for (int unsignedShort2 = readUnsignedShort(attributes); unsignedShort2 > 0; unsignedShort2--) {
            String utf85 = readUTF8(attributes + 2, cArr);
            if ("SourceFile".equals(utf85)) {
                utf82 = readUTF8(attributes + 8, cArr);
            } else if ("InnerClasses".equals(utf85)) {
                i10 = attributes + 8;
            } else if ("EnclosingMethod".equals(utf85)) {
                str3 = readClass(attributes + 8, cArr);
                int unsignedShort3 = readUnsignedShort(attributes + 10);
                if (unsignedShort3 != 0) {
                    utf83 = readUTF8(this.items[unsignedShort3], cArr);
                    utf84 = readUTF8(this.items[unsignedShort3] + 2, cArr);
                }
            } else if (Constants._TAG_SIGNATURE.equals(utf85)) {
                utf8 = readUTF8(attributes + 8, cArr);
            } else if ("RuntimeVisibleAnnotations".equals(utf85)) {
                i6 = attributes + 8;
            } else if ("RuntimeVisibleTypeAnnotations".equals(utf85)) {
                i8 = attributes + 8;
            } else if ("Deprecated".equals(utf85)) {
                unsignedShort |= 131072;
            } else if ("Synthetic".equals(utf85)) {
                unsignedShort |= 266240;
            } else if ("SourceDebugExtension".equals(utf85)) {
                int i11 = readInt(attributes + 4);
                if (i11 > this.f12859b.length - (attributes + 8)) {
                    throw new IllegalArgumentException();
                }
                utf = readUTF(attributes + 8, i11, new char[i11]);
            } else if ("RuntimeInvisibleAnnotations".equals(utf85)) {
                i7 = attributes + 8;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(utf85)) {
                i9 = attributes + 8;
            } else if ("BootstrapMethods".equals(utf85)) {
                int[] iArr = new int[readUnsignedShort(attributes + 8)];
                int unsignedShort4 = attributes + 10;
                for (int i12 = 0; i12 < iArr.length; i12++) {
                    iArr[i12] = unsignedShort4;
                    unsignedShort4 += (2 + readUnsignedShort(unsignedShort4 + 2)) << 1;
                }
                context.bootstrapMethods = iArr;
            } else {
                Attribute attribute2 = readAttribute(attributeArr, utf85, attributes + 8, readInt(attributes + 4), cArr, -1, null);
                if (attribute2 != null) {
                    attribute2.next = attribute;
                    attribute = attribute2;
                }
            }
            attributes += 6 + readInt(attributes + 4);
        }
        classVisitor.visit(readInt(this.items[1] - 7), unsignedShort, str, utf8, str2, strArr);
        if ((i2 & 2) == 0 && (utf82 != null || utf != null)) {
            classVisitor.visitSource(utf82, utf);
        }
        if (str3 != null) {
            classVisitor.visitOuterClass(str3, utf83, utf84);
        }
        if (i6 != 0) {
            int annotationValues = i6 + 2;
            for (int unsignedShort5 = readUnsignedShort(i6); unsignedShort5 > 0; unsignedShort5--) {
                annotationValues = readAnnotationValues(annotationValues + 2, cArr, true, classVisitor.visitAnnotation(readUTF8(annotationValues, cArr), true));
            }
        }
        if (i7 != 0) {
            int annotationValues2 = i7 + 2;
            for (int unsignedShort6 = readUnsignedShort(i7); unsignedShort6 > 0; unsignedShort6--) {
                annotationValues2 = readAnnotationValues(annotationValues2 + 2, cArr, true, classVisitor.visitAnnotation(readUTF8(annotationValues2, cArr), false));
            }
        }
        if (i8 != 0) {
            int annotationValues3 = i8 + 2;
            for (int unsignedShort7 = readUnsignedShort(i8); unsignedShort7 > 0; unsignedShort7--) {
                int annotationTarget = readAnnotationTarget(context, annotationValues3);
                annotationValues3 = readAnnotationValues(annotationTarget + 2, cArr, true, classVisitor.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(annotationTarget, cArr), true));
            }
        }
        if (i9 != 0) {
            int annotationValues4 = i9 + 2;
            for (int unsignedShort8 = readUnsignedShort(i9); unsignedShort8 > 0; unsignedShort8--) {
                int annotationTarget2 = readAnnotationTarget(context, annotationValues4);
                annotationValues4 = readAnnotationValues(annotationTarget2 + 2, cArr, true, classVisitor.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(annotationTarget2, cArr), false));
            }
        }
        while (attribute != null) {
            Attribute attribute3 = attribute.next;
            attribute.next = null;
            classVisitor.visitAttribute(attribute);
            attribute = attribute3;
        }
        if (i10 != 0) {
            int i13 = i10 + 2;
            for (int unsignedShort9 = readUnsignedShort(i10); unsignedShort9 > 0; unsignedShort9--) {
                classVisitor.visitInnerClass(readClass(i13, cArr), readClass(i13 + 2, cArr), readUTF8(i13 + 4, cArr), readUnsignedShort(i13 + 6));
                i13 += 8;
            }
        }
        int length = this.header + 10 + (2 * strArr.length);
        for (int unsignedShort10 = readUnsignedShort(length - 2); unsignedShort10 > 0; unsignedShort10--) {
            length = readField(classVisitor, context, length);
        }
        int method = length + 2;
        for (int unsignedShort11 = readUnsignedShort(method - 2); unsignedShort11 > 0; unsignedShort11--) {
            method = readMethod(classVisitor, context, method);
        }
        classVisitor.visitEnd();
    }

    private int readField(ClassVisitor classVisitor, Context context, int i2) {
        char[] cArr = context.buffer;
        int unsignedShort = readUnsignedShort(i2);
        String utf8 = readUTF8(i2 + 2, cArr);
        String utf82 = readUTF8(i2 + 4, cArr);
        int i3 = i2 + 6;
        String utf83 = null;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        Object obj = null;
        Attribute attribute = null;
        for (int unsignedShort2 = readUnsignedShort(i3); unsignedShort2 > 0; unsignedShort2--) {
            String utf84 = readUTF8(i3 + 2, cArr);
            if ("ConstantValue".equals(utf84)) {
                int unsignedShort3 = readUnsignedShort(i3 + 8);
                obj = unsignedShort3 == 0 ? null : readConst(unsignedShort3, cArr);
            } else if (Constants._TAG_SIGNATURE.equals(utf84)) {
                utf83 = readUTF8(i3 + 8, cArr);
            } else if ("Deprecated".equals(utf84)) {
                unsignedShort |= 131072;
            } else if ("Synthetic".equals(utf84)) {
                unsignedShort |= 266240;
            } else if ("RuntimeVisibleAnnotations".equals(utf84)) {
                i4 = i3 + 8;
            } else if ("RuntimeVisibleTypeAnnotations".equals(utf84)) {
                i6 = i3 + 8;
            } else if ("RuntimeInvisibleAnnotations".equals(utf84)) {
                i5 = i3 + 8;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(utf84)) {
                i7 = i3 + 8;
            } else {
                Attribute attribute2 = readAttribute(context.attrs, utf84, i3 + 8, readInt(i3 + 4), cArr, -1, null);
                if (attribute2 != null) {
                    attribute2.next = attribute;
                    attribute = attribute2;
                }
            }
            i3 += 6 + readInt(i3 + 4);
        }
        int i8 = i3 + 2;
        FieldVisitor fieldVisitorVisitField = classVisitor.visitField(unsignedShort, utf8, utf82, utf83, obj);
        if (fieldVisitorVisitField == null) {
            return i8;
        }
        if (i4 != 0) {
            int annotationValues = i4 + 2;
            for (int unsignedShort4 = readUnsignedShort(i4); unsignedShort4 > 0; unsignedShort4--) {
                annotationValues = readAnnotationValues(annotationValues + 2, cArr, true, fieldVisitorVisitField.visitAnnotation(readUTF8(annotationValues, cArr), true));
            }
        }
        if (i5 != 0) {
            int annotationValues2 = i5 + 2;
            for (int unsignedShort5 = readUnsignedShort(i5); unsignedShort5 > 0; unsignedShort5--) {
                annotationValues2 = readAnnotationValues(annotationValues2 + 2, cArr, true, fieldVisitorVisitField.visitAnnotation(readUTF8(annotationValues2, cArr), false));
            }
        }
        if (i6 != 0) {
            int annotationValues3 = i6 + 2;
            for (int unsignedShort6 = readUnsignedShort(i6); unsignedShort6 > 0; unsignedShort6--) {
                int annotationTarget = readAnnotationTarget(context, annotationValues3);
                annotationValues3 = readAnnotationValues(annotationTarget + 2, cArr, true, fieldVisitorVisitField.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(annotationTarget, cArr), true));
            }
        }
        if (i7 != 0) {
            int annotationValues4 = i7 + 2;
            for (int unsignedShort7 = readUnsignedShort(i7); unsignedShort7 > 0; unsignedShort7--) {
                int annotationTarget2 = readAnnotationTarget(context, annotationValues4);
                annotationValues4 = readAnnotationValues(annotationTarget2 + 2, cArr, true, fieldVisitorVisitField.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(annotationTarget2, cArr), false));
            }
        }
        while (attribute != null) {
            Attribute attribute3 = attribute.next;
            attribute.next = null;
            fieldVisitorVisitField.visitAttribute(attribute);
            attribute = attribute3;
        }
        fieldVisitorVisitField.visitEnd();
        return i8;
    }

    private int readMethod(ClassVisitor classVisitor, Context context, int i2) {
        char[] cArr = context.buffer;
        context.access = readUnsignedShort(i2);
        context.name = readUTF8(i2 + 2, cArr);
        context.desc = readUTF8(i2 + 4, cArr);
        int i3 = i2 + 6;
        int i4 = 0;
        int i5 = 0;
        String[] strArr = null;
        String utf8 = null;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        Attribute attribute = null;
        for (int unsignedShort = readUnsignedShort(i3); unsignedShort > 0; unsignedShort--) {
            String utf82 = readUTF8(i3 + 2, cArr);
            if ("Code".equals(utf82)) {
                if ((context.flags & 1) == 0) {
                    i4 = i3 + 8;
                }
            } else if ("Exceptions".equals(utf82)) {
                strArr = new String[readUnsignedShort(i3 + 8)];
                i5 = i3 + 10;
                for (int i14 = 0; i14 < strArr.length; i14++) {
                    strArr[i14] = readClass(i5, cArr);
                    i5 += 2;
                }
            } else if (Constants._TAG_SIGNATURE.equals(utf82)) {
                utf8 = readUTF8(i3 + 8, cArr);
            } else if ("Deprecated".equals(utf82)) {
                context.access |= 131072;
            } else if ("RuntimeVisibleAnnotations".equals(utf82)) {
                i7 = i3 + 8;
            } else if ("RuntimeVisibleTypeAnnotations".equals(utf82)) {
                i9 = i3 + 8;
            } else if ("AnnotationDefault".equals(utf82)) {
                i11 = i3 + 8;
            } else if ("Synthetic".equals(utf82)) {
                context.access |= 266240;
            } else if ("RuntimeInvisibleAnnotations".equals(utf82)) {
                i8 = i3 + 8;
            } else if ("RuntimeInvisibleTypeAnnotations".equals(utf82)) {
                i10 = i3 + 8;
            } else if ("RuntimeVisibleParameterAnnotations".equals(utf82)) {
                i12 = i3 + 8;
            } else if ("RuntimeInvisibleParameterAnnotations".equals(utf82)) {
                i13 = i3 + 8;
            } else if ("MethodParameters".equals(utf82)) {
                i6 = i3 + 8;
            } else {
                Attribute attribute2 = readAttribute(context.attrs, utf82, i3 + 8, readInt(i3 + 4), cArr, -1, null);
                if (attribute2 != null) {
                    attribute2.next = attribute;
                    attribute = attribute2;
                }
            }
            i3 += 6 + readInt(i3 + 4);
        }
        int i15 = i3 + 2;
        MethodVisitor methodVisitorVisitMethod = classVisitor.visitMethod(context.access, context.name, context.desc, utf8, strArr);
        if (methodVisitorVisitMethod == null) {
            return i15;
        }
        if (methodVisitorVisitMethod instanceof MethodWriter) {
            MethodWriter methodWriter = (MethodWriter) methodVisitorVisitMethod;
            if (methodWriter.cw.cr == this && utf8 == methodWriter.signature) {
                boolean z2 = false;
                if (strArr == null) {
                    z2 = methodWriter.exceptionCount == 0;
                } else if (strArr.length == methodWriter.exceptionCount) {
                    z2 = true;
                    int length = strArr.length - 1;
                    while (true) {
                        if (length < 0) {
                            break;
                        }
                        i5 -= 2;
                        if (methodWriter.exceptions[length] != readUnsignedShort(i5)) {
                            z2 = false;
                            break;
                        }
                        length--;
                    }
                }
                if (z2) {
                    methodWriter.classReaderOffset = i3;
                    methodWriter.classReaderLength = i15 - i3;
                    return i15;
                }
            }
        }
        if (i6 != 0) {
            int i16 = this.f12859b[i6] & 255;
            int i17 = i6;
            int i18 = 1;
            while (true) {
                int i19 = i17 + i18;
                if (i16 <= 0) {
                    break;
                }
                methodVisitorVisitMethod.visitParameter(readUTF8(i19, cArr), readUnsignedShort(i19 + 2));
                i16--;
                i17 = i19;
                i18 = 4;
            }
        }
        if (i11 != 0) {
            AnnotationVisitor annotationVisitorVisitAnnotationDefault = methodVisitorVisitMethod.visitAnnotationDefault();
            readAnnotationValue(i11, cArr, null, annotationVisitorVisitAnnotationDefault);
            if (annotationVisitorVisitAnnotationDefault != null) {
                annotationVisitorVisitAnnotationDefault.visitEnd();
            }
        }
        if (i7 != 0) {
            int annotationValues = i7 + 2;
            for (int unsignedShort2 = readUnsignedShort(i7); unsignedShort2 > 0; unsignedShort2--) {
                annotationValues = readAnnotationValues(annotationValues + 2, cArr, true, methodVisitorVisitMethod.visitAnnotation(readUTF8(annotationValues, cArr), true));
            }
        }
        if (i8 != 0) {
            int annotationValues2 = i8 + 2;
            for (int unsignedShort3 = readUnsignedShort(i8); unsignedShort3 > 0; unsignedShort3--) {
                annotationValues2 = readAnnotationValues(annotationValues2 + 2, cArr, true, methodVisitorVisitMethod.visitAnnotation(readUTF8(annotationValues2, cArr), false));
            }
        }
        if (i9 != 0) {
            int annotationValues3 = i9 + 2;
            for (int unsignedShort4 = readUnsignedShort(i9); unsignedShort4 > 0; unsignedShort4--) {
                int annotationTarget = readAnnotationTarget(context, annotationValues3);
                annotationValues3 = readAnnotationValues(annotationTarget + 2, cArr, true, methodVisitorVisitMethod.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(annotationTarget, cArr), true));
            }
        }
        if (i10 != 0) {
            int annotationValues4 = i10 + 2;
            for (int unsignedShort5 = readUnsignedShort(i10); unsignedShort5 > 0; unsignedShort5--) {
                int annotationTarget2 = readAnnotationTarget(context, annotationValues4);
                annotationValues4 = readAnnotationValues(annotationTarget2 + 2, cArr, true, methodVisitorVisitMethod.visitTypeAnnotation(context.typeRef, context.typePath, readUTF8(annotationTarget2, cArr), false));
            }
        }
        if (i12 != 0) {
            readParameterAnnotations(methodVisitorVisitMethod, context, i12, true);
        }
        if (i13 != 0) {
            readParameterAnnotations(methodVisitorVisitMethod, context, i13, false);
        }
        while (attribute != null) {
            Attribute attribute3 = attribute.next;
            attribute.next = null;
            methodVisitorVisitMethod.visitAttribute(attribute);
            attribute = attribute3;
        }
        if (i4 != 0) {
            methodVisitorVisitMethod.visitCode();
            readCode(methodVisitorVisitMethod, context, i4);
        }
        methodVisitorVisitMethod.visitEnd();
        return i15;
    }

    private void readCode(MethodVisitor methodVisitor, Context context, int i2) {
        int unsignedShort;
        Attribute attribute;
        byte[] bArr = this.f12859b;
        char[] cArr = context.buffer;
        int unsignedShort2 = readUnsignedShort(i2);
        int unsignedShort3 = readUnsignedShort(i2 + 2);
        int i3 = readInt(i2 + 4);
        int i4 = i2 + 8;
        if (i3 > bArr.length - i4) {
            throw new IllegalArgumentException();
        }
        int i5 = i4 + i3;
        Label[] labelArr = new Label[i3 + 2];
        context.labels = labelArr;
        readLabel(i3 + 1, labelArr);
        while (i4 < i5) {
            int i6 = i4 - i4;
            switch (ClassWriter.TYPE[bArr[i4] & 255]) {
                case 0:
                case 4:
                    i4++;
                    break;
                case 1:
                case 3:
                case 11:
                    i4 += 2;
                    break;
                case 2:
                case 5:
                case 6:
                case 12:
                case 13:
                    i4 += 3;
                    break;
                case 7:
                case 8:
                    i4 += 5;
                    break;
                case 9:
                    readLabel(i6 + readShort(i4 + 1), labelArr);
                    i4 += 3;
                    break;
                case 10:
                    readLabel(i6 + readInt(i4 + 1), labelArr);
                    i4 += 5;
                    break;
                case 14:
                    int i7 = (i4 + 4) - (i6 & 3);
                    readLabel(i6 + readInt(i7), labelArr);
                    for (int i8 = (readInt(i7 + 8) - readInt(i7 + 4)) + 1; i8 > 0; i8--) {
                        readLabel(i6 + readInt(i7 + 12), labelArr);
                        i7 += 4;
                    }
                    i4 = i7 + 12;
                    break;
                case 15:
                    int i9 = (i4 + 4) - (i6 & 3);
                    readLabel(i6 + readInt(i9), labelArr);
                    for (int i10 = readInt(i9 + 4); i10 > 0; i10--) {
                        readLabel(i6 + readInt(i9 + 12), labelArr);
                        i9 += 8;
                    }
                    i4 = i9 + 8;
                    break;
                case 16:
                default:
                    i4 += 4;
                    break;
                case 17:
                    if ((bArr[i4 + 1] & 255) == 132) {
                        i4 += 6;
                        break;
                    } else {
                        i4 += 4;
                        break;
                    }
            }
        }
        for (int unsignedShort4 = readUnsignedShort(i4); unsignedShort4 > 0; unsignedShort4--) {
            methodVisitor.visitTryCatchBlock(readLabel(readUnsignedShort(i4 + 2), labelArr), readLabel(readUnsignedShort(i4 + 4), labelArr), readLabel(readUnsignedShort(i4 + 6), labelArr), readUTF8(this.items[readUnsignedShort(i4 + 8)], cArr));
            i4 += 8;
        }
        int i11 = i4 + 2;
        int[] typeAnnotations = null;
        int[] typeAnnotations2 = null;
        int i12 = 0;
        int i13 = 0;
        int unsignedShort5 = -1;
        int unsignedShort6 = -1;
        int i14 = 0;
        int i15 = 0;
        boolean z2 = true;
        boolean z3 = (context.flags & 8) != 0;
        int frame = 0;
        int i16 = 0;
        int unsignedShort7 = 0;
        Context context2 = null;
        Attribute attribute2 = null;
        for (int unsignedShort8 = readUnsignedShort(i11); unsignedShort8 > 0; unsignedShort8--) {
            String utf8 = readUTF8(i11 + 2, cArr);
            if ("LocalVariableTable".equals(utf8)) {
                if ((context.flags & 2) == 0) {
                    i14 = i11 + 8;
                    int i17 = i11;
                    for (int unsignedShort9 = readUnsignedShort(i11 + 8); unsignedShort9 > 0; unsignedShort9--) {
                        int unsignedShort10 = readUnsignedShort(i17 + 10);
                        if (labelArr[unsignedShort10] == null) {
                            readLabel(unsignedShort10, labelArr).status |= 1;
                        }
                        int unsignedShort11 = unsignedShort10 + readUnsignedShort(i17 + 12);
                        if (labelArr[unsignedShort11] == null) {
                            readLabel(unsignedShort11, labelArr).status |= 1;
                        }
                        i17 += 10;
                    }
                }
            } else if ("LocalVariableTypeTable".equals(utf8)) {
                i15 = i11 + 8;
            } else if ("LineNumberTable".equals(utf8)) {
                if ((context.flags & 2) == 0) {
                    int i18 = i11;
                    for (int unsignedShort12 = readUnsignedShort(i11 + 8); unsignedShort12 > 0; unsignedShort12--) {
                        int unsignedShort13 = readUnsignedShort(i18 + 10);
                        if (labelArr[unsignedShort13] == null) {
                            readLabel(unsignedShort13, labelArr).status |= 1;
                        }
                        labelArr[unsignedShort13].line = readUnsignedShort(i18 + 12);
                        i18 += 4;
                    }
                }
            } else if ("RuntimeVisibleTypeAnnotations".equals(utf8)) {
                typeAnnotations = readTypeAnnotations(methodVisitor, context, i11 + 8, true);
                unsignedShort5 = (typeAnnotations.length == 0 || readByte(typeAnnotations[0]) < 67) ? -1 : readUnsignedShort(typeAnnotations[0] + 1);
            } else if ("RuntimeInvisibleTypeAnnotations".equals(utf8)) {
                typeAnnotations2 = readTypeAnnotations(methodVisitor, context, i11 + 8, false);
                unsignedShort6 = (typeAnnotations2.length == 0 || readByte(typeAnnotations2[0]) < 67) ? -1 : readUnsignedShort(typeAnnotations2[0] + 1);
            } else if ("StackMapTable".equals(utf8)) {
                if ((context.flags & 4) == 0) {
                    frame = i11 + 10;
                    i16 = readInt(i11 + 4);
                    unsignedShort7 = readUnsignedShort(i11 + 8);
                }
            } else if ("StackMap".equals(utf8)) {
                if ((context.flags & 4) == 0) {
                    z2 = false;
                    frame = i11 + 10;
                    i16 = readInt(i11 + 4);
                    unsignedShort7 = readUnsignedShort(i11 + 8);
                }
            } else {
                for (int i19 = 0; i19 < context.attrs.length; i19++) {
                    if (context.attrs[i19].type.equals(utf8) && (attribute = context.attrs[i19].read(this, i11 + 8, readInt(i11 + 4), cArr, i4 - 8, labelArr)) != null) {
                        attribute.next = attribute2;
                        attribute2 = attribute;
                    }
                }
            }
            i11 += 6 + readInt(i11 + 4);
        }
        int i20 = i11 + 2;
        if (frame != 0) {
            context2 = context;
            context2.offset = -1;
            context2.mode = 0;
            context2.localCount = 0;
            context2.localDiff = 0;
            context2.stackCount = 0;
            context2.local = new Object[unsignedShort3];
            context2.stack = new Object[unsignedShort2];
            if (z3) {
                getImplicitFrame(context);
            }
            for (int i21 = frame; i21 < (frame + i16) - 2; i21++) {
                if (bArr[i21] == 8 && (unsignedShort = readUnsignedShort(i21 + 1)) >= 0 && unsignedShort < i3 && (bArr[i4 + unsignedShort] & 255) == 187) {
                    readLabel(unsignedShort, labelArr);
                }
            }
        }
        int i22 = i4;
        while (i22 < i5) {
            int i23 = i22 - i4;
            Label label = labelArr[i23];
            if (label != null) {
                methodVisitor.visitLabel(label);
                if ((context.flags & 2) == 0 && label.line > 0) {
                    methodVisitor.visitLineNumber(label.line, label);
                }
            }
            while (context2 != null && (context2.offset == i23 || context2.offset == -1)) {
                if (context2.offset != -1) {
                    if (!z2 || z3) {
                        methodVisitor.visitFrame(-1, context2.localCount, context2.local, context2.stackCount, context2.stack);
                    } else {
                        methodVisitor.visitFrame(context2.mode, context2.localDiff, context2.local, context2.stackCount, context2.stack);
                    }
                }
                if (unsignedShort7 > 0) {
                    frame = readFrame(frame, z2, z3, context2);
                    unsignedShort7--;
                } else {
                    context2 = null;
                }
            }
            int i24 = bArr[i22] & 255;
            switch (ClassWriter.TYPE[i24]) {
                case 0:
                    methodVisitor.visitInsn(i24);
                    i22++;
                    break;
                case 1:
                    methodVisitor.visitIntInsn(i24, bArr[i22 + 1]);
                    i22 += 2;
                    break;
                case 2:
                    methodVisitor.visitIntInsn(i24, readShort(i22 + 1));
                    i22 += 3;
                    break;
                case 3:
                    methodVisitor.visitVarInsn(i24, bArr[i22 + 1] & 255);
                    i22 += 2;
                    break;
                case 4:
                    if (i24 > 54) {
                        int i25 = i24 - 59;
                        methodVisitor.visitVarInsn(54 + (i25 >> 2), i25 & 3);
                    } else {
                        int i26 = i24 - 26;
                        methodVisitor.visitVarInsn(21 + (i26 >> 2), i26 & 3);
                    }
                    i22++;
                    break;
                case 5:
                    methodVisitor.visitTypeInsn(i24, readClass(i22 + 1, cArr));
                    i22 += 3;
                    break;
                case 6:
                case 7:
                    int i27 = this.items[readUnsignedShort(i22 + 1)];
                    boolean z4 = bArr[i27 - 1] == 11;
                    String str = readClass(i27, cArr);
                    int i28 = this.items[readUnsignedShort(i27 + 2)];
                    String utf82 = readUTF8(i28, cArr);
                    String utf83 = readUTF8(i28 + 2, cArr);
                    if (i24 < 182) {
                        methodVisitor.visitFieldInsn(i24, str, utf82, utf83);
                    } else {
                        methodVisitor.visitMethodInsn(i24, str, utf82, utf83, z4);
                    }
                    if (i24 == 185) {
                        i22 += 5;
                        break;
                    } else {
                        i22 += 3;
                        break;
                    }
                case 8:
                    int i29 = this.items[readUnsignedShort(i22 + 1)];
                    int i30 = context.bootstrapMethods[readUnsignedShort(i29)];
                    Handle handle = (Handle) readConst(readUnsignedShort(i30), cArr);
                    int unsignedShort14 = readUnsignedShort(i30 + 2);
                    Object[] objArr = new Object[unsignedShort14];
                    int i31 = i30 + 4;
                    for (int i32 = 0; i32 < unsignedShort14; i32++) {
                        objArr[i32] = readConst(readUnsignedShort(i31), cArr);
                        i31 += 2;
                    }
                    int i33 = this.items[readUnsignedShort(i29 + 2)];
                    methodVisitor.visitInvokeDynamicInsn(readUTF8(i33, cArr), readUTF8(i33 + 2, cArr), handle, objArr);
                    i22 += 5;
                    break;
                case 9:
                    methodVisitor.visitJumpInsn(i24, labelArr[i23 + readShort(i22 + 1)]);
                    i22 += 3;
                    break;
                case 10:
                    methodVisitor.visitJumpInsn(i24 - 33, labelArr[i23 + readInt(i22 + 1)]);
                    i22 += 5;
                    break;
                case 11:
                    methodVisitor.visitLdcInsn(readConst(bArr[i22 + 1] & 255, cArr));
                    i22 += 2;
                    break;
                case 12:
                    methodVisitor.visitLdcInsn(readConst(readUnsignedShort(i22 + 1), cArr));
                    i22 += 3;
                    break;
                case 13:
                    methodVisitor.visitIincInsn(bArr[i22 + 1] & 255, bArr[i22 + 2]);
                    i22 += 3;
                    break;
                case 14:
                    int i34 = (i22 + 4) - (i23 & 3);
                    int i35 = i23 + readInt(i34);
                    int i36 = readInt(i34 + 4);
                    int i37 = readInt(i34 + 8);
                    Label[] labelArr2 = new Label[(i37 - i36) + 1];
                    i22 = i34 + 12;
                    for (int i38 = 0; i38 < labelArr2.length; i38++) {
                        labelArr2[i38] = labelArr[i23 + readInt(i22)];
                        i22 += 4;
                    }
                    methodVisitor.visitTableSwitchInsn(i36, i37, labelArr[i35], labelArr2);
                    break;
                case 15:
                    int i39 = (i22 + 4) - (i23 & 3);
                    int i40 = i23 + readInt(i39);
                    int i41 = readInt(i39 + 4);
                    int[] iArr = new int[i41];
                    Label[] labelArr3 = new Label[i41];
                    i22 = i39 + 8;
                    for (int i42 = 0; i42 < i41; i42++) {
                        iArr[i42] = readInt(i22);
                        labelArr3[i42] = labelArr[i23 + readInt(i22 + 4)];
                        i22 += 8;
                    }
                    methodVisitor.visitLookupSwitchInsn(labelArr[i40], iArr, labelArr3);
                    break;
                case 16:
                default:
                    methodVisitor.visitMultiANewArrayInsn(readClass(i22 + 1, cArr), bArr[i22 + 3] & 255);
                    i22 += 4;
                    break;
                case 17:
                    int i43 = bArr[i22 + 1] & 255;
                    if (i43 == 132) {
                        methodVisitor.visitIincInsn(readUnsignedShort(i22 + 2), readShort(i22 + 4));
                        i22 += 6;
                        break;
                    } else {
                        methodVisitor.visitVarInsn(i43, readUnsignedShort(i22 + 2));
                        i22 += 4;
                        break;
                    }
            }
            while (typeAnnotations != null && i12 < typeAnnotations.length && unsignedShort5 <= i23) {
                if (unsignedShort5 == i23) {
                    int annotationTarget = readAnnotationTarget(context, typeAnnotations[i12]);
                    readAnnotationValues(annotationTarget + 2, cArr, true, methodVisitor.visitInsnAnnotation(context.typeRef, context.typePath, readUTF8(annotationTarget, cArr), true));
                }
                i12++;
                unsignedShort5 = (i12 >= typeAnnotations.length || readByte(typeAnnotations[i12]) < 67) ? -1 : readUnsignedShort(typeAnnotations[i12] + 1);
            }
            while (typeAnnotations2 != null && i13 < typeAnnotations2.length && unsignedShort6 <= i23) {
                if (unsignedShort6 == i23) {
                    int annotationTarget2 = readAnnotationTarget(context, typeAnnotations2[i13]);
                    readAnnotationValues(annotationTarget2 + 2, cArr, true, methodVisitor.visitInsnAnnotation(context.typeRef, context.typePath, readUTF8(annotationTarget2, cArr), false));
                }
                i13++;
                unsignedShort6 = (i13 >= typeAnnotations2.length || readByte(typeAnnotations2[i13]) < 67) ? -1 : readUnsignedShort(typeAnnotations2[i13] + 1);
            }
        }
        if (labelArr[i3] != null) {
            methodVisitor.visitLabel(labelArr[i3]);
        }
        if ((context.flags & 2) == 0 && i14 != 0) {
            int[] iArr2 = null;
            if (i15 != 0) {
                int i44 = i15 + 2;
                iArr2 = new int[readUnsignedShort(i15) * 3];
                int length = iArr2.length;
                while (length > 0) {
                    int i45 = length - 1;
                    iArr2[i45] = i44 + 6;
                    int i46 = i45 - 1;
                    iArr2[i46] = readUnsignedShort(i44 + 8);
                    length = i46 - 1;
                    iArr2[length] = readUnsignedShort(i44);
                    i44 += 10;
                }
            }
            int i47 = i14 + 2;
            for (int unsignedShort15 = readUnsignedShort(i14); unsignedShort15 > 0; unsignedShort15--) {
                int unsignedShort16 = readUnsignedShort(i47);
                int unsignedShort17 = readUnsignedShort(i47 + 2);
                int unsignedShort18 = readUnsignedShort(i47 + 8);
                String utf84 = null;
                if (iArr2 != null) {
                    int i48 = 0;
                    while (true) {
                        if (i48 >= iArr2.length) {
                            break;
                        }
                        if (iArr2[i48] != unsignedShort16 || iArr2[i48 + 1] != unsignedShort18) {
                            i48 += 3;
                        } else {
                            utf84 = readUTF8(iArr2[i48 + 2], cArr);
                        }
                    }
                }
                methodVisitor.visitLocalVariable(readUTF8(i47 + 4, cArr), readUTF8(i47 + 6, cArr), utf84, labelArr[unsignedShort16], labelArr[unsignedShort16 + unsignedShort17], unsignedShort18);
                i47 += 10;
            }
        }
        if (typeAnnotations != null) {
            for (int i49 = 0; i49 < typeAnnotations.length; i49++) {
                if ((readByte(typeAnnotations[i49]) >> 1) == 32) {
                    int annotationTarget3 = readAnnotationTarget(context, typeAnnotations[i49]);
                    readAnnotationValues(annotationTarget3 + 2, cArr, true, methodVisitor.visitLocalVariableAnnotation(context.typeRef, context.typePath, context.start, context.end, context.index, readUTF8(annotationTarget3, cArr), true));
                }
            }
        }
        if (typeAnnotations2 != null) {
            for (int i50 = 0; i50 < typeAnnotations2.length; i50++) {
                if ((readByte(typeAnnotations2[i50]) >> 1) == 32) {
                    int annotationTarget4 = readAnnotationTarget(context, typeAnnotations2[i50]);
                    readAnnotationValues(annotationTarget4 + 2, cArr, true, methodVisitor.visitLocalVariableAnnotation(context.typeRef, context.typePath, context.start, context.end, context.index, readUTF8(annotationTarget4, cArr), false));
                }
            }
        }
        while (attribute2 != null) {
            Attribute attribute3 = attribute2.next;
            attribute2.next = null;
            methodVisitor.visitAttribute(attribute2);
            attribute2 = attribute3;
        }
        methodVisitor.visitMaxs(unsignedShort2, unsignedShort3);
    }

    private int[] readTypeAnnotations(MethodVisitor methodVisitor, Context context, int i2, boolean z2) {
        int i3;
        int annotationValues;
        char[] cArr = context.buffer;
        int[] iArr = new int[readUnsignedShort(i2)];
        int i4 = i2 + 2;
        for (int i5 = 0; i5 < iArr.length; i5++) {
            iArr[i5] = i4;
            int i6 = readInt(i4);
            switch (i6 >>> 24) {
                case 0:
                case 1:
                case 22:
                    i3 = i4 + 2;
                    break;
                case 19:
                case 20:
                case 21:
                    i3 = i4 + 1;
                    break;
                case 64:
                case 65:
                    for (int unsignedShort = readUnsignedShort(i4 + 1); unsignedShort > 0; unsignedShort--) {
                        int unsignedShort2 = readUnsignedShort(i4 + 3);
                        int unsignedShort3 = readUnsignedShort(i4 + 5);
                        readLabel(unsignedShort2, context.labels);
                        readLabel(unsignedShort2 + unsignedShort3, context.labels);
                        i4 += 6;
                    }
                    i3 = i4 + 3;
                    break;
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                    i3 = i4 + 4;
                    break;
                default:
                    i3 = i4 + 3;
                    break;
            }
            int i7 = readByte(i3);
            if ((i6 >>> 24) == 66) {
                TypePath typePath = i7 == 0 ? null : new TypePath(this.f12859b, i3);
                int i8 = i3 + 1 + (2 * i7);
                annotationValues = readAnnotationValues(i8 + 2, cArr, true, methodVisitor.visitTryCatchAnnotation(i6, typePath, readUTF8(i8, cArr), z2));
            } else {
                annotationValues = readAnnotationValues(i3 + 3 + (2 * i7), cArr, true, null);
            }
            i4 = annotationValues;
        }
        return iArr;
    }

    private int readAnnotationTarget(Context context, int i2) {
        int i3;
        int i4;
        int i5 = readInt(i2);
        switch (i5 >>> 24) {
            case 0:
            case 1:
            case 22:
                i3 = i5 & DTMManager.IDENT_DTM_DEFAULT;
                i4 = i2 + 2;
                break;
            case 19:
            case 20:
            case 21:
                i3 = i5 & (-16777216);
                i4 = i2 + 1;
                break;
            case 64:
            case 65:
                i3 = i5 & (-16777216);
                int unsignedShort = readUnsignedShort(i2 + 1);
                context.start = new Label[unsignedShort];
                context.end = new Label[unsignedShort];
                context.index = new int[unsignedShort];
                i4 = i2 + 3;
                for (int i6 = 0; i6 < unsignedShort; i6++) {
                    int unsignedShort2 = readUnsignedShort(i4);
                    int unsignedShort3 = readUnsignedShort(i4 + 2);
                    context.start[i6] = readLabel(unsignedShort2, context.labels);
                    context.end[i6] = readLabel(unsignedShort2 + unsignedShort3, context.labels);
                    context.index[i6] = readUnsignedShort(i4 + 4);
                    i4 += 6;
                }
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                i3 = i5 & (-16776961);
                i4 = i2 + 4;
                break;
            default:
                i3 = i5 & ((i5 >>> 24) < 67 ? -256 : -16777216);
                i4 = i2 + 3;
                break;
        }
        int i7 = readByte(i4);
        context.typeRef = i3;
        context.typePath = i7 == 0 ? null : new TypePath(this.f12859b, i4);
        return i4 + 1 + (2 * i7);
    }

    private void readParameterAnnotations(MethodVisitor methodVisitor, Context context, int i2, boolean z2) {
        int annotationValues = i2 + 1;
        int i3 = this.f12859b[i2] & 255;
        int length = Type.getArgumentTypes(context.desc).length - i3;
        int i4 = 0;
        while (i4 < length) {
            AnnotationVisitor annotationVisitorVisitParameterAnnotation = methodVisitor.visitParameterAnnotation(i4, "Ljava/lang/Synthetic;", false);
            if (annotationVisitorVisitParameterAnnotation != null) {
                annotationVisitorVisitParameterAnnotation.visitEnd();
            }
            i4++;
        }
        char[] cArr = context.buffer;
        while (i4 < i3 + length) {
            annotationValues += 2;
            for (int unsignedShort = readUnsignedShort(annotationValues); unsignedShort > 0; unsignedShort--) {
                annotationValues = readAnnotationValues(annotationValues + 2, cArr, true, methodVisitor.visitParameterAnnotation(i4, readUTF8(annotationValues, cArr), z2));
            }
            i4++;
        }
    }

    private int readAnnotationValues(int i2, char[] cArr, boolean z2, AnnotationVisitor annotationVisitor) {
        int unsignedShort = readUnsignedShort(i2);
        int annotationValue = i2 + 2;
        if (z2) {
            while (unsignedShort > 0) {
                annotationValue = readAnnotationValue(annotationValue + 2, cArr, readUTF8(annotationValue, cArr), annotationVisitor);
                unsignedShort--;
            }
        } else {
            while (unsignedShort > 0) {
                annotationValue = readAnnotationValue(annotationValue, cArr, null, annotationVisitor);
                unsignedShort--;
            }
        }
        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
        return annotationValue;
    }

    private int readAnnotationValue(int i2, char[] cArr, String str, AnnotationVisitor annotationVisitor) {
        if (annotationVisitor == null) {
            switch (this.f12859b[i2] & 255) {
                case 64:
                    return readAnnotationValues(i2 + 3, cArr, true, null);
                case 91:
                    return readAnnotationValues(i2 + 1, cArr, false, null);
                case 101:
                    return i2 + 5;
                default:
                    return i2 + 3;
            }
        }
        int annotationValues = i2 + 1;
        switch (this.f12859b[i2] & 255) {
            case 64:
                annotationValues = readAnnotationValues(annotationValues + 2, cArr, true, annotationVisitor.visitAnnotation(str, readUTF8(annotationValues, cArr)));
                break;
            case 66:
                annotationVisitor.visit(str, Byte.valueOf((byte) readInt(this.items[readUnsignedShort(annotationValues)])));
                annotationValues += 2;
                break;
            case 67:
                annotationVisitor.visit(str, Character.valueOf((char) readInt(this.items[readUnsignedShort(annotationValues)])));
                annotationValues += 2;
                break;
            case 68:
            case 70:
            case 73:
            case 74:
                annotationVisitor.visit(str, readConst(readUnsignedShort(annotationValues), cArr));
                annotationValues += 2;
                break;
            case 83:
                annotationVisitor.visit(str, Short.valueOf((short) readInt(this.items[readUnsignedShort(annotationValues)])));
                annotationValues += 2;
                break;
            case 90:
                annotationVisitor.visit(str, readInt(this.items[readUnsignedShort(annotationValues)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                annotationValues += 2;
                break;
            case 91:
                int unsignedShort = readUnsignedShort(annotationValues);
                int i3 = annotationValues + 2;
                if (unsignedShort == 0) {
                    return readAnnotationValues(i3 - 2, cArr, false, annotationVisitor.visitArray(str));
                }
                int i4 = i3 + 1;
                switch (this.f12859b[i3] & 255) {
                    case 66:
                        byte[] bArr = new byte[unsignedShort];
                        for (int i5 = 0; i5 < unsignedShort; i5++) {
                            bArr[i5] = (byte) readInt(this.items[readUnsignedShort(i4)]);
                            i4 += 3;
                        }
                        annotationVisitor.visit(str, bArr);
                        annotationValues = i4 - 1;
                        break;
                    case 67:
                        char[] cArr2 = new char[unsignedShort];
                        for (int i6 = 0; i6 < unsignedShort; i6++) {
                            cArr2[i6] = (char) readInt(this.items[readUnsignedShort(i4)]);
                            i4 += 3;
                        }
                        annotationVisitor.visit(str, cArr2);
                        annotationValues = i4 - 1;
                        break;
                    case 68:
                        double[] dArr = new double[unsignedShort];
                        for (int i7 = 0; i7 < unsignedShort; i7++) {
                            dArr[i7] = Double.longBitsToDouble(readLong(this.items[readUnsignedShort(i4)]));
                            i4 += 3;
                        }
                        annotationVisitor.visit(str, dArr);
                        annotationValues = i4 - 1;
                        break;
                    case 69:
                    case 71:
                    case 72:
                    case 75:
                    case 76:
                    case 77:
                    case 78:
                    case 79:
                    case 80:
                    case 81:
                    case 82:
                    case 84:
                    case 85:
                    case 86:
                    case 87:
                    case 88:
                    case 89:
                    default:
                        annotationValues = readAnnotationValues(i4 - 3, cArr, false, annotationVisitor.visitArray(str));
                        break;
                    case 70:
                        float[] fArr = new float[unsignedShort];
                        for (int i8 = 0; i8 < unsignedShort; i8++) {
                            fArr[i8] = Float.intBitsToFloat(readInt(this.items[readUnsignedShort(i4)]));
                            i4 += 3;
                        }
                        annotationVisitor.visit(str, fArr);
                        annotationValues = i4 - 1;
                        break;
                    case 73:
                        int[] iArr = new int[unsignedShort];
                        for (int i9 = 0; i9 < unsignedShort; i9++) {
                            iArr[i9] = readInt(this.items[readUnsignedShort(i4)]);
                            i4 += 3;
                        }
                        annotationVisitor.visit(str, iArr);
                        annotationValues = i4 - 1;
                        break;
                    case 74:
                        long[] jArr = new long[unsignedShort];
                        for (int i10 = 0; i10 < unsignedShort; i10++) {
                            jArr[i10] = readLong(this.items[readUnsignedShort(i4)]);
                            i4 += 3;
                        }
                        annotationVisitor.visit(str, jArr);
                        annotationValues = i4 - 1;
                        break;
                    case 83:
                        short[] sArr = new short[unsignedShort];
                        for (int i11 = 0; i11 < unsignedShort; i11++) {
                            sArr[i11] = (short) readInt(this.items[readUnsignedShort(i4)]);
                            i4 += 3;
                        }
                        annotationVisitor.visit(str, sArr);
                        annotationValues = i4 - 1;
                        break;
                    case 90:
                        boolean[] zArr = new boolean[unsignedShort];
                        for (int i12 = 0; i12 < unsignedShort; i12++) {
                            zArr[i12] = readInt(this.items[readUnsignedShort(i4)]) != 0;
                            i4 += 3;
                        }
                        annotationVisitor.visit(str, zArr);
                        annotationValues = i4 - 1;
                        break;
                }
            case 99:
                annotationVisitor.visit(str, Type.getType(readUTF8(annotationValues, cArr)));
                annotationValues += 2;
                break;
            case 101:
                annotationVisitor.visitEnum(str, readUTF8(annotationValues, cArr), readUTF8(annotationValues + 2, cArr));
                annotationValues += 4;
                break;
            case 115:
                annotationVisitor.visit(str, readUTF8(annotationValues, cArr));
                annotationValues += 2;
                break;
        }
        return annotationValues;
    }

    private void getImplicitFrame(Context context) {
        String str = context.desc;
        Object[] objArr = context.local;
        int i2 = 0;
        if ((context.access & 8) == 0) {
            if (com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME.equals(context.name)) {
                i2 = 0 + 1;
                objArr[0] = Opcodes.UNINITIALIZED_THIS;
            } else {
                i2 = 0 + 1;
                objArr[0] = readClass(this.header + 2, context.buffer);
            }
        }
        int i3 = 1;
        while (true) {
            int i4 = i3;
            int i5 = i3;
            i3++;
            switch (str.charAt(i5)) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z':
                    int i6 = i2;
                    i2++;
                    objArr[i6] = Opcodes.INTEGER;
                    break;
                case 'D':
                    int i7 = i2;
                    i2++;
                    objArr[i7] = Opcodes.DOUBLE;
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
                    context.localCount = i2;
                    return;
                case 'F':
                    int i8 = i2;
                    i2++;
                    objArr[i8] = Opcodes.FLOAT;
                    break;
                case 'J':
                    int i9 = i2;
                    i2++;
                    objArr[i9] = Opcodes.LONG;
                    break;
                case 'L':
                    while (str.charAt(i3) != ';') {
                        i3++;
                    }
                    int i10 = i2;
                    i2++;
                    int i11 = i3;
                    i3++;
                    objArr[i10] = str.substring(i4 + 1, i11);
                    break;
                case '[':
                    while (str.charAt(i3) == '[') {
                        i3++;
                    }
                    if (str.charAt(i3) == 'L') {
                        do {
                            i3++;
                        } while (str.charAt(i3) != ';');
                    }
                    int i12 = i2;
                    i2++;
                    i3++;
                    objArr[i12] = str.substring(i4, i3);
                    break;
            }
        }
    }

    private int readFrame(int i2, boolean z2, boolean z3, Context context) {
        int i3;
        int unsignedShort;
        char[] cArr = context.buffer;
        Label[] labelArr = context.labels;
        if (z2) {
            i2++;
            i3 = this.f12859b[i2] & 255;
        } else {
            i3 = 255;
            context.offset = -1;
        }
        context.localDiff = 0;
        if (i3 < 64) {
            unsignedShort = i3;
            context.mode = 3;
            context.stackCount = 0;
        } else if (i3 < 128) {
            unsignedShort = i3 - 64;
            i2 = readFrameType(context.stack, 0, i2, cArr, labelArr);
            context.mode = 4;
            context.stackCount = 1;
        } else {
            unsignedShort = readUnsignedShort(i2);
            i2 += 2;
            if (i3 == 247) {
                i2 = readFrameType(context.stack, 0, i2, cArr, labelArr);
                context.mode = 4;
                context.stackCount = 1;
            } else if (i3 >= 248 && i3 < 251) {
                context.mode = 2;
                context.localDiff = 251 - i3;
                context.localCount -= context.localDiff;
                context.stackCount = 0;
            } else if (i3 == 251) {
                context.mode = 3;
                context.stackCount = 0;
            } else if (i3 < 255) {
                int i4 = z3 ? context.localCount : 0;
                for (int i5 = i3 - 251; i5 > 0; i5--) {
                    int i6 = i4;
                    i4++;
                    i2 = readFrameType(context.local, i6, i2, cArr, labelArr);
                }
                context.mode = 1;
                context.localDiff = i3 - 251;
                context.localCount += context.localDiff;
                context.stackCount = 0;
            } else {
                context.mode = 0;
                int unsignedShort2 = readUnsignedShort(i2);
                int frameType = i2 + 2;
                context.localDiff = unsignedShort2;
                context.localCount = unsignedShort2;
                int i7 = 0;
                while (unsignedShort2 > 0) {
                    int i8 = i7;
                    i7++;
                    frameType = readFrameType(context.local, i8, frameType, cArr, labelArr);
                    unsignedShort2--;
                }
                int unsignedShort3 = readUnsignedShort(frameType);
                i2 = frameType + 2;
                context.stackCount = unsignedShort3;
                int i9 = 0;
                while (unsignedShort3 > 0) {
                    int i10 = i9;
                    i9++;
                    i2 = readFrameType(context.stack, i10, i2, cArr, labelArr);
                    unsignedShort3--;
                }
            }
        }
        context.offset += unsignedShort + 1;
        readLabel(context.offset, labelArr);
        return i2;
    }

    private int readFrameType(Object[] objArr, int i2, int i3, char[] cArr, Label[] labelArr) {
        int i4 = i3 + 1;
        switch (this.f12859b[i3] & 255) {
            case 0:
                objArr[i2] = Opcodes.TOP;
                break;
            case 1:
                objArr[i2] = Opcodes.INTEGER;
                break;
            case 2:
                objArr[i2] = Opcodes.FLOAT;
                break;
            case 3:
                objArr[i2] = Opcodes.DOUBLE;
                break;
            case 4:
                objArr[i2] = Opcodes.LONG;
                break;
            case 5:
                objArr[i2] = Opcodes.NULL;
                break;
            case 6:
                objArr[i2] = Opcodes.UNINITIALIZED_THIS;
                break;
            case 7:
                objArr[i2] = readClass(i4, cArr);
                i4 += 2;
                break;
            default:
                objArr[i2] = readLabel(readUnsignedShort(i4), labelArr);
                i4 += 2;
                break;
        }
        return i4;
    }

    protected Label readLabel(int i2, Label[] labelArr) {
        if (labelArr[i2] == null) {
            labelArr[i2] = new Label();
        }
        return labelArr[i2];
    }

    private int getAttributes() {
        int unsignedShort = this.header + 8 + (readUnsignedShort(this.header + 6) * 2);
        for (int unsignedShort2 = readUnsignedShort(unsignedShort); unsignedShort2 > 0; unsignedShort2--) {
            for (int unsignedShort3 = readUnsignedShort(unsignedShort + 8); unsignedShort3 > 0; unsignedShort3--) {
                unsignedShort += 6 + readInt(unsignedShort + 12);
            }
            unsignedShort += 8;
        }
        int i2 = unsignedShort + 2;
        for (int unsignedShort4 = readUnsignedShort(i2); unsignedShort4 > 0; unsignedShort4--) {
            for (int unsignedShort5 = readUnsignedShort(i2 + 8); unsignedShort5 > 0; unsignedShort5--) {
                i2 += 6 + readInt(i2 + 12);
            }
            i2 += 8;
        }
        return i2 + 2;
    }

    private Attribute readAttribute(Attribute[] attributeArr, String str, int i2, int i3, char[] cArr, int i4, Label[] labelArr) {
        for (int i5 = 0; i5 < attributeArr.length; i5++) {
            if (attributeArr[i5].type.equals(str)) {
                return attributeArr[i5].read(this, i2, i3, cArr, i4, labelArr);
            }
        }
        return new Attribute(str).read(this, i2, i3, null, -1, null);
    }

    public int getItemCount() {
        return this.items.length;
    }

    public int getItem(int i2) {
        return this.items[i2];
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public int readByte(int i2) {
        return this.f12859b[i2] & 255;
    }

    public int readUnsignedShort(int i2) {
        byte[] bArr = this.f12859b;
        return ((bArr[i2] & 255) << 8) | (bArr[i2 + 1] & 255);
    }

    public short readShort(int i2) {
        byte[] bArr = this.f12859b;
        return (short) (((bArr[i2] & 255) << 8) | (bArr[i2 + 1] & 255));
    }

    public int readInt(int i2) {
        byte[] bArr = this.f12859b;
        return ((bArr[i2] & 255) << 24) | ((bArr[i2 + 1] & 255) << 16) | ((bArr[i2 + 2] & 255) << 8) | (bArr[i2 + 3] & 255);
    }

    public long readLong(int i2) {
        return (readInt(i2) << 32) | (readInt(i2 + 4) & 4294967295L);
    }

    public String readUTF8(int i2, char[] cArr) {
        int unsignedShort = readUnsignedShort(i2);
        if (i2 == 0 || unsignedShort == 0) {
            return null;
        }
        String str = this.strings[unsignedShort];
        if (str != null) {
            return str;
        }
        int i3 = this.items[unsignedShort];
        String[] strArr = this.strings;
        String utf = readUTF(i3 + 2, readUnsignedShort(i3), cArr);
        strArr[unsignedShort] = utf;
        return utf;
    }

    private String readUTF(int i2, int i3, char[] cArr) {
        int i4 = i2 + i3;
        byte[] bArr = this.f12859b;
        int i5 = 0;
        boolean z2 = false;
        char c2 = 0;
        while (i2 < i4) {
            int i6 = i2;
            i2++;
            byte b2 = bArr[i6];
            switch (z2) {
                case false:
                    int i7 = b2 & 255;
                    if (i7 < 128) {
                        int i8 = i5;
                        i5++;
                        cArr[i8] = (char) i7;
                        break;
                    } else if (i7 < 224 && i7 > 191) {
                        c2 = (char) (i7 & 31);
                        z2 = true;
                        break;
                    } else {
                        c2 = (char) (i7 & 15);
                        z2 = 2;
                        break;
                    }
                case true:
                    int i9 = i5;
                    i5++;
                    cArr[i9] = (char) ((c2 << 6) | (b2 & 63));
                    z2 = false;
                    break;
                case true:
                    c2 = (char) ((c2 << 6) | (b2 & 63));
                    z2 = true;
                    break;
            }
        }
        return new String(cArr, 0, i5);
    }

    public String readClass(int i2, char[] cArr) {
        return readUTF8(this.items[readUnsignedShort(i2)], cArr);
    }

    public Object readConst(int i2, char[] cArr) {
        int i3 = this.items[i2];
        switch (this.f12859b[i3 - 1]) {
            case 3:
                return Integer.valueOf(readInt(i3));
            case 4:
                return Float.valueOf(Float.intBitsToFloat(readInt(i3)));
            case 5:
                return Long.valueOf(readLong(i3));
            case 6:
                return Double.valueOf(Double.longBitsToDouble(readLong(i3)));
            case 7:
                return Type.getObjectType(readUTF8(i3, cArr));
            case 8:
                return readUTF8(i3, cArr);
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                int i4 = readByte(i3);
                int[] iArr = this.items;
                int i5 = iArr[readUnsignedShort(i3 + 1)];
                String str = readClass(i5, cArr);
                int i6 = iArr[readUnsignedShort(i5 + 2)];
                return new Handle(i4, str, readUTF8(i6, cArr), readUTF8(i6 + 2, cArr));
            case 16:
                return Type.getMethodType(readUTF8(i3, cArr));
        }
    }
}
