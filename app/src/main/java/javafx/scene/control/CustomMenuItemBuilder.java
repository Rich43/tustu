package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.CustomMenuItemBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/CustomMenuItemBuilder.class */
public class CustomMenuItemBuilder<B extends CustomMenuItemBuilder<B>> extends MenuItemBuilder<B> {
    private int __set;
    private Node content;
    private boolean hideOnClick;

    protected CustomMenuItemBuilder() {
    }

    public static CustomMenuItemBuilder<?> create() {
        return new CustomMenuItemBuilder<>();
    }

    public void applyTo(CustomMenuItem x2) {
        super.applyTo((MenuItem) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setContent(this.content);
        }
        if ((set & 2) != 0) {
            x2.setHideOnClick(this.hideOnClick);
        }
    }

    public B content(Node x2) {
        this.content = x2;
        this.__set |= 1;
        return this;
    }

    public B hideOnClick(boolean x2) {
        this.hideOnClick = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.scene.control.MenuItemBuilder, javafx.util.Builder
    /* renamed from: build */
    public MenuItem build2() {
        CustomMenuItem x2 = new CustomMenuItem();
        applyTo(x2);
        return x2;
    }
}
