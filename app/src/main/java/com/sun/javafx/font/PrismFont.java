package com.sun.javafx.font;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/font/PrismFont.class */
class PrismFont implements PGFont {
    private String name;
    private float fontSize;
    protected FontResource fontResource;
    private int features;
    private int hash;

    PrismFont(FontResource fontResource, String name, float size) {
        this.fontResource = fontResource;
        this.name = name;
        this.fontSize = size;
    }

    @Override // com.sun.javafx.font.PGFont
    public String getFullName() {
        return this.fontResource.getFullName();
    }

    @Override // com.sun.javafx.font.PGFont
    public String getFamilyName() {
        return this.fontResource.getFamilyName();
    }

    @Override // com.sun.javafx.font.PGFont
    public String getStyleName() {
        return this.fontResource.getStyleName();
    }

    @Override // com.sun.javafx.font.PGFont
    public int getFeatures() {
        return this.features;
    }

    @Override // com.sun.javafx.font.PGFont
    public String getName() {
        return this.name;
    }

    @Override // com.sun.javafx.font.PGFont
    public float getSize() {
        return this.fontSize;
    }

    @Override // com.sun.javafx.font.PGFont
    public FontStrike getStrike(BaseTransform transform) {
        return this.fontResource.getStrike(this.fontSize, transform);
    }

    @Override // com.sun.javafx.font.PGFont
    public FontStrike getStrike(BaseTransform transform, int smoothingType) {
        return this.fontResource.getStrike(this.fontSize, transform, smoothingType);
    }

    @Override // com.sun.javafx.font.PGFont
    public FontResource getFontResource() {
        return this.fontResource;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PrismFont)) {
            return false;
        }
        PrismFont other = (PrismFont) obj;
        return this.fontSize == other.fontSize && this.fontResource.equals(other.fontResource);
    }

    public int hashCode() {
        if (this.hash != 0) {
            return this.hash;
        }
        this.hash = 497 + Float.floatToIntBits(this.fontSize);
        this.hash = (71 * this.hash) + this.fontResource.hashCode();
        return this.hash;
    }
}
