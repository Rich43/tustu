package com.sun.javafx.font;

import com.sun.javafx.font.FontConfigManager;
import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: jfxrt.jar:com/sun/javafx/font/LogicalFont.class */
public class LogicalFont implements CompositeFontResource {
    public static final String SYSTEM = "System";
    public static final String SERIF = "Serif";
    public static final String SANS_SERIF = "SansSerif";
    public static final String MONOSPACED = "Monospaced";
    public static final String STYLE_REGULAR = "Regular";
    public static final String STYLE_BOLD = "Bold";
    public static final String STYLE_ITALIC = "Italic";
    public static final String STYLE_BOLD_ITALIC = "Bold Italic";
    static final HashMap<String, String> canonicalFamilyMap = new HashMap<>();
    static LogicalFont[] logicalFonts;
    boolean isBold;
    boolean isItalic;
    private String fullName;
    private String familyName;
    private String styleName;
    private String physicalFamily;
    private String physicalFullName;
    private String physicalFileName;
    private FontResource slot0FontResource;
    private ArrayList<String> linkedFontFiles;
    private ArrayList<String> linkedFontNames;
    private FontResource[] fallbacks;
    private FontResource[] nativeFallbacks;
    CompositeGlyphMapper mapper;
    Map<FontStrikeDesc, WeakReference<FontStrike>> strikeMap = new ConcurrentHashMap();
    private static final int SANS_SERIF_INDEX = 0;
    private static final int SERIF_INDEX = 1;
    private static final int MONOSPACED_INDEX = 2;
    private static final int SYSTEM_INDEX = 3;
    static String[][] logFamilies;
    private int hash;

    static {
        canonicalFamilyMap.put("system", SYSTEM);
        canonicalFamilyMap.put("serif", "Serif");
        canonicalFamilyMap.put("sansserif", "SansSerif");
        canonicalFamilyMap.put("sans-serif", "SansSerif");
        canonicalFamilyMap.put("dialog", "SansSerif");
        canonicalFamilyMap.put("default", "SansSerif");
        canonicalFamilyMap.put("monospaced", "Monospaced");
        canonicalFamilyMap.put("monospace", "Monospaced");
        canonicalFamilyMap.put("dialoginput", "Monospaced");
        logicalFonts = new LogicalFont[16];
        logFamilies = (String[][]) null;
    }

    static boolean isLogicalFont(String name) {
        int spaceIndex = name.indexOf(32);
        if (spaceIndex != -1) {
            name = name.substring(0, spaceIndex);
        }
        return canonicalFamilyMap.get(name) != null;
    }

    private static String getCanonicalFamilyName(String name) {
        if (name == null) {
            return "SansSerif";
        }
        String lcName = name.toLowerCase();
        return canonicalFamilyMap.get(lcName);
    }

    static PGFont getLogicalFont(String familyName, boolean bold, boolean italic, float size) {
        int fontIndex;
        String canonicalFamilyName = getCanonicalFamilyName(familyName);
        if (canonicalFamilyName == null) {
            return null;
        }
        if (canonicalFamilyName.equals("SansSerif")) {
            fontIndex = 0;
        } else if (canonicalFamilyName.equals("Serif")) {
            fontIndex = 4;
        } else if (canonicalFamilyName.equals("Monospaced")) {
            fontIndex = 8;
        } else {
            fontIndex = 12;
        }
        if (bold) {
            fontIndex++;
        }
        if (italic) {
            fontIndex += 2;
        }
        LogicalFont font = logicalFonts[fontIndex];
        if (font == null) {
            font = new LogicalFont(canonicalFamilyName, bold, italic);
            logicalFonts[fontIndex] = font;
        }
        return new PrismFont(font, font.getFullName(), size);
    }

    static PGFont getLogicalFont(String fullName, float size) {
        int spaceIndex = fullName.indexOf(32);
        if (spaceIndex == -1 || spaceIndex == fullName.length() - 1) {
            return null;
        }
        String family = fullName.substring(0, spaceIndex);
        String canonicalFamily = getCanonicalFamilyName(family);
        if (canonicalFamily == null) {
            return null;
        }
        String style = fullName.substring(spaceIndex + 1).toLowerCase();
        boolean bold = false;
        boolean italic = false;
        if (!style.equals("regular")) {
            if (style.equals("bold")) {
                bold = true;
            } else if (style.equals("italic")) {
                italic = true;
            } else if (style.equals("bold italic")) {
                bold = true;
                italic = true;
            } else {
                return null;
            }
        }
        return getLogicalFont(canonicalFamily, bold, italic, size);
    }

    private LogicalFont(String family, boolean bold, boolean italic) {
        this.familyName = family;
        this.isBold = bold;
        this.isItalic = italic;
        if (!bold && !italic) {
            this.styleName = STYLE_REGULAR;
        } else if (bold && !italic) {
            this.styleName = STYLE_BOLD;
        } else if (!bold && italic) {
            this.styleName = STYLE_ITALIC;
        } else {
            this.styleName = STYLE_BOLD_ITALIC;
        }
        this.fullName = this.familyName + " " + this.styleName;
        if (PrismFontFactory.isLinux) {
            FontConfigManager.FcCompFont fcCompFont = FontConfigManager.getFontConfigFont(family, bold, italic);
            this.physicalFullName = fcCompFont.firstFont.fullName;
            this.physicalFileName = fcCompFont.firstFont.fontFile;
            return;
        }
        this.physicalFamily = PrismFontFactory.getSystemFont(this.familyName);
    }

    private FontResource getSlot0Resource() {
        if (this.slot0FontResource == null) {
            PrismFontFactory factory = PrismFontFactory.getFontFactory();
            if (this.physicalFamily != null) {
                this.slot0FontResource = factory.getFontResource(this.physicalFamily, this.isBold, this.isItalic, false);
            } else {
                this.slot0FontResource = factory.getFontResource(this.physicalFullName, this.physicalFileName, false);
            }
            if (this.slot0FontResource == null) {
                this.slot0FontResource = factory.getDefaultFontResource(false);
            }
        }
        return this.slot0FontResource;
    }

    private void getLinkedFonts() {
        if (this.fallbacks == null) {
            if (PrismFontFactory.isLinux) {
                FontConfigManager.FcCompFont font = FontConfigManager.getFontConfigFont(this.familyName, this.isBold, this.isItalic);
                this.linkedFontFiles = FontConfigManager.getFileNames(font, true);
                this.linkedFontNames = FontConfigManager.getFontNames(font, true);
            } else {
                ArrayList<String>[] linkedFontInfo = PrismFontFactory.getLinkedFonts("Tahoma", true);
                this.linkedFontFiles = linkedFontInfo[0];
                this.linkedFontNames = linkedFontInfo[1];
            }
            this.fallbacks = new FontResource[this.linkedFontFiles.size()];
        }
    }

    @Override // com.sun.javafx.font.CompositeFontResource
    public int getNumSlots() {
        getLinkedFonts();
        int num = this.linkedFontFiles.size();
        if (this.nativeFallbacks != null) {
            num += this.nativeFallbacks.length;
        }
        return num + 1;
    }

    @Override // com.sun.javafx.font.CompositeFontResource
    public int getSlotForFont(String fontName) {
        FontResource[] tmp;
        getLinkedFonts();
        int i2 = 1;
        Iterator<String> it = this.linkedFontNames.iterator();
        while (it.hasNext()) {
            String linkedFontName = it.next();
            if (fontName.equalsIgnoreCase(linkedFontName)) {
                return i2;
            }
            i2++;
        }
        if (this.nativeFallbacks != null) {
            for (FontResource nativeFallback : this.nativeFallbacks) {
                if (fontName.equalsIgnoreCase(nativeFallback.getFullName())) {
                    return i2;
                }
                i2++;
            }
        }
        if (i2 >= 126) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("\tToo many font fallbacks!");
                return -1;
            }
            return -1;
        }
        PrismFontFactory factory = PrismFontFactory.getFontFactory();
        FontResource fr = factory.getFontResource(fontName, null, false);
        if (fr == null) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("\t Font name not supported \"" + fontName + "\".");
                return -1;
            }
            return -1;
        }
        if (this.nativeFallbacks == null) {
            tmp = new FontResource[1];
        } else {
            tmp = new FontResource[this.nativeFallbacks.length + 1];
            System.arraycopy(this.nativeFallbacks, 0, tmp, 0, this.nativeFallbacks.length);
        }
        tmp[tmp.length - 1] = fr;
        this.nativeFallbacks = tmp;
        return i2;
    }

    @Override // com.sun.javafx.font.CompositeFontResource
    public FontResource getSlotResource(int slot) {
        if (slot == 0) {
            return getSlot0Resource();
        }
        getLinkedFonts();
        int slot2 = slot - 1;
        if (slot2 >= this.fallbacks.length) {
            int slot3 = slot2 - this.fallbacks.length;
            if (this.nativeFallbacks == null || slot3 >= this.nativeFallbacks.length) {
                return null;
            }
            return this.nativeFallbacks[slot3];
        }
        if (this.fallbacks[slot2] == null) {
            String file = this.linkedFontFiles.get(slot2);
            String name = this.linkedFontNames.get(slot2);
            this.fallbacks[slot2] = PrismFontFactory.getFontFactory().getFontResource(name, file, false);
            if (this.fallbacks[slot2] == null) {
                this.fallbacks[slot2] = getSlot0Resource();
            }
        }
        return this.fallbacks[slot2];
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFullName() {
        return this.fullName;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getPSName() {
        return this.fullName;
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
        return this.fullName;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleFamilyName() {
        return this.familyName;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleStyleName() {
        return this.styleName;
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isBold() {
        return getSlotResource(0).isBold();
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isItalic() {
        return getSlotResource(0).isItalic();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFileName() {
        return getSlotResource(0).getFileName();
    }

    @Override // com.sun.javafx.font.FontResource
    public int getFeatures() {
        return getSlotResource(0).getFeatures();
    }

    @Override // com.sun.javafx.font.FontResource
    public Object getPeer() {
        return null;
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isEmbeddedFont() {
        return getSlotResource(0).isEmbeddedFont();
    }

    @Override // com.sun.javafx.font.FontResource
    public void setPeer(Object peer) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override // com.sun.javafx.font.FontResource
    public float[] getGlyphBoundingBox(int glyphCode, float size, float[] retArr) {
        int slot = glyphCode >>> 24;
        int slotglyphCode = glyphCode & 16777215;
        FontResource slotResource = getSlotResource(slot);
        return slotResource.getGlyphBoundingBox(slotglyphCode, size, retArr);
    }

    @Override // com.sun.javafx.font.FontResource
    public float getAdvance(int glyphCode, float size) {
        int slot = glyphCode >>> 24;
        int slotglyphCode = glyphCode & 16777215;
        FontResource slotResource = getSlotResource(slot);
        return slotResource.getAdvance(slotglyphCode, size);
    }

    @Override // com.sun.javafx.font.FontResource
    public CharToGlyphMapper getGlyphMapper() {
        if (this.mapper == null) {
            this.mapper = new CompositeGlyphMapper(this);
        }
        return this.mapper;
    }

    @Override // com.sun.javafx.font.FontResource
    public Map<FontStrikeDesc, WeakReference<FontStrike>> getStrikeMap() {
        return this.strikeMap;
    }

    @Override // com.sun.javafx.font.FontResource
    public int getDefaultAAMode() {
        return getSlot0Resource().getDefaultAAMode();
    }

    @Override // com.sun.javafx.font.FontResource
    public FontStrike getStrike(float size, BaseTransform transform) {
        return getStrike(size, transform, getDefaultAAMode());
    }

    @Override // com.sun.javafx.font.FontResource
    public FontStrike getStrike(float size, BaseTransform transform, int aaMode) {
        WeakReference<FontStrike> ref;
        FontStrikeDesc desc = new FontStrikeDesc(size, transform, aaMode);
        WeakReference<FontStrike> ref2 = this.strikeMap.get(desc);
        CompositeStrike strike = null;
        if (ref2 != null) {
            strike = (CompositeStrike) ref2.get();
        }
        if (strike == null) {
            strike = new CompositeStrike(this, size, transform, aaMode, desc);
            if (strike.disposer != null) {
                ref = Disposer.addRecord(strike, strike.disposer);
            } else {
                ref = new WeakReference<>(strike);
            }
            this.strikeMap.put(desc, ref);
        }
        return strike;
    }

    private static void buildFamily(String[] fullNames, String family) {
        fullNames[0] = family + " " + STYLE_REGULAR;
        fullNames[1] = family + " " + STYLE_BOLD;
        fullNames[2] = family + " " + STYLE_ITALIC;
        fullNames[3] = family + " " + STYLE_BOLD_ITALIC;
    }

    private static void buildFamilies() {
        if (logFamilies == null) {
            String[][] tmpFamilies = new String[4][4];
            buildFamily(tmpFamilies[0], "SansSerif");
            buildFamily(tmpFamilies[1], "Serif");
            buildFamily(tmpFamilies[2], "Monospaced");
            buildFamily(tmpFamilies[3], SYSTEM);
            logFamilies = tmpFamilies;
        }
    }

    static void addFamilies(ArrayList<String> familyList) {
        familyList.add("SansSerif");
        familyList.add("Serif");
        familyList.add("Monospaced");
        familyList.add(SYSTEM);
    }

    static void addFullNames(ArrayList<String> fullNames) {
        buildFamilies();
        for (int f2 = 0; f2 < logFamilies.length; f2++) {
            for (int n2 = 0; n2 < logFamilies[f2].length; n2++) {
                fullNames.add(logFamilies[f2][n2]);
            }
        }
    }

    static String[] getFontsInFamily(String family) {
        String canonicalFamily = getCanonicalFamilyName(family);
        if (canonicalFamily == null) {
            return null;
        }
        buildFamilies();
        if (canonicalFamily.equals("SansSerif")) {
            return logFamilies[0];
        }
        if (canonicalFamily.equals("Serif")) {
            return logFamilies[1];
        }
        if (canonicalFamily.equals("Monospaced")) {
            return logFamilies[2];
        }
        return logFamilies[3];
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof LogicalFont)) {
            return false;
        }
        LogicalFont other = (LogicalFont) obj;
        return this.fullName.equals(other.fullName);
    }

    public int hashCode() {
        if (this.hash != 0) {
            return this.hash;
        }
        this.hash = this.fullName.hashCode();
        return this.hash;
    }
}
