package sun.awt.windows;

import java.awt.Component;
import java.awt.Container;
import java.awt.Event;
import java.awt.MenuContainer;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.peer.PopupMenuPeer;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:sun/awt/windows/WPopupMenuPeer.class */
final class WPopupMenuPeer extends WMenuPeer implements PopupMenuPeer {
    private native void createMenu(WComponentPeer wComponentPeer);

    private native void _show(Event event);

    WPopupMenuPeer(PopupMenu popupMenu) {
        MenuContainer parent;
        this.target = popupMenu;
        if (AWTAccessor.getPopupMenuAccessor().isTrayIconPopup(popupMenu)) {
            parent = AWTAccessor.getMenuComponentAccessor().getParent(popupMenu);
        } else {
            parent = popupMenu.getParent();
        }
        if (parent instanceof Component) {
            WComponentPeer wComponentPeer = (WComponentPeer) WToolkit.targetToPeer(parent);
            wComponentPeer = wComponentPeer == null ? (WComponentPeer) WToolkit.targetToPeer(WToolkit.getNativeContainer((Component) parent)) : wComponentPeer;
            wComponentPeer.addChildPeer(this);
            createMenu(wComponentPeer);
            checkMenuCreation();
            return;
        }
        throw new IllegalArgumentException("illegal popup menu container class");
    }

    @Override // java.awt.peer.PopupMenuPeer
    public void show(Event event) {
        Component component = (Component) event.target;
        if (((WComponentPeer) WToolkit.targetToPeer(component)) == null) {
            Container nativeContainer = WToolkit.getNativeContainer(component);
            event.target = nativeContainer;
            Component parent = component;
            while (true) {
                Component component2 = parent;
                if (component2 == nativeContainer) {
                    break;
                }
                Point location = component2.getLocation();
                event.f12363x += location.f12370x;
                event.f12364y += location.f12371y;
                parent = component2.getParent();
            }
        }
        _show(event);
    }

    void show(Component component, Point point) {
        WComponentPeer wComponentPeer = (WComponentPeer) WToolkit.targetToPeer(component);
        Event event = new Event(component, 0L, 501, point.f12370x, point.f12371y, 0, 0);
        if (wComponentPeer == null) {
            event.target = WToolkit.getNativeContainer(component);
        }
        event.f12363x = point.f12370x;
        event.f12364y = point.f12371y;
        _show(event);
    }
}
