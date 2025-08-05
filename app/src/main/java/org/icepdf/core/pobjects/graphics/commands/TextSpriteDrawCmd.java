package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;
import org.icepdf.core.pobjects.graphics.TextSprite;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/TextSpriteDrawCmd.class */
public class TextSpriteDrawCmd extends AbstractDrawCmd {
    private TextSprite textSprite;

    public TextSpriteDrawCmd(TextSprite textSprite) {
        this.textSprite = textSprite;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer lastPaintTime) {
        if (optionalContentState.isVisible() && this.textSprite.intersects(g2.getClip())) {
            this.textSprite.paint(g2);
        }
        return currentShape;
    }

    public TextSprite getTextSprite() {
        return this.textSprite;
    }
}
