package javafx.scene.control;

import javafx.scene.control.SeparatorMenuItemBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/SeparatorMenuItemBuilder.class */
public class SeparatorMenuItemBuilder<B extends SeparatorMenuItemBuilder<B>> extends CustomMenuItemBuilder<B> {
    protected SeparatorMenuItemBuilder() {
    }

    public static SeparatorMenuItemBuilder<?> create() {
        return new SeparatorMenuItemBuilder<>();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.scene.control.CustomMenuItemBuilder, javafx.scene.control.MenuItemBuilder, javafx.util.Builder
    /* renamed from: build */
    public MenuItem build2() {
        SeparatorMenuItem x2 = new SeparatorMenuItem();
        applyTo((CustomMenuItem) x2);
        return x2;
    }
}
