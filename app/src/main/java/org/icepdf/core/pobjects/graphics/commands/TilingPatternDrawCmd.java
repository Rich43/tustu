package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;
import org.icepdf.core.pobjects.graphics.TilingPattern;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/TilingPatternDrawCmd.class */
public class TilingPatternDrawCmd extends AbstractDrawCmd {
    private TilingPattern tilingPattern;

    public TilingPatternDrawCmd(TilingPattern tilingPattern) {
        this.tilingPattern = tilingPattern;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        this.tilingPattern.paintPattern(g2, base);
        return currentShape;
    }
}
