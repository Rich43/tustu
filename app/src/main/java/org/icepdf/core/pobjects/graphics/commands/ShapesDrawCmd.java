package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;
import org.icepdf.core.pobjects.graphics.Shapes;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/ShapesDrawCmd.class */
public class ShapesDrawCmd extends AbstractDrawCmd {
    private Shapes shapes;

    public ShapesDrawCmd(Shapes shapes) {
        this.shapes = shapes;
    }

    public Shapes getShapes() {
        return this.shapes;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        if (optionalContentState.isVisible() && this.shapes != null) {
            this.shapes.setPageParent(parentPage);
            this.shapes.setPaintAlpha(paintAlpha);
            this.shapes.paint(g2);
            this.shapes.setPageParent(null);
        }
        return currentShape;
    }
}
