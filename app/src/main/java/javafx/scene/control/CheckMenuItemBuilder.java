package javafx.scene.control;

import javafx.scene.control.CheckMenuItemBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/CheckMenuItemBuilder.class */
public class CheckMenuItemBuilder<B extends CheckMenuItemBuilder<B>> extends MenuItemBuilder<B> {
    private boolean __set;
    private boolean selected;

    protected CheckMenuItemBuilder() {
    }

    public static CheckMenuItemBuilder<?> create() {
        return new CheckMenuItemBuilder<>();
    }

    public void applyTo(CheckMenuItem x2) {
        super.applyTo((MenuItem) x2);
        if (this.__set) {
            x2.setSelected(this.selected);
        }
    }

    public B selected(boolean x2) {
        this.selected = x2;
        this.__set = true;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.scene.control.MenuItemBuilder, javafx.util.Builder
    /* renamed from: build */
    public MenuItem build2() {
        CheckMenuItem x2 = new CheckMenuItem();
        applyTo(x2);
        return x2;
    }
}
