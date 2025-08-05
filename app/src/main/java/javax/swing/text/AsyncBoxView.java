package javax.swing.text;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/AsyncBoxView.class */
public class AsyncBoxView extends View {
    int axis;
    List<ChildState> stats;
    float majorSpan;
    boolean estimatedMajorSpan;
    float minorSpan;
    protected ChildLocator locator;
    float topInset;
    float bottomInset;
    float leftInset;
    float rightInset;
    ChildState minRequest;
    ChildState prefRequest;
    boolean majorChanged;
    boolean minorChanged;
    Runnable flushTask;
    ChildState changing;

    public AsyncBoxView(Element element, int i2) {
        super(element);
        this.stats = new ArrayList();
        this.axis = i2;
        this.locator = new ChildLocator();
        this.flushTask = new FlushTask();
        this.minorSpan = 32767.0f;
        this.estimatedMajorSpan = false;
    }

    public int getMajorAxis() {
        return this.axis;
    }

    public int getMinorAxis() {
        return this.axis == 0 ? 1 : 0;
    }

    public float getTopInset() {
        return this.topInset;
    }

    public void setTopInset(float f2) {
        this.topInset = f2;
    }

    public float getBottomInset() {
        return this.bottomInset;
    }

    public void setBottomInset(float f2) {
        this.bottomInset = f2;
    }

    public float getLeftInset() {
        return this.leftInset;
    }

    public void setLeftInset(float f2) {
        this.leftInset = f2;
    }

    public float getRightInset() {
        return this.rightInset;
    }

    public void setRightInset(float f2) {
        this.rightInset = f2;
    }

    protected float getInsetSpan(int i2) {
        return i2 == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
    }

    protected void setEstimatedMajorSpan(boolean z2) {
        this.estimatedMajorSpan = z2;
    }

    protected boolean getEstimatedMajorSpan() {
        return this.estimatedMajorSpan;
    }

    protected ChildState getChildState(int i2) {
        synchronized (this.stats) {
            if (i2 >= 0) {
                if (i2 < this.stats.size()) {
                    return this.stats.get(i2);
                }
            }
            return null;
        }
    }

    protected LayoutQueue getLayoutQueue() {
        return LayoutQueue.getDefaultQueue();
    }

    protected ChildState createChildState(View view) {
        return new ChildState(view);
    }

    protected synchronized void majorRequirementChange(ChildState childState, float f2) {
        if (!this.estimatedMajorSpan) {
            this.majorSpan += f2;
        }
        this.majorChanged = true;
    }

    protected synchronized void minorRequirementChange(ChildState childState) {
        this.minorChanged = true;
    }

    protected void flushRequirementChanges() {
        AbstractDocument abstractDocument = (AbstractDocument) getDocument();
        try {
            abstractDocument.readLock();
            View parent = null;
            boolean z2 = false;
            boolean z3 = false;
            synchronized (this) {
                synchronized (this.stats) {
                    int viewCount = getViewCount();
                    if (viewCount > 0 && (this.minorChanged || this.estimatedMajorSpan)) {
                        getLayoutQueue();
                        ChildState childState = getChildState(0);
                        ChildState childState2 = getChildState(0);
                        float majorSpan = 0.0f;
                        for (int i2 = 1; i2 < viewCount; i2++) {
                            ChildState childState3 = getChildState(i2);
                            if (this.minorChanged) {
                                if (childState3.min > childState.min) {
                                    childState = childState3;
                                }
                                if (childState3.pref > childState2.pref) {
                                    childState2 = childState3;
                                }
                            }
                            if (this.estimatedMajorSpan) {
                                majorSpan += childState3.getMajorSpan();
                            }
                        }
                        if (this.minorChanged) {
                            this.minRequest = childState;
                            this.prefRequest = childState2;
                        }
                        if (this.estimatedMajorSpan) {
                            this.majorSpan = majorSpan;
                            this.estimatedMajorSpan = false;
                            this.majorChanged = true;
                        }
                    }
                }
                if (this.majorChanged || this.minorChanged) {
                    parent = getParent();
                    if (parent != null) {
                        if (this.axis == 0) {
                            z2 = this.majorChanged;
                            z3 = this.minorChanged;
                        } else {
                            z3 = this.majorChanged;
                            z2 = this.minorChanged;
                        }
                    }
                    this.majorChanged = false;
                    this.minorChanged = false;
                }
            }
            if (parent != null) {
                parent.preferenceChanged(this, z2, z3);
                Container container = getContainer();
                if (container != null) {
                    container.repaint();
                }
            }
        } finally {
            abstractDocument.readUnlock();
        }
    }

    @Override // javax.swing.text.View
    public void replace(int i2, int i3, View[] viewArr) {
        synchronized (this.stats) {
            for (int i4 = 0; i4 < i3; i4++) {
                ChildState childStateRemove = this.stats.remove(i2);
                float majorSpan = childStateRemove.getMajorSpan();
                childStateRemove.getChildView().setParent(null);
                if (majorSpan != 0.0f) {
                    majorRequirementChange(childStateRemove, -majorSpan);
                }
            }
            LayoutQueue layoutQueue = getLayoutQueue();
            if (viewArr != null) {
                for (int i5 = 0; i5 < viewArr.length; i5++) {
                    ChildState childStateCreateChildState = createChildState(viewArr[i5]);
                    this.stats.add(i2 + i5, childStateCreateChildState);
                    layoutQueue.addTask(childStateCreateChildState);
                }
            }
            layoutQueue.addTask(this.flushTask);
        }
    }

    protected void loadChildren(ViewFactory viewFactory) {
        Element element = getElement();
        int elementCount = element.getElementCount();
        if (elementCount > 0) {
            View[] viewArr = new View[elementCount];
            for (int i2 = 0; i2 < elementCount; i2++) {
                viewArr[i2] = viewFactory.create(element.getElement(i2));
            }
            replace(0, 0, viewArr);
        }
    }

    protected synchronized int getViewIndexAtPosition(int i2, Position.Bias bias) {
        return getElement().getElementIndex(bias == Position.Bias.Backward ? Math.max(0, i2 - 1) : i2);
    }

    @Override // javax.swing.text.View
    protected void updateLayout(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, Shape shape) {
        if (elementChange != null) {
            this.locator.childChanged(getChildState(Math.max(elementChange.getIndex() - 1, 0)));
        }
    }

    @Override // javax.swing.text.View
    public void setParent(View view) {
        super.setParent(view);
        if (view != null && getViewCount() == 0) {
            loadChildren(getViewFactory());
        }
    }

    @Override // javax.swing.text.View
    public synchronized void preferenceChanged(View view, boolean z2, boolean z3) {
        if (view == null) {
            getParent().preferenceChanged(this, z2, z3);
            return;
        }
        if (this.changing != null && this.changing.getChildView() == view) {
            this.changing.preferenceChanged(z2, z3);
            return;
        }
        ChildState childState = getChildState(getViewIndex(view.getStartOffset(), Position.Bias.Forward));
        childState.preferenceChanged(z2, z3);
        LayoutQueue layoutQueue = getLayoutQueue();
        layoutQueue.addTask(childState);
        layoutQueue.addTask(this.flushTask);
    }

    @Override // javax.swing.text.View
    public void setSize(float f2, float f3) {
        setSpanOnAxis(0, f2);
        setSpanOnAxis(1, f3);
    }

    float getSpanOnAxis(int i2) {
        if (i2 == getMajorAxis()) {
            return this.majorSpan;
        }
        return this.minorSpan;
    }

    void setSpanOnAxis(int i2, float f2) {
        float insetSpan = getInsetSpan(i2);
        if (i2 == getMinorAxis()) {
            float f3 = f2 - insetSpan;
            if (f3 != this.minorSpan) {
                this.minorSpan = f3;
                int viewCount = getViewCount();
                if (viewCount != 0) {
                    LayoutQueue layoutQueue = getLayoutQueue();
                    for (int i3 = 0; i3 < viewCount; i3++) {
                        ChildState childState = getChildState(i3);
                        childState.childSizeValid = false;
                        layoutQueue.addTask(childState);
                    }
                    layoutQueue.addTask(this.flushTask);
                    return;
                }
                return;
            }
            return;
        }
        if (this.estimatedMajorSpan) {
            this.majorSpan = f2 - insetSpan;
        }
    }

    @Override // javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        synchronized (this.locator) {
            this.locator.setAllocation(shape);
            this.locator.paintChildren(graphics);
        }
    }

    @Override // javax.swing.text.View
    public float getPreferredSpan(int i2) {
        float insetSpan = getInsetSpan(i2);
        if (i2 == this.axis) {
            return this.majorSpan + insetSpan;
        }
        if (this.prefRequest != null) {
            return this.prefRequest.getChildView().getPreferredSpan(i2) + insetSpan;
        }
        return insetSpan + 30.0f;
    }

    @Override // javax.swing.text.View
    public float getMinimumSpan(int i2) {
        if (i2 == this.axis) {
            return getPreferredSpan(i2);
        }
        if (this.minRequest != null) {
            return this.minRequest.getChildView().getMinimumSpan(i2);
        }
        if (i2 == 0) {
            return getLeftInset() + getRightInset() + 5.0f;
        }
        return getTopInset() + getBottomInset() + 5.0f;
    }

    @Override // javax.swing.text.View
    public float getMaximumSpan(int i2) {
        if (i2 == this.axis) {
            return getPreferredSpan(i2);
        }
        return 2.1474836E9f;
    }

    @Override // javax.swing.text.View
    public int getViewCount() {
        int size;
        synchronized (this.stats) {
            size = this.stats.size();
        }
        return size;
    }

    @Override // javax.swing.text.View
    public View getView(int i2) {
        ChildState childState = getChildState(i2);
        if (childState != null) {
            return childState.getChildView();
        }
        return null;
    }

    @Override // javax.swing.text.View
    public Shape getChildAllocation(int i2, Shape shape) {
        return this.locator.getChildAllocation(i2, shape);
    }

    @Override // javax.swing.text.View
    public int getViewIndex(int i2, Position.Bias bias) {
        return getViewIndexAtPosition(i2, bias);
    }

    @Override // javax.swing.text.View
    public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
        Shape shapeModelToView;
        int viewIndex = getViewIndex(i2, bias);
        Shape childAllocation = this.locator.getChildAllocation(viewIndex, shape);
        ChildState childState = getChildState(viewIndex);
        synchronized (childState) {
            shapeModelToView = childState.getChildView().modelToView(i2, childAllocation, bias);
        }
        return shapeModelToView;
    }

    @Override // javax.swing.text.View
    public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        int viewIndexAtPoint;
        Shape childAllocation;
        int iViewToModel;
        synchronized (this.locator) {
            viewIndexAtPoint = this.locator.getViewIndexAtPoint(f2, f3, shape);
            childAllocation = this.locator.getChildAllocation(viewIndexAtPoint, shape);
        }
        ChildState childState = getChildState(viewIndexAtPoint);
        synchronized (childState) {
            iViewToModel = childState.getChildView().viewToModel(f2, f3, childAllocation, biasArr);
        }
        return iViewToModel;
    }

    @Override // javax.swing.text.View
    public int getNextVisualPositionFrom(int i2, Position.Bias bias, Shape shape, int i3, Position.Bias[] biasArr) throws BadLocationException {
        if (i2 < -1) {
            throw new BadLocationException("invalid position", i2);
        }
        return Utilities.getNextVisualPositionFrom(this, i2, bias, shape, i3, biasArr);
    }

    /* loaded from: rt.jar:javax/swing/text/AsyncBoxView$ChildLocator.class */
    public class ChildLocator {
        protected ChildState lastValidOffset;
        protected Rectangle lastAlloc = new Rectangle();
        protected Rectangle childAlloc = new Rectangle();

        public ChildLocator() {
        }

        public synchronized void childChanged(ChildState childState) {
            if (this.lastValidOffset == null) {
                this.lastValidOffset = childState;
            } else if (childState.getChildView().getStartOffset() < this.lastValidOffset.getChildView().getStartOffset()) {
                this.lastValidOffset = childState;
            }
        }

        public synchronized void paintChildren(Graphics graphics) {
            Rectangle clipBounds = graphics.getClipBounds();
            int viewIndexAtVisualOffset = getViewIndexAtVisualOffset(AsyncBoxView.this.axis == 0 ? clipBounds.f12372x - this.lastAlloc.f12372x : clipBounds.f12373y - this.lastAlloc.f12373y);
            int viewCount = AsyncBoxView.this.getViewCount();
            float majorOffset = AsyncBoxView.this.getChildState(viewIndexAtVisualOffset).getMajorOffset();
            for (int i2 = viewIndexAtVisualOffset; i2 < viewCount; i2++) {
                ChildState childState = AsyncBoxView.this.getChildState(i2);
                childState.setMajorOffset(majorOffset);
                Shape childAllocation = getChildAllocation(i2);
                if (intersectsClip(childAllocation, clipBounds)) {
                    synchronized (childState) {
                        childState.getChildView().paint(graphics, childAllocation);
                    }
                    majorOffset += childState.getMajorSpan();
                } else {
                    return;
                }
            }
        }

        public synchronized Shape getChildAllocation(int i2, Shape shape) {
            if (shape == null) {
                return null;
            }
            setAllocation(shape);
            ChildState childState = AsyncBoxView.this.getChildState(i2);
            if (this.lastValidOffset == null) {
                this.lastValidOffset = AsyncBoxView.this.getChildState(0);
            }
            if (childState.getChildView().getStartOffset() > this.lastValidOffset.getChildView().getStartOffset()) {
                updateChildOffsetsToIndex(i2);
            }
            return getChildAllocation(i2);
        }

        public int getViewIndexAtPoint(float f2, float f3, Shape shape) {
            setAllocation(shape);
            return getViewIndexAtVisualOffset(AsyncBoxView.this.axis == 0 ? f2 - this.lastAlloc.f12372x : f3 - this.lastAlloc.f12373y);
        }

        protected Shape getChildAllocation(int i2) {
            ChildState childState = AsyncBoxView.this.getChildState(i2);
            if (!childState.isLayoutValid()) {
                childState.run();
            }
            if (AsyncBoxView.this.axis == 0) {
                this.childAlloc.f12372x = this.lastAlloc.f12372x + ((int) childState.getMajorOffset());
                this.childAlloc.f12373y = this.lastAlloc.f12373y + ((int) childState.getMinorOffset());
                this.childAlloc.width = (int) childState.getMajorSpan();
                this.childAlloc.height = (int) childState.getMinorSpan();
            } else {
                this.childAlloc.f12373y = this.lastAlloc.f12373y + ((int) childState.getMajorOffset());
                this.childAlloc.f12372x = this.lastAlloc.f12372x + ((int) childState.getMinorOffset());
                this.childAlloc.height = (int) childState.getMajorSpan();
                this.childAlloc.width = (int) childState.getMinorSpan();
            }
            this.childAlloc.f12372x += (int) AsyncBoxView.this.getLeftInset();
            this.childAlloc.f12373y += (int) AsyncBoxView.this.getRightInset();
            return this.childAlloc;
        }

        protected void setAllocation(Shape shape) {
            if (shape instanceof Rectangle) {
                this.lastAlloc.setBounds((Rectangle) shape);
            } else {
                this.lastAlloc.setBounds(shape.getBounds());
            }
            AsyncBoxView.this.setSize(this.lastAlloc.width, this.lastAlloc.height);
        }

        protected int getViewIndexAtVisualOffset(float f2) {
            int viewCount = AsyncBoxView.this.getViewCount();
            if (viewCount > 0) {
                boolean z2 = this.lastValidOffset != null;
                if (this.lastValidOffset == null) {
                    this.lastValidOffset = AsyncBoxView.this.getChildState(0);
                }
                if (f2 > AsyncBoxView.this.majorSpan) {
                    if (!z2) {
                        return 0;
                    }
                    return AsyncBoxView.this.getViewIndex(this.lastValidOffset.getChildView().getStartOffset(), Position.Bias.Forward);
                }
                if (f2 > this.lastValidOffset.getMajorOffset()) {
                    return updateChildOffsets(f2);
                }
                float f3 = 0.0f;
                for (int i2 = 0; i2 < viewCount; i2++) {
                    float majorSpan = f3 + AsyncBoxView.this.getChildState(i2).getMajorSpan();
                    if (f2 < majorSpan) {
                        return i2;
                    }
                    f3 = majorSpan;
                }
            }
            return viewCount - 1;
        }

        int updateChildOffsets(float f2) {
            int viewCount = AsyncBoxView.this.getViewCount();
            int i2 = viewCount - 1;
            int viewIndex = AsyncBoxView.this.getViewIndex(this.lastValidOffset.getChildView().getStartOffset(), Position.Bias.Forward);
            float majorOffset = this.lastValidOffset.getMajorOffset();
            int i3 = viewIndex;
            while (true) {
                if (i3 >= viewCount) {
                    break;
                }
                ChildState childState = AsyncBoxView.this.getChildState(i3);
                childState.setMajorOffset(majorOffset);
                majorOffset += childState.getMajorSpan();
                if (f2 >= majorOffset) {
                    i3++;
                } else {
                    i2 = i3;
                    this.lastValidOffset = childState;
                    break;
                }
            }
            return i2;
        }

        void updateChildOffsetsToIndex(int i2) {
            int viewIndex = AsyncBoxView.this.getViewIndex(this.lastValidOffset.getChildView().getStartOffset(), Position.Bias.Forward);
            float majorOffset = this.lastValidOffset.getMajorOffset();
            for (int i3 = viewIndex; i3 <= i2; i3++) {
                ChildState childState = AsyncBoxView.this.getChildState(i3);
                childState.setMajorOffset(majorOffset);
                majorOffset += childState.getMajorSpan();
            }
        }

        boolean intersectsClip(Shape shape, Rectangle rectangle) {
            Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
            if (bounds.intersects(rectangle)) {
                return this.lastAlloc.intersects(bounds);
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AsyncBoxView$ChildState.class */
    public class ChildState implements Runnable {
        private float min;
        private float pref;
        private float max;
        private float span;
        private float offset;
        private View child;
        private boolean minorValid = false;
        private boolean majorValid = false;
        private boolean childSizeValid = false;

        public ChildState(View view) {
            this.child = view;
            this.child.setParent(AsyncBoxView.this);
        }

        public View getChildView() {
            return this.child;
        }

        @Override // java.lang.Runnable
        public void run() {
            AbstractDocument abstractDocument = (AbstractDocument) AsyncBoxView.this.getDocument();
            try {
                abstractDocument.readLock();
                if (this.minorValid && this.majorValid && this.childSizeValid) {
                    return;
                }
                if (this.child.getParent() == AsyncBoxView.this) {
                    synchronized (AsyncBoxView.this) {
                        AsyncBoxView.this.changing = this;
                    }
                    updateChild();
                    synchronized (AsyncBoxView.this) {
                        AsyncBoxView.this.changing = null;
                    }
                    updateChild();
                }
                abstractDocument.readUnlock();
            } finally {
                abstractDocument.readUnlock();
            }
        }

        void updateChild() {
            float minorSpan;
            float minorSpan2;
            boolean z2 = false;
            synchronized (this) {
                if (!this.minorValid) {
                    int minorAxis = AsyncBoxView.this.getMinorAxis();
                    this.min = this.child.getMinimumSpan(minorAxis);
                    this.pref = this.child.getPreferredSpan(minorAxis);
                    this.max = this.child.getMaximumSpan(minorAxis);
                    this.minorValid = true;
                    z2 = true;
                }
            }
            if (z2) {
                AsyncBoxView.this.minorRequirementChange(this);
            }
            boolean z3 = false;
            float f2 = 0.0f;
            synchronized (this) {
                if (!this.majorValid) {
                    float f3 = this.span;
                    this.span = this.child.getPreferredSpan(AsyncBoxView.this.axis);
                    f2 = this.span - f3;
                    this.majorValid = true;
                    z3 = true;
                }
            }
            if (z3) {
                AsyncBoxView.this.majorRequirementChange(this, f2);
                AsyncBoxView.this.locator.childChanged(this);
            }
            synchronized (this) {
                if (!this.childSizeValid) {
                    if (AsyncBoxView.this.axis == 0) {
                        minorSpan = this.span;
                        minorSpan2 = getMinorSpan();
                    } else {
                        minorSpan = getMinorSpan();
                        minorSpan2 = this.span;
                    }
                    this.childSizeValid = true;
                    this.child.setSize(minorSpan, minorSpan2);
                }
            }
        }

        public float getMinorSpan() {
            if (this.max < AsyncBoxView.this.minorSpan) {
                return this.max;
            }
            return Math.max(this.min, AsyncBoxView.this.minorSpan);
        }

        public float getMinorOffset() {
            if (this.max < AsyncBoxView.this.minorSpan) {
                return (AsyncBoxView.this.minorSpan - this.max) * this.child.getAlignment(AsyncBoxView.this.getMinorAxis());
            }
            return 0.0f;
        }

        public float getMajorSpan() {
            return this.span;
        }

        public float getMajorOffset() {
            return this.offset;
        }

        public void setMajorOffset(float f2) {
            this.offset = f2;
        }

        public void preferenceChanged(boolean z2, boolean z3) {
            if (AsyncBoxView.this.axis == 0) {
                if (z2) {
                    this.majorValid = false;
                }
                if (z3) {
                    this.minorValid = false;
                }
            } else {
                if (z2) {
                    this.minorValid = false;
                }
                if (z3) {
                    this.majorValid = false;
                }
            }
            this.childSizeValid = false;
        }

        public boolean isLayoutValid() {
            return this.minorValid && this.majorValid && this.childSizeValid;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AsyncBoxView$FlushTask.class */
    class FlushTask implements Runnable {
        FlushTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AsyncBoxView.this.flushRequirementChanges();
        }
    }
}
