package org.icepdf.ri.common.views;

import java.util.ResourceBundle;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.search.DocumentSearchController;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/Controller.class */
public interface Controller {
    Document getDocument();

    int getCurrentPageNumber();

    float getUserRotation();

    float getUserZoom();

    DocumentViewController getDocumentViewController();

    DocumentSearchController getDocumentSearchController();

    void setDocumentToolMode(int i2);

    ResourceBundle getMessageBundle();
}
