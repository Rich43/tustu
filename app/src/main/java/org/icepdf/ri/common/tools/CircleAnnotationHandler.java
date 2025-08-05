package org.icepdf.ri.common.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.AnnotationFactory;
import org.icepdf.core.pobjects.annotations.CircleAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.AnnotationComponentFactory;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/CircleAnnotationHandler.class */
public class CircleAnnotationHandler extends SquareAnnotationHandler {
    public CircleAnnotationHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
    }

    @Override // org.icepdf.ri.common.tools.SquareAnnotationHandler, org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
        if (this.rectangle != null) {
            Ellipse2D.Double circle = new Ellipse2D.Double(this.rectangle.getMinX(), this.rectangle.getMinY(), this.rectangle.getWidth(), this.rectangle.getHeight());
            Graphics2D gg = (Graphics2D) g2;
            Color oldColor = gg.getColor();
            Stroke oldStroke = gg.getStroke();
            gg.setStroke(stroke);
            gg.setColor(lineColor);
            gg.draw(circle);
            g2.setColor(oldColor);
            gg.setStroke(oldStroke);
        }
    }

    @Override // org.icepdf.ri.common.tools.SquareAnnotationHandler, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        updateSelectionSize(e2, this.pageViewComponent);
        this.rectangle = convertToPageSpace(this.rectangle);
        this.rectToDraw.setRect(this.rectToDraw.getX() - 3.0d, this.rectToDraw.getY() - 3.0d, this.rectToDraw.getWidth() + 6.0d, this.rectToDraw.getHeight() + 6.0d);
        Rectangle tBbox = convertToPageSpace(this.rectToDraw);
        CircleAnnotation annotation = (CircleAnnotation) AnnotationFactory.buildAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), Annotation.SUBTYPE_CIRCLE, tBbox);
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
}
