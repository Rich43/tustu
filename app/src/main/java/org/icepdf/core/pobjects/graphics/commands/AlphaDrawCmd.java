package org.icepdf.core.pobjects.graphics.commands;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/AlphaDrawCmd.class */
public class AlphaDrawCmd extends AbstractDrawCmd {
    private AlphaComposite alphaComposite;

    public AlphaDrawCmd(AlphaComposite alphaComposite) {
        this.alphaComposite = alphaComposite;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        if (paintAlpha) {
            g2.setComposite(this.alphaComposite);
        }
        return currentShape;
    }
}
