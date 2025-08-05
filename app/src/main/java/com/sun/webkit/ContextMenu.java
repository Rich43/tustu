package com.sun.webkit;

/* loaded from: jfxrt.jar:com/sun/webkit/ContextMenu.class */
public abstract class ContextMenu {
    protected abstract void show(ShowContext showContext, int i2, int i3);

    protected abstract void appendItem(ContextMenuItem contextMenuItem);

    protected abstract void insertItem(ContextMenuItem contextMenuItem, int i2);

    protected abstract int getItemCount();

    /* JADX INFO: Access modifiers changed from: private */
    public native void twkHandleItemSelected(long j2, int i2);

    /* loaded from: jfxrt.jar:com/sun/webkit/ContextMenu$ShowContext.class */
    public final class ShowContext {
        private final WebPage page;
        private final long pdata;

        private ShowContext(WebPage page, long pdata) {
            this.page = page;
            this.pdata = pdata;
        }

        public WebPage getPage() {
            return this.page;
        }

        public void notifyItemSelected(int itemAction) {
            ContextMenu.this.twkHandleItemSelected(this.pdata, itemAction);
        }
    }

    private static ContextMenu fwkCreateContextMenu() {
        return Utilities.getUtilities().createContextMenu();
    }

    private void fwkShow(WebPage webPage, long pData, int x2, int y2) {
        show(new ShowContext(webPage, pData), x2, y2);
    }

    private void fwkAppendItem(ContextMenuItem item) {
        appendItem(item);
    }

    private void fwkInsertItem(ContextMenuItem item, int index) {
        insertItem(item, index);
    }

    private int fwkGetItemCount() {
        return getItemCount();
    }
}
