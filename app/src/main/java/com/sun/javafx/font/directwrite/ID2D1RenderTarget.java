package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/ID2D1RenderTarget.class */
class ID2D1RenderTarget extends IUnknown {
    ID2D1RenderTarget(long ptr) {
        super(ptr);
    }

    void BeginDraw() {
        OS.BeginDraw(this.ptr);
    }

    int EndDraw() {
        return OS.EndDraw(this.ptr);
    }

    void Clear(D2D1_COLOR_F clearColor) {
        OS.Clear(this.ptr, clearColor);
    }

    void SetTransform(D2D1_MATRIX_3X2_F transform) {
        OS.SetTransform(this.ptr, transform);
    }

    void SetTextAntialiasMode(int textAntialiasMode) {
        OS.SetTextAntialiasMode(this.ptr, textAntialiasMode);
    }

    void DrawGlyphRun(D2D1_POINT_2F baselineOrigin, DWRITE_GLYPH_RUN glyphRun, ID2D1Brush foregroundBrush, int measuringMode) {
        OS.DrawGlyphRun(this.ptr, baselineOrigin, glyphRun, foregroundBrush.ptr, measuringMode);
    }

    ID2D1Brush CreateSolidColorBrush(D2D1_COLOR_F color) {
        long result = OS.CreateSolidColorBrush(this.ptr, color);
        if (result != 0) {
            return new ID2D1Brush(result);
        }
        return null;
    }
}
