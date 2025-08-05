package com.sun.imageio.plugins.bmp;

/* loaded from: rt.jar:com/sun/imageio/plugins/bmp/BMPCompressionTypes.class */
public class BMPCompressionTypes {
    private static final String[] compressionTypeNames = {"BI_RGB", "BI_RLE8", "BI_RLE4", "BI_BITFIELDS", "BI_JPEG", "BI_PNG"};

    static int getType(String str) {
        for (int i2 = 0; i2 < compressionTypeNames.length; i2++) {
            if (compressionTypeNames[i2].equals(str)) {
                return i2;
            }
        }
        return 0;
    }

    static String getName(int i2) {
        return compressionTypeNames[i2];
    }

    public static String[] getCompressionTypes() {
        return (String[]) compressionTypeNames.clone();
    }
}
