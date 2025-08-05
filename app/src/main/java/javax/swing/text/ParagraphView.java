package javax.swing.text;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextAttribute;
import java.util.Arrays;
import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.text.FlowView;
import javax.swing.text.GlyphView;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/ParagraphView.class */
public class ParagraphView extends FlowView implements TabExpander {
    private int justification;
    private float lineSpacing;
    protected int firstLineIndent;
    private int tabBase;
    static Class i18nStrategy;
    static char[] tabChars = new char[1];
    static char[] tabDecimalChars;

    public ParagraphView(Element element) {
        super(element, 1);
        this.firstLineIndent = 0;
        setPropertiesFromAttributes();
        Object property = element.getDocument().getProperty("i18n");
        if (property != null && property.equals(Boolean.TRUE)) {
            try {
                if (i18nStrategy == null) {
                    ClassLoader classLoader = getClass().getClassLoader();
                    if (classLoader != null) {
                        i18nStrategy = classLoader.loadClass("javax.swing.text.TextLayoutStrategy");
                    } else {
                        i18nStrategy = Class.forName("javax.swing.text.TextLayoutStrategy");
                    }
                }
                Object objNewInstance = i18nStrategy.newInstance();
                if (objNewInstance instanceof FlowView.FlowStrategy) {
                    this.strategy = (FlowView.FlowStrategy) objNewInstance;
                }
            } catch (Throwable th) {
                throw new StateInvariantError("ParagraphView: Can't create i18n strategy: " + th.getMessage());
            }
        }
    }

    protected void setJustification(int i2) {
        this.justification = i2;
    }

    protected void setLineSpacing(float f2) {
        this.lineSpacing = f2;
    }

    protected void setFirstLineIndent(float f2) {
        this.firstLineIndent = (int) f2;
    }

    protected void setPropertiesFromAttributes() {
        int iIntValue;
        AttributeSet attributes = getAttributes();
        if (attributes != null) {
            setParagraphInsets(attributes);
            Integer num = (Integer) attributes.getAttribute(StyleConstants.Alignment);
            if (num == null) {
                Object property = getElement().getDocument().getProperty(TextAttribute.RUN_DIRECTION);
                if (property != null && property.equals(TextAttribute.RUN_DIRECTION_RTL)) {
                    iIntValue = 2;
                } else {
                    iIntValue = 0;
                }
            } else {
                iIntValue = num.intValue();
            }
            setJustification(iIntValue);
            setLineSpacing(StyleConstants.getLineSpacing(attributes));
            setFirstLineIndent(StyleConstants.getFirstLineIndent(attributes));
        }
    }

    protected int getLayoutViewCount() {
        return this.layoutPool.getViewCount();
    }

    protected View getLayoutView(int i2) {
        return this.layoutPool.getView(i2);
    }

    @Override // javax.swing.text.CompositeView
    protected int getNextNorthSouthVisualPositionFrom(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
        int viewIndexAtPosition;
        int viewCount;
        Rectangle rectangleModelToView;
        int i4;
        if (i2 == -1) {
            viewCount = i3 == 1 ? getViewCount() - 1 : 0;
        } else {
            if (bias == Position.Bias.Backward && i2 > 0) {
                viewIndexAtPosition = getViewIndexAtPosition(i2 - 1);
            } else {
                viewIndexAtPosition = getViewIndexAtPosition(i2);
            }
            if (i3 == 1) {
                if (viewIndexAtPosition == 0) {
                    return -1;
                }
                viewCount = viewIndexAtPosition - 1;
            } else {
                viewCount = viewIndexAtPosition + 1;
                if (viewCount >= getViewCount()) {
                    return -1;
                }
            }
        }
        JTextComponent jTextComponent = (JTextComponent) getContainer();
        Caret caret = jTextComponent.getCaret();
        Point magicCaretPosition = caret != null ? caret.getMagicCaretPosition() : null;
        if (magicCaretPosition == null) {
            try {
                rectangleModelToView = jTextComponent.getUI().modelToView(jTextComponent, i2, bias);
            } catch (BadLocationException e2) {
                rectangleModelToView = null;
            }
            if (rectangleModelToView == null) {
                i4 = 0;
            } else {
                i4 = rectangleModelToView.getBounds().f12372x;
            }
        } else {
            i4 = magicCaretPosition.f12370x;
        }
        return getClosestPositionTo(i2, bias, shape, i3, biasArr, viewCount, i4);
    }

    protected int getClosestPositionTo(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr, int i4, int i5) throws BadLocationException {
        JTextComponent jTextComponent = (JTextComponent) getContainer();
        Document document = getDocument();
        View view = getView(i4);
        int i6 = -1;
        biasArr[0] = Position.Bias.Forward;
        int viewCount = view.getViewCount();
        for (int i7 = 0; i7 < viewCount; i7++) {
            View view2 = view.getView(i7);
            int startOffset = view2.getStartOffset();
            if (AbstractDocument.isLeftToRight(document, startOffset, startOffset + 1)) {
                int i8 = startOffset;
                int endOffset = view2.getEndOffset();
                while (i8 < endOffset) {
                    float f2 = jTextComponent.modelToView(i8).getBounds().f12372x;
                    if (f2 < i5) {
                        i8++;
                    } else {
                        do {
                            i8++;
                            if (i8 >= endOffset) {
                                break;
                            }
                        } while (jTextComponent.modelToView(i8).getBounds().f12372x == f2);
                        return i8 - 1;
                    }
                }
                i6 = i8 - 1;
            } else {
                int endOffset2 = view2.getEndOffset() - 1;
                while (endOffset2 >= startOffset) {
                    float f3 = jTextComponent.modelToView(endOffset2).getBounds().f12372x;
                    if (f3 >= i5) {
                        do {
                            endOffset2--;
                            if (endOffset2 < startOffset) {
                                break;
                            }
                        } while (jTextComponent.modelToView(endOffset2).getBounds().f12372x == f3);
                        return endOffset2 + 1;
                    }
                    endOffset2--;
                }
                i6 = endOffset2 + 1;
            }
        }
        if (i6 == -1) {
            return getStartOffset();
        }
        return i6;
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.CompositeView
    protected boolean flipEastAndWestAtEnds(int i2, Position.Bias bias) {
        Document document = getDocument();
        int startOffset = getStartOffset();
        return !AbstractDocument.isLeftToRight(document, startOffset, startOffset + 1);
    }

    @Override // javax.swing.text.FlowView
    public int getFlowSpan(int i2) {
        View view = getView(i2);
        int leftInset = 0;
        if (view instanceof Row) {
            Row row = (Row) view;
            leftInset = row.getLeftInset() + row.getRightInset();
        }
        return this.layoutSpan == Integer.MAX_VALUE ? this.layoutSpan : this.layoutSpan - leftInset;
    }

    @Override // javax.swing.text.FlowView
    public int getFlowStart(int i2) {
        View view = getView(i2);
        short leftInset = 0;
        if (view instanceof Row) {
            leftInset = ((Row) view).getLeftInset();
        }
        return this.tabBase + leftInset;
    }

    @Override // javax.swing.text.FlowView
    protected View createRow() {
        return new Row(getElement());
    }

    @Override // javax.swing.text.TabExpander
    public float nextTabStop(float f2, int i2) {
        int iFindOffsetToCharactersInString;
        if (this.justification != 0) {
            return f2 + 10.0f;
        }
        float f3 = f2 - this.tabBase;
        TabSet tabSet = getTabSet();
        if (tabSet == null) {
            return this.tabBase + (((((int) f3) / 72) + 1) * 72);
        }
        TabStop tabAfter = tabSet.getTabAfter(f3 + 0.01f);
        if (tabAfter == null) {
            return this.tabBase + f3 + 5.0f;
        }
        int alignment = tabAfter.getAlignment();
        switch (alignment) {
            case 0:
            case 3:
            default:
                return this.tabBase + tabAfter.getPosition();
            case 1:
            case 2:
                iFindOffsetToCharactersInString = findOffsetToCharactersInString(tabChars, i2 + 1);
                break;
            case 4:
                iFindOffsetToCharactersInString = findOffsetToCharactersInString(tabDecimalChars, i2 + 1);
                break;
            case 5:
                return this.tabBase + tabAfter.getPosition();
        }
        if (iFindOffsetToCharactersInString == -1) {
            iFindOffsetToCharactersInString = getEndOffset();
        }
        float partialSize = getPartialSize(i2 + 1, iFindOffsetToCharactersInString);
        switch (alignment) {
            case 1:
            case 4:
                return this.tabBase + Math.max(f3, tabAfter.getPosition() - partialSize);
            case 2:
                return this.tabBase + Math.max(f3, tabAfter.getPosition() - (partialSize / 2.0f));
            case 3:
            default:
                return f3;
        }
    }

    protected TabSet getTabSet() {
        return StyleConstants.getTabSet(getElement().getAttributes());
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected float getPartialSize(int i2, int i3) {
        float f2;
        float preferredSpan;
        float f3 = 0.0f;
        getViewCount();
        int elementIndex = getElement().getElementIndex(i2);
        int viewCount = this.layoutPool.getViewCount();
        while (i2 < i3 && elementIndex < viewCount) {
            int i4 = elementIndex;
            elementIndex++;
            View view = this.layoutPool.getView(i4);
            int endOffset = view.getEndOffset();
            int iMin = Math.min(i3, endOffset);
            if (view instanceof TabableView) {
                f2 = f3;
                preferredSpan = ((TabableView) view).getPartialSpan(i2, iMin);
            } else if (i2 == view.getStartOffset() && iMin == view.getEndOffset()) {
                f2 = f3;
                preferredSpan = view.getPreferredSpan(0);
            } else {
                return 0.0f;
            }
            f3 = f2 + preferredSpan;
            i2 = endOffset;
        }
        return f3;
    }

    protected int findOffsetToCharactersInString(char[] cArr, int i2) {
        int endOffset = getEndOffset();
        Segment segment = new Segment();
        try {
            getDocument().getText(i2, endOffset - i2, segment);
            int i3 = segment.offset + segment.count;
            for (int i4 = segment.offset; i4 < i3; i4++) {
                char c2 = segment.array[i4];
                for (char c3 : cArr) {
                    if (c2 == c3) {
                        return (i4 - segment.offset) + i2;
                    }
                }
            }
            return -1;
        } catch (BadLocationException e2) {
            return -1;
        }
    }

    protected float getTabBase() {
        return this.tabBase;
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Shape childAllocation;
        Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
        this.tabBase = bounds.f12372x + getLeftInset();
        super.paint(graphics, shape);
        if (this.firstLineIndent < 0 && (childAllocation = getChildAllocation(0, shape)) != null && childAllocation.intersects(bounds)) {
            int leftInset = bounds.f12372x + getLeftInset() + this.firstLineIndent;
            int topInset = bounds.f12373y + getTopInset();
            Rectangle clipBounds = graphics.getClipBounds();
            this.tempRect.f12372x = leftInset + getOffset(0, 0);
            this.tempRect.f12373y = topInset + getOffset(1, 0);
            this.tempRect.width = getSpan(0, 0) - this.firstLineIndent;
            this.tempRect.height = getSpan(1, 0);
            if (this.tempRect.intersects(clipBounds)) {
                this.tempRect.f12372x -= this.firstLineIndent;
                paintChild(graphics, this.tempRect, 0);
            }
        }
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getAlignment(int i2) {
        switch (i2) {
            case 0:
                return 0.5f;
            case 1:
                float preferredSpan = 0.5f;
                if (getViewCount() != 0) {
                    int preferredSpan2 = (int) getPreferredSpan(1);
                    preferredSpan = preferredSpan2 != 0 ? (((int) getView(0).getPreferredSpan(1)) / 2) / preferredSpan2 : 0.0f;
                }
                return preferredSpan;
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    public View breakView(int i2, float f2, Shape shape) {
        if (i2 == 1) {
            if (shape != null) {
                Rectangle bounds = shape.getBounds();
                setSize(bounds.width, bounds.height);
            }
            return this;
        }
        return this;
    }

    public int getBreakWeight(int i2, float f2) {
        if (i2 == 1) {
            return 0;
        }
        return 0;
    }

    @Override // javax.swing.text.FlowView, javax.swing.text.BoxView
    protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
        SizeRequirements sizeRequirementsCalculateMinorAxisRequirements = super.calculateMinorAxisRequirements(i2, sizeRequirements);
        float fMax = 0.0f;
        float f2 = 0.0f;
        int layoutViewCount = getLayoutViewCount();
        for (int i3 = 0; i3 < layoutViewCount; i3++) {
            View layoutView = getLayoutView(i3);
            float minimumSpan = layoutView.getMinimumSpan(i2);
            if (layoutView.getBreakWeight(i2, 0.0f, layoutView.getMaximumSpan(i2)) > 0) {
                int startOffset = layoutView.getStartOffset();
                int endOffset = layoutView.getEndOffset();
                float fFindEdgeSpan = findEdgeSpan(layoutView, i2, startOffset, startOffset, endOffset);
                float fFindEdgeSpan2 = findEdgeSpan(layoutView, i2, endOffset, startOffset, endOffset);
                fMax = Math.max(fMax, Math.max(minimumSpan, f2 + fFindEdgeSpan));
                f2 = fFindEdgeSpan2;
            } else {
                f2 += minimumSpan;
                fMax = Math.max(fMax, f2);
            }
        }
        sizeRequirementsCalculateMinorAxisRequirements.minimum = Math.max(sizeRequirementsCalculateMinorAxisRequirements.minimum, (int) fMax);
        sizeRequirementsCalculateMinorAxisRequirements.preferred = Math.max(sizeRequirementsCalculateMinorAxisRequirements.minimum, sizeRequirementsCalculateMinorAxisRequirements.preferred);
        sizeRequirementsCalculateMinorAxisRequirements.maximum = Math.max(sizeRequirementsCalculateMinorAxisRequirements.preferred, sizeRequirementsCalculateMinorAxisRequirements.maximum);
        return sizeRequirementsCalculateMinorAxisRequirements;
    }

    private float findEdgeSpan(View view, int i2, int i3, int i4, int i5) {
        int i6 = i5 - i4;
        if (i6 <= 1) {
            return view.getMinimumSpan(i2);
        }
        int i7 = i4 + (i6 / 2);
        boolean z2 = i7 > i3;
        View viewCreateFragment = z2 ? view.createFragment(i3, i7) : view.createFragment(i7, i3);
        if ((viewCreateFragment.getBreakWeight(i2, 0.0f, viewCreateFragment.getMaximumSpan(i2)) > 0) == z2) {
            i5 = i7;
        } else {
            i4 = i7;
        }
        return findEdgeSpan(viewCreateFragment, i2, i3, i4, i5);
    }

    @Override // javax.swing.text.FlowView, javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        setPropertiesFromAttributes();
        layoutChanged(0);
        layoutChanged(1);
        super.changedUpdate(documentEvent, shape, viewFactory);
    }

    static {
        tabChars[0] = '\t';
        tabDecimalChars = new char[2];
        tabDecimalChars[0] = '\t';
        tabDecimalChars[1] = '.';
    }

    /* loaded from: rt.jar:javax/swing/text/ParagraphView$Row.class */
    class Row extends BoxView {
        static final int SPACE_ADDON = 0;
        static final int SPACE_ADDON_LEFTOVER_END = 1;
        static final int START_JUSTIFIABLE = 2;
        static final int END_JUSTIFIABLE = 3;
        int[] justificationData;

        Row(Element element) {
            super(element, 0);
            this.justificationData = null;
        }

        @Override // javax.swing.text.CompositeView
        protected void loadChildren(ViewFactory viewFactory) {
        }

        @Override // javax.swing.text.View
        public AttributeSet getAttributes() {
            View parent = getParent();
            if (parent != null) {
                return parent.getAttributes();
            }
            return null;
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public float getAlignment(int i2) {
            if (i2 == 0) {
                switch (ParagraphView.this.justification) {
                    case 0:
                        return 0.0f;
                    case 1:
                        return 0.5f;
                    case 2:
                        return 1.0f;
                    case 3:
                        float f2 = 0.5f;
                        if (isJustifiableDocument()) {
                            f2 = 0.0f;
                        }
                        return f2;
                }
            }
            return super.getAlignment(i2);
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.CompositeView, javax.swing.text.View
        public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
            View viewAtPosition = getViewAtPosition(i2, shape.getBounds());
            if (viewAtPosition != null && !viewAtPosition.getElement().isLeaf()) {
                return super.modelToView(i2, shape, bias);
            }
            Rectangle bounds = shape.getBounds();
            int i3 = bounds.height;
            int i4 = bounds.f12373y;
            Rectangle bounds2 = super.modelToView(i2, shape, bias).getBounds();
            bounds2.height = i3;
            bounds2.f12373y = i4;
            return bounds2;
        }

        @Override // javax.swing.text.View
        public int getStartOffset() {
            int iMin = Integer.MAX_VALUE;
            int viewCount = getViewCount();
            for (int i2 = 0; i2 < viewCount; i2++) {
                iMin = Math.min(iMin, getView(i2).getStartOffset());
            }
            return iMin;
        }

        @Override // javax.swing.text.View
        public int getEndOffset() {
            int iMax = 0;
            int viewCount = getViewCount();
            for (int i2 = 0; i2 < viewCount; i2++) {
                iMax = Math.max(iMax, getView(i2).getEndOffset());
            }
            return iMax;
        }

        @Override // javax.swing.text.BoxView
        protected void layoutMinorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
            baselineLayout(i2, i3, iArr, iArr2);
        }

        @Override // javax.swing.text.BoxView
        protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
            return baselineRequirements(i2, sizeRequirements);
        }

        private boolean isLastRow() {
            View parent = getParent();
            return parent == null || this == parent.getView(parent.getViewCount() - 1);
        }

        private boolean isBrokenRow() {
            boolean z2 = false;
            int viewCount = getViewCount();
            if (viewCount > 0 && getView(viewCount - 1).getBreakWeight(0, 0.0f, 0.0f) >= 3000) {
                z2 = true;
            }
            return z2;
        }

        private boolean isJustifiableDocument() {
            return !Boolean.TRUE.equals(getDocument().getProperty("i18n"));
        }

        private boolean isJustifyEnabled() {
            return (((ParagraphView.this.justification == 3) && isJustifiableDocument()) && !isLastRow()) && !isBrokenRow();
        }

        @Override // javax.swing.text.BoxView
        protected SizeRequirements calculateMajorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
            int[] iArr = this.justificationData;
            this.justificationData = null;
            SizeRequirements sizeRequirementsCalculateMajorAxisRequirements = super.calculateMajorAxisRequirements(i2, sizeRequirements);
            if (isJustifyEnabled()) {
                this.justificationData = iArr;
            }
            return sizeRequirementsCalculateMajorAxisRequirements;
        }

        @Override // javax.swing.text.BoxView
        protected void layoutMajorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
            int[] iArr3 = this.justificationData;
            this.justificationData = null;
            super.layoutMajorAxis(i2, i3, iArr, iArr2);
            if (!isJustifyEnabled()) {
                return;
            }
            int i4 = 0;
            for (int i5 : iArr2) {
                i4 += i5;
            }
            if (i4 == i2) {
                return;
            }
            int i6 = 0;
            int i7 = -1;
            int i8 = -1;
            int i9 = 0;
            int startOffset = getStartOffset();
            int[] iArr4 = new int[getEndOffset() - startOffset];
            Arrays.fill(iArr4, 0);
            for (int viewCount = getViewCount() - 1; viewCount >= 0; viewCount--) {
                View view = getView(viewCount);
                if (view instanceof GlyphView) {
                    GlyphView.JustificationInfo justificationInfo = ((GlyphView) view).getJustificationInfo(startOffset);
                    int startOffset2 = view.getStartOffset();
                    int i10 = startOffset2 - startOffset;
                    for (int i11 = 0; i11 < justificationInfo.spaceMap.length(); i11++) {
                        if (justificationInfo.spaceMap.get(i11)) {
                            iArr4[i11 + i10] = 1;
                        }
                    }
                    if (i7 > 0) {
                        if (justificationInfo.end >= 0) {
                            i6 += justificationInfo.trailingSpaces;
                        } else {
                            i9 += justificationInfo.trailingSpaces;
                        }
                    }
                    if (justificationInfo.start >= 0) {
                        i7 = justificationInfo.start + startOffset2;
                        i6 += i9;
                    }
                    if (justificationInfo.end >= 0 && i8 < 0) {
                        i8 = justificationInfo.end + startOffset2;
                    }
                    i6 += justificationInfo.contentSpaces;
                    i9 = justificationInfo.leadingSpaces;
                    if (justificationInfo.hasTab) {
                        break;
                    }
                }
            }
            if (i6 <= 0) {
                return;
            }
            int i12 = i2 - i4;
            int i13 = i6 > 0 ? i12 / i6 : 0;
            int i14 = -1;
            int i15 = i7 - startOffset;
            int i16 = i12 - (i13 * i6);
            while (i16 > 0) {
                i14 = i15;
                i16 -= iArr4[i15];
                i15++;
            }
            if (i13 > 0 || i14 >= 0) {
                this.justificationData = iArr3 != null ? iArr3 : new int[4];
                this.justificationData[0] = i13;
                this.justificationData[1] = i14;
                this.justificationData[2] = i7 - startOffset;
                this.justificationData[3] = i8 - startOffset;
                super.layoutMajorAxis(i2, i3, iArr, iArr2);
            }
        }

        @Override // javax.swing.text.BoxView, javax.swing.text.View
        public float getMaximumSpan(int i2) {
            float maximumSpan;
            if (0 == i2 && isJustifyEnabled()) {
                maximumSpan = Float.MAX_VALUE;
            } else {
                maximumSpan = super.getMaximumSpan(i2);
            }
            return maximumSpan;
        }

        @Override // javax.swing.text.CompositeView
        protected int getViewIndexAtPosition(int i2) {
            if (i2 < getStartOffset() || i2 >= getEndOffset()) {
                return -1;
            }
            for (int viewCount = getViewCount() - 1; viewCount >= 0; viewCount--) {
                View view = getView(viewCount);
                if (i2 >= view.getStartOffset() && i2 < view.getEndOffset()) {
                    return viewCount;
                }
            }
            return -1;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v10, types: [int] */
        @Override // javax.swing.text.CompositeView
        protected short getLeftInset() {
            short s2 = 0;
            View parent = getParent();
            if (parent != null && this == parent.getView(0)) {
                s2 = ParagraphView.this.firstLineIndent;
            }
            return (short) (super.getLeftInset() + s2);
        }

        @Override // javax.swing.text.CompositeView
        protected short getBottomInset() {
            return (short) (super.getBottomInset() + ((this.minorRequest != null ? this.minorRequest.preferred : 0) * ParagraphView.this.lineSpacing));
        }
    }
}
