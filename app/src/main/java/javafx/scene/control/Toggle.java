package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/scene/control/Toggle.class */
public interface Toggle {
    ToggleGroup getToggleGroup();

    void setToggleGroup(ToggleGroup toggleGroup);

    ObjectProperty<ToggleGroup> toggleGroupProperty();

    boolean isSelected();

    void setSelected(boolean z2);

    BooleanProperty selectedProperty();

    Object getUserData();

    void setUserData(Object obj);

    ObservableMap<Object, Object> getProperties();
}
