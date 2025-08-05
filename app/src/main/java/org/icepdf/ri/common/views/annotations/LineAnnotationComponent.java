package org.icepdf.ri.common.views.annotations;

import java.awt.Graphics;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.LineAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/LineAnnotationComponent.class */
public class LineAnnotationComponent extends MarkupAnnotationComponent {
    public LineAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(annotation, documentViewController, pageViewComponent, documentViewModel);
        this.isRollover = false;
        this.isResizable = false;
        this.isShowInvisibleBorder = false;
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, javax.swing.JComponent
    public void paintComponent(Graphics g2) {
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, org.icepdf.ri.common.views.AnnotationComponent
    public void resetAppearanceShapes() {
        LineAnnotation textMarkupAnnotation = (LineAnnotation) this.annotation;
        textMarkupAnnotation.resetAppearanceStream(getPageTransform());
    }
}
