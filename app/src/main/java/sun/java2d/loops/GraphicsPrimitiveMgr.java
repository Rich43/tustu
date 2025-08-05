package sun.java2d.loops;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Arrays;
import java.util.Comparator;
import sun.awt.SunHints;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/loops/GraphicsPrimitiveMgr.class */
public final class GraphicsPrimitiveMgr {
    private static final boolean debugTrace = false;
    private static GraphicsPrimitive[] primitives;
    private static GraphicsPrimitive[] generalPrimitives;
    private static boolean needssort = true;
    private static Comparator primSorter;
    private static Comparator primFinder;

    private static native void initIDs(Class cls, Class cls2, Class cls3, Class cls4, Class cls5, Class cls6, Class cls7, Class cls8, Class cls9, Class cls10, Class cls11);

    private static native void registerNativeLoops();

    static {
        initIDs(GraphicsPrimitive.class, SurfaceType.class, CompositeType.class, SunGraphics2D.class, Color.class, AffineTransform.class, XORComposite.class, AlphaComposite.class, Path2D.class, Path2D.Float.class, SunHints.class);
        CustomComponent.register();
        GeneralRenderer.register();
        registerNativeLoops();
        primSorter = new Comparator() { // from class: sun.java2d.loops.GraphicsPrimitiveMgr.1
            @Override // java.util.Comparator
            public int compare(Object obj, Object obj2) {
                int uniqueID = ((GraphicsPrimitive) obj).getUniqueID();
                int uniqueID2 = ((GraphicsPrimitive) obj2).getUniqueID();
                if (uniqueID == uniqueID2) {
                    return 0;
                }
                return uniqueID < uniqueID2 ? -1 : 1;
            }
        };
        primFinder = new Comparator() { // from class: sun.java2d.loops.GraphicsPrimitiveMgr.2
            @Override // java.util.Comparator
            public int compare(Object obj, Object obj2) {
                int uniqueID = ((GraphicsPrimitive) obj).getUniqueID();
                int i2 = ((PrimitiveSpec) obj2).uniqueID;
                if (uniqueID == i2) {
                    return 0;
                }
                return uniqueID < i2 ? -1 : 1;
            }
        };
    }

    /* loaded from: rt.jar:sun/java2d/loops/GraphicsPrimitiveMgr$PrimitiveSpec.class */
    private static class PrimitiveSpec {
        public int uniqueID;

        private PrimitiveSpec() {
        }
    }

    private GraphicsPrimitiveMgr() {
    }

    public static synchronized void register(GraphicsPrimitive[] graphicsPrimitiveArr) {
        GraphicsPrimitive[] graphicsPrimitiveArr2 = primitives;
        int length = 0;
        int length2 = graphicsPrimitiveArr.length;
        if (graphicsPrimitiveArr2 != null) {
            length = graphicsPrimitiveArr2.length;
        }
        GraphicsPrimitive[] graphicsPrimitiveArr3 = new GraphicsPrimitive[length + length2];
        if (graphicsPrimitiveArr2 != null) {
            System.arraycopy(graphicsPrimitiveArr2, 0, graphicsPrimitiveArr3, 0, length);
        }
        System.arraycopy(graphicsPrimitiveArr, 0, graphicsPrimitiveArr3, length, length2);
        needssort = true;
        primitives = graphicsPrimitiveArr3;
    }

    public static synchronized void registerGeneral(GraphicsPrimitive graphicsPrimitive) {
        if (generalPrimitives == null) {
            generalPrimitives = new GraphicsPrimitive[]{graphicsPrimitive};
            return;
        }
        int length = generalPrimitives.length;
        GraphicsPrimitive[] graphicsPrimitiveArr = new GraphicsPrimitive[length + 1];
        System.arraycopy(generalPrimitives, 0, graphicsPrimitiveArr, 0, length);
        graphicsPrimitiveArr[length] = graphicsPrimitive;
        generalPrimitives = graphicsPrimitiveArr;
    }

    public static synchronized GraphicsPrimitive locate(int i2, SurfaceType surfaceType) {
        return locate(i2, SurfaceType.OpaqueColor, CompositeType.Src, surfaceType);
    }

    public static synchronized GraphicsPrimitive locate(int i2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        GraphicsPrimitive graphicsPrimitiveLocatePrim = locatePrim(i2, surfaceType, compositeType, surfaceType2);
        if (graphicsPrimitiveLocatePrim == null) {
            graphicsPrimitiveLocatePrim = locateGeneral(i2);
            if (graphicsPrimitiveLocatePrim != null) {
                graphicsPrimitiveLocatePrim = graphicsPrimitiveLocatePrim.makePrimitive(surfaceType, compositeType, surfaceType2);
                if (graphicsPrimitiveLocatePrim != null && GraphicsPrimitive.traceflags != 0) {
                    graphicsPrimitiveLocatePrim = graphicsPrimitiveLocatePrim.traceWrap();
                }
            }
        }
        return graphicsPrimitiveLocatePrim;
    }

    public static synchronized GraphicsPrimitive locatePrim(int i2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        PrimitiveSpec primitiveSpec = new PrimitiveSpec();
        SurfaceType superType = surfaceType2;
        while (true) {
            SurfaceType surfaceType3 = superType;
            if (surfaceType3 != null) {
                SurfaceType superType2 = surfaceType;
                while (true) {
                    SurfaceType surfaceType4 = superType2;
                    if (surfaceType4 != null) {
                        CompositeType superType3 = compositeType;
                        while (true) {
                            CompositeType compositeType2 = superType3;
                            if (compositeType2 != null) {
                                primitiveSpec.uniqueID = GraphicsPrimitive.makeUniqueID(i2, surfaceType4, compositeType2, surfaceType3);
                                GraphicsPrimitive graphicsPrimitiveLocate = locate(primitiveSpec);
                                if (graphicsPrimitiveLocate == null) {
                                    superType3 = compositeType2.getSuperType();
                                } else {
                                    return graphicsPrimitiveLocate;
                                }
                            }
                        }
                    }
                    superType2 = surfaceType4.getSuperType();
                }
            } else {
                return null;
            }
            superType = surfaceType3.getSuperType();
        }
    }

    private static GraphicsPrimitive locateGeneral(int i2) {
        if (generalPrimitives == null) {
            return null;
        }
        for (int i3 = 0; i3 < generalPrimitives.length; i3++) {
            GraphicsPrimitive graphicsPrimitive = generalPrimitives[i3];
            if (graphicsPrimitive.getPrimTypeID() == i2) {
                return graphicsPrimitive;
            }
        }
        return null;
    }

    private static GraphicsPrimitive locate(PrimitiveSpec primitiveSpec) {
        int iBinarySearch;
        if (needssort) {
            if (GraphicsPrimitive.traceflags != 0) {
                for (int i2 = 0; i2 < primitives.length; i2++) {
                    primitives[i2] = primitives[i2].traceWrap();
                }
            }
            Arrays.sort(primitives, primSorter);
            needssort = false;
        }
        GraphicsPrimitive[] graphicsPrimitiveArr = primitives;
        if (graphicsPrimitiveArr != null && (iBinarySearch = Arrays.binarySearch(graphicsPrimitiveArr, primitiveSpec, primFinder)) >= 0) {
            GraphicsPrimitive graphicsPrimitiveInstantiate = graphicsPrimitiveArr[iBinarySearch];
            if (graphicsPrimitiveInstantiate instanceof GraphicsPrimitiveProxy) {
                graphicsPrimitiveInstantiate = ((GraphicsPrimitiveProxy) graphicsPrimitiveInstantiate).instantiate();
                graphicsPrimitiveArr[iBinarySearch] = graphicsPrimitiveInstantiate;
            }
            return graphicsPrimitiveInstantiate;
        }
        return null;
    }

    private static void writeLog(String str) {
    }

    public static void testPrimitiveInstantiation() {
        testPrimitiveInstantiation(false);
    }

    public static void testPrimitiveInstantiation(boolean z2) {
        int i2 = 0;
        int i3 = 0;
        for (GraphicsPrimitive graphicsPrimitive : primitives) {
            if (graphicsPrimitive instanceof GraphicsPrimitiveProxy) {
                GraphicsPrimitive graphicsPrimitiveInstantiate = ((GraphicsPrimitiveProxy) graphicsPrimitive).instantiate();
                if (!graphicsPrimitiveInstantiate.getSignature().equals(graphicsPrimitive.getSignature()) || graphicsPrimitiveInstantiate.getUniqueID() != graphicsPrimitive.getUniqueID()) {
                    System.out.println("r.getSignature == " + graphicsPrimitiveInstantiate.getSignature());
                    System.out.println("r.getUniqueID == " + graphicsPrimitiveInstantiate.getUniqueID());
                    System.out.println("p.getSignature == " + graphicsPrimitive.getSignature());
                    System.out.println("p.getUniqueID == " + graphicsPrimitive.getUniqueID());
                    throw new RuntimeException("Primitive " + ((Object) graphicsPrimitive) + " returns wrong signature for " + ((Object) graphicsPrimitiveInstantiate.getClass()));
                }
                i3++;
                if (z2) {
                    System.out.println(graphicsPrimitiveInstantiate);
                }
            } else {
                if (z2) {
                    System.out.println(((Object) graphicsPrimitive) + " (not proxied).");
                }
                i2++;
            }
        }
        System.out.println(i2 + " graphics primitives were not proxied.");
        System.out.println(i3 + " proxied graphics primitives resolved correctly.");
        System.out.println((i2 + i3) + " total graphics primitives");
    }

    public static void main(String[] strArr) {
        if (needssort) {
            Arrays.sort(primitives, primSorter);
            needssort = false;
        }
        testPrimitiveInstantiation(strArr.length > 0);
    }
}
