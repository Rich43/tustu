package javax.swing.text;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/BoxView.class */
public class BoxView extends CompositeView {
    int majorAxis;
    int majorSpan;
    int minorSpan;
    boolean majorReqValid;
    boolean minorReqValid;
    SizeRequirements majorRequest;
    SizeRequirements minorRequest;
    boolean majorAllocValid;
    int[] majorOffsets;
    int[] majorSpans;
    boolean minorAllocValid;
    int[] minorOffsets;
    int[] minorSpans;
    Rectangle tempRect;

    public BoxView(Element element, int i2) {
        super(element);
        this.tempRect = new Rectangle();
        this.majorAxis = i2;
        this.majorOffsets = new int[0];
        this.majorSpans = new int[0];
        this.majorReqValid = false;
        this.majorAllocValid = false;
        this.minorOffsets = new int[0];
        this.minorSpans = new int[0];
        this.minorReqValid = false;
        this.minorAllocValid = false;
    }

    public int getAxis() {
        return this.majorAxis;
    }

    public void setAxis(int i2) {
        boolean z2 = i2 != this.majorAxis;
        this.majorAxis = i2;
        if (z2) {
            preferenceChanged(null, true, true);
        }
    }

    public void layoutChanged(int i2) {
        if (i2 == this.majorAxis) {
            this.majorAllocValid = false;
        } else {
            this.minorAllocValid = false;
        }
    }

    protected boolean isLayoutValid(int i2) {
        if (i2 == this.majorAxis) {
            return this.majorAllocValid;
        }
        return this.minorAllocValid;
    }

    protected void paintChild(Graphics graphics, Rectangle rectangle, int i2) {
        getView(i2).paint(graphics, rectangle);
    }

    @Override // javax.swing.text.CompositeView, javax.swing.text.View
    public void replace(int i2, int i3, View[] viewArr) {
        super.replace(i2, i3, viewArr);
        int length = viewArr != null ? viewArr.length : 0;
        this.majorOffsets = updateLayoutArray(this.majorOffsets, i2, length);
        this.majorSpans = updateLayoutArray(this.majorSpans, i2, length);
        this.majorReqValid = false;
        this.majorAllocValid = false;
        this.minorOffsets = updateLayoutArray(this.minorOffsets, i2, length);
        this.minorSpans = updateLayoutArray(this.minorSpans, i2, length);
        this.minorReqValid = false;
        this.minorAllocValid = false;
    }

    int[] updateLayoutArray(int[] iArr, int i2, int i3) {
        int viewCount = getViewCount();
        int[] iArr2 = new int[viewCount];
        System.arraycopy(iArr, 0, iArr2, 0, i2);
        System.arraycopy(iArr, i2, iArr2, i2 + i3, (viewCount - i3) - i2);
        return iArr2;
    }

    @Override // javax.swing.text.View
    protected void forwardUpdate(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        boolean zIsLayoutValid = isLayoutValid(this.majorAxis);
        super.forwardUpdate(elementChange, documentEvent, shape, viewFactory);
        if (zIsLayoutValid && !isLayoutValid(this.majorAxis)) {
            Container container = getContainer();
            if (shape != null && container != null) {
                int viewIndexAtPosition = getViewIndexAtPosition(documentEvent.getOffset());
                Rectangle insideAllocation = getInsideAllocation(shape);
                if (this.majorAxis == 0) {
                    insideAllocation.f12372x += this.majorOffsets[viewIndexAtPosition];
                    insideAllocation.width -= this.majorOffsets[viewIndexAtPosition];
                } else {
                    insideAllocation.f12373y += this.minorOffsets[viewIndexAtPosition];
                    insideAllocation.height -= this.minorOffsets[viewIndexAtPosition];
                }
                container.repaint(insideAllocation.f12372x, insideAllocation.f12373y, insideAllocation.width, insideAllocation.height);
            }
        }
    }

    @Override // javax.swing.text.View
    public void preferenceChanged(View view, boolean z2, boolean z3) {
        boolean z4 = this.majorAxis == 0 ? z2 : z3;
        boolean z5 = this.majorAxis == 0 ? z3 : z2;
        if (z4) {
            this.majorReqValid = false;
            this.majorAllocValid = false;
        }
        if (z5) {
            this.minorReqValid = false;
            this.minorAllocValid = false;
        }
        super.preferenceChanged(view, z2, z3);
    }

    @Override // javax.swing.text.View
    public int getResizeWeight(int i2) {
        checkRequests(i2);
        if (i2 == this.majorAxis) {
            if (this.majorRequest.preferred != this.majorRequest.minimum || this.majorRequest.preferred != this.majorRequest.maximum) {
                return 1;
            }
            return 0;
        }
        if (this.minorRequest.preferred != this.minorRequest.minimum || this.minorRequest.preferred != this.minorRequest.maximum) {
            return 1;
        }
        return 0;
    }

    void setSpanOnAxis(int i2, float f2) {
        if (i2 == this.majorAxis) {
            if (this.majorSpan != ((int) f2)) {
                this.majorAllocValid = false;
            }
            if (!this.majorAllocValid) {
                this.majorSpan = (int) f2;
                checkRequests(this.majorAxis);
                layoutMajorAxis(this.majorSpan, i2, this.majorOffsets, this.majorSpans);
                this.majorAllocValid = true;
                updateChildSizes();
                return;
            }
            return;
        }
        if (((int) f2) != this.minorSpan) {
            this.minorAllocValid = false;
        }
        if (!this.minorAllocValid) {
            this.minorSpan = (int) f2;
            checkRequests(i2);
            layoutMinorAxis(this.minorSpan, i2, this.minorOffsets, this.minorSpans);
            this.minorAllocValid = true;
            updateChildSizes();
        }
    }

    void updateChildSizes() {
        int viewCount = getViewCount();
        if (this.majorAxis == 0) {
            for (int i2 = 0; i2 < viewCount; i2++) {
                getView(i2).setSize(this.majorSpans[i2], this.minorSpans[i2]);
            }
            return;
        }
        for (int i3 = 0; i3 < viewCount; i3++) {
            getView(i3).setSize(this.minorSpans[i3], this.majorSpans[i3]);
        }
    }

    float getSpanOnAxis(int i2) {
        if (i2 == this.majorAxis) {
            return this.majorSpan;
        }
        return this.minorSpan;
    }

    @Override // javax.swing.text.View
    public void setSize(float f2, float f3) {
        layout(Math.max(0, (int) ((f2 - getLeftInset()) - getRightInset())), Math.max(0, (int) ((f3 - getTopInset()) - getBottomInset())));
    }

    @Override // javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
        int viewCount = getViewCount();
        int leftInset = bounds.f12372x + getLeftInset();
        int topInset = bounds.f12373y + getTopInset();
        Rectangle clipBounds = graphics.getClipBounds();
        for (int i2 = 0; i2 < viewCount; i2++) {
            this.tempRect.f12372x = leftInset + getOffset(0, i2);
            this.tempRect.f12373y = topInset + getOffset(1, i2);
            this.tempRect.width = getSpan(0, i2);
            this.tempRect.height = getSpan(1, i2);
            int i3 = this.tempRect.f12372x;
            int i4 = i3 + this.tempRect.width;
            int i5 = this.tempRect.f12373y;
            int i6 = i5 + this.tempRect.height;
            int i7 = clipBounds.f12372x;
            int i8 = i7 + clipBounds.width;
            int i9 = clipBounds.f12373y;
            int i10 = i9 + clipBounds.height;
            if (i4 >= i7 && i6 >= i9 && i8 >= i3 && i10 >= i5) {
                paintChild(graphics, this.tempRect, i2);
            }
        }
    }

    @Override // javax.swing.text.CompositeView, javax.swing.text.View
    public Shape getChildAllocation(int i2, Shape shape) {
        if (shape != null) {
            Shape childAllocation = super.getChildAllocation(i2, shape);
            if (childAllocation != null && !isAllocationValid()) {
                Rectangle bounds = childAllocation instanceof Rectangle ? (Rectangle) childAllocation : childAllocation.getBounds();
                if (bounds.width == 0 && bounds.height == 0) {
                    return null;
                }
            }
            return childAllocation;
        }
        return null;
    }

    @Override // javax.swing.text.CompositeView, javax.swing.text.View
    public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
        if (!isAllocationValid()) {
            Rectangle bounds = shape.getBounds();
            setSize(bounds.width, bounds.height);
        }
        return super.modelToView(i2, shape, bias);
    }

    @Override // javax.swing.text.CompositeView, javax.swing.text.View
    public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        if (!isAllocationValid()) {
            Rectangle bounds = shape.getBounds();
            setSize(bounds.width, bounds.height);
        }
        return super.viewToModel(f2, f3, shape, biasArr);
    }

    @Override // javax.swing.text.View
    public float getAlignment(int i2) {
        checkRequests(i2);
        if (i2 == this.majorAxis) {
            return this.majorRequest.alignment;
        }
        return this.minorRequest.alignment;
    }

    @Override // javax.swing.text.View
    public float getPreferredSpan(int i2) {
        checkRequests(i2);
        float leftInset = i2 == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
        if (i2 == this.majorAxis) {
            return this.majorRequest.preferred + leftInset;
        }
        return this.minorRequest.preferred + leftInset;
    }

    @Override // javax.swing.text.View
    public float getMinimumSpan(int i2) {
        checkRequests(i2);
        float leftInset = i2 == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
        if (i2 == this.majorAxis) {
            return this.majorRequest.minimum + leftInset;
        }
        return this.minorRequest.minimum + leftInset;
    }

    @Override // javax.swing.text.View
    public float getMaximumSpan(int i2) {
        checkRequests(i2);
        float leftInset = i2 == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
        if (i2 == this.majorAxis) {
            return this.majorRequest.maximum + leftInset;
        }
        return this.minorRequest.maximum + leftInset;
    }

    protected boolean isAllocationValid() {
        return this.majorAllocValid && this.minorAllocValid;
    }

    @Override // javax.swing.text.CompositeView
    protected boolean isBefore(int i2, int i3, Rectangle rectangle) {
        return this.majorAxis == 0 ? i2 < rectangle.f12372x : i3 < rectangle.f12373y;
    }

    @Override // javax.swing.text.CompositeView
    protected boolean isAfter(int i2, int i3, Rectangle rectangle) {
        return this.majorAxis == 0 ? i2 > rectangle.width + rectangle.f12372x : i3 > rectangle.height + rectangle.f12373y;
    }

    @Override // javax.swing.text.CompositeView
    protected View getViewAtPoint(int i2, int i3, Rectangle rectangle) {
        int viewCount = getViewCount();
        if (this.majorAxis == 0) {
            if (i2 < rectangle.f12372x + this.majorOffsets[0]) {
                childAllocation(0, rectangle);
                return getView(0);
            }
            for (int i4 = 0; i4 < viewCount; i4++) {
                if (i2 < rectangle.f12372x + this.majorOffsets[i4]) {
                    childAllocation(i4 - 1, rectangle);
                    return getView(i4 - 1);
                }
            }
            childAllocation(viewCount - 1, rectangle);
            return getView(viewCount - 1);
        }
        if (i3 < rectangle.f12373y + this.majorOffsets[0]) {
            childAllocation(0, rectangle);
            return getView(0);
        }
        for (int i5 = 0; i5 < viewCount; i5++) {
            if (i3 < rectangle.f12373y + this.majorOffsets[i5]) {
                childAllocation(i5 - 1, rectangle);
                return getView(i5 - 1);
            }
        }
        childAllocation(viewCount - 1, rectangle);
        return getView(viewCount - 1);
    }

    @Override // javax.swing.text.CompositeView
    protected void childAllocation(int i2, Rectangle rectangle) {
        rectangle.f12372x += getOffset(0, i2);
        rectangle.f12373y += getOffset(1, i2);
        rectangle.width = getSpan(0, i2);
        rectangle.height = getSpan(1, i2);
    }

    protected void layout(int i2, int i3) {
        setSpanOnAxis(0, i2);
        setSpanOnAxis(1, i3);
    }

    public int getWidth() {
        int i2;
        if (this.majorAxis == 0) {
            i2 = this.majorSpan;
        } else {
            i2 = this.minorSpan;
        }
        return i2 + (getLeftInset() - getRightInset());
    }

    public int getHeight() {
        int i2;
        if (this.majorAxis == 1) {
            i2 = this.majorSpan;
        } else {
            i2 = this.minorSpan;
        }
        return i2 + (getTopInset() - getBottomInset());
    }

    protected void layoutMajorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
        int maximumSpan;
        long j2 = 0;
        int viewCount = getViewCount();
        for (int i4 = 0; i4 < viewCount; i4++) {
            iArr2[i4] = (int) getView(i4).getPreferredSpan(i3);
            j2 += iArr2[i4];
        }
        long j3 = i2 - j2;
        float fMax = 0.0f;
        int[] iArr3 = null;
        if (j3 != 0) {
            long j4 = 0;
            iArr3 = new int[viewCount];
            for (int i5 = 0; i5 < viewCount; i5++) {
                View view = getView(i5);
                if (j3 < 0) {
                    maximumSpan = (int) view.getMinimumSpan(i3);
                    iArr3[i5] = iArr2[i5] - maximumSpan;
                } else {
                    maximumSpan = (int) view.getMaximumSpan(i3);
                    iArr3[i5] = maximumSpan - iArr2[i5];
                }
                j4 += maximumSpan;
            }
            fMax = Math.max(Math.min(j3 / Math.abs(j4 - j2), 1.0f), -1.0f);
        }
        int iMin = 0;
        for (int i6 = 0; i6 < viewCount; i6++) {
            iArr[i6] = iMin;
            if (j3 != 0) {
                int i7 = i6;
                iArr2[i7] = iArr2[i7] + Math.round(fMax * iArr3[i6]);
            }
            iMin = (int) Math.min(iMin + iArr2[i6], 2147483647L);
        }
    }

    protected void layoutMinorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
        int viewCount = getViewCount();
        for (int i4 = 0; i4 < viewCount; i4++) {
            View view = getView(i4);
            int maximumSpan = (int) view.getMaximumSpan(i3);
            if (maximumSpan < i2) {
                iArr[i4] = (int) ((i2 - maximumSpan) * view.getAlignment(i3));
                iArr2[i4] = maximumSpan;
            } else {
                int minimumSpan = (int) view.getMinimumSpan(i3);
                iArr[i4] = 0;
                iArr2[i4] = Math.max(minimumSpan, i2);
            }
        }
    }

    protected SizeRequirements calculateMajorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
        float minimumSpan = 0.0f;
        float preferredSpan = 0.0f;
        float maximumSpan = 0.0f;
        int viewCount = getViewCount();
        for (int i3 = 0; i3 < viewCount; i3++) {
            View view = getView(i3);
            minimumSpan += view.getMinimumSpan(i2);
            preferredSpan += view.getPreferredSpan(i2);
            maximumSpan += view.getMaximumSpan(i2);
        }
        if (sizeRequirements == null) {
            sizeRequirements = new SizeRequirements();
        }
        sizeRequirements.alignment = 0.5f;
        sizeRequirements.minimum = (int) minimumSpan;
        sizeRequirements.preferred = (int) preferredSpan;
        sizeRequirements.maximum = (int) maximumSpan;
        return sizeRequirements;
    }

    protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
        int iMax = 0;
        long jMax = 0;
        int iMax2 = Integer.MAX_VALUE;
        int viewCount = getViewCount();
        for (int i3 = 0; i3 < viewCount; i3++) {
            View view = getView(i3);
            iMax = Math.max((int) view.getMinimumSpan(i2), iMax);
            jMax = Math.max((int) view.getPreferredSpan(i2), jMax);
            iMax2 = Math.max((int) view.getMaximumSpan(i2), iMax2);
        }
        if (sizeRequirements == null) {
            sizeRequirements = new SizeRequirements();
            sizeRequirements.alignment = 0.5f;
        }
        sizeRequirements.preferred = (int) jMax;
        sizeRequirements.minimum = iMax;
        sizeRequirements.maximum = iMax2;
        return sizeRequirements;
    }

    void checkRequests(int i2) {
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("Invalid axis: " + i2);
        }
        if (i2 == this.majorAxis) {
            if (!this.majorReqValid) {
                this.majorRequest = calculateMajorAxisRequirements(i2, this.majorRequest);
                this.majorReqValid = true;
                return;
            }
            return;
        }
        if (!this.minorReqValid) {
            this.minorRequest = calculateMinorAxisRequirements(i2, this.minorRequest);
            this.minorReqValid = true;
        }
    }

    protected void baselineLayout(int i2, int i3, int[] iArr, int[] iArr2) {
        float preferredSpan;
        int alignment = (int) (i2 * getAlignment(i3));
        int i4 = i2 - alignment;
        int viewCount = getViewCount();
        for (int i5 = 0; i5 < viewCount; i5++) {
            View view = getView(i5);
            float alignment2 = view.getAlignment(i3);
            if (view.getResizeWeight(i3) > 0) {
                float minimumSpan = view.getMinimumSpan(i3);
                float maximumSpan = view.getMaximumSpan(i3);
                if (alignment2 == 0.0f) {
                    preferredSpan = Math.max(Math.min(maximumSpan, i4), minimumSpan);
                } else if (alignment2 == 1.0f) {
                    preferredSpan = Math.max(Math.min(maximumSpan, alignment), minimumSpan);
                } else {
                    preferredSpan = Math.max(Math.min(maximumSpan, Math.min(alignment / alignment2, i4 / (1.0f - alignment2))), minimumSpan);
                }
            } else {
                preferredSpan = view.getPreferredSpan(i3);
            }
            float f2 = preferredSpan;
            iArr[i5] = alignment - ((int) (f2 * alignment2));
            iArr2[i5] = (int) f2;
        }
    }

    protected SizeRequirements baselineRequirements(int i2, SizeRequirements sizeRequirements) {
        SizeRequirements sizeRequirements2 = new SizeRequirements();
        SizeRequirements sizeRequirements3 = new SizeRequirements();
        if (sizeRequirements == null) {
            sizeRequirements = new SizeRequirements();
        }
        sizeRequirements.alignment = 0.5f;
        int viewCount = getViewCount();
        for (int i3 = 0; i3 < viewCount; i3++) {
            View view = getView(i3);
            float alignment = view.getAlignment(i2);
            float preferredSpan = view.getPreferredSpan(i2);
            int i4 = (int) (alignment * preferredSpan);
            int i5 = (int) (preferredSpan - i4);
            sizeRequirements2.preferred = Math.max(i4, sizeRequirements2.preferred);
            sizeRequirements3.preferred = Math.max(i5, sizeRequirements3.preferred);
            if (view.getResizeWeight(i2) > 0) {
                float minimumSpan = view.getMinimumSpan(i2);
                int i6 = (int) (alignment * minimumSpan);
                sizeRequirements2.minimum = Math.max(i6, sizeRequirements2.minimum);
                sizeRequirements3.minimum = Math.max((int) (minimumSpan - i6), sizeRequirements3.minimum);
                float maximumSpan = view.getMaximumSpan(i2);
                int i7 = (int) (alignment * maximumSpan);
                sizeRequirements2.maximum = Math.max(i7, sizeRequirements2.maximum);
                sizeRequirements3.maximum = Math.max((int) (maximumSpan - i7), sizeRequirements3.maximum);
            } else {
                sizeRequirements2.minimum = Math.max(i4, sizeRequirements2.minimum);
                sizeRequirements3.minimum = Math.max(i5, sizeRequirements3.minimum);
                sizeRequirements2.maximum = Math.max(i4, sizeRequirements2.maximum);
                sizeRequirements3.maximum = Math.max(i5, sizeRequirements3.maximum);
            }
        }
        sizeRequirements.preferred = (int) Math.min(sizeRequirements2.preferred + sizeRequirements3.preferred, 2147483647L);
        if (sizeRequirements.preferred > 0) {
            sizeRequirements.alignment = sizeRequirements2.preferred / sizeRequirements.preferred;
        }
        if (sizeRequirements.alignment == 0.0f) {
            sizeRequirements.minimum = sizeRequirements3.minimum;
            sizeRequirements.maximum = sizeRequirements3.maximum;
        } else if (sizeRequirements.alignment == 1.0f) {
            sizeRequirements.minimum = sizeRequirements2.minimum;
            sizeRequirements.maximum = sizeRequirements2.maximum;
        } else {
            sizeRequirements.minimum = Math.round(Math.max(sizeRequirements2.minimum / sizeRequirements.alignment, sizeRequirements3.minimum / (1.0f - sizeRequirements.alignment)));
            sizeRequirements.maximum = Math.round(Math.min(sizeRequirements2.maximum / sizeRequirements.alignment, sizeRequirements3.maximum / (1.0f - sizeRequirements.alignment)));
        }
        return sizeRequirements;
    }

    protected int getOffset(int i2, int i3) {
        return (i2 == this.majorAxis ? this.majorOffsets : this.minorOffsets)[i3];
    }

    protected int getSpan(int i2, int i3) {
        return (i2 == this.majorAxis ? this.majorSpans : this.minorSpans)[i3];
    }

    @Override // javax.swing.text.CompositeView
    protected boolean flipEastAndWestAtEnds(int i2, Position.Bias bias) {
        View view;
        if (this.majorAxis == 1) {
            int viewIndexAtPosition = getViewIndexAtPosition(bias == Position.Bias.Backward ? Math.max(0, i2 - 1) : i2);
            if (viewIndexAtPosition != -1 && (view = getView(viewIndexAtPosition)) != null && (view instanceof CompositeView)) {
                return ((CompositeView) view).flipEastAndWestAtEnds(i2, bias);
            }
            return false;
        }
        return false;
    }
}
