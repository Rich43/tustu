package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/StrokeDrawCmd.class */
public class StrokeDrawCmd extends AbstractDrawCmd {
    private Stroke stroke;

    public StrokeDrawCmd(Stroke stroke) {
        this.stroke = stroke;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        g2.setStroke(this.stroke);
        return currentShape;
    }

    public Stroke getStroke() {
        return this.stroke;
    }
}
