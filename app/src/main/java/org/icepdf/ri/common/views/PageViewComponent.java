package org.icepdf.ri.common.views;

import java.awt.Point;
import java.awt.Rectangle;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/PageViewComponent.class */
public interface PageViewComponent {
    void setDocumentViewCallback(DocumentView documentView);

    int getPageIndex();

    void init();

    void invalidatePage();

    void invalidatePageBuffer();

    void dispose();

    void invalidate();

    boolean isShowing();

    void clearSelectedText();

    void setSelectionRectangle(Point point, Rectangle rectangle);

    void clearSelectionRectangle();

    void addAnnotation(AnnotationComponent annotationComponent);

    void removeAnnotation(AnnotationComponent annotationComponent);

    void setToolMode(int i2);
}
