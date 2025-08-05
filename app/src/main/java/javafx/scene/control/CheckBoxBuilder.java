package javafx.scene.control;

import javafx.scene.control.CheckBoxBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/CheckBoxBuilder.class */
public class CheckBoxBuilder<B extends CheckBoxBuilder<B>> extends ButtonBaseBuilder<B> implements Builder<CheckBox> {
    private int __set;
    private boolean allowIndeterminate;
    private boolean indeterminate;
    private boolean selected;

    protected CheckBoxBuilder() {
    }

    public static CheckBoxBuilder<?> create() {
        return new CheckBoxBuilder<>();
    }

    public void applyTo(CheckBox x2) {
        super.applyTo((ButtonBase) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAllowIndeterminate(this.allowIndeterminate);
        }
        if ((set & 2) != 0) {
            x2.setIndeterminate(this.indeterminate);
        }
        if ((set & 4) != 0) {
            x2.setSelected(this.selected);
        }
    }

    public B allowIndeterminate(boolean x2) {
        this.allowIndeterminate = x2;
        this.__set |= 1;
        return this;
    }

    public B indeterminate(boolean x2) {
        this.indeterminate = x2;
        this.__set |= 2;
        return this;
    }

    public B selected(boolean x2) {
        this.selected = x2;
        this.__set |= 4;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public CheckBox build() {
        CheckBox x2 = new CheckBox();
        applyTo(x2);
        return x2;
    }
}
