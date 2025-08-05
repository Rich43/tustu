package sun.java2d.loops;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.Hashtable;
import sun.awt.image.BufImgSurfaceData;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/loops/BlitBg.class */
public class BlitBg extends GraphicsPrimitive {
    public static final String methodSignature = "BlitBg(...)".toString();
    public static final int primTypeID = makePrimTypeID();
    private static RenderCache blitcache = new RenderCache(20);

    public native void BlitBg(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    static {
        GraphicsPrimitiveMgr.registerGeneral(new BlitBg(null, null, null));
    }

    public static BlitBg locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (BlitBg) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public static BlitBg getFromCache(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        Object obj = blitcache.get(surfaceType, compositeType, surfaceType2);
        if (obj != null) {
            return (BlitBg) obj;
        }
        BlitBg blitBgLocate = locate(surfaceType, compositeType, surfaceType2);
        if (blitBgLocate == null) {
            System.out.println("blitbg loop not found for:");
            System.out.println("src:  " + ((Object) surfaceType));
            System.out.println("comp: " + ((Object) compositeType));
            System.out.println("dst:  " + ((Object) surfaceType2));
        } else {
            blitcache.put(surfaceType, compositeType, surfaceType2, blitBgLocate);
        }
        return blitBgLocate;
    }

    protected BlitBg(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public BlitBg(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return new General(surfaceType, compositeType, surfaceType2);
    }

    /* loaded from: rt.jar:sun/java2d/loops/BlitBg$General.class */
    private static class General extends BlitBg {
        CompositeType compositeType;
        private static Font defaultFont = new Font(Font.DIALOG, 0, 12);

        public General(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
            super(surfaceType, compositeType, surfaceType2);
            this.compositeType = compositeType;
        }

        @Override // sun.java2d.loops.BlitBg
        public void BlitBg(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            ColorModel colorModel = surfaceData2.getColorModel();
            boolean z2 = (i2 >>> 24) != 255;
            if (!colorModel.hasAlpha() && z2) {
                colorModel = ColorModel.getRGBdefault();
            }
            SurfaceData surfaceDataCreateData = BufImgSurfaceData.createData(new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(i7, i8), colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null));
            Color color = new Color(i2, z2);
            SunGraphics2D sunGraphics2D = new SunGraphics2D(surfaceDataCreateData, color, color, defaultFont);
            FillRect fillRectLocate = FillRect.locate(SurfaceType.AnyColor, CompositeType.SrcNoEa, surfaceDataCreateData.getSurfaceType());
            Blit fromCache = Blit.getFromCache(surfaceData.getSurfaceType(), CompositeType.SrcOverNoEa, surfaceDataCreateData.getSurfaceType());
            Blit fromCache2 = Blit.getFromCache(surfaceDataCreateData.getSurfaceType(), this.compositeType, surfaceData2.getSurfaceType());
            fillRectLocate.FillRect(sunGraphics2D, surfaceDataCreateData, 0, 0, i7, i8);
            fromCache.Blit(surfaceData, surfaceDataCreateData, AlphaComposite.SrcOver, null, i3, i4, 0, 0, i7, i8);
            fromCache2.Blit(surfaceDataCreateData, surfaceData2, composite, region, 0, 0, i5, i6, i7, i8);
        }
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceBlitBg(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/BlitBg$TraceBlitBg.class */
    private static class TraceBlitBg extends BlitBg {
        BlitBg target;

        public TraceBlitBg(BlitBg blitBg) {
            super(blitBg.getSourceType(), blitBg.getCompositeType(), blitBg.getDestType());
            this.target = blitBg;
        }

        @Override // sun.java2d.loops.BlitBg, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.BlitBg
        public void BlitBg(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            tracePrimitive(this.target);
            this.target.BlitBg(surfaceData, surfaceData2, composite, region, i2, i3, i4, i5, i6, i7, i8);
        }
    }
}
