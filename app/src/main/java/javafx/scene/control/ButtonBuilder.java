package javafx.scene.control;

import javafx.scene.control.ButtonBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ButtonBuilder.class */
public class ButtonBuilder<B extends ButtonBuilder<B>> extends ButtonBaseBuilder<B> implements Builder<Button> {
    private int __set;
    private boolean cancelButton;
    private boolean defaultButton;

    protected ButtonBuilder() {
    }

    public static ButtonBuilder<?> create() {
        return new ButtonBuilder<>();
    }

    public void applyTo(Button x2) {
        super.applyTo((ButtonBase) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setCancelButton(this.cancelButton);
        }
        if ((set & 2) != 0) {
            x2.setDefaultButton(this.defaultButton);
        }
    }

    public B cancelButton(boolean x2) {
        this.cancelButton = x2;
        this.__set |= 1;
        return this;
    }

    public B defaultButton(boolean x2) {
        this.defaultButton = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public Button build() {
        Button x2 = new Button();
        applyTo(x2);
        return x2;
    }
}
