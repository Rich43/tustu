package com.sun.javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.menu.CheckMenuItemBase;
import com.sun.javafx.menu.CustomMenuItemBase;
import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.menu.MenuItemBase;
import com.sun.javafx.menu.RadioMenuItemBase;
import com.sun.javafx.menu.SeparatorMenuItemBase;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/GlobalMenuAdapter.class */
public class GlobalMenuAdapter extends Menu implements MenuBase {
    private Menu menu;
    private final ObservableList<MenuItemBase> items;

    public static MenuBase adapt(Menu menu) {
        return new GlobalMenuAdapter(menu);
    }

    private GlobalMenuAdapter(Menu menu) {
        super(menu.getText());
        this.items = new TrackableObservableList<MenuItemBase>() { // from class: com.sun.javafx.scene.control.GlobalMenuAdapter.1
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<MenuItemBase> c2) {
            }
        };
        this.menu = menu;
        bindMenuItemProperties(this, menu);
        menu.showingProperty().addListener(property -> {
            if (menu.isShowing() && !isShowing()) {
                show();
            } else if (!menu.isShowing() && isShowing()) {
                hide();
            }
        });
        showingProperty().addListener(property2 -> {
            if (isShowing() && !menu.isShowing()) {
                menu.show();
            } else if (!isShowing() && menu.isShowing()) {
                menu.hide();
            }
        });
        menu.getItems().addListener(new ListChangeListener<MenuItem>() { // from class: com.sun.javafx.scene.control.GlobalMenuAdapter.2
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends MenuItem> change) {
                while (change.next()) {
                    int from = change.getFrom();
                    int to = change.getTo();
                    List<? extends MenuItem> removed = change.getRemoved();
                    for (int i2 = (from + removed.size()) - 1; i2 >= from; i2--) {
                        GlobalMenuAdapter.this.items.remove(i2);
                        GlobalMenuAdapter.this.getItems().remove(i2);
                    }
                    for (int i3 = from; i3 < to; i3++) {
                        MenuItem item = change.getList().get(i3);
                        GlobalMenuAdapter.this.insertItem(item, i3);
                    }
                }
            }
        });
        for (MenuItem menuItem : menu.getItems()) {
            insertItem(menuItem, this.items.size());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void insertItem(MenuItem menuItem, int pos) {
        MenuItem menuItemAdapter;
        if (menuItem instanceof Menu) {
            menuItemAdapter = new GlobalMenuAdapter((Menu) menuItem);
        } else if (menuItem instanceof CheckMenuItem) {
            menuItemAdapter = new CheckMenuItemAdapter((CheckMenuItem) menuItem);
        } else if (menuItem instanceof RadioMenuItem) {
            menuItemAdapter = new RadioMenuItemAdapter((RadioMenuItem) menuItem);
        } else if (menuItem instanceof SeparatorMenuItem) {
            menuItemAdapter = new SeparatorMenuItemAdapter((SeparatorMenuItem) menuItem);
        } else if (menuItem instanceof CustomMenuItem) {
            menuItemAdapter = new CustomMenuItemAdapter((CustomMenuItem) menuItem);
        } else {
            menuItemAdapter = new MenuItemAdapter(menuItem);
        }
        this.items.add(pos, menuItemAdapter);
        getItems().add(pos, menuItemAdapter);
    }

    @Override // com.sun.javafx.menu.MenuBase
    public final ObservableList<MenuItemBase> getItemsBase() {
        return this.items;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void bindMenuItemProperties(MenuItem adapter, MenuItem menuItem) {
        adapter.idProperty().bind(menuItem.idProperty());
        adapter.textProperty().bind(menuItem.textProperty());
        adapter.graphicProperty().bind(menuItem.graphicProperty());
        adapter.disableProperty().bind(menuItem.disableProperty());
        adapter.visibleProperty().bind(menuItem.visibleProperty());
        adapter.acceleratorProperty().bind(menuItem.acceleratorProperty());
        adapter.mnemonicParsingProperty().bind(menuItem.mnemonicParsingProperty());
        adapter.setOnAction(ev -> {
            menuItem.fire();
        });
    }

    @Override // com.sun.javafx.menu.MenuItemBase
    public void fireValidation() {
        if (this.menu.getOnMenuValidation() != null) {
            Event.fireEvent(this.menu, new Event(MENU_VALIDATION_EVENT));
        }
        Menu target = this.menu.getParentMenu();
        if (target != null && target.getOnMenuValidation() != null) {
            Event.fireEvent(target, new Event(MenuItem.MENU_VALIDATION_EVENT));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/GlobalMenuAdapter$MenuItemAdapter.class */
    private static class MenuItemAdapter extends MenuItem implements MenuItemBase {
        private MenuItem menuItem;

        private MenuItemAdapter(MenuItem menuItem) {
            super(menuItem.getText());
            this.menuItem = menuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, menuItem);
        }

        @Override // com.sun.javafx.menu.MenuItemBase
        public void fireValidation() {
            if (this.menuItem.getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
            Menu target = this.menuItem.getParentMenu();
            if (target.getOnMenuValidation() != null) {
                Event.fireEvent(target, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/GlobalMenuAdapter$CheckMenuItemAdapter.class */
    private static class CheckMenuItemAdapter extends CheckMenuItem implements CheckMenuItemBase {
        private CheckMenuItem menuItem;

        private CheckMenuItemAdapter(CheckMenuItem menuItem) {
            super(menuItem.getText());
            this.menuItem = menuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, menuItem);
            selectedProperty().bindBidirectional(menuItem.selectedProperty());
        }

        @Override // com.sun.javafx.menu.MenuItemBase
        public void fireValidation() {
            if (getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
            }
            Menu target = this.menuItem.getParentMenu();
            if (target.getOnMenuValidation() != null) {
                Event.fireEvent(target, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/GlobalMenuAdapter$RadioMenuItemAdapter.class */
    private static class RadioMenuItemAdapter extends RadioMenuItem implements RadioMenuItemBase {
        private RadioMenuItem menuItem;

        private RadioMenuItemAdapter(RadioMenuItem menuItem) {
            super(menuItem.getText());
            this.menuItem = menuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, menuItem);
            selectedProperty().bindBidirectional(menuItem.selectedProperty());
        }

        @Override // com.sun.javafx.menu.MenuItemBase
        public void fireValidation() {
            if (getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
            }
            Menu target = this.menuItem.getParentMenu();
            if (target.getOnMenuValidation() != null) {
                Event.fireEvent(target, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/GlobalMenuAdapter$SeparatorMenuItemAdapter.class */
    private static class SeparatorMenuItemAdapter extends SeparatorMenuItem implements SeparatorMenuItemBase {
        private SeparatorMenuItem menuItem;

        private SeparatorMenuItemAdapter(SeparatorMenuItem menuItem) {
            this.menuItem = menuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, menuItem);
        }

        @Override // com.sun.javafx.menu.MenuItemBase
        public void fireValidation() {
            if (getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
            }
            Menu target = this.menuItem.getParentMenu();
            if (target.getOnMenuValidation() != null) {
                Event.fireEvent(target, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/GlobalMenuAdapter$CustomMenuItemAdapter.class */
    private static class CustomMenuItemAdapter extends CustomMenuItem implements CustomMenuItemBase {
        private CustomMenuItem menuItem;

        private CustomMenuItemAdapter(CustomMenuItem menuItem) {
            this.menuItem = menuItem;
            GlobalMenuAdapter.bindMenuItemProperties(this, menuItem);
        }

        @Override // com.sun.javafx.menu.MenuItemBase
        public void fireValidation() {
            if (getOnMenuValidation() != null) {
                Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
            }
            Menu target = this.menuItem.getParentMenu();
            if (target.getOnMenuValidation() != null) {
                Event.fireEvent(target, new Event(MenuItem.MENU_VALIDATION_EVENT));
            }
        }
    }
}
