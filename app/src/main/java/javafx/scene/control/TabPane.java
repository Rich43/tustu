package javafx.scene.control;

import com.sun.javafx.collections.UnmodifiableListSet;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.TabPaneSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Side;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javax.swing.JSplitPane;

@DefaultProperty("tabs")
/* loaded from: jfxrt.jar:javafx/scene/control/TabPane.class */
public class TabPane extends Control {
    private static final double DEFAULT_TAB_MIN_WIDTH = 0.0d;
    private static final double DEFAULT_TAB_MAX_WIDTH = Double.MAX_VALUE;
    private static final double DEFAULT_TAB_MIN_HEIGHT = 0.0d;
    private static final double DEFAULT_TAB_MAX_HEIGHT = Double.MAX_VALUE;
    public static final String STYLE_CLASS_FLOATING = "floating";
    private ObservableList<Tab> tabs;
    private ObjectProperty<SingleSelectionModel<Tab>> selectionModel;
    private ObjectProperty<Side> side;
    private ObjectProperty<TabClosingPolicy> tabClosingPolicy;
    private BooleanProperty rotateGraphic;
    private DoubleProperty tabMinWidth;
    private DoubleProperty tabMaxWidth;
    private DoubleProperty tabMinHeight;
    private DoubleProperty tabMaxHeight;
    private static final PseudoClass TOP_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.TOP);
    private static final PseudoClass BOTTOM_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.BOTTOM);
    private static final PseudoClass LEFT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.LEFT);
    private static final PseudoClass RIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass(JSplitPane.RIGHT);

    /* loaded from: jfxrt.jar:javafx/scene/control/TabPane$TabClosingPolicy.class */
    public enum TabClosingPolicy {
        SELECTED_TAB,
        ALL_TABS,
        UNAVAILABLE
    }

    public TabPane() {
        this((Tab[]) null);
    }

    public TabPane(Tab... tabs) {
        this.tabs = FXCollections.observableArrayList();
        this.selectionModel = new SimpleObjectProperty(this, "selectionModel");
        getStyleClass().setAll("tab-pane");
        setAccessibleRole(AccessibleRole.TAB_PANE);
        setSelectionModel(new TabPaneSelectionModel(this));
        this.tabs.addListener(c2 -> {
            while (c2.next()) {
                for (Tab tab : c2.getRemoved()) {
                    if (tab != null && !getTabs().contains(tab)) {
                        tab.setTabPane(null);
                    }
                }
                for (Tab tab2 : c2.getAddedSubList()) {
                    if (tab2 != null) {
                        tab2.setTabPane(this);
                    }
                }
            }
        });
        if (tabs != null) {
            getTabs().addAll(tabs);
        }
        Side edge = getSide();
        pseudoClassStateChanged(TOP_PSEUDOCLASS_STATE, edge == Side.TOP);
        pseudoClassStateChanged(RIGHT_PSEUDOCLASS_STATE, edge == Side.RIGHT);
        pseudoClassStateChanged(BOTTOM_PSEUDOCLASS_STATE, edge == Side.BOTTOM);
        pseudoClassStateChanged(LEFT_PSEUDOCLASS_STATE, edge == Side.LEFT);
    }

    public final ObservableList<Tab> getTabs() {
        return this.tabs;
    }

    public final void setSelectionModel(SingleSelectionModel<Tab> value) {
        this.selectionModel.set(value);
    }

    public final SingleSelectionModel<Tab> getSelectionModel() {
        return this.selectionModel.get();
    }

    public final ObjectProperty<SingleSelectionModel<Tab>> selectionModelProperty() {
        return this.selectionModel;
    }

    public final void setSide(Side value) {
        sideProperty().set(value);
    }

    public final Side getSide() {
        return this.side == null ? Side.TOP : this.side.get();
    }

    public final ObjectProperty<Side> sideProperty() {
        if (this.side == null) {
            this.side = new ObjectPropertyBase<Side>(Side.TOP) { // from class: javafx.scene.control.TabPane.1
                private Side oldSide;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    this.oldSide = get();
                    TabPane.this.pseudoClassStateChanged(TabPane.TOP_PSEUDOCLASS_STATE, this.oldSide == Side.TOP || this.oldSide == null);
                    TabPane.this.pseudoClassStateChanged(TabPane.RIGHT_PSEUDOCLASS_STATE, this.oldSide == Side.RIGHT);
                    TabPane.this.pseudoClassStateChanged(TabPane.BOTTOM_PSEUDOCLASS_STATE, this.oldSide == Side.BOTTOM);
                    TabPane.this.pseudoClassStateChanged(TabPane.LEFT_PSEUDOCLASS_STATE, this.oldSide == Side.LEFT);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TabPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "side";
                }
            };
        }
        return this.side;
    }

    public final void setTabClosingPolicy(TabClosingPolicy value) {
        tabClosingPolicyProperty().set(value);
    }

    public final TabClosingPolicy getTabClosingPolicy() {
        return this.tabClosingPolicy == null ? TabClosingPolicy.SELECTED_TAB : this.tabClosingPolicy.get();
    }

    public final ObjectProperty<TabClosingPolicy> tabClosingPolicyProperty() {
        if (this.tabClosingPolicy == null) {
            this.tabClosingPolicy = new SimpleObjectProperty(this, "tabClosingPolicy", TabClosingPolicy.SELECTED_TAB);
        }
        return this.tabClosingPolicy;
    }

    public final void setRotateGraphic(boolean value) {
        rotateGraphicProperty().set(value);
    }

    public final boolean isRotateGraphic() {
        if (this.rotateGraphic == null) {
            return false;
        }
        return this.rotateGraphic.get();
    }

    public final BooleanProperty rotateGraphicProperty() {
        if (this.rotateGraphic == null) {
            this.rotateGraphic = new SimpleBooleanProperty(this, "rotateGraphic", false);
        }
        return this.rotateGraphic;
    }

    public final void setTabMinWidth(double value) {
        tabMinWidthProperty().setValue((Number) Double.valueOf(value));
    }

    public final double getTabMinWidth() {
        if (this.tabMinWidth == null) {
            return 0.0d;
        }
        return this.tabMinWidth.getValue2().doubleValue();
    }

    public final DoubleProperty tabMinWidthProperty() {
        if (this.tabMinWidth == null) {
            this.tabMinWidth = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.control.TabPane.2
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.TAB_MIN_WIDTH;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TabPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tabMinWidth";
                }
            };
        }
        return this.tabMinWidth;
    }

    public final void setTabMaxWidth(double value) {
        tabMaxWidthProperty().setValue((Number) Double.valueOf(value));
    }

    public final double getTabMaxWidth() {
        if (this.tabMaxWidth == null) {
            return Double.MAX_VALUE;
        }
        return this.tabMaxWidth.getValue2().doubleValue();
    }

    public final DoubleProperty tabMaxWidthProperty() {
        if (this.tabMaxWidth == null) {
            this.tabMaxWidth = new StyleableDoubleProperty(Double.MAX_VALUE) { // from class: javafx.scene.control.TabPane.3
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.TAB_MAX_WIDTH;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TabPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tabMaxWidth";
                }
            };
        }
        return this.tabMaxWidth;
    }

    public final void setTabMinHeight(double value) {
        tabMinHeightProperty().setValue((Number) Double.valueOf(value));
    }

    public final double getTabMinHeight() {
        if (this.tabMinHeight == null) {
            return 0.0d;
        }
        return this.tabMinHeight.getValue2().doubleValue();
    }

    public final DoubleProperty tabMinHeightProperty() {
        if (this.tabMinHeight == null) {
            this.tabMinHeight = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.control.TabPane.4
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.TAB_MIN_HEIGHT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TabPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tabMinHeight";
                }
            };
        }
        return this.tabMinHeight;
    }

    public final void setTabMaxHeight(double value) {
        tabMaxHeightProperty().setValue((Number) Double.valueOf(value));
    }

    public final double getTabMaxHeight() {
        if (this.tabMaxHeight == null) {
            return Double.MAX_VALUE;
        }
        return this.tabMaxHeight.getValue2().doubleValue();
    }

    public final DoubleProperty tabMaxHeightProperty() {
        if (this.tabMaxHeight == null) {
            this.tabMaxHeight = new StyleableDoubleProperty(Double.MAX_VALUE) { // from class: javafx.scene.control.TabPane.5
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.TAB_MAX_HEIGHT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TabPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tabMaxHeight";
                }
            };
        }
        return this.tabMaxHeight;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TabPaneSkin(this);
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public Node lookup(String selector) {
        Node n2 = super.lookup(selector);
        if (n2 == null) {
            for (Tab tab : this.tabs) {
                n2 = tab.lookup(selector);
                if (n2 != null) {
                    break;
                }
            }
        }
        return n2;
    }

    @Override // javafx.scene.Node
    public Set<Node> lookupAll(String selector) {
        if (selector == null) {
            return null;
        }
        List<Node> results = new ArrayList<>();
        results.addAll(super.lookupAll(selector));
        for (Tab tab : this.tabs) {
            results.addAll(tab.lookupAll(selector));
        }
        return new UnmodifiableListSet(results);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TabPane$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TabPane, Number> TAB_MIN_WIDTH = new CssMetaData<TabPane, Number>("-fx-tab-min-width", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.control.TabPane.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TabPane n2) {
                return n2.tabMinWidth == null || !n2.tabMinWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TabPane n2) {
                return (StyleableProperty) n2.tabMinWidthProperty();
            }
        };
        private static final CssMetaData<TabPane, Number> TAB_MAX_WIDTH = new CssMetaData<TabPane, Number>("-fx-tab-max-width", SizeConverter.getInstance(), Double.valueOf(Double.MAX_VALUE)) { // from class: javafx.scene.control.TabPane.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TabPane n2) {
                return n2.tabMaxWidth == null || !n2.tabMaxWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TabPane n2) {
                return (StyleableProperty) n2.tabMaxWidthProperty();
            }
        };
        private static final CssMetaData<TabPane, Number> TAB_MIN_HEIGHT = new CssMetaData<TabPane, Number>("-fx-tab-min-height", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.control.TabPane.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TabPane n2) {
                return n2.tabMinHeight == null || !n2.tabMinHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TabPane n2) {
                return (StyleableProperty) n2.tabMinHeightProperty();
            }
        };
        private static final CssMetaData<TabPane, Number> TAB_MAX_HEIGHT = new CssMetaData<TabPane, Number>("-fx-tab-max-height", SizeConverter.getInstance(), Double.valueOf(Double.MAX_VALUE)) { // from class: javafx.scene.control.TabPane.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TabPane n2) {
                return n2.tabMaxHeight == null || !n2.tabMaxHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TabPane n2) {
                return (StyleableProperty) n2.tabMaxHeightProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(TAB_MIN_WIDTH);
            styleables.add(TAB_MAX_WIDTH);
            styleables.add(TAB_MIN_HEIGHT);
            styleables.add(TAB_MAX_HEIGHT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TabPane$TabPaneSelectionModel.class */
    static class TabPaneSelectionModel extends SingleSelectionModel<Tab> {
        private final TabPane tabPane;

        public TabPaneSelectionModel(TabPane t2) {
            if (t2 == null) {
                throw new NullPointerException("TabPane can not be null");
            }
            this.tabPane = t2;
            ListChangeListener<Tab> itemsContentObserver = c2 -> {
                while (c2.next()) {
                    for (Tab tab : c2.getRemoved()) {
                        if (tab != null && !this.tabPane.getTabs().contains(tab) && tab.isSelected()) {
                            tab.setSelected(false);
                            int tabIndex = c2.getFrom();
                            findNearestAvailableTab(tabIndex, true);
                        }
                    }
                    if (c2.wasAdded() || c2.wasRemoved()) {
                        if (getSelectedIndex() != this.tabPane.getTabs().indexOf(getSelectedItem())) {
                            clearAndSelect(this.tabPane.getTabs().indexOf(getSelectedItem()));
                        }
                    }
                }
                if (getSelectedIndex() == -1 && getSelectedItem() == null && this.tabPane.getTabs().size() > 0) {
                    findNearestAvailableTab(0, true);
                } else if (this.tabPane.getTabs().isEmpty()) {
                    clearSelection();
                }
            };
            if (this.tabPane.getTabs() != null) {
                this.tabPane.getTabs().addListener(itemsContentObserver);
            }
        }

        @Override // javafx.scene.control.SingleSelectionModel, javafx.scene.control.SelectionModel
        public void select(int index) {
            if (index >= 0) {
                if (getItemCount() <= 0 || index < getItemCount()) {
                    if (index == getSelectedIndex() && getModelItem(index).isSelected()) {
                        return;
                    }
                    if (getSelectedIndex() >= 0 && getSelectedIndex() < this.tabPane.getTabs().size()) {
                        this.tabPane.getTabs().get(getSelectedIndex()).setSelected(false);
                    }
                    setSelectedIndex(index);
                    Tab tab = getModelItem(index);
                    if (tab != null) {
                        setSelectedItem(tab);
                    }
                    if (getSelectedIndex() >= 0 && getSelectedIndex() < this.tabPane.getTabs().size()) {
                        this.tabPane.getTabs().get(getSelectedIndex()).setSelected(true);
                    }
                    this.tabPane.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
                }
            }
        }

        @Override // javafx.scene.control.SingleSelectionModel, javafx.scene.control.SelectionModel
        public void select(Tab tab) {
            int itemCount = getItemCount();
            for (int i2 = 0; i2 < itemCount; i2++) {
                Tab value = getModelItem(i2);
                if (value != null && value.equals(tab)) {
                    select(i2);
                    return;
                }
            }
            if (tab != null) {
                setSelectedItem(tab);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.scene.control.SingleSelectionModel
        public Tab getModelItem(int index) {
            ObservableList<Tab> items = this.tabPane.getTabs();
            if (items != null && index >= 0 && index < items.size()) {
                return items.get(index);
            }
            return null;
        }

        @Override // javafx.scene.control.SingleSelectionModel
        protected int getItemCount() {
            ObservableList<Tab> items = this.tabPane.getTabs();
            if (items == null) {
                return 0;
            }
            return items.size();
        }

        private Tab findNearestAvailableTab(int tabIndex, boolean doSelect) {
            Tab _tab;
            Tab _tab2;
            int tabCount = getItemCount();
            int i2 = 1;
            Tab bestTab = null;
            while (true) {
                int downPos = tabIndex - i2;
                if (downPos >= 0 && (_tab2 = getModelItem(downPos)) != null && !_tab2.isDisable()) {
                    bestTab = _tab2;
                    break;
                }
                int upPos = (tabIndex + i2) - 1;
                if (upPos < tabCount && (_tab = getModelItem(upPos)) != null && !_tab.isDisable()) {
                    bestTab = _tab;
                    break;
                }
                if (downPos < 0 && upPos >= tabCount) {
                    break;
                }
                i2++;
            }
            if (doSelect && bestTab != null) {
                select(bestTab);
            }
            return bestTab;
        }
    }
}
