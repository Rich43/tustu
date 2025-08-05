package org.icepdf.ri.common.views;

import java.awt.Adjustable;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyListener;
import javax.swing.JViewport;
import org.icepdf.core.SecurityCallback;
import org.icepdf.core.pobjects.Destination;
import org.icepdf.core.pobjects.Document;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/DocumentViewController.class */
public interface DocumentViewController {
    public static final int PAGE_FIT_NONE = 1;
    public static final int PAGE_FIT_ACTUAL_SIZE = 2;
    public static final int PAGE_FIT_WINDOW_HEIGHT = 3;
    public static final int PAGE_FIT_WINDOW_WIDTH = 4;
    public static final int CURSOR_HAND_OPEN = 1;
    public static final int CURSOR_HAND_CLOSE = 2;
    public static final int CURSOR_ZOOM_IN = 3;
    public static final int CURSOR_ZOOM_OUT = 4;
    public static final int CURSOR_WAIT = 6;
    public static final int CURSOR_SELECT = 7;
    public static final int CURSOR_DEFAULT = 8;
    public static final int CURSOR_HAND_ANNOTATION = 9;
    public static final int CURSOR_TEXT_SELECTION = 10;
    public static final int CURSOR_CROSSHAIR = 11;
    public static final int CURSOR_MAGNIFY = 12;

    void setDocument(Document document);

    Document getDocument();

    void closeDocument();

    void dispose();

    Container getViewContainer();

    Controller getParentController();

    void setViewType(int i2);

    int getViewMode();

    boolean setFitMode(int i2);

    int getFitMode();

    void setDocumentViewType(int i2, int i3);

    boolean setCurrentPageIndex(int i2);

    int setCurrentPageNext();

    int setCurrentPagePrevious();

    void setDestinationTarget(Destination destination);

    int getCurrentPageIndex();

    int getCurrentPageDisplayValue();

    void setZoomLevels(float[] fArr);

    float[] getZoomLevels();

    boolean setZoom(float f2);

    boolean setZoomIn();

    boolean setZoomIn(Point point);

    boolean setZoomCentered(float f2, Point point, boolean z2);

    boolean setZoomToViewPort(float f2, Point point, int i2, boolean z2);

    boolean setZoomOut();

    boolean setZoomOut(Point point);

    float getZoom();

    boolean setRotation(float f2);

    float getRotation();

    float setRotateRight();

    float setRotateLeft();

    boolean setToolMode(int i2);

    int getToolMode();

    boolean isToolModeSelected(int i2);

    void requestViewFocusInWindow();

    void setViewCursor(int i2);

    Cursor getViewCursor(int i2);

    int getViewCursor();

    void setViewKeyListener(KeyListener keyListener);

    Adjustable getHorizontalScrollBar();

    Adjustable getVerticalScrollBar();

    JViewport getViewPort();

    void setAnnotationCallback(AnnotationCallback annotationCallback);

    void setSecurityCallback(SecurityCallback securityCallback);

    void deleteCurrentAnnotation();

    void deleteAnnotation(AnnotationComponent annotationComponent);

    void undo();

    void redo();

    AnnotationCallback getAnnotationCallback();

    SecurityCallback getSecurityCallback();

    DocumentViewModel getDocumentViewModel();

    void clearSelectedText();

    void clearHighlightedText();

    void clearSelectedAnnotations();

    void assignSelectedAnnotation(AnnotationComponent annotationComponent);

    void selectAllText();

    String getSelectedText();

    void firePropertyChange(String str, int i2, int i3);

    void firePropertyChange(String str, Object obj, Object obj2);
}
