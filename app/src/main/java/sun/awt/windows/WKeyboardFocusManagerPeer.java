package sun.awt.windows;

import java.awt.Component;
import java.awt.Window;
import java.awt.peer.ComponentPeer;
import sun.awt.CausedFocusEvent;
import sun.awt.KeyboardFocusManagerPeerImpl;

/* loaded from: rt.jar:sun/awt/windows/WKeyboardFocusManagerPeer.class */
final class WKeyboardFocusManagerPeer extends KeyboardFocusManagerPeerImpl {
    private static final WKeyboardFocusManagerPeer inst = new WKeyboardFocusManagerPeer();

    static native void setNativeFocusOwner(ComponentPeer componentPeer);

    static native Component getNativeFocusOwner();

    static native Window getNativeFocusedWindow();

    public static WKeyboardFocusManagerPeer getInstance() {
        return inst;
    }

    private WKeyboardFocusManagerPeer() {
    }

    @Override // java.awt.peer.KeyboardFocusManagerPeer
    public void setCurrentFocusOwner(Component component) {
        setNativeFocusOwner(component != null ? component.getPeer() : null);
    }

    @Override // java.awt.peer.KeyboardFocusManagerPeer
    public Component getCurrentFocusOwner() {
        return getNativeFocusOwner();
    }

    @Override // java.awt.peer.KeyboardFocusManagerPeer
    public void setCurrentFocusedWindow(Window window) {
        throw new RuntimeException("not implemented");
    }

    @Override // java.awt.peer.KeyboardFocusManagerPeer
    public Window getCurrentFocusedWindow() {
        return getNativeFocusedWindow();
    }

    public static boolean deliverFocus(Component component, Component component2, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause) {
        return KeyboardFocusManagerPeerImpl.deliverFocus(component, component2, z2, z3, j2, cause, getNativeFocusOwner());
    }
}
