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
import java.awt.geom.Point2D;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.PDate;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.AnnotationFactory;
import org.icepdf.core.pobjects.annotations.BorderStyle;
import org.icepdf.core.pobjects.annotations.LineAnnotation;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.AnnotationComponentFactory;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/LineAnnotationHandler.class */
public class LineAnnotationHandler extends SelectionBoxHandler implements ToolHandler {
    private static final Logger logger = Logger.getLogger(LineAnnotationHandler.class.toString());
    protected static BasicStroke stroke = new BasicStroke(1.0f, 0, 0, 1.0f);
    protected static Color lineColor;
    protected static Color internalColor;
    protected static Name startLineEnding;
    protected static Name endLineEnding;
    protected Point2D startOfLine;
    protected Point2D endOfLine;
    protected BorderStyle borderStyle;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.annotation.line.stroke.color", "#ff0000");
            int colorValue = ColorUtil.convertColor(color);
            lineColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("ff0000", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading line Annotation stroke colour");
            }
        }
        try {
            String color2 = Defs.sysProperty("org.icepdf.core.views.page.annotation.line.fill.color", "#ff0000");
            int colorValue2 = ColorUtil.convertColor(color2);
            internalColor = new Color(colorValue2 >= 0 ? colorValue2 : Integer.parseInt("ff0000", 16));
        } catch (NumberFormatException e3) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading line Annotation fill colour");
            }
        }
        startLineEnding = LineAnnotation.LINE_END_NONE;
        endLineEnding = LineAnnotation.LINE_END_NONE;
    }

    public LineAnnotationHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
        this.borderStyle = new BorderStyle();
        startLineEnding = LineAnnotation.LINE_END_NONE;
        endLineEnding = LineAnnotation.LINE_END_NONE;
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
        if (this.startOfLine != null && this.endOfLine != null) {
            Graphics2D gg = (Graphics2D) g2;
            Color oldColor = gg.getColor();
            Stroke oldStroke = gg.getStroke();
            g2.setColor(lineColor);
            gg.setStroke(stroke);
            g2.drawLine((int) this.startOfLine.getX(), (int) this.startOfLine.getY(), (int) this.endOfLine.getX(), (int) this.endOfLine.getY());
            g2.setColor(oldColor);
            gg.setStroke(oldStroke);
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        this.startOfLine = e2.getPoint();
        int x2 = e2.getX();
        int y2 = e2.getY();
        this.currentRect = new Rectangle(x2, y2, 0, 0);
        updateDrawableRect(this.pageViewComponent.getWidth(), this.pageViewComponent.getHeight());
        this.pageViewComponent.repaint();
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        this.endOfLine = e2.getPoint();
        updateSelectionSize(e2, this.pageViewComponent);
        this.rectToDraw.setRect(this.rectToDraw.getX() - 8.0d, this.rectToDraw.getY() - 8.0d, this.rectToDraw.getWidth() + 16.0d, this.rectToDraw.getHeight() + 16.0d);
        Rectangle tBbox = convertToPageSpace();
        LineAnnotation annotation = (LineAnnotation) AnnotationFactory.buildAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), Annotation.SUBTYPE_LINE, tBbox);
        annotation.setStartArrow(startLineEnding);
        annotation.setEndArrow(endLineEnding);
        annotation.setStartOfLine(this.startOfLine);
        annotation.setEndOfLine(this.endOfLine);
        annotation.setBorderStyle(this.borderStyle);
        annotation.setColor(lineColor);
        annotation.setInteriorColor(internalColor);
        annotation.setContents(annotation.getSubType().toString());
        annotation.setCreationDate(PDate.formatDateTime(new Date()));
        annotation.setTitleText(System.getProperty("user.name"));
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
        clearRectangle(this.pageViewComponent);
        this.endOfLine = null;
        this.startOfLine = null;
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
        updateSelectionSize(e2, this.pageViewComponent);
        this.endOfLine = e2.getPoint();
        this.pageViewComponent.repaint();
    }

    protected Rectangle convertToPageSpace() {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        try {
            at2 = at2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.FINE, "Error converting to page space.", (Throwable) e2);
        }
        Rectangle tBbox = new Rectangle(this.rectToDraw.f12372x, this.rectToDraw.f12373y, this.rectToDraw.width, this.rectToDraw.height);
        Rectangle tBbox2 = at2.createTransformedShape(tBbox).getBounds();
        this.startOfLine = at2.transform(this.startOfLine, null);
        this.endOfLine = at2.transform(this.endOfLine, null);
        return tBbox2;
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if (this.pageViewComponent != null) {
            this.pageViewComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
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

    @Override // org.icepdf.ri.common.tools.SelectionBoxHandler
    public void setSelectionRectangle(Point cursorLocation, Rectangle selection) {
    }
}
