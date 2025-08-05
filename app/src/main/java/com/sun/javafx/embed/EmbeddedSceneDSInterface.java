package com.sun.javafx.embed;

import java.util.Set;
import javafx.scene.input.TransferMode;

/* loaded from: jfxrt.jar:com/sun/javafx/embed/EmbeddedSceneDSInterface.class */
public interface EmbeddedSceneDSInterface {
    Set<TransferMode> getSupportedActions();

    Object getData(String str);

    String[] getMimeTypes();

    boolean isMimeTypeAvailable(String str);

    void dragDropEnd(TransferMode transferMode);
}
