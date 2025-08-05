package javax.swing.text.html;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Vector;
import javax.swing.JSplitPane;
import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import javax.swing.text.html.StyleSheet;

/* loaded from: rt.jar:javax/swing/text/html/TableView.class */
class TableView extends BoxView implements ViewFactory {
    private AttributeSet attr;
    private StyleSheet.BoxPainter painter;
    private int cellSpacing;
    private int borderWidth;
    private int captionIndex;
    private boolean relativeCells;
    private boolean multiRowCells;
    int[] columnSpans;
    int[] columnOffsets;
    SizeRequirements totalColumnRequirements;
    SizeRequirements[] columnRequirements;
    RowIterator rowIterator;
    ColumnIterator colIterator;
    Vector<RowView> rows;
    boolean skipComments;
    boolean gridValid;
    private static final BitSet EMPTY = new BitSet();

    public TableView(Element element) {
        super(element, 1);
        this.rowIterator = new RowIterator();
        this.colIterator = new ColumnIterator();
        this.skipComments = false;
        this.rows = new Vector<>();
        this.gridValid = false;
        this.captionIndex = -1;
        this.totalColumnRequirements = new SizeRequirements();
    }

    protected RowView createTableRow(Element element) {
        if (element.getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.TR) {
            return new RowView(element);
        }
        return null;
    }

    public int getColumnCount() {
        return this.columnSpans.length;
    }

    public int getColumnSpan(int i2) {
        if (i2 < this.columnSpans.length) {
            return this.columnSpans[i2];
        }
        return 0;
    }

    public int getRowCount() {
        return this.rows.size();
    }

    public int getMultiRowSpan(int i2, int i3) {
        RowView row = getRow(i2);
        RowView row2 = getRow(i3);
        if (row != null && row2 != null) {
            int i4 = row.viewIndex;
            int i5 = row2.viewIndex;
            return (getOffset(1, i5) - getOffset(1, i4)) + getSpan(1, i5);
        }
        return 0;
    }

    public int getRowSpan(int i2) {
        RowView row = getRow(i2);
        if (row != null) {
            return getSpan(1, row.viewIndex);
        }
        return 0;
    }

    RowView getRow(int i2) {
        if (i2 < this.rows.size()) {
            return this.rows.elementAt(i2);
        }
        return null;
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.CompositeView
    protected View getViewAtPoint(int i2, int i3, Rectangle rectangle) {
        View viewFindViewAtPoint;
        int viewCount = getViewCount();
        Rectangle rectangle2 = new Rectangle();
        for (int i4 = 0; i4 < viewCount; i4++) {
            rectangle2.setBounds(rectangle);
            childAllocation(i4, rectangle2);
            View view = getView(i4);
            if ((view instanceof RowView) && (viewFindViewAtPoint = ((RowView) view).findViewAtPoint(i2, i3, rectangle2)) != null) {
                rectangle.setBounds(rectangle2);
                return viewFindViewAtPoint;
            }
        }
        return super.getViewAtPoint(i2, i3, rectangle);
    }

    protected int getColumnsOccupied(View view) {
        String str;
        AttributeSet attributes = view.getElement().getAttributes();
        if (attributes.isDefined(HTML.Attribute.COLSPAN) && (str = (String) attributes.getAttribute(HTML.Attribute.COLSPAN)) != null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                return 1;
            }
        }
        return 1;
    }

    protected int getRowsOccupied(View view) {
        String str;
        AttributeSet attributes = view.getElement().getAttributes();
        if (attributes.isDefined(HTML.Attribute.ROWSPAN) && (str = (String) attributes.getAttribute(HTML.Attribute.ROWSPAN)) != null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                return 1;
            }
        }
        return 1;
    }

    protected void invalidateGrid() {
        this.gridValid = false;
    }

    protected StyleSheet getStyleSheet() {
        return ((HTMLDocument) getDocument()).getStyleSheet();
    }

    void updateInsets() {
        short inset = (short) this.painter.getInset(1, this);
        short inset2 = (short) this.painter.getInset(3, this);
        if (this.captionIndex != -1) {
            short preferredSpan = (short) r0.getPreferredSpan(1);
            Object attribute = getView(this.captionIndex).getAttributes().getAttribute(CSS.Attribute.CAPTION_SIDE);
            if (attribute != null && attribute.equals(JSplitPane.BOTTOM)) {
                inset2 = (short) (inset2 + preferredSpan);
            } else {
                inset = (short) (inset + preferredSpan);
            }
        }
        setInsets(inset, (short) this.painter.getInset(2, this), inset2, (short) this.painter.getInset(4, this));
    }

    protected void setPropertiesFromAttributes() {
        StyleSheet styleSheet = getStyleSheet();
        this.attr = styleSheet.getViewAttributes(this);
        this.painter = styleSheet.getBoxPainter(this.attr);
        if (this.attr != null) {
            setInsets((short) this.painter.getInset(1, this), (short) this.painter.getInset(2, this), (short) this.painter.getInset(3, this), (short) this.painter.getInset(4, this));
            CSS.LengthValue lengthValue = (CSS.LengthValue) this.attr.getAttribute(CSS.Attribute.BORDER_SPACING);
            if (lengthValue != null) {
                this.cellSpacing = (int) lengthValue.getValue();
            } else {
                this.cellSpacing = 2;
            }
            CSS.LengthValue lengthValue2 = (CSS.LengthValue) this.attr.getAttribute(CSS.Attribute.BORDER_TOP_WIDTH);
            if (lengthValue2 != null) {
                this.borderWidth = (int) lengthValue2.getValue();
            } else {
                this.borderWidth = 0;
            }
        }
    }

    void updateGrid() {
        CSS.LengthValue lengthValue;
        if (!this.gridValid) {
            this.relativeCells = false;
            this.multiRowCells = false;
            this.captionIndex = -1;
            this.rows.removeAllElements();
            int viewCount = getViewCount();
            for (int i2 = 0; i2 < viewCount; i2++) {
                View view = getView(i2);
                if (view instanceof RowView) {
                    this.rows.addElement((RowView) view);
                    RowView rowView = (RowView) view;
                    rowView.clearFilledColumns();
                    rowView.rowIndex = this.rows.size() - 1;
                    rowView.viewIndex = i2;
                } else {
                    Object attribute = view.getElement().getAttributes().getAttribute(StyleConstants.NameAttribute);
                    if ((attribute instanceof HTML.Tag) && ((HTML.Tag) attribute) == HTML.Tag.CAPTION) {
                        this.captionIndex = i2;
                    }
                }
            }
            int iMax = 0;
            int size = this.rows.size();
            for (int i3 = 0; i3 < size; i3++) {
                RowView row = getRow(i3);
                int i4 = 0;
                int i5 = 0;
                while (i5 < row.getViewCount()) {
                    View view2 = row.getView(i5);
                    if (!this.relativeCells && (lengthValue = (CSS.LengthValue) view2.getAttributes().getAttribute(CSS.Attribute.WIDTH)) != null && lengthValue.isPercentage()) {
                        this.relativeCells = true;
                    }
                    while (row.isFilled(i4)) {
                        i4++;
                    }
                    int rowsOccupied = getRowsOccupied(view2);
                    if (rowsOccupied > 1) {
                        this.multiRowCells = true;
                    }
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
                this.columnRequirements[i10].maximum = Integer.MAX_VALUE;
            }
            this.gridValid = true;
        }
    }

    void addFill(int i2, int i3) {
        RowView row = getRow(i2);
        if (row != null) {
            row.fillColumn(i3);
        }
    }

    protected void layoutColumns(int i2, int[] iArr, int[] iArr2, SizeRequirements[] sizeRequirementsArr) {
        Arrays.fill(iArr, 0);
        Arrays.fill(iArr2, 0);
        this.colIterator.setLayoutArrays(iArr, iArr2, i2);
        CSS.calculateTiledLayout(this.colIterator, i2);
    }

    void calculateColumnRequirements(int i2) {
        for (SizeRequirements sizeRequirements : this.columnRequirements) {
            sizeRequirements.minimum = 0;
            sizeRequirements.preferred = 0;
            sizeRequirements.maximum = Integer.MAX_VALUE;
        }
        Container container = getContainer();
        if (container != null) {
            if (container instanceof JTextComponent) {
                this.skipComments = !((JTextComponent) container).isEditable();
            } else {
                this.skipComments = true;
            }
        }
        boolean z2 = false;
        int rowCount = getRowCount();
        for (int i3 = 0; i3 < rowCount; i3++) {
            RowView row = getRow(i3);
            int i4 = 0;
            int viewCount = row.getViewCount();
            for (int i5 = 0; i5 < viewCount; i5++) {
                View view = row.getView(i5);
                if (!this.skipComments || (view instanceof CellView)) {
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
                    i4++;
                }
            }
        }
        if (z2) {
            for (int i6 = 0; i6 < rowCount; i6++) {
                RowView row2 = getRow(i6);
                int i7 = 0;
                int viewCount2 = row2.getViewCount();
                for (int i8 = 0; i8 < viewCount2; i8++) {
                    View view2 = row2.getView(i8);
                    if (!this.skipComments || (view2 instanceof CellView)) {
                        while (row2.isFilled(i7)) {
                            i7++;
                        }
                        int columnsOccupied2 = getColumnsOccupied(view2);
                        if (columnsOccupied2 > 1) {
                            checkMultiColumnCell(i2, i7, columnsOccupied2, view2);
                            i7 += columnsOccupied2 - 1;
                        }
                        i7++;
                    }
                }
            }
        }
    }

    void checkSingleColumnCell(int i2, int i3, View view) {
        SizeRequirements sizeRequirements = this.columnRequirements[i3];
        sizeRequirements.minimum = Math.max((int) view.getMinimumSpan(i2), sizeRequirements.minimum);
        sizeRequirements.preferred = Math.max((int) view.getPreferredSpan(i2), sizeRequirements.preferred);
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
                sizeRequirementsArr[i6] = this.columnRequirements[i3 + i6];
            }
            int[] iArr = new int[i4];
            SizeRequirements.calculateTiledPositions(minimumSpan, null, sizeRequirementsArr, new int[i4], iArr);
            for (int i7 = 0; i7 < i4; i7++) {
                SizeRequirements sizeRequirements2 = sizeRequirementsArr[i7];
                sizeRequirements2.minimum = Math.max(iArr[i7], sizeRequirements2.minimum);
                sizeRequirements2.preferred = Math.max(sizeRequirements2.minimum, sizeRequirements2.preferred);
                sizeRequirements2.maximum = Math.max(sizeRequirements2.preferred, sizeRequirements2.maximum);
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
                SizeRequirements sizeRequirements3 = sizeRequirementsArr2[i9];
                sizeRequirements3.preferred = Math.max(iArr2[i9], sizeRequirements3.preferred);
                sizeRequirements3.maximum = Math.max(sizeRequirements3.preferred, sizeRequirements3.maximum);
            }
        }
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
        int length = this.columnRequirements.length;
        for (int i3 = 0; i3 < length; i3++) {
            SizeRequirements sizeRequirements2 = this.columnRequirements[i3];
            j2 += sizeRequirements2.minimum;
            j3 += sizeRequirements2.preferred;
        }
        int i4 = ((length + 1) * this.cellSpacing) + (2 * this.borderWidth);
        long j4 = j2 + i4;
        long j5 = j3 + i4;
        sizeRequirements.minimum = (int) j4;
        sizeRequirements.preferred = (int) j5;
        sizeRequirements.maximum = (int) j5;
        AttributeSet attributes = getAttributes();
        if (BlockView.spanSetFromAttributes(i2, sizeRequirements, (CSS.LengthValue) attributes.getAttribute(CSS.Attribute.WIDTH), null) && sizeRequirements.minimum < ((int) j4)) {
            int i5 = (int) j4;
            sizeRequirements.preferred = i5;
            sizeRequirements.minimum = i5;
            sizeRequirements.maximum = i5;
        }
        this.totalColumnRequirements.minimum = sizeRequirements.minimum;
        this.totalColumnRequirements.preferred = sizeRequirements.preferred;
        this.totalColumnRequirements.maximum = sizeRequirements.maximum;
        Object attribute = attributes.getAttribute(CSS.Attribute.TEXT_ALIGN);
        if (attribute != null) {
            String string = attribute.toString();
            if (string.equals(JSplitPane.LEFT)) {
                sizeRequirements.alignment = 0.0f;
            } else if (string.equals("center")) {
                sizeRequirements.alignment = 0.5f;
            } else if (string.equals(JSplitPane.RIGHT)) {
                sizeRequirements.alignment = 1.0f;
            } else {
                sizeRequirements.alignment = 0.0f;
            }
        } else {
            sizeRequirements.alignment = 0.0f;
        }
        return sizeRequirements;
    }

    @Override // javax.swing.text.BoxView
    protected SizeRequirements calculateMajorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
        updateInsets();
        this.rowIterator.updateAdjustments();
        SizeRequirements sizeRequirementsCalculateTiledRequirements = CSS.calculateTiledRequirements(this.rowIterator, sizeRequirements);
        sizeRequirementsCalculateTiledRequirements.maximum = sizeRequirementsCalculateTiledRequirements.preferred;
        return sizeRequirementsCalculateTiledRequirements;
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
    protected void layoutMajorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
        this.rowIterator.setLayoutArrays(iArr, iArr2);
        CSS.calculateTiledLayout(this.rowIterator, i2);
        if (this.captionIndex != -1) {
            iArr2[this.captionIndex] = (int) getView(this.captionIndex).getPreferredSpan(1);
            short inset = (short) this.painter.getInset(3, this);
            if (inset != getBottomInset()) {
                iArr[this.captionIndex] = i2 + inset;
            } else {
                iArr[this.captionIndex] = -getTopInset();
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

    @Override // javax.swing.text.View
    public AttributeSet getAttributes() {
        if (this.attr == null) {
            this.attr = getStyleSheet().getViewAttributes(this);
        }
        return this.attr;
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Rectangle bounds = shape.getBounds();
        setSize(bounds.width, bounds.height);
        if (this.captionIndex != -1) {
            short inset = (short) this.painter.getInset(1, this);
            short inset2 = (short) this.painter.getInset(3, this);
            if (inset != getTopInset()) {
                int topInset = getTopInset() - inset;
                bounds.f12373y += topInset;
                bounds.height -= topInset;
            } else {
                bounds.height -= getBottomInset() - inset2;
            }
        }
        this.painter.paint(graphics, bounds.f12372x, bounds.f12373y, bounds.width, bounds.height, this);
        int viewCount = getViewCount();
        for (int i2 = 0; i2 < viewCount; i2++) {
            getView(i2).paint(graphics, getChildAllocation(i2, shape));
        }
    }

    @Override // javax.swing.text.CompositeView, javax.swing.text.View
    public void setParent(View view) {
        super.setParent(view);
        if (view != null) {
            setPropertiesFromAttributes();
        }
    }

    @Override // javax.swing.text.View
    public ViewFactory getViewFactory() {
        return this;
    }

    @Override // javax.swing.text.View
    public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.insertUpdate(documentEvent, shape, this);
    }

    @Override // javax.swing.text.View
    public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.removeUpdate(documentEvent, shape, this);
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.changedUpdate(documentEvent, shape, this);
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

    @Override // javax.swing.text.ViewFactory
    public View create(Element element) {
        ViewFactory viewFactory;
        Object attribute = element.getAttributes().getAttribute(StyleConstants.NameAttribute);
        if (attribute instanceof HTML.Tag) {
            HTML.Tag tag = (HTML.Tag) attribute;
            if (tag == HTML.Tag.TR) {
                return createTableRow(element);
            }
            if (tag == HTML.Tag.TD || tag == HTML.Tag.TH) {
                return new CellView(element);
            }
            if (tag == HTML.Tag.CAPTION) {
                return new ParagraphView(element);
            }
        }
        View parent = getParent();
        if (parent != null && (viewFactory = parent.getViewFactory()) != null) {
            return viewFactory.create(element);
        }
        return null;
    }

    /* loaded from: rt.jar:javax/swing/text/html/TableView$ColumnIterator.class */
    class ColumnIterator implements CSS.LayoutIterator {
        private int col;
        private int[] percentages;
        private int[] adjustmentWeights;
        private int[] offsets;
        private int[] spans;

        ColumnIterator() {
        }

        void disablePercentages() {
            this.percentages = null;
        }

        private void updatePercentagesAndAdjustmentWeights(int i2) {
            this.adjustmentWeights = new int[TableView.this.columnRequirements.length];
            for (int i3 = 0; i3 < TableView.this.columnRequirements.length; i3++) {
                this.adjustmentWeights[i3] = 0;
            }
            if (TableView.this.relativeCells) {
                this.percentages = new int[TableView.this.columnRequirements.length];
            } else {
                this.percentages = null;
            }
            int rowCount = TableView.this.getRowCount();
            for (int i4 = 0; i4 < rowCount; i4++) {
                RowView row = TableView.this.getRow(i4);
                int i5 = 0;
                int viewCount = row.getViewCount();
                int i6 = 0;
                while (i6 < viewCount) {
                    View view = row.getView(i6);
                    while (row.isFilled(i5)) {
                        i5++;
                    }
                    TableView.this.getRowsOccupied(view);
                    int columnsOccupied = TableView.this.getColumnsOccupied(view);
                    CSS.LengthValue lengthValue = (CSS.LengthValue) view.getAttributes().getAttribute(CSS.Attribute.WIDTH);
                    if (lengthValue != null) {
                        int value = (int) ((lengthValue.getValue(i2) / columnsOccupied) + 0.5f);
                        for (int i7 = 0; i7 < columnsOccupied; i7++) {
                            if (lengthValue.isPercentage()) {
                                this.percentages[i5 + i7] = Math.max(this.percentages[i5 + i7], value);
                                this.adjustmentWeights[i5 + i7] = Math.max(this.adjustmentWeights[i5 + i7], 2);
                            } else {
                                this.adjustmentWeights[i5 + i7] = Math.max(this.adjustmentWeights[i5 + i7], 1);
                            }
                        }
                    }
                    i6++;
                    i5 = i5 + (columnsOccupied - 1) + 1;
                }
            }
        }

        public void setLayoutArrays(int[] iArr, int[] iArr2, int i2) {
            this.offsets = iArr;
            this.spans = iArr2;
            updatePercentagesAndAdjustmentWeights(i2);
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public int getCount() {
            return TableView.this.columnRequirements.length;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public void setIndex(int i2) {
            this.col = i2;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public void setOffset(int i2) {
            this.offsets[this.col] = i2;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public int getOffset() {
            return this.offsets[this.col];
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public void setSpan(int i2) {
            this.spans[this.col] = i2;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public int getSpan() {
            return this.spans[this.col];
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getMinimumSpan(float f2) {
            return TableView.this.columnRequirements[this.col].minimum;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getPreferredSpan(float f2) {
            if (this.percentages != null && this.percentages[this.col] != 0) {
                return Math.max(this.percentages[this.col], TableView.this.columnRequirements[this.col].minimum);
            }
            return TableView.this.columnRequirements[this.col].preferred;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getMaximumSpan(float f2) {
            return TableView.this.columnRequirements[this.col].maximum;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getBorderWidth() {
            return TableView.this.borderWidth;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getLeadingCollapseSpan() {
            return TableView.this.cellSpacing;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getTrailingCollapseSpan() {
            return TableView.this.cellSpacing;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public int getAdjustmentWeight() {
            return this.adjustmentWeights[this.col];
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/TableView$RowIterator.class */
    class RowIterator implements CSS.LayoutIterator {
        private int row;
        private int[] adjustments;
        private int[] offsets;
        private int[] spans;

        RowIterator() {
        }

        void updateAdjustments() {
            if (TableView.this.multiRowCells) {
                int rowCount = TableView.this.getRowCount();
                this.adjustments = new int[rowCount];
                for (int i2 = 0; i2 < rowCount; i2++) {
                    RowView row = TableView.this.getRow(i2);
                    if (row.multiRowCells) {
                        int viewCount = row.getViewCount();
                        for (int i3 = 0; i3 < viewCount; i3++) {
                            View view = row.getView(i3);
                            int rowsOccupied = TableView.this.getRowsOccupied(view);
                            if (rowsOccupied > 1) {
                                adjustMultiRowSpan((int) view.getPreferredSpan(1), rowsOccupied, i2);
                            }
                        }
                    }
                }
                return;
            }
            this.adjustments = null;
        }

        void adjustMultiRowSpan(int i2, int i3, int i4) {
            if (i4 + i3 > getCount()) {
                i3 = getCount() - i4;
                if (i3 < 1) {
                    return;
                }
            }
            int preferredSpan = 0;
            for (int i5 = 0; i5 < i3; i5++) {
                preferredSpan = (int) (preferredSpan + TableView.this.getRow(i4 + i5).getPreferredSpan(1));
            }
            if (i2 > preferredSpan) {
                int i6 = i2 - preferredSpan;
                int i7 = i6 / i3;
                int i8 = i7 + (i6 - (i7 * i3));
                TableView.this.getRow(i4);
                this.adjustments[i4] = Math.max(this.adjustments[i4], i8);
                for (int i9 = 1; i9 < i3; i9++) {
                    this.adjustments[i4 + i9] = Math.max(this.adjustments[i4 + i9], i7);
                }
            }
        }

        void setLayoutArrays(int[] iArr, int[] iArr2) {
            this.offsets = iArr;
            this.spans = iArr2;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public void setOffset(int i2) {
            RowView row = TableView.this.getRow(this.row);
            if (row != null) {
                this.offsets[row.viewIndex] = i2;
            }
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public int getOffset() {
            RowView row = TableView.this.getRow(this.row);
            if (row != null) {
                return this.offsets[row.viewIndex];
            }
            return 0;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public void setSpan(int i2) {
            RowView row = TableView.this.getRow(this.row);
            if (row != null) {
                this.spans[row.viewIndex] = i2;
            }
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public int getSpan() {
            RowView row = TableView.this.getRow(this.row);
            if (row != null) {
                return this.spans[row.viewIndex];
            }
            return 0;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public int getCount() {
            return TableView.this.rows.size();
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public void setIndex(int i2) {
            this.row = i2;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getMinimumSpan(float f2) {
            return getPreferredSpan(f2);
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getPreferredSpan(float f2) {
            RowView row = TableView.this.getRow(this.row);
            if (row != null) {
                return row.getPreferredSpan(TableView.this.getAxis()) + (this.adjustments != null ? this.adjustments[this.row] : 0);
            }
            return 0.0f;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getMaximumSpan(float f2) {
            return getPreferredSpan(f2);
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getBorderWidth() {
            return TableView.this.borderWidth;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getLeadingCollapseSpan() {
            return TableView.this.cellSpacing;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public float getTrailingCollapseSpan() {
            return TableView.this.cellSpacing;
        }

        @Override // javax.swing.text.html.CSS.LayoutIterator
        public int getAdjustmentWeight() {
            return 0;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/TableView$RowView.class */
    public class RowView extends BoxView {
        private StyleSheet.BoxPainter painter;
        private AttributeSet attr;
        BitSet fillColumns;
        int rowIndex;
        int viewIndex;
        boolean multiRowCells;

        public RowView(Element element) {
            super(element, 0);
            this.fillColumns = new BitSet();
            setPropertiesFromAttributes();
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

        @Override // javax.swing.text.View
        public AttributeSet getAttributes() {
            return this.attr;
        }

        View findViewAtPoint(int i2, int i3, Rectangle rectangle) {
            int viewCount = getViewCount();
            for (int i4 = 0; i4 < viewCount; i4++) {
                if (getChildAllocation(i4, rectangle).contains(i2, i3)) {
                    childAllocation(i4, rectangle);
                    return getView(i4);
                }
            }
            return null;
        }

        protected StyleSheet getStyleSheet() {
            return ((HTMLDocument) getDocument()).getStyleSheet();
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public void preferenceChanged(View view, boolean z2, boolean z3) {
            super.preferenceChanged(view, z2, z3);
            if (TableView.this.multiRowCells && z3) {
                for (int i2 = this.rowIndex - 1; i2 >= 0; i2--) {
                    RowView row = TableView.this.getRow(i2);
                    if (row.multiRowCells) {
                        row.preferenceChanged(null, false, true);
                        return;
                    }
                }
            }
        }

        @Override // javax.swing.text.BoxView
        protected SizeRequirements calculateMajorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
            SizeRequirements sizeRequirements2 = new SizeRequirements();
            sizeRequirements2.minimum = TableView.this.totalColumnRequirements.minimum;
            sizeRequirements2.maximum = TableView.this.totalColumnRequirements.maximum;
            sizeRequirements2.preferred = TableView.this.totalColumnRequirements.preferred;
            sizeRequirements2.alignment = 0.0f;
            return sizeRequirements2;
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public float getMinimumSpan(int i2) {
            float minimumSpan;
            if (i2 == 0) {
                minimumSpan = TableView.this.totalColumnRequirements.minimum + getLeftInset() + getRightInset();
            } else {
                minimumSpan = super.getMinimumSpan(i2);
            }
            return minimumSpan;
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public float getMaximumSpan(int i2) {
            float maximumSpan;
            if (i2 == 0) {
                maximumSpan = 2.1474836E9f;
            } else {
                maximumSpan = super.getMaximumSpan(i2);
            }
            return maximumSpan;
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public float getPreferredSpan(int i2) {
            float preferredSpan;
            if (i2 == 0) {
                preferredSpan = TableView.this.totalColumnRequirements.preferred + getLeftInset() + getRightInset();
            } else {
                preferredSpan = super.getPreferredSpan(i2);
            }
            return preferredSpan;
        }

        @Override // javax.swing.text.View
        public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            super.changedUpdate(documentEvent, shape, viewFactory);
            int offset = documentEvent.getOffset();
            if (offset <= getStartOffset() && offset + documentEvent.getLength() >= getEndOffset()) {
                setPropertiesFromAttributes();
            }
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public void paint(Graphics graphics, Shape shape) {
            this.painter.paint(graphics, r0.f12372x, r0.f12373y, r0.width, r0.height, this);
            super.paint(graphics, (Rectangle) shape);
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.CompositeView, javax.swing.text.View
        public void replace(int i2, int i3, View[] viewArr) {
            super.replace(i2, i3, viewArr);
            TableView.this.invalidateGrid();
        }

        @Override // javax.swing.text.BoxView
        protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
            long jMax;
            long jMax2 = 0;
            long jMax3 = 0;
            long j2 = 0;
            this.multiRowCells = false;
            int viewCount = getViewCount();
            for (int i3 = 0; i3 < viewCount; i3++) {
                if (TableView.this.getRowsOccupied(getView(i3)) > 1) {
                    this.multiRowCells = true;
                    jMax = Math.max((int) r0.getMaximumSpan(i2), j2);
                } else {
                    jMax2 = Math.max((int) r0.getMinimumSpan(i2), jMax2);
                    jMax3 = Math.max((int) r0.getPreferredSpan(i2), jMax3);
                    jMax = Math.max((int) r0.getMaximumSpan(i2), j2);
                }
                j2 = jMax;
            }
            if (sizeRequirements == null) {
                sizeRequirements = new SizeRequirements();
                sizeRequirements.alignment = 0.5f;
            }
            sizeRequirements.preferred = (int) jMax3;
            sizeRequirements.minimum = (int) jMax2;
            sizeRequirements.maximum = (int) j2;
            return sizeRequirements;
        }

        @Override // javax.swing.text.BoxView
        protected void layoutMajorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
            int i4 = 0;
            int viewCount = getViewCount();
            for (int i5 = 0; i5 < viewCount; i5++) {
                View view = getView(i5);
                if (!TableView.this.skipComments || (view instanceof CellView)) {
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
                                int i8 = i5;
                                iArr2[i8] = iArr2[i8] + TableView.this.cellSpacing;
                            }
                        }
                        i4 += columnsOccupied - 1;
                    }
                    i4++;
                }
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
                    iArr2[i5] = TableView.this.getMultiRowSpan(this.rowIndex, Math.min((this.rowIndex + rowsOccupied) - 1, TableView.this.getRowCount() - 1));
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

        void setPropertiesFromAttributes() {
            StyleSheet styleSheet = getStyleSheet();
            this.attr = styleSheet.getViewAttributes(this);
            this.painter = styleSheet.getBoxPainter(this.attr);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/TableView$CellView.class */
    class CellView extends BlockView {
        public CellView(Element element) {
            super(element, 1);
        }

        @Override // javax.swing.text.BoxView
        protected void layoutMajorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
            super.layoutMajorAxis(i2, i3, iArr, iArr2);
            int i4 = 0;
            int length = iArr2.length;
            for (int i5 : iArr2) {
                i4 += i5;
            }
            int i6 = 0;
            if (i4 < i2) {
                String str = (String) getElement().getAttributes().getAttribute(HTML.Attribute.VALIGN);
                if (str == null) {
                    str = (String) getElement().getParentElement().getAttributes().getAttribute(HTML.Attribute.VALIGN);
                }
                if (str == null || str.equals("middle")) {
                    i6 = (i2 - i4) / 2;
                } else if (str.equals(JSplitPane.BOTTOM)) {
                    i6 = i2 - i4;
                }
            }
            if (i6 != 0) {
                for (int i7 = 0; i7 < length; i7++) {
                    int i8 = i7;
                    iArr[i8] = iArr[i8] + i6;
                }
            }
        }

        @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView
        protected SizeRequirements calculateMajorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
            SizeRequirements sizeRequirementsCalculateMajorAxisRequirements = super.calculateMajorAxisRequirements(i2, sizeRequirements);
            sizeRequirementsCalculateMajorAxisRequirements.maximum = Integer.MAX_VALUE;
            return sizeRequirementsCalculateMajorAxisRequirements;
        }

        @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView
        protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
            SizeRequirements sizeRequirementsCalculateMinorAxisRequirements = super.calculateMinorAxisRequirements(i2, sizeRequirements);
            int viewCount = getViewCount();
            int iMax = 0;
            for (int i3 = 0; i3 < viewCount; i3++) {
                iMax = Math.max((int) getView(i3).getMinimumSpan(i2), iMax);
            }
            sizeRequirementsCalculateMinorAxisRequirements.minimum = Math.min(sizeRequirementsCalculateMinorAxisRequirements.minimum, iMax);
            return sizeRequirementsCalculateMinorAxisRequirements;
        }
    }
}
