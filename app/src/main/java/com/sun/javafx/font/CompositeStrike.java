package com.sun.javafx.font;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;

/* loaded from: jfxrt.jar:com/sun/javafx/font/CompositeStrike.class */
public class CompositeStrike implements FontStrike {
    private CompositeFontResource fontResource;
    private float size;
    private int aaMode;
    BaseTransform transform;
    private FontStrike slot0Strike;
    private FontStrike[] strikeSlots;
    private FontStrikeDesc desc;
    DisposerRecord disposer;
    private PrismMetrics metrics;

    @Override // com.sun.javafx.font.FontStrike
    public void clearDesc() {
        this.fontResource.getStrikeMap().remove(this.desc);
        if (this.slot0Strike != null) {
            this.slot0Strike.clearDesc();
        }
        if (this.strikeSlots != null) {
            for (int i2 = 1; i2 < this.strikeSlots.length; i2++) {
                if (this.strikeSlots[i2] != null) {
                    this.strikeSlots[i2].clearDesc();
                }
            }
        }
    }

    CompositeStrike(CompositeFontResource fontResource, float size, BaseTransform graphicsTransform, int aaMode, FontStrikeDesc desc) {
        this.fontResource = fontResource;
        this.size = size;
        if (graphicsTransform.isTranslateOrIdentity()) {
            this.transform = BaseTransform.IDENTITY_TRANSFORM;
        } else {
            this.transform = graphicsTransform.copy();
        }
        this.desc = desc;
        this.aaMode = aaMode;
        this.disposer = new CompositeStrikeDisposer(fontResource, desc);
    }

    @Override // com.sun.javafx.font.FontStrike
    public int getAAMode() {
        PrismFontFactory factory = PrismFontFactory.getFontFactory();
        if (factory.isLCDTextSupported()) {
            return this.aaMode;
        }
        return 0;
    }

    @Override // com.sun.javafx.font.FontStrike
    public BaseTransform getTransform() {
        return this.transform;
    }

    public FontStrike getStrikeSlot(int slot) {
        if (slot == 0) {
            if (this.slot0Strike == null) {
                FontResource slot0Resource = this.fontResource.getSlotResource(0);
                this.slot0Strike = slot0Resource.getStrike(this.size, this.transform, getAAMode());
            }
            return this.slot0Strike;
        }
        if (this.strikeSlots == null) {
            this.strikeSlots = new FontStrike[this.fontResource.getNumSlots()];
        }
        if (slot >= this.strikeSlots.length) {
            FontStrike[] tmp = new FontStrike[this.fontResource.getNumSlots()];
            System.arraycopy(this.strikeSlots, 0, tmp, 0, this.strikeSlots.length);
            this.strikeSlots = tmp;
        }
        if (this.strikeSlots[slot] == null) {
            FontResource slotResource = this.fontResource.getSlotResource(slot);
            this.strikeSlots[slot] = slotResource.getStrike(this.size, this.transform, getAAMode());
        }
        return this.strikeSlots[slot];
    }

    @Override // com.sun.javafx.font.FontStrike
    public FontResource getFontResource() {
        return this.fontResource;
    }

    public int getStrikeSlotForGlyph(int glyphCode) {
        return glyphCode >>> 24;
    }

    @Override // com.sun.javafx.font.FontStrike
    public float getSize() {
        return this.size;
    }

    @Override // com.sun.javafx.font.FontStrike
    public boolean drawAsShapes() {
        return getStrikeSlot(0).drawAsShapes();
    }

    @Override // com.sun.javafx.font.FontStrike
    public Metrics getMetrics() {
        if (this.metrics == null) {
            PrismFontFile fr = (PrismFontFile) this.fontResource.getSlotResource(0);
            this.metrics = fr.getFontMetrics(this.size);
        }
        return this.metrics;
    }

    @Override // com.sun.javafx.font.FontStrike
    public Glyph getGlyph(char symbol) {
        int glyphCode = this.fontResource.getGlyphMapper().charToGlyph(symbol);
        return getGlyph(glyphCode);
    }

    @Override // com.sun.javafx.font.FontStrike
    public Glyph getGlyph(int glyphCode) {
        int slot = glyphCode >>> 24;
        int slotglyphCode = glyphCode & 16777215;
        return getStrikeSlot(slot).getGlyph(slotglyphCode);
    }

    @Override // com.sun.javafx.font.FontStrike
    public float getCharAdvance(char ch) {
        int glyphCode = this.fontResource.getGlyphMapper().charToGlyph((int) ch);
        return this.fontResource.getAdvance(glyphCode, this.size);
    }

    @Override // com.sun.javafx.font.FontStrike
    public int getQuantizedPosition(Point2D point) {
        return getStrikeSlot(0).getQuantizedPosition(point);
    }

    @Override // com.sun.javafx.font.FontStrike
    public Shape getOutline(GlyphList gl, BaseTransform transform) {
        Path2D result = new Path2D();
        getOutline(gl, transform, result);
        return result;
    }

    void getOutline(GlyphList gl, BaseTransform transform, Path2D p2) {
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
            if (glyphCode != 65535) {
                Glyph glyph = getGlyph(glyphCode);
                Shape gp = glyph.getShape();
                if (gp != null) {
                    t2.setTransform(transform);
                    t2.translate(gl.getPosX(i2), gl.getPosY(i2));
                    p2.append(gp.getPathIterator(t2), false);
                }
            }
        }
    }
}
