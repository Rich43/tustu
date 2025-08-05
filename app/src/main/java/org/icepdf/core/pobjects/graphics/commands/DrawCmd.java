package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/DrawCmd.class */
public interface DrawCmd {
    Shape paintOperand(Graphics2D graphics2D, Page page, Shape shape, Shape shape2, AffineTransform affineTransform, OptionalContentState optionalContentState, boolean z2, PaintTimer paintTimer);
}
