package javafx.scene.control;

import javafx.scene.control.ToggleButtonBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ToggleButtonBuilder.class */
public class ToggleButtonBuilder<B extends ToggleButtonBuilder<B>> extends ButtonBaseBuilder<B> implements Builder<ToggleButton> {
    private int __set;
    private boolean selected;
    private ToggleGroup toggleGroup;

    protected ToggleButtonBuilder() {
    }

    public static ToggleButtonBuilder<?> create() {
        return new ToggleButtonBuilder<>();
    }

    public void applyTo(ToggleButton x2) {
        super.applyTo((ButtonBase) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setSelected(this.selected);
        }
        if ((set & 2) != 0) {
            x2.setToggleGroup(this.toggleGroup);
        }
    }

    public B selected(boolean x2) {
        this.selected = x2;
        this.__set |= 1;
        return this;
    }

    public B toggleGroup(ToggleGroup x2) {
        this.toggleGroup = x2;
        this.__set |= 2;
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public ToggleButton build2() {
        ToggleButton x2 = new ToggleButton();
        applyTo(x2);
        return x2;
    }
}
