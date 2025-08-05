package javafx.scene.web;

import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/scene/web/PopupFeatures.class */
public final class PopupFeatures {
    private final boolean menu;
    private final boolean status;
    private final boolean toolbar;
    private final boolean resizable;

    public PopupFeatures(@NamedArg("menu") boolean menu, @NamedArg("status") boolean status, @NamedArg("toolbar") boolean toolbar, @NamedArg("resizable") boolean resizable) {
        this.menu = menu;
        this.status = status;
        this.toolbar = toolbar;
        this.resizable = resizable;
    }

    public final boolean hasMenu() {
        return this.menu;
    }

    public final boolean hasStatus() {
        return this.status;
    }

    public final boolean hasToolbar() {
        return this.toolbar;
    }

    public final boolean isResizable() {
        return this.resizable;
    }
}
