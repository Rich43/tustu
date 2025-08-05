package com.sun.javafx.font.coretext;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.geom.Path2D;
import java.nio.ByteOrder;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/javafx/font/coretext/OS.class */
class OS {
    static final int kCFURLPOSIXPathStyle = 0;
    static final int kCTFontOrientationDefault = 0;
    static final int kCTFontManagerScopeProcess = 1;
    static final int kCGBitmapByteOrder32Big = 16384;
    static final int kCGBitmapByteOrder32Little = 8192;
    static final int kCGBitmapByteOrder32Host;
    static final int kCGImageAlphaPremultipliedFirst = 2;
    static final int kCGImageAlphaNone = 0;
    static final int kCTWritingDirectionRightToLeft = 1;

    static final native byte[] CGBitmapContextGetData(long j2, int i2, int i3, int i4);

    static final native void CGRectApplyAffineTransform(CGRect cGRect, CGAffineTransform cGAffineTransform);

    static final native Path2D CGPathApply(long j2);

    static final native CGRect CGPathGetPathBoundingBox(long j2);

    static final native long CFStringCreateWithCharacters(long j2, char[] cArr, long j3, long j4);

    static final native String CTFontCopyAttributeDisplayName(long j2);

    static final native void CTFontDrawGlyphs(long j2, short s2, double d2, double d3, long j3);

    static final native double CTFontGetAdvancesForGlyphs(long j2, int i2, short s2, CGSize cGSize);

    static final native boolean CTFontGetBoundingRectForGlyphUsingTables(long j2, short s2, short s3, int[] iArr);

    static final native int CTRunGetGlyphs(long j2, int i2, int i3, int[] iArr);

    static final native int CTRunGetStringIndices(long j2, int i2, int[] iArr);

    static final native int CTRunGetPositions(long j2, int i2, float[] fArr);

    static final native long kCFAllocatorDefault();

    static final native long kCFTypeDictionaryKeyCallBacks();

    static final native long kCFTypeDictionaryValueCallBacks();

    static final native long kCTFontAttributeName();

    static final native long kCTParagraphStyleAttributeName();

    static final native long CFArrayGetCount(long j2);

    static final native long CFArrayGetValueAtIndex(long j2, long j3);

    static final native long CFAttributedStringCreate(long j2, long j3, long j4);

    static final native void CFDictionaryAddValue(long j2, long j3, long j4);

    static final native long CFDictionaryCreateMutable(long j2, long j3, long j4, long j5);

    static final native long CFDictionaryGetValue(long j2, long j3);

    static final native void CFRelease(long j2);

    static final native long CFStringCreateWithCharacters(long j2, char[] cArr, long j3);

    static final native long CFURLCreateWithFileSystemPath(long j2, long j3, long j4, boolean z2);

    static final native long CGBitmapContextCreate(long j2, long j3, long j4, long j5, long j6, long j7, int i2);

    static final native void CGContextFillRect(long j2, CGRect cGRect);

    static final native void CGContextRelease(long j2);

    static final native void CGContextSetAllowsFontSmoothing(long j2, boolean z2);

    static final native void CGContextSetAllowsAntialiasing(long j2, boolean z2);

    static final native void CGContextSetAllowsFontSubpixelPositioning(long j2, boolean z2);

    static final native void CGContextSetAllowsFontSubpixelQuantization(long j2, boolean z2);

    static final native void CGContextSetRGBFillColor(long j2, double d2, double d3, double d4, double d5);

    static final native void CGContextTranslateCTM(long j2, double d2, double d3);

    static final native long CGColorSpaceCreateDeviceGray();

    static final native long CGColorSpaceCreateDeviceRGB();

    static final native void CGColorSpaceRelease(long j2);

    static final native long CGDataProviderCreateWithURL(long j2);

    static final native long CGFontCreateWithDataProvider(long j2);

    static final native void CGPathRelease(long j2);

    static final native long CTFontCreatePathForGlyph(long j2, short s2, CGAffineTransform cGAffineTransform);

    static final native long CTFontCreateWithGraphicsFont(long j2, double d2, CGAffineTransform cGAffineTransform, long j3);

    static final native long CTFontCreateWithName(long j2, double d2, CGAffineTransform cGAffineTransform);

    static final native boolean CTFontManagerRegisterFontsForURL(long j2, int i2, long j3);

    static final native long CTLineCreateWithAttributedString(long j2);

    static final native long CTLineGetGlyphRuns(long j2);

    static final native long CTLineGetGlyphCount(long j2);

    static final native double CTLineGetTypographicBounds(long j2);

    static final native long CTRunGetGlyphCount(long j2);

    static final native long CTRunGetAttributes(long j2);

    static final native long CTParagraphStyleCreate(int i2);

    OS() {
    }

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            return null;
        });
        kCGBitmapByteOrder32Host = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? 8192 : 16384;
    }

    static final long CFStringCreate(String string) {
        char[] buffer = string.toCharArray();
        long alloc = kCFAllocatorDefault();
        return CFStringCreateWithCharacters(alloc, buffer, buffer.length);
    }
}
