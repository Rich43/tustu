package com.sun.javafx.webkit.theme;

import com.sun.webkit.ContextMenu;
import com.sun.webkit.ContextMenuItem;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/ContextMenuImpl.class */
public final class ContextMenuImpl extends ContextMenu {
    private static final Logger log = Logger.getLogger(ContextMenuImpl.class.getName());
    private final ObservableList<ContextMenuItem> items = FXCollections.observableArrayList();

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/ContextMenuImpl$MenuItemPeer.class */
    private interface MenuItemPeer {
        ContextMenuItem getItemPeer();
    }

    @Override // com.sun.webkit.ContextMenu
    protected void show(ContextMenu.ShowContext showContext, int x2, int y2) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "show at [{0}, {1}]", new Object[]{Integer.valueOf(x2), Integer.valueOf(y2)});
        }
        javafx.scene.control.ContextMenu popupMenu = new javafx.scene.control.ContextMenu();
        popupMenu.setOnAction(t2 -> {
            Object obj = (MenuItem) t2.getTarget();
            log.log(Level.FINE, "onAction: item={0}", obj);
            showContext.notifyItemSelected(((MenuItemPeer) obj).getItemPeer().getAction());
        });
        popupMenu.getItems().addAll(fillMenu());
        PopupMenuImpl.doShow(popupMenu, showContext.getPage(), x2, y2);
    }

    @Override // com.sun.webkit.ContextMenu
    protected void appendItem(ContextMenuItem item) {
        insertItem(item, this.items.size());
    }

    @Override // com.sun.webkit.ContextMenu
    protected void insertItem(ContextMenuItem item, int index) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "item={0}, index={1}", new Object[]{item, Integer.valueOf(index)});
        }
        if (item == null) {
            return;
        }
        this.items.remove(item);
        if (this.items.size() == 0) {
            this.items.add(item);
        } else {
            this.items.add(index, item);
        }
    }

    @Override // com.sun.webkit.ContextMenu
    protected int getItemCount() {
        return this.items.size();
    }

    private MenuItem createMenuItem(ContextMenuItem item) {
        MenuItem mi;
        log.log(Level.FINE, "item={0}", item);
        if (item.getType() == 2) {
            MenuImpl menu = new MenuImpl(item.getTitle());
            if (item.getSubmenu() != null) {
                menu.getItems().addAll(((ContextMenuImpl) item.getSubmenu()).fillMenu());
            }
            return menu;
        }
        if (item.getType() == 0) {
            if (item.isChecked()) {
                mi = new CheckMenuItemImpl(item);
            } else {
                mi = new MenuItemImpl(item);
            }
            mi.setDisable(!item.isEnabled());
            return mi;
        }
        if (item.getType() == 1) {
            return new SeparatorImpl(item);
        }
        throw new IllegalArgumentException("unexpected item type");
    }

    private ObservableList<MenuItem> fillMenu() {
        ObservableList<MenuItem> s2 = FXCollections.observableArrayList();
        for (ContextMenuItem item : this.items) {
            s2.add(createMenuItem(item));
        }
        return s2;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/ContextMenuImpl$MenuItemImpl.class */
    private static final class MenuItemImpl extends MenuItem implements MenuItemPeer {
        private final ContextMenuItem itemPeer;

        private MenuItemImpl(ContextMenuItem itemPeer) {
            super(itemPeer.getTitle());
            this.itemPeer = itemPeer;
        }

        @Override // com.sun.javafx.webkit.theme.ContextMenuImpl.MenuItemPeer
        public ContextMenuItem getItemPeer() {
            return this.itemPeer;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/ContextMenuImpl$CheckMenuItemImpl.class */
    private static final class CheckMenuItemImpl extends CheckMenuItem implements MenuItemPeer {
        private final ContextMenuItem itemPeer;

        private CheckMenuItemImpl(ContextMenuItem itemPeer) {
            this.itemPeer = itemPeer;
        }

        @Override // com.sun.javafx.webkit.theme.ContextMenuImpl.MenuItemPeer
        public ContextMenuItem getItemPeer() {
            return this.itemPeer;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/ContextMenuImpl$MenuImpl.class */
    private static final class MenuImpl extends Menu {
        private MenuImpl(String text) {
            super(text);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/ContextMenuImpl$SeparatorImpl.class */
    static final class SeparatorImpl extends MenuItem implements MenuItemPeer {
        private final ContextMenuItem itemPeer;

        SeparatorImpl(ContextMenuItem itemPeer) {
            this.itemPeer = itemPeer;
            setGraphic(new Separator());
            setDisable(true);
        }

        @Override // com.sun.javafx.webkit.theme.ContextMenuImpl.MenuItemPeer
        public ContextMenuItem getItemPeer() {
            return this.itemPeer;
        }
    }
}
