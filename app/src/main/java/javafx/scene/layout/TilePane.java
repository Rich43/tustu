package javafx.scene.layout;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:javafx/scene/layout/TilePane.class */
public class TilePane extends Pane {
    private static final String MARGIN_CONSTRAINT = "tilepane-margin";
    private static final String ALIGNMENT_CONSTRAINT = "tilepane-alignment";
    private static final Callback<Node, Insets> marginAccessor = n2 -> {
        return getMargin(n2);
    };
    private double _tileWidth;
    private double _tileHeight;
    private ObjectProperty<Orientation> orientation;
    private IntegerProperty prefRows;
    private IntegerProperty prefColumns;
    private DoubleProperty prefTileWidth;
    private DoubleProperty prefTileHeight;
    private TileSizeProperty tileWidth;
    private TileSizeProperty tileHeight;
    private DoubleProperty hgap;
    private DoubleProperty vgap;
    private ObjectProperty<Pos> alignment;
    private ObjectProperty<Pos> tileAlignment;
    private int actualRows;
    private int actualColumns;

    public static void setAlignment(Node node, Pos value) {
        setConstraint(node, ALIGNMENT_CONSTRAINT, value);
    }

    public static Pos getAlignment(Node node) {
        return (Pos) getConstraint(node, ALIGNMENT_CONSTRAINT);
    }

    public static void setMargin(Node node, Insets value) {
        setConstraint(node, MARGIN_CONSTRAINT, value);
    }

    public static Insets getMargin(Node node) {
        return (Insets) getConstraint(node, MARGIN_CONSTRAINT);
    }

    public static void clearConstraints(Node child) {
        setAlignment(child, null);
        setMargin(child, null);
    }

    public TilePane() {
        this._tileWidth = -1.0d;
        this._tileHeight = -1.0d;
        this.actualRows = 0;
        this.actualColumns = 0;
    }

    public TilePane(Orientation orientation) {
        this._tileWidth = -1.0d;
        this._tileHeight = -1.0d;
        this.actualRows = 0;
        this.actualColumns = 0;
        setOrientation(orientation);
    }

    public TilePane(double hgap, double vgap) {
        this._tileWidth = -1.0d;
        this._tileHeight = -1.0d;
        this.actualRows = 0;
        this.actualColumns = 0;
        setHgap(hgap);
        setVgap(vgap);
    }

    public TilePane(Orientation orientation, double hgap, double vgap) {
        this();
        setOrientation(orientation);
        setHgap(hgap);
        setVgap(vgap);
    }

    public TilePane(Node... children) {
        this._tileWidth = -1.0d;
        this._tileHeight = -1.0d;
        this.actualRows = 0;
        this.actualColumns = 0;
        getChildren().addAll(children);
    }

    public TilePane(Orientation orientation, Node... children) {
        this._tileWidth = -1.0d;
        this._tileHeight = -1.0d;
        this.actualRows = 0;
        this.actualColumns = 0;
        setOrientation(orientation);
        getChildren().addAll(children);
    }

    public TilePane(double hgap, double vgap, Node... children) {
        this._tileWidth = -1.0d;
        this._tileHeight = -1.0d;
        this.actualRows = 0;
        this.actualColumns = 0;
        setHgap(hgap);
        setVgap(vgap);
        getChildren().addAll(children);
    }

    public TilePane(Orientation orientation, double hgap, double vgap, Node... children) {
        this();
        setOrientation(orientation);
        setHgap(hgap);
        setVgap(vgap);
        getChildren().addAll(children);
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty(Orientation.HORIZONTAL) { // from class: javafx.scene.layout.TilePane.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<TilePane, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TilePane.this;
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

    public final IntegerProperty prefRowsProperty() {
        if (this.prefRows == null) {
            this.prefRows = new StyleableIntegerProperty(5) { // from class: javafx.scene.layout.TilePane.2
                @Override // javafx.beans.property.IntegerPropertyBase
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.PREF_ROWS;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TilePane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefRows";
                }
            };
        }
        return this.prefRows;
    }

    public final void setPrefRows(int value) {
        prefRowsProperty().set(value);
    }

    public final int getPrefRows() {
        if (this.prefRows == null) {
            return 5;
        }
        return this.prefRows.get();
    }

    public final IntegerProperty prefColumnsProperty() {
        if (this.prefColumns == null) {
            this.prefColumns = new StyleableIntegerProperty(5) { // from class: javafx.scene.layout.TilePane.3
                @Override // javafx.beans.property.IntegerPropertyBase
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.PREF_COLUMNS;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TilePane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefColumns";
                }
            };
        }
        return this.prefColumns;
    }

    public final void setPrefColumns(int value) {
        prefColumnsProperty().set(value);
    }

    public final int getPrefColumns() {
        if (this.prefColumns == null) {
            return 5;
        }
        return this.prefColumns.get();
    }

    public final DoubleProperty prefTileWidthProperty() {
        if (this.prefTileWidth == null) {
            this.prefTileWidth = new StyleableDoubleProperty(-1.0d) { // from class: javafx.scene.layout.TilePane.4
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.PREF_TILE_WIDTH;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TilePane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefTileWidth";
                }
            };
        }
        return this.prefTileWidth;
    }

    public final void setPrefTileWidth(double value) {
        prefTileWidthProperty().set(value);
    }

    public final double getPrefTileWidth() {
        if (this.prefTileWidth == null) {
            return -1.0d;
        }
        return this.prefTileWidth.get();
    }

    public final DoubleProperty prefTileHeightProperty() {
        if (this.prefTileHeight == null) {
            this.prefTileHeight = new StyleableDoubleProperty(-1.0d) { // from class: javafx.scene.layout.TilePane.5
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.PREF_TILE_HEIGHT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TilePane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "prefTileHeight";
                }
            };
        }
        return this.prefTileHeight;
    }

    public final void setPrefTileHeight(double value) {
        prefTileHeightProperty().set(value);
    }

    public final double getPrefTileHeight() {
        if (this.prefTileHeight == null) {
            return -1.0d;
        }
        return this.prefTileHeight.get();
    }

    public final ReadOnlyDoubleProperty tileWidthProperty() {
        if (this.tileWidth == null) {
            this.tileWidth = new TileSizeProperty("tileWidth", this._tileWidth) { // from class: javafx.scene.layout.TilePane.6
                @Override // javafx.scene.layout.TilePane.TileSizeProperty
                public double compute() {
                    return TilePane.this.computeTileWidth();
                }
            };
        }
        return this.tileWidth;
    }

    private void invalidateTileWidth() {
        if (this.tileWidth != null) {
            this.tileWidth.invalidate();
        } else {
            this._tileWidth = -1.0d;
        }
    }

    public final double getTileWidth() {
        if (this.tileWidth != null) {
            return this.tileWidth.get();
        }
        if (this._tileWidth == -1.0d) {
            this._tileWidth = computeTileWidth();
        }
        return this._tileWidth;
    }

    public final ReadOnlyDoubleProperty tileHeightProperty() {
        if (this.tileHeight == null) {
            this.tileHeight = new TileSizeProperty("tileHeight", this._tileHeight) { // from class: javafx.scene.layout.TilePane.7
                @Override // javafx.scene.layout.TilePane.TileSizeProperty
                public double compute() {
                    return TilePane.this.computeTileHeight();
                }
            };
        }
        return this.tileHeight;
    }

    private void invalidateTileHeight() {
        if (this.tileHeight != null) {
            this.tileHeight.invalidate();
        } else {
            this._tileHeight = -1.0d;
        }
    }

    public final double getTileHeight() {
        if (this.tileHeight != null) {
            return this.tileHeight.get();
        }
        if (this._tileHeight == -1.0d) {
            this._tileHeight = computeTileHeight();
        }
        return this._tileHeight;
    }

    public final DoubleProperty hgapProperty() {
        if (this.hgap == null) {
            this.hgap = new StyleableDoubleProperty() { // from class: javafx.scene.layout.TilePane.8
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.HGAP;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TilePane.this;
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
            this.vgap = new StyleableDoubleProperty() { // from class: javafx.scene.layout.TilePane.9
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.VGAP;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TilePane.this;
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
            this.alignment = new StyleableObjectProperty<Pos>(Pos.TOP_LEFT) { // from class: javafx.scene.layout.TilePane.10
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<TilePane, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TilePane.this;
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

    public final ObjectProperty<Pos> tileAlignmentProperty() {
        if (this.tileAlignment == null) {
            this.tileAlignment = new StyleableObjectProperty<Pos>(Pos.CENTER) { // from class: javafx.scene.layout.TilePane.11
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    TilePane.this.requestLayout();
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<TilePane, Pos> getCssMetaData() {
                    return StyleableProperties.TILE_ALIGNMENT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TilePane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "tileAlignment";
                }
            };
        }
        return this.tileAlignment;
    }

    public final void setTileAlignment(Pos value) {
        tileAlignmentProperty().set(value);
    }

    public final Pos getTileAlignment() {
        return this.tileAlignment == null ? Pos.CENTER : this.tileAlignment.get();
    }

    private Pos getTileAlignmentInternal() {
        Pos localPos = getTileAlignment();
        return localPos == null ? Pos.CENTER : localPos;
    }

    @Override // javafx.scene.Node
    public Orientation getContentBias() {
        return getOrientation();
    }

    @Override // javafx.scene.Parent
    public void requestLayout() {
        invalidateTileWidth();
        invalidateTileHeight();
        super.requestLayout();
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinWidth(double height) {
        if (getContentBias() == Orientation.HORIZONTAL) {
            return getInsets().getLeft() + getTileWidth() + getInsets().getRight();
        }
        return computePrefWidth(height);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computeMinHeight(double width) {
        if (getContentBias() == Orientation.VERTICAL) {
            return getInsets().getTop() + getTileHeight() + getInsets().getBottom();
        }
        return computePrefHeight(width);
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double forHeight) {
        int prefCols;
        List<Node> managed = getManagedChildren();
        Insets insets = getInsets();
        if (forHeight != -1.0d) {
            int prefRows = computeRows((forHeight - snapSpace(insets.getTop())) - snapSpace(insets.getBottom()), getTileHeight());
            prefCols = computeOther(managed.size(), prefRows);
        } else {
            prefCols = getOrientation() == Orientation.HORIZONTAL ? getPrefColumns() : computeOther(managed.size(), getPrefRows());
        }
        return snapSpace(insets.getLeft()) + computeContentWidth(prefCols, getTileWidth()) + snapSpace(insets.getRight());
    }

    @Override // javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double forWidth) {
        int prefRows;
        List<Node> managed = getManagedChildren();
        Insets insets = getInsets();
        if (forWidth != -1.0d) {
            int prefCols = computeColumns((forWidth - snapSpace(insets.getLeft())) - snapSpace(insets.getRight()), getTileWidth());
            prefRows = computeOther(managed.size(), prefCols);
        } else {
            prefRows = getOrientation() == Orientation.HORIZONTAL ? computeOther(managed.size(), getPrefColumns()) : getPrefRows();
        }
        return snapSpace(insets.getTop()) + computeContentHeight(prefRows, getTileHeight()) + snapSpace(insets.getBottom());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double computeTileWidth() {
        List<Node> managed = getManagedChildren();
        double preftilewidth = getPrefTileWidth();
        if (preftilewidth == -1.0d) {
            double h2 = -1.0d;
            boolean vertBias = false;
            int i2 = 0;
            int size = managed.size();
            while (true) {
                if (i2 >= size) {
                    break;
                }
                Node child = managed.get(i2);
                if (child.getContentBias() != Orientation.VERTICAL) {
                    i2++;
                } else {
                    vertBias = true;
                    break;
                }
            }
            if (vertBias) {
                h2 = computeMaxPrefAreaHeight(managed, marginAccessor, -1.0d, getTileAlignmentInternal().getVpos());
            }
            return snapSize(computeMaxPrefAreaWidth(managed, marginAccessor, h2, true));
        }
        return snapSize(preftilewidth);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double computeTileHeight() {
        List<Node> managed = getManagedChildren();
        double preftileheight = getPrefTileHeight();
        if (preftileheight == -1.0d) {
            double w2 = -1.0d;
            boolean horizBias = false;
            int i2 = 0;
            int size = managed.size();
            while (true) {
                if (i2 >= size) {
                    break;
                }
                Node child = managed.get(i2);
                if (child.getContentBias() != Orientation.HORIZONTAL) {
                    i2++;
                } else {
                    horizBias = true;
                    break;
                }
            }
            if (horizBias) {
                w2 = computeMaxPrefAreaWidth(managed, marginAccessor);
            }
            return snapSize(computeMaxPrefAreaHeight(managed, marginAccessor, w2, getTileAlignmentInternal().getVpos()));
        }
        return snapSize(preftileheight);
    }

    private int computeOther(int numNodes, int numCells) {
        double other = numNodes / Math.max(1, numCells);
        return (int) Math.ceil(other);
    }

    private int computeColumns(double width, double tilewidth) {
        return Math.max(1, (int) ((width + snapSpace(getHgap())) / (tilewidth + snapSpace(getHgap()))));
    }

    private int computeRows(double height, double tileheight) {
        return Math.max(1, (int) ((height + snapSpace(getVgap())) / (tileheight + snapSpace(getVgap()))));
    }

    private double computeContentWidth(int columns, double tilewidth) {
        if (columns == 0) {
            return 0.0d;
        }
        return (columns * tilewidth) + ((columns - 1) * snapSpace(getHgap()));
    }

    private double computeContentHeight(int rows, double tileheight) {
        if (rows == 0) {
            return 0.0d;
        }
        return (rows * tileheight) + ((rows - 1) * snapSpace(getVgap()));
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        List<Node> managed = getManagedChildren();
        HPos hpos = getAlignmentInternal().getHpos();
        VPos vpos = getAlignmentInternal().getVpos();
        double width = getWidth();
        double height = getHeight();
        double top = snapSpace(getInsets().getTop());
        double left = snapSpace(getInsets().getLeft());
        double bottom = snapSpace(getInsets().getBottom());
        double right = snapSpace(getInsets().getRight());
        double vgap = snapSpace(getVgap());
        double hgap = snapSpace(getHgap());
        double insideWidth = (width - left) - right;
        double insideHeight = (height - top) - bottom;
        double tileWidth = getTileWidth() > insideWidth ? insideWidth : getTileWidth();
        double tileHeight = getTileHeight() > insideHeight ? insideHeight : getTileHeight();
        int lastRowRemainder = 0;
        int lastColumnRemainder = 0;
        if (getOrientation() == Orientation.HORIZONTAL) {
            this.actualColumns = computeColumns(insideWidth, tileWidth);
            this.actualRows = computeOther(managed.size(), this.actualColumns);
            lastRowRemainder = hpos != HPos.LEFT ? this.actualColumns - ((this.actualColumns * this.actualRows) - managed.size()) : 0;
        } else {
            this.actualRows = computeRows(insideHeight, tileHeight);
            this.actualColumns = computeOther(managed.size(), this.actualRows);
            lastColumnRemainder = vpos != VPos.TOP ? this.actualRows - ((this.actualColumns * this.actualRows) - managed.size()) : 0;
        }
        double rowX = left + computeXOffset(insideWidth, computeContentWidth(this.actualColumns, tileWidth), hpos);
        double columnY = top + computeYOffset(insideHeight, computeContentHeight(this.actualRows, tileHeight), vpos);
        double lastRowX = lastRowRemainder > 0 ? left + computeXOffset(insideWidth, computeContentWidth(lastRowRemainder, tileWidth), hpos) : rowX;
        double lastColumnY = lastColumnRemainder > 0 ? top + computeYOffset(insideHeight, computeContentHeight(lastColumnRemainder, tileHeight), vpos) : columnY;
        double baselineOffset = getTileAlignmentInternal().getVpos() == VPos.BASELINE ? getAreaBaselineOffset(managed, marginAccessor, i2 -> {
            return Double.valueOf(tileWidth);
        }, tileHeight, false) : -1.0d;
        int r2 = 0;
        int c2 = 0;
        int size = managed.size();
        for (int i3 = 0; i3 < size; i3++) {
            Node child = managed.get(i3);
            double xoffset = r2 == this.actualRows - 1 ? lastRowX : rowX;
            double yoffset = c2 == this.actualColumns - 1 ? lastColumnY : columnY;
            double tileX = xoffset + (c2 * (tileWidth + hgap));
            double tileY = yoffset + (r2 * (tileHeight + vgap));
            Pos childAlignment = getAlignment(child);
            layoutInArea(child, tileX, tileY, tileWidth, tileHeight, baselineOffset, getMargin(child), childAlignment != null ? childAlignment.getHpos() : getTileAlignmentInternal().getHpos(), childAlignment != null ? childAlignment.getVpos() : getTileAlignmentInternal().getVpos());
            if (getOrientation() == Orientation.HORIZONTAL) {
                c2++;
                if (c2 == this.actualColumns) {
                    c2 = 0;
                    r2++;
                }
            } else {
                r2++;
                if (r2 == this.actualRows) {
                    r2 = 0;
                    c2++;
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/layout/TilePane$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TilePane, Pos> ALIGNMENT = new CssMetaData<TilePane, Pos>("-fx-alignment", new EnumConverter(Pos.class), Pos.TOP_LEFT) { // from class: javafx.scene.layout.TilePane.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TilePane node) {
                return node.alignment == null || !node.alignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(TilePane node) {
                return (StyleableProperty) node.alignmentProperty();
            }
        };
        private static final CssMetaData<TilePane, Number> PREF_COLUMNS = new CssMetaData<TilePane, Number>("-fx-pref-columns", SizeConverter.getInstance(), Double.valueOf(5.0d)) { // from class: javafx.scene.layout.TilePane.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TilePane node) {
                return node.prefColumns == null || !node.prefColumns.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TilePane node) {
                return (StyleableProperty) node.prefColumnsProperty();
            }
        };
        private static final CssMetaData<TilePane, Number> HGAP = new CssMetaData<TilePane, Number>("-fx-hgap", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.layout.TilePane.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TilePane node) {
                return node.hgap == null || !node.hgap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TilePane node) {
                return (StyleableProperty) node.hgapProperty();
            }
        };
        private static final CssMetaData<TilePane, Number> PREF_ROWS = new CssMetaData<TilePane, Number>("-fx-pref-rows", SizeConverter.getInstance(), Double.valueOf(5.0d)) { // from class: javafx.scene.layout.TilePane.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TilePane node) {
                return node.prefRows == null || !node.prefRows.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TilePane node) {
                return (StyleableProperty) node.prefRowsProperty();
            }
        };
        private static final CssMetaData<TilePane, Pos> TILE_ALIGNMENT = new CssMetaData<TilePane, Pos>("-fx-tile-alignment", new EnumConverter(Pos.class), Pos.CENTER) { // from class: javafx.scene.layout.TilePane.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TilePane node) {
                return node.tileAlignment == null || !node.tileAlignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Pos> getStyleableProperty(TilePane node) {
                return (StyleableProperty) node.tileAlignmentProperty();
            }
        };
        private static final CssMetaData<TilePane, Number> PREF_TILE_WIDTH = new CssMetaData<TilePane, Number>("-fx-pref-tile-width", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.layout.TilePane.StyleableProperties.6
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TilePane node) {
                return node.prefTileWidth == null || !node.prefTileWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TilePane node) {
                return (StyleableProperty) node.prefTileWidthProperty();
            }
        };
        private static final CssMetaData<TilePane, Number> PREF_TILE_HEIGHT = new CssMetaData<TilePane, Number>("-fx-pref-tile-height", SizeConverter.getInstance(), Double.valueOf(-1.0d)) { // from class: javafx.scene.layout.TilePane.StyleableProperties.7
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TilePane node) {
                return node.prefTileHeight == null || !node.prefTileHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TilePane node) {
                return (StyleableProperty) node.prefTileHeightProperty();
            }
        };
        private static final CssMetaData<TilePane, Orientation> ORIENTATION = new CssMetaData<TilePane, Orientation>("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) { // from class: javafx.scene.layout.TilePane.StyleableProperties.8
            @Override // javafx.css.CssMetaData
            public Orientation getInitialValue(TilePane node) {
                return node.getOrientation();
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(TilePane node) {
                return node.orientation == null || !node.orientation.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Orientation> getStyleableProperty(TilePane node) {
                return (StyleableProperty) node.orientationProperty();
            }
        };
        private static final CssMetaData<TilePane, Number> VGAP = new CssMetaData<TilePane, Number>("-fx-vgap", SizeConverter.getInstance(), Double.valueOf(0.0d)) { // from class: javafx.scene.layout.TilePane.StyleableProperties.9
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TilePane node) {
                return node.vgap == null || !node.vgap.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TilePane node) {
                return (StyleableProperty) node.vgapProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Region.getClassCssMetaData());
            styleables.add(ALIGNMENT);
            styleables.add(HGAP);
            styleables.add(ORIENTATION);
            styleables.add(PREF_COLUMNS);
            styleables.add(PREF_ROWS);
            styleables.add(PREF_TILE_WIDTH);
            styleables.add(PREF_TILE_HEIGHT);
            styleables.add(TILE_ALIGNMENT);
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

    /* loaded from: jfxrt.jar:javafx/scene/layout/TilePane$TileSizeProperty.class */
    private abstract class TileSizeProperty extends ReadOnlyDoubleProperty {
        private final String name;
        private ExpressionHelper<Number> helper;
        private double value;
        private boolean valid;

        public abstract double compute();

        TileSizeProperty(String name, double initSize) {
            this.name = name;
            this.value = initSize;
            this.valid = initSize != -1.0d;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return TilePane.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return this.name;
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void addListener(ChangeListener<? super Number> listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void removeListener(ChangeListener<? super Number> listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableDoubleValue
        public double get() {
            if (!this.valid) {
                this.value = compute();
                this.valid = true;
            }
            return this.value;
        }

        public void invalidate() {
            if (this.valid) {
                this.valid = false;
                ExpressionHelper.fireValueChangedEvent(this.helper);
            }
        }
    }
}
