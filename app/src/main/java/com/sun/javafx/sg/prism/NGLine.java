package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.prism.Graphics;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGLine.class */
public class NGLine extends NGShape {
    private Line2D line = new Line2D();

    public void updateLine(float x1, float y1, float x2, float y2) {
        this.line.x1 = x1;
        this.line.y1 = y1;
        this.line.x2 = x2;
        this.line.y2 = y2;
        geometryChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    protected void renderContent2D(Graphics g2, boolean printing) {
        if ((this.mode == NGShape.Mode.STROKE || this.mode == NGShape.Mode.STROKE_FILL) && this.drawStroke.getLineWidth() > 0.0f && this.drawStroke.getType() != 1) {
            g2.setPaint(this.drawPaint);
            g2.setStroke(this.drawStroke);
            g2.drawLine(this.line.x1, this.line.y1, this.line.x2, this.line.y2);
        }
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    public final Shape getShape() {
        return this.line;
    }
}
