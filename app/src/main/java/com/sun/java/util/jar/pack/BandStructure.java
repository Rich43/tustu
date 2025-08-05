package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.Attribute;
import com.sun.java.util.jar.pack.ConstantPool;
import com.sun.java.util.jar.pack.Package;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.Pack200;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/BandStructure.class */
abstract class BandStructure {
    static final int MAX_EFFORT = 9;
    static final int MIN_EFFORT = 1;
    static final int DEFAULT_EFFORT = 5;
    PropMap p200 = Utils.currentPropMap();
    int verbose = this.p200.getInteger("com.sun.java.util.jar.pack.verbose");
    int effort;
    boolean optDumpBands;
    boolean optDebugBands;
    boolean optVaryCodings;
    boolean optBigStrings;
    private Package.Version highestClassVersion;
    private final boolean isReader;
    static final Coding BYTE1;
    static final Coding CHAR3;
    static final Coding BCI5;
    static final Coding BRANCH5;
    static final Coding UNSIGNED5;
    static final Coding UDELTA5;
    static final Coding SIGNED5;
    static final Coding DELTA5;
    static final Coding MDELTA5;
    private static final Coding[] basicCodings;
    private static final Map<Coding, Integer> basicCodingIndexes;
    protected byte[] bandHeaderBytes;
    protected int bandHeaderBytePos;
    protected int bandHeaderBytePos0;
    static final int SHORT_BAND_HEURISTIC = 100;
    public static final int NO_PHASE = 0;
    public static final int COLLECT_PHASE = 1;
    public static final int FROZEN_PHASE = 3;
    public static final int WRITE_PHASE = 5;
    public static final int EXPECT_PHASE = 2;
    public static final int READ_PHASE = 4;
    public static final int DISBURSE_PHASE = 6;
    public static final int DONE_PHASE = 8;
    private final List<CPRefBand> allKQBands;
    private List<Object[]> needPredefIndex;
    private CodingChooser codingChooser;
    static final byte[] defaultMetaCoding;
    static final byte[] noMetaCoding;
    ByteCounter outputCounter;
    protected int archiveOptions;
    protected long archiveSize0;
    protected long archiveSize1;
    protected int archiveNextCount;
    static final int AH_LENGTH_0 = 3;
    static final int AH_LENGTH_MIN = 15;
    static final int AH_LENGTH_S = 2;
    static final int AH_ARCHIVE_SIZE_HI = 0;
    static final int AH_ARCHIVE_SIZE_LO = 1;
    static final int AH_FILE_HEADER_LEN = 5;
    static final int AH_SPECIAL_FORMAT_LEN = 2;
    static final int AH_CP_NUMBER_LEN = 4;
    static final int AH_CP_EXTRA_LEN = 4;
    static final int AB_FLAGS_HI = 0;
    static final int AB_FLAGS_LO = 1;
    static final int AB_ATTR_COUNT = 2;
    static final int AB_ATTR_INDEXES = 3;
    static final int AB_ATTR_CALLS = 4;
    private static final boolean NULL_IS_OK = true;
    MultiBand all_bands;
    ByteBand archive_magic;
    IntBand archive_header_0;
    IntBand archive_header_S;
    IntBand archive_header_1;
    ByteBand band_headers;
    MultiBand cp_bands;
    IntBand cp_Utf8_prefix;
    IntBand cp_Utf8_suffix;
    IntBand cp_Utf8_chars;
    IntBand cp_Utf8_big_suffix;
    MultiBand cp_Utf8_big_chars;
    IntBand cp_Int;
    IntBand cp_Float;
    IntBand cp_Long_hi;
    IntBand cp_Long_lo;
    IntBand cp_Double_hi;
    IntBand cp_Double_lo;
    CPRefBand cp_String;
    CPRefBand cp_Class;
    CPRefBand cp_Signature_form;
    CPRefBand cp_Signature_classes;
    CPRefBand cp_Descr_name;
    CPRefBand cp_Descr_type;
    CPRefBand cp_Field_class;
    CPRefBand cp_Field_desc;
    CPRefBand cp_Method_class;
    CPRefBand cp_Method_desc;
    CPRefBand cp_Imethod_class;
    CPRefBand cp_Imethod_desc;
    IntBand cp_MethodHandle_refkind;
    CPRefBand cp_MethodHandle_member;
    CPRefBand cp_MethodType;
    CPRefBand cp_BootstrapMethod_ref;
    IntBand cp_BootstrapMethod_arg_count;
    CPRefBand cp_BootstrapMethod_arg;
    CPRefBand cp_InvokeDynamic_spec;
    CPRefBand cp_InvokeDynamic_desc;
    MultiBand attr_definition_bands;
    ByteBand attr_definition_headers;
    CPRefBand attr_definition_name;
    CPRefBand attr_definition_layout;
    MultiBand ic_bands;
    CPRefBand ic_this_class;
    IntBand ic_flags;
    CPRefBand ic_outer_class;
    CPRefBand ic_name;
    MultiBand class_bands;
    CPRefBand class_this;
    CPRefBand class_super;
    IntBand class_interface_count;
    CPRefBand class_interface;
    IntBand class_field_count;
    IntBand class_method_count;
    CPRefBand field_descr;
    MultiBand field_attr_bands;
    IntBand field_flags_hi;
    IntBand field_flags_lo;
    IntBand field_attr_count;
    IntBand field_attr_indexes;
    IntBand field_attr_calls;
    CPRefBand field_ConstantValue_KQ;
    CPRefBand field_Signature_RS;
    MultiBand field_metadata_bands;
    MultiBand field_type_metadata_bands;
    CPRefBand method_descr;
    MultiBand method_attr_bands;
    IntBand method_flags_hi;
    IntBand method_flags_lo;
    IntBand method_attr_count;
    IntBand method_attr_indexes;
    IntBand method_attr_calls;
    IntBand method_Exceptions_N;
    CPRefBand method_Exceptions_RC;
    CPRefBand method_Signature_RS;
    MultiBand method_metadata_bands;
    IntBand method_MethodParameters_NB;
    CPRefBand method_MethodParameters_name_RUN;
    IntBand method_MethodParameters_flag_FH;
    MultiBand method_type_metadata_bands;
    MultiBand class_attr_bands;
    IntBand class_flags_hi;
    IntBand class_flags_lo;
    IntBand class_attr_count;
    IntBand class_attr_indexes;
    IntBand class_attr_calls;
    CPRefBand class_SourceFile_RUN;
    CPRefBand class_EnclosingMethod_RC;
    CPRefBand class_EnclosingMethod_RDN;
    CPRefBand class_Signature_RS;
    MultiBand class_metadata_bands;
    IntBand class_InnerClasses_N;
    CPRefBand class_InnerClasses_RC;
    IntBand class_InnerClasses_F;
    CPRefBand class_InnerClasses_outer_RCN;
    CPRefBand class_InnerClasses_name_RUN;
    IntBand class_ClassFile_version_minor_H;
    IntBand class_ClassFile_version_major_H;
    MultiBand class_type_metadata_bands;
    MultiBand code_bands;
    ByteBand code_headers;
    IntBand code_max_stack;
    IntBand code_max_na_locals;
    IntBand code_handler_count;
    IntBand code_handler_start_P;
    IntBand code_handler_end_PO;
    IntBand code_handler_catch_PO;
    CPRefBand code_handler_class_RCN;
    MultiBand code_attr_bands;
    IntBand code_flags_hi;
    IntBand code_flags_lo;
    IntBand code_attr_count;
    IntBand code_attr_indexes;
    IntBand code_attr_calls;
    MultiBand stackmap_bands;
    IntBand code_StackMapTable_N;
    IntBand code_StackMapTable_frame_T;
    IntBand code_StackMapTable_local_N;
    IntBand code_StackMapTable_stack_N;
    IntBand code_StackMapTable_offset;
    IntBand code_StackMapTable_T;
    CPRefBand code_StackMapTable_RC;
    IntBand code_StackMapTable_P;
    IntBand code_LineNumberTable_N;
    IntBand code_LineNumberTable_bci_P;
    IntBand code_LineNumberTable_line;
    IntBand code_LocalVariableTable_N;
    IntBand code_LocalVariableTable_bci_P;
    IntBand code_LocalVariableTable_span_O;
    CPRefBand code_LocalVariableTable_name_RU;
    CPRefBand code_LocalVariableTable_type_RS;
    IntBand code_LocalVariableTable_slot;
    IntBand code_LocalVariableTypeTable_N;
    IntBand code_LocalVariableTypeTable_bci_P;
    IntBand code_LocalVariableTypeTable_span_O;
    CPRefBand code_LocalVariableTypeTable_name_RU;
    CPRefBand code_LocalVariableTypeTable_type_RS;
    IntBand code_LocalVariableTypeTable_slot;
    MultiBand code_type_metadata_bands;
    MultiBand bc_bands;
    ByteBand bc_codes;
    IntBand bc_case_count;
    IntBand bc_case_value;
    ByteBand bc_byte;
    IntBand bc_short;
    IntBand bc_local;
    IntBand bc_label;
    CPRefBand bc_intref;
    CPRefBand bc_floatref;
    CPRefBand bc_longref;
    CPRefBand bc_doubleref;
    CPRefBand bc_stringref;
    CPRefBand bc_loadablevalueref;
    CPRefBand bc_classref;
    CPRefBand bc_fieldref;
    CPRefBand bc_methodref;
    CPRefBand bc_imethodref;
    CPRefBand bc_indyref;
    CPRefBand bc_thisfield;
    CPRefBand bc_superfield;
    CPRefBand bc_thismethod;
    CPRefBand bc_supermethod;
    IntBand bc_initref;
    CPRefBand bc_escref;
    IntBand bc_escrefsize;
    IntBand bc_escsize;
    ByteBand bc_escbyte;
    MultiBand file_bands;
    CPRefBand file_name;
    IntBand file_size_hi;
    IntBand file_size_lo;
    IntBand file_modtime;
    IntBand file_options;
    ByteBand file_bits;
    protected MultiBand[] metadataBands;
    protected MultiBand[] typeMetadataBands;
    public static final int ADH_CONTEXT_MASK = 3;
    public static final int ADH_BIT_SHIFT = 2;
    public static final int ADH_BIT_IS_LSB = 1;
    public static final int ATTR_INDEX_OVERFLOW = -1;
    public int[] attrIndexLimit;
    protected long[] attrFlagMask;
    protected long[] attrDefSeen;
    protected int[] attrOverflowMask;
    protected int attrClassFileVersionMask;
    protected Map<Attribute.Layout, Band[]> attrBandTable;
    protected final Attribute.Layout attrCodeEmpty;
    protected final Attribute.Layout attrInnerClassesEmpty;
    protected final Attribute.Layout attrClassFileVersion;
    protected final Attribute.Layout attrConstantValue;
    Map<Attribute.Layout, Integer> attrIndexTable;
    protected List<List<Attribute.Layout>> attrDefs;
    protected MultiBand[] attrBands;
    private static final int[][] shortCodeLimits;
    public final int shortCodeHeader_h_limit;
    static final int LONG_CODE_HEADER = 0;
    static int nextSeqForDebug;
    static File dumpDir;
    private Map<Band, Band> prevForAssertMap;
    static LinkedList<String> bandSequenceList;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract ConstantPool.Index getCPIndex(byte b2);

    /* JADX WARN: Type inference failed for: r0v40, types: [int[], int[][]] */
    static {
        $assertionsDisabled = !BandStructure.class.desiredAssertionStatus();
        BYTE1 = Coding.of(1, 256);
        CHAR3 = Coding.of(3, 128);
        BCI5 = Coding.of(5, 4);
        BRANCH5 = Coding.of(5, 4, 2);
        UNSIGNED5 = Coding.of(5, 64);
        UDELTA5 = UNSIGNED5.getDeltaCoding();
        SIGNED5 = Coding.of(5, 64, 1);
        DELTA5 = SIGNED5.getDeltaCoding();
        MDELTA5 = Coding.of(5, 64, 2).getDeltaCoding();
        basicCodings = new Coding[]{null, Coding.of(1, 256, 0), Coding.of(1, 256, 1), Coding.of(1, 256, 0).getDeltaCoding(), Coding.of(1, 256, 1).getDeltaCoding(), Coding.of(2, 256, 0), Coding.of(2, 256, 1), Coding.of(2, 256, 0).getDeltaCoding(), Coding.of(2, 256, 1).getDeltaCoding(), Coding.of(3, 256, 0), Coding.of(3, 256, 1), Coding.of(3, 256, 0).getDeltaCoding(), Coding.of(3, 256, 1).getDeltaCoding(), Coding.of(4, 256, 0), Coding.of(4, 256, 1), Coding.of(4, 256, 0).getDeltaCoding(), Coding.of(4, 256, 1).getDeltaCoding(), Coding.of(5, 4, 0), Coding.of(5, 4, 1), Coding.of(5, 4, 2), Coding.of(5, 16, 0), Coding.of(5, 16, 1), Coding.of(5, 16, 2), Coding.of(5, 32, 0), Coding.of(5, 32, 1), Coding.of(5, 32, 2), Coding.of(5, 64, 0), Coding.of(5, 64, 1), Coding.of(5, 64, 2), Coding.of(5, 128, 0), Coding.of(5, 128, 1), Coding.of(5, 128, 2), Coding.of(5, 4, 0).getDeltaCoding(), Coding.of(5, 4, 1).getDeltaCoding(), Coding.of(5, 4, 2).getDeltaCoding(), Coding.of(5, 16, 0).getDeltaCoding(), Coding.of(5, 16, 1).getDeltaCoding(), Coding.of(5, 16, 2).getDeltaCoding(), Coding.of(5, 32, 0).getDeltaCoding(), Coding.of(5, 32, 1).getDeltaCoding(), Coding.of(5, 32, 2).getDeltaCoding(), Coding.of(5, 64, 0).getDeltaCoding(), Coding.of(5, 64, 1).getDeltaCoding(), Coding.of(5, 64, 2).getDeltaCoding(), Coding.of(5, 128, 0).getDeltaCoding(), Coding.of(5, 128, 1).getDeltaCoding(), Coding.of(5, 128, 2).getDeltaCoding(), Coding.of(2, 192, 0), Coding.of(2, 224, 0), Coding.of(2, 240, 0), Coding.of(2, 248, 0), Coding.of(2, 252, 0), Coding.of(2, 8, 0).getDeltaCoding(), Coding.of(2, 8, 1).getDeltaCoding(), Coding.of(2, 16, 0).getDeltaCoding(), Coding.of(2, 16, 1).getDeltaCoding(), Coding.of(2, 32, 0).getDeltaCoding(), Coding.of(2, 32, 1).getDeltaCoding(), Coding.of(2, 64, 0).getDeltaCoding(), Coding.of(2, 64, 1).getDeltaCoding(), Coding.of(2, 128, 0).getDeltaCoding(), Coding.of(2, 128, 1).getDeltaCoding(), Coding.of(2, 192, 0).getDeltaCoding(), Coding.of(2, 192, 1).getDeltaCoding(), Coding.of(2, 224, 0).getDeltaCoding(), Coding.of(2, 224, 1).getDeltaCoding(), Coding.of(2, 240, 0).getDeltaCoding(), Coding.of(2, 240, 1).getDeltaCoding(), Coding.of(2, 248, 0).getDeltaCoding(), Coding.of(2, 248, 1).getDeltaCoding(), Coding.of(3, 192, 0), Coding.of(3, 224, 0), Coding.of(3, 240, 0), Coding.of(3, 248, 0), Coding.of(3, 252, 0), Coding.of(3, 8, 0).getDeltaCoding(), Coding.of(3, 8, 1).getDeltaCoding(), Coding.of(3, 16, 0).getDeltaCoding(), Coding.of(3, 16, 1).getDeltaCoding(), Coding.of(3, 32, 0).getDeltaCoding(), Coding.of(3, 32, 1).getDeltaCoding(), Coding.of(3, 64, 0).getDeltaCoding(), Coding.of(3, 64, 1).getDeltaCoding(), Coding.of(3, 128, 0).getDeltaCoding(), Coding.of(3, 128, 1).getDeltaCoding(), Coding.of(3, 192, 0).getDeltaCoding(), Coding.of(3, 192, 1).getDeltaCoding(), Coding.of(3, 224, 0).getDeltaCoding(), Coding.of(3, 224, 1).getDeltaCoding(), Coding.of(3, 240, 0).getDeltaCoding(), Coding.of(3, 240, 1).getDeltaCoding(), Coding.of(3, 248, 0).getDeltaCoding(), Coding.of(3, 248, 1).getDeltaCoding(), Coding.of(4, 192, 0), Coding.of(4, 224, 0), Coding.of(4, 240, 0), Coding.of(4, 248, 0), Coding.of(4, 252, 0), Coding.of(4, 8, 0).getDeltaCoding(), Coding.of(4, 8, 1).getDeltaCoding(), Coding.of(4, 16, 0).getDeltaCoding(), Coding.of(4, 16, 1).getDeltaCoding(), Coding.of(4, 32, 0).getDeltaCoding(), Coding.of(4, 32, 1).getDeltaCoding(), Coding.of(4, 64, 0).getDeltaCoding(), Coding.of(4, 64, 1).getDeltaCoding(), Coding.of(4, 128, 0).getDeltaCoding(), Coding.of(4, 128, 1).getDeltaCoding(), Coding.of(4, 192, 0).getDeltaCoding(), Coding.of(4, 192, 1).getDeltaCoding(), Coding.of(4, 224, 0).getDeltaCoding(), Coding.of(4, 224, 1).getDeltaCoding(), Coding.of(4, 240, 0).getDeltaCoding(), Coding.of(4, 240, 1).getDeltaCoding(), Coding.of(4, 248, 0).getDeltaCoding(), Coding.of(4, 248, 1).getDeltaCoding(), null};
        if (!$assertionsDisabled && basicCodings[0] != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && basicCodings[1] == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && basicCodings[115] == null) {
            throw new AssertionError();
        }
        HashMap map = new HashMap();
        for (int i2 = 0; i2 < basicCodings.length; i2++) {
            Coding coding = basicCodings[i2];
            if (coding != null) {
                if (!$assertionsDisabled && i2 < 1) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && i2 > 115) {
                    throw new AssertionError();
                }
                map.put(coding, Integer.valueOf(i2));
            }
        }
        basicCodingIndexes = map;
        defaultMetaCoding = new byte[]{0};
        noMetaCoding = new byte[0];
        boolean z2 = false;
        if (!$assertionsDisabled) {
            z2 = true;
            if (1 == 0) {
                throw new AssertionError();
            }
        }
        if (z2) {
            for (int i3 = 0; i3 < basicCodings.length; i3++) {
                Coding coding2 = basicCodings[i3];
                if (coding2 != null && coding2.B() != 1 && coding2.L() != 0) {
                    for (int i4 = 0; i4 <= 255; i4++) {
                        encodeEscapeValue(i4, coding2);
                    }
                }
            }
        }
        shortCodeLimits = new int[]{new int[]{12, 12}, new int[]{8, 8}, new int[]{7, 7}};
        dumpDir = null;
        bandSequenceList = null;
    }

    public void initHighestClassVersion(Package.Version version) throws IOException {
        if (this.highestClassVersion != null) {
            throw new IOException("Highest class major version is already initialized to " + ((Object) this.highestClassVersion) + "; new setting is " + ((Object) version));
        }
        this.highestClassVersion = version;
        adjustToClassVersion();
    }

    public Package.Version getHighestClassVersion() {
        return this.highestClassVersion;
    }

    protected BandStructure() {
        this.effort = this.p200.getInteger(Pack200.Packer.EFFORT);
        if (this.effort == 0) {
            this.effort = 5;
        }
        this.optDumpBands = this.p200.getBoolean("com.sun.java.util.jar.pack.dump.bands");
        this.optDebugBands = this.p200.getBoolean("com.sun.java.util.jar.pack.debug.bands");
        this.optVaryCodings = !this.p200.getBoolean("com.sun.java.util.jar.pack.no.vary.codings");
        this.optBigStrings = !this.p200.getBoolean("com.sun.java.util.jar.pack.no.big.strings");
        this.highestClassVersion = null;
        this.isReader = this instanceof PackageReader;
        this.allKQBands = new ArrayList();
        this.needPredefIndex = new ArrayList();
        this.all_bands = (MultiBand) new MultiBand("(package)", UNSIGNED5).init();
        this.archive_magic = this.all_bands.newByteBand("archive_magic");
        this.archive_header_0 = this.all_bands.newIntBand("archive_header_0", UNSIGNED5);
        this.archive_header_S = this.all_bands.newIntBand("archive_header_S", UNSIGNED5);
        this.archive_header_1 = this.all_bands.newIntBand("archive_header_1", UNSIGNED5);
        this.band_headers = this.all_bands.newByteBand("band_headers");
        this.cp_bands = this.all_bands.newMultiBand("(constant_pool)", DELTA5);
        this.cp_Utf8_prefix = this.cp_bands.newIntBand("cp_Utf8_prefix");
        this.cp_Utf8_suffix = this.cp_bands.newIntBand("cp_Utf8_suffix", UNSIGNED5);
        this.cp_Utf8_chars = this.cp_bands.newIntBand("cp_Utf8_chars", CHAR3);
        this.cp_Utf8_big_suffix = this.cp_bands.newIntBand("cp_Utf8_big_suffix");
        this.cp_Utf8_big_chars = this.cp_bands.newMultiBand("(cp_Utf8_big_chars)", DELTA5);
        this.cp_Int = this.cp_bands.newIntBand("cp_Int", UDELTA5);
        this.cp_Float = this.cp_bands.newIntBand("cp_Float", UDELTA5);
        this.cp_Long_hi = this.cp_bands.newIntBand("cp_Long_hi", UDELTA5);
        this.cp_Long_lo = this.cp_bands.newIntBand("cp_Long_lo");
        this.cp_Double_hi = this.cp_bands.newIntBand("cp_Double_hi", UDELTA5);
        this.cp_Double_lo = this.cp_bands.newIntBand("cp_Double_lo");
        this.cp_String = this.cp_bands.newCPRefBand("cp_String", UDELTA5, (byte) 1);
        this.cp_Class = this.cp_bands.newCPRefBand("cp_Class", UDELTA5, (byte) 1);
        this.cp_Signature_form = this.cp_bands.newCPRefBand("cp_Signature_form", (byte) 1);
        this.cp_Signature_classes = this.cp_bands.newCPRefBand("cp_Signature_classes", UDELTA5, (byte) 7);
        this.cp_Descr_name = this.cp_bands.newCPRefBand("cp_Descr_name", (byte) 1);
        this.cp_Descr_type = this.cp_bands.newCPRefBand("cp_Descr_type", UDELTA5, (byte) 13);
        this.cp_Field_class = this.cp_bands.newCPRefBand("cp_Field_class", (byte) 7);
        this.cp_Field_desc = this.cp_bands.newCPRefBand("cp_Field_desc", UDELTA5, (byte) 12);
        this.cp_Method_class = this.cp_bands.newCPRefBand("cp_Method_class", (byte) 7);
        this.cp_Method_desc = this.cp_bands.newCPRefBand("cp_Method_desc", UDELTA5, (byte) 12);
        this.cp_Imethod_class = this.cp_bands.newCPRefBand("cp_Imethod_class", (byte) 7);
        this.cp_Imethod_desc = this.cp_bands.newCPRefBand("cp_Imethod_desc", UDELTA5, (byte) 12);
        this.cp_MethodHandle_refkind = this.cp_bands.newIntBand("cp_MethodHandle_refkind", DELTA5);
        this.cp_MethodHandle_member = this.cp_bands.newCPRefBand("cp_MethodHandle_member", UDELTA5, (byte) 52);
        this.cp_MethodType = this.cp_bands.newCPRefBand("cp_MethodType", UDELTA5, (byte) 13);
        this.cp_BootstrapMethod_ref = this.cp_bands.newCPRefBand("cp_BootstrapMethod_ref", DELTA5, (byte) 15);
        this.cp_BootstrapMethod_arg_count = this.cp_bands.newIntBand("cp_BootstrapMethod_arg_count", UDELTA5);
        this.cp_BootstrapMethod_arg = this.cp_bands.newCPRefBand("cp_BootstrapMethod_arg", DELTA5, (byte) 51);
        this.cp_InvokeDynamic_spec = this.cp_bands.newCPRefBand("cp_InvokeDynamic_spec", DELTA5, (byte) 17);
        this.cp_InvokeDynamic_desc = this.cp_bands.newCPRefBand("cp_InvokeDynamic_desc", UDELTA5, (byte) 12);
        this.attr_definition_bands = this.all_bands.newMultiBand("(attr_definition_bands)", UNSIGNED5);
        this.attr_definition_headers = this.attr_definition_bands.newByteBand("attr_definition_headers");
        this.attr_definition_name = this.attr_definition_bands.newCPRefBand("attr_definition_name", (byte) 1);
        this.attr_definition_layout = this.attr_definition_bands.newCPRefBand("attr_definition_layout", (byte) 1);
        this.ic_bands = this.all_bands.newMultiBand("(ic_bands)", DELTA5);
        this.ic_this_class = this.ic_bands.newCPRefBand("ic_this_class", UDELTA5, (byte) 7);
        this.ic_flags = this.ic_bands.newIntBand("ic_flags", UNSIGNED5);
        this.ic_outer_class = this.ic_bands.newCPRefBand("ic_outer_class", DELTA5, (byte) 7, true);
        this.ic_name = this.ic_bands.newCPRefBand("ic_name", DELTA5, (byte) 1, true);
        this.class_bands = this.all_bands.newMultiBand("(class_bands)", DELTA5);
        this.class_this = this.class_bands.newCPRefBand("class_this", (byte) 7);
        this.class_super = this.class_bands.newCPRefBand("class_super", (byte) 7);
        this.class_interface_count = this.class_bands.newIntBand("class_interface_count");
        this.class_interface = this.class_bands.newCPRefBand("class_interface", (byte) 7);
        this.class_field_count = this.class_bands.newIntBand("class_field_count");
        this.class_method_count = this.class_bands.newIntBand("class_method_count");
        this.field_descr = this.class_bands.newCPRefBand("field_descr", (byte) 12);
        this.field_attr_bands = this.class_bands.newMultiBand("(field_attr_bands)", UNSIGNED5);
        this.field_flags_hi = this.field_attr_bands.newIntBand("field_flags_hi");
        this.field_flags_lo = this.field_attr_bands.newIntBand("field_flags_lo");
        this.field_attr_count = this.field_attr_bands.newIntBand("field_attr_count");
        this.field_attr_indexes = this.field_attr_bands.newIntBand("field_attr_indexes");
        this.field_attr_calls = this.field_attr_bands.newIntBand("field_attr_calls");
        this.field_ConstantValue_KQ = this.field_attr_bands.newCPRefBand("field_ConstantValue_KQ", (byte) 53);
        this.field_Signature_RS = this.field_attr_bands.newCPRefBand("field_Signature_RS", (byte) 13);
        this.field_metadata_bands = this.field_attr_bands.newMultiBand("(field_metadata_bands)", UNSIGNED5);
        this.field_type_metadata_bands = this.field_attr_bands.newMultiBand("(field_type_metadata_bands)", UNSIGNED5);
        this.method_descr = this.class_bands.newCPRefBand("method_descr", MDELTA5, (byte) 12);
        this.method_attr_bands = this.class_bands.newMultiBand("(method_attr_bands)", UNSIGNED5);
        this.method_flags_hi = this.method_attr_bands.newIntBand("method_flags_hi");
        this.method_flags_lo = this.method_attr_bands.newIntBand("method_flags_lo");
        this.method_attr_count = this.method_attr_bands.newIntBand("method_attr_count");
        this.method_attr_indexes = this.method_attr_bands.newIntBand("method_attr_indexes");
        this.method_attr_calls = this.method_attr_bands.newIntBand("method_attr_calls");
        this.method_Exceptions_N = this.method_attr_bands.newIntBand("method_Exceptions_N");
        this.method_Exceptions_RC = this.method_attr_bands.newCPRefBand("method_Exceptions_RC", (byte) 7);
        this.method_Signature_RS = this.method_attr_bands.newCPRefBand("method_Signature_RS", (byte) 13);
        this.method_metadata_bands = this.method_attr_bands.newMultiBand("(method_metadata_bands)", UNSIGNED5);
        this.method_MethodParameters_NB = this.method_attr_bands.newIntBand("method_MethodParameters_NB", BYTE1);
        this.method_MethodParameters_name_RUN = this.method_attr_bands.newCPRefBand("method_MethodParameters_name_RUN", UNSIGNED5, (byte) 1, true);
        this.method_MethodParameters_flag_FH = this.method_attr_bands.newIntBand("method_MethodParameters_flag_FH");
        this.method_type_metadata_bands = this.method_attr_bands.newMultiBand("(method_type_metadata_bands)", UNSIGNED5);
        this.class_attr_bands = this.class_bands.newMultiBand("(class_attr_bands)", UNSIGNED5);
        this.class_flags_hi = this.class_attr_bands.newIntBand("class_flags_hi");
        this.class_flags_lo = this.class_attr_bands.newIntBand("class_flags_lo");
        this.class_attr_count = this.class_attr_bands.newIntBand("class_attr_count");
        this.class_attr_indexes = this.class_attr_bands.newIntBand("class_attr_indexes");
        this.class_attr_calls = this.class_attr_bands.newIntBand("class_attr_calls");
        this.class_SourceFile_RUN = this.class_attr_bands.newCPRefBand("class_SourceFile_RUN", UNSIGNED5, (byte) 1, true);
        this.class_EnclosingMethod_RC = this.class_attr_bands.newCPRefBand("class_EnclosingMethod_RC", (byte) 7);
        this.class_EnclosingMethod_RDN = this.class_attr_bands.newCPRefBand("class_EnclosingMethod_RDN", UNSIGNED5, (byte) 12, true);
        this.class_Signature_RS = this.class_attr_bands.newCPRefBand("class_Signature_RS", (byte) 13);
        this.class_metadata_bands = this.class_attr_bands.newMultiBand("(class_metadata_bands)", UNSIGNED5);
        this.class_InnerClasses_N = this.class_attr_bands.newIntBand("class_InnerClasses_N");
        this.class_InnerClasses_RC = this.class_attr_bands.newCPRefBand("class_InnerClasses_RC", (byte) 7);
        this.class_InnerClasses_F = this.class_attr_bands.newIntBand("class_InnerClasses_F");
        this.class_InnerClasses_outer_RCN = this.class_attr_bands.newCPRefBand("class_InnerClasses_outer_RCN", UNSIGNED5, (byte) 7, true);
        this.class_InnerClasses_name_RUN = this.class_attr_bands.newCPRefBand("class_InnerClasses_name_RUN", UNSIGNED5, (byte) 1, true);
        this.class_ClassFile_version_minor_H = this.class_attr_bands.newIntBand("class_ClassFile_version_minor_H");
        this.class_ClassFile_version_major_H = this.class_attr_bands.newIntBand("class_ClassFile_version_major_H");
        this.class_type_metadata_bands = this.class_attr_bands.newMultiBand("(class_type_metadata_bands)", UNSIGNED5);
        this.code_bands = this.class_bands.newMultiBand("(code_bands)", UNSIGNED5);
        this.code_headers = this.code_bands.newByteBand("code_headers");
        this.code_max_stack = this.code_bands.newIntBand("code_max_stack", UNSIGNED5);
        this.code_max_na_locals = this.code_bands.newIntBand("code_max_na_locals", UNSIGNED5);
        this.code_handler_count = this.code_bands.newIntBand("code_handler_count", UNSIGNED5);
        this.code_handler_start_P = this.code_bands.newIntBand("code_handler_start_P", BCI5);
        this.code_handler_end_PO = this.code_bands.newIntBand("code_handler_end_PO", BRANCH5);
        this.code_handler_catch_PO = this.code_bands.newIntBand("code_handler_catch_PO", BRANCH5);
        this.code_handler_class_RCN = this.code_bands.newCPRefBand("code_handler_class_RCN", UNSIGNED5, (byte) 7, true);
        this.code_attr_bands = this.class_bands.newMultiBand("(code_attr_bands)", UNSIGNED5);
        this.code_flags_hi = this.code_attr_bands.newIntBand("code_flags_hi");
        this.code_flags_lo = this.code_attr_bands.newIntBand("code_flags_lo");
        this.code_attr_count = this.code_attr_bands.newIntBand("code_attr_count");
        this.code_attr_indexes = this.code_attr_bands.newIntBand("code_attr_indexes");
        this.code_attr_calls = this.code_attr_bands.newIntBand("code_attr_calls");
        this.stackmap_bands = this.code_attr_bands.newMultiBand("(StackMapTable_bands)", UNSIGNED5);
        this.code_StackMapTable_N = this.stackmap_bands.newIntBand("code_StackMapTable_N");
        this.code_StackMapTable_frame_T = this.stackmap_bands.newIntBand("code_StackMapTable_frame_T", BYTE1);
        this.code_StackMapTable_local_N = this.stackmap_bands.newIntBand("code_StackMapTable_local_N");
        this.code_StackMapTable_stack_N = this.stackmap_bands.newIntBand("code_StackMapTable_stack_N");
        this.code_StackMapTable_offset = this.stackmap_bands.newIntBand("code_StackMapTable_offset", UNSIGNED5);
        this.code_StackMapTable_T = this.stackmap_bands.newIntBand("code_StackMapTable_T", BYTE1);
        this.code_StackMapTable_RC = this.stackmap_bands.newCPRefBand("code_StackMapTable_RC", (byte) 7);
        this.code_StackMapTable_P = this.stackmap_bands.newIntBand("code_StackMapTable_P", BCI5);
        this.code_LineNumberTable_N = this.code_attr_bands.newIntBand("code_LineNumberTable_N");
        this.code_LineNumberTable_bci_P = this.code_attr_bands.newIntBand("code_LineNumberTable_bci_P", BCI5);
        this.code_LineNumberTable_line = this.code_attr_bands.newIntBand("code_LineNumberTable_line");
        this.code_LocalVariableTable_N = this.code_attr_bands.newIntBand("code_LocalVariableTable_N");
        this.code_LocalVariableTable_bci_P = this.code_attr_bands.newIntBand("code_LocalVariableTable_bci_P", BCI5);
        this.code_LocalVariableTable_span_O = this.code_attr_bands.newIntBand("code_LocalVariableTable_span_O", BRANCH5);
        this.code_LocalVariableTable_name_RU = this.code_attr_bands.newCPRefBand("code_LocalVariableTable_name_RU", (byte) 1);
        this.code_LocalVariableTable_type_RS = this.code_attr_bands.newCPRefBand("code_LocalVariableTable_type_RS", (byte) 13);
        this.code_LocalVariableTable_slot = this.code_attr_bands.newIntBand("code_LocalVariableTable_slot");
        this.code_LocalVariableTypeTable_N = this.code_attr_bands.newIntBand("code_LocalVariableTypeTable_N");
        this.code_LocalVariableTypeTable_bci_P = this.code_attr_bands.newIntBand("code_LocalVariableTypeTable_bci_P", BCI5);
        this.code_LocalVariableTypeTable_span_O = this.code_attr_bands.newIntBand("code_LocalVariableTypeTable_span_O", BRANCH5);
        this.code_LocalVariableTypeTable_name_RU = this.code_attr_bands.newCPRefBand("code_LocalVariableTypeTable_name_RU", (byte) 1);
        this.code_LocalVariableTypeTable_type_RS = this.code_attr_bands.newCPRefBand("code_LocalVariableTypeTable_type_RS", (byte) 13);
        this.code_LocalVariableTypeTable_slot = this.code_attr_bands.newIntBand("code_LocalVariableTypeTable_slot");
        this.code_type_metadata_bands = this.code_attr_bands.newMultiBand("(code_type_metadata_bands)", UNSIGNED5);
        this.bc_bands = this.all_bands.newMultiBand("(byte_codes)", UNSIGNED5);
        this.bc_codes = this.bc_bands.newByteBand("bc_codes");
        this.bc_case_count = this.bc_bands.newIntBand("bc_case_count");
        this.bc_case_value = this.bc_bands.newIntBand("bc_case_value", DELTA5);
        this.bc_byte = this.bc_bands.newByteBand("bc_byte");
        this.bc_short = this.bc_bands.newIntBand("bc_short", DELTA5);
        this.bc_local = this.bc_bands.newIntBand("bc_local");
        this.bc_label = this.bc_bands.newIntBand("bc_label", BRANCH5);
        this.bc_intref = this.bc_bands.newCPRefBand("bc_intref", DELTA5, (byte) 3);
        this.bc_floatref = this.bc_bands.newCPRefBand("bc_floatref", DELTA5, (byte) 4);
        this.bc_longref = this.bc_bands.newCPRefBand("bc_longref", DELTA5, (byte) 5);
        this.bc_doubleref = this.bc_bands.newCPRefBand("bc_doubleref", DELTA5, (byte) 6);
        this.bc_stringref = this.bc_bands.newCPRefBand("bc_stringref", DELTA5, (byte) 8);
        this.bc_loadablevalueref = this.bc_bands.newCPRefBand("bc_loadablevalueref", DELTA5, (byte) 51);
        this.bc_classref = this.bc_bands.newCPRefBand("bc_classref", UNSIGNED5, (byte) 7, true);
        this.bc_fieldref = this.bc_bands.newCPRefBand("bc_fieldref", DELTA5, (byte) 9);
        this.bc_methodref = this.bc_bands.newCPRefBand("bc_methodref", (byte) 10);
        this.bc_imethodref = this.bc_bands.newCPRefBand("bc_imethodref", DELTA5, (byte) 11);
        this.bc_indyref = this.bc_bands.newCPRefBand("bc_indyref", DELTA5, (byte) 18);
        this.bc_thisfield = this.bc_bands.newCPRefBand("bc_thisfield", (byte) 0);
        this.bc_superfield = this.bc_bands.newCPRefBand("bc_superfield", (byte) 0);
        this.bc_thismethod = this.bc_bands.newCPRefBand("bc_thismethod", (byte) 0);
        this.bc_supermethod = this.bc_bands.newCPRefBand("bc_supermethod", (byte) 0);
        this.bc_initref = this.bc_bands.newIntBand("bc_initref");
        this.bc_escref = this.bc_bands.newCPRefBand("bc_escref", (byte) 50);
        this.bc_escrefsize = this.bc_bands.newIntBand("bc_escrefsize");
        this.bc_escsize = this.bc_bands.newIntBand("bc_escsize");
        this.bc_escbyte = this.bc_bands.newByteBand("bc_escbyte");
        this.file_bands = this.all_bands.newMultiBand("(file_bands)", UNSIGNED5);
        this.file_name = this.file_bands.newCPRefBand("file_name", (byte) 1);
        this.file_size_hi = this.file_bands.newIntBand("file_size_hi");
        this.file_size_lo = this.file_bands.newIntBand("file_size_lo");
        this.file_modtime = this.file_bands.newIntBand("file_modtime", DELTA5);
        this.file_options = this.file_bands.newIntBand("file_options");
        this.file_bits = this.file_bands.newByteBand("file_bits");
        this.metadataBands = new MultiBand[4];
        this.metadataBands[0] = this.class_metadata_bands;
        this.metadataBands[1] = this.field_metadata_bands;
        this.metadataBands[2] = this.method_metadata_bands;
        this.typeMetadataBands = new MultiBand[4];
        this.typeMetadataBands[0] = this.class_type_metadata_bands;
        this.typeMetadataBands[1] = this.field_type_metadata_bands;
        this.typeMetadataBands[2] = this.method_type_metadata_bands;
        this.typeMetadataBands[3] = this.code_type_metadata_bands;
        this.attrIndexLimit = new int[4];
        this.attrFlagMask = new long[4];
        this.attrDefSeen = new long[4];
        this.attrOverflowMask = new int[4];
        this.attrBandTable = new HashMap();
        this.attrIndexTable = new HashMap();
        this.attrDefs = new FixedList(4);
        for (int i2 = 0; i2 < 4; i2++) {
            if (!$assertionsDisabled && this.attrIndexLimit[i2] != 0) {
                throw new AssertionError();
            }
            this.attrIndexLimit[i2] = 32;
            this.attrDefs.set(i2, new ArrayList(Collections.nCopies(this.attrIndexLimit[i2], (Attribute.Layout) null)));
        }
        this.attrInnerClassesEmpty = predefineAttribute(23, 0, null, "InnerClasses", "");
        if (!$assertionsDisabled && this.attrInnerClassesEmpty != Package.attrInnerClassesEmpty) {
            throw new AssertionError();
        }
        predefineAttribute(17, 0, new Band[]{this.class_SourceFile_RUN}, "SourceFile", "RUNH");
        predefineAttribute(18, 0, new Band[]{this.class_EnclosingMethod_RC, this.class_EnclosingMethod_RDN}, "EnclosingMethod", "RCHRDNH");
        this.attrClassFileVersion = predefineAttribute(24, 0, new Band[]{this.class_ClassFile_version_minor_H, this.class_ClassFile_version_major_H}, ".ClassFile.version", "HH");
        predefineAttribute(19, 0, new Band[]{this.class_Signature_RS}, com.sun.org.apache.xml.internal.security.utils.Constants._TAG_SIGNATURE, "RSH");
        predefineAttribute(20, 0, null, "Deprecated", "");
        predefineAttribute(16, 0, null, ".Overflow", "");
        this.attrConstantValue = predefineAttribute(17, 1, new Band[]{this.field_ConstantValue_KQ}, "ConstantValue", "KQH");
        predefineAttribute(19, 1, new Band[]{this.field_Signature_RS}, com.sun.org.apache.xml.internal.security.utils.Constants._TAG_SIGNATURE, "RSH");
        predefineAttribute(20, 1, null, "Deprecated", "");
        predefineAttribute(16, 1, null, ".Overflow", "");
        this.attrCodeEmpty = predefineAttribute(17, 2, null, "Code", "");
        predefineAttribute(18, 2, new Band[]{this.method_Exceptions_N, this.method_Exceptions_RC}, "Exceptions", "NH[RCH]");
        predefineAttribute(26, 2, new Band[]{this.method_MethodParameters_NB, this.method_MethodParameters_name_RUN, this.method_MethodParameters_flag_FH}, "MethodParameters", "NB[RUNHFH]");
        if (!$assertionsDisabled && this.attrCodeEmpty != Package.attrCodeEmpty) {
            throw new AssertionError();
        }
        predefineAttribute(19, 2, new Band[]{this.method_Signature_RS}, com.sun.org.apache.xml.internal.security.utils.Constants._TAG_SIGNATURE, "RSH");
        predefineAttribute(20, 2, null, "Deprecated", "");
        predefineAttribute(16, 2, null, ".Overflow", "");
        for (int i3 = 0; i3 < 4; i3++) {
            MultiBand multiBand = this.metadataBands[i3];
            if (i3 != 3) {
                predefineAttribute(21, Constants.ATTR_CONTEXT_NAME[i3] + "_RVA_", multiBand, Attribute.lookup(null, i3, "RuntimeVisibleAnnotations"));
                predefineAttribute(22, Constants.ATTR_CONTEXT_NAME[i3] + "_RIA_", multiBand, Attribute.lookup(null, i3, "RuntimeInvisibleAnnotations"));
                if (i3 == 2) {
                    predefineAttribute(23, "method_RVPA_", multiBand, Attribute.lookup(null, i3, "RuntimeVisibleParameterAnnotations"));
                    predefineAttribute(24, "method_RIPA_", multiBand, Attribute.lookup(null, i3, "RuntimeInvisibleParameterAnnotations"));
                    predefineAttribute(25, "method_AD_", multiBand, Attribute.lookup(null, i3, "AnnotationDefault"));
                }
            }
            MultiBand multiBand2 = this.typeMetadataBands[i3];
            predefineAttribute(27, Constants.ATTR_CONTEXT_NAME[i3] + "_RVTA_", multiBand2, Attribute.lookup(null, i3, "RuntimeVisibleTypeAnnotations"));
            predefineAttribute(28, Constants.ATTR_CONTEXT_NAME[i3] + "_RITA_", multiBand2, Attribute.lookup(null, i3, "RuntimeInvisibleTypeAnnotations"));
        }
        Attribute.Layout layout = Attribute.lookup(null, 3, "StackMapTable").layout();
        predefineAttribute(0, 3, this.stackmap_bands.toArray(), layout.name(), layout.layout());
        predefineAttribute(1, 3, new Band[]{this.code_LineNumberTable_N, this.code_LineNumberTable_bci_P, this.code_LineNumberTable_line}, "LineNumberTable", "NH[PHH]");
        predefineAttribute(2, 3, new Band[]{this.code_LocalVariableTable_N, this.code_LocalVariableTable_bci_P, this.code_LocalVariableTable_span_O, this.code_LocalVariableTable_name_RU, this.code_LocalVariableTable_type_RS, this.code_LocalVariableTable_slot}, "LocalVariableTable", "NH[PHOHRUHRSHH]");
        predefineAttribute(3, 3, new Band[]{this.code_LocalVariableTypeTable_N, this.code_LocalVariableTypeTable_bci_P, this.code_LocalVariableTypeTable_span_O, this.code_LocalVariableTypeTable_name_RU, this.code_LocalVariableTypeTable_type_RS, this.code_LocalVariableTypeTable_slot}, "LocalVariableTypeTable", "NH[PHOHRUHRSHH]");
        predefineAttribute(16, 3, null, ".Overflow", "");
        for (int i4 = 0; i4 < 4; i4++) {
            this.attrDefSeen[i4] = 0;
        }
        for (int i5 = 0; i5 < 4; i5++) {
            this.attrOverflowMask[i5] = 65536;
            this.attrIndexLimit[i5] = 0;
        }
        this.attrClassFileVersionMask = 16777216;
        this.attrBands = new MultiBand[4];
        this.attrBands[0] = this.class_attr_bands;
        this.attrBands[1] = this.field_attr_bands;
        this.attrBands[2] = this.method_attr_bands;
        this.attrBands[3] = this.code_attr_bands;
        this.shortCodeHeader_h_limit = shortCodeLimits.length;
    }

    public static Coding codingForIndex(int i2) {
        if (i2 < basicCodings.length) {
            return basicCodings[i2];
        }
        return null;
    }

    public static int indexOf(Coding coding) {
        Integer num = basicCodingIndexes.get(coding);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public static Coding[] getBasicCodings() {
        return (Coding[]) basicCodings.clone();
    }

    protected CodingMethod getBandHeader(int i2, Coding coding) {
        CodingMethod[] codingMethodArr = {null};
        byte[] bArr = this.bandHeaderBytes;
        int i3 = this.bandHeaderBytePos - 1;
        this.bandHeaderBytePos = i3;
        bArr[i3] = (byte) i2;
        this.bandHeaderBytePos0 = this.bandHeaderBytePos;
        this.bandHeaderBytePos = parseMetaCoding(this.bandHeaderBytes, this.bandHeaderBytePos, coding, codingMethodArr);
        return codingMethodArr[0];
    }

    public static int parseMetaCoding(byte[] bArr, int i2, Coding coding, CodingMethod[] codingMethodArr) {
        if ((bArr[i2] & 255) == 0) {
            codingMethodArr[0] = coding;
            return i2 + 1;
        }
        int metaCoding = Coding.parseMetaCoding(bArr, i2, coding, codingMethodArr);
        if (metaCoding > i2) {
            return metaCoding;
        }
        int metaCoding2 = PopulationCoding.parseMetaCoding(bArr, i2, coding, codingMethodArr);
        if (metaCoding2 > i2) {
            return metaCoding2;
        }
        int metaCoding3 = AdaptiveCoding.parseMetaCoding(bArr, i2, coding, codingMethodArr);
        if (metaCoding3 > i2) {
            return metaCoding3;
        }
        throw new RuntimeException("Bad meta-coding op " + (bArr[i2] & 255));
    }

    static boolean phaseIsRead(int i2) {
        return i2 % 2 == 0;
    }

    static int phaseCmp(int i2, int i3) {
        if ($assertionsDisabled || i2 % 2 == i3 % 2 || i2 % 8 == 0 || i3 % 8 == 0) {
            return i2 - i3;
        }
        throw new AssertionError();
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/BandStructure$Band.class */
    abstract class Band {
        private final String name;
        private int valuesExpected;
        public final Coding regularCoding;
        public final int seqForDebug;
        public int elementCountForDebug;
        static final /* synthetic */ boolean $assertionsDisabled;
        private int phase = 0;
        protected long outputSize = -1;
        protected int lengthForDebug = -1;

        public abstract int capacity();

        protected abstract void setCapacity(int i2);

        public abstract int length();

        protected abstract int valuesRemainingForDebug();

        abstract void chooseBandCodings() throws IOException;

        protected abstract long computeOutputSize();

        protected abstract void writeDataTo(OutputStream outputStream) throws IOException;

        protected abstract void readDataFrom(InputStream inputStream) throws IOException;

        static {
            $assertionsDisabled = !BandStructure.class.desiredAssertionStatus();
        }

        protected Band(String str, Coding coding) {
            this.name = str;
            this.regularCoding = coding;
            int i2 = BandStructure.nextSeqForDebug + 1;
            BandStructure.nextSeqForDebug = i2;
            this.seqForDebug = i2;
            if (BandStructure.this.verbose > 2) {
                Utils.log.fine("Band " + this.seqForDebug + " is " + str);
            }
        }

        public Band init() {
            if (BandStructure.this.isReader) {
                readyToExpect();
            } else {
                readyToCollect();
            }
            return this;
        }

        boolean isReader() {
            return BandStructure.this.isReader;
        }

        int phase() {
            return this.phase;
        }

        String name() {
            return this.name;
        }

        public final int valuesExpected() {
            return this.valuesExpected;
        }

        public final void writeTo(OutputStream outputStream) throws IOException {
            if (!$assertionsDisabled && !BandStructure.this.assertReadyToWriteTo(this, outputStream)) {
                throw new AssertionError();
            }
            setPhase(5);
            writeDataTo(outputStream);
            doneWriting();
        }

        public final long outputSize() {
            if (this.outputSize >= 0) {
                long j2 = this.outputSize;
                if ($assertionsDisabled || j2 == computeOutputSize()) {
                    return j2;
                }
                throw new AssertionError();
            }
            return computeOutputSize();
        }

        void expectLength(int i2) {
            if (!$assertionsDisabled && !BandStructure.assertPhase(this, 2)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.valuesExpected != 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && i2 < 0) {
                throw new AssertionError();
            }
            this.valuesExpected = i2;
        }

        void expectMoreLength(int i2) {
            if (!$assertionsDisabled && !BandStructure.assertPhase(this, 2)) {
                throw new AssertionError();
            }
            this.valuesExpected += i2;
        }

        private void readyToCollect() {
            setCapacity(1);
            setPhase(1);
        }

        protected void doneWriting() {
            if (!$assertionsDisabled && !BandStructure.assertPhase(this, 5)) {
                throw new AssertionError();
            }
            setPhase(8);
        }

        private void readyToExpect() {
            setPhase(2);
        }

        public final void readFrom(InputStream inputStream) throws IOException {
            if (!$assertionsDisabled && !BandStructure.this.assertReadyToReadFrom(this, inputStream)) {
                throw new AssertionError();
            }
            setCapacity(valuesExpected());
            setPhase(4);
            readDataFrom(inputStream);
            readyToDisburse();
        }

        protected void readyToDisburse() {
            if (BandStructure.this.verbose > 1) {
                Utils.log.fine("readyToDisburse " + ((Object) this));
            }
            setPhase(6);
        }

        public void doneDisbursing() {
            if (!$assertionsDisabled && !BandStructure.assertPhase(this, 6)) {
                throw new AssertionError();
            }
            setPhase(8);
        }

        public final void doneWithUnusedBand() {
            if (BandStructure.this.isReader) {
                if (!$assertionsDisabled && !BandStructure.assertPhase(this, 2)) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && valuesExpected() != 0) {
                    throw new AssertionError();
                }
                setPhase(4);
                setPhase(6);
                setPhase(8);
                return;
            }
            setPhase(3);
        }

        protected void setPhase(int i2) {
            if (!$assertionsDisabled && !BandStructure.assertPhaseChangeOK(this, this.phase, i2)) {
                throw new AssertionError();
            }
            this.phase = i2;
        }

        public String toString() {
            int length = this.lengthForDebug != -1 ? this.lengthForDebug : length();
            String str = this.name;
            if (length != 0) {
                str = str + "[" + length + "]";
            }
            if (this.elementCountForDebug != 0) {
                str = str + "(" + this.elementCountForDebug + ")";
            }
            return str;
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/BandStructure$ValueBand.class */
    class ValueBand extends Band {
        private int[] values;
        private int length;
        private int valuesDisbursed;
        private CodingMethod bandCoding;
        private byte[] metaCoding;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BandStructure.class.desiredAssertionStatus();
        }

        protected ValueBand(String str, Coding coding) {
            super(str, coding);
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public int capacity() {
            if (this.values == null) {
                return -1;
            }
            return this.values.length;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected void setCapacity(int i2) {
            if (!$assertionsDisabled && this.length > i2) {
                throw new AssertionError();
            }
            if (i2 == -1) {
                this.values = null;
            } else {
                this.values = BandStructure.realloc(this.values, i2);
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public int length() {
            return this.length;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected int valuesRemainingForDebug() {
            return this.length - this.valuesDisbursed;
        }

        protected int valueAtForDebug(int i2) {
            return this.values[i2];
        }

        void patchValue(int i2, int i3) {
            if (!$assertionsDisabled && this != BandStructure.this.archive_header_S) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && i2 != 0 && i2 != 1) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && i2 >= this.length) {
                throw new AssertionError();
            }
            this.values[i2] = i3;
            this.outputSize = -1L;
        }

        protected void initializeValues(int[] iArr) {
            if (!$assertionsDisabled && !BandStructure.assertCanChangeLength(this)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.length != 0) {
                throw new AssertionError();
            }
            this.values = iArr;
            this.length = iArr.length;
        }

        protected void addValue(int i2) {
            if (!$assertionsDisabled && !BandStructure.assertCanChangeLength(this)) {
                throw new AssertionError();
            }
            if (this.length == this.values.length) {
                setCapacity(this.length < 1000 ? this.length * 10 : this.length * 2);
            }
            int[] iArr = this.values;
            int i3 = this.length;
            this.length = i3 + 1;
            iArr[i3] = i2;
        }

        private boolean canVaryCoding() {
            if (!BandStructure.this.optVaryCodings || this.length == 0 || this == BandStructure.this.archive_header_0 || this == BandStructure.this.archive_header_S || this == BandStructure.this.archive_header_1) {
                return false;
            }
            return this.regularCoding.min() <= -256 || this.regularCoding.max() >= 256;
        }

        private boolean shouldVaryCoding() {
            if (!$assertionsDisabled && !canVaryCoding()) {
                throw new AssertionError();
            }
            if (BandStructure.this.effort < 9 && this.length < 100) {
                return false;
            }
            return true;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected void chooseBandCodings() throws IOException {
            boolean zCanVaryCoding = canVaryCoding();
            if (!zCanVaryCoding || !shouldVaryCoding()) {
                if (this.regularCoding.canRepresent(this.values, 0, this.length)) {
                    this.bandCoding = this.regularCoding;
                } else {
                    if (!$assertionsDisabled && !zCanVaryCoding) {
                        throw new AssertionError();
                    }
                    if (BandStructure.this.verbose > 1) {
                        Utils.log.fine("regular coding fails in band " + name());
                    }
                    this.bandCoding = BandStructure.UNSIGNED5;
                }
                this.outputSize = -1L;
            } else {
                this.bandCoding = BandStructure.this.chooseCoding(this.values, 0, this.length, this.regularCoding, name(), new int[]{0, 0});
                this.outputSize = r0[0];
                if (this.outputSize == 0) {
                    this.outputSize = -1L;
                }
            }
            if (this.bandCoding != this.regularCoding) {
                this.metaCoding = this.bandCoding.getMetaCoding(this.regularCoding);
                if (BandStructure.this.verbose > 1) {
                    Utils.log.fine("alternate coding " + ((Object) this) + " " + ((Object) this.bandCoding));
                }
            } else if (zCanVaryCoding && BandStructure.decodeEscapeValue(this.values[0], this.regularCoding) >= 0) {
                this.metaCoding = BandStructure.defaultMetaCoding;
            } else {
                this.metaCoding = BandStructure.noMetaCoding;
            }
            if (this.metaCoding.length > 0 && (BandStructure.this.verbose > 2 || (BandStructure.this.verbose > 1 && this.metaCoding.length > 1))) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i2 = 0; i2 < this.metaCoding.length; i2++) {
                    if (i2 == 1) {
                        stringBuffer.append(" /");
                    }
                    stringBuffer.append(" ").append(this.metaCoding[i2] & 255);
                }
                Utils.log.fine("   meta-coding " + ((Object) stringBuffer));
            }
            if (!$assertionsDisabled && this.outputSize >= 0 && (this.bandCoding instanceof Coding) && this.outputSize != ((Coding) this.bandCoding).getLength(this.values, 0, this.length)) {
                throw new AssertionError((Object) (((Object) this.bandCoding) + " : " + this.outputSize + " != " + ((Coding) this.bandCoding).getLength(this.values, 0, this.length) + " ?= " + BandStructure.this.getCodingChooser().computeByteSize(this.bandCoding, this.values, 0, this.length)));
            }
            if (this.metaCoding.length > 0) {
                if (this.outputSize >= 0) {
                    this.outputSize += computeEscapeSize();
                }
                for (int i3 = 1; i3 < this.metaCoding.length; i3++) {
                    BandStructure.this.band_headers.putByte(this.metaCoding[i3] & 255);
                }
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected long computeOutputSize() {
            this.outputSize = BandStructure.this.getCodingChooser().computeByteSize(this.bandCoding, this.values, 0, this.length);
            if (!$assertionsDisabled && this.outputSize >= 2147483647L) {
                throw new AssertionError();
            }
            this.outputSize += computeEscapeSize();
            return this.outputSize;
        }

        protected int computeEscapeSize() {
            if (this.metaCoding.length == 0) {
                return 0;
            }
            return this.regularCoding.setD(0).getLength(BandStructure.encodeEscapeValue(this.metaCoding[0] & 255, this.regularCoding));
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected void writeDataTo(OutputStream outputStream) throws IOException {
            if (this.length == 0) {
                return;
            }
            long count = 0;
            if (outputStream == BandStructure.this.outputCounter) {
                count = BandStructure.this.outputCounter.getCount();
            }
            if (this.metaCoding.length > 0) {
                this.regularCoding.setD(0).writeTo(outputStream, BandStructure.encodeEscapeValue(this.metaCoding[0] & 255, this.regularCoding));
            }
            this.bandCoding.writeArrayTo(outputStream, this.values, 0, this.length);
            if (outputStream == BandStructure.this.outputCounter && !$assertionsDisabled && this.outputSize != BandStructure.this.outputCounter.getCount() - count) {
                throw new AssertionError((Object) (this.outputSize + " != " + BandStructure.this.outputCounter.getCount() + LanguageTag.SEP + count));
            }
            if (BandStructure.this.optDumpBands) {
                dumpBand();
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected void readDataFrom(InputStream inputStream) throws IOException {
            this.length = valuesExpected();
            if (this.length == 0) {
                return;
            }
            if (BandStructure.this.verbose > 1) {
                Utils.log.fine("Reading band " + ((Object) this));
            }
            if (!canVaryCoding()) {
                this.bandCoding = this.regularCoding;
                this.metaCoding = BandStructure.noMetaCoding;
            } else {
                if (!$assertionsDisabled && !inputStream.markSupported()) {
                    throw new AssertionError();
                }
                inputStream.mark(5);
                int from = this.regularCoding.setD(0).readFrom(inputStream);
                int iDecodeEscapeValue = BandStructure.decodeEscapeValue(from, this.regularCoding);
                if (iDecodeEscapeValue < 0) {
                    inputStream.reset();
                    this.bandCoding = this.regularCoding;
                    this.metaCoding = BandStructure.noMetaCoding;
                } else if (iDecodeEscapeValue == 0) {
                    this.bandCoding = this.regularCoding;
                    this.metaCoding = BandStructure.defaultMetaCoding;
                } else {
                    if (BandStructure.this.verbose > 2) {
                        Utils.log.fine("found X=" + from + " => XB=" + iDecodeEscapeValue);
                    }
                    this.bandCoding = BandStructure.this.getBandHeader(iDecodeEscapeValue, this.regularCoding);
                    int i2 = BandStructure.this.bandHeaderBytePos0;
                    this.metaCoding = new byte[BandStructure.this.bandHeaderBytePos - i2];
                    System.arraycopy(BandStructure.this.bandHeaderBytes, i2, this.metaCoding, 0, this.metaCoding.length);
                }
            }
            if (this.bandCoding != this.regularCoding && BandStructure.this.verbose > 1) {
                Utils.log.fine(name() + ": irregular coding " + ((Object) this.bandCoding));
            }
            this.bandCoding.readArrayFrom(inputStream, this.values, 0, this.length);
            if (BandStructure.this.optDumpBands) {
                dumpBand();
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public void doneDisbursing() {
            super.doneDisbursing();
            this.values = null;
        }

        private void dumpBand() throws IOException {
            if (!$assertionsDisabled && !BandStructure.this.optDumpBands) {
                throw new AssertionError();
            }
            PrintStream printStream = new PrintStream(BandStructure.getDumpStream(this, ".txt"));
            Throwable th = null;
            try {
                printStream.print("# length=" + this.length + " size=" + outputSize() + (this.bandCoding == this.regularCoding ? "" : " irregular") + " coding=" + ((Object) this.bandCoding));
                if (this.metaCoding != BandStructure.noMetaCoding) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i2 = 0; i2 < this.metaCoding.length; i2++) {
                        if (i2 == 1) {
                            stringBuffer.append(" /");
                        }
                        stringBuffer.append(" ").append(this.metaCoding[i2] & 255);
                    }
                    printStream.print(" //header: " + ((Object) stringBuffer));
                }
                BandStructure.printArrayTo(printStream, this.values, 0, this.length);
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
                OutputStream dumpStream = BandStructure.getDumpStream(this, ".bnd");
                Throwable th3 = null;
                try {
                    this.bandCoding.writeArrayTo(dumpStream, this.values, 0, this.length);
                    if (dumpStream != null) {
                        if (0 != 0) {
                            try {
                                dumpStream.close();
                                return;
                            } catch (Throwable th4) {
                                th3.addSuppressed(th4);
                                return;
                            }
                        }
                        dumpStream.close();
                    }
                } catch (Throwable th5) {
                    if (dumpStream != null) {
                        if (0 != 0) {
                            try {
                                dumpStream.close();
                            } catch (Throwable th6) {
                                th3.addSuppressed(th6);
                            }
                        } else {
                            dumpStream.close();
                        }
                    }
                    throw th5;
                }
            } catch (Throwable th7) {
                if (printStream != null) {
                    if (0 != 0) {
                        try {
                            printStream.close();
                        } catch (Throwable th8) {
                            th.addSuppressed(th8);
                        }
                    } else {
                        printStream.close();
                    }
                }
                throw th7;
            }
        }

        protected int getValue() {
            if (!$assertionsDisabled && phase() != 6) {
                throw new AssertionError();
            }
            if (BandStructure.this.optDebugBands && this.length == 0 && this.valuesDisbursed == this.length) {
                return 0;
            }
            if (!$assertionsDisabled && this.valuesDisbursed > this.length) {
                throw new AssertionError();
            }
            int[] iArr = this.values;
            int i2 = this.valuesDisbursed;
            this.valuesDisbursed = i2 + 1;
            return iArr[i2];
        }

        public void resetForSecondPass() {
            if (!$assertionsDisabled && phase() != 6) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.valuesDisbursed != length()) {
                throw new AssertionError();
            }
            this.valuesDisbursed = 0;
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/BandStructure$ByteBand.class */
    class ByteBand extends Band {
        private ByteArrayOutputStream bytes;
        private ByteArrayOutputStream bytesForDump;
        private InputStream in;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BandStructure.class.desiredAssertionStatus();
        }

        public ByteBand(String str) {
            super(str, BandStructure.BYTE1);
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public int capacity() {
            return this.bytes == null ? -1 : Integer.MAX_VALUE;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected void setCapacity(int i2) {
            if (!$assertionsDisabled && this.bytes != null) {
                throw new AssertionError();
            }
            this.bytes = new ByteArrayOutputStream(i2);
        }

        public void destroy() {
            this.lengthForDebug = length();
            this.bytes = null;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public int length() {
            if (this.bytes == null) {
                return -1;
            }
            return this.bytes.size();
        }

        public void reset() {
            this.bytes.reset();
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected int valuesRemainingForDebug() {
            if (this.bytes == null) {
                return -1;
            }
            return ((ByteArrayInputStream) this.in).available();
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected void chooseBandCodings() throws IOException {
            if (!$assertionsDisabled && BandStructure.decodeEscapeValue(this.regularCoding.min(), this.regularCoding) >= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && BandStructure.decodeEscapeValue(this.regularCoding.max(), this.regularCoding) >= 0) {
                throw new AssertionError();
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected long computeOutputSize() {
            return this.bytes.size();
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public void writeDataTo(OutputStream outputStream) throws IOException {
            if (length() == 0) {
                return;
            }
            this.bytes.writeTo(outputStream);
            if (BandStructure.this.optDumpBands) {
                dumpBand();
            }
            destroy();
        }

        private void dumpBand() throws IOException {
            if (!$assertionsDisabled && !BandStructure.this.optDumpBands) {
                throw new AssertionError();
            }
            OutputStream dumpStream = BandStructure.getDumpStream(this, ".bnd");
            Throwable th = null;
            try {
                if (this.bytesForDump != null) {
                    this.bytesForDump.writeTo(dumpStream);
                } else {
                    this.bytes.writeTo(dumpStream);
                }
                if (dumpStream != null) {
                    if (0 != 0) {
                        try {
                            dumpStream.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    dumpStream.close();
                }
            } catch (Throwable th3) {
                if (dumpStream != null) {
                    if (0 != 0) {
                        try {
                            dumpStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        dumpStream.close();
                    }
                }
                throw th3;
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public void readDataFrom(InputStream inputStream) throws IOException {
            int iValuesExpected = valuesExpected();
            if (iValuesExpected == 0) {
                return;
            }
            if (BandStructure.this.verbose > 1) {
                this.lengthForDebug = iValuesExpected;
                Utils.log.fine("Reading band " + ((Object) this));
                this.lengthForDebug = -1;
            }
            byte[] bArr = new byte[Math.min(iValuesExpected, 16384)];
            while (iValuesExpected > 0) {
                int i2 = inputStream.read(bArr, 0, Math.min(iValuesExpected, bArr.length));
                if (i2 < 0) {
                    throw new EOFException();
                }
                this.bytes.write(bArr, 0, i2);
                iValuesExpected -= i2;
            }
            if (BandStructure.this.optDumpBands) {
                dumpBand();
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public void readyToDisburse() {
            this.in = new ByteArrayInputStream(this.bytes.toByteArray());
            super.readyToDisburse();
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public void doneDisbursing() {
            super.doneDisbursing();
            if (BandStructure.this.optDumpBands && this.bytesForDump != null && this.bytesForDump.size() > 0) {
                try {
                    dumpBand();
                } catch (IOException e2) {
                    throw new RuntimeException(e2);
                }
            }
            this.in = null;
            this.bytes = null;
            this.bytesForDump = null;
        }

        public void setInputStreamFrom(InputStream inputStream) throws IOException {
            if (!$assertionsDisabled && this.bytes != null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !BandStructure.this.assertReadyToReadFrom(this, inputStream)) {
                throw new AssertionError();
            }
            setPhase(4);
            this.in = inputStream;
            if (BandStructure.this.optDumpBands) {
                this.bytesForDump = new ByteArrayOutputStream();
                this.in = new FilterInputStream(inputStream) { // from class: com.sun.java.util.jar.pack.BandStructure.ByteBand.1
                    @Override // java.io.FilterInputStream, java.io.InputStream
                    public int read() throws IOException {
                        int i2 = this.in.read();
                        if (i2 >= 0) {
                            ByteBand.this.bytesForDump.write(i2);
                        }
                        return i2;
                    }

                    @Override // java.io.FilterInputStream, java.io.InputStream
                    public int read(byte[] bArr, int i2, int i3) throws IOException {
                        int i4 = this.in.read(bArr, i2, i3);
                        if (i4 >= 0) {
                            ByteBand.this.bytesForDump.write(bArr, i2, i4);
                        }
                        return i4;
                    }
                };
            }
            super.readyToDisburse();
        }

        public OutputStream collectorStream() {
            if (!$assertionsDisabled && phase() != 1) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || this.bytes != null) {
                return this.bytes;
            }
            throw new AssertionError();
        }

        public InputStream getInputStream() {
            if (!$assertionsDisabled && phase() != 6) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || this.in != null) {
                return this.in;
            }
            throw new AssertionError();
        }

        public int getByte() throws IOException {
            int i2 = getInputStream().read();
            if (i2 < 0) {
                throw new EOFException();
            }
            return i2;
        }

        public void putByte(int i2) throws IOException {
            if (!$assertionsDisabled && i2 != (i2 & 255)) {
                throw new AssertionError();
            }
            collectorStream().write(i2);
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public String toString() {
            return "byte " + super.toString();
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/BandStructure$IntBand.class */
    class IntBand extends ValueBand {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BandStructure.class.desiredAssertionStatus();
        }

        public IntBand(String str, Coding coding) {
            super(str, coding);
        }

        public void putInt(int i2) {
            if (!$assertionsDisabled && phase() != 1) {
                throw new AssertionError();
            }
            addValue(i2);
        }

        public int getInt() {
            return getValue();
        }

        public int getIntTotal() {
            if (!$assertionsDisabled && phase() != 6) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && valuesRemainingForDebug() != length()) {
                throw new AssertionError();
            }
            int i2 = 0;
            for (int length = length(); length > 0; length--) {
                i2 += getInt();
            }
            resetForSecondPass();
            return i2;
        }

        public int getIntCount(int i2) {
            if (!$assertionsDisabled && phase() != 6) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && valuesRemainingForDebug() != length()) {
                throw new AssertionError();
            }
            int i3 = 0;
            for (int length = length(); length > 0; length--) {
                if (getInt() == i2) {
                    i3++;
                }
            }
            resetForSecondPass();
            return i3;
        }
    }

    static int getIntTotal(int[] iArr) {
        int i2 = 0;
        for (int i3 : iArr) {
            i2 += i3;
        }
        return i2;
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/BandStructure$CPRefBand.class */
    class CPRefBand extends ValueBand {
        ConstantPool.Index index;
        boolean nullOK;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BandStructure.class.desiredAssertionStatus();
        }

        public CPRefBand(String str, Coding coding, byte b2, boolean z2) {
            super(str, coding);
            this.nullOK = z2;
            if (b2 != 0) {
                BandStructure.this.setBandIndex(this, b2);
            }
        }

        public CPRefBand(BandStructure bandStructure, String str, Coding coding, byte b2) {
            this(str, coding, b2, false);
        }

        public CPRefBand(BandStructure bandStructure, String str, Coding coding, Object obj) {
            this(str, coding, (byte) 0, false);
        }

        public void setIndex(ConstantPool.Index index) {
            this.index = index;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.ValueBand, com.sun.java.util.jar.pack.BandStructure.Band
        protected void readDataFrom(InputStream inputStream) throws IOException {
            super.readDataFrom(inputStream);
            if (!$assertionsDisabled && !BandStructure.this.assertValidCPRefs(this)) {
                throw new AssertionError();
            }
        }

        public void putRef(ConstantPool.Entry entry) {
            addValue(encodeRefOrNull(entry, this.index));
        }

        public void putRef(ConstantPool.Entry entry, ConstantPool.Index index) {
            if (!$assertionsDisabled && this.index != null) {
                throw new AssertionError();
            }
            addValue(encodeRefOrNull(entry, index));
        }

        public void putRef(ConstantPool.Entry entry, byte b2) {
            putRef(entry, BandStructure.this.getCPIndex(b2));
        }

        public ConstantPool.Entry getRef() {
            if (this.index == null) {
                Utils.log.warning("No index for " + ((Object) this));
            }
            if ($assertionsDisabled || this.index != null) {
                return decodeRefOrNull(getValue(), this.index);
            }
            throw new AssertionError();
        }

        public ConstantPool.Entry getRef(ConstantPool.Index index) {
            if ($assertionsDisabled || this.index == null) {
                return decodeRefOrNull(getValue(), index);
            }
            throw new AssertionError();
        }

        public ConstantPool.Entry getRef(byte b2) {
            return getRef(BandStructure.this.getCPIndex(b2));
        }

        private int encodeRefOrNull(ConstantPool.Entry entry, ConstantPool.Index index) {
            int iEncodeRef;
            if (entry == null) {
                iEncodeRef = -1;
            } else {
                iEncodeRef = BandStructure.this.encodeRef(entry, index);
            }
            return (this.nullOK ? 1 : 0) + iEncodeRef;
        }

        private ConstantPool.Entry decodeRefOrNull(int i2, ConstantPool.Index index) {
            int i3 = i2 - (this.nullOK ? 1 : 0);
            if (i3 == -1) {
                return null;
            }
            return BandStructure.this.decodeRef(i3, index);
        }
    }

    int encodeRef(ConstantPool.Entry entry, ConstantPool.Index index) {
        if (index == null) {
            throw new RuntimeException("null index for " + entry.stringValue());
        }
        int iIndexOf = index.indexOf(entry);
        if (this.verbose > 2) {
            Utils.log.fine("putRef " + iIndexOf + " => " + ((Object) entry));
        }
        return iIndexOf;
    }

    ConstantPool.Entry decodeRef(int i2, ConstantPool.Index index) {
        if (i2 < 0 || i2 >= index.size()) {
            Utils.log.warning("decoding bad ref " + i2 + " in " + ((Object) index));
        }
        ConstantPool.Entry entry = index.getEntry(i2);
        if (this.verbose > 2) {
            Utils.log.fine("getRef " + i2 + " => " + ((Object) entry));
        }
        return entry;
    }

    protected CodingChooser getCodingChooser() {
        if (this.codingChooser == null) {
            this.codingChooser = new CodingChooser(this.effort, basicCodings);
            if (this.codingChooser.stress != null && (this instanceof PackageWriter)) {
                ArrayList<Package.Class> arrayList = ((PackageWriter) this).pkg.classes;
                if (!arrayList.isEmpty()) {
                    this.codingChooser.addStressSeed(arrayList.get(0).getName().hashCode());
                }
            }
        }
        return this.codingChooser;
    }

    public CodingMethod chooseCoding(int[] iArr, int i2, int i3, Coding coding, String str, int[] iArr2) {
        if (!$assertionsDisabled && !this.optVaryCodings) {
            throw new AssertionError();
        }
        if (this.effort <= 1) {
            return coding;
        }
        CodingChooser codingChooser = getCodingChooser();
        if (this.verbose > 1 || codingChooser.verbose > 1) {
            Utils.log.fine("--- chooseCoding " + str);
        }
        return codingChooser.choose(iArr, i2, i3, coding, iArr2);
    }

    protected static int decodeEscapeValue(int i2, Coding coding) {
        if (coding.B() == 1 || coding.L() == 0) {
            return -1;
        }
        if (coding.S() != 0) {
            if (-256 <= i2 && i2 <= -1 && coding.min() <= -256) {
                int i3 = (-1) - i2;
                if ($assertionsDisabled || (i3 >= 0 && i3 < 256)) {
                    return i3;
                }
                throw new AssertionError();
            }
            return -1;
        }
        int iL = coding.L();
        if (iL <= i2 && i2 <= iL + 255 && coding.max() >= iL + 255) {
            int i4 = i2 - iL;
            if ($assertionsDisabled || (i4 >= 0 && i4 < 256)) {
                return i4;
            }
            throw new AssertionError();
        }
        return -1;
    }

    protected static int encodeEscapeValue(int i2, Coding coding) {
        int i3;
        if (!$assertionsDisabled && (i2 < 0 || i2 >= 256)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (coding.B() <= 1 || coding.L() <= 0)) {
            throw new AssertionError();
        }
        if (coding.S() != 0) {
            if (!$assertionsDisabled && coding.min() > -256) {
                throw new AssertionError();
            }
            i3 = (-1) - i2;
        } else {
            int iL = coding.L();
            if (!$assertionsDisabled && coding.max() < iL + 255) {
                throw new AssertionError();
            }
            i3 = i2 + iL;
        }
        if ($assertionsDisabled || decodeEscapeValue(i3, coding) == i2) {
            return i3;
        }
        throw new AssertionError((Object) (((Object) coding) + " XB=" + i2 + " X=" + i3));
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/BandStructure$MultiBand.class */
    class MultiBand extends Band {
        Band[] bands;
        int bandCount;
        private int cap;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BandStructure.class.desiredAssertionStatus();
        }

        MultiBand(String str, Coding coding) {
            super(str, coding);
            this.bands = new Band[10];
            this.bandCount = 0;
            this.cap = -1;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public Band init() {
            super.init();
            setCapacity(0);
            if (phase() == 2) {
                setPhase(4);
                setPhase(6);
            }
            return this;
        }

        int size() {
            return this.bandCount;
        }

        Band get(int i2) {
            if ($assertionsDisabled || i2 < this.bandCount) {
                return this.bands[i2];
            }
            throw new AssertionError();
        }

        Band[] toArray() {
            return (Band[]) BandStructure.realloc(this.bands, this.bandCount);
        }

        void add(Band band) {
            if (!$assertionsDisabled && this.bandCount != 0 && !BandStructure.this.notePrevForAssert(band, this.bands[this.bandCount - 1])) {
                throw new AssertionError();
            }
            if (this.bandCount == this.bands.length) {
                this.bands = (Band[]) BandStructure.realloc(this.bands);
            }
            Band[] bandArr = this.bands;
            int i2 = this.bandCount;
            this.bandCount = i2 + 1;
            bandArr[i2] = band;
        }

        ByteBand newByteBand(String str) {
            ByteBand byteBand = BandStructure.this.new ByteBand(str);
            byteBand.init();
            add(byteBand);
            return byteBand;
        }

        IntBand newIntBand(String str) {
            IntBand intBand = BandStructure.this.new IntBand(str, this.regularCoding);
            intBand.init();
            add(intBand);
            return intBand;
        }

        IntBand newIntBand(String str, Coding coding) {
            IntBand intBand = BandStructure.this.new IntBand(str, coding);
            intBand.init();
            add(intBand);
            return intBand;
        }

        MultiBand newMultiBand(String str, Coding coding) {
            MultiBand multiBand = BandStructure.this.new MultiBand(str, coding);
            multiBand.init();
            add(multiBand);
            return multiBand;
        }

        CPRefBand newCPRefBand(String str, byte b2) {
            CPRefBand cPRefBand = new CPRefBand(BandStructure.this, str, this.regularCoding, b2);
            cPRefBand.init();
            add(cPRefBand);
            return cPRefBand;
        }

        CPRefBand newCPRefBand(String str, Coding coding, byte b2) {
            CPRefBand cPRefBand = new CPRefBand(BandStructure.this, str, coding, b2);
            cPRefBand.init();
            add(cPRefBand);
            return cPRefBand;
        }

        CPRefBand newCPRefBand(String str, Coding coding, byte b2, boolean z2) {
            CPRefBand cPRefBand = BandStructure.this.new CPRefBand(str, coding, b2, z2);
            cPRefBand.init();
            add(cPRefBand);
            return cPRefBand;
        }

        int bandCount() {
            return this.bandCount;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public int capacity() {
            return this.cap;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public void setCapacity(int i2) {
            this.cap = i2;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public int length() {
            return 0;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public int valuesRemainingForDebug() {
            return 0;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected void chooseBandCodings() throws IOException {
            for (int i2 = 0; i2 < this.bandCount; i2++) {
                this.bands[i2].chooseBandCodings();
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected long computeOutputSize() {
            long j2 = 0;
            for (int i2 = 0; i2 < this.bandCount; i2++) {
                Band band = this.bands[i2];
                long jOutputSize = band.outputSize();
                if (!$assertionsDisabled && jOutputSize < 0) {
                    throw new AssertionError(band);
                }
                j2 += jOutputSize;
            }
            return j2;
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected void writeDataTo(OutputStream outputStream) throws IOException {
            long count = BandStructure.this.outputCounter != null ? BandStructure.this.outputCounter.getCount() : 0L;
            for (int i2 = 0; i2 < this.bandCount; i2++) {
                Band band = this.bands[i2];
                band.writeTo(outputStream);
                if (BandStructure.this.outputCounter != null) {
                    long count2 = BandStructure.this.outputCounter.getCount();
                    long j2 = count2 - count;
                    count = count2;
                    if ((BandStructure.this.verbose > 0 && j2 > 0) || BandStructure.this.verbose > 1) {
                        Utils.log.info("  ...wrote " + j2 + " bytes from " + ((Object) band));
                    }
                }
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        protected void readDataFrom(InputStream inputStream) throws IOException {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
            for (int i2 = 0; i2 < this.bandCount; i2++) {
                Band band = this.bands[i2];
                band.readFrom(inputStream);
                if ((BandStructure.this.verbose > 0 && band.length() > 0) || BandStructure.this.verbose > 1) {
                    Utils.log.info("  ...read " + ((Object) band));
                }
            }
        }

        @Override // com.sun.java.util.jar.pack.BandStructure.Band
        public String toString() {
            return VectorFormat.DEFAULT_PREFIX + bandCount() + " bands: " + super.toString() + "}";
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/BandStructure$ByteCounter.class */
    private static class ByteCounter extends FilterOutputStream {
        private long count;

        public ByteCounter(OutputStream outputStream) {
            super(outputStream);
        }

        public long getCount() {
            return this.count;
        }

        public void setCount(long j2) {
            this.count = j2;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(int i2) throws IOException {
            this.count++;
            if (this.out != null) {
                this.out.write(i2);
            }
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            this.count += i3;
            if (this.out != null) {
                this.out.write(bArr, i2, i3);
            }
        }

        public String toString() {
            return String.valueOf(getCount());
        }
    }

    void writeAllBandsTo(OutputStream outputStream) throws IOException {
        this.outputCounter = new ByteCounter(outputStream);
        this.all_bands.writeTo(this.outputCounter);
        if (this.verbose > 0) {
            long count = this.outputCounter.getCount();
            Utils.log.info("Wrote total of " + count + " bytes.");
            if (!$assertionsDisabled && count != this.archiveSize0 + this.archiveSize1) {
                throw new AssertionError();
            }
        }
        this.outputCounter = null;
    }

    static IntBand getAttrBand(MultiBand multiBand, int i2) {
        IntBand intBand = (IntBand) multiBand.get(i2);
        switch (i2) {
            case 0:
                if (!$assertionsDisabled && !intBand.name().endsWith("_flags_hi")) {
                    throw new AssertionError();
                }
                break;
            case 1:
                if (!$assertionsDisabled && !intBand.name().endsWith("_flags_lo")) {
                    throw new AssertionError();
                }
                break;
            case 2:
                if (!$assertionsDisabled && !intBand.name().endsWith("_attr_count")) {
                    throw new AssertionError();
                }
                break;
            case 3:
                if (!$assertionsDisabled && !intBand.name().endsWith("_attr_indexes")) {
                    throw new AssertionError();
                }
                break;
            case 4:
                if (!$assertionsDisabled && !intBand.name().endsWith("_attr_calls")) {
                    throw new AssertionError();
                }
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        return intBand;
    }

    protected void setBandIndexes() {
        for (Object[] objArr : this.needPredefIndex) {
            ((CPRefBand) objArr[0]).setIndex(getCPIndex(((Byte) objArr[1]).byteValue()));
        }
        this.needPredefIndex = null;
        if (this.verbose > 3) {
            printCDecl(this.all_bands);
        }
    }

    protected void setBandIndex(CPRefBand cPRefBand, byte b2) {
        Object[] objArr = {cPRefBand, Byte.valueOf(b2)};
        if (b2 == 53) {
            this.allKQBands.add(cPRefBand);
        } else if (this.needPredefIndex != null) {
            this.needPredefIndex.add(objArr);
        } else {
            cPRefBand.setIndex(getCPIndex(b2));
        }
    }

    protected void setConstantValueIndex(Package.Class.Field field) {
        ConstantPool.Index cPIndex = null;
        if (field != null) {
            byte literalTag = field.getLiteralTag();
            cPIndex = getCPIndex(literalTag);
            if (this.verbose > 2) {
                Utils.log.fine("setConstantValueIndex " + ((Object) field) + " " + ConstantPool.tagName(literalTag) + " => " + ((Object) cPIndex));
            }
            if (!$assertionsDisabled && cPIndex == null) {
                throw new AssertionError();
            }
        }
        Iterator<CPRefBand> it = this.allKQBands.iterator();
        while (it.hasNext()) {
            it.next().setIndex(cPIndex);
        }
    }

    private void adjustToClassVersion() throws IOException {
        if (getHighestClassVersion().lessThan(Constants.JAVA6_MAX_CLASS_VERSION)) {
            if (this.verbose > 0) {
                Utils.log.fine("Legacy package version");
            }
            undefineAttribute(0, 3);
        }
    }

    protected void initAttrIndexLimit() {
        for (int i2 = 0; i2 < 4; i2++) {
            if (!$assertionsDisabled && this.attrIndexLimit[i2] != 0) {
                throw new AssertionError();
            }
            this.attrIndexLimit[i2] = haveFlagsHi(i2) ? 63 : 32;
            List<Attribute.Layout> list = this.attrDefs.get(i2);
            if (!$assertionsDisabled && list.size() != 32) {
                throw new AssertionError();
            }
            list.addAll(Collections.nCopies(this.attrIndexLimit[i2] - list.size(), (Attribute.Layout) null));
        }
    }

    protected boolean haveFlagsHi(int i2) {
        int i3 = 1 << (9 + i2);
        switch (i2) {
            case 0:
                if (!$assertionsDisabled && i3 != 512) {
                    throw new AssertionError();
                }
                break;
            case 1:
                if (!$assertionsDisabled && i3 != 1024) {
                    throw new AssertionError();
                }
                break;
            case 2:
                if (!$assertionsDisabled && i3 != 2048) {
                    throw new AssertionError();
                }
                break;
            case 3:
                if (!$assertionsDisabled && i3 != 4096) {
                    throw new AssertionError();
                }
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        return testBit(this.archiveOptions, i3);
    }

    protected List<Attribute.Layout> getPredefinedAttrs(int i2) {
        Attribute.Layout layout;
        if (!$assertionsDisabled && this.attrIndexLimit[i2] == 0) {
            throw new AssertionError();
        }
        ArrayList arrayList = new ArrayList(this.attrIndexLimit[i2]);
        for (int i3 = 0; i3 < this.attrIndexLimit[i2]; i3++) {
            if (!testBit(this.attrDefSeen[i2], 1 << i3) && (layout = this.attrDefs.get(i2).get(i3)) != null) {
                if (!$assertionsDisabled && !isPredefinedAttr(i2, i3)) {
                    throw new AssertionError();
                }
                arrayList.add(layout);
            }
        }
        return arrayList;
    }

    protected boolean isPredefinedAttr(int i2, int i3) {
        if ($assertionsDisabled || this.attrIndexLimit[i2] != 0) {
            return (i3 >= this.attrIndexLimit[i2] || testBit(this.attrDefSeen[i2], 1 << i3) || this.attrDefs.get(i2).get(i3) == null) ? false : true;
        }
        throw new AssertionError();
    }

    protected void adjustSpecialAttrMasks() {
        this.attrClassFileVersionMask = (int) (this.attrClassFileVersionMask & (this.attrDefSeen[0] ^ (-1)));
        for (int i2 = 0; i2 < 4; i2++) {
            this.attrOverflowMask[i2] = (int) (r0[r1] & (this.attrDefSeen[i2] ^ (-1)));
        }
    }

    protected Attribute makeClassFileVersionAttr(Package.Version version) {
        return this.attrClassFileVersion.addContent(version.asBytes());
    }

    protected Package.Version parseClassFileVersionAttr(Attribute attribute) {
        if (!$assertionsDisabled && attribute.layout() != this.attrClassFileVersion) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || attribute.size() == 4) {
            return Package.Version.of(attribute.bytes());
        }
        throw new AssertionError();
    }

    private boolean assertBandOKForElems(Band[] bandArr, Attribute.Layout.Element[] elementArr) {
        for (Attribute.Layout.Element element : elementArr) {
            if (!$assertionsDisabled && !assertBandOKForElem(bandArr, element)) {
                throw new AssertionError();
            }
        }
        return true;
    }

    private boolean assertBandOKForElem(Band[] bandArr, Attribute.Layout.Element element) {
        Band band = null;
        if (element.bandIndex != -1) {
            band = bandArr[element.bandIndex];
        }
        Coding coding = UNSIGNED5;
        boolean z2 = true;
        switch (element.kind) {
            case 1:
                if (element.flagTest((byte) 1)) {
                    coding = SIGNED5;
                    break;
                } else if (element.len == 1) {
                    coding = BYTE1;
                    break;
                }
                break;
            case 2:
                if (!element.flagTest((byte) 2)) {
                    coding = BCI5;
                    break;
                } else {
                    coding = BRANCH5;
                    break;
                }
            case 3:
                coding = BRANCH5;
                break;
            case 4:
                if (element.len == 1) {
                    coding = BYTE1;
                    break;
                }
                break;
            case 5:
                if (element.len == 1) {
                    coding = BYTE1;
                }
                assertBandOKForElems(bandArr, element.body);
                break;
            case 6:
                z2 = false;
                if (!$assertionsDisabled && !(band instanceof CPRefBand)) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && ((CPRefBand) band).nullOK != element.flagTest((byte) 4)) {
                    throw new AssertionError();
                }
                break;
            case 7:
                if (element.flagTest((byte) 1)) {
                    coding = SIGNED5;
                } else if (element.len == 1) {
                    coding = BYTE1;
                }
                assertBandOKForElems(bandArr, element.body);
                break;
            case 8:
                if (!$assertionsDisabled && band != null) {
                    throw new AssertionError();
                }
                assertBandOKForElems(bandArr, element.body);
                return true;
            case 9:
                if ($assertionsDisabled || band == null) {
                    return true;
                }
                throw new AssertionError();
            case 10:
                if (!$assertionsDisabled && band != null) {
                    throw new AssertionError();
                }
                assertBandOKForElems(bandArr, element.body);
                return true;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        if (!$assertionsDisabled && band.regularCoding != coding) {
            throw new AssertionError((Object) (((Object) element) + " // " + ((Object) band)));
        }
        if (!z2 || $assertionsDisabled || (band instanceof IntBand)) {
            return true;
        }
        throw new AssertionError();
    }

    private Attribute.Layout predefineAttribute(int i2, int i3, Band[] bandArr, String str, String str2) {
        Attribute.Layout layout = Attribute.find(i3, str, str2).layout();
        if (i2 >= 0) {
            setAttributeLayoutIndex(layout, i2);
        }
        if (bandArr == null) {
            bandArr = new Band[0];
        }
        if (!$assertionsDisabled && this.attrBandTable.get(layout) != null) {
            throw new AssertionError();
        }
        this.attrBandTable.put(layout, bandArr);
        if (!$assertionsDisabled && layout.bandCount != bandArr.length) {
            throw new AssertionError((Object) (((Object) layout) + " // " + ((Object) Arrays.asList(bandArr))));
        }
        if ($assertionsDisabled || assertBandOKForElems(bandArr, layout.elems)) {
            return layout;
        }
        throw new AssertionError();
    }

    private Attribute.Layout predefineAttribute(int i2, String str, MultiBand multiBand, Attribute attribute) {
        Attribute.Layout layout = attribute.layout();
        return predefineAttribute(i2, layout.ctype(), makeNewAttributeBands(str, layout, multiBand), layout.name(), layout.layout());
    }

    private void undefineAttribute(int i2, int i3) {
        if (this.verbose > 1) {
            System.out.println("Removing predefined " + Constants.ATTR_CONTEXT_NAME[i3] + " attribute on bit " + i2);
        }
        List<Attribute.Layout> list = this.attrDefs.get(i3);
        Attribute.Layout layout = list.get(i2);
        if (!$assertionsDisabled && layout == null) {
            throw new AssertionError();
        }
        list.set(i2, null);
        this.attrIndexTable.put(layout, null);
        if (!$assertionsDisabled && i2 >= 64) {
            throw new AssertionError();
        }
        long[] jArr = this.attrDefSeen;
        jArr[i3] = jArr[i3] & ((1 << i2) ^ (-1));
        long[] jArr2 = this.attrFlagMask;
        jArr2[i3] = jArr2[i3] & ((1 << i2) ^ (-1));
        for (Band band : this.attrBandTable.get(layout)) {
            band.doneWithUnusedBand();
        }
    }

    void makeNewAttributeBands() {
        adjustSpecialAttrMasks();
        for (int i2 = 0; i2 < 4; i2++) {
            String str = Constants.ATTR_CONTEXT_NAME[i2];
            MultiBand multiBand = this.attrBands[i2];
            long j2 = this.attrDefSeen[i2];
            if (!$assertionsDisabled && (j2 & (this.attrFlagMask[i2] ^ (-1))) != 0) {
                throw new AssertionError();
            }
            for (int i3 = 0; i3 < this.attrDefs.get(i2).size(); i3++) {
                Attribute.Layout layout = this.attrDefs.get(i2).get(i3);
                if (layout != null && layout.bandCount != 0) {
                    if (i3 < this.attrIndexLimit[i2] && !testBit(j2, 1 << i3)) {
                        if (!$assertionsDisabled && this.attrBandTable.get(layout) == null) {
                            throw new AssertionError();
                        }
                    } else {
                        multiBand.size();
                        String str2 = str + "_" + layout.name() + "_";
                        if (this.verbose > 1) {
                            Utils.log.fine("Making new bands for " + ((Object) layout));
                        }
                        Band[] bandArrMakeNewAttributeBands = makeNewAttributeBands(str2, layout, multiBand);
                        if (!$assertionsDisabled && bandArrMakeNewAttributeBands.length != layout.bandCount) {
                            throw new AssertionError();
                        }
                        Band[] bandArrPut = this.attrBandTable.put(layout, bandArrMakeNewAttributeBands);
                        if (bandArrPut != null) {
                            for (Band band : bandArrPut) {
                                band.doneWithUnusedBand();
                            }
                        }
                    }
                }
            }
        }
    }

    private Band[] makeNewAttributeBands(String str, Attribute.Layout layout, MultiBand multiBand) {
        int size = multiBand.size();
        makeNewAttributeBands(str, layout.elems, multiBand);
        int size2 = multiBand.size() - size;
        Band[] bandArr = new Band[size2];
        for (int i2 = 0; i2 < size2; i2++) {
            bandArr[i2] = multiBand.get(size + i2);
        }
        return bandArr;
    }

    private void makeNewAttributeBands(String str, Attribute.Layout.Element[] elementArr, MultiBand multiBand) {
        Band bandNewCPRefBand;
        for (Attribute.Layout.Element element : elementArr) {
            String strSubstring = str + multiBand.size() + "_" + element.layout;
            int iIndexOf = strSubstring.indexOf(91);
            if (iIndexOf > 0) {
                strSubstring = strSubstring.substring(0, iIndexOf);
            }
            int iIndexOf2 = strSubstring.indexOf(40);
            if (iIndexOf2 > 0) {
                strSubstring = strSubstring.substring(0, iIndexOf2);
            }
            if (strSubstring.endsWith(PdfOps.H_TOKEN)) {
                strSubstring = strSubstring.substring(0, strSubstring.length() - 1);
            }
            switch (element.kind) {
                case 1:
                    bandNewCPRefBand = newElemBand(element, strSubstring, multiBand);
                    break;
                case 2:
                    if (!element.flagTest((byte) 2)) {
                        bandNewCPRefBand = multiBand.newIntBand(strSubstring, BCI5);
                        break;
                    } else {
                        bandNewCPRefBand = multiBand.newIntBand(strSubstring, BRANCH5);
                        break;
                    }
                case 3:
                    bandNewCPRefBand = multiBand.newIntBand(strSubstring, BRANCH5);
                    break;
                case 4:
                    if (!$assertionsDisabled && element.flagTest((byte) 1)) {
                        throw new AssertionError();
                    }
                    bandNewCPRefBand = newElemBand(element, strSubstring, multiBand);
                    break;
                    break;
                case 5:
                    if (!$assertionsDisabled && element.flagTest((byte) 1)) {
                        throw new AssertionError();
                    }
                    bandNewCPRefBand = newElemBand(element, strSubstring, multiBand);
                    makeNewAttributeBands(str, element.body, multiBand);
                    break;
                case 6:
                    bandNewCPRefBand = multiBand.newCPRefBand(strSubstring, UNSIGNED5, element.refKind, element.flagTest((byte) 4));
                    break;
                case 7:
                    bandNewCPRefBand = newElemBand(element, strSubstring, multiBand);
                    makeNewAttributeBands(str, element.body, multiBand);
                    break;
                case 8:
                    if (element.flagTest((byte) 8)) {
                        continue;
                    } else {
                        makeNewAttributeBands(str, element.body, multiBand);
                    }
                case 9:
                case 10:
                    makeNewAttributeBands(str, element.body, multiBand);
                    continue;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    continue;
            }
            if (this.verbose > 1) {
                Utils.log.fine("New attribute band " + ((Object) bandNewCPRefBand));
            }
        }
    }

    private Band newElemBand(Attribute.Layout.Element element, String str, MultiBand multiBand) {
        if (element.flagTest((byte) 1)) {
            return multiBand.newIntBand(str, SIGNED5);
        }
        if (element.len == 1) {
            return multiBand.newIntBand(str, BYTE1);
        }
        return multiBand.newIntBand(str, UNSIGNED5);
    }

    protected int setAttributeLayoutIndex(Attribute.Layout layout, int i2) {
        int i3 = layout.ctype;
        if (!$assertionsDisabled && (-1 > i2 || i2 >= this.attrIndexLimit[i3])) {
            throw new AssertionError();
        }
        List<Attribute.Layout> list = this.attrDefs.get(i3);
        if (i2 == -1) {
            int size = list.size();
            list.add(layout);
            if (this.verbose > 0) {
                Utils.log.info("Adding new attribute at " + ((Object) layout) + ": " + size);
            }
            this.attrIndexTable.put(layout, Integer.valueOf(size));
            return size;
        }
        if (testBit(this.attrDefSeen[i3], 1 << i2)) {
            throw new RuntimeException("Multiple explicit definition at " + i2 + ": " + ((Object) layout));
        }
        long[] jArr = this.attrDefSeen;
        jArr[i3] = jArr[i3] | (1 << i2);
        if (!$assertionsDisabled && (0 > i2 || i2 >= this.attrIndexLimit[i3])) {
            throw new AssertionError();
        }
        if (this.verbose > (this.attrClassFileVersionMask == 0 ? 2 : 0)) {
            Utils.log.fine("Fixing new attribute at " + i2 + ": " + ((Object) layout) + (list.get(i2) == null ? "" : "; replacing " + ((Object) list.get(i2))));
        }
        long[] jArr2 = this.attrFlagMask;
        jArr2[i3] = jArr2[i3] | (1 << i2);
        this.attrIndexTable.put(list.get(i2), null);
        list.set(i2, layout);
        this.attrIndexTable.put(layout, Integer.valueOf(i2));
        return i2;
    }

    static int shortCodeHeader(Code code) {
        int iShortCodeHeader_h_base;
        int i2 = code.max_stack;
        int i3 = code.max_locals;
        int length = code.handler_class.length;
        if (length >= shortCodeLimits.length) {
            return 0;
        }
        int argumentSize = code.getMethod().getArgumentSize();
        if (!$assertionsDisabled && i3 < argumentSize) {
            throw new AssertionError();
        }
        if (i3 < argumentSize) {
            return 0;
        }
        int i4 = i3 - argumentSize;
        int i5 = shortCodeLimits[length][0];
        int i6 = shortCodeLimits[length][1];
        if (i2 >= i5 || i4 >= i6 || (iShortCodeHeader_h_base = shortCodeHeader_h_base(length) + i2 + (i5 * i4)) > 255) {
            return 0;
        }
        if (!$assertionsDisabled && shortCodeHeader_max_stack(iShortCodeHeader_h_base) != i2) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && shortCodeHeader_max_na_locals(iShortCodeHeader_h_base) != i4) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || shortCodeHeader_handler_count(iShortCodeHeader_h_base) == length) {
            return iShortCodeHeader_h_base;
        }
        throw new AssertionError();
    }

    static int shortCodeHeader_handler_count(int i2) {
        if (!$assertionsDisabled && (i2 <= 0 || i2 > 255)) {
            throw new AssertionError();
        }
        int i3 = 0;
        while (i2 >= shortCodeHeader_h_base(i3 + 1)) {
            i3++;
        }
        return i3;
    }

    static int shortCodeHeader_max_stack(int i2) {
        int iShortCodeHeader_handler_count = shortCodeHeader_handler_count(i2);
        return (i2 - shortCodeHeader_h_base(iShortCodeHeader_handler_count)) % shortCodeLimits[iShortCodeHeader_handler_count][0];
    }

    static int shortCodeHeader_max_na_locals(int i2) {
        int iShortCodeHeader_handler_count = shortCodeHeader_handler_count(i2);
        return (i2 - shortCodeHeader_h_base(iShortCodeHeader_handler_count)) / shortCodeLimits[iShortCodeHeader_handler_count][0];
    }

    private static int shortCodeHeader_h_base(int i2) {
        if (!$assertionsDisabled && i2 > shortCodeLimits.length) {
            throw new AssertionError();
        }
        int i3 = 1;
        for (int i4 = 0; i4 < i2; i4++) {
            i3 += shortCodeLimits[i4][0] * shortCodeLimits[i4][1];
        }
        return i3;
    }

    protected void putLabel(IntBand intBand, Code code, int i2, int i3) {
        intBand.putInt(code.encodeBCI(i3) - code.encodeBCI(i2));
    }

    protected int getLabel(IntBand intBand, Code code, int i2) {
        return code.decodeBCI(intBand.getInt() + code.encodeBCI(i2));
    }

    protected CPRefBand getCPRefOpBand(int i2) {
        switch (Instruction.getCPRefOpTag(i2)) {
            case 7:
                return this.bc_classref;
            case 9:
                return this.bc_fieldref;
            case 10:
                return this.bc_methodref;
            case 11:
                return this.bc_imethodref;
            case 18:
                return this.bc_indyref;
            case 51:
                switch (i2) {
                    case 18:
                    case 19:
                        return this.bc_stringref;
                    case 20:
                        return this.bc_longref;
                    case 233:
                    case 236:
                        return this.bc_classref;
                    case 234:
                    case 237:
                        return this.bc_intref;
                    case 235:
                    case 238:
                        return this.bc_floatref;
                    case 239:
                        return this.bc_doubleref;
                    case 240:
                    case 241:
                        return this.bc_loadablevalueref;
                }
        }
        if ($assertionsDisabled) {
            return null;
        }
        throw new AssertionError();
    }

    protected CPRefBand selfOpRefBand(int i2) {
        if (!$assertionsDisabled && !Instruction.isSelfLinkerOp(i2)) {
            throw new AssertionError();
        }
        int i3 = i2 - 202;
        boolean z2 = i3 >= 14;
        if (z2) {
            i3 -= 14;
        }
        if (i3 >= 7) {
            i3 -= 7;
        }
        boolean zIsFieldOp = Instruction.isFieldOp(178 + i3);
        return !z2 ? zIsFieldOp ? this.bc_thisfield : this.bc_thismethod : zIsFieldOp ? this.bc_superfield : this.bc_supermethod;
    }

    static OutputStream getDumpStream(Band band, String str) throws IOException {
        return getDumpStream(band.name, band.seqForDebug, str, band);
    }

    static OutputStream getDumpStream(ConstantPool.Index index, String str) throws IOException {
        if (index.size() == 0) {
            return new ByteArrayOutputStream();
        }
        return getDumpStream(index.debugName, ConstantPool.TAG_ORDER[index.cpMap[0].tag], str, index);
    }

    static OutputStream getDumpStream(String str, int i2, String str2, Object obj) throws IOException {
        if (dumpDir == null) {
            dumpDir = File.createTempFile("BD_", "", new File("."));
            dumpDir.delete();
            if (dumpDir.mkdir()) {
                Utils.log.info("Dumping bands to " + ((Object) dumpDir));
            }
        }
        File file = new File(dumpDir, ((10000 + i2) + "_" + str.replace('(', ' ').replace(')', ' ').replace('/', ' ').replace('*', ' ').trim().replace(' ', '_')).substring(1) + str2);
        Utils.log.info("Dumping " + obj + " to " + ((Object) file));
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    static boolean assertCanChangeLength(Band band) {
        switch (band.phase) {
            case 1:
            case 4:
                return true;
            default:
                return false;
        }
    }

    static boolean assertPhase(Band band, int i2) {
        if (band.phase() != i2) {
            Utils.log.warning("phase expected " + i2 + " was " + band.phase() + " in " + ((Object) band));
            return false;
        }
        return true;
    }

    static int verbose() {
        return Utils.currentPropMap().getInteger("com.sun.java.util.jar.pack.verbose");
    }

    static boolean assertPhaseChangeOK(Band band, int i2, int i3) {
        switch ((i2 * 10) + i3) {
            case 1:
                if (!$assertionsDisabled && band.isReader()) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && band.capacity() < 0) {
                    throw new AssertionError();
                }
                if ($assertionsDisabled || band.length() == 0) {
                    return true;
                }
                throw new AssertionError();
            case 2:
                if (!$assertionsDisabled && !band.isReader()) {
                    throw new AssertionError();
                }
                if ($assertionsDisabled || band.capacity() < 0) {
                    return true;
                }
                throw new AssertionError();
            case 13:
            case 33:
                if ($assertionsDisabled || band.length() == 0) {
                    return true;
                }
                throw new AssertionError();
            case 15:
            case 35:
                return true;
            case 24:
                if (!$assertionsDisabled && Math.max(0, band.capacity()) < band.valuesExpected()) {
                    throw new AssertionError();
                }
                if ($assertionsDisabled || band.length() <= 0) {
                    return true;
                }
                throw new AssertionError();
            case 46:
                if ($assertionsDisabled || band.valuesRemainingForDebug() == band.length()) {
                    return true;
                }
                throw new AssertionError();
            case 58:
                return true;
            case 68:
                if ($assertionsDisabled || assertDoneDisbursing(band)) {
                    return true;
                }
                throw new AssertionError();
            default:
                if (i2 == i3) {
                    Utils.log.warning("Already in phase " + i2);
                    return false;
                }
                Utils.log.warning("Unexpected phase " + i2 + " -> " + i3);
                return false;
        }
    }

    private static boolean assertDoneDisbursing(Band band) {
        if (band.phase != 6) {
            Utils.log.warning("assertDoneDisbursing: still in phase " + band.phase + ": " + ((Object) band));
            if (verbose() <= 1) {
                return false;
            }
        }
        int iValuesRemainingForDebug = band.valuesRemainingForDebug();
        if (iValuesRemainingForDebug > 0) {
            Utils.log.warning("assertDoneDisbursing: " + iValuesRemainingForDebug + " values left in " + ((Object) band));
            if (verbose() <= 1) {
                return false;
            }
        }
        if (band instanceof MultiBand) {
            MultiBand multiBand = (MultiBand) band;
            for (int i2 = 0; i2 < multiBand.bandCount; i2++) {
                Band band2 = multiBand.bands[i2];
                if (band2.phase != 8) {
                    Utils.log.warning("assertDoneDisbursing: sub-band still in phase " + band2.phase + ": " + ((Object) band2));
                    if (verbose() <= 1) {
                        return false;
                    }
                }
            }
            return true;
        }
        return true;
    }

    private static void printCDecl(Band band) {
        String str;
        ConstantPool.Index index;
        if (band instanceof MultiBand) {
            MultiBand multiBand = (MultiBand) band;
            for (int i2 = 0; i2 < multiBand.bandCount; i2++) {
                printCDecl(multiBand.bands[i2]);
            }
            return;
        }
        String str2 = "NULL";
        if ((band instanceof CPRefBand) && (index = ((CPRefBand) band).index) != null) {
            str2 = "INDEX(" + index.debugName + ")";
        }
        Coding[] codingArr = {BYTE1, CHAR3, BCI5, BRANCH5, UNSIGNED5, UDELTA5, SIGNED5, DELTA5, MDELTA5};
        String[] strArr = {"BYTE1", "CHAR3", "BCI5", "BRANCH5", "UNSIGNED5", "UDELTA5", "SIGNED5", "DELTA5", "MDELTA5"};
        Coding coding = band.regularCoding;
        int iIndexOf = Arrays.asList(codingArr).indexOf(coding);
        if (iIndexOf >= 0) {
            str = strArr[iIndexOf];
        } else {
            str = "CODING" + coding.keyString();
        }
        System.out.println("  BAND_INIT(\"" + band.name() + "\", " + str + ", " + str2 + "),");
    }

    boolean notePrevForAssert(Band band, Band band2) {
        if (this.prevForAssertMap == null) {
            this.prevForAssertMap = new HashMap();
        }
        this.prevForAssertMap.put(band, band2);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean assertReadyToReadFrom(Band band, InputStream inputStream) throws IOException {
        Band band2 = this.prevForAssertMap.get(band);
        if (band2 != null && phaseCmp(band2.phase(), 6) < 0) {
            Utils.log.warning("Previous band not done reading.");
            Utils.log.info("    Previous band: " + ((Object) band2));
            Utils.log.info("        Next band: " + ((Object) band));
            if (!$assertionsDisabled && this.verbose <= 0) {
                throw new AssertionError();
            }
        }
        String str = band.name;
        if (this.optDebugBands && !str.startsWith("(")) {
            if (!$assertionsDisabled && bandSequenceList == null) {
                throw new AssertionError();
            }
            String strRemoveFirst = bandSequenceList.removeFirst();
            if (!strRemoveFirst.equals(str)) {
                Utils.log.warning("Expected " + str + " but read: " + strRemoveFirst);
                return false;
            }
            Utils.log.info("Read band in sequence: " + str);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean assertValidCPRefs(CPRefBand cPRefBand) {
        if (cPRefBand.index == null) {
            return true;
        }
        int size = cPRefBand.index.size() + 1;
        for (int i2 = 0; i2 < cPRefBand.length(); i2++) {
            int iValueAtForDebug = cPRefBand.valueAtForDebug(i2);
            if (iValueAtForDebug < 0 || iValueAtForDebug >= size) {
                Utils.log.warning("CP ref out of range [" + i2 + "] = " + iValueAtForDebug + " in " + ((Object) cPRefBand));
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean assertReadyToWriteTo(Band band, OutputStream outputStream) throws IOException {
        Band band2 = this.prevForAssertMap.get(band);
        if (band2 != null && phaseCmp(band2.phase(), 8) < 0) {
            Utils.log.warning("Previous band not done writing.");
            Utils.log.info("    Previous band: " + ((Object) band2));
            Utils.log.info("        Next band: " + ((Object) band));
            if (!$assertionsDisabled && this.verbose <= 0) {
                throw new AssertionError();
            }
        }
        String str = band.name;
        if (this.optDebugBands && !str.startsWith("(")) {
            if (bandSequenceList == null) {
                bandSequenceList = new LinkedList<>();
            }
            bandSequenceList.add(str);
            return true;
        }
        return true;
    }

    protected static boolean testBit(int i2, int i3) {
        return (i2 & i3) != 0;
    }

    protected static int setBit(int i2, int i3, boolean z2) {
        return z2 ? i2 | i3 : i2 & (i3 ^ (-1));
    }

    protected static boolean testBit(long j2, long j3) {
        return (j2 & j3) != 0;
    }

    protected static long setBit(long j2, long j3, boolean z2) {
        return z2 ? j2 | j3 : j2 & (j3 ^ (-1));
    }

    static void printArrayTo(PrintStream printStream, int[] iArr, int i2, int i3) {
        int i4 = i3 - i2;
        for (int i5 = 0; i5 < i4; i5++) {
            if (i5 % 10 == 0) {
                printStream.println();
            } else {
                printStream.print(" ");
            }
            printStream.print(iArr[i2 + i5]);
        }
        printStream.println();
    }

    static void printArrayTo(PrintStream printStream, ConstantPool.Entry[] entryArr, int i2, int i3) {
        printArrayTo(printStream, entryArr, i2, i3, false);
    }

    static void printArrayTo(PrintStream printStream, ConstantPool.Entry[] entryArr, int i2, int i3, boolean z2) {
        StringBuffer stringBuffer = new StringBuffer();
        int i4 = i3 - i2;
        for (int i5 = 0; i5 < i4; i5++) {
            ConstantPool.Entry entry = entryArr[i2 + i5];
            printStream.print(i2 + i5);
            printStream.print("=");
            if (z2) {
                printStream.print(entry.tag);
                printStream.print(CallSiteDescriptor.TOKEN_DELIMITER);
            }
            String strStringValue = entry.stringValue();
            stringBuffer.setLength(0);
            for (int i6 = 0; i6 < strStringValue.length(); i6++) {
                char cCharAt = strStringValue.charAt(i6);
                if (cCharAt >= ' ' && cCharAt <= '~' && cCharAt != '\\') {
                    stringBuffer.append(cCharAt);
                } else if (cCharAt == '\\') {
                    stringBuffer.append("\\\\");
                } else if (cCharAt == '\n') {
                    stringBuffer.append("\\n");
                } else if (cCharAt == '\t') {
                    stringBuffer.append("\\t");
                } else if (cCharAt == '\r') {
                    stringBuffer.append("\\r");
                } else {
                    String str = "000" + Integer.toHexString(cCharAt);
                    stringBuffer.append("\\u").append(str.substring(str.length() - 4));
                }
            }
            printStream.println(stringBuffer);
        }
    }

    protected static Object[] realloc(Object[] objArr, int i2) {
        Object[] objArr2 = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), i2);
        System.arraycopy(objArr, 0, objArr2, 0, Math.min(objArr.length, i2));
        return objArr2;
    }

    protected static Object[] realloc(Object[] objArr) {
        return realloc(objArr, Math.max(10, objArr.length * 2));
    }

    protected static int[] realloc(int[] iArr, int i2) {
        if (i2 == 0) {
            return Constants.noInts;
        }
        if (iArr == null) {
            return new int[i2];
        }
        int[] iArr2 = new int[i2];
        System.arraycopy(iArr, 0, iArr2, 0, Math.min(iArr.length, i2));
        return iArr2;
    }

    protected static int[] realloc(int[] iArr) {
        return realloc(iArr, Math.max(10, iArr.length * 2));
    }

    protected static byte[] realloc(byte[] bArr, int i2) {
        if (i2 == 0) {
            return Constants.noBytes;
        }
        if (bArr == null) {
            return new byte[i2];
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, 0, bArr2, 0, Math.min(bArr.length, i2));
        return bArr2;
    }

    protected static byte[] realloc(byte[] bArr) {
        return realloc(bArr, Math.max(10, bArr.length * 2));
    }
}
