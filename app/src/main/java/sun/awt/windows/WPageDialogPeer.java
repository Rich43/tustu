package sun.awt.windows;

/* loaded from: rt.jar:sun/awt/windows/WPageDialogPeer.class */
final class WPageDialogPeer extends WPrintDialogPeer {
    /* JADX INFO: Access modifiers changed from: private */
    public native boolean _show();

    WPageDialogPeer(WPageDialog wPageDialog) {
        super(wPageDialog);
    }

    @Override // sun.awt.windows.WPrintDialogPeer, sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer
    public void show() {
        new Thread(new Runnable() { // from class: sun.awt.windows.WPageDialogPeer.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ((WPrintDialog) WPageDialogPeer.this.target).setRetVal(WPageDialogPeer.this._show());
                } catch (Exception e2) {
                }
                ((WPrintDialog) WPageDialogPeer.this.target).setVisible(false);
            }
        }).start();
    }
}
