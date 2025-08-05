package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.text.TextRun;
import java.util.Arrays;
import org.icepdf.core.util.PdfOps;
import sun.font.Font2D;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/DWGlyphLayout.class */
public class DWGlyphLayout extends GlyphLayout {
    private static final String LOCALE = "en-us";

    @Override // com.sun.javafx.text.GlyphLayout
    protected TextRun addTextRun(PrismTextLayout layout, char[] chars, int start, int length, PGFont font, TextSpan span, byte level) {
        IDWriteFactory factory = DWFactory.getDWriteFactory();
        IDWriteTextAnalyzer analyzer = factory.CreateTextAnalyzer();
        if (analyzer == null) {
            return new TextRun(start, length, level, false, 0, span, 0, false);
        }
        int dir = (level & 1) != 0 ? 1 : 0;
        JFXTextAnalysisSink sink = OS.NewJFXTextAnalysisSink(chars, start, length, LOCALE, dir);
        if (sink == null) {
            return new TextRun(start, length, level, false, 0, span, 0, false);
        }
        sink.AddRef();
        TextRun textRun = null;
        int hr = analyzer.AnalyzeScript(sink, 0, length, sink);
        if (hr == 0) {
            while (sink.Next()) {
                int runStart = sink.GetStart();
                int runLength = sink.GetLength();
                DWRITE_SCRIPT_ANALYSIS analysis = sink.GetAnalysis();
                textRun = new TextRun(start + runStart, runLength, level, true, analysis.script, span, analysis.shapes, false);
                layout.addTextRun(textRun);
            }
        }
        analyzer.Release();
        sink.Release();
        return textRun;
    }

    @Override // com.sun.javafx.text.GlyphLayout
    public void layout(TextRun run, PGFont font, FontStrike strike, char[] text) {
        int slot = 0;
        FontResource fr = font.getFontResource();
        boolean composite = fr instanceof CompositeFontResource;
        if (composite) {
            slot = getInitialSlot(fr);
            fr = ((CompositeFontResource) fr).getSlotResource(slot);
        }
        IDWriteFontFace face = ((DWFontFile) fr).getFontFace();
        if (face == null) {
            return;
        }
        IDWriteFactory factory = DWFactory.getDWriteFactory();
        IDWriteTextAnalyzer analyzer = factory.CreateTextAnalyzer();
        if (analyzer == null) {
            return;
        }
        int length = run.getLength();
        short[] clusterMap = new short[length];
        short[] textProps = new short[length];
        int maxGlyphs = ((length * 3) / 2) + 16;
        short[] glyphs = new short[maxGlyphs];
        short[] glyphProps = new short[maxGlyphs];
        int[] retGlyphcount = new int[1];
        boolean rtl = !run.isLeftToRight();
        DWRITE_SCRIPT_ANALYSIS analysis = new DWRITE_SCRIPT_ANALYSIS();
        analysis.script = (short) run.getScript();
        analysis.shapes = run.getSlot();
        int start = run.getStart();
        int hr = analyzer.GetGlyphs(text, start, length, face, false, rtl, analysis, null, 0L, null, null, 0, maxGlyphs, clusterMap, textProps, glyphs, glyphProps, retGlyphcount);
        if (hr == -2147024774) {
            int maxGlyphs2 = maxGlyphs * 2;
            glyphs = new short[maxGlyphs2];
            glyphProps = new short[maxGlyphs2];
            hr = analyzer.GetGlyphs(text, start, length, face, false, rtl, analysis, null, 0L, null, null, 0, maxGlyphs2, clusterMap, textProps, glyphs, glyphProps, retGlyphcount);
        }
        if (hr != 0) {
            analyzer.Release();
            return;
        }
        int glyphCount = retGlyphcount[0];
        int step = rtl ? -1 : 1;
        int[] iglyphs = new int[glyphCount];
        int slotMask = slot << 24;
        boolean missingGlyph = false;
        int i2 = 0;
        int i3 = rtl ? glyphCount - 1 : 0;
        while (true) {
            int j2 = i3;
            if (i2 >= glyphCount) {
                break;
            }
            if (glyphs[i2] == 0) {
                missingGlyph = true;
                if (composite) {
                    break;
                }
            }
            iglyphs[i2] = glyphs[j2] | slotMask;
            i2++;
            i3 = j2 + step;
        }
        if (missingGlyph && composite) {
            analyzer.Release();
            renderShape(text, run, font, slot);
            return;
        }
        float size = font.getSize();
        float[] advances = new float[glyphCount];
        float[] offsets = new float[glyphCount * 2];
        analyzer.GetGlyphPlacements(text, clusterMap, textProps, start, length, glyphs, glyphProps, glyphCount, face, size, false, rtl, analysis, null, null, null, 0, advances, offsets);
        analyzer.Release();
        float[] pos = getPositions(advances, offsets, glyphCount, rtl);
        int[] indices = getIndices(clusterMap, glyphCount, rtl);
        run.shape(glyphCount, iglyphs, pos, indices);
    }

    private float[] getPositions(float[] advances, float[] offsets, int glyphCount, boolean rtl) {
        float[] pos = new float[(glyphCount * 2) + 2];
        int i2 = 0;
        int j2 = rtl ? glyphCount - 1 : 0;
        int step = rtl ? -1 : 1;
        float x2 = 0.0f;
        while (i2 < pos.length - 2) {
            int g2 = j2 << 1;
            int i3 = i2;
            int i4 = i2 + 1;
            pos[i3] = (rtl ? -offsets[g2] : offsets[g2]) + x2;
            i2 = i4 + 1;
            pos[i4] = -offsets[g2 + 1];
            x2 += advances[j2];
            j2 += step;
        }
        int i5 = i2;
        int i6 = i2 + 1;
        pos[i5] = x2;
        int i7 = i6 + 1;
        pos[i6] = 0.0f;
        return pos;
    }

    private int[] getIndices(short[] clusterMap, int glyphCount, boolean rtl) {
        int[] indices = new int[glyphCount];
        Arrays.fill(indices, -1);
        for (int i2 = 0; i2 < clusterMap.length; i2++) {
            short s2 = clusterMap[i2];
            if (0 <= s2 && s2 < glyphCount && indices[s2] == -1) {
                indices[s2] = i2;
            }
        }
        if (indices.length > 0) {
            if (indices[0] == -1) {
                indices[0] = 0;
            }
            for (int i3 = 1; i3 < indices.length; i3++) {
                if (indices[i3] == -1) {
                    indices[i3] = indices[i3 - 1];
                }
            }
        }
        if (rtl) {
            for (int i4 = 0; i4 < indices.length / 2; i4++) {
                int tmp = indices[i4];
                indices[i4] = indices[(indices.length - i4) - 1];
                indices[(indices.length - i4) - 1] = tmp;
            }
        }
        return indices;
    }

    private String getName(IDWriteLocalizedStrings localizedStrings) {
        if (localizedStrings == null) {
            return null;
        }
        int index = localizedStrings.FindLocaleName(LOCALE);
        String name = null;
        if (index >= 0) {
            int size = localizedStrings.GetStringLength(index);
            name = localizedStrings.GetString(index, size);
        }
        localizedStrings.Release();
        return name;
    }

    private FontResource checkFontResource(FontResource fr, String psName, String win32Name) {
        if (fr == null) {
            return null;
        }
        if (psName != null && psName.equals(fr.getPSName())) {
            return fr;
        }
        if (win32Name != null) {
            if (win32Name.equals(fr.getFullName())) {
                return fr;
            }
            String name = fr.getFamilyName() + " " + fr.getStyleName();
            if (win32Name.equals(name)) {
                return fr;
            }
            return null;
        }
        return null;
    }

    private int getFontSlot(IDWriteFontFace face, CompositeFontResource composite, String primaryFont, int slot) {
        if (face == null) {
            return -1;
        }
        IDWriteFontCollection collection = DWFactory.getFontCollection();
        PrismFontFactory prismFactory = PrismFontFactory.getFontFactory();
        IDWriteFont font = collection.GetFontFromFontFace(face);
        if (font == null) {
            return -1;
        }
        IDWriteFontFamily fallbackFamily = font.GetFontFamily();
        String family = getName(fallbackFamily.GetFamilyNames());
        fallbackFamily.Release();
        boolean italic = font.GetStyle() != 0;
        boolean bold = font.GetWeight() > 400;
        int simulation = font.GetSimulations();
        String psName = getName(font.GetInformationalStrings(17));
        String win32Family = getName(font.GetInformationalStrings(11));
        String win32SubFamily = getName(font.GetInformationalStrings(12));
        String win32Name = win32Family + " " + win32SubFamily;
        if (PrismFontFactory.debugFonts) {
            String styleName = getName(font.GetFaceNames());
            System.err.println("Mapping IDWriteFont=\"" + family + " " + styleName + "\" Postscript name=\"" + psName + "\" Win32 name=\"" + win32Name + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        font.Release();
        FontResource fr = checkFontResource(prismFactory.getFontResource(family, bold, italic, false), psName, win32Name);
        if (fr == null) {
            fr = checkFontResource(prismFactory.getFontResource(family, bold & ((simulation & 1) == 0), italic & ((simulation & 2) == 0), false), psName, win32Name);
        }
        if (fr == null) {
            fr = checkFontResource(prismFactory.getFontResource(win32Name, null, false), psName, win32Name);
        }
        if (fr == null) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("\t**** Failed to map IDWriteFont to Prism ****");
                return -1;
            }
            return -1;
        }
        String fallbackName = fr.getFullName();
        if (!primaryFont.equalsIgnoreCase(fallbackName)) {
            slot = composite.getSlotForFont(fallbackName);
        }
        if (PrismFontFactory.debugFonts) {
            System.err.println("\tFallback full name=\"" + fallbackName + "\" Postscript name=\"" + fr.getPSName() + "\" Style name=\"" + fr.getStyleName() + "\" slot=" + slot);
        }
        return slot;
    }

    private void renderShape(char[] text, TextRun run, PGFont font, int baseSlot) {
        CompositeFontResource composite = (CompositeFontResource) font.getFontResource();
        FontResource fr = composite.getSlotResource(baseSlot);
        String family = fr.getFamilyName();
        String fullName = fr.getFullName();
        int weight = fr.isBold() ? Font2D.FWEIGHT_BOLD : 400;
        int style = fr.isItalic() ? 2 : 0;
        float size = font.getSize();
        float fontsize = size > 0.0f ? size : 1.0f;
        IDWriteFactory factory = DWFactory.getDWriteFactory();
        IDWriteFontCollection collection = DWFactory.getFontCollection();
        IDWriteTextFormat format = factory.CreateTextFormat(family, collection, weight, style, 5, fontsize, LOCALE);
        if (format == null) {
            return;
        }
        int start = run.getStart();
        int length = run.getLength();
        IDWriteTextLayout layout = factory.CreateTextLayout(text, start, length, format, 100000.0f, 100000.0f);
        if (layout != null) {
            JFXTextRenderer renderer = OS.NewJFXTextRenderer();
            if (renderer != null) {
                renderer.AddRef();
                layout.Draw(0L, renderer, 0.0f, 0.0f);
                int glyphCount = renderer.GetTotalGlyphCount();
                int[] glyphs = new int[glyphCount];
                float[] advances = new float[glyphCount];
                float[] offsets = new float[glyphCount * 2];
                short[] clusterMap = new short[length];
                int glyphStart = 0;
                int iGetLength = 0;
                while (true) {
                    int textStart = iGetLength;
                    if (!renderer.Next()) {
                        break;
                    }
                    IDWriteFontFace fallback = renderer.GetFontFace();
                    int slot = getFontSlot(fallback, composite, fullName, baseSlot);
                    if (slot >= 0) {
                        renderer.GetGlyphIndices(glyphs, glyphStart, slot << 24);
                        renderer.GetGlyphOffsets(offsets, glyphStart * 2);
                    }
                    if (size > 0.0f) {
                        renderer.GetGlyphAdvances(advances, glyphStart);
                    }
                    renderer.GetClusterMap(clusterMap, textStart, glyphStart);
                    glyphStart += renderer.GetGlyphCount();
                    iGetLength = textStart + renderer.GetLength();
                }
                renderer.Release();
                boolean rtl = !run.isLeftToRight();
                if (rtl) {
                    for (int i2 = 0; i2 < glyphCount / 2; i2++) {
                        int tmp = glyphs[i2];
                        glyphs[i2] = glyphs[(glyphCount - i2) - 1];
                        glyphs[(glyphCount - i2) - 1] = tmp;
                    }
                }
                float[] pos = getPositions(advances, offsets, glyphCount, rtl);
                int[] indices = getIndices(clusterMap, glyphCount, rtl);
                run.shape(glyphCount, glyphs, pos, indices);
            }
            layout.Release();
        }
        format.Release();
    }
}
