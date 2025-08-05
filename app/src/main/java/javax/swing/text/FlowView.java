package javax.swing.text;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Vector;
import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/FlowView.class */
public abstract class FlowView extends BoxView {
    protected int layoutSpan;
    protected View layoutPool;
    protected FlowStrategy strategy;

    protected abstract View createRow();

    public FlowView(Element element, int i2) {
        super(element, i2);
        this.layoutSpan = Integer.MAX_VALUE;
        this.strategy = new FlowStrategy();
    }

    public int getFlowAxis() {
        if (getAxis() == 1) {
            return 0;
        }
        return 1;
    }

    public int getFlowSpan(int i2) {
        return this.layoutSpan;
    }

    public int getFlowStart(int i2) {
        return 0;
    }

    @Override // javax.swing.text.CompositeView
    protected void loadChildren(ViewFactory viewFactory) {
        if (this.layoutPool == null) {
            this.layoutPool = new LogicalView(getElement());
        }
        this.layoutPool.setParent(this);
        this.strategy.insertUpdate(this, null, null);
    }

    @Override // javax.swing.text.CompositeView
    protected int getViewIndexAtPosition(int i2) {
        if (i2 >= getStartOffset() && i2 < getEndOffset()) {
            for (int i3 = 0; i3 < getViewCount(); i3++) {
                View view = getView(i3);
                if (i2 >= view.getStartOffset() && i2 < view.getEndOffset()) {
                    return i3;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // javax.swing.text.BoxView
    protected void layout(int i2, int i3) {
        int i4;
        int flowAxis = getFlowAxis();
        if (flowAxis == 0) {
            i4 = i2;
        } else {
            i4 = i3;
        }
        if (this.layoutSpan != i4) {
            layoutChanged(flowAxis);
            layoutChanged(getAxis());
            this.layoutSpan = i4;
        }
        if (!isLayoutValid(flowAxis)) {
            int axis = getAxis();
            int width = axis == 0 ? getWidth() : getHeight();
            this.strategy.layout(this);
            if (width != ((int) getPreferredSpan(axis))) {
                View parent = getParent();
                if (parent != null) {
                    parent.preferenceChanged(this, axis == 0, axis == 1);
                }
                Container container = getContainer();
                if (container != null) {
                    container.repaint();
                }
            }
        }
        super.layout(i2, i3);
    }

    @Override // javax.swing.text.BoxView
    protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
        if (sizeRequirements == null) {
            sizeRequirements = new SizeRequirements();
        }
        float preferredSpan = this.layoutPool.getPreferredSpan(i2);
        sizeRequirements.minimum = (int) this.layoutPool.getMinimumSpan(i2);
        sizeRequirements.preferred = Math.max(sizeRequirements.minimum, (int) preferredSpan);
        sizeRequirements.maximum = Integer.MAX_VALUE;
        sizeRequirements.alignment = 0.5f;
        return sizeRequirements;
    }

    @Override // javax.swing.text.View
    public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        this.layoutPool.insertUpdate(documentEvent, shape, viewFactory);
        this.strategy.insertUpdate(this, documentEvent, getInsideAllocation(shape));
    }

    @Override // javax.swing.text.View
    public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        this.layoutPool.removeUpdate(documentEvent, shape, viewFactory);
        this.strategy.removeUpdate(this, documentEvent, getInsideAllocation(shape));
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        this.layoutPool.changedUpdate(documentEvent, shape, viewFactory);
        this.strategy.changedUpdate(this, documentEvent, getInsideAllocation(shape));
    }

    @Override // javax.swing.text.CompositeView, javax.swing.text.View
    public void setParent(View view) {
        super.setParent(view);
        if (view == null && this.layoutPool != null) {
            this.layoutPool.setParent(null);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/FlowView$FlowStrategy.class */
    public static class FlowStrategy {
        Position damageStart = null;
        Vector<View> viewBuffer;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !FlowView.class.desiredAssertionStatus();
        }

        void addDamage(FlowView flowView, int i2) {
            if (i2 >= flowView.getStartOffset() && i2 < flowView.getEndOffset()) {
                if (this.damageStart == null || i2 < this.damageStart.getOffset()) {
                    try {
                        this.damageStart = flowView.getDocument().createPosition(i2);
                    } catch (BadLocationException e2) {
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                    }
                }
            }
        }

        void unsetDamage() {
            this.damageStart = null;
        }

        public void insertUpdate(FlowView flowView, DocumentEvent documentEvent, Rectangle rectangle) {
            if (documentEvent != null) {
                addDamage(flowView, documentEvent.getOffset());
            }
            if (rectangle != null) {
                Container container = flowView.getContainer();
                if (container != null) {
                    container.repaint(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                    return;
                }
                return;
            }
            flowView.preferenceChanged(null, true, true);
        }

        public void removeUpdate(FlowView flowView, DocumentEvent documentEvent, Rectangle rectangle) {
            addDamage(flowView, documentEvent.getOffset());
            if (rectangle != null) {
                Container container = flowView.getContainer();
                if (container != null) {
                    container.repaint(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                    return;
                }
                return;
            }
            flowView.preferenceChanged(null, true, true);
        }

        public void changedUpdate(FlowView flowView, DocumentEvent documentEvent, Rectangle rectangle) {
            addDamage(flowView, documentEvent.getOffset());
            if (rectangle != null) {
                Container container = flowView.getContainer();
                if (container != null) {
                    container.repaint(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                    return;
                }
                return;
            }
            flowView.preferenceChanged(null, true, true);
        }

        protected View getLogicalView(FlowView flowView) {
            return flowView.layoutPool;
        }

        public void layout(FlowView flowView) {
            int i2;
            int startOffset;
            View logicalView = getLogicalView(flowView);
            int endOffset = flowView.getEndOffset();
            if (flowView.majorAllocValid) {
                if (this.damageStart == null) {
                    return;
                }
                int offset = this.damageStart.getOffset();
                while (true) {
                    int viewIndexAtPosition = flowView.getViewIndexAtPosition(offset);
                    i2 = viewIndexAtPosition;
                    if (viewIndexAtPosition >= 0) {
                        break;
                    } else {
                        offset--;
                    }
                }
                if (i2 > 0) {
                    i2--;
                }
                startOffset = flowView.getView(i2).getStartOffset();
            } else {
                i2 = 0;
                startOffset = flowView.getStartOffset();
            }
            reparentViews(logicalView, startOffset);
            this.viewBuffer = new Vector<>(10, 10);
            int viewCount = flowView.getViewCount();
            while (startOffset < endOffset) {
                if (i2 >= viewCount) {
                    flowView.append(flowView.createRow());
                } else {
                    flowView.getView(i2);
                }
                startOffset = layoutRow(flowView, i2, startOffset);
                i2++;
            }
            this.viewBuffer = null;
            if (i2 < viewCount) {
                flowView.replace(i2, viewCount - i2, null);
            }
            unsetDamage();
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r21v1, types: [java.lang.Object, javax.swing.text.View] */
        /* JADX WARN: Type inference failed for: r21v2 */
        /* JADX WARN: Type inference failed for: r21v4 */
        /* JADX WARN: Type inference failed for: r7v0, types: [javax.swing.text.FlowView] */
        protected int layoutRow(FlowView flowView, int i2, int i3) {
            float preferredSpan;
            View view = flowView.getView(i2);
            float flowStart = flowView.getFlowStart(i2);
            float flowSpan = flowView.getFlowSpan(i2);
            int endOffset = flowView.getEndOffset();
            TabExpander tabExpander = flowView instanceof TabExpander ? (TabExpander) flowView : null;
            int flowAxis = flowView.getFlowAxis();
            int i4 = 0;
            float f2 = 0.0f;
            float f3 = 0.0f;
            int i5 = -1;
            int i6 = 0;
            this.viewBuffer.clear();
            while (true) {
                if (i3 >= endOffset || flowSpan < 0.0f) {
                    break;
                }
                View viewCreateView = createView(flowView, i3, (int) flowSpan, i2);
                if (viewCreateView == 0) {
                    break;
                }
                int breakWeight = viewCreateView.getBreakWeight(flowAxis, flowStart, flowSpan);
                if (breakWeight >= 3000) {
                    View viewBreakView = viewCreateView.breakView(flowAxis, i3, flowStart, flowSpan);
                    if (viewBreakView != null) {
                        this.viewBuffer.add(viewBreakView);
                    } else if (i6 == 0) {
                        this.viewBuffer.add(viewCreateView);
                    }
                } else {
                    if (breakWeight >= i4 && breakWeight > 0) {
                        i4 = breakWeight;
                        f2 = flowStart;
                        f3 = flowSpan;
                        i5 = i6;
                    }
                    if (flowAxis == 0 && (viewCreateView instanceof TabableView)) {
                        preferredSpan = ((TabableView) viewCreateView).getTabbedSpan(flowStart, tabExpander);
                    } else {
                        preferredSpan = viewCreateView.getPreferredSpan(flowAxis);
                    }
                    if (preferredSpan > flowSpan && i5 >= 0) {
                        View view2 = viewCreateView;
                        if (i5 < i6) {
                            view2 = this.viewBuffer.get(i5);
                        }
                        for (int i7 = i6 - 1; i7 >= i5; i7--) {
                            this.viewBuffer.remove(i7);
                        }
                        viewCreateView = view2.breakView(flowAxis, view2.getStartOffset(), f2, f3);
                    }
                    flowSpan -= preferredSpan;
                    flowStart += preferredSpan;
                    this.viewBuffer.add(viewCreateView);
                    i3 = viewCreateView.getEndOffset();
                    i6++;
                }
            }
            View[] viewArr = new View[this.viewBuffer.size()];
            this.viewBuffer.toArray(viewArr);
            view.replace(0, view.getViewCount(), viewArr);
            return viewArr.length > 0 ? view.getEndOffset() : i3;
        }

        protected void adjustRow(FlowView flowView, int i2, int i3, int i4) {
            int flowAxis = flowView.getFlowAxis();
            View view = flowView.getView(i2);
            int viewCount = view.getViewCount();
            int preferredSpan = 0;
            int i5 = 0;
            int i6 = 0;
            int i7 = -1;
            for (int i8 = 0; i8 < viewCount; i8++) {
                View view2 = view.getView(i8);
                int breakWeight = view2.getBreakWeight(flowAxis, i4 + preferredSpan, i3 - preferredSpan);
                if (breakWeight >= i5 && breakWeight > 0) {
                    i5 = breakWeight;
                    i7 = i8;
                    i6 = preferredSpan;
                    if (breakWeight >= 3000) {
                        break;
                    }
                }
                preferredSpan = (int) (preferredSpan + view2.getPreferredSpan(flowAxis));
            }
            if (i7 < 0) {
                return;
            }
            View view3 = view.getView(i7);
            View[] viewArr = {view3.breakView(flowAxis, view3.getStartOffset(), i4 + i6, i3 - i6)};
            View logicalView = getLogicalView(flowView);
            int startOffset = view.getView(i7).getStartOffset();
            int endOffset = view.getEndOffset();
            for (int i9 = 0; i9 < logicalView.getViewCount(); i9++) {
                View view4 = logicalView.getView(i9);
                if (view4.getEndOffset() > endOffset) {
                    break;
                }
                if (view4.getStartOffset() >= startOffset) {
                    view4.setParent(logicalView);
                }
            }
            view.replace(i7, viewCount - i7, viewArr);
        }

        void reparentViews(View view, int i2) {
            int viewIndex = view.getViewIndex(i2, Position.Bias.Forward);
            if (viewIndex >= 0) {
                for (int i3 = viewIndex; i3 < view.getViewCount(); i3++) {
                    view.getView(i3).setParent(view);
                }
            }
        }

        protected View createView(FlowView flowView, int i2, int i3, int i4) {
            View logicalView = getLogicalView(flowView);
            View view = logicalView.getView(logicalView.getViewIndex(i2, Position.Bias.Forward));
            if (i2 == view.getStartOffset()) {
                return view;
            }
            return view.createFragment(i2, view.getEndOffset());
        }
    }

    /* loaded from: rt.jar:javax/swing/text/FlowView$LogicalView.class */
    static class LogicalView extends CompositeView {
        LogicalView(Element element) {
            super(element);
        }

        @Override // javax.swing.text.CompositeView
        protected int getViewIndexAtPosition(int i2) {
            if (getElement().isLeaf()) {
                return 0;
            }
            return super.getViewIndexAtPosition(i2);
        }

        @Override // javax.swing.text.CompositeView
        protected void loadChildren(ViewFactory viewFactory) {
            Element element = getElement();
            if (element.isLeaf()) {
                append(new LabelView(element));
            } else {
                super.loadChildren(viewFactory);
            }
        }

        @Override // javax.swing.text.View
        public AttributeSet getAttributes() {
            View parent = getParent();
            if (parent != null) {
                return parent.getAttributes();
            }
            return null;
        }

        @Override // javax.swing.text.View
        public float getPreferredSpan(int i2) {
            float fMax = 0.0f;
            float preferredSpan = 0.0f;
            int viewCount = getViewCount();
            for (int i3 = 0; i3 < viewCount; i3++) {
                View view = getView(i3);
                preferredSpan += view.getPreferredSpan(i2);
                if (view.getBreakWeight(i2, 0.0f, 2.1474836E9f) >= 3000) {
                    fMax = Math.max(fMax, preferredSpan);
                    preferredSpan = 0.0f;
                }
            }
            return Math.max(fMax, preferredSpan);
        }

        @Override // javax.swing.text.View
        public float getMinimumSpan(int i2) {
            float fMax = 0.0f;
            float preferredSpan = 0.0f;
            boolean z2 = false;
            int viewCount = getViewCount();
            for (int i3 = 0; i3 < viewCount; i3++) {
                View view = getView(i3);
                if (view.getBreakWeight(i2, 0.0f, 2.1474836E9f) == 0) {
                    preferredSpan += view.getPreferredSpan(i2);
                    z2 = true;
                } else if (z2) {
                    fMax = Math.max(preferredSpan, fMax);
                    z2 = false;
                    preferredSpan = 0.0f;
                }
                if (view instanceof ComponentView) {
                    fMax = Math.max(fMax, view.getMinimumSpan(i2));
                }
            }
            return Math.max(fMax, preferredSpan);
        }

        @Override // javax.swing.text.View
        protected void forwardUpdateToView(View view, DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            View parent = view.getParent();
            view.setParent(this);
            super.forwardUpdateToView(view, documentEvent, shape, viewFactory);
            view.setParent(parent);
        }

        @Override // javax.swing.text.View
        protected void forwardUpdate(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            super.forwardUpdate(elementChange, documentEvent, shape, viewFactory);
            DocumentEvent.EventType type = documentEvent.getType();
            if (type == DocumentEvent.EventType.INSERT || type == DocumentEvent.EventType.REMOVE) {
                this.firstUpdateIndex = Math.min(this.lastUpdateIndex + 1, getViewCount() - 1);
                this.lastUpdateIndex = Math.max(getViewCount() - 1, 0);
                for (int i2 = this.firstUpdateIndex; i2 <= this.lastUpdateIndex; i2++) {
                    View view = getView(i2);
                    if (view != null) {
                        view.updateAfterChange();
                    }
                }
            }
        }

        @Override // javax.swing.text.View
        public void paint(Graphics graphics, Shape shape) {
        }

        @Override // javax.swing.text.CompositeView
        protected boolean isBefore(int i2, int i3, Rectangle rectangle) {
            return false;
        }

        @Override // javax.swing.text.CompositeView
        protected boolean isAfter(int i2, int i3, Rectangle rectangle) {
            return false;
        }

        @Override // javax.swing.text.CompositeView
        protected View getViewAtPoint(int i2, int i3, Rectangle rectangle) {
            return null;
        }

        @Override // javax.swing.text.CompositeView
        protected void childAllocation(int i2, Rectangle rectangle) {
        }
    }
}
