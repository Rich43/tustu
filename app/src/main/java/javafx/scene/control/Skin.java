package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.Skinnable;

/* loaded from: jfxrt.jar:javafx/scene/control/Skin.class */
public interface Skin<C extends Skinnable> {
    C getSkinnable();

    Node getNode();

    void dispose();
}
