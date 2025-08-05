package com.sun.javafx.webkit.theme;

import com.sun.javafx.webkit.theme.ContextMenuImpl;
import com.sun.webkit.Invoker;
import com.sun.webkit.PopupMenu;
import com.sun.webkit.WebPage;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCFont;
import com.sun.webkit.graphics.WCPoint;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebView;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/PopupMenuImpl.class */
public final class PopupMenuImpl extends PopupMenu {
    private static final Logger log;
    private final ContextMenu popupMenu = new ContextMenu();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PopupMenuImpl.class.desiredAssertionStatus();
        log = Logger.getLogger(PopupMenuImpl.class.getName());
    }

    public PopupMenuImpl() {
        this.popupMenu.setOnHidden(t1 -> {
            log.finer("onHidden");
            Invoker.getInvoker().postOnEventThread(() -> {
                log.finer("onHidden: notifying");
                notifyPopupClosed();
            });
        });
        this.popupMenu.setOnAction(t2 -> {
            MenuItem item = (MenuItem) t2.getTarget();
            log.log(Level.FINE, "onAction: item={0}", item);
            notifySelectionCommited(this.popupMenu.getItems().indexOf(item));
        });
    }

    @Override // com.sun.webkit.PopupMenu
    protected void show(WebPage page, int x2, int y2, int width) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "show at [{0}, {1}], width={2}", new Object[]{Integer.valueOf(x2), Integer.valueOf(y2), Integer.valueOf(width)});
        }
        this.popupMenu.setPrefWidth(width);
        this.popupMenu.setPrefHeight(this.popupMenu.getHeight());
        doShow(this.popupMenu, page, x2, y2);
    }

    @Override // com.sun.webkit.PopupMenu
    protected void hide() {
        log.fine("hiding");
        this.popupMenu.hide();
    }

    @Override // com.sun.webkit.PopupMenu
    protected void appendItem(String itemText, boolean isLabel, boolean isSeparator, boolean isEnabled, int bgColor, int fgColor, WCFont font) {
        MenuItem item;
        if (log.isLoggable(Level.FINEST)) {
            log.log(Level.FINEST, "itemText={0}, isLabel={1}, isSeparator={2}, isEnabled={3}, bgColor={4}, fgColor={5}, font={6}", new Object[]{itemText, Boolean.valueOf(isLabel), Boolean.valueOf(isSeparator), Boolean.valueOf(isEnabled), Integer.valueOf(bgColor), Integer.valueOf(fgColor), font});
        }
        if (isSeparator) {
            item = new ContextMenuImpl.SeparatorImpl(null);
        } else {
            item = new MenuItem(itemText);
            item.setDisable(!isEnabled);
        }
        item.setMnemonicParsing(false);
        this.popupMenu.getItems().add(item);
    }

    @Override // com.sun.webkit.PopupMenu
    protected void setSelectedItem(int index) {
        log.log(Level.FINEST, "index={0}", Integer.valueOf(index));
    }

    static void doShow(ContextMenu popup, WebPage page, int anchorX, int anchorY) {
        WebPageClient<WebView> client = page.getPageClient();
        if (!$assertionsDisabled && client == null) {
            throw new AssertionError();
        }
        WCPoint pt = client.windowToScreen(new WCPoint(anchorX, anchorY));
        popup.show(client.getContainer().getScene().getWindow(), pt.getX(), pt.getY());
    }
}
