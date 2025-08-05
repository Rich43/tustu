package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/TextTransformDrawCmd.class */
public class TextTransformDrawCmd extends AbstractDrawCmd {
    private AffineTransform affineTransform;

    public TextTransformDrawCmd(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        AffineTransform af2 = new AffineTransform(base);
        af2.concatenate(this.affineTransform);
        g2.setTransform(af2);
        return currentShape;
    }

    public AffineTransform getAffineTransform() {
        return this.affineTransform;
    }
}
