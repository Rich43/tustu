package sun.java2d.pipe;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceLoader;
import sun.awt.geom.PathConsumer2D;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/java2d/pipe/RenderingEngine.class */
public abstract class RenderingEngine {
    private static RenderingEngine reImpl;

    public abstract Shape createStrokedShape(Shape shape, float f2, int i2, int i3, float f3, float[] fArr, float f4);

    public abstract void strokeTo(Shape shape, AffineTransform affineTransform, BasicStroke basicStroke, boolean z2, boolean z3, boolean z4, PathConsumer2D pathConsumer2D);

    public abstract AATileGenerator getAATileGenerator(Shape shape, AffineTransform affineTransform, Region region, BasicStroke basicStroke, boolean z2, boolean z3, int[] iArr);

    public abstract AATileGenerator getAATileGenerator(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, Region region, int[] iArr);

    public abstract float getMinimumAAPenSize();

    public static synchronized RenderingEngine getInstance() {
        if (reImpl != null) {
            return reImpl;
        }
        reImpl = (RenderingEngine) AccessController.doPrivileged(new PrivilegedAction<RenderingEngine>() { // from class: sun.java2d.pipe.RenderingEngine.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public RenderingEngine run2() {
                String property = System.getProperty("sun.java2d.renderer", "sun.dc.DuctusRenderingEngine");
                if (property.equals("sun.dc.DuctusRenderingEngine")) {
                    try {
                        return (RenderingEngine) Class.forName("sun.dc.DuctusRenderingEngine").newInstance();
                    } catch (ReflectiveOperationException e2) {
                    }
                }
                RenderingEngine renderingEngine = null;
                Iterator it = ServiceLoader.loadInstalled(RenderingEngine.class).iterator();
                while (it.hasNext()) {
                    RenderingEngine renderingEngine2 = (RenderingEngine) it.next();
                    renderingEngine = renderingEngine2;
                    if (renderingEngine2.getClass().getName().equals(property)) {
                        break;
                    }
                }
                return renderingEngine;
            }
        });
        if (reImpl == null) {
            throw new InternalError("No RenderingEngine module found");
        }
        if (((String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.renderer.trace"))) != null) {
            reImpl = new Tracer(reImpl);
        }
        return reImpl;
    }

    public static void feedConsumer(PathIterator pathIterator, PathConsumer2D pathConsumer2D) {
        float[] fArr = new float[6];
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(fArr)) {
                case 0:
                    pathConsumer2D.moveTo(fArr[0], fArr[1]);
                    break;
                case 1:
                    pathConsumer2D.lineTo(fArr[0], fArr[1]);
                    break;
                case 2:
                    pathConsumer2D.quadTo(fArr[0], fArr[1], fArr[2], fArr[3]);
                    break;
                case 3:
                    pathConsumer2D.curveTo(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5]);
                    break;
                case 4:
                    pathConsumer2D.closePath();
                    break;
            }
            pathIterator.next();
        }
    }

    /* loaded from: rt.jar:sun/java2d/pipe/RenderingEngine$Tracer.class */
    static class Tracer extends RenderingEngine {
        RenderingEngine target;
        String name;

        public Tracer(RenderingEngine renderingEngine) {
            this.target = renderingEngine;
            this.name = renderingEngine.getClass().getName();
        }

        @Override // sun.java2d.pipe.RenderingEngine
        public Shape createStrokedShape(Shape shape, float f2, int i2, int i3, float f3, float[] fArr, float f4) {
            System.out.println(this.name + ".createStrokedShape(" + shape.getClass().getName() + ", width = " + f2 + ", caps = " + i2 + ", join = " + i3 + ", miter = " + f3 + ", dashes = " + ((Object) fArr) + ", dashphase = " + f4 + ")");
            return this.target.createStrokedShape(shape, f2, i2, i3, f3, fArr, f4);
        }

        @Override // sun.java2d.pipe.RenderingEngine
        public void strokeTo(Shape shape, AffineTransform affineTransform, BasicStroke basicStroke, boolean z2, boolean z3, boolean z4, PathConsumer2D pathConsumer2D) {
            System.out.println(this.name + ".strokeTo(" + shape.getClass().getName() + ", " + ((Object) affineTransform) + ", " + ((Object) basicStroke) + ", " + (z2 ? "thin" : "wide") + ", " + (z3 ? "normalized" : "pure") + ", " + (z4 ? "AA" : "non-AA") + ", " + pathConsumer2D.getClass().getName() + ")");
            this.target.strokeTo(shape, affineTransform, basicStroke, z2, z3, z4, pathConsumer2D);
        }

        @Override // sun.java2d.pipe.RenderingEngine
        public float getMinimumAAPenSize() {
            System.out.println(this.name + ".getMinimumAAPenSize()");
            return this.target.getMinimumAAPenSize();
        }

        @Override // sun.java2d.pipe.RenderingEngine
        public AATileGenerator getAATileGenerator(Shape shape, AffineTransform affineTransform, Region region, BasicStroke basicStroke, boolean z2, boolean z3, int[] iArr) {
            System.out.println(this.name + ".getAATileGenerator(" + shape.getClass().getName() + ", " + ((Object) affineTransform) + ", " + ((Object) region) + ", " + ((Object) basicStroke) + ", " + (z2 ? "thin" : "wide") + ", " + (z3 ? "normalized" : "pure") + ")");
            return this.target.getAATileGenerator(shape, affineTransform, region, basicStroke, z2, z3, iArr);
        }

        @Override // sun.java2d.pipe.RenderingEngine
        public AATileGenerator getAATileGenerator(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, Region region, int[] iArr) {
            System.out.println(this.name + ".getAATileGenerator(" + d2 + ", " + d3 + ", " + d4 + ", " + d5 + ", " + d6 + ", " + d7 + ", " + d8 + ", " + d9 + ", " + ((Object) region) + ")");
            return this.target.getAATileGenerator(d2, d3, d4, d5, d6, d7, d8, d9, region, iArr);
        }
    }
}
