package sun.font;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.SoftReference;
import java.text.CharacterIterator;
import sun.java2d.loops.FontInfo;

/* loaded from: rt.jar:sun/font/StandardGlyphVector.class */
public class StandardGlyphVector extends GlyphVector {
    private Font font;
    private FontRenderContext frc;
    private int[] glyphs;
    private int[] userGlyphs;
    private float[] positions;
    private int[] charIndices;
    private int flags;
    private static final int UNINITIALIZED_FLAGS = -1;
    private GlyphTransformInfo gti;
    private AffineTransform ftx;
    private AffineTransform dtx;
    private AffineTransform invdtx;
    private AffineTransform frctx;
    private Font2D font2D;
    private SoftReference fsref;
    private SoftReference lbcacheRef;
    private SoftReference vbcacheRef;
    public static final int FLAG_USES_VERTICAL_BASELINE = 128;
    public static final int FLAG_USES_VERTICAL_METRICS = 256;
    public static final int FLAG_USES_ALTERNATE_ORIENTATION = 512;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StandardGlyphVector.class.desiredAssertionStatus();
    }

    public StandardGlyphVector(Font font, String str, FontRenderContext fontRenderContext) {
        init(font, str.toCharArray(), 0, str.length(), fontRenderContext, -1);
    }

    public StandardGlyphVector(Font font, char[] cArr, FontRenderContext fontRenderContext) {
        init(font, cArr, 0, cArr.length, fontRenderContext, -1);
    }

    public StandardGlyphVector(Font font, char[] cArr, int i2, int i3, FontRenderContext fontRenderContext) {
        init(font, cArr, i2, i3, fontRenderContext, -1);
    }

    private float getTracking(Font font) {
        if (font.hasLayoutAttributes()) {
            return ((AttributeMap) font.getAttributes()).getValues().getTracking();
        }
        return 0.0f;
    }

    public StandardGlyphVector(Font font, FontRenderContext fontRenderContext, int[] iArr, float[] fArr, int[] iArr2, int i2) {
        initGlyphVector(font, fontRenderContext, iArr, fArr, iArr2, i2);
        float tracking = getTracking(font);
        if (tracking != 0.0f) {
            Point2D.Float r0 = new Point2D.Float(tracking * font.getSize2D(), 0.0f);
            if (font.isTransformed()) {
                font.getTransform().deltaTransform(r0, r0);
            }
            FontStrike strike = FontUtilities.getFont2D(font).getStrike(font, fontRenderContext);
            float[] fArr2 = {r0.f12396x, r0.f12397y};
            for (int i3 = 0; i3 < fArr2.length; i3++) {
                float f2 = fArr2[i3];
                if (f2 != 0.0f) {
                    float f3 = 0.0f;
                    int i4 = i3;
                    int i5 = 0;
                    while (i5 < iArr.length) {
                        int i6 = i5;
                        i5++;
                        if (strike.getGlyphAdvance(iArr[i6]) != 0.0f) {
                            int i7 = i4;
                            fArr[i7] = fArr[i7] + f3;
                            f3 += f2;
                        }
                        i4 += 2;
                    }
                    int length = (fArr.length - 2) + i3;
                    fArr[length] = fArr[length] + f3;
                }
            }
        }
    }

    public void initGlyphVector(Font font, FontRenderContext fontRenderContext, int[] iArr, float[] fArr, int[] iArr2, int i2) {
        this.font = font;
        this.frc = fontRenderContext;
        this.glyphs = iArr;
        this.userGlyphs = iArr;
        this.positions = fArr;
        this.charIndices = iArr2;
        this.flags = i2;
        initFontData();
    }

    public StandardGlyphVector(Font font, CharacterIterator characterIterator, FontRenderContext fontRenderContext) {
        int beginIndex = characterIterator.getBeginIndex();
        char[] cArr = new char[characterIterator.getEndIndex() - beginIndex];
        char cFirst = characterIterator.first();
        while (true) {
            char c2 = cFirst;
            if (c2 != 65535) {
                cArr[characterIterator.getIndex() - beginIndex] = c2;
                cFirst = characterIterator.next();
            } else {
                init(font, cArr, 0, cArr.length, fontRenderContext, -1);
                return;
            }
        }
    }

    public StandardGlyphVector(Font font, int[] iArr, FontRenderContext fontRenderContext) {
        this.font = font;
        this.frc = fontRenderContext;
        this.flags = -1;
        initFontData();
        this.userGlyphs = iArr;
        this.glyphs = getValidatedGlyphs(this.userGlyphs);
    }

    public static StandardGlyphVector getStandardGV(GlyphVector glyphVector, FontInfo fontInfo) {
        Object antiAliasingHint;
        if (fontInfo.aaHint == 2 && (antiAliasingHint = glyphVector.getFontRenderContext().getAntiAliasingHint()) != RenderingHints.VALUE_TEXT_ANTIALIAS_ON && antiAliasingHint != RenderingHints.VALUE_TEXT_ANTIALIAS_GASP) {
            FontRenderContext fontRenderContext = glyphVector.getFontRenderContext();
            return new StandardGlyphVector(glyphVector, new FontRenderContext(fontRenderContext.getTransform(), RenderingHints.VALUE_TEXT_ANTIALIAS_ON, fontRenderContext.getFractionalMetricsHint()));
        }
        if (glyphVector instanceof StandardGlyphVector) {
            return (StandardGlyphVector) glyphVector;
        }
        return new StandardGlyphVector(glyphVector, glyphVector.getFontRenderContext());
    }

    @Override // java.awt.font.GlyphVector
    public Font getFont() {
        return this.font;
    }

    @Override // java.awt.font.GlyphVector
    public FontRenderContext getFontRenderContext() {
        return this.frc;
    }

    @Override // java.awt.font.GlyphVector
    public void performDefaultLayout() {
        this.positions = null;
        if (getTracking(this.font) == 0.0f) {
            clearFlags(2);
        }
    }

    @Override // java.awt.font.GlyphVector
    public int getNumGlyphs() {
        return this.glyphs.length;
    }

    @Override // java.awt.font.GlyphVector
    public int getGlyphCode(int i2) {
        return this.userGlyphs[i2];
    }

    @Override // java.awt.font.GlyphVector
    public int[] getGlyphCodes(int i2, int i3, int[] iArr) {
        if (i3 < 0) {
            throw new IllegalArgumentException("count = " + i3);
        }
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("start = " + i2);
        }
        if (i2 > this.glyphs.length - i3) {
            throw new IndexOutOfBoundsException("start + count = " + (i2 + i3));
        }
        if (iArr == null) {
            iArr = new int[i3];
        }
        for (int i4 = 0; i4 < i3; i4++) {
            iArr[i4] = this.userGlyphs[i4 + i2];
        }
        return iArr;
    }

    @Override // java.awt.font.GlyphVector
    public int getGlyphCharIndex(int i2) {
        if (i2 < 0 && i2 >= this.glyphs.length) {
            throw new IndexOutOfBoundsException("" + i2);
        }
        if (this.charIndices == null) {
            if ((getLayoutFlags() & 4) != 0) {
                return (this.glyphs.length - 1) - i2;
            }
            return i2;
        }
        return this.charIndices[i2];
    }

    @Override // java.awt.font.GlyphVector
    public int[] getGlyphCharIndices(int i2, int i3, int[] iArr) {
        if (i2 < 0 || i3 < 0 || i3 > this.glyphs.length - i2) {
            throw new IndexOutOfBoundsException("" + i2 + ", " + i3);
        }
        if (iArr == null) {
            iArr = new int[i3];
        }
        if (this.charIndices == null) {
            if ((getLayoutFlags() & 4) != 0) {
                int i4 = 0;
                int length = (this.glyphs.length - 1) - i2;
                while (i4 < i3) {
                    iArr[i4] = length;
                    i4++;
                    length--;
                }
            } else {
                int i5 = 0;
                int i6 = i2;
                while (i5 < i3) {
                    iArr[i5] = i6;
                    i5++;
                    i6++;
                }
            }
        } else {
            for (int i7 = 0; i7 < i3; i7++) {
                iArr[i7] = this.charIndices[i7 + i2];
            }
        }
        return iArr;
    }

    @Override // java.awt.font.GlyphVector
    public Rectangle2D getLogicalBounds() {
        setFRCTX();
        initPositions();
        LineMetrics lineMetrics = this.font.getLineMetrics("", this.frc);
        float f2 = -lineMetrics.getAscent();
        float f3 = 0.0f;
        float descent = lineMetrics.getDescent() + lineMetrics.getLeading();
        if (this.glyphs.length > 0) {
            f3 = this.positions[this.positions.length - 2];
        }
        return new Rectangle2D.Float(0.0f, f2, f3 - 0.0f, descent - f2);
    }

    @Override // java.awt.font.GlyphVector
    public Rectangle2D getVisualBounds() {
        Rectangle2D rectangle2D = null;
        for (int i2 = 0; i2 < this.glyphs.length; i2++) {
            Rectangle2D bounds2D = getGlyphVisualBounds(i2).getBounds2D();
            if (!bounds2D.isEmpty()) {
                if (rectangle2D == null) {
                    rectangle2D = bounds2D;
                } else {
                    Rectangle2D.union(rectangle2D, bounds2D, rectangle2D);
                }
            }
        }
        if (rectangle2D == null) {
            rectangle2D = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
        }
        return rectangle2D;
    }

    @Override // java.awt.font.GlyphVector
    public Rectangle getPixelBounds(FontRenderContext fontRenderContext, float f2, float f3) {
        return getGlyphsPixelBounds(fontRenderContext, f2, f3, 0, this.glyphs.length);
    }

    @Override // java.awt.font.GlyphVector
    public Shape getOutline() {
        return getGlyphsOutline(0, this.glyphs.length, 0.0f, 0.0f);
    }

    @Override // java.awt.font.GlyphVector
    public Shape getOutline(float f2, float f3) {
        return getGlyphsOutline(0, this.glyphs.length, f2, f3);
    }

    @Override // java.awt.font.GlyphVector
    public Shape getGlyphOutline(int i2) {
        return getGlyphsOutline(i2, 1, 0.0f, 0.0f);
    }

    @Override // java.awt.font.GlyphVector
    public Shape getGlyphOutline(int i2, float f2, float f3) {
        return getGlyphsOutline(i2, 1, f2, f3);
    }

    @Override // java.awt.font.GlyphVector
    public Point2D getGlyphPosition(int i2) {
        initPositions();
        int i3 = i2 * 2;
        return new Point2D.Float(this.positions[i3], this.positions[i3 + 1]);
    }

    @Override // java.awt.font.GlyphVector
    public void setGlyphPosition(int i2, Point2D point2D) {
        initPositions();
        int i3 = i2 << 1;
        this.positions[i3] = (float) point2D.getX();
        this.positions[i3 + 1] = (float) point2D.getY();
        clearCaches(i2);
        addFlags(2);
    }

    @Override // java.awt.font.GlyphVector
    public AffineTransform getGlyphTransform(int i2) {
        if (i2 < 0 || i2 >= this.glyphs.length) {
            throw new IndexOutOfBoundsException("ix = " + i2);
        }
        if (this.gti != null) {
            return this.gti.getGlyphTransform(i2);
        }
        return null;
    }

    @Override // java.awt.font.GlyphVector
    public void setGlyphTransform(int i2, AffineTransform affineTransform) {
        if (i2 < 0 || i2 >= this.glyphs.length) {
            throw new IndexOutOfBoundsException("ix = " + i2);
        }
        if (this.gti == null) {
            if (affineTransform == null || affineTransform.isIdentity()) {
                return;
            } else {
                this.gti = new GlyphTransformInfo(this);
            }
        }
        this.gti.setGlyphTransform(i2, affineTransform);
        if (this.gti.transformCount() == 0) {
            this.gti = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0067 A[PHI: r7
  0x0067: PHI (r7v2 int) = (r7v1 int), (r7v4 int) binds: [B:22:0x0057, B:24:0x0060] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // java.awt.font.GlyphVector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getLayoutFlags() {
        /*
            r4 = this;
            r0 = r4
            int r0 = r0.flags
            r1 = -1
            if (r0 != r1) goto L90
            r0 = r4
            r1 = 0
            r0.flags = r1
            r0 = r4
            int[] r0 = r0.charIndices
            if (r0 == 0) goto L90
            r0 = r4
            int[] r0 = r0.glyphs
            int r0 = r0.length
            r1 = 1
            if (r0 <= r1) goto L90
            r0 = 1
            r5 = r0
            r0 = 1
            r6 = r0
            r0 = r4
            int[] r0 = r0.charIndices
            int r0 = r0.length
            r7 = r0
            r0 = 0
            r8 = r0
        L2a:
            r0 = r8
            r1 = r4
            int[] r1 = r1.charIndices
            int r1 = r1.length
            if (r0 >= r1) goto L6f
            r0 = r5
            if (r0 != 0) goto L3c
            r0 = r6
            if (r0 == 0) goto L6f
        L3c:
            r0 = r4
            int[] r0 = r0.charIndices
            r1 = r8
            r0 = r0[r1]
            r9 = r0
            r0 = r5
            if (r0 == 0) goto L54
            r0 = r9
            r1 = r8
            if (r0 != r1) goto L54
            r0 = 1
            goto L55
        L54:
            r0 = 0
        L55:
            r5 = r0
            r0 = r6
            if (r0 == 0) goto L67
            r0 = r9
            int r7 = r7 + (-1)
            r1 = r7
            if (r0 != r1) goto L67
            r0 = 1
            goto L68
        L67:
            r0 = 0
        L68:
            r6 = r0
            int r8 = r8 + 1
            goto L2a
        L6f:
            r0 = r6
            if (r0 == 0) goto L7d
            r0 = r4
            r1 = r0
            int r1 = r1.flags
            r2 = 4
            r1 = r1 | r2
            r0.flags = r1
        L7d:
            r0 = r6
            if (r0 != 0) goto L90
            r0 = r5
            if (r0 != 0) goto L90
            r0 = r4
            r1 = r0
            int r1 = r1.flags
            r2 = 8
            r1 = r1 | r2
            r0.flags = r1
        L90:
            r0 = r4
            int r0 = r0.flags
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.font.StandardGlyphVector.getLayoutFlags():int");
    }

    @Override // java.awt.font.GlyphVector
    public float[] getGlyphPositions(int i2, int i3, float[] fArr) {
        if (i3 < 0) {
            throw new IllegalArgumentException("count = " + i3);
        }
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("start = " + i2);
        }
        if (i2 > (this.glyphs.length + 1) - i3) {
            throw new IndexOutOfBoundsException("start + count = " + (i2 + i3));
        }
        return internalGetGlyphPositions(i2, i3, 0, fArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0041  */
    @Override // java.awt.font.GlyphVector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.awt.Shape getGlyphLogicalBounds(int r6) {
        /*
            Method dump skipped, instructions count: 329
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.font.StandardGlyphVector.getGlyphLogicalBounds(int):java.awt.Shape");
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0041  */
    @Override // java.awt.font.GlyphVector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.awt.Shape getGlyphVisualBounds(int r6) {
        /*
            r5 = this;
            r0 = r6
            if (r0 < 0) goto Ld
            r0 = r6
            r1 = r5
            int[] r1 = r1.glyphs
            int r1 = r1.length
            if (r0 < r1) goto L28
        Ld:
            java.lang.IndexOutOfBoundsException r0 = new java.lang.IndexOutOfBoundsException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "ix = "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r6
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L28:
            r0 = r5
            java.lang.ref.SoftReference r0 = r0.vbcacheRef
            if (r0 == 0) goto L41
            r0 = r5
            java.lang.ref.SoftReference r0 = r0.vbcacheRef
            java.lang.Object r0 = r0.get()
            java.awt.Shape[] r0 = (java.awt.Shape[]) r0
            java.awt.Shape[] r0 = (java.awt.Shape[]) r0
            r1 = r0
            r7 = r1
            if (r0 != 0) goto L56
        L41:
            r0 = r5
            int[] r0 = r0.glyphs
            int r0 = r0.length
            java.awt.Shape[] r0 = new java.awt.Shape[r0]
            r7 = r0
            r0 = r5
            java.lang.ref.SoftReference r1 = new java.lang.ref.SoftReference
            r2 = r1
            r3 = r7
            r2.<init>(r3)
            r0.vbcacheRef = r1
        L56:
            r0 = r7
            r1 = r6
            r0 = r0[r1]
            r8 = r0
            r0 = r8
            if (r0 != 0) goto L6f
            sun.font.DelegatingShape r0 = new sun.font.DelegatingShape
            r1 = r0
            r2 = r5
            r3 = r6
            java.awt.geom.Rectangle2D r2 = r2.getGlyphOutlineBounds(r3)
            r1.<init>(r2)
            r8 = r0
            r0 = r7
            r1 = r6
            r2 = r8
            r0[r1] = r2
        L6f:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.font.StandardGlyphVector.getGlyphVisualBounds(int):java.awt.Shape");
    }

    @Override // java.awt.font.GlyphVector
    public Rectangle getGlyphPixelBounds(int i2, FontRenderContext fontRenderContext, float f2, float f3) {
        return getGlyphsPixelBounds(fontRenderContext, f2, f3, i2, 1);
    }

    @Override // java.awt.font.GlyphVector
    public GlyphMetrics getGlyphMetrics(int i2) {
        if (i2 < 0 || i2 >= this.glyphs.length) {
            throw new IndexOutOfBoundsException("ix = " + i2);
        }
        Rectangle2D bounds2D = getGlyphVisualBounds(i2).getBounds2D();
        Point2D glyphPosition = getGlyphPosition(i2);
        bounds2D.setRect(bounds2D.getMinX() - glyphPosition.getX(), bounds2D.getMinY() - glyphPosition.getY(), bounds2D.getWidth(), bounds2D.getHeight());
        Point2D.Float glyphMetrics = getGlyphStrike(i2).strike.getGlyphMetrics(this.glyphs[i2]);
        return new GlyphMetrics(true, glyphMetrics.f12396x, glyphMetrics.f12397y, bounds2D, (byte) 0);
    }

    @Override // java.awt.font.GlyphVector
    public GlyphJustificationInfo getGlyphJustificationInfo(int i2) {
        if (i2 < 0 || i2 >= this.glyphs.length) {
            throw new IndexOutOfBoundsException("ix = " + i2);
        }
        return null;
    }

    @Override // java.awt.font.GlyphVector
    public boolean equals(GlyphVector glyphVector) {
        if (this == glyphVector) {
            return true;
        }
        if (glyphVector == null) {
            return false;
        }
        try {
            StandardGlyphVector standardGlyphVector = (StandardGlyphVector) glyphVector;
            if (this.glyphs.length != standardGlyphVector.glyphs.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.glyphs.length; i2++) {
                if (this.glyphs[i2] != standardGlyphVector.glyphs[i2]) {
                    return false;
                }
            }
            if (!this.font.equals(standardGlyphVector.font) || !this.frc.equals(standardGlyphVector.frc)) {
                return false;
            }
            if ((standardGlyphVector.positions == null) != (this.positions == null)) {
                if (this.positions == null) {
                    initPositions();
                } else {
                    standardGlyphVector.initPositions();
                }
            }
            if (this.positions != null) {
                for (int i3 = 0; i3 < this.positions.length; i3++) {
                    if (this.positions[i3] != standardGlyphVector.positions[i3]) {
                        return false;
                    }
                }
            }
            if (this.gti == null) {
                return standardGlyphVector.gti == null;
            }
            return this.gti.equals(standardGlyphVector.gti);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public int hashCode() {
        return this.font.hashCode() ^ this.glyphs.length;
    }

    public boolean equals(Object obj) {
        try {
            return equals((GlyphVector) obj);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    public StandardGlyphVector copy() {
        return (StandardGlyphVector) clone();
    }

    public Object clone() {
        try {
            StandardGlyphVector standardGlyphVector = (StandardGlyphVector) super.clone();
            standardGlyphVector.clearCaches();
            if (this.positions != null) {
                standardGlyphVector.positions = (float[]) this.positions.clone();
            }
            if (this.gti != null) {
                standardGlyphVector.gti = new GlyphTransformInfo(standardGlyphVector, this.gti);
            }
            return standardGlyphVector;
        } catch (CloneNotSupportedException e2) {
            return this;
        }
    }

    public void setGlyphPositions(float[] fArr, int i2, int i3, int i4) {
        if (i4 < 0) {
            throw new IllegalArgumentException("count = " + i4);
        }
        initPositions();
        int i5 = i3 * 2;
        int i6 = i5 + (i4 * 2);
        int i7 = i2;
        while (i5 < i6) {
            this.positions[i5] = fArr[i7];
            i5++;
            i7++;
        }
        clearCaches();
        addFlags(2);
    }

    public void setGlyphPositions(float[] fArr) {
        int length = (this.glyphs.length * 2) + 2;
        if (fArr.length != length) {
            throw new IllegalArgumentException("srcPositions.length != " + length);
        }
        this.positions = (float[]) fArr.clone();
        clearCaches();
        addFlags(2);
    }

    public float[] getGlyphPositions(float[] fArr) {
        return internalGetGlyphPositions(0, this.glyphs.length + 1, 0, fArr);
    }

    public AffineTransform[] getGlyphTransforms(int i2, int i3, AffineTransform[] affineTransformArr) {
        if (i2 < 0 || i3 < 0 || i2 + i3 > this.glyphs.length) {
            throw new IllegalArgumentException("start: " + i2 + " count: " + i3);
        }
        if (this.gti == null) {
            return null;
        }
        if (affineTransformArr == null) {
            affineTransformArr = new AffineTransform[i3];
        }
        int i4 = 0;
        while (i4 < i3) {
            affineTransformArr[i4] = this.gti.getGlyphTransform(i2);
            i4++;
            i2++;
        }
        return affineTransformArr;
    }

    public AffineTransform[] getGlyphTransforms() {
        return getGlyphTransforms(0, this.glyphs.length, null);
    }

    public void setGlyphTransforms(AffineTransform[] affineTransformArr, int i2, int i3, int i4) {
        int i5 = i3 + i4;
        for (int i6 = i3; i6 < i5; i6++) {
            setGlyphTransform(i6, affineTransformArr[i2 + i6]);
        }
    }

    public void setGlyphTransforms(AffineTransform[] affineTransformArr) {
        setGlyphTransforms(affineTransformArr, 0, 0, this.glyphs.length);
    }

    public float[] getGlyphInfo() {
        setFRCTX();
        initPositions();
        float[] fArr = new float[this.glyphs.length * 8];
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.glyphs.length) {
            float f2 = this.positions[i2 * 2];
            float f3 = this.positions[(i2 * 2) + 1];
            fArr[i3] = f2;
            fArr[i3 + 1] = f3;
            Point2D.Float glyphMetrics = getGlyphStrike(i2).strike.getGlyphMetrics(this.glyphs[i2]);
            fArr[i3 + 2] = glyphMetrics.f12396x;
            fArr[i3 + 3] = glyphMetrics.f12397y;
            Rectangle2D bounds2D = getGlyphVisualBounds(i2).getBounds2D();
            fArr[i3 + 4] = (float) bounds2D.getMinX();
            fArr[i3 + 5] = (float) bounds2D.getMinY();
            fArr[i3 + 6] = (float) bounds2D.getWidth();
            fArr[i3 + 7] = (float) bounds2D.getHeight();
            i2++;
            i3 += 8;
        }
        return fArr;
    }

    public void pixellate(FontRenderContext fontRenderContext, Point2D point2D, Point point) {
        if (fontRenderContext == null) {
            fontRenderContext = this.frc;
        }
        AffineTransform transform = fontRenderContext.getTransform();
        transform.transform(point2D, point2D);
        point.f12370x = (int) point2D.getX();
        point.f12371y = (int) point2D.getY();
        point2D.setLocation(point.f12370x, point.f12371y);
        try {
            transform.inverseTransform(point2D, point2D);
        } catch (NoninvertibleTransformException e2) {
            throw new IllegalArgumentException("must be able to invert frc transform");
        }
    }

    boolean needsPositions(double[] dArr) {
        return (this.gti == null && (getLayoutFlags() & 2) == 0 && matchTX(dArr, this.frctx)) ? false : true;
    }

    Object setupGlyphImages(long[] jArr, float[] fArr, double[] dArr) {
        initPositions();
        setRenderTransform(dArr);
        if (this.gti != null) {
            return this.gti.setupGlyphImages(jArr, fArr, this.dtx);
        }
        GlyphStrike defaultStrike = getDefaultStrike();
        defaultStrike.strike.getGlyphImagePtrs(this.glyphs, jArr, this.glyphs.length);
        if (fArr != null) {
            if (this.dtx.isIdentity()) {
                System.arraycopy(this.positions, 0, fArr, 0, this.glyphs.length * 2);
            } else {
                this.dtx.transform(this.positions, 0, fArr, 0, this.glyphs.length);
            }
        }
        return defaultStrike;
    }

    private static boolean matchTX(double[] dArr, AffineTransform affineTransform) {
        return dArr[0] == affineTransform.getScaleX() && dArr[1] == affineTransform.getShearY() && dArr[2] == affineTransform.getShearX() && dArr[3] == affineTransform.getScaleY();
    }

    private static AffineTransform getNonTranslateTX(AffineTransform affineTransform) {
        if (affineTransform.getTranslateX() != 0.0d || affineTransform.getTranslateY() != 0.0d) {
            affineTransform = new AffineTransform(affineTransform.getScaleX(), affineTransform.getShearY(), affineTransform.getShearX(), affineTransform.getScaleY(), 0.0d, 0.0d);
        }
        return affineTransform;
    }

    private static boolean equalNonTranslateTX(AffineTransform affineTransform, AffineTransform affineTransform2) {
        return affineTransform.getScaleX() == affineTransform2.getScaleX() && affineTransform.getShearY() == affineTransform2.getShearY() && affineTransform.getShearX() == affineTransform2.getShearX() && affineTransform.getScaleY() == affineTransform2.getScaleY();
    }

    private void setRenderTransform(double[] dArr) {
        if (!$assertionsDisabled && dArr.length != 4) {
            throw new AssertionError();
        }
        if (!matchTX(dArr, this.dtx)) {
            resetDTX(new AffineTransform(dArr));
        }
    }

    private final void setDTX(AffineTransform affineTransform) {
        if (!equalNonTranslateTX(this.dtx, affineTransform)) {
            resetDTX(getNonTranslateTX(affineTransform));
        }
    }

    private final void setFRCTX() {
        if (!equalNonTranslateTX(this.frctx, this.dtx)) {
            resetDTX(getNonTranslateTX(this.frctx));
        }
    }

    private final void resetDTX(AffineTransform affineTransform) {
        this.fsref = null;
        this.dtx = affineTransform;
        this.invdtx = null;
        if (!this.dtx.isIdentity()) {
            try {
                this.invdtx = this.dtx.createInverse();
            } catch (NoninvertibleTransformException e2) {
            }
        }
        if (this.gti != null) {
            this.gti.strikesRef = null;
        }
    }

    private StandardGlyphVector(GlyphVector glyphVector, FontRenderContext fontRenderContext) {
        this.font = glyphVector.getFont();
        this.frc = fontRenderContext;
        initFontData();
        int numGlyphs = glyphVector.getNumGlyphs();
        this.userGlyphs = glyphVector.getGlyphCodes(0, numGlyphs, null);
        if (glyphVector instanceof StandardGlyphVector) {
            this.glyphs = this.userGlyphs;
        } else {
            this.glyphs = getValidatedGlyphs(this.userGlyphs);
        }
        this.flags = glyphVector.getLayoutFlags() & 15;
        if ((this.flags & 2) != 0) {
            this.positions = glyphVector.getGlyphPositions(0, numGlyphs + 1, null);
        }
        if ((this.flags & 8) != 0) {
            this.charIndices = glyphVector.getGlyphCharIndices(0, numGlyphs, null);
        }
        if ((this.flags & 1) != 0) {
            AffineTransform[] affineTransformArr = new AffineTransform[numGlyphs];
            for (int i2 = 0; i2 < numGlyphs; i2++) {
                affineTransformArr[i2] = glyphVector.getGlyphTransform(i2);
            }
            setGlyphTransforms(affineTransformArr);
        }
    }

    int[] getValidatedGlyphs(int[] iArr) {
        int length = iArr.length;
        int[] iArr2 = new int[length];
        for (int i2 = 0; i2 < length; i2++) {
            if (iArr[i2] == 65534 || iArr[i2] == 65535) {
                iArr2[i2] = iArr[i2];
            } else {
                iArr2[i2] = this.font2D.getValidatedGlyphCode(iArr[i2]);
            }
        }
        return iArr2;
    }

    private void init(Font font, char[] cArr, int i2, int i3, FontRenderContext fontRenderContext, int i4) {
        if (i2 < 0 || i3 < 0 || i2 + i3 > cArr.length) {
            throw new ArrayIndexOutOfBoundsException("start or count out of bounds");
        }
        this.font = font;
        this.frc = fontRenderContext;
        this.flags = i4;
        if (getTracking(font) != 0.0f) {
            addFlags(2);
        }
        if (i2 != 0) {
            char[] cArr2 = new char[i3];
            System.arraycopy(cArr, i2, cArr2, 0, i3);
            cArr = cArr2;
        }
        initFontData();
        this.glyphs = new int[i3];
        this.userGlyphs = this.glyphs;
        this.font2D.getMapper().charsToGlyphs(i3, cArr, this.glyphs);
    }

    private void initFontData() {
        this.font2D = FontUtilities.getFont2D(this.font);
        if (this.font2D instanceof FontSubstitution) {
            this.font2D = ((FontSubstitution) this.font2D).getCompositeFont2D();
        }
        float size2D = this.font.getSize2D();
        if (this.font.isTransformed()) {
            this.ftx = this.font.getTransform();
            if (this.ftx.getTranslateX() != 0.0d || this.ftx.getTranslateY() != 0.0d) {
                addFlags(2);
            }
            this.ftx.setTransform(this.ftx.getScaleX(), this.ftx.getShearY(), this.ftx.getShearX(), this.ftx.getScaleY(), 0.0d, 0.0d);
            this.ftx.scale(size2D, size2D);
        } else {
            this.ftx = AffineTransform.getScaleInstance(size2D, size2D);
        }
        this.frctx = this.frc.getTransform();
        resetDTX(getNonTranslateTX(this.frctx));
    }

    private float[] internalGetGlyphPositions(int i2, int i3, int i4, float[] fArr) {
        if (fArr == null) {
            fArr = new float[i4 + (i3 * 2)];
        }
        initPositions();
        int i5 = i4;
        int i6 = i4 + (i3 * 2);
        int i7 = i2 * 2;
        while (i5 < i6) {
            fArr[i5] = this.positions[i7];
            i5++;
            i7++;
        }
        return fArr;
    }

    private Rectangle2D getGlyphOutlineBounds(int i2) {
        setFRCTX();
        initPositions();
        return getGlyphStrike(i2).getGlyphOutlineBounds(this.glyphs[i2], this.positions[i2 * 2], this.positions[(i2 * 2) + 1]);
    }

    private Shape getGlyphsOutline(int i2, int i3, float f2, float f3) {
        setFRCTX();
        initPositions();
        GeneralPath generalPath = new GeneralPath(1);
        int i4 = i2;
        int i5 = i2 + i3;
        int i6 = i2 * 2;
        while (i4 < i5) {
            getGlyphStrike(i4).appendGlyphOutline(this.glyphs[i4], generalPath, f2 + this.positions[i6], f3 + this.positions[i6 + 1]);
            i4++;
            i6 += 2;
        }
        return generalPath;
    }

    private Rectangle getGlyphsPixelBounds(FontRenderContext fontRenderContext, float f2, float f3, int i2, int i3) {
        AffineTransform transform;
        initPositions();
        if (fontRenderContext == null || fontRenderContext.equals(this.frc)) {
            transform = this.frctx;
        } else {
            transform = fontRenderContext.getTransform();
        }
        setDTX(transform);
        if (this.gti != null) {
            return this.gti.getGlyphsPixelBounds(transform, f2, f3, i2, i3);
        }
        FontStrike fontStrike = getDefaultStrike().strike;
        Rectangle rectangle = null;
        Rectangle rectangle2 = new Rectangle();
        Point2D.Float r0 = new Point2D.Float();
        int i4 = i2 * 2;
        while (true) {
            i3--;
            if (i3 < 0) {
                break;
            }
            int i5 = i4;
            int i6 = i4 + 1;
            r0.f12396x = f2 + this.positions[i5];
            i4 = i6 + 1;
            r0.f12397y = f3 + this.positions[i6];
            transform.transform(r0, r0);
            int i7 = i2;
            i2++;
            fontStrike.getGlyphImageBounds(this.glyphs[i7], r0, rectangle2);
            if (!rectangle2.isEmpty()) {
                if (rectangle == null) {
                    rectangle = new Rectangle(rectangle2);
                } else {
                    rectangle.add(rectangle2);
                }
            }
        }
        return rectangle != null ? rectangle : rectangle2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearCaches(int i2) {
        Shape[] shapeArr;
        Shape[] shapeArr2;
        if (this.lbcacheRef != null && (shapeArr2 = (Shape[]) this.lbcacheRef.get()) != null) {
            shapeArr2[i2] = null;
        }
        if (this.vbcacheRef != null && (shapeArr = (Shape[]) this.vbcacheRef.get()) != null) {
            shapeArr[i2] = null;
        }
    }

    private void clearCaches() {
        this.lbcacheRef = null;
        this.vbcacheRef = null;
    }

    private void initPositions() {
        if (this.positions == null) {
            setFRCTX();
            this.positions = new float[(this.glyphs.length * 2) + 2];
            Point2D.Float r6 = null;
            float tracking = getTracking(this.font);
            if (tracking != 0.0f) {
                r6 = new Point2D.Float(tracking * this.font.getSize2D(), 0.0f);
            }
            Point2D.Float r0 = new Point2D.Float(0.0f, 0.0f);
            if (this.font.isTransformed()) {
                AffineTransform transform = this.font.getTransform();
                transform.transform(r0, r0);
                this.positions[0] = r0.f12396x;
                this.positions[1] = r0.f12397y;
                if (r6 != null) {
                    transform.deltaTransform(r6, r6);
                }
            }
            int i2 = 0;
            int i3 = 2;
            while (i2 < this.glyphs.length) {
                getGlyphStrike(i2).addDefaultGlyphAdvance(this.glyphs[i2], r0);
                if (r6 != null) {
                    r0.f12396x += r6.f12396x;
                    r0.f12397y += r6.f12397y;
                }
                this.positions[i3] = r0.f12396x;
                this.positions[i3 + 1] = r0.f12397y;
                i2++;
                i3 += 2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addFlags(int i2) {
        this.flags = getLayoutFlags() | i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearFlags(int i2) {
        this.flags = getLayoutFlags() & (i2 ^ (-1));
    }

    private GlyphStrike getGlyphStrike(int i2) {
        if (this.gti == null) {
            return getDefaultStrike();
        }
        return this.gti.getStrike(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GlyphStrike getDefaultStrike() {
        GlyphStrike glyphStrikeCreate = null;
        if (this.fsref != null) {
            glyphStrikeCreate = (GlyphStrike) this.fsref.get();
        }
        if (glyphStrikeCreate == null) {
            glyphStrikeCreate = GlyphStrike.create(this, this.dtx, null);
            this.fsref = new SoftReference(glyphStrikeCreate);
        }
        return glyphStrikeCreate;
    }

    /* loaded from: rt.jar:sun/font/StandardGlyphVector$GlyphTransformInfo.class */
    static final class GlyphTransformInfo {
        StandardGlyphVector sgv;
        int[] indices;
        double[] transforms;
        SoftReference strikesRef;
        boolean haveAllStrikes;

        GlyphTransformInfo(StandardGlyphVector standardGlyphVector) {
            this.sgv = standardGlyphVector;
        }

        GlyphTransformInfo(StandardGlyphVector standardGlyphVector, GlyphTransformInfo glyphTransformInfo) {
            this.sgv = standardGlyphVector;
            this.indices = glyphTransformInfo.indices == null ? null : (int[]) glyphTransformInfo.indices.clone();
            this.transforms = glyphTransformInfo.transforms == null ? null : (double[]) glyphTransformInfo.transforms.clone();
            this.strikesRef = null;
        }

        public boolean equals(GlyphTransformInfo glyphTransformInfo) {
            if (glyphTransformInfo == null) {
                return false;
            }
            if (glyphTransformInfo == this) {
                return true;
            }
            if (this.indices.length != glyphTransformInfo.indices.length || this.transforms.length != glyphTransformInfo.transforms.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.indices.length; i2++) {
                int i3 = this.indices[i2];
                int i4 = glyphTransformInfo.indices[i2];
                if ((i3 == 0) != (i4 == 0)) {
                    return false;
                }
                if (i3 != 0) {
                    int i5 = i3 * 6;
                    int i6 = i4 * 6;
                    for (int i7 = 6; i7 > 0; i7--) {
                        i5--;
                        i6--;
                        if (this.indices[i5] != glyphTransformInfo.indices[i6]) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        void setGlyphTransform(int i2, AffineTransform affineTransform) {
            int i3;
            double[] dArr = new double[6];
            boolean z2 = true;
            if (affineTransform == null || affineTransform.isIdentity()) {
                dArr[3] = 1.0d;
                dArr[0] = 1.0d;
            } else {
                z2 = false;
                affineTransform.getMatrix(dArr);
            }
            if (this.indices == null) {
                if (z2) {
                    return;
                }
                this.indices = new int[this.sgv.glyphs.length];
                this.indices[i2] = 1;
                this.transforms = dArr;
            } else {
                boolean z3 = false;
                if (z2) {
                    i3 = 0;
                } else {
                    z3 = true;
                    int i4 = 0;
                    loop2: while (true) {
                        if (i4 >= this.transforms.length) {
                            break;
                        }
                        for (int i5 = 0; i5 < 6; i5++) {
                            if (this.transforms[i4 + i5] != dArr[i5]) {
                                break;
                            }
                        }
                        z3 = false;
                        break loop2;
                        i4 += 6;
                    }
                    i3 = (i4 / 6) + 1;
                }
                int i6 = this.indices[i2];
                if (i3 != i6) {
                    boolean z4 = false;
                    if (i6 != 0) {
                        z4 = true;
                        int i7 = 0;
                        while (true) {
                            if (i7 >= this.indices.length) {
                                break;
                            }
                            if (this.indices[i7] != i6 || i7 == i2) {
                                i7++;
                            } else {
                                z4 = false;
                                break;
                            }
                        }
                    }
                    if (z4 && z3) {
                        i3 = i6;
                        System.arraycopy(dArr, 0, this.transforms, (i3 - 1) * 6, 6);
                    } else if (z4) {
                        if (this.transforms.length == 6) {
                            this.indices = null;
                            this.transforms = null;
                            this.sgv.clearCaches(i2);
                            this.sgv.clearFlags(1);
                            this.strikesRef = null;
                            return;
                        }
                        double[] dArr2 = new double[this.transforms.length - 6];
                        System.arraycopy(this.transforms, 0, dArr2, 0, (i6 - 1) * 6);
                        System.arraycopy(this.transforms, i6 * 6, dArr2, (i6 - 1) * 6, this.transforms.length - (i6 * 6));
                        this.transforms = dArr2;
                        for (int i8 = 0; i8 < this.indices.length; i8++) {
                            if (this.indices[i8] > i6) {
                                int[] iArr = this.indices;
                                int i9 = i8;
                                iArr[i9] = iArr[i9] - 1;
                            }
                        }
                        if (i3 > i6) {
                            i3--;
                        }
                    } else if (z3) {
                        double[] dArr3 = new double[this.transforms.length + 6];
                        System.arraycopy(this.transforms, 0, dArr3, 0, this.transforms.length);
                        System.arraycopy(dArr, 0, dArr3, this.transforms.length, 6);
                        this.transforms = dArr3;
                    }
                    this.indices[i2] = i3;
                }
            }
            this.sgv.clearCaches(i2);
            this.sgv.addFlags(1);
            this.strikesRef = null;
        }

        AffineTransform getGlyphTransform(int i2) {
            int i3 = this.indices[i2];
            if (i3 == 0) {
                return null;
            }
            int i4 = (i3 - 1) * 6;
            return new AffineTransform(this.transforms[i4 + 0], this.transforms[i4 + 1], this.transforms[i4 + 2], this.transforms[i4 + 3], this.transforms[i4 + 4], this.transforms[i4 + 5]);
        }

        int transformCount() {
            if (this.transforms == null) {
                return 0;
            }
            return this.transforms.length / 6;
        }

        Object setupGlyphImages(long[] jArr, float[] fArr, AffineTransform affineTransform) {
            int length = this.sgv.glyphs.length;
            GlyphStrike[] allStrikes = getAllStrikes();
            for (int i2 = 0; i2 < length; i2++) {
                GlyphStrike glyphStrike = allStrikes[this.indices[i2]];
                int i3 = this.sgv.glyphs[i2];
                jArr[i2] = glyphStrike.strike.getGlyphImagePtr(i3);
                glyphStrike.getGlyphPosition(i3, i2 * 2, this.sgv.positions, fArr);
            }
            affineTransform.transform(fArr, 0, fArr, 0, length);
            return allStrikes;
        }

        Rectangle getGlyphsPixelBounds(AffineTransform affineTransform, float f2, float f3, int i2, int i3) {
            Rectangle rectangle = null;
            Rectangle rectangle2 = new Rectangle();
            Point2D.Float r0 = new Point2D.Float();
            int i4 = i2 * 2;
            while (true) {
                i3--;
                if (i3 < 0) {
                    break;
                }
                GlyphStrike strike = getStrike(i2);
                int i5 = i4;
                int i6 = i4 + 1;
                r0.f12396x = f2 + this.sgv.positions[i5] + strike.dx;
                i4 = i6 + 1;
                r0.f12397y = f3 + this.sgv.positions[i6] + strike.dy;
                affineTransform.transform(r0, r0);
                int i7 = i2;
                i2++;
                strike.strike.getGlyphImageBounds(this.sgv.glyphs[i7], r0, rectangle2);
                if (!rectangle2.isEmpty()) {
                    if (rectangle == null) {
                        rectangle = new Rectangle(rectangle2);
                    } else {
                        rectangle.add(rectangle2);
                    }
                }
            }
            return rectangle != null ? rectangle : rectangle2;
        }

        GlyphStrike getStrike(int i2) {
            if (this.indices == null) {
                return this.sgv.getDefaultStrike();
            }
            return getStrikeAtIndex(getStrikeArray(), this.indices[i2]);
        }

        private GlyphStrike[] getAllStrikes() {
            if (this.indices == null) {
                return null;
            }
            GlyphStrike[] strikeArray = getStrikeArray();
            if (!this.haveAllStrikes) {
                for (int i2 = 0; i2 < strikeArray.length; i2++) {
                    getStrikeAtIndex(strikeArray, i2);
                }
                this.haveAllStrikes = true;
            }
            return strikeArray;
        }

        private GlyphStrike[] getStrikeArray() {
            GlyphStrike[] glyphStrikeArr = null;
            if (this.strikesRef != null) {
                glyphStrikeArr = (GlyphStrike[]) this.strikesRef.get();
            }
            if (glyphStrikeArr == null) {
                this.haveAllStrikes = false;
                glyphStrikeArr = new GlyphStrike[transformCount() + 1];
                this.strikesRef = new SoftReference(glyphStrikeArr);
            }
            return glyphStrikeArr;
        }

        private GlyphStrike getStrikeAtIndex(GlyphStrike[] glyphStrikeArr, int i2) {
            GlyphStrike glyphStrikeCreate = glyphStrikeArr[i2];
            if (glyphStrikeCreate == null) {
                if (i2 == 0) {
                    glyphStrikeCreate = this.sgv.getDefaultStrike();
                } else {
                    int i3 = (i2 - 1) * 6;
                    glyphStrikeCreate = GlyphStrike.create(this.sgv, this.sgv.dtx, new AffineTransform(this.transforms[i3], this.transforms[i3 + 1], this.transforms[i3 + 2], this.transforms[i3 + 3], this.transforms[i3 + 4], this.transforms[i3 + 5]));
                }
                glyphStrikeArr[i2] = glyphStrikeCreate;
            }
            return glyphStrikeCreate;
        }
    }

    /* loaded from: rt.jar:sun/font/StandardGlyphVector$GlyphStrike.class */
    public static final class GlyphStrike {
        StandardGlyphVector sgv;
        FontStrike strike;
        float dx;
        float dy;

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r19v0 */
        static GlyphStrike create(StandardGlyphVector standardGlyphVector, AffineTransform affineTransform, AffineTransform affineTransform2) {
            float translateX = 0.0f;
            float translateY = 0.0f;
            AffineTransform affineTransform3 = standardGlyphVector.ftx;
            if (!affineTransform.isIdentity() || affineTransform2 != null) {
                affineTransform3 = new AffineTransform(standardGlyphVector.ftx);
                if (affineTransform2 != null) {
                    affineTransform3.preConcatenate(affineTransform2);
                    translateX = (float) affineTransform3.getTranslateX();
                    translateY = (float) affineTransform3.getTranslateY();
                }
                if (!affineTransform.isIdentity()) {
                    affineTransform3.preConcatenate(affineTransform);
                }
            }
            int iAbs = 1;
            Object antiAliasingHint = standardGlyphVector.frc.getAntiAliasingHint();
            if (antiAliasingHint == RenderingHints.VALUE_TEXT_ANTIALIAS_GASP && !affineTransform3.isIdentity() && (affineTransform3.getType() & (-2)) != 0) {
                double shearX = affineTransform3.getShearX();
                if (shearX != 0.0d) {
                    double scaleY = affineTransform3.getScaleY();
                    iAbs = (int) Math.sqrt((shearX * shearX) + (scaleY * scaleY));
                } else {
                    iAbs = (int) Math.abs(affineTransform3.getScaleY());
                }
            }
            FontStrikeDesc fontStrikeDesc = new FontStrikeDesc(affineTransform, affineTransform3, standardGlyphVector.font.getStyle(), FontStrikeDesc.getAAHintIntVal(antiAliasingHint, standardGlyphVector.font2D, iAbs), FontStrikeDesc.getFMHintIntVal(standardGlyphVector.frc.getFractionalMetricsHint()));
            ?? r19 = standardGlyphVector.font2D;
            boolean z2 = r19 instanceof FontSubstitution;
            CompositeFont compositeFont2D = r19;
            if (z2) {
                compositeFont2D = ((FontSubstitution) r19).getCompositeFont2D();
            }
            return new GlyphStrike(standardGlyphVector, compositeFont2D.handle.font2D.getStrike(fontStrikeDesc), translateX, translateY);
        }

        private GlyphStrike(StandardGlyphVector standardGlyphVector, FontStrike fontStrike, float f2, float f3) {
            this.sgv = standardGlyphVector;
            this.strike = fontStrike;
            this.dx = f2;
            this.dy = f3;
        }

        void getADL(ADL adl) {
            StrikeMetrics fontMetrics = this.strike.getFontMetrics();
            if (this.sgv.font.isTransformed()) {
                Point2D.Float r0 = new Point2D.Float();
                r0.f12396x = (float) this.sgv.font.getTransform().getTranslateX();
                r0.f12397y = (float) this.sgv.font.getTransform().getTranslateY();
            }
            adl.ascentX = -fontMetrics.ascentX;
            adl.ascentY = -fontMetrics.ascentY;
            adl.descentX = fontMetrics.descentX;
            adl.descentY = fontMetrics.descentY;
            adl.leadingX = fontMetrics.leadingX;
            adl.leadingY = fontMetrics.leadingY;
        }

        void getGlyphPosition(int i2, int i3, float[] fArr, float[] fArr2) {
            fArr2[i3] = fArr[i3] + this.dx;
            int i4 = i3 + 1;
            fArr2[i4] = fArr[i4] + this.dy;
        }

        void addDefaultGlyphAdvance(int i2, Point2D.Float r7) {
            Point2D.Float glyphMetrics = this.strike.getGlyphMetrics(i2);
            r7.f12396x += glyphMetrics.f12396x + this.dx;
            r7.f12397y += glyphMetrics.f12397y + this.dy;
        }

        Rectangle2D getGlyphOutlineBounds(int i2, float f2, float f3) {
            Rectangle2D bounds2D;
            if (this.sgv.invdtx == null) {
                bounds2D = new Rectangle2D.Float();
                bounds2D.setRect(this.strike.getGlyphOutlineBounds(i2));
            } else {
                GeneralPath glyphOutline = this.strike.getGlyphOutline(i2, 0.0f, 0.0f);
                glyphOutline.transform(this.sgv.invdtx);
                bounds2D = glyphOutline.getBounds2D();
            }
            if (!bounds2D.isEmpty()) {
                bounds2D.setRect(bounds2D.getMinX() + f2 + this.dx, bounds2D.getMinY() + f3 + this.dy, bounds2D.getWidth(), bounds2D.getHeight());
            }
            return bounds2D;
        }

        void appendGlyphOutline(int i2, GeneralPath generalPath, float f2, float f3) {
            GeneralPath glyphOutline;
            if (this.sgv.invdtx == null) {
                glyphOutline = this.strike.getGlyphOutline(i2, f2 + this.dx, f3 + this.dy);
            } else {
                glyphOutline = this.strike.getGlyphOutline(i2, 0.0f, 0.0f);
                glyphOutline.transform(this.sgv.invdtx);
                glyphOutline.transform(AffineTransform.getTranslateInstance(f2 + this.dx, f3 + this.dy));
            }
            generalPath.append(glyphOutline.getPathIterator(null), false);
        }
    }

    public String toString() {
        return appendString(null).toString();
    }

    StringBuffer appendString(StringBuffer stringBuffer) {
        if (stringBuffer == null) {
            stringBuffer = new StringBuffer();
        }
        try {
            stringBuffer.append("SGV{font: ");
            stringBuffer.append(this.font.toString());
            stringBuffer.append(", frc: ");
            stringBuffer.append(this.frc.toString());
            stringBuffer.append(", glyphs: (");
            stringBuffer.append(this.glyphs.length);
            stringBuffer.append(")[");
            for (int i2 = 0; i2 < this.glyphs.length; i2++) {
                if (i2 > 0) {
                    stringBuffer.append(", ");
                }
                stringBuffer.append(Integer.toHexString(this.glyphs[i2]));
            }
            stringBuffer.append("]");
            if (this.positions != null) {
                stringBuffer.append(", positions: (");
                stringBuffer.append(this.positions.length);
                stringBuffer.append(")[");
                for (int i3 = 0; i3 < this.positions.length; i3 += 2) {
                    if (i3 > 0) {
                        stringBuffer.append(", ");
                    }
                    stringBuffer.append(this.positions[i3]);
                    stringBuffer.append("@");
                    stringBuffer.append(this.positions[i3 + 1]);
                }
                stringBuffer.append("]");
            }
            if (this.charIndices != null) {
                stringBuffer.append(", indices: (");
                stringBuffer.append(this.charIndices.length);
                stringBuffer.append(")[");
                for (int i4 = 0; i4 < this.charIndices.length; i4++) {
                    if (i4 > 0) {
                        stringBuffer.append(", ");
                    }
                    stringBuffer.append(this.charIndices[i4]);
                }
                stringBuffer.append("]");
            }
            stringBuffer.append(", flags:");
            if (getLayoutFlags() == 0) {
                stringBuffer.append(" default");
            } else {
                if ((this.flags & 1) != 0) {
                    stringBuffer.append(" tx");
                }
                if ((this.flags & 2) != 0) {
                    stringBuffer.append(" pos");
                }
                if ((this.flags & 4) != 0) {
                    stringBuffer.append(" rtl");
                }
                if ((this.flags & 8) != 0) {
                    stringBuffer.append(" complex");
                }
            }
        } catch (Exception e2) {
            stringBuffer.append(" " + e2.getMessage());
        }
        stringBuffer.append("}");
        return stringBuffer;
    }

    /* loaded from: rt.jar:sun/font/StandardGlyphVector$ADL.class */
    static class ADL {
        public float ascentX;
        public float ascentY;
        public float descentX;
        public float descentY;
        public float leadingX;
        public float leadingY;

        ADL() {
        }

        public String toString() {
            return toStringBuffer(null).toString();
        }

        protected StringBuffer toStringBuffer(StringBuffer stringBuffer) {
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
            }
            stringBuffer.append("ax: ");
            stringBuffer.append(this.ascentX);
            stringBuffer.append(" ay: ");
            stringBuffer.append(this.ascentY);
            stringBuffer.append(" dx: ");
            stringBuffer.append(this.descentX);
            stringBuffer.append(" dy: ");
            stringBuffer.append(this.descentY);
            stringBuffer.append(" lx: ");
            stringBuffer.append(this.leadingX);
            stringBuffer.append(" ly: ");
            stringBuffer.append(this.leadingY);
            return stringBuffer;
        }
    }
}
