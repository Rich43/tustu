package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.Attribute;
import com.sun.java.util.jar.pack.ConstantPool;
import com.sun.java.util.jar.pack.Package;
import com.sun.java.util.jar.pack.Package.Class.Field;
import com.sun.java.util.jar.pack.Package.Class.Method;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.jar.Pack200;
import javax.swing.plaf.nimbus.NimbusStyle;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/ClassReader.class */
class ClassReader {
    int verbose;
    Package pkg;
    Package.Class cls;
    long inPos;
    DataInputStream in;
    Map<Attribute.Layout, Attribute> attrDefs;
    Map<Attribute.Layout, String> attrCommands;
    private static final ConstantPool.Entry INVALID_ENTRY;
    boolean haveUnresolvedEntry;
    static final /* synthetic */ boolean $assertionsDisabled;
    long constantPoolLimit = -1;
    String unknownAttrCommand = Pack200.Packer.ERROR;

    static {
        $assertionsDisabled = !ClassReader.class.desiredAssertionStatus();
        INVALID_ENTRY = new ConstantPool.Entry((byte) -1) { // from class: com.sun.java.util.jar.pack.ClassReader.2
            @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
            public boolean equals(Object obj) {
                throw new IllegalStateException("Should not call this");
            }

            @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
            protected int computeValueHash() {
                throw new IllegalStateException("Should not call this");
            }

            @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
            public int compareTo(Object obj) {
                throw new IllegalStateException("Should not call this");
            }

            @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
            public String stringValue() {
                throw new IllegalStateException("Should not call this");
            }
        };
    }

    ClassReader(Package.Class r9, InputStream inputStream) throws IOException {
        this.pkg = r9.getPackage();
        this.cls = r9;
        this.verbose = this.pkg.verbose;
        this.in = new DataInputStream(new FilterInputStream(inputStream) { // from class: com.sun.java.util.jar.pack.ClassReader.1
            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read(byte[] bArr, int i2, int i3) throws IOException {
                int i4 = super.read(bArr, i2, i3);
                if (i4 >= 0) {
                    ClassReader.this.inPos += i4;
                }
                return i4;
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public int read() throws IOException {
                int i2 = super.read();
                if (i2 >= 0) {
                    ClassReader.this.inPos++;
                }
                return i2;
            }

            @Override // java.io.FilterInputStream, java.io.InputStream
            public long skip(long j2) throws IOException {
                long jSkip = super.skip(j2);
                if (jSkip >= 0) {
                    ClassReader.this.inPos += jSkip;
                }
                return jSkip;
            }
        });
    }

    public void setAttrDefs(Map<Attribute.Layout, Attribute> map) {
        this.attrDefs = map;
    }

    public void setAttrCommands(Map<Attribute.Layout, String> map) {
        this.attrCommands = map;
    }

    private void skip(int i2, String str) throws IOException {
        Utils.log.warning("skipping " + i2 + " bytes of " + str);
        long j2 = 0;
        while (true) {
            long j3 = j2;
            if (j3 < i2) {
                long jSkip = this.in.skip(i2 - j3);
                if (!$assertionsDisabled && jSkip <= 0) {
                    throw new AssertionError();
                }
                j2 = j3 + jSkip;
            } else {
                if (!$assertionsDisabled && j3 != i2) {
                    throw new AssertionError();
                }
                return;
            }
        }
    }

    private int readUnsignedShort() throws IOException {
        return this.in.readUnsignedShort();
    }

    private int readInt() throws IOException {
        return this.in.readInt();
    }

    private ConstantPool.Entry readRef() throws IOException {
        int unsignedShort = this.in.readUnsignedShort();
        if (unsignedShort == 0) {
            return null;
        }
        return this.cls.cpMap[unsignedShort];
    }

    private ConstantPool.Entry readRef(byte b2) throws IOException {
        ConstantPool.Entry ref = readRef();
        if (!$assertionsDisabled && (ref instanceof UnresolvedEntry)) {
            throw new AssertionError();
        }
        checkTag(ref, b2);
        return ref;
    }

    private ConstantPool.Entry checkValid(ConstantPool.Entry entry) {
        if (entry == INVALID_ENTRY) {
            throw new IllegalStateException("Invalid constant pool reference");
        }
        return entry;
    }

    private ConstantPool.Entry checkTag(ConstantPool.Entry entry, byte b2) throws ClassFormatException {
        if (entry == null || !entry.tagMatches(b2)) {
            throw new ClassFormatException("Bad constant, expected type=" + ConstantPool.tagName(b2) + " got " + (entry == null ? "null CP index" : "type=" + ConstantPool.tagName(entry.tag)) + ", in File: " + this.cls.file.nameString + (this.inPos == this.constantPoolLimit ? " in constant pool" : " at pos: " + this.inPos));
        }
        return entry;
    }

    private ConstantPool.Entry checkTag(ConstantPool.Entry entry, byte b2, boolean z2) throws ClassFormatException {
        if (z2 && entry == null) {
            return null;
        }
        return checkTag(entry, b2);
    }

    private ConstantPool.Entry readRefOrNull(byte b2) throws IOException {
        ConstantPool.Entry ref = readRef();
        checkTag(ref, b2, true);
        return ref;
    }

    private ConstantPool.Utf8Entry readUtf8Ref() throws IOException {
        return (ConstantPool.Utf8Entry) readRef((byte) 1);
    }

    private ConstantPool.ClassEntry readClassRef() throws IOException {
        return (ConstantPool.ClassEntry) readRef((byte) 7);
    }

    private ConstantPool.ClassEntry readClassRefOrNull() throws IOException {
        return (ConstantPool.ClassEntry) readRefOrNull((byte) 7);
    }

    private ConstantPool.SignatureEntry readSignatureRef() throws IOException {
        ConstantPool.Entry ref = readRef((byte) 13);
        return (ref == null || ref.getTag() != 1) ? (ConstantPool.SignatureEntry) ref : ConstantPool.getSignatureEntry(ref.stringValue());
    }

    void read() throws IOException {
        try {
            readMagicNumbers();
            readConstantPool();
            readHeader();
            readMembers(false);
            readMembers(true);
            readAttributes(0, this.cls);
            fixUnresolvedEntries();
            this.cls.finishReading();
            if (!$assertionsDisabled && 0 < this.in.read(new byte[1])) {
                throw new AssertionError();
            }
            if (1 != 0 || this.verbose <= 0) {
                return;
            }
            Utils.log.warning("Erroneous data at input offset " + this.inPos + " of " + ((Object) this.cls.file));
        } catch (Throwable th) {
            if (0 == 0 && this.verbose > 0) {
                Utils.log.warning("Erroneous data at input offset " + this.inPos + " of " + ((Object) this.cls.file));
            }
            throw th;
        }
    }

    void readMagicNumbers() throws IOException {
        this.cls.magic = this.in.readInt();
        if (this.cls.magic != -889275714) {
            throw new Attribute.FormatException("Bad magic number in class file " + Integer.toHexString(this.cls.magic), 0, "magic-number", Pack200.Packer.PASS);
        }
        short unsignedShort = (short) readUnsignedShort();
        this.cls.version = Package.Version.of((short) readUnsignedShort(), unsignedShort);
        String strCheckVersion = checkVersion(this.cls.version);
        if (strCheckVersion != null) {
            throw new Attribute.FormatException("classfile version too " + strCheckVersion + ": " + ((Object) this.cls.version) + " in " + ((Object) this.cls.file), 0, "version", Pack200.Packer.PASS);
        }
    }

    private String checkVersion(Package.Version version) {
        short s2 = version.major;
        short s3 = version.minor;
        if (s2 < this.pkg.minClassVersion.major) {
            return NimbusStyle.SMALL_KEY;
        }
        if (s2 == this.pkg.minClassVersion.major && s3 < this.pkg.minClassVersion.minor) {
            return NimbusStyle.SMALL_KEY;
        }
        if (s2 > this.pkg.maxClassVersion.major) {
            return NimbusStyle.LARGE_KEY;
        }
        if (s2 == this.pkg.maxClassVersion.major && s3 > this.pkg.maxClassVersion.minor) {
            return NimbusStyle.LARGE_KEY;
        }
        return null;
    }

    void readConstantPool() throws IOException {
        int unsignedShort = this.in.readUnsignedShort();
        int[] iArr = new int[unsignedShort * 4];
        int i2 = 0;
        ConstantPool.Entry[] entryArr = new ConstantPool.Entry[unsignedShort];
        entryArr[0] = INVALID_ENTRY;
        int i3 = 1;
        while (i3 < unsignedShort) {
            byte b2 = this.in.readByte();
            switch (b2) {
                case 1:
                    entryArr[i3] = ConstantPool.getUtf8Entry(this.in.readUTF());
                    break;
                case 2:
                case 13:
                case 14:
                case 17:
                default:
                    throw new ClassFormatException("Bad constant pool tag " + ((int) b2) + " in File: " + this.cls.file.nameString + " at pos: " + this.inPos);
                case 3:
                    entryArr[i3] = ConstantPool.getLiteralEntry(Integer.valueOf(this.in.readInt()));
                    break;
                case 4:
                    entryArr[i3] = ConstantPool.getLiteralEntry(Float.valueOf(this.in.readFloat()));
                    break;
                case 5:
                    entryArr[i3] = ConstantPool.getLiteralEntry(Long.valueOf(this.in.readLong()));
                    i3++;
                    entryArr[i3] = INVALID_ENTRY;
                    break;
                case 6:
                    entryArr[i3] = ConstantPool.getLiteralEntry(Double.valueOf(this.in.readDouble()));
                    i3++;
                    entryArr[i3] = INVALID_ENTRY;
                    break;
                case 7:
                case 8:
                case 16:
                    int i4 = i2;
                    int i5 = i2 + 1;
                    iArr[i4] = i3;
                    int i6 = i5 + 1;
                    iArr[i5] = b2;
                    int i7 = i6 + 1;
                    iArr[i6] = this.in.readUnsignedShort();
                    i2 = i7 + 1;
                    iArr[i7] = -1;
                    break;
                case 9:
                case 10:
                case 11:
                case 12:
                    int i8 = i2;
                    int i9 = i2 + 1;
                    iArr[i8] = i3;
                    int i10 = i9 + 1;
                    iArr[i9] = b2;
                    int i11 = i10 + 1;
                    iArr[i10] = this.in.readUnsignedShort();
                    i2 = i11 + 1;
                    iArr[i11] = this.in.readUnsignedShort();
                    break;
                case 15:
                    int i12 = i2;
                    int i13 = i2 + 1;
                    iArr[i12] = i3;
                    int i14 = i13 + 1;
                    iArr[i13] = b2;
                    int i15 = i14 + 1;
                    iArr[i14] = (-1) ^ this.in.readUnsignedByte();
                    i2 = i15 + 1;
                    iArr[i15] = this.in.readUnsignedShort();
                    break;
                case 18:
                    int i16 = i2;
                    int i17 = i2 + 1;
                    iArr[i16] = i3;
                    int i18 = i17 + 1;
                    iArr[i17] = b2;
                    int i19 = i18 + 1;
                    iArr[i18] = (-1) ^ this.in.readUnsignedShort();
                    i2 = i19 + 1;
                    iArr[i19] = this.in.readUnsignedShort();
                    break;
            }
            i3++;
        }
        this.constantPoolLimit = this.inPos;
        while (i2 > 0) {
            if (this.verbose > 3) {
                Utils.log.fine("CP fixups [" + (i2 / 4) + "]");
            }
            int i20 = i2;
            i2 = 0;
            int i21 = 0;
            while (i21 < i20) {
                int i22 = i21;
                int i23 = i21 + 1;
                int i24 = iArr[i22];
                int i25 = i23 + 1;
                int i26 = iArr[i23];
                int i27 = i25 + 1;
                int i28 = iArr[i25];
                i21 = i27 + 1;
                int i29 = iArr[i27];
                if (this.verbose > 3) {
                    Utils.log.fine("  cp[" + i24 + "] = " + ConstantPool.tagName(i26) + VectorFormat.DEFAULT_PREFIX + i28 + "," + i29 + "}");
                }
                if ((i28 >= 0 && checkValid(entryArr[i28]) == null) || (i29 >= 0 && checkValid(entryArr[i29]) == null)) {
                    int i30 = i2;
                    int i31 = i2 + 1;
                    iArr[i30] = i24;
                    int i32 = i31 + 1;
                    iArr[i31] = i26;
                    int i33 = i32 + 1;
                    iArr[i32] = i28;
                    i2 = i33 + 1;
                    iArr[i33] = i29;
                } else {
                    switch (i26) {
                        case 7:
                            entryArr[i24] = ConstantPool.getClassEntry(entryArr[i28].stringValue());
                            break;
                        case 8:
                            entryArr[i24] = ConstantPool.getStringEntry(entryArr[i28].stringValue());
                            break;
                        case 9:
                        case 10:
                        case 11:
                            entryArr[i24] = ConstantPool.getMemberEntry((byte) i26, (ConstantPool.ClassEntry) checkTag(entryArr[i28], (byte) 7), (ConstantPool.DescriptorEntry) checkTag(entryArr[i29], (byte) 12));
                            break;
                        case 12:
                            entryArr[i24] = ConstantPool.getDescriptorEntry((ConstantPool.Utf8Entry) checkTag(entryArr[i28], (byte) 1), (ConstantPool.Utf8Entry) checkTag(entryArr[i29], (byte) 13));
                            break;
                        case 13:
                        case 14:
                        case 17:
                        default:
                            if (!$assertionsDisabled) {
                                throw new AssertionError();
                            }
                            break;
                        case 15:
                            entryArr[i24] = ConstantPool.getMethodHandleEntry((byte) ((-1) ^ i28), (ConstantPool.MemberEntry) checkTag(entryArr[i29], (byte) 52));
                            break;
                        case 16:
                            entryArr[i24] = ConstantPool.getMethodTypeEntry((ConstantPool.Utf8Entry) checkTag(entryArr[i28], (byte) 13));
                            break;
                        case 18:
                            entryArr[i24] = new UnresolvedEntry((byte) i26, Integer.valueOf((-1) ^ i28), (ConstantPool.DescriptorEntry) checkTag(entryArr[i29], (byte) 12));
                            break;
                    }
                }
            }
            if (!$assertionsDisabled && i2 >= i20) {
                throw new AssertionError();
            }
        }
        this.cls.cpMap = entryArr;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ClassReader$UnresolvedEntry.class */
    private class UnresolvedEntry extends ConstantPool.Entry {
        final Object[] refsOrIndexes;

        UnresolvedEntry(byte b2, Object... objArr) {
            super(b2);
            this.refsOrIndexes = objArr;
            ClassReader.this.haveUnresolvedEntry = true;
        }

        ConstantPool.Entry resolve() {
            Package.Class r0 = ClassReader.this.cls;
            switch (this.tag) {
                case 18:
                    return ConstantPool.getInvokeDynamicEntry(r0.bootstrapMethods.get(((Integer) this.refsOrIndexes[0]).intValue()), (ConstantPool.DescriptorEntry) this.refsOrIndexes[1]);
                default:
                    throw new AssertionError();
            }
        }

        private void unresolved() {
            throw new RuntimeException("unresolved entry has no string");
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry, java.lang.Comparable
        public int compareTo(Object obj) {
            unresolved();
            return 0;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public boolean equals(Object obj) {
            unresolved();
            return false;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        protected int computeValueHash() {
            unresolved();
            return 0;
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String stringValue() {
            unresolved();
            return toString();
        }

        @Override // com.sun.java.util.jar.pack.ConstantPool.Entry
        public String toString() {
            return "(unresolved " + ConstantPool.tagName(this.tag) + ")";
        }
    }

    private void fixUnresolvedEntries() {
        if (this.haveUnresolvedEntry) {
            ConstantPool.Entry[] cPMap = this.cls.getCPMap();
            for (int i2 = 0; i2 < cPMap.length; i2++) {
                ConstantPool.Entry entry = cPMap[i2];
                if (entry instanceof UnresolvedEntry) {
                    ConstantPool.Entry entryResolve = ((UnresolvedEntry) entry).resolve();
                    cPMap[i2] = entryResolve;
                    if (!$assertionsDisabled && (entryResolve instanceof UnresolvedEntry)) {
                        throw new AssertionError();
                    }
                }
            }
            this.haveUnresolvedEntry = false;
        }
    }

    void readHeader() throws IOException {
        this.cls.flags = readUnsignedShort();
        this.cls.thisClass = readClassRef();
        this.cls.superClass = readClassRefOrNull();
        int unsignedShort = readUnsignedShort();
        this.cls.interfaces = new ConstantPool.ClassEntry[unsignedShort];
        for (int i2 = 0; i2 < unsignedShort; i2++) {
            this.cls.interfaces[i2] = readClassRef();
        }
    }

    void readMembers(boolean z2) throws IOException {
        int unsignedShort = readUnsignedShort();
        for (int i2 = 0; i2 < unsignedShort; i2++) {
            readMember(z2);
        }
    }

    void readMember(boolean z2) throws IOException {
        Attribute.Holder method;
        int unsignedShort = readUnsignedShort();
        ConstantPool.DescriptorEntry descriptorEntry = ConstantPool.getDescriptorEntry(readUtf8Ref(), readSignatureRef());
        if (!z2) {
            Package.Class r2 = this.cls;
            r2.getClass();
            method = r2.new Field(unsignedShort, descriptorEntry);
        } else {
            Package.Class r22 = this.cls;
            r22.getClass();
            method = r22.new Method(unsignedShort, descriptorEntry);
        }
        readAttributes(!z2 ? 1 : 2, method);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:16:0x0081. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0155  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void readAttributes(int r8, com.sun.java.util.jar.pack.Attribute.Holder r9) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1158
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.ClassReader.readAttributes(int, com.sun.java.util.jar.pack.Attribute$Holder):void");
    }

    void readCode(Code code) throws IOException {
        code.max_stack = readUnsignedShort();
        code.max_locals = readUnsignedShort();
        code.bytes = new byte[readInt()];
        this.in.readFully(code.bytes);
        Instruction.opcodeChecker(code.bytes, this.cls.getCPMap(), this.cls.version);
        int unsignedShort = readUnsignedShort();
        code.setHandlerCount(unsignedShort);
        for (int i2 = 0; i2 < unsignedShort; i2++) {
            code.handler_start[i2] = readUnsignedShort();
            code.handler_end[i2] = readUnsignedShort();
            code.handler_catch[i2] = readUnsignedShort();
            code.handler_class[i2] = readClassRefOrNull();
        }
        readAttributes(3, code);
    }

    void readBootstrapMethods(Package.Class r6) throws IOException {
        ConstantPool.BootstrapMethodEntry[] bootstrapMethodEntryArr = new ConstantPool.BootstrapMethodEntry[readUnsignedShort()];
        for (int i2 = 0; i2 < bootstrapMethodEntryArr.length; i2++) {
            ConstantPool.MethodHandleEntry methodHandleEntry = (ConstantPool.MethodHandleEntry) readRef((byte) 15);
            ConstantPool.Entry[] entryArr = new ConstantPool.Entry[readUnsignedShort()];
            for (int i3 = 0; i3 < entryArr.length; i3++) {
                entryArr[i3] = readRef((byte) 51);
            }
            bootstrapMethodEntryArr[i2] = ConstantPool.getBootstrapMethodEntry(methodHandleEntry, entryArr);
        }
        r6.setBootstrapMethods(Arrays.asList(bootstrapMethodEntryArr));
    }

    void readInnerClasses(Package.Class r8) throws IOException {
        int unsignedShort = readUnsignedShort();
        ArrayList<Package.InnerClass> arrayList = new ArrayList<>(unsignedShort);
        for (int i2 = 0; i2 < unsignedShort; i2++) {
            arrayList.add(new Package.InnerClass(readClassRef(), readClassRefOrNull(), (ConstantPool.Utf8Entry) readRefOrNull((byte) 1), readUnsignedShort()));
        }
        r8.innerClasses = arrayList;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/ClassReader$ClassFormatException.class */
    static class ClassFormatException extends IOException {
        private static final long serialVersionUID = -3564121733989501833L;

        public ClassFormatException(String str) {
            super(str);
        }

        public ClassFormatException(String str, Throwable th) {
            super(str, th);
        }
    }
}
