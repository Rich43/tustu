package com.sun.prism.image;

import com.sun.prism.GraphicsResource;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;

/* loaded from: jfxrt.jar:com/sun/prism/image/CompoundTexture.class */
public class CompoundTexture extends CompoundImage implements GraphicsResource {
    protected Texture[] texTiles;

    public CompoundTexture(Image image, int maxSize) {
        super(image, maxSize);
        this.texTiles = new Texture[this.tiles.length];
    }

    @Override // com.sun.prism.image.CompoundImage
    public Texture getTile(int x2, int y2, ResourceFactory factory) {
        int idx = x2 + (y2 * this.uSections);
        Texture tex = this.texTiles[idx];
        if (tex != null) {
            tex.lock();
            if (tex.isSurfaceLost()) {
                tex = null;
                this.texTiles[idx] = null;
            }
        }
        if (tex == null) {
            tex = factory.createTexture(this.tiles[idx], Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
            this.texTiles[idx] = tex;
        }
        return tex;
    }

    @Override // com.sun.prism.GraphicsResource
    public void dispose() {
        for (int i2 = 0; i2 != this.texTiles.length; i2++) {
            if (this.texTiles[i2] != null) {
                this.texTiles[i2].dispose();
                this.texTiles[i2] = null;
            }
        }
    }
}
