package com.sun.java.util.jar.pack;

import com.sun.imageio.plugins.jpeg.JPEG;
import com.sun.java.util.jar.pack.Attribute;
import com.sun.java.util.jar.pack.BandStructure;
import com.sun.java.util.jar.pack.ConstantPool;
import com.sun.java.util.jar.pack.Instruction;
import com.sun.java.util.jar.pack.Package;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Marker;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/PackageWriter.class */
class PackageWriter extends BandStructure {
    Package pkg;
    OutputStream finalOut;
    Package.Version packageVersion;
    Set<ConstantPool.Entry> requiredEntries;
    Map<Attribute.Layout, int[]> backCountTable;
    int[][] attrCounts;
    int[] maxFlags;
    List<Map<Attribute.Layout, int[]>> allLayouts;
    Attribute.Layout[] attrDefsWritten;
    private Code curCode;
    private Package.Class curClass;
    private ConstantPool.Entry[] curCPMap;
    int[] codeHist = new int[256];
    int[] ldcHist = new int[20];
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PackageWriter.class.desiredAssertionStatus();
    }

    PackageWriter(Package r4, OutputStream outputStream) throws IOException {
        this.pkg = r4;
        this.finalOut = outputStream;
        initHighestClassVersion(r4.getHighestClassVersion());
    }

    void write() throws IOException {
        try {
            if (this.verbose > 0) {
                Utils.log.info("Setting up constant pool...");
            }
            setup();
            if (this.verbose > 0) {
                Utils.log.info("Packing...");
            }
            writeConstantPool();
            writeFiles();
            writeAttrDefs();
            writeInnerClasses();
            writeClassesAndByteCodes();
            writeAttrCounts();
            if (this.verbose > 1) {
                printCodeHist();
            }
            if (this.verbose > 0) {
                Utils.log.info("Coding...");
            }
            this.all_bands.chooseBandCodings();
            writeFileHeader();
            writeAllBandsTo(this.finalOut);
        } catch (Exception e2) {
            Utils.log.warning("Error on output: " + ((Object) e2), e2);
            if (this.verbose > 0) {
                this.finalOut.close();
            }
            if (e2 instanceof IOException) {
                throw ((IOException) e2);
            }
            if (!(e2 instanceof RuntimeException)) {
                throw new Error("error packing", e2);
            }
            throw ((RuntimeException) e2);
        }
    }

    void setup() {
        this.requiredEntries = new HashSet();
        setArchiveOptions();
        trimClassAttributes();
        collectAttributeLayouts();
        this.pkg.buildGlobalConstantPool(this.requiredEntries);
        setBandIndexes();
        makeNewAttributeBands();
        collectInnerClasses();
    }

    void chooseDefaultPackageVersion() throws IOException {
        if (this.pkg.packageVersion != null) {
            this.packageVersion = this.pkg.packageVersion;
            if (this.verbose > 0) {
                Utils.log.info("package version overridden with: " + ((Object) this.packageVersion));
                return;
            }
            return;
        }
        Package.Version highestClassVersion = getHighestClassVersion();
        if (highestClassVersion.lessThan(Constants.JAVA6_MAX_CLASS_VERSION)) {
            this.packageVersion = Constants.JAVA5_PACKAGE_VERSION;
        } else if (highestClassVersion.equals(Constants.JAVA6_MAX_CLASS_VERSION) || (highestClassVersion.equals(Constants.JAVA7_MAX_CLASS_VERSION) && !this.pkg.cp.haveExtraTags())) {
            this.packageVersion = Constants.JAVA6_PACKAGE_VERSION;
        } else if (highestClassVersion.equals(Constants.JAVA7_MAX_CLASS_VERSION)) {
            this.packageVersion = Constants.JAVA7_PACKAGE_VERSION;
        } else {
            this.packageVersion = Constants.JAVA8_PACKAGE_VERSION;
        }
        if (this.verbose > 0) {
            Utils.log.info("Highest version class file: " + ((Object) highestClassVersion) + " package version: " + ((Object) this.packageVersion));
        }
    }

    void checkVersion() throws IOException {
        if (!$assertionsDisabled && this.packageVersion == null) {
            throw new AssertionError();
        }
        if (this.packageVersion.lessThan(Constants.JAVA7_PACKAGE_VERSION) && testBit(this.archiveOptions, 8)) {
            throw new IOException("Format bits for Java 7 must be zero in previous releases");
        }
        if (testBit(this.archiveOptions, Constants.AO_UNUSED_MBZ)) {
            throw new IOException("High archive option bits are reserved and must be zero: " + Integer.toHexString(this.archiveOptions));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void setArchiveOptions() {
        int i2 = this.pkg.default_modtime;
        int i3 = this.pkg.default_modtime;
        int i4 = -1;
        int i5 = 0;
        this.archiveOptions |= this.pkg.default_options;
        Iterator<Package.File> it = this.pkg.files.iterator();
        while (it.hasNext()) {
            Package.File next = it.next();
            int i6 = next.modtime;
            int i7 = next.options;
            if (i2 == 0) {
                i3 = i6;
                i2 = i6;
            } else {
                if (i2 > i6) {
                    i2 = i6;
                }
                if (i3 < i6) {
                    i3 = i6;
                }
            }
            i4 &= i7;
            i5 |= i7;
        }
        if (this.pkg.default_modtime == 0) {
            this.pkg.default_modtime = i2;
        }
        if (i2 != 0 && i2 != i3) {
            this.archiveOptions |= 64;
        }
        if (!testBit(this.archiveOptions, 32) && i4 != -1) {
            if (testBit(i4, 1)) {
                this.archiveOptions |= 32;
                i4--;
                i5--;
            }
            this.pkg.default_options |= i4;
            if (i4 != i5 || i4 != this.pkg.default_options) {
                this.archiveOptions |= 128;
            }
        }
        HashMap map = new HashMap();
        int i8 = 0;
        Package.Version version = null;
        Iterator<Package.Class> it2 = this.pkg.classes.iterator();
        while (it2.hasNext()) {
            Package.Version version2 = it2.next().getVersion();
            int[] iArr = (int[]) map.get(version2);
            if (iArr == null) {
                iArr = new int[1];
                map.put(version2, iArr);
            }
            int[] iArr2 = iArr;
            int i9 = iArr2[0] + 1;
            iArr2[0] = i9;
            if (i8 < i9) {
                i8 = i9;
                version = version2;
            }
        }
        map.clear();
        if (version == null) {
            version = Constants.JAVA_MIN_CLASS_VERSION;
        }
        this.pkg.defaultClassVersion = version;
        if (this.verbose > 0) {
            Utils.log.info("Consensus version number in segment is " + ((Object) version));
        }
        if (this.verbose > 0) {
            Utils.log.info("Highest version number in segment is " + ((Object) this.pkg.getHighestClassVersion()));
        }
        Iterator<Package.Class> it3 = this.pkg.classes.iterator();
        while (it3.hasNext()) {
            Package.Class next2 = it3.next();
            if (!next2.getVersion().equals(version)) {
                Attribute attributeMakeClassFileVersionAttr = makeClassFileVersionAttr(next2.getVersion());
                if (this.verbose > 1) {
                    Utils.log.fine("Version " + ((Object) next2.getVersion()) + " of " + ((Object) next2) + " doesn't match package version " + ((Object) version));
                }
                next2.addAttribute(attributeMakeClassFileVersionAttr);
            }
        }
        Iterator<Package.File> it4 = this.pkg.files.iterator();
        while (true) {
            if (!it4.hasNext()) {
                break;
            }
            Package.File next3 = it4.next();
            if (next3.getFileLength() != ((int) r0)) {
                this.archiveOptions |= 256;
                if (this.verbose > 0) {
                    Utils.log.info("Note: Huge resource file " + ((Object) next3.getFileName()) + " forces 64-bit sizing");
                }
            }
        }
        int i10 = 0;
        int i11 = 0;
        Iterator<Package.Class> it5 = this.pkg.classes.iterator();
        while (it5.hasNext()) {
            for (Package.Class.Method method : it5.next().getMethods()) {
                if (method.code != null) {
                    if (method.code.attributeSize() == 0) {
                        i11++;
                    } else if (shortCodeHeader(method.code) != 0) {
                        i10 += 3;
                    }
                }
            }
        }
        if (i10 > i11) {
            this.archiveOptions |= 4;
        }
        if (this.verbose > 0) {
            Utils.log.info("archiveOptions = 0b" + Integer.toBinaryString(this.archiveOptions));
        }
    }

    void writeFileHeader() throws IOException {
        chooseDefaultPackageVersion();
        writeArchiveMagic();
        writeArchiveHeader();
    }

    private void putMagicInt32(int i2) throws IOException {
        int i3 = i2;
        for (int i4 = 0; i4 < 4; i4++) {
            this.archive_magic.putByte(255 & (i3 >>> 24));
            i3 <<= 8;
        }
    }

    void writeArchiveMagic() throws IOException {
        this.pkg.getClass();
        putMagicInt32(Constants.JAVA_PACKAGE_MAGIC);
    }

    void writeArchiveHeader() throws IOException {
        int i2 = 15;
        boolean zTestBit = testBit(this.archiveOptions, 1);
        if (!zTestBit) {
            zTestBit = zTestBit | (this.band_headers.length() != 0) | (this.attrDefsWritten.length != 0);
            if (zTestBit) {
                this.archiveOptions |= 1;
            }
        }
        if (zTestBit) {
            i2 = 15 + 2;
        }
        boolean zTestBit2 = testBit(this.archiveOptions, 16);
        if (!zTestBit2) {
            zTestBit2 = zTestBit2 | (this.archiveNextCount > 0) | (this.pkg.default_modtime != 0);
            if (zTestBit2) {
                this.archiveOptions |= 16;
            }
        }
        if (zTestBit2) {
            i2 += 5;
        }
        boolean zTestBit3 = testBit(this.archiveOptions, 2);
        if (!zTestBit3) {
            zTestBit3 |= this.pkg.cp.haveNumbers();
            if (zTestBit3) {
                this.archiveOptions |= 2;
            }
        }
        if (zTestBit3) {
            i2 += 4;
        }
        boolean zTestBit4 = testBit(this.archiveOptions, 8);
        if (!zTestBit4) {
            zTestBit4 |= this.pkg.cp.haveExtraTags();
            if (zTestBit4) {
                this.archiveOptions |= 8;
            }
        }
        if (zTestBit4) {
            i2 += 4;
        }
        checkVersion();
        this.archive_header_0.putInt(this.packageVersion.minor);
        this.archive_header_0.putInt(this.packageVersion.major);
        if (this.verbose > 0) {
            Utils.log.info("Package Version for this segment:" + ((Object) this.packageVersion));
        }
        this.archive_header_0.putInt(this.archiveOptions);
        if (!$assertionsDisabled && this.archive_header_0.length() != 3) {
            throw new AssertionError();
        }
        if (zTestBit2) {
            if (!$assertionsDisabled && this.archive_header_S.length() != 0) {
                throw new AssertionError();
            }
            this.archive_header_S.putInt(0);
            if (!$assertionsDisabled && this.archive_header_S.length() != 1) {
                throw new AssertionError();
            }
            this.archive_header_S.putInt(0);
            if (!$assertionsDisabled && this.archive_header_S.length() != 2) {
                throw new AssertionError();
            }
        }
        if (zTestBit2) {
            this.archive_header_1.putInt(this.archiveNextCount);
            this.archive_header_1.putInt(this.pkg.default_modtime);
            this.archive_header_1.putInt(this.pkg.files.size());
        } else if (!$assertionsDisabled && !this.pkg.files.isEmpty()) {
            throw new AssertionError();
        }
        if (zTestBit) {
            this.archive_header_1.putInt(this.band_headers.length());
            this.archive_header_1.putInt(this.attrDefsWritten.length);
        } else {
            if (!$assertionsDisabled && this.band_headers.length() != 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.attrDefsWritten.length != 0) {
                throw new AssertionError();
            }
        }
        writeConstantPoolCounts(zTestBit3, zTestBit4);
        this.archive_header_1.putInt(this.pkg.getAllInnerClasses().size());
        this.archive_header_1.putInt(this.pkg.defaultClassVersion.minor);
        this.archive_header_1.putInt(this.pkg.defaultClassVersion.major);
        this.archive_header_1.putInt(this.pkg.classes.size());
        if (!$assertionsDisabled && this.archive_header_0.length() + this.archive_header_S.length() + this.archive_header_1.length() != i2) {
            throw new AssertionError();
        }
        this.archiveSize0 = 0L;
        this.archiveSize1 = this.all_bands.outputSize();
        this.archiveSize0 += this.archive_magic.outputSize();
        this.archiveSize0 += this.archive_header_0.outputSize();
        this.archiveSize0 += this.archive_header_S.outputSize();
        this.archiveSize1 -= this.archiveSize0;
        if (zTestBit2) {
            int i3 = (int) (this.archiveSize1 >>> 32);
            int i4 = (int) (this.archiveSize1 >>> 0);
            this.archive_header_S.patchValue(0, i3);
            this.archive_header_S.patchValue(1, i4);
            int length = UNSIGNED5.getLength(0);
            this.archiveSize0 += UNSIGNED5.getLength(i3) - length;
            this.archiveSize0 += UNSIGNED5.getLength(i4) - length;
        }
        if (this.verbose > 1) {
            Utils.log.fine("archive sizes: " + this.archiveSize0 + Marker.ANY_NON_NULL_MARKER + this.archiveSize1);
        }
        if (!$assertionsDisabled && this.all_bands.outputSize() != this.archiveSize0 + this.archiveSize1) {
            throw new AssertionError();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00d9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void writeConstantPoolCounts(boolean r4, boolean r5) throws java.io.IOException {
        /*
            r3 = this;
            byte[] r0 = com.sun.java.util.jar.pack.ConstantPool.TAGS_IN_ORDER
            r6 = r0
            r0 = r6
            int r0 = r0.length
            r7 = r0
            r0 = 0
            r8 = r0
        Lb:
            r0 = r8
            r1 = r7
            if (r0 >= r1) goto Le8
            r0 = r6
            r1 = r8
            r0 = r0[r1]
            r9 = r0
            r0 = r3
            com.sun.java.util.jar.pack.Package r0 = r0.pkg
            com.sun.java.util.jar.pack.ConstantPool$IndexGroup r0 = r0.cp
            r1 = r9
            com.sun.java.util.jar.pack.ConstantPool$Index r0 = r0.getIndexByTag(r1)
            int r0 = r0.size()
            r10 = r0
            r0 = r9
            switch(r0) {
                case 1: goto L80;
                case 2: goto Ld9;
                case 3: goto Lab;
                case 4: goto Lab;
                case 5: goto Lab;
                case 6: goto Lab;
                case 7: goto Ld9;
                case 8: goto Ld9;
                case 9: goto Ld9;
                case 10: goto Ld9;
                case 11: goto Ld9;
                case 12: goto Ld9;
                case 13: goto Ld9;
                case 14: goto Ld9;
                case 15: goto Lc2;
                case 16: goto Lc2;
                case 17: goto Lc2;
                case 18: goto Lc2;
                default: goto Ld9;
            }
        L80:
            r0 = r10
            if (r0 <= 0) goto Ld9
            boolean r0 = com.sun.java.util.jar.pack.PackageWriter.$assertionsDisabled
            if (r0 != 0) goto Ld9
            r0 = r3
            com.sun.java.util.jar.pack.Package r0 = r0.pkg
            com.sun.java.util.jar.pack.ConstantPool$IndexGroup r0 = r0.cp
            r1 = r9
            com.sun.java.util.jar.pack.ConstantPool$Index r0 = r0.getIndexByTag(r1)
            r1 = 0
            com.sun.java.util.jar.pack.ConstantPool$Entry r0 = r0.get(r1)
            java.lang.String r1 = ""
            com.sun.java.util.jar.pack.ConstantPool$Utf8Entry r1 = com.sun.java.util.jar.pack.ConstantPool.getUtf8Entry(r1)
            if (r0 == r1) goto Ld9
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r1.<init>()
            throw r0
        Lab:
            r0 = r4
            if (r0 != 0) goto Ld9
            boolean r0 = com.sun.java.util.jar.pack.PackageWriter.$assertionsDisabled
            if (r0 != 0) goto Le2
            r0 = r10
            if (r0 == 0) goto Le2
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r1.<init>()
            throw r0
        Lc2:
            r0 = r5
            if (r0 != 0) goto Ld9
            boolean r0 = com.sun.java.util.jar.pack.PackageWriter.$assertionsDisabled
            if (r0 != 0) goto Le2
            r0 = r10
            if (r0 == 0) goto Le2
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r1 = r0
            r1.<init>()
            throw r0
        Ld9:
            r0 = r3
            com.sun.java.util.jar.pack.BandStructure$IntBand r0 = r0.archive_header_1
            r1 = r10
            r0.putInt(r1)
        Le2:
            int r8 = r8 + 1
            goto Lb
        Le8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.PackageWriter.writeConstantPoolCounts(boolean, boolean):void");
    }

    @Override // com.sun.java.util.jar.pack.BandStructure
    protected ConstantPool.Index getCPIndex(byte b2) {
        return this.pkg.cp.getIndexByTag(b2);
    }

    void writeConstantPool() throws IOException {
        PrintStream printStream;
        ConstantPool.IndexGroup indexGroup = this.pkg.cp;
        if (this.verbose > 0) {
            Utils.log.info("Writing CP");
        }
        for (byte b2 : ConstantPool.TAGS_IN_ORDER) {
            ConstantPool.Index indexByTag = indexGroup.getIndexByTag(b2);
            ConstantPool.Entry[] entryArr = indexByTag.cpMap;
            if (this.verbose > 0) {
                Utils.log.info("Writing " + entryArr.length + " " + ConstantPool.tagName(b2) + " entries...");
            }
            if (this.optDumpBands) {
                printStream = new PrintStream(getDumpStream(indexByTag, ".idx"));
                Throwable th = null;
                try {
                    try {
                        printArrayTo(printStream, entryArr, 0, entryArr.length);
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
                    } catch (Throwable th3) {
                        th = th3;
                        throw th3;
                    }
                } finally {
                }
            }
            switch (b2) {
                case 1:
                    writeUtf8Bands(entryArr);
                    break;
                case 2:
                case 14:
                default:
                    throw new AssertionError((Object) "unexpected CP tag in package");
                case 3:
                    for (ConstantPool.Entry entry : entryArr) {
                        this.cp_Int.putInt(((Integer) ((ConstantPool.NumberEntry) entry).numberValue()).intValue());
                    }
                    break;
                case 4:
                    for (ConstantPool.Entry entry2 : entryArr) {
                        this.cp_Float.putInt(Float.floatToIntBits(((Float) ((ConstantPool.NumberEntry) entry2).numberValue()).floatValue()));
                    }
                    break;
                case 5:
                    for (ConstantPool.Entry entry3 : entryArr) {
                        long jLongValue = ((Long) ((ConstantPool.NumberEntry) entry3).numberValue()).longValue();
                        this.cp_Long_hi.putInt((int) (jLongValue >>> 32));
                        this.cp_Long_lo.putInt((int) (jLongValue >>> 0));
                    }
                    break;
                case 6:
                    for (ConstantPool.Entry entry4 : entryArr) {
                        long jDoubleToLongBits = Double.doubleToLongBits(((Double) ((ConstantPool.NumberEntry) entry4).numberValue()).doubleValue());
                        this.cp_Double_hi.putInt((int) (jDoubleToLongBits >>> 32));
                        this.cp_Double_lo.putInt((int) (jDoubleToLongBits >>> 0));
                    }
                    break;
                case 7:
                    for (ConstantPool.Entry entry5 : entryArr) {
                        this.cp_Class.putRef(((ConstantPool.ClassEntry) entry5).ref);
                    }
                    break;
                case 8:
                    for (ConstantPool.Entry entry6 : entryArr) {
                        this.cp_String.putRef(((ConstantPool.StringEntry) entry6).ref);
                    }
                    break;
                case 9:
                    writeMemberRefs(b2, entryArr, this.cp_Field_class, this.cp_Field_desc);
                    break;
                case 10:
                    writeMemberRefs(b2, entryArr, this.cp_Method_class, this.cp_Method_desc);
                    break;
                case 11:
                    writeMemberRefs(b2, entryArr, this.cp_Imethod_class, this.cp_Imethod_desc);
                    break;
                case 12:
                    for (ConstantPool.Entry entry7 : entryArr) {
                        ConstantPool.DescriptorEntry descriptorEntry = (ConstantPool.DescriptorEntry) entry7;
                        this.cp_Descr_name.putRef(descriptorEntry.nameRef);
                        this.cp_Descr_type.putRef(descriptorEntry.typeRef);
                    }
                    break;
                case 13:
                    writeSignatureBands(entryArr);
                    break;
                case 15:
                    for (ConstantPool.Entry entry8 : entryArr) {
                        ConstantPool.MethodHandleEntry methodHandleEntry = (ConstantPool.MethodHandleEntry) entry8;
                        this.cp_MethodHandle_refkind.putInt(methodHandleEntry.refKind);
                        this.cp_MethodHandle_member.putRef(methodHandleEntry.memRef);
                    }
                    break;
                case 16:
                    for (ConstantPool.Entry entry9 : entryArr) {
                        this.cp_MethodType.putRef(((ConstantPool.MethodTypeEntry) entry9).typeRef);
                    }
                    break;
                case 17:
                    for (ConstantPool.Entry entry10 : entryArr) {
                        ConstantPool.BootstrapMethodEntry bootstrapMethodEntry = (ConstantPool.BootstrapMethodEntry) entry10;
                        this.cp_BootstrapMethod_ref.putRef(bootstrapMethodEntry.bsmRef);
                        this.cp_BootstrapMethod_arg_count.putInt(bootstrapMethodEntry.argRefs.length);
                        for (ConstantPool.Entry entry11 : bootstrapMethodEntry.argRefs) {
                            this.cp_BootstrapMethod_arg.putRef(entry11);
                        }
                    }
                    break;
                case 18:
                    for (ConstantPool.Entry entry12 : entryArr) {
                        ConstantPool.InvokeDynamicEntry invokeDynamicEntry = (ConstantPool.InvokeDynamicEntry) entry12;
                        this.cp_InvokeDynamic_spec.putRef(invokeDynamicEntry.bssRef);
                        this.cp_InvokeDynamic_desc.putRef(invokeDynamicEntry.descRef);
                    }
                    break;
            }
        }
        if (this.optDumpBands || this.verbose > 1) {
            byte b3 = 50;
            while (true) {
                byte b4 = b3;
                if (b4 < 54) {
                    ConstantPool.Index indexByTag2 = indexGroup.getIndexByTag(b4);
                    if (indexByTag2 != null && !indexByTag2.isEmpty()) {
                        ConstantPool.Entry[] entryArr2 = indexByTag2.cpMap;
                        if (this.verbose > 1) {
                            Utils.log.info("Index group " + ConstantPool.tagName(b4) + " contains " + entryArr2.length + " entries.");
                        }
                        if (this.optDumpBands) {
                            printStream = new PrintStream(getDumpStream(indexByTag2.debugName, b4, ".gidx", indexByTag2));
                            Throwable th4 = null;
                            try {
                                try {
                                    printArrayTo(printStream, entryArr2, 0, entryArr2.length, true);
                                    if (printStream != null) {
                                        if (0 != 0) {
                                            try {
                                                printStream.close();
                                            } catch (Throwable th5) {
                                                th4.addSuppressed(th5);
                                            }
                                        } else {
                                            printStream.close();
                                        }
                                    }
                                } catch (Throwable th6) {
                                    th4 = th6;
                                    throw th6;
                                }
                            } finally {
                            }
                        } else {
                            continue;
                        }
                    }
                    b3 = (byte) (b4 + 1);
                } else {
                    return;
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void writeUtf8Bands(ConstantPool.Entry[] entryArr) throws IOException {
        if (entryArr.length == 0) {
            return;
        }
        if (!$assertionsDisabled && !entryArr[0].stringValue().equals("")) {
            throw new AssertionError();
        }
        char[] cArr = new char[entryArr.length];
        for (int i2 = 0; i2 < cArr.length; i2++) {
            cArr[i2] = entryArr[i2].stringValue().toCharArray();
        }
        int[] iArr = new int[entryArr.length];
        char[] cArr2 = new char[0];
        for (int i3 = 0; i3 < cArr.length; i3++) {
            int i4 = 0;
            char[] cArr3 = cArr[i3];
            int iMin = Math.min(cArr3.length, cArr2.length);
            while (i4 < iMin && cArr3[i4] == cArr2[i4]) {
                i4++;
            }
            iArr[i3] = i4;
            if (i3 >= 2) {
                this.cp_Utf8_prefix.putInt(i4);
            } else if (!$assertionsDisabled && i4 != 0) {
                throw new AssertionError();
            }
            cArr2 = cArr3;
        }
        int i5 = 0;
        while (i5 < cArr.length) {
            char[] cArr4 = cArr[i5];
            int i6 = iArr[i5];
            int length = cArr4.length - iArr[i5];
            boolean zTryAlternateEncoding = false;
            if (length == 0) {
                zTryAlternateEncoding = i5 >= 1;
            } else if (this.optBigStrings && this.effort > 1 && length > 100) {
                int i7 = 0;
                for (int i8 = 0; i8 < length; i8++) {
                    if (cArr4[i6 + i8] > 127) {
                        i7++;
                    }
                }
                if (i7 > 100) {
                    zTryAlternateEncoding = tryAlternateEncoding(i5, i7, cArr4, i6);
                }
            }
            if (i5 < 1) {
                if (!$assertionsDisabled && zTryAlternateEncoding) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && length != 0) {
                    throw new AssertionError();
                }
            } else if (zTryAlternateEncoding) {
                this.cp_Utf8_suffix.putInt(0);
                this.cp_Utf8_big_suffix.putInt(length);
            } else {
                if (!$assertionsDisabled && length == 0) {
                    throw new AssertionError();
                }
                this.cp_Utf8_suffix.putInt(length);
                for (int i9 = 0; i9 < length; i9++) {
                    this.cp_Utf8_chars.putInt(cArr4[i6 + i9]);
                }
            }
            i5++;
        }
        if (this.verbose > 0) {
            int length2 = this.cp_Utf8_chars.length();
            int length3 = this.cp_Utf8_big_chars.length();
            Utils.log.info("Utf8string #CHARS=" + (length2 + length3) + " #PACKEDCHARS=" + length3);
        }
    }

    private boolean tryAlternateEncoding(int i2, int i3, char[] cArr, int i4) {
        int length = cArr.length - i4;
        int[] iArr = new int[length];
        for (int i5 = 0; i5 < length; i5++) {
            iArr[i5] = cArr[i4 + i5];
        }
        CodingChooser codingChooser = getCodingChooser();
        Coding coding = this.cp_Utf8_big_chars.regularCoding;
        String str = "(Utf8_big_" + i2 + ")";
        int[] iArr2 = {0, 0};
        if (this.verbose > 1 || codingChooser.verbose > 1) {
            Utils.log.fine("--- chooseCoding " + str);
        }
        CodingMethod codingMethodChoose = codingChooser.choose(iArr, coding, iArr2);
        Coding coding2 = this.cp_Utf8_chars.regularCoding;
        if (this.verbose > 1) {
            Utils.log.fine("big string[" + i2 + "] len=" + length + " #wide=" + i3 + " size=" + iArr2[0] + "/z=" + iArr2[1] + " coding " + ((Object) codingMethodChoose));
        }
        if (codingMethodChoose != coding2) {
            int i6 = iArr2[1];
            int[] iArrComputeSize = codingChooser.computeSize(coding2, iArr);
            int i7 = iArrComputeSize[1];
            int iMax = Math.max(5, i7 / 1000);
            if (this.verbose > 1) {
                Utils.log.fine("big string[" + i2 + "] normalSize=" + iArrComputeSize[0] + "/z=" + iArrComputeSize[1] + " win=" + (i6 < i7 - iMax));
            }
            if (i6 < i7 - iMax) {
                this.cp_Utf8_big_chars.newIntBand(str).initializeValues(iArr);
                return true;
            }
            return false;
        }
        return false;
    }

    void writeSignatureBands(ConstantPool.Entry[] entryArr) throws IOException {
        for (ConstantPool.Entry entry : entryArr) {
            ConstantPool.SignatureEntry signatureEntry = (ConstantPool.SignatureEntry) entry;
            this.cp_Signature_form.putRef(signatureEntry.formRef);
            for (int i2 = 0; i2 < signatureEntry.classRefs.length; i2++) {
                this.cp_Signature_classes.putRef(signatureEntry.classRefs[i2]);
            }
        }
    }

    void writeMemberRefs(byte b2, ConstantPool.Entry[] entryArr, BandStructure.CPRefBand cPRefBand, BandStructure.CPRefBand cPRefBand2) throws IOException {
        for (ConstantPool.Entry entry : entryArr) {
            ConstantPool.MemberEntry memberEntry = (ConstantPool.MemberEntry) entry;
            cPRefBand.putRef(memberEntry.classRef);
            cPRefBand2.putRef(memberEntry.descRef);
        }
    }

    void writeFiles() throws IOException {
        int size = this.pkg.files.size();
        if (size == 0) {
            return;
        }
        int i2 = this.archiveOptions;
        boolean zTestBit = testBit(i2, 256);
        boolean zTestBit2 = testBit(i2, 64);
        boolean zTestBit3 = testBit(i2, 128);
        if (!zTestBit3) {
            Iterator<Package.File> it = this.pkg.files.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (it.next().isClassStub()) {
                    zTestBit3 = true;
                    i2 |= 128;
                    this.archiveOptions = i2;
                    break;
                }
            }
        }
        if (zTestBit || zTestBit2 || zTestBit3 || !this.pkg.files.isEmpty()) {
            this.archiveOptions = i2 | 16;
        }
        Iterator<Package.File> it2 = this.pkg.files.iterator();
        while (it2.hasNext()) {
            Package.File next = it2.next();
            this.file_name.putRef(next.name);
            long fileLength = next.getFileLength();
            this.file_size_lo.putInt((int) fileLength);
            if (zTestBit) {
                this.file_size_hi.putInt((int) (fileLength >>> 32));
            }
            if (zTestBit2) {
                this.file_modtime.putInt(next.modtime - this.pkg.default_modtime);
            }
            if (zTestBit3) {
                this.file_options.putInt(next.options);
            }
            next.writeTo(this.file_bits.collectorStream());
            if (this.verbose > 1) {
                Utils.log.fine("Wrote " + fileLength + " bytes of " + next.name.stringValue());
            }
        }
        if (this.verbose > 0) {
            Utils.log.info("Wrote " + size + " resource files");
        }
    }

    /* JADX WARN: Type inference failed for: r1v8, types: [int[], int[][]] */
    void collectAttributeLayouts() {
        int attributeLayoutIndex;
        this.maxFlags = new int[4];
        this.allLayouts = new FixedList(4);
        for (int i2 = 0; i2 < 4; i2++) {
            this.allLayouts.set(i2, new HashMap());
        }
        Iterator<Package.Class> it = this.pkg.classes.iterator();
        while (it.hasNext()) {
            Package.Class next = it.next();
            visitAttributeLayoutsIn(0, next);
            Iterator<Package.Class.Field> it2 = next.getFields().iterator();
            while (it2.hasNext()) {
                visitAttributeLayoutsIn(1, (Package.Class.Field) it2.next());
            }
            for (Package.Class.Method method : next.getMethods()) {
                visitAttributeLayoutsIn(2, method);
                if (method.code != null) {
                    visitAttributeLayoutsIn(3, method.code);
                }
            }
        }
        for (int i3 = 0; i3 < 4; i3++) {
            int size = this.allLayouts.get(i3).size();
            boolean zHaveFlagsHi = haveFlagsHi(i3);
            if (size >= 24) {
                this.archiveOptions |= 1 << (9 + i3);
                zHaveFlagsHi = true;
                if (this.verbose > 0) {
                    Utils.log.info("Note: Many " + Attribute.contextName(i3) + " attributes forces 63-bit flags");
                }
            }
            if (this.verbose > 1) {
                Utils.log.fine(Attribute.contextName(i3) + ".maxFlags = 0x" + Integer.toHexString(this.maxFlags[i3]));
                Utils.log.fine(Attribute.contextName(i3) + ".#layouts = " + size);
            }
            if (!$assertionsDisabled && haveFlagsHi(i3) != zHaveFlagsHi) {
                throw new AssertionError();
            }
        }
        initAttrIndexLimit();
        for (int i4 = 0; i4 < 4; i4++) {
            if (!$assertionsDisabled && (this.attrFlagMask[i4] & this.maxFlags[i4]) != 0) {
                throw new AssertionError();
            }
        }
        this.backCountTable = new HashMap();
        this.attrCounts = new int[4];
        for (int i5 = 0; i5 < 4; i5++) {
            long j2 = (this.maxFlags[i5] | this.attrFlagMask[i5]) ^ (-1);
            if (!$assertionsDisabled && this.attrIndexLimit[i5] <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.attrIndexLimit[i5] >= 64) {
                throw new AssertionError();
            }
            long j3 = j2 & ((1 << this.attrIndexLimit[i5]) - 1);
            int i6 = 0;
            Map<Attribute.Layout, int[]> map = this.allLayouts.get(i5);
            Map.Entry[] entryArr = new Map.Entry[map.size()];
            map.entrySet().toArray(entryArr);
            Arrays.sort(entryArr, new Comparator<Map.Entry<Attribute.Layout, int[]>>() { // from class: com.sun.java.util.jar.pack.PackageWriter.1
                @Override // java.util.Comparator
                public int compare(Map.Entry<Attribute.Layout, int[]> entry, Map.Entry<Attribute.Layout, int[]> entry2) {
                    int i7 = -(entry.getValue()[0] - entry2.getValue()[0]);
                    return i7 != 0 ? i7 : entry.getKey().compareTo(entry2.getKey());
                }
            });
            this.attrCounts[i5] = new int[this.attrIndexLimit[i5] + entryArr.length];
            for (Map.Entry entry : entryArr) {
                Attribute.Layout layout = (Attribute.Layout) entry.getKey();
                int i7 = ((int[]) entry.getValue())[0];
                Integer num = this.attrIndexTable.get(layout);
                if (num != null) {
                    attributeLayoutIndex = num.intValue();
                } else if (j3 != 0) {
                    while ((j3 & 1) == 0) {
                        j3 >>>= 1;
                        i6++;
                    }
                    j3--;
                    attributeLayoutIndex = setAttributeLayoutIndex(layout, i6);
                } else {
                    attributeLayoutIndex = setAttributeLayoutIndex(layout, -1);
                }
                this.attrCounts[i5][attributeLayoutIndex] = i7;
                Attribute.Layout.Element[] callables = layout.getCallables();
                int[] iArr = new int[callables.length];
                for (int i8 = 0; i8 < callables.length; i8++) {
                    if (!$assertionsDisabled && callables[i8].kind != 10) {
                        throw new AssertionError();
                    }
                    if (!callables[i8].flagTest((byte) 8)) {
                        iArr[i8] = -1;
                    }
                }
                this.backCountTable.put(layout, iArr);
                if (num == null) {
                    ConstantPool.Utf8Entry utf8Entry = ConstantPool.getUtf8Entry(layout.name());
                    ConstantPool.Utf8Entry utf8Entry2 = ConstantPool.getUtf8Entry(layout.layoutForClassVersion(getHighestClassVersion()));
                    this.requiredEntries.add(utf8Entry);
                    this.requiredEntries.add(utf8Entry2);
                    if (this.verbose > 0) {
                        if (attributeLayoutIndex < this.attrIndexLimit[i5]) {
                            Utils.log.info("Using free flag bit 1<<" + attributeLayoutIndex + " for " + i7 + " occurrences of " + ((Object) layout));
                        } else {
                            Utils.log.info("Using overflow index " + attributeLayoutIndex + " for " + i7 + " occurrences of " + ((Object) layout));
                        }
                    }
                }
            }
        }
        this.maxFlags = null;
        this.allLayouts = null;
    }

    void visitAttributeLayoutsIn(int i2, Attribute.Holder holder) {
        int[] iArr = this.maxFlags;
        iArr[i2] = iArr[i2] | holder.flags;
        Iterator<Attribute> it = holder.getAttributes().iterator();
        while (it.hasNext()) {
            Attribute.Layout layout = it.next().layout();
            Map<Attribute.Layout, int[]> map = this.allLayouts.get(i2);
            int[] iArr2 = map.get(layout);
            if (iArr2 == null) {
                int[] iArr3 = new int[1];
                iArr2 = iArr3;
                map.put(layout, iArr3);
            }
            if (iArr2[0] < Integer.MAX_VALUE) {
                int[] iArr4 = iArr2;
                iArr4[0] = iArr4[0] + 1;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:19:0x006d A[PHI: r11
  0x006d: PHI (r11v3 int) = (r11v2 int), (r11v4 int) binds: [B:9:0x0036, B:17:0x0067] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void writeAttrDefs() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 640
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.PackageWriter.writeAttrDefs():void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x010e, code lost:
    
        r6 = r6 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void writeAttrCounts() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 277
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.PackageWriter.writeAttrCounts():void");
    }

    void trimClassAttributes() {
        Iterator<Package.Class> it = this.pkg.classes.iterator();
        while (it.hasNext()) {
            Package.Class next = it.next();
            next.minimizeSourceFile();
            if (!$assertionsDisabled && next.getAttribute(Package.attrBootstrapMethodsEmpty) != null) {
                throw new AssertionError();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    void collectInnerClasses() {
        HashMap map = new HashMap();
        Iterator<Package.Class> it = this.pkg.classes.iterator();
        while (it.hasNext()) {
            Package.Class next = it.next();
            if (next.hasInnerClasses()) {
                for (Package.InnerClass innerClass : next.getInnerClasses()) {
                    Package.InnerClass innerClass2 = (Package.InnerClass) map.put(innerClass.thisClass, innerClass);
                    if (innerClass2 != null && !innerClass2.equals(innerClass) && innerClass2.predictable) {
                        map.put(innerClass2.thisClass, innerClass2);
                    }
                }
            }
        }
        Package.InnerClass[] innerClassArr = new Package.InnerClass[map.size()];
        map.values().toArray(innerClassArr);
        Arrays.sort(innerClassArr);
        this.pkg.setAllInnerClasses(Arrays.asList(innerClassArr));
        Iterator<Package.Class> it2 = this.pkg.classes.iterator();
        while (it2.hasNext()) {
            it2.next().minimizeLocalICs();
        }
    }

    void writeInnerClasses() throws IOException {
        for (Package.InnerClass innerClass : this.pkg.getAllInnerClasses()) {
            int i2 = innerClass.flags;
            if (!$assertionsDisabled && (i2 & 65536) != 0) {
                throw new AssertionError();
            }
            if (!innerClass.predictable) {
                i2 |= 65536;
            }
            this.ic_this_class.putRef(innerClass.thisClass);
            this.ic_flags.putInt(i2);
            if (!innerClass.predictable) {
                this.ic_outer_class.putRef(innerClass.outerClass);
                this.ic_name.putRef(innerClass.name);
            }
        }
    }

    void writeLocalInnerClasses(Package.Class r5) throws IOException {
        List<Package.InnerClass> innerClasses = r5.getInnerClasses();
        this.class_InnerClasses_N.putInt(innerClasses.size());
        for (Package.InnerClass innerClass : innerClasses) {
            this.class_InnerClasses_RC.putRef(innerClass.thisClass);
            if (innerClass.equals(this.pkg.getGlobalInnerClass(innerClass.thisClass))) {
                this.class_InnerClasses_F.putInt(0);
            } else {
                int i2 = innerClass.flags;
                if (i2 == 0) {
                    i2 = 65536;
                }
                this.class_InnerClasses_F.putInt(i2);
                this.class_InnerClasses_outer_RCN.putRef(innerClass.outerClass);
                this.class_InnerClasses_name_RUN.putRef(innerClass.name);
            }
        }
    }

    void writeClassesAndByteCodes() throws IOException {
        Package.Class[] classArr = new Package.Class[this.pkg.classes.size()];
        this.pkg.classes.toArray(classArr);
        if (this.verbose > 0) {
            Utils.log.info("  ...scanning " + classArr.length + " classes...");
        }
        int i2 = 0;
        for (Package.Class r0 : classArr) {
            if (this.verbose > 1) {
                Utils.log.fine("Scanning " + ((Object) r0));
            }
            ConstantPool.ClassEntry classEntry = r0.thisClass;
            ConstantPool.ClassEntry classEntry2 = r0.superClass;
            ConstantPool.ClassEntry[] classEntryArr = r0.interfaces;
            if (!$assertionsDisabled && classEntry2 == classEntry) {
                throw new AssertionError();
            }
            if (classEntry2 == null) {
                classEntry2 = classEntry;
            }
            this.class_this.putRef(classEntry);
            this.class_super.putRef(classEntry2);
            this.class_interface_count.putInt(r0.interfaces.length);
            for (ConstantPool.ClassEntry classEntry3 : classEntryArr) {
                this.class_interface.putRef(classEntry3);
            }
            writeMembers(r0);
            writeAttrs(0, r0, r0);
            i2++;
            if (this.verbose > 0 && i2 % 1000 == 0) {
                Utils.log.info("Have scanned " + i2 + " classes...");
            }
        }
    }

    void writeMembers(Package.Class r6) throws IOException {
        List<Package.Class.Field> fields = r6.getFields();
        this.class_field_count.putInt(fields.size());
        for (Package.Class.Field field : fields) {
            this.field_descr.putRef(field.getDescriptor());
            writeAttrs(1, field, r6);
        }
        List<Package.Class.Method> methods = r6.getMethods();
        this.class_method_count.putInt(methods.size());
        for (Package.Class.Method method : methods) {
            this.method_descr.putRef(method.getDescriptor());
            writeAttrs(2, method, r6);
            if (!$assertionsDisabled) {
                if ((method.code != null) != (method.getAttribute(this.attrCodeEmpty) != null)) {
                    throw new AssertionError();
                }
            }
            if (method.code != null) {
                writeCodeHeader(method.code);
                writeByteCodes(method.code);
            }
        }
    }

    void writeCodeHeader(Code code) throws IOException {
        boolean zTestBit = testBit(this.archiveOptions, 4);
        int iAttributeSize = code.attributeSize();
        int iShortCodeHeader = shortCodeHeader(code);
        if (!zTestBit && iAttributeSize > 0) {
            iShortCodeHeader = 0;
        }
        if (this.verbose > 2) {
            Utils.log.fine("Code sizes info " + code.max_stack + " " + code.max_locals + " " + code.getHandlerCount() + " " + code.getMethod().getArgumentSize() + " " + iAttributeSize + (iShortCodeHeader > 0 ? " SHORT=" + iShortCodeHeader : ""));
        }
        this.code_headers.putByte(iShortCodeHeader);
        if (iShortCodeHeader == 0) {
            this.code_max_stack.putInt(code.getMaxStack());
            this.code_max_na_locals.putInt(code.getMaxNALocals());
            this.code_handler_count.putInt(code.getHandlerCount());
        } else {
            if (!$assertionsDisabled && !zTestBit && iAttributeSize != 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && code.getHandlerCount() >= this.shortCodeHeader_h_limit) {
                throw new AssertionError();
            }
        }
        writeCodeHandlers(code);
        if (iShortCodeHeader == 0 || zTestBit) {
            writeAttrs(3, code, code.thisClass());
        }
    }

    void writeCodeHandlers(Code code) throws IOException {
        int handlerCount = code.getHandlerCount();
        for (int i2 = 0; i2 < handlerCount; i2++) {
            this.code_handler_class_RCN.putRef(code.handler_class[i2]);
            int iEncodeBCI = code.encodeBCI(code.handler_start[i2]);
            this.code_handler_start_P.putInt(iEncodeBCI);
            int iEncodeBCI2 = code.encodeBCI(code.handler_end[i2]) - iEncodeBCI;
            this.code_handler_end_PO.putInt(iEncodeBCI2);
            this.code_handler_catch_PO.putInt(code.encodeBCI(code.handler_catch[i2]) - (iEncodeBCI + iEncodeBCI2));
        }
    }

    void writeAttrs(int i2, final Attribute.Holder holder, Package.Class r15) throws IOException {
        BandStructure.MultiBand multiBand = this.attrBands[i2];
        BandStructure.IntBand attrBand = getAttrBand(multiBand, 0);
        BandStructure.IntBand attrBand2 = getAttrBand(multiBand, 1);
        boolean zHaveFlagsHi = haveFlagsHi(i2);
        if (!$assertionsDisabled) {
            if (this.attrIndexLimit[i2] != (zHaveFlagsHi ? 63 : 32)) {
                throw new AssertionError();
            }
        }
        if (holder.attributes == null) {
            attrBand2.putInt(holder.flags);
            if (zHaveFlagsHi) {
                attrBand.putInt(0);
                return;
            }
            return;
        }
        if (this.verbose > 3) {
            Utils.log.fine("Transmitting attrs for " + ((Object) holder) + " flags=" + Integer.toHexString(holder.flags));
        }
        long j2 = this.attrFlagMask[i2];
        long j3 = 0;
        int i3 = 0;
        for (Attribute attribute : holder.attributes) {
            Attribute.Layout layout = attribute.layout();
            int iIntValue = this.attrIndexTable.get(layout).intValue();
            if (!$assertionsDisabled && this.attrDefs.get(i2).get(iIntValue) != layout) {
                throw new AssertionError();
            }
            if (this.verbose > 3) {
                Utils.log.fine("add attr @" + iIntValue + " " + ((Object) attribute) + " in " + ((Object) holder));
            }
            if (iIntValue < this.attrIndexLimit[i2] && testBit(j2, 1 << iIntValue)) {
                if (this.verbose > 3) {
                    Utils.log.fine("Adding flag bit 1<<" + iIntValue + " in " + Long.toHexString(j2));
                }
                if (!$assertionsDisabled && testBit(holder.flags, 1 << iIntValue)) {
                    throw new AssertionError();
                }
                j3 |= 1 << iIntValue;
                j2 -= 1 << iIntValue;
            } else {
                j3 |= 65536;
                i3++;
                if (this.verbose > 3) {
                    Utils.log.fine("Adding overflow attr #" + i3);
                }
                getAttrBand(multiBand, 3).putInt(iIntValue);
            }
            if (layout.bandCount == 0) {
                if (layout == this.attrInnerClassesEmpty) {
                    writeLocalInnerClasses((Package.Class) holder);
                }
            } else {
                if (!$assertionsDisabled && attribute.fixups != null) {
                    throw new AssertionError();
                }
                final BandStructure.Band[] bandArr = this.attrBandTable.get(layout);
                if (!$assertionsDisabled && bandArr == null) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && bandArr.length != layout.bandCount) {
                    throw new AssertionError();
                }
                final int[] iArr = this.backCountTable.get(layout);
                if (!$assertionsDisabled && iArr == null) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && iArr.length != layout.getCallables().length) {
                    throw new AssertionError();
                }
                if (this.verbose > 2) {
                    Utils.log.fine("writing " + ((Object) attribute) + " in " + ((Object) holder));
                }
                boolean z2 = i2 == 1 && layout == this.attrConstantValue;
                if (z2) {
                    setConstantValueIndex((Package.Class.Field) holder);
                }
                attribute.parse(r15, attribute.bytes(), 0, attribute.size(), new Attribute.ValueStream() { // from class: com.sun.java.util.jar.pack.PackageWriter.3
                    static final /* synthetic */ boolean $assertionsDisabled;

                    static {
                        $assertionsDisabled = !PackageWriter.class.desiredAssertionStatus();
                    }

                    @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                    public void putInt(int i4, int i5) {
                        ((BandStructure.IntBand) bandArr[i4]).putInt(i5);
                    }

                    @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                    public void putRef(int i4, ConstantPool.Entry entry) {
                        ((BandStructure.CPRefBand) bandArr[i4]).putRef(entry);
                    }

                    @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                    public int encodeBCI(int i4) {
                        return ((Code) holder).encodeBCI(i4);
                    }

                    @Override // com.sun.java.util.jar.pack.Attribute.ValueStream
                    public void noteBackCall(int i4) {
                        if (!$assertionsDisabled && iArr[i4] < 0) {
                            throw new AssertionError();
                        }
                        int[] iArr2 = iArr;
                        iArr2[i4] = iArr2[i4] + 1;
                    }
                });
                if (z2) {
                    setConstantValueIndex(null);
                }
            }
        }
        if (i3 > 0) {
            getAttrBand(multiBand, 2).putInt(i3);
        }
        attrBand2.putInt(holder.flags | ((int) j3));
        if (zHaveFlagsHi) {
            attrBand.putInt((int) (j3 >>> 32));
        } else if (!$assertionsDisabled && (j3 >>> 32) != 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (holder.flags & j3) != 0) {
            throw new AssertionError((Object) (((Object) holder) + ".flags=" + Integer.toHexString(holder.flags) + "^" + Long.toHexString(j3)));
        }
    }

    private void beginCode(Code code) {
        if (!$assertionsDisabled && this.curCode != null) {
            throw new AssertionError();
        }
        this.curCode = code;
        this.curClass = code.f11843m.thisClass();
        this.curCPMap = code.getCPMap();
    }

    private void endCode() {
        this.curCode = null;
        this.curClass = null;
        this.curCPMap = null;
    }

    private int initOpVariant(Instruction instruction, ConstantPool.Entry entry) {
        if (instruction.getBC() != 183) {
            return -1;
        }
        ConstantPool.MemberEntry memberEntry = (ConstantPool.MemberEntry) instruction.getCPRef(this.curCPMap);
        if (!com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME.equals(memberEntry.descRef.nameRef.stringValue())) {
            return -1;
        }
        ConstantPool.ClassEntry classEntry = memberEntry.classRef;
        if (classEntry == this.curClass.thisClass) {
            return 230;
        }
        if (classEntry == this.curClass.superClass) {
            return 231;
        }
        if (classEntry == entry) {
            return JPEG.APP8;
        }
        return -1;
    }

    private int selfOpVariant(Instruction instruction) {
        int bc2 = instruction.getBC();
        if (bc2 < 178 || bc2 > 184) {
            return -1;
        }
        ConstantPool.MemberEntry memberEntry = (ConstantPool.MemberEntry) instruction.getCPRef(this.curCPMap);
        if ((bc2 == 183 || bc2 == 184) && memberEntry.tagEquals(11)) {
            return -1;
        }
        ConstantPool.ClassEntry classEntry = memberEntry.classRef;
        int i2 = 202 + (bc2 - 178);
        if (classEntry == this.curClass.thisClass) {
            return i2;
        }
        if (classEntry == this.curClass.superClass) {
            return i2 + 14;
        }
        return -1;
    }

    void writeByteCodes(Code code) throws IOException {
        BandStructure.CPRefBand cPRefBand;
        beginCode(code);
        ConstantPool.IndexGroup indexGroup = this.pkg.cp;
        boolean z2 = false;
        ConstantPool.Entry entry = null;
        Instruction instructionInstructionAt = code.instructionAt(0);
        while (true) {
            Instruction instruction = instructionInstructionAt;
            if (instruction != null) {
                if (this.verbose > 3) {
                    Utils.log.fine(instruction.toString());
                }
                if (instruction.isNonstandard()) {
                    String str = ((Object) code.getMethod()) + " contains an unrecognized bytecode " + ((Object) instruction) + "; please use the pass-file option on this class.";
                    Utils.log.warning(str);
                    throw new IOException(str);
                }
                if (instruction.isWide()) {
                    if (this.verbose > 1) {
                        Utils.log.fine("_wide opcode in " + ((Object) code));
                        Utils.log.fine(instruction.toString());
                    }
                    this.bc_codes.putByte(196);
                    int[] iArr = this.codeHist;
                    iArr[196] = iArr[196] + 1;
                }
                int bc2 = instruction.getBC();
                if (bc2 == 42 && selfOpVariant(code.instructionAt(instruction.getNextPC())) >= 0) {
                    z2 = true;
                } else {
                    int iInitOpVariant = initOpVariant(instruction, entry);
                    if (iInitOpVariant >= 0) {
                        if (z2) {
                            this.bc_codes.putByte(42);
                            int[] iArr2 = this.codeHist;
                            iArr2[42] = iArr2[42] + 1;
                            z2 = false;
                        }
                        this.bc_codes.putByte(iInitOpVariant);
                        int[] iArr3 = this.codeHist;
                        iArr3[iInitOpVariant] = iArr3[iInitOpVariant] + 1;
                        this.bc_initref.putInt(indexGroup.getOverloadingIndex((ConstantPool.MemberEntry) instruction.getCPRef(this.curCPMap)));
                    } else {
                        int iSelfOpVariant = selfOpVariant(instruction);
                        if (iSelfOpVariant >= 0) {
                            Instruction.isFieldOp(bc2);
                            boolean z3 = iSelfOpVariant >= 216;
                            boolean z4 = z2;
                            z2 = false;
                            if (z4) {
                                iSelfOpVariant += 7;
                            }
                            this.bc_codes.putByte(iSelfOpVariant);
                            int[] iArr4 = this.codeHist;
                            int i2 = iSelfOpVariant;
                            iArr4[i2] = iArr4[i2] + 1;
                            ConstantPool.MemberEntry memberEntry = (ConstantPool.MemberEntry) instruction.getCPRef(this.curCPMap);
                            selfOpRefBand(iSelfOpVariant).putRef(memberEntry, indexGroup.getMemberIndex(memberEntry.tag, memberEntry.classRef));
                        } else {
                            if (!$assertionsDisabled && z2) {
                                throw new AssertionError();
                            }
                            int[] iArr5 = this.codeHist;
                            iArr5[bc2] = iArr5[bc2] + 1;
                            switch (bc2) {
                                case 170:
                                case 171:
                                    this.bc_codes.putByte(bc2);
                                    Instruction.Switch r0 = (Instruction.Switch) instruction;
                                    r0.getAlignedPC();
                                    r0.getNextPC();
                                    int caseCount = r0.getCaseCount();
                                    this.bc_case_count.putInt(caseCount);
                                    putLabel(this.bc_label, code, instruction.getPC(), r0.getDefaultLabel());
                                    for (int i3 = 0; i3 < caseCount; i3++) {
                                        putLabel(this.bc_label, code, instruction.getPC(), r0.getCaseLabel(i3));
                                    }
                                    if (bc2 == 170) {
                                        this.bc_case_value.putInt(r0.getCaseValue(0));
                                        break;
                                    } else {
                                        for (int i4 = 0; i4 < caseCount; i4++) {
                                            this.bc_case_value.putInt(r0.getCaseValue(i4));
                                        }
                                        break;
                                    }
                                default:
                                    int branchLabel = instruction.getBranchLabel();
                                    if (branchLabel >= 0) {
                                        this.bc_codes.putByte(bc2);
                                        putLabel(this.bc_label, code, instruction.getPC(), branchLabel);
                                        break;
                                    } else {
                                        ConstantPool.Entry cPRef = instruction.getCPRef(this.curCPMap);
                                        if (cPRef != null) {
                                            if (bc2 == 187) {
                                                entry = cPRef;
                                            }
                                            if (bc2 == 18) {
                                                int[] iArr6 = this.ldcHist;
                                                byte b2 = cPRef.tag;
                                                iArr6[b2] = iArr6[b2] + 1;
                                            }
                                            int i5 = bc2;
                                            switch (instruction.getCPTag()) {
                                                case 7:
                                                    if (cPRef == this.curClass.thisClass) {
                                                        cPRef = null;
                                                    }
                                                    cPRefBand = this.bc_classref;
                                                    break;
                                                case 9:
                                                    cPRefBand = this.bc_fieldref;
                                                    break;
                                                case 10:
                                                    if (cPRef.tagEquals(11)) {
                                                        if (bc2 == 183) {
                                                            i5 = 242;
                                                        }
                                                        if (bc2 == 184) {
                                                            i5 = 243;
                                                        }
                                                        cPRefBand = this.bc_imethodref;
                                                        break;
                                                    } else {
                                                        cPRefBand = this.bc_methodref;
                                                        break;
                                                    }
                                                case 11:
                                                    cPRefBand = this.bc_imethodref;
                                                    break;
                                                case 18:
                                                    cPRefBand = this.bc_indyref;
                                                    break;
                                                case 51:
                                                    switch (cPRef.tag) {
                                                        case 3:
                                                            cPRefBand = this.bc_intref;
                                                            switch (bc2) {
                                                                case 18:
                                                                    i5 = 234;
                                                                    break;
                                                                case 19:
                                                                    i5 = 237;
                                                                    break;
                                                                default:
                                                                    if (!$assertionsDisabled) {
                                                                        throw new AssertionError();
                                                                    }
                                                                    break;
                                                            }
                                                        case 4:
                                                            cPRefBand = this.bc_floatref;
                                                            switch (bc2) {
                                                                case 18:
                                                                    i5 = 235;
                                                                    break;
                                                                case 19:
                                                                    i5 = 238;
                                                                    break;
                                                                default:
                                                                    if (!$assertionsDisabled) {
                                                                        throw new AssertionError();
                                                                    }
                                                                    break;
                                                            }
                                                        case 5:
                                                            cPRefBand = this.bc_longref;
                                                            if (!$assertionsDisabled && bc2 != 20) {
                                                                throw new AssertionError();
                                                            }
                                                            i5 = 20;
                                                            break;
                                                        case 6:
                                                            cPRefBand = this.bc_doubleref;
                                                            if (!$assertionsDisabled && bc2 != 20) {
                                                                throw new AssertionError();
                                                            }
                                                            i5 = 239;
                                                            break;
                                                            break;
                                                        case 7:
                                                            cPRefBand = this.bc_classref;
                                                            switch (bc2) {
                                                                case 18:
                                                                    i5 = 233;
                                                                    break;
                                                                case 19:
                                                                    i5 = 236;
                                                                    break;
                                                                default:
                                                                    if (!$assertionsDisabled) {
                                                                        throw new AssertionError();
                                                                    }
                                                                    break;
                                                            }
                                                        case 8:
                                                            cPRefBand = this.bc_stringref;
                                                            switch (bc2) {
                                                                case 18:
                                                                    i5 = 18;
                                                                    break;
                                                                case 19:
                                                                    i5 = 19;
                                                                    break;
                                                                default:
                                                                    if (!$assertionsDisabled) {
                                                                        throw new AssertionError();
                                                                    }
                                                                    break;
                                                            }
                                                        default:
                                                            if (getHighestClassVersion().lessThan(Constants.JAVA7_MAX_CLASS_VERSION)) {
                                                                throw new IOException("bad class file major version for Java 7 ldc");
                                                            }
                                                            cPRefBand = this.bc_loadablevalueref;
                                                            switch (bc2) {
                                                                case 18:
                                                                    i5 = 240;
                                                                    break;
                                                                case 19:
                                                                    i5 = 241;
                                                                    break;
                                                                default:
                                                                    if (!$assertionsDisabled) {
                                                                        throw new AssertionError();
                                                                    }
                                                                    break;
                                                            }
                                                    }
                                                default:
                                                    cPRefBand = null;
                                                    if (!$assertionsDisabled) {
                                                        throw new AssertionError();
                                                    }
                                                    break;
                                            }
                                            if (cPRef != null && cPRefBand.index != null && !cPRefBand.index.contains(cPRef)) {
                                                String str2 = ((Object) code.getMethod()) + " contains a bytecode " + ((Object) instruction) + " with an unsupported constant reference; please use the pass-file option on this class.";
                                                Utils.log.warning(str2);
                                                throw new IOException(str2);
                                            }
                                            this.bc_codes.putByte(i5);
                                            cPRefBand.putRef(cPRef);
                                            if (bc2 == 197) {
                                                if (!$assertionsDisabled && instruction.getConstant() != code.getByte(instruction.getPC() + 3)) {
                                                    throw new AssertionError();
                                                }
                                                this.bc_byte.putByte(255 & instruction.getConstant());
                                                break;
                                            } else if (bc2 == 185) {
                                                if (!$assertionsDisabled && instruction.getLength() != 5) {
                                                    throw new AssertionError();
                                                }
                                                if (!$assertionsDisabled && instruction.getConstant() != ((1 + ((ConstantPool.MemberEntry) cPRef).descRef.typeRef.computeSize(true)) << 8)) {
                                                    throw new AssertionError();
                                                }
                                                break;
                                            } else if (bc2 == 186) {
                                                if (getHighestClassVersion().lessThan(Constants.JAVA7_MAX_CLASS_VERSION)) {
                                                    throw new IOException("bad class major version for Java 7 invokedynamic");
                                                }
                                                if (!$assertionsDisabled && instruction.getLength() != 5) {
                                                    throw new AssertionError();
                                                }
                                                if (!$assertionsDisabled && instruction.getConstant() != 0) {
                                                    throw new AssertionError();
                                                }
                                                break;
                                            } else if ($assertionsDisabled) {
                                                continue;
                                            } else {
                                                if (instruction.getLength() != (bc2 == 18 ? 2 : 3)) {
                                                    throw new AssertionError();
                                                }
                                                break;
                                            }
                                        } else {
                                            int localSlot = instruction.getLocalSlot();
                                            if (localSlot >= 0) {
                                                this.bc_codes.putByte(bc2);
                                                this.bc_local.putInt(localSlot);
                                                int constant = instruction.getConstant();
                                                if (bc2 == 132) {
                                                    if (!instruction.isWide()) {
                                                        this.bc_byte.putByte(255 & constant);
                                                        break;
                                                    } else {
                                                        this.bc_short.putInt(65535 & constant);
                                                        break;
                                                    }
                                                } else {
                                                    if (!$assertionsDisabled && constant != 0) {
                                                        throw new AssertionError();
                                                    }
                                                    break;
                                                }
                                            } else {
                                                this.bc_codes.putByte(bc2);
                                                if (instruction.getPC() + 1 < instruction.getNextPC()) {
                                                    switch (bc2) {
                                                        case 16:
                                                            this.bc_byte.putByte(255 & instruction.getConstant());
                                                            break;
                                                        case 17:
                                                            this.bc_short.putInt(65535 & instruction.getConstant());
                                                            break;
                                                        case 188:
                                                            this.bc_byte.putByte(255 & instruction.getConstant());
                                                            break;
                                                        default:
                                                            if (!$assertionsDisabled) {
                                                                throw new AssertionError();
                                                            }
                                                            break;
                                                    }
                                                } else {
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                }
                instructionInstructionAt = instruction.next();
            } else {
                this.bc_codes.putByte(255);
                this.bc_codes.elementCountForDebug++;
                int[] iArr7 = this.codeHist;
                iArr7[255] = iArr7[255] + 1;
                endCode();
                return;
            }
        }
    }

    void printCodeHist() {
        String str;
        if (!$assertionsDisabled && this.verbose <= 0) {
            throw new AssertionError();
        }
        String[] strArr = new String[this.codeHist.length];
        int i2 = 0;
        for (int i3 = 0; i3 < this.codeHist.length; i3++) {
            i2 += this.codeHist[i3];
        }
        for (int i4 = 0; i4 < this.codeHist.length; i4++) {
            if (this.codeHist[i4] == 0) {
                strArr[i4] = "";
            } else {
                String strByteName = Instruction.byteName(i4);
                String str2 = "" + this.codeHist[i4];
                String str3 = "         ".substring(str2.length()) + str2;
                String str4 = "" + ((this.codeHist[i4] * 10000) / i2);
                while (true) {
                    str = str4;
                    if (str.length() >= 4) {
                        break;
                    } else {
                        str4 = "0" + str;
                    }
                }
                strArr[i4] = str3 + sun.security.pkcs11.wrapper.Constants.INDENT + (str.substring(0, str.length() - 2) + "." + str.substring(str.length() - 2)) + "%  " + strByteName;
            }
        }
        Arrays.sort(strArr);
        System.out.println("Bytecode histogram [" + i2 + "]");
        int length = strArr.length;
        while (true) {
            length--;
            if (length < 0) {
                break;
            } else if (!"".equals(strArr[length])) {
                System.out.println(strArr[length]);
            }
        }
        for (int i5 = 0; i5 < this.ldcHist.length; i5++) {
            int i6 = this.ldcHist[i5];
            if (i6 != 0) {
                System.out.println("ldc " + ConstantPool.tagName(i5) + " " + i6);
            }
        }
    }
}
