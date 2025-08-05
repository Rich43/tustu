package com.sun.javafx.embed;

import com.sun.javafx.cursor.CursorFrame;

/* loaded from: jfxrt.jar:com/sun/javafx/embed/HostInterface.class */
public interface HostInterface {
    void setEmbeddedStage(EmbeddedStageInterface embeddedStageInterface);

    void setEmbeddedScene(EmbeddedSceneInterface embeddedSceneInterface);

    boolean requestFocus();

    boolean traverseFocusOut(boolean z2);

    void repaint();

    void setPreferredSize(int i2, int i3);

    void setEnabled(boolean z2);

    void setCursor(CursorFrame cursorFrame);

    boolean grabFocus();

    void ungrabFocus();
}
