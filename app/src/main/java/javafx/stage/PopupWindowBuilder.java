package javafx.stage;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.PopupWindowBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/stage/PopupWindowBuilder.class */
public abstract class PopupWindowBuilder<B extends PopupWindowBuilder<B>> extends WindowBuilder<B> {
    private int __set;
    private boolean autoFix;
    private boolean autoHide;
    private boolean consumeAutoHidingEvents;
    private boolean hideOnEscape;
    private EventHandler<Event> onAutoHide;

    protected PopupWindowBuilder() {
    }

    public void applyTo(PopupWindow x2) {
        super.applyTo((Window) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setAutoFix(this.autoFix);
        }
        if ((set & 2) != 0) {
            x2.setAutoHide(this.autoHide);
        }
        if ((set & 4) != 0) {
            x2.setConsumeAutoHidingEvents(this.consumeAutoHidingEvents);
        }
        if ((set & 8) != 0) {
            x2.setHideOnEscape(this.hideOnEscape);
        }
        if ((set & 16) != 0) {
            x2.setOnAutoHide(this.onAutoHide);
        }
    }

    public B autoFix(boolean x2) {
        this.autoFix = x2;
        this.__set |= 1;
        return this;
    }

    public B autoHide(boolean x2) {
        this.autoHide = x2;
        this.__set |= 2;
        return this;
    }

    public B consumeAutoHidingEvents(boolean x2) {
        this.consumeAutoHidingEvents = x2;
        this.__set |= 4;
        return this;
    }

    public B hideOnEscape(boolean x2) {
        this.hideOnEscape = x2;
        this.__set |= 8;
        return this;
    }

    public B onAutoHide(EventHandler<Event> x2) {
        this.onAutoHide = x2;
        this.__set |= 16;
        return this;
    }
}
