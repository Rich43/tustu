package javafx.scene.input;

import javafx.geometry.Point2D;

/* loaded from: jfxrt.jar:javafx/scene/input/InputMethodRequests.class */
public interface InputMethodRequests {
    Point2D getTextLocation(int i2);

    int getLocationOffset(int i2, int i3);

    void cancelLatestCommittedText();

    String getSelectedText();
}
