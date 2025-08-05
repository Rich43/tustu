package com.sun.javafx.webkit.prism;

import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Paint;
import com.sun.webkit.graphics.WCStroke;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCStrokeImpl.class */
final class WCStrokeImpl extends WCStroke<Paint, BasicStroke> {
    private BasicStroke stroke;

    public WCStrokeImpl() {
    }

    public WCStrokeImpl(float width, int cap, int join, float miterLimit, float[] dash, float dashOffset) {
        setThickness(width);
        setLineCap(cap);
        setLineJoin(join);
        setMiterLimit(miterLimit);
        setDashSizes(dash);
        setDashOffset(dashOffset);
    }

    @Override // com.sun.webkit.graphics.WCStroke
    protected void invalidate() {
        this.stroke = null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.webkit.graphics.WCStroke
    public BasicStroke getPlatformStroke() {
        int style;
        if (this.stroke == null && (style = getStyle()) != 0) {
            float width = getThickness();
            float[] dash = getDashSizes();
            if (dash == null) {
                switch (style) {
                    case 2:
                        dash = new float[]{width, width};
                        break;
                    case 3:
                        dash = new float[]{3.0f * width, 3.0f * width};
                        break;
                }
            }
            this.stroke = new BasicStroke(width, getLineCap(), getLineJoin(), getMiterLimit(), dash, getDashOffset());
        }
        return this.stroke;
    }

    boolean isApplicable() {
        return (getPaint() == null || getPlatformStroke() == null) ? false : true;
    }

    boolean apply(Graphics graphics) {
        if (isApplicable()) {
            Paint _paint = getPaint();
            BasicStroke _stroke = getPlatformStroke();
            graphics.setPaint(_paint);
            graphics.setStroke(_stroke);
            return true;
        }
        return false;
    }
}
