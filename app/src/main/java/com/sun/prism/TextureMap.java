package com.sun.prism;

import com.sun.prism.PhongMaterial;

/* loaded from: jfxrt.jar:com/sun/prism/TextureMap.class */
public class TextureMap {
    private final PhongMaterial.MapType type;
    private Image image;
    private Texture texture;
    private boolean dirty;

    public TextureMap(PhongMaterial.MapType type) {
        this.type = type;
    }

    public PhongMaterial.MapType getType() {
        return this.type;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
