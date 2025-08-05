package com.sun.glass.ui.win;

import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinPlatformFactory.class */
public final class WinPlatformFactory extends PlatformFactory {
    @Override // com.sun.glass.ui.PlatformFactory
    public WinApplication createApplication() {
        return new WinApplication();
    }

    @Override // com.sun.glass.ui.PlatformFactory
    public MenuBarDelegate createMenuBarDelegate(MenuBar menubar) {
        return new WinMenuBarDelegate(menubar);
    }

    @Override // com.sun.glass.ui.PlatformFactory
    public MenuDelegate createMenuDelegate(Menu menu) {
        return new WinMenuDelegate(menu);
    }

    @Override // com.sun.glass.ui.PlatformFactory
    public MenuItemDelegate createMenuItemDelegate(MenuItem item) {
        return new WinMenuItemDelegate(item);
    }

    @Override // com.sun.glass.ui.PlatformFactory
    public ClipboardDelegate createClipboardDelegate() {
        return new WinClipboardDelegate();
    }
}
