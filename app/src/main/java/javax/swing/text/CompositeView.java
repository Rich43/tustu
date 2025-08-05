package javax.swing.text;

import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/CompositeView.class */
public abstract class CompositeView extends View {
    private static View[] ZERO = new View[0];
    private View[] children;
    private int nchildren;
    private short left;
    private short right;
    private short top;
    private short bottom;
    private Rectangle childAlloc;

    protected abstract boolean isBefore(int i2, int i3, Rectangle rectangle);

    protected abstract boolean isAfter(int i2, int i3, Rectangle rectangle);

    protected abstract View getViewAtPoint(int i2, int i3, Rectangle rectangle);

    protected abstract void childAllocation(int i2, Rectangle rectangle);

    public CompositeView(Element element) {
        super(element);
        this.children = new View[1];
        this.nchildren = 0;
        this.childAlloc = new Rectangle();
    }

    protected void loadChildren(ViewFactory viewFactory) {
        Element element;
        int elementCount;
        if (viewFactory != null && (elementCount = (element = getElement()).getElementCount()) > 0) {
            View[] viewArr = new View[elementCount];
            for (int i2 = 0; i2 < elementCount; i2++) {
                viewArr[i2] = viewFactory.create(element.getElement(i2));
            }
            replace(0, 0, viewArr);
        }
    }

    @Override // javax.swing.text.View
    public void setParent(View view) {
        super.setParent(view);
        if (view != null && this.nchildren == 0) {
            loadChildren(getViewFactory());
        }
    }

    @Override // javax.swing.text.View
    public int getViewCount() {
        return this.nchildren;
    }

    @Override // javax.swing.text.View
    public View getView(int i2) {
        return this.children[i2];
    }

    @Override // javax.swing.text.View
    public void replace(int i2, int i3, View[] viewArr) {
        if (viewArr == null) {
            viewArr = ZERO;
        }
        for (int i4 = i2; i4 < i2 + i3; i4++) {
            if (this.children[i4].getParent() == this) {
                this.children[i4].setParent(null);
            }
            this.children[i4] = null;
        }
        int length = viewArr.length - i3;
        int i5 = i2 + i3;
        int i6 = this.nchildren - i5;
        int i7 = i5 + length;
        if (this.nchildren + length >= this.children.length) {
            View[] viewArr2 = new View[Math.max(2 * this.children.length, this.nchildren + length)];
            System.arraycopy(this.children, 0, viewArr2, 0, i2);
            System.arraycopy(viewArr, 0, viewArr2, i2, viewArr.length);
            System.arraycopy(this.children, i5, viewArr2, i7, i6);
            this.children = viewArr2;
        } else {
            System.arraycopy(this.children, i5, this.children, i7, i6);
            System.arraycopy(viewArr, 0, this.children, i2, viewArr.length);
        }
        this.nchildren += length;
        for (View view : viewArr) {
            view.setParent(this);
        }
    }

    @Override // javax.swing.text.View
    public Shape getChildAllocation(int i2, Shape shape) {
        Rectangle insideAllocation = getInsideAllocation(shape);
        childAllocation(i2, insideAllocation);
        return insideAllocation;
    }

    @Override // javax.swing.text.View
    public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
        View view;
        int i3;
        boolean z2 = bias == Position.Bias.Backward;
        int iMax = z2 ? Math.max(0, i2 - 1) : i2;
        if (z2 && iMax < getStartOffset()) {
            return null;
        }
        int viewIndexAtPosition = getViewIndexAtPosition(iMax);
        if (viewIndexAtPosition != -1 && viewIndexAtPosition < getViewCount() && (view = getView(viewIndexAtPosition)) != null && iMax >= view.getStartOffset() && iMax < view.getEndOffset()) {
            Shape childAllocation = getChildAllocation(viewIndexAtPosition, shape);
            if (childAllocation == null) {
                return null;
            }
            Shape shapeModelToView = view.modelToView(i2, childAllocation, bias);
            if (shapeModelToView == null && view.getEndOffset() == i2 && (i3 = viewIndexAtPosition + 1) < getViewCount()) {
                shapeModelToView = getView(i3).modelToView(i2, getChildAllocation(i3, shape), bias);
            }
            return shapeModelToView;
        }
        throw new BadLocationException("Position not represented by view", i2);
    }

    @Override // javax.swing.text.View
    public Shape modelToView(int i2, Position.Bias bias, int i3, Position.Bias bias2, Shape shape) throws BadLocationException {
        Rectangle bounds;
        View view;
        Shape shapeModelToView;
        if (i2 == getStartOffset() && i3 == getEndOffset()) {
            return shape;
        }
        Rectangle insideAllocation = getInsideAllocation(shape);
        Rectangle rectangle = new Rectangle(insideAllocation);
        View viewAtPosition = getViewAtPosition(bias == Position.Bias.Backward ? Math.max(0, i2 - 1) : i2, rectangle);
        Rectangle rectangle2 = new Rectangle(insideAllocation);
        View viewAtPosition2 = getViewAtPosition(bias2 == Position.Bias.Backward ? Math.max(0, i3 - 1) : i3, rectangle2);
        if (viewAtPosition == viewAtPosition2) {
            if (viewAtPosition == null) {
                return shape;
            }
            return viewAtPosition.modelToView(i2, bias, i3, bias2, rectangle);
        }
        int viewCount = getViewCount();
        int i4 = 0;
        while (i4 < viewCount) {
            View view2 = getView(i4);
            if (view2 == viewAtPosition || view2 == viewAtPosition2) {
                Rectangle rectangle3 = new Rectangle();
                if (view2 == viewAtPosition) {
                    bounds = viewAtPosition.modelToView(i2, bias, viewAtPosition.getEndOffset(), Position.Bias.Backward, rectangle).getBounds();
                    view = viewAtPosition2;
                } else {
                    bounds = viewAtPosition2.modelToView(viewAtPosition2.getStartOffset(), Position.Bias.Forward, i3, bias2, rectangle2).getBounds();
                    view = viewAtPosition;
                }
                while (true) {
                    i4++;
                    if (i4 >= viewCount || getView(i4) == view) {
                        break;
                    }
                    rectangle3.setBounds(insideAllocation);
                    childAllocation(i4, rectangle3);
                    bounds.add(rectangle3);
                }
                if (view != null) {
                    if (view == viewAtPosition2) {
                        shapeModelToView = viewAtPosition2.modelToView(viewAtPosition2.getStartOffset(), Position.Bias.Forward, i3, bias2, rectangle2);
                    } else {
                        shapeModelToView = viewAtPosition.modelToView(i2, bias, viewAtPosition.getEndOffset(), Position.Bias.Backward, rectangle);
                    }
                    if (shapeModelToView instanceof Rectangle) {
                        bounds.add((Rectangle) shapeModelToView);
                    } else {
                        bounds.add(shapeModelToView.getBounds());
                    }
                }
                return bounds;
            }
            i4++;
        }
        throw new BadLocationException("Position not represented by view", i2);
    }

    @Override // javax.swing.text.View
    public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        Rectangle insideAllocation = getInsideAllocation(shape);
        if (isBefore((int) f2, (int) f3, insideAllocation)) {
            int startOffset = -1;
            try {
                startOffset = getNextVisualPositionFrom(-1, Position.Bias.Forward, shape, 3, biasArr);
            } catch (IllegalArgumentException e2) {
            } catch (BadLocationException e3) {
            }
            if (startOffset == -1) {
                startOffset = getStartOffset();
                biasArr[0] = Position.Bias.Forward;
            }
            return startOffset;
        }
        if (isAfter((int) f2, (int) f3, insideAllocation)) {
            int endOffset = -1;
            try {
                endOffset = getNextVisualPositionFrom(-1, Position.Bias.Forward, shape, 7, biasArr);
            } catch (IllegalArgumentException e4) {
            } catch (BadLocationException e5) {
            }
            if (endOffset == -1) {
                endOffset = getEndOffset() - 1;
                biasArr[0] = Position.Bias.Forward;
            }
            return endOffset;
        }
        View viewAtPoint = getViewAtPoint((int) f2, (int) f3, insideAllocation);
        if (viewAtPoint != null) {
            return viewAtPoint.viewToModel(f2, f3, insideAllocation, biasArr);
        }
        return -1;
    }

    @Override // javax.swing.text.View
    public int getNextVisualPositionFrom(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
        if (i2 < -1) {
            throw new BadLocationException("invalid position", i2);
        }
        getInsideAllocation(shape);
        switch (i3) {
            case 1:
                return getNextNorthSouthVisualPositionFrom(i2, bias, shape, i3, biasArr);
            case 2:
            case 4:
            case 6:
            default:
                throw new IllegalArgumentException("Bad direction: " + i3);
            case 3:
                return getNextEastWestVisualPositionFrom(i2, bias, shape, i3, biasArr);
            case 5:
                return getNextNorthSouthVisualPositionFrom(i2, bias, shape, i3, biasArr);
            case 7:
                return getNextEastWestVisualPositionFrom(i2, bias, shape, i3, biasArr);
        }
    }

    @Override // javax.swing.text.View
    public int getViewIndex(int i2, Position.Bias bias) {
        if (bias == Position.Bias.Backward) {
            i2--;
        }
        if (i2 >= getStartOffset() && i2 < getEndOffset()) {
            return getViewIndexAtPosition(i2);
        }
        return -1;
    }

    protected View getViewAtPosition(int i2, Rectangle rectangle) {
        int viewIndexAtPosition = getViewIndexAtPosition(i2);
        if (viewIndexAtPosition >= 0 && viewIndexAtPosition < getViewCount()) {
            View view = getView(viewIndexAtPosition);
            if (rectangle != null) {
                childAllocation(viewIndexAtPosition, rectangle);
            }
            return view;
        }
        return null;
    }

    protected int getViewIndexAtPosition(int i2) {
        return getElement().getElementIndex(i2);
    }

    protected Rectangle getInsideAllocation(Shape shape) {
        Rectangle bounds;
        if (shape != null) {
            if (shape instanceof Rectangle) {
                bounds = (Rectangle) shape;
            } else {
                bounds = shape.getBounds();
            }
            this.childAlloc.setBounds(bounds);
            this.childAlloc.f12372x += getLeftInset();
            this.childAlloc.f12373y += getTopInset();
            this.childAlloc.width -= getLeftInset() + getRightInset();
            this.childAlloc.height -= getTopInset() + getBottomInset();
            return this.childAlloc;
        }
        return null;
    }

    protected void setParagraphInsets(AttributeSet attributeSet) {
        this.top = (short) StyleConstants.getSpaceAbove(attributeSet);
        this.left = (short) StyleConstants.getLeftIndent(attributeSet);
        this.bottom = (short) StyleConstants.getSpaceBelow(attributeSet);
        this.right = (short) StyleConstants.getRightIndent(attributeSet);
    }

    protected void setInsets(short s2, short s3, short s4, short s5) {
        this.top = s2;
        this.left = s3;
        this.right = s5;
        this.bottom = s4;
    }

    protected short getLeftInset() {
        return this.left;
    }

    protected short getRightInset() {
        return this.right;
    }

    protected short getTopInset() {
        return this.top;
    }

    protected short getBottomInset() {
        return this.bottom;
    }

    protected int getNextNorthSouthVisualPositionFrom(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
        return Utilities.getNextVisualPositionFrom(this, i2, bias, shape, i3, biasArr);
    }

    protected int getNextEastWestVisualPositionFrom(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
        return Utilities.getNextVisualPositionFrom(this, i2, bias, shape, i3, biasArr);
    }

    protected boolean flipEastAndWestAtEnds(int i2, Position.Bias bias) {
        return false;
    }
}
