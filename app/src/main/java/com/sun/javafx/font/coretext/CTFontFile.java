package com.sun.javafx.font.coretext;

import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/font/coretext/CTFontFile.class */
class CTFontFile extends PrismFontFile {
    private final long cgFontRef;
    private static final CGAffineTransform tx = new CGAffineTransform();

    static {
        tx.f11880a = 1.0d;
        tx.f11883d = -1.0d;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/font/coretext/CTFontFile$SelfDisposerRecord.class */
    private static class SelfDisposerRecord implements DisposerRecord {
        private long cgFontRef;

        SelfDisposerRecord(long cgFontRef) {
            this.cgFontRef = cgFontRef;
        }

        @Override // com.sun.javafx.font.DisposerRecord
        public synchronized void dispose() {
            if (this.cgFontRef != 0) {
                OS.CFRelease(this.cgFontRef);
                this.cgFontRef = 0L;
            }
        }
    }

    CTFontFile(String name, String filename, int fIndex, boolean register, boolean embedded, boolean copy, boolean tracked) throws Exception {
        super(name, filename, fIndex, register, embedded, copy, tracked);
        if (embedded) {
            this.cgFontRef = createCGFontForEmbeddedFont();
            Disposer.addRecord(this, new SelfDisposerRecord(this.cgFontRef));
        } else {
            this.cgFontRef = 0L;
        }
    }

    public static boolean registerFont(String fontfile) {
        if (fontfile == null) {
            return false;
        }
        long alloc = OS.kCFAllocatorDefault();
        boolean result = false;
        long fileRef = OS.CFStringCreate(fontfile);
        if (fileRef != 0) {
            long urlRef = OS.CFURLCreateWithFileSystemPath(alloc, fileRef, 0, false);
            if (urlRef != 0) {
                result = OS.CTFontManagerRegisterFontsForURL(urlRef, 1, 0L);
                OS.CFRelease(urlRef);
            }
            OS.CFRelease(fileRef);
        }
        return result;
    }

    private long createCGFontForEmbeddedFont() {
        long cgFontRef = 0;
        long fileNameRef = OS.CFStringCreate(getFileName());
        if (fileNameRef != 0) {
            long url = OS.CFURLCreateWithFileSystemPath(OS.kCFAllocatorDefault(), fileNameRef, 0L, false);
            if (url != 0) {
                long dataProvider = OS.CGDataProviderCreateWithURL(url);
                if (dataProvider != 0) {
                    cgFontRef = OS.CGFontCreateWithDataProvider(dataProvider);
                    OS.CFRelease(dataProvider);
                }
                OS.CFRelease(url);
            }
            OS.CFRelease(fileNameRef);
        }
        return cgFontRef;
    }

    long getCGFontRef() {
        return this.cgFontRef;
    }

    CGRect getBBox(int gc, float size) {
        CTFontStrike strike = (CTFontStrike) getStrike(size, BaseTransform.IDENTITY_TRANSFORM);
        long fontRef = strike.getFontRef();
        if (fontRef == 0) {
            return null;
        }
        long pathRef = OS.CTFontCreatePathForGlyph(fontRef, (short) gc, tx);
        if (pathRef == 0) {
            return null;
        }
        CGRect rect = OS.CGPathGetPathBoundingBox(pathRef);
        OS.CGPathRelease(pathRef);
        return rect;
    }

    Path2D getGlyphOutline(int gc, float size) {
        CTFontStrike strike = (CTFontStrike) getStrike(size, BaseTransform.IDENTITY_TRANSFORM);
        long fontRef = strike.getFontRef();
        if (fontRef == 0) {
            return null;
        }
        long pathRef = OS.CTFontCreatePathForGlyph(fontRef, (short) gc, tx);
        if (pathRef == 0) {
            return null;
        }
        Path2D path = OS.CGPathApply(pathRef);
        OS.CGPathRelease(pathRef);
        return path;
    }

    @Override // com.sun.javafx.font.PrismFontFile
    protected int[] createGlyphBoundingBox(int gc) {
        CTFontStrike strike = (CTFontStrike) getStrike(12.0f, BaseTransform.IDENTITY_TRANSFORM);
        long fontRef = strike.getFontRef();
        if (fontRef == 0) {
            return null;
        }
        int[] bb2 = new int[4];
        if (!isCFF()) {
            short format = getIndexToLocFormat();
            if (OS.CTFontGetBoundingRectForGlyphUsingTables(fontRef, (short) gc, format, bb2)) {
                return bb2;
            }
        }
        long pathRef = OS.CTFontCreatePathForGlyph(fontRef, (short) gc, null);
        if (pathRef == 0) {
            return null;
        }
        CGRect rect = OS.CGPathGetPathBoundingBox(pathRef);
        OS.CGPathRelease(pathRef);
        float scale = getUnitsPerEm() / 12.0f;
        bb2[0] = (int) Math.round(rect.origin.f11884x * scale);
        bb2[1] = (int) Math.round(rect.origin.f11885y * scale);
        bb2[2] = (int) Math.round((rect.origin.f11884x + rect.size.width) * scale);
        bb2[3] = (int) Math.round((rect.origin.f11885y + rect.size.height) * scale);
        return bb2;
    }

    @Override // com.sun.javafx.font.PrismFontFile
    protected PrismFontStrike<CTFontFile> createStrike(float size, BaseTransform transform, int aaMode, FontStrikeDesc desc) {
        return new CTFontStrike(this, size, transform, aaMode, desc);
    }
}
