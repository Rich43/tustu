package com.sun.javafx.scene.control.skin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ResizeFeaturesBase;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException
    */
/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/NestedTableColumnHeader.class */
public class NestedTableColumnHeader extends TableColumnHeader {
    private static final int DRAG_RECT_WIDTH = 4;
    private static final String TABLE_COLUMN_KEY = "TableColumn";
    private static final String TABLE_COLUMN_HEADER_KEY = "TableColumnHeader";
    private ObservableList<? extends TableColumnBase> columns;
    private TableColumnHeader label;
    private ObservableList<TableColumnHeader> columnHeaders;
    private double lastX;
    private double dragAnchorX;
    private Map<TableColumnBase<?, ?>, Rectangle> dragRects;
    boolean updateColumns;
    private final ListChangeListener<TableColumnBase> columnsListener;
    private final WeakListChangeListener weakColumnsListener;
    private static final EventHandler<MouseEvent> rectMousePressed = new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.NestedTableColumnHeader.1
        /* JADX WARN: Failed to check method for inline after forced processcom.sun.javafx.scene.control.skin.NestedTableColumnHeader.access$102(com.sun.javafx.scene.control.skin.NestedTableColumnHeader, double):double */
        @Override // javafx.event.EventHandler
        public void handle(MouseEvent me) {
            Rectangle rect = (Rectangle) me.getSource();
            TableColumnBase column = (TableColumnBase) rect.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader header = (NestedTableColumnHeader) rect.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY);
            if (header.isColumnResizingEnabled()) {
                if (me.getClickCount() == 2 && me.isPrimaryButtonDown()) {
                    header.getTableViewSkin().resizeColumnToFitContent(column, -1);
                } else {
                    Rectangle innerRect = (Rectangle) me.getSource();
                    double startX = header.getTableHeaderRow().sceneToLocal(innerRect.localToScene(innerRect.getBoundsInLocal())).getMinX() + 2.0d;
                    NestedTableColumnHeader.access$102(header, me.getSceneX());
                    header.columnResizingStarted(startX);
                }
                me.consume();
            }
        }
    };
    private static final EventHandler<MouseEvent> rectMouseDragged = new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.NestedTableColumnHeader.2
        @Override // javafx.event.EventHandler
        public void handle(MouseEvent me) {
            Rectangle rect = (Rectangle) me.getSource();
            TableColumnBase column = (TableColumnBase) rect.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader header = (NestedTableColumnHeader) rect.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY);
            if (header.isColumnResizingEnabled()) {
                header.columnResizing(column, me);
                me.consume();
            }
        }
    };
    private static final EventHandler<MouseEvent> rectMouseReleased = new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.NestedTableColumnHeader.3
        @Override // javafx.event.EventHandler
        public void handle(MouseEvent me) {
            Rectangle rect = (Rectangle) me.getSource();
            TableColumnBase column = (TableColumnBase) rect.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader header = (NestedTableColumnHeader) rect.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY);
            if (header.isColumnResizingEnabled()) {
                header.columnResizingComplete(column, me);
                me.consume();
            }
        }
    };
    private static final EventHandler<MouseEvent> rectCursorChangeListener = new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.NestedTableColumnHeader.4
        @Override // javafx.event.EventHandler
        public void handle(MouseEvent me) {
            Rectangle rect = (Rectangle) me.getSource();
            TableColumnBase column = (TableColumnBase) rect.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_KEY);
            NestedTableColumnHeader header = (NestedTableColumnHeader) rect.getProperties().get(NestedTableColumnHeader.TABLE_COLUMN_HEADER_KEY);
            if (header.getCursor() == null) {
                rect.setCursor((header.isColumnResizingEnabled() && rect.isHover() && column.isResizable()) ? Cursor.H_RESIZE : null);
            }
        }
    };

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$102(com.sun.javafx.scene.control.skin.NestedTableColumnHeader r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.dragAnchorX = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.scene.control.skin.NestedTableColumnHeader.access$102(com.sun.javafx.scene.control.skin.NestedTableColumnHeader, double):double");
    }

    public NestedTableColumnHeader(TableViewSkinBase skin, TableColumnBase tc) {
        super(skin, tc);
        this.lastX = 0.0d;
        this.dragAnchorX = 0.0d;
        this.dragRects = new WeakHashMap();
        this.updateColumns = true;
        this.columnsListener = c2 -> {
            setHeadersNeedUpdate();
        };
        this.weakColumnsListener = new WeakListChangeListener(this.columnsListener);
        getStyleClass().setAll("nested-column-header");
        setFocusTraversable(false);
        this.label = new TableColumnHeader(skin, getTableColumn());
        this.label.setTableHeaderRow(getTableHeaderRow());
        this.label.setParentHeader(getParentHeader());
        this.label.setNestedColumnHeader(this);
        if (getTableColumn() != null) {
            this.changeListenerHandler.registerChangeListener(getTableColumn().textProperty(), "TABLE_COLUMN_TEXT");
        }
        this.changeListenerHandler.registerChangeListener(skin.columnResizePolicyProperty(), "TABLE_VIEW_COLUMN_RESIZE_POLICY");
    }

    static {
    }

    @Override // com.sun.javafx.scene.control.skin.TableColumnHeader
    protected void handlePropertyChanged(String p2) {
        super.handlePropertyChanged(p2);
        if ("TABLE_VIEW_COLUMN_RESIZE_POLICY".equals(p2)) {
            updateContent();
        } else if ("TABLE_COLUMN_TEXT".equals(p2)) {
            this.label.setVisible((getTableColumn().getText() == null || getTableColumn().getText().isEmpty()) ? false : true);
        }
    }

    @Override // com.sun.javafx.scene.control.skin.TableColumnHeader
    public void setTableHeaderRow(TableHeaderRow header) {
        super.setTableHeaderRow(header);
        this.label.setTableHeaderRow(header);
        for (TableColumnHeader c2 : getColumnHeaders()) {
            c2.setTableHeaderRow(header);
        }
    }

    @Override // com.sun.javafx.scene.control.skin.TableColumnHeader
    public void setParentHeader(NestedTableColumnHeader parentHeader) {
        super.setParentHeader(parentHeader);
        this.label.setParentHeader(parentHeader);
    }

    ObservableList<? extends TableColumnBase> getColumns() {
        return this.columns;
    }

    void setColumns(ObservableList<? extends TableColumnBase> newColumns) {
        if (this.columns != null) {
            this.columns.removeListener(this.weakColumnsListener);
        }
        this.columns = newColumns;
        if (this.columns != null) {
            this.columns.addListener(this.weakColumnsListener);
        }
    }

    void updateTableColumnHeaders() {
        if (getTableColumn() == null && getTableViewSkin() != null) {
            setColumns(getTableViewSkin().getColumns());
        } else if (getTableColumn() != null) {
            setColumns(getTableColumn().getColumns());
        }
        if (getColumns().isEmpty()) {
            for (int i2 = 0; i2 < getColumnHeaders().size(); i2++) {
                TableColumnHeader header = getColumnHeaders().get(i2);
                header.dispose();
            }
            NestedTableColumnHeader parentHeader = getParentHeader();
            if (parentHeader != null) {
                List<TableColumnHeader> parentColumnHeaders = parentHeader.getColumnHeaders();
                int index = parentColumnHeaders.indexOf(this);
                if (index >= 0 && index < parentColumnHeaders.size()) {
                    parentColumnHeaders.set(index, createColumnHeader(getTableColumn()));
                }
            } else {
                getColumnHeaders().clear();
            }
        } else {
            List<TableColumnHeader> oldHeaders = new ArrayList<>(getColumnHeaders());
            List<TableColumnHeader> newHeaders = new ArrayList<>();
            for (int i3 = 0; i3 < getColumns().size(); i3++) {
                TableColumnBase<?, ?> column = getColumns().get(i3);
                if (column != null && column.isVisible()) {
                    boolean found = false;
                    int j2 = 0;
                    while (true) {
                        if (j2 >= oldHeaders.size()) {
                            break;
                        }
                        TableColumnHeader oldColumn = oldHeaders.get(j2);
                        if (column != oldColumn.getTableColumn()) {
                            j2++;
                        } else {
                            newHeaders.add(oldColumn);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        newHeaders.add(createColumnHeader(column));
                    }
                }
            }
            getColumnHeaders().setAll(newHeaders);
            oldHeaders.removeAll(newHeaders);
            for (int i4 = 0; i4 < oldHeaders.size(); i4++) {
                oldHeaders.get(i4).dispose();
            }
        }
        updateContent();
        for (TableColumnHeader header2 : getColumnHeaders()) {
            header2.applyCss();
        }
    }

    @Override // com.sun.javafx.scene.control.skin.TableColumnHeader
    void dispose() {
        super.dispose();
        if (this.label != null) {
            this.label.dispose();
        }
        if (getColumns() != null) {
            getColumns().removeListener(this.weakColumnsListener);
        }
        for (int i2 = 0; i2 < getColumnHeaders().size(); i2++) {
            TableColumnHeader header = getColumnHeaders().get(i2);
            header.dispose();
        }
        for (Rectangle rect : this.dragRects.values()) {
            if (rect != null) {
                rect.visibleProperty().unbind();
            }
        }
        this.dragRects.clear();
        getChildren().clear();
        this.changeListenerHandler.dispose();
    }

    public ObservableList<TableColumnHeader> getColumnHeaders() {
        if (this.columnHeaders == null) {
            this.columnHeaders = FXCollections.observableArrayList();
        }
        return this.columnHeaders;
    }

    @Override // com.sun.javafx.scene.control.skin.TableColumnHeader, javafx.scene.Parent
    protected void layoutChildren() {
        double w2 = (getWidth() - snappedLeftInset()) - snappedRightInset();
        double h2 = (getHeight() - snappedTopInset()) - snappedBottomInset();
        int labelHeight = (int) this.label.prefHeight(-1.0d);
        if (this.label.isVisible()) {
            this.label.resize(w2, labelHeight);
            this.label.relocate(snappedLeftInset(), snappedTopInset());
        }
        double x2 = snappedLeftInset();
        int max = getColumnHeaders().size();
        for (int i2 = 0; i2 < max; i2++) {
            TableColumnHeader n2 = getColumnHeaders().get(i2);
            if (n2.isVisible()) {
                double prefWidth = snapSize(n2.prefWidth(-1.0d));
                n2.resize(prefWidth, snapSize(h2 - labelHeight));
                n2.relocate(x2, labelHeight + snappedTopInset());
                x2 += prefWidth;
                Rectangle dragRect = this.dragRects.get(n2.getTableColumn());
                if (dragRect != null) {
                    dragRect.setHeight(n2.getDragRectHeight());
                    dragRect.relocate(x2 - 2.0d, snappedTopInset() + labelHeight);
                }
            }
        }
    }

    @Override // com.sun.javafx.scene.control.skin.TableColumnHeader
    double getDragRectHeight() {
        return this.label.prefHeight(-1.0d);
    }

    @Override // com.sun.javafx.scene.control.skin.TableColumnHeader, javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefWidth(double height) {
        checkState();
        double width = 0.0d;
        if (getColumns() != null) {
            for (TableColumnHeader c2 : getColumnHeaders()) {
                if (c2.isVisible()) {
                    width += snapSize(c2.computePrefWidth(height));
                }
            }
        }
        return width;
    }

    @Override // com.sun.javafx.scene.control.skin.TableColumnHeader, javafx.scene.layout.Region, javafx.scene.Parent
    protected double computePrefHeight(double width) {
        checkState();
        double height = 0.0d;
        if (getColumnHeaders() != null) {
            for (TableColumnHeader n2 : getColumnHeaders()) {
                height = Math.max(height, n2.prefHeight(-1.0d));
            }
        }
        return height + this.label.prefHeight(-1.0d) + snappedTopInset() + snappedBottomInset();
    }

    protected TableColumnHeader createTableColumnHeader(TableColumnBase col) {
        if (col.getColumns().isEmpty()) {
            return new TableColumnHeader(getTableViewSkin(), col);
        }
        return new NestedTableColumnHeader(getTableViewSkin(), col);
    }

    protected void setHeadersNeedUpdate() {
        this.updateColumns = true;
        for (int i2 = 0; i2 < getColumnHeaders().size(); i2++) {
            TableColumnHeader header = getColumnHeaders().get(i2);
            if (header instanceof NestedTableColumnHeader) {
                ((NestedTableColumnHeader) header).setHeadersNeedUpdate();
            }
        }
        requestLayout();
    }

    private void updateContent() {
        List<Node> content = new ArrayList<>();
        content.add(this.label);
        content.addAll(getColumnHeaders());
        if (isColumnResizingEnabled()) {
            rebuildDragRects();
            content.addAll(this.dragRects.values());
        }
        getChildren().setAll(content);
    }

    private void rebuildDragRects() {
        boolean zEquals;
        if (isColumnResizingEnabled()) {
            getChildren().removeAll(this.dragRects.values());
            Iterator<Rectangle> it = this.dragRects.values().iterator();
            while (it.hasNext()) {
                it.next().visibleProperty().unbind();
            }
            this.dragRects.clear();
            List<? extends TableColumnBase> columns = getColumns();
            if (columns == null) {
                return;
            }
            TableViewSkinBase<?, ?, ?, ?, ?, ?> skin = getTableViewSkin();
            Callback<ResizeFeaturesBase, Boolean> columnResizePolicy = skin.columnResizePolicyProperty().get();
            if (skin instanceof TableViewSkin) {
                zEquals = TableView.CONSTRAINED_RESIZE_POLICY.equals(columnResizePolicy);
            } else {
                zEquals = skin instanceof TreeTableViewSkin ? TreeTableView.CONSTRAINED_RESIZE_POLICY.equals(columnResizePolicy) : false;
            }
            boolean isConstrainedResize = zEquals;
            if (isConstrainedResize && skin.getVisibleLeafColumns().size() == 1) {
                return;
            }
            for (int col = 0; col < columns.size(); col++) {
                if (!isConstrainedResize || col != getColumns().size() - 1) {
                    TableColumnBase c2 = columns.get(col);
                    Rectangle rect = new Rectangle();
                    rect.getProperties().put(TABLE_COLUMN_KEY, c2);
                    rect.getProperties().put(TABLE_COLUMN_HEADER_KEY, this);
                    rect.setWidth(4.0d);
                    rect.setHeight(getHeight() - this.label.getHeight());
                    rect.setFill(Color.TRANSPARENT);
                    rect.visibleProperty().bind(c2.visibleProperty());
                    rect.setOnMousePressed(rectMousePressed);
                    rect.setOnMouseDragged(rectMouseDragged);
                    rect.setOnMouseReleased(rectMouseReleased);
                    rect.setOnMouseEntered(rectCursorChangeListener);
                    rect.setOnMouseExited(rectCursorChangeListener);
                    this.dragRects.put(c2, rect);
                } else {
                    return;
                }
            }
        }
    }

    private void checkState() {
        if (this.updateColumns) {
            updateTableColumnHeaders();
            this.updateColumns = false;
        }
    }

    private TableColumnHeader createColumnHeader(TableColumnBase col) {
        TableColumnHeader newCol = createTableColumnHeader(col);
        newCol.setTableHeaderRow(getTableHeaderRow());
        newCol.setParentHeader(this);
        return newCol;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isColumnResizingEnabled() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void columnResizingStarted(double startX) {
        setCursor(Cursor.H_RESIZE);
        this.columnReorderLine.setLayoutX(startX);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void columnResizing(TableColumnBase col, MouseEvent me) {
        double draggedX = me.getSceneX() - this.dragAnchorX;
        if (getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            draggedX = -draggedX;
        }
        double delta = draggedX - this.lastX;
        boolean allowed = getTableViewSkin().resizeColumn(col, delta);
        if (allowed) {
            this.lastX = draggedX;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void columnResizingComplete(TableColumnBase col, MouseEvent me) {
        setCursor(null);
        this.columnReorderLine.setTranslateX(0.0d);
        this.columnReorderLine.setLayoutX(0.0d);
        this.lastX = 0.0d;
    }
}
