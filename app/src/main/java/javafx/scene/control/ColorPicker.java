package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ColorPickerSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

/* loaded from: jfxrt.jar:javafx/scene/control/ColorPicker.class */
public class ColorPicker extends ComboBoxBase<Color> {
    public static final String STYLE_CLASS_BUTTON = "button";
    public static final String STYLE_CLASS_SPLIT_BUTTON = "split-button";
    private ObservableList<Color> customColors;
    private static final String DEFAULT_STYLE_CLASS = "color-picker";

    public final ObservableList<Color> getCustomColors() {
        return this.customColors;
    }

    public ColorPicker() {
        this(Color.WHITE);
    }

    public ColorPicker(Color color) {
        this.customColors = FXCollections.observableArrayList();
        setValue(color);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ColorPickerSkin(this);
    }
}
