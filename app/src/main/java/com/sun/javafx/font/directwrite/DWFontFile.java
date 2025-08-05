package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import sun.font.Font2D;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/DWFontFile.class */
class DWFontFile extends PrismFontFile {
    private IDWriteFontFace fontFace;
    private DWDisposer disposer;

    DWFontFile(String name, String filename, int fIndex, boolean register, boolean embedded, boolean copy, boolean tracked) throws Exception {
        super(name, filename, fIndex, register, embedded, copy, tracked);
        this.fontFace = createFontFace();
        if (PrismFontFactory.debugFonts && this.fontFace == null) {
            System.err.println("Failed to create IDWriteFontFace for " + ((Object) this));
        }
        if (copy) {
            this.disposer = new DWDisposer(this.fontFace);
            Disposer.addRecord(this, this.disposer);
        }
    }

    private IDWriteFontFace createEmbeddedFontFace() {
        IDWriteFactory factory = DWFactory.getDWriteFactory();
        IDWriteFontFile fontFile = factory.CreateFontFileReference(getFileName());
        if (fontFile == null) {
            return null;
        }
        boolean[] isSupportedFontType = new boolean[1];
        int[] fontFileType = new int[1];
        int[] fontFaceType = new int[1];
        int[] numberOfFaces = new int[1];
        int hr = fontFile.Analyze(isSupportedFontType, fontFileType, fontFaceType, numberOfFaces);
        IDWriteFontFace face = null;
        if (hr == 0 && isSupportedFontType[0]) {
            int faceIndex = getFontIndex();
            face = factory.CreateFontFace(fontFaceType[0], fontFile, faceIndex, 0);
        }
        fontFile.Release();
        return face;
    }

    private IDWriteFontFace createFontFace() {
        if (isEmbeddedFont()) {
            return createEmbeddedFontFace();
        }
        IDWriteFontCollection collection = DWFactory.getFontCollection();
        int index = collection.FindFamilyName(getFamilyName());
        if (index == -1) {
            return createEmbeddedFontFace();
        }
        IDWriteFontFamily family = collection.GetFontFamily(index);
        if (family == null) {
            return null;
        }
        int weight = isBold() ? Font2D.FWEIGHT_BOLD : 400;
        int style = isItalic() ? 2 : 0;
        IDWriteFont font = family.GetFirstMatchingFont(weight, 5, style);
        family.Release();
        if (font == null) {
            return null;
        }
        IDWriteFontFace face = font.CreateFontFace();
        font.Release();
        return face;
    }

    IDWriteFontFace getFontFace() {
        return this.fontFace;
    }

    Path2D getGlyphOutline(int gc, float size) {
        if (this.fontFace == null) {
            return null;
        }
        return size == 0.0f ? new Path2D() : this.fontFace.GetGlyphRunOutline(size, (short) gc, false);
    }

    RectBounds getBBox(int glyphCode, float size) {
        float[] bb2 = new float[4];
        getGlyphBoundingBox(glyphCode, size, bb2);
        return new RectBounds(bb2[0], bb2[1], bb2[2], bb2[3]);
    }

    @Override // com.sun.javafx.font.PrismFontFile
    protected int[] createGlyphBoundingBox(int gc) {
        DWRITE_GLYPH_METRICS metrics;
        if (this.fontFace == null || (metrics = this.fontFace.GetDesignGlyphMetrics((short) gc, false)) == null) {
            return null;
        }
        int[] bb2 = {metrics.leftSideBearing, (metrics.verticalOriginY - metrics.advanceHeight) + metrics.bottomSideBearing, metrics.advanceWidth - metrics.rightSideBearing, metrics.verticalOriginY - metrics.topSideBearing};
        return bb2;
    }

    @Override // com.sun.javafx.font.PrismFontFile
    protected PrismFontStrike<DWFontFile> createStrike(float size, BaseTransform transform, int aaMode, FontStrikeDesc desc) {
        return new DWFontStrike(this, size, transform, aaMode, desc);
    }

    @Override // com.sun.javafx.font.PrismFontFile
    protected synchronized void disposeOnShutdown() {
        if (this.fontFace != null) {
            if (this.disposer != null) {
                this.disposer.dispose();
            } else {
                this.fontFace.Release();
                if (PrismFontFactory.debugFonts) {
                    System.err.println("null disposer for " + ((Object) this.fontFace));
                }
            }
            if (PrismFontFactory.debugFonts) {
                System.err.println("fontFace freed: " + ((Object) this.fontFace));
            }
            this.fontFace = null;
        }
        super.disposeOnShutdown();
    }
}
