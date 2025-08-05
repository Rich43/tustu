package javafx.scene.layout;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/layout/GridPane.class */
public class GridPane extends Pane {
    public static final int REMAINING = Integer.MAX_VALUE;
    private static final String MARGIN_CONSTRAINT = "gridpane-margin";
    private static final String HALIGNMENT_CONSTRAINT = "gridpane-halignment";
    private static final String VALIGNMENT_CONSTRAINT = "gridpane-valignment";
    private static final String HGROW_CONSTRAINT = "gridpane-hgrow";
    private static final String VGROW_CONSTRAINT = "gridpane-vgrow";
    private static final String ROW_INDEX_CONSTRAINT = "gridpane-row";
    private static final String COLUMN_INDEX_CONSTRAINT = "gridpane-column";
    private static final String ROW_SPAN_CONSTRAINT = "gridpane-row-span";
    private static final String COLUMN_SPAN_CONSTRAINT = "gridpane-column-span";
    private static final String FILL_WIDTH_CONSTRAINT = "gridpane-fill-width";
    private static final String FILL_HEIGHT_CONSTRAINT = "gridpane-fill-height";
    private static final Callback<Node, Insets> marginAccessor;
    private static final Color GRID_LINE_COLOR;
    private static final double GRID_LINE_DASH = 3.0d;
    private DoubleProperty hgap;
    private DoubleProperty vgap;
    private ObjectProperty<Pos> alignment;
    private BooleanProperty gridLinesVisible;
    private Group gridLines;
    private Orientation bias;
    private double[] rowPercentHeight;
    private CompositeSize rowMinHeight;
    private CompositeSize rowPrefHeight;
    private CompositeSize rowMaxHeight;
    private List<Node>[] rowBaseline;
    private double[] rowMinBaselineComplement;
    private double[] rowPrefBaselineComplement;
    private double[] rowMaxBaselineComplement;
    private Priority[] rowGrow;
    private double[] columnPercentWidth;
    private CompositeSize columnMinWidth;
    private CompositeSize columnPrefWidth;
    private CompositeSize columnMaxWidth;
    private Priority[] columnGrow;
    private int numRows;
    private int numColumns;
    private CompositeSize currentHeights;
    private CompositeSize currentWidths;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final ObservableList<RowConstraints> rowConstraints = new TrackableObservableList<RowConstraints>() { // from class: javafx.scene.layout.GridPane.5
        @Override // com.sun.javafx.collections.TrackableObservableList
        protected void onChanged(ListChangeListener.Change<RowConstraints> c2) {
            while (c2.next()) {
                for (RowConstraints constraints : c2.getRemoved()) {
                    if (constraints != null && !GridPane.this.rowConstraints.contains(constraints)) {
                        constraints.remove(GridPane.this);
                    }
                }
                for (RowConstraints constraints2 : c2.getAddedSubList()) {
                    if (constraints2 != null) {
                        constraints2.add(GridPane.this);
                    }
                }
            }
            GridPane.this.requestLayout();
        }
    };
    private final ObservableList<ColumnConstraints> columnConstraints = new TrackableObservableList<ColumnConstraints>() { // from class: javafx.scene.layout.GridPane.6
        @Override // com.sun.javafx.collections.TrackableObservableList
        protected void onChanged(ListChangeListener.Change<ColumnConstraints> c2) {
            while (c2.next()) {
                for (ColumnConstraints constraints : c2.getRemoved()) {
                    if (constraints != null && !GridPane.this.columnConstraints.contains(constraints)) {
                        constraints.remove(GridPane.this);
                    }
                }
                for (ColumnConstraints constraints2 : c2.getAddedSubList()) {
                    if (constraints2 != null) {
                        constraints2.add(GridPane.this);
                    }
                }
            }
            GridPane.this.requestLayout();
        }
    };
    private double rowPercentTotal = 0.0d;
    private double columnPercentTotal = 0.0d;
    private boolean metricsDirty = true;
    private boolean performingLayout = false;

    static {
        $assertionsDisabled = !GridPane.class.desiredAssertionStatus();
        marginAccessor = n2 -> {
            return getMargin(n2);
        };
        GRID_LINE_COLOR = Color.rgb(30, 30, 30);
    }

    public static void setRowIndex(Node child, Integer value) {
        if (value != null && value.intValue() < 0) {
            throw new IllegalArgumentException("rowIndex must be greater or equal to 0, but was " + ((Object) value));
        }
        setConstraint(child, ROW_INDEX_CONSTRAINT, value);
    }

    public static Integer getRowIndex(Node child) {
        return (Integer) getConstraint(child, ROW_INDEX_CONSTRAINT);
    }

    public static void setColumnIndex(Node child, Integer value) {
        if (value != null && value.intValue() < 0) {
            throw new IllegalArgumentException("columnIndex must be greater or equal to 0, but was " + ((Object) value));
        }
        setConstraint(child, COLUMN_INDEX_CONSTRAINT, value);
    }

    public static Integer getColumnIndex(Node child) {
        return (Integer) getConstraint(child, COLUMN_INDEX_CONSTRAINT);
    }

    public static void setRowSpan(Node child, Integer value) {
        if (value != null && value.intValue() < 1) {
            throw new IllegalArgumentException("rowSpan must be greater or equal to 1, but was " + ((Object) value));
        }
        setConstraint(child, ROW_SPAN_CONSTRAINT, value);
    }

    public static Integer getRowSpan(Node child) {
        return (Integer) getConstraint(child, ROW_SPAN_CONSTRAINT);
    }

    public static void setColumnSpan(Node child, Integer value) {
        if (value != null && value.intValue() < 1) {
            throw new IllegalArgumentException("columnSpan must be greater or equal to 1, but was " + ((Object) value));
        }
        setConstraint(child, COLUMN_SPAN_CONSTRAINT, value);
    }

    public static Integer getColumnSpan(Node child) {
        return (Integer) getConstraint(child, COLUMN_SPAN_CONSTRAINT);
    }

    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN_CONSTRAINT, value);
    }

    public static Insets getMargin(Node child) {
        return (Insets) getConstraint(child, MARGIN_CONSTRAINT);
    }

    private double getBaselineComplementForChild(Node child) {
        if (isNodePositionedByBaseline(child)) {
            return this.rowMinBaselineComplement[getNodeRowIndex(child)];
        }
        return -1.0d;
    }

    public static void setHalignment(Node child, HPos value) {
        setConstraint(child, HALIGNMENT_CONSTRAINT, value);
    }

    public static HPos getHalignment(Node child) {
        return (HPos) getConstraint(child, HALIGNMENT_CONSTRAINT);
    }

    public static void setValignment(Node child, VPos value) {
        setConstraint(child, VALIGNMENT_CONSTRAINT, value);
    }

    public static VPos getValignment(Node child) {
        return (VPos) getConstraint(child, VALIGNMENT_CONSTRAINT);
    }

    public static void setHgrow(Node child, Priority value) {
        setConstraint(child, HGROW_CONSTRAINT, value);
    }

    public static Priority getHgrow(Node child) {
        return (Priority) getConstraint(child, HGROW_CONSTRAINT);
    }

    public static void setVgrow(Node child, Priority value) {
        setConstraint(child, VGROW_CONSTRAINT, value);
    }

    public static Priority getVgrow(Node child) {
        return (Priority) getConstraint(child, VGROW_CONSTRAINT);
    }

    public static void setFillWidth(Node child, Boolean value) {
        setConstraint(child, FILL_WIDTH_CONSTRAINT, value);
    }

    public static Boolean isFillWidth(Node child) {
        return (Boolean) getConstraint(child, FILL_WIDTH_CONSTRAINT);
    }

    public static void setFillHeight(Node child, Boolean value) {
        setConstraint(child, FILL_HEIGHT_CONSTRAINT, value);
    }

    public static Boolean isFillHeight(Node child) {
        return (Boolean) getConstraint(child, FILL_HEIGHT_CONSTRAINT);
    }

    public static void setConstraints(Node child, int columnIndex, int rowIndex) {
        setRowIndex(child, Integer.valueOf(rowIndex));
        setColumnIndex(child, Integer.valueOf(columnIndex));
    }

    public static void setConstraints(Node child, int columnIndex, int rowIndex, int columnspan, int rowspan) {
        setRowIndex(child, Integer.valueOf(rowIndex));
        setColumnIndex(child, Integer.valueOf(columnIndex));
        setRowSpan(child, Integer.valueOf(rowspan));
        setColumnSpan(child, Integer.valueOf(columnspan));
    }

    public static void setConstraints(Node child, int columnIndex, int rowIndex, int columnspan, int rowspan, HPos halignment, VPos valignment) {
        setRowIndex(child, Integer.valueOf(rowIndex));
        setColumnIndex(child, Integer.valueOf(columnIndex));
        setRowSpan(child, Integer.valueOf(rowspan));
        setColumnSpan(child, Integer.valueOf(columnspan));
        setHalignment(child, halignment);
        setValignment(child, valignment);
    }

    public static void setConstraints(Node child, int columnIndex, int rowIndex, int columnspan, int rowspan, HPos halignment, VPos valignment, Priority hgrow, Priority vgrow) {
        setRowIndex(child, Integer.valueOf(rowIndex));
        setColumnIndex(child, Integer.valueOf(columnIndex));
        setRowSpan(child, Integer.valueOf(rowspan));
        setColumnSpan(child, Integer.valueOf(columnspan));
        setHalignment(child, halignment);
        setValignment(child, valignment);
        setHgrow(child, hgrow);
        setVgrow(child, vgrow);
    }

    public static void setConstraints(Node child, int columnIndex, int rowIndex, int columnspan, int rowspan, HPos halignment, VPos valignment, Priority hgrow, Priority vgrow, Insets margin) {
        setRowIndex(child, Integer.valueOf(rowIndex));
        setColumnIndex(child, Integer.valueOf(columnIndex));
        setRowSpan(child, Integer.valueOf(rowspan));
        setColumnSpan(child, Integer.valueOf(columnspan));
        setHalignment(child, halignment);
        setValignment(child, valignment);
        setHgrow(child, hgrow);
        setVgrow(child, vgrow);
        setMargin(child, margin);
    }

    public static void clearConstraints(Node child) {
        setRowIndex(child, null);
        setColumnIndex(child, null);
        setRowSpan(child, null);
        setColumnSpan(child, null);
        setHalignment(child, null);
        setValignment(child, null);
        setHgrow(child, null);
        setVgrow(child, null);
        setMargin(child, null);
    }

    static void createRow(int rowIndex, int columnIndex, Node... nodes) {
        for (int i2 = 0; i2 < nodes.length; i2++) {
            setConstraints(nodes[i2], columnIndex + i2, rowIndex);
        }
    }

    static void createColumn(int columnIndex, int rowIndex, Node... nodes) {
        for (int i2 = 0; i2 < nodes.length; i2++) {
            setConstraints(nodes[i2], columnIndex, rowIndex + i2);
        }
    }

    static int getNodeRowIndex(Node node) {
        Integer rowIndex = getRowIndex(node);
        if (rowIndex != null) {
            return rowIndex.intValue();
        }
        return 0;
    }

    private static int getNodeRowSpan(Node node) {
        Integer rowspan = getRowSpan(node);
        if (rowspan != null) {
            return rowspan.intValue();
        }
        return 1;
    }

    static int getNodeRowEnd(Node node) {
        int rowSpan = getNodeRowSpan(node);
        if (rowSpan != Integer.MAX_VALUE) {
            return (getNodeRowIndex(node) + rowSpan) - 1;
        }
        return Integer.MAX_VALUE;
    }

    static int getNodeColumnIndex(Node node) {
        Integer columnIndex = getColumnIndex(node);
        if (columnIndex != null) {
            return columnIndex.intValue();
        }
        return 0;
    }

    private static int getNodeColumnSpan(Node node) {
        Integer colspan = getColumnSpan(node);
        if (colspan != null) {
            return colspan.intValue();
        }
        return 1;
    }

    static int getNodeColumnEnd(Node node) {
        int columnSpan = getNodeColumnSpan(node);
        if (columnSpan != Integer.MAX_VALUE) {
            return (getNodeColumnIndex(node) + columnSpan) - 1;
        }
        return Integer.MAX_VALUE;
    }

    private static Priority getNodeHgrow(Node node) {
        Priority hgrow = getHgrow(node);
        return hgrow != null ? hgrow : Priority.NEVER;
    }

    private static Priority getNodeVgrow(Node node) {
        Priority vgrow = getVgrow(node);
        return vgrow != null ? vgrow : Priority.NEVER;
    }

    private static Priority[] createPriorityArray(int length, Priority value) {
        Priority[] array = new Priority[length];
        Arrays.fill(array, value);
        return array;
    }

    public GridPane() {
        getChildren().addListener(o2 -> {
            requestLayout();
        });
    }

    public final DoubleProperty hgapProperty() {
        if (this.hgap == null) {
            this.hgap = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.layout.GridPane.1
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    GridPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.HGAP;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return GridPane.this;
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
            this.vgap = new StyleableDoubleProperty(0.0d) { // from class: javafx.scene.layout.GridPane.2
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    GridPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.VGAP;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return GridPane.this;
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

    public final ObjectProperty<Pos> alignmentProperty() {
        if (this.alignment == null) {
            this.alignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT) { // from class: javafx.scene.layout.GridPane.3
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    GridPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<GridPane, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return GridPane.this;
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

    public final BooleanProperty gridLinesVisibleProperty() {
        if (this.gridLinesVisible == null) {
            this.gridLinesVisible = new StyleableBooleanProperty() { // from class: javafx.scene.layout.GridPane.4
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    if (!get()) {
                        GridPane.this.getChildren().remove(GridPane.this.gridLines);
                        GridPane.this.gridLines = null;
                    } else {
                        GridPane.this.gridLines = new Group();
                        GridPane.this.gridLines.setManaged(false);
                        GridPane.this.getChildren().add(GridPane.this.gridLines);
                    }
                    GridPane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.GRID_LINES_VISIBLE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return GridPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "gridLinesVisible";
                }
            };
        }
        return this.gridLinesVisible;
    }

    public final void setGridLinesVisible(boolean value) {
        gridLinesVisibleProperty().set(value);
    }

    public final boolean isGridLinesVisible() {
        if (this.gridLinesVisible == null) {
            return false;
        }
        return this.gridLinesVisible.get();
    }

    public final ObservableList<RowConstraints> getRowConstraints() {
        return this.rowConstraints;
    }

    public final ObservableList<ColumnConstraints> getColumnConstraints() {
        return this.columnConstraints;
    }

    public void add(Node child, int columnIndex, int rowIndex) {
        setConstraints(child, columnIndex, rowIndex);
        getChildren().add(child);
    }

    public void add(Node child, int columnIndex, int rowIndex, int colspan, int rowspan) {
        setConstraints(child, columnIndex, rowIndex, colspan, rowspan);
        getChildren().add(child);
    }

    public void addRow(int rowIndex, Node... children) {
        int columnIndex = 0;
        List<Node> managed = getManagedChildren();
        int size = managed.size();
        for (int i2 = 0; i2 < size; i2++) {
            Node child = managed.get(i2);
            int nodeRowIndex = getNodeRowIndex(child);
            int nodeRowEnd = getNodeRowEnd(child);
            if (rowIndex >= nodeRowIndex && (rowIndex <= nodeRowEnd || nodeRowEnd == Integer.MAX_VALUE)) {
                int index = getNodeColumnIndex(child);
                int end = getNodeColumnEnd(child);
                columnIndex = Math.max(columnIndex, (end != Integer.MAX_VALUE ? end : index) + 1);
            }
        }
        createRow(rowIndex, columnIndex, children);
        getChildren().addAll(children);
    }

    public void addColumn(int columnIndex, Node... children) {
        int rowIndex = 0;
        List<Node> managed = getManagedChildren();
        int size = managed.size();
        for (int i2 = 0; i2 < size; i2++) {
            Node child = managed.get(i2);
            int nodeColumnIndex = getNodeColumnIndex(child);
            int nodeColumnEnd = getNodeColumnEnd(child);
            if (columnIndex >= nodeColumnIndex && (columnIndex <= nodeColumnEnd || nodeColumnEnd == Integer.MAX_VALUE)) {
                int index = getNodeRowIndex(child);
                int end = getNodeRowEnd(child);
                rowIndex = Math.max(rowIndex, (end != Integer.MAX_VALUE ? end : index) + 1);
            }
        }
        createColumn(columnIndex, rowIndex, children);
        getChildren().addAll(children);
    }

    private int getNumberOfRows() {
        computeGridMetrics();
        return this.numRows;
    }

    private int getNumberOfColumns() {
        computeGridMetrics();
        return this.numColumns;
    }

    private boolean isNodePositionedByBaseline(Node n2) {
        return (getRowValignment(getNodeRowIndex(n2)) == VPos.BASELINE && getValignment(n2) == null) || getValignment(n2) == VPos.BASELINE;
    }

    private void computeGridMetrics() {
        if (this.metricsDirty) {
            this.numRows = this.rowConstraints.size();
            this.numColumns = this.columnConstraints.size();
            List<Node> managed = getManagedChildren();
            int size = managed.size();
            for (int i2 = 0; i2 < size; i2++) {
                Node child = managed.get(i2);
                int rowIndex = getNodeRowIndex(child);
                int columnIndex = getNodeColumnIndex(child);
                int rowEnd = getNodeRowEnd(child);
                int columnEnd = getNodeColumnEnd(child);
                this.numRows = Math.max(this.numRows, (rowEnd != Integer.MAX_VALUE ? rowEnd : rowIndex) + 1);
                this.numColumns = Math.max(this.numColumns, (columnEnd != Integer.MAX_VALUE ? columnEnd : columnIndex) + 1);
            }
            this.rowPercentHeight = createDoubleArray(this.numRows, -1.0d);
            this.rowPercentTotal = 0.0d;
            this.columnPercentWidth = createDoubleArray(this.numColumns, -1.0d);
            this.columnPercentTotal = 0.0d;
            this.columnGrow = createPriorityArray(this.numColumns, Priority.NEVER);
            this.rowGrow = createPriorityArray(this.numRows, Priority.NEVER);
            this.rowMinBaselineComplement = createDoubleArray(this.numRows, -1.0d);
            this.rowPrefBaselineComplement = createDoubleArray(this.numRows, -1.0d);
            this.rowMaxBaselineComplement = createDoubleArray(this.numRows, -1.0d);
            this.rowBaseline = new List[this.numRows];
            int sz = this.numRows;
            for (int i3 = 0; i3 < sz; i3++) {
                if (i3 < this.rowConstraints.size()) {
                    RowConstraints rc = this.rowConstraints.get(i3);
                    double percentHeight = rc.getPercentHeight();
                    Priority vGrow = rc.getVgrow();
                    if (percentHeight >= 0.0d) {
                        this.rowPercentHeight[i3] = percentHeight;
                    }
                    if (vGrow != null) {
                        this.rowGrow[i3] = vGrow;
                    }
                }
                List<Node> baselineNodes = new ArrayList<>(this.numColumns);
                int size2 = managed.size();
                for (int j2 = 0; j2 < size2; j2++) {
                    Node n2 = managed.get(j2);
                    if (getNodeRowIndex(n2) == i3 && isNodePositionedByBaseline(n2)) {
                        baselineNodes.add(n2);
                    }
                }
                this.rowMinBaselineComplement[i3] = getMinBaselineComplement(baselineNodes);
                this.rowPrefBaselineComplement[i3] = getPrefBaselineComplement(baselineNodes);
                this.rowMaxBaselineComplement[i3] = getMaxBaselineComplement(baselineNodes);
                this.rowBaseline[i3] = baselineNodes;
            }
            int sz2 = Math.min(this.numColumns, this.columnConstraints.size());
            for (int i4 = 0; i4 < sz2; i4++) {
                ColumnConstraints cc = this.columnConstraints.get(i4);
                double percentWidth = cc.getPercentWidth();
                Priority hGrow = cc.getHgrow();
                if (percentWidth >= 0.0d) {
                    this.columnPercentWidth[i4] = percentWidth;
                }
                if (hGrow != null) {
                    this.columnGrow[i4] = hGrow;
                }
            }
            int size3 = managed.size();
            for (int i5 = 0; i5 < size3; i5++) {
                Node child2 = managed.get(i5);
                if (getNodeColumnSpan(child2) == 1) {
                    Priority hg = getNodeHgrow(child2);
                    int idx = getNodeColumnIndex(child2);
                    this.columnGrow[idx] = Priority.max(this.columnGrow[idx], hg);
                }
                if (getNodeRowSpan(child2) == 1) {
                    Priority vg = getNodeVgrow(child2);
                    int idx2 = getNodeRowIndex(child2);
                    this.rowGrow[idx2] = Priority.max(this.rowGrow[idx2], vg);
                }
            }
            for (int i6 = 0; i6 < this.rowPercentHeight.length; i6++) {
                if (this.rowPercentHeight[i6] > 0.0d) {
                    this.rowPercentTotal += this.rowPercentHeight[i6];
                }
            }
            if (this.rowPercentTotal > 100.0d) {
                double weight = 100.0d / this.rowPercentTotal;
                for (int i7 = 0; i7 < this.rowPercentHeight.length; i7++) {
                    if (this.rowPercentHeight[i7] > 0.0d) {
                        double[] dArr = this.rowPercentHeight;
                        int i8 = i7;
                        dArr[i8] = dArr[i8] * weight;
                    }
                }
                this.rowPercentTotal = 100.0d;
            }
            for (int i9 = 0; i9 < this.columnPercentWidth.length; i9++) {
                if (this.columnPercentWidth[i9] > 0.0d) {
                    this.columnPercentTotal += this.columnPercentWidth[i9];
                }
            }
            if (this.columnPercentTotal > 100.0d) {
                double weight2 = 100.0d / this.columnPercentTotal;
                for (int i10 = 0; i10 < this.columnPercentWidth.length; i10++) {
                    if (this.columnPercentWidth[i10] > 0.0d) {
                        double[] dArr2 = this.columnPercentWidth;
                        int i11 = i10;
                        dArr2[i11] = dArr2[i11] * weight2;
                    }
                }
                this.columnPercentTotal = 100.0d;
            }
            this.bias = null;
            for (int i12 = 0; i12 < managed.size(); i12++) {
                Orientation b2 = managed.get(i12).getContentBias();
                if (b2 != null) {
                    this.bias = b2;
                    if (b2 == Orientation.HORIZONTAL) {
                        break;
                    }
                }
            }
            this.metricsDirty = false;
        }
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinWidth(double height) {
        double[] dArrAsArray;
        computeGridMetrics();
        this.performingLayout = true;
        if (height == -1.0d) {
            dArrAsArray = null;
        } else {
            try {
                dArrAsArray = computeHeightsToFit(height).asArray();
            } catch (Throwable th) {
                this.performingLayout = false;
                throw th;
            }
        }
        double[] heights = dArrAsArray;
        double dSnapSpace = snapSpace(getInsets().getLeft()) + computeMinWidths(heights).computeTotalWithMultiSize() + snapSpace(getInsets().getRight());
        this.performingLayout = false;
        return dSnapSpace;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        double[] dArrAsArray;
        computeGridMetrics();
        this.performingLayout = true;
        if (width == -1.0d) {
            dArrAsArray = null;
        } else {
            try {
                dArrAsArray = computeWidthsToFit(width).asArray();
            } catch (Throwable th) {
                this.performingLayout = false;
                throw th;
            }
        }
        double[] widths = dArrAsArray;
        double dSnapSpace = snapSpace(getInsets().getTop()) + computeMinHeights(widths).computeTotalWithMultiSize() + snapSpace(getInsets().getBottom());
        this.performingLayout = false;
        return dSnapSpace;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        double[] dArrAsArray;
        computeGridMetrics();
        this.performingLayout = true;
        if (height == -1.0d) {
            dArrAsArray = null;
        } else {
            try {
                dArrAsArray = computeHeightsToFit(height).asArray();
            } catch (Throwable th) {
                this.performingLayout = false;
                throw th;
            }
        }
        double[] heights = dArrAsArray;
        double dSnapSpace = snapSpace(getInsets().getLeft()) + computePrefWidths(heights).computeTotalWithMultiSize() + snapSpace(getInsets().getRight());
        this.performingLayout = false;
        return dSnapSpace;
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        double[] dArrAsArray;
        computeGridMetrics();
        this.performingLayout = true;
        if (width == -1.0d) {
            dArrAsArray = null;
        } else {
            try {
                dArrAsArray = computeWidthsToFit(width).asArray();
            } catch (Throwable th) {
                this.performingLayout = false;
                throw th;
            }
        }
        double[] widths = dArrAsArray;
        double dSnapSpace = snapSpace(getInsets().getTop()) + computePrefHeights(widths).computeTotalWithMultiSize() + snapSpace(getInsets().getBottom());
        this.performingLayout = false;
        return dSnapSpace;
    }

    private VPos getRowValignment(int rowIndex) {
        if (rowIndex < getRowConstraints().size()) {
            RowConstraints constraints = getRowConstraints().get(rowIndex);
            if (constraints.getValignment() != null) {
                return constraints.getValignment();
            }
        }
        return VPos.CENTER;
    }

    private HPos getColumnHalignment(int columnIndex) {
        if (columnIndex < getColumnConstraints().size()) {
            ColumnConstraints constraints = getColumnConstraints().get(columnIndex);
            if (constraints.getHalignment() != null) {
                return constraints.getHalignment();
            }
        }
        return HPos.LEFT;
    }

    private double getColumnMinWidth(int columnIndex) {
        if (columnIndex < getColumnConstraints().size()) {
            ColumnConstraints constraints = getColumnConstraints().get(columnIndex);
            return constraints.getMinWidth();
        }
        return -1.0d;
    }

    private double getRowMinHeight(int rowIndex) {
        if (rowIndex < getRowConstraints().size()) {
            RowConstraints constraints = getRowConstraints().get(rowIndex);
            return constraints.getMinHeight();
        }
        return -1.0d;
    }

    private double getColumnMaxWidth(int columnIndex) {
        if (columnIndex < getColumnConstraints().size()) {
            ColumnConstraints constraints = getColumnConstraints().get(columnIndex);
            return constraints.getMaxWidth();
        }
        return -1.0d;
    }

    private double getColumnPrefWidth(int columnIndex) {
        if (columnIndex < getColumnConstraints().size()) {
            ColumnConstraints constraints = getColumnConstraints().get(columnIndex);
            return constraints.getPrefWidth();
        }
        return -1.0d;
    }

    private double getRowPrefHeight(int rowIndex) {
        if (rowIndex < getRowConstraints().size()) {
            RowConstraints constraints = getRowConstraints().get(rowIndex);
            return constraints.getPrefHeight();
        }
        return -1.0d;
    }

    private double getRowMaxHeight(int rowIndex) {
        if (rowIndex < getRowConstraints().size()) {
            RowConstraints constraints = getRowConstraints().get(rowIndex);
            return constraints.getMaxHeight();
        }
        return -1.0d;
    }

    private boolean shouldRowFillHeight(int rowIndex) {
        if (rowIndex < getRowConstraints().size()) {
            return getRowConstraints().get(rowIndex).isFillHeight();
        }
        return true;
    }

    private boolean shouldColumnFillWidth(int columnIndex) {
        if (columnIndex < getColumnConstraints().size()) {
            return getColumnConstraints().get(columnIndex).isFillWidth();
        }
        return true;
    }

    private double getTotalWidthOfNodeColumns(Node child, double[] widths) {
        if (getNodeColumnSpan(child) == 1) {
            return widths[getNodeColumnIndex(child)];
        }
        double total = 0.0d;
        int last = getNodeColumnEndConvertRemaining(child);
        for (int i2 = getNodeColumnIndex(child); i2 <= last; i2++) {
            total += widths[i2];
        }
        return total;
    }

    private CompositeSize computeMaxHeights() {
        if (this.rowMaxHeight == null) {
            this.rowMaxHeight = createCompositeRows(Double.MAX_VALUE);
            ObservableList<RowConstraints> rowConstr = getRowConstraints();
            CompositeSize prefHeights = null;
            for (int i2 = 0; i2 < rowConstr.size(); i2++) {
                RowConstraints curConstraint = rowConstr.get(i2);
                double maxRowHeight = snapSize(curConstraint.getMaxHeight());
                if (maxRowHeight == Double.NEGATIVE_INFINITY) {
                    if (prefHeights == null) {
                        prefHeights = computePrefHeights(null);
                    }
                    this.rowMaxHeight.setPresetSize(i2, prefHeights.getSize(i2));
                } else if (maxRowHeight != -1.0d) {
                    double min = snapSize(curConstraint.getMinHeight());
                    if (min >= 0.0d) {
                        this.rowMaxHeight.setPresetSize(i2, boundedSize(min, maxRowHeight, maxRowHeight));
                    } else {
                        this.rowMaxHeight.setPresetSize(i2, maxRowHeight);
                    }
                }
            }
        }
        return this.rowMaxHeight;
    }

    private CompositeSize computePrefHeights(double[] widths) {
        CompositeSize result;
        if (widths == null) {
            if (this.rowPrefHeight != null) {
                return this.rowPrefHeight;
            }
            this.rowPrefHeight = createCompositeRows(0.0d);
            result = this.rowPrefHeight;
        } else {
            result = createCompositeRows(0.0d);
        }
        ObservableList<RowConstraints> rowConstr = getRowConstraints();
        for (int i2 = 0; i2 < rowConstr.size(); i2++) {
            RowConstraints curConstraint = rowConstr.get(i2);
            double prefRowHeight = snapSize(curConstraint.getPrefHeight());
            double min = snapSize(curConstraint.getMinHeight());
            if (prefRowHeight != -1.0d) {
                double max = snapSize(curConstraint.getMaxHeight());
                if (min >= 0.0d || max >= 0.0d) {
                    result.setPresetSize(i2, boundedSize(min < 0.0d ? 0.0d : min, prefRowHeight, max < 0.0d ? Double.POSITIVE_INFINITY : max));
                } else {
                    result.setPresetSize(i2, prefRowHeight);
                }
            } else if (min > 0.0d) {
                result.setSize(i2, min);
            }
        }
        List<Node> managed = getManagedChildren();
        int size = managed.size();
        for (int i3 = 0; i3 < size; i3++) {
            Node child = managed.get(i3);
            int start = getNodeRowIndex(child);
            int end = getNodeRowEndConvertRemaining(child);
            double childPrefAreaHeight = computeChildPrefAreaHeight(child, isNodePositionedByBaseline(child) ? this.rowPrefBaselineComplement[start] : -1.0d, getMargin(child), widths == null ? -1.0d : getTotalWidthOfNodeColumns(child, widths));
            if (start == end && !result.isPreset(start)) {
                double min2 = getRowMinHeight(start);
                double max2 = getRowMaxHeight(start);
                result.setMaxSize(start, boundedSize(min2 < 0.0d ? 0.0d : min2, childPrefAreaHeight, max2 < 0.0d ? Double.MAX_VALUE : max2));
            } else if (start != end) {
                result.setMaxMultiSize(start, end + 1, childPrefAreaHeight);
            }
        }
        return result;
    }

    private CompositeSize computeMinHeights(double[] widths) {
        CompositeSize result;
        if (widths == null) {
            if (this.rowMinHeight != null) {
                return this.rowMinHeight;
            }
            this.rowMinHeight = createCompositeRows(0.0d);
            result = this.rowMinHeight;
        } else {
            result = createCompositeRows(0.0d);
        }
        ObservableList<RowConstraints> rowConstr = getRowConstraints();
        CompositeSize prefHeights = null;
        for (int i2 = 0; i2 < rowConstr.size(); i2++) {
            double minRowHeight = snapSize(rowConstr.get(i2).getMinHeight());
            if (minRowHeight == Double.NEGATIVE_INFINITY) {
                if (prefHeights == null) {
                    prefHeights = computePrefHeights(widths);
                }
                result.setPresetSize(i2, prefHeights.getSize(i2));
            } else if (minRowHeight != -1.0d) {
                result.setPresetSize(i2, minRowHeight);
            }
        }
        List<Node> managed = getManagedChildren();
        int size = managed.size();
        for (int i3 = 0; i3 < size; i3++) {
            Node child = managed.get(i3);
            int start = getNodeRowIndex(child);
            int end = getNodeRowEndConvertRemaining(child);
            double childMinAreaHeight = computeChildMinAreaHeight(child, isNodePositionedByBaseline(child) ? this.rowMinBaselineComplement[start] : -1.0d, getMargin(child), widths == null ? -1.0d : getTotalWidthOfNodeColumns(child, widths));
            if (start != end || result.isPreset(start)) {
                if (start != end) {
                    result.setMaxMultiSize(start, end + 1, childMinAreaHeight);
                }
            } else {
                result.setMaxSize(start, childMinAreaHeight);
            }
        }
        return result;
    }

    private double getTotalHeightOfNodeRows(Node child, double[] heights) {
        if (getNodeRowSpan(child) == 1) {
            return heights[getNodeRowIndex(child)];
        }
        double total = 0.0d;
        int last = getNodeRowEndConvertRemaining(child);
        for (int i2 = getNodeRowIndex(child); i2 <= last; i2++) {
            total += heights[i2];
        }
        return total;
    }

    private CompositeSize computeMaxWidths() {
        if (this.columnMaxWidth == null) {
            this.columnMaxWidth = createCompositeColumns(Double.MAX_VALUE);
            ObservableList<ColumnConstraints> columnConstr = getColumnConstraints();
            CompositeSize prefWidths = null;
            for (int i2 = 0; i2 < columnConstr.size(); i2++) {
                ColumnConstraints curConstraint = columnConstr.get(i2);
                double maxColumnWidth = snapSize(curConstraint.getMaxWidth());
                if (maxColumnWidth == Double.NEGATIVE_INFINITY) {
                    if (prefWidths == null) {
                        prefWidths = computePrefWidths(null);
                    }
                    this.columnMaxWidth.setPresetSize(i2, prefWidths.getSize(i2));
                } else if (maxColumnWidth != -1.0d) {
                    double min = snapSize(curConstraint.getMinWidth());
                    if (min >= 0.0d) {
                        this.columnMaxWidth.setPresetSize(i2, boundedSize(min, maxColumnWidth, maxColumnWidth));
                    } else {
                        this.columnMaxWidth.setPresetSize(i2, maxColumnWidth);
                    }
                }
            }
        }
        return this.columnMaxWidth;
    }

    private CompositeSize computePrefWidths(double[] heights) {
        CompositeSize result;
        if (heights == null) {
            if (this.columnPrefWidth != null) {
                return this.columnPrefWidth;
            }
            this.columnPrefWidth = createCompositeColumns(0.0d);
            result = this.columnPrefWidth;
        } else {
            result = createCompositeColumns(0.0d);
        }
        ObservableList<ColumnConstraints> columnConstr = getColumnConstraints();
        for (int i2 = 0; i2 < columnConstr.size(); i2++) {
            ColumnConstraints curConstraint = columnConstr.get(i2);
            double prefColumnWidth = snapSize(curConstraint.getPrefWidth());
            double min = snapSize(curConstraint.getMinWidth());
            if (prefColumnWidth != -1.0d) {
                double max = snapSize(curConstraint.getMaxWidth());
                if (min >= 0.0d || max >= 0.0d) {
                    result.setPresetSize(i2, boundedSize(min < 0.0d ? 0.0d : min, prefColumnWidth, max < 0.0d ? Double.POSITIVE_INFINITY : max));
                } else {
                    result.setPresetSize(i2, prefColumnWidth);
                }
            } else if (min > 0.0d) {
                result.setSize(i2, min);
            }
        }
        List<Node> managed = getManagedChildren();
        int size = managed.size();
        for (int i3 = 0; i3 < size; i3++) {
            Node child = managed.get(i3);
            int start = getNodeColumnIndex(child);
            int end = getNodeColumnEndConvertRemaining(child);
            if (start == end && !result.isPreset(start)) {
                double min2 = getColumnMinWidth(start);
                double max2 = getColumnMaxWidth(start);
                result.setMaxSize(start, boundedSize(min2 < 0.0d ? 0.0d : min2, computeChildPrefAreaWidth(child, getBaselineComplementForChild(child), getMargin(child), heights == null ? -1.0d : getTotalHeightOfNodeRows(child, heights), false), max2 < 0.0d ? Double.MAX_VALUE : max2));
            } else if (start != end) {
                result.setMaxMultiSize(start, end + 1, computeChildPrefAreaWidth(child, getBaselineComplementForChild(child), getMargin(child), heights == null ? -1.0d : getTotalHeightOfNodeRows(child, heights), false));
            }
        }
        return result;
    }

    private CompositeSize computeMinWidths(double[] heights) {
        CompositeSize result;
        if (heights == null) {
            if (this.columnMinWidth != null) {
                return this.columnMinWidth;
            }
            this.columnMinWidth = createCompositeColumns(0.0d);
            result = this.columnMinWidth;
        } else {
            result = createCompositeColumns(0.0d);
        }
        ObservableList<ColumnConstraints> columnConstr = getColumnConstraints();
        CompositeSize prefWidths = null;
        for (int i2 = 0; i2 < columnConstr.size(); i2++) {
            double minColumnWidth = snapSize(columnConstr.get(i2).getMinWidth());
            if (minColumnWidth == Double.NEGATIVE_INFINITY) {
                if (prefWidths == null) {
                    prefWidths = computePrefWidths(heights);
                }
                result.setPresetSize(i2, prefWidths.getSize(i2));
            } else if (minColumnWidth != -1.0d) {
                result.setPresetSize(i2, minColumnWidth);
            }
        }
        List<Node> managed = getManagedChildren();
        int size = managed.size();
        for (int i3 = 0; i3 < size; i3++) {
            Node child = managed.get(i3);
            int start = getNodeColumnIndex(child);
            int end = getNodeColumnEndConvertRemaining(child);
            if (start != end || result.isPreset(start)) {
                if (start != end) {
                    result.setMaxMultiSize(start, end + 1, computeChildMinAreaWidth(child, getBaselineComplementForChild(child), getMargin(child), heights == null ? -1.0d : getTotalHeightOfNodeRows(child, heights), false));
                }
            } else {
                result.setMaxSize(start, computeChildMinAreaWidth(child, getBaselineComplementForChild(child), getMargin(child), heights == null ? -1.0d : getTotalHeightOfNodeRows(child, heights), false));
            }
        }
        return result;
    }

    private CompositeSize computeHeightsToFit(double height) {
        CompositeSize heights;
        if (!$assertionsDisabled && height == -1.0d) {
            throw new AssertionError();
        }
        if (this.rowPercentTotal == 100.0d) {
            heights = createCompositeRows(0.0d);
        } else {
            heights = (CompositeSize) computePrefHeights(null).clone();
        }
        adjustRowHeights(heights, height);
        return heights;
    }

    private CompositeSize computeWidthsToFit(double width) {
        CompositeSize widths;
        if (!$assertionsDisabled && width == -1.0d) {
            throw new AssertionError();
        }
        if (this.columnPercentTotal == 100.0d) {
            widths = createCompositeColumns(0.0d);
        } else {
            widths = (CompositeSize) computePrefWidths(null).clone();
        }
        adjustColumnWidths(widths, width);
        return widths;
    }

    @Override // javafx.scene.Node
    public Orientation getContentBias() {
        computeGridMetrics();
        return this.bias;
    }

    @Override // javafx.scene.Parent
    public void requestLayout() {
        if (this.performingLayout) {
            return;
        }
        if (this.metricsDirty) {
            super.requestLayout();
            return;
        }
        this.metricsDirty = true;
        this.bias = null;
        this.rowGrow = null;
        this.rowMaxHeight = null;
        this.rowPrefHeight = null;
        this.rowMinHeight = null;
        this.columnGrow = null;
        this.columnMaxWidth = null;
        this.columnPrefWidth = null;
        this.columnMinWidth = null;
        this.rowMaxBaselineComplement = null;
        this.rowPrefBaselineComplement = null;
        this.rowMinBaselineComplement = null;
        super.requestLayout();
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        CompositeSize heights;
        double rowTotal;
        CompositeSize widths;
        double columnTotal;
        this.performingLayout = true;
        try {
            double snaphgap = snapSpace(getHgap());
            double snapvgap = snapSpace(getVgap());
            double top = snapSpace(getInsets().getTop());
            double bottom = snapSpace(getInsets().getBottom());
            double left = snapSpace(getInsets().getLeft());
            double right = snapSpace(getInsets().getRight());
            double width = getWidth();
            double height = getHeight();
            double contentHeight = (height - top) - bottom;
            double contentWidth = (width - left) - right;
            computeGridMetrics();
            Orientation contentBias = getContentBias();
            if (contentBias == null) {
                heights = (CompositeSize) computePrefHeights(null).clone();
                widths = (CompositeSize) computePrefWidths(null).clone();
                rowTotal = adjustRowHeights(heights, height);
                columnTotal = adjustColumnWidths(widths, width);
            } else if (contentBias == Orientation.HORIZONTAL) {
                widths = (CompositeSize) computePrefWidths(null).clone();
                columnTotal = adjustColumnWidths(widths, width);
                heights = computePrefHeights(widths.asArray());
                rowTotal = adjustRowHeights(heights, height);
            } else {
                heights = (CompositeSize) computePrefHeights(null).clone();
                rowTotal = adjustRowHeights(heights, height);
                widths = computePrefWidths(heights.asArray());
                columnTotal = adjustColumnWidths(widths, width);
            }
            double x2 = left + computeXOffset(contentWidth, columnTotal, getAlignmentInternal().getHpos());
            double y2 = top + computeYOffset(contentHeight, rowTotal, getAlignmentInternal().getVpos());
            List<Node> managed = getManagedChildren();
            double[] baselineOffsets = createDoubleArray(this.numRows, -1.0d);
            int size = managed.size();
            for (int i2 = 0; i2 < size; i2++) {
                Node child = managed.get(i2);
                int rowIndex = getNodeRowIndex(child);
                int columnIndex = getNodeColumnIndex(child);
                int colspan = getNodeColumnSpan(child);
                if (colspan == Integer.MAX_VALUE) {
                    colspan = widths.getLength() - columnIndex;
                }
                int rowspan = getNodeRowSpan(child);
                if (rowspan == Integer.MAX_VALUE) {
                    rowspan = heights.getLength() - rowIndex;
                }
                double areaX = x2;
                for (int j2 = 0; j2 < columnIndex; j2++) {
                    areaX += widths.getSize(j2) + snaphgap;
                }
                double areaY = y2;
                for (int j3 = 0; j3 < rowIndex; j3++) {
                    areaY += heights.getSize(j3) + snapvgap;
                }
                double areaW = widths.getSize(columnIndex);
                for (int j4 = 2; j4 <= colspan; j4++) {
                    areaW += widths.getSize((columnIndex + j4) - 1) + snaphgap;
                }
                double areaH = heights.getSize(rowIndex);
                for (int j5 = 2; j5 <= rowspan; j5++) {
                    areaH += heights.getSize((rowIndex + j5) - 1) + snapvgap;
                }
                HPos halign = getHalignment(child);
                VPos valign = getValignment(child);
                Boolean fillWidth = isFillWidth(child);
                Boolean fillHeight = isFillHeight(child);
                if (halign == null) {
                    halign = getColumnHalignment(columnIndex);
                }
                if (valign == null) {
                    valign = getRowValignment(rowIndex);
                }
                if (fillWidth == null) {
                    fillWidth = Boolean.valueOf(shouldColumnFillWidth(columnIndex));
                }
                if (fillHeight == null) {
                    fillHeight = Boolean.valueOf(shouldRowFillHeight(rowIndex));
                }
                double baselineOffset = 0.0d;
                if (valign == VPos.BASELINE) {
                    if (baselineOffsets[rowIndex] == -1.0d) {
                        CompositeSize compositeSize = widths;
                        baselineOffsets[rowIndex] = getAreaBaselineOffset(this.rowBaseline[rowIndex], marginAccessor, t2 -> {
                            Node n2 = this.rowBaseline[rowIndex].get(t2.intValue());
                            int c2 = getNodeColumnIndex(n2);
                            int cs = getNodeColumnSpan(n2);
                            if (cs == Integer.MAX_VALUE) {
                                cs = compositeSize.getLength() - c2;
                            }
                            double w2 = compositeSize.getSize(c2);
                            for (int j6 = 2; j6 <= cs; j6++) {
                                w2 += compositeSize.getSize((c2 + j6) - 1) + snaphgap;
                            }
                            return Double.valueOf(w2);
                        }, areaH, t3 -> {
                            Boolean b2 = isFillHeight(child);
                            if (b2 != null) {
                                return b2;
                            }
                            return Boolean.valueOf(shouldRowFillHeight(getNodeRowIndex(child)));
                        }, this.rowMinBaselineComplement[rowIndex]);
                    }
                    baselineOffset = baselineOffsets[rowIndex];
                }
                Insets margin = getMargin(child);
                layoutInArea(child, areaX, areaY, areaW, areaH, baselineOffset, margin, fillWidth.booleanValue(), fillHeight.booleanValue(), halign, valign);
            }
            layoutGridLines(widths, heights, x2, y2, rowTotal, columnTotal);
            this.currentHeights = heights;
            this.currentWidths = widths;
            this.performingLayout = false;
        } catch (Throwable th) {
            this.performingLayout = false;
            throw th;
        }
    }

    private double adjustRowHeights(CompositeSize heights, double height) {
        if (!$assertionsDisabled && height == -1.0d) {
            throw new AssertionError();
        }
        double snapvgap = snapSpace(getVgap());
        double top = snapSpace(getInsets().getTop());
        double bottom = snapSpace(getInsets().getBottom());
        double vgaps = snapvgap * (getNumberOfRows() - 1);
        double contentHeight = (height - top) - bottom;
        if (this.rowPercentTotal > 0.0d) {
            double remainder = 0.0d;
            for (int i2 = 0; i2 < this.rowPercentHeight.length; i2++) {
                if (this.rowPercentHeight[i2] >= 0.0d) {
                    double size = (contentHeight - vgaps) * (this.rowPercentHeight[i2] / 100.0d);
                    double floor = Math.floor(size);
                    remainder += size - floor;
                    double size2 = floor;
                    if (remainder >= 0.5d) {
                        size2 += 1.0d;
                        remainder = (-1.0d) + remainder;
                    }
                    heights.setSize(i2, size2);
                }
            }
        }
        double rowTotal = heights.computeTotal();
        if (this.rowPercentTotal < 100.0d) {
            double heightAvailable = ((height - top) - bottom) - rowTotal;
            if (heightAvailable != 0.0d) {
                double remaining = growToMultiSpanPreferredHeights(heights, heightAvailable);
                rowTotal += heightAvailable - growOrShrinkRowHeights(heights, Priority.SOMETIMES, growOrShrinkRowHeights(heights, Priority.ALWAYS, remaining));
            }
        }
        return rowTotal;
    }

    private double growToMultiSpanPreferredHeights(CompositeSize heights, double extraHeight) {
        double dBoundedSize;
        double dBoundedSize2;
        double dBoundedSize3;
        if (extraHeight <= 0.0d) {
            return extraHeight;
        }
        Set<Integer> rowsAlways = new TreeSet<>();
        Set<Integer> rowsSometimes = new TreeSet<>();
        Set<Integer> lastRows = new TreeSet<>();
        Iterator it = heights.multiSizes().iterator();
        while (it.hasNext()) {
            Interval interval = ((Map.Entry) it.next()).getKey();
            for (int i2 = interval.begin; i2 < interval.end; i2++) {
                if (this.rowPercentHeight[i2] < 0.0d) {
                    switch (this.rowGrow[i2]) {
                        case ALWAYS:
                            rowsAlways.add(Integer.valueOf(i2));
                            break;
                        case SOMETIMES:
                            rowsSometimes.add(Integer.valueOf(i2));
                            break;
                    }
                }
            }
            if (this.rowPercentHeight[interval.end - 1] < 0.0d) {
                lastRows.add(Integer.valueOf(interval.end - 1));
            }
        }
        double remaining = extraHeight;
        while (rowsAlways.size() > 0 && remaining > rowsAlways.size()) {
            double rowPortion = Math.floor(remaining / rowsAlways.size());
            Iterator<Integer> it2 = rowsAlways.iterator();
            while (it2.hasNext()) {
                int i3 = it2.next().intValue();
                double maxOfRow = getRowMaxHeight(i3);
                double prefOfRow = getRowPrefHeight(i3);
                double actualPortion = rowPortion;
                for (Map.Entry<Interval, Double> ms : heights.multiSizes()) {
                    Interval interval2 = ms.getKey();
                    if (interval2.contains(i3)) {
                        int intervalRows = 0;
                        for (int j2 = interval2.begin; j2 < interval2.end; j2++) {
                            if (rowsAlways.contains(Integer.valueOf(j2))) {
                                intervalRows++;
                            }
                        }
                        double curLength = heights.computeTotal(interval2.begin, interval2.end);
                        actualPortion = Math.min(Math.floor(Math.max(0.0d, (ms.getValue().doubleValue() - curLength) / intervalRows)), actualPortion);
                    }
                }
                double current = heights.getSize(i3);
                if (maxOfRow >= 0.0d) {
                    dBoundedSize3 = boundedSize(0.0d, current + actualPortion, maxOfRow);
                } else {
                    dBoundedSize3 = (maxOfRow != Double.NEGATIVE_INFINITY || prefOfRow <= 0.0d) ? current + actualPortion : boundedSize(0.0d, current + actualPortion, prefOfRow);
                }
                double bounded = dBoundedSize3;
                double portionUsed = bounded - current;
                remaining -= portionUsed;
                if (portionUsed != actualPortion || portionUsed == 0.0d) {
                    it2.remove();
                }
                heights.setSize(i3, bounded);
            }
        }
        while (rowsSometimes.size() > 0 && remaining > rowsSometimes.size()) {
            double colPortion = Math.floor(remaining / rowsSometimes.size());
            Iterator<Integer> it3 = rowsSometimes.iterator();
            while (it3.hasNext()) {
                int i4 = it3.next().intValue();
                double maxOfRow2 = getRowMaxHeight(i4);
                double prefOfRow2 = getRowPrefHeight(i4);
                double actualPortion2 = colPortion;
                for (Map.Entry<Interval, Double> ms2 : heights.multiSizes()) {
                    Interval interval3 = ms2.getKey();
                    if (interval3.contains(i4)) {
                        int intervalRows2 = 0;
                        for (int j3 = interval3.begin; j3 < interval3.end; j3++) {
                            if (rowsSometimes.contains(Integer.valueOf(j3))) {
                                intervalRows2++;
                            }
                        }
                        double curLength2 = heights.computeTotal(interval3.begin, interval3.end);
                        actualPortion2 = Math.min(Math.floor(Math.max(0.0d, (ms2.getValue().doubleValue() - curLength2) / intervalRows2)), actualPortion2);
                    }
                }
                double current2 = heights.getSize(i4);
                if (maxOfRow2 >= 0.0d) {
                    dBoundedSize2 = boundedSize(0.0d, current2 + actualPortion2, maxOfRow2);
                } else {
                    dBoundedSize2 = (maxOfRow2 != Double.NEGATIVE_INFINITY || prefOfRow2 <= 0.0d) ? current2 + actualPortion2 : boundedSize(0.0d, current2 + actualPortion2, prefOfRow2);
                }
                double bounded2 = dBoundedSize2;
                double portionUsed2 = bounded2 - current2;
                remaining -= portionUsed2;
                if (portionUsed2 != actualPortion2 || portionUsed2 == 0.0d) {
                    it3.remove();
                }
                heights.setSize(i4, bounded2);
            }
        }
        while (lastRows.size() > 0 && remaining > lastRows.size()) {
            double colPortion2 = Math.floor(remaining / lastRows.size());
            Iterator<Integer> it4 = lastRows.iterator();
            while (it4.hasNext()) {
                int i5 = it4.next().intValue();
                double maxOfRow3 = getRowMaxHeight(i5);
                double prefOfRow3 = getRowPrefHeight(i5);
                double actualPortion3 = colPortion2;
                for (Map.Entry<Interval, Double> ms3 : heights.multiSizes()) {
                    Interval interval4 = ms3.getKey();
                    if (interval4.end - 1 == i5) {
                        double curLength3 = heights.computeTotal(interval4.begin, interval4.end);
                        actualPortion3 = Math.min(Math.max(0.0d, ms3.getValue().doubleValue() - curLength3), actualPortion3);
                    }
                }
                double current3 = heights.getSize(i5);
                if (maxOfRow3 >= 0.0d) {
                    dBoundedSize = boundedSize(0.0d, current3 + actualPortion3, maxOfRow3);
                } else {
                    dBoundedSize = (maxOfRow3 != Double.NEGATIVE_INFINITY || prefOfRow3 <= 0.0d) ? current3 + actualPortion3 : boundedSize(0.0d, current3 + actualPortion3, prefOfRow3);
                }
                double bounded3 = dBoundedSize;
                double portionUsed3 = bounded3 - current3;
                remaining -= portionUsed3;
                if (portionUsed3 != actualPortion3 || portionUsed3 == 0.0d) {
                    it4.remove();
                }
                heights.setSize(i5, bounded3);
            }
        }
        return remaining;
    }

    private double growOrShrinkRowHeights(CompositeSize heights, Priority priority, double extraHeight) {
        boolean shrinking = extraHeight < 0.0d;
        List<Integer> adjusting = new ArrayList<>();
        for (int i2 = 0; i2 < this.rowGrow.length; i2++) {
            if (this.rowPercentHeight[i2] < 0.0d && (shrinking || this.rowGrow[i2] == priority)) {
                adjusting.add(Integer.valueOf(i2));
            }
        }
        double available = extraHeight;
        boolean handleRemainder = false;
        double portion = 0.0d;
        boolean wasPositive = available >= 0.0d;
        boolean isPositive = wasPositive;
        CompositeSize limitSize = shrinking ? computeMinHeights(null) : computeMaxHeights();
        while (available != 0.0d && wasPositive == isPositive && adjusting.size() > 0) {
            if (!handleRemainder) {
                portion = available > 0.0d ? Math.floor(available / adjusting.size()) : Math.ceil(available / adjusting.size());
            }
            if (portion != 0.0d) {
                Iterator<Integer> i3 = adjusting.iterator();
                while (i3.hasNext()) {
                    int index = i3.next().intValue();
                    double limit = snapSpace(limitSize.getProportionalMinOrMaxSize(index, shrinking)) - heights.getSize(index);
                    if ((shrinking && limit > 0.0d) || (!shrinking && limit < 0.0d)) {
                        limit = 0.0d;
                    }
                    double change = Math.abs(limit) <= Math.abs(portion) ? limit : portion;
                    heights.addSize(index, change);
                    available -= change;
                    isPositive = available >= 0.0d;
                    if (Math.abs(change) < Math.abs(portion)) {
                        i3.remove();
                    }
                    if (available == 0.0d) {
                        break;
                    }
                }
            } else {
                double portion2 = ((int) available) % adjusting.size();
                if (portion2 == 0.0d) {
                    break;
                }
                portion = shrinking ? -1.0d : 1.0d;
                handleRemainder = true;
            }
        }
        return available;
    }

    private double adjustColumnWidths(CompositeSize widths, double width) {
        if (!$assertionsDisabled && width == -1.0d) {
            throw new AssertionError();
        }
        double snaphgap = snapSpace(getHgap());
        double left = snapSpace(getInsets().getLeft());
        double right = snapSpace(getInsets().getRight());
        double hgaps = snaphgap * (getNumberOfColumns() - 1);
        double contentWidth = (width - left) - right;
        if (this.columnPercentTotal > 0.0d) {
            double remainder = 0.0d;
            for (int i2 = 0; i2 < this.columnPercentWidth.length; i2++) {
                if (this.columnPercentWidth[i2] >= 0.0d) {
                    double size = (contentWidth - hgaps) * (this.columnPercentWidth[i2] / 100.0d);
                    double floor = Math.floor(size);
                    remainder += size - floor;
                    double size2 = floor;
                    if (remainder >= 0.5d) {
                        size2 += 1.0d;
                        remainder = (-1.0d) + remainder;
                    }
                    widths.setSize(i2, size2);
                }
            }
        }
        double columnTotal = widths.computeTotal();
        if (this.columnPercentTotal < 100.0d) {
            double widthAvailable = ((width - left) - right) - columnTotal;
            if (widthAvailable != 0.0d) {
                double remaining = growToMultiSpanPreferredWidths(widths, widthAvailable);
                columnTotal += widthAvailable - growOrShrinkColumnWidths(widths, Priority.SOMETIMES, growOrShrinkColumnWidths(widths, Priority.ALWAYS, remaining));
            }
        }
        return columnTotal;
    }

    private double growToMultiSpanPreferredWidths(CompositeSize widths, double extraWidth) {
        double dBoundedSize;
        double dBoundedSize2;
        double dBoundedSize3;
        if (extraWidth <= 0.0d) {
            return extraWidth;
        }
        Set<Integer> columnsAlways = new TreeSet<>();
        Set<Integer> columnsSometimes = new TreeSet<>();
        Set<Integer> lastColumns = new TreeSet<>();
        Iterator it = widths.multiSizes().iterator();
        while (it.hasNext()) {
            Interval interval = ((Map.Entry) it.next()).getKey();
            for (int i2 = interval.begin; i2 < interval.end; i2++) {
                if (this.columnPercentWidth[i2] < 0.0d) {
                    switch (this.columnGrow[i2]) {
                        case ALWAYS:
                            columnsAlways.add(Integer.valueOf(i2));
                            break;
                        case SOMETIMES:
                            columnsSometimes.add(Integer.valueOf(i2));
                            break;
                    }
                }
            }
            if (this.columnPercentWidth[interval.end - 1] < 0.0d) {
                lastColumns.add(Integer.valueOf(interval.end - 1));
            }
        }
        double remaining = extraWidth;
        while (columnsAlways.size() > 0 && remaining > columnsAlways.size()) {
            double colPortion = Math.floor(remaining / columnsAlways.size());
            Iterator<Integer> it2 = columnsAlways.iterator();
            while (it2.hasNext()) {
                int i3 = it2.next().intValue();
                double maxOfColumn = getColumnMaxWidth(i3);
                double prefOfColumn = getColumnPrefWidth(i3);
                double actualPortion = colPortion;
                for (Map.Entry<Interval, Double> ms : widths.multiSizes()) {
                    Interval interval2 = ms.getKey();
                    if (interval2.contains(i3)) {
                        int intervalColumns = 0;
                        for (int j2 = interval2.begin; j2 < interval2.end; j2++) {
                            if (columnsAlways.contains(Integer.valueOf(j2))) {
                                intervalColumns++;
                            }
                        }
                        double curLength = widths.computeTotal(interval2.begin, interval2.end);
                        actualPortion = Math.min(Math.floor(Math.max(0.0d, (ms.getValue().doubleValue() - curLength) / intervalColumns)), actualPortion);
                    }
                }
                double current = widths.getSize(i3);
                if (maxOfColumn >= 0.0d) {
                    dBoundedSize3 = boundedSize(0.0d, current + actualPortion, maxOfColumn);
                } else {
                    dBoundedSize3 = (maxOfColumn != Double.NEGATIVE_INFINITY || prefOfColumn <= 0.0d) ? current + actualPortion : boundedSize(0.0d, current + actualPortion, prefOfColumn);
                }
                double bounded = dBoundedSize3;
                double portionUsed = bounded - current;
                remaining -= portionUsed;
                if (portionUsed != actualPortion || portionUsed == 0.0d) {
                    it2.remove();
                }
                widths.setSize(i3, bounded);
            }
        }
        while (columnsSometimes.size() > 0 && remaining > columnsSometimes.size()) {
            double colPortion2 = Math.floor(remaining / columnsSometimes.size());
            Iterator<Integer> it3 = columnsSometimes.iterator();
            while (it3.hasNext()) {
                int i4 = it3.next().intValue();
                double maxOfColumn2 = getColumnMaxWidth(i4);
                double prefOfColumn2 = getColumnPrefWidth(i4);
                double actualPortion2 = colPortion2;
                for (Map.Entry<Interval, Double> ms2 : widths.multiSizes()) {
                    Interval interval3 = ms2.getKey();
                    if (interval3.contains(i4)) {
                        int intervalColumns2 = 0;
                        for (int j3 = interval3.begin; j3 < interval3.end; j3++) {
                            if (columnsSometimes.contains(Integer.valueOf(j3))) {
                                intervalColumns2++;
                            }
                        }
                        double curLength2 = widths.computeTotal(interval3.begin, interval3.end);
                        actualPortion2 = Math.min(Math.floor(Math.max(0.0d, (ms2.getValue().doubleValue() - curLength2) / intervalColumns2)), actualPortion2);
                    }
                }
                double current2 = widths.getSize(i4);
                if (maxOfColumn2 >= 0.0d) {
                    dBoundedSize2 = boundedSize(0.0d, current2 + actualPortion2, maxOfColumn2);
                } else {
                    dBoundedSize2 = (maxOfColumn2 != Double.NEGATIVE_INFINITY || prefOfColumn2 <= 0.0d) ? current2 + actualPortion2 : boundedSize(0.0d, current2 + actualPortion2, prefOfColumn2);
                }
                double bounded2 = dBoundedSize2;
                double portionUsed2 = bounded2 - current2;
                remaining -= portionUsed2;
                if (portionUsed2 != actualPortion2 || portionUsed2 == 0.0d) {
                    it3.remove();
                }
                widths.setSize(i4, bounded2);
            }
        }
        while (lastColumns.size() > 0 && remaining > lastColumns.size()) {
            double colPortion3 = Math.floor(remaining / lastColumns.size());
            Iterator<Integer> it4 = lastColumns.iterator();
            while (it4.hasNext()) {
                int i5 = it4.next().intValue();
                double maxOfColumn3 = getColumnMaxWidth(i5);
                double prefOfColumn3 = getColumnPrefWidth(i5);
                double actualPortion3 = colPortion3;
                for (Map.Entry<Interval, Double> ms3 : widths.multiSizes()) {
                    Interval interval4 = ms3.getKey();
                    if (interval4.end - 1 == i5) {
                        double curLength3 = widths.computeTotal(interval4.begin, interval4.end);
                        actualPortion3 = Math.min(Math.max(0.0d, ms3.getValue().doubleValue() - curLength3), actualPortion3);
                    }
                }
                double current3 = widths.getSize(i5);
                if (maxOfColumn3 >= 0.0d) {
                    dBoundedSize = boundedSize(0.0d, current3 + actualPortion3, maxOfColumn3);
                } else {
                    dBoundedSize = (maxOfColumn3 != Double.NEGATIVE_INFINITY || prefOfColumn3 <= 0.0d) ? current3 + actualPortion3 : boundedSize(0.0d, current3 + actualPortion3, prefOfColumn3);
                }
                double bounded3 = dBoundedSize;
                double portionUsed3 = bounded3 - current3;
                remaining -= portionUsed3;
                if (portionUsed3 != actualPortion3 || portionUsed3 == 0.0d) {
                    it4.remove();
                }
                widths.setSize(i5, bounded3);
            }
        }
        return remaining;
    }

    private double growOrShrinkColumnWidths(CompositeSize widths, Priority priority, double extraWidth) {
        if (extraWidth == 0.0d) {
            return 0.0d;
        }
        boolean shrinking = extraWidth < 0.0d;
        List<Integer> adjusting = new ArrayList<>();
        for (int i2 = 0; i2 < this.columnGrow.length; i2++) {
            if (this.columnPercentWidth[i2] < 0.0d && (shrinking || this.columnGrow[i2] == priority)) {
                adjusting.add(Integer.valueOf(i2));
            }
        }
        double available = extraWidth;
        boolean handleRemainder = false;
        double portion = 0.0d;
        boolean wasPositive = available >= 0.0d;
        boolean isPositive = wasPositive;
        CompositeSize limitSize = shrinking ? computeMinWidths(null) : computeMaxWidths();
        while (available != 0.0d && wasPositive == isPositive && adjusting.size() > 0) {
            if (!handleRemainder) {
                portion = available > 0.0d ? Math.floor(available / adjusting.size()) : Math.ceil(available / adjusting.size());
            }
            if (portion != 0.0d) {
                Iterator<Integer> i3 = adjusting.iterator();
                while (i3.hasNext()) {
                    int index = i3.next().intValue();
                    double limit = snapSpace(limitSize.getProportionalMinOrMaxSize(index, shrinking)) - widths.getSize(index);
                    if ((shrinking && limit > 0.0d) || (!shrinking && limit < 0.0d)) {
                        limit = 0.0d;
                    }
                    double change = Math.abs(limit) <= Math.abs(portion) ? limit : portion;
                    widths.addSize(index, change);
                    available -= change;
                    isPositive = available >= 0.0d;
                    if (Math.abs(change) < Math.abs(portion)) {
                        i3.remove();
                    }
                    if (available == 0.0d) {
                        break;
                    }
                }
            } else {
                double portion2 = ((int) available) % adjusting.size();
                if (portion2 == 0.0d) {
                    break;
                }
                portion = shrinking ? -1.0d : 1.0d;
                handleRemainder = true;
            }
        }
        return available;
    }

    private void layoutGridLines(CompositeSize columnWidths, CompositeSize rowHeights, double x2, double y2, double columnHeight, double rowWidth) {
        if (!isGridLinesVisible()) {
            return;
        }
        if (!this.gridLines.getChildren().isEmpty()) {
            this.gridLines.getChildren().clear();
        }
        snapSpace(getHgap());
        snapSpace(getVgap());
        double linex = x2;
        double liney = y2;
        for (int i2 = 0; i2 <= columnWidths.getLength(); i2++) {
            this.gridLines.getChildren().add(createGridLine(linex, liney, linex, liney + columnHeight));
            if (i2 > 0 && i2 < columnWidths.getLength() && getHgap() != 0.0d) {
                linex += getHgap();
                this.gridLines.getChildren().add(createGridLine(linex, liney, linex, liney + columnHeight));
            }
            if (i2 < columnWidths.getLength()) {
                linex += columnWidths.getSize(i2);
            }
        }
        for (int i3 = 0; i3 <= rowHeights.getLength(); i3++) {
            this.gridLines.getChildren().add(createGridLine(x2, liney, x2 + rowWidth, liney));
            if (i3 > 0 && i3 < rowHeights.getLength() && getVgap() != 0.0d) {
                liney += getVgap();
                this.gridLines.getChildren().add(createGridLine(x2, liney, x2 + rowWidth, liney));
            }
            if (i3 < rowHeights.getLength()) {
                liney += rowHeights.getSize(i3);
            }
        }
    }

    private Line createGridLine(double startX, double startY, double endX, double endY) {
        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStroke(GRID_LINE_COLOR);
        line.setStrokeDashOffset(GRID_LINE_DASH);
        return line;
    }

    @Override // javafx.scene.Node
    public String toString() {
        return "Grid hgap=" + getHgap() + ", vgap=" + getVgap() + ", alignment=" + ((Object) getAlignment());
    }

    private CompositeSize createCompositeRows(double initSize) {
        return new CompositeSize(getNumberOfRows(), this.rowPercentHeight, this.rowPercentTotal, snapSpace(getVgap()), initSize);
    }

    private CompositeSize createCompositeColumns(double initSize) {
        return new CompositeSize(getNumberOfColumns(), this.columnPercentWidth, this.columnPercentTotal, snapSpace(getHgap()), initSize);
    }

    private int getNodeRowEndConvertRemaining(Node child) {
        int rowSpan = getNodeRowSpan(child);
        return rowSpan != Integer.MAX_VALUE ? (getNodeRowIndex(child) + rowSpan) - 1 : getNumberOfRows() - 1;
    }

    private int getNodeColumnEndConvertRemaining(Node child) {
        int columnSpan = getNodeColumnSpan(child);
        return columnSpan != Integer.MAX_VALUE ? (getNodeColumnIndex(child) + columnSpan) - 1 : getNumberOfColumns() - 1;
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [double[], double[][]] */
    double[][] getGrid() {
        if (this.currentHeights == null || this.currentWidths == null) {
            return (double[][]) null;
        }
        return new double[]{this.currentWidths.asArray(), this.currentHeights.asArray()};
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/GridPane$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<GridPane, Boolean> GRID_LINES_VISIBLE = new CssMetaData<GridPane, Boolean>("-fx-grid-lines-visible", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.layout.GridPane.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(GridPane node) {
                return node.gridLinesVisible == null || !node.gridLinesVisible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(GridPane node) {
                return (StyleableProperty) node.gridLinesVisibleProperty();
            }
        };
        private static final CssMetaData<GridPane, Number> HGAP = new CssMetaData<GridPane, Number>("-fx-hgap", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.layout.GridPane.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(GridPane node) {
                return node.hgap == null || !node.hgap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(GridPane node) {
                return (StyleableProperty) node.hgapProperty();
            }
        };
        private static final CssMetaData<GridPane, Pos> ALIGNMENT = new CssMetaData<GridPane, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) { // from class: javafx.scene.layout.GridPane.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(GridPane node) {
                return node.alignment == null || !node.alignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(GridPane node) {
                return (StyleableProperty) node.alignmentProperty();
            }
        };
        private static final CssMetaData<GridPane, Number> VGAP = new CssMetaData<GridPane, Number>("-fx-vgap", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.layout.GridPane.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(GridPane node) {
                return node.vgap == null || !node.vgap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(GridPane node) {
                return (StyleableProperty) node.vgapProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(GRID_LINES_VISIBLE);
            styleables.add(HGAP);
            styleables.add(ALIGNMENT);
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

    /* loaded from: jfxrt.jar:javafx/scene/layout/GridPane$Interval.class */
    private static final class Interval implements Comparable<Interval> {
        public final int begin;
        public final int end;

        public Interval(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        @Override // java.lang.Comparable
        public int compareTo(Interval o2) {
            return this.begin != o2.begin ? this.begin - o2.begin : this.end - o2.end;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean contains(int position) {
            return this.begin <= position && position < this.end;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int size() {
            return this.end - this.begin;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/GridPane$CompositeSize.class */
    private static final class CompositeSize implements Cloneable {
        double[] singleSizes;
        private SortedMap<Interval, Double> multiSizes;
        private BitSet preset;
        private final double[] fixedPercent;
        private final double totalFixedPercent;
        private final double gap;

        public CompositeSize(int capacity, double[] fixedPercent, double totalFixedPercent, double gap, double initSize) {
            this.singleSizes = new double[capacity];
            Arrays.fill(this.singleSizes, initSize);
            this.fixedPercent = fixedPercent;
            this.totalFixedPercent = totalFixedPercent;
            this.gap = gap;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSize(int position, double size) {
            this.singleSizes[position] = size;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPresetSize(int position, double size) {
            setSize(position, size);
            if (this.preset == null) {
                this.preset = new BitSet(this.singleSizes.length);
            }
            this.preset.set(position);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isPreset(int position) {
            if (this.preset == null) {
                return false;
            }
            return this.preset.get(position);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addSize(int position, double change) {
            this.singleSizes[position] = this.singleSizes[position] + change;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double getSize(int position) {
            return this.singleSizes[position];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setMaxSize(int position, double size) {
            this.singleSizes[position] = Math.max(this.singleSizes[position], size);
        }

        private void setMultiSize(int startPosition, int endPosition, double size) {
            if (this.multiSizes == null) {
                this.multiSizes = new TreeMap();
            }
            Interval i2 = new Interval(startPosition, endPosition);
            this.multiSizes.put(i2, Double.valueOf(size));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Iterable<Map.Entry<Interval, Double>> multiSizes() {
            if (this.multiSizes == null) {
                return Collections.EMPTY_LIST;
            }
            return this.multiSizes.entrySet();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setMaxMultiSize(int startPosition, int endPosition, double size) {
            if (this.multiSizes == null) {
                this.multiSizes = new TreeMap();
            }
            Interval i2 = new Interval(startPosition, endPosition);
            Double sz = this.multiSizes.get(i2);
            if (sz == null) {
                this.multiSizes.put(i2, Double.valueOf(size));
            } else {
                this.multiSizes.put(i2, Double.valueOf(Math.max(size, sz.doubleValue())));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:23:0x009b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public double getProportionalMinOrMaxSize(int r8, boolean r9) {
            /*
                r7 = this;
                r0 = r7
                double[] r0 = r0.singleSizes
                r1 = r8
                r0 = r0[r1]
                r10 = r0
                r0 = r7
                r1 = r8
                boolean r0 = r0.isPreset(r1)
                if (r0 != 0) goto Lc7
                r0 = r7
                java.util.SortedMap<javafx.scene.layout.GridPane$Interval, java.lang.Double> r0 = r0.multiSizes
                if (r0 == 0) goto Lc7
                r0 = r7
                java.util.SortedMap<javafx.scene.layout.GridPane$Interval, java.lang.Double> r0 = r0.multiSizes
                java.util.Set r0 = r0.keySet()
                java.util.Iterator r0 = r0.iterator()
                r12 = r0
            L26:
                r0 = r12
                boolean r0 = r0.hasNext()
                if (r0 == 0) goto Lc7
                r0 = r12
                java.lang.Object r0 = r0.next()
                javafx.scene.layout.GridPane$Interval r0 = (javafx.scene.layout.GridPane.Interval) r0
                r13 = r0
                r0 = r13
                r1 = r8
                boolean r0 = javafx.scene.layout.GridPane.Interval.access$1800(r0, r1)
                if (r0 == 0) goto Lc4
                r0 = r7
                java.util.SortedMap<javafx.scene.layout.GridPane$Interval, java.lang.Double> r0 = r0.multiSizes
                r1 = r13
                java.lang.Object r0 = r0.get(r1)
                java.lang.Double r0 = (java.lang.Double) r0
                double r0 = r0.doubleValue()
                r1 = r13
                int r1 = javafx.scene.layout.GridPane.Interval.access$2700(r1)
                double r1 = (double) r1
                double r0 = r0 / r1
                r14 = r0
                r0 = r14
                r16 = r0
                r0 = r13
                int r0 = r0.begin
                r18 = r0
            L6a:
                r0 = r18
                r1 = r13
                int r1 = r1.end
                if (r0 >= r1) goto Lb0
                r0 = r18
                r1 = r8
                if (r0 == r1) goto Laa
                r0 = r9
                if (r0 == 0) goto L8e
                r0 = r7
                double[] r0 = r0.singleSizes
                r1 = r18
                r0 = r0[r1]
                r1 = r14
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 <= 0) goto Laa
                goto L9b
            L8e:
                r0 = r7
                double[] r0 = r0.singleSizes
                r1 = r18
                r0 = r0[r1]
                r1 = r14
                int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
                if (r0 >= 0) goto Laa
            L9b:
                r0 = r16
                r1 = r14
                r2 = r7
                double[] r2 = r2.singleSizes
                r3 = r18
                r2 = r2[r3]
                double r1 = r1 - r2
                double r0 = r0 + r1
                r16 = r0
            Laa:
                int r18 = r18 + 1
                goto L6a
            Lb0:
                r0 = r9
                if (r0 == 0) goto Lbd
                r0 = r10
                r1 = r16
                double r0 = java.lang.Math.max(r0, r1)
                goto Lc3
            Lbd:
                r0 = r10
                r1 = r16
                double r0 = java.lang.Math.min(r0, r1)
            Lc3:
                r10 = r0
            Lc4:
                goto L26
            Lc7:
                r0 = r10
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: javafx.scene.layout.GridPane.CompositeSize.getProportionalMinOrMaxSize(int, boolean):double");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double computeTotal(int from, int to) {
            double total = this.gap * ((to - from) - 1);
            for (int i2 = from; i2 < to; i2++) {
                total += this.singleSizes[i2];
            }
            return total;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double computeTotal() {
            return computeTotal(0, this.singleSizes.length);
        }

        private boolean allPreset(int begin, int end) {
            if (this.preset == null) {
                return false;
            }
            for (int i2 = begin; i2 < end; i2++) {
                if (!this.preset.get(i2)) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double computeTotalWithMultiSize() {
            double total = computeTotal();
            if (this.multiSizes != null) {
                for (Map.Entry<Interval, Double> e2 : this.multiSizes.entrySet()) {
                    Interval i2 = e2.getKey();
                    if (!allPreset(i2.begin, i2.end)) {
                        double subTotal = computeTotal(i2.begin, i2.end);
                        if (e2.getValue().doubleValue() > subTotal) {
                            total += e2.getValue().doubleValue() - subTotal;
                        }
                    }
                }
            }
            if (this.totalFixedPercent > 0.0d) {
                double totalNotFixed = 0.0d;
                for (int i3 = 0; i3 < this.fixedPercent.length; i3++) {
                    if (this.fixedPercent[i3] == 0.0d) {
                        total -= this.singleSizes[i3];
                    }
                }
                for (int i4 = 0; i4 < this.fixedPercent.length; i4++) {
                    if (this.fixedPercent[i4] > 0.0d) {
                        total = Math.max(total, this.singleSizes[i4] * (100.0d / this.fixedPercent[i4]));
                    } else if (this.fixedPercent[i4] < 0.0d) {
                        totalNotFixed += this.singleSizes[i4];
                    }
                }
                if (this.totalFixedPercent < 100.0d) {
                    total = Math.max(total, (totalNotFixed * 100.0d) / (100.0d - this.totalFixedPercent));
                }
            }
            return total;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getLength() {
            return this.singleSizes.length;
        }

        protected Object clone() {
            try {
                CompositeSize clone = (CompositeSize) super.clone();
                clone.singleSizes = (double[]) clone.singleSizes.clone();
                if (this.multiSizes != null) {
                    clone.multiSizes = new TreeMap((SortedMap) clone.multiSizes);
                }
                return clone;
            } catch (CloneNotSupportedException ex) {
                throw new RuntimeException(ex);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public double[] asArray() {
            return this.singleSizes;
        }
    }

    @Deprecated
    public final int impl_getRowCount() {
        int nRows = getRowConstraints().size();
        for (int i2 = 0; i2 < getChildren().size(); i2++) {
            Node child = getChildren().get(i2);
            if (child.isManaged()) {
                int rowIndex = getNodeRowIndex(child);
                int rowEnd = getNodeRowEnd(child);
                nRows = Math.max(nRows, (rowEnd != Integer.MAX_VALUE ? rowEnd : rowIndex) + 1);
            }
        }
        return nRows;
    }

    @Deprecated
    public final int impl_getColumnCount() {
        int nColumns = getColumnConstraints().size();
        for (int i2 = 0; i2 < getChildren().size(); i2++) {
            Node child = getChildren().get(i2);
            if (child.isManaged()) {
                int columnIndex = getNodeColumnIndex(child);
                int columnEnd = getNodeColumnEnd(child);
                nColumns = Math.max(nColumns, (columnEnd != Integer.MAX_VALUE ? columnEnd : columnIndex) + 1);
            }
        }
        return nColumns;
    }

    @Deprecated
    public final Bounds impl_getCellBounds(int columnIndex, int rowIndex) {
        double[] columnWidths;
        double[] rowHeights;
        double snaphgap = snapSpace(getHgap());
        double snapvgap = snapSpace(getVgap());
        double top = snapSpace(getInsets().getTop());
        double right = snapSpace(getInsets().getRight());
        double bottom = snapSpace(getInsets().getBottom());
        double left = snapSpace(getInsets().getLeft());
        double gridPaneHeight = snapSize(getHeight()) - (top + bottom);
        double gridPaneWidth = snapSize(getWidth()) - (left + right);
        double[][] grid = getGrid();
        if (grid == null) {
            rowHeights = new double[]{0.0d};
            rowIndex = 0;
            columnWidths = new double[]{0.0d};
            columnIndex = 0;
        } else {
            columnWidths = grid[0];
            rowHeights = grid[1];
        }
        double rowTotal = 0.0d;
        for (double d2 : rowHeights) {
            rowTotal += d2;
        }
        double minY = top + Region.computeYOffset(gridPaneHeight, rowTotal + ((rowHeights.length - 1) * snapvgap), getAlignment().getVpos());
        double height = rowHeights[rowIndex];
        for (int j2 = 0; j2 < rowIndex; j2++) {
            minY += rowHeights[j2] + snapvgap;
        }
        double columnTotal = 0.0d;
        for (double d3 : columnWidths) {
            columnTotal += d3;
        }
        double minX = left + Region.computeXOffset(gridPaneWidth, columnTotal + ((columnWidths.length - 1) * snaphgap), getAlignment().getHpos());
        double width = columnWidths[columnIndex];
        for (int j3 = 0; j3 < columnIndex; j3++) {
            minX += columnWidths[j3] + snaphgap;
        }
        return new BoundingBox(minX, minY, width, height);
    }
}
