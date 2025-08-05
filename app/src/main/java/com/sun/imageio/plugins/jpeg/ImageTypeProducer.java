package com.sun.imageio.plugins.jpeg;

import com.sun.imageio.plugins.jpeg.JPEG;
import javax.imageio.ImageTypeSpecifier;
import sun.text.normalizer.NormalizerImpl;

/* compiled from: JPEGImageReader.java */
/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/ImageTypeProducer.class */
class ImageTypeProducer {
    private ImageTypeSpecifier type;
    boolean failed;
    private int csCode;
    private static final ImageTypeProducer[] defaultTypes = new ImageTypeProducer[12];

    public ImageTypeProducer(int i2) {
        this.type = null;
        this.failed = false;
        this.csCode = i2;
    }

    public ImageTypeProducer() {
        this.type = null;
        this.failed = false;
        this.csCode = -1;
    }

    public synchronized ImageTypeSpecifier getType() {
        if (!this.failed && this.type == null) {
            try {
                this.type = produce();
            } catch (Throwable th) {
                this.failed = true;
            }
        }
        return this.type;
    }

    public static synchronized ImageTypeProducer getTypeProducer(int i2) {
        if (i2 < 0 || i2 >= 12) {
            return null;
        }
        if (defaultTypes[i2] == null) {
            defaultTypes[i2] = new ImageTypeProducer(i2);
        }
        return defaultTypes[i2];
    }

    protected ImageTypeSpecifier produce() {
        switch (this.csCode) {
            case 1:
                return ImageTypeSpecifier.createFromBufferedImageType(10);
            case 2:
                return ImageTypeSpecifier.createInterleaved(JPEG.JCS.sRGB, JPEG.bOffsRGB, 0, false, false);
            case 3:
            case 4:
            case 7:
            case 8:
            case 9:
            default:
                return null;
            case 5:
                if (JPEG.JCS.getYCC() != null) {
                    return ImageTypeSpecifier.createInterleaved(JPEG.JCS.getYCC(), JPEG.bandOffsets[2], 0, false, false);
                }
                return null;
            case 6:
                return ImageTypeSpecifier.createPacked(JPEG.JCS.sRGB, -16777216, 16711680, NormalizerImpl.CC_MASK, 255, 3, false);
            case 10:
                if (JPEG.JCS.getYCC() != null) {
                    return ImageTypeSpecifier.createInterleaved(JPEG.JCS.getYCC(), JPEG.bandOffsets[3], 0, true, false);
                }
                return null;
        }
    }
}
