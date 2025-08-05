package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Side;
import javafx.scene.control.MenuButtonBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/MenuButtonBuilder.class */
public class MenuButtonBuilder<B extends MenuButtonBuilder<B>> extends ButtonBaseBuilder<B> implements Builder<MenuButton> {
    private int __set;
    private Collection<? extends MenuItem> items;
    private Side popupSide;

    protected MenuButtonBuilder() {
    }

    public static MenuButtonBuilder<?> create() {
        return new MenuButtonBuilder<>();
    }

    public void applyTo(MenuButton x2) {
        super.applyTo((ButtonBase) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getItems().addAll(this.items);
        }
        if ((set & 2) != 0) {
            x2.setPopupSide(this.popupSide);
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

    public B popupSide(Side x2) {
        this.popupSide = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    public MenuButton build() {
        MenuButton x2 = new MenuButton();
        applyTo(x2);
        return x2;
    }
}
