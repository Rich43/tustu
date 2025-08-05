package com.sun.prism;

import com.sun.javafx.geom.Area;
import com.sun.javafx.geom.GeneralShapePair;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.openpisces.Dasher;
import com.sun.openpisces.Stroker;
import com.sun.prism.impl.shape.OpenPiscesPrismUtils;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/prism/BasicStroke.class */
public final class BasicStroke {
    public static final int CAP_BUTT = 0;
    public static final int CAP_ROUND = 1;
    public static final int CAP_SQUARE = 2;
    public static final int JOIN_BEVEL = 2;
    public static final int JOIN_MITER = 0;
    public static final int JOIN_ROUND = 1;
    public static final int TYPE_CENTERED = 0;
    public static final int TYPE_INNER = 1;
    public static final int TYPE_OUTER = 2;
    float width;
    int type;
    int cap;
    int join;
    float miterLimit;
    float[] dash;
    float dashPhase;
    private static final int SAFE_ACCUMULATE_MASK = 91;
    private float[] tmpMiter;
    static final float SQRT_2 = (float) Math.sqrt(2.0d);

    public BasicStroke() {
        this.tmpMiter = new float[2];
        set(0, 1.0f, 2, 0, 10.0f);
    }

    public BasicStroke(float width, int cap, int join, float miterLimit) {
        this.tmpMiter = new float[2];
        set(0, width, cap, join, miterLimit);
    }

    public BasicStroke(int type, float width, int cap, int join, float miterLimit) {
        this.tmpMiter = new float[2];
        set(type, width, cap, join, miterLimit);
    }

    public BasicStroke(float width, int cap, int join, float miterLimit, float[] dash, float dashPhase) {
        this.tmpMiter = new float[2];
        set(0, width, cap, join, miterLimit);
        set(dash, dashPhase);
    }

    public BasicStroke(float width, int cap, int join, float miterLimit, double[] dash, float dashPhase) {
        this.tmpMiter = new float[2];
        set(0, width, cap, join, miterLimit);
        set(dash, dashPhase);
    }

    public BasicStroke(int type, float width, int cap, int join, float miterLimit, float[] dash, float dashPhase) {
        this.tmpMiter = new float[2];
        set(type, width, cap, join, miterLimit);
        set(dash, dashPhase);
    }

    public BasicStroke(int type, float width, int cap, int join, float miterLimit, double[] dash, float dashPhase) {
        this.tmpMiter = new float[2];
        set(type, width, cap, join, miterLimit);
        set(dash, dashPhase);
    }

    public void set(int type, float width, int cap, int join, float miterLimit) {
        if (type != 0 && type != 1 && type != 2) {
            throw new IllegalArgumentException("illegal type");
        }
        if (width < 0.0f) {
            throw new IllegalArgumentException("negative width");
        }
        if (cap != 0 && cap != 1 && cap != 2) {
            throw new IllegalArgumentException("illegal end cap value");
        }
        if (join == 0) {
            if (miterLimit < 1.0f) {
                throw new IllegalArgumentException("miter limit < 1");
            }
        } else if (join != 1 && join != 2) {
            throw new IllegalArgumentException("illegal line join value");
        }
        this.type = type;
        this.width = width;
        this.cap = cap;
        this.join = join;
        this.miterLimit = miterLimit;
    }

    public void set(float[] dash, float dashPhase) {
        if (dash != null) {
            boolean allzero = true;
            for (float d2 : dash) {
                if (d2 > 0.0d) {
                    allzero = false;
                } else if (d2 < 0.0d) {
                    throw new IllegalArgumentException("negative dash length");
                }
            }
            if (allzero) {
                throw new IllegalArgumentException("dash lengths all zero");
            }
        }
        this.dash = dash;
        this.dashPhase = dashPhase;
    }

    public void set(double[] dash, float dashPhase) {
        if (dash != null) {
            float[] newdashes = new float[dash.length];
            boolean allzero = true;
            for (int i2 = 0; i2 < dash.length; i2++) {
                float d2 = (float) dash[i2];
                if (d2 > 0.0d) {
                    allzero = false;
                } else if (d2 < 0.0d) {
                    throw new IllegalArgumentException("negative dash length");
                }
                newdashes[i2] = d2;
            }
            if (allzero) {
                throw new IllegalArgumentException("dash lengths all zero");
            }
            this.dash = newdashes;
        } else {
            this.dash = null;
        }
        this.dashPhase = dashPhase;
    }

    public int getType() {
        return this.type;
    }

    public float getLineWidth() {
        return this.width;
    }

    public int getEndCap() {
        return this.cap;
    }

    public int getLineJoin() {
        return this.join;
    }

    public float getMiterLimit() {
        return this.miterLimit;
    }

    public boolean isDashed() {
        return this.dash != null;
    }

    public float[] getDashArray() {
        return this.dash;
    }

    public float getDashPhase() {
        return this.dashPhase;
    }

    public Shape createStrokedShape(Shape s2) {
        Shape ret;
        if (s2 instanceof RoundRectangle2D) {
            ret = strokeRoundRectangle((RoundRectangle2D) s2);
        } else {
            ret = null;
        }
        if (ret != null) {
            return ret;
        }
        Shape ret2 = createCenteredStrokedShape(s2);
        if (this.type == 1) {
            ret2 = makeIntersectedShape(ret2, s2);
        } else if (this.type == 2) {
            ret2 = makeSubtractedShape(ret2, s2);
        }
        return ret2;
    }

    private boolean isCW(float dx1, float dy1, float dx2, float dy2) {
        return dx1 * dy2 <= dy1 * dx2;
    }

    private void computeOffset(float lx, float ly, float w2, float[] m2, int off) {
        float len = (float) Math.sqrt((lx * lx) + (ly * ly));
        if (len == 0.0f) {
            m2[off + 1] = 0.0f;
            m2[off + 0] = 0.0f;
        } else {
            m2[off + 0] = (ly * w2) / len;
            m2[off + 1] = (-(lx * w2)) / len;
        }
    }

    private void computeMiter(float x0, float y0, float x1, float y1, float x0p, float y0p, float x1p, float y1p, float[] m2, int off) {
        float x10 = x1 - x0;
        float y10 = y1 - y0;
        float x10p = x1p - x0p;
        float y10p = y1p - y0p;
        float den = (x10 * y10p) - (x10p * y10);
        float t2 = ((x10p * (y0 - y0p)) - (y10p * (x0 - x0p))) / den;
        m2[off] = x0 + (t2 * x10);
        m2[off + 1] = y0 + (t2 * y10);
    }

    private void accumulateQuad(float[] bbox, int off, float v0, float vc, float v1, float w2) {
        float num = v0 - vc;
        float den = (v1 - vc) + num;
        if (den != 0.0f) {
            float t2 = num / den;
            if (t2 > 0.0f && t2 < 1.0f) {
                float u2 = 1.0f - t2;
                float v2 = (v0 * u2 * u2) + (2.0f * vc * t2 * u2) + (v1 * t2 * t2);
                if (bbox[off] > v2 - w2) {
                    bbox[off] = v2 - w2;
                }
                if (bbox[off + 2] < v2 + w2) {
                    bbox[off + 2] = v2 + w2;
                }
            }
        }
    }

    private void accumulateCubic(float[] bbox, int off, float t2, float v0, float vc0, float vc1, float v1, float w2) {
        if (t2 > 0.0f && t2 < 1.0f) {
            float u2 = 1.0f - t2;
            float v2 = (v0 * u2 * u2 * u2) + (3.0f * vc0 * t2 * u2 * u2) + (3.0f * vc1 * t2 * t2 * u2) + (v1 * t2 * t2 * t2);
            if (bbox[off] > v2 - w2) {
                bbox[off] = v2 - w2;
            }
            if (bbox[off + 2] < v2 + w2) {
                bbox[off + 2] = v2 + w2;
            }
        }
    }

    private void accumulateCubic(float[] bbox, int off, float v0, float vc0, float vc1, float v1, float w2) {
        float c2 = vc0 - v0;
        float b2 = 2.0f * ((vc1 - vc0) - c2);
        float a2 = ((v1 - vc1) - b2) - c2;
        if (a2 == 0.0f) {
            if (b2 == 0.0f) {
                return;
            }
            accumulateCubic(bbox, off, (-c2) / b2, v0, vc0, vc1, v1, w2);
            return;
        }
        float d2 = (b2 * b2) - ((4.0f * a2) * c2);
        if (d2 < 0.0f) {
            return;
        }
        float d3 = (float) Math.sqrt(d2);
        if (b2 < 0.0f) {
            d3 = -d3;
        }
        float q2 = (b2 + d3) / (-2.0f);
        accumulateCubic(bbox, off, q2 / a2, v0, vc0, vc1, v1, w2);
        if (q2 != 0.0f) {
            accumulateCubic(bbox, off, c2 / q2, v0, vc0, vc1, v1, w2);
        }
    }

    public void accumulateShapeBounds(float[] bbox, Shape shape, BaseTransform tx) {
        if (this.type == 1) {
            Shape.accumulate(bbox, shape, tx);
            return;
        }
        if ((tx.getType() & (-92)) != 0) {
            Shape.accumulate(bbox, createStrokedShape(shape), tx);
            return;
        }
        PathIterator pi = shape.getPathIterator(tx);
        boolean lastSegmentMove = true;
        float[] coords = new float[6];
        float w2 = (float) ((this.type == 0 ? getLineWidth() / 2.0f : getLineWidth()) * Math.hypot(tx.getMxx(), tx.getMyx()));
        float sx = 0.0f;
        float sy = 0.0f;
        float x0 = 0.0f;
        float y0 = 0.0f;
        float sdx = 0.0f;
        float sdy = 0.0f;
        float pdx = 0.0f;
        float pdy = 0.0f;
        float[] o2 = new float[4];
        float pox = 0.0f;
        float poy = 0.0f;
        float sox = 0.0f;
        float soy = 0.0f;
        while (!pi.isDone()) {
            int cur = pi.currentSegment(coords);
            switch (cur) {
                case 0:
                    if (!lastSegmentMove) {
                        accumulateCap(pdx, pdy, x0, y0, pox, poy, bbox, w2);
                        accumulateCap(-sdx, -sdy, sx, sy, -sox, -soy, bbox, w2);
                    }
                    float f2 = coords[0];
                    sx = f2;
                    x0 = f2;
                    float f3 = coords[1];
                    sy = f3;
                    y0 = f3;
                    break;
                case 1:
                    float x1 = coords[0];
                    float y1 = coords[1];
                    float dx = x1 - x0;
                    float dy = y1 - y0;
                    if (dx == 0.0f && dy == 0.0f) {
                        dx = 1.0f;
                    }
                    computeOffset(dx, dy, w2, o2, 0);
                    if (!lastSegmentMove) {
                        accumulateJoin(pdx, pdy, dx, dy, x0, y0, pox, poy, o2[0], o2[1], bbox, w2);
                    }
                    x0 = x1;
                    y0 = y1;
                    pdx = dx;
                    pdy = dy;
                    pox = o2[0];
                    poy = o2[1];
                    if (lastSegmentMove) {
                        sdx = pdx;
                        sdy = pdy;
                        sox = pox;
                        soy = poy;
                        break;
                    }
                    break;
                case 2:
                    float x12 = coords[2];
                    float y12 = coords[3];
                    float dx2 = coords[0] - x0;
                    float dy2 = coords[1] - y0;
                    computeOffset(dx2, dy2, w2, o2, 0);
                    if (!lastSegmentMove) {
                        accumulateJoin(pdx, pdy, dx2, dy2, x0, y0, pox, poy, o2[0], o2[1], bbox, w2);
                    }
                    if (bbox[0] > coords[0] - w2 || bbox[2] < coords[0] + w2) {
                        accumulateQuad(bbox, 0, x0, coords[0], x12, w2);
                    }
                    if (bbox[1] > coords[1] - w2 || bbox[3] < coords[1] + w2) {
                        accumulateQuad(bbox, 1, y0, coords[1], y12, w2);
                    }
                    x0 = x12;
                    y0 = y12;
                    if (lastSegmentMove) {
                        sdx = dx2;
                        sdy = dy2;
                        sox = o2[0];
                        soy = o2[1];
                    }
                    pdx = x12 - coords[0];
                    pdy = y12 - coords[1];
                    computeOffset(pdx, pdy, w2, o2, 0);
                    pox = o2[0];
                    poy = o2[1];
                    break;
                case 3:
                    float x13 = coords[4];
                    float y13 = coords[5];
                    float dx3 = coords[0] - x0;
                    float dy3 = coords[1] - y0;
                    computeOffset(dx3, dy3, w2, o2, 0);
                    if (!lastSegmentMove) {
                        accumulateJoin(pdx, pdy, dx3, dy3, x0, y0, pox, poy, o2[0], o2[1], bbox, w2);
                    }
                    if (bbox[0] > coords[0] - w2 || bbox[2] < coords[0] + w2 || bbox[0] > coords[2] - w2 || bbox[2] < coords[2] + w2) {
                        accumulateCubic(bbox, 0, x0, coords[0], coords[2], x13, w2);
                    }
                    if (bbox[1] > coords[1] - w2 || bbox[3] < coords[1] + w2 || bbox[1] > coords[3] - w2 || bbox[3] < coords[3] + w2) {
                        accumulateCubic(bbox, 1, y0, coords[1], coords[3], y13, w2);
                    }
                    x0 = x13;
                    y0 = y13;
                    if (lastSegmentMove) {
                        sdx = dx3;
                        sdy = dy3;
                        sox = o2[0];
                        soy = o2[1];
                    }
                    pdx = x13 - coords[2];
                    pdy = y13 - coords[3];
                    computeOffset(pdx, pdy, w2, o2, 0);
                    pox = o2[0];
                    poy = o2[1];
                    break;
                case 4:
                    float dx4 = sx - x0;
                    float dy4 = sy - y0;
                    float x14 = sx;
                    float y14 = sy;
                    if (!lastSegmentMove) {
                        computeOffset(sdx, sdy, w2, o2, 2);
                        if (dx4 == 0.0f && dy4 == 0.0f) {
                            accumulateJoin(pdx, pdy, sdx, sdy, sx, sy, pox, poy, o2[2], o2[3], bbox, w2);
                        } else {
                            computeOffset(dx4, dy4, w2, o2, 0);
                            accumulateJoin(pdx, pdy, dx4, dy4, x0, y0, pox, poy, o2[0], o2[1], bbox, w2);
                            accumulateJoin(dx4, dy4, sdx, sdy, x14, y14, o2[0], o2[1], o2[2], o2[3], bbox, w2);
                        }
                    }
                    x0 = x14;
                    y0 = y14;
                    break;
            }
            lastSegmentMove = cur == 0 || cur == 4;
            pi.next();
        }
        if (!lastSegmentMove) {
            accumulateCap(pdx, pdy, x0, y0, pox, poy, bbox, w2);
            accumulateCap(-sdx, -sdy, sx, sy, -sox, -soy, bbox, w2);
        }
    }

    private void accumulate(float o0, float o1, float o2, float o3, float[] bbox) {
        if (o0 <= o2) {
            if (o0 < bbox[0]) {
                bbox[0] = o0;
            }
            if (o2 > bbox[2]) {
                bbox[2] = o2;
            }
        } else {
            if (o2 < bbox[0]) {
                bbox[0] = o2;
            }
            if (o0 > bbox[2]) {
                bbox[2] = o0;
            }
        }
        if (o1 <= o3) {
            if (o1 < bbox[1]) {
                bbox[1] = o1;
            }
            if (o3 > bbox[3]) {
                bbox[3] = o3;
                return;
            }
            return;
        }
        if (o3 < bbox[1]) {
            bbox[1] = o3;
        }
        if (o1 > bbox[3]) {
            bbox[3] = o1;
        }
    }

    private void accumulateOrdered(float o0, float o1, float o2, float o3, float[] bbox) {
        if (o0 < bbox[0]) {
            bbox[0] = o0;
        }
        if (o2 > bbox[2]) {
            bbox[2] = o2;
        }
        if (o1 < bbox[1]) {
            bbox[1] = o1;
        }
        if (o3 > bbox[3]) {
            bbox[3] = o3;
        }
    }

    private void accumulateJoin(float pdx, float pdy, float dx, float dy, float x0, float y0, float pox, float poy, float ox, float oy, float[] bbox, float w2) {
        if (this.join == 2) {
            accumulateBevel(x0, y0, pox, poy, ox, oy, bbox);
        } else if (this.join == 0) {
            accumulateMiter(pdx, pdy, dx, dy, pox, poy, ox, oy, x0, y0, bbox, w2);
        } else {
            accumulateOrdered(x0 - w2, y0 - w2, x0 + w2, y0 + w2, bbox);
        }
    }

    private void accumulateCap(float dx, float dy, float x0, float y0, float ox, float oy, float[] bbox, float w2) {
        if (this.cap == 2) {
            accumulate((x0 + ox) - oy, y0 + oy + ox, (x0 - ox) - oy, (y0 - oy) + ox, bbox);
        } else if (this.cap == 0) {
            accumulate(x0 + ox, y0 + oy, x0 - ox, y0 - oy, bbox);
        } else {
            accumulateOrdered(x0 - w2, y0 - w2, x0 + w2, y0 + w2, bbox);
        }
    }

    private void accumulateMiter(float pdx, float pdy, float dx, float dy, float pox, float poy, float ox, float oy, float x0, float y0, float[] bbox, float w2) {
        accumulateBevel(x0, y0, pox, poy, ox, oy, bbox);
        boolean cw = isCW(pdx, pdy, dx, dy);
        if (cw) {
            pox = -pox;
            poy = -poy;
            ox = -ox;
            oy = -oy;
        }
        computeMiter((x0 - pdx) + pox, (y0 - pdy) + poy, x0 + pox, y0 + poy, x0 + dx + ox, y0 + dy + oy, x0 + ox, y0 + oy, this.tmpMiter, 0);
        float lenSq = ((this.tmpMiter[0] - x0) * (this.tmpMiter[0] - x0)) + ((this.tmpMiter[1] - y0) * (this.tmpMiter[1] - y0));
        float miterLimitWidth = this.miterLimit * w2;
        if (lenSq < miterLimitWidth * miterLimitWidth) {
            accumulateOrdered(this.tmpMiter[0], this.tmpMiter[1], this.tmpMiter[0], this.tmpMiter[1], bbox);
        }
    }

    private void accumulateBevel(float x0, float y0, float pox, float poy, float ox, float oy, float[] bbox) {
        accumulate(x0 + pox, y0 + poy, x0 - pox, y0 - poy, bbox);
        accumulate(x0 + ox, y0 + oy, x0 - ox, y0 - oy, bbox);
    }

    public Shape createCenteredStrokedShape(Shape s2) {
        Path2D p2d = new Path2D(1);
        float lw = this.type == 0 ? this.width : this.width * 2.0f;
        PathConsumer2D pc2d = new Stroker(p2d, lw, this.cap, this.join, this.miterLimit);
        if (this.dash != null) {
            pc2d = new Dasher(pc2d, this.dash, this.dashPhase);
        }
        OpenPiscesPrismUtils.feedConsumer(s2.getPathIterator(null), pc2d);
        return p2d;
    }

    Shape strokeRoundRectangle(RoundRectangle2D rr) {
        int j2;
        float id;
        float od;
        Shape outer;
        if (rr.width < 0.0f || rr.height < 0.0f) {
            return new Path2D();
        }
        if (isDashed()) {
            return null;
        }
        float aw2 = rr.arcWidth;
        float ah2 = rr.arcHeight;
        if (aw2 <= 0.0f || ah2 <= 0.0f) {
            ah2 = 0.0f;
            aw2 = 0.0f;
            if (this.type == 1) {
                j2 = 0;
            } else {
                j2 = this.join;
                if (j2 == 0 && this.miterLimit < SQRT_2) {
                    j2 = 2;
                }
            }
        } else {
            if (aw2 < ah2 * 0.9f || ah2 < aw2 * 0.9f) {
                return null;
            }
            j2 = 1;
        }
        if (this.type == 1) {
            od = 0.0f;
            id = this.width;
        } else if (this.type == 2) {
            od = this.width;
            id = 0.0f;
        } else {
            float f2 = this.width / 2.0f;
            id = f2;
            od = f2;
        }
        switch (j2) {
            case 0:
                outer = new RoundRectangle2D(rr.f11924x - od, rr.f11925y - od, rr.width + (od * 2.0f), rr.height + (od * 2.0f), 0.0f, 0.0f);
                break;
            case 1:
                outer = new RoundRectangle2D(rr.f11924x - od, rr.f11925y - od, rr.width + (od * 2.0f), rr.height + (od * 2.0f), aw2 + (od * 2.0f), ah2 + (od * 2.0f));
                break;
            case 2:
                outer = makeBeveledRect(rr.f11924x, rr.f11925y, rr.width, rr.height, od);
                break;
            default:
                throw new InternalError("Unrecognized line join style");
        }
        if (rr.width <= id * 2.0f || rr.height <= id * 2.0f) {
            return outer;
        }
        float aw3 = aw2 - (id * 2.0f);
        float ah3 = ah2 - (id * 2.0f);
        if (aw3 <= 0.0f || ah3 <= 0.0f) {
            ah3 = 0.0f;
            aw3 = 0.0f;
        }
        Shape inner = new RoundRectangle2D(rr.f11924x + id, rr.f11925y + id, rr.width - (id * 2.0f), rr.height - (id * 2.0f), aw3, ah3);
        Path2D p2d = outer instanceof Path2D ? (Path2D) outer : new Path2D(outer);
        p2d.setWindingRule(0);
        p2d.append(inner, false);
        return p2d;
    }

    static Shape makeBeveledRect(float rx, float ry, float rw, float rh, float d2) {
        float rx1 = rx + rw;
        float ry1 = ry + rh;
        Path2D p2 = new Path2D();
        p2.moveTo(rx, ry - d2);
        p2.lineTo(rx1, ry - d2);
        p2.lineTo(rx1 + d2, ry);
        p2.lineTo(rx1 + d2, ry1);
        p2.lineTo(rx1, ry1 + d2);
        p2.lineTo(rx, ry1 + d2);
        p2.lineTo(rx - d2, ry1);
        p2.lineTo(rx - d2, ry);
        p2.closePath();
        return p2;
    }

    protected Shape makeIntersectedShape(Shape outer, Shape inner) {
        return new CAGShapePair(outer, inner, 4);
    }

    protected Shape makeSubtractedShape(Shape outer, Shape inner) {
        return new CAGShapePair(outer, inner, 1);
    }

    /* loaded from: jfxrt.jar:com/sun/prism/BasicStroke$CAGShapePair.class */
    static class CAGShapePair extends GeneralShapePair {
        private Shape cagshape;

        public CAGShapePair(Shape outer, Shape inner, int type) {
            super(outer, inner, type);
        }

        @Override // com.sun.javafx.geom.GeneralShapePair, com.sun.javafx.geom.Shape
        public PathIterator getPathIterator(BaseTransform tx) {
            if (this.cagshape == null) {
                Area o2 = new Area(getOuterShape());
                Area i2 = new Area(getInnerShape());
                if (getCombinationType() == 4) {
                    o2.intersect(i2);
                } else {
                    o2.subtract(i2);
                }
                this.cagshape = o2;
            }
            return this.cagshape.getPathIterator(tx);
        }
    }

    public int hashCode() {
        int hash = (((((Float.floatToIntBits(this.width) * 31) + this.join) * 31) + this.cap) * 31) + Float.floatToIntBits(this.miterLimit);
        if (this.dash != null) {
            hash = (hash * 31) + Float.floatToIntBits(this.dashPhase);
            for (int i2 = 0; i2 < this.dash.length; i2++) {
                hash = (hash * 31) + Float.floatToIntBits(this.dash[i2]);
            }
        }
        return hash;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BasicStroke)) {
            return false;
        }
        BasicStroke bs2 = (BasicStroke) obj;
        if (this.width != bs2.width || this.join != bs2.join || this.cap != bs2.cap || this.miterLimit != bs2.miterLimit) {
            return false;
        }
        if (this.dash != null) {
            if (this.dashPhase != bs2.dashPhase || !Arrays.equals(this.dash, bs2.dash)) {
                return false;
            }
            return true;
        }
        if (bs2.dash != null) {
            return false;
        }
        return true;
    }

    public BasicStroke copy() {
        return new BasicStroke(this.type, this.width, this.cap, this.join, this.miterLimit, this.dash, this.dashPhase);
    }
}
