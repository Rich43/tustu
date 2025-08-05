package javafx.scene.control;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBoxBaseBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ComboBoxBaseBuilder.class */
public abstract class ComboBoxBaseBuilder<T, B extends ComboBoxBaseBuilder<T, B>> extends ControlBuilder<B> {
    private int __set;
    private boolean editable;
    private EventHandler<ActionEvent> onAction;
    private EventHandler<Event> onHidden;
    private EventHandler<Event> onHiding;
    private EventHandler<Event> onShowing;
    private EventHandler<Event> onShown;
    private String promptText;
    private T value;

    protected ComboBoxBaseBuilder() {
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(ComboBoxBase<T> x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setEditable(this.editable);
                    break;
                case 1:
                    x2.setOnAction(this.onAction);
                    break;
                case 2:
                    x2.setOnHidden(this.onHidden);
                    break;
                case 3:
                    x2.setOnHiding(this.onHiding);
                    break;
                case 4:
                    x2.setOnShowing(this.onShowing);
                    break;
                case 5:
                    x2.setOnShown(this.onShown);
                    break;
                case 6:
                    x2.setPromptText(this.promptText);
                    break;
                case 7:
                    x2.setValue(this.value);
                    break;
            }
        }
    }

    public B editable(boolean x2) {
        this.editable = x2;
        __set(0);
        return this;
    }

    public B onAction(EventHandler<ActionEvent> x2) {
        this.onAction = x2;
        __set(1);
        return this;
    }

    public B onHidden(EventHandler<Event> x2) {
        this.onHidden = x2;
        __set(2);
        return this;
    }

    public B onHiding(EventHandler<Event> x2) {
        this.onHiding = x2;
        __set(3);
        return this;
    }

    public B onShowing(EventHandler<Event> x2) {
        this.onShowing = x2;
        __set(4);
        return this;
    }

    public B onShown(EventHandler<Event> x2) {
        this.onShown = x2;
        __set(5);
        return this;
    }

    public B promptText(String x2) {
        this.promptText = x2;
        __set(6);
        return this;
    }

    public B value(T x2) {
        this.value = x2;
        __set(7);
        return this;
    }
}
