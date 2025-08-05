package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.MenuBarBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/MenuBarBuilder.class */
public class MenuBarBuilder<B extends MenuBarBuilder<B>> extends ControlBuilder<B> implements Builder<MenuBar> {
    private int __set;
    private Collection<? extends Menu> menus;
    private boolean useSystemMenuBar;

    protected MenuBarBuilder() {
    }

    public static MenuBarBuilder<?> create() {
        return new MenuBarBuilder<>();
    }

    public void applyTo(MenuBar x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getMenus().addAll(this.menus);
        }
        if ((set & 2) != 0) {
            x2.setUseSystemMenuBar(this.useSystemMenuBar);
        }
    }

    public B menus(Collection<? extends Menu> x2) {
        this.menus = x2;
        this.__set |= 1;
        return this;
    }

    public B menus(Menu... menuArr) {
        return (B) menus(Arrays.asList(menuArr));
    }

    public B useSystemMenuBar(boolean x2) {
        this.useSystemMenuBar = x2;
        this.__set |= 2;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public MenuBar build2() {
        MenuBar x2 = new MenuBar();
        applyTo(x2);
        return x2;
    }
}
