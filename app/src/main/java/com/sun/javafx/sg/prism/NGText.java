package com.sun.javafx.sg.prism;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Color;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGText.class */
public class NGText extends NGShape {
    private GlyphList[] runs;
    private float layoutX;
    private float layoutY;
    private PGFont font;
    private int fontSmoothingType;
    private boolean underline;
    private boolean strikethrough;
    private Object selectionPaint;
    private int selectionStart;
    private int selectionEnd;
    private FontStrike fontStrike = null;
    private FontStrike identityStrike = null;
    private double[] strikeMat = new double[4];
    private boolean drawingEffect = false;
    static final BaseTransform IDENT = BaseTransform.IDENTITY_TRANSFORM;
    private static double EPSILON = 0.01d;
    private static int FILL = 2;
    private static int SHAPE_FILL = 4;
    private static int TEXT = 8;
    private static int DECORATION = 16;

    public void setGlyphs(Object[] glyphs) {
        this.runs = (GlyphList[]) glyphs;
        geometryChanged();
    }

    public void setLayoutLocation(float x2, float y2) {
        this.layoutX = x2;
        this.layoutY = y2;
        geometryChanged();
    }

    public void setFont(Object font) {
        if (font != null && font.equals(this.font)) {
            return;
        }
        this.font = (PGFont) font;
        this.fontStrike = null;
        this.identityStrike = null;
        geometryChanged();
    }

    public void setFontSmoothingType(int fontSmoothingType) {
        this.fontSmoothingType = fontSmoothingType;
        geometryChanged();
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
        geometryChanged();
    }

    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
        geometryChanged();
    }

    public void setSelection(int start, int end, Object color) {
        this.selectionPaint = color;
        this.selectionStart = start;
        this.selectionEnd = end;
        geometryChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected BaseBounds computePadding(BaseBounds region) {
        float pad = this.fontSmoothingType == 1 ? 2.0f : 1.0f;
        return region.deriveWithNewBounds(region.getMinX() - pad, region.getMinY() - pad, region.getMinZ(), region.getMaxX() + pad, region.getMaxY() + pad, region.getMaxZ());
    }

    private FontStrike getStrike(BaseTransform xform) {
        int smoothingType = this.fontSmoothingType;
        if (getMode() == NGShape.Mode.STROKE_FILL) {
            smoothingType = 0;
        }
        if (xform.isIdentity()) {
            if (this.identityStrike == null || smoothingType != this.identityStrike.getAAMode()) {
                this.identityStrike = this.font.getStrike(IDENT, smoothingType);
            }
            return this.identityStrike;
        }
        if (this.fontStrike == null || this.fontStrike.getSize() != this.font.getSize() || ((xform.getMxy() == 0.0d && this.strikeMat[1] != 0.0d) || ((xform.getMyx() == 0.0d && this.strikeMat[2] != 0.0d) || Math.abs(this.strikeMat[0] - xform.getMxx()) > EPSILON || Math.abs(this.strikeMat[1] - xform.getMxy()) > EPSILON || Math.abs(this.strikeMat[2] - xform.getMyx()) > EPSILON || Math.abs(this.strikeMat[3] - xform.getMyy()) > EPSILON || smoothingType != this.fontStrike.getAAMode()))) {
            this.fontStrike = this.font.getStrike(xform, smoothingType);
            this.strikeMat[0] = xform.getMxx();
            this.strikeMat[1] = xform.getMxy();
            this.strikeMat[2] = xform.getMyx();
            this.strikeMat[3] = xform.getMyy();
        }
        return this.fontStrike;
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    public Shape getShape() {
        if (this.runs == null) {
            return new Path2D();
        }
        FontStrike strike = getStrike(IDENT);
        Path2D outline = new Path2D();
        for (int i2 = 0; i2 < this.runs.length; i2++) {
            GlyphList run = this.runs[i2];
            Point2D pt = run.getLocation();
            float x2 = pt.f11907x - this.layoutX;
            float y2 = pt.f11908y - this.layoutY;
            BaseTransform t2 = BaseTransform.getTranslateInstance(x2, y2);
            outline.append(strike.getOutline(run, t2), false);
            Metrics metrics = null;
            if (this.underline) {
                metrics = strike.getMetrics();
                RoundRectangle2D rect = new RoundRectangle2D();
                rect.f11924x = x2;
                rect.f11925y = y2 + metrics.getUnderLineOffset();
                rect.width = run.getWidth();
                rect.height = metrics.getUnderLineThickness();
                outline.append((Shape) rect, false);
            }
            if (this.strikethrough) {
                if (metrics == null) {
                    metrics = strike.getMetrics();
                }
                RoundRectangle2D rect2 = new RoundRectangle2D();
                rect2.f11924x = x2;
                rect2.f11925y = y2 + metrics.getStrikethroughOffset();
                rect2.width = run.getWidth();
                rect2.height = metrics.getStrikethroughThickness();
                outline.append((Shape) rect2, false);
            }
        }
        return outline;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderEffect(Graphics g2) {
        if (!g2.getTransformNoClone().isTranslateOrIdentity()) {
            this.drawingEffect = true;
        }
        try {
            super.renderEffect(g2);
        } finally {
            this.drawingEffect = false;
        }
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    protected void renderContent2D(Graphics g2, boolean printing) {
        if (this.mode == NGShape.Mode.EMPTY || this.runs == null || this.runs.length == 0) {
            return;
        }
        BaseTransform tx = g2.getTransformNoClone();
        FontStrike strike = getStrike(tx);
        if (strike.getAAMode() == 1 || ((this.fillPaint != null && this.fillPaint.isProportional()) || (this.drawPaint != null && this.drawPaint.isProportional()))) {
            BaseBounds bds = getContentBounds(new RectBounds(), IDENT);
            g2.setNodeBounds((RectBounds) bds);
        }
        Color selectionColor = null;
        if (this.selectionStart != this.selectionEnd && (this.selectionPaint instanceof Color)) {
            selectionColor = (Color) this.selectionPaint;
        }
        BaseBounds clipBds = null;
        if (getClipNode() != null) {
            clipBds = getClippedBounds(new RectBounds(), IDENT);
        }
        if (this.mode != NGShape.Mode.STROKE) {
            g2.setPaint(this.fillPaint);
            renderText(g2, strike, clipBds, selectionColor, TEXT | ((strike.drawAsShapes() || this.drawingEffect) ? SHAPE_FILL : FILL));
            if (this.underline || this.strikethrough) {
                renderText(g2, strike, clipBds, selectionColor, DECORATION | SHAPE_FILL);
            }
        }
        if (this.mode != NGShape.Mode.FILL) {
            g2.setPaint(this.drawPaint);
            g2.setStroke(this.drawStroke);
            int op = TEXT;
            if (this.underline || this.strikethrough) {
                op |= DECORATION;
            }
            renderText(g2, strike, clipBds, selectionColor, op);
        }
        g2.setNodeBounds(null);
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0083  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void renderText(com.sun.prism.Graphics r11, com.sun.javafx.font.FontStrike r12, com.sun.javafx.geom.BaseBounds r13, com.sun.prism.paint.Color r14, int r15) {
        /*
            Method dump skipped, instructions count: 523
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.sg.prism.NGText.renderText(com.sun.prism.Graphics, com.sun.javafx.font.FontStrike, com.sun.javafx.geom.BaseBounds, com.sun.prism.paint.Color, int):void");
    }
}
