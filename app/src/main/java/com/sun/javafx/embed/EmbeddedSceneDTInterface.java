package com.sun.javafx.embed;

import javafx.scene.input.TransferMode;

/* loaded from: jfxrt.jar:com/sun/javafx/embed/EmbeddedSceneDTInterface.class */
public interface EmbeddedSceneDTInterface {
    TransferMode handleDragEnter(int i2, int i3, int i4, int i5, TransferMode transferMode, EmbeddedSceneDSInterface embeddedSceneDSInterface);

    void handleDragLeave();

    TransferMode handleDragDrop(int i2, int i3, int i4, int i5, TransferMode transferMode);

    TransferMode handleDragOver(int i2, int i3, int i4, int i5, TransferMode transferMode);
}
