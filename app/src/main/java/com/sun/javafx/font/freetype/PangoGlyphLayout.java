package com.sun.javafx.font.freetype;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.TextRun;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import sun.font.Font2D;

/* loaded from: jfxrt.jar:com/sun/javafx/font/freetype/PangoGlyphLayout.class */
class PangoGlyphLayout extends GlyphLayout {
    private static final long fontmap = OSPango.pango_ft2_font_map_new();
    private Map<TextRun, Long> runUtf8 = new LinkedHashMap();

    PangoGlyphLayout() {
    }

    private int getSlot(PGFont font, PangoGlyphString glyphString) {
        CompositeFontResource fr = (CompositeFontResource) font.getFontResource();
        long fallbackFont = glyphString.font;
        long fallbackFd = OSPango.pango_font_describe(fallbackFont);
        String fallbackFamily = OSPango.pango_font_description_get_family(fallbackFd);
        int fallbackStyle = OSPango.pango_font_description_get_style(fallbackFd);
        int fallbackWeight = OSPango.pango_font_description_get_weight(fallbackFd);
        OSPango.pango_font_description_free(fallbackFd);
        boolean bold = fallbackWeight == 700;
        boolean italic = fallbackStyle != 0;
        PrismFontFactory prismFactory = PrismFontFactory.getFontFactory();
        PGFont fallbackPGFont = prismFactory.createFont(fallbackFamily, bold, italic, font.getSize());
        String fallbackFullname = fallbackPGFont.getFullName();
        String primaryFullname = fr.getSlotResource(0).getFullName();
        int slot = 0;
        if (!fallbackFullname.equalsIgnoreCase(primaryFullname)) {
            slot = fr.getSlotForFont(fallbackFullname);
            if (PrismFontFactory.debugFonts) {
                System.err.println("\tFallback font= " + fallbackFullname + " slot=" + (slot >> 24));
            }
        }
        return slot;
    }

    private boolean check(long checkValue, String message, long context, long desc, long attrList) {
        if (checkValue != 0) {
            return false;
        }
        if (message != null && PrismFontFactory.debugFonts) {
            System.err.println(message);
        }
        if (attrList != 0) {
            OSPango.pango_attr_list_unref(attrList);
        }
        if (desc != 0) {
            OSPango.pango_font_description_free(desc);
        }
        if (context != 0) {
            OSPango.g_object_unref(context);
            return true;
        }
        return true;
    }

    @Override // com.sun.javafx.text.GlyphLayout
    public void layout(TextRun run, PGFont font, FontStrike strike, char[] text) {
        int gg;
        FontResource fr = font.getFontResource();
        boolean composite = fr instanceof CompositeFontResource;
        if (composite) {
            fr = ((CompositeFontResource) fr).getSlotResource(0);
        }
        if (check(fontmap, "Failed allocating PangoFontMap.", 0L, 0L, 0L)) {
            return;
        }
        long context = OSPango.pango_font_map_create_context(fontmap);
        if (check(context, "Failed allocating PangoContext.", 0L, 0L, 0L)) {
            return;
        }
        boolean rtl = (run.getLevel() & 1) != 0;
        if (rtl) {
            OSPango.pango_context_set_base_dir(context, 1);
        }
        float size = font.getSize();
        int style = fr.isItalic() ? 2 : 0;
        int weight = fr.isBold() ? Font2D.FWEIGHT_BOLD : 400;
        long desc = OSPango.pango_font_description_new();
        if (check(desc, "Failed allocating FontDescription.", context, 0L, 0L)) {
            return;
        }
        OSPango.pango_font_description_set_family(desc, fr.getFamilyName());
        OSPango.pango_font_description_set_absolute_size(desc, size * 1024.0f);
        OSPango.pango_font_description_set_stretch(desc, 4);
        OSPango.pango_font_description_set_style(desc, style);
        OSPango.pango_font_description_set_weight(desc, weight);
        long attrList = OSPango.pango_attr_list_new();
        if (check(attrList, "Failed allocating PangoAttributeList.", context, desc, 0L)) {
            return;
        }
        long attr = OSPango.pango_attr_font_desc_new(desc);
        if (check(attr, "Failed allocating PangoAttribute.", context, desc, attrList)) {
            return;
        }
        OSPango.pango_attr_list_insert(attrList, attr);
        if (!composite) {
            OSPango.pango_attr_list_insert(attrList, OSPango.pango_attr_fallback_new(false));
        }
        Long str = this.runUtf8.get(run);
        if (str == null) {
            char[] rtext = Arrays.copyOfRange(text, run.getStart(), run.getEnd());
            str = Long.valueOf(OSPango.g_utf16_to_utf8(rtext));
            if (check(str.longValue(), "Failed allocating UTF-8 buffer.", context, desc, attrList)) {
                return;
            } else {
                this.runUtf8.put(run, str);
            }
        }
        long utflen = OSPango.g_utf8_strlen(str.longValue(), -1L);
        long end = OSPango.g_utf8_offset_to_pointer(str.longValue(), utflen);
        long runs = OSPango.pango_itemize(context, str.longValue(), 0, (int) (end - str.longValue()), attrList, 0L);
        if (runs != 0) {
            int runsCount = OSPango.g_list_length(runs);
            PangoGlyphString[] pangoGlyphs = new PangoGlyphString[runsCount];
            for (int i2 = 0; i2 < runsCount; i2++) {
                long pangoItem = OSPango.g_list_nth_data(runs, i2);
                if (pangoItem != 0) {
                    pangoGlyphs[i2] = OSPango.pango_shape(str.longValue(), pangoItem);
                    OSPango.pango_item_free(pangoItem);
                }
            }
            OSPango.g_list_free(runs);
            int glyphCount = 0;
            for (PangoGlyphString g2 : pangoGlyphs) {
                if (g2 != null) {
                    glyphCount += g2.num_glyphs;
                }
            }
            int[] glyphs = new int[glyphCount];
            float[] pos = new float[(glyphCount * 2) + 2];
            int[] indices = new int[glyphCount];
            int gi = 0;
            int ci = rtl ? run.getLength() : 0;
            int width = 0;
            for (PangoGlyphString g3 : pangoGlyphs) {
                if (g3 != null) {
                    int slot = composite ? getSlot(font, g3) : 0;
                    if (rtl) {
                        ci -= g3.num_chars;
                    }
                    for (int i3 = 0; i3 < g3.num_glyphs; i3++) {
                        int gii = gi + i3;
                        if (slot != -1 && 0 <= (gg = g3.glyphs[i3]) && gg <= 16777215) {
                            glyphs[gii] = (slot << 24) | gg;
                        }
                        if (size != 0.0f) {
                            width += g3.widths[i3];
                            pos[2 + (gii << 1)] = width / 1024.0f;
                        }
                        indices[gii] = g3.log_clusters[i3] + ci;
                    }
                    if (!rtl) {
                        ci += g3.num_chars;
                    }
                    gi += g3.num_glyphs;
                }
            }
            run.shape(glyphCount, glyphs, pos, indices);
        }
        check(0L, null, context, desc, attrList);
    }

    @Override // com.sun.javafx.text.GlyphLayout
    public void dispose() {
        super.dispose();
        for (Long str : this.runUtf8.values()) {
            OSPango.g_free(str.longValue());
        }
        this.runUtf8.clear();
    }
}
