package org.icepdf.ri.common.views.annotations;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.PDate;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.MarkupAnnotation;
import org.icepdf.core.pobjects.annotations.PopupAnnotation;
import org.icepdf.ri.common.tools.TextAnnotationHandler;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.AnnotationCallback;
import org.icepdf.ri.common.views.AnnotationComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/MarkupAnnotationComponent.class */
public abstract class MarkupAnnotationComponent extends AbstractAnnotationComponent {
    private static final Logger logger = Logger.getLogger(TextAnnotationComponent.class.toString());
    protected MarkupAnnotation markupAnnotation;

    public MarkupAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(annotation, documentViewController, pageViewComponent, documentViewModel);
        if (annotation instanceof MarkupAnnotation) {
            this.markupAnnotation = (MarkupAnnotation) annotation;
        }
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        super.mouseClicked(e2);
        if (e2.getClickCount() == 2 && this.markupAnnotation != null) {
            PopupAnnotation popup = this.markupAnnotation.getPopupAnnotation();
            if (popup != null) {
                popup.setOpen(!popup.isOpen());
                ArrayList<AnnotationComponent> annotationComponents = this.pageViewComponent.getAnnotationComponents();
                Reference popupReference = popup.getPObjectReference();
                Iterator i$ = annotationComponents.iterator();
                while (i$.hasNext()) {
                    AnnotationComponent annotationComponent = i$.next();
                    Reference compReference = annotationComponent.getAnnotation().getPObjectReference();
                    if (compReference.equals(popupReference)) {
                        if (annotationComponent instanceof PopupAnnotationComponent) {
                            PopupAnnotationComponent popupComponent = (PopupAnnotationComponent) annotationComponent;
                            popupComponent.setVisible(popup.isOpen());
                            Rectangle popupBounds = popupComponent.getBounds();
                            Rectangle pageBounds = this.pageViewComponent.getBounds();
                            if (!pageBounds.contains(popupBounds.getX(), popupBounds.getY(), popupBounds.getWidth(), popupBounds.getHeight())) {
                                int x2 = popupBounds.f12372x;
                                int y2 = popupBounds.f12373y;
                                if (x2 + popupBounds.width > pageBounds.width) {
                                    x2 -= popupBounds.width - (pageBounds.width - popupBounds.f12372x);
                                }
                                if (y2 + popupBounds.height > pageBounds.height) {
                                    y2 -= popupBounds.height - (pageBounds.height - popupBounds.f12373y);
                                }
                                popupBounds.setLocation(x2, y2);
                                popupComponent.setBounds(popupBounds);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                }
                return;
            }
            Rectangle bounds = getBounds();
            Rectangle bBox = new Rectangle(bounds.f12372x, bounds.f12373y, 215, 150);
            Rectangle tBbox = convertToPageSpace(bBox).getBounds();
            if (this.markupAnnotation != null) {
                this.markupAnnotation.setCreationDate(PDate.formatDateTime(new Date()));
                this.markupAnnotation.setTitleText(System.getProperty("user.name"));
                this.markupAnnotation.setContents("");
            }
            PopupAnnotation annotation = TextAnnotationHandler.createPopupAnnotation(this.documentViewModel.getDocument().getPageTree().getLibrary(), tBbox, this.markupAnnotation, getPageTransform());
            AbstractAnnotationComponent comp = AnnotationComponentFactory.buildAnnotationComponent(annotation, this.documentViewController, this.pageViewComponent, this.documentViewModel);
            comp.setBounds(bBox);
            comp.refreshAnnotationRect();
            if (this.documentViewController.getAnnotationCallback() != null) {
                AnnotationCallback annotationCallback = this.documentViewController.getAnnotationCallback();
                annotationCallback.newAnnotation(this.pageViewComponent, comp);
            }
            this.pageViewComponent.revalidate();
        }
    }

    protected Shape convertToPageSpace(Shape shape) {
        Page currentPage = this.pageViewComponent.getPage();
        AffineTransform at2 = currentPage.getPageTransform(this.documentViewModel.getPageBoundary(), this.documentViewModel.getViewRotation(), this.documentViewModel.getViewZoom());
        try {
            at2 = at2.createInverse();
        } catch (NoninvertibleTransformException e2) {
            logger.log(Level.FINE, "Error converting to page space.", (Throwable) e2);
        }
        return at2.createTransformedShape(shape);
    }
}
