package org.icepdf.ri.common.views.annotations;

import java.awt.Graphics;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.TextMarkupAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/AnnotationComponentFactory.class */
public class AnnotationComponentFactory {
    private AnnotationComponentFactory() {
    }

    public static AbstractAnnotationComponent buildAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        Name subtype = annotation.getSubType();
        if (subtype != null) {
            if (Annotation.SUBTYPE_LINK.equals(subtype)) {
                return new LinkAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (TextMarkupAnnotation.isTextMarkupAnnotation(subtype)) {
                return new TextMarkupAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_LINE.equals(subtype)) {
                return new LineAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_CIRCLE.equals(subtype)) {
                return new CircleAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_POLYGON.equals(subtype)) {
                return new PolygonAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_POLYLINE.equals(subtype)) {
                return new PolyLineAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_SQUARE.equals(subtype)) {
                return new SquareAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_POPUP.equals(subtype)) {
                return new PopupAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_TEXT.equals(subtype)) {
                return new TextAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_INK.equals(subtype)) {
                return new InkAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_FREE_TEXT.equals(subtype)) {
                return new FreeTextAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            if (Annotation.SUBTYPE_WIDGET.equals(subtype)) {
                return new WidgetAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel);
            }
            return new AbstractAnnotationComponent(annotation, documentViewController, pageViewComponent, documentViewModel) { // from class: org.icepdf.ri.common.views.annotations.AnnotationComponentFactory.1
                @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, org.icepdf.ri.common.views.AnnotationComponent
                public void resetAppearanceShapes() {
                }

                @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, javax.swing.JComponent
                public void paintComponent(Graphics g2) {
                }
            };
        }
        return null;
    }
}
