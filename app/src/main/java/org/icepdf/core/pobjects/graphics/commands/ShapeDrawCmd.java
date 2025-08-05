package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/ShapeDrawCmd.class */
public class ShapeDrawCmd implements DrawCmd {
    private Shape shape;

    public ShapeDrawCmd(Shape shape) {
        this.shape = shape;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        return this.shape;
    }

    public Shape getShape() {
        return this.shape;
    }
}
