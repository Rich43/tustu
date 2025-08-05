package com.sun.javafx.font.directwrite;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.geom.Path2D;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/OS.class */
class OS {
    static final int S_OK = 0;
    static final int E_NOT_SUFFICIENT_BUFFER = -2147024774;
    static final int COINIT_APARTMENTTHREADED = 2;
    static final int COINIT_DISABLE_OLE1DDE = 4;
    static final int D2D1_FACTORY_TYPE_SINGLE_THREADED = 0;
    static final int D2D1_RENDER_TARGET_TYPE_DEFAULT = 0;
    static final int D2D1_RENDER_TARGET_TYPE_SOFTWARE = 1;
    static final int D2D1_RENDER_TARGET_TYPE_HARDWARE = 2;
    static final int D2D1_RENDER_TARGET_USAGE_NONE = 0;
    static final int D2D1_RENDER_TARGET_USAGE_FORCE_BITMAP_REMOTING = 1;
    static final int D2D1_RENDER_TARGET_USAGE_GDI_COMPATIBLE = 2;
    static final int D2D1_FEATURE_LEVEL_DEFAULT = 0;
    static final int D2D1_ALPHA_MODE_UNKNOWN = 0;
    static final int D2D1_ALPHA_MODE_PREMULTIPLIED = 1;
    static final int D2D1_ALPHA_MODE_STRAIGHT = 2;
    static final int D2D1_ALPHA_MODE_IGNORE = 3;
    static final int DXGI_FORMAT_UNKNOWN = 0;
    static final int DXGI_FORMAT_A8_UNORM = 65;
    static final int DXGI_FORMAT_B8G8R8A8_UNORM = 87;
    static final int D2D1_TEXT_ANTIALIAS_MODE_DEFAULT = 0;
    static final int D2D1_TEXT_ANTIALIAS_MODE_CLEARTYPE = 1;
    static final int D2D1_TEXT_ANTIALIAS_MODE_GRAYSCALE = 2;
    static final int D2D1_TEXT_ANTIALIAS_MODE_ALIASED = 3;
    static final int GUID_WICPixelFormat8bppGray = 1;
    static final int GUID_WICPixelFormat8bppAlpha = 2;
    static final int GUID_WICPixelFormat16bppGray = 3;
    static final int GUID_WICPixelFormat24bppRGB = 4;
    static final int GUID_WICPixelFormat24bppBGR = 5;
    static final int GUID_WICPixelFormat32bppBGR = 6;
    static final int GUID_WICPixelFormat32bppBGRA = 7;
    static final int GUID_WICPixelFormat32bppPBGRA = 8;
    static final int GUID_WICPixelFormat32bppGrayFloat = 9;
    static final int GUID_WICPixelFormat32bppRGBA = 10;
    static final int GUID_WICPixelFormat32bppPRGBA = 11;
    static final int WICBitmapNoCache = 0;
    static final int WICBitmapCacheOnDemand = 1;
    static final int WICBitmapCacheOnLoad = 2;
    static final int WICBitmapLockRead = 1;
    static final int WICBitmapLockWrite = 2;
    static final int DWRITE_FONT_WEIGHT_THIN = 100;
    static final int DWRITE_FONT_WEIGHT_EXTRA_LIGHT = 200;
    static final int DWRITE_FONT_WEIGHT_ULTRA_LIGHT = 200;
    static final int DWRITE_FONT_WEIGHT_LIGHT = 300;
    static final int DWRITE_FONT_WEIGHT_SEMI_LIGHT = 350;
    static final int DWRITE_FONT_WEIGHT_NORMAL = 400;
    static final int DWRITE_FONT_WEIGHT_REGULAR = 400;
    static final int DWRITE_FONT_WEIGHT_MEDIUM = 500;
    static final int DWRITE_FONT_WEIGHT_DEMI_BOLD = 600;
    static final int DWRITE_FONT_WEIGHT_SEMI_BOLD = 600;
    static final int DWRITE_FONT_WEIGHT_BOLD = 700;
    static final int DWRITE_FONT_WEIGHT_EXTRA_BOLD = 800;
    static final int DWRITE_FONT_WEIGHT_ULTRA_BOLD = 800;
    static final int DWRITE_FONT_WEIGHT_BLACK = 900;
    static final int DWRITE_FONT_WEIGHT_HEAVY = 900;
    static final int DWRITE_FONT_WEIGHT_EXTRA_BLACK = 950;
    static final int DWRITE_FONT_WEIGHT_ULTRA_BLACK = 950;
    static final int DWRITE_FONT_STRETCH_UNDEFINED = 0;
    static final int DWRITE_FONT_STRETCH_ULTRA_CONDENSED = 1;
    static final int DWRITE_FONT_STRETCH_EXTRA_CONDENSED = 2;
    static final int DWRITE_FONT_STRETCH_CONDENSED = 3;
    static final int DWRITE_FONT_STRETCH_SEMI_CONDENSED = 4;
    static final int DWRITE_FONT_STRETCH_NORMAL = 5;
    static final int DWRITE_FONT_STRETCH_MEDIUM = 5;
    static final int DWRITE_FONT_STRETCH_SEMI_EXPANDED = 6;
    static final int DWRITE_FONT_STRETCH_EXPANDED = 7;
    static final int DWRITE_FONT_STRETCH_EXTRA_EXPANDED = 8;
    static final int DWRITE_FONT_STRETCH_ULTRA_EXPANDED = 9;
    static final int DWRITE_FONT_STYLE_NORMAL = 0;
    static final int DWRITE_FONT_STYLE_OBLIQUE = 1;
    static final int DWRITE_FONT_STYLE_ITALIC = 2;
    static final int DWRITE_TEXTURE_ALIASED_1x1 = 0;
    static final int DWRITE_TEXTURE_CLEARTYPE_3x1 = 1;
    static final int DWRITE_RENDERING_MODE_DEFAULT = 0;
    static final int DWRITE_RENDERING_MODE_ALIASED = 1;
    static final int DWRITE_RENDERING_MODE_GDI_CLASSIC = 2;
    static final int DWRITE_RENDERING_MODE_GDI_NATURAL = 3;
    static final int DWRITE_RENDERING_MODE_NATURAL = 4;
    static final int DWRITE_RENDERING_MODE_NATURAL_SYMMETRIC = 5;
    static final int DWRITE_RENDERING_MODE_OUTLINE = 6;
    static final int DWRITE_RENDERING_MODE_CLEARTYPE_GDI_CLASSIC = 2;
    static final int DWRITE_RENDERING_MODE_CLEARTYPE_GDI_NATURAL = 3;
    static final int DWRITE_RENDERING_MODE_CLEARTYPE_NATURAL = 4;
    static final int DWRITE_RENDERING_MODE_CLEARTYPE_NATURAL_SYMMETRIC = 5;
    static final int DWRITE_MEASURING_MODE_NATURAL = 0;
    static final int DWRITE_MEASURING_MODE_GDI_CLASSIC = 1;
    static final int DWRITE_MEASURING_MODE_GDI_NATURAL = 2;
    static final int DWRITE_FACTORY_TYPE_SHARED = 0;
    static final int DWRITE_READING_DIRECTION_LEFT_TO_RIGHT = 0;
    static final int DWRITE_READING_DIRECTION_RIGHT_TO_LEFT = 1;
    static final int DWRITE_FONT_SIMULATIONS_NONE = 0;
    static final int DWRITE_FONT_SIMULATIONS_BOLD = 1;
    static final int DWRITE_FONT_SIMULATIONS_OBLIQUE = 2;
    static final int DWRITE_INFORMATIONAL_STRING_NONE = 0;
    static final int DWRITE_INFORMATIONAL_STRING_COPYRIGHT_NOTICE = 1;
    static final int DWRITE_INFORMATIONAL_STRING_VERSION_STRINGS = 2;
    static final int DWRITE_INFORMATIONAL_STRING_TRADEMARK = 3;
    static final int DWRITE_INFORMATIONAL_STRING_MANUFACTURER = 4;
    static final int DWRITE_INFORMATIONAL_STRING_DESIGNER = 5;
    static final int DWRITE_INFORMATIONAL_STRING_DESIGNER_URL = 6;
    static final int DWRITE_INFORMATIONAL_STRING_DESCRIPTION = 7;
    static final int DWRITE_INFORMATIONAL_STRING_FONT_VENDOR_URL = 8;
    static final int DWRITE_INFORMATIONAL_STRING_LICENSE_DESCRIPTION = 9;
    static final int DWRITE_INFORMATIONAL_STRING_LICENSE_INFO_URL = 10;
    static final int DWRITE_INFORMATIONAL_STRING_WIN32_FAMILY_NAMES = 11;
    static final int DWRITE_INFORMATIONAL_STRING_WIN32_SUBFAMILY_NAMES = 12;
    static final int DWRITE_INFORMATIONAL_STRING_PREFERRED_FAMILY_NAMES = 13;
    static final int DWRITE_INFORMATIONAL_STRING_PREFERRED_SUBFAMILY_NAMES = 14;
    static final int DWRITE_INFORMATIONAL_STRING_SAMPLE_TEXT = 15;
    static final int DWRITE_INFORMATIONAL_STRING_FULL_NAME = 16;
    static final int DWRITE_INFORMATIONAL_STRING_POSTSCRIPT_NAME = 17;
    static final int DWRITE_INFORMATIONAL_STRING_POSTSCRIPT_CID_NAME = 18;

    private static final native long _DWriteCreateFactory(int i2);

    private static final native long _D2D1CreateFactory(int i2);

    private static final native long _WICCreateImagingFactory();

    static final native boolean CoInitializeEx(int i2);

    static final native void CoUninitialize();

    private static final native long _NewJFXTextAnalysisSink(char[] cArr, int i2, int i3, char[] cArr2, int i4, long j2);

    private static final native long _NewJFXTextRenderer();

    static final native boolean Next(long j2);

    static final native int GetStart(long j2);

    static final native int GetLength(long j2);

    static final native DWRITE_SCRIPT_ANALYSIS GetAnalysis(long j2);

    static final native boolean JFXTextRendererNext(long j2);

    static final native int JFXTextRendererGetStart(long j2);

    static final native int JFXTextRendererGetLength(long j2);

    static final native int JFXTextRendererGetGlyphCount(long j2);

    static final native int JFXTextRendererGetTotalGlyphCount(long j2);

    static final native long JFXTextRendererGetFontFace(long j2);

    static final native int JFXTextRendererGetGlyphIndices(long j2, int[] iArr, int i2, int i3);

    static final native int JFXTextRendererGetGlyphAdvances(long j2, float[] fArr, int i2);

    static final native int JFXTextRendererGetGlyphOffsets(long j2, float[] fArr, int i2);

    static final native int JFXTextRendererGetClusterMap(long j2, short[] sArr, int i2, int i3);

    static final native DWRITE_GLYPH_METRICS GetDesignGlyphMetrics(long j2, short s2, boolean z2);

    static final native Path2D GetGlyphRunOutline(long j2, float f2, short s2, boolean z2);

    static final native long CreateFontFace(long j2);

    static final native long GetFaceNames(long j2);

    static final native long GetFontFamily(long j2);

    static final native int GetStretch(long j2);

    static final native int GetStyle(long j2);

    static final native int GetWeight(long j2);

    static final native long GetInformationalStrings(long j2, int i2);

    static final native int GetSimulations(long j2);

    static final native int GetFontCount(long j2);

    static final native long GetFont(long j2, int i2);

    static final native int Analyze(long j2, boolean[] zArr, int[] iArr, int[] iArr2, int[] iArr3);

    static final native char[] GetString(long j2, int i2, int i3);

    static final native int GetStringLength(long j2, int i2);

    static final native int FindLocaleName(long j2, char[] cArr);

    static final native long GetFamilyNames(long j2);

    static final native long GetFirstMatchingFont(long j2, int i2, int i3, int i4);

    static final native int GetFontFamilyCount(long j2);

    static final native long GetFontFamily(long j2, int i2);

    static final native int FindFamilyName(long j2, char[] cArr);

    static final native long GetFontFromFontFace(long j2, long j3);

    static final native byte[] CreateAlphaTexture(long j2, int i2, RECT rect);

    static final native RECT GetAlphaTextureBounds(long j2, int i2);

    static final native long GetSystemFontCollection(long j2, boolean z2);

    static final native long CreateGlyphRunAnalysis(long j2, DWRITE_GLYPH_RUN dwrite_glyph_run, float f2, DWRITE_MATRIX dwrite_matrix, int i2, int i3, float f3, float f4);

    static final native long CreateTextAnalyzer(long j2);

    static final native long CreateTextFormat(long j2, char[] cArr, long j3, int i2, int i3, int i4, float f2, char[] cArr2);

    static final native long CreateTextLayout(long j2, char[] cArr, int i2, int i3, long j3, float f2, float f3);

    static final native long CreateFontFileReference(long j2, char[] cArr);

    static final native long CreateFontFace(long j2, int i2, long j3, int i3, int i4);

    static final native int AddRef(long j2);

    static final native int Release(long j2);

    static final native int AnalyzeScript(long j2, long j3, int i2, int i3, long j4);

    static final native int GetGlyphs(long j2, char[] cArr, int i2, int i3, long j3, boolean z2, boolean z3, DWRITE_SCRIPT_ANALYSIS dwrite_script_analysis, char[] cArr2, long j4, long[] jArr, int[] iArr, int i4, int i5, short[] sArr, short[] sArr2, short[] sArr3, short[] sArr4, int[] iArr2);

    static final native int GetGlyphPlacements(long j2, char[] cArr, short[] sArr, short[] sArr2, int i2, int i3, short[] sArr3, short[] sArr4, int i4, long j3, float f2, boolean z2, boolean z3, DWRITE_SCRIPT_ANALYSIS dwrite_script_analysis, char[] cArr2, long[] jArr, int[] iArr, int i5, float[] fArr, float[] fArr2);

    static final native int Draw(long j2, long j3, long j4, float f2, float f3);

    static final native long CreateBitmap(long j2, int i2, int i3, int i4, int i5);

    static final native long Lock(long j2, int i2, int i3, int i4, int i5, int i6);

    static final native byte[] GetDataPointer(long j2);

    static final native int GetStride(long j2);

    static final native long CreateWicBitmapRenderTarget(long j2, long j3, D2D1_RENDER_TARGET_PROPERTIES d2d1_render_target_properties);

    static final native void BeginDraw(long j2);

    static final native int EndDraw(long j2);

    static final native void Clear(long j2, D2D1_COLOR_F d2d1_color_f);

    static final native void SetTextAntialiasMode(long j2, int i2);

    static final native void SetTransform(long j2, D2D1_MATRIX_3X2_F d2d1_matrix_3x2_f);

    static final native void DrawGlyphRun(long j2, D2D1_POINT_2F d2d1_point_2f, DWRITE_GLYPH_RUN dwrite_glyph_run, long j3, int i2);

    static final native long CreateSolidColorBrush(long j2, D2D1_COLOR_F d2d1_color_f);

    OS() {
    }

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            return null;
        });
    }

    static final IDWriteFactory DWriteCreateFactory(int factoryType) {
        long ptr = _DWriteCreateFactory(factoryType);
        if (ptr != 0) {
            return new IDWriteFactory(ptr);
        }
        return null;
    }

    static final ID2D1Factory D2D1CreateFactory(int factoryType) {
        long ptr = _D2D1CreateFactory(factoryType);
        if (ptr != 0) {
            return new ID2D1Factory(ptr);
        }
        return null;
    }

    static final IWICImagingFactory WICCreateImagingFactory() {
        long ptr = _WICCreateImagingFactory();
        if (ptr != 0) {
            return new IWICImagingFactory(ptr);
        }
        return null;
    }

    static final JFXTextAnalysisSink NewJFXTextAnalysisSink(char[] text, int start, int length, String locale, int direction) {
        long ptr = _NewJFXTextAnalysisSink(text, start, length, (locale + (char) 0).toCharArray(), direction, 0L);
        if (ptr != 0) {
            return new JFXTextAnalysisSink(ptr);
        }
        return null;
    }

    static final JFXTextRenderer NewJFXTextRenderer() {
        long ptr = _NewJFXTextRenderer();
        if (ptr != 0) {
            return new JFXTextRenderer(ptr);
        }
        return null;
    }
}
