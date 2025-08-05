package org.icepdf.ri.common.views;

import java.awt.Dimension;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusListener;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/DocumentView.class */
public interface DocumentView extends AdjustmentListener, FocusListener {
    public static final int LEFT_VIEW = 0;
    public static final int RIGHT_VIEW = 1;

    int getNextPageIncrement();

    int getPreviousPageIncrement();

    Dimension getDocumentSize();

    DocumentViewController getParentViewController();

    DocumentViewModel getViewModel();

    void dispose();

    void updateDocumentView();

    void setToolMode(int i2);
}
