package sun.font;

import java.security.AccessController;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/font/CompositeFont.class */
public final class CompositeFont extends Font2D {
    private boolean[] deferredInitialisation;
    String[] componentFileNames;
    String[] componentNames;
    private PhysicalFont[] components;
    int numSlots;
    int numMetricsSlots;
    int[] exclusionRanges;
    int[] maxIndices;
    int numGlyphs;
    int localeSlot;
    boolean isStdComposite;

    public CompositeFont(String str, String[] strArr, String[] strArr2, int i2, int[] iArr, int[] iArr2, boolean z2, SunFontManager sunFontManager) {
        this.numGlyphs = 0;
        this.localeSlot = -1;
        this.isStdComposite = true;
        this.handle = new Font2DHandle(this);
        this.fullName = str;
        this.componentFileNames = strArr;
        this.componentNames = strArr2;
        if (strArr2 == null) {
            this.numSlots = this.componentFileNames.length;
        } else {
            this.numSlots = this.componentNames.length;
        }
        this.numSlots = this.numSlots <= 254 ? this.numSlots : 254;
        this.numMetricsSlots = i2;
        this.exclusionRanges = iArr;
        this.maxIndices = iArr2;
        if (sunFontManager.getEUDCFont() != null) {
            int i3 = this.numMetricsSlots;
            int i4 = this.numSlots - i3;
            this.numSlots++;
            if (this.componentNames != null) {
                this.componentNames = new String[this.numSlots];
                System.arraycopy(strArr2, 0, this.componentNames, 0, i3);
                this.componentNames[i3] = sunFontManager.getEUDCFont().getFontName(null);
                System.arraycopy(strArr2, i3, this.componentNames, i3 + 1, i4);
            }
            if (this.componentFileNames != null) {
                this.componentFileNames = new String[this.numSlots];
                System.arraycopy(strArr, 0, this.componentFileNames, 0, i3);
                System.arraycopy(strArr, i3, this.componentFileNames, i3 + 1, i4);
            }
            this.components = new PhysicalFont[this.numSlots];
            this.components[i3] = sunFontManager.getEUDCFont();
            this.deferredInitialisation = new boolean[this.numSlots];
            if (z2) {
                for (int i5 = 0; i5 < this.numSlots - 1; i5++) {
                    this.deferredInitialisation[i5] = true;
                }
            }
        } else {
            this.components = new PhysicalFont[this.numSlots];
            this.deferredInitialisation = new boolean[this.numSlots];
            if (z2) {
                for (int i6 = 0; i6 < this.numSlots; i6++) {
                    this.deferredInitialisation[i6] = true;
                }
            }
        }
        this.fontRank = 2;
        int iIndexOf = this.fullName.indexOf(46);
        if (iIndexOf > 0) {
            this.familyName = this.fullName.substring(0, iIndexOf);
            if (iIndexOf + 1 < this.fullName.length()) {
                String strSubstring = this.fullName.substring(iIndexOf + 1);
                if ("plain".equals(strSubstring)) {
                    this.style = 0;
                    return;
                }
                if ("bold".equals(strSubstring)) {
                    this.style = 1;
                    return;
                } else if ("italic".equals(strSubstring)) {
                    this.style = 2;
                    return;
                } else {
                    if ("bolditalic".equals(strSubstring)) {
                        this.style = 3;
                        return;
                    }
                    return;
                }
            }
            return;
        }
        this.familyName = this.fullName;
    }

    CompositeFont(PhysicalFont[] physicalFontArr) {
        this.numGlyphs = 0;
        this.localeSlot = -1;
        this.isStdComposite = true;
        this.isStdComposite = false;
        this.handle = new Font2DHandle(this);
        this.fullName = physicalFontArr[0].fullName;
        this.familyName = physicalFontArr[0].familyName;
        this.style = physicalFontArr[0].style;
        this.numMetricsSlots = 1;
        this.numSlots = physicalFontArr.length;
        this.components = new PhysicalFont[this.numSlots];
        System.arraycopy(physicalFontArr, 0, this.components, 0, this.numSlots);
        this.deferredInitialisation = new boolean[this.numSlots];
    }

    CompositeFont(PhysicalFont physicalFont, CompositeFont compositeFont) {
        this.numGlyphs = 0;
        this.localeSlot = -1;
        this.isStdComposite = true;
        this.isStdComposite = false;
        this.handle = new Font2DHandle(this);
        this.fullName = physicalFont.fullName;
        this.familyName = physicalFont.familyName;
        this.style = physicalFont.style;
        this.numMetricsSlots = 1;
        this.numSlots = compositeFont.numSlots + 1;
        synchronized (FontManagerFactory.getInstance()) {
            this.components = new PhysicalFont[this.numSlots];
            this.components[0] = physicalFont;
            System.arraycopy(compositeFont.components, 0, this.components, 1, compositeFont.numSlots);
            if (compositeFont.componentNames != null) {
                this.componentNames = new String[this.numSlots];
                this.componentNames[0] = physicalFont.fullName;
                System.arraycopy(compositeFont.componentNames, 0, this.componentNames, 1, compositeFont.numSlots);
            }
            if (compositeFont.componentFileNames != null) {
                this.componentFileNames = new String[this.numSlots];
                this.componentFileNames[0] = null;
                System.arraycopy(compositeFont.componentFileNames, 0, this.componentFileNames, 1, compositeFont.numSlots);
            }
            this.deferredInitialisation = new boolean[this.numSlots];
            this.deferredInitialisation[0] = false;
            System.arraycopy(compositeFont.deferredInitialisation, 0, this.deferredInitialisation, 1, compositeFont.numSlots);
        }
    }

    private void doDeferredInitialisation(int i2) {
        if (!this.deferredInitialisation[i2]) {
            return;
        }
        SunFontManager sunFontManager = SunFontManager.getInstance();
        synchronized (sunFontManager) {
            if (this.componentNames == null) {
                this.componentNames = new String[this.numSlots];
            }
            if (this.components[i2] == null) {
                if (this.componentFileNames != null && this.componentFileNames[i2] != null) {
                    this.components[i2] = sunFontManager.initialiseDeferredFont(this.componentFileNames[i2]);
                }
                if (this.components[i2] == null) {
                    this.components[i2] = sunFontManager.getDefaultPhysicalFont();
                }
                String fontName = this.components[i2].getFontName(null);
                if (this.componentNames[i2] == null) {
                    this.componentNames[i2] = fontName;
                } else if (!this.componentNames[i2].equalsIgnoreCase(fontName)) {
                    try {
                        this.components[i2] = (PhysicalFont) sunFontManager.findFont2D(this.componentNames[i2], this.style, 1);
                    } catch (ClassCastException e2) {
                        this.components[i2] = sunFontManager.getDefaultPhysicalFont();
                    }
                }
                this.deferredInitialisation[i2] = false;
            } else {
                this.deferredInitialisation[i2] = false;
            }
        }
    }

    void replaceComponentFont(PhysicalFont physicalFont, PhysicalFont physicalFont2) {
        if (this.components == null) {
            return;
        }
        for (int i2 = 0; i2 < this.numSlots; i2++) {
            if (this.components[i2] == physicalFont) {
                this.components[i2] = physicalFont2;
                if (this.componentNames != null) {
                    this.componentNames[i2] = physicalFont2.getFontName(null);
                }
            }
        }
    }

    public boolean isExcludedChar(int i2, int i3) {
        if (this.exclusionRanges == null || this.maxIndices == null || i2 >= this.numMetricsSlots) {
            return false;
        }
        int i4 = 0;
        int i5 = this.maxIndices[i2];
        if (i2 > 0) {
            i4 = this.maxIndices[i2 - 1];
        }
        for (int i6 = i4; i5 > i6; i6 += 2) {
            if (i3 >= this.exclusionRanges[i6] && i3 <= this.exclusionRanges[i6 + 1]) {
                return true;
            }
        }
        return false;
    }

    @Override // sun.font.Font2D
    public void getStyleMetrics(float f2, float[] fArr, int i2) {
        PhysicalFont slotFont = getSlotFont(0);
        if (slotFont == null) {
            super.getStyleMetrics(f2, fArr, i2);
        } else {
            slotFont.getStyleMetrics(f2, fArr, i2);
        }
    }

    public int getNumSlots() {
        return this.numSlots;
    }

    public PhysicalFont getSlotFont(int i2) {
        if (this.deferredInitialisation[i2]) {
            doDeferredInitialisation(i2);
        }
        SunFontManager sunFontManager = SunFontManager.getInstance();
        try {
            PhysicalFont defaultPhysicalFont = this.components[i2];
            if (defaultPhysicalFont == null) {
                try {
                    defaultPhysicalFont = (PhysicalFont) sunFontManager.findFont2D(this.componentNames[i2], this.style, 1);
                    this.components[i2] = defaultPhysicalFont;
                } catch (ClassCastException e2) {
                    defaultPhysicalFont = sunFontManager.getDefaultPhysicalFont();
                }
            }
            return defaultPhysicalFont;
        } catch (Exception e3) {
            return sunFontManager.getDefaultPhysicalFont();
        }
    }

    @Override // sun.font.Font2D
    FontStrike createStrike(FontStrikeDesc fontStrikeDesc) {
        return new CompositeStrike(this, fontStrikeDesc);
    }

    public boolean isStdComposite() {
        return this.isStdComposite;
    }

    @Override // sun.font.Font2D
    protected int getValidatedGlyphCode(int i2) {
        int i3 = i2 >>> 24;
        if (i3 >= this.numSlots) {
            return getMapper().getMissingGlyphCode();
        }
        PhysicalFont slotFont = getSlotFont(i3);
        if (slotFont.getValidatedGlyphCode(i2 & 16777215) == slotFont.getMissingGlyphCode()) {
            return getMapper().getMissingGlyphCode();
        }
        return i2;
    }

    @Override // sun.font.Font2D
    public CharToGlyphMapper getMapper() {
        if (this.mapper == null) {
            this.mapper = new CompositeGlyphMapper(this);
        }
        return this.mapper;
    }

    @Override // sun.font.Font2D
    public boolean hasSupplementaryChars() {
        for (int i2 = 0; i2 < this.numSlots; i2++) {
            if (getSlotFont(i2).hasSupplementaryChars()) {
                return true;
            }
        }
        return false;
    }

    @Override // sun.font.Font2D
    public int getNumGlyphs() {
        if (this.numGlyphs == 0) {
            this.numGlyphs = getMapper().getNumGlyphs();
        }
        return this.numGlyphs;
    }

    @Override // sun.font.Font2D
    public int getMissingGlyphCode() {
        return getMapper().getMissingGlyphCode();
    }

    @Override // sun.font.Font2D
    public boolean canDisplay(char c2) {
        return getMapper().canDisplay(c2);
    }

    @Override // sun.font.Font2D
    public boolean useAAForPtSize(int i2) {
        if (this.localeSlot == -1) {
            int i3 = this.numMetricsSlots;
            if (i3 == 1 && !isStdComposite()) {
                i3 = this.numSlots;
            }
            int i4 = 0;
            while (true) {
                if (i4 >= i3) {
                    break;
                }
                if (!getSlotFont(i4).supportsEncoding(null)) {
                    i4++;
                } else {
                    this.localeSlot = i4;
                    break;
                }
            }
            if (this.localeSlot == -1) {
                this.localeSlot = 0;
            }
        }
        return getSlotFont(this.localeSlot).useAAForPtSize(i2);
    }

    public String toString() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("line.separator"));
        String str2 = "";
        for (int i2 = 0; i2 < this.numSlots; i2++) {
            str2 = str2 + "    Slot[" + i2 + "]=" + ((Object) getSlotFont(i2)) + str;
        }
        return "** Composite Font: Family=" + this.familyName + " Name=" + this.fullName + " style=" + this.style + str + str2;
    }
}
