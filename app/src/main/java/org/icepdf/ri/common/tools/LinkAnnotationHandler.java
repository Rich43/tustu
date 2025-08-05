package org.icepdf.ri.common.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;
import javax.swing.event.MouseInputListener;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.AnnotationFactory;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.AnnotationComponentFactory;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/LinkAnnotationHandler.class */
public class LinkAnnotationHandler extends SelectionBoxHandler implements ToolHandler, MouseInputListener {
    private static final Logger logger = Logger.getLogger(LinkAnnotationHandler.class.toString());

    public LinkAnnotationHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
        selectionBoxColour = Color.GRAY;
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if (this.pageViewComponent != null) {
            this.pageViewComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
        int x2 = e2.getX();
        int y2 = e2.getY();
        this.currentRect = new Rectangle(x2, y2, 0, 0);
        updateDrawableRect(this.pageViewComponent.getWidth(), this.pageViewComponent.getHeight());
        this.pageViewComponent.repaint();
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        updateSelectionSize(e2, this.pageViewComponent);
        if (this.rectToDraw.getWidth() < 5.0d || this.rectToDraw.getHeight() < 5.0d) {
            this.rectToDraw.setSize(new Dimension(15, 25));
        }
        Rectangle tBbox = convertToPageSpace(this.rectToDraw).getBounds();
        Annotation annotation = AnnotationFactory.buildAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), Annotation.SUBTYPE_LINK, tBbox);
        AbstractAnnotationComponent comp = AnnotationComponentFactory.buildAnnotationComponent(annotation, this.documentViewController, this.pageViewComponent, this.documentViewModel);
        comp.setBounds(this.rectToDraw);
        comp.refreshAnnotationRect();
        if (this.documentViewController.getAnnotationCallback() != null) {
            AnnotationCallback annotationCallback = this.documentViewController.getAnnotationCallback();
            annotationCallback.newAnnotation(this.pageViewComponent, comp);
        }
        this.documentViewController.getParentController().setDocumentToolMode(6);
        clearRectangle(this.pageViewComponent);
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
        updateSelectionSize(e2, this.pageViewComponent);
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

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
        paintSelectionBox(g2, this.rectToDraw);
    }
}
