package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxListViewBehavior;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.swing.JInternalFrame;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ComboBoxListViewSkin.class */
public class ComboBoxListViewSkin<T> extends ComboBoxPopupControl<T> {
    private static final String COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY = "comboBoxRowsToMeasureWidth";
    private final ComboBox<T> comboBox;
    private ObservableList<T> comboBoxItems;
    private ListCell<T> buttonCell;
    private Callback<ListView<T>, ListCell<T>> cellFactory;
    private final ListView<T> listView;
    private ObservableList<T> listViewItems;
    private boolean listSelectionLock;
    private boolean listViewSelectionDirty;
    private boolean itemCountDirty;
    private final ListChangeListener<T> listViewItemsListener;
    private final InvalidationListener itemsObserver;
    private final WeakListChangeListener<T> weakListViewItemsListener;
    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass(JInternalFrame.IS_SELECTED_PROPERTY);
    private static final PseudoClass PSEUDO_CLASS_EMPTY = PseudoClass.getPseudoClass(Constants.ELEMNAME_EMPTY_STRING);
    private static final PseudoClass PSEUDO_CLASS_FILLED = PseudoClass.getPseudoClass("filled");

    public ComboBoxListViewSkin(ComboBox<T> comboBox) {
        super(comboBox, new ComboBoxListViewBehavior(comboBox));
        this.listSelectionLock = false;
        this.listViewSelectionDirty = false;
        this.listViewItemsListener = new ListChangeListener<T>() { // from class: com.sun.javafx.scene.control.skin.ComboBoxListViewSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends T> c2) {
                ComboBoxListViewSkin.this.itemCountDirty = true;
                ((ComboBoxBase) ComboBoxListViewSkin.this.getSkinnable()).requestLayout();
            }
        };
        this.weakListViewItemsListener = new WeakListChangeListener<>(this.listViewItemsListener);
        this.comboBox = comboBox;
        updateComboBoxItems();
        this.itemsObserver = observable -> {
            updateComboBoxItems();
            updateListViewItems();
        };
        this.comboBox.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
        this.listView = createListView();
        this.listView.setManaged(false);
        getChildren().add(this.listView);
        updateListViewItems();
        updateCellFactory();
        updateButtonCell();
        updateValue();
        registerChangeListener(comboBox.itemsProperty(), "ITEMS");
        registerChangeListener(comboBox.promptTextProperty(), "PROMPT_TEXT");
        registerChangeListener(comboBox.cellFactoryProperty(), "CELL_FACTORY");
        registerChangeListener(comboBox.visibleRowCountProperty(), "VISIBLE_ROW_COUNT");
        registerChangeListener(comboBox.converterProperty(), "CONVERTER");
        registerChangeListener(comboBox.buttonCellProperty(), "BUTTON_CELL");
        registerChangeListener(comboBox.valueProperty(), "VALUE");
        registerChangeListener(comboBox.editableProperty(), "EDITABLE");
        if (comboBox.isShowing()) {
            show();
        }
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String p2) {
        super.handleControlPropertyChanged(p2);
        if ("ITEMS".equals(p2)) {
            updateComboBoxItems();
            updateListViewItems();
            return;
        }
        if ("PROMPT_TEXT".equals(p2)) {
            updateDisplayNode();
            return;
        }
        if ("CELL_FACTORY".equals(p2)) {
            updateCellFactory();
            return;
        }
        if ("VISIBLE_ROW_COUNT".equals(p2)) {
            if (this.listView == null) {
                return;
            }
            this.listView.requestLayout();
            return;
        }
        if ("CONVERTER".equals(p2)) {
            updateListViewItems();
            return;
        }
        if ("EDITOR".equals(p2)) {
            getEditableInputNode();
            return;
        }
        if ("BUTTON_CELL".equals(p2)) {
            updateButtonCell();
            updateDisplayArea();
        } else if ("VALUE".equals(p2)) {
            updateValue();
            this.comboBox.fireEvent(new ActionEvent());
        } else if ("EDITABLE".equals(p2)) {
            updateEditable();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    protected TextField getEditor() {
        if (((ComboBoxBase) getSkinnable()).isEditable()) {
            return ((ComboBox) getSkinnable()).getEditor();
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    protected StringConverter<T> getConverter() {
        return ((ComboBox) getSkinnable()).getConverter();
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin
    public Node getDisplayNode() {
        Node displayNode;
        if (this.comboBox.isEditable()) {
            displayNode = getEditableInputNode();
        } else {
            displayNode = this.buttonCell;
        }
        updateDisplayNode();
        return displayNode;
    }

    public void updateComboBoxItems() {
        this.comboBoxItems = this.comboBox.getItems();
        this.comboBoxItems = this.comboBoxItems == null ? FXCollections.emptyObservableList() : this.comboBoxItems;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void updateListViewItems() {
        if (this.listViewItems != null) {
            this.listViewItems.removeListener(this.weakListViewItemsListener);
        }
        this.listViewItems = this.comboBoxItems;
        this.listView.setItems(this.listViewItems);
        if (this.listViewItems != null) {
            this.listViewItems.addListener(this.weakListViewItemsListener);
        }
        this.itemCountDirty = true;
        ((ComboBoxBase) getSkinnable()).requestLayout();
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    public Node getPopupContent() {
        return this.listView;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return 50.0d;
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double superPrefWidth = super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        double listViewWidth = this.listView.prefWidth(height);
        double pw = Math.max(superPrefWidth, listViewWidth);
        reconfigurePopup();
        return pw;
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return super.computeMaxWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return super.computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        reconfigurePopup();
        return super.computeMaxHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxBaseSkin, javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        if (this.listViewSelectionDirty) {
            try {
                this.listSelectionLock = true;
                T item = this.comboBox.getSelectionModel().getSelectedItem();
                this.listView.getSelectionModel().clearSelection();
                this.listView.getSelectionModel().select((MultipleSelectionModel<T>) item);
                this.listSelectionLock = false;
                this.listViewSelectionDirty = false;
            } catch (Throwable th) {
                this.listSelectionLock = false;
                this.listViewSelectionDirty = false;
                throw th;
            }
        }
        super.layoutChildren(x2, y2, w2, h2);
    }

    protected boolean isHideOnClickEnabled() {
        return true;
    }

    private void updateValue() {
        T newValue = this.comboBox.getValue();
        SelectionModel<T> listViewSM = this.listView.getSelectionModel();
        if (newValue == null) {
            listViewSM.clearSelection();
            return;
        }
        int indexOfNewValue = getIndexOfComboBoxValueInItemsList();
        if (indexOfNewValue == -1) {
            this.listSelectionLock = true;
            listViewSM.clearSelection();
            this.listSelectionLock = false;
            return;
        }
        int index = this.comboBox.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < this.comboBoxItems.size()) {
            T itemsObj = this.comboBoxItems.get(index);
            if (itemsObj != null && itemsObj.equals(newValue)) {
                listViewSM.select(index);
                return;
            } else {
                listViewSM.select((SelectionModel<T>) newValue);
                return;
            }
        }
        int listViewIndex = this.comboBoxItems.indexOf(newValue);
        if (listViewIndex == -1) {
            updateDisplayNode();
        } else {
            listViewSM.select(listViewIndex);
        }
    }

    @Override // com.sun.javafx.scene.control.skin.ComboBoxPopupControl
    protected void updateDisplayNode() {
        if (getEditor() != null) {
            super.updateDisplayNode();
            return;
        }
        T value = this.comboBox.getValue();
        int index = getIndexOfComboBoxValueInItemsList();
        if (index > -1) {
            this.buttonCell.setItem(null);
            this.buttonCell.updateIndex(index);
            return;
        }
        this.buttonCell.updateIndex(-1);
        boolean empty = updateDisplayText(this.buttonCell, value, false);
        this.buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_EMPTY, empty);
        this.buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_FILLED, !empty);
        this.buttonCell.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean updateDisplayText(ListCell<T> cell, T t2, boolean empty) {
        if (empty) {
            if (cell == null) {
                return true;
            }
            cell.setGraphic(null);
            cell.setText(null);
            return true;
        }
        if (t2 instanceof Node) {
            Node currentNode = cell.getGraphic();
            Node newNode = (Node) t2;
            if (currentNode == null || !currentNode.equals(newNode)) {
                cell.setText(null);
                cell.setGraphic(newNode);
            }
            return newNode == null;
        }
        StringConverter<T> c2 = this.comboBox.getConverter();
        String s2 = t2 == 0 ? this.comboBox.getPromptText() : c2 == null ? t2.toString() : c2.toString(t2);
        cell.setText(s2);
        cell.setGraphic(null);
        return s2 == null || s2.isEmpty();
    }

    private int getIndexOfComboBoxValueInItemsList() {
        T value = this.comboBox.getValue();
        int index = this.comboBoxItems.indexOf(value);
        return index;
    }

    private void updateButtonCell() {
        this.buttonCell = this.comboBox.getButtonCell() != null ? this.comboBox.getButtonCell() : getDefaultCellFactory().call(this.listView);
        this.buttonCell.setMouseTransparent(true);
        this.buttonCell.updateListView(this.listView);
        this.buttonCell.setAccessibleRole(AccessibleRole.NODE);
    }

    private void updateCellFactory() {
        Callback<ListView<T>, ListCell<T>> cf = this.comboBox.getCellFactory();
        this.cellFactory = cf != null ? cf : getDefaultCellFactory();
        this.listView.setCellFactory(this.cellFactory);
    }

    private Callback<ListView<T>, ListCell<T>> getDefaultCellFactory() {
        return new Callback<ListView<T>, ListCell<T>>() { // from class: com.sun.javafx.scene.control.skin.ComboBoxListViewSkin.2
            @Override // javafx.util.Callback
            public ListCell<T> call(ListView<T> listView) {
                return new ListCell<T>() { // from class: com.sun.javafx.scene.control.skin.ComboBoxListViewSkin.2.1
                    @Override // javafx.scene.control.Cell
                    public void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        ComboBoxListViewSkin.this.updateDisplayText(this, item, empty);
                    }
                };
            }
        };
    }

    private ListView<T> createListView() {
        ListView<T> _listView = new ListView<T>() { // from class: com.sun.javafx.scene.control.skin.ComboBoxListViewSkin.3
            {
                getProperties().put("selectFirstRowByDefault", false);
            }

            @Override // javafx.scene.control.Control, javafx.scene.layout.Region, javafx.scene.Parent
            protected double computeMinHeight(double width) {
                return 30.0d;
            }

            @Override // javafx.scene.control.Control, javafx.scene.layout.Region, javafx.scene.Parent
            protected double computePrefWidth(double height) {
                double pw;
                if (getSkin() instanceof ListViewSkin) {
                    ListViewSkin<?> skin = (ListViewSkin) getSkin();
                    if (ComboBoxListViewSkin.this.itemCountDirty) {
                        skin.updateRowCount();
                        ComboBoxListViewSkin.this.itemCountDirty = false;
                    }
                    int rowsToMeasure = -1;
                    if (ComboBoxListViewSkin.this.comboBox.getProperties().containsKey(ComboBoxListViewSkin.COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY)) {
                        rowsToMeasure = ((Integer) ComboBoxListViewSkin.this.comboBox.getProperties().get(ComboBoxListViewSkin.COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY)).intValue();
                    }
                    pw = Math.max(ComboBoxListViewSkin.this.comboBox.getWidth(), skin.getMaxCellWidth(rowsToMeasure) + 30.0d);
                } else {
                    pw = Math.max(100.0d, ComboBoxListViewSkin.this.comboBox.getWidth());
                }
                if (getItems().isEmpty() && getPlaceholder() != null) {
                    pw = Math.max(super.computePrefWidth(height), pw);
                }
                return Math.max(50.0d, pw);
            }

            @Override // javafx.scene.control.Control, javafx.scene.layout.Region, javafx.scene.Parent
            protected double computePrefHeight(double width) {
                return ComboBoxListViewSkin.this.getListViewPrefHeight();
            }
        };
        _listView.setId("list-view");
        _listView.placeholderProperty().bind(this.comboBox.placeholderProperty());
        _listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _listView.setFocusTraversable(false);
        _listView.getSelectionModel().selectedIndexProperty().addListener(o2 -> {
            if (this.listSelectionLock) {
                return;
            }
            int index = this.listView.getSelectionModel().getSelectedIndex();
            this.comboBox.getSelectionModel().select(index);
            updateDisplayNode();
            this.comboBox.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
        });
        this.comboBox.getSelectionModel().selectedItemProperty().addListener(o3 -> {
            this.listViewSelectionDirty = true;
        });
        _listView.addEventFilter(MouseEvent.MOUSE_RELEASED, t2 -> {
            EventTarget target = t2.getTarget();
            if (target instanceof Parent) {
                List<String> s2 = ((Parent) target).getStyleClass();
                if (s2.contains("thumb") || s2.contains("track") || s2.contains("decrement-arrow") || s2.contains("increment-arrow")) {
                    return;
                }
            }
            if (isHideOnClickEnabled()) {
                this.comboBox.hide();
            }
        });
        _listView.setOnKeyPressed(t3 -> {
            if (t3.getCode() == KeyCode.ENTER || t3.getCode() == KeyCode.SPACE || t3.getCode() == KeyCode.ESCAPE) {
                this.comboBox.hide();
            }
        });
        return _listView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getListViewPrefHeight() {
        double ph;
        if (this.listView.getSkin() instanceof VirtualContainerBase) {
            int maxRows = this.comboBox.getVisibleRowCount();
            VirtualContainerBase<?, ?, ?> skin = (VirtualContainerBase) this.listView.getSkin();
            ph = skin.getVirtualFlowPreferredHeight(maxRows);
        } else {
            double ch = this.comboBoxItems.size() * 25;
            ph = Math.min(ch, 200.0d);
        }
        return ph;
    }

    public ListView<T> getListView() {
        return this.listView;
    }

    @Override // javafx.scene.control.SkinBase
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case FOCUS_ITEM:
                if (this.comboBox.isShowing()) {
                    return this.listView.queryAccessibleAttribute(attribute, parameters);
                }
                return null;
            case TEXT:
                String accText = this.comboBox.getAccessibleText();
                if (accText != null && !accText.isEmpty()) {
                    return accText;
                }
                String title = this.comboBox.isEditable() ? getEditor().getText() : this.buttonCell.getText();
                if (title == null || title.isEmpty()) {
                    title = this.comboBox.getPromptText();
                }
                return title;
            case SELECTION_START:
                return Integer.valueOf(getEditor().getSelection().getStart());
            case SELECTION_END:
                return Integer.valueOf(getEditor().getSelection().getEnd());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
