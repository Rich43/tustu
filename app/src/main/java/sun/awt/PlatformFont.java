package sun.awt;

import java.awt.peer.FontPeer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Locale;
import java.util.Vector;
import sun.font.SunFontManager;
import sun.java2d.FontSupport;

/* loaded from: rt.jar:sun/awt/PlatformFont.class */
public abstract class PlatformFont implements FontPeer {
    protected FontDescriptor[] componentFonts;
    protected char defaultChar;
    protected FontConfiguration fontConfig;
    protected FontDescriptor defaultFont;
    protected String familyName;
    private Object[] fontCache;
    protected static int FONTCACHESIZE;
    protected static int FONTCACHEMASK;
    protected static String osVersion;

    protected abstract char getMissingGlyphCharacter();

    private static native void initIDs();

    static {
        NativeLibLoader.loadLibraries();
        initIDs();
        FONTCACHESIZE = 256;
        FONTCACHEMASK = FONTCACHESIZE - 1;
    }

    public PlatformFont(String str, int i2) {
        SunFontManager sunFontManager = SunFontManager.getInstance();
        if (sunFontManager instanceof FontSupport) {
            this.fontConfig = sunFontManager.getFontConfiguration();
        }
        if (this.fontConfig == null) {
            return;
        }
        this.familyName = str.toLowerCase(Locale.ENGLISH);
        if (!FontConfiguration.isLogicalFontFamilyName(this.familyName)) {
            this.familyName = this.fontConfig.getFallbackFamilyName(this.familyName, "sansserif");
        }
        this.componentFonts = this.fontConfig.getFontDescriptors(this.familyName, i2);
        char missingGlyphCharacter = getMissingGlyphCharacter();
        this.defaultChar = '?';
        if (this.componentFonts.length > 0) {
            this.defaultFont = this.componentFonts[0];
        }
        for (int i3 = 0; i3 < this.componentFonts.length; i3++) {
            if (!this.componentFonts[i3].isExcluded(missingGlyphCharacter) && this.componentFonts[i3].encoder.canEncode(missingGlyphCharacter)) {
                this.defaultFont = this.componentFonts[i3];
                this.defaultChar = missingGlyphCharacter;
                return;
            }
        }
    }

    public CharsetString[] makeMultiCharsetString(String str) {
        return makeMultiCharsetString(str.toCharArray(), 0, str.length(), true);
    }

    public CharsetString[] makeMultiCharsetString(String str, boolean z2) {
        return makeMultiCharsetString(str.toCharArray(), 0, str.length(), z2);
    }

    public CharsetString[] makeMultiCharsetString(char[] cArr, int i2, int i3) {
        return makeMultiCharsetString(cArr, i2, i3, true);
    }

    public CharsetString[] makeMultiCharsetString(char[] cArr, int i2, int i3, boolean z2) {
        CharsetString[] charsetStringArr;
        if (i3 < 1) {
            return new CharsetString[0];
        }
        Vector vector = null;
        char[] cArr2 = new char[i3];
        char c2 = this.defaultChar;
        boolean z3 = false;
        FontDescriptor fontDescriptor = this.defaultFont;
        int i4 = 0;
        while (true) {
            if (i4 >= this.componentFonts.length) {
                break;
            }
            if (this.componentFonts[i4].isExcluded(cArr[i2]) || !this.componentFonts[i4].encoder.canEncode(cArr[i2])) {
                i4++;
            } else {
                fontDescriptor = this.componentFonts[i4];
                c2 = cArr[i2];
                z3 = true;
                break;
            }
        }
        if (!z2 && !z3) {
            return null;
        }
        cArr2[0] = c2;
        int i5 = 0;
        for (int i6 = 1; i6 < i3; i6++) {
            char c3 = cArr[i2 + i6];
            FontDescriptor fontDescriptor2 = this.defaultFont;
            char c4 = this.defaultChar;
            boolean z4 = false;
            int i7 = 0;
            while (true) {
                if (i7 >= this.componentFonts.length) {
                    break;
                }
                if (this.componentFonts[i7].isExcluded(c3) || !this.componentFonts[i7].encoder.canEncode(c3)) {
                    i7++;
                } else {
                    fontDescriptor2 = this.componentFonts[i7];
                    c4 = c3;
                    z4 = true;
                    break;
                }
            }
            if (!z2 && !z4) {
                return null;
            }
            cArr2[i6] = c4;
            if (fontDescriptor != fontDescriptor2) {
                if (vector == null) {
                    vector = new Vector(3);
                }
                vector.addElement(new CharsetString(cArr2, i5, i6 - i5, fontDescriptor));
                fontDescriptor = fontDescriptor2;
                FontDescriptor fontDescriptor3 = this.defaultFont;
                i5 = i6;
            }
        }
        CharsetString charsetString = new CharsetString(cArr2, i5, i3 - i5, fontDescriptor);
        if (vector == null) {
            charsetStringArr = new CharsetString[]{charsetString};
        } else {
            vector.addElement(charsetString);
            charsetStringArr = new CharsetString[vector.size()];
            for (int i8 = 0; i8 < vector.size(); i8++) {
                charsetStringArr[i8] = (CharsetString) vector.elementAt(i8);
            }
        }
        return charsetStringArr;
    }

    public boolean mightHaveMultiFontMetrics() {
        return this.fontConfig != null;
    }

    public Object[] makeConvertedMultiFontString(String str) {
        return makeConvertedMultiFontChars(str.toCharArray(), 0, str.length());
    }

    public Object[] makeConvertedMultiFontChars(char[] cArr, int i2, int i3) {
        Object[] objArr = new Object[2];
        byte[] bArr = null;
        int i4 = i2;
        int i5 = 0;
        int i6 = 0;
        FontDescriptor fontDescriptor = null;
        int i7 = i2 + i3;
        if (i2 < 0 || i7 > cArr.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (i4 >= i7) {
            return null;
        }
        while (i4 < i7) {
            char c2 = cArr[i4];
            int i8 = c2 & FONTCACHEMASK;
            PlatformFontCache platformFontCache = (PlatformFontCache) getFontCache()[i8];
            if (platformFontCache == null || platformFontCache.uniChar != c2) {
                FontDescriptor fontDescriptor2 = this.defaultFont;
                char c3 = this.defaultChar;
                char c4 = cArr[i4];
                int length = this.componentFonts.length;
                int i9 = 0;
                while (true) {
                    if (i9 < length) {
                        FontDescriptor fontDescriptor3 = this.componentFonts[i9];
                        fontDescriptor3.encoder.reset();
                        if (fontDescriptor3.isExcluded(c4) || !fontDescriptor3.encoder.canEncode(c4)) {
                            i9++;
                        } else {
                            fontDescriptor2 = fontDescriptor3;
                            c3 = c4;
                            break;
                        }
                    }
                }
                try {
                    char[] cArr2 = {c3};
                    platformFontCache = new PlatformFontCache();
                    if (!fontDescriptor2.useUnicode()) {
                        fontDescriptor2.encoder.encode(CharBuffer.wrap(cArr2), platformFontCache.f13542bb, true);
                    } else if (FontDescriptor.isLE) {
                        platformFontCache.f13542bb.put((byte) (cArr2[0] & 255));
                        platformFontCache.f13542bb.put((byte) (cArr2[0] >> '\b'));
                    } else {
                        platformFontCache.f13542bb.put((byte) (cArr2[0] >> '\b'));
                        platformFontCache.f13542bb.put((byte) (cArr2[0] & 255));
                    }
                    platformFontCache.fontDescriptor = fontDescriptor2;
                    platformFontCache.uniChar = cArr[i4];
                    getFontCache()[i8] = platformFontCache;
                } catch (Exception e2) {
                    System.err.println(e2);
                    e2.printStackTrace();
                    return null;
                }
            }
            if (fontDescriptor != platformFontCache.fontDescriptor) {
                if (fontDescriptor != null) {
                    int i10 = i6;
                    int i11 = i6 + 1;
                    objArr[i10] = fontDescriptor;
                    i6 = i11 + 1;
                    objArr[i11] = bArr;
                    if (bArr != null) {
                        int i12 = i5 - 4;
                        bArr[0] = (byte) (i12 >> 24);
                        bArr[1] = (byte) (i12 >> 16);
                        bArr[2] = (byte) (i12 >> 8);
                        bArr[3] = (byte) i12;
                    }
                    if (i6 >= objArr.length) {
                        Object[] objArr2 = new Object[objArr.length * 2];
                        System.arraycopy(objArr, 0, objArr2, 0, objArr.length);
                        objArr = objArr2;
                    }
                }
                if (platformFontCache.fontDescriptor.useUnicode()) {
                    bArr = new byte[(((i7 - i4) + 1) * ((int) platformFontCache.fontDescriptor.unicodeEncoder.maxBytesPerChar())) + 4];
                } else {
                    bArr = new byte[(((i7 - i4) + 1) * ((int) platformFontCache.fontDescriptor.encoder.maxBytesPerChar())) + 4];
                }
                i5 = 4;
                fontDescriptor = platformFontCache.fontDescriptor;
            }
            byte[] bArrArray = platformFontCache.f13542bb.array();
            int iPosition = platformFontCache.f13542bb.position();
            if (iPosition == 1) {
                int i13 = i5;
                i5++;
                bArr[i13] = bArrArray[0];
            } else if (iPosition == 2) {
                int i14 = i5;
                int i15 = i5 + 1;
                bArr[i14] = bArrArray[0];
                i5 = i15 + 1;
                bArr[i15] = bArrArray[1];
            } else if (iPosition == 3) {
                int i16 = i5;
                int i17 = i5 + 1;
                bArr[i16] = bArrArray[0];
                int i18 = i17 + 1;
                bArr[i17] = bArrArray[1];
                i5 = i18 + 1;
                bArr[i18] = bArrArray[2];
            } else if (iPosition == 4) {
                int i19 = i5;
                int i20 = i5 + 1;
                bArr[i19] = bArrArray[0];
                int i21 = i20 + 1;
                bArr[i20] = bArrArray[1];
                int i22 = i21 + 1;
                bArr[i21] = bArrArray[2];
                i5 = i22 + 1;
                bArr[i22] = bArrArray[3];
            }
            i4++;
        }
        objArr[i6] = fontDescriptor;
        objArr[i6 + 1] = bArr;
        if (bArr != null) {
            int i23 = i5 - 4;
            bArr[0] = (byte) (i23 >> 24);
            bArr[1] = (byte) (i23 >> 16);
            bArr[2] = (byte) (i23 >> 8);
            bArr[3] = (byte) i23;
        }
        return objArr;
    }

    protected final Object[] getFontCache() {
        if (this.fontCache == null) {
            this.fontCache = new Object[FONTCACHESIZE];
        }
        return this.fontCache;
    }

    /* loaded from: rt.jar:sun/awt/PlatformFont$PlatformFontCache.class */
    class PlatformFontCache {
        char uniChar;
        FontDescriptor fontDescriptor;

        /* renamed from: bb, reason: collision with root package name */
        ByteBuffer f13542bb = ByteBuffer.allocate(4);

        PlatformFontCache() {
        }
    }
}
