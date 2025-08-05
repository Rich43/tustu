package org.icepdf.ri.common.views.annotations;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.CircleAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/CircleAnnotationComponent.class */
public class CircleAnnotationComponent extends MarkupAnnotationComponent {
    public CircleAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(annotation, documentViewController, pageViewComponent, documentViewModel);
        this.isShowInvisibleBorder = false;
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, javax.swing.JComponent
    public void paintComponent(Graphics g2) {
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, org.icepdf.ri.common.views.AnnotationComponent
    public void resetAppearanceShapes() {
        refreshAnnotationRect();
        CircleAnnotation squareAnnotation = (CircleAnnotation) this.annotation;
        squareAnnotation.resetAppearanceStream(getPageTransform());
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent me) {
        super.mouseDragged(me);
        resetAppearanceShapes();
    }
}
