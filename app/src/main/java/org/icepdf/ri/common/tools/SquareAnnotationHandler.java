package org.icepdf.ri.common.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.AnnotationFactory;
import org.icepdf.core.pobjects.annotations.BorderStyle;
import org.icepdf.core.pobjects.annotations.SquareAnnotation;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.AnnotationComponentFactory;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/SquareAnnotationHandler.class */
public class SquareAnnotationHandler extends SelectionBoxHandler implements ToolHandler {
    private static final Logger logger = Logger.getLogger(SquareAnnotationHandler.class.toString());
    protected static final float DEFAULT_STROKE_WIDTH = 3.0f;
    protected static BasicStroke stroke = new BasicStroke(DEFAULT_STROKE_WIDTH, 0, 0, 1.0f);
    protected static Color lineColor;
    protected static Color internalColor;
    protected Rectangle rectangle;
    protected BorderStyle borderStyle;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.annotation.squareCircle.stroke.color", "#ff0000");
            int colorValue = ColorUtil.convertColor(color);
            lineColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("ff0000", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading squareCircle Annotation stroke colour");
            }
        }
        try {
            String color2 = Defs.sysProperty("org.icepdf.core.views.page.annotation.squareCircle.fill.color", "#ffffff");
            int colorValue2 = ColorUtil.convertColor(color2);
            internalColor = new Color(colorValue2 >= 0 ? colorValue2 : Integer.parseInt("ffffff", 16));
        } catch (NumberFormatException e3) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading squareCircle Annotation fill colour");
            }
        }
    }

    public SquareAnnotationHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
        this.borderStyle = new BorderStyle();
        this.borderStyle.setStrokeWidth(DEFAULT_STROKE_WIDTH);
    }

    public void paintTool(Graphics g2) {
        if (this.rectangle != null) {
            Graphics2D gg = (Graphics2D) g2;
            Color oldColor = gg.getColor();
            Stroke oldStroke = gg.getStroke();
            gg.setStroke(stroke);
            gg.setColor(lineColor);
            gg.draw(this.rectangle);
            g2.setColor(oldColor);
            gg.setStroke(oldStroke);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        int x2 = e2.getX();
        int y2 = e2.getY();
        if (this.rectangle == null) {
            this.rectangle = new Rectangle();
        }
        this.currentRect = new Rectangle(x2, y2, 0, 0);
        updateDrawableRect(this.pageViewComponent.getWidth(), this.pageViewComponent.getHeight());
        this.rectangle.setRect(this.currentRect);
        this.pageViewComponent.repaint();
    }

    public void mouseReleased(MouseEvent e2) {
        updateSelectionSize(e2, this.pageViewComponent);
        this.rectangle = convertToPageSpace(this.rectangle);
        this.rectToDraw.setRect(this.rectToDraw.getX() - 3.0d, this.rectToDraw.getY() - 3.0d, this.rectToDraw.getWidth() + 6.0d, this.rectToDraw.getHeight() + 6.0d);
        Rectangle tBbox = convertToPageSpace(this.rectToDraw);
        SquareAnnotation annotation = (SquareAnnotation) AnnotationFactory.buildAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), Annotation.SUBTYPE_SQUARE, tBbox);
        annotation.setColor(lineColor);
        if (annotation.isFillColor()) {
            annotation.setFillColor(internalColor);
        }
        annotation.setRectangle(this.rectangle);
        annotation.setBorderStyle(this.borderStyle);
        annotation.setBBox(tBbox);
        annotation.resetAppearanceStream(getPageTransform());
        AbstractAnnotationComponent comp = AnnotationComponentFactory.buildAnnotationComponent(annotation, this.documentViewController, this.pageViewComponent, this.documentViewModel);
        Rectangle bbox = new Rectangle(this.rectToDraw.f12372x, this.rectToDraw.f12373y, this.rectToDraw.width, this.rectToDraw.height);
        comp.setBounds(bbox);
        comp.refreshAnnotationRect();
        if (this.documentViewController.getAnnotationCallback() != null) {
            AnnotationCallback annotationCallback = this.documentViewController.getAnnotationCallback();
            annotationCallback.newAnnotation(this.pageViewComponent, comp);
        }
        this.documentViewController.getParentController().setDocumentToolMode(6);
        this.rectangle = null;
        clearRectangle(this.pageViewComponent);
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if (this.pageViewComponent != null) {
            this.pageViewComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent e2) {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void installTool() {
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void uninstallTool() {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
        updateSelectionSize(e2, this.pageViewComponent);
        this.rectangle.setRect(this.rectToDraw);
        this.pageViewComponent.repaint();
    }

    protected Rectangle convertToPageSpace(Rectangle rect) {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        try {
            at2 = at2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.FINE, "Error converting to page space.", (Throwable) e2);
        }
        Rectangle tBbox = new Rectangle(rect.f12372x, rect.f12373y, rect.width, rect.height);
        return at2.createTransformedShape(tBbox).getBounds();
    }

    @Override // org.icepdf.ri.common.tools.SelectionBoxHandler
    public void setSelectionRectangle(Point cursorLocation, Rectangle selection) {
    }
}
