package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import org.icepdf.core.pobjects.fonts.FontFile;
import org.icepdf.core.pobjects.graphics.text.GlyphText;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/TextSprite.class */
public class TextSprite {
    private static final boolean OPTIMIZED_DRAWING_ENABLED = Defs.booleanProperty("org.icepdf.core.text.optimized", true);
    private ArrayList<GlyphText> glyphTexts;
    Rectangle2D.Float bounds = new Rectangle2D.Float();
    private AffineTransform graphicStateTransform;
    private Color strokeColor;
    private int rmode;
    private FontFile font;
    private String fontName;
    private int fontSize;

    public TextSprite(FontFile font, int size, AffineTransform graphicStateTransform) {
        this.glyphTexts = new ArrayList<>(size);
        this.graphicStateTransform = graphicStateTransform;
        this.font = font;
    }

    public GlyphText addText(String cid, String unicode, float x2, float y2, float width) {
        float w2 = width;
        float h2 = (float) (this.font.getAscent() + this.font.getDescent());
        if (h2 <= 0.0f) {
            h2 = (float) this.font.getMaxCharBounds().getHeight();
        }
        if (w2 <= 0.0f) {
            w2 = (float) this.font.getMaxCharBounds().getWidth();
        }
        if (h2 <= 0.0f) {
            h2 = 1.0f;
        }
        if (w2 <= 0.0f) {
            w2 = 1.0f;
        }
        Rectangle2D.Float glyphBounds = new Rectangle2D.Float(x2, y2 - ((float) this.font.getAscent()), w2, h2);
        this.bounds.add(glyphBounds);
        GlyphText glyphText = new GlyphText(x2, y2, glyphBounds, cid, unicode);
        glyphText.normalizeToUserSpace(this.graphicStateTransform);
        this.glyphTexts.add(glyphText);
        return glyphText;
    }

    public ArrayList<GlyphText> getGlyphSprites() {
        return this.glyphTexts;
    }

    public AffineTransform getGraphicStateTransform() {
        return this.graphicStateTransform;
    }

    public void setGraphicStateTransform(AffineTransform graphicStateTransform) {
        this.graphicStateTransform = graphicStateTransform;
        Iterator i$ = this.glyphTexts.iterator();
        while (i$.hasNext()) {
            GlyphText sprite = i$.next();
            sprite.normalizeToUserSpace(this.graphicStateTransform);
        }
    }

    public void setRMode(int rmode) {
        if (rmode >= 0) {
            this.rmode = rmode;
        }
    }

    public String toString() {
        StringBuilder text = new StringBuilder(this.glyphTexts.size());
        Iterator i$ = this.glyphTexts.iterator();
        while (i$.hasNext()) {
            GlyphText glyphText = i$.next();
            text.append(glyphText.getUnicode());
        }
        return text.toString();
    }

    public void setStrokeColor(Color color) {
        this.strokeColor = color;
    }

    public Rectangle2D.Float getBounds() {
        return this.bounds;
    }

    public void paint(Graphics g2) {
        Graphics2D g2d = (Graphics2D) g2;
        Iterator i$ = this.glyphTexts.iterator();
        while (i$.hasNext()) {
            GlyphText glyphText = i$.next();
            this.font.drawEstring(g2d, glyphText.getCid(), glyphText.getX(), glyphText.getY(), 0L, this.rmode, this.strokeColor);
        }
    }

    public Area getGlyphOutline() {
        Area glyphOutline = null;
        Iterator i$ = this.glyphTexts.iterator();
        while (i$.hasNext()) {
            GlyphText glyphText = i$.next();
            if (glyphOutline != null) {
                glyphOutline.add(new Area(this.font.getEstringOutline(glyphText.getCid(), glyphText.getX(), glyphText.getY())));
            } else {
                glyphOutline = new Area(this.font.getEstringOutline(glyphText.getCid(), glyphText.getX(), glyphText.getY()));
            }
        }
        return glyphOutline;
    }

    public FontFile getFont() {
        return this.font;
    }

    public Color getStrokeColor() {
        return this.strokeColor;
    }

    public String getFontName() {
        return this.fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean intersects(Shape shape) {
        return !OPTIMIZED_DRAWING_ENABLED || (shape != null && shape.intersects(this.bounds));
    }
}
