package com.sun.javafx.tk;

import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/TKListener.class */
public interface TKListener {
    void changedTopLevelWindows(List<TKStage> list);

    void exitedLastNestedLoop();
}
