package sun.java2d.d3d;

import java.awt.Composite;
import sun.java2d.InvalidPipeException;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.GraphicsPrimitive;
import sun.java2d.loops.GraphicsPrimitiveMgr;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.BufferedMaskFill;

/* loaded from: rt.jar:sun/java2d/d3d/D3DMaskFill.class */
class D3DMaskFill extends BufferedMaskFill {
    @Override // sun.java2d.pipe.BufferedMaskFill
    protected native void maskFill(int i2, int i3, int i4, int i5, int i6, int i7, int i8, byte[] bArr);

    static void register() {
        GraphicsPrimitiveMgr.register(new GraphicsPrimitive[]{new D3DMaskFill(SurfaceType.AnyColor, CompositeType.SrcOver), new D3DMaskFill(SurfaceType.OpaqueColor, CompositeType.SrcNoEa), new D3DMaskFill(SurfaceType.GradientPaint, CompositeType.SrcOver), new D3DMaskFill(SurfaceType.OpaqueGradientPaint, CompositeType.SrcNoEa), new D3DMaskFill(SurfaceType.LinearGradientPaint, CompositeType.SrcOver), new D3DMaskFill(SurfaceType.OpaqueLinearGradientPaint, CompositeType.SrcNoEa), new D3DMaskFill(SurfaceType.RadialGradientPaint, CompositeType.SrcOver), new D3DMaskFill(SurfaceType.OpaqueRadialGradientPaint, CompositeType.SrcNoEa), new D3DMaskFill(SurfaceType.TexturePaint, CompositeType.SrcOver), new D3DMaskFill(SurfaceType.OpaqueTexturePaint, CompositeType.SrcNoEa)});
    }

    protected D3DMaskFill(SurfaceType surfaceType, CompositeType compositeType) {
        super(D3DRenderQueue.getInstance(), surfaceType, compositeType, D3DSurfaceData.D3DSurface);
    }

    @Override // sun.java2d.pipe.BufferedMaskFill
    protected void validateContext(SunGraphics2D sunGraphics2D, Composite composite, int i2) {
        try {
            D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) sunGraphics2D.surfaceData;
            D3DContext.validateContext(d3DSurfaceData, d3DSurfaceData, sunGraphics2D.getCompClip(), composite, null, sunGraphics2D.paint, sunGraphics2D, i2);
        } catch (ClassCastException e2) {
            throw new InvalidPipeException("wrong surface data type: " + ((Object) sunGraphics2D.surfaceData));
        }
    }
}
