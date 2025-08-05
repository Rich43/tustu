package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Menu;
import com.sun.javafx.menu.MenuBase;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassMenuEventHandler.class */
class GlassMenuEventHandler extends Menu.EventHandler {
    private MenuBase menuBase;
    private boolean menuOpen;

    public GlassMenuEventHandler(MenuBase mb) {
        this.menuBase = mb;
    }

    @Override // com.sun.glass.ui.Menu.EventHandler
    public void handleMenuOpening(Menu menu, long time) {
        this.menuBase.show();
        this.menuOpen = true;
    }

    @Override // com.sun.glass.ui.Menu.EventHandler
    public void handleMenuClosed(Menu menu, long time) {
        this.menuBase.hide();
        this.menuOpen = false;
    }

    protected boolean isMenuOpen() {
        return this.menuOpen;
    }
}
