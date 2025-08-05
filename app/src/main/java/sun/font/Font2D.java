package sun.font;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:sun/font/Font2D.class */
public abstract class Font2D {
    public static final int FONT_CONFIG_RANK = 2;
    public static final int JRE_RANK = 2;
    public static final int TTF_RANK = 3;
    public static final int TYPE1_RANK = 4;
    public static final int NATIVE_RANK = 5;
    public static final int UNKNOWN_RANK = 6;
    public static final int DEFAULT_RANK = 4;
    private static final String[] boldNames = {"bold", "demibold", "demi-bold", "demi bold", "negreta", "demi"};
    private static final String[] italicNames = {"italic", "cursiva", "oblique", "inclined"};
    private static final String[] boldItalicNames = {"bolditalic", "bold-italic", "bold italic", "boldoblique", "bold-oblique", "bold oblique", "demibold italic", "negreta cursiva", "demi oblique"};
    private static final FontRenderContext DEFAULT_FRC = new FontRenderContext((AffineTransform) null, false, false);
    public Font2DHandle handle;
    protected String familyName;
    protected String fullName;
    protected FontFamily family;
    protected CharToGlyphMapper mapper;
    private boolean useWeak;
    public static final int FWIDTH_NORMAL = 5;
    public static final int FWEIGHT_NORMAL = 400;
    public static final int FWEIGHT_BOLD = 700;
    protected int style = 0;
    protected int fontRank = 4;
    protected ConcurrentHashMap<FontStrikeDesc, Reference> strikeCache = new ConcurrentHashMap<>();
    protected Reference<FontStrike> lastFontStrike = new WeakReference(null);
    private int strikeCacheMax = 0;

    abstract CharToGlyphMapper getMapper();

    abstract FontStrike createStrike(FontStrikeDesc fontStrikeDesc);

    void setUseWeakRefs(boolean z2, int i2) {
        this.useWeak = z2;
        this.strikeCacheMax = (!z2 || i2 <= 0) ? 0 : i2;
    }

    public int getStyle() {
        return this.style;
    }

    protected void setStyle() {
        String lowerCase = this.fullName.toLowerCase();
        for (int i2 = 0; i2 < boldItalicNames.length; i2++) {
            if (lowerCase.indexOf(boldItalicNames[i2]) != -1) {
                this.style = 3;
                return;
            }
        }
        for (int i3 = 0; i3 < italicNames.length; i3++) {
            if (lowerCase.indexOf(italicNames[i3]) != -1) {
                this.style = 2;
                return;
            }
        }
        for (int i4 = 0; i4 < boldNames.length; i4++) {
            if (lowerCase.indexOf(boldNames[i4]) != -1) {
                this.style = 1;
                return;
            }
        }
    }

    public int getWidth() {
        return 5;
    }

    public int getWeight() {
        if ((this.style & 1) != 0) {
            return FWEIGHT_BOLD;
        }
        return 400;
    }

    int getRank() {
        return this.fontRank;
    }

    void setRank(int i2) {
        this.fontRank = i2;
    }

    protected int getValidatedGlyphCode(int i2) {
        if (i2 < 0 || i2 >= getMapper().getNumGlyphs()) {
            i2 = getMapper().getMissingGlyphCode();
        }
        return i2;
    }

    public FontStrike getStrike(Font font) {
        FontStrike fontStrike = this.lastFontStrike.get();
        if (fontStrike != null) {
            return fontStrike;
        }
        return getStrike(font, DEFAULT_FRC);
    }

    public FontStrike getStrike(Font font, AffineTransform affineTransform, int i2, int i3) {
        double size2D = font.getSize2D();
        AffineTransform affineTransform2 = (AffineTransform) affineTransform.clone();
        affineTransform2.scale(size2D, size2D);
        if (font.isTransformed()) {
            affineTransform2.concatenate(font.getTransform());
        }
        if (affineTransform2.getTranslateX() != 0.0d || affineTransform2.getTranslateY() != 0.0d) {
            affineTransform2.setTransform(affineTransform2.getScaleX(), affineTransform2.getShearY(), affineTransform2.getShearX(), affineTransform2.getScaleY(), 0.0d, 0.0d);
        }
        return getStrike(new FontStrikeDesc(affineTransform, affineTransform2, font.getStyle(), i2, i3), false);
    }

    public FontStrike getStrike(Font font, AffineTransform affineTransform, AffineTransform affineTransform2, int i2, int i3) {
        return getStrike(new FontStrikeDesc(affineTransform, affineTransform2, font.getStyle(), i2, i3), false);
    }

    public FontStrike getStrike(Font font, FontRenderContext fontRenderContext) {
        AffineTransform transform = fontRenderContext.getTransform();
        double size2D = font.getSize2D();
        transform.scale(size2D, size2D);
        if (font.isTransformed()) {
            transform.concatenate(font.getTransform());
            if (transform.getTranslateX() != 0.0d || transform.getTranslateY() != 0.0d) {
                transform.setTransform(transform.getScaleX(), transform.getShearY(), transform.getShearX(), transform.getScaleY(), 0.0d, 0.0d);
            }
        }
        return getStrike(new FontStrikeDesc(fontRenderContext.getTransform(), transform, font.getStyle(), FontStrikeDesc.getAAHintIntVal(this, font, fontRenderContext), FontStrikeDesc.getFMHintIntVal(fontRenderContext.getFractionalMetricsHint())), false);
    }

    void updateLastStrikeRef(FontStrike fontStrike) {
        this.lastFontStrike.clear();
        if (this.useWeak) {
            this.lastFontStrike = new WeakReference(fontStrike);
        } else {
            this.lastFontStrike = new SoftReference(fontStrike);
        }
    }

    FontStrike getStrike(FontStrikeDesc fontStrikeDesc) {
        return getStrike(fontStrikeDesc, true);
    }

    private FontStrike getStrike(FontStrikeDesc fontStrikeDesc, boolean z2) {
        Reference strikeRef;
        FontStrike fontStrike;
        FontStrike fontStrike2 = this.lastFontStrike.get();
        if (fontStrike2 != null && fontStrikeDesc.equals(fontStrike2.desc)) {
            return fontStrike2;
        }
        Reference reference = this.strikeCache.get(fontStrikeDesc);
        if (reference != null && (fontStrike = (FontStrike) reference.get()) != null) {
            updateLastStrikeRef(fontStrike);
            StrikeCache.refStrike(fontStrike);
            return fontStrike;
        }
        if (z2) {
            fontStrikeDesc = new FontStrikeDesc(fontStrikeDesc);
        }
        FontStrike fontStrikeCreateStrike = createStrike(fontStrikeDesc);
        int type = fontStrikeDesc.glyphTx.getType();
        if (this.useWeak || type == 32 || ((type & 16) != 0 && this.strikeCache.size() > 10)) {
            strikeRef = StrikeCache.getStrikeRef(fontStrikeCreateStrike, true);
        } else {
            strikeRef = StrikeCache.getStrikeRef(fontStrikeCreateStrike, this.useWeak);
        }
        this.strikeCache.put(fontStrikeDesc, strikeRef);
        updateLastStrikeRef(fontStrikeCreateStrike);
        StrikeCache.refStrike(fontStrikeCreateStrike);
        return fontStrikeCreateStrike;
    }

    public void getFontMetrics(Font font, AffineTransform affineTransform, Object obj, Object obj2, float[] fArr) {
        StrikeMetrics fontMetrics = getStrike(font, affineTransform, FontStrikeDesc.getAAHintIntVal(obj, this, font.getSize()), FontStrikeDesc.getFMHintIntVal(obj2)).getFontMetrics();
        fArr[0] = fontMetrics.getAscent();
        fArr[1] = fontMetrics.getDescent();
        fArr[2] = fontMetrics.getLeading();
        fArr[3] = fontMetrics.getMaxAdvance();
        getStyleMetrics(font.getSize2D(), fArr, 4);
    }

    public void getStyleMetrics(float f2, float[] fArr, int i2) {
        fArr[i2] = (-fArr[0]) / 2.5f;
        fArr[i2 + 1] = f2 / 12.0f;
        fArr[i2 + 2] = fArr[i2 + 1] / 1.5f;
        fArr[i2 + 3] = fArr[i2 + 1];
    }

    public void getFontMetrics(Font font, FontRenderContext fontRenderContext, float[] fArr) {
        StrikeMetrics fontMetrics = getStrike(font, fontRenderContext).getFontMetrics();
        fArr[0] = fontMetrics.getAscent();
        fArr[1] = fontMetrics.getDescent();
        fArr[2] = fontMetrics.getLeading();
        fArr[3] = fontMetrics.getMaxAdvance();
    }

    protected byte[] getTableBytes(int i2) {
        return null;
    }

    protected long getLayoutTableCache() {
        return 0L;
    }

    protected long getUnitsPerEm() {
        return 2048L;
    }

    boolean supportsEncoding(String str) {
        return false;
    }

    public boolean canDoStyle(int i2) {
        return i2 == this.style;
    }

    public boolean useAAForPtSize(int i2) {
        return true;
    }

    public boolean hasSupplementaryChars() {
        return false;
    }

    public String getPostscriptName() {
        return this.fullName;
    }

    public String getFontName(Locale locale) {
        return this.fullName;
    }

    public String getFamilyName(Locale locale) {
        return this.familyName;
    }

    public int getNumGlyphs() {
        return getMapper().getNumGlyphs();
    }

    public int charToGlyph(int i2) {
        return getMapper().charToGlyph(i2);
    }

    public int getMissingGlyphCode() {
        return getMapper().getMissingGlyphCode();
    }

    public boolean canDisplay(char c2) {
        return getMapper().canDisplay(c2);
    }

    public boolean canDisplay(int i2) {
        return getMapper().canDisplay(i2);
    }

    public byte getBaselineFor(char c2) {
        return (byte) 0;
    }

    public float getItalicAngle(Font font, AffineTransform affineTransform, Object obj, Object obj2) {
        StrikeMetrics fontMetrics = getStrike(font, affineTransform, FontStrikeDesc.getAAHintIntVal(obj, this, 12), FontStrikeDesc.getFMHintIntVal(obj2)).getFontMetrics();
        if (fontMetrics.ascentY == 0.0f || fontMetrics.ascentX == 0.0f) {
            return 0.0f;
        }
        return fontMetrics.ascentX / (-fontMetrics.ascentY);
    }
}
