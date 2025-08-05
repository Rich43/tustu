package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/AbstractDrawCmd.class */
public abstract class AbstractDrawCmd implements DrawCmd {
    protected static final Logger logger = Logger.getLogger(AbstractDrawCmd.class.toString());
    protected static boolean disableClipping = Defs.sysPropertyBoolean("org.icepdf.core.paint.disableClipping", false);

    @Override // org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public abstract Shape paintOperand(Graphics2D graphics2D, Page page, Shape shape, Shape shape2, AffineTransform affineTransform, OptionalContentState optionalContentState, boolean z2, PaintTimer paintTimer);
}
