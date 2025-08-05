package javafx.scene.control;

import javafx.scene.control.PasswordFieldBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/PasswordFieldBuilder.class */
public class PasswordFieldBuilder<B extends PasswordFieldBuilder<B>> extends TextFieldBuilder<B> {
    private boolean __set;
    private String promptText;

    protected PasswordFieldBuilder() {
    }

    public static PasswordFieldBuilder<?> create() {
        return new PasswordFieldBuilder<>();
    }

    public void applyTo(PasswordField x2) {
        super.applyTo((TextField) x2);
        if (this.__set) {
            x2.setPromptText(this.promptText);
        }
    }

    @Override // javafx.scene.control.TextFieldBuilder, javafx.scene.control.TextInputControlBuilder
    public B promptText(String x2) {
        this.promptText = x2;
        this.__set = true;
        return this;
    }

    @Override // javafx.scene.control.TextFieldBuilder, javafx.util.Builder
    /* renamed from: build */
    public PasswordField build2() {
        PasswordField x2 = new PasswordField();
        applyTo(x2);
        return x2;
    }
}
