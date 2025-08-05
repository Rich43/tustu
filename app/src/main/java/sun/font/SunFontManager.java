package sun.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.plaf.FontUIResource;
import sun.applet.AppletSecurity;
import sun.awt.AppContext;
import sun.awt.FontConfiguration;
import sun.awt.SunToolkit;
import sun.java2d.FontSupport;
import sun.misc.ThreadGroupUtils;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/font/SunFontManager.class */
public abstract class SunFontManager implements FontSupport, FontManagerForSGE {
    public static final int FONTFORMAT_NONE = -1;
    public static final int FONTFORMAT_TRUETYPE = 0;
    public static final int FONTFORMAT_TYPE1 = 1;
    public static final int FONTFORMAT_T2K = 2;
    public static final int FONTFORMAT_TTC = 3;
    public static final int FONTFORMAT_COMPOSITE = 4;
    public static final int FONTFORMAT_NATIVE = 5;
    protected static final int CHANNELPOOLSIZE = 20;
    private HashMap<String, TrueTypeFont> localeFullNamesToFont;
    private PhysicalFont defaultPhysicalFont;
    static boolean longAddresses;
    HashMap<String, String> jreFontMap;
    HashSet<String> jreLucidaFontFiles;
    String[] jreOtherFontFiles;
    public static final String lucidaFontName = "Lucida Sans Regular";
    public static String jreLibDirName;
    public static String jreFontDirName;
    private String defaultFontName;
    private String defaultFontFileName;
    private ArrayList badFonts;
    protected String fontPath;
    private FontConfiguration fontConfig;
    private Font[] allFonts;
    private String[] allFamilies;
    private Locale lastDefaultLocale;
    public static boolean noType1Font;
    private boolean usePlatformFontMetrics;
    private boolean haveCheckedUnreferencedFontFiles;
    static HashMap<String, FamilyDescription> platformFontMap;
    private static final Object altJAFontKey;
    private static final Object localeFontKey;
    private static final Object proportionalFontKey;
    private static boolean gAltJAFont;
    private static HashSet<String> installedNames;
    private static final Object regFamilyKey;
    private static final Object regFullNameKey;
    private Hashtable<String, FontFamily> createdByFamilyName;
    private Hashtable<String, Font2D> createdByFullName;
    private static Locale systemLocale;
    private static HashSet<String> missingFontFiles = null;
    private static final FilenameFilter ttFilter = new TTFilter();
    private static final FilenameFilter t1Filter = new T1Filter();
    private static String[] STR_ARRAY = new String[0];
    private static int maxSoftRefCnt = 10;
    protected FileFont[] fontFileCache = new FileFont[20];
    private int lastPoolIndex = 0;
    private int maxCompFont = 0;
    private CompositeFont[] compFonts = new CompositeFont[20];
    private ConcurrentHashMap<String, CompositeFont> compositeFonts = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, PhysicalFont> physicalFonts = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, PhysicalFont> registeredFonts = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<String, Font2D> fullNameToFont = new ConcurrentHashMap<>();
    private boolean loaded1dot0Fonts = false;
    boolean loadedAllFonts = false;
    boolean loadedAllFontFiles = false;
    boolean noOtherJREFontFiles = false;
    protected HashSet registeredFontFiles = new HashSet();
    private boolean discoveredAllFonts = false;
    private final ConcurrentHashMap<String, FontRegistrationInfo> deferredFontFiles = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Font2DHandle> initialisedFonts = new ConcurrentHashMap<>();
    private HashMap<String, String> fontToFileMap = null;
    private HashMap<String, String> fontToFamilyNameMap = null;
    private HashMap<String, ArrayList<String>> familyToFontListMap = null;
    private String[] pathDirs = null;
    private ConcurrentHashMap<String, Font2D> fontNameCache = new ConcurrentHashMap<>();
    protected Thread fileCloser = null;
    Vector<File> tmpFontFiles = null;
    private int createdFontCount = 0;
    private boolean _usingPerAppContextComposites = false;
    private boolean _usingAlternateComposites = false;
    private boolean gLocalePref = false;
    private boolean gPropPref = false;
    private boolean fontsAreRegistered = false;
    private boolean fontsAreRegisteredPerAppContext = false;

    /* loaded from: rt.jar:sun/font/SunFontManager$FamilyDescription.class */
    public static class FamilyDescription {
        public String familyName;
        public String plainFullName;
        public String boldFullName;
        public String italicFullName;
        public String boldItalicFullName;
        public String plainFileName;
        public String boldFileName;
        public String italicFileName;
        public String boldItalicFileName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void initIDs();

    protected abstract String getFontPath(boolean z2);

    protected abstract String[] getDefaultPlatformFont();

    protected abstract FontConfiguration createFontConfiguration();

    public abstract FontConfiguration createFontConfiguration(boolean z2, boolean z3);

    /* loaded from: rt.jar:sun/font/SunFontManager$TTFilter.class */
    private static class TTFilter implements FilenameFilter {
        private TTFilter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            int length = str.length() - 4;
            if (length <= 0) {
                return false;
            }
            return str.startsWith(".ttf", length) || str.startsWith(".TTF", length) || str.startsWith(".ttc", length) || str.startsWith(".TTC", length) || str.startsWith(".otf", length) || str.startsWith(".OTF", length);
        }
    }

    /* loaded from: rt.jar:sun/font/SunFontManager$T1Filter.class */
    private static class T1Filter implements FilenameFilter {
        private T1Filter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            int length;
            if (!SunFontManager.noType1Font && (length = str.length() - 4) > 0) {
                return str.startsWith(".pfa", length) || str.startsWith(".pfb", length) || str.startsWith(".PFA", length) || str.startsWith(".PFB", length);
            }
            return false;
        }
    }

    /* loaded from: rt.jar:sun/font/SunFontManager$TTorT1Filter.class */
    private static class TTorT1Filter implements FilenameFilter {
        private TTorT1Filter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File file, String str) {
            int length = str.length() - 4;
            if (length <= 0) {
                return false;
            }
            if (str.startsWith(".ttf", length) || str.startsWith(".TTF", length) || str.startsWith(".ttc", length) || str.startsWith(".TTC", length) || str.startsWith(".otf", length) || str.startsWith(".OTF", length)) {
                return true;
            }
            if (SunFontManager.noType1Font) {
                return false;
            }
            return str.startsWith(".pfa", length) || str.startsWith(".pfb", length) || str.startsWith(".PFA", length) || str.startsWith(".PFB", length);
        }
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                FontManagerNativeLibrary.load();
                SunFontManager.initIDs();
                switch (StrikeCache.nativeAddressSize) {
                    case 4:
                        SunFontManager.longAddresses = false;
                        break;
                    case 8:
                        SunFontManager.longAddresses = true;
                        break;
                    default:
                        throw new RuntimeException("Unexpected address size");
                }
                SunFontManager.noType1Font = "true".equals(System.getProperty("sun.java2d.noType1Font"));
                SunFontManager.jreLibDirName = System.getProperty("java.home", "") + File.separator + "lib";
                SunFontManager.jreFontDirName = SunFontManager.jreLibDirName + File.separator + "fonts";
                new File(SunFontManager.jreFontDirName + File.separator + "LucidaSansRegular.ttf");
                int unused = SunFontManager.maxSoftRefCnt = Integer.getInteger("sun.java2d.font.maxSoftRefs", 10).intValue();
                return null;
            }
        });
        altJAFontKey = new Object();
        localeFontKey = new Object();
        proportionalFontKey = new Object();
        gAltJAFont = false;
        installedNames = null;
        regFamilyKey = new Object();
        regFullNameKey = new Object();
        systemLocale = null;
    }

    public static SunFontManager getInstance() {
        return (SunFontManager) FontManagerFactory.getInstance();
    }

    public FilenameFilter getTrueTypeFilter() {
        return ttFilter;
    }

    public FilenameFilter getType1Filter() {
        return t1Filter;
    }

    @Override // sun.font.FontManager
    public boolean usingPerAppContextComposites() {
        return this._usingPerAppContextComposites;
    }

    private void initJREFontMap() {
        this.jreFontMap = new HashMap<>();
        this.jreLucidaFontFiles = new HashSet<>();
        if (isOpenJDK()) {
            return;
        }
        this.jreFontMap.put("lucida sans0", "LucidaSansRegular.ttf");
        this.jreFontMap.put("lucida sans1", "LucidaSansDemiBold.ttf");
        this.jreFontMap.put("lucida sans regular0", "LucidaSansRegular.ttf");
        this.jreFontMap.put("lucida sans regular1", "LucidaSansDemiBold.ttf");
        this.jreFontMap.put("lucida sans bold1", "LucidaSansDemiBold.ttf");
        this.jreFontMap.put("lucida sans demibold1", "LucidaSansDemiBold.ttf");
        this.jreFontMap.put("lucida sans typewriter0", "LucidaTypewriterRegular.ttf");
        this.jreFontMap.put("lucida sans typewriter1", "LucidaTypewriterBold.ttf");
        this.jreFontMap.put("lucida sans typewriter regular0", "LucidaTypewriter.ttf");
        this.jreFontMap.put("lucida sans typewriter regular1", "LucidaTypewriterBold.ttf");
        this.jreFontMap.put("lucida sans typewriter bold1", "LucidaTypewriterBold.ttf");
        this.jreFontMap.put("lucida sans typewriter demibold1", "LucidaTypewriterBold.ttf");
        this.jreFontMap.put("lucida bright0", "LucidaBrightRegular.ttf");
        this.jreFontMap.put("lucida bright1", "LucidaBrightDemiBold.ttf");
        this.jreFontMap.put("lucida bright2", "LucidaBrightItalic.ttf");
        this.jreFontMap.put("lucida bright3", "LucidaBrightDemiItalic.ttf");
        this.jreFontMap.put("lucida bright regular0", "LucidaBrightRegular.ttf");
        this.jreFontMap.put("lucida bright regular1", "LucidaBrightDemiBold.ttf");
        this.jreFontMap.put("lucida bright regular2", "LucidaBrightItalic.ttf");
        this.jreFontMap.put("lucida bright regular3", "LucidaBrightDemiItalic.ttf");
        this.jreFontMap.put("lucida bright bold1", "LucidaBrightDemiBold.ttf");
        this.jreFontMap.put("lucida bright bold3", "LucidaBrightDemiItalic.ttf");
        this.jreFontMap.put("lucida bright demibold1", "LucidaBrightDemiBold.ttf");
        this.jreFontMap.put("lucida bright demibold3", "LucidaBrightDemiItalic.ttf");
        this.jreFontMap.put("lucida bright italic2", "LucidaBrightItalic.ttf");
        this.jreFontMap.put("lucida bright italic3", "LucidaBrightDemiItalic.ttf");
        this.jreFontMap.put("lucida bright bold italic3", "LucidaBrightDemiItalic.ttf");
        this.jreFontMap.put("lucida bright demibold italic3", "LucidaBrightDemiItalic.ttf");
        Iterator<String> it = this.jreFontMap.values().iterator();
        while (it.hasNext()) {
            this.jreLucidaFontFiles.add(it.next());
        }
    }

    public TrueTypeFont getEUDCFont() {
        return null;
    }

    protected SunFontManager() {
        this.usePlatformFontMetrics = false;
        initJREFontMap();
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                File file = new File(SunFontManager.jreFontDirName + File.separator + "badfonts.txt");
                if (file.exists()) {
                    FileInputStream fileInputStream = null;
                    try {
                        SunFontManager.this.badFonts = new ArrayList();
                        fileInputStream = new FileInputStream(file);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            if (FontUtilities.debugFonts()) {
                                FontUtilities.getLogger().warning("read bad font: " + line);
                            }
                            SunFontManager.this.badFonts.add(line);
                        }
                    } catch (IOException e2) {
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e3) {
                            }
                        }
                    }
                }
                if (FontUtilities.isLinux) {
                    SunFontManager.this.registerFontDir(SunFontManager.jreFontDirName);
                }
                SunFontManager.this.registerFontsInDir(SunFontManager.jreFontDirName, true, 2, true, false);
                SunFontManager.this.fontConfig = SunFontManager.this.createFontConfiguration();
                if (SunFontManager.isOpenJDK()) {
                    String[] defaultPlatformFont = SunFontManager.this.getDefaultPlatformFont();
                    SunFontManager.this.defaultFontName = defaultPlatformFont[0];
                    SunFontManager.this.defaultFontFileName = defaultPlatformFont[1];
                }
                String extraFontPath = SunFontManager.this.fontConfig.getExtraFontPath();
                boolean z2 = false;
                boolean z3 = false;
                String property = System.getProperty("sun.java2d.fontpath");
                if (property != null) {
                    if (property.startsWith("prepend:")) {
                        z2 = true;
                        property = property.substring("prepend:".length());
                    } else if (property.startsWith("append:")) {
                        z3 = true;
                        property = property.substring("append:".length());
                    }
                }
                if (FontUtilities.debugFonts()) {
                    PlatformLogger logger = FontUtilities.getLogger();
                    logger.info("JRE font directory: " + SunFontManager.jreFontDirName);
                    logger.info("Extra font path: " + extraFontPath);
                    logger.info("Debug font path: " + property);
                }
                if (property != null) {
                    SunFontManager.this.fontPath = SunFontManager.this.getPlatformFontPath(SunFontManager.noType1Font);
                    if (extraFontPath != null) {
                        SunFontManager.this.fontPath = extraFontPath + File.pathSeparator + SunFontManager.this.fontPath;
                    }
                    if (z3) {
                        SunFontManager.this.fontPath += File.pathSeparator + property;
                    } else if (z2) {
                        SunFontManager.this.fontPath = property + File.pathSeparator + SunFontManager.this.fontPath;
                    } else {
                        SunFontManager.this.fontPath = property;
                    }
                    SunFontManager.this.registerFontDirs(SunFontManager.this.fontPath);
                } else if (extraFontPath != null) {
                    SunFontManager.this.registerFontDirs(extraFontPath);
                }
                if (FontUtilities.isSolaris && Locale.JAPAN.equals(Locale.getDefault())) {
                    SunFontManager.this.registerFontDir("/usr/openwin/lib/locale/ja/X11/fonts/TT");
                }
                SunFontManager.this.initCompositeFonts(SunFontManager.this.fontConfig, null);
                return null;
            }
        });
        if (((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.font.SunFontManager.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return Boolean.valueOf("true".equals(System.getProperty("java2d.font.usePlatformFont")) || System.getenv("JAVA2D_USEPLATFORMFONT") != null);
            }
        })).booleanValue()) {
            this.usePlatformFontMetrics = true;
            System.out.println("Enabling platform font metrics for win32. This is an unsupported option.");
            System.out.println("This yields incorrect composite font metrics as reported by 1.1.x releases.");
            System.out.println("It is appropriate only for use by applications which do not use any Java 2");
            System.out.println("functionality. This property will be removed in a later release.");
        }
    }

    @Override // sun.font.FontManager
    public Font2DHandle getNewComposite(String str, int i2, Font2DHandle font2DHandle) {
        if (!(font2DHandle.font2D instanceof CompositeFont)) {
            return font2DHandle;
        }
        CompositeFont compositeFont = (CompositeFont) font2DHandle.font2D;
        PhysicalFont slotFont = compositeFont.getSlotFont(0);
        if (str == null) {
            str = slotFont.getFamilyName(null);
        }
        if (i2 == -1) {
            i2 = compositeFont.getStyle();
        }
        Font2D font2DFindFont2D = findFont2D(str, i2, 0);
        if (!(font2DFindFont2D instanceof PhysicalFont)) {
            font2DFindFont2D = slotFont;
        }
        PhysicalFont physicalFont = (PhysicalFont) font2DFindFont2D;
        CompositeFont compositeFont2 = (CompositeFont) findFont2D("dialog", i2, 0);
        if (compositeFont2 == null) {
            return font2DHandle;
        }
        return new Font2DHandle(new CompositeFont(physicalFont, compositeFont2));
    }

    protected void registerCompositeFont(String str, String[] strArr, String[] strArr2, int i2, int[] iArr, int[] iArr2, boolean z2) {
        CompositeFont compositeFont = new CompositeFont(str, strArr, strArr2, i2, iArr, iArr2, z2, this);
        addCompositeToFontList(compositeFont, 2);
        synchronized (this.compFonts) {
            CompositeFont[] compositeFontArr = this.compFonts;
            int i3 = this.maxCompFont;
            this.maxCompFont = i3 + 1;
            compositeFontArr[i3] = compositeFont;
        }
    }

    protected static void registerCompositeFont(String str, String[] strArr, String[] strArr2, int i2, int[] iArr, int[] iArr2, boolean z2, ConcurrentHashMap<String, Font2D> concurrentHashMap) {
        CompositeFont compositeFont = new CompositeFont(str, strArr, strArr2, i2, iArr, iArr2, z2, getInstance());
        Font2D font2D = concurrentHashMap.get(str.toLowerCase(Locale.ENGLISH));
        if (font2D instanceof CompositeFont) {
            font2D.handle.font2D = compositeFont;
        }
        concurrentHashMap.put(str.toLowerCase(Locale.ENGLISH), compositeFont);
    }

    private void addCompositeToFontList(CompositeFont compositeFont, int i2) {
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().info("Add to Family " + compositeFont.familyName + ", Font " + compositeFont.fullName + " rank=" + i2);
        }
        compositeFont.setRank(i2);
        this.compositeFonts.put(compositeFont.fullName, compositeFont);
        this.fullNameToFont.put(compositeFont.fullName.toLowerCase(Locale.ENGLISH), compositeFont);
        FontFamily family = FontFamily.getFamily(compositeFont.familyName);
        if (family == null) {
            family = new FontFamily(compositeFont.familyName, true, i2);
        }
        family.setFont(compositeFont, compositeFont.style);
    }

    protected PhysicalFont addToFontList(PhysicalFont physicalFont, int i2) {
        String str = physicalFont.fullName;
        String str2 = physicalFont.familyName;
        if (str == null || "".equals(str) || this.compositeFonts.containsKey(str)) {
            return null;
        }
        physicalFont.setRank(i2);
        if (!this.physicalFonts.containsKey(str)) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("Add to Family " + str2 + ", Font " + str + " rank=" + i2);
            }
            this.physicalFonts.put(str, physicalFont);
            FontFamily family = FontFamily.getFamily(str2);
            if (family == null) {
                new FontFamily(str2, false, i2).setFont(physicalFont, physicalFont.style);
            } else {
                family.setFont(physicalFont, physicalFont.style);
            }
            this.fullNameToFont.put(str.toLowerCase(Locale.ENGLISH), physicalFont);
            return physicalFont;
        }
        PhysicalFont physicalFont2 = this.physicalFonts.get(str);
        if (physicalFont2 == null) {
            return null;
        }
        if (physicalFont2.getRank() >= i2) {
            if (physicalFont2.mapper != null && i2 > 2) {
                return physicalFont2;
            }
            if (physicalFont2.getRank() == i2) {
                if ((physicalFont2 instanceof TrueTypeFont) && (physicalFont instanceof TrueTypeFont)) {
                    if (((TrueTypeFont) physicalFont2).fileSize >= ((TrueTypeFont) physicalFont).fileSize) {
                        return physicalFont2;
                    }
                } else {
                    return physicalFont2;
                }
            }
            if (physicalFont2.platName.startsWith(jreFontDirName)) {
                if (FontUtilities.isLogging()) {
                    FontUtilities.getLogger().warning("Unexpected attempt to replace a JRE  font " + str + " from " + physicalFont2.platName + " with " + physicalFont.platName);
                }
                return physicalFont2;
            }
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("Replace in Family " + str2 + ",Font " + str + " new rank=" + i2 + " from " + physicalFont2.platName + " with " + physicalFont.platName);
            }
            replaceFont(physicalFont2, physicalFont);
            this.physicalFonts.put(str, physicalFont);
            this.fullNameToFont.put(str.toLowerCase(Locale.ENGLISH), physicalFont);
            FontFamily family2 = FontFamily.getFamily(str2);
            if (family2 == null) {
                new FontFamily(str2, false, i2).setFont(physicalFont, physicalFont.style);
            } else {
                family2.setFont(physicalFont, physicalFont.style);
            }
            return physicalFont;
        }
        return physicalFont2;
    }

    public Font2D[] getRegisteredFonts() {
        PhysicalFont[] physicalFonts = getPhysicalFonts();
        int i2 = this.maxCompFont;
        Font2D[] font2DArr = new Font2D[physicalFonts.length + i2];
        System.arraycopy(this.compFonts, 0, font2DArr, 0, i2);
        System.arraycopy(physicalFonts, 0, font2DArr, i2, physicalFonts.length);
        return font2DArr;
    }

    protected PhysicalFont[] getPhysicalFonts() {
        return (PhysicalFont[]) this.physicalFonts.values().toArray(new PhysicalFont[0]);
    }

    /* loaded from: rt.jar:sun/font/SunFontManager$FontRegistrationInfo.class */
    private static final class FontRegistrationInfo {
        String fontFilePath;
        String[] nativeNames;
        int fontFormat;
        boolean javaRasterizer;
        int fontRank;

        FontRegistrationInfo(String str, String[] strArr, int i2, boolean z2, int i3) {
            this.fontFilePath = str;
            this.nativeNames = strArr;
            this.fontFormat = i2;
            this.javaRasterizer = z2;
            this.fontRank = i3;
        }
    }

    protected synchronized void initialiseDeferredFonts() {
        Iterator<String> it = this.deferredFontFiles.keySet().iterator();
        while (it.hasNext()) {
            initialiseDeferredFont(it.next());
        }
    }

    protected synchronized void registerDeferredJREFonts(String str) {
        for (FontRegistrationInfo fontRegistrationInfo : this.deferredFontFiles.values()) {
            if (fontRegistrationInfo.fontFilePath != null && fontRegistrationInfo.fontFilePath.startsWith(str)) {
                initialiseDeferredFont(fontRegistrationInfo.fontFilePath);
            }
        }
    }

    public boolean isDeferredFont(String str) {
        return this.deferredFontFiles.containsKey(str);
    }

    public PhysicalFont findJREDeferredFont(String str, int i2) {
        PhysicalFont physicalFontInitialiseDeferredFont;
        String str2 = this.jreFontMap.get(str.toLowerCase(Locale.ENGLISH) + i2);
        if (str2 != null) {
            String str3 = jreFontDirName + File.separator + str2;
            if (this.deferredFontFiles.get(str3) != null && (physicalFontInitialiseDeferredFont = initialiseDeferredFont(str3)) != null && ((physicalFontInitialiseDeferredFont.getFontName(null).equalsIgnoreCase(str) || physicalFontInitialiseDeferredFont.getFamilyName(null).equalsIgnoreCase(str)) && physicalFontInitialiseDeferredFont.style == i2)) {
                return physicalFontInitialiseDeferredFont;
            }
        }
        if (this.noOtherJREFontFiles) {
            return null;
        }
        synchronized (this.jreLucidaFontFiles) {
            if (this.jreOtherFontFiles == null) {
                HashSet hashSet = new HashSet();
                Iterator<String> it = this.deferredFontFiles.keySet().iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    File file = new File(next);
                    String parent = file.getParent();
                    String name = file.getName();
                    if (parent != null && parent.equals(jreFontDirName) && !this.jreLucidaFontFiles.contains(name)) {
                        hashSet.add(next);
                    }
                }
                this.jreOtherFontFiles = (String[]) hashSet.toArray(STR_ARRAY);
                if (this.jreOtherFontFiles.length == 0) {
                    this.noOtherJREFontFiles = true;
                }
            }
            for (int i3 = 0; i3 < this.jreOtherFontFiles.length; i3++) {
                String str4 = this.jreOtherFontFiles[i3];
                if (str4 != null) {
                    this.jreOtherFontFiles[i3] = null;
                    PhysicalFont physicalFontInitialiseDeferredFont2 = initialiseDeferredFont(str4);
                    if (physicalFontInitialiseDeferredFont2 != null && ((physicalFontInitialiseDeferredFont2.getFontName(null).equalsIgnoreCase(str) || physicalFontInitialiseDeferredFont2.getFamilyName(null).equalsIgnoreCase(str)) && physicalFontInitialiseDeferredFont2.style == i2)) {
                        return physicalFontInitialiseDeferredFont2;
                    }
                }
            }
            return null;
        }
    }

    private PhysicalFont findOtherDeferredFont(String str, int i2) {
        Iterator<String> it = this.deferredFontFiles.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            File file = new File(next);
            String parent = file.getParent();
            String name = file.getName();
            if (parent == null || !parent.equals(jreFontDirName) || !this.jreLucidaFontFiles.contains(name)) {
                PhysicalFont physicalFontInitialiseDeferredFont = initialiseDeferredFont(next);
                if (physicalFontInitialiseDeferredFont != null && (physicalFontInitialiseDeferredFont.getFontName(null).equalsIgnoreCase(str) || physicalFontInitialiseDeferredFont.getFamilyName(null).equalsIgnoreCase(str))) {
                    if (physicalFontInitialiseDeferredFont.style == i2) {
                        return physicalFontInitialiseDeferredFont;
                    }
                }
            }
        }
        return null;
    }

    private PhysicalFont findDeferredFont(String str, int i2) {
        PhysicalFont physicalFontFindJREDeferredFont = findJREDeferredFont(str, i2);
        if (physicalFontFindJREDeferredFont != null) {
            return physicalFontFindJREDeferredFont;
        }
        return findOtherDeferredFont(str, i2);
    }

    public void registerDeferredFont(String str, String str2, String[] strArr, int i2, boolean z2, int i3) {
        this.deferredFontFiles.put(str, new FontRegistrationInfo(str2, strArr, i2, z2, i3));
    }

    public synchronized PhysicalFont initialiseDeferredFont(String str) {
        PhysicalFont defaultPhysicalFont;
        if (str == null) {
            return null;
        }
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().info("Opening deferred font file " + str);
        }
        FontRegistrationInfo fontRegistrationInfo = this.deferredFontFiles.get(str);
        if (fontRegistrationInfo != null) {
            this.deferredFontFiles.remove(str);
            defaultPhysicalFont = registerFontFile(fontRegistrationInfo.fontFilePath, fontRegistrationInfo.nativeNames, fontRegistrationInfo.fontFormat, fontRegistrationInfo.javaRasterizer, fontRegistrationInfo.fontRank);
            if (defaultPhysicalFont != null) {
                this.initialisedFonts.put(str, defaultPhysicalFont.handle);
            } else {
                this.initialisedFonts.put(str, getDefaultPhysicalFont().handle);
            }
        } else {
            Font2DHandle font2DHandle = this.initialisedFonts.get(str);
            defaultPhysicalFont = font2DHandle == null ? getDefaultPhysicalFont() : (PhysicalFont) font2DHandle.font2D;
        }
        return defaultPhysicalFont;
    }

    public boolean isRegisteredFontFile(String str) {
        return this.registeredFonts.containsKey(str);
    }

    public PhysicalFont getRegisteredFontFile(String str) {
        return this.registeredFonts.get(str);
    }

    public PhysicalFont registerFontFile(String str, String[] strArr, int i2, boolean z2, int i3) {
        TrueTypeFont trueTypeFont;
        PhysicalFont physicalFont = this.registeredFonts.get(str);
        if (physicalFont != null) {
            return physicalFont;
        }
        PhysicalFont physicalFontAddToFontList = null;
        try {
            switch (i2) {
                case 0:
                    int i4 = 0;
                    do {
                        int i5 = i4;
                        i4++;
                        trueTypeFont = new TrueTypeFont(str, strArr, i5, z2);
                        PhysicalFont physicalFontAddToFontList2 = addToFontList(trueTypeFont, i3);
                        if (physicalFontAddToFontList == null) {
                            physicalFontAddToFontList = physicalFontAddToFontList2;
                        }
                    } while (i4 < trueTypeFont.getFontCount());
                case 1:
                    physicalFontAddToFontList = addToFontList(new Type1Font(str, strArr), i3);
                    break;
                case 5:
                    physicalFontAddToFontList = addToFontList(new NativeFont(str, false), i3);
                    break;
            }
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("Registered file " + str + " as font " + ((Object) physicalFontAddToFontList) + " rank=" + i3);
            }
        } catch (FontFormatException e2) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().warning("Unusable font: " + str + " " + e2.toString());
            }
        }
        if (physicalFontAddToFontList != null && i2 != 5) {
            this.registeredFonts.put(str, physicalFontAddToFontList);
        }
        return physicalFontAddToFontList;
    }

    public void registerFonts(String[] strArr, String[][] strArr2, int i2, int i3, boolean z2, int i4, boolean z3) {
        for (int i5 = 0; i5 < i2; i5++) {
            if (z3) {
                registerDeferredFont(strArr[i5], strArr[i5], strArr2[i5], i3, z2, i4);
            } else {
                registerFontFile(strArr[i5], strArr2[i5], i3, z2, i4);
            }
        }
    }

    public PhysicalFont getDefaultPhysicalFont() {
        if (this.defaultPhysicalFont == null) {
            this.defaultPhysicalFont = (PhysicalFont) findFont2D(lucidaFontName, 0, 0);
            if (this.defaultPhysicalFont == null) {
                this.defaultPhysicalFont = (PhysicalFont) findFont2D("Arial", 0, 0);
            }
            if (this.defaultPhysicalFont == null) {
                Iterator<PhysicalFont> it = this.physicalFonts.values().iterator();
                if (it.hasNext()) {
                    this.defaultPhysicalFont = it.next();
                } else {
                    throw new Error("Probable fatal error:No fonts found.");
                }
            }
        }
        return this.defaultPhysicalFont;
    }

    public Font2D getDefaultLogicalFont(int i2) {
        return findFont2D("dialog", i2, 0);
    }

    private static String dotStyleStr(int i2) {
        switch (i2) {
            case 1:
                return ".bold";
            case 2:
                return ".italic";
            case 3:
                return ".bolditalic";
            default:
                return ".plain";
        }
    }

    protected void populateFontFileNameMap(HashMap<String, String> map, HashMap<String, String> map2, HashMap<String, ArrayList<String>> map3, Locale locale) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v6, types: [java.io.FilenameFilter] */
    private String[] getFontFilesFromPath(boolean z2) {
        TTorT1Filter tTorT1Filter;
        if (z2) {
            tTorT1Filter = ttFilter;
        } else {
            tTorT1Filter = new TTorT1Filter();
        }
        final TTorT1Filter tTorT1Filter2 = tTorT1Filter;
        return (String[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.4
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                if (SunFontManager.this.pathDirs.length == 1) {
                    String[] list = new File(SunFontManager.this.pathDirs[0]).list(tTorT1Filter2);
                    if (list == null) {
                        return new String[0];
                    }
                    for (int i2 = 0; i2 < list.length; i2++) {
                        list[i2] = list[i2].toLowerCase();
                    }
                    return list;
                }
                ArrayList arrayList = new ArrayList();
                for (int i3 = 0; i3 < SunFontManager.this.pathDirs.length; i3++) {
                    String[] list2 = new File(SunFontManager.this.pathDirs[i3]).list(tTorT1Filter2);
                    if (list2 != null) {
                        for (String str : list2) {
                            arrayList.add(str.toLowerCase());
                        }
                    }
                }
                return arrayList.toArray(SunFontManager.STR_ARRAY);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void resolveWindowsFonts() {
        ArrayList<String> arrayList;
        ArrayList arrayList2 = null;
        for (String str : this.fontToFamilyNameMap.keySet()) {
            if (this.fontToFileMap.get(str) == null) {
                if (str.indexOf(Constants.INDENT) > 0) {
                    String strReplaceFirst = str.replaceFirst(Constants.INDENT, " ");
                    String str2 = this.fontToFileMap.get(strReplaceFirst);
                    if (str2 != null && !this.fontToFamilyNameMap.containsKey(strReplaceFirst)) {
                        this.fontToFileMap.remove(strReplaceFirst);
                        this.fontToFileMap.put(str, str2);
                    }
                } else if (str.equals("marlett")) {
                    this.fontToFileMap.put(str, "marlett.ttf");
                } else if (str.equals("david")) {
                    String str3 = this.fontToFileMap.get("david regular");
                    if (str3 != null) {
                        this.fontToFileMap.remove("david regular");
                        this.fontToFileMap.put("david", str3);
                    }
                } else {
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                    }
                    arrayList2.add(str);
                }
            }
        }
        if (arrayList2 != null) {
            HashSet hashSet = new HashSet();
            HashMap map = (HashMap) this.fontToFileMap.clone();
            Iterator<String> it = this.fontToFamilyNameMap.keySet().iterator();
            while (it.hasNext()) {
                map.remove(it.next());
            }
            for (String str4 : map.keySet()) {
                hashSet.add(map.get(str4));
                this.fontToFileMap.remove(str4);
            }
            resolveFontFiles(hashSet, arrayList2);
            if (arrayList2.size() > 0) {
                ArrayList arrayList3 = new ArrayList();
                Iterator<String> it2 = this.fontToFileMap.values().iterator();
                while (it2.hasNext()) {
                    arrayList3.add(it2.next().toLowerCase());
                }
                for (String str5 : getFontFilesFromPath(true)) {
                    if (!arrayList3.contains(str5)) {
                        hashSet.add(str5);
                    }
                }
                resolveFontFiles(hashSet, arrayList2);
            }
            if (arrayList2.size() > 0) {
                int size = arrayList2.size();
                for (int i2 = 0; i2 < size; i2++) {
                    String str6 = (String) arrayList2.get(i2);
                    String str7 = this.fontToFamilyNameMap.get(str6);
                    if (str7 != null && (arrayList = this.familyToFontListMap.get(str7)) != null && arrayList.size() <= 1) {
                        this.familyToFontListMap.remove(str7);
                    }
                    this.fontToFamilyNameMap.remove(str6);
                    if (FontUtilities.isLogging()) {
                        FontUtilities.getLogger().info("No file for font:" + str6);
                    }
                }
            }
        }
    }

    private synchronized void checkForUnreferencedFontFiles() {
        ArrayList<String> arrayList;
        if (this.haveCheckedUnreferencedFontFiles) {
            return;
        }
        this.haveCheckedUnreferencedFontFiles = true;
        if (!FontUtilities.isWindows) {
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator<String> it = this.fontToFileMap.values().iterator();
        while (it.hasNext()) {
            arrayList2.add(it.next().toLowerCase());
        }
        HashMap<String, String> map = null;
        HashMap<String, String> map2 = null;
        HashMap<String, ArrayList<String>> map3 = null;
        for (String str : getFontFilesFromPath(false)) {
            if (!arrayList2.contains(str)) {
                if (FontUtilities.isLogging()) {
                    FontUtilities.getLogger().info("Found non-registry file : " + str);
                }
                PhysicalFont physicalFontRegisterFontFile = registerFontFile(getPathName(str));
                if (physicalFontRegisterFontFile != null) {
                    if (map == null) {
                        map = new HashMap<>(this.fontToFileMap);
                        map2 = new HashMap<>(this.fontToFamilyNameMap);
                        map3 = new HashMap<>(this.familyToFontListMap);
                    }
                    String fontName = physicalFontRegisterFontFile.getFontName(null);
                    String familyName = physicalFontRegisterFontFile.getFamilyName(null);
                    String lowerCase = familyName.toLowerCase();
                    map2.put(fontName, familyName);
                    map.put(fontName, str);
                    ArrayList<String> arrayList3 = map3.get(lowerCase);
                    if (arrayList3 == null) {
                        arrayList = new ArrayList<>();
                    } else {
                        arrayList = new ArrayList<>(arrayList3);
                    }
                    arrayList.add(fontName);
                    map3.put(lowerCase, arrayList);
                }
            }
        }
        if (map != null) {
            this.fontToFileMap = map;
            this.familyToFontListMap = map3;
            this.fontToFamilyNameMap = map2;
        }
    }

    private void resolveFontFiles(HashSet<String> hashSet, ArrayList<String> arrayList) {
        TrueTypeFont trueTypeFont;
        Locale startupLocale = SunToolkit.getStartupLocale();
        Iterator<String> it = hashSet.iterator();
        while (it.hasNext()) {
            String next = it.next();
            try {
                int i2 = 0;
                String pathName = getPathName(next);
                if (FontUtilities.isLogging()) {
                    FontUtilities.getLogger().info("Trying to resolve file " + pathName);
                }
                do {
                    int i3 = i2;
                    i2++;
                    trueTypeFont = new TrueTypeFont(pathName, null, i3, false);
                    String lowerCase = trueTypeFont.getFontName(startupLocale).toLowerCase();
                    if (arrayList.contains(lowerCase)) {
                        this.fontToFileMap.put(lowerCase, next);
                        arrayList.remove(lowerCase);
                        if (FontUtilities.isLogging()) {
                            FontUtilities.getLogger().info("Resolved absent registry entry for " + lowerCase + " located in " + pathName);
                        }
                    }
                } while (i2 < trueTypeFont.getFontCount());
            } catch (Exception e2) {
            }
        }
    }

    public HashMap<String, FamilyDescription> populateHardcodedFileNameMap() {
        return new HashMap<>(0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v64, types: [sun.font.Font2D] */
    /* JADX WARN: Type inference failed for: r0v67, types: [sun.font.Font2D] */
    /* JADX WARN: Type inference failed for: r0v69, types: [sun.font.Font2D] */
    /* JADX WARN: Type inference failed for: r0v72, types: [sun.font.Font2D] */
    Font2D findFontFromPlatformMap(String str, int i2) {
        if (platformFontMap == null) {
            platformFontMap = populateHardcodedFileNameMap();
        }
        if (platformFontMap == null || platformFontMap.size() == 0) {
            return null;
        }
        int iIndexOf = str.indexOf(32);
        String strSubstring = str;
        if (iIndexOf > 0) {
            strSubstring = str.substring(0, iIndexOf);
        }
        FamilyDescription familyDescription = platformFontMap.get(strSubstring);
        if (familyDescription == null) {
            return null;
        }
        int i3 = -1;
        if (str.equalsIgnoreCase(familyDescription.plainFullName)) {
            i3 = 0;
        } else if (str.equalsIgnoreCase(familyDescription.boldFullName)) {
            i3 = 1;
        } else if (str.equalsIgnoreCase(familyDescription.italicFullName)) {
            i3 = 2;
        } else if (str.equalsIgnoreCase(familyDescription.boldItalicFullName)) {
            i3 = 3;
        }
        if (i3 == -1 && !str.equalsIgnoreCase(familyDescription.familyName)) {
            return null;
        }
        String pathName = null;
        String pathName2 = null;
        String pathName3 = null;
        String pathName4 = null;
        boolean z2 = false;
        getPlatformFontDirs(noType1Font);
        if (familyDescription.plainFileName != null) {
            pathName = getPathName(familyDescription.plainFileName);
            if (pathName == null) {
                z2 = true;
            }
        }
        if (familyDescription.boldFileName != null) {
            pathName2 = getPathName(familyDescription.boldFileName);
            if (pathName2 == null) {
                z2 = true;
            }
        }
        if (familyDescription.italicFileName != null) {
            pathName3 = getPathName(familyDescription.italicFileName);
            if (pathName3 == null) {
                z2 = true;
            }
        }
        if (familyDescription.boldItalicFileName != null) {
            pathName4 = getPathName(familyDescription.boldItalicFileName);
            if (pathName4 == null) {
                z2 = true;
            }
        }
        if (z2) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("Hardcoded file missing looking for " + str);
            }
            platformFontMap.remove(strSubstring);
            return null;
        }
        final String[] strArr = {pathName, pathName2, pathName3, pathName4};
        if (((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.font.SunFontManager.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                for (int i4 = 0; i4 < strArr.length; i4++) {
                    if (strArr[i4] != null && !new File(strArr[i4]).exists()) {
                        return Boolean.TRUE;
                    }
                }
                return Boolean.FALSE;
            }
        })).booleanValue()) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("Hardcoded file missing looking for " + str);
            }
            platformFontMap.remove(strSubstring);
            return null;
        }
        PhysicalFont font = null;
        for (int i4 = 0; i4 < strArr.length; i4++) {
            if (strArr[i4] != null) {
                PhysicalFont physicalFontRegisterFontFile = registerFontFile(strArr[i4], null, 0, false, 3);
                if (i4 == i3) {
                    font = physicalFontRegisterFontFile;
                }
            }
        }
        FontFamily family = FontFamily.getFamily(familyDescription.familyName);
        if (family != null) {
            if (font == null) {
                font = family.getFont(i2);
                if (font == null) {
                    font = family.getClosestStyle(i2);
                }
            } else if (i2 > 0 && i2 != font.style) {
                int i5 = i2 | font.style;
                font = family.getFont(i5);
                if (font == null) {
                    font = family.getClosestStyle(i5);
                }
            }
        }
        return font;
    }

    private synchronized HashMap<String, String> getFullNameToFileMap() {
        if (this.fontToFileMap == null) {
            this.pathDirs = getPlatformFontDirs(noType1Font);
            this.fontToFileMap = new HashMap<>(100);
            this.fontToFamilyNameMap = new HashMap<>(100);
            this.familyToFontListMap = new HashMap<>(50);
            populateFontFileNameMap(this.fontToFileMap, this.fontToFamilyNameMap, this.familyToFontListMap, Locale.ENGLISH);
            if (FontUtilities.isWindows) {
                resolveWindowsFonts();
            }
            if (FontUtilities.isLogging()) {
                logPlatformFontInfo();
            }
        }
        return this.fontToFileMap;
    }

    private void logPlatformFontInfo() {
        PlatformLogger logger = FontUtilities.getLogger();
        for (int i2 = 0; i2 < this.pathDirs.length; i2++) {
            logger.info("fontdir=" + this.pathDirs[i2]);
        }
        for (String str : this.fontToFileMap.keySet()) {
            logger.info("font=" + str + " file=" + this.fontToFileMap.get(str));
        }
        for (String str2 : this.fontToFamilyNameMap.keySet()) {
            logger.info("font=" + str2 + " family=" + this.fontToFamilyNameMap.get(str2));
        }
        for (String str3 : this.familyToFontListMap.keySet()) {
            logger.info("family=" + str3 + " fonts=" + ((Object) this.familyToFontListMap.get(str3)));
        }
    }

    protected String[] getFontNamesFromPlatform() {
        if (getFullNameToFileMap().size() == 0) {
            return null;
        }
        checkForUnreferencedFontFiles();
        ArrayList arrayList = new ArrayList();
        Iterator<ArrayList<String>> it = this.familyToFontListMap.values().iterator();
        while (it.hasNext()) {
            Iterator<String> it2 = it.next().iterator();
            while (it2.hasNext()) {
                arrayList.add(it2.next());
            }
        }
        return (String[]) arrayList.toArray(STR_ARRAY);
    }

    public boolean gotFontsFromPlatform() {
        return getFullNameToFileMap().size() != 0;
    }

    public String getFileNameForFontName(String str) {
        return this.fontToFileMap.get(str.toLowerCase(Locale.ENGLISH));
    }

    private PhysicalFont registerFontFile(String str) {
        if (new File(str).isAbsolute() && !this.registeredFonts.contains(str)) {
            int i2 = -1;
            int i3 = 6;
            if (ttFilter.accept(null, str)) {
                i2 = 0;
                i3 = 3;
            } else if (t1Filter.accept(null, str)) {
                i2 = 1;
                i3 = 4;
            }
            if (i2 == -1) {
                return null;
            }
            return registerFontFile(str, null, i2, false, i3);
        }
        return null;
    }

    protected void registerOtherFontFiles(HashSet hashSet) {
        if (getFullNameToFileMap().size() == 0) {
            return;
        }
        Iterator<String> it = this.fontToFileMap.values().iterator();
        while (it.hasNext()) {
            registerFontFile(it.next());
        }
    }

    public boolean getFamilyNamesFromPlatform(TreeMap<String, String> treeMap, Locale locale) {
        if (getFullNameToFileMap().size() == 0) {
            return false;
        }
        checkForUnreferencedFontFiles();
        for (String str : this.fontToFamilyNameMap.values()) {
            treeMap.put(str.toLowerCase(locale), str);
        }
        return true;
    }

    private String getPathName(final String str) {
        if (new File(str).isAbsolute()) {
            return str;
        }
        if (this.pathDirs.length == 1) {
            return this.pathDirs[0] + File.separator + str;
        }
        String str2 = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.font.SunFontManager.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                for (int i2 = 0; i2 < SunFontManager.this.pathDirs.length; i2++) {
                    File file = new File(SunFontManager.this.pathDirs[i2] + File.separator + str);
                    if (file.exists()) {
                        return file.getAbsolutePath();
                    }
                }
                return null;
            }
        });
        if (str2 != null) {
            return str2;
        }
        return str;
    }

    private Font2D findFontFromPlatform(String str, int i2) {
        ArrayList<String> arrayList;
        String lowerCase;
        if (getFullNameToFileMap().size() == 0) {
            return null;
        }
        String str2 = null;
        String str3 = this.fontToFamilyNameMap.get(str);
        if (str3 != null) {
            str2 = this.fontToFileMap.get(str);
            arrayList = this.familyToFontListMap.get(str3.toLowerCase(Locale.ENGLISH));
        } else {
            arrayList = this.familyToFontListMap.get(str);
            if (arrayList != null && arrayList.size() > 0 && (lowerCase = arrayList.get(0).toLowerCase(Locale.ENGLISH)) != null) {
                str3 = this.fontToFamilyNameMap.get(lowerCase);
            }
        }
        if (arrayList == null || str3 == null) {
            return null;
        }
        String[] strArr = (String[]) arrayList.toArray(STR_ARRAY);
        if (strArr.length == 0) {
            return null;
        }
        for (int i3 = 0; i3 < strArr.length; i3++) {
            if (this.fontToFileMap.get(strArr[i3].toLowerCase(Locale.ENGLISH)) == null) {
                if (FontUtilities.isLogging()) {
                    FontUtilities.getLogger().info("Platform lookup : No file for font " + strArr[i3] + " in family " + str3);
                    return null;
                }
                return null;
            }
        }
        PhysicalFont physicalFontRegisterFontFile = null;
        if (str2 != null) {
            physicalFontRegisterFontFile = registerFontFile(getPathName(str2), null, 0, false, 3);
        }
        for (String str4 : strArr) {
            String str5 = this.fontToFileMap.get(str4.toLowerCase(Locale.ENGLISH));
            if (str2 == null || !str2.equals(str5)) {
                registerFontFile(getPathName(str5), null, 0, false, 3);
            }
        }
        Font2D font = null;
        FontFamily family = FontFamily.getFamily(str3);
        if (physicalFontRegisterFontFile != null) {
            i2 |= physicalFontRegisterFontFile.style;
        }
        if (family != null) {
            font = family.getFont(i2);
            if (font == null) {
                font = family.getClosestStyle(i2);
            }
        }
        return font;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v219, types: [sun.font.Font2D] */
    /* JADX WARN: Type inference failed for: r0v229, types: [sun.font.Font2D] */
    /* JADX WARN: Type inference failed for: r0v231, types: [sun.font.Font2D] */
    @Override // sun.font.FontManager
    public Font2D findFont2D(String str, int i2, int i3) {
        Font2D font2D;
        Hashtable<String, FontFamily> hashtable;
        Hashtable<String, Font2D> hashtable2;
        PhysicalFont physicalFontFindDeferredFont;
        PhysicalFont physicalFontFindJREDeferredFont;
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        String str2 = lowerCase + dotStyleStr(i2);
        if (this._usingPerAppContextComposites) {
            ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) AppContext.getAppContext().get(CompositeFont.class);
            if (concurrentHashMap != null) {
                font2D = (Font2D) concurrentHashMap.get(str2);
            } else {
                font2D = null;
            }
        } else {
            font2D = this.fontNameCache.get(str2);
        }
        if (font2D != null) {
            return font2D;
        }
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().info("Search for font: " + str);
        }
        if (FontUtilities.isWindows) {
            if (lowerCase.equals("ms sans serif")) {
                str = "sansserif";
            } else if (lowerCase.equals("ms serif")) {
                str = "serif";
            }
        }
        if (lowerCase.equals("default")) {
            str = "dialog";
        }
        FontFamily family = FontFamily.getFamily(str);
        if (family != null) {
            PhysicalFont fontWithExactStyleMatch = family.getFontWithExactStyleMatch(i2);
            if (fontWithExactStyleMatch == null) {
                fontWithExactStyleMatch = findDeferredFont(str, i2);
            }
            if (fontWithExactStyleMatch == null) {
                fontWithExactStyleMatch = family.getFont(i2);
            }
            if (fontWithExactStyleMatch == null) {
                fontWithExactStyleMatch = family.getClosestStyle(i2);
            }
            if (fontWithExactStyleMatch != null) {
                this.fontNameCache.put(str2, fontWithExactStyleMatch);
                return fontWithExactStyleMatch;
            }
        }
        Font2D font2D2 = this.fullNameToFont.get(lowerCase);
        if (font2D2 != null) {
            if (font2D2.style == i2 || i2 == 0) {
                this.fontNameCache.put(str2, font2D2);
                return font2D2;
            }
            FontFamily family2 = FontFamily.getFamily(font2D2.getFamilyName(null));
            if (family2 != null) {
                Font2D font = family2.getFont(i2 | font2D2.style);
                if (font != null) {
                    this.fontNameCache.put(str2, font);
                    return font;
                }
                Font2D closestStyle = family2.getClosestStyle(i2 | font2D2.style);
                if (closestStyle != null && closestStyle.canDoStyle(i2 | font2D2.style)) {
                    this.fontNameCache.put(str2, closestStyle);
                    return closestStyle;
                }
            }
        }
        if (FontUtilities.isWindows) {
            Font2D font2DFindFontFromPlatformMap = findFontFromPlatformMap(lowerCase, i2);
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("findFontFromPlatformMap returned " + ((Object) font2DFindFontFromPlatformMap));
            }
            if (font2DFindFontFromPlatformMap != null) {
                this.fontNameCache.put(str2, font2DFindFontFromPlatformMap);
                return font2DFindFontFromPlatformMap;
            }
            if (this.deferredFontFiles.size() > 0 && (physicalFontFindJREDeferredFont = findJREDeferredFont(lowerCase, i2)) != null) {
                this.fontNameCache.put(str2, physicalFontFindJREDeferredFont);
                return physicalFontFindJREDeferredFont;
            }
            Font2D font2DFindFontFromPlatform = findFontFromPlatform(lowerCase, i2);
            if (font2DFindFontFromPlatform != null) {
                if (FontUtilities.isLogging()) {
                    FontUtilities.getLogger().info("Found font via platform API for request:\"" + str + "\":, style=" + i2 + " found font: " + ((Object) font2DFindFontFromPlatform));
                }
                this.fontNameCache.put(str2, font2DFindFontFromPlatform);
                return font2DFindFontFromPlatform;
            }
        }
        if (this.deferredFontFiles.size() > 0 && (physicalFontFindDeferredFont = findDeferredFont(str, i2)) != null) {
            this.fontNameCache.put(str2, physicalFontFindDeferredFont);
            return physicalFontFindDeferredFont;
        }
        if (FontUtilities.isSolaris && !this.loaded1dot0Fonts) {
            if (lowerCase.equals("timesroman")) {
                this.fontNameCache.put(str2, findFont2D("serif", i2, i3));
            }
            register1dot0Fonts();
            this.loaded1dot0Fonts = true;
            return findFont2D(str, i2, i3);
        }
        if (this.fontsAreRegistered || this.fontsAreRegisteredPerAppContext) {
            if (this.fontsAreRegistered) {
                hashtable = this.createdByFamilyName;
                hashtable2 = this.createdByFullName;
            } else {
                AppContext appContext = AppContext.getAppContext();
                hashtable = (Hashtable) appContext.get(regFamilyKey);
                hashtable2 = (Hashtable) appContext.get(regFullNameKey);
            }
            FontFamily fontFamily = hashtable.get(lowerCase);
            if (fontFamily != null) {
                Font2D fontWithExactStyleMatch2 = fontFamily.getFontWithExactStyleMatch(i2);
                if (fontWithExactStyleMatch2 == null) {
                    fontWithExactStyleMatch2 = fontFamily.getFont(i2);
                }
                if (fontWithExactStyleMatch2 == null) {
                    fontWithExactStyleMatch2 = fontFamily.getClosestStyle(i2);
                }
                if (fontWithExactStyleMatch2 != null) {
                    if (this.fontsAreRegistered) {
                        this.fontNameCache.put(str2, fontWithExactStyleMatch2);
                    }
                    return fontWithExactStyleMatch2;
                }
            }
            Font2D font2D3 = hashtable2.get(lowerCase);
            if (font2D3 != null) {
                if (this.fontsAreRegistered) {
                    this.fontNameCache.put(str2, font2D3);
                }
                return font2D3;
            }
        }
        if (!this.loadedAllFonts) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("Load fonts looking for:" + str);
            }
            loadFonts();
            this.loadedAllFonts = true;
            return findFont2D(str, i2, i3);
        }
        if (!this.loadedAllFontFiles) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("Load font files looking for:" + str);
            }
            loadFontFiles();
            this.loadedAllFontFiles = true;
            return findFont2D(str, i2, i3);
        }
        Font2D font2DFindFont2DAllLocales = findFont2DAllLocales(str, i2);
        if (font2DFindFont2DAllLocales != null) {
            this.fontNameCache.put(str2, font2DFindFont2DAllLocales);
            return font2DFindFont2DAllLocales;
        }
        if (FontUtilities.isWindows) {
            String fallbackFamilyName = getFontConfiguration().getFallbackFamilyName(str, null);
            if (fallbackFamilyName != null) {
                Font2D font2DFindFont2D = findFont2D(fallbackFamilyName, i2, i3);
                this.fontNameCache.put(str2, font2DFindFont2D);
                return font2DFindFont2D;
            }
        } else {
            if (lowerCase.equals("timesroman")) {
                Font2D font2DFindFont2D2 = findFont2D("serif", i2, i3);
                this.fontNameCache.put(str2, font2DFindFont2D2);
                return font2DFindFont2D2;
            }
            if (lowerCase.equals("helvetica")) {
                Font2D font2DFindFont2D3 = findFont2D("sansserif", i2, i3);
                this.fontNameCache.put(str2, font2DFindFont2D3);
                return font2DFindFont2D3;
            }
            if (lowerCase.equals("courier")) {
                Font2D font2DFindFont2D4 = findFont2D("monospaced", i2, i3);
                this.fontNameCache.put(str2, font2DFindFont2D4);
                return font2DFindFont2D4;
            }
        }
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().info("No font found for:" + str);
        }
        switch (i3) {
            case 1:
                return getDefaultPhysicalFont();
            case 2:
                return getDefaultLogicalFont(i2);
            default:
                return null;
        }
    }

    public boolean usePlatformFontMetrics() {
        return this.usePlatformFontMetrics;
    }

    public int getNumFonts() {
        return this.physicalFonts.size() + this.maxCompFont;
    }

    private static boolean fontSupportsEncoding(Font font, String str) {
        return FontUtilities.getFont2D(font).supportsEncoding(str);
    }

    @Override // sun.font.FontManager
    public Font2D createFont2D(final File file, int i2, boolean z2, final CreatedFontTracker createdFontTracker) throws FontFormatException {
        FileFont type1Font;
        String path = file.getPath();
        boolean z3 = false;
        int i3 = 0;
        synchronized (this) {
            if (this.createdFontCount < maxSoftRefCnt) {
                this.createdFontCount++;
            } else {
                z3 = true;
                i3 = 10;
            }
        }
        try {
            switch (i2) {
                case 0:
                    type1Font = new TrueTypeFont(path, null, 0, true);
                    type1Font.setUseWeakRefs(z3, i3);
                    break;
                case 1:
                    type1Font = new Type1Font(path, null, z2);
                    type1Font.setUseWeakRefs(z3, i3);
                    break;
                default:
                    throw new FontFormatException("Unrecognised Font Format");
            }
            if (z2) {
                type1Font.setFileToRemove(file, createdFontTracker);
                synchronized (FontManager.class) {
                    if (this.tmpFontFiles == null) {
                        this.tmpFontFiles = new Vector<>();
                    }
                    this.tmpFontFiles.add(file);
                    if (this.fileCloser == null) {
                        Runnable runnable = new Runnable() { // from class: sun.font.SunFontManager.8
                            @Override // java.lang.Runnable
                            public void run() {
                                AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.8.1
                                    @Override // java.security.PrivilegedAction
                                    /* renamed from: run */
                                    public Object run2() {
                                        for (int i4 = 0; i4 < 20; i4++) {
                                            if (SunFontManager.this.fontFileCache[i4] != null) {
                                                try {
                                                    SunFontManager.this.fontFileCache[i4].close();
                                                } catch (Exception e2) {
                                                }
                                            }
                                        }
                                        if (SunFontManager.this.tmpFontFiles != null) {
                                            for (File file2 : (File[]) SunFontManager.this.tmpFontFiles.toArray(new File[SunFontManager.this.tmpFontFiles.size()])) {
                                                try {
                                                    file2.delete();
                                                } catch (Exception e3) {
                                                }
                                            }
                                            return null;
                                        }
                                        return null;
                                    }
                                });
                            }
                        };
                        AccessController.doPrivileged(() -> {
                            this.fileCloser = new Thread(ThreadGroupUtils.getRootThreadGroup(), runnable);
                            this.fileCloser.setContextClassLoader(null);
                            Runtime.getRuntime().addShutdownHook(this.fileCloser);
                            return null;
                        });
                    }
                }
            }
            return type1Font;
        } catch (FontFormatException e2) {
            if (z2) {
                AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.7
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() {
                        if (createdFontTracker != null) {
                            createdFontTracker.subBytes((int) file.length());
                        }
                        file.delete();
                        return null;
                    }
                });
            }
            throw e2;
        }
    }

    public synchronized String getFullNameByFileName(String str) {
        PhysicalFont[] physicalFonts = getPhysicalFonts();
        for (int i2 = 0; i2 < physicalFonts.length; i2++) {
            if (physicalFonts[i2].platName.equals(str)) {
                return physicalFonts[i2].getFontName(null);
            }
        }
        return null;
    }

    @Override // sun.font.FontManager
    public synchronized void deRegisterBadFont(Font2D font2D) {
        if (!(font2D instanceof PhysicalFont)) {
            return;
        }
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().severe("Deregister bad font: " + ((Object) font2D));
        }
        replaceFont((PhysicalFont) font2D, getDefaultPhysicalFont());
    }

    public synchronized void replaceFont(PhysicalFont physicalFont, PhysicalFont physicalFont2) {
        if (physicalFont.handle.font2D != physicalFont) {
            return;
        }
        if (physicalFont == physicalFont2) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().severe("Can't replace bad font with itself " + ((Object) physicalFont));
            }
            PhysicalFont[] physicalFonts = getPhysicalFonts();
            int i2 = 0;
            while (true) {
                if (i2 >= physicalFonts.length) {
                    break;
                }
                if (physicalFonts[i2] == physicalFont2) {
                    i2++;
                } else {
                    physicalFont2 = physicalFonts[i2];
                    break;
                }
            }
            if (physicalFont == physicalFont2) {
                if (FontUtilities.isLogging()) {
                    FontUtilities.getLogger().severe("This is bad. No good physicalFonts found.");
                    return;
                }
                return;
            }
        }
        physicalFont.handle.font2D = physicalFont2;
        this.physicalFonts.remove(physicalFont.fullName);
        this.fullNameToFont.remove(physicalFont.fullName.toLowerCase(Locale.ENGLISH));
        FontFamily.remove(physicalFont);
        if (this.localeFullNamesToFont != null) {
            Map.Entry[] entryArr = (Map.Entry[]) this.localeFullNamesToFont.entrySet().toArray(new Map.Entry[0]);
            for (int i3 = 0; i3 < entryArr.length; i3++) {
                if (entryArr[i3].getValue() == physicalFont) {
                    try {
                        entryArr[i3].setValue(physicalFont2);
                    } catch (Exception e2) {
                        this.localeFullNamesToFont.remove(entryArr[i3].getKey());
                    }
                }
            }
        }
        for (int i4 = 0; i4 < this.maxCompFont; i4++) {
            if (physicalFont2.getRank() > 2) {
                this.compFonts[i4].replaceComponentFont(physicalFont, physicalFont2);
            }
        }
    }

    private synchronized void loadLocaleNames() {
        if (this.localeFullNamesToFont != null) {
            return;
        }
        this.localeFullNamesToFont = new HashMap<>();
        Font2D[] registeredFonts = getRegisteredFonts();
        for (int i2 = 0; i2 < registeredFonts.length; i2++) {
            if (registeredFonts[i2] instanceof TrueTypeFont) {
                TrueTypeFont trueTypeFont = (TrueTypeFont) registeredFonts[i2];
                for (String str : trueTypeFont.getAllFullNames()) {
                    this.localeFullNamesToFont.put(str, trueTypeFont);
                }
                FontFamily family = FontFamily.getFamily(trueTypeFont.familyName);
                if (family != null) {
                    FontFamily.addLocaleNames(family, trueTypeFont.getAllFamilyNames());
                }
            }
        }
    }

    private Font2D findFont2DAllLocales(String str, int i2) {
        TrueTypeFont trueTypeFont;
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().info("Searching localised font names for:" + str);
        }
        if (this.localeFullNamesToFont == null) {
            loadLocaleNames();
        }
        FontFamily localeFamily = FontFamily.getLocaleFamily(str.toLowerCase());
        if (localeFamily != null) {
            Font2D font = localeFamily.getFont(i2);
            if (font == null) {
                font = localeFamily.getClosestStyle(i2);
            }
            if (font != null) {
                return font;
            }
        }
        synchronized (this) {
            trueTypeFont = this.localeFullNamesToFont.get(str);
        }
        if (trueTypeFont != null) {
            if (trueTypeFont.style == i2 || i2 == 0) {
                return trueTypeFont;
            }
            FontFamily family = FontFamily.getFamily(trueTypeFont.getFamilyName(null));
            if (family != null) {
                Font2D font2 = family.getFont(i2);
                if (font2 != null) {
                    return font2;
                }
                Font2D closestStyle = family.getClosestStyle(i2);
                if (closestStyle != null) {
                    if (!closestStyle.canDoStyle(i2)) {
                        closestStyle = null;
                    }
                    return closestStyle;
                }
            }
        }
        return trueTypeFont;
    }

    public boolean maybeUsingAlternateCompositeFonts() {
        return this._usingAlternateComposites || this._usingPerAppContextComposites;
    }

    public boolean usingAlternateCompositeFonts() {
        return this._usingAlternateComposites || (this._usingPerAppContextComposites && AppContext.getAppContext().get(CompositeFont.class) != null);
    }

    private static boolean maybeMultiAppContext() {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.9
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return new Boolean(System.getSecurityManager() instanceof AppletSecurity);
            }
        })).booleanValue();
    }

    @Override // sun.font.FontManagerForSGE
    public synchronized void useAlternateFontforJALocales() {
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().info("Entered useAlternateFontforJALocales().");
        }
        if (!FontUtilities.isWindows) {
            return;
        }
        if (!maybeMultiAppContext()) {
            gAltJAFont = true;
        } else {
            AppContext.getAppContext().put(altJAFontKey, altJAFontKey);
        }
    }

    public boolean usingAlternateFontforJALocales() {
        if (maybeMultiAppContext()) {
            return AppContext.getAppContext().get(altJAFontKey) == altJAFontKey;
        }
        return gAltJAFont;
    }

    @Override // sun.font.FontManager
    public synchronized void preferLocaleFonts() {
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().info("Entered preferLocaleFonts().");
        }
        if (!FontConfiguration.willReorderForStartupLocale()) {
            return;
        }
        if (!maybeMultiAppContext()) {
            if (this.gLocalePref) {
                return;
            }
            this.gLocalePref = true;
            createCompositeFonts(this.fontNameCache, this.gLocalePref, this.gPropPref);
            this._usingAlternateComposites = true;
            return;
        }
        AppContext appContext = AppContext.getAppContext();
        if (appContext.get(localeFontKey) == localeFontKey) {
            return;
        }
        appContext.put(localeFontKey, localeFontKey);
        boolean z2 = appContext.get(proportionalFontKey) == proportionalFontKey;
        ConcurrentHashMap<String, Font2D> concurrentHashMap = new ConcurrentHashMap<>();
        appContext.put(CompositeFont.class, concurrentHashMap);
        this._usingPerAppContextComposites = true;
        createCompositeFonts(concurrentHashMap, true, z2);
    }

    @Override // sun.font.FontManager
    public synchronized void preferProportionalFonts() {
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().info("Entered preferProportionalFonts().");
        }
        if (!FontConfiguration.hasMonoToPropMap()) {
            return;
        }
        if (!maybeMultiAppContext()) {
            if (this.gPropPref) {
                return;
            }
            this.gPropPref = true;
            createCompositeFonts(this.fontNameCache, this.gLocalePref, this.gPropPref);
            this._usingAlternateComposites = true;
            return;
        }
        AppContext appContext = AppContext.getAppContext();
        if (appContext.get(proportionalFontKey) == proportionalFontKey) {
            return;
        }
        appContext.put(proportionalFontKey, proportionalFontKey);
        boolean z2 = appContext.get(localeFontKey) == localeFontKey;
        ConcurrentHashMap<String, Font2D> concurrentHashMap = new ConcurrentHashMap<>();
        appContext.put(CompositeFont.class, concurrentHashMap);
        this._usingPerAppContextComposites = true;
        createCompositeFonts(concurrentHashMap, z2, true);
    }

    private static HashSet<String> getInstalledNames() {
        if (installedNames == null) {
            Locale systemStartupLocale = getSystemStartupLocale();
            SunFontManager sunFontManager = getInstance();
            String[] installedFontFamilyNames = sunFontManager.getInstalledFontFamilyNames(systemStartupLocale);
            Font[] allInstalledFonts = sunFontManager.getAllInstalledFonts();
            HashSet<String> hashSet = new HashSet<>();
            for (String str : installedFontFamilyNames) {
                hashSet.add(str.toLowerCase(systemStartupLocale));
            }
            for (Font font : allInstalledFonts) {
                hashSet.add(font.getFontName(systemStartupLocale).toLowerCase(systemStartupLocale));
            }
            installedNames = hashSet;
        }
        return installedNames;
    }

    @Override // sun.font.FontManager
    public boolean registerFont(Font font) {
        Hashtable<String, FontFamily> hashtable;
        Hashtable<String, Font2D> hashtable2;
        if (font == null) {
            return false;
        }
        synchronized (regFamilyKey) {
            if (this.createdByFamilyName == null) {
                this.createdByFamilyName = new Hashtable<>();
                this.createdByFullName = new Hashtable<>();
            }
        }
        if (!FontAccess.getFontAccess().isCreatedFont(font)) {
            return false;
        }
        HashSet<String> installedNames2 = getInstalledNames();
        Locale systemStartupLocale = getSystemStartupLocale();
        String lowerCase = font.getFamily(systemStartupLocale).toLowerCase();
        String lowerCase2 = font.getFontName(systemStartupLocale).toLowerCase();
        if (installedNames2.contains(lowerCase) || installedNames2.contains(lowerCase2)) {
            return false;
        }
        if (!maybeMultiAppContext()) {
            hashtable = this.createdByFamilyName;
            hashtable2 = this.createdByFullName;
            this.fontsAreRegistered = true;
        } else {
            AppContext appContext = AppContext.getAppContext();
            hashtable = (Hashtable) appContext.get(regFamilyKey);
            hashtable2 = (Hashtable) appContext.get(regFullNameKey);
            if (hashtable == null) {
                hashtable = new Hashtable<>();
                hashtable2 = new Hashtable<>();
                appContext.put(regFamilyKey, hashtable);
                appContext.put(regFullNameKey, hashtable2);
            }
            this.fontsAreRegisteredPerAppContext = true;
        }
        Font2D font2D = FontUtilities.getFont2D(font);
        int style = font2D.getStyle();
        FontFamily fontFamily = hashtable.get(lowerCase);
        if (fontFamily == null) {
            fontFamily = new FontFamily(font.getFamily(systemStartupLocale));
            hashtable.put(lowerCase, fontFamily);
        }
        if (this.fontsAreRegistered) {
            removeFromCache(fontFamily.getFont(0));
            removeFromCache(fontFamily.getFont(1));
            removeFromCache(fontFamily.getFont(2));
            removeFromCache(fontFamily.getFont(3));
            removeFromCache(hashtable2.get(lowerCase2));
        }
        fontFamily.setFont(font2D, style);
        hashtable2.put(lowerCase2, font2D);
        return true;
    }

    private void removeFromCache(Font2D font2D) {
        if (font2D == null) {
            return;
        }
        String[] strArr = (String[]) this.fontNameCache.keySet().toArray(STR_ARRAY);
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (this.fontNameCache.get(strArr[i2]) == font2D) {
                this.fontNameCache.remove(strArr[i2]);
            }
        }
    }

    @Override // sun.font.FontManagerForSGE
    public TreeMap<String, String> getCreatedFontFamilyNames() {
        Hashtable<String, FontFamily> hashtable;
        TreeMap<String, String> treeMap;
        if (this.fontsAreRegistered) {
            hashtable = this.createdByFamilyName;
        } else if (this.fontsAreRegisteredPerAppContext) {
            hashtable = (Hashtable) AppContext.getAppContext().get(regFamilyKey);
        } else {
            return null;
        }
        Locale systemStartupLocale = getSystemStartupLocale();
        synchronized (hashtable) {
            treeMap = new TreeMap<>();
            for (FontFamily fontFamily : hashtable.values()) {
                Font2D font = fontFamily.getFont(0);
                if (font == null) {
                    font = fontFamily.getClosestStyle(0);
                }
                String familyName = font.getFamilyName(systemStartupLocale);
                treeMap.put(familyName.toLowerCase(systemStartupLocale), familyName);
            }
        }
        return treeMap;
    }

    @Override // sun.font.FontManagerForSGE
    public Font[] getCreatedFonts() {
        Hashtable<String, Font2D> hashtable;
        Font[] fontArr;
        if (this.fontsAreRegistered) {
            hashtable = this.createdByFullName;
        } else if (this.fontsAreRegisteredPerAppContext) {
            hashtable = (Hashtable) AppContext.getAppContext().get(regFullNameKey);
        } else {
            return null;
        }
        Locale systemStartupLocale = getSystemStartupLocale();
        synchronized (hashtable) {
            fontArr = new Font[hashtable.size()];
            int i2 = 0;
            Iterator<Font2D> it = hashtable.values().iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                fontArr[i3] = new Font(it.next().getFontName(systemStartupLocale), 0, 1);
            }
        }
        return fontArr;
    }

    protected String[] getPlatformFontDirs(boolean z2) {
        if (this.pathDirs != null) {
            return this.pathDirs;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(getPlatformFontPath(z2), File.pathSeparator);
        ArrayList arrayList = new ArrayList();
        while (stringTokenizer.hasMoreTokens()) {
            try {
                arrayList.add(stringTokenizer.nextToken());
            } catch (NoSuchElementException e2) {
            }
        }
        this.pathDirs = (String[]) arrayList.toArray(new String[0]);
        return this.pathDirs;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.lang.String[], java.lang.String[][]] */
    private void addDirFonts(String str, File file, FilenameFilter filenameFilter, int i2, boolean z2, int i3, boolean z3, boolean z4) {
        String str2;
        String[] list = file.list(filenameFilter);
        if (list == null || list.length == 0) {
            return;
        }
        String[] strArr = new String[list.length];
        ?? r0 = new String[list.length];
        int i4 = 0;
        for (int i5 = 0; i5 < list.length; i5++) {
            File file2 = new File(file, list[i5]);
            String canonicalPath = null;
            if (z4) {
                try {
                    canonicalPath = file2.getCanonicalPath();
                } catch (IOException e2) {
                }
            }
            if (canonicalPath == null) {
                canonicalPath = str + File.separator + list[i5];
            }
            if (!this.registeredFontFiles.contains(canonicalPath)) {
                if (this.badFonts != null && this.badFonts.contains(canonicalPath)) {
                    if (FontUtilities.debugFonts()) {
                        FontUtilities.getLogger().warning("skip bad font " + canonicalPath);
                    }
                } else {
                    this.registeredFontFiles.add(canonicalPath);
                    if (FontUtilities.debugFonts() && FontUtilities.getLogger().isLoggable(PlatformLogger.Level.INFO)) {
                        String str3 = "Registering font " + canonicalPath;
                        String[] nativeNames = getNativeNames(canonicalPath, null);
                        if (nativeNames == null) {
                            str2 = str3 + " with no native name";
                        } else {
                            str2 = str3 + " with native name(s) " + nativeNames[0];
                            for (int i6 = 1; i6 < nativeNames.length; i6++) {
                                str2 = str2 + ", " + nativeNames[i6];
                            }
                        }
                        FontUtilities.getLogger().info(str2);
                    }
                    strArr[i4] = canonicalPath;
                    int i7 = i4;
                    i4++;
                    r0[i7] = getNativeNames(canonicalPath, null);
                }
            }
        }
        registerFonts(strArr, r0, i4, i2, z2, i3, z3);
    }

    protected String[] getNativeNames(String str, String str2) {
        return null;
    }

    protected String getFileNameFromPlatformName(String str) {
        return this.fontConfig.getFileNameFromPlatformName(str);
    }

    @Override // sun.java2d.FontSupport
    public FontConfiguration getFontConfiguration() {
        return this.fontConfig;
    }

    public String getPlatformFontPath(boolean z2) {
        if (this.fontPath == null) {
            this.fontPath = getFontPath(z2);
        }
        return this.fontPath;
    }

    public static boolean isOpenJDK() {
        return FontUtilities.isOpenJDK;
    }

    protected void loadFonts() {
        if (this.discoveredAllFonts) {
            return;
        }
        synchronized (this) {
            if (FontUtilities.debugFonts()) {
                Thread.dumpStack();
                FontUtilities.getLogger().info("SunGraphicsEnvironment.loadFonts() called");
            }
            initialiseDeferredFonts();
            AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.10
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    if (SunFontManager.this.fontPath == null) {
                        SunFontManager.this.fontPath = SunFontManager.this.getPlatformFontPath(SunFontManager.noType1Font);
                        SunFontManager.this.registerFontDirs(SunFontManager.this.fontPath);
                    }
                    if (SunFontManager.this.fontPath != null && !SunFontManager.this.gotFontsFromPlatform()) {
                        SunFontManager.this.registerFontsOnPath(SunFontManager.this.fontPath, false, 6, false, true);
                        SunFontManager.this.loadedAllFontFiles = true;
                    }
                    SunFontManager.this.registerOtherFontFiles(SunFontManager.this.registeredFontFiles);
                    SunFontManager.this.discoveredAllFonts = true;
                    return null;
                }
            });
        }
    }

    protected void registerFontDirs(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerFontsOnPath(String str, boolean z2, int i2, boolean z3, boolean z4) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, File.pathSeparator);
        while (stringTokenizer.hasMoreTokens()) {
            try {
                registerFontsInDir(stringTokenizer.nextToken(), z2, i2, z3, z4);
            } catch (NoSuchElementException e2) {
                return;
            }
        }
    }

    public void registerFontsInDir(String str) {
        registerFontsInDir(str, true, 2, true, false);
    }

    protected void registerFontsInDir(String str, boolean z2, int i2, boolean z3, boolean z4) {
        File file = new File(str);
        addDirFonts(str, file, ttFilter, 0, z2, i2 == 6 ? 3 : i2, z3, z4);
        addDirFonts(str, file, t1Filter, 1, z2, i2 == 6 ? 4 : i2, z3, z4);
    }

    protected void registerFontDir(String str) {
    }

    public synchronized String getDefaultFontFile() {
        if (this.defaultFontFileName == null) {
            initDefaultFonts();
        }
        return this.defaultFontFileName;
    }

    private void initDefaultFonts() {
        if (!isOpenJDK()) {
            this.defaultFontName = lucidaFontName;
            if (useAbsoluteFontFileNames()) {
                this.defaultFontFileName = jreFontDirName + File.separator + "LucidaSansRegular.ttf";
            } else {
                this.defaultFontFileName = "LucidaSansRegular.ttf";
            }
        }
    }

    protected boolean useAbsoluteFontFileNames() {
        return true;
    }

    public synchronized String getDefaultFontFaceName() {
        if (this.defaultFontName == null) {
            initDefaultFonts();
        }
        return this.defaultFontName;
    }

    public void loadFontFiles() {
        loadFonts();
        if (this.loadedAllFontFiles) {
            return;
        }
        synchronized (this) {
            if (FontUtilities.debugFonts()) {
                Thread.dumpStack();
                FontUtilities.getLogger().info("loadAllFontFiles() called");
            }
            AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.11
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    if (SunFontManager.this.fontPath == null) {
                        SunFontManager.this.fontPath = SunFontManager.this.getPlatformFontPath(SunFontManager.noType1Font);
                    }
                    if (SunFontManager.this.fontPath != null) {
                        SunFontManager.this.registerFontsOnPath(SunFontManager.this.fontPath, false, 6, false, true);
                    }
                    SunFontManager.this.loadedAllFontFiles = true;
                    return null;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initCompositeFonts(FontConfiguration fontConfiguration, ConcurrentHashMap<String, Font2D> concurrentHashMap) {
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().info("Initialising composite fonts");
        }
        int numberCoreFonts = fontConfiguration.getNumberCoreFonts();
        String[] platformFontNames = fontConfiguration.getPlatformFontNames();
        for (int i2 = 0; i2 < platformFontNames.length; i2++) {
            String str = platformFontNames[i2];
            String fileNameFromPlatformName = getFileNameFromPlatformName(str);
            String[] nativeNames = null;
            if (fileNameFromPlatformName == null || fileNameFromPlatformName.equals(str)) {
                fileNameFromPlatformName = str;
            } else {
                if (i2 < numberCoreFonts) {
                    addFontToPlatformFontPath(str);
                }
                nativeNames = getNativeNames(fileNameFromPlatformName, str);
            }
            registerFontFile(fileNameFromPlatformName, nativeNames, 2, true);
        }
        registerPlatformFontsUsedByFontConfiguration();
        for (CompositeFontDescriptor compositeFontDescriptor : fontConfiguration.get2DCompositeFontInfo()) {
            String[] componentFileNames = compositeFontDescriptor.getComponentFileNames();
            String[] componentFaceNames = compositeFontDescriptor.getComponentFaceNames();
            if (missingFontFiles != null) {
                for (int i3 = 0; i3 < componentFileNames.length; i3++) {
                    if (missingFontFiles.contains(componentFileNames[i3])) {
                        componentFileNames[i3] = getDefaultFontFile();
                        componentFaceNames[i3] = getDefaultFontFaceName();
                    }
                }
            }
            if (concurrentHashMap != null) {
                registerCompositeFont(compositeFontDescriptor.getFaceName(), componentFileNames, componentFaceNames, compositeFontDescriptor.getCoreComponentCount(), compositeFontDescriptor.getExclusionRanges(), compositeFontDescriptor.getExclusionRangeLimits(), true, concurrentHashMap);
            } else {
                registerCompositeFont(compositeFontDescriptor.getFaceName(), componentFileNames, componentFaceNames, compositeFontDescriptor.getCoreComponentCount(), compositeFontDescriptor.getExclusionRanges(), compositeFontDescriptor.getExclusionRangeLimits(), true);
            }
            if (FontUtilities.debugFonts()) {
                FontUtilities.getLogger().info("registered " + compositeFontDescriptor.getFaceName());
            }
        }
    }

    protected void addFontToPlatformFontPath(String str) {
    }

    protected void registerFontFile(String str, String[] strArr, int i2, boolean z2) {
        int i3;
        if (this.registeredFontFiles.contains(str)) {
            return;
        }
        if (ttFilter.accept(null, str)) {
            i3 = 0;
        } else if (t1Filter.accept(null, str)) {
            i3 = 1;
        } else {
            i3 = 5;
        }
        this.registeredFontFiles.add(str);
        if (z2) {
            registerDeferredFont(str, str, strArr, i3, false, i2);
        } else {
            registerFontFile(str, strArr, i3, false, i2);
        }
    }

    protected void registerPlatformFontsUsedByFontConfiguration() {
    }

    protected void addToMissingFontFileList(String str) {
        if (missingFontFiles == null) {
            missingFontFiles = new HashSet<>();
        }
        missingFontFiles.add(str);
    }

    private boolean isNameForRegisteredFile(String str) {
        String fileNameForFontName = getFileNameForFontName(str);
        if (fileNameForFontName == null) {
            return false;
        }
        return this.registeredFontFiles.contains(fileNameForFontName);
    }

    public void createCompositeFonts(ConcurrentHashMap<String, Font2D> concurrentHashMap, boolean z2, boolean z3) {
        initCompositeFonts(createFontConfiguration(z2, z3), concurrentHashMap);
    }

    @Override // sun.font.FontManagerForSGE
    public Font[] getAllInstalledFonts() {
        if (this.allFonts == null) {
            loadFonts();
            TreeMap treeMap = new TreeMap();
            Font2D[] registeredFonts = getRegisteredFonts();
            for (int i2 = 0; i2 < registeredFonts.length; i2++) {
                if (!(registeredFonts[i2] instanceof NativeFont)) {
                    treeMap.put(registeredFonts[i2].getFontName(null), registeredFonts[i2]);
                }
            }
            String[] fontNamesFromPlatform = getFontNamesFromPlatform();
            if (fontNamesFromPlatform != null) {
                for (int i3 = 0; i3 < fontNamesFromPlatform.length; i3++) {
                    if (!isNameForRegisteredFile(fontNamesFromPlatform[i3])) {
                        treeMap.put(fontNamesFromPlatform[i3], null);
                    }
                }
            }
            String[] strArr = null;
            if (treeMap.size() > 0) {
                strArr = new String[treeMap.size()];
                Object[] array = treeMap.keySet().toArray();
                for (int i4 = 0; i4 < array.length; i4++) {
                    strArr[i4] = (String) array[i4];
                }
            }
            Font[] fontArr = new Font[strArr.length];
            for (int i5 = 0; i5 < strArr.length; i5++) {
                fontArr[i5] = new Font(strArr[i5], 0, 1);
                Font2D font2D = (Font2D) treeMap.get(strArr[i5]);
                if (font2D != null) {
                    FontAccess.getFontAccess().setFont2D(fontArr[i5], font2D.handle);
                }
            }
            this.allFonts = fontArr;
        }
        Font[] fontArr2 = new Font[this.allFonts.length];
        System.arraycopy(this.allFonts, 0, fontArr2, 0, this.allFonts.length);
        return fontArr2;
    }

    @Override // sun.font.FontManagerForSGE
    public String[] getInstalledFontFamilyNames(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        if (this.allFamilies != null && this.lastDefaultLocale != null && locale.equals(this.lastDefaultLocale)) {
            String[] strArr = new String[this.allFamilies.length];
            System.arraycopy(this.allFamilies, 0, strArr, 0, this.allFamilies.length);
            return strArr;
        }
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("Serif".toLowerCase(), "Serif");
        treeMap.put("SansSerif".toLowerCase(), "SansSerif");
        treeMap.put("Monospaced".toLowerCase(), "Monospaced");
        treeMap.put(Font.DIALOG.toLowerCase(), Font.DIALOG);
        treeMap.put(Font.DIALOG_INPUT.toLowerCase(), Font.DIALOG_INPUT);
        if (locale.equals(getSystemStartupLocale()) && getFamilyNamesFromPlatform(treeMap, locale)) {
            getJREFontFamilyNames(treeMap, locale);
        } else {
            loadFontFiles();
            PhysicalFont[] physicalFonts = getPhysicalFonts();
            for (int i2 = 0; i2 < physicalFonts.length; i2++) {
                if (!(physicalFonts[i2] instanceof NativeFont)) {
                    String familyName = physicalFonts[i2].getFamilyName(locale);
                    treeMap.put(familyName.toLowerCase(locale), familyName);
                }
            }
        }
        addNativeFontFamilyNames(treeMap, locale);
        String[] strArr2 = new String[treeMap.size()];
        Object[] array = treeMap.keySet().toArray();
        for (int i3 = 0; i3 < array.length; i3++) {
            strArr2[i3] = treeMap.get(array[i3]);
        }
        if (locale.equals(Locale.getDefault())) {
            this.lastDefaultLocale = locale;
            this.allFamilies = new String[strArr2.length];
            System.arraycopy(strArr2, 0, this.allFamilies, 0, this.allFamilies.length);
        }
        return strArr2;
    }

    protected void addNativeFontFamilyNames(TreeMap<String, String> treeMap, Locale locale) {
    }

    public void register1dot0Fonts() {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.12
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                SunFontManager.this.registerFontsInDir("/usr/openwin/lib/X11/fonts/Type1", true, 4, false, false);
                return null;
            }
        });
    }

    protected void getJREFontFamilyNames(TreeMap<String, String> treeMap, Locale locale) {
        registerDeferredJREFonts(jreFontDirName);
        PhysicalFont[] physicalFonts = getPhysicalFonts();
        for (int i2 = 0; i2 < physicalFonts.length; i2++) {
            if (!(physicalFonts[i2] instanceof NativeFont)) {
                String familyName = physicalFonts[i2].getFamilyName(locale);
                treeMap.put(familyName.toLowerCase(locale), familyName);
            }
        }
    }

    private static Locale getSystemStartupLocale() {
        if (systemLocale == null) {
            systemLocale = (Locale) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.SunFontManager.13
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    String property = System.getProperty("file.encoding", "");
                    String property2 = System.getProperty("sun.jnu.encoding");
                    if (property2 != null && !property2.equals(property)) {
                        return Locale.ROOT;
                    }
                    return new Locale(System.getProperty("user.language", "en"), System.getProperty("user.country", ""), System.getProperty("user.variant", ""));
                }
            });
        }
        return systemLocale;
    }

    void addToPool(FileFont fileFont) {
        int i2 = -1;
        synchronized (this.fontFileCache) {
            for (int i3 = 0; i3 < 20; i3++) {
                if (this.fontFileCache[i3] == fileFont) {
                    return;
                }
                if (this.fontFileCache[i3] == null && i2 < 0) {
                    i2 = i3;
                }
            }
            if (i2 >= 0) {
                this.fontFileCache[i2] = fileFont;
                return;
            }
            FileFont fileFont2 = this.fontFileCache[this.lastPoolIndex];
            this.fontFileCache[this.lastPoolIndex] = fileFont;
            this.lastPoolIndex = (this.lastPoolIndex + 1) % 20;
            if (fileFont2 != null) {
                fileFont2.close();
            }
        }
    }

    protected FontUIResource getFontConfigFUIR(String str, int i2, int i3) {
        return new FontUIResource(str, i2, i3);
    }
}
