package sun.java2d.d3d;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.GraphicsPrimitiveMgr;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.BufferedMaskBlit;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/d3d/D3DMaskBlit.class */
class D3DMaskBlit extends BufferedMaskBlit {
    static void register() {
        GraphicsPrimitiveMgr.register(new GraphicsPrimitive[]{new D3DMaskBlit(SurfaceType.IntArgb, CompositeType.SrcOver), new D3DMaskBlit(SurfaceType.IntArgbPre, CompositeType.SrcOver), new D3DMaskBlit(SurfaceType.IntRgb, CompositeType.SrcOver), new D3DMaskBlit(SurfaceType.IntRgb, CompositeType.SrcNoEa), new D3DMaskBlit(SurfaceType.IntBgr, CompositeType.SrcOver), new D3DMaskBlit(SurfaceType.IntBgr, CompositeType.SrcNoEa)});
    }

    private D3DMaskBlit(SurfaceType surfaceType, CompositeType compositeType) {
        super(D3DRenderQueue.getInstance(), surfaceType, compositeType, D3DSurfaceData.D3DSurface);
    }

    @Override // sun.java2d.pipe.BufferedMaskBlit
    protected void validateContext(SurfaceData surfaceData, Composite composite, Region region) {
        D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) surfaceData;
        D3DContext.validateContext(d3DSurfaceData, d3DSurfaceData, region, composite, null, null, null, 0);
    }
}
