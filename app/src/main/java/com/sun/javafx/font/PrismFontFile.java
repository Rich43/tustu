package com.sun.javafx.font;

import com.sun.javafx.font.FontFileReader;
import com.sun.javafx.font.FontFileWriter;
import com.sun.javafx.fxml.BeanAdapter;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/javafx/font/PrismFontFile.class */
public abstract class PrismFontFile implements FontResource, FontConstants {
    String familyName;
    String fullName;
    String psName;
    String localeFamilyName;
    String localeFullName;
    String styleName;
    String localeStyleName;
    String filename;
    int filesize;
    FontFileReader filereader;
    short indexToLocFormat;
    int fontIndex;
    boolean isCFF;
    boolean isEmbedded;
    boolean isCopy;
    boolean isTracked;
    boolean isRegistered;
    private Object peer;
    int numTables;
    DirectoryEntry[] tableDirectory;
    private static final int fsSelectionItalicBit = 1;
    private static final int fsSelectionBoldBit = 32;
    private static final int MACSTYLE_BOLD_BIT = 1;
    private static final int MACSTYLE_ITALIC_BIT = 2;
    private boolean isBold;
    private boolean isItalic;
    private float upem;
    private float ascent;
    private float descent;
    private float linegap;
    private int numHMetrics;
    public static final int MAC_PLATFORM_ID = 1;
    public static final int MACROMAN_SPECIFIC_ID = 0;
    public static final int MACROMAN_ENGLISH_LANG = 0;
    public static final int MS_PLATFORM_ID = 3;
    public static final short MS_ENGLISH_LOCALE_ID = 1033;
    public static final int FAMILY_NAME_ID = 1;
    public static final int STYLE_NAME_ID = 2;
    public static final int FULL_NAME_ID = 4;
    public static final int PS_NAME_ID = 6;
    private static Map<String, Short> lcidMap;
    private float[] styleMetrics;
    static final int[] EMPTY_BOUNDS = new int[4];
    static short nameLocaleID = getSystemLCID();
    private int fontInstallationType = -1;
    int numGlyphs = -1;
    boolean isDecoded = false;
    Map<FontStrikeDesc, WeakReference<PrismFontStrike>> strikeMap = new ConcurrentHashMap();
    HashMap<Integer, int[]> bbCache = null;
    int directoryCount = 1;
    private OpenTypeGlyphMapper mapper = null;
    char[] advanceWidths = null;

    protected abstract PrismFontStrike createStrike(float f2, BaseTransform baseTransform, int i2, FontStrikeDesc fontStrikeDesc);

    protected abstract int[] createGlyphBoundingBox(int i2);

    protected PrismFontFile(String name, String filename, int fIndex, boolean register, boolean embedded, boolean copy, boolean tracked) throws Exception {
        this.isEmbedded = false;
        this.isCopy = false;
        this.isTracked = false;
        this.isRegistered = true;
        this.filename = filename;
        this.isRegistered = register;
        this.isEmbedded = embedded;
        this.isCopy = copy;
        this.isTracked = tracked;
        init(name, fIndex);
    }

    WeakReference<PrismFontFile> createFileDisposer(PrismFontFactory factory) {
        FileDisposer disposer = new FileDisposer(this.filename, this.isTracked);
        WeakReference<PrismFontFile> ref = Disposer.addRecord(this, disposer);
        disposer.setFactory(factory, ref);
        return ref;
    }

    void setIsDecoded(boolean decoded) {
        this.isDecoded = decoded;
    }

    protected synchronized void disposeOnShutdown() {
        if (this.isCopy || this.isDecoded) {
            AccessController.doPrivileged(() -> {
                try {
                    boolean delOK = new File(this.filename).delete();
                    if (!delOK && PrismFontFactory.debugFonts) {
                        System.err.println("Temp file not deleted : " + this.filename);
                    }
                    this.isDecoded = false;
                    this.isCopy = false;
                    return null;
                } catch (Exception e2) {
                    return null;
                }
            });
            if (PrismFontFactory.debugFonts) {
                System.err.println("Temp file deleted: " + this.filename);
            }
        }
    }

    @Override // com.sun.javafx.font.FontResource
    public int getDefaultAAMode() {
        return 0;
    }

    public boolean isInstalledFont() {
        if (this.fontInstallationType == -1) {
            PrismFontFactory factory = PrismFontFactory.getFontFactory();
            this.fontInstallationType = factory.isInstalledFont(this.filename) ? 1 : 0;
        }
        return this.fontInstallationType > 0;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/PrismFontFile$FileDisposer.class */
    static class FileDisposer implements DisposerRecord {
        String fileName;
        boolean isTracked;
        PrismFontFactory factory;
        WeakReference<PrismFontFile> refKey;

        public FileDisposer(String fileName, boolean isTracked) {
            this.fileName = fileName;
            this.isTracked = isTracked;
        }

        public void setFactory(PrismFontFactory factory, WeakReference<PrismFontFile> refKey) {
            this.factory = factory;
            this.refKey = refKey;
        }

        @Override // com.sun.javafx.font.DisposerRecord
        public synchronized void dispose() {
            if (this.fileName != null) {
                AccessController.doPrivileged(() -> {
                    try {
                        File file = new File(this.fileName);
                        int size = (int) file.length();
                        file.delete();
                        if (this.isTracked) {
                            FontFileWriter.FontTracker.getTracker().subBytes(size);
                        }
                        if (this.factory != null && this.refKey != null) {
                            Object o2 = this.refKey.get();
                            if (o2 == null) {
                                this.factory.removeTmpFont(this.refKey);
                                this.factory = null;
                                this.refKey = null;
                            }
                        }
                        if (PrismFontFactory.debugFonts) {
                            System.err.println("FileDisposer=" + this.fileName);
                        }
                        return null;
                    } catch (Exception e2) {
                        if (PrismFontFactory.debugFonts) {
                            e2.printStackTrace();
                            return null;
                        }
                        return null;
                    }
                });
                this.fileName = null;
            }
        }
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFileName() {
        return this.filename;
    }

    protected int getFileSize() {
        return this.filesize;
    }

    protected int getFontIndex() {
        return this.fontIndex;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFullName() {
        return this.fullName;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getPSName() {
        if (this.psName == null) {
            this.psName = this.fullName;
        }
        return this.psName;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFamilyName() {
        return this.familyName;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getStyleName() {
        return this.styleName;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleFullName() {
        return this.localeFullName;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleFamilyName() {
        return this.localeFamilyName;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleStyleName() {
        return this.localeStyleName;
    }

    @Override // com.sun.javafx.font.FontResource
    public int getFeatures() {
        return -1;
    }

    @Override // com.sun.javafx.font.FontResource
    public Map getStrikeMap() {
        return this.strikeMap;
    }

    @Override // com.sun.javafx.font.FontResource
    public FontStrike getStrike(float size, BaseTransform transform, int aaMode) {
        WeakReference<PrismFontStrike> ref;
        FontStrikeDesc desc = new FontStrikeDesc(size, transform, aaMode);
        WeakReference<PrismFontStrike> ref2 = this.strikeMap.get(desc);
        PrismFontStrike strike = null;
        if (ref2 != null) {
            strike = ref2.get();
        }
        if (strike == null) {
            strike = createStrike(size, transform, aaMode, desc);
            DisposerRecord disposer = strike.getDisposer();
            if (disposer != null) {
                ref = Disposer.addRecord(strike, disposer);
            } else {
                ref = new WeakReference<>(strike);
            }
            this.strikeMap.put(desc, ref);
        }
        return strike;
    }

    @Override // com.sun.javafx.font.FontResource
    public float[] getGlyphBoundingBox(int gc, float size, float[] retArr) {
        if (retArr == null || retArr.length < 4) {
            retArr = new float[4];
        }
        if (gc >= getNumGlyphs()) {
            retArr[3] = 0.0f;
            retArr[2] = 0.0f;
            retArr[1] = 0.0f;
            retArr[0] = 0.0f;
            return retArr;
        }
        if (this.bbCache == null) {
            this.bbCache = new HashMap<>();
        }
        int[] bb2 = this.bbCache.get(Integer.valueOf(gc));
        if (bb2 == null) {
            bb2 = createGlyphBoundingBox(gc);
            if (bb2 == null) {
                bb2 = EMPTY_BOUNDS;
            }
            this.bbCache.put(Integer.valueOf(gc), bb2);
        }
        float scale = size / getUnitsPerEm();
        retArr[0] = bb2[0] * scale;
        retArr[1] = bb2[1] * scale;
        retArr[2] = bb2[2] * scale;
        retArr[3] = bb2[3] * scale;
        return retArr;
    }

    int getNumGlyphs() {
        if (this.numGlyphs == -1) {
            FontFileReader.Buffer buffer = readTable(1835104368);
            this.numGlyphs = buffer.getChar(4);
        }
        return this.numGlyphs;
    }

    protected boolean isCFF() {
        return this.isCFF;
    }

    @Override // com.sun.javafx.font.FontResource
    public Object getPeer() {
        return this.peer;
    }

    @Override // com.sun.javafx.font.FontResource
    public void setPeer(Object peer) {
        this.peer = peer;
    }

    int getTableLength(int tag) {
        int len = 0;
        DirectoryEntry tagDE = getDirectoryEntry(tag);
        if (tagDE != null) {
            len = tagDE.length;
        }
        return len;
    }

    synchronized FontFileReader.Buffer readTable(int tag) {
        FontFileReader.Buffer buffer = null;
        boolean openedFile = false;
        try {
            try {
                openedFile = this.filereader.openFile();
                DirectoryEntry tagDE = getDirectoryEntry(tag);
                if (tagDE != null) {
                    buffer = this.filereader.readBlock(tagDE.offset, tagDE.length);
                }
                if (openedFile) {
                    try {
                        this.filereader.closeFile();
                    } catch (Exception e2) {
                    }
                }
            } catch (Throwable th) {
                if (openedFile) {
                    try {
                        this.filereader.closeFile();
                    } catch (Exception e3) {
                    }
                }
                throw th;
            }
        } catch (Exception e4) {
            if (PrismFontFactory.debugFonts) {
                e4.printStackTrace();
            }
            if (openedFile) {
                try {
                    this.filereader.closeFile();
                } catch (Exception e5) {
                }
            }
        }
        return buffer;
    }

    public int getFontCount() {
        return this.directoryCount;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/PrismFontFile$DirectoryEntry.class */
    static class DirectoryEntry {
        int tag;
        int offset;
        int length;

        DirectoryEntry() {
        }
    }

    DirectoryEntry getDirectoryEntry(int tag) {
        for (int i2 = 0; i2 < this.numTables; i2++) {
            if (this.tableDirectory[i2].tag == tag) {
                return this.tableDirectory[i2];
            }
        }
        return null;
    }

    private void init(String name, int fIndex) throws Exception {
        this.filereader = new FontFileReader(this.filename);
        WoffDecoder decoder = null;
        try {
            try {
                if (!this.filereader.openFile()) {
                    throw new FileNotFoundException("Unable to create FontResource for file " + this.filename);
                }
                FontFileReader.Buffer buffer = this.filereader.readBlock(0, 12);
                int sfntTag = buffer.getInt();
                if (sfntTag == 2001684038) {
                    decoder = new WoffDecoder();
                    File file = decoder.openFile();
                    decoder.decode(this.filereader);
                    decoder.closeFile();
                    this.filereader.closeFile();
                    this.filereader = new FontFileReader(file.getPath());
                    if (!this.filereader.openFile()) {
                        throw new FileNotFoundException("Unable to create FontResource for file " + this.filename);
                    }
                    buffer = this.filereader.readBlock(0, 12);
                    sfntTag = buffer.getInt();
                }
                this.filesize = (int) this.filereader.getLength();
                int headerOffset = 0;
                if (sfntTag == 1953784678) {
                    buffer.getInt();
                    this.directoryCount = buffer.getInt();
                    if (fIndex >= this.directoryCount) {
                        throw new Exception("Bad collection index");
                    }
                    this.fontIndex = fIndex;
                    headerOffset = this.filereader.readBlock(12 + (4 * fIndex), 4).getInt();
                    sfntTag = this.filereader.readBlock(headerOffset, 4).getInt();
                }
                switch (sfntTag) {
                    case 65536:
                    case 1953658213:
                        break;
                    case 1330926671:
                        this.isCFF = true;
                        break;
                    default:
                        throw new Exception("Unsupported sfnt " + this.filename);
                }
                this.numTables = this.filereader.readBlock(headerOffset + 4, 2).getShort();
                int directoryOffset = headerOffset + 12;
                FontFileReader.Buffer ibuffer = this.filereader.readBlock(directoryOffset, this.numTables * 16);
                this.tableDirectory = new DirectoryEntry[this.numTables];
                for (int i2 = 0; i2 < this.numTables; i2++) {
                    DirectoryEntry table = new DirectoryEntry();
                    this.tableDirectory[i2] = table;
                    table.tag = ibuffer.getInt();
                    ibuffer.skip(4);
                    table.offset = ibuffer.getInt();
                    table.length = ibuffer.getInt();
                    if (table.offset + table.length > this.filesize) {
                        throw new Exception("bad table, tag=" + table.tag);
                    }
                }
                DirectoryEntry headDE = getDirectoryEntry(1751474532);
                FontFileReader.Buffer headTable = this.filereader.readBlock(headDE.offset, headDE.length);
                this.upem = headTable.getShort(18) & 65535;
                if (16.0f > this.upem || this.upem > 16384.0f) {
                    this.upem = 2048.0f;
                }
                this.indexToLocFormat = headTable.getShort(50);
                if (this.indexToLocFormat < 0 || this.indexToLocFormat > 1) {
                    throw new Exception("Bad indexToLocFormat");
                }
                FontFileReader.Buffer hhea = readTable(1751672161);
                if (hhea == null) {
                    this.numHMetrics = -1;
                } else {
                    this.ascent = -hhea.getShort(4);
                    this.descent = -hhea.getShort(6);
                    this.linegap = hhea.getShort(8);
                    this.numHMetrics = hhea.getChar(34) & 65535;
                    int hmtxEntries = getTableLength(1752003704) >> 2;
                    if (this.numHMetrics > hmtxEntries) {
                        this.numHMetrics = hmtxEntries;
                    }
                }
                getNumGlyphs();
                setStyle();
                checkCMAP();
                initNames();
                if (this.familyName == null || this.fullName == null) {
                    String fontName = name != null ? name : "";
                    if (this.fullName == null) {
                        this.fullName = this.familyName != null ? this.familyName : fontName;
                    }
                    if (this.familyName == null) {
                        this.familyName = this.fullName != null ? this.fullName : fontName;
                    }
                    throw new Exception("Font name not found.");
                }
                if (decoder != null) {
                    this.isDecoded = true;
                    this.filename = this.filereader.getFilename();
                    PrismFontFactory.getFontFactory().addDecodedFont(this);
                }
            } catch (Exception e2) {
                if (0 != 0) {
                    decoder.deleteFile();
                }
                throw e2;
            }
        } finally {
            this.filereader.closeFile();
        }
    }

    private void setStyle() {
        DirectoryEntry os2_DE = getDirectoryEntry(1330851634);
        if (os2_DE != null) {
            FontFileReader.Buffer os_2Table = this.filereader.readBlock(os2_DE.offset, os2_DE.length);
            int fsSelection = os_2Table.getChar(62) & 65535;
            this.isItalic = (fsSelection & 1) != 0;
            this.isBold = (fsSelection & 32) != 0;
            return;
        }
        DirectoryEntry headDE = getDirectoryEntry(1751474532);
        FontFileReader.Buffer headTable = this.filereader.readBlock(headDE.offset, headDE.length);
        short macStyleBits = headTable.getShort(44);
        this.isItalic = (macStyleBits & 2) != 0;
        this.isBold = (macStyleBits & 1) != 0;
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isBold() {
        return this.isBold;
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isItalic() {
        return this.isItalic;
    }

    public boolean isDecoded() {
        return this.isDecoded;
    }

    public boolean isRegistered() {
        return this.isRegistered;
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isEmbeddedFont() {
        return this.isEmbedded;
    }

    public int getUnitsPerEm() {
        return (int) this.upem;
    }

    public short getIndexToLocFormat() {
        return this.indexToLocFormat;
    }

    public int getNumHMetrics() {
        return this.numHMetrics;
    }

    void initNames() throws Exception {
        String enc;
        String enc2;
        String enc3;
        String enc4;
        byte[] name = new byte[256];
        DirectoryEntry nameDE = getDirectoryEntry(1851878757);
        FontFileReader.Buffer buffer = this.filereader.readBlock(nameDE.offset, nameDE.length);
        buffer.skip(2);
        int numRecords = buffer.getShort();
        int stringPtr = buffer.getShort() & 65535;
        for (int i2 = 0; i2 < numRecords; i2++) {
            short platformID = buffer.getShort();
            if (platformID != 3 && platformID != 1) {
                buffer.skip(10);
            } else {
                short encodingID = buffer.getShort();
                if ((platformID == 3 && encodingID > 1) || (platformID == 1 && encodingID != 0)) {
                    buffer.skip(8);
                } else {
                    short langID = buffer.getShort();
                    if (platformID == 1 && langID != 0) {
                        buffer.skip(6);
                    } else {
                        short nameID = buffer.getShort();
                        int nameLen = buffer.getShort() & 65535;
                        int namePtr = (buffer.getShort() & 65535) + stringPtr;
                        switch (nameID) {
                            case 1:
                                if (this.familyName == null || langID == 1033 || langID == nameLocaleID) {
                                    buffer.get(namePtr, name, 0, nameLen);
                                    if (platformID == 1) {
                                        enc4 = "US-ASCII";
                                    } else {
                                        enc4 = FastInfosetSerializer.UTF_16BE;
                                    }
                                    String tmpName = new String(name, 0, nameLen, enc4);
                                    if (this.familyName == null || langID == 1033) {
                                        this.familyName = tmpName;
                                    }
                                    if (langID == nameLocaleID) {
                                        this.localeFamilyName = tmpName;
                                        break;
                                    }
                                }
                                break;
                            case 2:
                                if (this.styleName == null || langID == 1033 || langID == nameLocaleID) {
                                    buffer.get(namePtr, name, 0, nameLen);
                                    if (platformID == 1) {
                                        enc = "US-ASCII";
                                    } else {
                                        enc = FastInfosetSerializer.UTF_16BE;
                                    }
                                    String tmpName2 = new String(name, 0, nameLen, enc);
                                    if (this.styleName == null || langID == 1033) {
                                        this.styleName = tmpName2;
                                    }
                                    if (langID == nameLocaleID) {
                                        this.localeStyleName = tmpName2;
                                        break;
                                    }
                                }
                                break;
                            case 4:
                                if (this.fullName == null || langID == 1033 || langID == nameLocaleID) {
                                    buffer.get(namePtr, name, 0, nameLen);
                                    if (platformID == 1) {
                                        enc3 = "US-ASCII";
                                    } else {
                                        enc3 = FastInfosetSerializer.UTF_16BE;
                                    }
                                    String tmpName3 = new String(name, 0, nameLen, enc3);
                                    if (this.fullName == null || langID == 1033) {
                                        this.fullName = tmpName3;
                                    }
                                    if (langID == nameLocaleID) {
                                        this.localeFullName = tmpName3;
                                        break;
                                    }
                                }
                                break;
                            case 6:
                                if (this.psName == null) {
                                    buffer.get(namePtr, name, 0, nameLen);
                                    if (platformID == 1) {
                                        enc2 = "US-ASCII";
                                    } else {
                                        enc2 = FastInfosetSerializer.UTF_16BE;
                                    }
                                    this.psName = new String(name, 0, nameLen, enc2);
                                    break;
                                }
                                break;
                        }
                        if (this.localeFamilyName == null) {
                            this.localeFamilyName = this.familyName;
                        }
                        if (this.localeFullName == null) {
                            this.localeFullName = this.fullName;
                        }
                        if (this.localeStyleName == null) {
                            this.localeStyleName = this.styleName;
                        }
                    }
                }
            }
        }
    }

    private void checkCMAP() throws Exception {
        DirectoryEntry cmapDE = getDirectoryEntry(1668112752);
        if (cmapDE != null) {
            if (cmapDE.length < 4) {
                throw new Exception("Invalid cmap table length");
            }
            FontFileReader.Buffer cmapTableHeader = this.filereader.readBlock(cmapDE.offset, 4);
            cmapTableHeader.getShort();
            int numberSubTables = cmapTableHeader.getShort();
            int indexLength = numberSubTables * 8;
            if (numberSubTables <= 0 || cmapDE.length < indexLength + 4) {
                throw new Exception("Invalid cmap subtables count");
            }
            FontFileReader.Buffer cmapTableIndex = this.filereader.readBlock(cmapDE.offset + 4, indexLength);
            for (int i2 = 0; i2 < numberSubTables; i2++) {
                cmapTableIndex.getShort();
                cmapTableIndex.getShort();
                int offset = cmapTableIndex.getInt();
                if (offset < 0 || offset >= cmapDE.length) {
                    throw new Exception("Invalid cmap subtable offset");
                }
            }
        }
    }

    private static void addLCIDMapEntry(Map<String, Short> map, String key, short value) {
        map.put(key, Short.valueOf(value));
    }

    private static synchronized void createLCIDMap() {
        if (lcidMap != null) {
            return;
        }
        Map<String, Short> map = new HashMap<>(200);
        addLCIDMapEntry(map, "ar", (short) 1025);
        addLCIDMapEntry(map, "bg", (short) 1026);
        addLCIDMapEntry(map, "ca", (short) 1027);
        addLCIDMapEntry(map, "zh", (short) 1028);
        addLCIDMapEntry(map, PdfOps.cs_TOKEN, (short) 1029);
        addLCIDMapEntry(map, "da", (short) 1030);
        addLCIDMapEntry(map, "de", (short) 1031);
        addLCIDMapEntry(map, "el", (short) 1032);
        addLCIDMapEntry(map, "es", (short) 1034);
        addLCIDMapEntry(map, "fi", (short) 1035);
        addLCIDMapEntry(map, "fr", (short) 1036);
        addLCIDMapEntry(map, "iw", (short) 1037);
        addLCIDMapEntry(map, "hu", (short) 1038);
        addLCIDMapEntry(map, BeanAdapter.IS_PREFIX, (short) 1039);
        addLCIDMapEntry(map, "it", (short) 1040);
        addLCIDMapEntry(map, "ja", (short) 1041);
        addLCIDMapEntry(map, "ko", (short) 1042);
        addLCIDMapEntry(map, "nl", (short) 1043);
        addLCIDMapEntry(map, "no", (short) 1044);
        addLCIDMapEntry(map, "pl", (short) 1045);
        addLCIDMapEntry(map, "pt", (short) 1046);
        addLCIDMapEntry(map, "rm", (short) 1047);
        addLCIDMapEntry(map, "ro", (short) 1048);
        addLCIDMapEntry(map, "ru", (short) 1049);
        addLCIDMapEntry(map, "hr", (short) 1050);
        addLCIDMapEntry(map, "sk", (short) 1051);
        addLCIDMapEntry(map, "sq", (short) 1052);
        addLCIDMapEntry(map, "sv", (short) 1053);
        addLCIDMapEntry(map, "th", (short) 1054);
        addLCIDMapEntry(map, "tr", (short) 1055);
        addLCIDMapEntry(map, "ur", (short) 1056);
        addLCIDMapEntry(map, "in", (short) 1057);
        addLCIDMapEntry(map, "uk", (short) 1058);
        addLCIDMapEntry(map, "be", (short) 1059);
        addLCIDMapEntry(map, "sl", (short) 1060);
        addLCIDMapEntry(map, "et", (short) 1061);
        addLCIDMapEntry(map, "lv", (short) 1062);
        addLCIDMapEntry(map, "lt", (short) 1063);
        addLCIDMapEntry(map, "fa", (short) 1065);
        addLCIDMapEntry(map, "vi", (short) 1066);
        addLCIDMapEntry(map, "hy", (short) 1067);
        addLCIDMapEntry(map, "eu", (short) 1069);
        addLCIDMapEntry(map, "mk", (short) 1071);
        addLCIDMapEntry(map, "tn", (short) 1074);
        addLCIDMapEntry(map, "xh", (short) 1076);
        addLCIDMapEntry(map, "zu", (short) 1077);
        addLCIDMapEntry(map, "af", (short) 1078);
        addLCIDMapEntry(map, "ka", (short) 1079);
        addLCIDMapEntry(map, "fo", (short) 1080);
        addLCIDMapEntry(map, "hi", (short) 1081);
        addLCIDMapEntry(map, "mt", (short) 1082);
        addLCIDMapEntry(map, "se", (short) 1083);
        addLCIDMapEntry(map, "gd", (short) 1084);
        addLCIDMapEntry(map, "ms", (short) 1086);
        addLCIDMapEntry(map, "kk", (short) 1087);
        addLCIDMapEntry(map, "ky", (short) 1088);
        addLCIDMapEntry(map, "sw", (short) 1089);
        addLCIDMapEntry(map, "tt", (short) 1092);
        addLCIDMapEntry(map, "bn", (short) 1093);
        addLCIDMapEntry(map, "pa", (short) 1094);
        addLCIDMapEntry(map, "gu", (short) 1095);
        addLCIDMapEntry(map, "ta", (short) 1097);
        addLCIDMapEntry(map, "te", (short) 1098);
        addLCIDMapEntry(map, "kn", (short) 1099);
        addLCIDMapEntry(map, "ml", (short) 1100);
        addLCIDMapEntry(map, "mr", (short) 1102);
        addLCIDMapEntry(map, "sa", (short) 1103);
        addLCIDMapEntry(map, "mn", (short) 1104);
        addLCIDMapEntry(map, "cy", (short) 1106);
        addLCIDMapEntry(map, "gl", (short) 1110);
        addLCIDMapEntry(map, "dv", (short) 1125);
        addLCIDMapEntry(map, "qu", (short) 1131);
        addLCIDMapEntry(map, "mi", (short) 1153);
        addLCIDMapEntry(map, "ar_IQ", (short) 2049);
        addLCIDMapEntry(map, "zh_CN", (short) 2052);
        addLCIDMapEntry(map, "de_CH", (short) 2055);
        addLCIDMapEntry(map, "en_GB", (short) 2057);
        addLCIDMapEntry(map, "es_MX", (short) 2058);
        addLCIDMapEntry(map, "fr_BE", (short) 2060);
        addLCIDMapEntry(map, "it_CH", (short) 2064);
        addLCIDMapEntry(map, "nl_BE", (short) 2067);
        addLCIDMapEntry(map, "no_NO_NY", (short) 2068);
        addLCIDMapEntry(map, "pt_PT", (short) 2070);
        addLCIDMapEntry(map, "ro_MD", (short) 2072);
        addLCIDMapEntry(map, "ru_MD", (short) 2073);
        addLCIDMapEntry(map, "sr_CS", (short) 2074);
        addLCIDMapEntry(map, "sv_FI", (short) 2077);
        addLCIDMapEntry(map, "az_AZ", (short) 2092);
        addLCIDMapEntry(map, "se_SE", (short) 2107);
        addLCIDMapEntry(map, "ga_IE", (short) 2108);
        addLCIDMapEntry(map, "ms_BN", (short) 2110);
        addLCIDMapEntry(map, "uz_UZ", (short) 2115);
        addLCIDMapEntry(map, "qu_EC", (short) 2155);
        addLCIDMapEntry(map, "ar_EG", (short) 3073);
        addLCIDMapEntry(map, "zh_HK", (short) 3076);
        addLCIDMapEntry(map, "de_AT", (short) 3079);
        addLCIDMapEntry(map, "en_AU", (short) 3081);
        addLCIDMapEntry(map, "fr_CA", (short) 3084);
        addLCIDMapEntry(map, "sr_CS", (short) 3098);
        addLCIDMapEntry(map, "se_FI", (short) 3131);
        addLCIDMapEntry(map, "qu_PE", (short) 3179);
        addLCIDMapEntry(map, "ar_LY", (short) 4097);
        addLCIDMapEntry(map, "zh_SG", (short) 4100);
        addLCIDMapEntry(map, "de_LU", (short) 4103);
        addLCIDMapEntry(map, "en_CA", (short) 4105);
        addLCIDMapEntry(map, "es_GT", (short) 4106);
        addLCIDMapEntry(map, "fr_CH", (short) 4108);
        addLCIDMapEntry(map, "hr_BA", (short) 4122);
        addLCIDMapEntry(map, "ar_DZ", (short) 5121);
        addLCIDMapEntry(map, "zh_MO", (short) 5124);
        addLCIDMapEntry(map, "de_LI", (short) 5127);
        addLCIDMapEntry(map, "en_NZ", (short) 5129);
        addLCIDMapEntry(map, "es_CR", (short) 5130);
        addLCIDMapEntry(map, "fr_LU", (short) 5132);
        addLCIDMapEntry(map, "bs_BA", (short) 5146);
        addLCIDMapEntry(map, "ar_MA", (short) 6145);
        addLCIDMapEntry(map, "en_IE", (short) 6153);
        addLCIDMapEntry(map, "es_PA", (short) 6154);
        addLCIDMapEntry(map, "fr_MC", (short) 6156);
        addLCIDMapEntry(map, "sr_BA", (short) 6170);
        addLCIDMapEntry(map, "ar_TN", (short) 7169);
        addLCIDMapEntry(map, "en_ZA", (short) 7177);
        addLCIDMapEntry(map, "es_DO", (short) 7178);
        addLCIDMapEntry(map, "sr_BA", (short) 7194);
        addLCIDMapEntry(map, "ar_OM", (short) 8193);
        addLCIDMapEntry(map, "en_JM", (short) 8201);
        addLCIDMapEntry(map, "es_VE", (short) 8202);
        addLCIDMapEntry(map, "ar_YE", (short) 9217);
        addLCIDMapEntry(map, "es_CO", (short) 9226);
        addLCIDMapEntry(map, "ar_SY", (short) 10241);
        addLCIDMapEntry(map, "en_BZ", (short) 10249);
        addLCIDMapEntry(map, "es_PE", (short) 10250);
        addLCIDMapEntry(map, "ar_JO", (short) 11265);
        addLCIDMapEntry(map, "en_TT", (short) 11273);
        addLCIDMapEntry(map, "es_AR", (short) 11274);
        addLCIDMapEntry(map, "ar_LB", (short) 12289);
        addLCIDMapEntry(map, "en_ZW", (short) 12297);
        addLCIDMapEntry(map, "es_EC", (short) 12298);
        addLCIDMapEntry(map, "ar_KW", (short) 13313);
        addLCIDMapEntry(map, "en_PH", (short) 13321);
        addLCIDMapEntry(map, "es_CL", (short) 13322);
        addLCIDMapEntry(map, "ar_AE", (short) 14337);
        addLCIDMapEntry(map, "es_UY", (short) 14346);
        addLCIDMapEntry(map, "ar_BH", (short) 15361);
        addLCIDMapEntry(map, "es_PY", (short) 15370);
        addLCIDMapEntry(map, "ar_QA", (short) 16385);
        addLCIDMapEntry(map, "es_BO", (short) 16394);
        addLCIDMapEntry(map, "es_SV", (short) 17418);
        addLCIDMapEntry(map, "es_HN", (short) 18442);
        addLCIDMapEntry(map, "es_NI", (short) 19466);
        addLCIDMapEntry(map, "es_PR", (short) 20490);
        lcidMap = map;
    }

    private static short getLCIDFromLocale(Locale locale) {
        if (locale.equals(Locale.US) || locale.getLanguage().equals("en")) {
            return (short) 1033;
        }
        if (lcidMap == null) {
            createLCIDMap();
        }
        String string = locale.toString();
        while (true) {
            String key = string;
            if (!key.isEmpty()) {
                Short lcidObject = lcidMap.get(key);
                if (lcidObject != null) {
                    return lcidObject.shortValue();
                }
                int pos = key.lastIndexOf(95);
                if (pos < 1) {
                    return (short) 1033;
                }
                string = key.substring(0, pos);
            } else {
                return (short) 1033;
            }
        }
    }

    private static short getSystemLCID() {
        if (PrismFontFactory.isWindows) {
            return PrismFontFactory.getSystemLCID();
        }
        return getLCIDFromLocale(Locale.getDefault());
    }

    @Override // com.sun.javafx.font.FontResource
    public CharToGlyphMapper getGlyphMapper() {
        if (this.mapper == null) {
            this.mapper = new OpenTypeGlyphMapper(this);
        }
        return this.mapper;
    }

    @Override // com.sun.javafx.font.FontResource
    public FontStrike getStrike(float size, BaseTransform transform) {
        return getStrike(size, transform, getDefaultAAMode());
    }

    @Override // com.sun.javafx.font.FontResource
    public float getAdvance(int glyphCode, float ptSize) {
        char cadv;
        if (glyphCode == 65535) {
            return 0.0f;
        }
        if (this.advanceWidths == null && this.numHMetrics > 0) {
            synchronized (this) {
                FontFileReader.Buffer hmtx = readTable(1752003704);
                if (hmtx == null) {
                    this.numHMetrics = -1;
                    return 0.0f;
                }
                char[] aw2 = new char[this.numHMetrics];
                for (int i2 = 0; i2 < this.numHMetrics; i2++) {
                    aw2[i2] = hmtx.getChar(i2 * 4);
                }
                this.advanceWidths = aw2;
            }
        }
        if (this.numHMetrics > 0) {
            if (glyphCode < this.numHMetrics) {
                cadv = this.advanceWidths[glyphCode];
            } else {
                cadv = this.advanceWidths[this.numHMetrics - 1];
            }
            return ((cadv & 65535) * ptSize) / this.upem;
        }
        return 0.0f;
    }

    public PrismMetrics getFontMetrics(float ptSize) {
        return new PrismMetrics((this.ascent * ptSize) / this.upem, (this.descent * ptSize) / this.upem, (this.linegap * ptSize) / this.upem, this, ptSize);
    }

    float[] getStyleMetrics(float ptSize) {
        if (this.styleMetrics == null) {
            float[] smetrics = new float[9];
            FontFileReader.Buffer os_2 = readTable(1330851634);
            int length = os_2 != null ? os_2.capacity() : 0;
            if (length >= 30) {
                smetrics[5] = os_2.getShort(26) / this.upem;
                smetrics[6] = (-os_2.getShort(28)) / this.upem;
                if (smetrics[5] < 0.0f) {
                    smetrics[5] = 0.05f;
                }
                if (Math.abs(smetrics[6]) > 2.0f) {
                    smetrics[6] = -0.4f;
                }
            } else {
                smetrics[5] = 0.05f;
                smetrics[6] = -0.4f;
            }
            if (length >= 74) {
                smetrics[2] = (-os_2.getShort(68)) / this.upem;
                smetrics[3] = (-os_2.getShort(70)) / this.upem;
                smetrics[4] = os_2.getShort(72) / this.upem;
            } else {
                smetrics[2] = this.ascent / this.upem;
                smetrics[3] = this.descent / this.upem;
                smetrics[4] = this.linegap / this.upem;
            }
            if (length >= 90) {
                smetrics[0] = os_2.getShort(86) / this.upem;
                smetrics[1] = os_2.getShort(88);
                if (smetrics[1] / this.ascent < 0.5d) {
                    smetrics[1] = 0.0f;
                } else {
                    smetrics[1] = smetrics[1] / this.upem;
                }
            }
            if (smetrics[0] == 0.0f || smetrics[1] == 0.0f) {
                FontStrike strike = getStrike(ptSize, BaseTransform.IDENTITY_TRANSFORM);
                CharToGlyphMapper mapper = getGlyphMapper();
                int missingGlyph = mapper.getMissingGlyphCode();
                if (smetrics[0] == 0.0f) {
                    int gc = mapper.charToGlyph('x');
                    if (gc != missingGlyph) {
                        RectBounds fbds = strike.getGlyph(gc).getBBox();
                        smetrics[0] = fbds.getHeight() / ptSize;
                    } else {
                        smetrics[0] = ((-this.ascent) * 0.6f) / this.upem;
                    }
                }
                if (smetrics[1] == 0.0f) {
                    int gc2 = mapper.charToGlyph('H');
                    if (gc2 != missingGlyph) {
                        RectBounds fbds2 = strike.getGlyph(gc2).getBBox();
                        smetrics[1] = fbds2.getHeight() / ptSize;
                    } else {
                        smetrics[1] = ((-this.ascent) * 0.9f) / this.upem;
                    }
                }
            }
            FontFileReader.Buffer postTable = readTable(1886352244);
            if (postTable == null || postTable.capacity() < 12) {
                smetrics[8] = 0.1f;
                smetrics[7] = 0.05f;
            } else {
                smetrics[8] = (-postTable.getShort(8)) / this.upem;
                smetrics[7] = postTable.getShort(10) / this.upem;
                if (smetrics[7] < 0.0f) {
                    smetrics[7] = 0.05f;
                }
                if (Math.abs(smetrics[8]) > 2.0f) {
                    smetrics[8] = 0.1f;
                }
            }
            this.styleMetrics = smetrics;
        }
        float[] metrics = new float[9];
        for (int i2 = 0; i2 < 9; i2++) {
            metrics[i2] = this.styleMetrics[i2] * ptSize;
        }
        return metrics;
    }

    byte[] getTableBytes(int tag) {
        FontFileReader.Buffer buffer = readTable(tag);
        byte[] table = null;
        if (buffer != null) {
            table = new byte[buffer.capacity()];
            buffer.get(0, table, 0, buffer.capacity());
        }
        return table;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PrismFontFile)) {
            return false;
        }
        PrismFontFile other = (PrismFontFile) obj;
        return this.filename.equals(other.filename) && this.fullName.equals(other.fullName);
    }

    public int hashCode() {
        return this.filename.hashCode() + (71 * this.fullName.hashCode());
    }
}
