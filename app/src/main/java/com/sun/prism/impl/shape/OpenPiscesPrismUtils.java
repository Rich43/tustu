package com.sun.prism.impl.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.openpisces.Dasher;
import com.sun.openpisces.Renderer;
import com.sun.openpisces.Stroker;
import com.sun.openpisces.TransformingPathConsumer2D;
import com.sun.prism.BasicStroke;

/* loaded from: jfxrt.jar:com/sun/prism/impl/shape/OpenPiscesPrismUtils.class */
public class OpenPiscesPrismUtils {
    private static final Renderer savedAARenderer = new Renderer(3, 3);
    private static final Renderer savedRenderer = new Renderer(0, 0);
    private static final Stroker savedStroker = new Stroker(savedRenderer);
    private static final Dasher savedDasher = new Dasher(savedStroker);
    private static TransformingPathConsumer2D.FilterSet transformer = new TransformingPathConsumer2D.FilterSet();

    private static PathConsumer2D initRenderer(BasicStroke stroke, BaseTransform tx, Rectangle clip, int pirule, Renderer renderer) {
        int oprule = (stroke == null && pirule == 0) ? 0 : 1;
        renderer.reset(clip.f11913x, clip.f11914y, clip.width, clip.height, oprule);
        PathConsumer2D ret = transformer.getConsumer(renderer, tx);
        if (stroke != null) {
            savedStroker.reset(stroke.getLineWidth(), stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit());
            savedStroker.setConsumer(ret);
            ret = savedStroker;
            float[] dashes = stroke.getDashArray();
            if (dashes != null) {
                savedDasher.reset(dashes, stroke.getDashPhase());
                ret = savedDasher;
            }
        }
        return ret;
    }

    public static void feedConsumer(PathIterator pi, PathConsumer2D pc) {
        float[] coords = new float[6];
        while (!pi.isDone()) {
            int type = pi.currentSegment(coords);
            switch (type) {
                case 0:
                    pc.moveTo(coords[0], coords[1]);
                    break;
                case 1:
                    pc.lineTo(coords[0], coords[1]);
                    break;
                case 2:
                    pc.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                case 3:
                    pc.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case 4:
                    pc.closePath();
                    break;
            }
            pi.next();
        }
        pc.pathDone();
    }

    public static Renderer setupRenderer(Shape shape, BasicStroke stroke, BaseTransform xform, Rectangle rclip, boolean antialiasedShape) {
        PathIterator pi = shape.getPathIterator(null);
        Renderer r2 = antialiasedShape ? savedAARenderer : savedRenderer;
        feedConsumer(pi, initRenderer(stroke, xform, rclip, pi.getWindingRule(), r2));
        return r2;
    }

    public static Renderer setupRenderer(Path2D p2d, BasicStroke stroke, BaseTransform xform, Rectangle rclip, boolean antialiasedShape) {
        Renderer r2 = antialiasedShape ? savedAARenderer : savedRenderer;
        PathConsumer2D pc2d = initRenderer(stroke, xform, rclip, p2d.getWindingRule(), r2);
        float[] coords = p2d.getFloatCoordsNoClone();
        byte[] types = p2d.getCommandsNoClone();
        int nsegs = p2d.getNumCommands();
        int coff = 0;
        for (int i2 = 0; i2 < nsegs; i2++) {
            switch (types[i2]) {
                case 0:
                    pc2d.moveTo(coords[coff + 0], coords[coff + 1]);
                    coff += 2;
                    break;
                case 1:
                    pc2d.lineTo(coords[coff + 0], coords[coff + 1]);
                    coff += 2;
                    break;
                case 2:
                    pc2d.quadTo(coords[coff + 0], coords[coff + 1], coords[coff + 2], coords[coff + 3]);
                    coff += 4;
                    break;
                case 3:
                    pc2d.curveTo(coords[coff + 0], coords[coff + 1], coords[coff + 2], coords[coff + 3], coords[coff + 4], coords[coff + 5]);
                    coff += 6;
                    break;
                case 4:
                    pc2d.closePath();
                    break;
            }
        }
        pc2d.pathDone();
        return r2;
    }
}
