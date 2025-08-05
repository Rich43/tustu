package javax.swing.text;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.BitSet;
import java.util.Vector;
import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/TableView.class */
public abstract class TableView extends BoxView {
    int[] columnSpans;
    int[] columnOffsets;
    SizeRequirements[] columnRequirements;
    Vector<TableRow> rows;
    boolean gridValid;
    private static final BitSet EMPTY = new BitSet();

    /* loaded from: rt.jar:javax/swing/text/TableView$GridCell.class */
    interface GridCell {
        void setGridLocation(int i2, int i3);

        int getGridRow();

        int getGridColumn();

        int getColumnCount();

        int getRowCount();
    }

    public TableView(Element element) {
        super(element, 1);
        this.rows = new Vector<>();
        this.gridValid = false;
    }

    protected TableRow createTableRow(Element element) {
        return new TableRow(element);
    }

    @Deprecated
    protected TableCell createTableCell(Element element) {
        return new TableCell(element);
    }

    int getColumnCount() {
        return this.columnSpans.length;
    }

    int getColumnSpan(int i2) {
        return this.columnSpans[i2];
    }

    int getRowCount() {
        return this.rows.size();
    }

    int getRowSpan(int i2) {
        TableRow row = getRow(i2);
        if (row != null) {
            return (int) row.getPreferredSpan(1);
        }
        return 0;
    }

    TableRow getRow(int i2) {
        if (i2 < this.rows.size()) {
            return this.rows.elementAt(i2);
        }
        return null;
    }

    int getColumnsOccupied(View view) {
        String str = (String) view.getElement().getAttributes().getAttribute(HTML.Attribute.COLSPAN);
        if (str != null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                return 1;
            }
        }
        return 1;
    }

    int getRowsOccupied(View view) {
        String str = (String) view.getElement().getAttributes().getAttribute(HTML.Attribute.ROWSPAN);
        if (str != null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                return 1;
            }
        }
        return 1;
    }

    void invalidateGrid() {
        this.gridValid = false;
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    protected void forwardUpdate(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        Container container;
        super.forwardUpdate(elementChange, documentEvent, shape, viewFactory);
        if (shape != null && (container = getContainer()) != null) {
            Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
            container.repaint(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
        }
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.CompositeView, javax.swing.text.View
    public void replace(int i2, int i3, View[] viewArr) {
        super.replace(i2, i3, viewArr);
        invalidateGrid();
    }

    void updateGrid() {
        if (!this.gridValid) {
            this.rows.removeAllElements();
            int viewCount = getViewCount();
            for (int i2 = 0; i2 < viewCount; i2++) {
                View view = getView(i2);
                if (view instanceof TableRow) {
                    this.rows.addElement((TableRow) view);
                    TableRow tableRow = (TableRow) view;
                    tableRow.clearFilledColumns();
                    tableRow.setRow(i2);
                }
            }
            int iMax = 0;
            int size = this.rows.size();
            for (int i3 = 0; i3 < size; i3++) {
                TableRow row = getRow(i3);
                int i4 = 0;
                int i5 = 0;
                while (i5 < row.getViewCount()) {
                    View view2 = row.getView(i5);
                    while (row.isFilled(i4)) {
                        i4++;
                    }
                    int rowsOccupied = getRowsOccupied(view2);
                    int columnsOccupied = getColumnsOccupied(view2);
                    if (columnsOccupied > 1 || rowsOccupied > 1) {
                        int i6 = i3 + rowsOccupied;
                        int i7 = i4 + columnsOccupied;
                        for (int i8 = i3; i8 < i6; i8++) {
                            for (int i9 = i4; i9 < i7; i9++) {
                                if (i8 != i3 || i9 != i4) {
                                    addFill(i8, i9);
                                }
                            }
                        }
                        if (columnsOccupied > 1) {
                            i4 += columnsOccupied - 1;
                        }
                    }
                    i5++;
                    i4++;
                }
                iMax = Math.max(iMax, i4);
            }
            this.columnSpans = new int[iMax];
            this.columnOffsets = new int[iMax];
            this.columnRequirements = new SizeRequirements[iMax];
            for (int i10 = 0; i10 < iMax; i10++) {
                this.columnRequirements[i10] = new SizeRequirements();
            }
            this.gridValid = true;
        }
    }

    void addFill(int i2, int i3) {
        TableRow row = getRow(i2);
        if (row != null) {
            row.fillColumn(i3);
        }
    }

    protected void layoutColumns(int i2, int[] iArr, int[] iArr2, SizeRequirements[] sizeRequirementsArr) {
        SizeRequirements.calculateTiledPositions(i2, null, sizeRequirementsArr, iArr, iArr2);
    }

    @Override // javax.swing.text.BoxView
    protected void layoutMinorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
        updateGrid();
        int rowCount = getRowCount();
        for (int i4 = 0; i4 < rowCount; i4++) {
            getRow(i4).layoutChanged(i3);
        }
        layoutColumns(i2, this.columnOffsets, this.columnSpans, this.columnRequirements);
        super.layoutMinorAxis(i2, i3, iArr, iArr2);
    }

    @Override // javax.swing.text.BoxView
    protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
        updateGrid();
        calculateColumnRequirements(i2);
        if (sizeRequirements == null) {
            sizeRequirements = new SizeRequirements();
        }
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        for (SizeRequirements sizeRequirements2 : this.columnRequirements) {
            j2 += sizeRequirements2.minimum;
            j3 += sizeRequirements2.preferred;
            j4 += sizeRequirements2.maximum;
        }
        sizeRequirements.minimum = (int) j2;
        sizeRequirements.preferred = (int) j3;
        sizeRequirements.maximum = (int) j4;
        sizeRequirements.alignment = 0.0f;
        return sizeRequirements;
    }

    void calculateColumnRequirements(int i2) {
        boolean z2 = false;
        int rowCount = getRowCount();
        for (int i3 = 0; i3 < rowCount; i3++) {
            TableRow row = getRow(i3);
            int i4 = 0;
            int viewCount = row.getViewCount();
            int i5 = 0;
            while (i5 < viewCount) {
                View view = row.getView(i5);
                while (row.isFilled(i4)) {
                    i4++;
                }
                getRowsOccupied(view);
                int columnsOccupied = getColumnsOccupied(view);
                if (columnsOccupied == 1) {
                    checkSingleColumnCell(i2, i4, view);
                } else {
                    z2 = true;
                    i4 += columnsOccupied - 1;
                }
                i5++;
                i4++;
            }
        }
        if (z2) {
            for (int i6 = 0; i6 < rowCount; i6++) {
                TableRow row2 = getRow(i6);
                int i7 = 0;
                int viewCount2 = row2.getViewCount();
                int i8 = 0;
                while (i8 < viewCount2) {
                    View view2 = row2.getView(i8);
                    while (row2.isFilled(i7)) {
                        i7++;
                    }
                    int columnsOccupied2 = getColumnsOccupied(view2);
                    if (columnsOccupied2 > 1) {
                        checkMultiColumnCell(i2, i7, columnsOccupied2, view2);
                        i7 += columnsOccupied2 - 1;
                    }
                    i8++;
                    i7++;
                }
            }
        }
    }

    void checkSingleColumnCell(int i2, int i3, View view) {
        SizeRequirements sizeRequirements = this.columnRequirements[i3];
        sizeRequirements.minimum = Math.max((int) view.getMinimumSpan(i2), sizeRequirements.minimum);
        sizeRequirements.preferred = Math.max((int) view.getPreferredSpan(i2), sizeRequirements.preferred);
        sizeRequirements.maximum = Math.max((int) view.getMaximumSpan(i2), sizeRequirements.maximum);
    }

    void checkMultiColumnCell(int i2, int i3, int i4, View view) {
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        for (int i5 = 0; i5 < i4; i5++) {
            SizeRequirements sizeRequirements = this.columnRequirements[i3 + i5];
            j2 += sizeRequirements.minimum;
            j3 += sizeRequirements.preferred;
            j4 += sizeRequirements.maximum;
        }
        int minimumSpan = (int) view.getMinimumSpan(i2);
        if (minimumSpan > j2) {
            SizeRequirements[] sizeRequirementsArr = new SizeRequirements[i4];
            for (int i6 = 0; i6 < i4; i6++) {
                SizeRequirements sizeRequirements2 = this.columnRequirements[i3 + i6];
                sizeRequirementsArr[i6] = sizeRequirements2;
                sizeRequirements2.maximum = Math.max(sizeRequirements2.maximum, (int) view.getMaximumSpan(i2));
            }
            int[] iArr = new int[i4];
            SizeRequirements.calculateTiledPositions(minimumSpan, null, sizeRequirementsArr, new int[i4], iArr);
            for (int i7 = 0; i7 < i4; i7++) {
                SizeRequirements sizeRequirements3 = sizeRequirementsArr[i7];
                sizeRequirements3.minimum = Math.max(iArr[i7], sizeRequirements3.minimum);
                sizeRequirements3.preferred = Math.max(sizeRequirements3.minimum, sizeRequirements3.preferred);
                sizeRequirements3.maximum = Math.max(sizeRequirements3.preferred, sizeRequirements3.maximum);
            }
        }
        int preferredSpan = (int) view.getPreferredSpan(i2);
        if (preferredSpan > j3) {
            SizeRequirements[] sizeRequirementsArr2 = new SizeRequirements[i4];
            for (int i8 = 0; i8 < i4; i8++) {
                sizeRequirementsArr2[i8] = this.columnRequirements[i3 + i8];
            }
            int[] iArr2 = new int[i4];
            SizeRequirements.calculateTiledPositions(preferredSpan, null, sizeRequirementsArr2, new int[i4], iArr2);
            for (int i9 = 0; i9 < i4; i9++) {
                SizeRequirements sizeRequirements4 = sizeRequirementsArr2[i9];
                sizeRequirements4.preferred = Math.max(iArr2[i9], sizeRequirements4.preferred);
                sizeRequirements4.maximum = Math.max(sizeRequirements4.preferred, sizeRequirements4.maximum);
            }
        }
    }

    @Override // javax.swing.text.CompositeView
    protected View getViewAtPosition(int i2, Rectangle rectangle) {
        int viewCount = getViewCount();
        for (int i3 = 0; i3 < viewCount; i3++) {
            View view = getView(i3);
            int startOffset = view.getStartOffset();
            int endOffset = view.getEndOffset();
            if (i2 >= startOffset && i2 < endOffset) {
                if (rectangle != null) {
                    childAllocation(i3, rectangle);
                }
                return view;
            }
        }
        if (i2 == getEndOffset()) {
            View view2 = getView(viewCount - 1);
            if (rectangle != null) {
                childAllocation(viewCount - 1, rectangle);
            }
            return view2;
        }
        return null;
    }

    /* loaded from: rt.jar:javax/swing/text/TableView$TableRow.class */
    public class TableRow extends BoxView {
        BitSet fillColumns;
        int row;

        public TableRow(Element element) {
            super(element, 0);
            this.fillColumns = new BitSet();
        }

        void clearFilledColumns() {
            this.fillColumns.and(TableView.EMPTY);
        }

        void fillColumn(int i2) {
            this.fillColumns.set(i2);
        }

        boolean isFilled(int i2) {
            return this.fillColumns.get(i2);
        }

        int getRow() {
            return this.row;
        }

        void setRow(int i2) {
            this.row = i2;
        }

        int getColumnCount() {
            int i2 = 0;
            int size = this.fillColumns.size();
            for (int i3 = 0; i3 < size; i3++) {
                if (this.fillColumns.get(i3)) {
                    i2++;
                }
            }
            return getViewCount() + i2;
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.CompositeView, javax.swing.text.View
        public void replace(int i2, int i3, View[] viewArr) {
            super.replace(i2, i3, viewArr);
            TableView.this.invalidateGrid();
        }

        @Override // javax.swing.text.BoxView
        protected void layoutMajorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
            int i4 = 0;
            int viewCount = getViewCount();
            int i5 = 0;
            while (i5 < viewCount) {
                View view = getView(i5);
                while (isFilled(i4)) {
                    i4++;
                }
                int columnsOccupied = TableView.this.getColumnsOccupied(view);
                iArr2[i5] = TableView.this.columnSpans[i4];
                iArr[i5] = TableView.this.columnOffsets[i4];
                if (columnsOccupied > 1) {
                    int length = TableView.this.columnSpans.length;
                    for (int i6 = 1; i6 < columnsOccupied; i6++) {
                        if (i4 + i6 < length) {
                            int i7 = i5;
                            iArr2[i7] = iArr2[i7] + TableView.this.columnSpans[i4 + i6];
                        }
                    }
                    i4 += columnsOccupied - 1;
                }
                i5++;
                i4++;
            }
        }

        @Override // javax.swing.text.BoxView
        protected void layoutMinorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
            super.layoutMinorAxis(i2, i3, iArr, iArr2);
            int i4 = 0;
            int viewCount = getViewCount();
            int i5 = 0;
            while (i5 < viewCount) {
                View view = getView(i5);
                while (isFilled(i4)) {
                    i4++;
                }
                int columnsOccupied = TableView.this.getColumnsOccupied(view);
                int rowsOccupied = TableView.this.getRowsOccupied(view);
                if (rowsOccupied > 1) {
                    for (int i6 = 1; i6 < rowsOccupied; i6++) {
                        if (getRow() + i6 < TableView.this.getViewCount()) {
                            int i7 = i5;
                            iArr2[i7] = iArr2[i7] + TableView.this.getSpan(1, getRow() + i6);
                        }
                    }
                }
                if (columnsOccupied > 1) {
                    i4 += columnsOccupied - 1;
                }
                i5++;
                i4++;
            }
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public int getResizeWeight(int i2) {
            return 1;
        }

        @Override // javax.swing.text.CompositeView
        protected View getViewAtPosition(int i2, Rectangle rectangle) {
            int viewCount = getViewCount();
            for (int i3 = 0; i3 < viewCount; i3++) {
                View view = getView(i3);
                int startOffset = view.getStartOffset();
                int endOffset = view.getEndOffset();
                if (i2 >= startOffset && i2 < endOffset) {
                    if (rectangle != null) {
                        childAllocation(i3, rectangle);
                    }
                    return view;
                }
            }
            if (i2 == getEndOffset()) {
                View view2 = getView(viewCount - 1);
                if (rectangle != null) {
                    childAllocation(viewCount - 1, rectangle);
                }
                return view2;
            }
            return null;
        }
    }

    @Deprecated
    /* loaded from: rt.jar:javax/swing/text/TableView$TableCell.class */
    public class TableCell extends BoxView implements GridCell {
        int row;
        int col;

        public TableCell(Element element) {
            super(element, 1);
        }

        @Override // javax.swing.text.TableView.GridCell
        public int getColumnCount() {
            return 1;
        }

        @Override // javax.swing.text.TableView.GridCell
        public int getRowCount() {
            return 1;
        }

        @Override // javax.swing.text.TableView.GridCell
        public void setGridLocation(int i2, int i3) {
            this.row = i2;
            this.col = i3;
        }

        @Override // javax.swing.text.TableView.GridCell
        public int getGridRow() {
            return this.row;
        }

        @Override // javax.swing.text.TableView.GridCell
        public int getGridColumn() {
            return this.col;
        }
    }
}
