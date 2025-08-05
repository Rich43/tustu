package org.icepdf.ri.common.views;

import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.annotations.Annotation;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/AnnotationCallback.class */
public interface AnnotationCallback {
    void processAnnotationAction(Annotation annotation);

    void pageAnnotationsInitialized(Page page);

    void newAnnotation(PageViewComponent pageViewComponent, AnnotationComponent annotationComponent);

    void updateAnnotation(AnnotationComponent annotationComponent);

    void removeAnnotation(PageViewComponent pageViewComponent, AnnotationComponent annotationComponent);
}
