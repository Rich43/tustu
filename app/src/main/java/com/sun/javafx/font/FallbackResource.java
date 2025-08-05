package com.sun.javafx.font;

import com.sun.javafx.font.FontConfigManager;
import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: jfxrt.jar:com/sun/javafx/font/FallbackResource.class */
class FallbackResource implements CompositeFontResource {
    private ArrayList<String> linkedFontFiles;
    private ArrayList<String> linkedFontNames;
    private FontResource[] fallbacks;
    private FontResource[] nativeFallbacks;
    private boolean isBold;
    private boolean isItalic;
    private int aaMode;
    private CompositeGlyphMapper mapper;
    Map<FontStrikeDesc, WeakReference<FontStrike>> strikeMap = new ConcurrentHashMap();
    static FallbackResource[] greyFallBackResource = new FallbackResource[4];
    static FallbackResource[] lcdFallBackResource = new FallbackResource[4];

    @Override // com.sun.javafx.font.FontResource
    public Map<FontStrikeDesc, WeakReference<FontStrike>> getStrikeMap() {
        return this.strikeMap;
    }

    FallbackResource(boolean bold, boolean italic, int aaMode) {
        this.isBold = bold;
        this.isItalic = italic;
        this.aaMode = aaMode;
    }

    static FallbackResource getFallbackResource(boolean bold, boolean italic, int aaMode) {
        FallbackResource[] arr = aaMode == 0 ? greyFallBackResource : lcdFallBackResource;
        int index = bold ? 1 : 0;
        if (italic) {
            index += 2;
        }
        FallbackResource font = arr[index];
        if (font == null) {
            font = new FallbackResource(bold, italic, aaMode);
            arr[index] = font;
        }
        return font;
    }

    @Override // com.sun.javafx.font.FontResource
    public int getDefaultAAMode() {
        return this.aaMode;
    }

    private String throwException() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFullName() {
        return throwException();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getPSName() {
        return throwException();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFamilyName() {
        return throwException();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getStyleName() {
        return throwException();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleFullName() {
        return throwException();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleFamilyName() {
        return throwException();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleStyleName() {
        return throwException();
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isBold() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isItalic() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override // com.sun.javafx.font.FontResource
    public int getFeatures() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFileName() {
        return throwException();
    }

    @Override // com.sun.javafx.font.FontResource
    public Object getPeer() {
        return null;
    }

    @Override // com.sun.javafx.font.FontResource
    public void setPeer(Object peer) {
        throwException();
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isEmbeddedFont() {
        return false;
    }

    @Override // com.sun.javafx.font.FontResource
    public CharToGlyphMapper getGlyphMapper() {
        if (this.mapper == null) {
            this.mapper = new CompositeGlyphMapper(this);
        }
        return this.mapper;
    }

    @Override // com.sun.javafx.font.CompositeFontResource
    public int getSlotForFont(String fontName) {
        FontResource[] tmp;
        getLinkedFonts();
        int i2 = 0;
        Iterator<String> it = this.linkedFontNames.iterator();
        while (it.hasNext()) {
            String linkedFontName = it.next();
            if (fontName.equalsIgnoreCase(linkedFontName)) {
                return i2;
            }
            i2++;
        }
        if (this.nativeFallbacks != null) {
            for (FontResource nativeFallback : this.nativeFallbacks) {
                if (fontName.equalsIgnoreCase(nativeFallback.getFullName())) {
                    return i2;
                }
                i2++;
            }
        }
        if (i2 >= 126) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("\tToo many font fallbacks!");
                return -1;
            }
            return -1;
        }
        PrismFontFactory factory = PrismFontFactory.getFontFactory();
        FontResource fr = factory.getFontResource(fontName, null, false);
        if (fr == null) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("\t Font name not supported \"" + fontName + "\".");
                return -1;
            }
            return -1;
        }
        if (this.nativeFallbacks == null) {
            tmp = new FontResource[1];
        } else {
            tmp = new FontResource[this.nativeFallbacks.length + 1];
            System.arraycopy(this.nativeFallbacks, 0, tmp, 0, this.nativeFallbacks.length);
        }
        tmp[tmp.length - 1] = fr;
        this.nativeFallbacks = tmp;
        return i2;
    }

    private void getLinkedFonts() {
        ArrayList<String>[] linkedFontInfo;
        if (this.fallbacks == null) {
            if (PrismFontFactory.isLinux) {
                FontConfigManager.FcCompFont font = FontConfigManager.getFontConfigFont("sans", this.isBold, this.isItalic);
                this.linkedFontFiles = FontConfigManager.getFileNames(font, false);
                this.linkedFontNames = FontConfigManager.getFontNames(font, false);
                this.fallbacks = new FontResource[this.linkedFontFiles.size()];
                return;
            }
            if (PrismFontFactory.isMacOSX) {
                linkedFontInfo = PrismFontFactory.getLinkedFonts("Arial Unicode MS", true);
            } else {
                linkedFontInfo = PrismFontFactory.getLinkedFonts("Tahoma", true);
            }
            this.linkedFontFiles = linkedFontInfo[0];
            this.linkedFontNames = linkedFontInfo[1];
            this.fallbacks = new FontResource[this.linkedFontFiles.size()];
        }
    }

    @Override // com.sun.javafx.font.CompositeFontResource
    public int getNumSlots() {
        getLinkedFonts();
        int num = this.linkedFontFiles.size();
        if (this.nativeFallbacks != null) {
            num += this.nativeFallbacks.length;
        }
        return num;
    }

    @Override // com.sun.javafx.font.FontResource
    public float[] getGlyphBoundingBox(int glyphCode, float size, float[] retArr) {
        int slot = glyphCode >>> 24;
        int slotglyphCode = glyphCode & 16777215;
        FontResource slotResource = getSlotResource(slot);
        return slotResource.getGlyphBoundingBox(slotglyphCode, size, retArr);
    }

    @Override // com.sun.javafx.font.FontResource
    public float getAdvance(int glyphCode, float size) {
        int slot = glyphCode >>> 24;
        int slotglyphCode = glyphCode & 16777215;
        FontResource slotResource = getSlotResource(slot);
        return slotResource.getAdvance(slotglyphCode, size);
    }

    @Override // com.sun.javafx.font.CompositeFontResource
    public synchronized FontResource getSlotResource(int slot) {
        getLinkedFonts();
        if (slot >= this.fallbacks.length) {
            int slot2 = slot - this.fallbacks.length;
            if (this.nativeFallbacks == null || slot2 >= this.nativeFallbacks.length) {
                return null;
            }
            return this.nativeFallbacks[slot2];
        }
        if (this.fallbacks[slot] == null) {
            String file = this.linkedFontFiles.get(slot);
            String name = this.linkedFontNames.get(slot);
            this.fallbacks[slot] = PrismFontFactory.getFontFactory().getFontResource(name, file, false);
        }
        return this.fallbacks[slot];
    }

    @Override // com.sun.javafx.font.FontResource
    public FontStrike getStrike(float size, BaseTransform transform) {
        return getStrike(size, transform, getDefaultAAMode());
    }

    @Override // com.sun.javafx.font.FontResource
    public FontStrike getStrike(float size, BaseTransform transform, int aaMode) {
        WeakReference<FontStrike> ref;
        FontStrikeDesc desc = new FontStrikeDesc(size, transform, aaMode);
        WeakReference<FontStrike> ref2 = this.strikeMap.get(desc);
        CompositeStrike strike = null;
        if (ref2 != null) {
            strike = (CompositeStrike) ref2.get();
        }
        if (strike == null) {
            strike = new CompositeStrike(this, size, transform, aaMode, desc);
            if (strike.disposer != null) {
                ref = Disposer.addRecord(strike, strike.disposer);
            } else {
                ref = new WeakReference<>(strike);
            }
            this.strikeMap.put(desc, ref);
        }
        return strike;
    }
}
