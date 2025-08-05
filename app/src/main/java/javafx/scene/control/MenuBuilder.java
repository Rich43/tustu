package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/MenuBuilder.class */
public class MenuBuilder<B extends MenuBuilder<B>> extends MenuItemBuilder<B> {
    private int __set;
    private Collection<? extends MenuItem> items;
    private EventHandler<Event> onHidden;
    private EventHandler<Event> onHiding;
    private EventHandler<Event> onShowing;
    private EventHandler<Event> onShown;

    protected MenuBuilder() {
    }

    public static MenuBuilder<?> create() {
        return new MenuBuilder<>();
    }

    public void applyTo(Menu x2) {
        super.applyTo((MenuItem) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getItems().addAll(this.items);
        }
        if ((set & 2) != 0) {
            x2.setOnHidden(this.onHidden);
        }
        if ((set & 4) != 0) {
            x2.setOnHiding(this.onHiding);
        }
        if ((set & 8) != 0) {
            x2.setOnShowing(this.onShowing);
        }
        if ((set & 16) != 0) {
            x2.setOnShown(this.onShown);
        }
    }

    public B items(Collection<? extends MenuItem> x2) {
        this.items = x2;
        this.__set |= 1;
        return this;
    }

    public B items(MenuItem... menuItemArr) {
        return (B) items(Arrays.asList(menuItemArr));
    }

    public B onHidden(EventHandler<Event> x2) {
        this.onHidden = x2;
        this.__set |= 2;
        return this;
    }

    public B onHiding(EventHandler<Event> x2) {
        this.onHiding = x2;
        this.__set |= 4;
        return this;
    }

    public B onShowing(EventHandler<Event> x2) {
        this.onShowing = x2;
        this.__set |= 8;
        return this;
    }

    public B onShown(EventHandler<Event> x2) {
        this.onShown = x2;
        this.__set |= 16;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.scene.control.MenuItemBuilder, javafx.util.Builder
    /* renamed from: build */
    public MenuItem build2() {
        Menu x2 = new Menu();
        applyTo(x2);
        return x2;
    }
}
