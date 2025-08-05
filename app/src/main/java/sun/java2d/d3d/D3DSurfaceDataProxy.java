package sun.java2d.d3d;

import java.awt.Color;
import sun.java2d.InvalidPipeException;
import sun.java2d.SurfaceData;
import sun.java2d.SurfaceDataProxy;
import sun.java2d.loops.CompositeType;

/* loaded from: rt.jar:sun/java2d/d3d/D3DSurfaceDataProxy.class */
public class D3DSurfaceDataProxy extends SurfaceDataProxy {
    D3DGraphicsConfig d3dgc;
    int transparency;

    public static SurfaceDataProxy createProxy(SurfaceData surfaceData, D3DGraphicsConfig d3DGraphicsConfig) {
        if (surfaceData instanceof D3DSurfaceData) {
            return UNCACHED;
        }
        return new D3DSurfaceDataProxy(d3DGraphicsConfig, surfaceData.getTransparency());
    }

    public D3DSurfaceDataProxy(D3DGraphicsConfig d3DGraphicsConfig, int i2) {
        this.d3dgc = d3DGraphicsConfig;
        this.transparency = i2;
        activateDisplayListener();
    }

    @Override // sun.java2d.SurfaceDataProxy
    public SurfaceData validateSurfaceData(SurfaceData surfaceData, SurfaceData surfaceData2, int i2, int i3) {
        if (surfaceData2 == null || surfaceData2.isSurfaceLost()) {
            try {
                surfaceData2 = this.d3dgc.createManagedSurface(i2, i3, this.transparency);
            } catch (InvalidPipeException e2) {
                this.d3dgc.getD3DDevice();
                if (!D3DGraphicsDevice.isD3DAvailable()) {
                    invalidate();
                    flush();
                    return null;
                }
            }
        }
        return surfaceData2;
    }

    @Override // sun.java2d.SurfaceDataProxy
    public boolean isSupportedOperation(SurfaceData surfaceData, int i2, CompositeType compositeType, Color color) {
        return color == null || this.transparency == 1;
    }
}
