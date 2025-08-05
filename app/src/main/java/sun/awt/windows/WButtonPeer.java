package sun.awt.windows;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.peer.ButtonPeer;

/* loaded from: rt.jar:sun/awt/windows/WButtonPeer.class */
final class WButtonPeer extends WComponentPeer implements ButtonPeer {
    @Override // java.awt.peer.ButtonPeer
    public native void setLabel(String str);

    @Override // sun.awt.windows.WComponentPeer
    native void create(WComponentPeer wComponentPeer);

    private static native void initIDs();

    static {
        initIDs();
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        FontMetrics fontMetrics = getFontMetrics(((Button) this.target).getFont());
        String label = ((Button) this.target).getLabel();
        if (label == null) {
            label = "";
        }
        return new Dimension(fontMetrics.stringWidth(label) + 14, fontMetrics.getHeight() + 8);
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public boolean isFocusable() {
        return true;
    }

    WButtonPeer(Button button) {
        super(button);
    }

    public void handleAction(final long j2, final int i2) {
        WToolkit.executeOnEventHandlerThread(this.target, new Runnable() { // from class: sun.awt.windows.WButtonPeer.1
            @Override // java.lang.Runnable
            public void run() {
                WButtonPeer.this.postEvent(new ActionEvent(WButtonPeer.this.target, 1001, ((Button) WButtonPeer.this.target).getActionCommand(), j2, i2));
            }
        }, j2);
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean shouldClearRectBeforePaint() {
        return false;
    }

    @Override // sun.awt.windows.WComponentPeer
    public boolean handleJavaKeyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getID()) {
            case 402:
                if (keyEvent.getKeyCode() == 32) {
                    handleAction(keyEvent.getWhen(), keyEvent.getModifiers());
                    break;
                }
                break;
        }
        return false;
    }
}
