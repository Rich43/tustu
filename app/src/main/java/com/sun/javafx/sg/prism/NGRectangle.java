package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RectangularShape;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.RectShadowGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.shape.ShapeRep;
import com.sun.scenario.effect.Effect;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGRectangle.class */
public class NGRectangle extends NGShape {
    private RoundRectangle2D rrect = new RoundRectangle2D();
    static final float HALF_MINUS_HALF_SQRT_HALF = 0.14700001f;
    private static final double SQRT_2 = Math.sqrt(2.0d);

    public void updateRectangle(float x2, float y2, float width, float height, float arcWidth, float arcHeight) {
        this.rrect.f11924x = x2;
        this.rrect.f11925y = y2;
        this.rrect.width = width;
        this.rrect.height = height;
        this.rrect.arcWidth = arcWidth;
        this.rrect.arcHeight = arcHeight;
        geometryChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override // com.sun.javafx.sg.prism.NGShape, com.sun.javafx.sg.prism.NGNode
    protected boolean hasOpaqueRegion() {
        return super.hasOpaqueRegion() && this.rrect.width > 1.0f && this.rrect.height > 1.0f;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected RectBounds computeOpaqueRegion(RectBounds opaqueRegion) {
        float x2 = this.rrect.f11924x;
        float y2 = this.rrect.f11925y;
        float w2 = this.rrect.width;
        float h2 = this.rrect.height;
        float aw2 = this.rrect.arcWidth;
        float ah2 = this.rrect.arcHeight;
        if (aw2 <= 0.0f || ah2 <= 0.0f) {
            return (RectBounds) opaqueRegion.deriveWithNewBounds(x2, y2, 0.0f, x2 + w2, y2 + h2, 0.0f);
        }
        float arcInsetWidth = Math.min(w2, aw2) * HALF_MINUS_HALF_SQRT_HALF;
        float arcInsetHeight = Math.min(h2, ah2) * HALF_MINUS_HALF_SQRT_HALF;
        return (RectBounds) opaqueRegion.deriveWithNewBounds(x2 + arcInsetWidth, y2 + arcInsetHeight, 0.0f, (x2 + w2) - arcInsetWidth, (y2 + h2) - arcInsetHeight, 0.0f);
    }

    boolean isRounded() {
        return this.rrect.arcWidth > 0.0f && this.rrect.arcHeight > 0.0f;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderEffect(Graphics g2) {
        if (!(g2 instanceof RectShadowGraphics) || !renderEffectDirectly(g2)) {
            super.renderEffect(g2);
        }
    }

    private boolean renderEffectDirectly(Graphics g2) {
        if (this.mode != NGShape.Mode.FILL || isRounded()) {
            return false;
        }
        float alpha = g2.getExtraAlpha();
        if (this.fillPaint instanceof Color) {
            float alpha2 = alpha * ((Color) this.fillPaint).getAlpha();
            Effect effect = getEffect();
            if (EffectUtil.renderEffectForRectangularNode(this, g2, effect, alpha2, true, this.rrect.f11924x, this.rrect.f11925y, this.rrect.width, this.rrect.height)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    public final Shape getShape() {
        return this.rrect;
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    protected ShapeRep createShapeRep(Graphics g2) {
        return g2.getResourceFactory().createRoundRectRep();
    }

    private static boolean hasRightAngleMiterAndNoDashes(BasicStroke bs2) {
        return bs2.getLineJoin() == 0 && ((double) bs2.getMiterLimit()) >= SQRT_2 && bs2.getDashArray() == null;
    }

    static boolean rectContains(float x2, float y2, NGShape node, RectangularShape r2) {
        NGShape.Mode mode;
        double rw = r2.getWidth();
        double rh = r2.getHeight();
        if (rw < 0.0d || rh < 0.0d || (mode = node.mode) == NGShape.Mode.EMPTY) {
            return false;
        }
        double rx = r2.getX();
        double ry = r2.getY();
        if (mode == NGShape.Mode.FILL) {
            return ((double) x2) >= rx && ((double) y2) >= ry && ((double) x2) < rx + rw && ((double) y2) < ry + rh;
        }
        float outerpad = -1.0f;
        float innerpad = -1.0f;
        boolean checkstroke = false;
        BasicStroke drawstroke = node.drawStroke;
        int type = drawstroke.getType();
        if (type == 1) {
            if (mode == NGShape.Mode.STROKE_FILL) {
                outerpad = 0.0f;
            } else if (drawstroke.getDashArray() == null) {
                outerpad = 0.0f;
                innerpad = drawstroke.getLineWidth();
            } else {
                checkstroke = true;
            }
        } else if (type == 2) {
            if (hasRightAngleMiterAndNoDashes(drawstroke)) {
                outerpad = drawstroke.getLineWidth();
                if (mode == NGShape.Mode.STROKE) {
                    innerpad = 0.0f;
                }
            } else {
                if (mode == NGShape.Mode.STROKE_FILL) {
                    outerpad = 0.0f;
                }
                checkstroke = true;
            }
        } else if (type == 0) {
            if (hasRightAngleMiterAndNoDashes(drawstroke)) {
                outerpad = drawstroke.getLineWidth() / 2.0f;
                if (mode == NGShape.Mode.STROKE) {
                    innerpad = outerpad;
                }
            } else {
                if (mode == NGShape.Mode.STROKE_FILL) {
                    outerpad = 0.0f;
                }
                checkstroke = true;
            }
        } else {
            if (mode == NGShape.Mode.STROKE_FILL) {
                outerpad = 0.0f;
            }
            checkstroke = true;
        }
        if (outerpad < 0.0f || x2 < rx - outerpad || y2 < ry - outerpad || x2 >= rx + rw + outerpad || y2 >= ry + rh + outerpad) {
            if (checkstroke) {
                return node.getStrokeShape().contains(x2, y2);
            }
            return false;
        }
        if (innerpad >= 0.0f && innerpad < rw / 2.0d && innerpad < rh / 2.0d && x2 >= rx + innerpad && y2 >= ry + innerpad && x2 < (rx + rw) - innerpad && y2 < (ry + rh) - innerpad) {
            return false;
        }
        return true;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected final boolean isRectClip(BaseTransform xform, boolean permitRoundedRectangle) {
        if (this.mode != NGShape.Mode.FILL || getClipNode() != null) {
            return false;
        }
        if ((getEffect() != null && getEffect().reducesOpaquePixels()) || getOpacity() < 1.0f) {
            return false;
        }
        if ((!permitRoundedRectangle && isRounded()) || !this.fillPaint.isOpaque()) {
            return false;
        }
        BaseTransform nodeXform = getTransform();
        if (!nodeXform.isIdentity()) {
            if (!xform.isIdentity()) {
                TEMP_TRANSFORM.setTransform(xform);
                TEMP_TRANSFORM.concatenate(nodeXform);
                xform = TEMP_TRANSFORM;
            } else {
                xform = nodeXform;
            }
        }
        long t2 = xform.getType();
        return (t2 & (-16)) == 0;
    }
}
