package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TwoLevelFocusPopupBehavior;
import com.sun.javafx.scene.control.skin.MenuBarSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Window;
import javafx.util.Duration;
import javax.swing.JInternalFrame;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ContextMenuContent.class */
public class ContextMenuContent extends Region {
    private ContextMenu contextMenu;
    MenuBox itemsContainer;
    private ArrowMenuItem upArrow;
    private ArrowMenuItem downArrow;
    private double ty;
    private Menu openSubmenu;
    private ContextMenu submenu;
    Region selectedBackground;
    private static final PseudoClass SELECTED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JInternalFrame.IS_SELECTED_PROPERTY);
    private static final PseudoClass DISABLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("disabled");
    private static final PseudoClass CHECKED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("checked");
    private double maxGraphicWidth = 0.0d;
    private double maxRightWidth = 0.0d;
    private double maxLabelWidth = 0.0d;
    private double maxRowHeight = 0.0d;
    private double maxLeftWidth = 0.0d;
    private double oldWidth = 0.0d;
    private int currentFocusedIndex = -1;
    private boolean itemsDirty = true;
    private InvalidationListener popupShowingListener = arg0 -> {
        updateItems();
    };
    private WeakInvalidationListener weakPopupShowingListener = new WeakInvalidationListener(this.popupShowingListener);
    private boolean isFirstShow = true;
    private ChangeListener<Boolean> menuShowingListener = (observable, wasShowing, isShowing) -> {
        ReadOnlyBooleanProperty isShowingProperty = (ReadOnlyBooleanProperty) observable;
        Menu menu = (Menu) isShowingProperty.getBean();
        if (wasShowing.booleanValue() && !isShowing.booleanValue()) {
            hideSubmenu();
        } else if (!wasShowing.booleanValue() && isShowing.booleanValue()) {
            showSubmenu(menu);
        }
    };
    private ListChangeListener<MenuItem> contextMenuItemsListener = c2 -> {
        while (c2.next()) {
            updateMenuShowingListeners(c2.getRemoved(), false);
            updateMenuShowingListeners(c2.getAddedSubList(), true);
        }
        this.itemsDirty = true;
        updateItems();
    };
    private ChangeListener<Boolean> menuItemVisibleListener = (observable, oldValue, newValue) -> {
        requestLayout();
    };
    private Rectangle clipRect = new Rectangle();

    public ContextMenuContent(ContextMenu popupMenu) {
        this.contextMenu = popupMenu;
        this.clipRect.setSmooth(false);
        this.itemsContainer = new MenuBox();
        this.itemsContainer.setClip(this.clipRect);
        this.upArrow = new ArrowMenuItem(this);
        this.upArrow.setUp(true);
        this.upArrow.setFocusTraversable(false);
        this.downArrow = new ArrowMenuItem(this);
        this.downArrow.setUp(false);
        this.downArrow.setFocusTraversable(false);
        getChildren().add(this.itemsContainer);
        getChildren().add(this.upArrow);
        getChildren().add(this.downArrow);
        initialize();
        setUpBinds();
        updateItems();
        popupMenu.showingProperty().addListener(this.weakPopupShowingListener);
        if (Utils.isTwoLevelFocus()) {
            new TwoLevelFocusPopupBehavior(this);
        }
    }

    public VBox getItemsContainer() {
        return this.itemsContainer;
    }

    int getCurrentFocusIndex() {
        return this.currentFocusedIndex;
    }

    void setCurrentFocusedIndex(int index) {
        if (index < this.itemsContainer.getChildren().size()) {
            this.currentFocusedIndex = index;
        }
    }

    private void updateItems() {
        if (this.itemsDirty) {
            updateVisualItems();
            this.itemsDirty = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void computeVisualMetrics() {
        double alt;
        double alt2;
        double alt3;
        double alt4;
        this.maxRightWidth = 0.0d;
        this.maxLabelWidth = 0.0d;
        this.maxRowHeight = 0.0d;
        this.maxGraphicWidth = 0.0d;
        this.maxLeftWidth = 0.0d;
        for (int i2 = 0; i2 < this.itemsContainer.getChildren().size(); i2++) {
            Node child = this.itemsContainer.getChildren().get(i2);
            if (child instanceof MenuItemContainer) {
                MenuItemContainer menuItemContainer = (MenuItemContainer) this.itemsContainer.getChildren().get(i2);
                if (menuItemContainer.isVisible()) {
                    Node n2 = menuItemContainer.left;
                    if (n2 != null) {
                        if (n2.getContentBias() == Orientation.VERTICAL) {
                            alt4 = snapSize(n2.prefHeight(-1.0d));
                        } else {
                            alt4 = -1.0d;
                        }
                        this.maxLeftWidth = Math.max(this.maxLeftWidth, snapSize(n2.prefWidth(alt4)));
                        this.maxRowHeight = Math.max(this.maxRowHeight, n2.prefHeight(-1.0d));
                    }
                    Node n3 = menuItemContainer.graphic;
                    if (n3 != null) {
                        if (n3.getContentBias() == Orientation.VERTICAL) {
                            alt3 = snapSize(n3.prefHeight(-1.0d));
                        } else {
                            alt3 = -1.0d;
                        }
                        this.maxGraphicWidth = Math.max(this.maxGraphicWidth, snapSize(n3.prefWidth(alt3)));
                        this.maxRowHeight = Math.max(this.maxRowHeight, n3.prefHeight(-1.0d));
                    }
                    Node n4 = menuItemContainer.label;
                    if (n4 != null) {
                        if (n4.getContentBias() == Orientation.VERTICAL) {
                            alt2 = snapSize(n4.prefHeight(-1.0d));
                        } else {
                            alt2 = -1.0d;
                        }
                        this.maxLabelWidth = Math.max(this.maxLabelWidth, snapSize(n4.prefWidth(alt2)));
                        this.maxRowHeight = Math.max(this.maxRowHeight, n4.prefHeight(-1.0d));
                    }
                    Node n5 = menuItemContainer.right;
                    if (n5 != null) {
                        if (n5.getContentBias() == Orientation.VERTICAL) {
                            alt = snapSize(n5.prefHeight(-1.0d));
                        } else {
                            alt = -1.0d;
                        }
                        this.maxRightWidth = Math.max(this.maxRightWidth, snapSize(n5.prefWidth(alt)));
                        this.maxRowHeight = Math.max(this.maxRowHeight, n5.prefHeight(-1.0d));
                    }
                }
            }
        }
        double newWidth = this.maxRightWidth + this.maxLabelWidth + this.maxGraphicWidth + this.maxLeftWidth;
        Window ownerWindow = this.contextMenu.getOwnerWindow();
        if ((ownerWindow instanceof ContextMenu) && this.contextMenu.getX() < ownerWindow.getX() && this.oldWidth != newWidth) {
            this.contextMenu.setX((this.contextMenu.getX() + this.oldWidth) - newWidth);
        }
        this.oldWidth = newWidth;
    }

    private void updateVisualItems() {
        ObservableList<Node> itemsContainerChilder = this.itemsContainer.getChildren();
        disposeVisualItems();
        for (int row = 0; row < getItems().size(); row++) {
            MenuItem item = getItems().get(row);
            if (!(item instanceof CustomMenuItem) || ((CustomMenuItem) item).getContent() != null) {
                if (item instanceof SeparatorMenuItem) {
                    Node node = ((CustomMenuItem) item).getContent();
                    node.visibleProperty().bind(item.visibleProperty());
                    itemsContainerChilder.add(node);
                    node.getProperties().put(MenuItem.class, item);
                } else {
                    MenuItemContainer menuItemContainer = new MenuItemContainer(item);
                    menuItemContainer.visibleProperty().bind(item.visibleProperty());
                    itemsContainerChilder.add(menuItemContainer);
                }
            }
        }
        if (getItems().size() > 0) {
            getProperties().put(Menu.class, getItems().get(0).getParentMenu());
        }
        impl_reapplyCSS();
    }

    private void disposeVisualItems() {
        ObservableList<Node> itemsContainerChilder = this.itemsContainer.getChildren();
        int max = itemsContainerChilder.size();
        for (int i2 = 0; i2 < max; i2++) {
            Node n2 = itemsContainerChilder.get(i2);
            if (n2 instanceof MenuItemContainer) {
                MenuItemContainer container = (MenuItemContainer) n2;
                container.visibleProperty().unbind();
                container.dispose();
            }
        }
        itemsContainerChilder.clear();
    }

    public void dispose() {
        disposeBinds();
        disposeVisualItems();
        disposeContextMenu(this.submenu);
        this.submenu = null;
        this.openSubmenu = null;
        this.selectedBackground = null;
        if (this.contextMenu != null) {
            this.contextMenu.getItems().clear();
            this.contextMenu = null;
        }
    }

    public void disposeContextMenu(ContextMenu menu) {
        Skin<?> skin;
        ContextMenuContent cmContent;
        if (menu == null || (skin = menu.getSkin()) == null || (cmContent = (ContextMenuContent) skin.getNode()) == null) {
            return;
        }
        cmContent.dispose();
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        if (this.itemsContainer.getChildren().size() == 0) {
            return;
        }
        double x2 = snappedLeftInset();
        double y2 = snappedTopInset();
        double w2 = (getWidth() - x2) - snappedRightInset();
        double h2 = (getHeight() - y2) - snappedBottomInset();
        double contentHeight = snapSize(getContentHeight());
        this.itemsContainer.resize(w2, contentHeight);
        this.itemsContainer.relocate(x2, y2);
        if (this.isFirstShow && this.ty == 0.0d) {
            this.upArrow.setVisible(false);
            this.isFirstShow = false;
        } else {
            this.upArrow.setVisible(this.ty < y2 && this.ty < 0.0d);
        }
        this.downArrow.setVisible(this.ty + contentHeight > y2 + h2);
        this.clipRect.setX(0.0d);
        this.clipRect.setY(0.0d);
        this.clipRect.setWidth(w2);
        this.clipRect.setHeight(h2);
        if (this.upArrow.isVisible()) {
            double prefHeight = snapSize(this.upArrow.prefHeight(-1.0d));
            this.clipRect.setHeight(snapSize(this.clipRect.getHeight() - prefHeight));
            this.clipRect.setY(snapSize(this.clipRect.getY()) + prefHeight);
            this.upArrow.resize(snapSize(this.upArrow.prefWidth(-1.0d)), prefHeight);
            positionInArea(this.upArrow, x2, y2, w2, prefHeight, 0.0d, HPos.CENTER, VPos.CENTER);
        }
        if (this.downArrow.isVisible()) {
            double prefHeight2 = snapSize(this.downArrow.prefHeight(-1.0d));
            this.clipRect.setHeight(snapSize(this.clipRect.getHeight()) - prefHeight2);
            this.downArrow.resize(snapSize(this.downArrow.prefWidth(-1.0d)), prefHeight2);
            positionInArea(this.downArrow, x2, (y2 + h2) - prefHeight2, w2, prefHeight2, 0.0d, HPos.CENTER, VPos.CENTER);
        }
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        computeVisualMetrics();
        double prefWidth = 0.0d;
        if (this.itemsContainer.getChildren().size() == 0) {
            return 0.0d;
        }
        for (Node n2 : this.itemsContainer.getChildren()) {
            if (n2.isVisible()) {
                prefWidth = Math.max(prefWidth, snapSize(n2.prefWidth(-1.0d)));
            }
        }
        return snappedLeftInset() + snapSize(prefWidth) + snappedRightInset();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        if (this.itemsContainer.getChildren().size() == 0) {
            return 0.0d;
        }
        double screenHeight = getScreenHeight();
        double contentHeight = getContentHeight();
        double totalHeight = snappedTopInset() + snapSize(contentHeight) + snappedBottomInset();
        double prefHeight = screenHeight <= 0.0d ? totalHeight : Math.min(totalHeight, screenHeight);
        return prefHeight;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        return 0.0d;
    }

    @Override // javafx.scene.layout.Region
    protected double computeMaxHeight(double height) {
        return getScreenHeight();
    }

    private double getScreenHeight() {
        if (this.contextMenu == null || this.contextMenu.getOwnerWindow() == null || this.contextMenu.getOwnerWindow().getScene() == null) {
            return -1.0d;
        }
        return snapSize(com.sun.javafx.util.Utils.getScreen(this.contextMenu.getOwnerWindow().getScene().getRoot()).getVisualBounds().getHeight());
    }

    private double getContentHeight() {
        double h2 = 0.0d;
        for (Node i2 : this.itemsContainer.getChildren()) {
            if (i2.isVisible()) {
                h2 += snapSize(i2.prefHeight(-1.0d));
            }
        }
        return h2;
    }

    private void ensureFocusedMenuItemIsVisible(Node node) {
        if (node == null) {
            return;
        }
        Bounds nodeBounds = node.getBoundsInParent();
        Bounds clipBounds = this.clipRect.getBoundsInParent();
        if (nodeBounds.getMaxY() >= clipBounds.getMaxY()) {
            scroll((-nodeBounds.getMaxY()) + clipBounds.getMaxY());
        } else if (nodeBounds.getMinY() <= clipBounds.getMinY()) {
            scroll((-nodeBounds.getMinY()) + clipBounds.getMinY());
        }
    }

    protected ObservableList<MenuItem> getItems() {
        return this.contextMenu.getItems();
    }

    private int findFocusedIndex() {
        for (int i2 = 0; i2 < this.itemsContainer.getChildren().size(); i2++) {
            Node n2 = this.itemsContainer.getChildren().get(i2);
            if (n2.isFocused()) {
                return i2;
            }
        }
        return -1;
    }

    private void initialize() {
        this.contextMenu.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.booleanValue()) {
                this.currentFocusedIndex = -1;
                requestFocus();
            }
        });
        this.contextMenu.addEventHandler(Menu.ON_SHOWN, event -> {
            for (Node child : this.itemsContainer.getChildren()) {
                if (child instanceof MenuItemContainer) {
                    MenuItem item = ((MenuItemContainer) child).item;
                    if ("choice-box-menu-item".equals(item.getId()) && ((RadioMenuItem) item).isSelected()) {
                        child.requestFocus();
                        return;
                    }
                }
            }
        });
        setOnKeyPressed(new EventHandler<KeyEvent>() { // from class: com.sun.javafx.scene.control.skin.ContextMenuContent.1
            @Override // javafx.event.EventHandler
            public void handle(KeyEvent ke) {
                MenuBarSkin mbs;
                Parent parent;
                switch (AnonymousClass3.$SwitchMap$javafx$scene$input$KeyCode[ke.getCode().ordinal()]) {
                    case 1:
                        if (ContextMenuContent.this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                            ContextMenuContent.this.processRightKey(ke);
                            break;
                        } else {
                            ContextMenuContent.this.processLeftKey(ke);
                            break;
                        }
                    case 2:
                        if (ContextMenuContent.this.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                            ContextMenuContent.this.processLeftKey(ke);
                            break;
                        } else {
                            ContextMenuContent.this.processRightKey(ke);
                            break;
                        }
                    case 3:
                        ke.consume();
                        break;
                    case 4:
                        if (!(ContextMenuContent.this.contextMenu.getOwnerNode() instanceof MenuBarSkin.MenuBarButton)) {
                            ContextMenuContent.this.contextMenu.hide();
                            ke.consume();
                            break;
                        }
                        break;
                    case 5:
                        ContextMenuContent.this.moveToNextSibling();
                        ke.consume();
                        break;
                    case 6:
                        ContextMenuContent.this.moveToPreviousSibling();
                        ke.consume();
                        break;
                    case 7:
                    case 8:
                        ContextMenuContent.this.selectMenuItem();
                        ke.consume();
                        break;
                }
                if (!ke.isConsumed()) {
                    Node ownerNode = ContextMenuContent.this.contextMenu.getOwnerNode();
                    if (ownerNode instanceof MenuItemContainer) {
                        Parent parent2 = ownerNode.getParent();
                        while (true) {
                            parent = parent2;
                            if (parent != null && !(parent instanceof ContextMenuContent)) {
                                parent2 = parent.getParent();
                            }
                        }
                        if (parent instanceof ContextMenuContent) {
                            parent.getOnKeyPressed().handle(ke);
                            return;
                        }
                        return;
                    }
                    if ((ownerNode instanceof MenuBarSkin.MenuBarButton) && (mbs = ((MenuBarSkin.MenuBarButton) ownerNode).getMenuBarSkin()) != null && mbs.getKeyEventHandler() != null) {
                        mbs.getKeyEventHandler().handle(ke);
                    }
                }
            }
        });
        addEventHandler(ScrollEvent.SCROLL, event2 -> {
            double textDeltaY = event2.getTextDeltaY();
            double deltaY = event2.getDeltaY();
            if (!this.downArrow.isVisible() || (textDeltaY >= 0.0d && deltaY >= 0.0d)) {
                if (!this.upArrow.isVisible()) {
                    return;
                }
                if (textDeltaY <= 0.0d && deltaY <= 0.0d) {
                    return;
                }
            }
            switch (event2.getTextDeltaYUnits()) {
                case LINES:
                    int focusedIndex = findFocusedIndex();
                    if (focusedIndex == -1) {
                        focusedIndex = 0;
                    }
                    double rowHeight = this.itemsContainer.getChildren().get(focusedIndex).prefHeight(-1.0d);
                    scroll(textDeltaY * rowHeight);
                    break;
                case PAGES:
                    scroll(textDeltaY * this.itemsContainer.getHeight());
                    break;
                case NONE:
                    scroll(deltaY);
                    break;
            }
            event2.consume();
        });
    }

    /* renamed from: com.sun.javafx.scene.control.skin.ContextMenuContent$3, reason: invalid class name */
    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ContextMenuContent$3.class */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$javafx$scene$input$KeyCode;

        static {
            try {
                $SwitchMap$javafx$scene$input$ScrollEvent$VerticalTextScrollUnits[ScrollEvent.VerticalTextScrollUnits.LINES.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javafx$scene$input$ScrollEvent$VerticalTextScrollUnits[ScrollEvent.VerticalTextScrollUnits.PAGES.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javafx$scene$input$ScrollEvent$VerticalTextScrollUnits[ScrollEvent.VerticalTextScrollUnits.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$javafx$scene$AccessibleAction = new int[AccessibleAction.values().length];
            try {
                $SwitchMap$javafx$scene$AccessibleAction[AccessibleAction.SHOW_MENU.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAction[AccessibleAction.FIRE.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            $SwitchMap$javafx$scene$AccessibleAttribute = new int[AccessibleAttribute.values().length];
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.VISIBLE.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.PARENT_MENU.ordinal()] = 2;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.SELECTED.ordinal()] = 3;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.ACCELERATOR.ordinal()] = 4;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.TEXT.ordinal()] = 5;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.MNEMONIC.ordinal()] = 6;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.DISABLED.ordinal()] = 7;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$javafx$scene$AccessibleAttribute[AccessibleAttribute.SUBMENU.ordinal()] = 8;
            } catch (NoSuchFieldError e14) {
            }
            $SwitchMap$javafx$scene$input$KeyCode = new int[KeyCode.values().length];
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.CANCEL.ordinal()] = 3;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.ESCAPE.ordinal()] = 4;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.DOWN.ordinal()] = 5;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.UP.ordinal()] = 6;
            } catch (NoSuchFieldError e20) {
            }
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.SPACE.ordinal()] = 7;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$javafx$scene$input$KeyCode[KeyCode.ENTER.ordinal()] = 8;
            } catch (NoSuchFieldError e22) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLeftKey(KeyEvent ke) {
        if (this.currentFocusedIndex != -1) {
            Node n2 = this.itemsContainer.getChildren().get(this.currentFocusedIndex);
            if (!(n2 instanceof MenuItemContainer)) {
                return;
            }
            MenuItem item = ((MenuItemContainer) n2).item;
            if (item instanceof Menu) {
                Menu menu = (Menu) item;
                if (menu == this.openSubmenu && this.submenu != null && this.submenu.isShowing()) {
                    hideSubmenu();
                    ke.consume();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processRightKey(KeyEvent ke) {
        if (this.currentFocusedIndex != -1) {
            Node n2 = this.itemsContainer.getChildren().get(this.currentFocusedIndex);
            if (!(n2 instanceof MenuItemContainer)) {
                return;
            }
            MenuItem item = ((MenuItemContainer) n2).item;
            if (item instanceof Menu) {
                Menu menu = (Menu) item;
                if (menu.isDisable()) {
                    return;
                }
                this.selectedBackground = (MenuItemContainer) n2;
                if (this.openSubmenu == menu && this.submenu != null && this.submenu.isShowing()) {
                    return;
                }
                showMenu(menu);
                ke.consume();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showMenu(Menu menu) {
        menu.show();
        ContextMenuContent cmContent = (ContextMenuContent) this.submenu.getSkin().getNode();
        if (cmContent != null) {
            if (cmContent.itemsContainer.getChildren().size() > 0) {
                cmContent.itemsContainer.getChildren().get(0).requestFocus();
                cmContent.currentFocusedIndex = 0;
            } else {
                cmContent.requestFocus();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectMenuItem() {
        if (this.currentFocusedIndex != -1) {
            Node n2 = this.itemsContainer.getChildren().get(this.currentFocusedIndex);
            if (!(n2 instanceof MenuItemContainer)) {
                return;
            }
            MenuItem item = ((MenuItemContainer) n2).item;
            if (item instanceof Menu) {
                Menu menu = (Menu) item;
                if (this.openSubmenu != null) {
                    hideSubmenu();
                }
                if (menu.isDisable()) {
                    return;
                }
                this.selectedBackground = (MenuItemContainer) n2;
                menu.show();
                return;
            }
            ((MenuItemContainer) n2).doSelect();
        }
    }

    private int findNext(int from) {
        for (int i2 = from; i2 < this.itemsContainer.getChildren().size(); i2++) {
            Node n2 = this.itemsContainer.getChildren().get(i2);
            if (n2 instanceof MenuItemContainer) {
                return i2;
            }
        }
        for (int i3 = 0; i3 < from; i3++) {
            Node n3 = this.itemsContainer.getChildren().get(i3);
            if (n3 instanceof MenuItemContainer) {
                return i3;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void moveToNextSibling() {
        if (this.currentFocusedIndex != -1) {
            this.currentFocusedIndex = findNext(this.currentFocusedIndex + 1);
        } else if (this.currentFocusedIndex == -1 || this.currentFocusedIndex == this.itemsContainer.getChildren().size() - 1) {
            this.currentFocusedIndex = findNext(0);
        }
        if (this.currentFocusedIndex != -1) {
            Node n2 = this.itemsContainer.getChildren().get(this.currentFocusedIndex);
            this.selectedBackground = (MenuItemContainer) n2;
            n2.requestFocus();
            ensureFocusedMenuItemIsVisible(n2);
        }
    }

    private int findPrevious(int from) {
        for (int i2 = from; i2 >= 0; i2--) {
            Node n2 = this.itemsContainer.getChildren().get(i2);
            if (n2 instanceof MenuItemContainer) {
                return i2;
            }
        }
        for (int i3 = this.itemsContainer.getChildren().size() - 1; i3 > from; i3--) {
            Node n3 = this.itemsContainer.getChildren().get(i3);
            if (n3 instanceof MenuItemContainer) {
                return i3;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void moveToPreviousSibling() {
        if (this.currentFocusedIndex != -1) {
            this.currentFocusedIndex = findPrevious(this.currentFocusedIndex - 1);
        } else if (this.currentFocusedIndex == -1 || this.currentFocusedIndex == 0) {
            this.currentFocusedIndex = findPrevious(this.itemsContainer.getChildren().size() - 1);
        }
        if (this.currentFocusedIndex != -1) {
            Node n2 = this.itemsContainer.getChildren().get(this.currentFocusedIndex);
            this.selectedBackground = (MenuItemContainer) n2;
            n2.requestFocus();
            ensureFocusedMenuItemIsVisible(n2);
        }
    }

    double getMenuYOffset(int menuIndex) {
        double offset = 0.0d;
        if (this.itemsContainer.getChildren().size() > menuIndex) {
            double offset2 = snappedTopInset();
            Node menuitem = this.itemsContainer.getChildren().get(menuIndex);
            offset = offset2 + menuitem.getLayoutY() + menuitem.prefHeight(-1.0d);
        }
        return offset;
    }

    private void setUpBinds() {
        updateMenuShowingListeners(this.contextMenu.getItems(), true);
        this.contextMenu.getItems().addListener(this.contextMenuItemsListener);
    }

    private void disposeBinds() {
        updateMenuShowingListeners(this.contextMenu.getItems(), false);
        this.contextMenu.getItems().removeListener(this.contextMenuItemsListener);
    }

    private void updateMenuShowingListeners(List<? extends MenuItem> items, boolean addListeners) {
        for (MenuItem item : items) {
            if (item instanceof Menu) {
                Menu menu = (Menu) item;
                if (addListeners) {
                    menu.showingProperty().addListener(this.menuShowingListener);
                } else {
                    menu.showingProperty().removeListener(this.menuShowingListener);
                }
            }
            if (addListeners) {
                item.visibleProperty().addListener(this.menuItemVisibleListener);
            } else {
                item.visibleProperty().removeListener(this.menuItemVisibleListener);
            }
        }
    }

    ContextMenu getSubMenu() {
        return this.submenu;
    }

    Menu getOpenSubMenu() {
        return this.openSubmenu;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createSubmenu() {
        if (this.submenu == null) {
            this.submenu = new ContextMenu();
            this.submenu.showingProperty().addListener(new ChangeListener<Boolean>() { // from class: com.sun.javafx.scene.control.skin.ContextMenuContent.2
                @Override // javafx.beans.value.ChangeListener
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!ContextMenuContent.this.submenu.isShowing()) {
                        for (Node node : ContextMenuContent.this.itemsContainer.getChildren()) {
                            if ((node instanceof MenuItemContainer) && (((MenuItemContainer) node).item instanceof Menu)) {
                                Menu menu = (Menu) ((MenuItemContainer) node).item;
                                if (menu.isShowing()) {
                                    menu.hide();
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void showSubmenu(Menu menu) {
        this.openSubmenu = menu;
        createSubmenu();
        this.submenu.getItems().setAll(menu.getItems());
        this.submenu.show(this.selectedBackground, Side.RIGHT, 0.0d, 0.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideSubmenu() {
        if (this.submenu == null) {
            return;
        }
        this.submenu.hide();
        this.openSubmenu = null;
        disposeContextMenu(this.submenu);
        this.submenu = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideAllMenus(MenuItem item) {
        if (this.contextMenu != null) {
            this.contextMenu.hide();
        }
        while (true) {
            Menu parentMenu = item.getParentMenu();
            if (parentMenu == null) {
                break;
            }
            parentMenu.hide();
            item = parentMenu;
        }
        if (item.getParentPopup() != null) {
            item.getParentPopup().hide();
        }
    }

    void scroll(double delta) {
        double newTy = this.ty + delta;
        if (this.ty == newTy) {
            return;
        }
        if (newTy > 0.0d) {
            newTy = 0.0d;
        }
        if (delta < 0.0d && getHeight() - newTy > this.itemsContainer.getHeight() - this.downArrow.getHeight()) {
            newTy = (getHeight() - this.itemsContainer.getHeight()) - this.downArrow.getHeight();
        }
        this.ty = newTy;
        this.itemsContainer.requestLayout();
    }

    @Override // javafx.scene.Node, javafx.css.Styleable
    public Styleable getStyleableParent() {
        return this.contextMenu;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ContextMenuContent$StyleableProperties.class */
    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            List<CssMetaData<? extends Styleable, ?>> nodeStyleables = Node.getClassCssMetaData();
            int n2 = 0;
            int max = nodeStyleables.size();
            while (true) {
                if (n2 >= max) {
                    break;
                }
                CssMetaData<? extends Styleable, ?> styleable = nodeStyleables.get(n2);
                if (!"effect".equals(styleable.getProperty())) {
                    n2++;
                } else {
                    styleables.add(styleable);
                    break;
                }
            }
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Node, javafx.css.Styleable
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    protected Label getLabelAt(int index) {
        return ((MenuItemContainer) this.itemsContainer.getChildren().get(index)).getLabel();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ContextMenuContent$MenuBox.class */
    class MenuBox extends VBox {
        MenuBox() {
            setAccessibleRole(AccessibleRole.CONTEXT_MENU);
        }

        @Override // javafx.scene.layout.VBox, javafx.scene.Parent
        protected void layoutChildren() {
            double yOffset = ContextMenuContent.this.ty;
            for (Node n2 : getChildren()) {
                if (n2.isVisible()) {
                    double prefHeight = snapSize(n2.prefHeight(-1.0d));
                    n2.resize(snapSize(getWidth()), prefHeight);
                    n2.relocate(snappedLeftInset(), yOffset);
                    yOffset += prefHeight;
                }
            }
        }

        @Override // javafx.scene.Parent, javafx.scene.Node
        public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
            switch (attribute) {
                case VISIBLE:
                    return Boolean.valueOf(ContextMenuContent.this.contextMenu.isShowing());
                case PARENT_MENU:
                    return ContextMenuContent.this.contextMenu.getOwnerNode();
                default:
                    return super.queryAccessibleAttribute(attribute, parameters);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ContextMenuContent$ArrowMenuItem.class */
    class ArrowMenuItem extends StackPane {
        private StackPane upDownArrow;
        private ContextMenuContent popupMenuContent;
        private boolean up = false;
        private Timeline scrollTimeline;

        public final boolean isUp() {
            return this.up;
        }

        public void setUp(boolean value) {
            this.up = value;
            ObservableList<String> styleClass = this.upDownArrow.getStyleClass();
            String[] strArr = new String[1];
            strArr[0] = isUp() ? "menu-up-arrow" : "menu-down-arrow";
            styleClass.setAll(strArr);
        }

        public ArrowMenuItem(ContextMenuContent pmc) {
            getStyleClass().setAll("scroll-arrow");
            this.upDownArrow = new StackPane();
            this.popupMenuContent = pmc;
            this.upDownArrow.setMouseTransparent(true);
            ObservableList<String> styleClass = this.upDownArrow.getStyleClass();
            String[] strArr = new String[1];
            strArr[0] = isUp() ? "menu-up-arrow" : "menu-down-arrow";
            styleClass.setAll(strArr);
            addEventHandler(MouseEvent.MOUSE_ENTERED, me -> {
                if (this.scrollTimeline != null && this.scrollTimeline.getStatus() != Animation.Status.STOPPED) {
                    return;
                }
                startTimeline();
            });
            addEventHandler(MouseEvent.MOUSE_EXITED, me2 -> {
                stopTimeline();
            });
            setVisible(false);
            setManaged(false);
            getChildren().add(this.upDownArrow);
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            return ContextMenuContent.this.itemsContainer.getWidth();
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            return snappedTopInset() + this.upDownArrow.prefHeight(-1.0d) + snappedBottomInset();
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
        protected void layoutChildren() {
            double w2 = snapSize(this.upDownArrow.prefWidth(-1.0d));
            double h2 = snapSize(this.upDownArrow.prefHeight(-1.0d));
            this.upDownArrow.resize(w2, h2);
            positionInArea(this.upDownArrow, 0.0d, 0.0d, getWidth(), getHeight(), 0.0d, HPos.CENTER, VPos.CENTER);
        }

        private void adjust() {
            if (this.up) {
                this.popupMenuContent.scroll(12.0d);
            } else {
                this.popupMenuContent.scroll(-12.0d);
            }
        }

        private void startTimeline() {
            this.scrollTimeline = new Timeline();
            this.scrollTimeline.setCycleCount(-1);
            KeyFrame kf = new KeyFrame(Duration.millis(60.0d), (EventHandler<ActionEvent>) event -> {
                adjust();
            }, new KeyValue[0]);
            this.scrollTimeline.getKeyFrames().clear();
            this.scrollTimeline.getKeyFrames().add(kf);
            this.scrollTimeline.play();
        }

        private void stopTimeline() {
            this.scrollTimeline.stop();
            this.scrollTimeline = null;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ContextMenuContent$MenuItemContainer.class */
    public class MenuItemContainer extends Region {
        private final MenuItem item;
        private Node left;
        private Node graphic;
        private Node label;
        private Node right;
        private final MultiplePropertyChangeListenerHandler listener = new MultiplePropertyChangeListenerHandler(param -> {
            handlePropertyChanged(param);
            return null;
        });
        private EventHandler<MouseEvent> mouseEnteredEventHandler;
        private EventHandler<MouseEvent> mouseReleasedEventHandler;
        private EventHandler<ActionEvent> actionEventHandler;
        private EventHandler<MouseEvent> customMenuItemMouseClickedHandler;

        protected Label getLabel() {
            return (Label) this.label;
        }

        public MenuItem getItem() {
            return this.item;
        }

        public MenuItemContainer(MenuItem item) {
            if (item == null) {
                throw new NullPointerException("MenuItem can not be null");
            }
            getStyleClass().addAll(item.getStyleClass());
            setId(item.getId());
            setFocusTraversable(!(item instanceof CustomMenuItem));
            this.item = item;
            createChildren();
            if (item instanceof Menu) {
                ReadOnlyBooleanProperty pseudoProperty = ((Menu) item).showingProperty();
                this.listener.registerChangeListener(pseudoProperty, "MENU_SHOWING");
                pseudoClassStateChanged(ContextMenuContent.SELECTED_PSEUDOCLASS_STATE, pseudoProperty.get());
                setAccessibleRole(AccessibleRole.MENU);
            } else if (item instanceof RadioMenuItem) {
                ReadOnlyBooleanProperty pseudoProperty2 = ((RadioMenuItem) item).selectedProperty();
                this.listener.registerChangeListener(pseudoProperty2, "RADIO_ITEM_SELECTED");
                pseudoClassStateChanged(ContextMenuContent.CHECKED_PSEUDOCLASS_STATE, pseudoProperty2.get());
                setAccessibleRole(AccessibleRole.RADIO_MENU_ITEM);
            } else if (item instanceof CheckMenuItem) {
                ReadOnlyBooleanProperty pseudoProperty3 = ((CheckMenuItem) item).selectedProperty();
                this.listener.registerChangeListener(pseudoProperty3, "CHECK_ITEM_SELECTED");
                pseudoClassStateChanged(ContextMenuContent.CHECKED_PSEUDOCLASS_STATE, pseudoProperty3.get());
                setAccessibleRole(AccessibleRole.CHECK_MENU_ITEM);
            } else {
                setAccessibleRole(AccessibleRole.MENU_ITEM);
            }
            pseudoClassStateChanged(ContextMenuContent.DISABLED_PSEUDOCLASS_STATE, item.disableProperty().get());
            this.listener.registerChangeListener(item.disableProperty(), "DISABLE");
            getProperties().put(MenuItem.class, item);
            this.listener.registerChangeListener(item.graphicProperty(), "GRAPHIC");
            this.actionEventHandler = e2 -> {
                if (item instanceof Menu) {
                    Menu menu = (Menu) item;
                    if (ContextMenuContent.this.openSubmenu == menu && ContextMenuContent.this.submenu.isShowing()) {
                        return;
                    }
                    if (ContextMenuContent.this.openSubmenu != null) {
                        ContextMenuContent.this.hideSubmenu();
                    }
                    ContextMenuContent.this.selectedBackground = this;
                    ContextMenuContent.this.showMenu(menu);
                    return;
                }
                doSelect();
            };
            addEventHandler(ActionEvent.ACTION, this.actionEventHandler);
        }

        public void dispose() {
            Node node;
            if ((this.item instanceof CustomMenuItem) && (node = ((CustomMenuItem) this.item).getContent()) != null) {
                node.removeEventHandler(MouseEvent.MOUSE_CLICKED, this.customMenuItemMouseClickedHandler);
            }
            this.listener.dispose();
            removeEventHandler(ActionEvent.ACTION, this.actionEventHandler);
            if (this.label != null) {
                ((Label) this.label).textProperty().unbind();
            }
            this.left = null;
            this.graphic = null;
            this.label = null;
            this.right = null;
        }

        private void handlePropertyChanged(String p2) {
            if ("MENU_SHOWING".equals(p2)) {
                Menu menu = (Menu) this.item;
                pseudoClassStateChanged(ContextMenuContent.SELECTED_PSEUDOCLASS_STATE, menu.isShowing());
                return;
            }
            if ("RADIO_ITEM_SELECTED".equals(p2)) {
                RadioMenuItem radioItem = (RadioMenuItem) this.item;
                pseudoClassStateChanged(ContextMenuContent.CHECKED_PSEUDOCLASS_STATE, radioItem.isSelected());
                return;
            }
            if ("CHECK_ITEM_SELECTED".equals(p2)) {
                CheckMenuItem checkItem = (CheckMenuItem) this.item;
                pseudoClassStateChanged(ContextMenuContent.CHECKED_PSEUDOCLASS_STATE, checkItem.isSelected());
                return;
            }
            if ("DISABLE".equals(p2)) {
                pseudoClassStateChanged(ContextMenuContent.DISABLED_PSEUDOCLASS_STATE, this.item.isDisable());
                return;
            }
            if ("GRAPHIC".equals(p2)) {
                createChildren();
                ContextMenuContent.this.computeVisualMetrics();
            } else if ("ACCELERATOR".equals(p2)) {
                updateAccelerator();
            } else if ("FOCUSED".equals(p2) && isFocused()) {
                ContextMenuContent.this.currentFocusedIndex = ContextMenuContent.this.itemsContainer.getChildren().indexOf(this);
            }
        }

        private void createChildren() {
            getChildren().clear();
            if (this.item instanceof CustomMenuItem) {
                createNodeMenuItemChildren((CustomMenuItem) this.item);
                if (this.mouseEnteredEventHandler == null) {
                    this.mouseEnteredEventHandler = event -> {
                        requestFocus();
                    };
                } else {
                    removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
                }
                addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
                return;
            }
            Node leftNode = getLeftGraphic(this.item);
            if (leftNode != null) {
                StackPane leftPane = new StackPane();
                leftPane.getStyleClass().add("left-container");
                leftPane.getChildren().add(leftNode);
                this.left = leftPane;
                getChildren().add(this.left);
                this.left.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            }
            if (this.item.getGraphic() != null) {
                Node graphicNode = this.item.getGraphic();
                StackPane graphicPane = new StackPane();
                graphicPane.getStyleClass().add("graphic-container");
                graphicPane.getChildren().add(graphicNode);
                this.graphic = graphicPane;
                getChildren().add(this.graphic);
            }
            this.label = ContextMenuContent.this.new MenuLabel(this.item, this);
            this.label.setStyle(this.item.getStyle());
            ((Label) this.label).textProperty().bind(this.item.textProperty());
            this.label.setMouseTransparent(true);
            getChildren().add(this.label);
            this.listener.unregisterChangeListener(focusedProperty());
            this.listener.registerChangeListener(focusedProperty(), "FOCUSED");
            if (this.item instanceof Menu) {
                Region rightNode = new Region();
                rightNode.setMouseTransparent(true);
                rightNode.getStyleClass().add("arrow");
                StackPane rightPane = new StackPane();
                rightPane.setMaxWidth(Math.max(rightNode.prefWidth(-1.0d), 10.0d));
                rightPane.setMouseTransparent(true);
                rightPane.getStyleClass().add("right-container");
                rightPane.getChildren().add(rightNode);
                this.right = rightPane;
                getChildren().add(rightPane);
                if (this.mouseEnteredEventHandler == null) {
                    this.mouseEnteredEventHandler = event2 -> {
                        if (ContextMenuContent.this.openSubmenu != null && this.item != ContextMenuContent.this.openSubmenu) {
                            ContextMenuContent.this.hideSubmenu();
                        }
                        Menu menu = (Menu) this.item;
                        if (menu.isDisable()) {
                            return;
                        }
                        ContextMenuContent.this.selectedBackground = this;
                        menu.show();
                        requestFocus();
                    };
                } else {
                    removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
                }
                if (this.mouseReleasedEventHandler == null) {
                    this.mouseReleasedEventHandler = event3 -> {
                        this.item.fire();
                    };
                } else {
                    removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
                }
                addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
                addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
                return;
            }
            this.listener.unregisterChangeListener(this.item.acceleratorProperty());
            updateAccelerator();
            if (this.mouseEnteredEventHandler == null) {
                this.mouseEnteredEventHandler = event4 -> {
                    if (ContextMenuContent.this.openSubmenu != null) {
                        ContextMenuContent.this.openSubmenu.hide();
                    }
                    requestFocus();
                };
            } else {
                removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
            }
            if (this.mouseReleasedEventHandler == null) {
                this.mouseReleasedEventHandler = event5 -> {
                    doSelect();
                };
            } else {
                removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
            }
            addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseEnteredEventHandler);
            addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseReleasedEventHandler);
            this.listener.registerChangeListener(this.item.acceleratorProperty(), "ACCELERATOR");
        }

        private void updateAccelerator() {
            if (this.item.getAccelerator() != null) {
                if (this.right != null) {
                    getChildren().remove(this.right);
                }
                String text = this.item.getAccelerator().getDisplayText();
                this.right = new Label(text);
                this.right.setStyle(this.item.getStyle());
                this.right.getStyleClass().add("accelerator-text");
                getChildren().add(this.right);
                return;
            }
            getChildren().remove(this.right);
        }

        void doSelect() {
            if (this.item.isDisable()) {
                return;
            }
            if (this.item instanceof CheckMenuItem) {
                CheckMenuItem checkItem = (CheckMenuItem) this.item;
                checkItem.setSelected(!checkItem.isSelected());
            } else if (this.item instanceof RadioMenuItem) {
                RadioMenuItem radioItem = (RadioMenuItem) this.item;
                boolean z2 = (radioItem.getToggleGroup() == null && radioItem.isSelected()) ? false : true;
                radioItem.setSelected(z2);
            }
            this.item.fire();
            if (!(this.item instanceof CustomMenuItem)) {
                ContextMenuContent.this.hideAllMenus(this.item);
                return;
            }
            CustomMenuItem customMenuItem = (CustomMenuItem) this.item;
            if (customMenuItem.isHideOnClick()) {
                ContextMenuContent.this.hideAllMenus(this.item);
            }
        }

        private void createNodeMenuItemChildren(CustomMenuItem item) {
            Node node = item.getContent();
            getChildren().add(node);
            this.customMenuItemMouseClickedHandler = event -> {
                if (item == null || item.isDisable()) {
                    return;
                }
                item.fire();
                if (item.isHideOnClick()) {
                    ContextMenuContent.this.hideAllMenus(item);
                }
            };
            node.addEventHandler(MouseEvent.MOUSE_CLICKED, this.customMenuItemMouseClickedHandler);
        }

        @Override // javafx.scene.Parent
        protected void layoutChildren() {
            double prefHeight = prefHeight(-1.0d);
            if (this.left != null) {
                double xOffset = snappedLeftInset();
                this.left.resize(this.left.prefWidth(-1.0d), this.left.prefHeight(-1.0d));
                positionInArea(this.left, xOffset, 0.0d, ContextMenuContent.this.maxLeftWidth, prefHeight, 0.0d, HPos.LEFT, VPos.CENTER);
            }
            if (this.graphic != null) {
                double xOffset2 = snappedLeftInset() + ContextMenuContent.this.maxLeftWidth;
                this.graphic.resize(this.graphic.prefWidth(-1.0d), this.graphic.prefHeight(-1.0d));
                positionInArea(this.graphic, xOffset2, 0.0d, ContextMenuContent.this.maxGraphicWidth, prefHeight, 0.0d, HPos.LEFT, VPos.CENTER);
            }
            if (this.label != null) {
                double xOffset3 = snappedLeftInset() + ContextMenuContent.this.maxLeftWidth + ContextMenuContent.this.maxGraphicWidth;
                this.label.resize(this.label.prefWidth(-1.0d), this.label.prefHeight(-1.0d));
                positionInArea(this.label, xOffset3, 0.0d, ContextMenuContent.this.maxLabelWidth, prefHeight, 0.0d, HPos.LEFT, VPos.CENTER);
            }
            if (this.right != null) {
                double xOffset4 = snappedLeftInset() + ContextMenuContent.this.maxLeftWidth + ContextMenuContent.this.maxGraphicWidth + ContextMenuContent.this.maxLabelWidth;
                this.right.resize(this.right.prefWidth(-1.0d), this.right.prefHeight(-1.0d));
                positionInArea(this.right, xOffset4, 0.0d, ContextMenuContent.this.maxRightWidth, prefHeight, 0.0d, HPos.RIGHT, VPos.CENTER);
            }
            if (this.item instanceof CustomMenuItem) {
                Node n2 = ((CustomMenuItem) this.item).getContent();
                if (this.item instanceof SeparatorMenuItem) {
                    double width = prefWidth(-1.0d) - ((snappedLeftInset() + ContextMenuContent.this.maxGraphicWidth) + snappedRightInset());
                    n2.resize(width, n2.prefHeight(-1.0d));
                    positionInArea(n2, snappedLeftInset() + ContextMenuContent.this.maxGraphicWidth, 0.0d, prefWidth(-1.0d), prefHeight, 0.0d, HPos.LEFT, VPos.CENTER);
                } else {
                    n2.resize(n2.prefWidth(-1.0d), n2.prefHeight(-1.0d));
                    positionInArea(n2, snappedLeftInset(), 0.0d, getWidth(), prefHeight, 0.0d, HPos.LEFT, VPos.CENTER);
                }
            }
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            double prefHeight;
            if ((this.item instanceof CustomMenuItem) || (this.item instanceof SeparatorMenuItem)) {
                prefHeight = getChildren().isEmpty() ? 0.0d : getChildren().get(0).prefHeight(-1.0d);
            } else {
                double prefHeight2 = Math.max(0.0d, this.left != null ? this.left.prefHeight(-1.0d) : 0.0d);
                prefHeight = Math.max(Math.max(Math.max(prefHeight2, this.graphic != null ? this.graphic.prefHeight(-1.0d) : 0.0d), this.label != null ? this.label.prefHeight(-1.0d) : 0.0d), this.right != null ? this.right.prefHeight(-1.0d) : 0.0d);
            }
            return snappedTopInset() + prefHeight + snappedBottomInset();
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            double nodeMenuItemWidth = 0.0d;
            if ((this.item instanceof CustomMenuItem) && !(this.item instanceof SeparatorMenuItem)) {
                nodeMenuItemWidth = snappedLeftInset() + ((CustomMenuItem) this.item).getContent().prefWidth(-1.0d) + snappedRightInset();
            }
            return Math.max(nodeMenuItemWidth, snappedLeftInset() + ContextMenuContent.this.maxLeftWidth + ContextMenuContent.this.maxGraphicWidth + ContextMenuContent.this.maxLabelWidth + ContextMenuContent.this.maxRightWidth + snappedRightInset());
        }

        private Node getLeftGraphic(MenuItem item) {
            if (item instanceof RadioMenuItem) {
                Region _graphic = new Region();
                _graphic.getStyleClass().add("radio");
                return _graphic;
            }
            if (item instanceof CheckMenuItem) {
                StackPane _graphic2 = new StackPane();
                _graphic2.getStyleClass().add("check");
                return _graphic2;
            }
            return null;
        }

        @Override // javafx.scene.Parent, javafx.scene.Node
        public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
            String mnemonic;
            Node content;
            String t2;
            String t3;
            String t4;
            switch (attribute) {
                case SELECTED:
                    if (this.item instanceof CheckMenuItem) {
                        return Boolean.valueOf(((CheckMenuItem) this.item).isSelected());
                    }
                    if (this.item instanceof RadioMenuItem) {
                        return Boolean.valueOf(((RadioMenuItem) this.item).isSelected());
                    }
                    return false;
                case ACCELERATOR:
                    return this.item.getAccelerator();
                case TEXT:
                    String title = "";
                    if (this.graphic != null && (t4 = (String) this.graphic.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0])) != null) {
                        title = title + t4;
                    }
                    Label label = getLabel();
                    if (label != null && (t3 = (String) label.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0])) != null) {
                        title = title + t3;
                    }
                    if ((this.item instanceof CustomMenuItem) && (content = ((CustomMenuItem) this.item).getContent()) != null && (t2 = (String) content.queryAccessibleAttribute(AccessibleAttribute.TEXT, new Object[0])) != null) {
                        title = title + t2;
                    }
                    return title;
                case MNEMONIC:
                    Label label2 = getLabel();
                    if (label2 == null || (mnemonic = (String) label2.queryAccessibleAttribute(AccessibleAttribute.MNEMONIC, new Object[0])) == null) {
                        return null;
                    }
                    return mnemonic;
                case DISABLED:
                    return Boolean.valueOf(this.item.isDisable());
                case SUBMENU:
                    ContextMenuContent.this.createSubmenu();
                    if (ContextMenuContent.this.submenu.getSkin() == null) {
                        ContextMenuContent.this.submenu.impl_styleableGetNode().impl_processCSS(true);
                    }
                    ContextMenuContent cmContent = (ContextMenuContent) ContextMenuContent.this.submenu.getSkin().getNode();
                    return cmContent.itemsContainer;
                default:
                    return super.queryAccessibleAttribute(attribute, parameters);
            }
        }

        @Override // javafx.scene.Node
        public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
            switch (action) {
                case SHOW_MENU:
                    if (this.item instanceof Menu) {
                        Menu menuItem = (Menu) this.item;
                        if (menuItem.isShowing()) {
                            menuItem.hide();
                            break;
                        } else {
                            menuItem.show();
                            break;
                        }
                    }
                    break;
                case FIRE:
                    doSelect();
                    break;
                default:
                    super.executeAccessibleAction(action, new Object[0]);
                    break;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ContextMenuContent$MenuLabel.class */
    private class MenuLabel extends Label {
        public MenuLabel(MenuItem item, MenuItemContainer mic) {
            super(item.getText());
            setMnemonicParsing(item.isMnemonicParsing());
            setLabelFor(mic);
        }
    }
}
