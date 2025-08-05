package org.icepdf.ri.common.views;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.annotations.Annotation;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/AnnotationComponent.class */
public interface AnnotationComponent {
    Annotation getAnnotation();

    void refreshDirtyBounds();

    void refreshAnnotationRect();

    boolean hasFocus();

    boolean isEditable();

    boolean isShowInvisibleBorder();

    boolean isRollover();

    boolean isMovable();

    boolean isResizable();

    boolean isBorderStyle();

    boolean isSelected();

    Document getDocument();

    int getPageIndex();

    PageViewComponent getParentPageView();

    void setSelected(boolean z2);

    void repaint();

    void resetAppearanceShapes();

    PageViewComponent getPageViewComponent();

    void dispose();
}
