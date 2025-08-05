package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.TabPaneBehavior;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.TraversalEngine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TabPaneSkin.class */
public class TabPaneSkin extends BehaviorSkinBase<TabPane, TabPaneBehavior> {
    private ObjectProperty<TabAnimation> openTabAnimation;
    private ObjectProperty<TabAnimation> closeTabAnimation;
    private static final double ANIMATION_SPEED = 150.0d;
    private static final int SPACER = 10;
    private TabHeaderArea tabHeaderArea;
    private ObservableList<TabContentRegion> tabContentRegions;
    private Rectangle clipRect;
    private Rectangle tabHeaderAreaClipRect;
    private Tab selectedTab;
    private boolean isSelectingTab;
    private double maxw;
    private double maxh;
    static int CLOSE_BTN_SIZE = 16;
    private static final PseudoClass SELECTED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JInternalFrame.IS_SELECTED_PROPERTY);
    private static final PseudoClass TOP_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.TOP);
    private static final PseudoClass BOTTOM_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.BOTTOM);
    private static final PseudoClass LEFT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.LEFT);
    private static final PseudoClass RIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.RIGHT);
    private static final PseudoClass DISABLED_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("disabled");

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TabPaneSkin$TabAnimation.class */
    private enum TabAnimation {
        NONE,
        GROW
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TabPaneSkin$TabAnimationState.class */
    private enum TabAnimationState {
        SHOWING,
        HIDING,
        NONE
    }

    private static int getRotation(Side pos) {
        switch (pos) {
            case TOP:
                return 0;
            case BOTTOM:
                return 180;
            case LEFT:
                return -90;
            case RIGHT:
                return 90;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Node clone(Node n2) {
        if (n2 == null) {
            return null;
        }
        if (n2 instanceof ImageView) {
            ImageView iv = (ImageView) n2;
            ImageView imageview = new ImageView();
            imageview.setImage(iv.getImage());
            return imageview;
        }
        if (n2 instanceof Label) {
            Label l2 = (Label) n2;
            Label label = new Label(l2.getText(), l2.getGraphic());
            return label;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public TabPaneSkin(TabPane tabPane) {
        super(tabPane, new TabPaneBehavior(tabPane));
        this.openTabAnimation = new StyleableObjectProperty<TabAnimation>(TabAnimation.GROW) { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.1
            @Override // javafx.css.StyleableProperty
            public CssMetaData<TabPane, TabAnimation> getCssMetaData() {
                return StyleableProperties.OPEN_TAB_ANIMATION;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TabPaneSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "openTabAnimation";
            }
        };
        this.closeTabAnimation = new StyleableObjectProperty<TabAnimation>(TabAnimation.GROW) { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.2
            @Override // javafx.css.StyleableProperty
            public CssMetaData<TabPane, TabAnimation> getCssMetaData() {
                return StyleableProperties.CLOSE_TAB_ANIMATION;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TabPaneSkin.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "closeTabAnimation";
            }
        };
        this.maxw = 0.0d;
        this.maxh = 0.0d;
        this.clipRect = new Rectangle(tabPane.getWidth(), tabPane.getHeight());
        ((TabPane) getSkinnable()).setClip(this.clipRect);
        this.tabContentRegions = FXCollections.observableArrayList();
        for (Tab tab : ((TabPane) getSkinnable()).getTabs()) {
            addTabContent(tab);
        }
        this.tabHeaderAreaClipRect = new Rectangle();
        this.tabHeaderArea = new TabHeaderArea();
        this.tabHeaderArea.setClip(this.tabHeaderAreaClipRect);
        getChildren().add(this.tabHeaderArea);
        if (((TabPane) getSkinnable()).getTabs().size() == 0) {
            this.tabHeaderArea.setVisible(false);
        }
        initializeTabListener();
        registerChangeListener(tabPane.getSelectionModel().selectedItemProperty(), "SELECTED_TAB");
        registerChangeListener(tabPane.sideProperty(), "SIDE");
        registerChangeListener(tabPane.widthProperty(), "WIDTH");
        registerChangeListener(tabPane.heightProperty(), "HEIGHT");
        this.selectedTab = ((TabPane) getSkinnable()).getSelectionModel().getSelectedItem();
        if (this.selectedTab == null && ((TabPane) getSkinnable()).getSelectionModel().getSelectedIndex() != -1) {
            ((TabPane) getSkinnable()).getSelectionModel().select(((TabPane) getSkinnable()).getSelectionModel().getSelectedIndex());
            this.selectedTab = ((TabPane) getSkinnable()).getSelectionModel().getSelectedItem();
        }
        if (this.selectedTab == null) {
            ((TabPane) getSkinnable()).getSelectionModel().selectFirst();
        }
        this.selectedTab = ((TabPane) getSkinnable()).getSelectionModel().getSelectedItem();
        this.isSelectingTab = false;
        initializeSwipeHandlers();
    }

    public StackPane getSelectedTabContentRegion() {
        for (TabContentRegion contentRegion : this.tabContentRegions) {
            if (contentRegion.getTab().equals(this.selectedTab)) {
                return contentRegion;
            }
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase
    protected void handleControlPropertyChanged(String property) {
        super.handleControlPropertyChanged(property);
        if ("SELECTED_TAB".equals(property)) {
            this.isSelectingTab = true;
            this.selectedTab = ((TabPane) getSkinnable()).getSelectionModel().getSelectedItem();
            ((TabPane) getSkinnable()).requestLayout();
        } else if ("SIDE".equals(property)) {
            updateTabPosition();
        } else if ("WIDTH".equals(property)) {
            this.clipRect.setWidth(((TabPane) getSkinnable()).getWidth());
        } else if ("HEIGHT".equals(property)) {
            this.clipRect.setHeight(((TabPane) getSkinnable()).getHeight());
        }
    }

    private void removeTabs(List<? extends Tab> removedList) {
        for (Tab tab : removedList) {
            stopCurrentAnimation(tab);
            TabHeaderSkin tabRegion = this.tabHeaderArea.getTabHeaderSkin(tab);
            if (tabRegion != null) {
                tabRegion.isClosing = true;
                tabRegion.removeListeners(tab);
                removeTabContent(tab);
                ContextMenu popupMenu = this.tabHeaderArea.controlButtons.popup;
                TabMenuItem tabItem = null;
                if (popupMenu != null) {
                    for (MenuItem item : popupMenu.getItems()) {
                        tabItem = (TabMenuItem) item;
                        if (tab == tabItem.getTab()) {
                            break;
                        } else {
                            tabItem = null;
                        }
                    }
                }
                if (tabItem != null) {
                    tabItem.dispose();
                    popupMenu.getItems().remove(tabItem);
                }
                EventHandler<ActionEvent> cleanup = ae2 -> {
                    tabRegion.animationState = TabAnimationState.NONE;
                    this.tabHeaderArea.removeTab(tab);
                    this.tabHeaderArea.requestLayout();
                    if (((TabPane) getSkinnable()).getTabs().isEmpty()) {
                        this.tabHeaderArea.setVisible(false);
                    }
                };
                if (this.closeTabAnimation.get() != TabAnimation.GROW) {
                    cleanup.handle(null);
                } else {
                    tabRegion.animationState = TabAnimationState.HIDING;
                    Timeline closedTabTimeline = tabRegion.currentAnimation = createTimeline(tabRegion, Duration.millis(ANIMATION_SPEED), 0.0d, cleanup);
                    closedTabTimeline.play();
                }
            }
        }
    }

    private void stopCurrentAnimation(Tab tab) {
        Timeline timeline;
        TabHeaderSkin tabRegion = this.tabHeaderArea.getTabHeaderSkin(tab);
        if (tabRegion != null && (timeline = tabRegion.currentAnimation) != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.getOnFinished().handle(null);
            timeline.stop();
            tabRegion.currentAnimation = null;
        }
    }

    private void addTabs(List<? extends Tab> addedList, int from) {
        int i2 = 0;
        List<Node> headers = new ArrayList<>(this.tabHeaderArea.headersRegion.getChildren());
        for (Node n2 : headers) {
            TabHeaderSkin header = (TabHeaderSkin) n2;
            if (header.animationState == TabAnimationState.HIDING) {
                stopCurrentAnimation(header.tab);
            }
        }
        for (Tab tab : addedList) {
            stopCurrentAnimation(tab);
            if (!this.tabHeaderArea.isVisible()) {
                this.tabHeaderArea.setVisible(true);
            }
            int i3 = i2;
            i2++;
            int index = from + i3;
            this.tabHeaderArea.addTab(tab, index);
            addTabContent(tab);
            TabHeaderSkin tabRegion = this.tabHeaderArea.getTabHeaderSkin(tab);
            if (tabRegion != null) {
                if (this.openTabAnimation.get() != TabAnimation.GROW) {
                    tabRegion.setVisible(true);
                    tabRegion.inner.requestLayout();
                } else {
                    tabRegion.animationState = TabAnimationState.SHOWING;
                    tabRegion.animationTransition.setValue((Number) Double.valueOf(0.0d));
                    tabRegion.setVisible(true);
                    tabRegion.currentAnimation = createTimeline(tabRegion, Duration.millis(ANIMATION_SPEED), 1.0d, event -> {
                        tabRegion.animationState = TabAnimationState.NONE;
                        tabRegion.setVisible(true);
                        tabRegion.inner.requestLayout();
                    });
                    tabRegion.currentAnimation.play();
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initializeTabListener() {
        ((TabPane) getSkinnable()).getTabs().addListener(c2 -> {
            List<Tab> tabsToRemove = new ArrayList<>();
            List<Tab> tabsToAdd = new ArrayList<>();
            int insertPos = -1;
            while (c2.next()) {
                if (c2.wasPermutated()) {
                    TabPane tabPane = (TabPane) getSkinnable();
                    List<Tab> tabs = tabPane.getTabs();
                    int size = c2.getTo() - c2.getFrom();
                    Tab selTab = tabPane.getSelectionModel().getSelectedItem();
                    List<Tab> permutatedTabs = new ArrayList<>(size);
                    ((TabPane) getSkinnable()).getSelectionModel().clearSelection();
                    TabAnimation prevOpenAnimation = this.openTabAnimation.get();
                    TabAnimation prevCloseAnimation = this.closeTabAnimation.get();
                    this.openTabAnimation.set(TabAnimation.NONE);
                    this.closeTabAnimation.set(TabAnimation.NONE);
                    for (int i2 = c2.getFrom(); i2 < c2.getTo(); i2++) {
                        permutatedTabs.add(tabs.get(i2));
                    }
                    removeTabs(permutatedTabs);
                    addTabs(permutatedTabs, c2.getFrom());
                    this.openTabAnimation.set(prevOpenAnimation);
                    this.closeTabAnimation.set(prevCloseAnimation);
                    ((TabPane) getSkinnable()).getSelectionModel().select((SingleSelectionModel<Tab>) selTab);
                }
                if (c2.wasRemoved()) {
                    tabsToRemove.addAll(c2.getRemoved());
                }
                if (c2.wasAdded()) {
                    tabsToAdd.addAll(c2.getAddedSubList());
                    insertPos = c2.getFrom();
                }
            }
            tabsToRemove.removeAll(tabsToAdd);
            removeTabs(tabsToRemove);
            if (!tabsToAdd.isEmpty()) {
                for (TabContentRegion tabContentRegion : this.tabContentRegions) {
                    Tab tab = tabContentRegion.getTab();
                    TabHeaderSkin tabHeader = this.tabHeaderArea.getTabHeaderSkin(tab);
                    if (!tabHeader.isClosing && tabsToAdd.contains(tabContentRegion.getTab())) {
                        tabsToAdd.remove(tabContentRegion.getTab());
                    }
                }
                addTabs(tabsToAdd, insertPos == -1 ? this.tabContentRegions.size() : insertPos);
            }
            ((TabPane) getSkinnable()).requestLayout();
        });
    }

    private void addTabContent(Tab tab) {
        TabContentRegion tabContentRegion = new TabContentRegion(tab);
        tabContentRegion.setClip(new Rectangle());
        this.tabContentRegions.add(tabContentRegion);
        getChildren().add(0, tabContentRegion);
    }

    private void removeTabContent(Tab tab) {
        for (TabContentRegion contentRegion : this.tabContentRegions) {
            if (contentRegion.getTab().equals(tab)) {
                contentRegion.removeListeners(tab);
                getChildren().remove(contentRegion);
                this.tabContentRegions.remove(contentRegion);
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void updateTabPosition() {
        this.tabHeaderArea.setScrollOffset(0.0d);
        ((TabPane) getSkinnable()).applyCss();
        ((TabPane) getSkinnable()).requestLayout();
    }

    private Timeline createTimeline(TabHeaderSkin tabRegion, Duration duration, double endValue, EventHandler<ActionEvent> func) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        KeyValue keyValue = new KeyValue(tabRegion.animationTransition, Double.valueOf(endValue), Interpolator.LINEAR);
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().add(new KeyFrame(duration, keyValue));
        timeline.setOnFinished(func);
        return timeline;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean isHorizontal() {
        Side tabPosition = ((TabPane) getSkinnable()).getSide();
        return Side.TOP.equals(tabPosition) || Side.BOTTOM.equals(tabPosition);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void initializeSwipeHandlers() {
        if (IS_TOUCH_SUPPORTED) {
            ((TabPane) getSkinnable()).addEventHandler(SwipeEvent.SWIPE_LEFT, t2 -> {
                getBehavior().selectNextTab();
            });
            ((TabPane) getSkinnable()).addEventHandler(SwipeEvent.SWIPE_RIGHT, t3 -> {
                getBehavior().selectPreviousTab();
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean isFloatingStyleClass() {
        return ((TabPane) getSkinnable()).getStyleClass().contains(TabPane.STYLE_CLASS_FLOATING);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        for (TabContentRegion contentRegion : this.tabContentRegions) {
            this.maxw = Math.max(this.maxw, snapSize(contentRegion.prefWidth(-1.0d)));
        }
        boolean isHorizontal = isHorizontal();
        double tabHeaderAreaSize = snapSize(isHorizontal ? this.tabHeaderArea.prefWidth(-1.0d) : this.tabHeaderArea.prefHeight(-1.0d));
        double prefWidth = isHorizontal ? Math.max(this.maxw, tabHeaderAreaSize) : this.maxw + tabHeaderAreaSize;
        return snapSize(prefWidth) + rightInset + leftInset;
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        for (TabContentRegion contentRegion : this.tabContentRegions) {
            this.maxh = Math.max(this.maxh, snapSize(contentRegion.prefHeight(-1.0d)));
        }
        boolean isHorizontal = isHorizontal();
        double tabHeaderAreaSize = snapSize(isHorizontal ? this.tabHeaderArea.prefHeight(-1.0d) : this.tabHeaderArea.prefWidth(-1.0d));
        double prefHeight = isHorizontal ? this.maxh + snapSize(tabHeaderAreaSize) : Math.max(this.maxh, tabHeaderAreaSize);
        return snapSize(prefHeight) + topInset + bottomInset;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    public double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        Side tabPosition = ((TabPane) getSkinnable()).getSide();
        if (tabPosition == Side.TOP) {
            return this.tabHeaderArea.getBaselineOffset() + topInset;
        }
        return 0.0d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        TabPane tabPane = (TabPane) getSkinnable();
        Side tabPosition = tabPane.getSide();
        double headerHeight = snapSize(this.tabHeaderArea.prefHeight(-1.0d));
        double tabsStartX = tabPosition.equals(Side.RIGHT) ? (x2 + w2) - headerHeight : x2;
        double tabsStartY = tabPosition.equals(Side.BOTTOM) ? (y2 + h2) - headerHeight : y2;
        if (tabPosition == Side.TOP) {
            this.tabHeaderArea.resize(w2, headerHeight);
            this.tabHeaderArea.relocate(tabsStartX, tabsStartY);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add(new Rotate(getRotation(Side.TOP)));
        } else if (tabPosition == Side.BOTTOM) {
            this.tabHeaderArea.resize(w2, headerHeight);
            this.tabHeaderArea.relocate(w2, tabsStartY - headerHeight);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add(new Rotate(getRotation(Side.BOTTOM), 0.0d, headerHeight));
        } else if (tabPosition == Side.LEFT) {
            this.tabHeaderArea.resize(h2, headerHeight);
            this.tabHeaderArea.relocate(tabsStartX + headerHeight, h2 - headerHeight);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add(new Rotate(getRotation(Side.LEFT), 0.0d, headerHeight));
        } else if (tabPosition == Side.RIGHT) {
            this.tabHeaderArea.resize(h2, headerHeight);
            this.tabHeaderArea.relocate(tabsStartX, y2 - headerHeight);
            this.tabHeaderArea.getTransforms().clear();
            this.tabHeaderArea.getTransforms().add(new Rotate(getRotation(Side.RIGHT), 0.0d, headerHeight));
        }
        this.tabHeaderAreaClipRect.setX(0.0d);
        this.tabHeaderAreaClipRect.setY(0.0d);
        if (isHorizontal()) {
            this.tabHeaderAreaClipRect.setWidth(w2);
        } else {
            this.tabHeaderAreaClipRect.setWidth(h2);
        }
        this.tabHeaderAreaClipRect.setHeight(headerHeight);
        double contentStartX = 0.0d;
        double contentStartY = 0.0d;
        if (tabPosition == Side.TOP) {
            contentStartX = x2;
            contentStartY = y2 + headerHeight;
            if (isFloatingStyleClass()) {
                contentStartY -= 1.0d;
            }
        } else if (tabPosition == Side.BOTTOM) {
            contentStartX = x2;
            contentStartY = y2;
            if (isFloatingStyleClass()) {
                contentStartY = 1.0d;
            }
        } else if (tabPosition == Side.LEFT) {
            contentStartX = x2 + headerHeight;
            contentStartY = y2;
            if (isFloatingStyleClass()) {
                contentStartX -= 1.0d;
            }
        } else if (tabPosition == Side.RIGHT) {
            contentStartX = x2;
            contentStartY = y2;
            if (isFloatingStyleClass()) {
                contentStartX = 1.0d;
            }
        }
        double contentWidth = w2 - (isHorizontal() ? 0.0d : headerHeight);
        double contentHeight = h2 - (isHorizontal() ? headerHeight : 0.0d);
        int max = this.tabContentRegions.size();
        for (int i2 = 0; i2 < max; i2++) {
            TabContentRegion tabContent = this.tabContentRegions.get(i2);
            tabContent.setAlignment(Pos.TOP_LEFT);
            if (tabContent.getClip() != null) {
                ((Rectangle) tabContent.getClip()).setWidth(contentWidth);
                ((Rectangle) tabContent.getClip()).setHeight(contentHeight);
            }
            tabContent.resize(contentWidth, contentHeight);
            tabContent.relocate(contentStartX, contentStartY);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TabPaneSkin$StyleableProperties.class */
    private static class StyleableProperties {
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        private static final CssMetaData<TabPane, TabAnimation> OPEN_TAB_ANIMATION = new CssMetaData<TabPane, TabAnimation>("-fx-open-tab-animation", new EnumConverter(TabAnimation.class), TabAnimation.GROW) { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TabPane node) {
                return true;
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<TabAnimation> getStyleableProperty(TabPane node) {
                TabPaneSkin skin = (TabPaneSkin) node.getSkin();
                return (StyleableProperty) skin.openTabAnimation;
            }
        };
        private static final CssMetaData<TabPane, TabAnimation> CLOSE_TAB_ANIMATION = new CssMetaData<TabPane, TabAnimation>("-fx-close-tab-animation", new EnumConverter(TabAnimation.class), TabAnimation.GROW) { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TabPane node) {
                return true;
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<TabAnimation> getStyleableProperty(TabPane node) {
                TabPaneSkin skin = (TabPaneSkin) node.getSkin();
                return (StyleableProperty) skin.closeTabAnimation;
            }
        };

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(SkinBase.getClassCssMetaData());
            styleables.add(OPEN_TAB_ANIMATION);
            styleables.add(CLOSE_TAB_ANIMATION);
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

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TabPaneSkin$TabHeaderArea.class */
    class TabHeaderArea extends StackPane {
        private Rectangle headerClip;
        private StackPane headersRegion;
        private StackPane headerBackground;
        private TabControlButtons controlButtons;
        private double scrollOffset;
        private boolean measureClosingTabs = false;
        private List<TabHeaderSkin> removeTab = new ArrayList();

        /* JADX WARN: Multi-variable type inference failed */
        public TabHeaderArea() {
            getStyleClass().setAll("tab-header-area");
            setManaged(false);
            TabPane tabPane = (TabPane) TabPaneSkin.this.getSkinnable();
            this.headerClip = new Rectangle();
            this.headersRegion = new StackPane() { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabHeaderArea.1
                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computePrefWidth(double height) {
                    double width = 0.0d;
                    for (Node child : getChildren()) {
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin) child;
                        if (tabHeaderSkin.isVisible() && (TabHeaderArea.this.measureClosingTabs || !tabHeaderSkin.isClosing)) {
                            width += tabHeaderSkin.prefWidth(height);
                        }
                    }
                    return snapSize(width) + snappedLeftInset() + snappedRightInset();
                }

                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computePrefHeight(double width) {
                    double height = 0.0d;
                    for (Node child : getChildren()) {
                        TabHeaderSkin tabHeaderSkin = (TabHeaderSkin) child;
                        height = Math.max(height, tabHeaderSkin.prefHeight(width));
                    }
                    return snapSize(height) + snappedTopInset() + snappedBottomInset();
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
                protected void layoutChildren() {
                    if (TabHeaderArea.this.tabsFit()) {
                        TabHeaderArea.this.setScrollOffset(0.0d);
                    } else if (!TabHeaderArea.this.removeTab.isEmpty()) {
                        double offset = 0.0d;
                        double w2 = ((TabPaneSkin.this.tabHeaderArea.getWidth() - snapSize(TabHeaderArea.this.controlButtons.prefWidth(-1.0d))) - TabHeaderArea.this.firstTabIndent()) - 10.0d;
                        Iterator<Node> i2 = getChildren().iterator();
                        while (i2.hasNext()) {
                            TabHeaderSkin tabHeader = (TabHeaderSkin) i2.next();
                            double tabHeaderPrefWidth = snapSize(tabHeader.prefWidth(-1.0d));
                            if (TabHeaderArea.this.removeTab.contains(tabHeader)) {
                                if (offset < w2) {
                                    TabPaneSkin.this.isSelectingTab = true;
                                }
                                i2.remove();
                                TabHeaderArea.this.removeTab.remove(tabHeader);
                                if (TabHeaderArea.this.removeTab.isEmpty()) {
                                    break;
                                }
                            }
                            offset += tabHeaderPrefWidth;
                        }
                    }
                    if (TabPaneSkin.this.isSelectingTab) {
                        TabHeaderArea.this.ensureSelectedTabIsVisible();
                        TabPaneSkin.this.isSelectingTab = false;
                    } else {
                        TabHeaderArea.this.validateScrollOffset();
                    }
                    Side tabPosition = ((TabPane) TabPaneSkin.this.getSkinnable()).getSide();
                    double tabBackgroundHeight = snapSize(prefHeight(-1.0d));
                    double tabX = (tabPosition.equals(Side.LEFT) || tabPosition.equals(Side.BOTTOM)) ? snapSize(getWidth()) - TabHeaderArea.this.getScrollOffset() : TabHeaderArea.this.getScrollOffset();
                    TabHeaderArea.this.updateHeaderClip();
                    for (Node node : getChildren()) {
                        TabHeaderSkin tabHeader2 = (TabHeaderSkin) node;
                        double tabHeaderPrefWidth2 = snapSize(tabHeader2.prefWidth(-1.0d) * tabHeader2.animationTransition.get());
                        double tabHeaderPrefHeight = snapSize(tabHeader2.prefHeight(-1.0d));
                        tabHeader2.resize(tabHeaderPrefWidth2, tabHeaderPrefHeight);
                        double startY = tabPosition.equals(Side.BOTTOM) ? 0.0d : (tabBackgroundHeight - tabHeaderPrefHeight) - snappedBottomInset();
                        if (tabPosition.equals(Side.LEFT) || tabPosition.equals(Side.BOTTOM)) {
                            tabX -= tabHeaderPrefWidth2;
                            tabHeader2.relocate(tabX, startY);
                        } else {
                            tabHeader2.relocate(tabX, startY);
                            tabX += tabHeaderPrefWidth2;
                        }
                    }
                }
            };
            this.headersRegion.getStyleClass().setAll("headers-region");
            this.headersRegion.setClip(this.headerClip);
            this.headerBackground = new StackPane();
            this.headerBackground.getStyleClass().setAll("tab-header-background");
            int i2 = 0;
            for (Tab tab : tabPane.getTabs()) {
                int i3 = i2;
                i2++;
                addTab(tab, i3);
            }
            this.controlButtons = TabPaneSkin.this.new TabControlButtons();
            this.controlButtons.setVisible(false);
            if (this.controlButtons.isVisible()) {
                this.controlButtons.setVisible(true);
            }
            getChildren().addAll(this.headerBackground, this.headersRegion, this.controlButtons);
            addEventHandler(ScrollEvent.SCROLL, e2 -> {
                Side side = ((TabPane) TabPaneSkin.this.getSkinnable()).getSide();
                switch (side == null ? Side.TOP : side) {
                    case TOP:
                    case BOTTOM:
                    default:
                        setScrollOffset(this.scrollOffset - e2.getDeltaY());
                        break;
                    case LEFT:
                    case RIGHT:
                        setScrollOffset(this.scrollOffset + e2.getDeltaY());
                        break;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public void updateHeaderClip() {
            double clipWidth;
            double clipHeight;
            Side tabPosition = ((TabPane) TabPaneSkin.this.getSkinnable()).getSide();
            double x2 = 0.0d;
            double shadowRadius = 0.0d;
            double clipOffset = firstTabIndent();
            double controlButtonPrefWidth = snapSize(this.controlButtons.prefWidth(-1.0d));
            this.measureClosingTabs = true;
            double headersPrefWidth = snapSize(this.headersRegion.prefWidth(-1.0d));
            this.measureClosingTabs = false;
            double headersPrefHeight = snapSize(this.headersRegion.prefHeight(-1.0d));
            if (controlButtonPrefWidth > 0.0d) {
                controlButtonPrefWidth += 10.0d;
            }
            if (this.headersRegion.getEffect() instanceof DropShadow) {
                DropShadow shadow = (DropShadow) this.headersRegion.getEffect();
                shadowRadius = shadow.getRadius();
            }
            double maxWidth = (snapSize(getWidth()) - controlButtonPrefWidth) - clipOffset;
            if (tabPosition.equals(Side.LEFT) || tabPosition.equals(Side.BOTTOM)) {
                if (headersPrefWidth < maxWidth) {
                    clipWidth = headersPrefWidth + shadowRadius;
                } else {
                    x2 = headersPrefWidth - maxWidth;
                    clipWidth = maxWidth + shadowRadius;
                }
                clipHeight = headersPrefHeight;
            } else {
                x2 = -shadowRadius;
                clipWidth = (headersPrefWidth < maxWidth ? headersPrefWidth : maxWidth) + shadowRadius;
                clipHeight = headersPrefHeight;
            }
            this.headerClip.setX(x2);
            this.headerClip.setY(0.0d);
            this.headerClip.setWidth(clipWidth);
            this.headerClip.setHeight(clipHeight);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addTab(Tab tab, int addToIndex) {
            TabHeaderSkin tabHeaderSkin = TabPaneSkin.this.new TabHeaderSkin(tab);
            this.headersRegion.getChildren().add(addToIndex, tabHeaderSkin);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeTab(Tab tab) {
            TabHeaderSkin tabHeaderSkin = getTabHeaderSkin(tab);
            if (tabHeaderSkin != null) {
                if (tabsFit()) {
                    this.headersRegion.getChildren().remove(tabHeaderSkin);
                } else {
                    this.removeTab.add(tabHeaderSkin);
                    tabHeaderSkin.removeListeners(tab);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public TabHeaderSkin getTabHeaderSkin(Tab tab) {
            for (Node child : this.headersRegion.getChildren()) {
                TabHeaderSkin tabHeaderSkin = (TabHeaderSkin) child;
                if (tabHeaderSkin.getTab().equals(tab)) {
                    return tabHeaderSkin;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean tabsFit() {
            double headerPrefWidth = snapSize(this.headersRegion.prefWidth(-1.0d));
            double controlTabWidth = snapSize(this.controlButtons.prefWidth(-1.0d));
            double visibleWidth = headerPrefWidth + controlTabWidth + firstTabIndent() + 10.0d;
            return visibleWidth < getWidth();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public void ensureSelectedTabIsVisible() {
            double tabPaneWidth = snapSize(TabPaneSkin.this.isHorizontal() ? ((TabPane) TabPaneSkin.this.getSkinnable()).getWidth() : ((TabPane) TabPaneSkin.this.getSkinnable()).getHeight());
            double controlTabWidth = snapSize(this.controlButtons.getWidth());
            double visibleWidth = ((tabPaneWidth - controlTabWidth) - firstTabIndent()) - 10.0d;
            double offset = 0.0d;
            double selectedTabOffset = 0.0d;
            double selectedTabWidth = 0.0d;
            for (Node node : this.headersRegion.getChildren()) {
                TabHeaderSkin tabHeader = (TabHeaderSkin) node;
                double tabHeaderPrefWidth = snapSize(tabHeader.prefWidth(-1.0d));
                if (TabPaneSkin.this.selectedTab != null && TabPaneSkin.this.selectedTab.equals(tabHeader.getTab())) {
                    selectedTabOffset = offset;
                    selectedTabWidth = tabHeaderPrefWidth;
                }
                offset += tabHeaderPrefWidth;
            }
            double scrollOffset = getScrollOffset();
            double selectedTabStartX = selectedTabOffset;
            double selectedTabEndX = selectedTabOffset + selectedTabWidth;
            if (selectedTabStartX < (-scrollOffset)) {
                setScrollOffset(-selectedTabStartX);
            } else if (selectedTabEndX > visibleWidth - scrollOffset) {
                setScrollOffset(visibleWidth - selectedTabEndX);
            }
        }

        public double getScrollOffset() {
            return this.scrollOffset;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void validateScrollOffset() {
            setScrollOffset(getScrollOffset());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public void setScrollOffset(double newScrollOffset) {
            double actualNewScrollOffset;
            double tabPaneWidth = snapSize(TabPaneSkin.this.isHorizontal() ? ((TabPane) TabPaneSkin.this.getSkinnable()).getWidth() : ((TabPane) TabPaneSkin.this.getSkinnable()).getHeight());
            double controlTabWidth = snapSize(this.controlButtons.getWidth());
            double visibleWidth = ((tabPaneWidth - controlTabWidth) - firstTabIndent()) - 10.0d;
            double offset = 0.0d;
            for (Node node : this.headersRegion.getChildren()) {
                TabHeaderSkin tabHeader = (TabHeaderSkin) node;
                double tabHeaderPrefWidth = snapSize(tabHeader.prefWidth(-1.0d));
                offset += tabHeaderPrefWidth;
            }
            if (visibleWidth - newScrollOffset > offset && newScrollOffset < 0.0d) {
                actualNewScrollOffset = visibleWidth - offset;
            } else if (newScrollOffset > 0.0d) {
                actualNewScrollOffset = 0.0d;
            } else {
                actualNewScrollOffset = newScrollOffset;
            }
            if (actualNewScrollOffset != this.scrollOffset) {
                this.scrollOffset = actualNewScrollOffset;
                this.headersRegion.requestLayout();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public double firstTabIndent() {
            switch (((TabPane) TabPaneSkin.this.getSkinnable()).getSide()) {
                case TOP:
                case BOTTOM:
                    return snappedLeftInset();
                case LEFT:
                case RIGHT:
                    return snappedTopInset();
                default:
                    return 0.0d;
            }
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            double dSnappedTopInset;
            if (TabPaneSkin.this.isHorizontal()) {
                dSnappedTopInset = snappedLeftInset() + snappedRightInset();
            } else {
                dSnappedTopInset = snappedTopInset() + snappedBottomInset();
            }
            double padding = dSnappedTopInset;
            return snapSize(this.headersRegion.prefWidth(height)) + this.controlButtons.prefWidth(height) + firstTabIndent() + 10.0d + padding;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            double dSnappedLeftInset;
            if (TabPaneSkin.this.isHorizontal()) {
                dSnappedLeftInset = snappedTopInset() + snappedBottomInset();
            } else {
                dSnappedLeftInset = snappedLeftInset() + snappedRightInset();
            }
            double padding = dSnappedLeftInset;
            return snapSize(this.headersRegion.prefHeight(-1.0d)) + padding;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.Parent, javafx.scene.Node
        public double getBaselineOffset() {
            if (((TabPane) TabPaneSkin.this.getSkinnable()).getSide() == Side.TOP) {
                return this.headersRegion.getBaselineOffset() + snappedTopInset();
            }
            return 0.0d;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
        protected void layoutChildren() {
            double leftInset = snappedLeftInset();
            double rightInset = snappedRightInset();
            double topInset = snappedTopInset();
            double bottomInset = snappedBottomInset();
            double w2 = snapSize(getWidth()) - (TabPaneSkin.this.isHorizontal() ? leftInset + rightInset : topInset + bottomInset);
            double h2 = snapSize(getHeight()) - (TabPaneSkin.this.isHorizontal() ? topInset + bottomInset : leftInset + rightInset);
            double tabBackgroundHeight = snapSize(prefHeight(-1.0d));
            double headersPrefWidth = snapSize(this.headersRegion.prefWidth(-1.0d));
            double headersPrefHeight = snapSize(this.headersRegion.prefHeight(-1.0d));
            this.controlButtons.showTabsMenu(!tabsFit());
            updateHeaderClip();
            this.headersRegion.requestLayout();
            double btnWidth = snapSize(this.controlButtons.prefWidth(-1.0d));
            double btnHeight = this.controlButtons.prefHeight(btnWidth);
            this.controlButtons.resize(btnWidth, btnHeight);
            this.headersRegion.resize(headersPrefWidth, headersPrefHeight);
            if (TabPaneSkin.this.isFloatingStyleClass()) {
                this.headerBackground.setVisible(false);
            } else {
                this.headerBackground.resize(snapSize(getWidth()), snapSize(getHeight()));
                this.headerBackground.setVisible(true);
            }
            double startX = 0.0d;
            double startY = 0.0d;
            double controlStartX = 0.0d;
            double controlStartY = 0.0d;
            Side tabPosition = ((TabPane) TabPaneSkin.this.getSkinnable()).getSide();
            if (tabPosition.equals(Side.TOP)) {
                startX = leftInset;
                startY = (tabBackgroundHeight - headersPrefHeight) - bottomInset;
                controlStartX = (w2 - btnWidth) + leftInset;
                controlStartY = (snapSize(getHeight()) - btnHeight) - bottomInset;
            } else if (tabPosition.equals(Side.RIGHT)) {
                startX = topInset;
                startY = (tabBackgroundHeight - headersPrefHeight) - leftInset;
                controlStartX = (w2 - btnWidth) + topInset;
                controlStartY = (snapSize(getHeight()) - btnHeight) - leftInset;
            } else if (tabPosition.equals(Side.BOTTOM)) {
                startX = (snapSize(getWidth()) - headersPrefWidth) - leftInset;
                startY = (tabBackgroundHeight - headersPrefHeight) - topInset;
                controlStartX = rightInset;
                controlStartY = (snapSize(getHeight()) - btnHeight) - topInset;
            } else if (tabPosition.equals(Side.LEFT)) {
                startX = (snapSize(getWidth()) - headersPrefWidth) - topInset;
                startY = (tabBackgroundHeight - headersPrefHeight) - rightInset;
                controlStartX = leftInset;
                controlStartY = (snapSize(getHeight()) - btnHeight) - rightInset;
            }
            if (this.headerBackground.isVisible()) {
                positionInArea(this.headerBackground, 0.0d, 0.0d, snapSize(getWidth()), snapSize(getHeight()), 0.0d, HPos.CENTER, VPos.CENTER);
            }
            positionInArea(this.headersRegion, startX, startY, w2, h2, 0.0d, HPos.LEFT, VPos.CENTER);
            positionInArea(this.controlButtons, controlStartX, controlStartY, btnWidth, btnHeight, 0.0d, HPos.CENTER, VPos.CENTER);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TabPaneSkin$TabHeaderSkin.class */
    class TabHeaderSkin extends StackPane {
        private final Tab tab;
        private Label label;
        private StackPane closeBtn;
        private StackPane inner;
        private Tooltip oldTooltip;
        private Tooltip tooltip;
        private Rectangle clip;
        private boolean isClosing = false;
        private MultiplePropertyChangeListenerHandler listener = new MultiplePropertyChangeListenerHandler(param -> {
            handlePropertyChanged(param);
            return null;
        });
        private final ListChangeListener<String> styleClassListener = new ListChangeListener<String>() { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabHeaderSkin.1
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends String> c2) {
                TabHeaderSkin.this.getStyleClass().setAll(TabHeaderSkin.this.tab.getStyleClass());
            }
        };
        private final WeakListChangeListener<String> weakStyleClassListener = new WeakListChangeListener<>(this.styleClassListener);
        private final DoubleProperty animationTransition = new SimpleDoubleProperty(this, "animationTransition", 1.0d) { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabHeaderSkin.6
            @Override // javafx.beans.property.DoublePropertyBase
            protected void invalidated() {
                TabHeaderSkin.this.requestLayout();
            }
        };
        private TabAnimationState animationState = TabAnimationState.NONE;
        private Timeline currentAnimation;

        public Tab getTab() {
            return this.tab;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public TabHeaderSkin(Tab tab) {
            getStyleClass().setAll(tab.getStyleClass());
            setId(tab.getId());
            setStyle(tab.getStyle());
            setAccessibleRole(AccessibleRole.TAB_ITEM);
            this.tab = tab;
            this.clip = new Rectangle();
            setClip(this.clip);
            this.label = new Label(tab.getText(), tab.getGraphic());
            this.label.getStyleClass().setAll("tab-label");
            this.closeBtn = new StackPane() { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabHeaderSkin.2
                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computePrefWidth(double h2) {
                    return TabPaneSkin.CLOSE_BTN_SIZE;
                }

                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computePrefHeight(double w2) {
                    return TabPaneSkin.CLOSE_BTN_SIZE;
                }

                @Override // javafx.scene.Node
                public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
                    switch (action) {
                        case FIRE:
                            Tab tab2 = TabHeaderSkin.this.getTab();
                            TabPaneBehavior behavior = TabPaneSkin.this.getBehavior();
                            if (behavior.canCloseTab(tab2)) {
                                behavior.closeTab(tab2);
                                setOnMousePressed(null);
                                break;
                            }
                            break;
                    }
                    super.executeAccessibleAction(action, parameters);
                }
            };
            this.closeBtn.setAccessibleRole(AccessibleRole.BUTTON);
            this.closeBtn.setAccessibleText(ControlResources.getString("Accessibility.title.TabPane.CloseButton"));
            this.closeBtn.getStyleClass().setAll("tab-close-button");
            this.closeBtn.setOnMousePressed(new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabHeaderSkin.3
                @Override // javafx.event.EventHandler
                public void handle(MouseEvent me) {
                    Tab tab2 = TabHeaderSkin.this.getTab();
                    TabPaneBehavior behavior = TabPaneSkin.this.getBehavior();
                    if (behavior.canCloseTab(tab2)) {
                        behavior.closeTab(tab2);
                        TabHeaderSkin.this.setOnMousePressed(null);
                    }
                }
            });
            updateGraphicRotation();
            final Region focusIndicator = new Region();
            focusIndicator.setMouseTransparent(true);
            focusIndicator.getStyleClass().add("focus-indicator");
            this.inner = new StackPane() { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabHeaderSkin.4
                /* JADX WARN: Multi-variable type inference failed */
                @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
                protected void layoutChildren() {
                    TabPane skinnable = (TabPane) TabPaneSkin.this.getSkinnable();
                    double paddingTop = snappedTopInset();
                    double paddingRight = snappedRightInset();
                    double paddingBottom = snappedBottomInset();
                    double paddingLeft = snappedLeftInset();
                    double w2 = getWidth() - (paddingLeft + paddingRight);
                    double h2 = getHeight() - (paddingTop + paddingBottom);
                    double prefLabelWidth = snapSize(TabHeaderSkin.this.label.prefWidth(-1.0d));
                    double prefLabelHeight = snapSize(TabHeaderSkin.this.label.prefHeight(-1.0d));
                    double closeBtnWidth = TabHeaderSkin.this.showCloseButton() ? snapSize(TabHeaderSkin.this.closeBtn.prefWidth(-1.0d)) : 0.0d;
                    double closeBtnHeight = TabHeaderSkin.this.showCloseButton() ? snapSize(TabHeaderSkin.this.closeBtn.prefHeight(-1.0d)) : 0.0d;
                    double minWidth = snapSize(skinnable.getTabMinWidth());
                    double maxWidth = snapSize(skinnable.getTabMaxWidth());
                    double maxHeight = snapSize(skinnable.getTabMaxHeight());
                    double labelAreaWidth = prefLabelWidth;
                    double labelWidth = prefLabelWidth;
                    double labelHeight = prefLabelHeight;
                    double childrenWidth = labelAreaWidth + closeBtnWidth;
                    double childrenHeight = Math.max(labelHeight, closeBtnHeight);
                    if (childrenWidth > maxWidth && maxWidth != Double.MAX_VALUE) {
                        labelAreaWidth = maxWidth - closeBtnWidth;
                        labelWidth = maxWidth - closeBtnWidth;
                    } else if (childrenWidth < minWidth) {
                        labelAreaWidth = minWidth - closeBtnWidth;
                    }
                    if (childrenHeight > maxHeight && maxHeight != Double.MAX_VALUE) {
                        labelHeight = maxHeight;
                    }
                    if (TabHeaderSkin.this.animationState != TabAnimationState.NONE) {
                        labelAreaWidth *= TabHeaderSkin.this.animationTransition.get();
                        TabHeaderSkin.this.closeBtn.setVisible(false);
                    } else {
                        TabHeaderSkin.this.closeBtn.setVisible(TabHeaderSkin.this.showCloseButton());
                    }
                    TabHeaderSkin.this.label.resize(labelWidth, labelHeight);
                    double closeBtnStartX = ((maxWidth < Double.MAX_VALUE ? Math.min(w2, maxWidth) : w2) - paddingRight) - closeBtnWidth;
                    positionInArea(TabHeaderSkin.this.label, paddingLeft, paddingTop, labelAreaWidth, h2, 0.0d, HPos.CENTER, VPos.CENTER);
                    if (TabHeaderSkin.this.closeBtn.isVisible()) {
                        TabHeaderSkin.this.closeBtn.resize(closeBtnWidth, closeBtnHeight);
                        positionInArea(TabHeaderSkin.this.closeBtn, closeBtnStartX, paddingTop, closeBtnWidth, h2, 0.0d, HPos.CENTER, VPos.CENTER);
                    }
                    int vPadding = com.sun.javafx.util.Utils.isMac() ? 2 : 3;
                    int hPadding = com.sun.javafx.util.Utils.isMac() ? 2 : 1;
                    focusIndicator.resizeRelocate(paddingLeft - hPadding, paddingTop + vPadding, w2 + (2 * hPadding), h2 - (2 * vPadding));
                }
            };
            this.inner.getStyleClass().add("tab-container");
            this.inner.setRotate(((TabPane) TabPaneSkin.this.getSkinnable()).getSide().equals(Side.BOTTOM) ? 180.0d : 0.0d);
            this.inner.getChildren().addAll(this.label, this.closeBtn, focusIndicator);
            getChildren().addAll(this.inner);
            this.tooltip = tab.getTooltip();
            if (this.tooltip != null) {
                Tooltip.install(this, this.tooltip);
                this.oldTooltip = this.tooltip;
            }
            this.listener.registerChangeListener(tab.closableProperty(), "CLOSABLE");
            this.listener.registerChangeListener(tab.selectedProperty(), "SELECTED");
            this.listener.registerChangeListener(tab.textProperty(), "TEXT");
            this.listener.registerChangeListener(tab.graphicProperty(), "GRAPHIC");
            this.listener.registerChangeListener(tab.contextMenuProperty(), "CONTEXT_MENU");
            this.listener.registerChangeListener(tab.tooltipProperty(), "TOOLTIP");
            this.listener.registerChangeListener(tab.disableProperty(), "DISABLE");
            this.listener.registerChangeListener(tab.styleProperty(), "STYLE");
            tab.getStyleClass().addListener(this.weakStyleClassListener);
            this.listener.registerChangeListener(((TabPane) TabPaneSkin.this.getSkinnable()).tabClosingPolicyProperty(), "TAB_CLOSING_POLICY");
            this.listener.registerChangeListener(((TabPane) TabPaneSkin.this.getSkinnable()).sideProperty(), "SIDE");
            this.listener.registerChangeListener(((TabPane) TabPaneSkin.this.getSkinnable()).rotateGraphicProperty(), "ROTATE_GRAPHIC");
            this.listener.registerChangeListener(((TabPane) TabPaneSkin.this.getSkinnable()).tabMinWidthProperty(), "TAB_MIN_WIDTH");
            this.listener.registerChangeListener(((TabPane) TabPaneSkin.this.getSkinnable()).tabMaxWidthProperty(), "TAB_MAX_WIDTH");
            this.listener.registerChangeListener(((TabPane) TabPaneSkin.this.getSkinnable()).tabMinHeightProperty(), "TAB_MIN_HEIGHT");
            this.listener.registerChangeListener(((TabPane) TabPaneSkin.this.getSkinnable()).tabMaxHeightProperty(), "TAB_MAX_HEIGHT");
            getProperties().put(Tab.class, tab);
            getProperties().put(ContextMenu.class, tab.getContextMenu());
            setOnContextMenuRequested(me -> {
                if (getTab().getContextMenu() != null) {
                    getTab().getContextMenu().show(this.inner, me.getScreenX(), me.getScreenY());
                    me.consume();
                }
            });
            setOnMousePressed(new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabHeaderSkin.5
                @Override // javafx.event.EventHandler
                public void handle(MouseEvent me2) {
                    if (TabHeaderSkin.this.getTab().isDisable()) {
                        return;
                    }
                    if (me2.getButton().equals(MouseButton.MIDDLE)) {
                        if (TabHeaderSkin.this.showCloseButton()) {
                            Tab tab2 = TabHeaderSkin.this.getTab();
                            TabPaneBehavior behavior = TabPaneSkin.this.getBehavior();
                            if (behavior.canCloseTab(tab2)) {
                                TabHeaderSkin.this.removeListeners(tab2);
                                behavior.closeTab(tab2);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if (me2.getButton().equals(MouseButton.PRIMARY)) {
                        TabPaneSkin.this.getBehavior().selectTab(TabHeaderSkin.this.getTab());
                    }
                }
            });
            pseudoClassStateChanged(TabPaneSkin.SELECTED_PSEUDOCLASS_STATE, tab.isSelected());
            pseudoClassStateChanged(TabPaneSkin.DISABLED_PSEUDOCLASS_STATE, tab.isDisable());
            Side side = ((TabPane) TabPaneSkin.this.getSkinnable()).getSide();
            pseudoClassStateChanged(TabPaneSkin.TOP_PSEUDOCLASS_STATE, side == Side.TOP);
            pseudoClassStateChanged(TabPaneSkin.RIGHT_PSEUDOCLASS_STATE, side == Side.RIGHT);
            pseudoClassStateChanged(TabPaneSkin.BOTTOM_PSEUDOCLASS_STATE, side == Side.BOTTOM);
            pseudoClassStateChanged(TabPaneSkin.LEFT_PSEUDOCLASS_STATE, side == Side.LEFT);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void handlePropertyChanged(String p2) {
            if ("CLOSABLE".equals(p2)) {
                this.inner.requestLayout();
                requestLayout();
                return;
            }
            if ("SELECTED".equals(p2)) {
                pseudoClassStateChanged(TabPaneSkin.SELECTED_PSEUDOCLASS_STATE, this.tab.isSelected());
                this.inner.requestLayout();
                requestLayout();
                return;
            }
            if ("TEXT".equals(p2)) {
                this.label.setText(getTab().getText());
                return;
            }
            if ("GRAPHIC".equals(p2)) {
                this.label.setGraphic(getTab().getGraphic());
                return;
            }
            if (!"CONTEXT_MENU".equals(p2)) {
                if ("TOOLTIP".equals(p2)) {
                    if (this.oldTooltip != null) {
                        Tooltip.uninstall(this, this.oldTooltip);
                    }
                    this.tooltip = this.tab.getTooltip();
                    if (this.tooltip != null) {
                        Tooltip.install(this, this.tooltip);
                        this.oldTooltip = this.tooltip;
                        return;
                    }
                    return;
                }
                if ("DISABLE".equals(p2)) {
                    pseudoClassStateChanged(TabPaneSkin.DISABLED_PSEUDOCLASS_STATE, this.tab.isDisable());
                    this.inner.requestLayout();
                    requestLayout();
                    return;
                }
                if ("STYLE".equals(p2)) {
                    setStyle(this.tab.getStyle());
                    return;
                }
                if ("TAB_CLOSING_POLICY".equals(p2)) {
                    this.inner.requestLayout();
                    requestLayout();
                    return;
                }
                if ("SIDE".equals(p2)) {
                    Side side = ((TabPane) TabPaneSkin.this.getSkinnable()).getSide();
                    pseudoClassStateChanged(TabPaneSkin.TOP_PSEUDOCLASS_STATE, side == Side.TOP);
                    pseudoClassStateChanged(TabPaneSkin.RIGHT_PSEUDOCLASS_STATE, side == Side.RIGHT);
                    pseudoClassStateChanged(TabPaneSkin.BOTTOM_PSEUDOCLASS_STATE, side == Side.BOTTOM);
                    pseudoClassStateChanged(TabPaneSkin.LEFT_PSEUDOCLASS_STATE, side == Side.LEFT);
                    this.inner.setRotate(side == Side.BOTTOM ? 180.0d : 0.0d);
                    if (((TabPane) TabPaneSkin.this.getSkinnable()).isRotateGraphic()) {
                        updateGraphicRotation();
                        return;
                    }
                    return;
                }
                if ("ROTATE_GRAPHIC".equals(p2)) {
                    updateGraphicRotation();
                    return;
                }
                if ("TAB_MIN_WIDTH".equals(p2)) {
                    requestLayout();
                    ((TabPane) TabPaneSkin.this.getSkinnable()).requestLayout();
                    return;
                }
                if ("TAB_MAX_WIDTH".equals(p2)) {
                    requestLayout();
                    ((TabPane) TabPaneSkin.this.getSkinnable()).requestLayout();
                } else if ("TAB_MIN_HEIGHT".equals(p2)) {
                    requestLayout();
                    ((TabPane) TabPaneSkin.this.getSkinnable()).requestLayout();
                } else if ("TAB_MAX_HEIGHT".equals(p2)) {
                    requestLayout();
                    ((TabPane) TabPaneSkin.this.getSkinnable()).requestLayout();
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void updateGraphicRotation() {
            float f2;
            double d2;
            if (this.label.getGraphic() != null) {
                Node graphic = this.label.getGraphic();
                if (((TabPane) TabPaneSkin.this.getSkinnable()).isRotateGraphic()) {
                    d2 = 0.0d;
                } else {
                    if (((TabPane) TabPaneSkin.this.getSkinnable()).getSide().equals(Side.RIGHT)) {
                        f2 = -90.0f;
                    } else {
                        f2 = ((TabPane) TabPaneSkin.this.getSkinnable()).getSide().equals(Side.LEFT) ? 90.0f : 0.0f;
                    }
                    d2 = f2;
                }
                graphic.setRotate(d2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public boolean showCloseButton() {
            return this.tab.isClosable() && (((TabPane) TabPaneSkin.this.getSkinnable()).getTabClosingPolicy().equals(TabPane.TabClosingPolicy.ALL_TABS) || (((TabPane) TabPaneSkin.this.getSkinnable()).getTabClosingPolicy().equals(TabPane.TabClosingPolicy.SELECTED_TAB) && this.tab.isSelected()));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeListeners(Tab tab) {
            this.listener.dispose();
            this.inner.getChildren().clear();
            getChildren().clear();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            double minWidth = snapSize(((TabPane) TabPaneSkin.this.getSkinnable()).getTabMinWidth());
            double maxWidth = snapSize(((TabPane) TabPaneSkin.this.getSkinnable()).getTabMaxWidth());
            double paddingRight = snappedRightInset();
            double paddingLeft = snappedLeftInset();
            double tmpPrefWidth = snapSize(this.label.prefWidth(-1.0d));
            if (showCloseButton()) {
                tmpPrefWidth += snapSize(this.closeBtn.prefWidth(-1.0d));
            }
            if (tmpPrefWidth > maxWidth) {
                tmpPrefWidth = maxWidth;
            } else if (tmpPrefWidth < minWidth) {
                tmpPrefWidth = minWidth;
            }
            return tmpPrefWidth + paddingRight + paddingLeft;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            double minHeight = snapSize(((TabPane) TabPaneSkin.this.getSkinnable()).getTabMinHeight());
            double maxHeight = snapSize(((TabPane) TabPaneSkin.this.getSkinnable()).getTabMaxHeight());
            double paddingTop = snappedTopInset();
            double paddingBottom = snappedBottomInset();
            double tmpPrefHeight = snapSize(this.label.prefHeight(width));
            if (tmpPrefHeight > maxHeight) {
                tmpPrefHeight = maxHeight;
            } else if (tmpPrefHeight < minHeight) {
                tmpPrefHeight = minHeight;
            }
            return tmpPrefHeight + paddingTop + paddingBottom;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
        protected void layoutChildren() {
            double w2 = ((snapSize(getWidth()) - snappedRightInset()) - snappedLeftInset()) * this.animationTransition.getValue2().doubleValue();
            this.inner.resize(w2, (snapSize(getHeight()) - snappedTopInset()) - snappedBottomInset());
            this.inner.relocate(snappedLeftInset(), snappedTopInset());
        }

        @Override // javafx.scene.layout.Region
        protected void setWidth(double value) {
            super.setWidth(value);
            this.clip.setWidth(value);
        }

        @Override // javafx.scene.layout.Region
        protected void setHeight(double value) {
            super.setHeight(value);
            this.clip.setHeight(value);
        }

        @Override // javafx.scene.Parent, javafx.scene.Node
        public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
            switch (attribute) {
                case TEXT:
                    return getTab().getText();
                case SELECTED:
                    return Boolean.valueOf(TabPaneSkin.this.selectedTab == getTab());
                default:
                    return super.queryAccessibleAttribute(attribute, parameters);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.Node
        public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
            switch (action) {
                case REQUEST_FOCUS:
                    ((TabPane) TabPaneSkin.this.getSkinnable()).getSelectionModel().select((SingleSelectionModel<Tab>) getTab());
                    break;
                default:
                    super.executeAccessibleAction(action, parameters);
                    break;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TabPaneSkin$TabContentRegion.class */
    class TabContentRegion extends StackPane {
        private TraversalEngine engine;
        private Tab tab;
        private Direction direction = Direction.NEXT;
        private InvalidationListener tabContentListener = valueModel -> {
            updateContent();
        };
        private InvalidationListener tabSelectedListener = new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabContentRegion.1
            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable valueModel) {
                TabContentRegion.this.setVisible(TabContentRegion.this.tab.isSelected());
            }
        };
        private WeakInvalidationListener weakTabContentListener = new WeakInvalidationListener(this.tabContentListener);
        private WeakInvalidationListener weakTabSelectedListener = new WeakInvalidationListener(this.tabSelectedListener);

        public Tab getTab() {
            return this.tab;
        }

        public TabContentRegion(Tab tab) {
            getStyleClass().setAll("tab-content-area");
            setManaged(false);
            this.tab = tab;
            updateContent();
            setVisible(tab.isSelected());
            tab.selectedProperty().addListener(this.weakTabSelectedListener);
            tab.contentProperty().addListener(this.weakTabContentListener);
        }

        private void updateContent() {
            Node newContent = getTab().getContent();
            if (newContent == null) {
                getChildren().clear();
            } else {
                getChildren().setAll(newContent);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeListeners(Tab tab) {
            tab.selectedProperty().removeListener(this.weakTabSelectedListener);
            tab.contentProperty().removeListener(this.weakTabContentListener);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TabPaneSkin$TabControlButtons.class */
    class TabControlButtons extends StackPane {
        private StackPane inner;
        private StackPane downArrow;
        private Pane downArrowBtn;
        private boolean showControlButtons;
        private ContextMenu popup;
        private boolean showTabsMenu = false;

        /* JADX WARN: Multi-variable type inference failed */
        public TabControlButtons() {
            getStyleClass().setAll("control-buttons-tab");
            TabPane tabPane = (TabPane) TabPaneSkin.this.getSkinnable();
            this.downArrowBtn = new Pane();
            this.downArrowBtn.getStyleClass().setAll("tab-down-button");
            this.downArrowBtn.setVisible(isShowTabsMenu());
            this.downArrow = new StackPane();
            this.downArrow.setManaged(false);
            this.downArrow.getStyleClass().setAll("arrow");
            this.downArrow.setRotate(tabPane.getSide().equals(Side.BOTTOM) ? 180.0d : 0.0d);
            this.downArrowBtn.getChildren().add(this.downArrow);
            this.downArrowBtn.setOnMouseClicked(me -> {
                showPopupMenu();
            });
            setupPopupMenu();
            this.inner = new StackPane() { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabControlButtons.1
                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computePrefWidth(double height) {
                    double maxArrowWidth = !TabControlButtons.this.isShowTabsMenu() ? 0.0d : snapSize(TabControlButtons.this.downArrow.prefWidth(getHeight())) + snapSize(TabControlButtons.this.downArrowBtn.prefWidth(getHeight()));
                    double pw = 0.0d;
                    if (TabControlButtons.this.isShowTabsMenu()) {
                        pw = 0.0d + maxArrowWidth;
                    }
                    if (pw > 0.0d) {
                        pw += snappedLeftInset() + snappedRightInset();
                    }
                    return pw;
                }

                @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
                protected double computePrefHeight(double width) {
                    double height = 0.0d;
                    if (TabControlButtons.this.isShowTabsMenu()) {
                        height = Math.max(0.0d, snapSize(TabControlButtons.this.downArrowBtn.prefHeight(width)));
                    }
                    if (height > 0.0d) {
                        height += snappedTopInset() + snappedBottomInset();
                    }
                    return height;
                }

                @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
                protected void layoutChildren() {
                    if (TabControlButtons.this.isShowTabsMenu()) {
                        double y2 = snappedTopInset();
                        double w2 = (snapSize(getWidth()) - 0.0d) + snappedLeftInset();
                        double h2 = (snapSize(getHeight()) - y2) + snappedBottomInset();
                        positionArrow(TabControlButtons.this.downArrowBtn, TabControlButtons.this.downArrow, 0.0d, y2, w2, h2);
                    }
                }

                private void positionArrow(Pane btn, StackPane arrow, double x2, double y2, double width, double height) {
                    btn.resize(width, height);
                    positionInArea(btn, x2, y2, width, height, 0.0d, HPos.CENTER, VPos.CENTER);
                    double arrowWidth = snapSize(arrow.prefWidth(-1.0d));
                    double arrowHeight = snapSize(arrow.prefHeight(-1.0d));
                    arrow.resize(arrowWidth, arrowHeight);
                    positionInArea(arrow, btn.snappedLeftInset(), btn.snappedTopInset(), (width - btn.snappedLeftInset()) - btn.snappedRightInset(), (height - btn.snappedTopInset()) - btn.snappedBottomInset(), 0.0d, HPos.CENTER, VPos.CENTER);
                }
            };
            this.inner.getStyleClass().add("container");
            this.inner.getChildren().add(this.downArrowBtn);
            getChildren().add(this.inner);
            tabPane.sideProperty().addListener(valueModel -> {
                Side tabPosition = ((TabPane) TabPaneSkin.this.getSkinnable()).getSide();
                this.downArrow.setRotate(tabPosition.equals(Side.BOTTOM) ? 180.0d : 0.0d);
            });
            tabPane.getTabs().addListener(c2 -> {
                setupPopupMenu();
            });
            this.showControlButtons = false;
            if (isShowTabsMenu()) {
                this.showControlButtons = true;
                requestLayout();
            }
            getProperties().put(ContextMenu.class, this.popup);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showTabsMenu(boolean value) {
            boolean wasTabsMenuShowing = isShowTabsMenu();
            this.showTabsMenu = value;
            if (this.showTabsMenu && !wasTabsMenuShowing) {
                this.downArrowBtn.setVisible(true);
                this.showControlButtons = true;
                this.inner.requestLayout();
                TabPaneSkin.this.tabHeaderArea.requestLayout();
                return;
            }
            if (!this.showTabsMenu && wasTabsMenuShowing) {
                hideControlButtons();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isShowTabsMenu() {
            return this.showTabsMenu;
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefWidth(double height) {
            double pw = snapSize(this.inner.prefWidth(height));
            if (pw > 0.0d) {
                pw += snappedLeftInset() + snappedRightInset();
            }
            return pw;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javafx.scene.layout.StackPane, javafx.scene.layout.Region, javafx.scene.Parent
        protected double computePrefHeight(double width) {
            return Math.max(((TabPane) TabPaneSkin.this.getSkinnable()).getTabMinHeight(), snapSize(this.inner.prefHeight(width))) + snappedTopInset() + snappedBottomInset();
        }

        @Override // javafx.scene.layout.StackPane, javafx.scene.Parent
        protected void layoutChildren() {
            double x2 = snappedLeftInset();
            double y2 = snappedTopInset();
            double w2 = (snapSize(getWidth()) - x2) + snappedRightInset();
            double h2 = (snapSize(getHeight()) - y2) + snappedBottomInset();
            if (this.showControlButtons) {
                showControlButtons();
                this.showControlButtons = false;
            }
            this.inner.resize(w2, h2);
            positionInArea(this.inner, x2, y2, w2, h2, 0.0d, HPos.CENTER, VPos.BOTTOM);
        }

        private void showControlButtons() {
            setVisible(true);
            if (this.popup == null) {
                setupPopupMenu();
            }
        }

        private void hideControlButtons() {
            if (isShowTabsMenu()) {
                this.showControlButtons = true;
            } else {
                setVisible(false);
                this.popup.getItems().clear();
                this.popup = null;
            }
            requestLayout();
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void setupPopupMenu() {
            if (this.popup == null) {
                this.popup = new ContextMenu();
            }
            this.popup.getItems().clear();
            ToggleGroup group = new ToggleGroup();
            ObservableList<RadioMenuItem> menuitems = FXCollections.observableArrayList();
            for (Tab tab : ((TabPane) TabPaneSkin.this.getSkinnable()).getTabs()) {
                TabMenuItem item = TabPaneSkin.this.new TabMenuItem(tab);
                item.setToggleGroup(group);
                item.setOnAction(t2 -> {
                    ((TabPane) TabPaneSkin.this.getSkinnable()).getSelectionModel().select((SingleSelectionModel<Tab>) tab);
                });
                menuitems.add(item);
            }
            this.popup.getItems().addAll(menuitems);
        }

        private void showPopupMenu() {
            Iterator<MenuItem> it = this.popup.getItems().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MenuItem mi = it.next();
                TabMenuItem tmi = (TabMenuItem) mi;
                if (TabPaneSkin.this.selectedTab.equals(tmi.getTab())) {
                    tmi.setSelected(true);
                    break;
                }
            }
            this.popup.show(this.downArrowBtn, Side.BOTTOM, 0.0d, 0.0d);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/TabPaneSkin$TabMenuItem.class */
    class TabMenuItem extends RadioMenuItem {
        Tab tab;
        private InvalidationListener disableListener;
        private WeakInvalidationListener weakDisableListener;

        public TabMenuItem(Tab tab) {
            super(tab.getText(), TabPaneSkin.clone(tab.getGraphic()));
            this.disableListener = new InvalidationListener() { // from class: com.sun.javafx.scene.control.skin.TabPaneSkin.TabMenuItem.1
                @Override // javafx.beans.InvalidationListener
                public void invalidated(Observable o2) {
                    TabMenuItem.this.setDisable(TabMenuItem.this.tab.isDisable());
                }
            };
            this.weakDisableListener = new WeakInvalidationListener(this.disableListener);
            this.tab = tab;
            setDisable(tab.isDisable());
            tab.disableProperty().addListener(this.weakDisableListener);
            textProperty().bind(tab.textProperty());
        }

        public Tab getTab() {
            return this.tab;
        }

        public void dispose() {
            this.tab.disableProperty().removeListener(this.weakDisableListener);
        }
    }

    @Override // javafx.scene.control.SkinBase
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case FOCUS_ITEM:
                return this.tabHeaderArea.getTabHeaderSkin(this.selectedTab);
            case ITEM_COUNT:
                return Integer.valueOf(this.tabHeaderArea.headersRegion.getChildren().size());
            case ITEM_AT_INDEX:
                Integer index = (Integer) parameters[0];
                if (index == null) {
                    return null;
                }
                return this.tabHeaderArea.headersRegion.getChildren().get(index.intValue());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
