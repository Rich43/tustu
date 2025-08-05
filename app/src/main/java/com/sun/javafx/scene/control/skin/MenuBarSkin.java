package com.sun.javafx.scene.control.skin;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.control.GlobalMenuAdapter;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraverseListener;
import com.sun.javafx.stage.StageHelper;
import com.sun.javafx.tk.Toolkit;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/MenuBarSkin.class */
public class MenuBarSkin extends BehaviorSkinBase<MenuBar, BehaviorBase<MenuBar>> implements TraverseListener {
    private final HBox container;
    private Menu openMenu;
    private MenuBarButton openMenuButton;
    private int focusedMenuIndex;
    private static WeakHashMap<Stage, Reference<MenuBarSkin>> systemMenuMap;
    private static Stage currentMenuBarStage;
    private List<MenuBase> wrappedMenus;
    private WeakEventHandler<KeyEvent> weakSceneKeyEventHandler;
    private WeakEventHandler<MouseEvent> weakSceneMouseEventHandler;
    private WeakChangeListener<Boolean> weakWindowFocusListener;
    private WeakChangeListener<Window> weakWindowSceneListener;
    private EventHandler<KeyEvent> keyEventHandler;
    private EventHandler<MouseEvent> mouseEventHandler;
    private ChangeListener<Boolean> menuBarFocusedPropertyListener;
    private ChangeListener<Scene> sceneChangeListener;
    Runnable firstMenuRunnable;
    private boolean pendingDismiss;
    private EventHandler<ActionEvent> menuActionEventHandler;
    private ListChangeListener<MenuItem> menuItemListener;
    private DoubleProperty spacing;
    private ObjectProperty<Pos> containerAlignment;
    private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
    private static List<MenuBase> wrappedDefaultMenus = new ArrayList();
    private static final CssMetaData<MenuBar, Number> SPACING = new CssMetaData<MenuBar, Number>("-fx-spacing", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: com.sun.javafx.scene.control.skin.MenuBarSkin.4
        @Override // javafx.css.CssMetaData
        public boolean isSettable(MenuBar n2) {
            MenuBarSkin skin = (MenuBarSkin) n2.getSkin();
            return skin.spacing == null || !skin.spacing.isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Number> getStyleableProperty(MenuBar n2) {
            MenuBarSkin skin = (MenuBarSkin) n2.getSkin();
            return (StyleableProperty) skin.spacingProperty();
        }
    };
    private static final CssMetaData<MenuBar, Pos> ALIGNMENT = new CssMetaData<MenuBar, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) { // from class: com.sun.javafx.scene.control.skin.MenuBarSkin.5
        @Override // javafx.css.CssMetaData
        public boolean isSettable(MenuBar n2) {
            MenuBarSkin skin = (MenuBarSkin) n2.getSkin();
            return skin.containerAlignment == null || !skin.containerAlignment.isBound();
        }

        @Override // javafx.css.CssMetaData
        public StyleableProperty<Pos> getStyleableProperty(MenuBar n2) {
            MenuBarSkin skin = (MenuBarSkin) n2.getSkin();
            return (StyleableProperty) skin.containerAlignmentProperty();
        }
    };

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

    public static void setDefaultSystemMenuBar(MenuBar menuBar) {
        if (Toolkit.getToolkit().getSystemMenu().isSupported()) {
            wrappedDefaultMenus.clear();
            for (Menu menu : menuBar.getMenus()) {
                wrappedDefaultMenus.add(GlobalMenuAdapter.adapt(menu));
            }
            menuBar.getMenus().addListener(c2 -> {
                wrappedDefaultMenus.clear();
                for (Menu menu2 : menuBar.getMenus()) {
                    wrappedDefaultMenus.add(GlobalMenuAdapter.adapt(menu2));
                }
            });
        }
    }

    private static MenuBarSkin getMenuBarSkin(Stage stage) {
        Reference<MenuBarSkin> skinRef;
        if (systemMenuMap == null || (skinRef = systemMenuMap.get(stage)) == null) {
            return null;
        }
        return skinRef.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setSystemMenu(Stage stage) {
        MenuBarSkin skin;
        MenuBarSkin skin2;
        if (stage != null && stage.isFocused()) {
            while (stage != null && (stage.getOwner() instanceof Stage) && ((skin2 = getMenuBarSkin(stage)) == null || skin2.wrappedMenus == null)) {
                stage = (Stage) stage.getOwner();
            }
        } else {
            stage = null;
        }
        if (stage != currentMenuBarStage) {
            List<MenuBase> menuList = null;
            if (stage != null && (skin = getMenuBarSkin(stage)) != null) {
                menuList = skin.wrappedMenus;
            }
            if (menuList == null) {
                menuList = wrappedDefaultMenus;
            }
            Toolkit.getToolkit().getSystemMenu().setMenus(menuList);
            currentMenuBarStage = stage;
        }
    }

    private static void initSystemMenuBar() {
        systemMenuMap = new WeakHashMap<>();
        InvalidationListener focusedStageListener = ov -> {
            setSystemMenu((Stage) ((ReadOnlyProperty) ov).getBean());
        };
        ObservableList<Stage> stages = StageHelper.getStages();
        for (Stage stage : stages) {
            stage.focusedProperty().addListener(focusedStageListener);
        }
        stages.addListener(c2 -> {
            while (c2.next()) {
                Iterator it = c2.getRemoved().iterator();
                while (it.hasNext()) {
                    ((Stage) it.next()).focusedProperty().removeListener(focusedStageListener);
                }
                for (Stage stage2 : c2.getAddedSubList()) {
                    stage2.focusedProperty().addListener(focusedStageListener);
                    setSystemMenu(stage2);
                }
            }
        });
    }

    EventHandler<KeyEvent> getKeyEventHandler() {
        return this.keyEventHandler;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v9, types: [javafx.scene.Parent, javafx.scene.control.Control] */
    public MenuBarSkin(MenuBar control) {
        KeyCombination acceleratorKeyCombo;
        super(control, new BehaviorBase(control, Collections.emptyList()));
        this.focusedMenuIndex = -1;
        this.firstMenuRunnable = new Runnable() { // from class: com.sun.javafx.scene.control.skin.MenuBarSkin.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                if (MenuBarSkin.this.container.getChildren().size() > 0 && (MenuBarSkin.this.container.getChildren().get(0) instanceof MenuButton)) {
                    if (MenuBarSkin.this.focusedMenuIndex != 0) {
                        MenuBarSkin.this.unSelectMenus();
                        MenuBarSkin.this.menuModeStart(0);
                        MenuBarSkin.this.openMenuButton = (MenuBarButton) MenuBarSkin.this.container.getChildren().get(0);
                        MenuBarSkin.this.openMenu = ((MenuBar) MenuBarSkin.this.getSkinnable()).getMenus().get(0);
                        MenuBarSkin.this.openMenuButton.setHover();
                        return;
                    }
                    MenuBarSkin.this.unSelectMenus();
                }
            }
        };
        this.pendingDismiss = false;
        this.menuActionEventHandler = t2 -> {
            if (t2.getSource() instanceof CustomMenuItem) {
                CustomMenuItem cmi = (CustomMenuItem) t2.getSource();
                if (!cmi.isHideOnClick()) {
                    return;
                }
            }
            unSelectMenus();
        };
        this.menuItemListener = c2 -> {
            while (c2.next()) {
                for (MenuItem mi : c2.getAddedSubList()) {
                    mi.addEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
                }
                for (MenuItem mi2 : c2.getRemoved()) {
                    mi2.removeEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
                }
            }
        };
        this.container = new HBox();
        this.container.getStyleClass().add("container");
        getChildren().add(this.container);
        this.keyEventHandler = event -> {
            if (this.openMenu != null) {
                switch (event.getCode()) {
                    case LEFT:
                        boolean isRTL = control.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
                        if (control.getScene().getWindow().isFocused()) {
                            if (this.openMenu != null) {
                                if (!this.openMenu.isShowing()) {
                                    if (isRTL) {
                                        selectNextMenu();
                                    } else {
                                        selectPrevMenu();
                                    }
                                    event.consume();
                                    break;
                                } else if (isRTL) {
                                    showNextMenu();
                                } else {
                                    showPrevMenu();
                                }
                            }
                        }
                        event.consume();
                        break;
                    case RIGHT:
                        boolean isRTL2 = control.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
                        if (control.getScene().getWindow().isFocused()) {
                            if (this.openMenu != null) {
                                if (!this.openMenu.isShowing()) {
                                    if (isRTL2) {
                                        selectPrevMenu();
                                    } else {
                                        selectNextMenu();
                                    }
                                    event.consume();
                                    break;
                                } else if (isRTL2) {
                                    showPrevMenu();
                                } else {
                                    showNextMenu();
                                }
                            }
                        }
                        event.consume();
                        break;
                    case DOWN:
                        if (control.getScene().getWindow().isFocused() && this.focusedMenuIndex != -1 && this.openMenu != null) {
                            this.openMenu = ((MenuBar) getSkinnable()).getMenus().get(this.focusedMenuIndex);
                            if (!isMenuEmpty(((MenuBar) getSkinnable()).getMenus().get(this.focusedMenuIndex))) {
                                this.openMenu.show();
                            }
                            event.consume();
                            break;
                        }
                        break;
                    case ESCAPE:
                        unSelectMenus();
                        event.consume();
                        break;
                }
            }
        };
        this.menuBarFocusedPropertyListener = (ov, t3, t1) -> {
            if (t1.booleanValue()) {
                unSelectMenus();
                menuModeStart(0);
                this.openMenuButton = (MenuBarButton) this.container.getChildren().get(0);
                this.openMenu = ((MenuBar) getSkinnable()).getMenus().get(0);
                this.openMenuButton.setHover();
                return;
            }
            unSelectMenus();
        };
        this.weakSceneKeyEventHandler = new WeakEventHandler<>(this.keyEventHandler);
        Utils.executeOnceWhenPropertyIsNonNull(control.sceneProperty(), scene -> {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, this.weakSceneKeyEventHandler);
        });
        this.mouseEventHandler = t4 -> {
            if (!this.container.localToScreen(this.container.getLayoutBounds()).contains(t4.getScreenX(), t4.getScreenY())) {
                unSelectMenus();
            }
        };
        this.weakSceneMouseEventHandler = new WeakEventHandler<>(this.mouseEventHandler);
        Utils.executeOnceWhenPropertyIsNonNull(control.sceneProperty(), scene2 -> {
            scene2.addEventFilter(MouseEvent.MOUSE_CLICKED, this.weakSceneMouseEventHandler);
        });
        this.weakWindowFocusListener = new WeakChangeListener<>((ov2, t5, t12) -> {
            if (!t12.booleanValue()) {
                unSelectMenus();
            }
        });
        Utils.executeOnceWhenPropertyIsNonNull(control.sceneProperty(), scene3 -> {
            if (scene3.getWindow() != null) {
                scene3.getWindow().focusedProperty().addListener(this.weakWindowFocusListener);
                return;
            }
            ChangeListener<Window> sceneWindowListener = (observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    oldValue.focusedProperty().removeListener(this.weakWindowFocusListener);
                }
                if (newValue != null) {
                    newValue.focusedProperty().addListener(this.weakWindowFocusListener);
                }
            };
            this.weakWindowSceneListener = new WeakChangeListener<>(sceneWindowListener);
            scene3.windowProperty().addListener(this.weakWindowSceneListener);
        });
        rebuildUI();
        control.getMenus().addListener(c3 -> {
            rebuildUI();
        });
        for (Menu menu : ((MenuBar) getSkinnable()).getMenus()) {
            menu.visibleProperty().addListener((ov3, t6, t13) -> {
                rebuildUI();
            });
        }
        if (Toolkit.getToolkit().getSystemMenu().isSupported()) {
            control.useSystemMenuBarProperty().addListener(valueModel -> {
                rebuildUI();
            });
        }
        if (com.sun.javafx.util.Utils.isMac()) {
            acceleratorKeyCombo = KeyCombination.keyCombination("ctrl+F10");
        } else {
            acceleratorKeyCombo = KeyCombination.keyCombination("F10");
        }
        KeyCombination keyCombination = acceleratorKeyCombo;
        Utils.executeOnceWhenPropertyIsNonNull(control.sceneProperty(), scene4 -> {
            scene4.getAccelerators().put(keyCombination, this.firstMenuRunnable);
            scene4.addEventHandler(KeyEvent.KEY_PRESSED, e2 -> {
                if (e2.isAltDown() && !e2.isConsumed()) {
                    this.firstMenuRunnable.run();
                }
            });
        });
        ParentTraversalEngine engine = new ParentTraversalEngine(getSkinnable());
        engine.addTraverseListener(this);
        ((MenuBar) getSkinnable()).setImpl_traversalEngine(engine);
        KeyCombination keyCombination2 = acceleratorKeyCombo;
        control.sceneProperty().addListener((ov4, t7, t14) -> {
            if (this.weakSceneKeyEventHandler != null && t7 != null) {
                t7.removeEventFilter(KeyEvent.KEY_PRESSED, this.weakSceneKeyEventHandler);
            }
            if (this.weakSceneMouseEventHandler != null && t7 != null) {
                t7.removeEventFilter(MouseEvent.MOUSE_CLICKED, this.weakSceneMouseEventHandler);
            }
            if (t7 != null) {
                t7.getAccelerators().remove(keyCombination2);
            }
            if (t14 != null) {
                t14.getAccelerators().put(keyCombination2, this.firstMenuRunnable);
            }
        });
    }

    MenuButton getNodeForMenu(int i2) {
        if (i2 < this.container.getChildren().size()) {
            return (MenuBarButton) this.container.getChildren().get(i2);
        }
        return null;
    }

    int getFocusedMenuIndex() {
        return this.focusedMenuIndex;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean menusContainCustomMenuItem() {
        for (Menu menu : ((MenuBar) getSkinnable()).getMenus()) {
            if (menuContainsCustomMenuItem(menu)) {
                System.err.println("Warning: MenuBar ignored property useSystemMenuBar because menus contain CustomMenuItem");
                return true;
            }
        }
        return false;
    }

    private boolean menuContainsCustomMenuItem(Menu menu) {
        for (MenuItem mi : menu.getItems()) {
            if ((mi instanceof CustomMenuItem) && !(mi instanceof SeparatorMenuItem)) {
                return true;
            }
            if ((mi instanceof Menu) && menuContainsCustomMenuItem((Menu) mi)) {
                return true;
            }
        }
        return false;
    }

    private int getMenuBarButtonIndex(MenuBarButton m2) {
        for (int i2 = 0; i2 < this.container.getChildren().size(); i2++) {
            MenuBarButton menuButton = (MenuBarButton) this.container.getChildren().get(i2);
            if (m2 == menuButton) {
                return i2;
            }
        }
        return -1;
    }

    private void updateActionListeners(Menu m2, boolean add) {
        if (add) {
            m2.getItems().addListener(this.menuItemListener);
        } else {
            m2.getItems().removeListener(this.menuItemListener);
        }
        for (MenuItem mi : m2.getItems()) {
            if (mi instanceof Menu) {
                updateActionListeners((Menu) mi, add);
            } else if (add) {
                mi.addEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
            } else {
                mi.removeEventHandler(ActionEvent.ACTION, this.menuActionEventHandler);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void rebuildUI() {
        ((MenuBar) getSkinnable()).focusedProperty().removeListener(this.menuBarFocusedPropertyListener);
        for (Menu m2 : ((MenuBar) getSkinnable()).getMenus()) {
            updateActionListeners(m2, false);
        }
        for (Node n2 : this.container.getChildren()) {
            MenuBarButton menuButton = (MenuBarButton) n2;
            menuButton.hide();
            menuButton.menu.showingProperty().removeListener(menuButton.menuListener);
            menuButton.disableProperty().unbind();
            menuButton.textProperty().unbind();
            menuButton.graphicProperty().unbind();
            menuButton.styleProperty().unbind();
            menuButton.dispose();
            menuButton.setSkin(null);
        }
        this.container.getChildren().clear();
        if (Toolkit.getToolkit().getSystemMenu().isSupported()) {
            Scene scene = ((MenuBar) getSkinnable()).getScene();
            if (scene != null) {
                if (this.sceneChangeListener == null) {
                    this.sceneChangeListener = (observable, oldValue, newValue) -> {
                        Stage stage;
                        MenuBarSkin curMBSkin;
                        if (oldValue != null && (oldValue.getWindow() instanceof Stage) && (curMBSkin = getMenuBarSkin((stage = (Stage) oldValue.getWindow()))) == this) {
                            curMBSkin.wrappedMenus = null;
                            systemMenuMap.remove(stage);
                            if (currentMenuBarStage == stage) {
                                currentMenuBarStage = null;
                                setSystemMenu(stage);
                            }
                        }
                        if (newValue != null && ((MenuBar) getSkinnable()).isUseSystemMenuBar() && !menusContainCustomMenuItem() && (newValue.getWindow() instanceof Stage)) {
                            Stage stage2 = (Stage) newValue.getWindow();
                            if (systemMenuMap == null) {
                                initSystemMenuBar();
                            }
                            if (this.wrappedMenus == null) {
                                this.wrappedMenus = new ArrayList();
                                systemMenuMap.put(stage2, new WeakReference(this));
                            } else {
                                this.wrappedMenus.clear();
                            }
                            for (Menu menu : ((MenuBar) getSkinnable()).getMenus()) {
                                this.wrappedMenus.add(GlobalMenuAdapter.adapt(menu));
                            }
                            currentMenuBarStage = null;
                            setSystemMenu(stage2);
                            ((MenuBar) getSkinnable()).requestLayout();
                            Platform.runLater(() -> {
                                ((MenuBar) getSkinnable()).requestLayout();
                            });
                        }
                    };
                    ((MenuBar) getSkinnable()).sceneProperty().addListener(this.sceneChangeListener);
                }
                this.sceneChangeListener.changed(((MenuBar) getSkinnable()).sceneProperty(), scene, scene);
                if (currentMenuBarStage != null) {
                    if (getMenuBarSkin(currentMenuBarStage) == this) {
                        return;
                    }
                } else if (((MenuBar) getSkinnable()).isUseSystemMenuBar()) {
                    return;
                }
            } else if (currentMenuBarStage != null) {
                MenuBarSkin curMBSkin = getMenuBarSkin(currentMenuBarStage);
                if (curMBSkin == this) {
                    setSystemMenu(null);
                }
            }
        }
        ((MenuBar) getSkinnable()).focusedProperty().addListener(this.menuBarFocusedPropertyListener);
        for (Menu menu : ((MenuBar) getSkinnable()).getMenus()) {
            if (menu.isVisible()) {
                MenuBarButton menuButton2 = new MenuBarButton(this, menu);
                menuButton2.setFocusTraversable(false);
                menuButton2.getStyleClass().add("menu");
                menuButton2.setStyle(menu.getStyle());
                menuButton2.getItems().setAll(menu.getItems());
                this.container.getChildren().add(menuButton2);
                menuButton2.menuListener = (observable2, oldValue2, newValue2) -> {
                    if (menu.isShowing()) {
                        menuButton2.show();
                        menuModeStart(this.container.getChildren().indexOf(menuButton2));
                    } else {
                        menuButton2.hide();
                    }
                };
                menuButton2.menu = menu;
                menu.showingProperty().addListener(menuButton2.menuListener);
                menuButton2.disableProperty().bindBidirectional(menu.disableProperty());
                menuButton2.textProperty().bind(menu.textProperty());
                menuButton2.graphicProperty().bind(menu.graphicProperty());
                menuButton2.styleProperty().bind(menu.styleProperty());
                menuButton2.getProperties().addListener(c2 -> {
                    if (c2.wasAdded() && "autoHide".equals(c2.getKey())) {
                        menuButton2.getProperties().remove("autoHide");
                        menu.hide();
                    }
                });
                menuButton2.showingProperty().addListener((observable3, oldValue3, isShowing) -> {
                    if (isShowing.booleanValue()) {
                        if (this.openMenuButton != null && this.openMenuButton != menuButton2) {
                            this.openMenuButton.hide();
                        }
                        this.openMenuButton = menuButton2;
                        this.openMenu = menu;
                        if (!menu.isShowing()) {
                            menu.show();
                        }
                    }
                });
                menuButton2.setOnMousePressed(event -> {
                    this.pendingDismiss = menuButton2.isShowing();
                    if (menuButton2.getScene().getWindow().isFocused()) {
                        this.openMenu = menu;
                        if (!isMenuEmpty(menu)) {
                            this.openMenu.show();
                        }
                        menuModeStart(getMenuBarButtonIndex(menuButton2));
                    }
                });
                menuButton2.setOnMouseReleased(event2 -> {
                    if (menuButton2.getScene().getWindow().isFocused() && this.pendingDismiss) {
                        resetOpenMenu();
                    }
                    this.pendingDismiss = false;
                });
                menuButton2.setOnMouseEntered(event3 -> {
                    if (menuButton2.getScene() != null && menuButton2.getScene().getWindow() != null && menuButton2.getScene().getWindow().isFocused()) {
                        if (this.openMenuButton != null && this.openMenuButton != menuButton2) {
                            this.openMenuButton.clearHover();
                            this.openMenuButton = null;
                            this.openMenuButton = menuButton2;
                        }
                        updateFocusedIndex();
                        if (this.openMenu != null && this.openMenu != menu) {
                            this.openMenu.hide();
                            this.openMenu = menu;
                            updateFocusedIndex();
                            if (!isMenuEmpty(menu)) {
                                this.openMenu.show();
                            }
                        }
                    }
                });
                updateActionListeners(menu, true);
            }
        }
        ((MenuBar) getSkinnable()).requestLayout();
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
            this.spacing = new StyleableDoubleProperty() { // from class: com.sun.javafx.scene.control.skin.MenuBarSkin.2
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    double value = get();
                    MenuBarSkin.this.container.setSpacing(value);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MenuBarSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "spacing";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return MenuBarSkin.SPACING;
                }
            };
        }
        return this.spacing;
    }

    public final void setContainerAlignment(Pos value) {
        containerAlignmentProperty().set(value);
    }

    public final Pos getContainerAlignment() {
        return this.containerAlignment == null ? Pos.TOP_LEFT : this.containerAlignment.get();
    }

    public final ObjectProperty<Pos> containerAlignmentProperty() {
        if (this.containerAlignment == null) {
            this.containerAlignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT) { // from class: com.sun.javafx.scene.control.skin.MenuBarSkin.3
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    Pos value = get();
                    MenuBarSkin.this.container.setAlignment(value);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MenuBarSkin.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "containerAlignment";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<MenuBar, Pos> getCssMetaData() {
                    return MenuBarSkin.ALIGNMENT;
                }
            };
        }
        return this.containerAlignment;
    }

    @Override // com.sun.javafx.scene.control.skin.BehaviorSkinBase, javafx.scene.control.SkinBase, javafx.scene.control.Skin
    public void dispose() {
        cleanUpSystemMenu();
        super.dispose();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void cleanUpSystemMenu() {
        if (this.sceneChangeListener != null && getSkinnable() != 0) {
            ((MenuBar) getSkinnable()).sceneProperty().removeListener(this.sceneChangeListener);
            this.sceneChangeListener = null;
        }
        if (currentMenuBarStage != null && getMenuBarSkin(currentMenuBarStage) == this) {
            setSystemMenu(null);
        }
        if (systemMenuMap != null) {
            Iterator<Map.Entry<Stage, Reference<MenuBarSkin>>> iterator = systemMenuMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Stage, Reference<MenuBarSkin>> entry = iterator.next();
                Reference<MenuBarSkin> ref = entry.getValue();
                MenuBarSkin skin = ref != null ? ref.get() : null;
                if (skin == null || skin == this) {
                    iterator.remove();
                }
            }
        }
    }

    private boolean isMenuEmpty(Menu menu) {
        boolean retVal = true;
        if (menu != null) {
            for (MenuItem m2 : menu.getItems()) {
                if (m2 != null && m2.isVisible()) {
                    retVal = false;
                }
            }
        }
        return retVal;
    }

    private void resetOpenMenu() {
        if (this.openMenu != null) {
            this.openMenu.hide();
            this.openMenu = null;
            this.openMenuButton = (MenuBarButton) this.container.getChildren().get(this.focusedMenuIndex);
            this.openMenuButton.clearHover();
            this.openMenuButton = null;
            menuModeEnd();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unSelectMenus() {
        clearMenuButtonHover();
        if (this.focusedMenuIndex == -1) {
            return;
        }
        if (this.openMenu != null) {
            this.openMenu.hide();
            this.openMenu = null;
        }
        if (this.openMenuButton != null) {
            this.openMenuButton.clearHover();
            this.openMenuButton = null;
        }
        menuModeEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [javafx.scene.Node, javafx.scene.control.Control] */
    public void menuModeStart(int newIndex) {
        if (this.focusedMenuIndex == -1) {
            SceneHelper.getSceneAccessor().setTransientFocusContainer(((MenuBar) getSkinnable()).getScene(), getSkinnable());
        }
        this.focusedMenuIndex = newIndex;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void menuModeEnd() {
        if (this.focusedMenuIndex != -1) {
            SceneHelper.getSceneAccessor().setTransientFocusContainer(((MenuBar) getSkinnable()).getScene(), null);
            ((MenuBar) getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE);
        }
        this.focusedMenuIndex = -1;
    }

    private void selectNextMenu() {
        Menu nextMenu = findNextSibling();
        if (nextMenu != null && this.focusedMenuIndex != -1) {
            this.openMenuButton = (MenuBarButton) this.container.getChildren().get(this.focusedMenuIndex);
            this.openMenuButton.setHover();
            this.openMenu = nextMenu;
        }
    }

    private void selectPrevMenu() {
        Menu prevMenu = findPreviousSibling();
        if (prevMenu != null && this.focusedMenuIndex != -1) {
            this.openMenuButton = (MenuBarButton) this.container.getChildren().get(this.focusedMenuIndex);
            this.openMenuButton.setHover();
            this.openMenu = prevMenu;
        }
    }

    private void showNextMenu() {
        Menu nextMenu = findNextSibling();
        if (this.openMenu != null) {
            this.openMenu.hide();
        }
        this.openMenu = nextMenu;
        if (!isMenuEmpty(nextMenu)) {
            this.openMenu.show();
        }
    }

    private void showPrevMenu() {
        Menu prevMenu = findPreviousSibling();
        if (this.openMenu != null) {
            this.openMenu.hide();
        }
        this.openMenu = prevMenu;
        if (!isMenuEmpty(prevMenu)) {
            this.openMenu.show();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Menu findPreviousSibling() {
        if (this.focusedMenuIndex == -1) {
            return null;
        }
        if (this.focusedMenuIndex == 0) {
            this.focusedMenuIndex = this.container.getChildren().size() - 1;
        } else {
            this.focusedMenuIndex--;
        }
        if (((MenuBar) getSkinnable()).getMenus().get(this.focusedMenuIndex).isDisable()) {
            return findPreviousSibling();
        }
        clearMenuButtonHover();
        return ((MenuBar) getSkinnable()).getMenus().get(this.focusedMenuIndex);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Menu findNextSibling() {
        if (this.focusedMenuIndex == -1) {
            return null;
        }
        if (this.focusedMenuIndex == this.container.getChildren().size() - 1) {
            this.focusedMenuIndex = 0;
        } else {
            this.focusedMenuIndex++;
        }
        if (((MenuBar) getSkinnable()).getMenus().get(this.focusedMenuIndex).isDisable()) {
            return findNextSibling();
        }
        clearMenuButtonHover();
        return ((MenuBar) getSkinnable()).getMenus().get(this.focusedMenuIndex);
    }

    private void updateFocusedIndex() {
        int index = 0;
        for (Node n2 : this.container.getChildren()) {
            if (n2.isHover()) {
                this.focusedMenuIndex = index;
                return;
            }
            index++;
        }
        menuModeEnd();
    }

    private void clearMenuButtonHover() {
        for (Node n2 : this.container.getChildren()) {
            if (n2.isHover()) {
                ((MenuBarButton) n2).clearHover();
                return;
            }
        }
    }

    @Override // com.sun.javafx.scene.traversal.TraverseListener
    public void onTraverse(Node node, Bounds bounds) {
        if (this.openMenu != null) {
            this.openMenu.hide();
        }
        this.focusedMenuIndex = 0;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/MenuBarSkin$MenuBarButton.class */
    static class MenuBarButton extends MenuButton {
        private ChangeListener<Boolean> menuListener;
        private MenuBarSkin menuBarSkin;
        private Menu menu;
        private final ListChangeListener<MenuItem> itemsListener;
        private final ListChangeListener<String> styleClassListener;

        public MenuBarButton(MenuBarSkin menuBarSkin, Menu menu) {
            super(menu.getText(), menu.getGraphic());
            this.menuBarSkin = menuBarSkin;
            setAccessibleRole(AccessibleRole.MENU);
            ObservableList<MenuItem> items = menu.getItems();
            ListChangeListener<MenuItem> listChangeListener = c2 -> {
                while (c2.next()) {
                    getItems().removeAll(c2.getRemoved());
                    getItems().addAll(c2.getFrom(), c2.getAddedSubList());
                }
            };
            this.itemsListener = listChangeListener;
            items.addListener(listChangeListener);
            ObservableList<String> styleClass = menu.getStyleClass();
            ListChangeListener<String> listChangeListener2 = c3 -> {
                while (c3.next()) {
                    for (int i2 = c3.getFrom(); i2 < c3.getTo(); i2++) {
                        getStyleClass().add(menu.getStyleClass().get(i2));
                    }
                    for (String str : c3.getRemoved()) {
                        getStyleClass().remove(str);
                    }
                }
            };
            this.styleClassListener = listChangeListener2;
            styleClass.addListener(listChangeListener2);
            idProperty().bind(menu.idProperty());
        }

        public MenuBarSkin getMenuBarSkin() {
            return this.menuBarSkin;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void clearHover() {
            setHover(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public void setHover() {
            setHover(true);
            ((MenuBar) this.menuBarSkin.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE);
        }

        void dispose() {
            this.menu.getItems().removeListener(this.itemsListener);
            this.menu.getStyleClass().removeListener(this.styleClassListener);
            idProperty().unbind();
        }

        @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
        public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
            switch (attribute) {
                case FOCUS_ITEM:
                    return this;
                default:
                    return super.queryAccessibleAttribute(attribute, parameters);
            }
        }
    }

    @Override // javafx.scene.control.SkinBase
    protected double snappedTopInset() {
        if (this.container.getChildren().isEmpty()) {
            return 0.0d;
        }
        return super.snappedTopInset();
    }

    @Override // javafx.scene.control.SkinBase
    protected double snappedBottomInset() {
        if (this.container.getChildren().isEmpty()) {
            return 0.0d;
        }
        return super.snappedBottomInset();
    }

    @Override // javafx.scene.control.SkinBase
    protected double snappedLeftInset() {
        if (this.container.getChildren().isEmpty()) {
            return 0.0d;
        }
        return super.snappedLeftInset();
    }

    @Override // javafx.scene.control.SkinBase
    protected double snappedRightInset() {
        if (this.container.getChildren().isEmpty()) {
            return 0.0d;
        }
        return super.snappedRightInset();
    }

    @Override // javafx.scene.control.SkinBase
    protected void layoutChildren(double x2, double y2, double w2, double h2) {
        this.container.resizeRelocate(x2, y2, w2, h2);
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return this.container.minWidth(height) + snappedLeftInset() + snappedRightInset();
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return this.container.prefWidth(height) + snappedLeftInset() + snappedRightInset();
    }

    @Override // javafx.scene.control.SkinBase
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return this.container.minHeight(width) + snappedTopInset() + snappedBottomInset();
    }

    @Override // javafx.scene.control.SkinBase
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return this.container.prefHeight(width) + snappedTopInset() + snappedBottomInset();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.scene.control.SkinBase
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return ((MenuBar) getSkinnable()).prefHeight(-1.0d);
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return STYLEABLES;
    }

    @Override // javafx.scene.control.SkinBase
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.SkinBase
    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case FOCUS_NODE:
                return this.openMenuButton;
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
