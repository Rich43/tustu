package javafx.scene.layout;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.util.Callback;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/layout/FlowPane.class */
public class FlowPane extends Pane {
    private static final String MARGIN_CONSTRAINT = "flowpane-margin";
    private static final Callback<Node, Insets> marginAccessor = n2 -> {
        return getMargin(n2);
    };
    private ObjectProperty<Orientation> orientation;
    private DoubleProperty hgap;
    private DoubleProperty vgap;
    private DoubleProperty prefWrapLength;
    private ObjectProperty<Pos> alignment;
    private ObjectProperty<HPos> columnHalignment;
    private ObjectProperty<VPos> rowValignment;
    private List<Run> runs;
    private double lastMaxRunLength;
    boolean computingRuns;

    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN_CONSTRAINT, value);
    }

    public static Insets getMargin(Node child) {
        return (Insets) getConstraint(child, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node child) {
        setMargin(child, null);
    }

    public FlowPane() {
        this.runs = null;
        this.lastMaxRunLength = -1.0d;
        this.computingRuns = false;
    }

    public FlowPane(Orientation orientation) {
        this();
        setOrientation(orientation);
    }

    public FlowPane(double hgap, double vgap) {
        this();
        setHgap(hgap);
        setVgap(vgap);
    }

    public FlowPane(Orientation orientation, double hgap, double vgap) {
        this();
        setOrientation(orientation);
        setHgap(hgap);
        setVgap(vgap);
    }

    public FlowPane(Node... children) {
        this.runs = null;
        this.lastMaxRunLength = -1.0d;
        this.computingRuns = false;
        getChildren().addAll(children);
    }

    public FlowPane(Orientation orientation, Node... children) {
        this();
        setOrientation(orientation);
        getChildren().addAll(children);
    }

    public FlowPane(double hgap, double vgap, Node... children) {
        this();
        setHgap(hgap);
        setVgap(vgap);
        getChildren().addAll(children);
    }

    public FlowPane(Orientation orientation, double hgap, double vgap, Node... children) {
        this();
        setOrientation(orientation);
        setHgap(hgap);
        setVgap(vgap);
        getChildren().addAll(children);
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty(Orientation.HORIZONTAL) { // from class: javafx.scene.layout.FlowPane.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<FlowPane, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "orientation";
                }
            };
        }
        return this.orientation;
    }

    public final void setOrientation(Orientation value) {
        orientationProperty().set(value);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.HORIZONTAL : this.orientation.get();
    }

    public final DoubleProperty hgapProperty() {
        if (this.hgap == null) {
            this.hgap = new StyleableDoubleProperty() { // from class: javafx.scene.layout.FlowPane.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.HGAP;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "hgap";
                }
            };
        }
        return this.hgap;
    }

    public final void setHgap(double value) {
        hgapProperty().set(value);
    }

    public final double getHgap() {
        if (this.hgap == null) {
            return 0.0d;
        }
        return this.hgap.get();
    }

    public final DoubleProperty vgapProperty() {
        if (this.vgap == null) {
            this.vgap = new StyleableDoubleProperty() { // from class: javafx.scene.layout.FlowPane.3
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.VGAP;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "vgap";
                }
            };
        }
        return this.vgap;
    }

    public final void setVgap(double value) {
        vgapProperty().set(value);
    }

    public final double getVgap() {
        if (this.vgap == null) {
            return 0.0d;
        }
        return this.vgap.get();
    }

    public final DoubleProperty prefWrapLengthProperty() {
        if (this.prefWrapLength == null) {
            this.prefWrapLength = new DoublePropertyBase(400.0d) { // from class: javafx.scene.layout.FlowPane.4
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefWrapLength";
                }
            };
        }
        return this.prefWrapLength;
    }

    public final void setPrefWrapLength(double value) {
        prefWrapLengthProperty().set(value);
    }

    public final double getPrefWrapLength() {
        if (this.prefWrapLength == null) {
            return 400.0d;
        }
        return this.prefWrapLength.get();
    }

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT) { // from class: javafx.scene.layout.FlowPane.5
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<FlowPane, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "alignment";
                }
            };
        }
        return this.alignment;
    }

    public final void setAlignment(Pos value) {
        alignmentProperty().set(value);
    }

    public final Pos getAlignment() {
        return this.alignment == null ? Pos.TOP_LEFT : this.alignment.get();
    }

    private Pos getAlignmentInternal() {
        Pos localPos = getAlignment();
        return localPos == null ? Pos.TOP_LEFT : localPos;
    }

    public final ObjectProperty<HPos> columnHalignmentProperty() {
        if (this.columnHalignment == null) {
            this.columnHalignment = new StyleableObjectProperty<HPos>(HPos.LEFT) { // from class: javafx.scene.layout.FlowPane.6
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<FlowPane, HPos> getCssMetaData() {
                    return StyleableProperties.COLUMN_HALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "columnHalignment";
                }
            };
        }
        return this.columnHalignment;
    }

    public final void setColumnHalignment(HPos value) {
        columnHalignmentProperty().set(value);
    }

    public final HPos getColumnHalignment() {
        return this.columnHalignment == null ? HPos.LEFT : this.columnHalignment.get();
    }

    private HPos getColumnHalignmentInternal() {
        HPos localPos = getColumnHalignment();
        return localPos == null ? HPos.LEFT : localPos;
    }

    public final ObjectProperty<VPos> rowValignmentProperty() {
        if (this.rowValignment == null) {
            this.rowValignment = new StyleableObjectProperty<VPos>(VPos.CENTER) { // from class: javafx.scene.layout.FlowPane.7
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    FlowPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<FlowPane, VPos> getCssMetaData() {
                    return StyleableProperties.ROW_VALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return FlowPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "rowValignment";
                }
            };
        }
        return this.rowValignment;
    }

    public final void setRowValignment(VPos value) {
        rowValignmentProperty().set(value);
    }

    public final VPos getRowValignment() {
        return this.rowValignment == null ? VPos.CENTER : this.rowValignment.get();
    }

    private VPos getRowValignmentInternal() {
        VPos localPos = getRowValignment();
        return localPos == null ? VPos.CENTER : localPos;
    }

    @Override // javafx.scene.Node
    public Orientation getContentBias() {
        return getOrientation();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinWidth(double height) {
        if (getContentBias() == Orientation.HORIZONTAL) {
            double maxPref = 0.0d;
            List<Node> children = getChildren();
            int size = children.size();
            for (int i2 = 0; i2 < size; i2++) {
                Node child = children.get(i2);
                if (child.isManaged()) {
                    maxPref = Math.max(maxPref, child.prefWidth(-1.0d));
                }
            }
            Insets insets = getInsets();
            return insets.getLeft() + snapSize(maxPref) + insets.getRight();
        }
        return computePrefWidth(height);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        if (getContentBias() == Orientation.VERTICAL) {
            double maxPref = 0.0d;
            List<Node> children = getChildren();
            int size = children.size();
            for (int i2 = 0; i2 < size; i2++) {
                Node child = children.get(i2);
                if (child.isManaged()) {
                    maxPref = Math.max(maxPref, child.prefHeight(-1.0d));
                }
            }
            Insets insets = getInsets();
            return insets.getTop() + snapSize(maxPref) + insets.getBottom();
        }
        return computePrefHeight(width);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double forHeight) {
        Insets insets = getInsets();
        if (getOrientation() == Orientation.HORIZONTAL) {
            double maxRunWidth = getPrefWrapLength();
            List<Run> hruns = getRuns(maxRunWidth);
            double w2 = computeContentWidth(hruns);
            return insets.getLeft() + snapSize(getPrefWrapLength() > w2 ? getPrefWrapLength() : w2) + insets.getRight();
        }
        double maxRunHeight = forHeight != -1.0d ? (forHeight - insets.getTop()) - insets.getBottom() : getPrefWrapLength();
        List<Run> vruns = getRuns(maxRunHeight);
        return insets.getLeft() + computeContentWidth(vruns) + insets.getRight();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double forWidth) {
        Insets insets = getInsets();
        if (getOrientation() == Orientation.HORIZONTAL) {
            double maxRunWidth = forWidth != -1.0d ? (forWidth - insets.getLeft()) - insets.getRight() : getPrefWrapLength();
            List<Run> hruns = getRuns(maxRunWidth);
            return insets.getTop() + computeContentHeight(hruns) + insets.getBottom();
        }
        double maxRunHeight = getPrefWrapLength();
        List<Run> vruns = getRuns(maxRunHeight);
        double h2 = computeContentHeight(vruns);
        return insets.getTop() + snapSize(getPrefWrapLength() > h2 ? getPrefWrapLength() : h2) + insets.getBottom();
    }

    @Override // javafx.scene.Parent
    public void requestLayout() {
        if (!this.computingRuns) {
            this.runs = null;
        }
        super.requestLayout();
    }

    private List<Run> getRuns(double maxRunLength) {
        if (this.runs == null || maxRunLength != this.lastMaxRunLength) {
            this.computingRuns = true;
            this.lastMaxRunLength = maxRunLength;
            this.runs = new ArrayList();
            double runLength = 0.0d;
            double runOffset = 0.0d;
            Run run = new Run();
            double vgap = snapSpace(getVgap());
            double hgap = snapSpace(getHgap());
            List<Node> children = getChildren();
            int size = children.size();
            for (int i2 = 0; i2 < size; i2++) {
                Node child = children.get(i2);
                if (child.isManaged()) {
                    LayoutRect nodeRect = new LayoutRect();
                    nodeRect.node = child;
                    Insets margin = getMargin(child);
                    nodeRect.width = computeChildPrefAreaWidth(child, margin);
                    nodeRect.height = computeChildPrefAreaHeight(child, margin);
                    double nodeLength = getOrientation() == Orientation.HORIZONTAL ? nodeRect.width : nodeRect.height;
                    if (runLength + nodeLength > maxRunLength && runLength > 0.0d) {
                        normalizeRun(run, runOffset);
                        if (getOrientation() == Orientation.HORIZONTAL) {
                            runOffset += run.height + vgap;
                        } else {
                            runOffset += run.width + hgap;
                        }
                        this.runs.add(run);
                        runLength = 0.0d;
                        run = new Run();
                    }
                    if (getOrientation() == Orientation.HORIZONTAL) {
                        nodeRect.f12705x = runLength;
                        runLength += nodeRect.width + hgap;
                    } else {
                        nodeRect.f12706y = runLength;
                        runLength += nodeRect.height + vgap;
                    }
                    run.rects.add(nodeRect);
                }
            }
            normalizeRun(run, runOffset);
            this.runs.add(run);
            this.computingRuns = false;
        }
        return this.runs;
    }

    private void normalizeRun(Run run, double runOffset) {
        if (getOrientation() == Orientation.HORIZONTAL) {
            ArrayList<Node> rownodes = new ArrayList<>();
            run.width = (run.rects.size() - 1) * snapSpace(getHgap());
            int max = run.rects.size();
            for (int i2 = 0; i2 < max; i2++) {
                LayoutRect lrect = run.rects.get(i2);
                rownodes.add(lrect.node);
                run.width += lrect.width;
                lrect.f12706y = runOffset;
            }
            run.height = computeMaxPrefAreaHeight(rownodes, marginAccessor, getRowValignment());
            run.baselineOffset = getRowValignment() == VPos.BASELINE ? getAreaBaselineOffset(rownodes, marginAccessor, i3 -> {
                return Double.valueOf(run.rects.get(i3.intValue()).width);
            }, run.height, true) : 0.0d;
            return;
        }
        run.height = (run.rects.size() - 1) * snapSpace(getVgap());
        double maxw = 0.0d;
        int max2 = run.rects.size();
        for (int i4 = 0; i4 < max2; i4++) {
            LayoutRect lrect2 = run.rects.get(i4);
            run.height += lrect2.height;
            lrect2.f12705x = runOffset;
            maxw = Math.max(maxw, lrect2.width);
        }
        run.width = maxw;
        run.baselineOffset = run.height;
    }

    private double computeContentWidth(List<Run> runs) {
        double dMax;
        double cwidth = getOrientation() == Orientation.HORIZONTAL ? 0.0d : (runs.size() - 1) * snapSpace(getHgap());
        int max = runs.size();
        for (int i2 = 0; i2 < max; i2++) {
            Run run = runs.get(i2);
            if (getOrientation() == Orientation.HORIZONTAL) {
                dMax = Math.max(cwidth, run.width);
            } else {
                dMax = cwidth + run.width;
            }
            cwidth = dMax;
        }
        return cwidth;
    }

    private double computeContentHeight(List<Run> runs) {
        double dMax;
        double cheight = getOrientation() == Orientation.VERTICAL ? 0.0d : (runs.size() - 1) * snapSpace(getVgap());
        int max = runs.size();
        for (int i2 = 0; i2 < max; i2++) {
            Run run = runs.get(i2);
            if (getOrientation() == Orientation.VERTICAL) {
                dMax = Math.max(cheight, run.height);
            } else {
                dMax = cheight + run.height;
            }
            cheight = dMax;
        }
        return cheight;
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        Insets insets = getInsets();
        double width = getWidth();
        double height = getHeight();
        double top = insets.getTop();
        double left = insets.getLeft();
        double bottom = insets.getBottom();
        double right = insets.getRight();
        double insideWidth = (width - left) - right;
        double insideHeight = (height - top) - bottom;
        List<Run> runs = getRuns(getOrientation() == Orientation.HORIZONTAL ? insideWidth : insideHeight);
        int max = runs.size();
        for (int i2 = 0; i2 < max; i2++) {
            Run run = runs.get(i2);
            double xoffset = left + computeXOffset(insideWidth, getOrientation() == Orientation.HORIZONTAL ? run.width : computeContentWidth(runs), getAlignmentInternal().getHpos());
            double yoffset = top + computeYOffset(insideHeight, getOrientation() == Orientation.VERTICAL ? run.height : computeContentHeight(runs), getAlignmentInternal().getVpos());
            for (int j2 = 0; j2 < run.rects.size(); j2++) {
                LayoutRect lrect = run.rects.get(j2);
                double x2 = xoffset + lrect.f12705x;
                double y2 = yoffset + lrect.f12706y;
                layoutInArea(lrect.node, x2, y2, getOrientation() == Orientation.HORIZONTAL ? lrect.width : run.width, getOrientation() == Orientation.VERTICAL ? lrect.height : run.height, run.baselineOffset, getMargin(lrect.node), getColumnHalignmentInternal(), getRowValignmentInternal());
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/FlowPane$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<FlowPane, Pos> ALIGNMENT = new CssMetaData<FlowPane, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) { // from class: javafx.scene.layout.FlowPane.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(FlowPane node) {
                return node.alignment == null || !node.alignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(FlowPane node) {
                return (StyleableProperty) node.alignmentProperty();
            }
        };
        private static final CssMetaData<FlowPane, HPos> COLUMN_HALIGNMENT = new CssMetaData<FlowPane, HPos>("-fx-column-halignment", new EnumConverter(HPos.class), HPos.LEFT) { // from class: javafx.scene.layout.FlowPane.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(FlowPane node) {
                return node.columnHalignment == null || !node.columnHalignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<HPos> getStyleableProperty(FlowPane node) {
                return (StyleableProperty) node.columnHalignmentProperty();
            }
        };
        private static final CssMetaData<FlowPane, Number> HGAP = new CssMetaData<FlowPane, Number>("-fx-hgap", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.layout.FlowPane.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(FlowPane node) {
                return node.hgap == null || !node.hgap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(FlowPane node) {
                return (StyleableProperty) node.hgapProperty();
            }
        };
        private static final CssMetaData<FlowPane, VPos> ROW_VALIGNMENT = new CssMetaData<FlowPane, VPos>("-fx-row-valignment", new EnumConverter(VPos.class), VPos.CENTER) { // from class: javafx.scene.layout.FlowPane.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(FlowPane node) {
                return node.rowValignment == null || !node.rowValignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<VPos> getStyleableProperty(FlowPane node) {
                return (StyleableProperty) node.rowValignmentProperty();
            }
        };
        private static final CssMetaData<FlowPane, Orientation> ORIENTATION = new CssMetaData<FlowPane, Orientation>("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) { // from class: javafx.scene.layout.FlowPane.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public Orientation getInitialValue(FlowPane node) {
                return node.getOrientation();
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(FlowPane node) {
                return node.orientation == null || !node.orientation.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Orientation> getStyleableProperty(FlowPane node) {
                return (StyleableProperty) node.orientationProperty();
            }
        };
        private static final CssMetaData<FlowPane, Number> VGAP = new CssMetaData<FlowPane, Number>("-fx-vgap", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.layout.FlowPane.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(FlowPane node) {
                return node.vgap == null || !node.vgap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(FlowPane node) {
                return (StyleableProperty) node.vgapProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(ALIGNMENT);
            styleables.add(COLUMN_HALIGNMENT);
            styleables.add(HGAP);
            styleables.add(ROW_VALIGNMENT);
            styleables.add(ORIENTATION);
            styleables.add(VGAP);
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

    /* loaded from: jfxrt.jar:javafx/scene/layout/FlowPane$LayoutRect.class */
    private static class LayoutRect {
        public Node node;

        /* renamed from: x, reason: collision with root package name */
        double f12705x;

        /* renamed from: y, reason: collision with root package name */
        double f12706y;
        double width;
        double height;

        private LayoutRect() {
        }

        public String toString() {
            return "LayoutRect node id=" + this.node.getId() + " " + this.f12705x + "," + this.f12706y + " " + this.width + LanguageTag.PRIVATEUSE + this.height;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/FlowPane$Run.class */
    private static class Run {
        ArrayList<LayoutRect> rects;
        double width;
        double height;
        double baselineOffset;

        private Run() {
            this.rects = new ArrayList<>();
        }
    }
}
