package sun.font;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphJustificationInfo;
import java.awt.font.GraphicAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import sun.font.Decoration;

/* loaded from: rt.jar:sun/font/GraphicComponent.class */
public final class GraphicComponent implements TextLineComponent, Decoration.Label {
    public static final float GRAPHIC_LEADING = 2.0f;
    private GraphicAttribute graphic;
    private int graphicCount;
    private int[] charsLtoV;
    private byte[] levels;
    private Rectangle2D visualBounds = null;
    private float graphicAdvance;
    private AffineTransform baseTx;
    private CoreMetrics cm;
    private Decoration decorator;

    public GraphicComponent(GraphicAttribute graphicAttribute, Decoration decoration, int[] iArr, byte[] bArr, int i2, int i3, AffineTransform affineTransform) {
        if (i3 <= i2) {
            throw new IllegalArgumentException("0 or negative length in GraphicComponent");
        }
        this.graphic = graphicAttribute;
        this.graphicAdvance = graphicAttribute.getAdvance();
        this.decorator = decoration;
        this.cm = createCoreMetrics(graphicAttribute);
        this.baseTx = affineTransform;
        initLocalOrdering(iArr, bArr, i2, i3);
    }

    private GraphicComponent(GraphicComponent graphicComponent, int i2, int i3, int i4) {
        this.graphic = graphicComponent.graphic;
        this.graphicAdvance = graphicComponent.graphicAdvance;
        this.decorator = graphicComponent.decorator;
        this.cm = graphicComponent.cm;
        this.baseTx = graphicComponent.baseTx;
        int[] iArr = null;
        byte[] bArr = null;
        if (i4 == 2) {
            iArr = graphicComponent.charsLtoV;
            bArr = graphicComponent.levels;
        } else if (i4 == 0 || i4 == 1) {
            i3 -= i2;
            i2 = 0;
            if (i4 == 1) {
                iArr = new int[i3];
                bArr = new byte[i3];
                for (int i5 = 0; i5 < i3; i5++) {
                    iArr[i5] = (i3 - i5) - 1;
                    bArr[i5] = 1;
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid direction flag");
        }
        initLocalOrdering(iArr, bArr, i2, i3);
    }

    private void initLocalOrdering(int[] iArr, byte[] bArr, int i2, int i3) {
        this.graphicCount = i3 - i2;
        if (iArr == null || iArr.length == this.graphicCount) {
            this.charsLtoV = iArr;
        } else {
            this.charsLtoV = BidiUtils.createNormalizedMap(iArr, bArr, i2, i3);
        }
        if (bArr == null || bArr.length == this.graphicCount) {
            this.levels = bArr;
        } else {
            this.levels = new byte[this.graphicCount];
            System.arraycopy(bArr, i2, this.levels, 0, this.graphicCount);
        }
    }

    @Override // sun.font.TextLineComponent
    public boolean isSimple() {
        return false;
    }

    @Override // sun.font.TextLineComponent
    public Rectangle getPixelBounds(FontRenderContext fontRenderContext, float f2, float f3) {
        throw new InternalError("do not call if isSimple returns false");
    }

    @Override // sun.font.Decoration.Label
    public Rectangle2D handleGetVisualBounds() {
        Rectangle2D bounds = this.graphic.getBounds();
        return new Rectangle2D.Float((float) bounds.getX(), (float) bounds.getY(), ((float) bounds.getWidth()) + (this.graphicAdvance * (this.graphicCount - 1)), (float) bounds.getHeight());
    }

    @Override // sun.font.TextLineComponent
    public CoreMetrics getCoreMetrics() {
        return this.cm;
    }

    public static CoreMetrics createCoreMetrics(GraphicAttribute graphicAttribute) {
        return new CoreMetrics(graphicAttribute.getAscent(), graphicAttribute.getDescent(), 2.0f, graphicAttribute.getAscent() + graphicAttribute.getDescent() + 2.0f, graphicAttribute.getAlignment(), new float[]{0.0f, (-graphicAttribute.getAscent()) / 2.0f, -graphicAttribute.getAscent()}, (-graphicAttribute.getAscent()) / 2.0f, graphicAttribute.getAscent() / 12.0f, graphicAttribute.getDescent() / 3.0f, graphicAttribute.getAscent() / 12.0f, 0.0f, 0.0f);
    }

    public float getItalicAngle() {
        return 0.0f;
    }

    @Override // sun.font.TextLineComponent
    public Rectangle2D getVisualBounds() {
        if (this.visualBounds == null) {
            this.visualBounds = this.decorator.getVisualBounds(this);
        }
        Rectangle2D.Float r0 = new Rectangle2D.Float();
        r0.setRect(this.visualBounds);
        return r0;
    }

    @Override // sun.font.Decoration.Label
    public Shape handleGetOutline(float f2, float f3) {
        double[] dArr = {1.0d, 0.0d, 0.0d, 1.0d, f2, f3};
        if (this.graphicCount == 1) {
            return this.graphic.getOutline(new AffineTransform(dArr));
        }
        GeneralPath generalPath = new GeneralPath();
        for (int i2 = 0; i2 < this.graphicCount; i2++) {
            generalPath.append(this.graphic.getOutline(new AffineTransform(dArr)), false);
            dArr[4] = dArr[4] + this.graphicAdvance;
        }
        return generalPath;
    }

    @Override // sun.font.TextLineComponent
    public AffineTransform getBaselineTransform() {
        return this.baseTx;
    }

    @Override // sun.font.TextLineComponent
    public Shape getOutline(float f2, float f3) {
        return this.decorator.getOutline(this, f2, f3);
    }

    @Override // sun.font.Decoration.Label
    public void handleDraw(Graphics2D graphics2D, float f2, float f3) {
        for (int i2 = 0; i2 < this.graphicCount; i2++) {
            this.graphic.draw(graphics2D, f2, f3);
            f2 += this.graphicAdvance;
        }
    }

    @Override // sun.font.TextLineComponent
    public void draw(Graphics2D graphics2D, float f2, float f3) {
        this.decorator.drawTextAndDecorations(this, graphics2D, f2, f3);
    }

    @Override // sun.font.TextLineComponent
    public Rectangle2D getCharVisualBounds(int i2) {
        return this.decorator.getCharVisualBounds(this, i2);
    }

    @Override // sun.font.TextLineComponent
    public int getNumCharacters() {
        return this.graphicCount;
    }

    @Override // sun.font.TextLineComponent
    public float getCharX(int i2) {
        return this.graphicAdvance * (this.charsLtoV == null ? i2 : this.charsLtoV[i2]);
    }

    @Override // sun.font.TextLineComponent
    public float getCharY(int i2) {
        return 0.0f;
    }

    @Override // sun.font.TextLineComponent
    public float getCharAdvance(int i2) {
        return this.graphicAdvance;
    }

    @Override // sun.font.TextLineComponent
    public boolean caretAtOffsetIsValid(int i2) {
        return true;
    }

    @Override // sun.font.Decoration.Label
    public Rectangle2D handleGetCharVisualBounds(int i2) {
        Rectangle2D bounds = this.graphic.getBounds();
        Rectangle2D.Float r0 = new Rectangle2D.Float();
        r0.setRect(bounds);
        r0.f12404x += this.graphicAdvance * i2;
        return r0;
    }

    @Override // sun.font.TextLineComponent
    public int getLineBreakIndex(int i2, float f2) {
        int i3 = (int) (f2 / this.graphicAdvance);
        if (i3 > this.graphicCount - i2) {
            i3 = this.graphicCount - i2;
        }
        return i3;
    }

    @Override // sun.font.TextLineComponent
    public float getAdvanceBetween(int i2, int i3) {
        return this.graphicAdvance * (i3 - i2);
    }

    @Override // sun.font.TextLineComponent, sun.font.Decoration.Label
    public Rectangle2D getLogicalBounds() {
        float f2 = -this.cm.ascent;
        return new Rectangle2D.Float(0.0f, f2, this.graphicAdvance * this.graphicCount, this.cm.descent - f2);
    }

    @Override // sun.font.TextLineComponent
    public float getAdvance() {
        return this.graphicAdvance * this.graphicCount;
    }

    @Override // sun.font.TextLineComponent
    public Rectangle2D getItalicBounds() {
        return getLogicalBounds();
    }

    @Override // sun.font.TextLineComponent
    public TextLineComponent getSubset(int i2, int i3, int i4) {
        if (i2 < 0 || i3 > this.graphicCount || i2 >= i3) {
            throw new IllegalArgumentException("Invalid range.  start=" + i2 + "; limit=" + i3);
        }
        if (i2 == 0 && i3 == this.graphicCount && i4 == 2) {
            return this;
        }
        return new GraphicComponent(this, i2, i3, i4);
    }

    public String toString() {
        return "[graphic=" + ((Object) this.graphic) + ":count=" + getNumCharacters() + "]";
    }

    @Override // sun.font.TextLineComponent
    public int getNumJustificationInfos() {
        return 0;
    }

    @Override // sun.font.TextLineComponent
    public void getJustificationInfos(GlyphJustificationInfo[] glyphJustificationInfoArr, int i2, int i3, int i4) {
    }

    @Override // sun.font.TextLineComponent
    public TextLineComponent applyJustificationDeltas(float[] fArr, int i2, boolean[] zArr) {
        return this;
    }
}
