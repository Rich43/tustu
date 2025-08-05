package sun.awt.windows;

import java.awt.CheckboxMenuItem;
import java.awt.event.ItemEvent;
import java.awt.peer.CheckboxMenuItemPeer;

/* loaded from: rt.jar:sun/awt/windows/WCheckboxMenuItemPeer.class */
final class WCheckboxMenuItemPeer extends WMenuItemPeer implements CheckboxMenuItemPeer {
    @Override // java.awt.peer.CheckboxMenuItemPeer
    public native void setState(boolean z2);

    WCheckboxMenuItemPeer(CheckboxMenuItem checkboxMenuItem) {
        super(checkboxMenuItem, true);
        setState(checkboxMenuItem.getState());
    }

    public void handleAction(final boolean z2) {
        final CheckboxMenuItem checkboxMenuItem = (CheckboxMenuItem) this.target;
        WToolkit.executeOnEventHandlerThread(checkboxMenuItem, new Runnable() { // from class: sun.awt.windows.WCheckboxMenuItemPeer.1
            @Override // java.lang.Runnable
            public void run() {
                checkboxMenuItem.setState(z2);
                WCheckboxMenuItemPeer.this.postEvent(new ItemEvent(checkboxMenuItem, 701, checkboxMenuItem.getLabel(), z2 ? 1 : 2));
            }
        });
    }
}
