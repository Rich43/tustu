package org.icepdf.ri.common.views;

import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.icepdf.core.Memento;
import org.icepdf.core.pobjects.Document;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/DocumentViewModel.class */
public interface DocumentViewModel {
    public static final int DISPLAY_TOOL_PAN = 1;
    public static final int DISPLAY_TOOL_ZOOM_IN = 2;
    public static final int DISPLAY_TOOL_ZOOM_OUT = 3;
    public static final int DISPLAY_TOOL_ZOOM_DYNAMIC = 4;
    public static final int DISPLAY_TOOL_TEXT_SELECTION = 5;
    public static final int DISPLAY_TOOL_SELECTION = 6;
    public static final int DISPLAY_TOOL_LINK_ANNOTATION = 7;
    public static final int DISPLAY_TOOL_HIGHLIGHT_ANNOTATION = 8;
    public static final int DISPLAY_TOOL_UNDERLINE_ANNOTATION = 9;
    public static final int DISPLAY_TOOL_SQUIGGLY_ANNOTATION = 10;
    public static final int DISPLAY_TOOL_STRIKEOUT_ANNOTATION = 11;
    public static final int DISPLAY_TOOL_LINE_ANNOTATION = 12;
    public static final int DISPLAY_TOOL_LINE_ARROW_ANNOTATION = 13;
    public static final int DISPLAY_TOOL_SQUARE_ANNOTATION = 14;
    public static final int DISPLAY_TOOL_CIRCLE_ANNOTATION = 15;
    public static final int DISPLAY_TOOL_INK_ANNOTATION = 16;
    public static final int DISPLAY_TOOL_FREE_TEXT_ANNOTATION = 17;
    public static final int DISPLAY_TOOL_TEXT_ANNOTATION = 18;
    public static final int DISPLAY_TOOL_NONE = 50;
    public static final int DISPLAY_TOOL_WAIT = 51;

    Document getDocument();

    ArrayList<WeakReference<AbstractPageViewComponent>> getSelectedPageText();

    void addSelectedPageText(AbstractPageViewComponent abstractPageViewComponent);

    boolean isSelectAll();

    void setSelectAll(boolean z2);

    void clearSelectedPageText();

    List<AbstractPageViewComponent> getPageComponents();

    boolean setViewCurrentPageIndex(int i2);

    int getViewCurrentPageIndex();

    boolean setViewZoom(float f2);

    float getViewZoom();

    boolean setViewRotation(float f2);

    float getViewRotation();

    void invalidate();

    boolean setViewToolMode(int i2);

    int getViewToolMode();

    boolean isViewToolModeSelected(int i2);

    Rectangle getPageBounds(int i2);

    void dispose();

    void setPageBoundary(int i2);

    int getPageBoundary();

    AnnotationComponent getCurrentAnnotation();

    void setCurrentAnnotation(AnnotationComponent annotationComponent);

    void addMemento(Memento memento, Memento memento2);
}
