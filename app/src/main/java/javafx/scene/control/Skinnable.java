package javafx.scene.control;

import javafx.beans.property.ObjectProperty;

/* loaded from: jfxrt.jar:javafx/scene/control/Skinnable.class */
public interface Skinnable {
    ObjectProperty<Skin<?>> skinProperty();

    void setSkin(Skin<?> skin);

    Skin<?> getSkin();
}
