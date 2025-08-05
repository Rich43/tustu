package com.sun.javafx.font.freetype;

import com.sun.glass.utils.NativeLibLoader;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/javafx/font/freetype/OSPango.class */
class OSPango {
    static final int PANGO_SCALE = 1024;
    static final int PANGO_STRETCH_ULTRA_CONDENSED = 0;
    static final int PANGO_STRETCH_EXTRA_CONDENSED = 1;
    static final int PANGO_STRETCH_CONDENSED = 2;
    static final int PANGO_STRETCH_SEMI_CONDENSED = 3;
    static final int PANGO_STRETCH_NORMAL = 4;
    static final int PANGO_STRETCH_SEMI_EXPANDED = 5;
    static final int PANGO_STRETCH_EXPANDED = 6;
    static final int PANGO_STRETCH_EXTRA_EXPANDED = 7;
    static final int PANGO_STRETCH_ULTRA_EXPANDED = 8;
    static final int PANGO_STYLE_ITALIC = 2;
    static final int PANGO_STYLE_NORMAL = 0;
    static final int PANGO_STYLE_OBLIQUE = 1;
    static final int PANGO_WEIGHT_BOLD = 700;
    static final int PANGO_WEIGHT_NORMAL = 400;
    static final int PANGO_DIRECTION_RTL = 1;

    static final native void pango_context_set_base_dir(long j2, int i2);

    static final native long pango_ft2_font_map_new();

    static final native long pango_font_map_create_context(long j2);

    static final native long pango_font_describe(long j2);

    static final native long pango_font_description_new();

    static final native void pango_font_description_free(long j2);

    static final native String pango_font_description_get_family(long j2);

    static final native int pango_font_description_get_stretch(long j2);

    static final native int pango_font_description_get_style(long j2);

    static final native int pango_font_description_get_weight(long j2);

    static final native void pango_font_description_set_family(long j2, String str);

    static final native void pango_font_description_set_absolute_size(long j2, double d2);

    static final native void pango_font_description_set_stretch(long j2, int i2);

    static final native void pango_font_description_set_style(long j2, int i2);

    static final native void pango_font_description_set_weight(long j2, int i2);

    static final native long pango_attr_list_new();

    static final native long pango_attr_font_desc_new(long j2);

    static final native long pango_attr_fallback_new(boolean z2);

    static final native void pango_attr_list_unref(long j2);

    static final native void pango_attr_list_insert(long j2, long j3);

    static final native long pango_itemize(long j2, long j3, int i2, int i3, long j4, long j5);

    static final native PangoGlyphString pango_shape(long j2, long j3);

    static final native void pango_item_free(long j2);

    static final native long g_utf8_offset_to_pointer(long j2, long j3);

    static final native long g_utf8_pointer_to_offset(long j2, long j3);

    static final native long g_utf8_strlen(long j2, long j3);

    static final native long g_utf16_to_utf8(char[] cArr);

    static final native void g_free(long j2);

    static final native int g_list_length(long j2);

    static final native long g_list_nth_data(long j2, int i2);

    static final native void g_list_free(long j2);

    static final native void g_object_unref(long j2);

    static final native boolean FcConfigAppFontAddFile(long j2, String str);

    OSPango() {
    }

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font_pango");
            return null;
        });
    }
}
