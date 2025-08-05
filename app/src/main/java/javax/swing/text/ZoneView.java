package javax.swing.text;

import java.awt.Graphics;
import java.awt.Shape;
import java.util.Vector;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/ZoneView.class */
public class ZoneView extends BoxView {
    int maxZoneSize;
    int maxZonesLoaded;
    Vector<View> loadedZones;

    public ZoneView(Element element, int i2) {
        super(element, i2);
        this.maxZoneSize = 8192;
        this.maxZonesLoaded = 3;
        this.loadedZones = new Vector<>();
    }

    public int getMaximumZoneSize() {
        return this.maxZoneSize;
    }

    public void setMaximumZoneSize(int i2) {
        this.maxZoneSize = i2;
    }

    public int getMaxZonesLoaded() {
        return this.maxZonesLoaded;
    }

    public void setMaxZonesLoaded(int i2) {
        if (i2 < 1) {
            throw new IllegalArgumentException("ZoneView.setMaxZonesLoaded must be greater than 0.");
        }
        this.maxZonesLoaded = i2;
        unloadOldZones();
    }

    protected void zoneWasLoaded(View view) {
        this.loadedZones.addElement(view);
        unloadOldZones();
    }

    void unloadOldZones() {
        while (this.loadedZones.size() > getMaxZonesLoaded()) {
            View viewElementAt = this.loadedZones.elementAt(0);
            this.loadedZones.removeElementAt(0);
            unloadZone(viewElementAt);
        }
    }

    protected void unloadZone(View view) {
        view.removeAll();
    }

    protected boolean isZoneLoaded(View view) {
        return view.getViewCount() > 0;
    }

    protected View createZone(int i2, int i3) {
        Document document = getDocument();
        try {
            return new Zone(getElement(), document.createPosition(i2), document.createPosition(i3));
        } catch (BadLocationException e2) {
            throw new StateInvariantError(e2.getMessage());
        }
    }

    @Override // javax.swing.text.CompositeView
    protected void loadChildren(ViewFactory viewFactory) {
        getDocument();
        int startOffset = getStartOffset();
        int endOffset = getEndOffset();
        append(createZone(startOffset, endOffset));
        handleInsert(startOffset, endOffset - startOffset);
    }

    @Override // javax.swing.text.CompositeView
    protected int getViewIndexAtPosition(int i2) {
        int viewCount = getViewCount();
        if (i2 == getEndOffset()) {
            return viewCount - 1;
        }
        for (int i3 = 0; i3 < viewCount; i3++) {
            View view = getView(i3);
            if (i2 >= view.getStartOffset() && i2 < view.getEndOffset()) {
                return i3;
            }
        }
        return -1;
    }

    void handleInsert(int i2, int i3) {
        int viewIndex = getViewIndex(i2, Position.Bias.Forward);
        View view = getView(viewIndex);
        int startOffset = view.getStartOffset();
        int endOffset = view.getEndOffset();
        if (endOffset - startOffset > this.maxZoneSize) {
            splitZone(viewIndex, startOffset, endOffset);
        }
    }

    void handleRemove(int i2, int i3) {
    }

    void splitZone(int i2, int i3, int i4) {
        getElement().getDocument();
        Vector vector = new Vector();
        int iMin = i3;
        do {
            int i5 = iMin;
            iMin = Math.min(getDesiredZoneEnd(i5), i4);
            vector.addElement(createZone(i5, iMin));
        } while (iMin < i4);
        getView(i2);
        View[] viewArr = new View[vector.size()];
        vector.copyInto(viewArr);
        replace(i2, 1, viewArr);
    }

    int getDesiredZoneEnd(int i2) {
        Element element = getElement();
        Element element2 = element.getElement(element.getElementIndex(i2 + (this.maxZoneSize / 2)));
        int startOffset = element2.getStartOffset();
        int endOffset = element2.getEndOffset();
        if (endOffset - i2 > this.maxZoneSize && startOffset > i2) {
            return startOffset;
        }
        return endOffset;
    }

    @Override // javax.swing.text.View
    protected boolean updateChildren(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, ViewFactory viewFactory) {
        return false;
    }

    @Override // javax.swing.text.View
    public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        handleInsert(documentEvent.getOffset(), documentEvent.getLength());
        super.insertUpdate(documentEvent, shape, viewFactory);
    }

    @Override // javax.swing.text.View
    public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        handleRemove(documentEvent.getOffset(), documentEvent.getLength());
        super.removeUpdate(documentEvent, shape, viewFactory);
    }

    /* loaded from: rt.jar:javax/swing/text/ZoneView$Zone.class */
    class Zone extends AsyncBoxView {
        private Position start;
        private Position end;

        public Zone(Element element, Position position, Position position2) {
            super(element, ZoneView.this.getAxis());
            this.start = position;
            this.end = position2;
        }

        public void load() {
            if (!isLoaded()) {
                setEstimatedMajorSpan(true);
                Element element = getElement();
                ViewFactory viewFactory = getViewFactory();
                int elementIndex = element.getElementIndex(getStartOffset());
                int elementIndex2 = element.getElementIndex(getEndOffset());
                View[] viewArr = new View[(elementIndex2 - elementIndex) + 1];
                for (int i2 = elementIndex; i2 <= elementIndex2; i2++) {
                    viewArr[i2 - elementIndex] = viewFactory.create(element.getElement(i2));
                }
                replace(0, 0, viewArr);
                ZoneView.this.zoneWasLoaded(this);
            }
        }

        public void unload() {
            setEstimatedMajorSpan(true);
            removeAll();
        }

        public boolean isLoaded() {
            return getViewCount() != 0;
        }

        @Override // javax.swing.text.AsyncBoxView
        protected void loadChildren(ViewFactory viewFactory) {
            setEstimatedMajorSpan(true);
            Element element = getElement();
            int elementIndex = element.getElementIndex(getStartOffset());
            int elementIndex2 = element.getElementIndex(getEndOffset()) - elementIndex;
            View viewCreate = viewFactory.create(element.getElement(elementIndex));
            viewCreate.setParent(this);
            float preferredSpan = viewCreate.getPreferredSpan(0);
            float preferredSpan2 = viewCreate.getPreferredSpan(1);
            if (getMajorAxis() == 0) {
                preferredSpan *= elementIndex2;
            } else {
                preferredSpan2 += elementIndex2;
            }
            setSize(preferredSpan, preferredSpan2);
        }

        @Override // javax.swing.text.AsyncBoxView
        protected void flushRequirementChanges() {
            if (isLoaded()) {
                super.flushRequirementChanges();
            }
        }

        @Override // javax.swing.text.AsyncBoxView, javax.swing.text.View
        public int getViewIndex(int i2, Position.Bias bias) {
            int iMax = bias == Position.Bias.Backward ? Math.max(0, i2 - 1) : i2;
            Element element = getElement();
            return element.getElementIndex(iMax) - element.getElementIndex(getStartOffset());
        }

        @Override // javax.swing.text.View
        protected boolean updateChildren(DocumentEvent.ElementChange elementChange, DocumentEvent documentEvent, ViewFactory viewFactory) {
            Element[] childrenRemoved = elementChange.getChildrenRemoved();
            Element[] childrenAdded = elementChange.getChildrenAdded();
            Element element = getElement();
            int elementIndex = element.getElementIndex(getStartOffset());
            int elementIndex2 = element.getElementIndex(getEndOffset() - 1);
            int index = elementChange.getIndex();
            if (index >= elementIndex && index <= elementIndex2) {
                int i2 = index - elementIndex;
                int iMin = Math.min((elementIndex2 - elementIndex) + 1, childrenAdded.length);
                int iMin2 = Math.min((elementIndex2 - elementIndex) + 1, childrenRemoved.length);
                View[] viewArr = new View[iMin];
                for (int i3 = 0; i3 < iMin; i3++) {
                    viewArr[i3] = viewFactory.create(childrenAdded[i3]);
                }
                replace(i2, iMin2, viewArr);
                return true;
            }
            return true;
        }

        @Override // javax.swing.text.View
        public AttributeSet getAttributes() {
            return ZoneView.this.getAttributes();
        }

        @Override // javax.swing.text.AsyncBoxView, javax.swing.text.View
        public void paint(Graphics graphics, Shape shape) {
            load();
            super.paint(graphics, shape);
        }

        @Override // javax.swing.text.AsyncBoxView, javax.swing.text.View
        public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
            load();
            return super.viewToModel(f2, f3, shape, biasArr);
        }

        @Override // javax.swing.text.AsyncBoxView, javax.swing.text.View
        public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
            load();
            return super.modelToView(i2, shape, bias);
        }

        @Override // javax.swing.text.View
        public int getStartOffset() {
            return this.start.getOffset();
        }

        @Override // javax.swing.text.View
        public int getEndOffset() {
            return this.end.getOffset();
        }

        @Override // javax.swing.text.View
        public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            if (isLoaded()) {
                super.insertUpdate(documentEvent, shape, viewFactory);
            }
        }

        @Override // javax.swing.text.View
        public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            if (isLoaded()) {
                super.removeUpdate(documentEvent, shape, viewFactory);
            }
        }

        @Override // javax.swing.text.View
        public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
            if (isLoaded()) {
                super.changedUpdate(documentEvent, shape, viewFactory);
            }
        }
    }
}
