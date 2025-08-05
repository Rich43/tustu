package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/DrawDrawCmd.class */
public class DrawDrawCmd extends AbstractDrawCmd {
    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        if ((g2.getClip() != null && optionalContentState.isVisible() && currentShape.intersects(g2.getClip().getBounds())) || currentShape.getBounds().getWidth() < 1.0d || currentShape.getBounds().getHeight() < 1.0d) {
            g2.draw(currentShape);
            if (parentPage != null && paintTimer.shouldTriggerRepaint()) {
                parentPage.notifyPaintPageListeners();
            }
        } else if (g2.getClip() == null) {
            g2.draw(currentShape);
            if (parentPage != null && paintTimer.shouldTriggerRepaint()) {
                parentPage.notifyPaintPageListeners();
            }
        }
        return currentShape;
    }
}
