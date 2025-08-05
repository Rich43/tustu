package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.Attribute;
import com.sun.java.util.jar.pack.BandStructure;
import com.sun.java.util.jar.pack.ConstantPool;
import com.sun.java.util.jar.pack.Instruction;
import com.sun.java.util.jar.pack.Package;
import com.sun.java.util.jar.pack.Package.Class;
import com.sun.java.util.jar.pack.Package.Class.Field;
import com.sun.java.util.jar.pack.Package.Class.Method;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/PackageReader.class */
class PackageReader extends BandStructure {
    Package pkg;
    byte[] bytes;
    LimitedBuffer in;
    Package.Version packageVersion;
    int numFiles;
    int numAttrDefs;
    int numInnerClasses;
    int numClasses;
    static final int MAGIC_BYTES = 4;
    Map<ConstantPool.Utf8Entry, ConstantPool.SignatureEntry> utf8Signatures;
    static final int NO_FLAGS_YET = 0;
    Code[] allCodes;
    List<Code> codesWithFlags;
    static final /* synthetic */ boolean $assertionsDisabled;
    int[] tagCount = new int[19];
    Comparator<ConstantPool.Entry> entryOutputOrder = new Comparator<ConstantPool.Entry>() { // from class: com.sun.java.util.jar.pack.PackageReader.1
        @Override // java.util.Comparator
        public int compare(ConstantPool.Entry entry, ConstantPool.Entry entry2) {
            int outputIndex = PackageReader.this.getOutputIndex(entry);
            int outputIndex2 = PackageReader.this.getOutputIndex(entry2);
            if (outputIndex >= 0 && outputIndex2 >= 0) {
                return outputIndex - outputIndex2;
            }
            if (outputIndex == outputIndex2) {
                return entry.compareTo(entry2);
            }
            return outputIndex >= 0 ? -1 : 1;
        }
    };
    Map<Package.Class, Set<ConstantPool.Entry>> ldcRefMap = new HashMap();

    static {
        $assertionsDisabled = !PackageReader.class.desiredAssertionStatus();
    }

    PackageReader(Package r6, InputStream inputStream) throws IOException {
        this.pkg = r6;
        this.in = new LimitedBuffer(inputStream);
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/PackageReader$LimitedBuffer.class */
    static class LimitedBuffer extends BufferedInputStream {
        long served;
        int servedPos;
        long limit;
        long buffered;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !PackageReader.class.desiredAssertionStatus();
        }

        public boolean atLimit() {
            boolean z2 = getBytesServed() == this.limit;
            if ($assertionsDisabled || !z2 || this.limit == this.buffered) {
                return z2;
            }
            throw new AssertionError();
        }

        public long getBytesServed() {
            return this.served + (this.pos - this.servedPos);
        }

        public void setReadLimit(long j2) {
            if (j2 == -1) {
                this.limit = -1L;
            } else {
                this.limit = getBytesServed() + j2;
            }
        }

        public long getReadLimit() {
            if (this.limit == -1) {
                return this.limit;
            }
            return this.limit - getBytesServed();
        }

        @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            if (this.pos < this.count) {
                byte[] bArr = this.buf;
                int i2 = this.pos;
                this.pos = i2 + 1;
                return bArr[i2] & 255;
            }
            this.served += this.pos - this.servedPos;
            int i3 = super.read();
            this.servedPos = this.pos;
            if (i3 >= 0) {
                this.served++;
            }
            if ($assertionsDisabled || this.served <= this.limit || this.limit == -1) {
                return i3;
            }
            throw new AssertionError();
        }

        @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            this.served += this.pos - this.servedPos;
            int i4 = super.read(bArr, i2, i3);
            this.servedPos = this.pos;
            if (i4 >= 0) {
                this.served += i4;
            }
            return i4;
        }

        @Override // java.io.BufferedInputStream, java.io.FilterInputStream, java.io.InputStream
        public long skip(long j2) throws IOException {
            throw new RuntimeException("no skipping");
        }

        LimitedBuffer(InputStream inputStream) {
            super(null, 16384);
            this.servedPos = this.pos;
            this.in = new FilterInputStream(inputStream) { // from class: com.sun.java.util.jar.pack.PackageReader.LimitedBuffer.1
                @Override // java.io.FilterInputStream, java.io.InputStream
                public int read() throws IOException {
                    if (LimitedBuffer.this.buffered == LimitedBuffer.this.limit) {
                        return -1;
                    }
                    LimitedBuffer.this.buffered++;
                    return super.read();
                }

                @Override // java.io.FilterInputStream, java.io.InputStream
                public int read(byte[] bArr, int i2, int i3) throws IOException {
                    if (LimitedBuffer.this.buffered == LimitedBuffer.this.limit) {
                        return -1;
                    }
                    if (LimitedBuffer.this.limit != -1) {
                        long j2 = LimitedBuffer.this.limit - LimitedBuffer.this.buffered;
                        if (i3 > j2) {
                            i3 = (int) j2;
                        }
                    }
                    int i4 = super.read(bArr, i2, i3);
                    if (i4 >= 0) {
                        LimitedBuffer.this.buffered += i4;
                    }
                    return i4;
                }
            };
        }
    }

    void read() throws IOException {
        try {
            readFileHeader();
            readBandHeaders();
            readConstantPool();
            readAttrDefs();
            readInnerClasses();
            Package.Class[] classes = readClasses();
            readByteCodes();
            readFiles();
            if (!$assertionsDisabled && this.archiveSize1 != 0 && !this.in.atLimit()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.archiveSize1 != 0 && this.in.getBytesServed() != this.archiveSize0 + this.archiveSize1) {
                throw new AssertionError();
            }
            this.all_bands.doneDisbursing();
            for (Package.Class r0 : classes) {
                reconstructClass(r0);
            }
        } catch (Exception e2) {
            Utils.log.warning("Error on input: " + ((Object) e2), e2);
            if (this.verbose > 0) {
                Utils.log.info("Stream offsets: served=" + this.in.getBytesServed() + " buffered=" + this.in.buffered + " limit=" + this.in.limit);
            }
            if (e2 instanceof IOException) {
                throw ((IOException) e2);
            }
            if (!(e2 instanceof RuntimeException)) {
                throw new Error("error unpacking", e2);
            }
            throw ((RuntimeException) e2);
        }
    }

    void readFileHeader() throws IOException {
        readArchiveMagic();
        readArchiveHeader();
    }

    private int getMagicInt32() throws IOException {
        int i2 = 0;
        for (int i3 = 0; i3 < 4; i3++) {
            i2 = (i2 << 8) | (this.archive_magic.getByte() & 255);
        }
        return i2;
    }

    void readArchiveMagic() throws IOException {
        this.in.setReadLimit(19L);
        this.archive_magic.expectLength(4);
        this.archive_magic.readFrom(this.in);
        int magicInt32 = getMagicInt32();
        this.pkg.getClass();
        if (-889270259 != magicInt32) {
            StringBuilder sbAppend = new StringBuilder().append("Unexpected package magic number: got ").append(magicInt32).append("; expected ");
            this.pkg.getClass();
            throw new IOException(sbAppend.append(Constants.JAVA_PACKAGE_MAGIC).toString());
        }
        this.archive_magic.doneDisbursing();
    }

    void checkArchiveVersion() throws IOException {
        Package.Version version = null;
        Package.Version[] versionArr = {Constants.JAVA8_PACKAGE_VERSION, Constants.JAVA7_PACKAGE_VERSION, Constants.JAVA6_PACKAGE_VERSION, Constants.JAVA5_PACKAGE_VERSION};
        int length = versionArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            Package.Version version2 = versionArr[i2];
            if (!this.packageVersion.equals(version2)) {
                i2++;
            } else {
                version = version2;
                break;
            }
        }
        if (version == null) {
            throw new IOException("Unexpected package minor version: got " + this.packageVersion.toString() + "; expected " + (Constants.JAVA8_PACKAGE_VERSION.toString() + "OR" + Constants.JAVA7_PACKAGE_VERSION.toString() + " OR " + Constants.JAVA6_PACKAGE_VERSION.toString() + " OR " + Constants.JAVA5_PACKAGE_VERSION.toString()));
        }
    }

    void readArchiveHeader() throws IOException {
        this.archive_header_0.expectLength(3);
        this.archive_header_0.readFrom(this.in);
        this.packageVersion = Package.Version.of(this.archive_header_0.getInt(), this.archive_header_0.getInt());
        checkArchiveVersion();
        initHighestClassVersion(Constants.JAVA7_MAX_CLASS_VERSION);
        this.archiveOptions = this.archive_header_0.getInt();
        this.archive_header_0.doneDisbursing();
        boolean zTestBit = testBit(this.archiveOptions, 1);
        boolean zTestBit2 = testBit(this.archiveOptions, 16);
        boolean zTestBit3 = testBit(this.archiveOptions, 2);
        boolean zTestBit4 = testBit(this.archiveOptions, 8);
        initAttrIndexLimit();
        this.archive_header_S.expectLength(zTestBit2 ? 2 : 0);
        this.archive_header_S.readFrom(this.in);
        if (zTestBit2) {
            this.archiveSize1 = (this.archive_header_S.getInt() << 32) + ((this.archive_header_S.getInt() << 32) >>> 32);
            this.in.setReadLimit(this.archiveSize1);
        } else {
            this.archiveSize1 = 0L;
            this.in.setReadLimit(-1L);
        }
        this.archive_header_S.doneDisbursing();
        this.archiveSize0 = this.in.getBytesServed();
        int i2 = 10;
        if (zTestBit2) {
            i2 = 10 + 5;
        }
        if (zTestBit) {
            i2 += 2;
        }
        if (zTestBit3) {
            i2 += 4;
        }
        if (zTestBit4) {
            i2 += 4;
        }
        this.archive_header_1.expectLength(i2);
        this.archive_header_1.readFrom(this.in);
        if (zTestBit2) {
            this.archiveNextCount = this.archive_header_1.getInt();
            this.pkg.default_modtime = this.archive_header_1.getInt();
            this.numFiles = this.archive_header_1.getInt();
        } else {
            this.archiveNextCount = 0;
            this.numFiles = 0;
        }
        if (zTestBit) {
            this.band_headers.expectLength(this.archive_header_1.getInt());
            this.numAttrDefs = this.archive_header_1.getInt();
        } else {
            this.band_headers.expectLength(0);
            this.numAttrDefs = 0;
        }
        readConstantPoolCounts(zTestBit3, zTestBit4);
        this.numInnerClasses = this.archive_header_1.getInt();
        this.pkg.defaultClassVersion = Package.Version.of((short) this.archive_header_1.getInt(), (short) this.archive_header_1.getInt());
        this.numClasses = this.archive_header_1.getInt();
        this.archive_header_1.doneDisbursing();
        if (testBit(this.archiveOptions, 32)) {
            this.pkg.default_options |= 1;
        }
    }

    void readBandHeaders() throws IOException {
        this.band_headers.readFrom(this.in);
        this.bandHeaderBytePos = 1;
        this.bandHeaderBytes = new byte[this.bandHeaderBytePos + this.band_headers.length()];
        for (int i2 = this.bandHeaderBytePos; i2 < this.bandHeaderBytes.length; i2++) {
            this.bandHeaderBytes[i2] = (byte) this.band_headers.getByte();
        }
        this.band_headers.doneDisbursing();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void readConstantPoolCounts(boolean r5, boolean r6) throws java.io.IOException {
        /*
            r4 = this;
            r0 = 0
            r7 = r0
        L2:
            r0 = r7
            byte[] r1 = com.sun.java.util.jar.pack.ConstantPool.TAGS_IN_ORDER
            int r1 = r1.length
            if (r0 >= r1) goto L73
            byte[] r0 = com.sun.java.util.jar.pack.ConstantPool.TAGS_IN_ORDER
            r1 = r7
            r0 = r0[r1]
            r8 = r0
            r0 = r5
            if (r0 != 0) goto L37
            r0 = r8
            switch(r0) {
                case 3: goto L34;
                case 4: goto L34;
                case 5: goto L34;
                case 6: goto L34;
                default: goto L37;
            }
        L34:
            goto L6d
        L37:
            r0 = r6
            if (r0 != 0) goto L5f
            r0 = r8
            switch(r0) {
                case 15: goto L5c;
                case 16: goto L5c;
                case 17: goto L5c;
                case 18: goto L5c;
                default: goto L5f;
            }
        L5c:
            goto L6d
        L5f:
            r0 = r4
            int[] r0 = r0.tagCount
            r1 = r8
            r2 = r4
            com.sun.java.util.jar.pack.BandStructure$IntBand r2 = r2.archive_header_1
            int r2 = r2.getInt()
            r0[r1] = r2
        L6d:
            int r7 = r7 + 1
            goto L2
        L73:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.PackageReader.readConstantPoolCounts(boolean, boolean):void");
    }

    @Override // com.sun.java.util.jar.pack.BandStructure
    protected ConstantPool.Index getCPIndex(byte b2) {
        return this.pkg.cp.getIndexByTag(b2);
    }

    ConstantPool.Index initCPIndex(byte b2, ConstantPool.Entry[] entryArr) {
        if (this.verbose > 3) {
            for (ConstantPool.Entry entry : entryArr) {
                Utils.log.fine("cp.add " + ((Object) entry));
            }
        }
        ConstantPool.Index indexMakeIndex = ConstantPool.makeIndex(ConstantPool.tagName(b2), entryArr);
        if (this.verbose > 1) {
            Utils.log.fine("Read " + ((Object) indexMakeIndex));
        }
        this.pkg.cp.initIndexByTag(b2, indexMakeIndex);
        return indexMakeIndex;
    }

    void checkLegacy(String str) {
        if (this.packageVersion.lessThan(Constants.JAVA7_PACKAGE_VERSION)) {
            throw new RuntimeException("unexpected band " + str);
        }
    }

    void readConstantPool() throws IOException {
        PrintStream printStream;
        if (this.verbose > 0) {
            Utils.log.info("Reading CP");
        }
        for (int i2 = 0; i2 < ConstantPool.TAGS_IN_ORDER.length; i2++) {
            byte b2 = ConstantPool.TAGS_IN_ORDER[i2];
            ConstantPool.Entry[] entryArr = new ConstantPool.Entry[this.tagCount[b2]];
            if (this.verbose > 0) {
                Utils.log.info("Reading " + entryArr.length + " " + ConstantPool.tagName(b2) + " entries...");
            }
            switch (b2) {
                case 1:
                    readUtf8Bands(entryArr);
                    break;
                case 2:
                case 14:
                default:
                    throw new AssertionError((Object) "unexpected CP tag in package");
                case 3:
                    this.cp_Int.expectLength(entryArr.length);
                    this.cp_Int.readFrom(this.in);
                    for (int i3 = 0; i3 < entryArr.length; i3++) {
                        entryArr[i3] = ConstantPool.getLiteralEntry(Integer.valueOf(this.cp_Int.getInt()));
                    }
                    this.cp_Int.doneDisbursing();
                    break;
                case 4:
                    this.cp_Float.expectLength(entryArr.length);
                    this.cp_Float.readFrom(this.in);
                    for (int i4 = 0; i4 < entryArr.length; i4++) {
                        entryArr[i4] = ConstantPool.getLiteralEntry(Float.valueOf(Float.intBitsToFloat(this.cp_Float.getInt())));
                    }
                    this.cp_Float.doneDisbursing();
                    break;
                case 5:
                    this.cp_Long_hi.expectLength(entryArr.length);
                    this.cp_Long_hi.readFrom(this.in);
                    this.cp_Long_lo.expectLength(entryArr.length);
                    this.cp_Long_lo.readFrom(this.in);
                    for (int i5 = 0; i5 < entryArr.length; i5++) {
                        entryArr[i5] = ConstantPool.getLiteralEntry(Long.valueOf((this.cp_Long_hi.getInt() << 32) + ((this.cp_Long_lo.getInt() << 32) >>> 32)));
                    }
                    this.cp_Long_hi.doneDisbursing();
                    this.cp_Long_lo.doneDisbursing();
                    break;
                case 6:
                    this.cp_Double_hi.expectLength(entryArr.length);
                    this.cp_Double_hi.readFrom(this.in);
                    this.cp_Double_lo.expectLength(entryArr.length);
                    this.cp_Double_lo.readFrom(this.in);
                    for (int i6 = 0; i6 < entryArr.length; i6++) {
                        entryArr[i6] = ConstantPool.getLiteralEntry(Double.valueOf(Double.longBitsToDouble((this.cp_Double_hi.getInt() << 32) + ((this.cp_Double_lo.getInt() << 32) >>> 32))));
                    }
                    this.cp_Double_hi.doneDisbursing();
                    this.cp_Double_lo.doneDisbursing();
                    break;
                case 7:
                    this.cp_Class.expectLength(entryArr.length);
                    this.cp_Class.readFrom(this.in);
                    this.cp_Class.setIndex(getCPIndex((byte) 1));
                    for (int i7 = 0; i7 < entryArr.length; i7++) {
                        entryArr[i7] = ConstantPool.getClassEntry(this.cp_Class.getRef().stringValue());
                    }
                    this.cp_Class.doneDisbursing();
                    break;
                case 8:
                    this.cp_String.expectLength(entryArr.length);
                    this.cp_String.readFrom(this.in);
                    this.cp_String.setIndex(getCPIndex((byte) 1));
                    for (int i8 = 0; i8 < entryArr.length; i8++) {
                        entryArr[i8] = ConstantPool.getLiteralEntry(this.cp_String.getRef().stringValue());
                    }
                    this.cp_String.doneDisbursing();
                    break;
                case 9:
                    readMemberRefs(b2, entryArr, this.cp_Field_class, this.cp_Field_desc);
                    break;
                case 10:
                    readMemberRefs(b2, entryArr, this.cp_Method_class, this.cp_Method_desc);
                    break;
                case 11:
                    readMemberRefs(b2, entryArr, this.cp_Imethod_class, this.cp_Imethod_desc);
                    break;
                case 12:
                    this.cp_Descr_name.expectLength(entryArr.length);
                    this.cp_Descr_name.readFrom(this.in);
                    this.cp_Descr_name.setIndex(getCPIndex((byte) 1));
                    this.cp_Descr_type.expectLength(entryArr.length);
                    this.cp_Descr_type.readFrom(this.in);
                    this.cp_Descr_type.setIndex(getCPIndex((byte) 13));
                    for (int i9 = 0; i9 < entryArr.length; i9++) {
                        entryArr[i9] = ConstantPool.getDescriptorEntry((ConstantPool.Utf8Entry) this.cp_Descr_name.getRef(), (ConstantPool.SignatureEntry) this.cp_Descr_type.getRef());
                    }
                    this.cp_Descr_name.doneDisbursing();
                    this.cp_Descr_type.doneDisbursing();
                    break;
                case 13:
                    readSignatureBands(entryArr);
                    break;
                case 15:
                    if (entryArr.length > 0) {
                        checkLegacy(this.cp_MethodHandle_refkind.name());
                    }
                    this.cp_MethodHandle_refkind.expectLength(entryArr.length);
                    this.cp_MethodHandle_refkind.readFrom(this.in);
                    this.cp_MethodHandle_member.expectLength(entryArr.length);
                    this.cp_MethodHandle_member.readFrom(this.in);
                    this.cp_MethodHandle_member.setIndex(getCPIndex((byte) 52));
                    for (int i10 = 0; i10 < entryArr.length; i10++) {
                        entryArr[i10] = ConstantPool.getMethodHandleEntry((byte) this.cp_MethodHandle_refkind.getInt(), (ConstantPool.MemberEntry) this.cp_MethodHandle_member.getRef());
                    }
                    this.cp_MethodHandle_refkind.doneDisbursing();
                    this.cp_MethodHandle_member.doneDisbursing();
                    break;
                case 16:
                    if (entryArr.length > 0) {
                        checkLegacy(this.cp_MethodType.name());
                    }
                    this.cp_MethodType.expectLength(entryArr.length);
                    this.cp_MethodType.readFrom(this.in);
                    this.cp_MethodType.setIndex(getCPIndex((byte) 13));
                    for (int i11 = 0; i11 < entryArr.length; i11++) {
                        entryArr[i11] = ConstantPool.getMethodTypeEntry((ConstantPool.SignatureEntry) this.cp_MethodType.getRef());
                    }
                    this.cp_MethodType.doneDisbursing();
                    break;
                case 17:
                    if (entryArr.length > 0) {
                        checkLegacy(this.cp_BootstrapMethod_ref.name());
                    }
                    this.cp_BootstrapMethod_ref.expectLength(entryArr.length);
                    this.cp_BootstrapMethod_ref.readFrom(this.in);
                    this.cp_BootstrapMethod_ref.setIndex(getCPIndex((byte) 15));
                    this.cp_BootstrapMethod_arg_count.expectLength(entryArr.length);
                    this.cp_BootstrapMethod_arg_count.readFrom(this.in);
                    this.cp_BootstrapMethod_arg.expectLength(this.cp_BootstrapMethod_arg_count.getIntTotal());
                    this.cp_BootstrapMethod_arg.readFrom(this.in);
                    this.cp_BootstrapMethod_arg.setIndex(getCPIndex((byte) 51));
                    for (int i12 = 0; i12 < entryArr.length; i12++) {
                        ConstantPool.MethodHandleEntry methodHandleEntry = (ConstantPool.MethodHandleEntry) this.cp_BootstrapMethod_ref.getRef();
                        int i13 = this.cp_BootstrapMethod_arg_count.getInt();
                        ConstantPool.Entry[] entryArr2 = new ConstantPool.Entry[i13];
                        for (int i14 = 0; i14 < i13; i14++) {
                            entryArr2[i14] = this.cp_BootstrapMethod_arg.getRef();
                        }
                        entryArr[i12] = ConstantPool.getBootstrapMethodEntry(methodHandleEntry, entryArr2);
                    }
                    this.cp_BootstrapMethod_ref.doneDisbursing();
                    this.cp_BootstrapMethod_arg_count.doneDisbursing();
                    this.cp_BootstrapMethod_arg.doneDisbursing();
                    break;
                case 18:
                    if (entryArr.length > 0) {
                        checkLegacy(this.cp_InvokeDynamic_spec.name());
                    }
                    this.cp_InvokeDynamic_spec.expectLength(entryArr.length);
                    this.cp_InvokeDynamic_spec.readFrom(this.in);
                    this.cp_InvokeDynamic_spec.setIndex(getCPIndex((byte) 17));
                    this.cp_InvokeDynamic_desc.expectLength(entryArr.length);
                    this.cp_InvokeDynamic_desc.readFrom(this.in);
                    this.cp_InvokeDynamic_desc.setIndex(getCPIndex((byte) 12));
                    for (int i15 = 0; i15 < entryArr.length; i15++) {
                        entryArr[i15] = ConstantPool.getInvokeDynamicEntry((ConstantPool.BootstrapMethodEntry) this.cp_InvokeDynamic_spec.getRef(), (ConstantPool.DescriptorEntry) this.cp_InvokeDynamic_desc.getRef());
                    }
                    this.cp_InvokeDynamic_spec.doneDisbursing();
                    this.cp_InvokeDynamic_desc.doneDisbursing();
                    break;
            }
            ConstantPool.Index indexInitCPIndex = initCPIndex(b2, entryArr);
            if (this.optDumpBands) {
                printStream = new PrintStream(getDumpStream(indexInitCPIndex, ".idx"));
                Throwable th = null;
                try {
                    try {
                        printArrayTo(printStream, indexInitCPIndex.cpMap, 0, indexInitCPIndex.cpMap.length);
                        if (printStream != null) {
                            if (0 != 0) {
                                try {
                                    printStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                printStream.close();
                            }
                        }
                    } finally {
                    }
                } finally {
                }
            }
        }
        this.cp_bands.doneDisbursing();
        if (this.optDumpBands || this.verbose > 1) {
            byte b3 = 50;
            while (true) {
                byte b4 = b3;
                if (b4 < 54) {
                    ConstantPool.Index indexByTag = this.pkg.cp.getIndexByTag(b4);
                    if (indexByTag != null && !indexByTag.isEmpty()) {
                        ConstantPool.Entry[] entryArr3 = indexByTag.cpMap;
                        if (this.verbose > 1) {
                            Utils.log.info("Index group " + ConstantPool.tagName(b4) + " contains " + entryArr3.length + " entries.");
                        }
                        if (this.optDumpBands) {
                            printStream = new PrintStream(getDumpStream(indexByTag.debugName, b4, ".gidx", indexByTag));
                            Throwable th3 = null;
                            try {
                                try {
                                    printArrayTo(printStream, entryArr3, 0, entryArr3.length, true);
                                    if (printStream != null) {
                                        if (0 != 0) {
                                            try {
                                                printStream.close();
                                            } catch (Throwable th4) {
                                                th3.addSuppressed(th4);
                                            }
                                        } else {
                                            printStream.close();
                                        }
                                    }
                                } finally {
                                }
                            } finally {
                            }
                        } else {
                            continue;
                        }
                    }
                    b3 = (byte) (b4 + 1);
                }
            }
        }
        setBandIndexes();
    }

    /* JADX WARN: Multi-variable type inference failed */
    void readUtf8Bands(ConstantPool.Entry[] entryArr) throws IOException {
        int length = entryArr.length;
        if (length == 0) {
            return;
        }
        this.cp_Utf8_prefix.expectLength(Math.max(0, length - 2));
        this.cp_Utf8_prefix.readFrom(this.in);
        this.cp_Utf8_suffix.expectLength(Math.max(0, length - 1));
        this.cp_Utf8_suffix.readFrom(this.in);
        char[] cArr = new char[length];
        int i2 = 0;
        this.cp_Utf8_chars.expectLength(this.cp_Utf8_suffix.getIntTotal());
        this.cp_Utf8_chars.readFrom(this.in);
        int i3 = 0;
        while (i3 < length) {
            int i4 = i3 < 1 ? 0 : this.cp_Utf8_suffix.getInt();
            if (i4 == 0 && i3 >= 1) {
                i2++;
            } else {
                cArr[i3] = new char[i4];
                for (int i5 = 0; i5 < i4; i5++) {
                    int i6 = this.cp_Utf8_chars.getInt();
                    if (!$assertionsDisabled && i6 != ((char) i6)) {
                        throw new AssertionError();
                    }
                    cArr[i3][i5] = (char) i6;
                }
            }
            i3++;
        }
        this.cp_Utf8_chars.doneDisbursing();
        int i7 = 0;
        this.cp_Utf8_big_suffix.expectLength(i2);
        this.cp_Utf8_big_suffix.readFrom(this.in);
        this.cp_Utf8_suffix.resetForSecondPass();
        int i8 = 0;
        while (i8 < length) {
            int i9 = i8 < 1 ? 0 : this.cp_Utf8_suffix.getInt();
            int i10 = i8 < 2 ? 0 : this.cp_Utf8_prefix.getInt();
            if (i9 == 0 && i8 >= 1) {
                if (!$assertionsDisabled && cArr[i8] != 0) {
                    throw new AssertionError();
                }
                i9 = this.cp_Utf8_big_suffix.getInt();
            } else if (!$assertionsDisabled && cArr[i8] == 0) {
                throw new AssertionError();
            }
            if (i7 < i10 + i9) {
                i7 = i10 + i9;
            }
            i8++;
        }
        char[] cArr2 = new char[i7];
        this.cp_Utf8_suffix.resetForSecondPass();
        this.cp_Utf8_big_suffix.resetForSecondPass();
        for (int i11 = 0; i11 < length; i11++) {
            if (i11 >= 1 && this.cp_Utf8_suffix.getInt() == 0) {
                int i12 = this.cp_Utf8_big_suffix.getInt();
                cArr[i11] = new char[i12];
                if (i12 == 0) {
                    continue;
                } else {
                    BandStructure.IntBand intBandNewIntBand = this.cp_Utf8_big_chars.newIntBand("(Utf8_big_" + i11 + ")");
                    intBandNewIntBand.expectLength(i12);
                    intBandNewIntBand.readFrom(this.in);
                    for (int i13 = 0; i13 < i12; i13++) {
                        int i14 = intBandNewIntBand.getInt();
                        if (!$assertionsDisabled && i14 != ((char) i14)) {
                            throw new AssertionError();
                        }
                        cArr[i11][i13] = (char) i14;
                    }
                    intBandNewIntBand.doneDisbursing();
                }
            }
        }
        this.cp_Utf8_big_chars.doneDisbursing();
        this.cp_Utf8_prefix.resetForSecondPass();
        this.cp_Utf8_suffix.resetForSecondPass();
        this.cp_Utf8_big_suffix.resetForSecondPass();
        int i15 = 0;
        while (i15 < length) {
            int i16 = i15 < 2 ? 0 : this.cp_Utf8_prefix.getInt();
            int i17 = i15 < 1 ? 0 : this.cp_Utf8_suffix.getInt();
            if (i17 == 0 && i15 >= 1) {
                i17 = this.cp_Utf8_big_suffix.getInt();
            }
            System.arraycopy(cArr[i15], 0, cArr2, i16, i17);
            entryArr[i15] = ConstantPool.getUtf8Entry(new String(cArr2, 0, i16 + i17));
            i15++;
        }
        this.cp_Utf8_prefix.doneDisbursing();
        this.cp_Utf8_suffix.doneDisbursing();
        this.cp_Utf8_big_suffix.doneDisbursing();
    }

    void readSignatureBands(ConstantPool.Entry[] entryArr) throws IOException {
        this.cp_Signature_form.expectLength(entryArr.length);
        this.cp_Signature_form.readFrom(this.in);
        this.cp_Signature_form.setIndex(getCPIndex((byte) 1));
        int[] iArr = new int[entryArr.length];
        for (int i2 = 0; i2 < entryArr.length; i2++) {
            iArr[i2] = ConstantPool.countClassParts((ConstantPool.Utf8Entry) this.cp_Signature_form.getRef());
        }
        this.cp_Signature_form.resetForSecondPass();
        this.cp_Signature_classes.expectLength(getIntTotal(iArr));
        this.cp_Signature_classes.readFrom(this.in);
        this.cp_Signature_classes.setIndex(getCPIndex((byte) 7));
        this.utf8Signatures = new HashMap();
        for (int i3 = 0; i3 < entryArr.length; i3++) {
            ConstantPool.Utf8Entry utf8Entry = (ConstantPool.Utf8Entry) this.cp_Signature_form.getRef();
            ConstantPool.ClassEntry[] classEntryArr = new ConstantPool.ClassEntry[iArr[i3]];
            for (int i4 = 0; i4 < classEntryArr.length; i4++) {
                classEntryArr[i4] = (ConstantPool.ClassEntry) this.cp_Signature_classes.getRef();
            }
            ConstantPool.SignatureEntry signatureEntry = ConstantPool.getSignatureEntry(utf8Entry, classEntryArr);
            entryArr[i3] = signatureEntry;
            this.utf8Signatures.put(signatureEntry.asUtf8Entry(), signatureEntry);
        }
        this.cp_Signature_form.doneDisbursing();
        this.cp_Signature_classes.doneDisbursing();
    }

    void readMemberRefs(byte b2, ConstantPool.Entry[] entryArr, BandStructure.CPRefBand cPRefBand, BandStructure.CPRefBand cPRefBand2) throws IOException {
        cPRefBand.expectLength(entryArr.length);
        cPRefBand.readFrom(this.in);
        cPRefBand.setIndex(getCPIndex((byte) 7));
        cPRefBand2.expectLength(entryArr.length);
        cPRefBand2.readFrom(this.in);
        cPRefBand2.setIndex(getCPIndex((byte) 12));
        for (int i2 = 0; i2 < entryArr.length; i2++) {
            entryArr[i2] = ConstantPool.getMemberEntry(b2, (ConstantPool.ClassEntry) cPRefBand.getRef(), (ConstantPool.DescriptorEntry) cPRefBand2.getRef());
        }
        cPRefBand.doneDisbursing();
        cPRefBand2.doneDisbursing();
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x024d, code lost:
    
        r8.pkg.addFile(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x025b, code lost:
    
        if (r0.isClassStub() == false) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0261, code lost:
    
        if (com.sun.java.util.jar.pack.PackageReader.$assertionsDisabled != false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x026b, code lost:
    
        if (r0.getFileLength() == 0) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0275, code lost:
    
        throw new java.lang.AssertionError();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0276, code lost:
    
        r0.next().initFile(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0289, code lost:
    
        r18 = r18 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void readFiles() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 822
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.PackageReader.readFiles():void");
    }

    void readAttrDefs() throws IOException {
        this.attr_definition_headers.expectLength(this.numAttrDefs);
        this.attr_definition_name.expectLength(this.numAttrDefs);
        this.attr_definition_layout.expectLength(this.numAttrDefs);
        this.attr_definition_headers.readFrom(this.in);
        this.attr_definition_name.readFrom(this.in);
        this.attr_definition_layout.readFrom(this.in);
        PrintStream printStream = !this.optDumpBands ? null : new PrintStream(getDumpStream(this.attr_definition_headers, ".def"));
        Throwable th = null;
        try {
            for (int i2 = 0; i2 < this.numAttrDefs; i2++) {
                int i3 = this.attr_definition_headers.getByte();
                int i4 = i3 & 3;
                int i5 = (i3 >> 2) - 1;
                Attribute.Layout layout = new Attribute.Layout(i4, ((ConstantPool.Utf8Entry) this.attr_definition_name.getRef()).stringValue(), ((ConstantPool.Utf8Entry) this.attr_definition_layout.getRef()).stringValue());
                if (!layout.layoutForClassVersion(getHighestClassVersion()).equals(layout.layout())) {
                    throw new IOException("Bad attribute layout in archive: " + layout.layout());
                }
                setAttributeLayoutIndex(layout, i5);
                if (printStream != null) {
                    printStream.println(i5 + " " + ((Object) layout));
                }
            }
            this.attr_definition_headers.doneDisbursing();
            this.attr_definition_name.doneDisbursing();
            this.attr_definition_layout.doneDisbursing();
            makeNewAttributeBands();
            this.attr_definition_bands.doneDisbursing();
        } finally {
            if (printStream != null) {
                if (0 != 0) {
                    try {
                        printStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    printStream.close();
                }
            }
        }
    }

    void readInnerClasses() throws IOException {
        ConstantPool.ClassEntry classEntry;
        ConstantPool.Utf8Entry utf8Entry;
        this.ic_this_class.expectLength(this.numInnerClasses);
        this.ic_this_class.readFrom(this.in);
        this.ic_flags.expectLength(this.numInnerClasses);
        this.ic_flags.readFrom(this.in);
        int i2 = 0;
        for (int i3 = 0; i3 < this.numInnerClasses; i3++) {
            if ((this.ic_flags.getInt() & 65536) != 0) {
                i2++;
            }
        }
        this.ic_outer_class.expectLength(i2);
        this.ic_outer_class.readFrom(this.in);
        this.ic_name.expectLength(i2);
        this.ic_name.readFrom(this.in);
        this.ic_flags.resetForSecondPass();
        ArrayList arrayList = new ArrayList(this.numInnerClasses);
        for (int i4 = 0; i4 < this.numInnerClasses; i4++) {
            int i5 = this.ic_flags.getInt();
            boolean z2 = (i5 & 65536) != 0;
            int i6 = i5 & (-65537);
            ConstantPool.ClassEntry classEntry2 = (ConstantPool.ClassEntry) this.ic_this_class.getRef();
            if (z2) {
                classEntry = (ConstantPool.ClassEntry) this.ic_outer_class.getRef();
                utf8Entry = (ConstantPool.Utf8Entry) this.ic_name.getRef();
            } else {
                String[] innerClassName = Package.parseInnerClassName(classEntry2.stringValue());
                if (!$assertionsDisabled && innerClassName == null) {
                    throw new AssertionError();
                }
                String str = innerClassName[0];
                String str2 = innerClassName[2];
                if (str == null) {
                    classEntry = null;
                } else {
                    classEntry = ConstantPool.getClassEntry(str);
                }
                if (str2 == null) {
                    utf8Entry = null;
                } else {
                    utf8Entry = ConstantPool.getUtf8Entry(str2);
                }
            }
            Package.InnerClass innerClass = new Package.InnerClass(classEntry2, classEntry, utf8Entry, i6);
            if (!$assertionsDisabled && !z2 && !innerClass.predictable) {
                throw new AssertionError();
            }
            arrayList.add(innerClass);
        }
        this.ic_flags.doneDisbursing();
        this.ic_this_class.doneDisbursing();
        this.ic_outer_class.doneDisbursing();
        this.ic_name.doneDisbursing();
        this.pkg.setAllInnerClasses(arrayList);
        this.ic_bands.doneDisbursing();
    }

    void readLocalInnerClasses(Package.Class r9) throws IOException {
        int i2 = this.class_InnerClasses_N.getInt();
        ArrayList arrayList = new ArrayList(i2);
        for (int i3 = 0; i3 < i2; i3++) {
            ConstantPool.ClassEntry classEntry = (ConstantPool.ClassEntry) this.class_InnerClasses_RC.getRef();
            int i4 = this.class_InnerClasses_F.getInt();
            if (i4 == 0) {
                Package.InnerClass globalInnerClass = this.pkg.getGlobalInnerClass(classEntry);
                if (!$assertionsDisabled && globalInnerClass == null) {
                    throw new AssertionError();
                }
                arrayList.add(globalInnerClass);
            } else {
                if (i4 == 65536) {
                    i4 = 0;
                }
                arrayList.add(new Package.InnerClass(classEntry, (ConstantPool.ClassEntry) this.class_InnerClasses_outer_RCN.getRef(), (ConstantPool.Utf8Entry) this.class_InnerClasses_name_RUN.getRef(), i4));
            }
        }
        r9.setInnerClasses(arrayList);
    }

    Package.Class[] readClasses() throws IOException {
        Package.Class[] classArr = new Package.Class[this.numClasses];
        if (this.verbose > 0) {
            Utils.log.info("  ...building " + classArr.length + " classes...");
        }
        this.class_this.expectLength(this.numClasses);
        this.class_super.expectLength(this.numClasses);
        this.class_interface_count.expectLength(this.numClasses);
        this.class_this.readFrom(this.in);
        this.class_super.readFrom(this.in);
        this.class_interface_count.readFrom(this.in);
        this.class_interface.expectLength(this.class_interface_count.getIntTotal());
        this.class_interface.readFrom(this.in);
        for (int i2 = 0; i2 < classArr.length; i2++) {
            ConstantPool.ClassEntry classEntry = (ConstantPool.ClassEntry) this.class_this.getRef();
            ConstantPool.ClassEntry classEntry2 = (ConstantPool.ClassEntry) this.class_super.getRef();
            ConstantPool.ClassEntry[] classEntryArr = new ConstantPool.ClassEntry[this.class_interface_count.getInt()];
            for (int i3 = 0; i3 < classEntryArr.length; i3++) {
                classEntryArr[i3] = (ConstantPool.ClassEntry) this.class_interface.getRef();
            }
            if (classEntry2 == classEntry) {
                classEntry2 = null;
            }
            Package r2 = this.pkg;
            r2.getClass();
            classArr[i2] = r2.new Class(0, classEntry, classEntry2, classEntryArr);
        }
        this.class_this.doneDisbursing();
        this.class_super.doneDisbursing();
        this.class_interface_count.doneDisbursing();
        this.class_interface.doneDisbursing();
        readMembers(classArr);
        countAndReadAttrs(0, Arrays.asList(classArr));
        this.pkg.trimToSize();
        readCodeHeaders();
        return classArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getOutputIndex(ConstantPool.Entry entry) {
        if (!$assertionsDisabled && entry.tag == 13) {
            throw new AssertionError();
        }
        int iUntypedIndexOf = this.pkg.cp.untypedIndexOf(entry);
        if (iUntypedIndexOf >= 0) {
            return iUntypedIndexOf;
        }
        if (entry.tag == 1) {
            return this.pkg.cp.untypedIndexOf(this.utf8Signatures.get(entry));
        }
        return -1;
    }

    void reconstructClass(Package.Class r5) {
        if (this.verbose > 1) {
            Utils.log.fine("reconstruct " + ((Object) r5));
        }
        Attribute attribute = r5.getAttribute(this.attrClassFileVersion);
        if (attribute != null) {
            r5.removeAttribute(attribute);
            r5.version = parseClassFileVersionAttr(attribute);
        } else {
            r5.version = this.pkg.defaultClassVersion;
        }
        r5.expandSourceFile();
        r5.setCPMap(reconstructLocalCPMap(r5));
    }

    ConstantPool.Entry[] reconstructLocalCPMap(Package.Class r6) {
        Set<ConstantPool.Entry> set = this.ldcRefMap.get(r6);
        HashSet hashSet = new HashSet();
        r6.visitRefs(0, hashSet);
        ArrayList arrayList = new ArrayList();
        r6.addAttribute(Package.attrBootstrapMethodsEmpty.canonicalInstance());
        ConstantPool.completeReferencesIn(hashSet, true, arrayList);
        int iExpandLocalICs = r6.expandLocalICs();
        if (iExpandLocalICs != 0) {
            if (iExpandLocalICs > 0) {
                r6.visitInnerClassRefs(0, hashSet);
            } else {
                hashSet.clear();
                r6.visitRefs(0, hashSet);
            }
            ConstantPool.completeReferencesIn(hashSet, true, arrayList);
        }
        if (arrayList.isEmpty()) {
            r6.attributes.remove(Package.attrBootstrapMethodsEmpty.canonicalInstance());
        } else {
            hashSet.add(Package.getRefString("BootstrapMethods"));
            Collections.sort(arrayList);
            r6.setBootstrapMethods(arrayList);
        }
        int i2 = 0;
        Iterator<E> it = hashSet.iterator();
        while (it.hasNext()) {
            if (((ConstantPool.Entry) it.next()).isDoubleWord()) {
                i2++;
            }
        }
        ConstantPool.Entry[] entryArr = new ConstantPool.Entry[1 + i2 + hashSet.size()];
        int i3 = 1;
        if (set != null) {
            if (!$assertionsDisabled && !hashSet.containsAll(set)) {
                throw new AssertionError();
            }
            Iterator<ConstantPool.Entry> it2 = set.iterator();
            while (it2.hasNext()) {
                int i4 = i3;
                i3++;
                entryArr[i4] = it2.next();
            }
            if (!$assertionsDisabled && i3 != 1 + set.size()) {
                throw new AssertionError();
            }
            hashSet.removeAll(set);
        }
        int i5 = i3;
        Iterator<E> it3 = hashSet.iterator();
        while (it3.hasNext()) {
            int i6 = i3;
            i3++;
            entryArr[i6] = (ConstantPool.Entry) it3.next();
        }
        if (!$assertionsDisabled && i3 != i5 + hashSet.size()) {
            throw new AssertionError();
        }
        Arrays.sort(entryArr, 1, i5, this.entryOutputOrder);
        Arrays.sort(entryArr, i5, i3, this.entryOutputOrder);
        if (this.verbose > 3) {
            Utils.log.fine("CP of " + ((Object) this) + " {");
            for (int i7 = 0; i7 < i3; i7++) {
                ConstantPool.Entry entry = entryArr[i7];
                Utils.log.fine(sun.security.pkcs11.wrapper.Constants.INDENT + (entry == null ? -1 : getOutputIndex(entry)) + " : " + ((Object) entry));
            }
            Utils.log.fine("}");
        }
        int length = entryArr.length;
        int i8 = i3;
        while (true) {
            i8--;
            if (i8 < 1) {
                break;
            }
            ConstantPool.Entry entry2 = entryArr[i8];
            if (entry2.isDoubleWord()) {
                length--;
                entryArr[length] = null;
            }
            length--;
            entryArr[length] = entry2;
        }
        if ($assertionsDisabled || length == 1) {
            return entryArr;
        }
        throw new AssertionError();
    }

    void readMembers(Package.Class[] classArr) throws IOException {
        if (!$assertionsDisabled && classArr.length != this.numClasses) {
            throw new AssertionError();
        }
        this.class_field_count.expectLength(this.numClasses);
        this.class_method_count.expectLength(this.numClasses);
        this.class_field_count.readFrom(this.in);
        this.class_method_count.readFrom(this.in);
        int intTotal = this.class_field_count.getIntTotal();
        int intTotal2 = this.class_method_count.getIntTotal();
        this.field_descr.expectLength(intTotal);
        this.method_descr.expectLength(intTotal2);
        if (this.verbose > 1) {
            Utils.log.fine("expecting #fields=" + intTotal + " and #methods=" + intTotal2 + " in #classes=" + this.numClasses);
        }
        ArrayList arrayList = new ArrayList(intTotal);
        this.field_descr.readFrom(this.in);
        for (Package.Class r0 : classArr) {
            int i2 = this.class_field_count.getInt();
            for (int i3 = 0; i3 < i2; i3++) {
                r0.getClass();
                arrayList.add(r0.new Field(0, (ConstantPool.DescriptorEntry) this.field_descr.getRef()));
            }
        }
        this.class_field_count.doneDisbursing();
        this.field_descr.doneDisbursing();
        countAndReadAttrs(1, arrayList);
        List<Package.Class.Method> arrayList2 = new ArrayList<>(intTotal2);
        this.method_descr.readFrom(this.in);
        for (Package.Class r02 : classArr) {
            int i4 = this.class_method_count.getInt();
            for (int i5 = 0; i5 < i4; i5++) {
                r02.getClass();
                arrayList2.add(r02.new Method(0, (ConstantPool.DescriptorEntry) this.method_descr.getRef()));
            }
        }
        this.class_method_count.doneDisbursing();
        this.method_descr.doneDisbursing();
        countAndReadAttrs(2, arrayList2);
        this.allCodes = buildCodeAttrs(arrayList2);
    }

    Code[] buildCodeAttrs(List<Package.Class.Method> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (Package.Class.Method method : list) {
            if (method.getAttribute(this.attrCodeEmpty) != null) {
                method.code = new Code(method);
                arrayList.add(method.code);
            }
        }
        Code[] codeArr = new Code[arrayList.size()];
        arrayList.toArray(codeArr);
        return codeArr;
    }

    void readCodeHeaders() throws IOException {
        boolean zTestBit = testBit(this.archiveOptions, 4);
        this.code_headers.expectLength(this.allCodes.length);
        this.code_headers.readFrom(this.in);
        ArrayList<Code> arrayList = new ArrayList(this.allCodes.length / 10);
        for (int i2 = 0; i2 < this.allCodes.length; i2++) {
            Code code = this.allCodes[i2];
            int i3 = this.code_headers.getByte();
            if (!$assertionsDisabled && i3 != (i3 & 255)) {
                throw new AssertionError();
            }
            if (this.verbose > 2) {
                Utils.log.fine("codeHeader " + ((Object) code) + " = " + i3);
            }
            if (i3 == 0) {
                arrayList.add(code);
            } else {
                code.setMaxStack(shortCodeHeader_max_stack(i3));
                code.setMaxNALocals(shortCodeHeader_max_na_locals(i3));
                code.setHandlerCount(shortCodeHeader_handler_count(i3));
                if (!$assertionsDisabled && shortCodeHeader(code) != i3) {
                    throw new AssertionError();
                }
            }
        }
        this.code_headers.doneDisbursing();
        this.code_max_stack.expectLength(arrayList.size());
        this.code_max_na_locals.expectLength(arrayList.size());
        this.code_handler_count.expectLength(arrayList.size());
        this.code_max_stack.readFrom(this.in);
        this.code_max_na_locals.readFrom(this.in);
        this.code_handler_count.readFrom(this.in);
        for (Code code2 : arrayList) {
            code2.setMaxStack(this.code_max_stack.getInt());
            code2.setMaxNALocals(this.code_max_na_locals.getInt());
            code2.setHandlerCount(this.code_handler_count.getInt());
        }
        this.code_max_stack.doneDisbursing();
        this.code_max_na_locals.doneDisbursing();
        this.code_handler_count.doneDisbursing();
        readCodeHandlers();
        if (zTestBit) {
            this.codesWithFlags = Arrays.asList(this.allCodes);
        } else {
            this.codesWithFlags = arrayList;
        }
        countAttrs(3, this.codesWithFlags);
    }

    void readCodeHandlers() throws IOException {
        int handlerCount = 0;
        for (int i2 = 0; i2 < this.allCodes.length; i2++) {
            handlerCount += this.allCodes[i2].getHandlerCount();
        }
        BandStructure.ValueBand[] valueBandArr = {this.code_handler_start_P, this.code_handler_end_PO, this.code_handler_catch_PO, this.code_handler_class_RCN};
        for (int i3 = 0; i3 < valueBandArr.length; i3++) {
            valueBandArr[i3].expectLength(handlerCount);
            valueBandArr[i3].readFrom(this.in);
        }
        for (int i4 = 0; i4 < this.allCodes.length; i4++) {
            Code code = this.allCodes[i4];
            int handlerCount2 = code.getHandlerCount();
            for (int i5 = 0; i5 < handlerCount2; i5++) {
                code.handler_class[i5] = this.code_handler_class_RCN.getRef();
                code.handler_start[i5] = this.code_handler_start_P.getInt();
                code.handler_end[i5] = this.code_handler_end_PO.getInt();
                code.handler_catch[i5] = this.code_handler_catch_PO.getInt();
            }
        }
        for (BandStructure.ValueBand valueBand : valueBandArr) {
            valueBand.doneDisbursing();
        }
    }

    void fixupCodeHandlers() {
        for (int i2 = 0; i2 < this.allCodes.length; i2++) {
            Code code = this.allCodes[i2];
            int handlerCount = code.getHandlerCount();
            for (int i3 = 0; i3 < handlerCount; i3++) {
                int i4 = code.handler_start[i3];
                code.handler_start[i3] = code.decodeBCI(i4);
                int i5 = i4 + code.handler_end[i3];
                code.handler_end[i3] = code.decodeBCI(i5);
                code.handler_catch[i3] = code.decodeBCI(i5 + code.handler_catch[i3]);
            }
        }
    }

    void countAndReadAttrs(int i2, Collection<? extends Attribute.Holder> collection) throws IOException {
        countAttrs(i2, collection);
        readAttrs(i2, collection);
    }

    void countAttrs(int i2, Collection<? extends Attribute.Holder> collection) throws IOException {
        BandStructure.MultiBand multiBand = this.attrBands[i2];
        long j2 = this.attrFlagMask[i2];
        if (this.verbose > 1) {
            Utils.log.fine("scanning flags and attrs for " + Attribute.contextName(i2) + "[" + collection.size() + "]");
        }
        List<Attribute.Layout> list = this.attrDefs.get(i2);
        Attribute.Layout[] layoutArr = new Attribute.Layout[list.size()];
        list.toArray(layoutArr);
        BandStructure.IntBand attrBand = getAttrBand(multiBand, 0);
        BandStructure.IntBand attrBand2 = getAttrBand(multiBand, 1);
        BandStructure.IntBand attrBand3 = getAttrBand(multiBand, 2);
        BandStructure.IntBand attrBand4 = getAttrBand(multiBand, 3);
        BandStructure.IntBand attrBand5 = getAttrBand(multiBand, 4);
        int i3 = this.attrOverflowMask[i2];
        int i4 = 0;
        boolean zHaveFlagsHi = haveFlagsHi(i2);
        attrBand.expectLength(zHaveFlagsHi ? collection.size() : 0);
        attrBand.readFrom(this.in);
        attrBand2.expectLength(collection.size());
        attrBand2.readFrom(this.in);
        if (!$assertionsDisabled && (j2 & i3) != i3) {
            throw new AssertionError();
        }
        for (Attribute.Holder holder : collection) {
            int i5 = attrBand2.getInt();
            holder.flags = i5;
            if ((i5 & i3) != 0) {
                i4++;
            }
        }
        attrBand3.expectLength(i4);
        attrBand3.readFrom(this.in);
        attrBand4.expectLength(attrBand3.getIntTotal());
        attrBand4.readFrom(this.in);
        int[] iArr = new int[layoutArr.length];
        for (Attribute.Holder holder2 : collection) {
            if (!$assertionsDisabled && holder2.attributes != null) {
                throw new AssertionError();
            }
            long j3 = ((holder2.flags & j2) << 32) >>> 32;
            holder2.flags -= (int) j3;
            if (!$assertionsDisabled && holder2.flags != ((char) holder2.flags)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && i2 == 3 && holder2.flags != 0) {
                throw new AssertionError();
            }
            if (zHaveFlagsHi) {
                j3 += attrBand.getInt() << 32;
            }
            if (j3 != 0) {
                int i6 = 0;
                long j4 = j3 & i3;
                if (!$assertionsDisabled && j4 < 0) {
                    throw new AssertionError();
                }
                long j5 = j3 - j4;
                if (j4 != 0) {
                    i6 = attrBand3.getInt();
                }
                int i7 = 0;
                long j6 = j5;
                int i8 = 0;
                while (j6 != 0) {
                    if ((j6 & (1 << i8)) != 0) {
                        j6 -= 1 << i8;
                        i7++;
                    }
                    i8++;
                }
                ArrayList arrayList = new ArrayList(i7 + i6);
                holder2.attributes = arrayList;
                long j7 = j5;
                int i9 = 0;
                while (j7 != 0) {
                    if ((j7 & (1 << i9)) != 0) {
                        j7 -= 1 << i9;
                        int i10 = i9;
                        iArr[i10] = iArr[i10] + 1;
                        if (layoutArr[i9] == null) {
                            badAttrIndex(i9, i2);
                        }
                        arrayList.add(layoutArr[i9].canonicalInstance());
                        i7--;
                    }
                    i9++;
                }
                if (!$assertionsDisabled && i7 != 0) {
                    throw new AssertionError();
                }
                while (i6 > 0) {
                    int i11 = attrBand4.getInt();
                    iArr[i11] = iArr[i11] + 1;
                    if (layoutArr[i11] == null) {
                        badAttrIndex(i11, i2);
                    }
                    arrayList.add(layoutArr[i11].canonicalInstance());
                    i6--;
                }
            }
        }
        attrBand.doneDisbursing();
        attrBand2.doneDisbursing();
        attrBand3.doneDisbursing();
        attrBand4.doneDisbursing();
        int i12 = 0;
        boolean z2 = true;
        while (true) {
            boolean z3 = z2;
            for (int i13 = 0; i13 < layoutArr.length; i13++) {
                Attribute.Layout layout = layoutArr[i13];
                if (layout != null && z3 == isPredefinedAttr(i2, i13) && iArr[i13] != 0) {
                    Attribute.Layout.Element[] callables = layout.getCallables();
                    for (int i14 = 0; i14 < callables.length; i14++) {
                        if (!$assertionsDisabled && callables[i14].kind != 10) {
                            throw new AssertionError();
                        }
                        if (callables[i14].flagTest((byte) 8)) {
                            i12++;
                        }
                    }
                }
            }
            if (z3) {
                z2 = false;
            } else {
                attrBand5.expectLength(i12);
                attrBand5.readFrom(this.in);
                boolean z4 = true;
                while (true) {
                    boolean z5 = z4;
                    for (int i15 = 0; i15 < layoutArr.length; i15++) {
                        Attribute.Layout layout2 = layoutArr[i15];
                        if (layout2 != null && z5 == isPredefinedAttr(i2, i15)) {
                            int i16 = iArr[i15];
                            BandStructure.Band[] bandArr = this.attrBandTable.get(layout2);
                            if (layout2 == this.attrInnerClassesEmpty) {
                                this.class_InnerClasses_N.expectLength(i16);
                                this.class_InnerClasses_N.readFrom(this.in);
                                int intTotal = this.class_InnerClasses_N.getIntTotal();
                                this.class_InnerClasses_RC.expectLength(intTotal);
                                this.class_InnerClasses_RC.readFrom(this.in);
                                this.class_InnerClasses_F.expectLength(intTotal);
                                this.class_InnerClasses_F.readFrom(this.in);
                                int intCount = intTotal - this.class_InnerClasses_F.getIntCount(0);
                                this.class_InnerClasses_outer_RCN.expectLength(intCount);
                                this.class_InnerClasses_outer_RCN.readFrom(this.in);
                                this.class_InnerClasses_name_RUN.expectLength(intCount);
                                this.class_InnerClasses_name_RUN.readFrom(this.in);
                            } else if (!this.optDebugBands && i16 == 0) {
                                for (BandStructure.Band band : bandArr) {
                                    band.doneWithUnusedBand();
                                }
                            } else {
                                if (!layout2.hasCallables()) {
                                    readAttrBands(layout2.elems, i16, new int[0], bandArr);
                                } else {
                                    Attribute.Layout.Element[] callables2 = layout2.getCallables();
                                    int[] iArr2 = new int[callables2.length];
                                    iArr2[0] = i16;
                                    for (int i17 = 0; i17 < callables2.length; i17++) {
                                        if (!$assertionsDisabled && callables2[i17].kind != 10) {
                                            throw new AssertionError();
                                        }
                                        int i18 = iArr2[i17];
                                        iArr2[i17] = -1;
                                        if (i16 > 0 && callables2[i17].flagTest((byte) 8)) {
                                            i18 += attrBand5.getInt();
                                        }
                                        readAttrBands(callables2[i17].body, i18, iArr2, bandArr);
                                    }
                                }
                                if (this.optDebugBands && i16 == 0) {
                                    for (BandStructure.Band band2 : bandArr) {
                                        band2.doneDisbursing();
                                    }
                                }
                            }
                        }
                    }
                    if (z5) {
                        z4 = false;
                    } else {
                        attrBand5.doneDisbursing();
                        return;
                    }
                }
            }
        }
    }

    void badAttrIndex(int i2, int i3) throws IOException {
        throw new IOException("Unknown attribute index " + i2 + " for " + Constants.ATTR_CONTEXT_NAME[i3] + " attribute");
    }

    void readAttrs(int i2, Collection<? extends Attribute.Holder> collection) throws IOException {
        HashSet<Attribute.Layout> hashSet = new HashSet();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (final Attribute.Holder holder : collection) {
            if (holder.attributes != null) {
                ListIterator<Attribute> listIterator = holder.attributes.listIterator();
                while (listIterator.hasNext()) {
                    Attribute next = listIterator.next();
                    Attribute.Layout layout = next.layout();
                    if (layout.bandCount == 0) {
                        if (layout == this.attrInnerClassesEmpty) {
                            readLocalInnerClasses((Package.Class) holder);
                        }
                    } else {
                        hashSet.add(layout);
                        boolean z2 = i2 == 1 && layout == this.attrConstantValue;
                        if (z2) {
                            setConstantValueIndex((Package.Class.Field) holder);
                        }
                        if (this.verbose > 2) {
                            Utils.log.fine("read " + ((Object) next) + " in " + ((Object) holder));
                        }
                        final BandStructure.Band[] bandArr = this.attrBandTable.get(layout);
                        byteArrayOutputStream.reset();
                        listIterator.set(next.addContent(byteArrayOutputStream.toByteArray(), next.unparse(new Attribute.ValueStream() { // from class: com.sun.java.util.jar.pack.PackageReader.2
                            @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                            public int getInt(int i3) {
                                return ((BandStructure.IntBand) bandArr[i3]).getInt();
                            }

                            @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                            public ConstantPool.Entry getRef(int i3) {
                                return ((BandStructure.CPRefBand) bandArr[i3]).getRef();
                            }

                            @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                            public int decodeBCI(int i3) {
                                return ((Code) holder).decodeBCI(i3);
                            }
                        }, byteArrayOutputStream)));
                        if (z2) {
                            setConstantValueIndex(null);
                        }
                    }
                }
            }
        }
        for (Attribute.Layout layout2 : hashSet) {
            if (layout2 != null) {
                for (BandStructure.Band band : this.attrBandTable.get(layout2)) {
                    band.doneDisbursing();
                }
            }
        }
        if (i2 == 0) {
            this.class_InnerClasses_N.doneDisbursing();
            this.class_InnerClasses_RC.doneDisbursing();
            this.class_InnerClasses_F.doneDisbursing();
            this.class_InnerClasses_outer_RCN.doneDisbursing();
            this.class_InnerClasses_name_RUN.doneDisbursing();
        }
        BandStructure.MultiBand multiBand = this.attrBands[i2];
        for (int i3 = 0; i3 < multiBand.size(); i3++) {
            BandStructure.Band band2 = multiBand.get(i3);
            if (band2 instanceof BandStructure.MultiBand) {
                band2.doneDisbursing();
            }
        }
        multiBand.doneDisbursing();
    }

    private void readAttrBands(Attribute.Layout.Element[] elementArr, int i2, int[] iArr, BandStructure.Band[] bandArr) throws IOException {
        int intCount;
        for (Attribute.Layout.Element element : elementArr) {
            BandStructure.Band band = null;
            if (element.hasBand()) {
                band = bandArr[element.bandIndex];
                band.expectLength(i2);
                band.readFrom(this.in);
            }
            switch (element.kind) {
                case 5:
                    readAttrBands(element.body, ((BandStructure.IntBand) band).getIntTotal(), iArr, bandArr);
                    break;
                case 7:
                    int i3 = i2;
                    int i4 = 0;
                    while (i4 < element.body.length) {
                        if (i4 == element.body.length - 1) {
                            intCount = i3;
                        } else {
                            intCount = 0;
                            int i5 = i4;
                            while (true) {
                                if (i4 == i5 || (i4 < element.body.length && element.body[i4].flagTest((byte) 8))) {
                                    intCount += ((BandStructure.IntBand) band).getIntCount(element.body[i4].value);
                                    i4++;
                                }
                            }
                            i4--;
                        }
                        i3 -= intCount;
                        readAttrBands(element.body[i4].body, intCount, iArr, bandArr);
                        i4++;
                    }
                    if (!$assertionsDisabled && i3 != 0) {
                        throw new AssertionError();
                    }
                    break;
                case 9:
                    if (!$assertionsDisabled && element.body.length != 1) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && element.body[0].kind != 10) {
                        throw new AssertionError();
                    }
                    if (!element.flagTest((byte) 8)) {
                        if (!$assertionsDisabled && iArr[element.value] < 0) {
                            throw new AssertionError();
                        }
                        int i6 = element.value;
                        iArr[i6] = iArr[i6] + i2;
                        break;
                    } else {
                        continue;
                    }
                    break;
                case 10:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
        }
    }

    void readByteCodes() throws IOException {
        this.bc_codes.elementCountForDebug = this.allCodes.length;
        this.bc_codes.setInputStreamFrom(this.in);
        readByteCodeOps();
        this.bc_codes.doneDisbursing();
        BandStructure.Band[] bandArr = {this.bc_case_value, this.bc_byte, this.bc_short, this.bc_local, this.bc_label, this.bc_intref, this.bc_floatref, this.bc_longref, this.bc_doubleref, this.bc_stringref, this.bc_loadablevalueref, this.bc_classref, this.bc_fieldref, this.bc_methodref, this.bc_imethodref, this.bc_indyref, this.bc_thisfield, this.bc_superfield, this.bc_thismethod, this.bc_supermethod, this.bc_initref, this.bc_escref, this.bc_escrefsize, this.bc_escsize};
        for (BandStructure.Band band : bandArr) {
            band.readFrom(this.in);
        }
        this.bc_escbyte.expectLength(this.bc_escsize.getIntTotal());
        this.bc_escbyte.readFrom(this.in);
        expandByteCodeOps();
        this.bc_case_count.doneDisbursing();
        for (BandStructure.Band band2 : bandArr) {
            band2.doneDisbursing();
        }
        this.bc_escbyte.doneDisbursing();
        this.bc_bands.doneDisbursing();
        readAttrs(3, this.codesWithFlags);
        fixupCodeHandlers();
        this.code_bands.doneDisbursing();
        this.class_bands.doneDisbursing();
    }

    private void readByteCodeOps() throws IOException {
        byte[] bArrRealloc = new byte[4096];
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.allCodes.length; i2++) {
            Code code = this.allCodes[i2];
            int i3 = 0;
            while (true) {
                int i4 = this.bc_codes.getByte();
                if (i3 + 10 > bArrRealloc.length) {
                    bArrRealloc = realloc(bArrRealloc);
                }
                bArrRealloc[i3] = (byte) i4;
                boolean z2 = false;
                if (i4 == 196) {
                    i4 = this.bc_codes.getByte();
                    i3++;
                    bArrRealloc[i3] = (byte) i4;
                    z2 = true;
                }
                if (!$assertionsDisabled && i4 != (255 & i4)) {
                    throw new AssertionError();
                }
                switch (i4) {
                    case 16:
                        this.bc_byte.expectMoreLength(1);
                        break;
                    case 17:
                        this.bc_short.expectMoreLength(1);
                        break;
                    case 132:
                        this.bc_local.expectMoreLength(1);
                        if (z2) {
                            this.bc_short.expectMoreLength(1);
                            break;
                        } else {
                            this.bc_byte.expectMoreLength(1);
                            break;
                        }
                    case 170:
                    case 171:
                        this.bc_case_count.expectMoreLength(1);
                        arrayList.add(Integer.valueOf(i4));
                        break;
                    case 188:
                        this.bc_byte.expectMoreLength(1);
                        break;
                    case 197:
                        if (!$assertionsDisabled && getCPRefOpBand(i4) != this.bc_classref) {
                            throw new AssertionError();
                        }
                        this.bc_classref.expectMoreLength(1);
                        this.bc_byte.expectMoreLength(1);
                        break;
                    case 253:
                        this.bc_escrefsize.expectMoreLength(1);
                        this.bc_escref.expectMoreLength(1);
                        break;
                    case 254:
                        this.bc_escsize.expectMoreLength(1);
                        break;
                    case 255:
                        code.bytes = realloc(bArrRealloc, i3);
                    default:
                        if (Instruction.isInvokeInitOp(i4)) {
                            this.bc_initref.expectMoreLength(1);
                            break;
                        } else if (Instruction.isSelfLinkerOp(i4)) {
                            selfOpRefBand(i4).expectMoreLength(1);
                            break;
                        } else if (Instruction.isBranchOp(i4)) {
                            this.bc_label.expectMoreLength(1);
                            break;
                        } else if (Instruction.isCPRefOp(i4)) {
                            getCPRefOpBand(i4).expectMoreLength(1);
                            if (!$assertionsDisabled && i4 == 197) {
                                throw new AssertionError();
                            }
                            break;
                        } else if (Instruction.isLocalSlotOp(i4)) {
                            this.bc_local.expectMoreLength(1);
                            break;
                        } else {
                            break;
                        }
                }
                i3++;
            }
        }
        this.bc_case_count.readFrom(this.in);
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            int iIntValue = ((Integer) it.next()).intValue();
            int i5 = this.bc_case_count.getInt();
            this.bc_label.expectMoreLength(1 + i5);
            this.bc_case_value.expectMoreLength(iIntValue == 170 ? 1 : i5);
        }
        this.bc_case_count.resetForSecondPass();
    }

    private void expandByteCodeOps() throws IOException {
        BandStructure.CPRefBand cPRefBand;
        ConstantPool.Index memberIndex;
        ConstantPool.ClassEntry classEntry;
        byte[] bArrRealloc = new byte[4096];
        int[] iArrRealloc = new int[4096];
        int[] iArrRealloc2 = new int[1024];
        Fixups fixups = new Fixups();
        for (int i2 = 0; i2 < this.allCodes.length; i2++) {
            Code code = this.allCodes[i2];
            byte[] bArr = code.bytes;
            code.bytes = null;
            Package.Class classThisClass = code.thisClass();
            Set<ConstantPool.Entry> set = this.ldcRefMap.get(classThisClass);
            if (set == null) {
                Map<Package.Class, Set<ConstantPool.Entry>> map = this.ldcRefMap;
                HashSet hashSet = new HashSet();
                set = hashSet;
                map.put(classThisClass, hashSet);
            }
            ConstantPool.ClassEntry classEntry2 = classThisClass.thisClass;
            ConstantPool.ClassEntry classEntry3 = classThisClass.superClass;
            ConstantPool.ClassEntry classEntry4 = null;
            int nextPC = 0;
            int i3 = 0;
            int i4 = 0;
            fixups.clear();
            int i5 = 0;
            while (i5 < bArr.length) {
                int i6 = Instruction.getByte(bArr, i5);
                int i7 = nextPC;
                int i8 = i3;
                i3++;
                iArrRealloc[i8] = i7;
                if (nextPC + 10 > bArrRealloc.length) {
                    bArrRealloc = realloc(bArrRealloc);
                }
                if (i3 + 10 > iArrRealloc.length) {
                    iArrRealloc = realloc(iArrRealloc);
                }
                if (i4 + 10 > iArrRealloc2.length) {
                    iArrRealloc2 = realloc(iArrRealloc2);
                }
                boolean z2 = false;
                if (i6 == 196) {
                    int i9 = nextPC;
                    nextPC++;
                    bArrRealloc[i9] = (byte) i6;
                    i5++;
                    i6 = Instruction.getByte(bArr, i5);
                    z2 = true;
                }
                switch (i6) {
                    case 16:
                    case 188:
                        int i10 = this.bc_byte.getByte();
                        int i11 = nextPC;
                        int i12 = nextPC + 1;
                        bArrRealloc[i11] = (byte) i6;
                        nextPC = i12 + 1;
                        bArrRealloc[i12] = (byte) i10;
                        break;
                    case 17:
                        int i13 = this.bc_short.getInt();
                        int i14 = nextPC;
                        int i15 = nextPC + 1;
                        bArrRealloc[i14] = (byte) i6;
                        Instruction.setShort(bArrRealloc, i15, i13);
                        nextPC = i15 + 2;
                        break;
                    case 132:
                        int i16 = nextPC;
                        int i17 = nextPC + 1;
                        bArrRealloc[i16] = (byte) i6;
                        int i18 = this.bc_local.getInt();
                        if (z2) {
                            int i19 = this.bc_short.getInt();
                            Instruction.setShort(bArrRealloc, i17, i18);
                            int i20 = i17 + 2;
                            Instruction.setShort(bArrRealloc, i20, i19);
                            nextPC = i20 + 2;
                            break;
                        } else {
                            byte b2 = (byte) this.bc_byte.getByte();
                            int i21 = i17 + 1;
                            bArrRealloc[i17] = (byte) i18;
                            nextPC = i21 + 1;
                            bArrRealloc[i21] = b2;
                            break;
                        }
                    case 170:
                    case 171:
                        int i22 = this.bc_case_count.getInt();
                        while (nextPC + 30 + (i22 * 8) > bArrRealloc.length) {
                            bArrRealloc = realloc(bArrRealloc);
                        }
                        int i23 = nextPC;
                        int i24 = nextPC + 1;
                        bArrRealloc[i23] = (byte) i6;
                        Arrays.fill(bArrRealloc, i24, i24 + 30, (byte) 0);
                        Instruction.Switch r0 = (Instruction.Switch) Instruction.at(bArrRealloc, i7);
                        r0.setCaseCount(i22);
                        if (i6 == 170) {
                            r0.setCaseValue(0, this.bc_case_value.getInt());
                        } else {
                            for (int i25 = 0; i25 < i22; i25++) {
                                r0.setCaseValue(i25, this.bc_case_value.getInt());
                            }
                        }
                        int i26 = i4;
                        i4++;
                        iArrRealloc2[i26] = i7;
                        nextPC = r0.getNextPC();
                        break;
                    case 253:
                        int i27 = this.bc_escrefsize.getInt();
                        ConstantPool.Entry ref = this.bc_escref.getRef();
                        if (i27 == 1) {
                            set.add(ref);
                        }
                        switch (i27) {
                            case 1:
                                fixups.addU1(nextPC, ref);
                                break;
                            case 2:
                                fixups.addU2(nextPC, ref);
                                break;
                            default:
                                if (!$assertionsDisabled) {
                                    throw new AssertionError();
                                }
                                break;
                        }
                        bArrRealloc[nextPC + 1] = 0;
                        bArrRealloc[nextPC + 0] = 0;
                        nextPC += i27;
                        break;
                    case 254:
                        int i28 = this.bc_escsize.getInt();
                        while (nextPC + i28 > bArrRealloc.length) {
                            bArrRealloc = realloc(bArrRealloc);
                        }
                        while (true) {
                            int i29 = i28;
                            i28--;
                            if (i29 > 0) {
                                int i30 = nextPC;
                                nextPC++;
                                bArrRealloc[i30] = (byte) this.bc_escbyte.getByte();
                            }
                        }
                        break;
                    default:
                        if (Instruction.isInvokeInitOp(i6)) {
                            int i31 = i6 - 230;
                            switch (i31) {
                                case 0:
                                    classEntry = classEntry2;
                                    break;
                                case 1:
                                    classEntry = classEntry3;
                                    break;
                                default:
                                    if (!$assertionsDisabled && i31 != 2) {
                                        throw new AssertionError();
                                    }
                                    classEntry = classEntry4;
                                    break;
                                    break;
                            }
                            int i32 = nextPC;
                            int i33 = nextPC + 1;
                            bArrRealloc[i32] = (byte) 183;
                            fixups.addU2(i33, this.pkg.cp.getOverloadingForIndex((byte) 10, classEntry, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, this.bc_initref.getInt()));
                            bArrRealloc[i33 + 1] = 0;
                            bArrRealloc[i33 + 0] = 0;
                            nextPC = i33 + 2;
                            if (!$assertionsDisabled && Instruction.opLength(183) != nextPC - i7) {
                                throw new AssertionError();
                            }
                            break;
                        } else if (Instruction.isSelfLinkerOp(i6)) {
                            int i34 = i6 - 202;
                            boolean z3 = i34 >= 14;
                            if (z3) {
                                i34 -= 14;
                            }
                            boolean z4 = i34 >= 7;
                            if (z4) {
                                i34 -= 7;
                            }
                            int i35 = 178 + i34;
                            boolean zIsFieldOp = Instruction.isFieldOp(i35);
                            ConstantPool.ClassEntry classEntry5 = z3 ? classEntry3 : classEntry2;
                            if (zIsFieldOp) {
                                cPRefBand = z3 ? this.bc_superfield : this.bc_thisfield;
                                memberIndex = this.pkg.cp.getMemberIndex((byte) 9, classEntry5);
                            } else {
                                cPRefBand = z3 ? this.bc_supermethod : this.bc_thismethod;
                                memberIndex = this.pkg.cp.getMemberIndex((byte) 10, classEntry5);
                            }
                            if (!$assertionsDisabled && cPRefBand != selfOpRefBand(i6)) {
                                throw new AssertionError();
                            }
                            ConstantPool.MemberEntry memberEntry = (ConstantPool.MemberEntry) cPRefBand.getRef(memberIndex);
                            if (z4) {
                                int i36 = nextPC;
                                nextPC++;
                                bArrRealloc[i36] = 42;
                                i7 = nextPC;
                                i3++;
                                iArrRealloc[i3] = i7;
                            }
                            int i37 = nextPC;
                            int i38 = nextPC + 1;
                            bArrRealloc[i37] = (byte) i35;
                            fixups.addU2(i38, memberEntry);
                            bArrRealloc[i38 + 1] = 0;
                            bArrRealloc[i38 + 0] = 0;
                            nextPC = i38 + 2;
                            if (!$assertionsDisabled && Instruction.opLength(i35) != nextPC - i7) {
                                throw new AssertionError();
                            }
                            break;
                        } else if (Instruction.isBranchOp(i6)) {
                            int i39 = nextPC;
                            nextPC++;
                            bArrRealloc[i39] = (byte) i6;
                            if (!$assertionsDisabled && z2) {
                                throw new AssertionError();
                            }
                            int iOpLength = i7 + Instruction.opLength(i6);
                            int i40 = i4;
                            i4++;
                            iArrRealloc2[i40] = i7;
                            while (nextPC < iOpLength) {
                                int i41 = nextPC;
                                nextPC++;
                                bArrRealloc[i41] = 0;
                            }
                            break;
                        } else if (Instruction.isCPRefOp(i6)) {
                            BandStructure.CPRefBand cPRefOpBand = getCPRefOpBand(i6);
                            ConstantPool.Entry ref2 = cPRefOpBand.getRef();
                            if (ref2 == null) {
                                if (cPRefOpBand == this.bc_classref) {
                                    ref2 = classEntry2;
                                } else if (!$assertionsDisabled) {
                                    throw new AssertionError();
                                }
                            }
                            int i42 = i6;
                            int i43 = 2;
                            switch (i6) {
                                case 18:
                                case 233:
                                case 234:
                                case 235:
                                case 240:
                                    i42 = 18;
                                    i43 = 1;
                                    set.add(ref2);
                                    break;
                                case 19:
                                case 236:
                                case 237:
                                case 238:
                                case 241:
                                    i42 = 19;
                                    break;
                                case 20:
                                case 239:
                                    i42 = 20;
                                    break;
                                case 187:
                                    classEntry4 = (ConstantPool.ClassEntry) ref2;
                                    break;
                                case 242:
                                    i42 = 183;
                                    break;
                                case 243:
                                    i42 = 184;
                                    break;
                            }
                            int i44 = nextPC;
                            int i45 = nextPC + 1;
                            bArrRealloc[i44] = (byte) i42;
                            switch (i43) {
                                case 1:
                                    fixups.addU1(i45, ref2);
                                    break;
                                case 2:
                                    fixups.addU2(i45, ref2);
                                    break;
                                default:
                                    if (!$assertionsDisabled) {
                                        throw new AssertionError();
                                    }
                                    break;
                            }
                            bArrRealloc[i45 + 1] = 0;
                            bArrRealloc[i45 + 0] = 0;
                            nextPC = i45 + i43;
                            if (i42 == 197) {
                                nextPC++;
                                bArrRealloc[nextPC] = (byte) this.bc_byte.getByte();
                            } else if (i42 == 185) {
                                int i46 = nextPC + 1;
                                bArrRealloc[nextPC] = (byte) (1 + ((ConstantPool.MemberEntry) ref2).descRef.typeRef.computeSize(true));
                                nextPC = i46 + 1;
                                bArrRealloc[i46] = 0;
                            } else if (i42 == 186) {
                                int i47 = nextPC + 1;
                                bArrRealloc[nextPC] = 0;
                                nextPC = i47 + 1;
                                bArrRealloc[i47] = 0;
                            }
                            if (!$assertionsDisabled && Instruction.opLength(i42) != nextPC - i7) {
                                throw new AssertionError();
                            }
                            break;
                        } else if (Instruction.isLocalSlotOp(i6)) {
                            int i48 = nextPC;
                            int i49 = nextPC + 1;
                            bArrRealloc[i48] = (byte) i6;
                            int i50 = this.bc_local.getInt();
                            if (z2) {
                                Instruction.setShort(bArrRealloc, i49, i50);
                                nextPC = i49 + 2;
                                if (i6 == 132) {
                                    Instruction.setShort(bArrRealloc, nextPC, this.bc_short.getInt());
                                    nextPC += 2;
                                }
                            } else {
                                Instruction.setByte(bArrRealloc, i49, i50);
                                nextPC = i49 + 1;
                                if (i6 == 132) {
                                    Instruction.setByte(bArrRealloc, nextPC, this.bc_byte.getByte());
                                    nextPC++;
                                }
                            }
                            if (!$assertionsDisabled && Instruction.opLength(i6) != nextPC - i7) {
                                throw new AssertionError();
                            }
                            break;
                        } else {
                            if (i6 >= 202) {
                                Utils.log.warning("unrecognized bytescode " + i6 + " " + Instruction.byteName(i6));
                            }
                            if (!$assertionsDisabled && i6 >= 202) {
                                throw new AssertionError();
                            }
                            int i51 = nextPC;
                            nextPC++;
                            bArrRealloc[i51] = (byte) i6;
                            if (!$assertionsDisabled && Instruction.opLength(i6) != nextPC - i7) {
                                throw new AssertionError();
                            }
                            break;
                        }
                }
                i5++;
            }
            code.setBytes(realloc(bArrRealloc, nextPC));
            code.setInstructionMap(iArrRealloc, i3);
            Instruction instructionAt = null;
            for (int i52 = 0; i52 < i4; i52++) {
                int i53 = iArrRealloc2[i52];
                instructionAt = Instruction.at(code.bytes, i53, instructionAt);
                if (instructionAt instanceof Instruction.Switch) {
                    Instruction.Switch r02 = (Instruction.Switch) instructionAt;
                    r02.setDefaultLabel(getLabel(this.bc_label, code, i53));
                    int caseCount = r02.getCaseCount();
                    for (int i54 = 0; i54 < caseCount; i54++) {
                        r02.setCaseLabel(i54, getLabel(this.bc_label, code, i53));
                    }
                } else {
                    instructionAt.setBranchLabel(getLabel(this.bc_label, code, i53));
                }
            }
            if (fixups.size() > 0) {
                if (this.verbose > 2) {
                    Utils.log.fine("Fixups in code: " + ((Object) fixups));
                }
                code.addFixups(fixups);
            }
        }
    }
}
