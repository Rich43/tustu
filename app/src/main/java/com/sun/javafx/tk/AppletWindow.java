package com.sun.javafx.tk;

import java.util.Map;
import javafx.stage.Stage;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/AppletWindow.class */
public interface AppletWindow {
    void setStageOnTop(Stage stage);

    void setBackgroundColor(int i2);

    void setForegroundColor(int i2);

    void setVisible(boolean z2);

    void setSize(int i2, int i3);

    int getWidth();

    int getHeight();

    void setPosition(int i2, int i3);

    int getPositionX();

    int getPositionY();

    float getUIScale();

    int getRemoteLayerId();

    void dispatchEvent(Map map);
}
