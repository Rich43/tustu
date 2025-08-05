package javafx.scene.control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBaseBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ButtonBaseBuilder.class */
public abstract class ButtonBaseBuilder<B extends ButtonBaseBuilder<B>> extends LabeledBuilder<B> {
    private boolean __set;
    private EventHandler<ActionEvent> onAction;

    protected ButtonBaseBuilder() {
    }

    public void applyTo(ButtonBase x2) {
        super.applyTo((Labeled) x2);
        if (this.__set) {
            x2.setOnAction(this.onAction);
        }
    }

    public B onAction(EventHandler<ActionEvent> x2) {
        this.onAction = x2;
        this.__set = true;
        return this;
    }
}
