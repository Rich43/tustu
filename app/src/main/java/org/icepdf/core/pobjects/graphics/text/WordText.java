package org.icepdf.core.pobjects.graphics.text;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/text/WordText.class */
public class WordText extends AbstractText implements TextSelect {
    private static final Logger logger = Logger.getLogger(WordText.class.toString());
    public static int spaceFraction;
    private static boolean autoSpaceInsertion;
    private GlyphText currentGlyph;
    private boolean isWhiteSpace;
    private int previousGlyphText;
    private StringBuilder text = new StringBuilder();
    private ArrayList<GlyphText> glyphs = new ArrayList<>(4);

    static {
        try {
            spaceFraction = Defs.sysPropertyInt("org.icepdf.core.views.page.text.spaceFraction", 3);
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading text space fraction");
            }
        }
        try {
            autoSpaceInsertion = Defs.sysPropertyBoolean("org.icepdf.core.views.page.text.autoSpace", true);
        } catch (NumberFormatException e3) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading text text auto space detection");
            }
        }
    }

    public boolean isWhiteSpace() {
        return this.isWhiteSpace;
    }

    public void setWhiteSpace(boolean whiteSpace) {
        this.isWhiteSpace = whiteSpace;
    }

    protected boolean detectSpace(GlyphText sprite) {
        if (this.currentGlyph != null) {
            Rectangle2D.Float bounds1 = this.currentGlyph.getBounds();
            float spriteXCoord = sprite.getBounds().f12404x;
            float space = Math.abs(spriteXCoord - (bounds1.f12404x + bounds1.width));
            float tolerance = bounds1.width / spaceFraction;
            return space > tolerance;
        }
        return false;
    }

    protected static boolean detectPunctuation(GlyphText sprite, WordText currentWord) {
        String glyphText = sprite.getUnicode();
        if (glyphText != null && glyphText.length() > 0) {
            int c2 = glyphText.charAt(0);
            return isPunctuation(c2) && !isDigit(currentWord);
        }
        return false;
    }

    protected static boolean detectWhiteSpace(GlyphText sprite) {
        String glyphText = sprite.getUnicode();
        if (glyphText != null && glyphText.length() > 0) {
            int c2 = glyphText.charAt(0);
            return isWhiteSpace(c2);
        }
        return false;
    }

    public static boolean isPunctuation(int c2) {
        return c2 == 46 || c2 == 44 || c2 == 63 || c2 == 33 || c2 == 58 || c2 == 59 || c2 == 34 || c2 == 39 || c2 == 47 || c2 == 92 || c2 == 96 || c2 == 35;
    }

    public static boolean isWhiteSpace(int c2) {
        return c2 == 32 || c2 == 9 || c2 == 13 || c2 == 10 || c2 == 12;
    }

    public static boolean isDigit(WordText currentWord) {
        if (currentWord != null) {
            int c2 = currentWord.getPreviousGlyphText();
            return isDigit((char) c2);
        }
        return false;
    }

    public static boolean isDigit(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    protected WordText buildSpaceWord(GlyphText sprite) {
        double offset;
        Rectangle2D.Float spaceBounds;
        double d2;
        Rectangle2D.Float bounds1 = this.currentGlyph.getBounds();
        Rectangle2D.Float bounds2 = sprite.getBounds();
        float space = bounds2.f12404x - (bounds1.f12404x + bounds1.width);
        float maxWidth = Math.max(bounds1.width, bounds2.width) / 2.0f;
        int spaces = (int) (space / maxWidth);
        if (spaces == 0) {
            spaces = 1;
        }
        WordText whiteSpace = new WordText();
        float spaceWidth = space / spaces;
        boolean ltr = true;
        if (spaces > 0) {
            offset = bounds1.f12404x + bounds1.width;
            spaceBounds = new Rectangle2D.Float(bounds1.f12404x + bounds1.width, bounds1.f12405y, spaceWidth, bounds1.height);
        } else {
            ltr = false;
            offset = bounds1.f12404x - bounds1.width;
            spaces = 1;
            spaceBounds = new Rectangle2D.Float(this.bounds.f12404x - spaceWidth, bounds1.f12405y, spaceWidth, bounds1.height);
        }
        if (autoSpaceInsertion) {
            for (int i2 = 0; i2 < spaces && i2 < 50; i2++) {
                whiteSpace = autoSpaceCalculation(offset, spaceBounds, whiteSpace);
                if (ltr) {
                    spaceBounds.f12404x += spaceBounds.width;
                    d2 = offset + spaceWidth;
                } else {
                    spaceBounds.f12404x -= spaceBounds.width;
                    d2 = offset - spaceWidth;
                }
                offset = d2;
            }
        } else {
            whiteSpace = autoSpaceCalculation(offset, spaceBounds, whiteSpace);
        }
        return whiteSpace;
    }

    private WordText autoSpaceCalculation(double offset, Rectangle2D.Float spaceBounds, WordText whiteSpace) {
        GlyphText spaceText = new GlyphText((float) offset, this.currentGlyph.getY(), new Rectangle2D.Float(spaceBounds.f12404x, spaceBounds.f12405y, spaceBounds.width, spaceBounds.height), String.valueOf(' '), String.valueOf(' '));
        whiteSpace.addText(spaceText);
        whiteSpace.setWhiteSpace(true);
        return whiteSpace;
    }

    protected void addText(GlyphText sprite) {
        this.glyphs.add(sprite);
        this.currentGlyph = sprite;
        if (this.bounds == null) {
            Rectangle2D.Float rect = sprite.getBounds();
            this.bounds = new Rectangle2D.Float(rect.f12404x, rect.f12405y, rect.width, rect.height);
        } else {
            this.bounds.add(sprite.getBounds());
        }
        String unicode = sprite.getUnicode();
        this.previousGlyphText = (unicode == null || unicode.length() <= 0) ? (char) 0 : unicode.charAt(0);
        this.text.append(unicode);
    }

    @Override // org.icepdf.core.pobjects.graphics.text.AbstractText, org.icepdf.core.pobjects.graphics.text.Text
    public Rectangle2D.Float getBounds() {
        if (this.bounds == null) {
            Iterator i$ = this.glyphs.iterator();
            while (i$.hasNext()) {
                GlyphText glyph = i$.next();
                if (this.bounds == null) {
                    this.bounds = new Rectangle2D.Float();
                    this.bounds.setRect(glyph.getBounds());
                } else {
                    this.bounds.add(glyph.getBounds());
                }
            }
        }
        return this.bounds;
    }

    public ArrayList<GlyphText> getGlyphs() {
        return this.glyphs;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public StringBuilder getSelected() {
        StringBuilder selectedText = new StringBuilder();
        Iterator i$ = this.glyphs.iterator();
        while (i$.hasNext()) {
            GlyphText glyph = i$.next();
            if (glyph.isSelected()) {
                selectedText.append(glyph.getUnicode());
            }
        }
        return selectedText;
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public void clearHighlighted() {
        setHighlighted(false);
        setHasHighlight(false);
        Iterator i$ = this.glyphs.iterator();
        while (i$.hasNext()) {
            GlyphText glyph = i$.next();
            glyph.setHighlighted(false);
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public void clearSelected() {
        setSelected(false);
        setHasSelected(false);
        Iterator i$ = this.glyphs.iterator();
        while (i$.hasNext()) {
            GlyphText glyph = i$.next();
            glyph.setSelected(false);
        }
    }

    @Override // org.icepdf.core.pobjects.graphics.text.TextSelect
    public void selectAll() {
        setSelected(true);
        setHasSelected(true);
        Iterator i$ = this.glyphs.iterator();
        while (i$.hasNext()) {
            GlyphText glyph = i$.next();
            glyph.setSelected(true);
        }
    }

    public String getText() {
        return this.text.toString();
    }

    public int getPreviousGlyphText() {
        return this.previousGlyphText;
    }

    public String toString() {
        return getText();
    }
}
