package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.ToolBarBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ToolBarSkin.class */
public class ToolBarSkin extends BehaviorSkinBase<ToolBar, ToolBarBehavior> {
    private Pane box;
    private ToolBarOverflowMenu overflowMenu;
    private boolean overflow;
    private double previousWidth;
    private double previousHeight;
    private double savedPrefWidth;
    private double savedPrefHeight;
    private ObservableList<MenuItem> overflowMenuItems;
    private boolean needsUpdate;
    private final ParentTraversalEngine engine;
    private DoubleProperty spacing;
    private ObjectProperty<Pos> boxAlignment;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v2, types: [javafx.scene.Parent, javafx.scene.control.Control] */
    public ToolBarSkin(ToolBar toolbar) {
        super(toolbar, new ToolBarBehavior(toolbar));
        this.overflow = false;
        this.previousWidth = 0.0d;
        this.previousHeight = 0.0d;
        this.savedPrefWidth = 0.0d;
        this.savedPrefHeight = 0.0d;
        this.needsUpdate = false;
        this.overflowMenuItems = FXCollections.observableArrayList();
        initialize();
        registerChangeListener(toolbar.orientationProperty(), "ORIENTATION");
        this.engine = new ParentTraversalEngine(getSkinnable(), new Algorithm() { // from class: com.sun.javafx.scene.control.skin.ToolBarSkin.1
            private Node selectPrev(int from, TraversalContext context) {
                Node selected;
                for (int i2 = from; i2 >= 0; i2--) {
                    Node n2 = ToolBarSkin.this.box.getChildren().get(i2);
                    if (!n2.isDisabled() && n2.impl_isTreeVisible()) {
                        if ((n2 instanceof Parent) && (selected = context.selectLastInParent((Parent) n2)) != null) {
                            return selected;
                        }
                        if (n2.isFocusTraversable()) {
                            return n2;
                        }
                    }
                }
                return null;
            }

            private Node selectNext(int from, TraversalContext context) {
                Node selected;
                int max = ToolBarSkin.this.box.getChildren().size();
                for (int i2 = from; i2 < max; i2++) {
                    Node n2 = ToolBarSkin.this.box.getChildren().get(i2);
                    if (!n2.isDisabled() && n2.impl_isTreeVisible()) {
                        if (n2.isFocusTraversable()) {
                            return n2;
                        }
                        if ((n2 instanceof Parent) && (selected = context.selectFirstInParent((Parent) n2)) != null) {
                            return selected;
                        }
                    }
                }
                return null;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node select(Node owner, Direction dir, TraversalContext context) {
                Parent item;
                ObservableList<Node> boxChildren = ToolBarSkin.this.box.getChildren();
                if (owner == ToolBarSkin.this.overflowMenu) {
                    if (dir.isForward()) {
                        return null;
                    }
                    Node selected = selectPrev(boxChildren.size() - 1, context);
                    if (selected != null) {
                        return selected;
                    }
                }
                int idx = boxChildren.indexOf(owner);
                if (idx < 0) {
                    Parent parent = owner.getParent();
                    while (true) {
                        item = parent;
                        if (boxChildren.contains(item)) {
                            break;
                        }
                        parent = item.getParent();
                    }
                    Node selected2 = context.selectInSubtree(item, owner, dir);
                    if (selected2 != null) {
                        return selected2;
                    }
                    idx = boxChildren.indexOf(owner);
                    if (dir == Direction.NEXT) {
                        dir = Direction.NEXT_IN_LINE;
                    }
                }
                if (idx >= 0) {
                    if (dir.isForward()) {
                        Node selected3 = selectNext(idx + 1, context);
                        if (selected3 != null) {
                            return selected3;
                        }
                        if (ToolBarSkin.this.overflow) {
                            ToolBarSkin.this.overflowMenu.requestFocus();
                            return ToolBarSkin.this.overflowMenu;
                        }
                        return null;
                    }
                    Node selected4 = selectPrev(idx - 1, context);
                    if (selected4 != null) {
                        return selected4;
                    }
                    return null;
                }
                return null;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectFirst(TraversalContext context) {
                Node selected = selectNext(0, context);
                if (selected != null) {
                    return selected;
                }
                if (ToolBarSkin.this.overflow) {
                    return ToolBarSkin.this.overflowMenu;
                }
                return null;
            }

            @Override // com.sun.javafx.scene.traversal.Algorithm
            public Node selectLast(TraversalContext context) {
                if (ToolBarSkin.this.overflow) {
                    return ToolBarSkin.this.overflowMenu;
                }
                return selectPrev(ToolBarSkin.this.box.getChildren().size() - 1, context);
            }
        });
        ((ToolBar) getSkinnable()).setImpl_traversalEngine(this.engine);
        toolbar.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.booleanValue()) {
                if (!this.box.getChildren().isEmpty()) {
                    this.box.getChildren().get(0).requestFocus();
                } else {
                    this.overflowMenu.requestFocus();
                }
            }
        });
        toolbar.getItems().addListener(c2 -> {
            while (c2.next()) {
                for (Node n2 : c2.getRemoved()) {
                    this.box.getChildren().remove(n2);
                }
                this.box.getChildren().addAll(c2.getAddedSubList());
            }
            this.needsUpdate = true;
            ((ToolBar) getSkinnable()).requestLayout();
        });
    }

    public final void setSpacing(double value) {
        spacingProperty().set(snapSpace(value));
    }

    public final double getSpacing() {
        if (this.spacing == null) {
            return 0.0d;
        }
        return snapSpace(this.spacing.get());
    }

    public final DoubleProperty spacingProperty() {
        if (this.spacing == null) {
            this.spacing = new StyleableDoubleProperty() { // from class: com.sun.javafx.scene.control.skin.ToolBarSkin.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    double value = get();
                    if (((ToolBar) ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                        ((VBox) ToolBarSkin.this.box).setSpacing(value);
                    } else {
                        ((HBox) ToolBarSkin.this.box).setSpacing(value);
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ToolBarSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "spacing";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.SPACING;
                }
            };
        }
        return this.spacing;
    }

    public final void setBoxAlignment(Pos value) {
        boxAlignmentProperty().set(value);
    }

    public final Pos getBoxAlignment() {
        return this.boxAlignment == null ? Pos.TOP_LEFT : this.boxAlignment.get();
    }

    public final ObjectProperty<Pos> boxAlignmentProperty() {
        if (this.boxAlignment == null) {
            this.boxAlignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT) { // from class: com.sun.javafx.scene.control.skin.ToolBarSkin.3
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Pos value = get();
                    if (((ToolBar) ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                        ((VBox) ToolBarSkin.this.box).setAlignment(value);
                    } else {
                        ((HBox) ToolBarSkin.this.box).setAlignment(value);
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ToolBarSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "boxAlignment";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<ToolBar, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }
            };
        }
        return this.boxAlignment;
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String property) {
        super.handleControlPropertyChanged(property);
        if ("ORIENTATION".equals(property)) {
            initialize();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        ToolBar toolbar = (ToolBar) getSkinnable();
        if (toolbar.getOrientation() == Orientation.VERTICAL) {
            return computePrefWidth(-1.0d, topInset, rightInset, bottomInset, leftInset);
        }
        return snapSize(this.overflowMenu.prefWidth(-1.0d)) + leftInset + rightInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        ToolBar toolbar = (ToolBar) getSkinnable();
        if (toolbar.getOrientation() == Orientation.VERTICAL) {
            return snapSize(this.overflowMenu.prefHeight(-1.0d)) + topInset + bottomInset;
        }
        return computePrefHeight(-1.0d, topInset, rightInset, bottomInset, leftInset);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        double prefWidth = 0.0d;
        ToolBar toolbar = (ToolBar) getSkinnable();
        if (toolbar.getOrientation() == Orientation.HORIZONTAL) {
            for (Node node : toolbar.getItems()) {
                prefWidth += snapSize(node.prefWidth(-1.0d)) + getSpacing();
            }
            prefWidth -= getSpacing();
        } else {
            for (Node node2 : toolbar.getItems()) {
                prefWidth = Math.max(prefWidth, snapSize(node2.prefWidth(-1.0d)));
            }
            if (toolbar.getItems().size() > 0) {
                this.savedPrefWidth = prefWidth;
            } else {
                prefWidth = this.savedPrefWidth;
            }
        }
        return leftInset + prefWidth + rightInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        double prefHeight = 0.0d;
        ToolBar toolbar = (ToolBar) getSkinnable();
        if (toolbar.getOrientation() == Orientation.VERTICAL) {
            for (Node node : toolbar.getItems()) {
                prefHeight += snapSize(node.prefHeight(-1.0d)) + getSpacing();
            }
            prefHeight -= getSpacing();
        } else {
            for (Node node2 : toolbar.getItems()) {
                prefHeight = Math.max(prefHeight, snapSize(node2.prefHeight(-1.0d)));
            }
            if (toolbar.getItems().size() > 0) {
                this.savedPrefHeight = prefHeight;
            } else {
                prefHeight = this.savedPrefHeight;
            }
        }
        return topInset + prefHeight + bottomInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (((ToolBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            return snapSize(((ToolBar) getSkinnable()).prefWidth(-1.0d));
        }
        return Double.MAX_VALUE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (((ToolBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            return Double.MAX_VALUE;
        }
        return snapSize(((ToolBar) getSkinnable()).prefHeight(-1.0d));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        double overflowY;
        double overflowX;
        ToolBar toolbar = (ToolBar) getSkinnable();
        if (toolbar.getOrientation() == Orientation.VERTICAL) {
            if (snapSize(toolbar.getHeight()) != this.previousHeight || this.needsUpdate) {
                ((VBox) this.box).setSpacing(getSpacing());
                ((VBox) this.box).setAlignment(getBoxAlignment());
                this.previousHeight = snapSize(toolbar.getHeight());
                addNodesToToolBar();
            }
        } else if (snapSize(toolbar.getWidth()) != this.previousWidth || this.needsUpdate) {
            ((HBox) this.box).setSpacing(getSpacing());
            ((HBox) this.box).setAlignment(getBoxAlignment());
            this.previousWidth = snapSize(toolbar.getWidth());
            addNodesToToolBar();
        }
        this.needsUpdate = false;
        double toolbarWidth = w2;
        double toolbarHeight = h2;
        if (((ToolBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            toolbarHeight -= this.overflow ? snapSize(this.overflowMenu.prefHeight(-1.0d)) : 0.0d;
        } else {
            toolbarWidth -= this.overflow ? snapSize(this.overflowMenu.prefWidth(-1.0d)) : 0.0d;
        }
        this.box.resize(toolbarWidth, toolbarHeight);
        positionInArea(this.box, x2, y2, toolbarWidth, toolbarHeight, 0.0d, HPos.CENTER, VPos.CENTER);
        if (this.overflow) {
            double overflowMenuWidth = snapSize(this.overflowMenu.prefWidth(-1.0d));
            double overflowMenuHeight = snapSize(this.overflowMenu.prefHeight(-1.0d));
            if (((ToolBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                if (toolbarWidth == 0.0d) {
                    toolbarWidth = this.savedPrefWidth;
                }
                HPos pos = ((VBox) this.box).getAlignment().getHpos();
                if (HPos.LEFT.equals(pos)) {
                    overflowX = x2 + Math.abs((toolbarWidth - overflowMenuWidth) / 2.0d);
                } else if (HPos.RIGHT.equals(pos)) {
                    overflowX = ((snapSize(toolbar.getWidth()) - snappedRightInset()) - toolbarWidth) + Math.abs((toolbarWidth - overflowMenuWidth) / 2.0d);
                } else {
                    overflowX = x2 + Math.abs((((snapSize(toolbar.getWidth()) - x2) + snappedRightInset()) - overflowMenuWidth) / 2.0d);
                }
                overflowY = (snapSize(toolbar.getHeight()) - overflowMenuHeight) - y2;
            } else {
                if (toolbarHeight == 0.0d) {
                    toolbarHeight = this.savedPrefHeight;
                }
                VPos pos2 = ((HBox) this.box).getAlignment().getVpos();
                if (!VPos.TOP.equals(pos2) && VPos.BOTTOM.equals(pos2)) {
                    overflowY = ((snapSize(toolbar.getHeight()) - snappedBottomInset()) - toolbarHeight) + Math.abs((toolbarHeight - overflowMenuHeight) / 2.0d);
                } else {
                    overflowY = y2 + Math.abs((toolbarHeight - overflowMenuHeight) / 2.0d);
                }
                overflowX = (snapSize(toolbar.getWidth()) - overflowMenuWidth) - snappedRightInset();
            }
            this.overflowMenu.resize(overflowMenuWidth, overflowMenuHeight);
            positionInArea(this.overflowMenu, overflowX, overflowY, overflowMenuWidth, overflowMenuHeight, 0.0d, HPos.CENTER, VPos.CENTER);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initialize() {
        if (((ToolBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            this.box = new VBox();
        } else {
            this.box = new HBox();
        }
        this.box.getStyleClass().add("container");
        this.box.getChildren().addAll(((ToolBar) getSkinnable()).getItems());
        this.overflowMenu = new ToolBarOverflowMenu(this.overflowMenuItems);
        this.overflowMenu.setVisible(false);
        this.overflowMenu.setManaged(false);
        getChildren().clear();
        getChildren().add(this.box);
        getChildren().add(this.overflowMenu);
        this.previousWidth = 0.0d;
        this.previousHeight = 0.0d;
        this.savedPrefWidth = 0.0d;
        this.savedPrefHeight = 0.0d;
        this.needsUpdate = true;
        ((ToolBar) getSkinnable()).requestLayout();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void addNodesToToolBar() {
        double length;
        Node last;
        CustomMenuItem customMenuItem;
        double length2;
        ToolBar toolbar = (ToolBar) getSkinnable();
        if (((ToolBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
            length = ((snapSize(toolbar.getHeight()) - snappedTopInset()) - snappedBottomInset()) + getSpacing();
        } else {
            length = ((snapSize(toolbar.getWidth()) - snappedLeftInset()) - snappedRightInset()) + getSpacing();
        }
        double x2 = 0.0d;
        boolean hasOverflow = false;
        Iterator<Node> it = ((ToolBar) getSkinnable()).getItems().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Node node = it.next();
            if (((ToolBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                x2 += snapSize(node.prefHeight(-1.0d)) + getSpacing();
            } else {
                x2 += snapSize(node.prefWidth(-1.0d)) + getSpacing();
            }
            if (x2 > length) {
                hasOverflow = true;
                break;
            }
        }
        if (hasOverflow) {
            if (((ToolBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                length2 = length - snapSize(this.overflowMenu.prefHeight(-1.0d));
            } else {
                length2 = length - snapSize(this.overflowMenu.prefWidth(-1.0d));
            }
            length = length2 - getSpacing();
        }
        double x3 = 0.0d;
        this.overflowMenuItems.clear();
        this.box.getChildren().clear();
        for (Node node2 : ((ToolBar) getSkinnable()).getItems()) {
            node2.getStyleClass().remove("menu-item");
            node2.getStyleClass().remove("custom-menu-item");
            if (((ToolBar) getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                x3 += snapSize(node2.prefHeight(-1.0d)) + getSpacing();
            } else {
                x3 += snapSize(node2.prefWidth(-1.0d)) + getSpacing();
            }
            if (x3 <= length) {
                this.box.getChildren().add(node2);
            } else {
                if (node2.isFocused()) {
                    if (!this.box.getChildren().isEmpty()) {
                        Node last2 = this.engine.selectLast();
                        if (last2 != null) {
                            last2.requestFocus();
                        }
                    } else {
                        this.overflowMenu.requestFocus();
                    }
                }
                if (node2 instanceof Separator) {
                    this.overflowMenuItems.add(new SeparatorMenuItem());
                } else {
                    customMenuItem = new CustomMenuItem(node2);
                    String nodeType = node2.getTypeSelector();
                    switch (nodeType) {
                        case "Button":
                        case "Hyperlink":
                        case "Label":
                            customMenuItem.setHideOnClick(true);
                            break;
                        case "CheckBox":
                        case "ChoiceBox":
                        case "ColorPicker":
                        case "ComboBox":
                        case "DatePicker":
                        case "MenuButton":
                        case "PasswordField":
                        case "RadioButton":
                        case "ScrollBar":
                        case "ScrollPane":
                        case "Slider":
                        case "SplitMenuButton":
                        case "SplitPane":
                        case "TextArea":
                        case "TextField":
                        case "ToggleButton":
                        case "ToolBar":
                            customMenuItem.setHideOnClick(false);
                            break;
                    }
                    this.overflowMenuItems.add(customMenuItem);
                }
            }
        }
        this.overflow = this.overflowMenuItems.size() > 0;
        if (!this.overflow && this.overflowMenu.isFocused() && (last = this.engine.selectLast()) != null) {
            last.requestFocus();
        }
        this.overflowMenu.setVisible(this.overflow);
        this.overflowMenu.setManaged(this.overflow);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ToolBarSkin$ToolBarOverflowMenu.class */
    class ToolBarOverflowMenu extends StackPane {
        private StackPane downArrow;
        private ContextMenu popup;
        private ObservableList<MenuItem> menuItems;

        public ToolBarOverflowMenu(ObservableList<MenuItem> items) {
            getStyleClass().setAll("tool-bar-overflow-button");
            setAccessibleRole(AccessibleRole.BUTTON);
            setAccessibleText(ControlResources.getString("Accessibility.title.ToolBar.OverflowButton"));
            setFocusTraversable(true);
            this.menuItems = items;
            this.downArrow = new StackPane();
            this.downArrow.getStyleClass().setAll("arrow");
            this.downArrow.setOnMousePressed(me -> {
                fire();
            });
            setOnKeyPressed(ke -> {
                if (KeyCode.SPACE.equals(ke.getCode())) {
                    if (!this.popup.isShowing()) {
                        this.popup.getItems().clear();
                        this.popup.getItems().addAll(this.menuItems);
                        this.popup.show(this.downArrow, Side.BOTTOM, 0.0d, 0.0d);
                    }
                    ke.consume();
                    return;
                }
                if (KeyCode.ESCAPE.equals(ke.getCode())) {
                    if (this.popup.isShowing()) {
                        this.popup.hide();
                    }
                    ke.consume();
                } else if (KeyCode.ENTER.equals(ke.getCode())) {
                    fire();
                    ke.consume();
                }
            });
            visibleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.booleanValue() && ToolBarSkin.this.box.getChildren().isEmpty()) {
                    setFocusTraversable(true);
                }
            });
            this.popup = new ContextMenu();
            setVisible(false);
            setManaged(false);
            getChildren().add(this.downArrow);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void fire() {
            if (this.popup.isShowing()) {
                this.popup.hide();
                return;
            }
            this.popup.getItems().clear();
            this.popup.getItems().addAll(this.menuItems);
            this.popup.show(this.downArrow, Side.BOTTOM, 0.0d, 0.0d);
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            return snappedLeftInset() + snappedRightInset();
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            return snappedTopInset() + snappedBottomInset();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
        protected void layoutChildren() {
            double w2 = snapSize(this.downArrow.prefWidth(-1.0d));
            double h2 = snapSize(this.downArrow.prefHeight(-1.0d));
            double x2 = (snapSize(getWidth()) - w2) / 2.0d;
            double y2 = (snapSize(getHeight()) - h2) / 2.0d;
            if (((ToolBar) ToolBarSkin.this.getSkinnable()).getOrientation() == Orientation.VERTICAL) {
                this.downArrow.setRotate(0.0d);
            }
            this.downArrow.resize(w2, h2);
            positionInArea(this.downArrow, x2, y2, w2, h2, 0.0d, HPos.CENTER, VPos.CENTER);
        }

        @Override // javafx.scene.Node
        public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
            switch (action) {
                case FIRE:
                    fire();
                    break;
                default:
                    super.executeAccessibleAction(action, new Object[0]);
                    break;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/ToolBarSkin$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<ToolBar, Number> SPACING = new CssMetaData<ToolBar, Number>("-fx-spacing", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: com.sun.javafx.scene.control.skin.ToolBarSkin.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ToolBar n2) {
                ToolBarSkin skin = (ToolBarSkin) n2.getSkin();
                return skin.spacing == null || !skin.spacing.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(ToolBar n2) {
                ToolBarSkin skin = (ToolBarSkin) n2.getSkin();
                return (StyleableProperty) skin.spacingProperty();
            }
        };
        private static final CssMetaData<ToolBar, Pos> ALIGNMENT = new CssMetaData<ToolBar, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) { // from class: com.sun.javafx.scene.control.skin.ToolBarSkin.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ToolBar n2) {
                ToolBarSkin skin = (ToolBarSkin) n2.getSkin();
                return skin.boxAlignment == null || !skin.boxAlignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(ToolBar n2) {
                ToolBarSkin skin = (ToolBarSkin) n2.getSkin();
                return (StyleableProperty) skin.boxAlignmentProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(SkinBase.getClassCssMetaData());
            String alignmentProperty = ALIGNMENT.getProperty();
            int nMax = styleables.size();
            for (int n2 = 0; n2 < nMax; n2++) {
                CssMetaData<?, ?> prop = styleables.get(n2);
                if (alignmentProperty.equals(prop.getProperty())) {
                    styleables.remove(prop);
                }
            }
            styleables.add(SPACING);
            styleables.add(ALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.SkinBase
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case OVERFLOW_BUTTON:
                return this.overflowMenu;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case SHOW_MENU:
                this.overflowMenu.fire();
                break;
            default:
                super.executeAccessibleAction(action, parameters);
                break;
        }
    }
}
