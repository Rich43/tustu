package sun.awt.windows;

import java.awt.AWTEvent;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.peer.MenuItemPeer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/windows/WMenuItemPeer.class */
class WMenuItemPeer extends WObjectPeer implements MenuItemPeer {
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.WMenuItemPeer");
    String shortcutLabel;
    protected WMenuPeer parent;
    private final boolean isCheckbox;
    private static Font defaultMenuFont;

    private native synchronized void _dispose();

    public native void _setLabel(String str);

    native void create(WMenuPeer wMenuPeer);

    native void enable(boolean z2);

    private static native void initIDs();

    private native void _setFont(Font font);

    static {
        initIDs();
        defaultMenuFont = (Font) AccessController.doPrivileged(new PrivilegedAction<Font>() { // from class: sun.awt.windows.WMenuItemPeer.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Font run2() {
                try {
                    return Font.decode(ResourceBundle.getBundle("sun.awt.windows.awtLocalization").getString("menuFont"));
                } catch (MissingResourceException e2) {
                    if (WMenuItemPeer.log.isLoggable(PlatformLogger.Level.FINE)) {
                        WMenuItemPeer.log.fine("WMenuItemPeer: " + e2.getMessage() + ". Using default MenuItem font.", e2);
                    }
                    return new Font("SanSerif", 0, 11);
                }
            }
        });
    }

    @Override // sun.awt.windows.WObjectPeer
    protected void disposeImpl() {
        WToolkit.targetDisposedPeer(this.target, this);
        _dispose();
    }

    @Override // java.awt.peer.MenuItemPeer
    public void setEnabled(boolean z2) {
        enable(z2);
    }

    public void enable() {
        enable(true);
    }

    public void disable() {
        enable(false);
    }

    private void readShortcutLabel() {
        WMenuPeer wMenuPeer;
        WMenuPeer wMenuPeer2 = this.parent;
        while (true) {
            wMenuPeer = wMenuPeer2;
            if (wMenuPeer == null || (wMenuPeer instanceof WMenuBarPeer)) {
                break;
            } else {
                wMenuPeer2 = wMenuPeer.parent;
            }
        }
        if (wMenuPeer instanceof WMenuBarPeer) {
            MenuShortcut shortcut = ((MenuItem) this.target).getShortcut();
            this.shortcutLabel = shortcut != null ? shortcut.toString() : null;
        } else {
            this.shortcutLabel = null;
        }
    }

    @Override // java.awt.peer.MenuItemPeer
    public void setLabel(String str) {
        readShortcutLabel();
        _setLabel(str);
    }

    protected WMenuItemPeer() {
        this.isCheckbox = false;
    }

    WMenuItemPeer(MenuItem menuItem) {
        this(menuItem, false);
    }

    WMenuItemPeer(MenuItem menuItem, boolean z2) {
        this.target = menuItem;
        this.parent = (WMenuPeer) WToolkit.targetToPeer(menuItem.getParent());
        this.isCheckbox = z2;
        this.parent.addChildPeer(this);
        create(this.parent);
        checkMenuCreation();
        readShortcutLabel();
    }

    void checkMenuCreation() {
        if (this.pData == 0) {
            if (this.createError != null) {
                throw this.createError;
            }
            throw new InternalError("couldn't create menu peer");
        }
    }

    void postEvent(AWTEvent aWTEvent) {
        WToolkit.postEvent(WToolkit.targetToAppContext(this.target), aWTEvent);
    }

    void handleAction(final long j2, final int i2) {
        WToolkit.executeOnEventHandlerThread(this.target, new Runnable() { // from class: sun.awt.windows.WMenuItemPeer.1
            @Override // java.lang.Runnable
            public void run() {
                WMenuItemPeer.this.postEvent(new ActionEvent(WMenuItemPeer.this.target, 1001, ((MenuItem) WMenuItemPeer.this.target).getActionCommand(), j2, i2));
            }
        });
    }

    static Font getDefaultFont() {
        return defaultMenuFont;
    }

    @Override // java.awt.peer.MenuComponentPeer
    public void setFont(Font font) {
        _setFont(font);
    }
}
