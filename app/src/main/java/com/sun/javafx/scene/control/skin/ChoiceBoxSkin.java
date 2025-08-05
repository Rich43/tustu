package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ChoiceBoxBehavior;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ChoiceBoxSkin.class */
public class ChoiceBoxSkin<T> extends BehaviorSkinBase<ChoiceBox<T>, ChoiceBoxBehavior<T>> {
    private ObservableList<T> choiceBoxItems;
    private ContextMenu popup;
    private StackPane openButton;
    private final ToggleGroup toggleGroup;
    private SelectionModel<T> selectionModel;
    private Label label;
    private final ListChangeListener<T> choiceBoxItemsListener;
    private final WeakListChangeListener<T> weakChoiceBoxItemsListener;
    private final InvalidationListener itemsObserver;
    private InvalidationListener selectionChangeListener;

    public ChoiceBoxSkin(ChoiceBox<T> control) {
        super(control, new ChoiceBoxBehavior(control));
        this.toggleGroup = new ToggleGroup();
        this.choiceBoxItemsListener = new ListChangeListener<T>() { // from class: com.sun.javafx.scene.control.skin.ChoiceBoxSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends T> c2) {
                while (c2.next()) {
                    if (c2.getRemovedSize() > 0 || c2.wasPermutated()) {
                        ChoiceBoxSkin.this.toggleGroup.getToggles().clear();
                        ChoiceBoxSkin.this.popup.getItems().clear();
                        int i2 = 0;
                        for (T obj : c2.getList()) {
                            ChoiceBoxSkin.this.addPopupItem(obj, i2);
                            i2++;
                        }
                    } else {
                        for (int i3 = c2.getFrom(); i3 < c2.getTo(); i3++) {
                            T obj2 = c2.getList().get(i3);
                            ChoiceBoxSkin.this.addPopupItem(obj2, i3);
                        }
                    }
                }
                ChoiceBoxSkin.this.updateSelection();
                ((ChoiceBox) ChoiceBoxSkin.this.getSkinnable()).requestLayout();
            }
        };
        this.weakChoiceBoxItemsListener = new WeakListChangeListener<>(this.choiceBoxItemsListener);
        this.selectionChangeListener = observable -> {
            updateSelection();
        };
        initialize();
        this.itemsObserver = observable2 -> {
            updateChoiceBoxItems();
        };
        control.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
        control.requestLayout();
        registerChangeListener(control.selectionModelProperty(), "SELECTION_MODEL");
        registerChangeListener(control.showingProperty(), "SHOWING");
        registerChangeListener(control.itemsProperty(), "ITEMS");
        registerChangeListener(control.getSelectionModel().selectedItemProperty(), "SELECTION_CHANGED");
        registerChangeListener(control.converterProperty(), "CONVERTER");
    }

    private void initialize() {
        updateChoiceBoxItems();
        this.label = new Label();
        this.label.setMnemonicParsing(false);
        this.openButton = new StackPane();
        this.openButton.getStyleClass().setAll("open-button");
        StackPane region = new StackPane();
        region.getStyleClass().setAll("arrow");
        this.openButton.getChildren().clear();
        this.openButton.getChildren().addAll(region);
        this.popup = new ContextMenu();
        this.popup.showingProperty().addListener((o2, ov, nv) -> {
            if (!nv.booleanValue()) {
                ((ChoiceBox) getSkinnable()).hide();
            }
        });
        this.popup.setId("choice-box-popup-menu");
        getChildren().setAll(this.label, this.openButton);
        updatePopupItems();
        updateSelectionModel();
        updateSelection();
        if (this.selectionModel != null && this.selectionModel.getSelectedIndex() == -1) {
            this.label.setText("");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateChoiceBoxItems() {
        if (this.choiceBoxItems != null) {
            this.choiceBoxItems.removeListener(this.weakChoiceBoxItemsListener);
        }
        this.choiceBoxItems = ((ChoiceBox) getSkinnable()).getItems();
        if (this.choiceBoxItems != null) {
            this.choiceBoxItems.addListener(this.weakChoiceBoxItemsListener);
        }
    }

    String getChoiceBoxSelectedText() {
        return this.label.getText();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v9, types: [javafx.scene.Node, javafx.scene.control.Control] */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        ContextMenuContent cmContent;
        int index;
        super.handleControlPropertyChanged(p2);
        if ("ITEMS".equals(p2)) {
            updateChoiceBoxItems();
            updatePopupItems();
            updateSelectionModel();
            updateSelection();
            if (this.selectionModel != null && this.selectionModel.getSelectedIndex() == -1) {
                this.label.setText("");
                return;
            }
            return;
        }
        if ("SELECTION_MODEL".equals(p2)) {
            updateSelectionModel();
            return;
        }
        if ("SELECTION_CHANGED".equals(p2)) {
            if (((ChoiceBox) getSkinnable()).getSelectionModel() != null && (index = ((ChoiceBox) getSkinnable()).getSelectionModel().getSelectedIndex()) != -1) {
                MenuItem item = this.popup.getItems().get(index);
                if (item instanceof RadioMenuItem) {
                    ((RadioMenuItem) item).setSelected(true);
                    return;
                }
                return;
            }
            return;
        }
        if ("SHOWING".equals(p2)) {
            if (((ChoiceBox) getSkinnable()).isShowing()) {
                SelectionModel sm = ((ChoiceBox) getSkinnable()).getSelectionModel();
                if (sm == null) {
                    return;
                }
                long currentSelectedIndex = sm.getSelectedIndex();
                int itemInControlCount = this.choiceBoxItems.size();
                boolean hasSelection = currentSelectedIndex >= 0 && currentSelectedIndex < ((long) itemInControlCount);
                if (hasSelection) {
                    MenuItem item2 = this.popup.getItems().get((int) currentSelectedIndex);
                    if (item2 != null && (item2 instanceof RadioMenuItem)) {
                        ((RadioMenuItem) item2).setSelected(true);
                    }
                } else if (itemInControlCount > 0) {
                    this.popup.getItems().get(0);
                }
                ((ChoiceBox) getSkinnable()).autosize();
                double y2 = 0.0d;
                if (this.popup.getSkin() != null && (cmContent = (ContextMenuContent) this.popup.getSkin().getNode()) != null && currentSelectedIndex != -1) {
                    y2 = -cmContent.getMenuYOffset((int) currentSelectedIndex);
                }
                this.popup.show(getSkinnable(), Side.BOTTOM, 2.0d, y2);
                return;
            }
            this.popup.hide();
            return;
        }
        if ("CONVERTER".equals(p2)) {
            updateChoiceBoxItems();
            updatePopupItems();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void addPopupItem(T t2, int i2) {
        MenuItem popupItem;
        if (t2 instanceof Separator) {
            popupItem = new SeparatorMenuItem();
        } else if (t2 instanceof SeparatorMenuItem) {
            popupItem = (SeparatorMenuItem) t2;
        } else {
            StringConverter<T> c2 = ((ChoiceBox) getSkinnable()).getConverter();
            String displayString = c2 == null ? t2 == 0 ? "" : t2.toString() : c2.toString(t2);
            RadioMenuItem item = new RadioMenuItem(displayString);
            item.setId("choice-box-menu-item");
            item.setToggleGroup(this.toggleGroup);
            item.setOnAction(e2 -> {
                if (this.selectionModel == null) {
                    return;
                }
                int index = ((ChoiceBox) getSkinnable()).getItems().indexOf(t2);
                this.selectionModel.select(index);
                item.setSelected(true);
            });
            popupItem = item;
        }
        popupItem.setMnemonicParsing(false);
        this.popup.getItems().add(i2, popupItem);
    }

    private void updatePopupItems() {
        this.toggleGroup.getToggles().clear();
        this.popup.getItems().clear();
        this.toggleGroup.selectToggle(null);
        for (int i2 = 0; i2 < this.choiceBoxItems.size(); i2++) {
            T o2 = this.choiceBoxItems.get(i2);
            addPopupItem(o2, i2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateSelectionModel() {
        if (this.selectionModel != null) {
            this.selectionModel.selectedIndexProperty().removeListener(this.selectionChangeListener);
        }
        this.selectionModel = ((ChoiceBox) getSkinnable()).getSelectionModel();
        if (this.selectionModel != null) {
            this.selectionModel.selectedIndexProperty().addListener(this.selectionChangeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelection() {
        if (this.selectionModel == null || this.selectionModel.isEmpty()) {
            this.toggleGroup.selectToggle(null);
            this.label.setText("");
            return;
        }
        int selectedIndex = this.selectionModel.getSelectedIndex();
        if (selectedIndex == -1 || selectedIndex > this.popup.getItems().size()) {
            this.label.setText("");
            return;
        }
        if (selectedIndex < this.popup.getItems().size()) {
            MenuItem selectedItem = this.popup.getItems().get(selectedIndex);
            if (selectedItem instanceof RadioMenuItem) {
                ((RadioMenuItem) selectedItem).setSelected(true);
                this.toggleGroup.selectToggle(null);
            }
            this.label.setText(this.popup.getItems().get(selectedIndex).getText());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double obw = this.openButton.prefWidth(-1.0d);
        this.label.resizeRelocate(x2, y2, w2, h2);
        this.openButton.resize(obw, this.openButton.prefHeight(-1.0d));
        positionInArea(this.openButton, (x2 + w2) - obw, y2, obw, h2, 0.0d, HPos.CENTER, VPos.CENTER);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double boxWidth = this.label.minWidth(-1.0d) + this.openButton.minWidth(-1.0d);
        double popupWidth = this.popup.minWidth(-1.0d);
        return leftInset + Math.max(boxWidth, popupWidth) + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double displayHeight = this.label.minHeight(-1.0d);
        double openButtonHeight = this.openButton.minHeight(-1.0d);
        return topInset + Math.max(displayHeight, openButtonHeight) + bottomInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double boxWidth = this.label.prefWidth(-1.0d) + this.openButton.prefWidth(-1.0d);
        double popupWidth = this.popup.prefWidth(-1.0d);
        if (popupWidth <= 0.0d && this.popup.getItems().size() > 0) {
            popupWidth = new Text(this.popup.getItems().get(0).getText()).prefWidth(-1.0d);
        }
        if (this.popup.getItems().size() == 0) {
            return 50.0d;
        }
        return leftInset + Math.max(boxWidth, popupWidth) + rightInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double displayHeight = this.label.prefHeight(-1.0d);
        double openButtonHeight = this.openButton.prefHeight(-1.0d);
        return topInset + Math.max(displayHeight, openButtonHeight) + bottomInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((ChoiceBox) getSkinnable()).prefHeight(width);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((ChoiceBox) getSkinnable()).prefWidth(height);
    }
}
