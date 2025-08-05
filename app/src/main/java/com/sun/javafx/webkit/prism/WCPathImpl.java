package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.webkit.graphics.WCPath;
import com.sun.webkit.graphics.WCPathIterator;
import com.sun.webkit.graphics.WCRectangle;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCPathImpl.class */
final class WCPathImpl extends WCPath<Path2D> {
    private final Path2D path;
    private boolean hasCP;
    private static final Logger log = Logger.getLogger(WCPathImpl.class.getName());

    WCPathImpl() {
        this.hasCP = false;
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Create empty WCPathImpl({0})", Integer.valueOf(getID()));
        }
        this.path = new Path2D();
    }

    WCPathImpl(WCPathImpl wcp) {
        this.hasCP = false;
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Create WCPathImpl({0}) from WCPathImpl({1})", new Object[]{Integer.valueOf(getID()), Integer.valueOf(wcp.getID())});
        }
        this.path = new Path2D(wcp.path);
        this.hasCP = wcp.hasCP;
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void addRect(double x2, double y2, double w2, double h2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addRect({1},{2},{3},{4})", new Object[]{Integer.valueOf(getID()), Double.valueOf(x2), Double.valueOf(y2), Double.valueOf(w2), Double.valueOf(h2)});
        }
        this.hasCP = true;
        this.path.append((Shape) new RoundRectangle2D((float) x2, (float) y2, (float) w2, (int) h2, 0.0f, 0.0f), false);
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void addEllipse(double x2, double y2, double w2, double h2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addEllipse({1},{2},{3},{4})", new Object[]{Integer.valueOf(getID()), Double.valueOf(x2), Double.valueOf(y2), Double.valueOf(w2), Double.valueOf(h2)});
        }
        this.hasCP = true;
        this.path.append((Shape) new Ellipse2D((float) x2, (float) y2, (float) w2, (float) h2), false);
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void addArcTo(double x1, double y1, double x2, double y2, double r2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addArcTo({1},{2},{3},{4})", new Object[]{Integer.valueOf(getID()), Double.valueOf(x1), Double.valueOf(y1), Double.valueOf(x2), Double.valueOf(y2)});
        }
        Arc2D arc = new Arc2D();
        arc.setArcByTangent(this.path.getCurrentPoint(), new Point2D((float) x1, (float) y1), new Point2D((float) x2, (float) y2), (float) r2);
        this.hasCP = true;
        this.path.append((Shape) arc, true);
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void addArc(double x2, double y2, double r2, double sa, double ea, boolean aclockwise) {
        float startAngle = (float) sa;
        float endAngle = (float) ea;
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addArc(x={1},y={2},r={3},sa=|{4}|,ea=|{5}|,aclock={6})", new Object[]{Integer.valueOf(getID()), Double.valueOf(x2), Double.valueOf(y2), Double.valueOf(r2), Float.valueOf(startAngle), Float.valueOf(endAngle), Boolean.valueOf(aclockwise)});
        }
        this.hasCP = true;
        float newEndAngle = endAngle;
        if (!aclockwise && startAngle > endAngle) {
            newEndAngle = startAngle + (6.2831855f - ((startAngle - endAngle) % 6.2831855f));
        } else if (aclockwise && startAngle < endAngle) {
            newEndAngle = startAngle - (6.2831855f - ((endAngle - startAngle) % 6.2831855f));
        }
        this.path.append((Shape) new Arc2D((float) (x2 - r2), (float) (y2 - r2), (float) (2.0d * r2), (float) (2.0d * r2), (float) Math.toDegrees(-startAngle), (float) Math.toDegrees(startAngle - newEndAngle), 0), true);
    }

    @Override // com.sun.webkit.graphics.WCPath
    public boolean contains(int rule, double x2, double y2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).contains({1},{2},{3})", new Object[]{Integer.valueOf(getID()), Integer.valueOf(rule), Double.valueOf(x2), Double.valueOf(y2)});
        }
        int savedRule = this.path.getWindingRule();
        this.path.setWindingRule(rule);
        boolean res = this.path.contains((float) x2, (float) y2);
        this.path.setWindingRule(savedRule);
        return res;
    }

    @Override // com.sun.webkit.graphics.WCPath
    public WCRectangle getBounds() {
        RectBounds b2 = this.path.getBounds();
        return new WCRectangle(b2.getMinX(), b2.getMinY(), b2.getWidth(), b2.getHeight());
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void clear() {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).clear()", Integer.valueOf(getID()));
        }
        this.hasCP = false;
        this.path.reset();
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void moveTo(double x2, double y2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).moveTo({1},{2})", new Object[]{Integer.valueOf(getID()), Double.valueOf(x2), Double.valueOf(y2)});
        }
        this.hasCP = true;
        this.path.moveTo((float) x2, (float) y2);
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void addLineTo(double x2, double y2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addLineTo({1},{2})", new Object[]{Integer.valueOf(getID()), Double.valueOf(x2), Double.valueOf(y2)});
        }
        this.hasCP = true;
        this.path.lineTo((float) x2, (float) y2);
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void addQuadCurveTo(double x0, double y0, double x1, double y1) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addQuadCurveTo({1},{2},{3},{4})", new Object[]{Integer.valueOf(getID()), Double.valueOf(x0), Double.valueOf(y0), Double.valueOf(x1), Double.valueOf(y1)});
        }
        this.hasCP = true;
        this.path.quadTo((float) x0, (float) y0, (float) x1, (float) y1);
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void addBezierCurveTo(double x0, double y0, double x1, double y1, double x2, double y2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addBezierCurveTo({1},{2},{3},{4},{5},{6})", new Object[]{Integer.valueOf(getID()), Double.valueOf(x0), Double.valueOf(y0), Double.valueOf(x1), Double.valueOf(y1), Double.valueOf(x2), Double.valueOf(y2)});
        }
        this.hasCP = true;
        this.path.curveTo((float) x0, (float) y0, (float) x1, (float) y1, (float) x2, (float) y2);
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void addPath(WCPath p2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).addPath({1})", new Object[]{Integer.valueOf(getID()), Integer.valueOf(p2.getID())});
        }
        this.hasCP = this.hasCP || ((WCPathImpl) p2).hasCP;
        this.path.append((Shape) ((WCPathImpl) p2).path, false);
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void closeSubpath() {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).closeSubpath()", Integer.valueOf(getID()));
        }
        this.path.closePath();
    }

    @Override // com.sun.webkit.graphics.WCPath
    public boolean isEmpty() {
        return !this.hasCP;
    }

    @Override // com.sun.webkit.graphics.WCPath
    public int getWindingRule() {
        return 1 - this.path.getWindingRule();
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void setWindingRule(int rule) {
        this.path.setWindingRule(1 - rule);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.webkit.graphics.WCPath
    public Path2D getPlatformPath() {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).getPath() BEGIN=====", Integer.valueOf(getID()));
            PathIterator pi = this.path.getPathIterator(null);
            float[] coords = new float[6];
            while (!pi.isDone()) {
                switch (pi.currentSegment(coords)) {
                    case 0:
                        log.log(Level.FINE, "SEG_MOVETO ({0},{1})", new Object[]{Float.valueOf(coords[0]), Float.valueOf(coords[1])});
                        break;
                    case 1:
                        log.log(Level.FINE, "SEG_LINETO ({0},{1})", new Object[]{Float.valueOf(coords[0]), Float.valueOf(coords[1])});
                        break;
                    case 2:
                        log.log(Level.FINE, "SEG_QUADTO ({0},{1},{2},{3})", new Object[]{Float.valueOf(coords[0]), Float.valueOf(coords[1]), Float.valueOf(coords[2]), Float.valueOf(coords[3])});
                        break;
                    case 3:
                        log.log(Level.FINE, "SEG_CUBICTO ({0},{1},{2},{3},{4},{5})", new Object[]{Float.valueOf(coords[0]), Float.valueOf(coords[1]), Float.valueOf(coords[2]), Float.valueOf(coords[3]), Float.valueOf(coords[4]), Float.valueOf(coords[5])});
                        break;
                    case 4:
                        log.fine("SEG_CLOSE");
                        break;
                }
                pi.next();
            }
            log.fine("========getPath() END=====");
        }
        return this.path;
    }

    @Override // com.sun.webkit.graphics.WCPath
    public WCPathIterator getPathIterator() {
        final PathIterator pi = this.path.getPathIterator(null);
        return new WCPathIterator() { // from class: com.sun.javafx.webkit.prism.WCPathImpl.1
            @Override // com.sun.webkit.graphics.WCPathIterator
            public int getWindingRule() {
                return pi.getWindingRule();
            }

            @Override // com.sun.webkit.graphics.WCPathIterator
            public boolean isDone() {
                return pi.isDone();
            }

            @Override // com.sun.webkit.graphics.WCPathIterator
            public void next() {
                pi.next();
            }

            @Override // com.sun.webkit.graphics.WCPathIterator
            public int currentSegment(double[] coords) {
                float[] _coords = new float[6];
                int segmentType = pi.currentSegment(_coords);
                for (int i2 = 0; i2 < coords.length; i2++) {
                    coords[i2] = _coords[i2];
                }
                return segmentType;
            }
        };
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void translate(double x2, double y2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).translate({1}, {2})", new Object[]{Integer.valueOf(getID()), Double.valueOf(x2), Double.valueOf(y2)});
        }
        this.path.transform(BaseTransform.getTranslateInstance(x2, y2));
    }

    @Override // com.sun.webkit.graphics.WCPath
    public void transform(double mxx, double myx, double mxy, double myy, double mxt, double myt) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).transform({1},{2},{3},{4},{5},{6})", new Object[]{Integer.valueOf(getID()), Double.valueOf(mxx), Double.valueOf(myx), Double.valueOf(mxy), Double.valueOf(myy), Double.valueOf(mxt), Double.valueOf(myt)});
        }
        this.path.transform(BaseTransform.getInstance(mxx, myx, mxy, myy, mxt, myt));
    }

    @Override // com.sun.webkit.graphics.WCPath
    public boolean strokeContains(double x2, double y2, double thickness, double miterLimit, int cap, int join, double dashOffset, double[] dashArray) {
        BasicStroke stroke = new BasicStroke((float) thickness, cap, join, (float) miterLimit);
        if (dashArray.length > 0) {
            stroke.set(dashArray, (float) dashOffset);
        }
        boolean result = stroke.createCenteredStrokedShape(this.path).contains((float) x2, (float) y2);
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "WCPathImpl({0}).strokeContains({1},{2},{3},{4},{5},{6},{7},{8}) = {9}", new Object[]{Integer.valueOf(getID()), Double.valueOf(x2), Double.valueOf(y2), Double.valueOf(thickness), Double.valueOf(miterLimit), Integer.valueOf(cap), Integer.valueOf(join), Double.valueOf(dashOffset), Arrays.toString(dashArray), Boolean.valueOf(result)});
        }
        return result;
    }
}
