package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.GlyphOutlineClip;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/GlyphOutlineDrawCmd.class */
public class GlyphOutlineDrawCmd extends AbstractDrawCmd {
    private GlyphOutlineClip glyphOutlineClip;

    public GlyphOutlineDrawCmd(GlyphOutlineClip glyphOutlineClip) {
        this.glyphOutlineClip = glyphOutlineClip;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        if (optionalContentState.isVisible()) {
            AffineTransform preTrans = new AffineTransform(g2.getTransform());
            g2.setTransform(base);
            Shape glyphClip = this.glyphOutlineClip.getGlyphOutlineClip();
            g2.setClip(glyphClip);
            g2.setTransform(preTrans);
        }
        return currentShape;
    }
}
