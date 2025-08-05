package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.menu.CheckMenuItemBase;
import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.menu.MenuItemBase;
import com.sun.javafx.menu.RadioMenuItemBase;
import com.sun.javafx.menu.SeparatorMenuItemBase;
import com.sun.javafx.tk.TKSystemMenu;
import com.sun.prism.Image;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassSystemMenu.class */
class GlassSystemMenu implements TKSystemMenu {
    private List<MenuBase> systemMenus = null;
    private MenuBar glassSystemMenuBar = null;
    private InvalidationListener visibilityListener = valueModel -> {
        if (this.systemMenus != null) {
            setMenus(this.systemMenus);
        }
    };
    static final /* synthetic */ boolean $assertionsDisabled;

    GlassSystemMenu() {
    }

    static {
        $assertionsDisabled = !GlassSystemMenu.class.desiredAssertionStatus();
    }

    protected void createMenuBar() throws IndexOutOfBoundsException {
        if (this.glassSystemMenuBar == null) {
            Application app = Application.GetApplication();
            this.glassSystemMenuBar = app.createMenuBar();
            app.installDefaultMenus(this.glassSystemMenuBar);
            if (this.systemMenus != null) {
                setMenus(this.systemMenus);
            }
        }
    }

    protected MenuBar getMenuBar() {
        return this.glassSystemMenuBar;
    }

    @Override // com.sun.javafx.tk.TKSystemMenu
    public boolean isSupported() {
        return Application.GetApplication().supportsSystemMenu();
    }

    @Override // com.sun.javafx.tk.TKSystemMenu
    public void setMenus(List<MenuBase> menus) throws IndexOutOfBoundsException {
        this.systemMenus = menus;
        if (this.glassSystemMenuBar != null) {
            List<Menu> existingMenus = this.glassSystemMenuBar.getMenus();
            int existingSize = existingMenus.size();
            for (int index = existingSize - 1; index >= 1; index--) {
                Menu menu = existingMenus.get(index);
                clearMenu(menu);
                this.glassSystemMenuBar.remove(index);
            }
            for (MenuBase menu2 : menus) {
                addMenu(null, menu2);
            }
        }
    }

    private void clearMenu(Menu menu) {
        for (int i2 = menu.getItems().size() - 1; i2 >= 0; i2--) {
            Object o2 = menu.getItems().get(i2);
            if (o2 instanceof MenuItem) {
                ((MenuItem) o2).setCallback(null);
            } else if (o2 instanceof Menu) {
                clearMenu((Menu) o2);
            }
        }
        menu.setEventHandler(null);
    }

    private void addMenu(Menu parent, MenuBase mb) throws IndexOutOfBoundsException {
        if (parent != null) {
            insertMenu(parent, mb, parent.getItems().size());
        } else {
            insertMenu(parent, mb, this.glassSystemMenuBar.getMenus().size());
        }
    }

    private void insertMenu(Menu parent, MenuBase mb, int pos) throws IndexOutOfBoundsException {
        Application app = Application.GetApplication();
        Menu glassMenu = app.createMenu(parseText(mb), !mb.isDisable());
        glassMenu.setEventHandler(new GlassMenuEventHandler(mb));
        mb.visibleProperty().removeListener(this.visibilityListener);
        mb.visibleProperty().addListener(this.visibilityListener);
        if (!mb.isVisible()) {
            return;
        }
        ObservableList<MenuItemBase> items = mb.getItemsBase();
        items.addListener(change -> {
            while (change.next()) {
                int from = change.getFrom();
                int to = change.getTo();
                List<? extends MenuItemBase> removed = change.getRemoved();
                for (int i2 = (from + removed.size()) - 1; i2 >= from; i2--) {
                    List<Object> menuItemList = glassMenu.getItems();
                    if (i2 >= 0 && menuItemList.size() > i2) {
                        glassMenu.remove(i2);
                    }
                }
                for (int i3 = from; i3 < to; i3++) {
                    MenuItemBase item = (MenuItemBase) change.getList().get(i3);
                    if (item instanceof MenuBase) {
                        insertMenu(glassMenu, (MenuBase) item, i3);
                    } else {
                        insertMenuItem(glassMenu, item, i3);
                    }
                }
            }
        });
        for (MenuItemBase item : items) {
            if (item instanceof MenuBase) {
                addMenu(glassMenu, (MenuBase) item);
            } else {
                addMenuItem(glassMenu, item);
            }
        }
        glassMenu.setPixels(getPixels(mb));
        setMenuBindings(glassMenu, mb);
        if (parent != null) {
            parent.insert(glassMenu, pos);
        } else {
            this.glassSystemMenuBar.insert(glassMenu, pos);
        }
    }

    private void setMenuBindings(Menu glassMenu, MenuBase mb) {
        mb.textProperty().addListener(valueModel -> {
            glassMenu.setTitle(parseText(mb));
        });
        mb.disableProperty().addListener(valueModel2 -> {
            glassMenu.setEnabled(!mb.isDisable());
        });
        mb.mnemonicParsingProperty().addListener(valueModel3 -> {
            glassMenu.setTitle(parseText(mb));
        });
    }

    private void addMenuItem(Menu parent, MenuItemBase menuitem) throws IndexOutOfBoundsException {
        insertMenuItem(parent, menuitem, parent.getItems().size());
    }

    private void insertMenuItem(final Menu parent, final MenuItemBase menuitem, int pos) throws IndexOutOfBoundsException {
        Application app = Application.GetApplication();
        menuitem.visibleProperty().removeListener(this.visibilityListener);
        menuitem.visibleProperty().addListener(this.visibilityListener);
        if (!menuitem.isVisible()) {
            return;
        }
        if (menuitem instanceof SeparatorMenuItemBase) {
            if (menuitem.isVisible()) {
                parent.insert(MenuItem.Separator, pos);
                return;
            }
            return;
        }
        MenuItem.Callback callback = new MenuItem.Callback() { // from class: com.sun.javafx.tk.quantum.GlassSystemMenu.1
            @Override // com.sun.glass.ui.MenuItem.Callback
            public void action() {
                if (menuitem instanceof CheckMenuItemBase) {
                    CheckMenuItemBase checkItem = (CheckMenuItemBase) menuitem;
                    checkItem.setSelected(!checkItem.isSelected());
                } else if (menuitem instanceof RadioMenuItemBase) {
                    RadioMenuItemBase radioItem = (RadioMenuItemBase) menuitem;
                    radioItem.setSelected(true);
                }
                menuitem.fire();
            }

            @Override // com.sun.glass.ui.MenuItem.Callback
            public void validate() {
                Menu.EventHandler meh = parent.getEventHandler();
                GlassMenuEventHandler gmeh = (GlassMenuEventHandler) meh;
                if (gmeh.isMenuOpen()) {
                    return;
                }
                menuitem.fireValidation();
            }
        };
        MenuItem glassSubMenuItem = app.createMenuItem(parseText(menuitem), callback);
        menuitem.textProperty().addListener(valueModel -> {
            glassSubMenuItem.setTitle(parseText(menuitem));
        });
        glassSubMenuItem.setPixels(getPixels(menuitem));
        menuitem.graphicProperty().addListener(valueModel2 -> {
            glassSubMenuItem.setPixels(getPixels(menuitem));
        });
        glassSubMenuItem.setEnabled(!menuitem.isDisable());
        menuitem.disableProperty().addListener(valueModel3 -> {
            glassSubMenuItem.setEnabled(!menuitem.isDisable());
        });
        setShortcut(glassSubMenuItem, menuitem);
        menuitem.acceleratorProperty().addListener(valueModel4 -> {
            setShortcut(glassSubMenuItem, menuitem);
        });
        menuitem.mnemonicParsingProperty().addListener(valueModel5 -> {
            glassSubMenuItem.setTitle(parseText(menuitem));
        });
        if (menuitem instanceof CheckMenuItemBase) {
            CheckMenuItemBase checkItem = (CheckMenuItemBase) menuitem;
            glassSubMenuItem.setChecked(checkItem.isSelected());
            checkItem.selectedProperty().addListener(valueModel6 -> {
                glassSubMenuItem.setChecked(checkItem.isSelected());
            });
        } else if (menuitem instanceof RadioMenuItemBase) {
            RadioMenuItemBase radioItem = (RadioMenuItemBase) menuitem;
            glassSubMenuItem.setChecked(radioItem.isSelected());
            radioItem.selectedProperty().addListener(valueModel7 -> {
                glassSubMenuItem.setChecked(radioItem.isSelected());
            });
        }
        parent.insert(glassSubMenuItem, pos);
    }

    private String parseText(MenuItemBase menuItem) {
        String text = menuItem.getText();
        if (text == null) {
            return "";
        }
        if (!text.isEmpty() && menuItem.isMnemonicParsing()) {
            return text.replaceFirst("_([^_])", "$1");
        }
        return text;
    }

    private Pixels getPixels(MenuItemBase menuItem) {
        Image pi;
        if (menuItem.getGraphic() instanceof ImageView) {
            ImageView iv = (ImageView) menuItem.getGraphic();
            javafx.scene.image.Image im = iv.getImage();
            if (im == null) {
                return null;
            }
            String url = im.impl_getUrl();
            if ((url == null || PixelUtils.supportedFormatType(url)) && (pi = (Image) im.impl_getPlatformImage()) != null) {
                return PixelUtils.imageToPixels(pi);
            }
            return null;
        }
        return null;
    }

    private void setShortcut(MenuItem glassSubMenuItem, MenuItemBase menuItem) {
        KeyCombination accelerator = menuItem.getAccelerator();
        if (accelerator == null) {
            glassSubMenuItem.setShortcut(0, 0);
            return;
        }
        if (!(accelerator instanceof KeyCodeCombination)) {
            if (accelerator instanceof KeyCharacterCombination) {
                KeyCharacterCombination kcc = (KeyCharacterCombination) accelerator;
                String kchar = kcc.getCharacter();
                glassSubMenuItem.setShortcut(kchar.charAt(0), glassModifiers(kcc));
                return;
            }
            return;
        }
        KeyCodeCombination kcc2 = (KeyCodeCombination) accelerator;
        KeyCode code = kcc2.getCode();
        if (!$assertionsDisabled && !PlatformUtil.isMac() && !PlatformUtil.isLinux()) {
            throw new AssertionError();
        }
        int modifier = glassModifiers(kcc2);
        if (PlatformUtil.isMac()) {
            int finalCode = code.isLetterKey() ? code.impl_getChar().toUpperCase().charAt(0) : code.impl_getCode();
            glassSubMenuItem.setShortcut(finalCode, modifier);
        } else {
            if (PlatformUtil.isLinux()) {
                String lower = code.impl_getChar().toLowerCase();
                if ((modifier & 4) != 0) {
                    glassSubMenuItem.setShortcut(lower.charAt(0), modifier);
                    return;
                } else {
                    glassSubMenuItem.setShortcut(0, 0);
                    return;
                }
            }
            glassSubMenuItem.setShortcut(0, 0);
        }
    }

    private int glassModifiers(KeyCombination kcc) {
        int ret = 0;
        if (kcc.getShift() == KeyCombination.ModifierValue.DOWN) {
            ret = 0 + 1;
        }
        if (kcc.getControl() == KeyCombination.ModifierValue.DOWN) {
            ret += 4;
        }
        if (kcc.getAlt() == KeyCombination.ModifierValue.DOWN) {
            ret += 8;
        }
        if (kcc.getShortcut() == KeyCombination.ModifierValue.DOWN) {
            if (PlatformUtil.isLinux()) {
                ret += 4;
            } else if (PlatformUtil.isMac()) {
                ret += 16;
            }
        }
        if (kcc.getMeta() == KeyCombination.ModifierValue.DOWN && (PlatformUtil.isLinux() || PlatformUtil.isMac())) {
            ret += 16;
        }
        if (kcc instanceof KeyCodeCombination) {
            KeyCode kcode = ((KeyCodeCombination) kcc).getCode();
            int code = kcode.impl_getCode();
            if ((code >= KeyCode.F1.impl_getCode() && code <= KeyCode.F12.impl_getCode()) || (code >= KeyCode.F13.impl_getCode() && code <= KeyCode.F24.impl_getCode())) {
                ret += 2;
            }
        }
        return ret;
    }
}
