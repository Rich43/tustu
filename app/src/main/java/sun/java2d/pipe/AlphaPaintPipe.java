package sun.java2d.pipe;

import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import sun.awt.image.BufImgSurfaceData;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.MaskBlit;

/* loaded from: rt.jar:sun/java2d/pipe/AlphaPaintPipe.class */
public class AlphaPaintPipe implements CompositePipe {
    static WeakReference cachedLastRaster;
    static WeakReference cachedLastColorModel;
    static WeakReference cachedLastData;
    private static final int TILE_SIZE = 32;

    /* loaded from: rt.jar:sun/java2d/pipe/AlphaPaintPipe$TileContext.class */
    static class TileContext {
        SunGraphics2D sunG2D;
        PaintContext paintCtxt;
        ColorModel paintModel;
        WeakReference lastRaster;
        WeakReference lastData;
        MaskBlit lastMask;
        Blit lastBlit;
        SurfaceData dstData;

        public TileContext(SunGraphics2D sunGraphics2D, PaintContext paintContext) {
            this.sunG2D = sunGraphics2D;
            this.paintCtxt = paintContext;
            this.paintModel = paintContext.getColorModel();
            this.dstData = sunGraphics2D.getSurfaceData();
            synchronized (AlphaPaintPipe.class) {
                if (AlphaPaintPipe.cachedLastColorModel != null && AlphaPaintPipe.cachedLastColorModel.get() == this.paintModel) {
                    this.lastRaster = AlphaPaintPipe.cachedLastRaster;
                    this.lastData = AlphaPaintPipe.cachedLastData;
                }
            }
        }
    }

    @Override // sun.java2d.pipe.CompositePipe
    public Object startSequence(SunGraphics2D sunGraphics2D, Shape shape, Rectangle rectangle, int[] iArr) {
        return new TileContext(sunGraphics2D, sunGraphics2D.paint.createContext(sunGraphics2D.getDeviceColorModel(), rectangle, shape.getBounds2D(), sunGraphics2D.cloneTransform(), sunGraphics2D.getRenderingHints()));
    }

    @Override // sun.java2d.pipe.CompositePipe
    public boolean needTile(Object obj, int i2, int i3, int i4, int i5) {
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sun.java2d.pipe.CompositePipe
    public void renderPathTile(Object obj, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        TileContext tileContext = (TileContext) obj;
        PaintContext paintContext = tileContext.paintCtxt;
        SunGraphics2D sunGraphics2D = tileContext.sunG2D;
        SurfaceData surfaceData = tileContext.dstData;
        SurfaceData surfaceDataCreateData = null;
        Raster raster = null;
        if (tileContext.lastData != null && tileContext.lastRaster != null) {
            surfaceDataCreateData = (SurfaceData) tileContext.lastData.get();
            raster = (Raster) tileContext.lastRaster.get();
            if (surfaceDataCreateData == null || raster == null) {
                surfaceDataCreateData = null;
                raster = null;
            }
        }
        ColorModel colorModel = tileContext.paintModel;
        for (int i8 = 0; i8 < i7; i8 += 32) {
            int i9 = i5 + i8;
            int iMin = Math.min(i7 - i8, 32);
            for (int i10 = 0; i10 < i6; i10 += 32) {
                int i11 = i4 + i10;
                int iMin2 = Math.min(i6 - i10, 32);
                Raster raster2 = paintContext.getRaster(i11, i9, iMin2, iMin);
                if (raster2.getMinX() != 0 || raster2.getMinY() != 0) {
                    raster2 = raster2.createTranslatedChild(0, 0);
                }
                if (raster != raster2) {
                    raster = raster2;
                    tileContext.lastRaster = new WeakReference(raster);
                    surfaceDataCreateData = BufImgSurfaceData.createData(new BufferedImage(colorModel, (WritableRaster) raster2, colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null));
                    tileContext.lastData = new WeakReference(surfaceDataCreateData);
                    tileContext.lastMask = null;
                    tileContext.lastBlit = null;
                }
                if (bArr == null) {
                    if (tileContext.lastBlit == null) {
                        CompositeType compositeType = sunGraphics2D.imageComp;
                        if (CompositeType.SrcOverNoEa.equals(compositeType) && colorModel.getTransparency() == 1) {
                            compositeType = CompositeType.SrcNoEa;
                        }
                        tileContext.lastBlit = Blit.getFromCache(surfaceDataCreateData.getSurfaceType(), compositeType, surfaceData.getSurfaceType());
                    }
                    tileContext.lastBlit.Blit(surfaceDataCreateData, surfaceData, sunGraphics2D.composite, null, 0, 0, i11, i9, iMin2, iMin);
                } else {
                    if (tileContext.lastMask == null) {
                        CompositeType compositeType2 = sunGraphics2D.imageComp;
                        if (CompositeType.SrcOverNoEa.equals(compositeType2) && colorModel.getTransparency() == 1) {
                            compositeType2 = CompositeType.SrcNoEa;
                        }
                        tileContext.lastMask = MaskBlit.getFromCache(surfaceDataCreateData.getSurfaceType(), compositeType2, surfaceData.getSurfaceType());
                    }
                    tileContext.lastMask.MaskBlit(surfaceDataCreateData, surfaceData, sunGraphics2D.composite, null, 0, 0, i11, i9, iMin2, iMin, bArr, i2 + (i8 * i3) + i10, i3);
                }
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
        synchronized (AlphaPaintPipe.class) {
            if (tileContext.lastData != null) {
                cachedLastRaster = tileContext.lastRaster;
                if (cachedLastColorModel == null || cachedLastColorModel.get() != tileContext.paintModel) {
                    cachedLastColorModel = new WeakReference(tileContext.paintModel);
                }
                cachedLastData = tileContext.lastData;
            }
        }
    }
}
