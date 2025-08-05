package org.icepdf.ri.common.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.PDate;
import org.icepdf.core.pobjects.PObject;
import org.icepdf.core.pobjects.StateManager;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.AnnotationFactory;
import org.icepdf.core.pobjects.annotations.MarkupAnnotation;
import org.icepdf.core.pobjects.annotations.PopupAnnotation;
import org.icepdf.core.pobjects.annotations.TextAnnotation;
import org.icepdf.core.util.ColorUtil;
import org.icepdf.core.util.Defs;
import org.icepdf.core.util.Library;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;
import org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent;
import org.icepdf.ri.common.views.annotations.AnnotationComponentFactory;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/tools/TextAnnotationHandler.class */
public class TextAnnotationHandler extends CommonToolHandler implements ToolHandler {
    private static final Logger logger = Logger.getLogger(TextAnnotationHandler.class.toString());
    protected static Color defaultFillColor;
    protected static final Dimension ICON_SIZE;

    static {
        try {
            String color = Defs.sysProperty("org.icepdf.core.views.page.annotation.text.fill.color", "#ffff00");
            int colorValue = ColorUtil.convertColor(color);
            defaultFillColor = new Color(colorValue >= 0 ? colorValue : Integer.parseInt("ffff00", 16));
        } catch (NumberFormatException e2) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Error reading text annotation fill colour");
            }
        }
        ICON_SIZE = new Dimension(23, 23);
    }

    public TextAnnotationHandler(DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(documentViewController, pageViewComponent, documentViewModel);
    }

    @Override // org.icepdf.ri.common.tools.ToolHandler
    public void paintTool(Graphics g2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if (this.pageViewComponent != null) {
            this.pageViewComponent.requestFocus();
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
    }

    public static TextAnnotation createTextAnnotation(Library library, Rectangle bbox, AffineTransform pageSpace) {
        TextAnnotation textAnnotation = (TextAnnotation) AnnotationFactory.buildAnnotation(library, Annotation.SUBTYPE_TEXT, bbox);
        textAnnotation.setCreationDate(PDate.formatDateTime(new Date()));
        textAnnotation.setTitleText(System.getProperty("user.name"));
        textAnnotation.setContents("");
        textAnnotation.setIconName(TextAnnotation.COMMENT_ICON);
        textAnnotation.setState(TextAnnotation.STATE_UNMARKED);
        textAnnotation.setColor(defaultFillColor);
        textAnnotation.setBBox(bbox);
        textAnnotation.resetAppearanceStream(pageSpace);
        return textAnnotation;
    }

    public static PopupAnnotation createPopupAnnotation(Library library, Rectangle bbox, MarkupAnnotation parent, AffineTransform pageSpace) {
        PopupAnnotation popupAnnotation = (PopupAnnotation) AnnotationFactory.buildAnnotation(library, Annotation.SUBTYPE_POPUP, bbox);
        StateManager stateManager = library.getStateManager();
        stateManager.addChange(new PObject(popupAnnotation, popupAnnotation.getPObjectReference()));
        library.addObject(popupAnnotation, popupAnnotation.getPObjectReference());
        popupAnnotation.setOpen(true);
        popupAnnotation.setParent(parent);
        parent.setPopupAnnotation(popupAnnotation);
        popupAnnotation.resetAppearanceStream(0.0d, 0.0d, pageSpace);
        return popupAnnotation;
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
        AffineTransform pageTransform = getPageTransform();
        AffineTransform pageInverseTransform = new AffineTransform();
        try {
            pageInverseTransform = pageTransform.createInverse();
        } catch (NoninvertibleTransformException ex) {
            logger.log(Level.FINE, "Error converting to page space.", (Throwable) ex);
        }
        Dimension scaledSize = new Dimension((int) Math.abs(ICON_SIZE.width * pageInverseTransform.getScaleX()), (int) Math.abs(ICON_SIZE.height * pageInverseTransform.getScaleY()));
        Rectangle bBox = new Rectangle(e2.getX(), e2.getY(), scaledSize.width, scaledSize.height);
        Rectangle tBbox = convertToPageSpace(bBox).getBounds();
        TextAnnotation markupAnnotation = createTextAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), tBbox, pageTransform);
        AbstractAnnotationComponent comp = AnnotationComponentFactory.buildAnnotationComponent(markupAnnotation, this.documentViewController, this.pageViewComponent, this.documentViewModel);
        comp.setBounds(bBox);
        comp.refreshAnnotationRect();
        if (this.documentViewController.getAnnotationCallback() != null) {
            AnnotationCallback annotationCallback = this.documentViewController.getAnnotationCallback();
            annotationCallback.newAnnotation(this.pageViewComponent, comp);
        }
        Rectangle bBox2 = new Rectangle(e2.getX() + (scaledSize.width / 2), e2.getY() + (scaledSize.height / 2), (int) Math.abs(215.0d * pageInverseTransform.getScaleX()), (int) Math.abs(150.0d * pageInverseTransform.getScaleY()));
        Rectangle pageBounds = this.pageViewComponent.getBounds();
        if (!pageBounds.contains(bBox2.getX(), bBox2.getY(), bBox2.getWidth(), bBox2.getHeight())) {
            bBox2.setLocation(bBox2.f12372x - bBox2.width, bBox2.f12373y - bBox2.height);
        }
        Rectangle tBbox2 = convertToPageSpace(bBox2).getBounds();
        PopupAnnotation popupAnnotation = createPopupAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), tBbox2, markupAnnotation, pageTransform);
        AbstractAnnotationComponent comp2 = AnnotationComponentFactory.buildAnnotationComponent(popupAnnotation, this.documentViewController, this.pageViewComponent, this.documentViewModel);
        comp2.setBounds(bBox2);
        comp2.refreshAnnotationRect();
        if (this.documentViewController.getAnnotationCallback() != null) {
            AnnotationCallback annotationCallback2 = this.documentViewController.getAnnotationCallback();
            annotationCallback2.newAnnotation(this.pageViewComponent, comp2);
        }
        this.documentViewController.getParentController().setDocumentToolMode(6);
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent e2) {
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
}
