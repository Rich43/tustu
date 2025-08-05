package sun.java2d.windows;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.GraphicsPrimitiveMgr;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/windows/GDIBlitLoops.class */
public class GDIBlitLoops extends Blit {
    int rmask;
    int gmask;
    int bmask;
    boolean indexed;

    public native void nativeBlit(SurfaceData surfaceData, SurfaceData surfaceData2, Region region, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, boolean z2);

    public static void register() {
        GraphicsPrimitiveMgr.register(new GraphicsPrimitive[]{new GDIBlitLoops(SurfaceType.IntRgb, GDIWindowSurfaceData.AnyGdi), new GDIBlitLoops(SurfaceType.Ushort555Rgb, GDIWindowSurfaceData.AnyGdi, 31744, 992, 31), new GDIBlitLoops(SurfaceType.Ushort565Rgb, GDIWindowSurfaceData.AnyGdi, 63488, 2016, 31), new GDIBlitLoops(SurfaceType.ThreeByteBgr, GDIWindowSurfaceData.AnyGdi), new GDIBlitLoops(SurfaceType.ByteIndexedOpaque, GDIWindowSurfaceData.AnyGdi, true), new GDIBlitLoops(SurfaceType.Index8Gray, GDIWindowSurfaceData.AnyGdi, true), new GDIBlitLoops(SurfaceType.ByteGray, GDIWindowSurfaceData.AnyGdi)});
    }

    public GDIBlitLoops(SurfaceType surfaceType, SurfaceType surfaceType2) {
        this(surfaceType, surfaceType2, 0, 0, 0);
    }

    public GDIBlitLoops(SurfaceType surfaceType, SurfaceType surfaceType2, boolean z2) {
        this(surfaceType, surfaceType2, 0, 0, 0);
        this.indexed = z2;
    }

    public GDIBlitLoops(SurfaceType surfaceType, SurfaceType surfaceType2, int i2, int i3, int i4) {
        super(surfaceType, CompositeType.SrcNoEa, surfaceType2);
        this.indexed = false;
        this.rmask = i2;
        this.gmask = i3;
        this.bmask = i4;
    }

    @Override // sun.java2d.loops.Blit
    public void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        nativeBlit(surfaceData, surfaceData2, region, i2, i3, i4, i5, i6, i7, this.rmask, this.gmask, this.bmask, this.indexed);
    }
}
