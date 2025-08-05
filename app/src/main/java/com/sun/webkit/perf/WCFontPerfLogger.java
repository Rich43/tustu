package com.sun.webkit.perf;

import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCTextRun;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/perf/WCFontPerfLogger.class */
public final class WCFontPerfLogger extends WCFont {
    private static final Logger log = Logger.getLogger(WCFontPerfLogger.class.getName());
    private static final PerfLogger logger = PerfLogger.getLogger(log);
    private final WCFont fnt;

    public WCFontPerfLogger(WCFont fnt) {
        this.fnt = fnt;
    }

    public static synchronized boolean isEnabled() {
        return logger.isEnabled();
    }

    public static void log() {
        logger.log();
    }

    public static void reset() {
        logger.reset();
    }

    @Override // com.sun.webkit.graphics.WCFont
    public Object getPlatformFont() {
        return this.fnt.getPlatformFont();
    }

    @Override // com.sun.webkit.graphics.WCFont
    public WCFont deriveFont(float size) {
        logger.resumeCount("DERIVEFONT");
        WCFont res = this.fnt.deriveFont(size);
        logger.suspendCount("DERIVEFONT");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public WCTextRun[] getTextRuns(String str) {
        logger.resumeCount("GETTEXTRUNS");
        WCTextRun[] runs = this.fnt.getTextRuns(str);
        logger.suspendCount("GETTEXTRUNS");
        return runs;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public int[] getGlyphCodes(char[] chars) {
        logger.resumeCount("GETGLYPHCODES");
        int[] res = this.fnt.getGlyphCodes(chars);
        logger.suspendCount("GETGLYPHCODES");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getXHeight() {
        logger.resumeCount("GETXHEIGHT");
        float res = this.fnt.getXHeight();
        logger.suspendCount("GETXHEIGHT");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public double getGlyphWidth(int glyph) {
        logger.resumeCount("GETGLYPHWIDTH");
        double res = this.fnt.getGlyphWidth(glyph);
        logger.suspendCount("GETGLYPHWIDTH");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float[] getGlyphBoundingBox(int glyph) {
        logger.resumeCount("GETGLYPHBOUNDINGBOX");
        float[] res = this.fnt.getGlyphBoundingBox(glyph);
        logger.suspendCount("GETGLYPHBOUNDINGBOX");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public int hashCode() {
        logger.resumeCount("HASH");
        int res = this.fnt.hashCode();
        logger.suspendCount("HASH");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public boolean equals(Object object) {
        logger.resumeCount("COMPARE");
        boolean res = this.fnt.equals(object);
        logger.suspendCount("COMPARE");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getAscent() {
        logger.resumeCount("GETASCENT");
        float res = this.fnt.getAscent();
        logger.suspendCount("GETASCENT");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getDescent() {
        logger.resumeCount("GETDESCENT");
        float res = this.fnt.getDescent();
        logger.suspendCount("GETDESCENT");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getLineSpacing() {
        logger.resumeCount("GETLINESPACING");
        float res = this.fnt.getLineSpacing();
        logger.suspendCount("GETLINESPACING");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getLineGap() {
        logger.resumeCount("GETLINEGAP");
        float res = this.fnt.getLineGap();
        logger.suspendCount("GETLINEGAP");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public boolean hasUniformLineMetrics() {
        logger.resumeCount("HASUNIFORMLINEMETRICS");
        boolean res = this.fnt.hasUniformLineMetrics();
        logger.suspendCount("HASUNIFORMLINEMETRICS");
        return res;
    }

    @Override // com.sun.webkit.graphics.WCFont
    public float getCapHeight() {
        logger.resumeCount("GETCAPHEIGHT");
        float res = this.fnt.getCapHeight();
        logger.suspendCount("GETCAPHEIGHT");
        return res;
    }
}
