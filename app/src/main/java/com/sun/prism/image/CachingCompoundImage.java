package com.sun.prism.image;

import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;

/* loaded from: jfxrt.jar:com/sun/prism/image/CachingCompoundImage.class */
public class CachingCompoundImage extends CompoundImage {
    public CachingCompoundImage(Image image, int maxSize) {
        super(image, maxSize);
    }

    @Override // com.sun.prism.image.CompoundImage
    public Texture getTile(int x2, int y2, ResourceFactory factory) {
        return factory.getCachedTexture(this.tiles[x2 + (y2 * this.uSections)], Texture.WrapMode.CLAMP_TO_EDGE);
    }
}
