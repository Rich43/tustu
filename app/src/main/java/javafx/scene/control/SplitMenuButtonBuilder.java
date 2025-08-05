package javafx.scene.control;

import javafx.scene.control.SplitMenuButtonBuilder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/SplitMenuButtonBuilder.class */
public class SplitMenuButtonBuilder<B extends SplitMenuButtonBuilder<B>> extends MenuButtonBuilder<B> {
    protected SplitMenuButtonBuilder() {
    }

    public static SplitMenuButtonBuilder<?> create() {
        return new SplitMenuButtonBuilder<>();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.scene.control.MenuButtonBuilder, javafx.util.Builder
    /* renamed from: build, reason: merged with bridge method [inline-methods] */
    public MenuButton build2() {
        SplitMenuButton x2 = new SplitMenuButton();
        applyTo((MenuButton) x2);
        return x2;
    }
}
