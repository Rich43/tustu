package sun.java2d.loops;

import java.awt.Rectangle;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/java2d/loops/CustomComponent.class */
public final class CustomComponent {
    public static void register() {
        GraphicsPrimitiveMgr.register(new GraphicsPrimitive[]{new GraphicsPrimitiveProxy(CustomComponent.class, "OpaqueCopyAnyToArgb", Blit.methodSignature, Blit.primTypeID, SurfaceType.Any, CompositeType.SrcNoEa, SurfaceType.IntArgb), new GraphicsPrimitiveProxy(CustomComponent.class, "OpaqueCopyArgbToAny", Blit.methodSignature, Blit.primTypeID, SurfaceType.IntArgb, CompositeType.SrcNoEa, SurfaceType.Any), new GraphicsPrimitiveProxy(CustomComponent.class, "XorCopyArgbToAny", Blit.methodSignature, Blit.primTypeID, SurfaceType.IntArgb, CompositeType.Xor, SurfaceType.Any)});
    }

    public static Region getRegionOfInterest(SurfaceData surfaceData, SurfaceData surfaceData2, Region region, int i2, int i3, int i4, int i5, int i6, int i7) {
        Region intersection = Region.getInstanceXYWH(i4, i5, i6, i7).getIntersection(surfaceData2.getBounds());
        Rectangle bounds = surfaceData.getBounds();
        bounds.translate(i4 - i2, i5 - i3);
        Region intersection2 = intersection.getIntersection(bounds);
        if (region != null) {
            intersection2 = intersection2.getIntersection(region);
        }
        return intersection2;
    }
}
