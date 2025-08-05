package javax.swing.text;

import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.BreakIterator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.text.FlowView;
import javax.swing.text.Position;
import sun.font.BidiUtils;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/TextLayoutStrategy.class */
class TextLayoutStrategy extends FlowView.FlowStrategy {
    private LineBreakMeasurer measurer;
    private AttributedSegment text = new AttributedSegment();

    @Override // javax.swing.text.FlowView.FlowStrategy
    public void insertUpdate(FlowView flowView, DocumentEvent documentEvent, Rectangle rectangle) {
        sync(flowView);
        super.insertUpdate(flowView, documentEvent, rectangle);
    }

    @Override // javax.swing.text.FlowView.FlowStrategy
    public void removeUpdate(FlowView flowView, DocumentEvent documentEvent, Rectangle rectangle) {
        sync(flowView);
        super.removeUpdate(flowView, documentEvent, rectangle);
    }

    @Override // javax.swing.text.FlowView.FlowStrategy
    public void changedUpdate(FlowView flowView, DocumentEvent documentEvent, Rectangle rectangle) {
        sync(flowView);
        super.changedUpdate(flowView, documentEvent, rectangle);
    }

    @Override // javax.swing.text.FlowView.FlowStrategy
    public void layout(FlowView flowView) {
        super.layout(flowView);
    }

    @Override // javax.swing.text.FlowView.FlowStrategy
    protected int layoutRow(FlowView flowView, int i2, int i3) {
        int viewCount;
        int iLayoutRow = super.layoutRow(flowView, i2, i3);
        View view = flowView.getView(i2);
        Object property = flowView.getDocument().getProperty("i18n");
        if (property != null && property.equals(Boolean.TRUE) && (viewCount = view.getViewCount()) > 1) {
            Element bidiRootElement = ((AbstractDocument) flowView.getDocument()).getBidiRootElement();
            byte[] bArr = new byte[viewCount];
            View[] viewArr = new View[viewCount];
            for (int i4 = 0; i4 < viewCount; i4++) {
                View view2 = view.getView(i4);
                bArr[i4] = (byte) StyleConstants.getBidiLevel(bidiRootElement.getElement(bidiRootElement.getElementIndex(view2.getStartOffset())).getAttributes());
                viewArr[i4] = view2;
            }
            BidiUtils.reorderVisually(bArr, viewArr);
            view.replace(0, viewCount, viewArr);
        }
        return iLayoutRow;
    }

    @Override // javax.swing.text.FlowView.FlowStrategy
    protected void adjustRow(FlowView flowView, int i2, int i3, int i4) {
    }

    @Override // javax.swing.text.FlowView.FlowStrategy
    protected View createView(FlowView flowView, int i2, int i3, int i4) {
        View viewCreateFragment;
        View logicalView = getLogicalView(flowView);
        flowView.getView(i4);
        boolean z2 = this.viewBuffer.size() != 0;
        View view = logicalView.getView(logicalView.getViewIndex(i2, Position.Bias.Forward));
        int limitingOffset = getLimitingOffset(view, i2, i3, z2);
        if (limitingOffset == i2) {
            return null;
        }
        if (i2 == view.getStartOffset() && limitingOffset == view.getEndOffset()) {
            viewCreateFragment = view;
        } else {
            viewCreateFragment = view.createFragment(i2, limitingOffset);
        }
        if ((viewCreateFragment instanceof GlyphView) && this.measurer != null) {
            boolean z3 = false;
            int startOffset = viewCreateFragment.getStartOffset();
            int endOffset = viewCreateFragment.getEndOffset();
            if (endOffset - startOffset == 1 && ((GlyphView) viewCreateFragment).getText(startOffset, endOffset).first() == '\t') {
                z3 = true;
            }
            TextLayout textLayoutNextLayout = z3 ? null : this.measurer.nextLayout(i3, this.text.toIteratorIndex(limitingOffset), z2);
            if (textLayoutNextLayout != null) {
                ((GlyphView) viewCreateFragment).setGlyphPainter(new GlyphPainter2(textLayoutNextLayout));
            }
        }
        return viewCreateFragment;
    }

    int getLimitingOffset(View view, int i2, int i3, boolean z2) {
        int endOffset = view.getEndOffset();
        Document document = view.getDocument();
        if (document instanceof AbstractDocument) {
            Element bidiRootElement = ((AbstractDocument) document).getBidiRootElement();
            if (bidiRootElement.getElementCount() > 1) {
                endOffset = Math.min(bidiRootElement.getElement(bidiRootElement.getElementIndex(i2)).getEndOffset(), endOffset);
            }
        }
        if (view instanceof GlyphView) {
            Segment text = ((GlyphView) view).getText(i2, endOffset);
            if (text.first() == '\t') {
                endOffset = i2 + 1;
            } else {
                char next = text.next();
                while (true) {
                    char c2 = next;
                    if (c2 == 65535) {
                        break;
                    }
                    if (c2 != '\t') {
                        next = text.next();
                    } else {
                        endOffset = (i2 + text.getIndex()) - text.getBeginIndex();
                        break;
                    }
                }
            }
        }
        int iteratorIndex = this.text.toIteratorIndex(endOffset);
        if (this.measurer != null) {
            int iteratorIndex2 = this.text.toIteratorIndex(i2);
            if (this.measurer.getPosition() != iteratorIndex2) {
                this.measurer.setPosition(iteratorIndex2);
            }
            iteratorIndex = this.measurer.nextOffset(i3, iteratorIndex, z2);
        }
        return this.text.toModelPosition(iteratorIndex);
    }

    void sync(FlowView flowView) {
        BreakIterator lineInstance;
        View logicalView = getLogicalView(flowView);
        this.text.setView(logicalView);
        FontRenderContext fontRenderContext = SwingUtilities2.getFontRenderContext(flowView.getContainer());
        Container container = flowView.getContainer();
        if (container != null) {
            lineInstance = BreakIterator.getLineInstance(container.getLocale());
        } else {
            lineInstance = BreakIterator.getLineInstance();
        }
        Object clientProperty = null;
        if (container instanceof JComponent) {
            clientProperty = ((JComponent) container).getClientProperty(TextAttribute.NUMERIC_SHAPING);
        }
        this.text.setShaper(clientProperty);
        this.measurer = new LineBreakMeasurer(this.text, lineInstance, fontRenderContext);
        int viewCount = logicalView.getViewCount();
        for (int i2 = 0; i2 < viewCount; i2++) {
            View view = logicalView.getView(i2);
            if (view instanceof GlyphView) {
                int startOffset = view.getStartOffset();
                int endOffset = view.getEndOffset();
                this.measurer.setPosition(this.text.toIteratorIndex(startOffset));
                ((GlyphView) view).setGlyphPainter(new GlyphPainter2(this.measurer.nextLayout(Float.MAX_VALUE, this.text.toIteratorIndex(endOffset), false)));
            }
        }
        this.measurer.setPosition(this.text.getBeginIndex());
    }

    /* loaded from: rt.jar:javax/swing/text/TextLayoutStrategy$AttributedSegment.class */
    static class AttributedSegment extends Segment implements AttributedCharacterIterator {

        /* renamed from: v, reason: collision with root package name */
        View f12843v;
        static Set<AttributedCharacterIterator.Attribute> keys = new HashSet();
        private Object shaper = null;

        AttributedSegment() {
        }

        View getView() {
            return this.f12843v;
        }

        void setView(View view) {
            this.f12843v = view;
            Document document = view.getDocument();
            int startOffset = view.getStartOffset();
            try {
                document.getText(startOffset, view.getEndOffset() - startOffset, this);
                first();
            } catch (BadLocationException e2) {
                throw new IllegalArgumentException("Invalid view");
            }
        }

        int getFontBoundary(int i2, int i3) {
            View view = this.f12843v.getView(i2);
            Font font = getFont(i2);
            while (true) {
                i2 += i3;
                if (i2 < 0 || i2 >= this.f12843v.getViewCount() || getFont(i2) != font) {
                    break;
                }
                view = this.f12843v.getView(i2);
            }
            return i3 < 0 ? view.getStartOffset() : view.getEndOffset();
        }

        Font getFont(int i2) {
            View view = this.f12843v.getView(i2);
            if (view instanceof GlyphView) {
                return ((GlyphView) view).getFont();
            }
            return null;
        }

        int toModelPosition(int i2) {
            return this.f12843v.getStartOffset() + (i2 - getBeginIndex());
        }

        int toIteratorIndex(int i2) {
            return (i2 - this.f12843v.getStartOffset()) + getBeginIndex();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setShaper(Object obj) {
            this.shaper = obj;
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunStart() {
            return toIteratorIndex(this.f12843v.getView(this.f12843v.getViewIndex(toModelPosition(getIndex()), Position.Bias.Forward)).getStartOffset());
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunStart(AttributedCharacterIterator.Attribute attribute) {
            if (attribute instanceof TextAttribute) {
                int viewIndex = this.f12843v.getViewIndex(toModelPosition(getIndex()), Position.Bias.Forward);
                if (attribute == TextAttribute.FONT) {
                    return toIteratorIndex(getFontBoundary(viewIndex, -1));
                }
            }
            return getBeginIndex();
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunStart(Set<? extends AttributedCharacterIterator.Attribute> set) {
            int beginIndex = getBeginIndex();
            for (Object obj : set.toArray()) {
                beginIndex = Math.max(getRunStart((TextAttribute) obj), beginIndex);
            }
            return Math.min(getIndex(), beginIndex);
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunLimit() {
            return toIteratorIndex(this.f12843v.getView(this.f12843v.getViewIndex(toModelPosition(getIndex()), Position.Bias.Forward)).getEndOffset());
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunLimit(AttributedCharacterIterator.Attribute attribute) {
            if (attribute instanceof TextAttribute) {
                int viewIndex = this.f12843v.getViewIndex(toModelPosition(getIndex()), Position.Bias.Forward);
                if (attribute == TextAttribute.FONT) {
                    return toIteratorIndex(getFontBoundary(viewIndex, 1));
                }
            }
            return getEndIndex();
        }

        @Override // java.text.AttributedCharacterIterator
        public int getRunLimit(Set<? extends AttributedCharacterIterator.Attribute> set) {
            int endIndex = getEndIndex();
            for (Object obj : set.toArray()) {
                endIndex = Math.min(getRunLimit((TextAttribute) obj), endIndex);
            }
            return Math.max(getIndex(), endIndex);
        }

        @Override // java.text.AttributedCharacterIterator
        public Map<AttributedCharacterIterator.Attribute, Object> getAttributes() {
            Object[] array = keys.toArray();
            Hashtable hashtable = new Hashtable();
            for (Object obj : array) {
                TextAttribute textAttribute = (TextAttribute) obj;
                Object attribute = getAttribute(textAttribute);
                if (attribute != null) {
                    hashtable.put(textAttribute, attribute);
                }
            }
            return hashtable;
        }

        @Override // java.text.AttributedCharacterIterator
        public Object getAttribute(AttributedCharacterIterator.Attribute attribute) {
            int viewIndex = this.f12843v.getViewIndex(toModelPosition(getIndex()), Position.Bias.Forward);
            if (attribute == TextAttribute.FONT) {
                return getFont(viewIndex);
            }
            if (attribute == TextAttribute.RUN_DIRECTION) {
                return this.f12843v.getDocument().getProperty(TextAttribute.RUN_DIRECTION);
            }
            if (attribute == TextAttribute.NUMERIC_SHAPING) {
                return this.shaper;
            }
            return null;
        }

        @Override // java.text.AttributedCharacterIterator
        public Set<AttributedCharacterIterator.Attribute> getAllAttributeKeys() {
            return keys;
        }

        static {
            keys.add(TextAttribute.FONT);
            keys.add(TextAttribute.RUN_DIRECTION);
            keys.add(TextAttribute.NUMERIC_SHAPING);
        }
    }
}
