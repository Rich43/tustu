package sun.java2d.pipe;

import java.awt.AlphaComposite;
import java.awt.CompositeContext;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import sun.awt.image.BufImgSurfaceData;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.MaskBlit;

/* loaded from: rt.jar:sun/java2d/pipe/GeneralCompositePipe.class */
public class GeneralCompositePipe implements CompositePipe {

    /* loaded from: rt.jar:sun/java2d/pipe/GeneralCompositePipe$TileContext.class */
    class TileContext {
        SunGraphics2D sunG2D;
        PaintContext paintCtxt;
        CompositeContext compCtxt;
        ColorModel compModel;
        Object pipeState;

        public TileContext(SunGraphics2D sunGraphics2D, PaintContext paintContext, CompositeContext compositeContext, ColorModel colorModel) {
            this.sunG2D = sunGraphics2D;
            this.paintCtxt = paintContext;
            this.compCtxt = compositeContext;
            this.compModel = colorModel;
        }
    }

    @Override // sun.java2d.pipe.CompositePipe
    public Object startSequence(SunGraphics2D sunGraphics2D, Shape shape, Rectangle rectangle, int[] iArr) {
        RenderingHints renderingHints = sunGraphics2D.getRenderingHints();
        ColorModel deviceColorModel = sunGraphics2D.getDeviceColorModel();
        PaintContext paintContextCreateContext = sunGraphics2D.paint.createContext(deviceColorModel, rectangle, shape.getBounds2D(), sunGraphics2D.cloneTransform(), renderingHints);
        return new TileContext(sunGraphics2D, paintContextCreateContext, sunGraphics2D.composite.createContext(paintContextCreateContext.getColorModel(), deviceColorModel, renderingHints), deviceColorModel);
    }

    @Override // sun.java2d.pipe.CompositePipe
    public boolean needTile(Object obj, int i2, int i3, int i4, int i5) {
        return true;
    }

    @Override // sun.java2d.pipe.CompositePipe
    public void renderPathTile(Object obj, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        Raster rasterCreateChild;
        WritableRaster writableRasterCreateCompatibleWritableRaster;
        TileContext tileContext = (TileContext) obj;
        PaintContext paintContext = tileContext.paintCtxt;
        CompositeContext compositeContext = tileContext.compCtxt;
        SunGraphics2D sunGraphics2D = tileContext.sunG2D;
        Raster raster = paintContext.getRaster(i4, i5, i6, i7);
        paintContext.getColorModel();
        SurfaceData surfaceData = sunGraphics2D.getSurfaceData();
        Raster raster2 = surfaceData.getRaster(i4, i5, i6, i7);
        if ((raster2 instanceof WritableRaster) && bArr == null) {
            writableRasterCreateCompatibleWritableRaster = ((WritableRaster) raster2).createWritableChild(i4, i5, i6, i7, 0, 0, null);
            rasterCreateChild = writableRasterCreateCompatibleWritableRaster;
        } else {
            rasterCreateChild = raster2.createChild(i4, i5, i6, i7, 0, 0, null);
            writableRasterCreateCompatibleWritableRaster = rasterCreateChild.createCompatibleWritableRaster();
        }
        compositeContext.compose(raster, rasterCreateChild, writableRasterCreateCompatibleWritableRaster);
        if (raster2 != writableRasterCreateCompatibleWritableRaster && writableRasterCreateCompatibleWritableRaster.getParent() != raster2) {
            if ((raster2 instanceof WritableRaster) && bArr == null) {
                ((WritableRaster) raster2).setDataElements(i4, i5, (Raster) writableRasterCreateCompatibleWritableRaster);
                return;
            }
            ColorModel deviceColorModel = sunGraphics2D.getDeviceColorModel();
            SurfaceData surfaceDataCreateData = BufImgSurfaceData.createData(new BufferedImage(deviceColorModel, writableRasterCreateCompatibleWritableRaster, deviceColorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null));
            if (bArr == null) {
                Blit.getFromCache(surfaceDataCreateData.getSurfaceType(), CompositeType.SrcNoEa, surfaceData.getSurfaceType()).Blit(surfaceDataCreateData, surfaceData, AlphaComposite.Src, null, 0, 0, i4, i5, i6, i7);
            } else {
                MaskBlit.getFromCache(surfaceDataCreateData.getSurfaceType(), CompositeType.SrcNoEa, surfaceData.getSurfaceType()).MaskBlit(surfaceDataCreateData, surfaceData, AlphaComposite.Src, null, 0, 0, i4, i5, i6, i7, bArr, i2, i3);
            }
        }
    }

    @Override // sun.java2d.pipe.CompositePipe
    public void skipTile(Object obj, int i2, int i3) {
    }

    @Override // sun.java2d.pipe.CompositePipe
    public void endSequence(Object obj) {
        TileContext tileContext = (TileContext) obj;
        if (tileContext.paintCtxt != null) {
            tileContext.paintCtxt.dispose();
        }
        if (tileContext.compCtxt != null) {
            tileContext.compCtxt.dispose();
        }
    }
}
