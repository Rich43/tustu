package sun.java2d;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import sun.awt.image.BufImgSurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.XORComposite;

/* loaded from: rt.jar:sun/java2d/SunCompositeContext.class */
public class SunCompositeContext implements CompositeContext {
    ColorModel srcCM;
    ColorModel dstCM;
    Composite composite;
    CompositeType comptype;

    public SunCompositeContext(AlphaComposite alphaComposite, ColorModel colorModel, ColorModel colorModel2) {
        if (colorModel == null) {
            throw new NullPointerException("Source color model cannot be null");
        }
        if (colorModel2 == null) {
            throw new NullPointerException("Destination color model cannot be null");
        }
        this.srcCM = colorModel;
        this.dstCM = colorModel2;
        this.composite = alphaComposite;
        this.comptype = CompositeType.forAlphaComposite(alphaComposite);
    }

    public SunCompositeContext(XORComposite xORComposite, ColorModel colorModel, ColorModel colorModel2) {
        if (colorModel == null) {
            throw new NullPointerException("Source color model cannot be null");
        }
        if (colorModel2 == null) {
            throw new NullPointerException("Destination color model cannot be null");
        }
        this.srcCM = colorModel;
        this.dstCM = colorModel2;
        this.composite = xORComposite;
        this.comptype = CompositeType.Xor;
    }

    @Override // java.awt.CompositeContext
    public void dispose() {
    }

    @Override // java.awt.CompositeContext
    public void compose(Raster raster, Raster raster2, WritableRaster writableRaster) {
        WritableRaster writableRasterCreateCompatibleWritableRaster;
        if (raster2 != writableRaster) {
            writableRaster.setDataElements(0, 0, raster2);
        }
        if (raster instanceof WritableRaster) {
            writableRasterCreateCompatibleWritableRaster = (WritableRaster) raster;
        } else {
            writableRasterCreateCompatibleWritableRaster = raster.createCompatibleWritableRaster();
            writableRasterCreateCompatibleWritableRaster.setDataElements(0, 0, raster);
        }
        int iMin = Math.min(writableRasterCreateCompatibleWritableRaster.getWidth(), raster2.getWidth());
        int iMin2 = Math.min(writableRasterCreateCompatibleWritableRaster.getHeight(), raster2.getHeight());
        BufferedImage bufferedImage = new BufferedImage(this.srcCM, writableRasterCreateCompatibleWritableRaster, this.srcCM.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
        BufferedImage bufferedImage2 = new BufferedImage(this.dstCM, writableRaster, this.dstCM.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
        SurfaceData surfaceDataCreateData = BufImgSurfaceData.createData(bufferedImage);
        SurfaceData surfaceDataCreateData2 = BufImgSurfaceData.createData(bufferedImage2);
        Blit.getFromCache(surfaceDataCreateData.getSurfaceType(), this.comptype, surfaceDataCreateData2.getSurfaceType()).Blit(surfaceDataCreateData, surfaceDataCreateData2, this.composite, null, 0, 0, 0, 0, iMin, iMin2);
    }
}
