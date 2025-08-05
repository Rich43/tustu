package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteFactory.class */
class IDWriteFactory extends IUnknown {
    IDWriteFactory(long ptr) {
        super(ptr);
    }

    IDWriteFontCollection GetSystemFontCollection(boolean checkForUpdates) {
        long result = OS.GetSystemFontCollection(this.ptr, checkForUpdates);
        if (result != 0) {
            return new IDWriteFontCollection(result);
        }
        return null;
    }

    IDWriteTextAnalyzer CreateTextAnalyzer() {
        long result = OS.CreateTextAnalyzer(this.ptr);
        if (result != 0) {
            return new IDWriteTextAnalyzer(result);
        }
        return null;
    }

    IDWriteTextFormat CreateTextFormat(String fontFamily, IDWriteFontCollection fontCollection, int fontWeight, int fontStyle, int fontStretch, float fontSize, String localeName) {
        long result = OS.CreateTextFormat(this.ptr, (fontFamily + (char) 0).toCharArray(), fontCollection.ptr, fontWeight, fontStyle, fontStretch, fontSize, (localeName + (char) 0).toCharArray());
        if (result != 0) {
            return new IDWriteTextFormat(result);
        }
        return null;
    }

    IDWriteTextLayout CreateTextLayout(char[] text, int stringStart, int stringLength, IDWriteTextFormat textFormat, float maxWidth, float maxHeight) {
        long result = OS.CreateTextLayout(this.ptr, text, stringStart, stringLength, textFormat.ptr, maxWidth, maxHeight);
        if (result != 0) {
            return new IDWriteTextLayout(result);
        }
        return null;
    }

    IDWriteGlyphRunAnalysis CreateGlyphRunAnalysis(DWRITE_GLYPH_RUN glyphRun, float pixelsPerDip, DWRITE_MATRIX transform, int renderingMode, int measuringMode, float baselineOriginX, float baselineOriginY) {
        long result = OS.CreateGlyphRunAnalysis(this.ptr, glyphRun, pixelsPerDip, transform, renderingMode, measuringMode, baselineOriginX, baselineOriginY);
        if (result != 0) {
            return new IDWriteGlyphRunAnalysis(result);
        }
        return null;
    }

    IDWriteFontFile CreateFontFileReference(String filePath) {
        long result = OS.CreateFontFileReference(this.ptr, (filePath + (char) 0).toCharArray());
        if (result != 0) {
            return new IDWriteFontFile(result);
        }
        return null;
    }

    IDWriteFontFace CreateFontFace(int fontFaceType, IDWriteFontFile fontFiles, int faceIndex, int fontFaceSimulationFlags) {
        long result = OS.CreateFontFace(this.ptr, fontFaceType, fontFiles.ptr, faceIndex, fontFaceSimulationFlags);
        if (result != 0) {
            return new IDWriteFontFace(result);
        }
        return null;
    }
}
