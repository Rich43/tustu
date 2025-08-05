package sun.java2d.cmm;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/* loaded from: rt.jar:sun/java2d/cmm/ColorTransform.class */
public interface ColorTransform {
    public static final int Any = -1;
    public static final int In = 1;
    public static final int Out = 2;
    public static final int Gamut = 3;
    public static final int Simulation = 4;

    int getNumInComponents();

    int getNumOutComponents();

    void colorConvert(BufferedImage bufferedImage, BufferedImage bufferedImage2);

    void colorConvert(Raster raster, WritableRaster writableRaster, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4);

    void colorConvert(Raster raster, WritableRaster writableRaster);

    short[] colorConvert(short[] sArr, short[] sArr2);

    byte[] colorConvert(byte[] bArr, byte[] bArr2);
}
