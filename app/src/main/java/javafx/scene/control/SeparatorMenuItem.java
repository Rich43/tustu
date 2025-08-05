package javafx.scene.control;

import javafx.geometry.Orientation;

/* loaded from: jfxrt.jar:javafx/scene/control/SeparatorMenuItem.class */
public class SeparatorMenuItem extends CustomMenuItem {
    private static final String DEFAULT_STYLE_CLASS = "separator-menu-item";

    public SeparatorMenuItem() {
        super(new Separator(Orientation.HORIZONTAL), false);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }
}
