package sun.awt;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import org.icepdf.core.util.PdfOps;
import sun.font.CompositeFontDescriptor;
import sun.font.FontUtilities;
import sun.font.SunFontManager;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/FontConfiguration.class */
public abstract class FontConfiguration {
    protected static String osVersion;
    protected static String osName;
    protected static String encoding;
    protected static Locale startupLocale;
    protected static Hashtable localeMap;
    private static FontConfiguration fontConfig;
    private static PlatformLogger logger;
    protected static boolean isProperties;
    protected SunFontManager fontManager;
    protected boolean preferLocaleFonts;
    protected boolean preferPropFonts;
    private File fontConfigFile;
    private boolean foundOsSpecificFile;
    private boolean inited;
    private String javaLib;
    private static short stringIDNum;
    private static short[] stringIDs;
    private static StringBuilder stringTable;
    public static boolean verbose;
    private short initELC;
    private Locale initLocale;
    private String initEncoding;
    private String alphabeticSuffix;
    private short[][][] compFontNameIDs;
    private int[][][] compExclusions;
    private int[] compCoreNum;
    private Set<Short> coreFontNameIDs;
    private Set<Short> fallbackFontNameIDs;
    protected static final int NUM_FONTS = 5;
    protected static final int NUM_STYLES = 4;
    protected static final String[] fontNames;
    protected static final String[] publicFontNames;
    protected static final String[] styleNames;
    protected static String[] installedFallbackFontFiles;
    protected HashMap reorderMap;
    private Hashtable charsetRegistry;
    private FontDescriptor[][][] fontDescriptors;
    HashMap<String, Boolean> existsMap;
    private int numCoreFonts;
    private String[] componentFonts;
    HashMap<String, String> filenamesMap;
    HashSet<String> coreFontFileNames;
    private static final int HEAD_LENGTH = 20;
    private static final int INDEX_scriptIDs = 0;
    private static final int INDEX_scriptFonts = 1;
    private static final int INDEX_elcIDs = 2;
    private static final int INDEX_sequences = 3;
    private static final int INDEX_fontfileNameIDs = 4;
    private static final int INDEX_componentFontNameIDs = 5;
    private static final int INDEX_filenames = 6;
    private static final int INDEX_awtfontpaths = 7;
    private static final int INDEX_exclusions = 8;
    private static final int INDEX_proportionals = 9;
    private static final int INDEX_scriptFontsMotif = 10;
    private static final int INDEX_alphabeticSuffix = 11;
    private static final int INDEX_stringIDs = 12;
    private static final int INDEX_stringTable = 13;
    private static final int INDEX_TABLEEND = 14;
    private static final int INDEX_fallbackScripts = 15;
    private static final int INDEX_appendedfontpath = 16;
    private static final int INDEX_version = 17;
    private static final int INDEX_systemfontconf = 18;
    private static short[] head;
    private static short[] table_scriptIDs;
    private static short[] table_scriptFonts;
    private static short[] table_elcIDs;
    private static short[] table_sequences;
    private static short[] table_fontfileNameIDs;
    private static short[] table_componentFontNameIDs;
    private static short[] table_filenames;
    protected static short[] table_awtfontpaths;
    private static short[] table_exclusions;
    private static short[] table_proportionals;
    private static short[] table_scriptFontsMotif;
    private static short[] table_alphabeticSuffix;
    private static short[] table_stringIDs;
    private static char[] table_stringTable;
    private HashMap<String, Short> reorderScripts;
    private static String[] stringCache;
    private static final int[] EMPTY_INT_ARRAY;
    private static final String[] EMPTY_STRING_ARRAY;
    private static final short[] EMPTY_SHORT_ARRAY;
    private static final String UNDEFINED_COMPONENT_FONT = "unknown";
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract String getFallbackFamilyName(String str, String str2);

    protected abstract void initReorderMap();

    protected abstract String getEncoding(String str, String str2);

    protected abstract Charset getDefaultFontCharset(String str);

    protected abstract String getFaceNameFromComponentFontName(String str);

    protected abstract String getFileNameFromComponentFontName(String str);

    static {
        $assertionsDisabled = !FontConfiguration.class.desiredAssertionStatus();
        startupLocale = null;
        localeMap = null;
        isProperties = true;
        fontNames = new String[]{"serif", "sansserif", "monospaced", "dialog", "dialoginput"};
        publicFontNames = new String[]{"Serif", "SansSerif", "Monospaced", Font.DIALOG, Font.DIALOG_INPUT};
        styleNames = new String[]{"plain", "bold", "italic", "bolditalic"};
        installedFallbackFontFiles = null;
        EMPTY_INT_ARRAY = new int[0];
        EMPTY_STRING_ARRAY = new String[0];
        EMPTY_SHORT_ARRAY = new short[0];
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [int[][], int[][][]] */
    public FontConfiguration(SunFontManager sunFontManager) {
        this.initELC = (short) -1;
        this.compFontNameIDs = new short[5][4][];
        this.compExclusions = new int[5][];
        this.compCoreNum = new int[5];
        this.coreFontNameIDs = new HashSet();
        this.fallbackFontNameIDs = new HashSet();
        this.reorderMap = null;
        this.charsetRegistry = new Hashtable(5);
        this.fontDescriptors = new FontDescriptor[5][4][];
        this.numCoreFonts = -1;
        this.componentFonts = null;
        this.filenamesMap = new HashMap<>();
        this.coreFontFileNames = new HashSet<>();
        if (FontUtilities.debugFonts()) {
            FontUtilities.getLogger().info("Creating standard Font Configuration");
        }
        if (FontUtilities.debugFonts() && logger == null) {
            logger = PlatformLogger.getLogger("sun.awt.FontConfiguration");
        }
        this.fontManager = sunFontManager;
        setOsNameAndVersion();
        setEncoding();
        findFontConfigFile();
    }

    public synchronized boolean init() {
        if (!this.inited) {
            this.preferLocaleFonts = false;
            this.preferPropFonts = false;
            setFontConfiguration();
            readFontConfigFile(this.fontConfigFile);
            if (this.fontConfigFile == null) {
                return false;
            }
            initFontConfig();
            this.inited = true;
            return true;
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [int[][], int[][][]] */
    public FontConfiguration(SunFontManager sunFontManager, boolean z2, boolean z3) {
        this.initELC = (short) -1;
        this.compFontNameIDs = new short[5][4][];
        this.compExclusions = new int[5][];
        this.compCoreNum = new int[5];
        this.coreFontNameIDs = new HashSet();
        this.fallbackFontNameIDs = new HashSet();
        this.reorderMap = null;
        this.charsetRegistry = new Hashtable(5);
        this.fontDescriptors = new FontDescriptor[5][4][];
        this.numCoreFonts = -1;
        this.componentFonts = null;
        this.filenamesMap = new HashMap<>();
        this.coreFontFileNames = new HashSet<>();
        this.fontManager = sunFontManager;
        if (FontUtilities.debugFonts()) {
            FontUtilities.getLogger().info("Creating alternate Font Configuration");
        }
        this.preferLocaleFonts = z2;
        this.preferPropFonts = z3;
        initFontConfig();
    }

    protected void setOsNameAndVersion() {
        osName = System.getProperty("os.name");
        osVersion = System.getProperty("os.version");
    }

    private void setEncoding() {
        encoding = Charset.defaultCharset().name();
        startupLocale = SunToolkit.getStartupLocale();
    }

    public boolean foundOsSpecificFile() {
        return this.foundOsSpecificFile;
    }

    public boolean fontFilesArePresent() {
        if (!init()) {
            return false;
        }
        final String strMapFileName = mapFileName(getComponentFileName(getComponentFileID(this.compFontNameIDs[0][0][0])));
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.FontConfiguration.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                try {
                    return Boolean.valueOf(new File(strMapFileName).exists());
                } catch (Exception e2) {
                    return false;
                }
            }
        })).booleanValue();
    }

    private void findFontConfigFile() {
        this.foundOsSpecificFile = true;
        String property = System.getProperty("java.home");
        if (property == null) {
            throw new Error("java.home property not set");
        }
        this.javaLib = property + File.separator + "lib";
        String property2 = System.getProperty("sun.awt.fontconfig");
        if (property2 != null) {
            this.fontConfigFile = new File(property2);
        } else {
            this.fontConfigFile = findFontConfigFile(this.javaLib);
        }
    }

    private void readFontConfigFile(File file) {
        getInstalledFallbackFonts(this.javaLib);
        if (file != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                if (isProperties) {
                    loadProperties(fileInputStream);
                } else {
                    loadBinary(fileInputStream);
                }
                fileInputStream.close();
                if (FontUtilities.debugFonts()) {
                    logger.config("Read logical font configuration from " + ((Object) file));
                }
            } catch (IOException e2) {
                if (FontUtilities.debugFonts()) {
                    logger.config("Failed to read logical font configuration from " + ((Object) file));
                }
            }
        }
        if (file == null) {
            return;
        }
        String version = getVersion();
        if (!"1".equals(version) && FontUtilities.debugFonts()) {
            logger.config("Unsupported fontconfig version: " + version);
        }
    }

    protected void getInstalledFallbackFonts(String str) {
        String str2 = str + File.separator + "fonts" + File.separator + Constants.ELEMNAME_FALLBACK_STRING;
        File file = new File(str2);
        if (file.exists() && file.isDirectory()) {
            String[] list = file.list(this.fontManager.getTrueTypeFilter());
            String[] list2 = file.list(this.fontManager.getType1Filter());
            int length = list == null ? 0 : list.length;
            int length2 = list2 == null ? 0 : list2.length;
            int i2 = length + length2;
            if (length + length2 == 0) {
                return;
            }
            installedFallbackFontFiles = new String[i2];
            for (int i3 = 0; i3 < length; i3++) {
                installedFallbackFontFiles[i3] = ((Object) file) + File.separator + list[i3];
            }
            for (int i4 = 0; i4 < length2; i4++) {
                installedFallbackFontFiles[i4 + length] = ((Object) file) + File.separator + list2[i4];
            }
            this.fontManager.registerFontsInDir(str2);
        }
    }

    private File findImpl(String str) {
        File file = new File(str + ".properties");
        if (file.canRead()) {
            isProperties = true;
            return file;
        }
        File file2 = new File(str + ".bfc");
        if (file2.canRead()) {
            isProperties = false;
            return file2;
        }
        return null;
    }

    private File findFontConfigFile(String str) {
        File fileFindImpl;
        File fileFindImpl2;
        String str2 = str + File.separator + "fontconfig";
        String strSubstring = null;
        if (osVersion != null && osName != null) {
            File fileFindImpl3 = findImpl(str2 + "." + osName + "." + osVersion);
            if (fileFindImpl3 != null) {
                return fileFindImpl3;
            }
            if (osVersion.indexOf(".") != -1) {
                strSubstring = osVersion.substring(0, osVersion.indexOf("."));
                File fileFindImpl4 = findImpl(str2 + "." + osName + "." + strSubstring);
                if (fileFindImpl4 != null) {
                    return fileFindImpl4;
                }
            }
        }
        if (osName != null && (fileFindImpl2 = findImpl(str2 + "." + osName)) != null) {
            return fileFindImpl2;
        }
        if (osVersion != null) {
            File fileFindImpl5 = findImpl(str2 + "." + osVersion);
            if (fileFindImpl5 != null) {
                return fileFindImpl5;
            }
            if (strSubstring != null && (fileFindImpl = findImpl(str2 + "." + strSubstring)) != null) {
                return fileFindImpl;
            }
        }
        this.foundOsSpecificFile = false;
        File fileFindImpl6 = findImpl(str2);
        if (fileFindImpl6 != null) {
            return fileFindImpl6;
        }
        return null;
    }

    public static void loadBinary(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        head = readShortTable(dataInputStream, 20);
        int[] iArr = new int[14];
        for (int i2 = 0; i2 < 14; i2++) {
            iArr[i2] = head[i2 + 1] - head[i2];
        }
        table_scriptIDs = readShortTable(dataInputStream, iArr[0]);
        table_scriptFonts = readShortTable(dataInputStream, iArr[1]);
        table_elcIDs = readShortTable(dataInputStream, iArr[2]);
        table_sequences = readShortTable(dataInputStream, iArr[3]);
        table_fontfileNameIDs = readShortTable(dataInputStream, iArr[4]);
        table_componentFontNameIDs = readShortTable(dataInputStream, iArr[5]);
        table_filenames = readShortTable(dataInputStream, iArr[6]);
        table_awtfontpaths = readShortTable(dataInputStream, iArr[7]);
        table_exclusions = readShortTable(dataInputStream, iArr[8]);
        table_proportionals = readShortTable(dataInputStream, iArr[9]);
        table_scriptFontsMotif = readShortTable(dataInputStream, iArr[10]);
        table_alphabeticSuffix = readShortTable(dataInputStream, iArr[11]);
        table_stringIDs = readShortTable(dataInputStream, iArr[12]);
        stringCache = new String[table_stringIDs.length + 1];
        int i3 = iArr[13];
        byte[] bArr = new byte[i3 * 2];
        table_stringTable = new char[i3];
        dataInputStream.read(bArr);
        int i4 = 0;
        int i5 = 0;
        while (i4 < i3) {
            int i6 = i4;
            i4++;
            int i7 = i5;
            int i8 = i5 + 1;
            i5 = i8 + 1;
            table_stringTable[i6] = (char) ((bArr[i7] << 8) | (bArr[i8] & 255));
        }
        if (verbose) {
            dump();
        }
    }

    public static void saveBinary(OutputStream outputStream) throws IOException {
        sanityCheck();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        writeShortTable(dataOutputStream, head);
        writeShortTable(dataOutputStream, table_scriptIDs);
        writeShortTable(dataOutputStream, table_scriptFonts);
        writeShortTable(dataOutputStream, table_elcIDs);
        writeShortTable(dataOutputStream, table_sequences);
        writeShortTable(dataOutputStream, table_fontfileNameIDs);
        writeShortTable(dataOutputStream, table_componentFontNameIDs);
        writeShortTable(dataOutputStream, table_filenames);
        writeShortTable(dataOutputStream, table_awtfontpaths);
        writeShortTable(dataOutputStream, table_exclusions);
        writeShortTable(dataOutputStream, table_proportionals);
        writeShortTable(dataOutputStream, table_scriptFontsMotif);
        writeShortTable(dataOutputStream, table_alphabeticSuffix);
        writeShortTable(dataOutputStream, table_stringIDs);
        dataOutputStream.writeChars(new String(table_stringTable));
        outputStream.close();
        if (verbose) {
            dump();
        }
    }

    public static void loadProperties(InputStream inputStream) throws IOException {
        stringIDNum = (short) 1;
        stringIDs = new short[1000];
        stringTable = new StringBuilder(4096);
        if (verbose && logger == null) {
            logger = PlatformLogger.getLogger("sun.awt.FontConfiguration");
        }
        new PropertiesHandler().load(inputStream);
        stringIDs = null;
        stringTable = null;
    }

    private void initFontConfig() {
        this.initLocale = startupLocale;
        this.initEncoding = encoding;
        if (this.preferLocaleFonts && !willReorderForStartupLocale()) {
            this.preferLocaleFonts = false;
        }
        this.initELC = getInitELC();
        initAllComponentFonts();
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x00a5 A[PHI: r9
  0x00a5: PHI (r9v1 java.lang.String) = (r9v0 java.lang.String), (r9v2 java.lang.String), (r9v3 java.lang.String) binds: [B:11:0x0070, B:13:0x0094, B:15:0x00a2] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private short getInitELC() {
        /*
            Method dump skipped, instructions count: 256
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.awt.FontConfiguration.getInitELC():short");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initAllComponentFonts() {
        short[] fallbackScripts = getFallbackScripts();
        for (int i2 = 0; i2 < 5; i2++) {
            short[] coreScripts = getCoreScripts(i2);
            this.compCoreNum[i2] = coreScripts.length;
            int[] iArr = new int[coreScripts.length];
            for (int i3 = 0; i3 < coreScripts.length; i3++) {
                iArr[i3] = getExclusionRanges(coreScripts[i3]);
            }
            this.compExclusions[i2] = iArr;
            for (int i4 = 0; i4 < 4; i4++) {
                short[] sArr = new short[coreScripts.length + fallbackScripts.length];
                int i5 = 0;
                while (i5 < coreScripts.length) {
                    sArr[i5] = getComponentFontID(coreScripts[i5], i2, i4);
                    if (this.preferLocaleFonts && localeMap != null && this.fontManager.usingAlternateFontforJALocales()) {
                        sArr[i5] = remapLocaleMap(i2, i4, coreScripts[i5], sArr[i5]);
                    }
                    if (this.preferPropFonts) {
                        sArr[i5] = remapProportional(i2, sArr[i5]);
                    }
                    this.coreFontNameIDs.add(Short.valueOf(sArr[i5]));
                    i5++;
                }
                for (int i6 = 0; i6 < fallbackScripts.length; i6++) {
                    short componentFontID = getComponentFontID(fallbackScripts[i6], i2, i4);
                    if (this.preferLocaleFonts && localeMap != null && this.fontManager.usingAlternateFontforJALocales()) {
                        componentFontID = remapLocaleMap(i2, i4, fallbackScripts[i6], componentFontID);
                    }
                    if (this.preferPropFonts) {
                        componentFontID = remapProportional(i2, componentFontID);
                    }
                    if (!contains(sArr, componentFontID, i5)) {
                        this.fallbackFontNameIDs.add(Short.valueOf(componentFontID));
                        int i7 = i5;
                        i5++;
                        sArr[i7] = componentFontID;
                    }
                }
                if (i5 < sArr.length) {
                    short[] sArr2 = new short[i5];
                    System.arraycopy(sArr, 0, sArr2, 0, i5);
                    sArr = sArr2;
                }
                this.compFontNameIDs[i2][i4] = sArr;
            }
        }
    }

    private short remapLocaleMap(int i2, int i3, short s2, short s3) {
        String string = getString(table_scriptIDs[s2]);
        String str = (String) localeMap.get(string);
        if (str == null) {
            str = (String) localeMap.get(fontNames[i2] + "." + styleNames[i3] + "." + string);
        }
        if (str == null) {
            return s3;
        }
        int i4 = 0;
        while (true) {
            if (i4 >= table_componentFontNameIDs.length) {
                break;
            }
            if (!str.equalsIgnoreCase(getString(table_componentFontNameIDs[i4]))) {
                i4++;
            } else {
                s3 = (short) i4;
                break;
            }
        }
        return s3;
    }

    public static boolean hasMonoToPropMap() {
        return (table_proportionals == null || table_proportionals.length == 0) ? false : true;
    }

    private short remapProportional(int i2, short s2) {
        if (this.preferPropFonts && table_proportionals.length != 0 && i2 != 2 && i2 != 4) {
            for (int i3 = 0; i3 < table_proportionals.length; i3 += 2) {
                if (table_proportionals[i3] == s2) {
                    return table_proportionals[i3 + 1];
                }
            }
        }
        return s2;
    }

    public static boolean isLogicalFontFamilyName(String str) {
        return isLogicalFontFamilyNameLC(str.toLowerCase(Locale.ENGLISH));
    }

    public static boolean isLogicalFontFamilyNameLC(String str) {
        for (int i2 = 0; i2 < fontNames.length; i2++) {
            if (str.equals(fontNames[i2])) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLogicalFontStyleName(String str) {
        for (int i2 = 0; i2 < styleNames.length; i2++) {
            if (str.equals(styleNames[i2])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLogicalFontFaceName(String str) {
        return isLogicalFontFaceNameLC(str.toLowerCase(Locale.ENGLISH));
    }

    public static boolean isLogicalFontFaceNameLC(String str) {
        int iIndexOf = str.indexOf(46);
        if (iIndexOf >= 0) {
            return isLogicalFontFamilyName(str.substring(0, iIndexOf)) && isLogicalFontStyleName(str.substring(iIndexOf + 1));
        }
        return isLogicalFontFamilyName(str);
    }

    protected static int getFontIndex(String str) {
        return getArrayIndex(fontNames, str);
    }

    protected static int getStyleIndex(String str) {
        return getArrayIndex(styleNames, str);
    }

    private static int getArrayIndex(String[] strArr, String str) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (str.equals(strArr[i2])) {
                return i2;
            }
        }
        if ($assertionsDisabled) {
            return 0;
        }
        throw new AssertionError();
    }

    protected static int getStyleIndex(int i2) {
        switch (i2) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    protected static String getFontName(int i2) {
        return fontNames[i2];
    }

    protected static String getStyleName(int i2) {
        return styleNames[i2];
    }

    public static String getLogicalFontFaceName(String str, int i2) {
        if ($assertionsDisabled || isLogicalFontFamilyName(str)) {
            return str.toLowerCase(Locale.ENGLISH) + "." + getStyleString(i2);
        }
        throw new AssertionError();
    }

    public static String getStyleString(int i2) {
        return getStyleName(getStyleIndex(i2));
    }

    protected String getCompatibilityFamilyName(String str) {
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        if (lowerCase.equals("timesroman")) {
            return "serif";
        }
        if (lowerCase.equals("helvetica")) {
            return "sansserif";
        }
        if (lowerCase.equals("courier")) {
            return "monospaced";
        }
        return null;
    }

    protected String mapFileName(String str) {
        return str;
    }

    private void shuffle(String[] strArr, int i2, int i3) {
        if (i3 >= i2) {
            return;
        }
        String str = strArr[i2];
        for (int i4 = i2; i4 > i3; i4--) {
            strArr[i4] = strArr[i4 - 1];
        }
        strArr[i3] = str;
    }

    public static boolean willReorderForStartupLocale() {
        return getReorderSequence() != null;
    }

    private static Object getReorderSequence() {
        if (fontConfig.reorderMap == null) {
            fontConfig.initReorderMap();
        }
        HashMap map = fontConfig.reorderMap;
        String language = startupLocale.getLanguage();
        Object obj = map.get(encoding + "." + language + "." + startupLocale.getCountry());
        if (obj == null) {
            obj = map.get(encoding + "." + language);
        }
        if (obj == null) {
            obj = map.get(encoding);
        }
        return obj;
    }

    private void reorderSequenceForLocale(String[] strArr) {
        Object reorderSequence = getReorderSequence();
        if (reorderSequence instanceof String) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (strArr[i2].equals(reorderSequence)) {
                    shuffle(strArr, i2, 0);
                    return;
                }
            }
            return;
        }
        if (reorderSequence instanceof String[]) {
            String[] strArr2 = (String[]) reorderSequence;
            for (int i3 = 0; i3 < strArr2.length; i3++) {
                for (int i4 = 0; i4 < strArr.length; i4++) {
                    if (strArr[i4].equals(strArr2[i3])) {
                        shuffle(strArr, i4, i3);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Vector splitSequence(String str) {
        int i2;
        Vector vector = new Vector();
        int i3 = 0;
        while (true) {
            i2 = i3;
            int iIndexOf = str.indexOf(44, i2);
            if (iIndexOf < 0) {
                break;
            }
            vector.add(str.substring(i2, iIndexOf));
            i3 = iIndexOf + 1;
        }
        if (str.length() > i2) {
            vector.add(str.substring(i2, str.length()));
        }
        return vector;
    }

    protected String[] split(String str) {
        return (String[]) splitSequence(str).toArray(new String[0]);
    }

    public FontDescriptor[] getFontDescriptors(String str, int i2) {
        if ($assertionsDisabled || isLogicalFontFamilyName(str)) {
            return getFontDescriptors(getFontIndex(str.toLowerCase(Locale.ENGLISH)), getStyleIndex(i2));
        }
        throw new AssertionError();
    }

    private FontDescriptor[] getFontDescriptors(int i2, int i3) {
        FontDescriptor[] fontDescriptorArrBuildFontDescriptors = this.fontDescriptors[i2][i3];
        if (fontDescriptorArrBuildFontDescriptors == null) {
            fontDescriptorArrBuildFontDescriptors = buildFontDescriptors(i2, i3);
            this.fontDescriptors[i2][i3] = fontDescriptorArrBuildFontDescriptors;
        }
        return fontDescriptorArrBuildFontDescriptors;
    }

    protected FontDescriptor[] buildFontDescriptors(int i2, int i3) {
        String str = fontNames[i2];
        String str2 = styleNames[i3];
        short[] coreScripts = getCoreScripts(i2);
        short[] sArr = this.compFontNameIDs[i2][i3];
        String[] strArr = new String[coreScripts.length];
        String[] strArr2 = new String[coreScripts.length];
        for (int i4 = 0; i4 < strArr.length; i4++) {
            strArr2[i4] = getComponentFontName(sArr[i4]);
            strArr[i4] = getScriptName(coreScripts[i4]);
            if (this.alphabeticSuffix != null && Constants.ATTRVAL_ALPHABETIC.equals(strArr[i4])) {
                strArr[i4] = strArr[i4] + "/" + this.alphabeticSuffix;
            }
        }
        int[][] iArr = this.compExclusions[i2];
        FontDescriptor[] fontDescriptorArr = new FontDescriptor[strArr2.length];
        for (int i5 = 0; i5 < strArr2.length; i5++) {
            String strMakeAWTFontName = makeAWTFontName(strArr2[i5], strArr[i5]);
            String encoding2 = getEncoding(strArr2[i5], strArr[i5]);
            if (encoding2 == null) {
                encoding2 = "default";
            }
            fontDescriptorArr[i5] = new FontDescriptor(strMakeAWTFontName, getFontCharsetEncoder(encoding2.trim(), strMakeAWTFontName), iArr[i5]);
        }
        return fontDescriptorArr;
    }

    protected String makeAWTFontName(String str, String str2) {
        return str;
    }

    private CharsetEncoder getFontCharsetEncoder(final String str, String str2) {
        Charset defaultFontCharset;
        if (str.equals("default")) {
            defaultFontCharset = (Charset) this.charsetRegistry.get(str2);
        } else {
            defaultFontCharset = (Charset) this.charsetRegistry.get(str);
        }
        if (defaultFontCharset != null) {
            return defaultFontCharset.newEncoder();
        }
        if (!str.startsWith("sun.awt.") && !str.equals("default")) {
            defaultFontCharset = Charset.forName(str);
        } else {
            Class cls = (Class) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.FontConfiguration.2
                @Override // java.security.PrivilegedAction
                public Object run() {
                    try {
                        return Class.forName(str, true, ClassLoader.getSystemClassLoader());
                    } catch (ClassNotFoundException e2) {
                        return null;
                    }
                }
            });
            if (cls != null) {
                try {
                    defaultFontCharset = (Charset) cls.newInstance();
                } catch (Exception e2) {
                }
            }
        }
        if (defaultFontCharset == null) {
            defaultFontCharset = getDefaultFontCharset(str2);
        }
        if (str.equals("default")) {
            this.charsetRegistry.put(str2, defaultFontCharset);
        } else {
            this.charsetRegistry.put(str, defaultFontCharset);
        }
        return defaultFontCharset.newEncoder();
    }

    public HashSet<String> getAWTFontPathSet() {
        return null;
    }

    public CompositeFontDescriptor[] get2DCompositeFontInfo() {
        CompositeFontDescriptor[] compositeFontDescriptorArr = new CompositeFontDescriptor[20];
        String defaultFontFile = this.fontManager.getDefaultFontFile();
        String defaultFontFaceName = this.fontManager.getDefaultFontFaceName();
        for (int i2 = 0; i2 < 5; i2++) {
            String str = publicFontNames[i2];
            int[][] iArr = this.compExclusions[i2];
            int length = 0;
            for (int[] iArr2 : iArr) {
                length += iArr2.length;
            }
            int[] iArr3 = new int[length];
            int[] iArr4 = new int[iArr.length];
            int i3 = 0;
            for (int i4 = 0; i4 < iArr.length; i4++) {
                int[] iArr5 = iArr[i4];
                int i5 = 0;
                while (i5 < iArr5.length) {
                    int i6 = iArr5[i5];
                    int i7 = i3;
                    int i8 = i3 + 1;
                    int i9 = i5;
                    int i10 = i5 + 1;
                    iArr3[i7] = iArr5[i9];
                    i3 = i8 + 1;
                    i5 = i10 + 1;
                    iArr3[i8] = iArr5[i10];
                }
                iArr4[i4] = i3;
            }
            for (int i11 = 0; i11 < 4; i11++) {
                int length2 = this.compFontNameIDs[i2][i11].length;
                boolean z2 = false;
                if (installedFallbackFontFiles != null) {
                    length2 += installedFallbackFontFiles.length;
                }
                String str2 = str + "." + styleNames[i11];
                String[] strArr = new String[length2];
                String[] strArr2 = new String[length2];
                int i12 = 0;
                while (i12 < this.compFontNameIDs[i2][i11].length) {
                    short s2 = this.compFontNameIDs[i2][i11][i12];
                    short componentFileID = getComponentFileID(s2);
                    strArr[i12] = getFaceNameFromComponentFontName(getComponentFontName(s2));
                    strArr2[i12] = mapFileName(getComponentFileName(componentFileID));
                    if (strArr2[i12] == null || needToSearchForFile(strArr2[i12])) {
                        strArr2[i12] = getFileNameFromComponentFontName(getComponentFontName(s2));
                    }
                    if (!z2 && defaultFontFile.equals(strArr2[i12])) {
                        z2 = true;
                    }
                    i12++;
                }
                if (!z2) {
                    int length3 = 0;
                    if (installedFallbackFontFiles != null) {
                        length3 = installedFallbackFontFiles.length;
                    }
                    if (i12 + length3 == length2) {
                        String[] strArr3 = new String[length2 + 1];
                        System.arraycopy(strArr, 0, strArr3, 0, i12);
                        strArr = strArr3;
                        String[] strArr4 = new String[length2 + 1];
                        System.arraycopy(strArr2, 0, strArr4, 0, i12);
                        strArr2 = strArr4;
                    }
                    strArr[i12] = defaultFontFaceName;
                    strArr2[i12] = defaultFontFile;
                    i12++;
                }
                if (installedFallbackFontFiles != null) {
                    for (int i13 = 0; i13 < installedFallbackFontFiles.length; i13++) {
                        strArr[i12] = null;
                        strArr2[i12] = installedFallbackFontFiles[i13];
                        i12++;
                    }
                }
                if (i12 < length2) {
                    String[] strArr5 = new String[i12];
                    System.arraycopy(strArr, 0, strArr5, 0, i12);
                    strArr = strArr5;
                    String[] strArr6 = new String[i12];
                    System.arraycopy(strArr2, 0, strArr6, 0, i12);
                    strArr2 = strArr6;
                }
                int[] iArr6 = iArr4;
                if (i12 != iArr6.length) {
                    int length4 = iArr4.length;
                    iArr6 = new int[i12];
                    System.arraycopy(iArr4, 0, iArr6, 0, length4);
                    for (int i14 = length4; i14 < i12; i14++) {
                        iArr6[i14] = iArr3.length;
                    }
                }
                compositeFontDescriptorArr[(i2 * 4) + i11] = new CompositeFontDescriptor(str2, this.compCoreNum[i2], strArr, strArr2, iArr3, iArr6);
            }
        }
        return compositeFontDescriptorArr;
    }

    public boolean needToSearchForFile(String str) {
        if (!FontUtilities.isLinux) {
            return false;
        }
        if (this.existsMap == null) {
            this.existsMap = new HashMap<>();
        }
        Boolean boolValueOf = this.existsMap.get(str);
        if (boolValueOf == null) {
            getNumberCoreFonts();
            if (!this.coreFontFileNames.contains(str)) {
                boolValueOf = Boolean.TRUE;
            } else {
                boolValueOf = Boolean.valueOf(new File(str).exists());
                this.existsMap.put(str, boolValueOf);
                if (FontUtilities.debugFonts() && boolValueOf == Boolean.FALSE) {
                    logger.warning("Couldn't locate font file " + str);
                }
            }
        }
        return boolValueOf == Boolean.FALSE;
    }

    public int getNumberCoreFonts() {
        if (this.numCoreFonts == -1) {
            this.numCoreFonts = this.coreFontNameIDs.size();
            Short[] shArr = new Short[0];
            Short[] shArr2 = (Short[]) this.coreFontNameIDs.toArray(shArr);
            Short[] shArr3 = (Short[]) this.fallbackFontNameIDs.toArray(shArr);
            int i2 = 0;
            for (int i3 = 0; i3 < shArr3.length; i3++) {
                if (this.coreFontNameIDs.contains(shArr3[i3])) {
                    shArr3[i3] = null;
                } else {
                    i2++;
                }
            }
            this.componentFonts = new String[this.numCoreFonts + i2];
            int i4 = 0;
            while (i4 < shArr2.length) {
                short sShortValue = shArr2[i4].shortValue();
                short componentFileID = getComponentFileID(sShortValue);
                this.componentFonts[i4] = getComponentFontName(sShortValue);
                String componentFileName = getComponentFileName(componentFileID);
                if (componentFileName != null) {
                    this.coreFontFileNames.add(componentFileName);
                }
                this.filenamesMap.put(this.componentFonts[i4], mapFileName(componentFileName));
                i4++;
            }
            for (int i5 = 0; i5 < shArr3.length; i5++) {
                if (shArr3[i5] != null) {
                    short sShortValue2 = shArr3[i5].shortValue();
                    short componentFileID2 = getComponentFileID(sShortValue2);
                    this.componentFonts[i4] = getComponentFontName(sShortValue2);
                    this.filenamesMap.put(this.componentFonts[i4], mapFileName(getComponentFileName(componentFileID2)));
                    i4++;
                }
            }
        }
        return this.numCoreFonts;
    }

    public String[] getPlatformFontNames() {
        if (this.numCoreFonts == -1) {
            getNumberCoreFonts();
        }
        return this.componentFonts;
    }

    public String getFileNameFromPlatformName(String str) {
        return this.filenamesMap.get(str);
    }

    public String getExtraFontPath() {
        return getString(head[16]);
    }

    public String getVersion() {
        return getString(head[17]);
    }

    public boolean useSystemFontConfig() {
        if (head == null) {
            return true;
        }
        return Boolean.parseBoolean(getString(head[18]));
    }

    protected static FontConfiguration getFontConfiguration() {
        return fontConfig;
    }

    protected void setFontConfiguration() {
        fontConfig = this;
    }

    private static void sanityCheck() {
        int i2 = 0;
        String str = (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.FontConfiguration.3
            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperty("os.name");
            }
        });
        for (int i3 = 1; i3 < table_filenames.length; i3++) {
            if (table_filenames[i3] == -1) {
                if (str.contains("Windows")) {
                    System.err.println("\n Error: <filename." + getString(table_componentFontNameIDs[i3]) + "> entry is missing!!!");
                    i2++;
                } else if (verbose && !isEmpty(table_filenames)) {
                    System.err.println("\n Note: 'filename' entry is undefined for \"" + getString(table_componentFontNameIDs[i3]) + PdfOps.DOUBLE_QUOTE__TOKEN);
                }
            }
        }
        for (int i4 = 0; i4 < table_scriptIDs.length; i4++) {
            short s2 = table_scriptFonts[i4];
            if (s2 == 0) {
                System.out.println("\n Error: <allfonts." + getString(table_scriptIDs[i4]) + "> entry is missing!!!");
                i2++;
            } else if (s2 < 0) {
                short s3 = (short) (-s2);
                for (int i5 = 0; i5 < 5; i5++) {
                    for (int i6 = 0; i6 < 4; i6++) {
                        if (table_scriptFonts[s3 + (i5 * 4) + i6] == 0) {
                            System.err.println("\n Error: <" + getFontName(i5) + "." + getStyleName(i6) + "." + getString(table_scriptIDs[i4]) + "> entry is missing!!!");
                            i2++;
                        }
                    }
                }
            }
        }
        if ("SunOS".equals(str)) {
            for (int i7 = 0; i7 < table_awtfontpaths.length; i7++) {
                if (table_awtfontpaths[i7] == 0) {
                    String string = getString(table_scriptIDs[i7]);
                    if (!string.contains("lucida") && !string.contains("dingbats") && !string.contains("symbol")) {
                        System.err.println("\nError: <awtfontpath." + string + "> entry is missing!!!");
                        i2++;
                    }
                }
            }
        }
        if (i2 != 0) {
            System.err.println("!!THERE ARE " + i2 + " ERROR(S) IN THE FONTCONFIG FILE, PLEASE CHECK ITS CONTENT!!\n");
            System.exit(1);
        }
    }

    private static boolean isEmpty(short[] sArr) {
        for (short s2 : sArr) {
            if (s2 != -1) {
                return false;
            }
        }
        return true;
    }

    private static void dump() {
        System.out.println("\n----Head Table------------");
        for (int i2 = 0; i2 < 20; i2++) {
            System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + i2 + " : " + ((int) head[i2]));
        }
        System.out.println("\n----scriptIDs-------------");
        printTable(table_scriptIDs, 0);
        System.out.println("\n----scriptFonts----------------");
        for (int i3 = 0; i3 < table_scriptIDs.length; i3++) {
            short s2 = table_scriptFonts[i3];
            if (s2 >= 0) {
                System.out.println("  allfonts." + getString(table_scriptIDs[i3]) + "=" + getString(table_componentFontNameIDs[s2]));
            }
        }
        for (int i4 = 0; i4 < table_scriptIDs.length; i4++) {
            short s3 = table_scriptFonts[i4];
            if (s3 < 0) {
                short s4 = (short) (-s3);
                for (int i5 = 0; i5 < 5; i5++) {
                    for (int i6 = 0; i6 < 4; i6++) {
                        System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + getFontName(i5) + "." + getStyleName(i6) + "." + getString(table_scriptIDs[i4]) + "=" + getString(table_componentFontNameIDs[table_scriptFonts[s4 + (i5 * 4) + i6]]));
                    }
                }
            }
        }
        System.out.println("\n----elcIDs----------------");
        printTable(table_elcIDs, 0);
        System.out.println("\n----sequences-------------");
        for (int i7 = 0; i7 < table_elcIDs.length; i7++) {
            System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + i7 + "/" + getString(table_elcIDs[i7]));
            for (short s5 : getShortArray(table_sequences[(i7 * 5) + 0])) {
                System.out.println("     " + getString(table_scriptIDs[s5]));
            }
        }
        System.out.println("\n----fontfileNameIDs-------");
        printTable(table_fontfileNameIDs, 0);
        System.out.println("\n----componentFontNameIDs--");
        printTable(table_componentFontNameIDs, 1);
        System.out.println("\n----filenames-------------");
        for (int i8 = 0; i8 < table_filenames.length; i8++) {
            if (table_filenames[i8] == -1) {
                System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + i8 + " : null");
            } else {
                System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + i8 + " : " + getString(table_fontfileNameIDs[table_filenames[i8]]));
            }
        }
        System.out.println("\n----awtfontpaths---------");
        for (int i9 = 0; i9 < table_awtfontpaths.length; i9++) {
            System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + getString(table_scriptIDs[i9]) + " : " + getString(table_awtfontpaths[i9]));
        }
        System.out.println("\n----proportionals--------");
        int i10 = 0;
        while (i10 < table_proportionals.length) {
            int i11 = i10;
            int i12 = i10 + 1;
            System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + getString(table_componentFontNameIDs[table_proportionals[i11]]) + " -> " + getString(table_componentFontNameIDs[table_proportionals[i12]]));
            i10 = i12 + 1;
        }
        int i13 = 0;
        System.out.println("\n----alphabeticSuffix----");
        while (i13 < table_alphabeticSuffix.length) {
            int i14 = i13;
            int i15 = i13 + 1;
            i13 = i15 + 1;
            System.out.println("    " + getString(table_elcIDs[table_alphabeticSuffix[i14]]) + " -> " + getString(table_alphabeticSuffix[i15]));
        }
        System.out.println("\n----String Table---------");
        System.out.println("    stringID:    Num =" + table_stringIDs.length);
        System.out.println("    stringTable: Size=" + (table_stringTable.length * 2));
        System.out.println("\n----fallbackScriptIDs---");
        for (short s6 : getShortArray(head[15])) {
            System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + getString(table_scriptIDs[s6]));
        }
        System.out.println("\n----appendedfontpath-----");
        System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + getString(head[16]));
        System.out.println("\n----Version--------------");
        System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + getString(head[17]));
        System.out.println("\n----SystemFontConfig--------------");
        String string = getString(head[18]);
        System.out.println("  property: " + string + ", boolean: " + Boolean.parseBoolean(string));
    }

    protected static short getComponentFontID(short s2, int i2, int i3) {
        short s3 = table_scriptFonts[s2];
        if (s3 >= 0) {
            return s3;
        }
        return table_scriptFonts[(-s3) + (i2 * 4) + i3];
    }

    protected static short getComponentFontIDMotif(short s2, int i2, int i3) {
        if (table_scriptFontsMotif.length == 0) {
            return (short) 0;
        }
        short s3 = table_scriptFontsMotif[s2];
        if (s3 >= 0) {
            return s3;
        }
        return table_scriptFontsMotif[(-s3) + (i2 * 4) + i3];
    }

    private static int[] getExclusionRanges(short s2) {
        short s3 = table_exclusions[s2];
        if (s3 == 0) {
            return EMPTY_INT_ARRAY;
        }
        char[] charArray = getString(s3).toCharArray();
        int[] iArr = new int[charArray.length / 2];
        int i2 = 0;
        for (int i3 = 0; i3 < iArr.length; i3++) {
            int i4 = i2;
            int i5 = i2 + 1;
            i2 = i5 + 1;
            iArr[i3] = (charArray[i4] << 16) + (charArray[i5] & 65535);
        }
        return iArr;
    }

    private static boolean contains(short[] sArr, short s2, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (sArr[i3] == s2) {
                return true;
            }
        }
        return false;
    }

    protected static String getComponentFontName(short s2) {
        if (s2 < 0) {
            return null;
        }
        return getString(table_componentFontNameIDs[s2]);
    }

    private static String getComponentFileName(short s2) {
        if (s2 < 0) {
            return null;
        }
        return getString(table_fontfileNameIDs[s2]);
    }

    private static short getComponentFileID(short s2) {
        return table_filenames[s2];
    }

    private static String getScriptName(short s2) {
        return getString(table_scriptIDs[s2]);
    }

    protected short[] getCoreScripts(int i2) {
        short[] shortArray = getShortArray(table_sequences[(getInitELC() * 5) + i2]);
        if (this.preferLocaleFonts) {
            if (this.reorderScripts == null) {
                this.reorderScripts = new HashMap<>();
            }
            String[] strArr = new String[shortArray.length];
            for (int i3 = 0; i3 < strArr.length; i3++) {
                strArr[i3] = getScriptName(shortArray[i3]);
                this.reorderScripts.put(strArr[i3], Short.valueOf(shortArray[i3]));
            }
            reorderSequenceForLocale(strArr);
            for (int i4 = 0; i4 < strArr.length; i4++) {
                shortArray[i4] = this.reorderScripts.get(strArr[i4]).shortValue();
            }
        }
        return shortArray;
    }

    private static short[] getFallbackScripts() {
        return getShortArray(head[15]);
    }

    private static void printTable(short[] sArr, int i2) {
        for (int i3 = i2; i3 < sArr.length; i3++) {
            System.out.println(sun.security.pkcs11.wrapper.Constants.INDENT + i3 + " : " + getString(sArr[i3]));
        }
    }

    private static short[] readShortTable(DataInputStream dataInputStream, int i2) throws IOException {
        if (i2 == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] sArr = new short[i2];
        byte[] bArr = new byte[i2 * 2];
        dataInputStream.read(bArr);
        int i3 = 0;
        int i4 = 0;
        while (i3 < i2) {
            int i5 = i3;
            i3++;
            int i6 = i4;
            int i7 = i4 + 1;
            i4 = i7 + 1;
            sArr[i5] = (short) ((bArr[i6] << 8) | (bArr[i7] & 255));
        }
        return sArr;
    }

    private static void writeShortTable(DataOutputStream dataOutputStream, short[] sArr) throws IOException {
        for (short s2 : sArr) {
            dataOutputStream.writeShort(s2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static short[] toList(HashMap<String, Short> map) {
        short[] sArr = new short[map.size()];
        Arrays.fill(sArr, (short) -1);
        for (Map.Entry<String, Short> entry : map.entrySet()) {
            sArr[entry.getValue().shortValue()] = getStringID(entry.getKey());
        }
        return sArr;
    }

    protected static String getString(short s2) {
        if (s2 == 0) {
            return null;
        }
        if (stringCache[s2] == null) {
            stringCache[s2] = new String(table_stringTable, (int) table_stringIDs[s2], table_stringIDs[s2 + 1] - table_stringIDs[s2]);
        }
        return stringCache[s2];
    }

    private static short[] getShortArray(short s2) {
        char[] charArray = getString(s2).toCharArray();
        short[] sArr = new short[charArray.length];
        for (int i2 = 0; i2 < charArray.length; i2++) {
            sArr[i2] = (short) (charArray[i2] & 65535);
        }
        return sArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static short getStringID(String str) {
        if (str == null) {
            return (short) 0;
        }
        short length = (short) stringTable.length();
        stringTable.append(str);
        short length2 = (short) stringTable.length();
        stringIDs[stringIDNum] = length;
        stringIDs[stringIDNum + 1] = length2;
        stringIDNum = (short) (stringIDNum + 1);
        if (stringIDNum + 1 >= stringIDs.length) {
            short[] sArr = new short[stringIDNum + 1000];
            System.arraycopy(stringIDs, 0, sArr, 0, stringIDNum);
            stringIDs = sArr;
        }
        return (short) (stringIDNum - 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static short getShortArrayID(short[] sArr) {
        char[] cArr = new char[sArr.length];
        for (int i2 = 0; i2 < sArr.length; i2++) {
            cArr[i2] = (char) sArr[i2];
        }
        return getStringID(new String(cArr));
    }

    /* loaded from: rt.jar:sun/awt/FontConfiguration$PropertiesHandler.class */
    static class PropertiesHandler {
        private HashMap<String, Short> scriptIDs;
        private HashMap<String, Short> elcIDs;
        private HashMap<String, Short> componentFontNameIDs;
        private HashMap<String, Short> fontfileNameIDs;
        private HashMap<String, Integer> logicalFontIDs;
        private HashMap<String, Integer> fontStyleIDs;
        private HashMap<Short, Short> filenames;
        private HashMap<Short, short[]> sequences;
        private HashMap<Short, Short[]> scriptFonts;
        private HashMap<Short, Short> scriptAllfonts;
        private HashMap<Short, int[]> exclusions;
        private HashMap<Short, Short> awtfontpaths;
        private HashMap<Short, Short> proportionals;
        private HashMap<Short, Short> scriptAllfontsMotif;
        private HashMap<Short, Short[]> scriptFontsMotif;
        private HashMap<Short, Short> alphabeticSuffix;
        private short[] fallbackScriptIDs;
        private String version;
        private String preferSystemFontConf;
        private String appendedfontpath;

        PropertiesHandler() {
        }

        public void load(InputStream inputStream) throws IOException {
            initLogicalNameStyle();
            initHashMaps();
            new FontProperties().load(inputStream);
            initBinaryTable();
        }

        private void initBinaryTable() {
            short[] unused = FontConfiguration.head = new short[20];
            FontConfiguration.head[0] = 20;
            short[] unused2 = FontConfiguration.table_scriptIDs = FontConfiguration.toList(this.scriptIDs);
            FontConfiguration.head[1] = (short) (FontConfiguration.head[0] + FontConfiguration.table_scriptIDs.length);
            short[] unused3 = FontConfiguration.table_scriptFonts = new short[FontConfiguration.table_scriptIDs.length + (this.scriptFonts.size() * 20)];
            for (Map.Entry<Short, Short> entry : this.scriptAllfonts.entrySet()) {
                FontConfiguration.table_scriptFonts[entry.getKey().intValue()] = entry.getValue().shortValue();
            }
            int length = FontConfiguration.table_scriptIDs.length;
            for (Map.Entry<Short, Short[]> entry2 : this.scriptFonts.entrySet()) {
                FontConfiguration.table_scriptFonts[entry2.getKey().intValue()] = (short) (-length);
                Short[] value = entry2.getValue();
                for (int i2 = 0; i2 < 20; i2++) {
                    if (value[i2] != null) {
                        int i3 = length;
                        length++;
                        FontConfiguration.table_scriptFonts[i3] = value[i2].shortValue();
                    } else {
                        int i4 = length;
                        length++;
                        FontConfiguration.table_scriptFonts[i4] = 0;
                    }
                }
            }
            FontConfiguration.head[2] = (short) (FontConfiguration.head[1] + FontConfiguration.table_scriptFonts.length);
            short[] unused4 = FontConfiguration.table_elcIDs = FontConfiguration.toList(this.elcIDs);
            FontConfiguration.head[3] = (short) (FontConfiguration.head[2] + FontConfiguration.table_elcIDs.length);
            short[] unused5 = FontConfiguration.table_sequences = new short[this.elcIDs.size() * 5];
            for (Map.Entry<Short, short[]> entry3 : this.sequences.entrySet()) {
                int iIntValue = entry3.getKey().intValue();
                short[] value2 = entry3.getValue();
                if (value2.length == 1) {
                    for (int i5 = 0; i5 < 5; i5++) {
                        FontConfiguration.table_sequences[(iIntValue * 5) + i5] = value2[0];
                    }
                } else {
                    for (int i6 = 0; i6 < 5; i6++) {
                        FontConfiguration.table_sequences[(iIntValue * 5) + i6] = value2[i6];
                    }
                }
            }
            FontConfiguration.head[4] = (short) (FontConfiguration.head[3] + FontConfiguration.table_sequences.length);
            short[] unused6 = FontConfiguration.table_fontfileNameIDs = FontConfiguration.toList(this.fontfileNameIDs);
            FontConfiguration.head[5] = (short) (FontConfiguration.head[4] + FontConfiguration.table_fontfileNameIDs.length);
            short[] unused7 = FontConfiguration.table_componentFontNameIDs = FontConfiguration.toList(this.componentFontNameIDs);
            FontConfiguration.head[6] = (short) (FontConfiguration.head[5] + FontConfiguration.table_componentFontNameIDs.length);
            short[] unused8 = FontConfiguration.table_filenames = new short[FontConfiguration.table_componentFontNameIDs.length];
            Arrays.fill(FontConfiguration.table_filenames, (short) -1);
            for (Map.Entry<Short, Short> entry4 : this.filenames.entrySet()) {
                FontConfiguration.table_filenames[entry4.getKey().shortValue()] = entry4.getValue().shortValue();
            }
            FontConfiguration.head[7] = (short) (FontConfiguration.head[6] + FontConfiguration.table_filenames.length);
            FontConfiguration.table_awtfontpaths = new short[FontConfiguration.table_scriptIDs.length];
            for (Map.Entry<Short, Short> entry5 : this.awtfontpaths.entrySet()) {
                FontConfiguration.table_awtfontpaths[entry5.getKey().shortValue()] = entry5.getValue().shortValue();
            }
            FontConfiguration.head[8] = (short) (FontConfiguration.head[7] + FontConfiguration.table_awtfontpaths.length);
            short[] unused9 = FontConfiguration.table_exclusions = new short[this.scriptIDs.size()];
            for (Map.Entry<Short, int[]> entry6 : this.exclusions.entrySet()) {
                int[] value3 = entry6.getValue();
                char[] cArr = new char[value3.length * 2];
                int i7 = 0;
                for (int i8 = 0; i8 < value3.length; i8++) {
                    int i9 = i7;
                    int i10 = i7 + 1;
                    cArr[i9] = (char) (value3[i8] >> 16);
                    i7 = i10 + 1;
                    cArr[i10] = (char) (value3[i8] & 65535);
                }
                FontConfiguration.table_exclusions[entry6.getKey().shortValue()] = FontConfiguration.getStringID(new String(cArr));
            }
            FontConfiguration.head[9] = (short) (FontConfiguration.head[8] + FontConfiguration.table_exclusions.length);
            short[] unused10 = FontConfiguration.table_proportionals = new short[this.proportionals.size() * 2];
            int i11 = 0;
            for (Map.Entry<Short, Short> entry7 : this.proportionals.entrySet()) {
                int i12 = i11;
                int i13 = i11 + 1;
                FontConfiguration.table_proportionals[i12] = entry7.getKey().shortValue();
                i11 = i13 + 1;
                FontConfiguration.table_proportionals[i13] = entry7.getValue().shortValue();
            }
            FontConfiguration.head[10] = (short) (FontConfiguration.head[9] + FontConfiguration.table_proportionals.length);
            if (this.scriptAllfontsMotif.size() != 0 || this.scriptFontsMotif.size() != 0) {
                short[] unused11 = FontConfiguration.table_scriptFontsMotif = new short[FontConfiguration.table_scriptIDs.length + (this.scriptFontsMotif.size() * 20)];
                for (Map.Entry<Short, Short> entry8 : this.scriptAllfontsMotif.entrySet()) {
                    FontConfiguration.table_scriptFontsMotif[entry8.getKey().intValue()] = entry8.getValue().shortValue();
                }
                int length2 = FontConfiguration.table_scriptIDs.length;
                for (Map.Entry<Short, Short[]> entry9 : this.scriptFontsMotif.entrySet()) {
                    FontConfiguration.table_scriptFontsMotif[entry9.getKey().intValue()] = (short) (-length2);
                    Short[] value4 = entry9.getValue();
                    for (int i14 = 0; i14 < 20; i14++) {
                        if (value4[i14] != null) {
                            int i15 = length2;
                            length2++;
                            FontConfiguration.table_scriptFontsMotif[i15] = value4[i14].shortValue();
                        } else {
                            int i16 = length2;
                            length2++;
                            FontConfiguration.table_scriptFontsMotif[i16] = 0;
                        }
                    }
                }
            } else {
                short[] unused12 = FontConfiguration.table_scriptFontsMotif = FontConfiguration.EMPTY_SHORT_ARRAY;
            }
            FontConfiguration.head[11] = (short) (FontConfiguration.head[10] + FontConfiguration.table_scriptFontsMotif.length);
            short[] unused13 = FontConfiguration.table_alphabeticSuffix = new short[this.alphabeticSuffix.size() * 2];
            int i17 = 0;
            for (Map.Entry<Short, Short> entry10 : this.alphabeticSuffix.entrySet()) {
                int i18 = i17;
                int i19 = i17 + 1;
                FontConfiguration.table_alphabeticSuffix[i18] = entry10.getKey().shortValue();
                i17 = i19 + 1;
                FontConfiguration.table_alphabeticSuffix[i19] = entry10.getValue().shortValue();
            }
            FontConfiguration.head[15] = FontConfiguration.getShortArrayID(this.fallbackScriptIDs);
            FontConfiguration.head[16] = FontConfiguration.getStringID(this.appendedfontpath);
            FontConfiguration.head[17] = FontConfiguration.getStringID(this.version);
            FontConfiguration.head[18] = FontConfiguration.getStringID(this.preferSystemFontConf);
            FontConfiguration.head[12] = (short) (FontConfiguration.head[11] + FontConfiguration.table_alphabeticSuffix.length);
            short[] unused14 = FontConfiguration.table_stringIDs = new short[FontConfiguration.stringIDNum + 1];
            System.arraycopy(FontConfiguration.stringIDs, 0, FontConfiguration.table_stringIDs, 0, FontConfiguration.stringIDNum + 1);
            FontConfiguration.head[13] = (short) (FontConfiguration.head[12] + FontConfiguration.stringIDNum + 1);
            char[] unused15 = FontConfiguration.table_stringTable = FontConfiguration.stringTable.toString().toCharArray();
            FontConfiguration.head[14] = (short) (FontConfiguration.head[13] + FontConfiguration.stringTable.length());
            String[] unused16 = FontConfiguration.stringCache = new String[FontConfiguration.table_stringIDs.length];
        }

        private void initLogicalNameStyle() {
            this.logicalFontIDs = new HashMap<>();
            this.fontStyleIDs = new HashMap<>();
            this.logicalFontIDs.put("serif", 0);
            this.logicalFontIDs.put("sansserif", 1);
            this.logicalFontIDs.put("monospaced", 2);
            this.logicalFontIDs.put("dialog", 3);
            this.logicalFontIDs.put("dialoginput", 4);
            this.fontStyleIDs.put("plain", 0);
            this.fontStyleIDs.put("bold", 1);
            this.fontStyleIDs.put("italic", 2);
            this.fontStyleIDs.put("bolditalic", 3);
        }

        private void initHashMaps() {
            this.scriptIDs = new HashMap<>();
            this.elcIDs = new HashMap<>();
            this.componentFontNameIDs = new HashMap<>();
            this.componentFontNameIDs.put("", (short) 0);
            this.fontfileNameIDs = new HashMap<>();
            this.filenames = new HashMap<>();
            this.sequences = new HashMap<>();
            this.scriptFonts = new HashMap<>();
            this.scriptAllfonts = new HashMap<>();
            this.exclusions = new HashMap<>();
            this.awtfontpaths = new HashMap<>();
            this.proportionals = new HashMap<>();
            this.scriptFontsMotif = new HashMap<>();
            this.scriptAllfontsMotif = new HashMap<>();
            this.alphabeticSuffix = new HashMap<>();
            this.fallbackScriptIDs = FontConfiguration.EMPTY_SHORT_ARRAY;
        }

        private int[] parseExclusions(String str, String str2) throws Exception {
            if (str2 == null) {
                return FontConfiguration.EMPTY_INT_ARRAY;
            }
            int i2 = 1;
            int i3 = 0;
            while (true) {
                int iIndexOf = str2.indexOf(44, i3);
                if (iIndexOf == -1) {
                    break;
                }
                i2++;
                i3 = iIndexOf + 1;
            }
            int[] iArr = new int[i2 * 2];
            int i4 = 0;
            int i5 = 0;
            while (i5 < i2 * 2) {
                try {
                    int iIndexOf2 = str2.indexOf(45, i4);
                    String strSubstring = str2.substring(i4, iIndexOf2);
                    int i6 = iIndexOf2 + 1;
                    int iIndexOf3 = str2.indexOf(44, i6);
                    if (iIndexOf3 == -1) {
                        iIndexOf3 = str2.length();
                    }
                    String strSubstring2 = str2.substring(i6, iIndexOf3);
                    i4 = iIndexOf3 + 1;
                    int length = strSubstring.length();
                    int length2 = strSubstring2.length();
                    if ((length != 4 && length != 6) || (length2 != 4 && length2 != 6)) {
                        throw new Exception();
                    }
                    int i7 = Integer.parseInt(strSubstring, 16);
                    int i8 = Integer.parseInt(strSubstring2, 16);
                    if (i7 > i8) {
                        throw new Exception();
                    }
                    int i9 = i5;
                    int i10 = i5 + 1;
                    iArr[i9] = i7;
                    i5 = i10 + 1;
                    iArr[i10] = i8;
                } catch (Exception e2) {
                    if (FontUtilities.debugFonts() && FontConfiguration.logger != null) {
                        FontConfiguration.logger.config("Failed parsing " + str + " property of font configuration.");
                    }
                    return FontConfiguration.EMPTY_INT_ARRAY;
                }
            }
            return iArr;
        }

        private Short getID(HashMap<String, Short> map, String str) {
            Short sh = map.get(str);
            if (sh == null) {
                map.put(str, Short.valueOf((short) map.size()));
                return map.get(str);
            }
            return sh;
        }

        /* loaded from: rt.jar:sun/awt/FontConfiguration$PropertiesHandler$FontProperties.class */
        class FontProperties extends Properties {
            FontProperties() {
            }

            @Override // java.util.Hashtable, java.util.Dictionary
            public synchronized Object put(Object obj, Object obj2) {
                PropertiesHandler.this.parseProperty((String) obj, (String) obj2);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void parseProperty(String str, String str2) {
            Short[] shArr;
            Short id;
            short[] sArr;
            if ("systemfontconf".equals(str)) {
                this.preferSystemFontConf = str2;
                return;
            }
            if (str.startsWith("filename.")) {
                String strSubstring = str.substring(9);
                if (!"MingLiU_HKSCS".equals(strSubstring)) {
                    strSubstring = strSubstring.replace('_', ' ');
                }
                this.filenames.put(getID(this.componentFontNameIDs, strSubstring), getID(this.fontfileNameIDs, str2));
                return;
            }
            if (str.startsWith("exclusion.")) {
                String strSubstring2 = str.substring(10);
                this.exclusions.put(getID(this.scriptIDs, strSubstring2), parseExclusions(strSubstring2, str2));
                return;
            }
            if (str.startsWith("sequence.")) {
                String strSubstring3 = str.substring(9);
                boolean z2 = false;
                boolean z3 = false;
                String[] strArr = (String[]) FontConfiguration.splitSequence(str2).toArray(FontConfiguration.EMPTY_STRING_ARRAY);
                short[] sArr2 = new short[strArr.length];
                for (int i2 = 0; i2 < strArr.length; i2++) {
                    if ("alphabetic/default".equals(strArr[i2])) {
                        strArr[i2] = Constants.ATTRVAL_ALPHABETIC;
                        z2 = true;
                    } else if ("alphabetic/1252".equals(strArr[i2])) {
                        strArr[i2] = Constants.ATTRVAL_ALPHABETIC;
                        z3 = true;
                    }
                    sArr2[i2] = getID(this.scriptIDs, strArr[i2]).shortValue();
                }
                short shortArrayID = FontConfiguration.getShortArrayID(sArr2);
                int iIndexOf = strSubstring3.indexOf(46);
                if (iIndexOf == -1) {
                    if (Constants.ELEMNAME_FALLBACK_STRING.equals(strSubstring3)) {
                        this.fallbackScriptIDs = sArr2;
                        return;
                    } else {
                        if (!"allfonts".equals(strSubstring3)) {
                            if (FontConfiguration.logger != null) {
                                FontConfiguration.logger.config("Error sequence def: <sequence." + strSubstring3 + ">");
                                return;
                            }
                            return;
                        }
                        id = getID(this.elcIDs, "NULL.NULL.NULL");
                    }
                } else {
                    id = getID(this.elcIDs, strSubstring3.substring(iIndexOf + 1));
                    strSubstring3 = strSubstring3.substring(0, iIndexOf);
                }
                if ("allfonts".equals(strSubstring3)) {
                    sArr = new short[]{shortArrayID};
                } else {
                    sArr = this.sequences.get(id);
                    if (sArr == null) {
                        sArr = new short[5];
                    }
                    Integer num = this.logicalFontIDs.get(strSubstring3);
                    if (num == null) {
                        if (FontConfiguration.logger != null) {
                            FontConfiguration.logger.config("Unrecognizable logicfont name " + strSubstring3);
                            return;
                        }
                        return;
                    }
                    sArr[num.intValue()] = shortArrayID;
                }
                this.sequences.put(id, sArr);
                if (z2) {
                    this.alphabeticSuffix.put(id, Short.valueOf(FontConfiguration.getStringID("default")));
                    return;
                } else {
                    if (z3) {
                        this.alphabeticSuffix.put(id, Short.valueOf(FontConfiguration.getStringID("1252")));
                        return;
                    }
                    return;
                }
            }
            if (str.startsWith("allfonts.")) {
                String strSubstring4 = str.substring(9);
                if (strSubstring4.endsWith(".motif")) {
                    this.scriptAllfontsMotif.put(getID(this.scriptIDs, strSubstring4.substring(0, strSubstring4.length() - 6)), getID(this.componentFontNameIDs, str2));
                    return;
                } else {
                    this.scriptAllfonts.put(getID(this.scriptIDs, strSubstring4), getID(this.componentFontNameIDs, str2));
                    return;
                }
            }
            if (str.startsWith("awtfontpath.")) {
                this.awtfontpaths.put(getID(this.scriptIDs, str.substring(12)), Short.valueOf(FontConfiguration.getStringID(str2)));
                return;
            }
            if ("version".equals(str)) {
                this.version = str2;
                return;
            }
            if ("appendedfontpath".equals(str)) {
                this.appendedfontpath = str2;
                return;
            }
            if (str.startsWith("proportional.")) {
                this.proportionals.put(getID(this.componentFontNameIDs, str.substring(13).replace('_', ' ')), getID(this.componentFontNameIDs, str2));
                return;
            }
            boolean z4 = false;
            int iIndexOf2 = str.indexOf(46);
            if (iIndexOf2 == -1) {
                if (FontConfiguration.logger != null) {
                    FontConfiguration.logger.config("Failed parsing " + str + " property of font configuration.");
                    return;
                }
                return;
            }
            int iIndexOf3 = str.indexOf(46, iIndexOf2 + 1);
            if (iIndexOf3 == -1) {
                if (FontConfiguration.logger != null) {
                    FontConfiguration.logger.config("Failed parsing " + str + " property of font configuration.");
                    return;
                }
                return;
            }
            if (str.endsWith(".motif")) {
                str = str.substring(0, str.length() - 6);
                z4 = true;
            }
            Integer num2 = this.logicalFontIDs.get(str.substring(0, iIndexOf2));
            Integer num3 = this.fontStyleIDs.get(str.substring(iIndexOf2 + 1, iIndexOf3));
            Short id2 = getID(this.scriptIDs, str.substring(iIndexOf3 + 1));
            if (num2 == null || num3 == null) {
                if (FontConfiguration.logger != null) {
                    FontConfiguration.logger.config("unrecognizable logicfont name/style at " + str);
                    return;
                }
                return;
            }
            if (z4) {
                shArr = this.scriptFontsMotif.get(id2);
            } else {
                shArr = this.scriptFonts.get(id2);
            }
            if (shArr == null) {
                shArr = new Short[20];
            }
            shArr[(num2.intValue() * 4) + num3.intValue()] = getID(this.componentFontNameIDs, str2);
            if (z4) {
                this.scriptFontsMotif.put(id2, shArr);
            } else {
                this.scriptFonts.put(id2, shArr);
            }
        }
    }
}
