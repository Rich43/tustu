package sun.java2d.opengl;

import java.awt.Composite;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.Region;

/* compiled from: OGLBlitLoops.java */
/* loaded from: rt.jar:sun/java2d/opengl/OGLSwToTextureBlit.class */
class OGLSwToTextureBlit extends Blit {
    private int typeval;

    OGLSwToTextureBlit(SurfaceType surfaceType, int i2) {
        super(surfaceType, CompositeType.SrcNoEa, OGLSurfaceData.OpenGLTexture);
        this.typeval = i2;
    }

    @Override // sun.java2d.loops.Blit
    public void Blit(SurfaceData surfaceData, SurfaceData surfaceData2, Composite composite, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        OGLBlitLoops.Blit(surfaceData, surfaceData2, composite, region, null, 1, i2, i3, i2 + i6, i3 + i7, i4, i5, i4 + i6, i5 + i7, this.typeval, true);
    }
}
