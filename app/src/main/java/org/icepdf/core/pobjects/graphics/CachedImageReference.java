package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Resources;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/CachedImageReference.class */
public abstract class CachedImageReference extends ImageReference {
    private ImagePool imagePool;
    private boolean isNull;

    protected CachedImageReference(ImageStream imageStream, Color fillColor, Resources resources, int imageIndex, Page page) {
        super(imageStream, fillColor, resources, imageIndex, page);
        this.imagePool = imageStream.getLibrary().getImagePool();
        this.reference = imageStream.getPObjectReference();
    }

    @Override // org.icepdf.core.pobjects.graphics.ImageReference
    public BufferedImage getImage() {
        if (this.isNull) {
            return null;
        }
        if (this.image != null && this.reference != null) {
            this.imagePool.put(this.reference, this.image);
            return this.image;
        }
        BufferedImage cached = this.imagePool.get(this.reference);
        if (cached != null) {
            return cached;
        }
        BufferedImage im = createImage();
        if (im != null && this.reference != null) {
            this.imagePool.put(this.reference, im);
        } else if (this.reference != null) {
            this.isNull = true;
        }
        return im;
    }
}
