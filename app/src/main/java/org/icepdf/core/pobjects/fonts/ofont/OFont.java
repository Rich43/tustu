package org.icepdf.core.pobjects.fonts.ofont;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.fonts.FontFile;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/ofont/OFont.class */
public class OFont implements FontFile {
    private static final Logger log = Logger.getLogger(OFont.class.toString());
    private java.awt.Font awtFont;
    private Rectangle2D maxCharBounds;
    private HashMap<String, Point2D.Float> echarAdvanceCache;
    protected float[] widths;
    protected Map<Integer, Float> cidWidths;
    protected float missingWidth;
    protected int firstCh;
    protected float ascent;
    protected float descent;
    protected org.icepdf.core.pobjects.fonts.Encoding encoding;
    protected org.icepdf.core.pobjects.fonts.CMap toUnicode;
    protected char[] cMap;

    public OFont(java.awt.Font awtFont) {
        this.maxCharBounds = new Rectangle2D.Double(0.0d, 0.0d, 1.0d, 1.0d);
        this.awtFont = awtFont;
        this.maxCharBounds = new Rectangle2D.Double();
        this.echarAdvanceCache = new HashMap<>(256);
    }

    private OFont(OFont font) {
        this.maxCharBounds = new Rectangle2D.Double(0.0d, 0.0d, 1.0d, 1.0d);
        this.echarAdvanceCache = font.echarAdvanceCache;
        this.awtFont = font.awtFont;
        this.encoding = font.encoding;
        this.toUnicode = font.toUnicode;
        this.missingWidth = font.missingWidth;
        this.firstCh = font.firstCh;
        this.ascent = font.ascent;
        this.descent = font.descent;
        this.widths = font.widths;
        this.cidWidths = font.cidWidths;
        this.cMap = font.cMap;
        this.maxCharBounds = font.maxCharBounds;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public FontFile deriveFont(org.icepdf.core.pobjects.fonts.Encoding encoding, org.icepdf.core.pobjects.fonts.CMap toUnicode) {
        OFont font = new OFont(this);
        this.echarAdvanceCache.clear();
        font.encoding = encoding;
        font.toUnicode = toUnicode;
        return font;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public FontFile deriveFont(float[] widths, int firstCh, float missingWidth, float ascent, float descent, char[] diff) {
        OFont font = new OFont(this);
        this.echarAdvanceCache.clear();
        font.missingWidth = this.missingWidth;
        font.firstCh = firstCh;
        font.ascent = ascent;
        font.descent = descent;
        font.widths = widths;
        font.cMap = diff;
        return font;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public FontFile deriveFont(Map<Integer, Float> widths, int firstCh, float missingWidth, float ascent, float descent, char[] diff) {
        OFont font = new OFont(this);
        this.echarAdvanceCache.clear();
        font.missingWidth = this.missingWidth;
        font.firstCh = firstCh;
        font.ascent = ascent;
        font.descent = descent;
        font.cidWidths = widths;
        font.cMap = diff;
        return font;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public FontFile deriveFont(AffineTransform at2) {
        OFont font = new OFont(this);
        if (!font.getTransform().equals(this.awtFont.getTransform())) {
            this.echarAdvanceCache.clear();
        }
        font.awtFont = this.awtFont.deriveFont(at2);
        font.maxCharBounds = this.maxCharBounds;
        return font;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public boolean canDisplayEchar(char ech) {
        return true;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public FontFile deriveFont(float pointsize) {
        OFont font = new OFont(this);
        if (font.getSize() != pointsize) {
            this.echarAdvanceCache.clear();
        }
        font.awtFont = this.awtFont.deriveFont(pointsize);
        font.maxCharBounds = this.maxCharBounds;
        return font;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public Point2D echarAdvance(char ech) {
        float advance;
        float advanceY;
        String text = String.valueOf(ech);
        Point2D.Float echarAdvance = this.echarAdvanceCache.get(text);
        if (echarAdvance == null) {
            char echGlyph = getCMapping(ech);
            GlyphVector glyphVector = this.awtFont.createGlyphVector(new FontRenderContext(new AffineTransform(), true, true), String.valueOf(echGlyph));
            FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
            TextLayout textLayout = new TextLayout(String.valueOf(echGlyph), this.awtFont, frc);
            this.maxCharBounds = this.awtFont.getMaxCharBounds(frc);
            this.ascent = textLayout.getAscent();
            this.descent = textLayout.getDescent();
            GlyphMetrics glyphMetrics = glyphVector.getGlyphMetrics(0);
            advance = glyphMetrics.getAdvanceX();
            advanceY = glyphMetrics.getAdvanceY();
            this.echarAdvanceCache.put(text, new Point2D.Float(advance, advanceY));
        } else {
            advance = echarAdvance.f12396x;
            advanceY = echarAdvance.f12397y;
        }
        if (this.widths != null && ech - this.firstCh >= 0 && ech - this.firstCh < this.widths.length) {
            advance = this.widths[ech - this.firstCh] * this.awtFont.getSize2D();
        } else if (this.cidWidths != null) {
            Float width = this.cidWidths.get(Integer.valueOf(ech));
            if (width != null) {
                advance = this.cidWidths.get(Integer.valueOf(ech)).floatValue() * this.awtFont.getSize2D();
            }
        } else if (this.missingWidth > 0.0f) {
            advance = this.missingWidth / 1000.0f;
        }
        return new Point2D.Float(advance, advanceY);
    }

    private char getCMapping(char currentChar) {
        if (this.toUnicode != null) {
            return this.toUnicode.toSelector(currentChar);
        }
        return currentChar;
    }

    public char getCharDiff(char character) {
        if (this.cMap != null && character < this.cMap.length) {
            return this.cMap[character];
        }
        return character;
    }

    private char findAlternateSymbol(char character) {
        for (int i2 = 0; i2 < Encoding.symbolAlaises.length; i2++) {
            for (int j2 = 0; j2 < Encoding.symbolAlaises[i2].length; j2++) {
                if (Encoding.symbolAlaises[i2][j2] == character) {
                    return (char) Encoding.symbolAlaises[i2][0];
                }
            }
        }
        return character;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public org.icepdf.core.pobjects.fonts.CMap getToUnicode() {
        return this.toUnicode;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public int getStyle() {
        return this.awtFont.getStyle();
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public String getFamily() {
        return this.awtFont.getFamily();
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public float getSize() {
        return this.awtFont.getSize();
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public double getAscent() {
        return this.ascent;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public double getDescent() {
        return this.descent;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public Rectangle2D getMaxCharBounds() {
        return this.maxCharBounds;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public AffineTransform getTransform() {
        return this.awtFont.getTransform();
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public int getRights() {
        return 0;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public String getName() {
        return this.awtFont.getName();
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public boolean isHinted() {
        return false;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public int getNumGlyphs() {
        return this.awtFont.getNumGlyphs();
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public char getSpaceEchar() {
        return ' ';
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public Rectangle2D getEstringBounds(String estr, int beginIndex, int limit) {
        return null;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public String getFormat() {
        return null;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public void drawEstring(Graphics2D g2, String displayText, float x2, float y2, long layout, int mode, Color strokecolor) {
        AffineTransform af2 = g2.getTransform();
        Shape outline = getEstringOutline(displayText, x2, y2);
        if (0 == mode || 2 == mode || 4 == mode || 6 == mode) {
            g2.fill(outline);
        }
        if (1 == mode || 2 == mode || 5 == mode || 6 == mode) {
            g2.draw(outline);
        }
        g2.setTransform(af2);
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public String toUnicode(String displayText) {
        StringBuilder sb = new StringBuilder(displayText.length());
        for (int i2 = 0; i2 < displayText.length(); i2++) {
            sb.append(toUnicode(displayText.charAt(i2)));
        }
        return sb.toString();
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public String toUnicode(char c1) {
        char c2 = this.toUnicode == null ? getCharDiff(c1) : c1;
        if (this.toUnicode != null) {
            return this.toUnicode.toUnicode(c2);
        }
        char c3 = getCMapping(c2);
        if (!this.awtFont.canDisplay(c3)) {
            c3 = (char) (c3 | 61440);
        }
        if (!this.awtFont.canDisplay(c3)) {
            c3 = findAlternateSymbol(c3);
        }
        if (log.isLoggable(Level.FINER) && !this.awtFont.canDisplay(c3)) {
            log.finer(((int) c1) + " " + Character.toString(c1) + " " + ((int) c3) + " " + c3 + " " + ((Object) this.awtFont));
        }
        return String.valueOf(c3);
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public boolean isOneByteEncoding() {
        return false;
    }

    @Override // org.icepdf.core.pobjects.fonts.FontFile
    public Shape getEstringOutline(String displayText, float x2, float y2) {
        String displayText2 = toUnicode(displayText);
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
        GlyphVector glyphVector = this.awtFont.createGlyphVector(frc, displayText2);
        glyphVector.setGlyphPosition(0, new Point2D.Float(x2, y2));
        int displayLength = displayText2.length();
        if (displayLength > 1) {
            float advance = 0.0f;
            for (int i2 = 0; i2 < displayText2.length(); i2++) {
                Point2D p2 = glyphVector.getGlyphPosition(i2);
                float lastx = (float) p2.getX();
                glyphVector.setGlyphPosition(i2, new Point2D.Double(lastx + advance, p2.getY()));
                float adv1 = glyphVector.getGlyphMetrics(i2).getAdvance();
                double adv2 = echarAdvance(displayText2.charAt(i2)).getX();
                advance = (float) (advance + (-adv1) + adv2 + lastx);
            }
        }
        return glyphVector.getOutline();
    }
}
