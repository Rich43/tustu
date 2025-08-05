package sun.awt.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.peer.DialogPeer;
import java.util.Iterator;
import java.util.List;
import sun.awt.AWTAccessor;
import sun.awt.im.InputMethodManager;

/* loaded from: rt.jar:sun/awt/windows/WDialogPeer.class */
final class WDialogPeer extends WWindowPeer implements DialogPeer {
    static final Color defaultBackground = SystemColor.control;
    boolean needDefaultBackground;

    native void createAwtDialog(WComponentPeer wComponentPeer);

    native void showModal();

    native void endModal();

    native void pSetIMMOption(String str);

    WDialogPeer(Dialog dialog) {
        super(dialog);
        String triggerMenuString = InputMethodManager.getInstance().getTriggerMenuString();
        if (triggerMenuString != null) {
            pSetIMMOption(triggerMenuString);
        }
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void create(WComponentPeer wComponentPeer) {
        preCreate(wComponentPeer);
        createAwtDialog(wComponentPeer);
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WPanelPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void initialize() {
        Dialog dialog = (Dialog) this.target;
        if (this.needDefaultBackground) {
            dialog.setBackground(defaultBackground);
        }
        super.initialize();
        if (dialog.getTitle() != null) {
            setTitle(dialog.getTitle());
        }
        setResizable(dialog.isResizable());
    }

    @Override // sun.awt.windows.WWindowPeer
    protected void realShow() {
        if (((Dialog) this.target).getModalityType() != Dialog.ModalityType.MODELESS) {
            showModal();
        } else {
            super.realShow();
        }
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer
    void hide() {
        if (((Dialog) this.target).getModalityType() != Dialog.ModalityType.MODELESS) {
            endModal();
        } else {
            super.hide();
        }
    }

    @Override // java.awt.peer.DialogPeer
    public void blockWindows(List<Window> list) {
        Iterator<Window> it = list.iterator();
        while (it.hasNext()) {
            WWindowPeer wWindowPeer = (WWindowPeer) AWTAccessor.getComponentAccessor().getPeer(it.next());
            if (wWindowPeer != null) {
                wWindowPeer.setModalBlocked((Dialog) this.target, true);
            }
        }
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        if (((Dialog) this.target).isUndecorated()) {
            return super.getMinimumSize();
        }
        return new Dimension(getSysMinWidth(), getSysMinHeight());
    }

    @Override // sun.awt.windows.WWindowPeer
    boolean isTargetUndecorated() {
        return ((Dialog) this.target).isUndecorated();
    }

    @Override // sun.awt.windows.WComponentPeer
    public void reshape(int i2, int i3, int i4, int i5) {
        if (((Dialog) this.target).isUndecorated()) {
            super.reshape(i2, i3, i4, i5);
        } else {
            reshapeFrame(i2, i3, i4, i5);
        }
    }

    private void setDefaultColor() {
        this.needDefaultBackground = true;
    }

    void notifyIMMOptionChange() {
        InputMethodManager.getInstance().notifyChangeRequest((Component) this.target);
    }
}
