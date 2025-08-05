package sun.java2d.loops;

/* loaded from: rt.jar:sun/java2d/loops/GraphicsPrimitiveProxy.class */
public class GraphicsPrimitiveProxy extends GraphicsPrimitive {
    private Class owner;
    private String relativeClassName;

    public GraphicsPrimitiveProxy(Class cls, String str, String str2, int i2, SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        super(str2, i2, surfaceType, compositeType, surfaceType2);
        this.owner = cls;
        this.relativeClassName = str;
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive makePrimitive(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        throw new InternalError("makePrimitive called on a Proxy!");
    }

    GraphicsPrimitive instantiate() {
        String str = getPackageName(this.owner.getName()) + "." + this.relativeClassName;
        try {
            GraphicsPrimitive graphicsPrimitive = (GraphicsPrimitive) Class.forName(str).newInstance();
            if (!satisfiesSameAs(graphicsPrimitive)) {
                throw new RuntimeException("Primitive " + ((Object) graphicsPrimitive) + " incompatible with proxy for " + str);
            }
            return graphicsPrimitive;
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException(e2.toString());
        } catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.toString());
        } catch (InstantiationException e4) {
            throw new RuntimeException(e4.toString());
        }
    }

    private static String getPackageName(String str) {
        int iLastIndexOf = str.lastIndexOf(46);
        if (iLastIndexOf < 0) {
            return str;
        }
        return str.substring(0, iLastIndexOf);
    }

    @Override // sun.java2d.loops.GraphicsPrimitive
    public GraphicsPrimitive traceWrap() {
        return instantiate().traceWrap();
    }
}
