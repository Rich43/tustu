package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenuBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ContextMenuBuilder.class */
public class ContextMenuBuilder<B extends ContextMenuBuilder<B>> extends PopupControlBuilder<B> {
    private int __set;
    private boolean impl_showRelativeToWindow;
    private Collection<? extends MenuItem> items;
    private EventHandler<ActionEvent> onAction;

    protected ContextMenuBuilder() {
    }

    public static ContextMenuBuilder<?> create() {
        return new ContextMenuBuilder<>();
    }

    public void applyTo(ContextMenu x2) {
        super.applyTo((PopupControl) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setImpl_showRelativeToWindow(this.impl_showRelativeToWindow);
        }
        if ((set & 2) != 0) {
            x2.getItems().addAll(this.items);
        }
        if ((set & 4) != 0) {
            x2.setOnAction(this.onAction);
        }
    }

    @Deprecated
    public B impl_showRelativeToWindow(boolean x2) {
        this.impl_showRelativeToWindow = x2;
        this.__set |= 1;
        return this;
    }

    public B items(Collection<? extends MenuItem> x2) {
        this.items = x2;
        this.__set |= 2;
        return this;
    }

    public B items(MenuItem... menuItemArr) {
        return (B) items(Arrays.asList(menuItemArr));
    }

    public B onAction(EventHandler<ActionEvent> x2) {
        this.onAction = x2;
        this.__set |= 4;
        return this;
    }

    @Override // javafx.scene.control.PopupControlBuilder, javafx.util.Builder
    /* renamed from: build */
    public ContextMenu build2() {
        ContextMenu x2 = new ContextMenu();
        applyTo(x2);
        return x2;
    }
}
