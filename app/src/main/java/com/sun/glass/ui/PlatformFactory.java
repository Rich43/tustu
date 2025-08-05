package com.sun.glass.ui;

import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import java.util.Locale;

/* loaded from: jfxrt.jar:com/sun/glass/ui/PlatformFactory.class */
public abstract class PlatformFactory {
    private static PlatformFactory instance;

    public abstract Application createApplication();

    public abstract MenuBarDelegate createMenuBarDelegate(MenuBar menuBar);

    public abstract MenuDelegate createMenuDelegate(Menu menu);

    public abstract MenuItemDelegate createMenuItemDelegate(MenuItem menuItem);

    public abstract ClipboardDelegate createClipboardDelegate();

    public static synchronized PlatformFactory getPlatformFactory() {
        if (instance == null) {
            try {
                String platform = Platform.determinePlatform();
                String factory = "com.sun.glass.ui." + platform.toLowerCase(Locale.ROOT) + "." + platform + "PlatformFactory";
                Class c2 = Class.forName(factory);
                instance = (PlatformFactory) c2.newInstance();
            } catch (Exception e2) {
                e2.printStackTrace();
                System.out.println("Failed to load Glass factory class");
            }
        }
        return instance;
    }
}
