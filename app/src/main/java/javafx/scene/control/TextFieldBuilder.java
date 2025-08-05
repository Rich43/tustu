package javafx.scene.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextFieldBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TextFieldBuilder.class */
public class TextFieldBuilder<B extends TextFieldBuilder<B>> extends TextInputControlBuilder<B> implements Builder<TextField> {
    private int __set;
    private Pos alignment;
    private EventHandler<ActionEvent> onAction;
    private int prefColumnCount;
    private String promptText;

    protected TextFieldBuilder() {
    }

    public static TextFieldBuilder<?> create() {
        return new TextFieldBuilder<>();
    }

    public void applyTo(TextField x2) {
        super.applyTo((TextInputControl) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAlignment(this.alignment);
        }
        if ((set & 2) != 0) {
            x2.setOnAction(this.onAction);
        }
        if ((set & 4) != 0) {
            x2.setPrefColumnCount(this.prefColumnCount);
        }
        if ((set & 8) != 0) {
            x2.setPromptText(this.promptText);
        }
    }

    public B alignment(Pos x2) {
        this.alignment = x2;
        this.__set |= 1;
        return this;
    }

    public B onAction(EventHandler<ActionEvent> x2) {
        this.onAction = x2;
        this.__set |= 2;
        return this;
    }

    public B prefColumnCount(int x2) {
        this.prefColumnCount = x2;
        this.__set |= 4;
        return this;
    }

    @Override // javafx.scene.control.TextInputControlBuilder
    public B promptText(String x2) {
        this.promptText = x2;
        this.__set |= 8;
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public TextField build2() {
        TextField x2 = new TextField();
        applyTo(x2);
        return x2;
    }
}
