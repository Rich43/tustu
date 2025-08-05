package org.icepdf.core.pobjects.graphics.text;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.logging.Logger;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/text/GlyphText.class */
public class GlyphText extends AbstractText {
    private static final Logger logger = Logger.getLogger(GlyphText.class.toString());

    /* renamed from: x, reason: collision with root package name */
    private float f13128x;

    /* renamed from: y, reason: collision with root package name */
    private float f13129y;
    private String cid;
    private String unicode;

    public GlyphText(float x2, float y2, Rectangle2D.Float bounds, String cid, String unicode) {
        this.f13128x = x2;
        this.f13129y = y2;
        this.bounds = bounds;
        this.cid = cid;
        this.unicode = unicode;
    }

    public void normalizeToUserSpace(AffineTransform af2) {
        GeneralPath generalPath = new GeneralPath(this.bounds);
        generalPath.transform(af2);
        this.bounds = (Rectangle2D.Float) generalPath.getBounds2D();
    }

    public String getCid() {
        return this.cid;
    }

    public String getUnicode() {
        return this.unicode;
    }

    public float getX() {
        return this.f13128x;
    }

    public float getY() {
        return this.f13129y;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.AbstractText, org.icepdf.core.pobjects.graphics.text.Text
    public Rectangle2D.Float getBounds() {
        return this.bounds;
    }
}
