package sun.font;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/TextSourceLabel.class */
public class TextSourceLabel extends TextLabel {
    TextSource source;
    Rectangle2D lb;

    /* renamed from: ab, reason: collision with root package name */
    Rectangle2D f13557ab;
    Rectangle2D vb;
    Rectangle2D ib;
    GlyphVector gv;

    public TextSourceLabel(TextSource textSource) {
        this(textSource, null, null, null);
    }

    public TextSourceLabel(TextSource textSource, Rectangle2D rectangle2D, Rectangle2D rectangle2D2, GlyphVector glyphVector) {
        this.source = textSource;
        this.lb = rectangle2D;
        this.f13557ab = rectangle2D2;
        this.gv = glyphVector;
    }

    public TextSource getSource() {
        return this.source;
    }

    @Override // sun.font.TextLabel
    public final Rectangle2D getLogicalBounds(float f2, float f3) {
        if (this.lb == null) {
            this.lb = createLogicalBounds();
        }
        return new Rectangle2D.Float((float) (this.lb.getX() + f2), (float) (this.lb.getY() + f3), (float) this.lb.getWidth(), (float) this.lb.getHeight());
    }

    @Override // sun.font.TextLabel
    public final Rectangle2D getVisualBounds(float f2, float f3) {
        if (this.vb == null) {
            this.vb = createVisualBounds();
        }
        return new Rectangle2D.Float((float) (this.vb.getX() + f2), (float) (this.vb.getY() + f3), (float) this.vb.getWidth(), (float) this.vb.getHeight());
    }

    @Override // sun.font.TextLabel
    public final Rectangle2D getAlignBounds(float f2, float f3) {
        if (this.f13557ab == null) {
            this.f13557ab = createAlignBounds();
        }
        return new Rectangle2D.Float((float) (this.f13557ab.getX() + f2), (float) (this.f13557ab.getY() + f3), (float) this.f13557ab.getWidth(), (float) this.f13557ab.getHeight());
    }

    @Override // sun.font.TextLabel
    public Rectangle2D getItalicBounds(float f2, float f3) {
        if (this.ib == null) {
            this.ib = createItalicBounds();
        }
        return new Rectangle2D.Float((float) (this.ib.getX() + f2), (float) (this.ib.getY() + f3), (float) this.ib.getWidth(), (float) this.ib.getHeight());
    }

    public Rectangle getPixelBounds(FontRenderContext fontRenderContext, float f2, float f3) {
        return getGV().getPixelBounds(fontRenderContext, f2, f3);
    }

    public AffineTransform getBaselineTransform() {
        Font font = this.source.getFont();
        if (font.hasLayoutAttributes()) {
            return AttributeValues.getBaselineTransform(font.getAttributes());
        }
        return null;
    }

    @Override // sun.font.TextLabel, sun.font.TextLineComponent
    public Shape getOutline(float f2, float f3) {
        return getGV().getOutline(f2, f3);
    }

    @Override // sun.font.TextLabel, sun.font.TextLineComponent
    public void draw(Graphics2D graphics2D, float f2, float f3) {
        graphics2D.drawGlyphVector(getGV(), f2, f3);
    }

    protected Rectangle2D createLogicalBounds() {
        return getGV().getLogicalBounds();
    }

    protected Rectangle2D createVisualBounds() {
        return getGV().getVisualBounds();
    }

    protected Rectangle2D createItalicBounds() {
        return getGV().getLogicalBounds();
    }

    protected Rectangle2D createAlignBounds() {
        return createLogicalBounds();
    }

    private final GlyphVector getGV() {
        if (this.gv == null) {
            this.gv = createGV();
        }
        return this.gv;
    }

    protected GlyphVector createGV() {
        Font font = this.source.getFont();
        FontRenderContext frc = this.source.getFRC();
        int layoutFlags = this.source.getLayoutFlags();
        char[] chars = this.source.getChars();
        int start = this.source.getStart();
        int length = this.source.getLength();
        GlyphLayout glyphLayout = GlyphLayout.get(null);
        StandardGlyphVector standardGlyphVectorLayout = glyphLayout.layout(font, frc, chars, start, length, layoutFlags, null);
        GlyphLayout.done(glyphLayout);
        return standardGlyphVectorLayout;
    }
}
