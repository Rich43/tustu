package javafx.scene.control;

import javafx.scene.control.RadioButtonBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/RadioButtonBuilder.class */
public class RadioButtonBuilder<B extends RadioButtonBuilder<B>> extends ToggleButtonBuilder<B> {
    protected RadioButtonBuilder() {
    }

    public static RadioButtonBuilder<?> create() {
        return new RadioButtonBuilder<>();
    }

    @Override // javafx.scene.control.ToggleButtonBuilder, javafx.util.Builder
    /* renamed from: build */
    public RadioButton build2() {
        RadioButton x2 = new RadioButton();
        applyTo((ToggleButton) x2);
        return x2;
    }
}
