package org.icepdf.core.pobjects.graphics;

import java.awt.Color;
import org.icepdf.core.pobjects.ImageStream;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Resources;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ImageReferenceFactory.class */
public class ImageReferenceFactory {
    private static ImageReference scaleType;

    /* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/ImageReferenceFactory$ImageReference.class */
    public enum ImageReference {
        DEFAULT,
        SCALED,
        MIP_MAP,
        SMOOTH_SCALED
    }

    static {
        String imageReferencetype = Defs.sysProperty("org.icepdf.core.imageReference", "default");
        if ("scaled".equals(imageReferencetype)) {
            scaleType = ImageReference.SCALED;
            return;
        }
        if ("mipmap".equals(imageReferencetype)) {
            scaleType = ImageReference.MIP_MAP;
        } else if ("smoothScaled".equals(imageReferencetype)) {
            scaleType = ImageReference.SMOOTH_SCALED;
        } else {
            scaleType = ImageReference.DEFAULT;
        }
    }

    private ImageReferenceFactory() {
    }

    public static ImageReference getScaleType() {
        return scaleType;
    }

    public static void setScaleType(ImageReference scaleType2) {
        scaleType = scaleType2;
    }

    public static org.icepdf.core.pobjects.graphics.ImageReference getImageReference(ImageStream imageStream, Resources resources, Color fillColor, Integer imageIndex, Page page) {
        switch (scaleType) {
            case SCALED:
                return new ScaledImageReference(imageStream, fillColor, resources, imageIndex.intValue(), page);
            case SMOOTH_SCALED:
                return new SmoothScaledImageReference(imageStream, fillColor, resources, imageIndex.intValue(), page);
            case MIP_MAP:
                return new MipMappedImageReference(imageStream, fillColor, resources, imageIndex.intValue(), page);
            default:
                return new ImageStreamReference(imageStream, fillColor, resources, imageIndex.intValue(), page);
        }
    }
}
