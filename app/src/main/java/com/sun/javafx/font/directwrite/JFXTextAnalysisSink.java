package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/JFXTextAnalysisSink.class */
class JFXTextAnalysisSink extends IUnknown {
    JFXTextAnalysisSink(long ptr) {
        super(ptr);
    }

    boolean Next() {
        return OS.Next(this.ptr);
    }

    int GetStart() {
        return OS.GetStart(this.ptr);
    }

    int GetLength() {
        return OS.GetLength(this.ptr);
    }

    DWRITE_SCRIPT_ANALYSIS GetAnalysis() {
        return OS.GetAnalysis(this.ptr);
    }
}
