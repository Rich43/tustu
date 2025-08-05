package com.sun.javafx.text;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.scene.text.TextSpan;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Bidi;

/* loaded from: jfxrt.jar:com/sun/javafx/text/GlyphLayout.class */
public abstract class GlyphLayout {
    public static final int CANONICAL_SUBSTITUTION = 1073741824;
    public static final int LAYOUT_LEFT_TO_RIGHT = 1;
    public static final int LAYOUT_RIGHT_TO_LEFT = 2;
    public static final int LAYOUT_NO_START_CONTEXT = 4;
    public static final int LAYOUT_NO_LIMIT_CONTEXT = 8;
    public static final int HINTING = 16;
    private static Method isIdeographicMethod;
    private static GlyphLayout reusableGL;
    private static boolean inUse;

    public abstract void layout(TextRun textRun, PGFont pGFont, FontStrike fontStrike, char[] cArr);

    static {
        isIdeographicMethod = null;
        try {
            isIdeographicMethod = Character.class.getMethod("isIdeographic", Integer.TYPE);
        } catch (NoSuchMethodException | SecurityException e2) {
            isIdeographicMethod = null;
        }
        reusableGL = newInstance();
    }

    protected TextRun addTextRun(PrismTextLayout layout, char[] chars, int start, int length, PGFont font, TextSpan span, byte level) {
        TextRun run = new TextRun(start, length, level, true, 0, span, 0, false);
        layout.addTextRun(run);
        return run;
    }

    private TextRun addTextRun(PrismTextLayout layout, char[] chars, int start, int length, PGFont font, TextSpan span, byte level, boolean complex) {
        if (complex || (level & 1) != 0) {
            return addTextRun(layout, chars, start, length, font, span, level);
        }
        TextRun run = new TextRun(start, length, level, false, 0, span, 0, false);
        layout.addTextRun(run);
        return run;
    }

    public int breakRuns(PrismTextLayout layout, char[] chars, int flags) {
        int length = chars.length;
        boolean complex = false;
        boolean feature = false;
        int scriptRun = 0;
        int script = 0;
        boolean checkComplex = true;
        boolean checkBidi = true;
        if ((flags & 2) != 0) {
            checkComplex = (flags & 16) != 0;
            checkBidi = (flags & 8) != 0;
        }
        TextRun run = null;
        Bidi bidi = null;
        byte bidiLevel = 0;
        int bidiEnd = length;
        int bidiIndex = 0;
        int spanIndex = 0;
        TextSpan span = null;
        int spanEnd = length;
        PGFont font = null;
        TextSpan[] spans = layout.getTextSpans();
        if (spans != null) {
            if (spans.length > 0) {
                span = spans[0];
                spanEnd = span.getText().length();
                font = (PGFont) span.getFont();
                if (font == null) {
                    flags |= 32;
                }
            }
        } else {
            font = layout.getFont();
        }
        if (font != null) {
            FontResource fr = font.getFontResource();
            int requestedFeatures = font.getFeatures();
            int supportedFeatures = fr.getFeatures();
            feature = (requestedFeatures & supportedFeatures) != 0;
        }
        if (checkBidi && length > 0) {
            int direction = layout.getDirection();
            bidi = new Bidi(chars, 0, null, 0, length, direction);
            bidiLevel = (byte) bidi.getLevelAt(bidi.getRunStart(0));
            bidiEnd = bidi.getRunLimit(0);
            if ((bidiLevel & 1) != 0) {
                flags |= 24;
            }
        }
        int start = 0;
        int i2 = 0;
        while (i2 < length) {
            char ch = chars[i2];
            int codePoint = ch;
            boolean delimiter = ch == '\t' || ch == '\n' || ch == '\r';
            if (delimiter && i2 != start) {
                run = addTextRun(layout, chars, start, i2 - start, font, span, bidiLevel, complex);
                if (complex) {
                    flags |= 16;
                    complex = false;
                }
                start = i2;
            }
            boolean spanChanged = i2 >= spanEnd && i2 < length;
            boolean levelChanged = i2 >= bidiEnd && i2 < length;
            boolean scriptChanged = false;
            if (!delimiter) {
                boolean oldComplex = complex;
                if (checkComplex) {
                    if (Character.isHighSurrogate(ch) && i2 + 1 < spanEnd && Character.isLowSurrogate(chars[i2 + 1])) {
                        i2++;
                        codePoint = Character.toCodePoint(ch, chars[i2]);
                    }
                    if (isIdeographic(codePoint)) {
                        flags |= 64;
                    }
                    script = ScriptMapper.getScript(codePoint);
                    if (scriptRun > 1 && script > 1 && script != scriptRun) {
                        scriptChanged = true;
                    }
                    if (!complex) {
                        complex = feature || ScriptMapper.isComplexCharCode(codePoint);
                    }
                }
                if ((spanChanged || levelChanged || scriptChanged) && start != i2) {
                    run = addTextRun(layout, chars, start, i2 - start, font, span, bidiLevel, oldComplex);
                    if (complex) {
                        flags |= 16;
                        complex = false;
                    }
                    start = i2;
                }
                i2++;
            }
            if (spanChanged) {
                spanIndex++;
                span = spans[spanIndex];
                spanEnd += span.getText().length();
                font = (PGFont) span.getFont();
                if (font == null) {
                    flags |= 32;
                } else {
                    FontResource fr2 = font.getFontResource();
                    int requestedFeatures2 = font.getFeatures();
                    int supportedFeatures2 = fr2.getFeatures();
                    feature = (requestedFeatures2 & supportedFeatures2) != 0;
                }
            }
            if (levelChanged) {
                bidiIndex++;
                bidiLevel = (byte) bidi.getLevelAt(bidi.getRunStart(bidiIndex));
                bidiEnd = bidi.getRunLimit(bidiIndex);
                if ((bidiLevel & 1) != 0) {
                    flags |= 24;
                }
            }
            if (scriptChanged) {
                scriptRun = script;
            }
            if (delimiter) {
                i2++;
                if (ch == '\r' && i2 < spanEnd && chars[i2] == '\n') {
                    i2++;
                }
                run = new TextRun(start, i2 - start, bidiLevel, false, 0, span, 0, false);
                if (ch == '\t') {
                    run.setTab();
                    flags |= 4;
                } else {
                    run.setLinebreak();
                }
                layout.addTextRun(run);
                start = i2;
            }
        }
        if (start < length) {
            addTextRun(layout, chars, start, length - start, font, span, bidiLevel, complex);
            if (complex) {
                flags |= 16;
            }
        } else if (run == null || run.isLinebreak()) {
            TextRun run2 = new TextRun(start, 0, (byte) 0, false, 0, span, 0, false);
            layout.addTextRun(run2);
        }
        if (bidi != null && !bidi.baseIsLeftToRight()) {
            flags |= 256;
        }
        return flags | 2;
    }

    protected int getInitialSlot(FontResource fr) {
        if (PrismFontFactory.isJreFont(fr)) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("Avoiding JRE Font: " + fr.getFullName());
                return 1;
            }
            return 1;
        }
        return 0;
    }

    private static GlyphLayout newInstance() {
        PrismFontFactory factory = PrismFontFactory.getFontFactory();
        return factory.createGlyphLayout();
    }

    public static GlyphLayout getInstance() {
        if (inUse) {
            return newInstance();
        }
        synchronized (GlyphLayout.class) {
            if (inUse) {
                return newInstance();
            }
            inUse = true;
            return reusableGL;
        }
    }

    public void dispose() {
        if (this == reusableGL) {
            inUse = false;
        }
    }

    private static boolean isIdeographic(int codePoint) {
        if (isIdeographicMethod != null) {
            try {
                return ((Boolean) isIdeographicMethod.invoke(null, Integer.valueOf(codePoint))).booleanValue();
            } catch (IllegalAccessException | InvocationTargetException e2) {
                return false;
            }
        }
        return false;
    }
}
