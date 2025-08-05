package javax.swing.text;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/View.class */
public abstract class View implements SwingConstants {
    public static final int BadBreakWeight = 0;
    public static final int GoodBreakWeight = 1000;
    public static final int ExcellentBreakWeight = 2000;
    public static final int ForcedBreakWeight = 3000;
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    static final Position.Bias[] sharedBiasReturn = new Position.Bias[1];
    private View parent;
    private Element elem;
    int firstUpdateIndex;
    int lastUpdateIndex;

    public abstract float getPreferredSpan(int i2);

    public abstract void paint(Graphics graphics, Shape shape);

    public abstract Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException;

    public abstract int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr);

    public View(Element element) {
        this.elem = element;
    }

    public View getParent() {
        return this.parent;
    }

    public boolean isVisible() {
        return true;
    }

    public float getMinimumSpan(int i2) {
        if (getResizeWeight(i2) == 0) {
            return getPreferredSpan(i2);
        }
        return 0.0f;
    }

    public float getMaximumSpan(int i2) {
        if (getResizeWeight(i2) == 0) {
            return getPreferredSpan(i2);
        }
        return 2.1474836E9f;
    }

    public void preferenceChanged(View view, boolean z2, boolean z3) {
        View parent = getParent();
        if (parent != null) {
            parent.preferenceChanged(this, z2, z3);
        }
    }

    public float getAlignment(int i2) {
        return 0.5f;
    }

    public void setParent(View view) {
        if (view == null) {
            for (int i2 = 0; i2 < getViewCount(); i2++) {
                if (getView(i2).getParent() == this) {
                    getView(i2).setParent(null);
                }
            }
        }
        this.parent = view;
    }

    public int getViewCount() {
        return 0;
    }

    public View getView(int i2) {
        return null;
    }

    public void removeAll() {
        replace(0, getViewCount(), null);
    }

    public void remove(int i2) {
        replace(i2, 1, null);
    }

    public void insert(int i2, View view) {
        replace(i2, 0, new View[]{view});
    }

    public void append(View view) {
        replace(getViewCount(), 0, new View[]{view});
    }

    public void replace(int i2, int i3, View[] viewArr) {
    }

    public int getViewIndex(int i2, Position.Bias bias) {
        return -1;
    }

    public Shape getChildAllocation(int i2, Shape shape) {
        return null;
    }

    public int getNextVisualPositionFrom(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
        int iMin;
        Point magicCaretPosition;
        int i4;
        if (i2 < -1) {
            throw new BadLocationException("Invalid position", i2);
        }
        biasArr[0] = Position.Bias.Forward;
        switch (i3) {
            case 1:
            case 5:
                if (i2 == -1) {
                    iMin = i3 == 1 ? Math.max(0, getEndOffset() - 1) : getStartOffset();
                    break;
                } else {
                    JTextComponent jTextComponent = (JTextComponent) getContainer();
                    Caret caret = jTextComponent != null ? jTextComponent.getCaret() : null;
                    if (caret != null) {
                        magicCaretPosition = caret.getMagicCaretPosition();
                    } else {
                        magicCaretPosition = null;
                    }
                    if (magicCaretPosition == null) {
                        Rectangle rectangleModelToView = jTextComponent.modelToView(i2);
                        i4 = rectangleModelToView == null ? 0 : rectangleModelToView.f12372x;
                    } else {
                        i4 = magicCaretPosition.f12370x;
                    }
                    if (i3 == 1) {
                        iMin = Utilities.getPositionAbove(jTextComponent, i2, i4);
                        break;
                    } else {
                        iMin = Utilities.getPositionBelow(jTextComponent, i2, i4);
                        break;
                    }
                }
            case 2:
            case 4:
            case 6:
            default:
                throw new IllegalArgumentException("Bad direction: " + i3);
            case 3:
                if (i2 == -1) {
                    iMin = getStartOffset();
                    break;
                } else {
                    iMin = Math.min(i2 + 1, getDocument().getLength());
                    break;
                }
            case 7:
                if (i2 == -1) {
                    iMin = Math.max(0, getEndOffset() - 1);
                    break;
                } else {
                    iMin = Math.max(0, i2 - 1);
                    break;
                }
        }
        return iMin;
    }

    public Shape modelToView(int i2, Position.Bias bias, int i3, Position.Bias bias2, Shape shape) throws BadLocationException {
        Rectangle rectangle;
        Shape shapeModelToView = modelToView(i2, shape, bias);
        if (i3 == getEndOffset()) {
            try {
                rectangle = modelToView(i3, shape, bias2);
            } catch (BadLocationException e2) {
                rectangle = null;
            }
            if (rectangle == null) {
                Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
                rectangle = new Rectangle((bounds.f12372x + bounds.width) - 1, bounds.f12373y, 1, bounds.height);
            }
        } else {
            rectangle = modelToView(i3, shape, bias2);
        }
        Rectangle bounds2 = shapeModelToView.getBounds();
        Rectangle bounds3 = rectangle instanceof Rectangle ? rectangle : rectangle.getBounds();
        if (bounds2.f12373y != bounds3.f12373y) {
            Rectangle bounds4 = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
            bounds2.f12372x = bounds4.f12372x;
            bounds2.width = bounds4.width;
        }
        bounds2.add(bounds3);
        return bounds2;
    }

    public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        if (getViewCount() > 0) {
            DocumentEvent.ElementChange change = documentEvent.getChange(getElement());
            if (change != null && !updateChildren(change, documentEvent, viewFactory)) {
                change = null;
            }
            forwardUpdate(change, documentEvent, shape, viewFactory);
            updateLayout(change, documentEvent, shape);
        }
    }

    public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        if (getViewCount() > 0) {
            DocumentEvent.ElementChange change = documentEvent.getChange(getElement());
            if (change != null && !updateChildren(change, documentEvent, viewFactory)) {
                change = null;
            }
            forwardUpdate(change, documentEvent, shape, viewFactory);
            updateLayout(change, documentEvent, shape);
        }
    }

    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        if (getViewCount() > 0) {
            DocumentEvent.ElementChange change = documentEvent.getChange(getElement());
            if (change != null && !updateChildren(change, documentEvent, viewFactory)) {
                change = null;
            }
            forwardUpdate(change, documentEvent, shape, viewFactory);
            updateLayout(change, documentEvent, shape);
        }
    }

    public Document getDocument() {
        return this.elem.getDocument();
    }

    public int getStartOffset() {
        return this.elem.getStartOffset();
    }

    public int getEndOffset() {
        return this.elem.getEndOffset();
    }

    public Element getElement() {
        return this.elem;
    }

    public Graphics getGraphics() {
        return getContainer().getGraphics();
    }

    public AttributeSet getAttributes() {
        return this.elem.getAttributes();
    }

    public View breakView(int i2, int i3, float f2, float f3) {
        return this;
    }

    public View createFragment(int i2, int i3) {
        return this;
    }

    public int getBreakWeight(int i2, float f2, float f3) {
        if (f3 > getPreferredSpan(i2)) {
            return 1000;
        }
        return 0;
    }

    public int getResizeWeight(int i2) {
        return 0;
    }

    public void setSize(float f2, float f3) {
    }

    public Container getContainer() {
        View parent = getParent();
        if (parent != null) {
            return parent.getContainer();
        }
        return null;
    }

    public ViewFactory getViewFactory() {
        View parent = getParent();
        if (parent != null) {
            return parent.getViewFactory();
        }
        return null;
    }

    public String getToolTipText(float f2, float f3, Shape shape) {
        int viewIndex = getViewIndex(f2, f3, shape);
        if (viewIndex >= 0) {
            Shape childAllocation = getChildAllocation(viewIndex, shape);
            if ((childAllocation instanceof Rectangle ? (Rectangle) childAllocation : childAllocation.getBounds()).contains(f2, f3)) {
                return getView(viewIndex).getToolTipText(f2, f3, childAllocation);
            }
            return null;
        }
        return null;
    }

    public int getViewIndex(float f2, float f3, Shape shape) {
        for (int viewCount = getViewCount() - 1; viewCount >= 0; viewCount--) {
            Shape childAllocation = getChildAllocation(viewCount, shape);
            if (childAllocation != null) {
                if ((childAllocation instanceof Rectangle ? (Rectangle) childAllocation : childAllocation.getBounds()).contains(f2, f3)) {
                    return viewCount;
                }
            }
        }
        return -1;
    }

    protected boolean updateChildren(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, ViewFactory viewFactory) {
        Element[] childrenRemoved = elementChange.getChildrenRemoved();
        Element[] childrenAdded = elementChange.getChildrenAdded();
        View[] viewArr = null;
        if (childrenAdded != null) {
            viewArr = new View[childrenAdded.length];
            for (int i2 = 0; i2 < childrenAdded.length; i2++) {
                viewArr[i2] = viewFactory.create(childrenAdded[i2]);
            }
        }
        int length = 0;
        int index = elementChange.getIndex();
        if (childrenRemoved != null) {
            length = childrenRemoved.length;
        }
        replace(index, length, viewArr);
        return true;
    }

    protected void forwardUpdate(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        View view;
        calculateUpdateIndexes(documentEvent);
        int index = this.lastUpdateIndex + 1;
        int length = index;
        Element[] childrenAdded = elementChange != null ? elementChange.getChildrenAdded() : null;
        if (childrenAdded != null && childrenAdded.length > 0) {
            index = elementChange.getIndex();
            length = (index + childrenAdded.length) - 1;
        }
        for (int i2 = this.firstUpdateIndex; i2 <= this.lastUpdateIndex; i2++) {
            if ((i2 < index || i2 > length) && (view = getView(i2)) != null) {
                forwardUpdateToView(view, documentEvent, getChildAllocation(i2, shape), viewFactory);
            }
        }
    }

    void calculateUpdateIndexes(DocumentEvent documentEvent) {
        int offset = documentEvent.getOffset();
        this.firstUpdateIndex = getViewIndex(offset, Position.Bias.Forward);
        if (this.firstUpdateIndex == -1 && documentEvent.getType() == DocumentEvent.EventType.REMOVE && offset >= getEndOffset()) {
            this.firstUpdateIndex = getViewCount() - 1;
        }
        this.lastUpdateIndex = this.firstUpdateIndex;
        View view = this.firstUpdateIndex >= 0 ? getView(this.firstUpdateIndex) : null;
        if (view != null && view.getStartOffset() == offset && offset > 0) {
            this.firstUpdateIndex = Math.max(this.firstUpdateIndex - 1, 0);
        }
        if (documentEvent.getType() != DocumentEvent.EventType.REMOVE) {
            this.lastUpdateIndex = getViewIndex(offset + documentEvent.getLength(), Position.Bias.Forward);
            if (this.lastUpdateIndex < 0) {
                this.lastUpdateIndex = getViewCount() - 1;
            }
        }
        this.firstUpdateIndex = Math.max(this.firstUpdateIndex, 0);
    }

    void updateAfterChange() {
    }

    protected void forwardUpdateToView(View view, DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        DocumentEvent.EventType type = documentEvent.getType();
        if (type == DocumentEvent.EventType.INSERT) {
            view.insertUpdate(documentEvent, shape, viewFactory);
        } else if (type == DocumentEvent.EventType.REMOVE) {
            view.removeUpdate(documentEvent, shape, viewFactory);
        } else {
            view.changedUpdate(documentEvent, shape, viewFactory);
        }
    }

    protected void updateLayout(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, Shape shape) {
        if (elementChange != null && shape != null) {
            preferenceChanged(null, true, true);
            Container container = getContainer();
            if (container != null) {
                container.repaint();
            }
        }
    }

    @Deprecated
    public Shape modelToView(int i2, Shape shape) throws BadLocationException {
        return modelToView(i2, shape, Position.Bias.Forward);
    }

    @Deprecated
    public int viewToModel(float f2, float f3, Shape shape) {
        sharedBiasReturn[0] = Position.Bias.Forward;
        return viewToModel(f2, f3, shape, sharedBiasReturn);
    }
}
