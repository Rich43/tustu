package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: jfxrt.jar:com/sun/javafx/font/PrismCompositeFontResource.class */
class PrismCompositeFontResource implements CompositeFontResource {
    private FontResource primaryResource;
    private FallbackResource fallbackResource;
    CompositeGlyphMapper mapper;
    Map<FontStrikeDesc, WeakReference<FontStrike>> strikeMap = new ConcurrentHashMap();

    PrismCompositeFontResource(FontResource primaryResource, String lookupName) {
        if (!(primaryResource instanceof PrismFontFile)) {
            Thread.dumpStack();
            throw new IllegalStateException("wrong resource type");
        }
        if (lookupName != null) {
            PrismFontFactory factory = PrismFontFactory.getFontFactory();
            factory.compResourceMap.put(lookupName, this);
        }
        this.primaryResource = primaryResource;
        int aaMode = primaryResource.getDefaultAAMode();
        boolean bold = primaryResource.isBold();
        boolean italic = primaryResource.isItalic();
        this.fallbackResource = FallbackResource.getFallbackResource(bold, italic, aaMode);
    }

    @Override // com.sun.javafx.font.CompositeFontResource
    public int getNumSlots() {
        return this.fallbackResource.getNumSlots() + 1;
    }

    @Override // com.sun.javafx.font.CompositeFontResource
    public int getSlotForFont(String fontName) {
        if (this.primaryResource.getFullName().equalsIgnoreCase(fontName)) {
            return 0;
        }
        return this.fallbackResource.getSlotForFont(fontName) + 1;
    }

    @Override // com.sun.javafx.font.CompositeFontResource
    public FontResource getSlotResource(int slot) {
        if (slot == 0) {
            return this.primaryResource;
        }
        FontResource fb = this.fallbackResource.getSlotResource(slot - 1);
        if (fb != null) {
            return fb;
        }
        return this.primaryResource;
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFullName() {
        return this.primaryResource.getFullName();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getPSName() {
        return this.primaryResource.getPSName();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFamilyName() {
        return this.primaryResource.getFamilyName();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getStyleName() {
        return this.primaryResource.getStyleName();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleFullName() {
        return this.primaryResource.getLocaleFullName();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleFamilyName() {
        return this.primaryResource.getLocaleFamilyName();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getLocaleStyleName() {
        return this.primaryResource.getLocaleStyleName();
    }

    @Override // com.sun.javafx.font.FontResource
    public String getFileName() {
        return this.primaryResource.getFileName();
    }

    @Override // com.sun.javafx.font.FontResource
    public int getFeatures() {
        return this.primaryResource.getFeatures();
    }

    @Override // com.sun.javafx.font.FontResource
    public Object getPeer() {
        return this.primaryResource.getPeer();
    }

    @Override // com.sun.javafx.font.FontResource
    public void setPeer(Object peer) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isEmbeddedFont() {
        return this.primaryResource.isEmbeddedFont();
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isBold() {
        return this.primaryResource.isBold();
    }

    @Override // com.sun.javafx.font.FontResource
    public boolean isItalic() {
        return this.primaryResource.isItalic();
    }

    @Override // com.sun.javafx.font.FontResource
    public CharToGlyphMapper getGlyphMapper() {
        if (this.mapper == null) {
            this.mapper = new CompositeGlyphMapper(this);
        }
        return this.mapper;
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

    @Override // com.sun.javafx.font.FontResource
    public Map<FontStrikeDesc, WeakReference<FontStrike>> getStrikeMap() {
        return this.strikeMap;
    }

    @Override // com.sun.javafx.font.FontResource
    public int getDefaultAAMode() {
        return getSlotResource(0).getDefaultAAMode();
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

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PrismCompositeFontResource)) {
            return false;
        }
        PrismCompositeFontResource other = (PrismCompositeFontResource) obj;
        return this.primaryResource.equals(other.primaryResource);
    }

    public int hashCode() {
        return this.primaryResource.hashCode();
    }
}
