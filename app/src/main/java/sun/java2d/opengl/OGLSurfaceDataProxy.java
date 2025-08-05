package sun.java2d.opengl;

import java.awt.Color;
import sun.java2d.SurfaceData;
import sun.java2d.SurfaceDataProxy;
import sun.java2d.loops.CompositeType;

/* loaded from: rt.jar:sun/java2d/opengl/OGLSurfaceDataProxy.class */
public class OGLSurfaceDataProxy extends SurfaceDataProxy {
    OGLGraphicsConfig oglgc;
    int transparency;

    public static SurfaceDataProxy createProxy(SurfaceData surfaceData, OGLGraphicsConfig oGLGraphicsConfig) {
        if (surfaceData instanceof OGLSurfaceData) {
            return UNCACHED;
        }
        return new OGLSurfaceDataProxy(oGLGraphicsConfig, surfaceData.getTransparency());
    }

    public OGLSurfaceDataProxy(OGLGraphicsConfig oGLGraphicsConfig, int i2) {
        this.oglgc = oGLGraphicsConfig;
        this.transparency = i2;
    }

    @Override // sun.java2d.SurfaceDataProxy
    public SurfaceData validateSurfaceData(SurfaceData surfaceData, SurfaceData surfaceData2, int i2, int i3) {
        if (surfaceData2 == null) {
            try {
                surfaceData2 = this.oglgc.createManagedSurface(i2, i3, this.transparency);
            } catch (OutOfMemoryError e2) {
                return null;
            }
        }
        return surfaceData2;
    }

    @Override // sun.java2d.SurfaceDataProxy
    public boolean isSupportedOperation(SurfaceData surfaceData, int i2, CompositeType compositeType, Color color) {
        return compositeType.isDerivedFrom(CompositeType.AnyAlpha) && (color == null || this.transparency == 1);
    }
}
