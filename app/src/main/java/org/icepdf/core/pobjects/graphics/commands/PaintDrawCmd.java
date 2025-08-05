package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/PaintDrawCmd.class */
public class PaintDrawCmd extends AbstractDrawCmd {
    private Paint paint;

    public PaintDrawCmd(Paint paint) {
        this.paint = paint;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        g2.setPaint(this.paint);
        return currentShape;
    }
}
