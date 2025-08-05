package javafx.scene.control;

import javafx.scene.control.RadioMenuItemBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/RadioMenuItemBuilder.class */
public class RadioMenuItemBuilder<B extends RadioMenuItemBuilder<B>> extends MenuItemBuilder<B> {
    private int __set;
    private boolean selected;
    private String text;
    private ToggleGroup toggleGroup;

    protected RadioMenuItemBuilder() {
    }

    public static RadioMenuItemBuilder<?> create() {
        return new RadioMenuItemBuilder<>();
    }

    public void applyTo(RadioMenuItem x2) {
        super.applyTo((MenuItem) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setSelected(this.selected);
        }
        if ((set & 2) != 0) {
            x2.setToggleGroup(this.toggleGroup);
        }
    }

    public B selected(boolean x2) {
        this.selected = x2;
        this.__set |= 1;
        return this;
    }

    @Override // javafx.scene.control.MenuItemBuilder
    public B text(String x2) {
        this.text = x2;
        return this;
    }

    public B toggleGroup(ToggleGroup x2) {
        this.toggleGroup = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.scene.control.MenuItemBuilder, javafx.util.Builder
    /* renamed from: build */
    public MenuItem build2() {
        RadioMenuItem x2 = new RadioMenuItem(this.text);
        applyTo(x2);
        return x2;
    }
}
