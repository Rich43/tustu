package sun.font;

import com.sun.javafx.fxml.BeanAdapter;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.util.PdfOps;
import sun.awt.SunToolkit;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/font/TrueTypeFont.class */
public class TrueTypeFont extends FileFont {
    public static final int cmapTag = 1668112752;
    public static final int glyfTag = 1735162214;
    public static final int headTag = 1751474532;
    public static final int hheaTag = 1751672161;
    public static final int hmtxTag = 1752003704;
    public static final int locaTag = 1819239265;
    public static final int maxpTag = 1835104368;
    public static final int nameTag = 1851878757;
    public static final int postTag = 1886352244;
    public static final int os_2Tag = 1330851634;
    public static final int GDEFTag = 1195656518;
    public static final int GPOSTag = 1196445523;
    public static final int GSUBTag = 1196643650;
    public static final int mortTag = 1836020340;
    public static final int morxTag = 1836020344;
    public static final int fdscTag = 1717859171;
    public static final int fvarTag = 1719034226;
    public static final int featTag = 1717920116;
    public static final int EBLCTag = 1161972803;
    public static final int gaspTag = 1734439792;
    public static final int ttcfTag = 1953784678;
    public static final int v1ttTag = 65536;
    public static final int trueTag = 1953658213;
    public static final int ottoTag = 1330926671;
    public static final int MS_PLATFORM_ID = 3;
    public static final short ENGLISH_LOCALE_ID = 1033;
    public static final int FAMILY_NAME_ID = 1;
    public static final int FULL_NAME_ID = 4;
    public static final int POSTSCRIPT_NAME_ID = 6;
    private static final short US_LCID = 1033;
    private static Map<String, Short> lcidMap;
    TTDisposerRecord disposerRecord;
    int fontIndex;
    int directoryCount;
    int directoryOffset;
    int numTables;
    DirectoryEntry[] tableDirectory;
    private boolean supportsJA;
    private boolean supportsCJK;
    private Locale nameLocale;
    private String localeFamilyName;
    private String localeFullName;
    int fontDataSize;
    private static final int TTCHEADERSIZE = 12;
    private static final int DIRECTORYHEADERSIZE = 12;
    private static final int DIRECTORYENTRYSIZE = 16;
    static final String[] encoding_mapping = {"cp1252", "cp1250", "cp1251", "cp1253", "cp1254", "cp1255", "cp1256", "cp1257", "", "", "", "", "", "", "", "", "ms874", "ms932", "gbk", "ms949", "ms950", "ms1361", "", "", "", "", "", "", "", "", "", ""};
    private static final String[][] languages = {new String[]{"en", "ca", "da", "de", "es", "fi", "fr", BeanAdapter.IS_PREFIX, "it", "nl", "no", "pt", "sq", "sv"}, new String[]{PdfOps.cs_TOKEN, "cz", "et", "hr", "hu", "nr", "pl", "ro", "sk", "sl", "sq", "sr"}, new String[]{"bg", "mk", "ru", PdfOps.sh_TOKEN, "uk"}, new String[]{"el"}, new String[]{"tr"}, new String[]{"he"}, new String[]{"ar"}, new String[]{"et", "lt", "lv"}, new String[]{"th"}, new String[]{"ja"}, new String[]{"zh", "zh_CN"}, new String[]{"ko"}, new String[]{"zh_HK", "zh_TW"}, new String[]{"ko"}};
    private static final String[] codePages = {"cp1252", "cp1250", "cp1251", "cp1253", "cp1254", "cp1255", "cp1256", "cp1257", "ms874", "ms932", "gbk", "ms949", "ms950", "ms1361"};
    private static String defaultCodePage = null;
    public static final int reserved_bits1 = Integer.MIN_VALUE;
    public static final int reserved_bits2 = 65535;
    private int fontWidth;
    private int fontWeight;
    private static final int fsSelectionItalicBit = 1;
    private static final int fsSelectionBoldBit = 32;
    private static final int fsSelectionRegularBit = 64;
    private float stSize;
    private float stPos;
    private float ulSize;
    private float ulPos;
    private char[] gaspTable;

    /* loaded from: rt.jar:sun/font/TrueTypeFont$DirectoryEntry.class */
    static class DirectoryEntry {
        int tag;
        int offset;
        int length;

        DirectoryEntry() {
        }
    }

    /* loaded from: rt.jar:sun/font/TrueTypeFont$TTDisposerRecord.class */
    private static class TTDisposerRecord implements DisposerRecord {
        FileChannel channel;

        private TTDisposerRecord() {
            this.channel = null;
        }

        @Override // sun.java2d.DisposerRecord
        public synchronized void dispose() {
            try {
                if (this.channel != null) {
                    this.channel.close();
                }
            } catch (IOException e2) {
            } finally {
                this.channel = null;
            }
        }
    }

    public TrueTypeFont(String str, Object obj, int i2, boolean z2) throws FontFormatException {
        this(str, obj, i2, z2, true);
    }

    public TrueTypeFont(String str, Object obj, int i2, boolean z2, boolean z3) throws FontFormatException {
        super(str, obj);
        this.disposerRecord = new TTDisposerRecord();
        this.fontIndex = 0;
        this.directoryCount = 1;
        this.fontWidth = 0;
        this.fontWeight = 0;
        this.useJavaRasterizer = z2;
        this.fontRank = 3;
        try {
            verify(z3);
            init(i2);
            if (!z3) {
                close();
            }
            Disposer.addObjectRecord(this, this.disposerRecord);
        } catch (Throwable th) {
            close();
            if (th instanceof FontFormatException) {
                throw ((FontFormatException) th);
            }
            throw new FontFormatException("Unexpected runtime exception.");
        }
    }

    @Override // sun.font.FileFont
    protected boolean checkUseNatives() {
        if (this.checkedNatives) {
            return this.useNatives;
        }
        if (!FontUtilities.isSolaris || this.useJavaRasterizer || FontUtilities.useT2K || this.nativeNames == null || getDirectoryEntry(1161972803) != null || GraphicsEnvironment.isHeadless()) {
            this.checkedNatives = true;
            return false;
        }
        if (this.nativeNames instanceof String) {
            String str = (String) this.nativeNames;
            if (str.indexOf("8859") > 0) {
                this.checkedNatives = true;
                return false;
            }
            if (NativeFont.hasExternalBitmaps(str)) {
                this.nativeFonts = new NativeFont[1];
                try {
                    this.nativeFonts[0] = new NativeFont(str, true);
                    this.useNatives = true;
                } catch (FontFormatException e2) {
                    this.nativeFonts = null;
                }
            }
        } else if (this.nativeNames instanceof String[]) {
            String[] strArr = (String[]) this.nativeNames;
            int length = strArr.length;
            boolean z2 = false;
            for (int i2 = 0; i2 < length; i2++) {
                if (strArr[i2].indexOf("8859") > 0) {
                    this.checkedNatives = true;
                    return false;
                }
                if (NativeFont.hasExternalBitmaps(strArr[i2])) {
                    z2 = true;
                }
            }
            if (!z2) {
                this.checkedNatives = true;
                return false;
            }
            this.useNatives = true;
            this.nativeFonts = new NativeFont[length];
            for (int i3 = 0; i3 < length; i3++) {
                try {
                    this.nativeFonts[i3] = new NativeFont(strArr[i3], true);
                } catch (FontFormatException e3) {
                    this.useNatives = false;
                    this.nativeFonts = null;
                }
            }
        }
        if (this.useNatives) {
            this.glyphToCharMap = new char[getMapper().getNumGlyphs()];
        }
        this.checkedNatives = true;
        return this.useNatives;
    }

    private synchronized FileChannel open() throws FontFormatException {
        return open(true);
    }

    private synchronized FileChannel open(boolean z2) throws FontFormatException {
        if (this.disposerRecord.channel == null) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("open TTF: " + this.platName);
            }
            try {
                this.disposerRecord.channel = ((RandomAccessFile) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.TrueTypeFont.1
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() {
                        try {
                            return new RandomAccessFile(TrueTypeFont.this.platName, InternalZipConstants.READ_MODE);
                        } catch (FileNotFoundException e2) {
                            return null;
                        }
                    }
                })).getChannel();
                this.fileSize = (int) this.disposerRecord.channel.size();
                if (z2) {
                    FontManager fontManagerFactory = FontManagerFactory.getInstance();
                    if (fontManagerFactory instanceof SunFontManager) {
                        ((SunFontManager) fontManagerFactory).addToPool(this);
                    }
                }
            } catch (NullPointerException e2) {
                close();
                throw new FontFormatException(e2.toString());
            } catch (ClosedChannelException e3) {
                Thread.interrupted();
                close();
                open();
            } catch (IOException e4) {
                close();
                throw new FontFormatException(e4.toString());
            }
        }
        return this.disposerRecord.channel;
    }

    @Override // sun.font.FileFont
    protected synchronized void close() {
        this.disposerRecord.dispose();
    }

    int readBlock(ByteBuffer byteBuffer, int i2, int i3) {
        int i4 = 0;
        try {
        } catch (FontFormatException e2) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().severe("While reading " + this.platName, e2);
            }
            i4 = -1;
            deregisterFontAndClearStrikeCache();
        } catch (ClosedChannelException e3) {
            Thread.interrupted();
            close();
            return readBlock(byteBuffer, i2, i3);
        } catch (IOException e4) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().severe("While reading " + this.platName, e4);
            }
            if (0 == 0) {
                i4 = -1;
                deregisterFontAndClearStrikeCache();
            }
        }
        synchronized (this) {
            if (this.disposerRecord.channel == null) {
                open();
            }
            if (i2 + i3 > this.fileSize) {
                if (i2 >= this.fileSize) {
                    if (FontUtilities.isLogging()) {
                        FontUtilities.getLogger().severe("Read offset is " + i2 + " file size is " + this.fileSize + " file is " + this.platName);
                    }
                    return -1;
                }
                i3 = this.fileSize - i2;
            }
            byteBuffer.clear();
            this.disposerRecord.channel.position(i2);
            while (i4 < i3) {
                int i5 = this.disposerRecord.channel.read(byteBuffer);
                if (i5 == -1) {
                    String str = "Unexpected EOF " + ((Object) this);
                    int size = (int) this.disposerRecord.channel.size();
                    if (size != this.fileSize) {
                        str = str + " File size was " + this.fileSize + " and now is " + size;
                    }
                    if (FontUtilities.isLogging()) {
                        FontUtilities.getLogger().severe(str);
                    }
                    if (i4 > i3 / 2 || i4 > 16384) {
                        byteBuffer.flip();
                        if (FontUtilities.isLogging()) {
                            str = "Returning " + i4 + " bytes instead of " + i3;
                            FontUtilities.getLogger().severe(str);
                        }
                    }
                    throw new IOException(str);
                }
                i4 += i5;
            }
            byteBuffer.flip();
            if (i4 > i3) {
                i4 = i3;
            }
            return i4;
        }
    }

    @Override // sun.font.FileFont
    ByteBuffer readBlock(int i2, int i3) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(i3);
        try {
        } catch (FontFormatException e2) {
            return null;
        } catch (ClosedChannelException e3) {
            Thread.interrupted();
            close();
            readBlock(byteBufferAllocate, i2, i3);
        } catch (IOException e4) {
            return null;
        }
        synchronized (this) {
            if (this.disposerRecord.channel == null) {
                open();
            }
            if (i2 + i3 > this.fileSize) {
                if (i2 > this.fileSize) {
                    return null;
                }
                byteBufferAllocate = ByteBuffer.allocate(this.fileSize - i2);
            }
            this.disposerRecord.channel.position(i2);
            this.disposerRecord.channel.read(byteBufferAllocate);
            byteBufferAllocate.flip();
            return byteBufferAllocate;
        }
    }

    byte[] readBytes(int i2, int i3) {
        ByteBuffer block = readBlock(i2, i3);
        if (block.hasArray()) {
            return block.array();
        }
        byte[] bArr = new byte[block.limit()];
        block.get(bArr);
        return bArr;
    }

    private void verify(boolean z2) throws FontFormatException {
        open(z2);
    }

    protected void init(int i2) throws FontFormatException {
        int i3 = 0;
        ByteBuffer block = readBlock(0, 12);
        try {
            switch (block.getInt()) {
                case 65536:
                case 1330926671:
                case 1953658213:
                    this.fontDataSize = this.fileSize;
                    break;
                case 1953784678:
                    block.getInt();
                    this.directoryCount = block.getInt();
                    if (i2 >= this.directoryCount) {
                        throw new FontFormatException("Bad collection index");
                    }
                    this.fontIndex = i2;
                    i3 = readBlock(12 + (4 * i2), 4).getInt();
                    this.fontDataSize = Math.max(0, this.fileSize - i3);
                    break;
                default:
                    throw new FontFormatException("Unsupported sfnt " + getPublicFileName());
            }
            this.numTables = readBlock(i3 + 4, 2).getShort();
            this.directoryOffset = i3 + 12;
            IntBuffer intBufferAsIntBuffer = readBlock(this.directoryOffset, this.numTables * 16).asIntBuffer();
            this.tableDirectory = new DirectoryEntry[this.numTables];
            for (int i4 = 0; i4 < this.numTables; i4++) {
                DirectoryEntry directoryEntry = new DirectoryEntry();
                this.tableDirectory[i4] = directoryEntry;
                directoryEntry.tag = intBufferAsIntBuffer.get();
                intBufferAsIntBuffer.get();
                directoryEntry.offset = intBufferAsIntBuffer.get() & Integer.MAX_VALUE;
                directoryEntry.length = intBufferAsIntBuffer.get() & Integer.MAX_VALUE;
                if (directoryEntry.offset + directoryEntry.length < directoryEntry.length || directoryEntry.offset + directoryEntry.length > this.fileSize) {
                    throw new FontFormatException("bad table, tag=" + directoryEntry.tag);
                }
            }
            if (getDirectoryEntry(1751474532) == null) {
                throw new FontFormatException("missing head table");
            }
            if (getDirectoryEntry(1835104368) == null) {
                throw new FontFormatException("missing maxp table");
            }
            if (getDirectoryEntry(1752003704) != null && getDirectoryEntry(1751672161) == null) {
                throw new FontFormatException("missing hhea table");
            }
            if (getTableBuffer(1835104368).getChar(4) == 0) {
                throw new FontFormatException("zero glyphs");
            }
            initNames();
            if (this.familyName == null || this.fullName == null) {
                throw new FontFormatException("Font name not found");
            }
            ByteBuffer tableBuffer = getTableBuffer(1330851634);
            setStyle(tableBuffer);
            setCJKSupport(tableBuffer);
        } catch (Exception e2) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().severe(e2.toString());
            }
            if (e2 instanceof FontFormatException) {
                throw ((FontFormatException) e2);
            }
            throw new FontFormatException(e2.toString());
        }
    }

    static String getCodePage() {
        String country;
        if (defaultCodePage != null) {
            return defaultCodePage;
        }
        if (FontUtilities.isWindows) {
            defaultCodePage = (String) AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
        } else {
            if (languages.length != codePages.length) {
                throw new InternalError("wrong code pages array length");
            }
            Locale startupLocale = SunToolkit.getStartupLocale();
            String language = startupLocale.getLanguage();
            if (language != null) {
                if (language.equals("zh") && (country = startupLocale.getCountry()) != null) {
                    language = language + "_" + country;
                }
                for (int i2 = 0; i2 < languages.length; i2++) {
                    for (int i3 = 0; i3 < languages[i2].length; i3++) {
                        if (language.equals(languages[i2][i3])) {
                            defaultCodePage = codePages[i2];
                            return defaultCodePage;
                        }
                    }
                }
            }
        }
        if (defaultCodePage == null) {
            defaultCodePage = "";
        }
        return defaultCodePage;
    }

    @Override // sun.font.Font2D
    boolean supportsEncoding(String str) {
        if (str == null) {
            str = getCodePage();
        }
        if ("".equals(str)) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        if (lowerCase.equals("gb18030")) {
            lowerCase = "gbk";
        } else if (lowerCase.equals("ms950_hkscs")) {
            lowerCase = "ms950";
        }
        ByteBuffer tableBuffer = getTableBuffer(1330851634);
        if (tableBuffer == null || tableBuffer.capacity() < 86) {
            return false;
        }
        int i2 = tableBuffer.getInt(78);
        tableBuffer.getInt(82);
        for (int i3 = 0; i3 < encoding_mapping.length; i3++) {
            if (encoding_mapping[i3].equals(lowerCase) && ((1 << i3) & i2) != 0) {
                return true;
            }
        }
        return false;
    }

    private void setCJKSupport(ByteBuffer byteBuffer) {
        if (byteBuffer == null || byteBuffer.capacity() < 50) {
            return;
        }
        int i2 = byteBuffer.getInt(46);
        this.supportsCJK = (i2 & 700383232) != 0;
        this.supportsJA = (i2 & 393216) != 0;
    }

    boolean supportsJA() {
        return this.supportsJA;
    }

    ByteBuffer getTableBuffer(int i2) {
        DirectoryEntry directoryEntry = null;
        int i3 = 0;
        while (true) {
            if (i3 >= this.numTables) {
                break;
            }
            if (this.tableDirectory[i3].tag != i2) {
                i3++;
            } else {
                directoryEntry = this.tableDirectory[i3];
                break;
            }
        }
        if (directoryEntry == null || directoryEntry.length == 0 || directoryEntry.offset + directoryEntry.length < directoryEntry.length || directoryEntry.offset + directoryEntry.length > this.fileSize) {
            return null;
        }
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(directoryEntry.length);
        synchronized (this) {
            try {
                if (this.disposerRecord.channel == null) {
                    open();
                }
                this.disposerRecord.channel.position(directoryEntry.offset);
                int i4 = this.disposerRecord.channel.read(byteBufferAllocate);
                byteBufferAllocate.flip();
                if (i4 < directoryEntry.length) {
                    return null;
                }
                return byteBufferAllocate;
            } catch (FontFormatException e2) {
                return null;
            } catch (ClosedChannelException e3) {
                Thread.interrupted();
                close();
                return getTableBuffer(i2);
            } catch (IOException e4) {
                return null;
            }
        }
    }

    @Override // sun.font.Font2D
    protected long getLayoutTableCache() {
        try {
            return getScaler().getLayoutTableCache();
        } catch (FontScalerException e2) {
            return 0L;
        }
    }

    @Override // sun.font.Font2D
    protected byte[] getTableBytes(int i2) {
        ByteBuffer tableBuffer = getTableBuffer(i2);
        if (tableBuffer == null) {
            return null;
        }
        if (tableBuffer.hasArray()) {
            try {
                return tableBuffer.array();
            } catch (Exception e2) {
            }
        }
        byte[] bArr = new byte[getTableSize(i2)];
        tableBuffer.get(bArr);
        return bArr;
    }

    int getTableSize(int i2) {
        for (int i3 = 0; i3 < this.numTables; i3++) {
            if (this.tableDirectory[i3].tag == i2) {
                return this.tableDirectory[i3].length;
            }
        }
        return 0;
    }

    int getTableOffset(int i2) {
        for (int i3 = 0; i3 < this.numTables; i3++) {
            if (this.tableDirectory[i3].tag == i2) {
                return this.tableDirectory[i3].offset;
            }
        }
        return 0;
    }

    DirectoryEntry getDirectoryEntry(int i2) {
        for (int i3 = 0; i3 < this.numTables; i3++) {
            if (this.tableDirectory[i3].tag == i2) {
                return this.tableDirectory[i3];
            }
        }
        return null;
    }

    boolean useEmbeddedBitmapsForSize(int i2) {
        ByteBuffer tableBuffer;
        if (!this.supportsCJK || getDirectoryEntry(1161972803) == null || (tableBuffer = getTableBuffer(1161972803)) == null) {
            return false;
        }
        int i3 = tableBuffer.getInt(4);
        for (int i4 = 0; i4 < i3; i4++) {
            if ((tableBuffer.get(8 + (i4 * 48) + 45) & 255) == i2) {
                return true;
            }
        }
        return false;
    }

    public String getFullName() {
        return this.fullName;
    }

    @Override // sun.font.Font2D
    protected void setStyle() {
        setStyle(getTableBuffer(1330851634));
    }

    @Override // sun.font.Font2D
    public int getWidth() {
        return this.fontWidth > 0 ? this.fontWidth : super.getWidth();
    }

    @Override // sun.font.Font2D
    public int getWeight() {
        return this.fontWeight > 0 ? this.fontWeight : super.getWeight();
    }

    private void setStyle(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
        }
        if (byteBuffer.capacity() >= 8) {
            this.fontWeight = byteBuffer.getChar(4) & 65535;
            this.fontWidth = byteBuffer.getChar(6) & 65535;
        }
        if (byteBuffer.capacity() < 64) {
            super.setStyle();
            return;
        }
        int i2 = byteBuffer.getChar(62) & 65535;
        int i3 = i2 & 1;
        int i4 = i2 & 32;
        int i5 = i2 & 64;
        if (i5 != 0 && (i3 | i4) != 0) {
            super.setStyle();
            return;
        }
        if ((i5 | i3 | i4) == 0) {
            super.setStyle();
            return;
        }
        switch (i4 | i3) {
            case 1:
                this.style = 2;
                break;
            case 32:
                if (FontUtilities.isSolaris && this.platName.endsWith("HG-GothicB.ttf")) {
                    this.style = 0;
                    break;
                } else {
                    this.style = 1;
                    break;
                }
                break;
            case 33:
                this.style = 3;
                break;
        }
    }

    private void setStrikethroughMetrics(ByteBuffer byteBuffer, int i2) {
        if (byteBuffer == null || byteBuffer.capacity() < 30 || i2 < 0) {
            this.stSize = 0.05f;
            this.stPos = -0.4f;
            return;
        }
        ShortBuffer shortBufferAsShortBuffer = byteBuffer.asShortBuffer();
        this.stSize = shortBufferAsShortBuffer.get(13) / i2;
        this.stPos = (-shortBufferAsShortBuffer.get(14)) / i2;
        if (this.stSize < 0.0f) {
            this.stSize = 0.05f;
        }
        if (Math.abs(this.stPos) > 2.0f) {
            this.stPos = -0.4f;
        }
    }

    private void setUnderlineMetrics(ByteBuffer byteBuffer, int i2) {
        if (byteBuffer == null || byteBuffer.capacity() < 12 || i2 < 0) {
            this.ulSize = 0.05f;
            this.ulPos = 0.1f;
            return;
        }
        ShortBuffer shortBufferAsShortBuffer = byteBuffer.asShortBuffer();
        this.ulSize = shortBufferAsShortBuffer.get(5) / i2;
        this.ulPos = (-shortBufferAsShortBuffer.get(4)) / i2;
        if (this.ulSize < 0.0f) {
            this.ulSize = 0.05f;
        }
        if (Math.abs(this.ulPos) > 2.0f) {
            this.ulPos = 0.1f;
        }
    }

    @Override // sun.font.Font2D
    public void getStyleMetrics(float f2, float[] fArr, int i2) {
        if (this.ulSize == 0.0f && this.ulPos == 0.0f) {
            ByteBuffer tableBuffer = getTableBuffer(1751474532);
            int i3 = -1;
            if (tableBuffer != null && tableBuffer.capacity() >= 18) {
                i3 = tableBuffer.asShortBuffer().get(9) & 65535;
                if (i3 < 16 || i3 > 16384) {
                    i3 = 2048;
                }
            }
            setStrikethroughMetrics(getTableBuffer(1330851634), i3);
            setUnderlineMetrics(getTableBuffer(1886352244), i3);
        }
        fArr[i2] = this.stPos * f2;
        fArr[i2 + 1] = this.stSize * f2;
        fArr[i2 + 2] = this.ulPos * f2;
        fArr[i2 + 3] = this.ulSize * f2;
    }

    private String makeString(byte[] bArr, int i2, short s2) {
        String str;
        if (s2 >= 2 && s2 <= 6) {
            bArr = new byte[i2];
            i2 = 0;
            for (int i3 = 0; i3 < i2; i3++) {
                if (bArr[i3] != 0) {
                    int i4 = i2;
                    i2++;
                    bArr[i4] = bArr[i3];
                }
            }
        }
        switch (s2) {
            case 0:
                str = "UTF-16";
                break;
            case 1:
                str = "UTF-16";
                break;
            case 2:
                str = "SJIS";
                break;
            case 3:
                str = "GBK";
                break;
            case 4:
                str = "MS950";
                break;
            case 5:
                str = "EUC_KR";
                break;
            case 6:
                str = "Johab";
                break;
            default:
                str = "UTF-16";
                break;
        }
        try {
            return new String(bArr, 0, i2, str);
        } catch (UnsupportedEncodingException e2) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().warning(((Object) e2) + " EncodingID=" + ((int) s2));
            }
            return new String(bArr, 0, i2);
        } catch (Throwable th) {
            return null;
        }
    }

    protected void initNames() {
        byte[] bArr = new byte[256];
        ByteBuffer tableBuffer = getTableBuffer(1851878757);
        if (tableBuffer != null) {
            ShortBuffer shortBufferAsShortBuffer = tableBuffer.asShortBuffer();
            shortBufferAsShortBuffer.get();
            int i2 = shortBufferAsShortBuffer.get();
            int i3 = shortBufferAsShortBuffer.get() & 65535;
            this.nameLocale = SunToolkit.getStartupLocale();
            short lCIDFromLocale = getLCIDFromLocale(this.nameLocale);
            for (int i4 = 0; i4 < i2; i4++) {
                if (shortBufferAsShortBuffer.get() != 3) {
                    shortBufferAsShortBuffer.position(shortBufferAsShortBuffer.position() + 5);
                } else {
                    short s2 = shortBufferAsShortBuffer.get();
                    short s3 = shortBufferAsShortBuffer.get();
                    short s4 = shortBufferAsShortBuffer.get();
                    int i5 = shortBufferAsShortBuffer.get() & 65535;
                    int i6 = (shortBufferAsShortBuffer.get() & 65535) + i3;
                    switch (s4) {
                        case 1:
                            if (this.familyName != null && s3 != 1033 && s3 != lCIDFromLocale) {
                                break;
                            } else {
                                tableBuffer.position(i6);
                                tableBuffer.get(bArr, 0, i5);
                                String strMakeString = makeString(bArr, i5, s2);
                                if (this.familyName == null || s3 == 1033) {
                                    this.familyName = strMakeString;
                                }
                                if (s3 == lCIDFromLocale) {
                                    this.localeFamilyName = strMakeString;
                                    break;
                                } else {
                                    break;
                                }
                            }
                            break;
                        case 4:
                            if (this.fullName != null && s3 != 1033 && s3 != lCIDFromLocale) {
                                break;
                            } else {
                                tableBuffer.position(i6);
                                tableBuffer.get(bArr, 0, i5);
                                String strMakeString2 = makeString(bArr, i5, s2);
                                if (this.fullName == null || s3 == 1033) {
                                    this.fullName = strMakeString2;
                                }
                                if (s3 == lCIDFromLocale) {
                                    this.localeFullName = strMakeString2;
                                    break;
                                } else {
                                    break;
                                }
                            }
                            break;
                    }
                }
            }
            if (this.localeFamilyName == null) {
                this.localeFamilyName = this.familyName;
            }
            if (this.localeFullName == null) {
                this.localeFullName = this.fullName;
            }
        }
    }

    protected String lookupName(short s2, int i2) {
        String strMakeString = null;
        byte[] bArr = new byte[1024];
        ByteBuffer tableBuffer = getTableBuffer(1851878757);
        if (tableBuffer != null) {
            ShortBuffer shortBufferAsShortBuffer = tableBuffer.asShortBuffer();
            shortBufferAsShortBuffer.get();
            int i3 = shortBufferAsShortBuffer.get();
            int i4 = shortBufferAsShortBuffer.get() & 65535;
            for (int i5 = 0; i5 < i3; i5++) {
                if (shortBufferAsShortBuffer.get() != 3) {
                    shortBufferAsShortBuffer.position(shortBufferAsShortBuffer.position() + 5);
                } else {
                    short s3 = shortBufferAsShortBuffer.get();
                    short s4 = shortBufferAsShortBuffer.get();
                    short s5 = shortBufferAsShortBuffer.get();
                    int i6 = shortBufferAsShortBuffer.get() & 65535;
                    int i7 = (shortBufferAsShortBuffer.get() & 65535) + i4;
                    if (s5 == i2 && ((strMakeString == null && s4 == 1033) || s4 == s2)) {
                        tableBuffer.position(i7);
                        tableBuffer.get(bArr, 0, i6);
                        strMakeString = makeString(bArr, i6, s3);
                        if (s4 == s2) {
                            return strMakeString;
                        }
                    }
                }
            }
        }
        return strMakeString;
    }

    public int getFontCount() {
        return this.directoryCount;
    }

    @Override // sun.font.FileFont
    protected synchronized FontScaler getScaler() {
        if (this.scaler == null) {
            this.scaler = FontScaler.getScaler(this, this.fontIndex, this.supportsCJK, this.fileSize);
        }
        return this.scaler;
    }

    @Override // sun.font.Font2D
    public String getPostscriptName() {
        String strLookupName = lookupName((short) 1033, 6);
        if (strLookupName == null) {
            return this.fullName;
        }
        return strLookupName;
    }

    @Override // sun.font.Font2D
    public String getFontName(Locale locale) {
        if (locale == null) {
            return this.fullName;
        }
        if (locale.equals(this.nameLocale) && this.localeFullName != null) {
            return this.localeFullName;
        }
        String strLookupName = lookupName(getLCIDFromLocale(locale), 4);
        if (strLookupName == null) {
            return this.fullName;
        }
        return strLookupName;
    }

    private static void addLCIDMapEntry(Map<String, Short> map, String str, short s2) {
        map.put(str, Short.valueOf(s2));
    }

    private static synchronized void createLCIDMap() {
        if (lcidMap != null) {
            return;
        }
        HashMap map = new HashMap(200);
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
        if (locale.equals(Locale.US)) {
            return (short) 1033;
        }
        if (lcidMap == null) {
            createLCIDMap();
        }
        String string = locale.toString();
        while (true) {
            String str = string;
            if (!"".equals(str)) {
                Short sh = lcidMap.get(str);
                if (sh != null) {
                    return sh.shortValue();
                }
                int iLastIndexOf = str.lastIndexOf(95);
                if (iLastIndexOf < 1) {
                    return (short) 1033;
                }
                string = str.substring(0, iLastIndexOf);
            } else {
                return (short) 1033;
            }
        }
    }

    @Override // sun.font.Font2D
    public String getFamilyName(Locale locale) {
        if (locale == null) {
            return this.familyName;
        }
        if (locale.equals(this.nameLocale) && this.localeFamilyName != null) {
            return this.localeFamilyName;
        }
        String strLookupName = lookupName(getLCIDFromLocale(locale), 1);
        if (strLookupName == null) {
            return this.familyName;
        }
        return strLookupName;
    }

    @Override // sun.font.Font2D
    public CharToGlyphMapper getMapper() {
        if (this.mapper == null) {
            this.mapper = new TrueTypeGlyphMapper(this);
        }
        return this.mapper;
    }

    protected void initAllNames(int i2, HashSet hashSet) {
        byte[] bArr = new byte[256];
        ByteBuffer tableBuffer = getTableBuffer(1851878757);
        if (tableBuffer != null) {
            ShortBuffer shortBufferAsShortBuffer = tableBuffer.asShortBuffer();
            shortBufferAsShortBuffer.get();
            int i3 = shortBufferAsShortBuffer.get();
            int i4 = shortBufferAsShortBuffer.get() & 65535;
            for (int i5 = 0; i5 < i3; i5++) {
                if (shortBufferAsShortBuffer.get() != 3) {
                    shortBufferAsShortBuffer.position(shortBufferAsShortBuffer.position() + 5);
                } else {
                    short s2 = shortBufferAsShortBuffer.get();
                    shortBufferAsShortBuffer.get();
                    short s3 = shortBufferAsShortBuffer.get();
                    int i6 = shortBufferAsShortBuffer.get() & 65535;
                    int i7 = (shortBufferAsShortBuffer.get() & 65535) + i4;
                    if (s3 == i2) {
                        tableBuffer.position(i7);
                        tableBuffer.get(bArr, 0, i6);
                        hashSet.add(makeString(bArr, i6, s2));
                    }
                }
            }
        }
    }

    String[] getAllFamilyNames() {
        HashSet hashSet = new HashSet();
        try {
            initAllNames(1, hashSet);
        } catch (Exception e2) {
        }
        return (String[]) hashSet.toArray(new String[0]);
    }

    String[] getAllFullNames() {
        HashSet hashSet = new HashSet();
        try {
            initAllNames(4, hashSet);
        } catch (Exception e2) {
        }
        return (String[]) hashSet.toArray(new String[0]);
    }

    @Override // sun.font.PhysicalFont
    Point2D.Float getGlyphPoint(long j2, int i2, int i3) {
        try {
            return getScaler().getGlyphPoint(j2, i2, i3);
        } catch (FontScalerException e2) {
            return null;
        }
    }

    private char[] getGaspTable() {
        if (this.gaspTable != null) {
            return this.gaspTable;
        }
        ByteBuffer tableBuffer = getTableBuffer(1734439792);
        if (tableBuffer == null) {
            char[] cArr = new char[0];
            this.gaspTable = cArr;
            return cArr;
        }
        CharBuffer charBufferAsCharBuffer = tableBuffer.asCharBuffer();
        if (charBufferAsCharBuffer.get() > 1) {
            char[] cArr2 = new char[0];
            this.gaspTable = cArr2;
            return cArr2;
        }
        char c2 = charBufferAsCharBuffer.get();
        if (4 + (c2 * 4) > getTableSize(1734439792)) {
            char[] cArr3 = new char[0];
            this.gaspTable = cArr3;
            return cArr3;
        }
        this.gaspTable = new char[2 * c2];
        charBufferAsCharBuffer.get(this.gaspTable);
        return this.gaspTable;
    }

    @Override // sun.font.Font2D
    public boolean useAAForPtSize(int i2) {
        char[] gaspTable = getGaspTable();
        if (gaspTable.length <= 0) {
            return this.style == 1 || i2 <= 8 || i2 >= 18;
        }
        for (int i3 = 0; i3 < gaspTable.length; i3 += 2) {
            if (i2 <= gaspTable[i3]) {
                return (gaspTable[i3 + 1] & 2) != 0;
            }
        }
        return true;
    }

    @Override // sun.font.Font2D
    public boolean hasSupplementaryChars() {
        return ((TrueTypeGlyphMapper) getMapper()).hasSupplementaryChars();
    }

    public String toString() {
        return "** TrueType Font: Family=" + this.familyName + " Name=" + this.fullName + " style=" + this.style + " fileName=" + getPublicFileName();
    }
}
