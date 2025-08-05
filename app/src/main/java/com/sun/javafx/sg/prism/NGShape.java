package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Paint;
import com.sun.prism.shape.ShapeRep;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGShape.class */
public abstract class NGShape extends NGNode {
    private RTTexture cached3D;
    private double cachedW;
    private double cachedH;
    protected Paint fillPaint;
    protected Paint drawPaint;
    protected BasicStroke drawStroke;
    protected Mode mode = Mode.FILL;
    protected ShapeRep shapeRep;
    private boolean smooth;
    static final double THRESHOLD = 0.00390625d;

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGShape$Mode.class */
    public enum Mode {
        EMPTY,
        FILL,
        STROKE,
        STROKE_FILL
    }

    public abstract Shape getShape();

    public void setMode(Mode mode) {
        if (mode != this.mode) {
            this.mode = mode;
            geometryChanged();
        }
    }

    public Mode getMode() {
        return this.mode;
    }

    public void setSmooth(boolean smooth) {
        boolean smooth2 = !PrismSettings.forceNonAntialiasedShape && smooth;
        if (smooth2 != this.smooth) {
            this.smooth = smooth2;
            visualsChanged();
        }
    }

    public boolean isSmooth() {
        return this.smooth;
    }

    public void setFillPaint(Object fillPaint) {
        if (fillPaint != this.fillPaint || (this.fillPaint != null && this.fillPaint.isMutable())) {
            this.fillPaint = (Paint) fillPaint;
            visualsChanged();
            invalidateOpaqueRegion();
        }
    }

    public Paint getFillPaint() {
        return this.fillPaint;
    }

    public void setDrawPaint(Object drawPaint) {
        if (drawPaint != this.drawPaint || (this.drawPaint != null && this.drawPaint.isMutable())) {
            this.drawPaint = (Paint) drawPaint;
            visualsChanged();
        }
    }

    public void setDrawStroke(BasicStroke drawStroke) {
        if (this.drawStroke != drawStroke) {
            this.drawStroke = drawStroke;
            geometryChanged();
        }
    }

    public void setDrawStroke(float strokeWidth, StrokeType strokeType, StrokeLineCap lineCap, StrokeLineJoin lineJoin, float strokeMiterLimit, float[] strokeDashArray, float strokeDashOffset) {
        int type;
        int cap;
        int join;
        if (strokeType == StrokeType.CENTERED) {
            type = 0;
        } else if (strokeType == StrokeType.INSIDE) {
            type = 1;
        } else {
            type = 2;
        }
        if (lineCap == StrokeLineCap.BUTT) {
            cap = 0;
        } else if (lineCap == StrokeLineCap.SQUARE) {
            cap = 2;
        } else {
            cap = 1;
        }
        if (lineJoin == StrokeLineJoin.BEVEL) {
            join = 2;
        } else if (lineJoin == StrokeLineJoin.MITER) {
            join = 0;
        } else {
            join = 1;
        }
        if (this.drawStroke == null) {
            this.drawStroke = new BasicStroke(type, strokeWidth, cap, join, strokeMiterLimit);
        } else {
            this.drawStroke.set(type, strokeWidth, cap, join, strokeMiterLimit);
        }
        if (strokeDashArray.length > 0) {
            this.drawStroke.set(strokeDashArray, strokeDashOffset);
        } else {
            this.drawStroke.set((float[]) null, 0.0f);
        }
        geometryChanged();
    }

    protected ShapeRep createShapeRep(Graphics g2) {
        return g2.getResourceFactory().createPathRep();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void visualsChanged() {
        super.visualsChanged();
        if (this.cached3D != null) {
            this.cached3D.dispose();
            this.cached3D = null;
        }
    }

    private static double hypot(double x2, double y2, double z2) {
        return Math.sqrt((x2 * x2) + (y2 * y2) + (z2 * z2));
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        if (this.mode == Mode.EMPTY) {
            return;
        }
        boolean printing = g2 instanceof PrinterGraphics;
        BaseTransform tx = g2.getTransformNoClone();
        boolean needs3D = !tx.is2D();
        if (needs3D) {
            double scaleX = hypot(tx.getMxx(), tx.getMyx(), tx.getMzx());
            double scaleY = hypot(tx.getMxy(), tx.getMyy(), tx.getMzy());
            double scaledW = scaleX * this.contentBounds.getWidth();
            double scaledH = scaleY * this.contentBounds.getHeight();
            if (this.cached3D != null) {
                this.cached3D.lock();
                if (this.cached3D.isSurfaceLost() || Math.max(Math.abs(scaledW - this.cachedW), Math.abs(scaledH - this.cachedH)) > THRESHOLD) {
                    this.cached3D.unlock();
                    this.cached3D.dispose();
                    this.cached3D = null;
                }
            }
            if (this.cached3D == null) {
                int w2 = (int) Math.ceil(scaledW);
                int h2 = (int) Math.ceil(scaledH);
                this.cachedW = scaledW;
                this.cachedH = scaledH;
                if (w2 <= 0 || h2 <= 0) {
                    return;
                }
                this.cached3D = g2.getResourceFactory().createRTTexture(w2, h2, Texture.WrapMode.CLAMP_TO_ZERO, false);
                this.cached3D.setLinearFiltering(isSmooth());
                this.cached3D.contentsUseful();
                Graphics textureGraphics = this.cached3D.createGraphics();
                textureGraphics.scale((float) scaleX, (float) scaleY);
                textureGraphics.translate(-this.contentBounds.getMinX(), -this.contentBounds.getMinY());
                renderContent2D(textureGraphics, printing);
            }
            int rtWidth = this.cached3D.getContentWidth();
            int rtHeight = this.cached3D.getContentHeight();
            float dx0 = this.contentBounds.getMinX();
            float dy0 = this.contentBounds.getMinY();
            float dx1 = dx0 + ((float) (rtWidth / scaleX));
            float dy1 = dy0 + ((float) (rtHeight / scaleY));
            g2.drawTexture(this.cached3D, dx0, dy0, dx1, dy1, 0.0f, 0.0f, rtWidth, rtHeight);
            this.cached3D.unlock();
            return;
        }
        if (this.cached3D != null) {
            this.cached3D.dispose();
            this.cached3D = null;
        }
        renderContent2D(g2, printing);
    }

    protected void renderContent2D(Graphics g2, boolean printing) {
        boolean saveAA = g2.isAntialiasedShape();
        boolean isAA = isSmooth();
        if (isAA != saveAA) {
            g2.setAntialiasedShape(isAA);
        }
        ShapeRep localShapeRep = printing ? null : this.shapeRep;
        if (localShapeRep == null) {
            localShapeRep = createShapeRep(g2);
        }
        Shape shape = getShape();
        if (this.mode != Mode.STROKE) {
            g2.setPaint(this.fillPaint);
            localShapeRep.fill(g2, shape, this.contentBounds);
        }
        if (this.mode != Mode.FILL && this.drawStroke.getLineWidth() > 0.0f) {
            g2.setPaint(this.drawPaint);
            g2.setStroke(this.drawStroke);
            localShapeRep.draw(g2, shape, this.contentBounds);
        }
        if (isAA != saveAA) {
            g2.setAntialiasedShape(saveAA);
        }
        if (!printing) {
            this.shapeRep = localShapeRep;
        }
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return this.mode == Mode.STROKE_FILL;
    }

    protected Shape getStrokeShape() {
        return this.drawStroke.createStrokedShape(getShape());
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void geometryChanged() {
        super.geometryChanged();
        if (this.shapeRep != null) {
            this.shapeRep.invalidate(ShapeRep.InvalidationType.LOCATION_AND_GEOMETRY);
        }
        if (this.cached3D != null) {
            this.cached3D.dispose();
            this.cached3D = null;
        }
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOpaqueRegion() {
        Mode mode = getMode();
        Paint fillPaint = getFillPaint();
        return super.hasOpaqueRegion() && (mode == Mode.FILL || mode == Mode.STROKE_FILL) && fillPaint != null && fillPaint.isOpaque();
    }
}
