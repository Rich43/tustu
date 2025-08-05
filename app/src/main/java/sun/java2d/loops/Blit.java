package sun.java2d.loops;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.lang.ref.WeakReference;
import sun.java2d.SurfaceData;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.SpanIterator;

/* loaded from: rt.jar:sun/java2d/loops/Blit.class */
public class Blit extends GraphicsPrimitive {
    public static final String methodSignature = "Blit(...)".toString();
    public static final int primTypeID = makePrimTypeID();
    private static RenderCache blitcache = new RenderCache(20);

    public native void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7);

    static {
        GraphicsPrimitiveMgr.registerGeneral(new Blit(null, null, null));
    }

    public static Blit locate(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        return (Blit) GraphicsPrimitiveMgr.locate(primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public static Blit getFromCache(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        Object obj = blitcache.get(surfaceType, compositeType, surfaceType2);
        if (obj != null) {
            return (Blit) obj;
        }
        Blit blitLocate = locate(surfaceType, compositeType, surfaceType2);
        if (blitLocate == null) {
            System.out.println("blit loop not found for:");
            System.out.println("src:  " + ((Object) surfaceType));
            System.out.println("comp: " + ((Object) compositeType));
            System.out.println("dst:  " + ((Object) surfaceType2));
        } else {
            blitcache.put(surfaceType, compositeType, surfaceType2, blitLocate);
        }
        return blitLocate;
    }

    protected Blit(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    public Blit(long j2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(j2, methodSignature, primTypeID, surfaceType, compositeType, surfaceType2);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        if (compositeType.isDerivedFrom(CompositeType.Xor)) {
            GeneralXorBlit generalXorBlit = new GeneralXorBlit(surfaceType, compositeType, surfaceType2);
            setupGeneralBinaryOp(generalXorBlit);
            return generalXorBlit;
        }
        if (compositeType.isDerivedFrom(CompositeType.AnyAlpha)) {
            return new GeneralMaskBlit(surfaceType, compositeType, surfaceType2);
        }
        return AnyBlit.instance;
    }

    /* loaded from: rt.jar:sun/java2d/loops/Blit$AnyBlit.class */
    private static class AnyBlit extends Blit {
        public static AnyBlit instance = new AnyBlit();

        public AnyBlit() {
            super(SurfaceType.Any, CompositeType.Any, SurfaceType.Any);
        }

        @Override // sun.java2d.loops.Blit
        public void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
            CompositeContext compositeContextCreateContext = composite.createContext(surfaceData.getColorModel(), surfaceData2.getColorModel(), new RenderingHints(null));
            Raster raster = surfaceData.getRaster(i2, i3, i6, i7);
            WritableRaster writableRaster = (WritableRaster) surfaceData2.getRaster(i4, i5, i6, i7);
            if (region == null) {
                region = Region.getInstanceXYWH(i4, i5, i6, i7);
            }
            int[] iArr = {i4, i5, i4 + i6, i5 + i7};
            SpanIterator spanIterator = region.getSpanIterator(iArr);
            int i8 = i2 - i4;
            int i9 = i3 - i5;
            while (spanIterator.nextSpan(iArr)) {
                int i10 = iArr[2] - iArr[0];
                int i11 = iArr[3] - iArr[1];
                Raster rasterCreateChild = raster.createChild(i8 + iArr[0], i9 + iArr[1], i10, i11, 0, 0, null);
                WritableRaster writableRasterCreateWritableChild = writableRaster.createWritableChild(iArr[0], iArr[1], i10, i11, 0, 0, null);
                compositeContextCreateContext.compose(rasterCreateChild, writableRasterCreateWritableChild, writableRasterCreateWritableChild);
            }
            compositeContextCreateContext.dispose();
        }
    }

    /* loaded from: rt.jar:sun/java2d/loops/Blit$GeneralMaskBlit.class */
    private static class GeneralMaskBlit extends Blit {
        MaskBlit performop;

        public GeneralMaskBlit(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
            super(surfaceType, compositeType, surfaceType2);
            this.performop = MaskBlit.locate(surfaceType, compositeType, surfaceType2);
        }

        @Override // sun.java2d.loops.Blit
        public void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
            this.performop.MaskBlit(surfaceData, surfaceData2, composite, region, i2, i3, i4, i5, i6, i7, null, 0, 0);
        }
    }

    /* loaded from: rt.jar:sun/java2d/loops/Blit$GeneralXorBlit.class */
    private static class GeneralXorBlit extends Blit implements GraphicsPrimitive.GeneralBinaryOp {
        Blit convertsrc;
        Blit convertdst;
        Blit performop;
        Blit convertresult;
        WeakReference srcTmp;
        WeakReference dstTmp;

        public GeneralXorBlit(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
            super(surfaceType, compositeType, surfaceType2);
        }

        @Override // sun.java2d.loops.GraphicsPrimitive.GeneralBinaryOp
        public void setPrimitives(Blit blit, Blit blit2, GraphicsPrimitive graphicsPrimitive, Blit blit3) {
            this.convertsrc = blit;
            this.convertdst = blit2;
            this.performop = (Blit) graphicsPrimitive;
            this.convertresult = blit3;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // sun.java2d.loops.Blit
        public synchronized void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
            SurfaceData surfaceDataConvertFrom;
            int i8;
            int i9;
            SurfaceData surfaceDataConvertFrom2;
            int i10;
            int i11;
            Region region2;
            if (this.convertsrc == null) {
                surfaceDataConvertFrom = surfaceData;
                i8 = i2;
                i9 = i3;
            } else {
                SurfaceData surfaceData3 = null;
                if (this.srcTmp != null) {
                    surfaceData3 = (SurfaceData) this.srcTmp.get();
                }
                surfaceDataConvertFrom = convertFrom(this.convertsrc, surfaceData, i2, i3, i6, i7, surfaceData3);
                i8 = 0;
                i9 = 0;
                if (surfaceDataConvertFrom != surfaceData3) {
                    this.srcTmp = new WeakReference(surfaceDataConvertFrom);
                }
            }
            if (this.convertdst == null) {
                surfaceDataConvertFrom2 = surfaceData2;
                i10 = i4;
                i11 = i5;
                region2 = region;
            } else {
                SurfaceData surfaceData4 = null;
                if (this.dstTmp != null) {
                    surfaceData4 = (SurfaceData) this.dstTmp.get();
                }
                surfaceDataConvertFrom2 = convertFrom(this.convertdst, surfaceData2, i4, i5, i6, i7, surfaceData4);
                i10 = 0;
                i11 = 0;
                region2 = null;
                if (surfaceDataConvertFrom2 != surfaceData4) {
                    this.dstTmp = new WeakReference(surfaceDataConvertFrom2);
                }
            }
            this.performop.Blit(surfaceDataConvertFrom, surfaceDataConvertFrom2, composite, region2, i8, i9, i10, i11, i6, i7);
            if (this.convertresult != null) {
                convertTo(this.convertresult, surfaceDataConvertFrom2, surfaceData2, region, i4, i5, i6, i7);
            }
        }
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return new TraceBlit(this);
    }

    /* loaded from: rt.jar:sun/java2d/loops/Blit$TraceBlit.class */
    private static class TraceBlit extends Blit {
        Blit target;

        public TraceBlit(Blit blit) {
            super(blit.getSourceType(), blit.getCompositeType(), blit.getDestType());
            this.target = blit;
        }

        @Override // sun.java2d.loops.Blit, sun.java2d.loops.GraphicsPrimitive
        public GraphicsPrimitive traceWrap() {
            return this;
        }

        @Override // sun.java2d.loops.Blit
        public void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
            tracePrimitive(this.target);
            this.target.Blit(surfaceData, surfaceData2, composite, region, i2, i3, i4, i5, i6, i7);
        }
    }
}
