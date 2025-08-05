package org.icepdf.ri.common.views.annotations;

import java.awt.Graphics;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.TextMarkupAnnotation;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/TextMarkupAnnotationComponent.class */
public class TextMarkupAnnotationComponent extends MarkupAnnotationComponent {
    private static final Logger logger = Logger.getLogger(TextMarkupAnnotationComponent.class.toString());

    public TextMarkupAnnotationComponent(Annotation annotation, DocumentViewController documentViewController, AbstractPageViewComponent pageViewComponent, DocumentViewModel documentViewModel) {
        super(annotation, documentViewController, pageViewComponent, documentViewModel);
        this.isMovable = false;
        this.isResizable = false;
        this.isShowInvisibleBorder = false;
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, org.icepdf.ri.common.views.AnnotationComponent
    public void resetAppearanceShapes() {
        TextMarkupAnnotation textMarkupAnnotation = (TextMarkupAnnotation) this.annotation;
        textMarkupAnnotation.resetAppearanceStream(getPageTransform());
    }

    @Override // org.icepdf.ri.common.views.annotations.AbstractAnnotationComponent, javax.swing.JComponent
    public void paintComponent(Graphics g2) {
    }
}
