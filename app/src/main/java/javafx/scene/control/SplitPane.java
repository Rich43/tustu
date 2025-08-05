package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.SplitPaneSkin;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import javafx.beans.DefaultProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;

@DefaultProperty("items")
/* loaded from: jfxrt.jar:javafx/scene/control/SplitPane.class */
public class SplitPane extends Control {
    private static final String RESIZABLE_WITH_PARENT = "resizable-with-parent";
    private ObjectProperty<Orientation> orientation;
    private final ObservableList<Node> items;
    private final ObservableList<Divider> dividers;
    private final ObservableList<Divider> unmodifiableDividers;
    private final WeakHashMap<Integer, Double> dividerCache;
    private static final String DEFAULT_STYLE_CLASS = "split-pane";
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public static void setResizableWithParent(Node node, Boolean value) {
        if (value == null) {
            node.getProperties().remove(RESIZABLE_WITH_PARENT);
        } else {
            node.getProperties().put(RESIZABLE_WITH_PARENT, value);
        }
    }

    public static Boolean isResizableWithParent(Node node) {
        Object value;
        if (node.hasProperties() && (value = node.getProperties().get(RESIZABLE_WITH_PARENT)) != null) {
            return (Boolean) value;
        }
        return true;
    }

    public SplitPane() {
        this((Node[]) null);
    }

    public SplitPane(Node... items) {
        this.items = FXCollections.observableArrayList();
        this.dividers = FXCollections.observableArrayList();
        this.unmodifiableDividers = FXCollections.unmodifiableObservableList(this.dividers);
        this.dividerCache = new WeakHashMap<>();
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
        getItems().addListener(new ListChangeListener<Node>() { // from class: javafx.scene.control.SplitPane.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends Node> c2) {
                while (c2.next()) {
                    int from = c2.getFrom();
                    int index = from;
                    for (int i2 = 0; i2 < c2.getRemovedSize(); i2++) {
                        if (index < SplitPane.this.dividers.size()) {
                            SplitPane.this.dividerCache.put(Integer.valueOf(index), Double.valueOf(Double.MAX_VALUE));
                        } else if (index == SplitPane.this.dividers.size() && !SplitPane.this.dividers.isEmpty()) {
                            if (c2.wasReplaced()) {
                                SplitPane.this.dividerCache.put(Integer.valueOf(index - 1), Double.valueOf(((Divider) SplitPane.this.dividers.get(index - 1)).getPosition()));
                            } else {
                                SplitPane.this.dividerCache.put(Integer.valueOf(index - 1), Double.valueOf(Double.MAX_VALUE));
                            }
                        }
                        index++;
                    }
                    for (int i3 = 0; i3 < SplitPane.this.dividers.size(); i3++) {
                        if (SplitPane.this.dividerCache.get(Integer.valueOf(i3)) == null) {
                            SplitPane.this.dividerCache.put(Integer.valueOf(i3), Double.valueOf(((Divider) SplitPane.this.dividers.get(i3)).getPosition()));
                        }
                    }
                }
                SplitPane.this.dividers.clear();
                for (int i4 = 0; i4 < SplitPane.this.getItems().size() - 1; i4++) {
                    if (!SplitPane.this.dividerCache.containsKey(Integer.valueOf(i4)) || ((Double) SplitPane.this.dividerCache.get(Integer.valueOf(i4))).doubleValue() == Double.MAX_VALUE) {
                        SplitPane.this.dividers.add(new Divider());
                    } else {
                        Divider d2 = new Divider();
                        d2.setPosition(((Double) SplitPane.this.dividerCache.get(Integer.valueOf(i4))).doubleValue());
                        SplitPane.this.dividers.add(d2);
                    }
                    SplitPane.this.dividerCache.remove(Integer.valueOf(i4));
                }
            }
        });
        if (items != null) {
            getItems().addAll(items);
        }
        pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
    }

    public final void setOrientation(Orientation value) {
        orientationProperty().set(value);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.HORIZONTAL : this.orientation.get();
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL) { // from class: javafx.scene.control.SplitPane.2
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    boolean isVertical = get() == Orientation.VERTICAL;
                    SplitPane.this.pseudoClassStateChanged(SplitPane.VERTICAL_PSEUDOCLASS_STATE, isVertical);
                    SplitPane.this.pseudoClassStateChanged(SplitPane.HORIZONTAL_PSEUDOCLASS_STATE, !isVertical);
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<SplitPane, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return SplitPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "orientation";
                }
            };
        }
        return this.orientation;
    }

    public ObservableList<Node> getItems() {
        return this.items;
    }

    public ObservableList<Divider> getDividers() {
        return this.unmodifiableDividers;
    }

    public void setDividerPosition(int dividerIndex, double position) {
        if (getDividers().size() <= dividerIndex) {
            this.dividerCache.put(Integer.valueOf(dividerIndex), Double.valueOf(position));
        } else if (dividerIndex >= 0) {
            getDividers().get(dividerIndex).setPosition(position);
        }
    }

    public void setDividerPositions(double... positions) {
        if (this.dividers.isEmpty()) {
            for (int i2 = 0; i2 < positions.length; i2++) {
                this.dividerCache.put(Integer.valueOf(i2), Double.valueOf(positions[i2]));
            }
            return;
        }
        for (int i3 = 0; i3 < positions.length && i3 < this.dividers.size(); i3++) {
            this.dividers.get(i3).setPosition(positions[i3]);
        }
    }

    public double[] getDividerPositions() {
        double[] positions = new double[this.dividers.size()];
        for (int i2 = 0; i2 < this.dividers.size(); i2++) {
            positions[i2] = this.dividers.get(i2).getPosition();
        }
        return positions;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new SplitPaneSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/SplitPane$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<SplitPane, Orientation> ORIENTATION = new CssMetaData<SplitPane, Orientation>("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) { // from class: javafx.scene.control.SplitPane.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public Orientation getInitialValue(SplitPane node) {
                return node.getOrientation();
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(SplitPane n2) {
                return n2.orientation == null || !n2.orientation.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Orientation> getStyleableProperty(SplitPane n2) {
                return (StyleableProperty) n2.orientationProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(ORIENTATION);
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

    @Override // javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/SplitPane$Divider.class */
    public static class Divider {
        private DoubleProperty position;

        public final void setPosition(double value) {
            positionProperty().set(value);
        }

        public final double getPosition() {
            if (this.position == null) {
                return 0.5d;
            }
            return this.position.get();
        }

        public final DoubleProperty positionProperty() {
            if (this.position == null) {
                this.position = new SimpleDoubleProperty(this, Keywords.FUNC_POSITION_STRING, 0.5d);
            }
            return this.position;
        }
    }
}
