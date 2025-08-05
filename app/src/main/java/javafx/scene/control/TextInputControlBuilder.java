package javafx.scene.control;

import javafx.scene.control.TextInputControlBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TextInputControlBuilder.class */
public abstract class TextInputControlBuilder<B extends TextInputControlBuilder<B>> extends ControlBuilder<B> {
    private int __set;
    private boolean editable;
    private String promptText;
    private String text;

    protected TextInputControlBuilder() {
    }

    public void applyTo(TextInputControl x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setEditable(this.editable);
        }
        if ((set & 2) != 0) {
            x2.setPromptText(this.promptText);
        }
        if ((set & 4) != 0) {
            x2.setText(this.text);
        }
    }

    public B editable(boolean x2) {
        this.editable = x2;
        this.__set |= 1;
        return this;
    }

    public B promptText(String x2) {
        this.promptText = x2;
        this.__set |= 2;
        return this;
    }

    public B text(String x2) {
        this.text = x2;
        this.__set |= 4;
        return this;
    }
}
