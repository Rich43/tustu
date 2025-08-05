package org.icepdf.ri.common.views.annotations;

import java.awt.Graphics;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.InkAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/InkAnnotationComponent.class */
public class InkAnnotationComponent extends MarkupAnnotationComponent {
    public InkAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(annotation, documentViewController, pageViewComponent, documentViewModel);
        this.isShowInvisibleBorder = false;
        this.isResizable = false;
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, org.icepdf.ri.common.views.AnnotationComponent
    public void resetAppearanceShapes() {
        refreshAnnotationRect();
        InkAnnotation inkAnnotation = (InkAnnotation) this.annotation;
        inkAnnotation.resetAppearanceStream(getPageTransform());
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, javax.swing.JComponent
    public void paintComponent(Graphics g2) {
    }
}
