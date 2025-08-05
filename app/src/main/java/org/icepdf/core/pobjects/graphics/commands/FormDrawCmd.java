package org.icepdf.core.pobjects.graphics.commands;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import org.icepdf.core.pobjects.Form;
import org.icepdf.core.pobjects.ImageUtility;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.graphics.GraphicsState;
import org.icepdf.core.pobjects.graphics.OptionalContentState;
import org.icepdf.core.pobjects.graphics.PaintTimer;
import org.icepdf.core.pobjects.graphics.Shapes;
import org.icepdf.core.pobjects.graphics.SoftMask;
import org.icepdf.core.util.Defs;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/commands/FormDrawCmd.class */
public class FormDrawCmd extends AbstractDrawCmd {
    private Form xForm;
    private BufferedImage xFormBuffer;

    /* renamed from: x, reason: collision with root package name */
    private int f13126x;

    /* renamed from: y, reason: collision with root package name */
    private int f13127y;
    private static boolean disableXObjectSMask = Defs.sysPropertyBoolean("org.icepdf.core.disableXObjectSMask", false);

    public FormDrawCmd(Form xForm) {
        this.xForm = xForm;
    }

    @Override // org.icepdf.core.pobjects.graphics.commands.AbstractDrawCmd, org.icepdf.core.pobjects.graphics.commands.DrawCmd
    public Shape paintOperand(Graphics2D g2, Page parentPage, Shape currentShape, Shape clip, AffineTransform base, OptionalContentState optionalContentState, boolean paintAlpha, PaintTimer paintTimer) {
        if (optionalContentState.isVisible() && this.xFormBuffer == null) {
            RenderingHints renderingHints = g2.getRenderingHints();
            Rectangle2D bBox = this.xForm.getBBox();
            this.f13126x = (int) bBox.getX();
            this.f13127y = (int) bBox.getY();
            GraphicsState graphicsState = this.xForm.getGraphicsState();
            this.xFormBuffer = createBufferXObject(parentPage, this.xForm, graphicsState, renderingHints);
            if (!disableXObjectSMask && graphicsState != null && graphicsState.getSoftMask() != null) {
                SoftMask softMask = graphicsState.getSoftMask();
                Form sMaskForm = softMask.getG();
                BufferedImage sMaskBuffer = createBufferXObject(parentPage, sMaskForm, graphicsState, renderingHints);
                if (sMaskBuffer.getWidth() > this.xFormBuffer.getWidth()) {
                    this.f13126x = (int) sMaskForm.getBBox().getX();
                    this.f13127y = (int) sMaskForm.getBBox().getY();
                }
                if (!sMaskForm.getResources().isShading()) {
                    this.xFormBuffer = ImageUtility.applyExplicitSMask(this.xFormBuffer, sMaskBuffer);
                    sMaskBuffer.flush();
                }
            }
        }
        g2.drawImage(this.xFormBuffer, (BufferedImageOp) null, this.f13126x, this.f13127y);
        return currentShape;
    }

    private BufferedImage createBufferXObject(Page parentPage, Form xForm, GraphicsState graphicsState, RenderingHints renderingHints) {
        Rectangle2D bBox = xForm.getBBox();
        int width = (int) bBox.getWidth();
        int height = (int) bBox.getHeight();
        if (width == 0) {
            width = 1;
        } else if (width >= 32767) {
            width = this.xFormBuffer.getWidth();
        }
        if (height == 0) {
            height = 1;
        } else if (height >= 32767) {
            height = this.xFormBuffer.getHeight();
        }
        BufferedImage bi2 = new BufferedImage(width, height, 2);
        Graphics2D canvas = bi2.createGraphics();
        canvas.setRenderingHints(renderingHints);
        Shapes xFormShapes = xForm.getShapes();
        if (xFormShapes != null) {
            xFormShapes.setPageParent(parentPage);
            if (!xForm.getResources().isShading()) {
                canvas.translate(-((int) bBox.getX()), -((int) bBox.getY()));
                canvas.setClip(bBox);
                xFormShapes.paint(canvas);
                xFormShapes.setPageParent(null);
            }
        }
        canvas.dispose();
        return bi2;
    }
}
