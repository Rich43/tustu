package com.sun.javafx.tk;

import java.security.AccessControlContext;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.TransferMode;
import javafx.util.Pair;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/TKClipboard.class */
public interface TKClipboard {
    void setSecurityContext(AccessControlContext accessControlContext);

    Set<DataFormat> getContentTypes();

    boolean putContent(Pair<DataFormat, Object>... pairArr);

    Object getContent(DataFormat dataFormat);

    boolean hasContent(DataFormat dataFormat);

    Set<TransferMode> getTransferModes();

    void setDragView(Image image);

    void setDragViewOffsetX(double d2);

    void setDragViewOffsetY(double d2);

    Image getDragView();

    double getDragViewOffsetX();

    double getDragViewOffsetY();
}
