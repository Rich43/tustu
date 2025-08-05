package com.sun.javafx.tk;

import javafx.scene.input.TransferMode;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/TKDropTargetListener.class */
public interface TKDropTargetListener {
    TransferMode dragEnter(double d2, double d3, double d4, double d5, TransferMode transferMode, TKClipboard tKClipboard);

    TransferMode dragOver(double d2, double d3, double d4, double d5, TransferMode transferMode);

    void dragExit(double d2, double d3, double d4, double d5);

    TransferMode drop(double d2, double d3, double d4, double d5, TransferMode transferMode);
}
