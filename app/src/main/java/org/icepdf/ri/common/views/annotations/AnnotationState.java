package org.icepdf.ri.common.views.annotations;

import java.awt.Component;
import java.awt.geom.Rectangle2D;
import org.icepdf.core.Memento;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.PageTree;
import org.icepdf.core.pobjects.annotations.Annotation;
import org.icepdf.core.pobjects.annotations.BorderStyle;
import org.icepdf.ri.common.views.AnnotationComponent;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/AnnotationState.class */
public class AnnotationState implements Memento {
    protected Rectangle2D.Float userSpaceRectangle;
    protected AnnotationComponent annotationComponent;

    public AnnotationState(AnnotationComponent annotationComponent) {
        this.annotationComponent = annotationComponent;
    }

    public void apply(AnnotationState applyState) {
        Rectangle2D.Float rect = applyState.userSpaceRectangle;
        if (rect != null) {
            this.userSpaceRectangle = new Rectangle2D.Float(rect.f12404x, rect.f12405y, rect.width, rect.height);
        }
        restore();
    }

    @Override // org.icepdf.core.Memento
    public void restore() {
        if (this.annotationComponent != null && this.annotationComponent.getAnnotation() != null) {
            Annotation annotation = this.annotationComponent.getAnnotation();
            restore(annotation);
            synchronizeState();
        }
    }

    public void restore(Annotation annotation) {
        if (annotation.getBorderStyle() == null) {
            annotation.setBorderStyle(new BorderStyle());
        }
        annotation.setUserSpaceRectangle(this.userSpaceRectangle);
    }

    public void synchronizeState() {
        int pageIndex = this.annotationComponent.getPageIndex();
        Document document = this.annotationComponent.getDocument();
        Annotation annotation = this.annotationComponent.getAnnotation();
        PageTree pageTree = document.getPageTree();
        Page page = pageTree.getPage(pageIndex);
        if (!annotation.isDeleted()) {
            page.updateAnnotation(annotation);
            this.annotationComponent.refreshDirtyBounds();
        } else {
            annotation.setDeleted(false);
            page.addAnnotation(annotation);
            ((Component) this.annotationComponent).setVisible(true);
            this.annotationComponent.refreshDirtyBounds();
        }
    }
}
