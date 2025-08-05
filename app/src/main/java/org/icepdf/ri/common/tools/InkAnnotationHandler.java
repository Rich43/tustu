package org.icepdf.ri.common.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.AnnotationFactory;
import org.icepdf.core.pobjects.annotations.BorderStyle;
import org.icepdf.core.pobjects.annotations.InkAnnotation;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.AnnotationComponentFactory;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/InkAnnotationHandler.class */
public class InkAnnotationHandler extends CommonToolHandler implements ToolHandler {
    private static final Logger logger = Logger.getLogger(LineAnnotationHandler.class.toString());
    protected static BasicStroke stroke = new BasicStroke(1.0f, 0, 0, 1.0f);
    protected static Color lineColor;
    protected GeneralPath inkPath;
    protected BorderStyle borderStyle;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.annotation.ink.line.color", "#00ff00");
            int colorValue = ColorUtil.convertColor(color);
            lineColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("00ff00", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading Ink Annotation line colour");
            }
        }
    }

    public InkAnnotationHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
        this.borderStyle = new BorderStyle();
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
        if (this.inkPath != null) {
            Graphics2D gg = (Graphics2D) g2;
            Color oldColor = gg.getColor();
            Stroke oldStroke = gg.getStroke();
            gg.setColor(lineColor);
            gg.setStroke(stroke);
            gg.draw(this.inkPath);
            gg.setColor(oldColor);
            gg.setStroke(oldStroke);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if (this.pageViewComponent != null) {
            this.pageViewComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        if (this.inkPath == null) {
            this.inkPath = new GeneralPath();
        }
        this.inkPath.moveTo(e2.getX(), e2.getY());
        this.pageViewComponent.repaint();
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        this.inkPath.moveTo(e2.getX(), e2.getY());
        Rectangle bBox = this.inkPath.getBounds();
        bBox.setRect(bBox.getX() - 5.0d, bBox.getY() - 5.0d, bBox.getWidth() + 10.0d, bBox.getHeight() + 10.0d);
        Shape tInkPath = convertToPageSpace(this.inkPath);
        Rectangle tBbox = convertToPageSpace(bBox).getBounds();
        InkAnnotation annotation = (InkAnnotation) AnnotationFactory.buildAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), Annotation.SUBTYPE_INK, tBbox);
        annotation.setColor(lineColor);
        annotation.setBorderStyle(this.borderStyle);
        annotation.setInkPath(tInkPath);
        annotation.setBBox(tBbox);
        annotation.resetAppearanceStream(getPageTransform());
        AbstractAnnotationComponent comp = AnnotationComponentFactory.buildAnnotationComponent(annotation, this.documentViewController, this.pageViewComponent, this.documentViewModel);
        comp.setBounds(bBox);
        comp.refreshAnnotationRect();
        if (this.documentViewController.getAnnotationCallback() != null) {
            AnnotationCallback annotationCallback = this.documentViewController.getAnnotationCallback();
            annotationCallback.newAnnotation(this.pageViewComponent, comp);
        }
        this.documentViewController.getParentController().setDocumentToolMode(6);
        this.inkPath = null;
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void installTool() {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void uninstallTool() {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
        this.inkPath.lineTo(e2.getX(), e2.getY());
        this.pageViewComponent.repaint();
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent e2) {
    }
}
