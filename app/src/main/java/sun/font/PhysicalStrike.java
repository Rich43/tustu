package sun.font;

import java.awt.geom.Point2D;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:sun/font/PhysicalStrike.class */
public abstract class PhysicalStrike extends FontStrike {
    static final long INTMASK = 4294967295L;
    static boolean longAddresses;
    private PhysicalFont physicalFont;
    protected CharToGlyphMapper mapper;
    protected long pScalerContext;
    protected long[] longGlyphImages;
    protected int[] intGlyphImages;
    ConcurrentHashMap<Integer, Point2D.Float> glyphPointMapCache;
    protected boolean getImageWithAdvance;
    protected static final int complexTX = 124;

    static {
        switch (StrikeCache.nativeAddressSize) {
            case 4:
                longAddresses = false;
                return;
            case 8:
                longAddresses = true;
                return;
            default:
                throw new RuntimeException("Unexpected address size");
        }
    }

    PhysicalStrike(PhysicalFont physicalFont, FontStrikeDesc fontStrikeDesc) {
        this.physicalFont = physicalFont;
        this.desc = fontStrikeDesc;
    }

    protected PhysicalStrike() {
    }

    @Override // sun.font.FontStrike
    public int getNumGlyphs() {
        return this.physicalFont.getNumGlyphs();
    }

    @Override // sun.font.FontStrike
    StrikeMetrics getFontMetrics() {
        if (this.strikeMetrics == null) {
            this.strikeMetrics = this.physicalFont.getFontMetrics(this.pScalerContext);
        }
        return this.strikeMetrics;
    }

    @Override // sun.font.FontStrike
    float getCodePointAdvance(int i2) {
        return getGlyphAdvance(this.physicalFont.getMapper().charToGlyph(i2));
    }

    @Override // sun.font.FontStrike
    Point2D.Float getCharMetrics(char c2) {
        return getGlyphMetrics(this.physicalFont.getMapper().charToGlyph(c2));
    }

    int getSlot0GlyphImagePtrs(int[] iArr, long[] jArr, int i2) {
        return 0;
    }

    Point2D.Float getGlyphPoint(int i2, int i3) {
        Point2D.Float glyphPoint = null;
        Integer numValueOf = Integer.valueOf((i2 << 16) | i3);
        if (this.glyphPointMapCache == null) {
            synchronized (this) {
                if (this.glyphPointMapCache == null) {
                    this.glyphPointMapCache = new ConcurrentHashMap<>();
                }
            }
        } else {
            glyphPoint = this.glyphPointMapCache.get(numValueOf);
        }
        if (glyphPoint == null) {
            glyphPoint = this.physicalFont.getGlyphPoint(this.pScalerContext, i2, i3);
            adjustPoint(glyphPoint);
            this.glyphPointMapCache.put(numValueOf, glyphPoint);
        }
        return glyphPoint;
    }

    protected void adjustPoint(Point2D.Float r2) {
    }
}
