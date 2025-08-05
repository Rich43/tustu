package com.sun.javafx.font;

import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/font/PrismFontStrike.class */
public abstract class PrismFontStrike<T extends PrismFontFile> implements FontStrike {
    private DisposerRecord disposer;
    private T fontResource;
    private PrismMetrics metrics;
    private float size;
    private BaseTransform transform;
    private int aaMode;
    private FontStrikeDesc desc;
    private int hash;
    private Map<Integer, Glyph> glyphMap = new HashMap();
    protected boolean drawShapes = false;

    protected abstract DisposerRecord createDisposer(FontStrikeDesc fontStrikeDesc);

    protected abstract Glyph createGlyph(int i2);

    protected abstract Path2D createGlyphOutline(int i2);

    protected PrismFontStrike(T fontResource, float size, BaseTransform tx, int aaMode, FontStrikeDesc desc) {
        this.fontResource = fontResource;
        this.size = size;
        this.desc = desc;
        PrismFontFactory factory = PrismFontFactory.getFontFactory();
        boolean lcdEnabled = factory.isLCDTextSupported();
        this.aaMode = lcdEnabled ? aaMode : 0;
        if (tx.isTranslateOrIdentity()) {
            this.transform = BaseTransform.IDENTITY_TRANSFORM;
        } else {
            this.transform = new Affine2D(tx.getMxx(), tx.getMyx(), tx.getMxy(), tx.getMyy(), 0.0d, 0.0d);
        }
    }

    DisposerRecord getDisposer() {
        if (this.disposer == null) {
            this.disposer = createDisposer(this.desc);
        }
        return this.disposer;
    }

    @Override // com.sun.javafx.font.FontStrike
    public synchronized void clearDesc() {
        this.fontResource.getStrikeMap().remove(this.desc);
    }

    @Override // com.sun.javafx.font.FontStrike
    public float getSize() {
        return this.size;
    }

    @Override // com.sun.javafx.font.FontStrike
    public Metrics getMetrics() {
        if (this.metrics == null) {
            this.metrics = this.fontResource.getFontMetrics(this.size);
        }
        return this.metrics;
    }

    @Override // com.sun.javafx.font.FontStrike
    public T getFontResource() {
        return this.fontResource;
    }

    @Override // com.sun.javafx.font.FontStrike
    public boolean drawAsShapes() {
        return this.drawShapes;
    }

    @Override // com.sun.javafx.font.FontStrike
    public int getAAMode() {
        return this.aaMode;
    }

    @Override // com.sun.javafx.font.FontStrike
    public BaseTransform getTransform() {
        return this.transform;
    }

    @Override // com.sun.javafx.font.FontStrike
    public int getQuantizedPosition(Point2D point) {
        if (this.aaMode == 0) {
            point.f11907x = Math.round(point.f11907x);
        } else {
            point.f11907x = Math.round(3.0d * point.f11907x) / 3.0f;
        }
        point.f11908y = Math.round(point.f11908y);
        return 0;
    }

    @Override // com.sun.javafx.font.FontStrike
    public float getCharAdvance(char ch) {
        int glyphCode = this.fontResource.getGlyphMapper().charToGlyph((int) ch);
        return this.fontResource.getAdvance(glyphCode, this.size);
    }

    @Override // com.sun.javafx.font.FontStrike
    public Glyph getGlyph(char ch) {
        int glyphCode = this.fontResource.getGlyphMapper().charToGlyph((int) ch);
        return getGlyph(glyphCode);
    }

    @Override // com.sun.javafx.font.FontStrike
    public Glyph getGlyph(int glyphCode) {
        Glyph glyph = this.glyphMap.get(Integer.valueOf(glyphCode));
        if (glyph == null) {
            glyph = createGlyph(glyphCode);
            this.glyphMap.put(Integer.valueOf(glyphCode), glyph);
        }
        return glyph;
    }

    @Override // com.sun.javafx.font.FontStrike
    public Shape getOutline(GlyphList gl, BaseTransform transform) {
        Path2D result = new Path2D();
        getOutline(gl, transform, result);
        return result;
    }

    void getOutline(GlyphList gl, BaseTransform transform, Path2D p2) {
        Shape gp;
        p2.reset();
        if (gl == null) {
            return;
        }
        if (transform == null) {
            transform = BaseTransform.IDENTITY_TRANSFORM;
        }
        Affine2D t2 = new Affine2D();
        for (int i2 = 0; i2 < gl.getGlyphCount(); i2++) {
            int glyphCode = gl.getGlyphCode(i2);
            if (glyphCode != 65535 && (gp = createGlyphOutline(glyphCode)) != null) {
                t2.setTransform(transform);
                t2.translate(gl.getPosX(i2), gl.getPosY(i2));
                p2.append(gp.getPathIterator(t2), false);
            }
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PrismFontStrike)) {
            return false;
        }
        PrismFontStrike other = (PrismFontStrike) obj;
        return this.size == other.size && this.transform.getMxx() == other.transform.getMxx() && this.transform.getMxy() == other.transform.getMxy() && this.transform.getMyx() == other.transform.getMyx() && this.transform.getMyy() == other.transform.getMyy() && this.fontResource.equals(other.fontResource);
    }

    public int hashCode() {
        if (this.hash != 0) {
            return this.hash;
        }
        this.hash = Float.floatToIntBits(this.size) + Float.floatToIntBits((float) this.transform.getMxx()) + Float.floatToIntBits((float) this.transform.getMyx()) + Float.floatToIntBits((float) this.transform.getMxy()) + Float.floatToIntBits((float) this.transform.getMyy());
        this.hash = (71 * this.hash) + this.fontResource.hashCode();
        return this.hash;
    }

    public String toString() {
        return "FontStrike: " + super.toString() + " font resource = " + ((Object) this.fontResource) + " size = " + this.size + " matrix = " + ((Object) this.transform);
    }
}
