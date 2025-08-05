package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.scene.shape.FillRule;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGPath.class */
public class NGPath extends NGShape {

    /* renamed from: p, reason: collision with root package name */
    private Path2D f11958p = new Path2D();

    public void reset() {
        this.f11958p.reset();
    }

    public void update() {
        geometryChanged();
    }

    private int toWindingRule(FillRule rule) {
        if (rule == FillRule.NON_ZERO) {
            return 1;
        }
        return 0;
    }

    public void setFillRule(FillRule fillRule) {
        this.f11958p.setWindingRule(toWindingRule(fillRule));
    }

    public float getCurrentX() {
        return this.f11958p.getCurrentPoint().f11907x;
    }

    public float getCurrentY() {
        return this.f11958p.getCurrentPoint().f11908y;
    }

    public void addClosePath() {
        this.f11958p.closePath();
    }

    public void addMoveTo(float x2, float y2) {
        this.f11958p.moveTo(x2, y2);
    }

    public void addLineTo(float x2, float y2) {
        this.f11958p.lineTo(x2, y2);
    }

    public void addQuadTo(float ctrlx, float ctrly, float x2, float y2) {
        this.f11958p.quadTo(ctrlx, ctrly, x2, y2);
    }

    public void addCubicTo(float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
        this.f11958p.curveTo(ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
    }

    public void addArcTo(float arcX, float arcY, float arcW, float arcH, float arcStart, float arcExtent, float xAxisRotation) {
        Arc2D arc = new Arc2D(arcX, arcY, arcW, arcH, arcStart, arcExtent, 0);
        BaseTransform xform = ((double) xAxisRotation) == 0.0d ? null : BaseTransform.getRotateInstance(xAxisRotation, arc.getCenterX(), arc.getCenterY());
        PathIterator pi = arc.getPathIterator(xform);
        pi.next();
        this.f11958p.append(pi, true);
    }

    public Path2D getGeometry() {
        return this.f11958p;
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    public Shape getShape() {
        return this.f11958p;
    }

    public boolean acceptsPath2dOnUpdate() {
        return true;
    }

    public void updateWithPath2d(Path2D path) {
        this.f11958p.setTo(path);
        geometryChanged();
    }
}
