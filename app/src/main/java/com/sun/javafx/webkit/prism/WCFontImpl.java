package com.sun.javafx.webkit.prism;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.prism.GraphicsPipeline;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCTextRun;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCFontImpl.class */
final class WCFontImpl extends WCFont {
    private static final Logger log = Logger.getLogger(WCFontImpl.class.getName());
    private static final HashMap<String, String> FONT_MAP = new HashMap<>();
    private final PGFont font;
    private FontStrike strike;

    static WCFont getFont(String name, boolean bold, boolean italic, float size) {
        FontFactory factory = GraphicsPipeline.getPipeline().getFontFactory();
        synchronized (FONT_MAP) {
            if (FONT_MAP.isEmpty()) {
                FONT_MAP.put("serif", "Serif");
                FONT_MAP.put("dialog", "SansSerif");
                FONT_MAP.put("helvetica", "SansSerif");
                FONT_MAP.put("sansserif", "SansSerif");
                FONT_MAP.put("sans-serif", "SansSerif");
                FONT_MAP.put("monospace", "Monospaced");
                FONT_MAP.put("monospaced", "Monospaced");
                FONT_MAP.put("times", "Times New Roman");
                FONT_MAP.put("courier", "Courier New");
                for (String family : factory.getFontFamilyNames()) {
                    FONT_MAP.put(family.toLowerCase(), family);
                }
            }
        }
        String family2 = FONT_MAP.get(name.toLowerCase());
        if (log.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder("WCFontImpl.get(");
            sb.append(name).append(", ").append(size);
            if (bold) {
                sb.append(", bold");
            }
            if (italic) {
                sb.append(", italic");
            }
            log.fine(sb.append(") = ").append(family2).toString());
        }
        if (family2 != null) {
            return new WCFontImpl(factory.createFont(family2, bold, italic, size));
        }
        return null;
    }

    WCFontImpl(PGFont font) {
        this.font = font;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public WCFont deriveFont(float size) {
        FontFactory factory = GraphicsPipeline.getPipeline().getFontFactory();
        return new WCFontImpl(factory.deriveFont(this.font, this.font.getFontResource().isBold(), this.font.getFontResource().isItalic(), size));
    }

    private FontStrike getFontStrike() {
        if (this.strike == null) {
            this.strike = this.font.getStrike(BaseTransform.IDENTITY_TRANSFORM, 1);
        }
        return this.strike;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public double getGlyphWidth(int glyph) {
        return getFontStrike().getFontResource().getAdvance(glyph, this.font.getSize());
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float[] getGlyphBoundingBox(int glyph) {
        float[] bb2 = getFontStrike().getFontResource().getGlyphBoundingBox(glyph, this.font.getSize(), new float[4]);
        return new float[]{bb2[0], -bb2[3], bb2[2], bb2[3] - bb2[1]};
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getXHeight() {
        return getFontStrike().getMetrics().getXHeight();
    }

    private static boolean needsTextLayout(int[] glyphs) {
        for (int g2 : glyphs) {
            if (g2 == 0) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public int[] getGlyphCodes(char[] chars) {
        int[] glyphs = new int[chars.length];
        CharToGlyphMapper mapper = getFontStrike().getFontResource().getGlyphMapper();
        mapper.charsToGlyphs(chars.length, chars, glyphs);
        if (needsTextLayout(glyphs)) {
            TextUtilities.createLayout(new String(chars), getPlatformFont()).getRuns();
            mapper.charsToGlyphs(chars.length, chars, glyphs);
        }
        return glyphs;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getAscent() {
        float res = -getFontStrike().getMetrics().getAscent();
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getAscent({0}, {1}) = {2}", new Object[]{this.font.getName(), Float.valueOf(this.font.getSize()), Float.valueOf(res)});
        }
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getDescent() {
        float res = getFontStrike().getMetrics().getDescent();
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getDescent({0}, {1}) = {2}", new Object[]{this.font.getName(), Float.valueOf(this.font.getSize()), Float.valueOf(res)});
        }
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getLineSpacing() {
        float res = getFontStrike().getMetrics().getLineHeight();
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getLineSpacing({0}, {1}) = {2}", new Object[]{this.font.getName(), Float.valueOf(this.font.getSize()), Float.valueOf(res)});
        }
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getLineGap() {
        float res = getFontStrike().getMetrics().getLineGap();
        if (log.isLoggable(Level.FINER)) {
            log.log(Level.FINER, "getLineGap({0}, {1}) = {2}", new Object[]{this.font.getName(), Float.valueOf(this.font.getSize()), Float.valueOf(res)});
        }
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public boolean hasUniformLineMetrics() {
        return false;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public Object getPlatformFont() {
        return this.font;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getCapHeight() {
        return getFontStrike().getMetrics().getCapHeight();
    }

    @Override // com.sun.webkit.graphics.WCFont
    public WCTextRun[] getTextRuns(String str) {
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format("str='%s' length=%d", str, Integer.valueOf(str.length())));
        }
        TextLayout layout = TextUtilities.createLayout(str, getPlatformFont());
        return (WCTextRun[]) Arrays.stream(layout.getRuns()).map(WCTextRunImpl::new).toArray(x$0 -> {
            return new WCTextRunImpl[x$0];
        });
    }
}
