package javax.swing.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument.class */
public class DefaultStyledDocument extends AbstractDocument implements StyledDocument {
    public static final int BUFFER_SIZE_DEFAULT = 4096;
    protected ElementBuffer buffer;
    private transient Vector<Style> listeningStyles;
    private transient ChangeListener styleChangeListener;
    private transient ChangeListener styleContextChangeListener;
    private transient ChangeUpdateRunnable updateRunnable;

    public DefaultStyledDocument(AbstractDocument.Content content, StyleContext styleContext) {
        super(content, styleContext);
        this.listeningStyles = new Vector<>();
        this.buffer = new ElementBuffer(createDefaultRoot());
        setLogicalStyle(0, styleContext.getStyle("default"));
    }

    public DefaultStyledDocument(StyleContext styleContext) {
        this(new GapContent(4096), styleContext);
    }

    public DefaultStyledDocument() {
        this(new GapContent(4096), new StyleContext());
    }

    @Override // javax.swing.text.AbstractDocument, javax.swing.text.Document
    public Element getDefaultRootElement() {
        return this.buffer.getRootElement();
    }

    protected void create(ElementSpec[] elementSpecArr) {
        try {
            try {
                if (getLength() != 0) {
                    remove(0, getLength());
                }
                writeLock();
                AbstractDocument.Content content = getContent();
                StringBuilder sb = new StringBuilder();
                for (ElementSpec elementSpec : elementSpecArr) {
                    if (elementSpec.getLength() > 0) {
                        sb.append(elementSpec.getArray(), elementSpec.getOffset(), elementSpec.getLength());
                    }
                }
                UndoableEdit undoableEditInsertString = content.insertString(0, sb.toString());
                int length = sb.length();
                AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(0, length, DocumentEvent.EventType.INSERT);
                defaultDocumentEvent.addEdit(undoableEditInsertString);
                this.buffer.create(length, elementSpecArr, defaultDocumentEvent);
                super.insertUpdate(defaultDocumentEvent, null);
                defaultDocumentEvent.end();
                fireInsertUpdate(defaultDocumentEvent);
                fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
                writeUnlock();
            } catch (BadLocationException e2) {
                throw new StateInvariantError("problem initializing");
            }
        } catch (Throwable th) {
            writeUnlock();
            throw th;
        }
    }

    protected void insert(int i2, ElementSpec[] elementSpecArr) throws BadLocationException {
        if (elementSpecArr == null || elementSpecArr.length == 0) {
            return;
        }
        try {
            writeLock();
            AbstractDocument.Content content = getContent();
            StringBuilder sb = new StringBuilder();
            for (ElementSpec elementSpec : elementSpecArr) {
                if (elementSpec.getLength() > 0) {
                    sb.append(elementSpec.getArray(), elementSpec.getOffset(), elementSpec.getLength());
                }
            }
            if (sb.length() == 0) {
                return;
            }
            UndoableEdit undoableEditInsertString = content.insertString(i2, sb.toString());
            int length = sb.length();
            AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(i2, length, DocumentEvent.EventType.INSERT);
            defaultDocumentEvent.addEdit(undoableEditInsertString);
            this.buffer.insert(i2, length, elementSpecArr, defaultDocumentEvent);
            super.insertUpdate(defaultDocumentEvent, null);
            defaultDocumentEvent.end();
            fireInsertUpdate(defaultDocumentEvent);
            fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
            writeUnlock();
        } finally {
            writeUnlock();
        }
    }

    public void removeElement(Element element) {
        try {
            writeLock();
            removeElementImpl(element);
        } finally {
            writeUnlock();
        }
    }

    private void removeElementImpl(Element element) {
        Element element2;
        if (element.getDocument() != this) {
            throw new IllegalArgumentException("element doesn't belong to document");
        }
        AbstractDocument.BranchElement branchElement = (AbstractDocument.BranchElement) element.getParentElement();
        if (branchElement == null) {
            throw new IllegalArgumentException("can't remove the root element");
        }
        int startOffset = element.getStartOffset();
        int i2 = startOffset;
        int endOffset = element.getEndOffset();
        int i3 = endOffset;
        int length = getLength() + 1;
        AbstractDocument.Content content = getContent();
        boolean z2 = false;
        boolean zIsComposedTextElement = Utilities.isComposedTextElement(element);
        if (endOffset >= length) {
            if (startOffset <= 0) {
                throw new IllegalArgumentException("can't remove the whole content");
            }
            i3 = length - 1;
            try {
                if (content.getString(startOffset - 1, 1).charAt(0) == '\n') {
                    i2--;
                }
                z2 = true;
            } catch (BadLocationException e2) {
                throw new IllegalStateException(e2);
            }
        }
        int i4 = i3 - i2;
        AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(i2, i4, DocumentEvent.EventType.REMOVE);
        UndoableEdit undoableEditRemove = null;
        while (branchElement.getElementCount() == 1) {
            element = branchElement;
            branchElement = (AbstractDocument.BranchElement) branchElement.getParentElement();
            if (branchElement == null) {
                throw new IllegalStateException("invalid element structure");
            }
        }
        Element[] elementArr = new Element[0];
        int elementIndex = branchElement.getElementIndex(startOffset);
        branchElement.replace(elementIndex, 1, elementArr);
        defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(branchElement, elementIndex, new Element[]{element}, elementArr));
        if (i4 > 0) {
            try {
                undoableEditRemove = content.remove(i2, i4);
                if (undoableEditRemove != null) {
                    defaultDocumentEvent.addEdit(undoableEditRemove);
                }
                length -= i4;
            } catch (BadLocationException e3) {
                throw new IllegalStateException(e3);
            }
        }
        if (z2) {
            Element element3 = branchElement.getElement(branchElement.getElementCount() - 1);
            while (true) {
                element2 = element3;
                if (element2 == null || element2.isLeaf()) {
                    break;
                } else {
                    element3 = element2.getElement(element2.getElementCount() - 1);
                }
            }
            if (element2 == null) {
                throw new IllegalStateException("invalid element structure");
            }
            int startOffset2 = element2.getStartOffset();
            AbstractDocument.BranchElement branchElement2 = (AbstractDocument.BranchElement) element2.getParentElement();
            int elementIndex2 = branchElement2.getElementIndex(startOffset2);
            Element[] elementArr2 = {createLeafElement(branchElement2, element2.getAttributes(), startOffset2, length)};
            branchElement2.replace(elementIndex2, 1, elementArr2);
            defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(branchElement2, elementIndex2, new Element[]{element2}, elementArr2));
        }
        postRemoveUpdate(defaultDocumentEvent);
        defaultDocumentEvent.end();
        fireRemoveUpdate(defaultDocumentEvent);
        if (!zIsComposedTextElement || undoableEditRemove == null) {
            fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
        }
    }

    @Override // javax.swing.text.StyledDocument
    public Style addStyle(String str, Style style) {
        return ((StyleContext) getAttributeContext()).addStyle(str, style);
    }

    @Override // javax.swing.text.StyledDocument
    public void removeStyle(String str) {
        ((StyleContext) getAttributeContext()).removeStyle(str);
    }

    @Override // javax.swing.text.StyledDocument
    public Style getStyle(String str) {
        return ((StyleContext) getAttributeContext()).getStyle(str);
    }

    public Enumeration<?> getStyleNames() {
        return ((StyleContext) getAttributeContext()).getStyleNames();
    }

    @Override // javax.swing.text.StyledDocument
    public void setLogicalStyle(int i2, Style style) {
        Element paragraphElement = getParagraphElement(i2);
        if (paragraphElement != null && (paragraphElement instanceof AbstractDocument.AbstractElement)) {
            try {
                writeLock();
                StyleChangeUndoableEdit styleChangeUndoableEdit = new StyleChangeUndoableEdit((AbstractDocument.AbstractElement) paragraphElement, style);
                ((AbstractDocument.AbstractElement) paragraphElement).setResolveParent(style);
                int startOffset = paragraphElement.getStartOffset();
                AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(startOffset, paragraphElement.getEndOffset() - startOffset, DocumentEvent.EventType.CHANGE);
                defaultDocumentEvent.addEdit(styleChangeUndoableEdit);
                defaultDocumentEvent.end();
                fireChangedUpdate(defaultDocumentEvent);
                fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
                writeUnlock();
            } catch (Throwable th) {
                writeUnlock();
                throw th;
            }
        }
    }

    @Override // javax.swing.text.StyledDocument
    public Style getLogicalStyle(int i2) {
        Style style = null;
        Element paragraphElement = getParagraphElement(i2);
        if (paragraphElement != null) {
            AttributeSet resolveParent = paragraphElement.getAttributes().getResolveParent();
            if (resolveParent instanceof Style) {
                style = (Style) resolveParent;
            }
        }
        return style;
    }

    @Override // javax.swing.text.StyledDocument
    public void setCharacterAttributes(int i2, int i3, AttributeSet attributeSet, boolean z2) {
        Element characterElement;
        int endOffset;
        if (i3 == 0) {
            return;
        }
        try {
            writeLock();
            AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(i2, i3, DocumentEvent.EventType.CHANGE);
            this.buffer.change(i2, i3, defaultDocumentEvent);
            AttributeSet attributeSetCopyAttributes = attributeSet.copyAttributes();
            int i4 = i2;
            while (i4 < i2 + i3 && i4 != (endOffset = (characterElement = getCharacterElement(i4)).getEndOffset())) {
                MutableAttributeSet mutableAttributeSet = (MutableAttributeSet) characterElement.getAttributes();
                defaultDocumentEvent.addEdit(new AttributeUndoableEdit(characterElement, attributeSetCopyAttributes, z2));
                if (z2) {
                    mutableAttributeSet.removeAttributes(mutableAttributeSet);
                }
                mutableAttributeSet.addAttributes(attributeSet);
                i4 = endOffset;
            }
            defaultDocumentEvent.end();
            fireChangedUpdate(defaultDocumentEvent);
            fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
            writeUnlock();
        } catch (Throwable th) {
            writeUnlock();
            throw th;
        }
    }

    @Override // javax.swing.text.StyledDocument
    public void setParagraphAttributes(int i2, int i3, AttributeSet attributeSet, boolean z2) {
        try {
            writeLock();
            AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(i2, i3, DocumentEvent.EventType.CHANGE);
            AttributeSet attributeSetCopyAttributes = attributeSet.copyAttributes();
            Element defaultRootElement = getDefaultRootElement();
            int elementIndex = defaultRootElement.getElementIndex(i2);
            int elementIndex2 = defaultRootElement.getElementIndex(i2 + (i3 > 0 ? i3 - 1 : 0));
            boolean zEquals = Boolean.TRUE.equals(getProperty("i18n"));
            boolean z3 = false;
            for (int i4 = elementIndex; i4 <= elementIndex2; i4++) {
                Element element = defaultRootElement.getElement(i4);
                MutableAttributeSet mutableAttributeSet = (MutableAttributeSet) element.getAttributes();
                defaultDocumentEvent.addEdit(new AttributeUndoableEdit(element, attributeSetCopyAttributes, z2));
                if (z2) {
                    mutableAttributeSet.removeAttributes(mutableAttributeSet);
                }
                mutableAttributeSet.addAttributes(attributeSet);
                if (zEquals && !z3) {
                    z3 = mutableAttributeSet.getAttribute(TextAttribute.RUN_DIRECTION) != null;
                }
            }
            if (z3) {
                updateBidi(defaultDocumentEvent);
            }
            defaultDocumentEvent.end();
            fireChangedUpdate(defaultDocumentEvent);
            fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
            writeUnlock();
        } catch (Throwable th) {
            writeUnlock();
            throw th;
        }
    }

    @Override // javax.swing.text.AbstractDocument, javax.swing.text.StyledDocument
    public Element getParagraphElement(int i2) {
        Element element;
        Element defaultRootElement = getDefaultRootElement();
        while (true) {
            element = defaultRootElement;
            if (element.isLeaf()) {
                break;
            }
            defaultRootElement = element.getElement(element.getElementIndex(i2));
        }
        if (element != null) {
            return element.getParentElement();
        }
        return element;
    }

    @Override // javax.swing.text.StyledDocument
    public Element getCharacterElement(int i2) {
        Element defaultRootElement = getDefaultRootElement();
        while (true) {
            Element element = defaultRootElement;
            if (!element.isLeaf()) {
                defaultRootElement = element.getElement(element.getElementIndex(i2));
            } else {
                return element;
            }
        }
    }

    @Override // javax.swing.text.AbstractDocument
    protected void insertUpdate(AbstractDocument.DefaultDocumentEvent defaultDocumentEvent, AttributeSet attributeSet) {
        int offset = defaultDocumentEvent.getOffset();
        int length = defaultDocumentEvent.getLength();
        if (attributeSet == null) {
            attributeSet = SimpleAttributeSet.EMPTY;
        }
        Element paragraphElement = getParagraphElement(offset + length);
        AttributeSet attributes = paragraphElement.getAttributes();
        Element paragraphElement2 = getParagraphElement(offset);
        Element element = paragraphElement2.getElement(paragraphElement2.getElementIndex(offset));
        int i2 = offset + length;
        boolean z2 = element.getEndOffset() == i2;
        AttributeSet attributes2 = element.getAttributes();
        try {
            Segment segment = new Segment();
            Vector<ElementSpec> vector = new Vector<>();
            ElementSpec elementSpec = null;
            boolean z3 = false;
            short sCreateSpecsForInsertAfterNewline = 6;
            if (offset > 0) {
                getText(offset - 1, 1, segment);
                if (segment.array[segment.offset] == '\n') {
                    z3 = true;
                    sCreateSpecsForInsertAfterNewline = createSpecsForInsertAfterNewline(paragraphElement, paragraphElement2, attributes, vector, offset, i2);
                    int size = vector.size() - 1;
                    while (true) {
                        if (size < 0) {
                            break;
                        }
                        ElementSpec elementSpecElementAt = vector.elementAt(size);
                        if (elementSpecElementAt.getType() == 1) {
                            elementSpec = elementSpecElementAt;
                            break;
                        }
                        size--;
                    }
                }
            }
            if (!z3) {
                attributes = paragraphElement2.getAttributes();
            }
            getText(offset, length, segment);
            char[] cArr = segment.array;
            int i3 = segment.offset + segment.count;
            int i4 = segment.offset;
            for (int i5 = segment.offset; i5 < i3; i5++) {
                if (cArr[i5] == '\n') {
                    int i6 = i5 + 1;
                    vector.addElement(new ElementSpec(attributeSet, (short) 3, i6 - i4));
                    vector.addElement(new ElementSpec(null, (short) 2));
                    elementSpec = new ElementSpec(attributes, (short) 1);
                    vector.addElement(elementSpec);
                    i4 = i6;
                }
            }
            if (i4 < i3) {
                vector.addElement(new ElementSpec(attributeSet, (short) 3, i3 - i4));
            }
            ElementSpec elementSpecFirstElement = vector.firstElement();
            int length2 = getLength();
            if (elementSpecFirstElement.getType() == 3 && attributes2.isEqual(attributeSet)) {
                elementSpecFirstElement.setDirection((short) 4);
            }
            if (elementSpec != null) {
                if (z3) {
                    elementSpec.setDirection(sCreateSpecsForInsertAfterNewline);
                } else if (paragraphElement2.getEndOffset() != i2) {
                    elementSpec.setDirection((short) 7);
                } else {
                    Element parentElement = paragraphElement2.getParentElement();
                    int elementIndex = parentElement.getElementIndex(offset);
                    if (elementIndex + 1 < parentElement.getElementCount() && !parentElement.getElement(elementIndex + 1).isLeaf()) {
                        elementSpec.setDirection((short) 5);
                    }
                }
            }
            if (z2 && i2 < length2) {
                ElementSpec elementSpecLastElement = vector.lastElement();
                if (elementSpecLastElement.getType() == 3 && elementSpecLastElement.getDirection() != 4 && ((elementSpec == null && (paragraphElement == paragraphElement2 || z3)) || (elementSpec != null && elementSpec.getDirection() != 6))) {
                    Element element2 = paragraphElement.getElement(paragraphElement.getElementIndex(i2));
                    if (element2.isLeaf() && attributeSet.isEqual(element2.getAttributes())) {
                        elementSpecLastElement.setDirection((short) 5);
                    }
                }
            } else if (!z2 && elementSpec != null && elementSpec.getDirection() == 7) {
                ElementSpec elementSpecLastElement2 = vector.lastElement();
                if (elementSpecLastElement2.getType() == 3 && elementSpecLastElement2.getDirection() != 4 && attributeSet.isEqual(attributes2)) {
                    elementSpecLastElement2.setDirection((short) 5);
                }
            }
            if (Utilities.isComposedTextAttributeDefined(attributeSet)) {
                MutableAttributeSet mutableAttributeSet = (MutableAttributeSet) attributeSet;
                mutableAttributeSet.addAttributes(attributes2);
                mutableAttributeSet.addAttribute(AbstractDocument.ElementNameAttribute, AbstractDocument.ContentElementName);
                mutableAttributeSet.addAttribute(StyleConstants.NameAttribute, AbstractDocument.ContentElementName);
                if (mutableAttributeSet.isDefined(SwingUtilities2.IMPLIED_CR)) {
                    mutableAttributeSet.removeAttribute(SwingUtilities2.IMPLIED_CR);
                }
            }
            ElementSpec[] elementSpecArr = new ElementSpec[vector.size()];
            vector.copyInto(elementSpecArr);
            this.buffer.insert(offset, length, elementSpecArr, defaultDocumentEvent);
        } catch (BadLocationException e2) {
        }
        super.insertUpdate(defaultDocumentEvent, attributeSet);
    }

    short createSpecsForInsertAfterNewline(Element element, Element element2, AttributeSet attributeSet, Vector<ElementSpec> vector, int i2, int i3) {
        if (element.getParentElement() == element2.getParentElement()) {
            vector.addElement(new ElementSpec(attributeSet, (short) 2));
            vector.addElement(new ElementSpec(attributeSet, (short) 1));
            if (element2.getEndOffset() != i3) {
                return (short) 7;
            }
            Element parentElement = element2.getParentElement();
            if (parentElement.getElementIndex(i2) + 1 < parentElement.getElementCount()) {
                return (short) 5;
            }
            return (short) 6;
        }
        Vector vector2 = new Vector();
        Vector vector3 = new Vector();
        Element parentElement2 = element2;
        while (true) {
            Element element3 = parentElement2;
            if (element3 == null) {
                break;
            }
            vector2.addElement(element3);
            parentElement2 = element3.getParentElement();
        }
        Element parentElement3 = element;
        int i4 = -1;
        while (parentElement3 != null) {
            int iIndexOf = vector2.indexOf(parentElement3);
            i4 = iIndexOf;
            if (iIndexOf != -1) {
                break;
            }
            vector3.addElement(parentElement3);
            parentElement3 = parentElement3.getParentElement();
        }
        if (parentElement3 != null) {
            for (int i5 = 0; i5 < i4; i5++) {
                vector.addElement(new ElementSpec(null, (short) 2));
            }
            for (int size = vector3.size() - 1; size >= 0; size--) {
                ElementSpec elementSpec = new ElementSpec(((Element) vector3.elementAt(size)).getAttributes(), (short) 1);
                if (size > 0) {
                    elementSpec.setDirection((short) 5);
                }
                vector.addElement(elementSpec);
            }
            if (vector3.size() > 0) {
                return (short) 5;
            }
            return (short) 7;
        }
        return (short) 6;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // javax.swing.text.AbstractDocument
    public void removeUpdate(AbstractDocument.DefaultDocumentEvent defaultDocumentEvent) {
        super.removeUpdate(defaultDocumentEvent);
        this.buffer.remove(defaultDocumentEvent.getOffset(), defaultDocumentEvent.getLength(), defaultDocumentEvent);
    }

    protected AbstractDocument.AbstractElement createDefaultRoot() {
        writeLock();
        SectionElement sectionElement = new SectionElement();
        AbstractDocument.BranchElement branchElement = new AbstractDocument.BranchElement(sectionElement, null);
        Element[] elementArr = {new AbstractDocument.LeafElement(branchElement, null, 0, 1)};
        branchElement.replace(0, 0, elementArr);
        elementArr[0] = branchElement;
        sectionElement.replace(0, 0, elementArr);
        writeUnlock();
        return sectionElement;
    }

    @Override // javax.swing.text.StyledDocument
    public Color getForeground(AttributeSet attributeSet) {
        return ((StyleContext) getAttributeContext()).getForeground(attributeSet);
    }

    @Override // javax.swing.text.StyledDocument
    public Color getBackground(AttributeSet attributeSet) {
        return ((StyleContext) getAttributeContext()).getBackground(attributeSet);
    }

    @Override // javax.swing.text.StyledDocument
    public Font getFont(AttributeSet attributeSet) {
        return ((StyleContext) getAttributeContext()).getFont(attributeSet);
    }

    protected void styleChanged(Style style) {
        if (getLength() != 0) {
            if (this.updateRunnable == null) {
                this.updateRunnable = new ChangeUpdateRunnable();
            }
            synchronized (this.updateRunnable) {
                if (!this.updateRunnable.isPending) {
                    SwingUtilities.invokeLater(this.updateRunnable);
                    this.updateRunnable.isPending = true;
                }
            }
        }
    }

    @Override // javax.swing.text.AbstractDocument, javax.swing.text.Document
    public void addDocumentListener(DocumentListener documentListener) {
        synchronized (this.listeningStyles) {
            int listenerCount = this.listenerList.getListenerCount(DocumentListener.class);
            super.addDocumentListener(documentListener);
            if (listenerCount == 0) {
                if (this.styleContextChangeListener == null) {
                    this.styleContextChangeListener = createStyleContextChangeListener();
                }
                if (this.styleContextChangeListener != null) {
                    StyleContext styleContext = (StyleContext) getAttributeContext();
                    Iterator<ChangeListener> it = AbstractChangeHandler.getStaleListeners(this.styleContextChangeListener).iterator();
                    while (it.hasNext()) {
                        styleContext.removeChangeListener(it.next());
                    }
                    styleContext.addChangeListener(this.styleContextChangeListener);
                }
                updateStylesListeningTo();
            }
        }
    }

    @Override // javax.swing.text.AbstractDocument, javax.swing.text.Document
    public void removeDocumentListener(DocumentListener documentListener) {
        synchronized (this.listeningStyles) {
            super.removeDocumentListener(documentListener);
            if (this.listenerList.getListenerCount(DocumentListener.class) == 0) {
                for (int size = this.listeningStyles.size() - 1; size >= 0; size--) {
                    this.listeningStyles.elementAt(size).removeChangeListener(this.styleChangeListener);
                }
                this.listeningStyles.removeAllElements();
                if (this.styleContextChangeListener != null) {
                    ((StyleContext) getAttributeContext()).removeChangeListener(this.styleContextChangeListener);
                }
            }
        }
    }

    ChangeListener createStyleChangeListener() {
        return new StyleChangeHandler(this);
    }

    ChangeListener createStyleContextChangeListener() {
        return new StyleContextChangeHandler(this);
    }

    void updateStylesListeningTo() {
        synchronized (this.listeningStyles) {
            StyleContext styleContext = (StyleContext) getAttributeContext();
            if (this.styleChangeListener == null) {
                this.styleChangeListener = createStyleChangeListener();
            }
            if (this.styleChangeListener != null && styleContext != null) {
                Enumeration<?> styleNames = styleContext.getStyleNames();
                Vector vector = (Vector) this.listeningStyles.clone();
                this.listeningStyles.removeAllElements();
                List<ChangeListener> staleListeners = AbstractChangeHandler.getStaleListeners(this.styleChangeListener);
                while (styleNames.hasMoreElements()) {
                    Style style = styleContext.getStyle((String) styleNames.nextElement2());
                    int iIndexOf = vector.indexOf(style);
                    this.listeningStyles.addElement(style);
                    if (iIndexOf == -1) {
                        Iterator<ChangeListener> it = staleListeners.iterator();
                        while (it.hasNext()) {
                            style.removeChangeListener(it.next());
                        }
                        style.addChangeListener(this.styleChangeListener);
                    } else {
                        vector.removeElementAt(iIndexOf);
                    }
                }
                for (int size = vector.size() - 1; size >= 0; size--) {
                    ((Style) vector.elementAt(size)).removeChangeListener(this.styleChangeListener);
                }
                if (this.listeningStyles.size() == 0) {
                    this.styleChangeListener = null;
                }
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.listeningStyles = new Vector<>();
        objectInputStream.defaultReadObject();
        if (this.styleContextChangeListener == null && this.listenerList.getListenerCount(DocumentListener.class) > 0) {
            this.styleContextChangeListener = createStyleContextChangeListener();
            if (this.styleContextChangeListener != null) {
                ((StyleContext) getAttributeContext()).addChangeListener(this.styleContextChangeListener);
            }
            updateStylesListeningTo();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$SectionElement.class */
    protected class SectionElement extends AbstractDocument.BranchElement {
        public SectionElement() {
            super(null, null);
        }

        @Override // javax.swing.text.AbstractDocument.BranchElement, javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public String getName() {
            return AbstractDocument.SectionElementName;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$ElementSpec.class */
    public static class ElementSpec {
        public static final short StartTagType = 1;
        public static final short EndTagType = 2;
        public static final short ContentType = 3;
        public static final short JoinPreviousDirection = 4;
        public static final short JoinNextDirection = 5;
        public static final short OriginateDirection = 6;
        public static final short JoinFractureDirection = 7;
        private AttributeSet attr;
        private int len;
        private short type;
        private short direction;
        private int offs;
        private char[] data;

        public ElementSpec(AttributeSet attributeSet, short s2) {
            this(attributeSet, s2, null, 0, 0);
        }

        public ElementSpec(AttributeSet attributeSet, short s2, int i2) {
            this(attributeSet, s2, null, 0, i2);
        }

        public ElementSpec(AttributeSet attributeSet, short s2, char[] cArr, int i2, int i3) {
            this.attr = attributeSet;
            this.type = s2;
            this.data = cArr;
            this.offs = i2;
            this.len = i3;
            this.direction = (short) 6;
        }

        public void setType(short s2) {
            this.type = s2;
        }

        public short getType() {
            return this.type;
        }

        public void setDirection(short s2) {
            this.direction = s2;
        }

        public short getDirection() {
            return this.direction;
        }

        public AttributeSet getAttributes() {
            return this.attr;
        }

        public char[] getArray() {
            return this.data;
        }

        public int getOffset() {
            return this.offs;
        }

        public int getLength() {
            return this.len;
        }

        public String toString() {
            String str = "??";
            String str2 = "??";
            switch (this.type) {
                case 1:
                    str = "StartTag";
                    break;
                case 2:
                    str = "EndTag";
                    break;
                case 3:
                    str = "Content";
                    break;
            }
            switch (this.direction) {
                case 4:
                    str2 = "JoinPrevious";
                    break;
                case 5:
                    str2 = "JoinNext";
                    break;
                case 6:
                    str2 = "Originate";
                    break;
                case 7:
                    str2 = "Fracture";
                    break;
            }
            return str + CallSiteDescriptor.TOKEN_DELIMITER + str2 + CallSiteDescriptor.TOKEN_DELIMITER + getLength();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$ElementBuffer.class */
    public class ElementBuffer implements Serializable {
        Element root;
        transient int pos;
        transient int offset;
        transient int length;
        transient int endOffset;
        transient Vector<ElemChanges> changes = new Vector<>();
        transient Stack<ElemChanges> path = new Stack<>();
        transient boolean insertOp;
        transient boolean recreateLeafs;
        transient ElemChanges[] insertPath;
        transient boolean createdFracture;
        transient Element fracturedParent;
        transient Element fracturedChild;
        transient boolean offsetLastIndex;
        transient boolean offsetLastIndexOnReplace;

        public ElementBuffer(Element element) {
            this.root = element;
        }

        public Element getRootElement() {
            return this.root;
        }

        public void insert(int i2, int i3, ElementSpec[] elementSpecArr, AbstractDocument.DefaultDocumentEvent defaultDocumentEvent) {
            if (i3 == 0) {
                return;
            }
            this.insertOp = true;
            beginEdits(i2, i3);
            insertUpdate(elementSpecArr);
            endEdits(defaultDocumentEvent);
            this.insertOp = false;
        }

        void create(int i2, ElementSpec[] elementSpecArr, AbstractDocument.DefaultDocumentEvent defaultDocumentEvent) {
            this.insertOp = true;
            beginEdits(this.offset, i2);
            Element element = this.root;
            int elementIndex = element.getElementIndex(0);
            while (true) {
                int i3 = elementIndex;
                if (element.isLeaf()) {
                    break;
                }
                Element element2 = element.getElement(i3);
                push(element, i3);
                element = element2;
                elementIndex = element.getElementIndex(0);
            }
            ElemChanges elemChangesPeek = this.path.peek();
            Element element3 = elemChangesPeek.parent.getElement(elemChangesPeek.index);
            elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element3.getAttributes(), DefaultStyledDocument.this.getLength(), element3.getEndOffset()));
            elemChangesPeek.removed.addElement(element3);
            while (this.path.size() > 1) {
                pop();
            }
            int length = elementSpecArr.length;
            AttributeSet attributes = null;
            if (length > 0 && elementSpecArr[0].getType() == 1) {
                attributes = elementSpecArr[0].getAttributes();
            }
            if (attributes == null) {
                attributes = SimpleAttributeSet.EMPTY;
            }
            MutableAttributeSet mutableAttributeSet = (MutableAttributeSet) this.root.getAttributes();
            defaultDocumentEvent.addEdit(new AttributeUndoableEdit(this.root, attributes, true));
            mutableAttributeSet.removeAttributes(mutableAttributeSet);
            mutableAttributeSet.addAttributes(attributes);
            for (int i4 = 1; i4 < length; i4++) {
                insertElement(elementSpecArr[i4]);
            }
            while (this.path.size() != 0) {
                pop();
            }
            endEdits(defaultDocumentEvent);
            this.insertOp = false;
        }

        public void remove(int i2, int i3, AbstractDocument.DefaultDocumentEvent defaultDocumentEvent) {
            beginEdits(i2, i3);
            removeUpdate();
            endEdits(defaultDocumentEvent);
        }

        public void change(int i2, int i3, AbstractDocument.DefaultDocumentEvent defaultDocumentEvent) {
            beginEdits(i2, i3);
            changeUpdate();
            endEdits(defaultDocumentEvent);
        }

        protected void insertUpdate(ElementSpec[] elementSpecArr) {
            int i2;
            Element element = this.root;
            int elementIndex = element.getElementIndex(this.offset);
            while (true) {
                int i3 = elementIndex;
                if (element.isLeaf()) {
                    break;
                }
                Element element2 = element.getElement(i3);
                push(element, element2.isLeaf() ? i3 : i3 + 1);
                element = element2;
                elementIndex = element.getElementIndex(this.offset);
            }
            this.insertPath = new ElemChanges[this.path.size()];
            this.path.copyInto(this.insertPath);
            this.createdFracture = false;
            this.recreateLeafs = false;
            if (elementSpecArr[0].getType() == 3) {
                insertFirstContent(elementSpecArr);
                this.pos += elementSpecArr[0].getLength();
                i2 = 1;
            } else {
                fractureDeepestLeaf(elementSpecArr);
                i2 = 0;
            }
            int length = elementSpecArr.length;
            while (i2 < length) {
                insertElement(elementSpecArr[i2]);
                i2++;
            }
            if (!this.createdFracture) {
                fracture(-1);
            }
            while (this.path.size() != 0) {
                pop();
            }
            if (this.offsetLastIndex && this.offsetLastIndexOnReplace) {
                this.insertPath[this.insertPath.length - 1].index++;
            }
            for (int length2 = this.insertPath.length - 1; length2 >= 0; length2--) {
                ElemChanges elemChanges = this.insertPath[length2];
                if (elemChanges.parent == this.fracturedParent) {
                    elemChanges.added.addElement(this.fracturedChild);
                }
                if ((elemChanges.added.size() > 0 || elemChanges.removed.size() > 0) && !this.changes.contains(elemChanges)) {
                    this.changes.addElement(elemChanges);
                }
            }
            if (this.offset == 0 && this.fracturedParent != null && elementSpecArr[0].getType() == 2) {
                int i4 = 0;
                while (i4 < elementSpecArr.length && elementSpecArr[i4].getType() == 2) {
                    i4++;
                }
                ElemChanges elemChanges2 = this.insertPath[(this.insertPath.length - i4) - 1];
                Vector<Element> vector = elemChanges2.removed;
                Element element3 = elemChanges2.parent;
                int i5 = elemChanges2.index - 1;
                elemChanges2.index = i5;
                vector.insertElementAt(element3.getElement(i5), 0);
            }
        }

        protected void removeUpdate() {
            removeElements(this.root, this.offset, this.offset + this.length);
        }

        protected void changeUpdate() {
            if (!split(this.offset, this.length)) {
                while (this.path.size() != 0) {
                    pop();
                }
                split(this.offset + this.length, 0);
            }
            while (this.path.size() != 0) {
                pop();
            }
        }

        boolean split(int i2, int i3) {
            boolean z2 = false;
            Element element = this.root;
            int elementIndex = element.getElementIndex(i2);
            while (true) {
                int i4 = elementIndex;
                if (element.isLeaf()) {
                    break;
                }
                push(element, i4);
                element = element.getElement(i4);
                elementIndex = element.getElementIndex(i2);
            }
            ElemChanges elemChangesPeek = this.path.peek();
            Element element2 = elemChangesPeek.parent.getElement(elemChangesPeek.index);
            if (element2.getStartOffset() < i2 && i2 < element2.getEndOffset()) {
                int i5 = elemChangesPeek.index;
                int elementIndex2 = i5;
                if (i2 + i3 < elemChangesPeek.parent.getEndOffset() && i3 != 0) {
                    elementIndex2 = elemChangesPeek.parent.getElementIndex(i2 + i3);
                    if (elementIndex2 == i5) {
                        elemChangesPeek.removed.addElement(element2);
                        elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element2.getAttributes(), element2.getStartOffset(), i2));
                        elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element2.getAttributes(), i2, i2 + i3));
                        elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element2.getAttributes(), i2 + i3, element2.getEndOffset()));
                        return true;
                    }
                    if (i2 + i3 == elemChangesPeek.parent.getElement(elementIndex2).getStartOffset()) {
                        elementIndex2 = i5;
                    }
                    z2 = true;
                }
                this.pos = i2;
                Element element3 = elemChangesPeek.parent.getElement(i5);
                elemChangesPeek.removed.addElement(element3);
                elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element3.getAttributes(), element3.getStartOffset(), this.pos));
                elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element3.getAttributes(), this.pos, element3.getEndOffset()));
                for (int i6 = i5 + 1; i6 < elementIndex2; i6++) {
                    Element element4 = elemChangesPeek.parent.getElement(i6);
                    elemChangesPeek.removed.addElement(element4);
                    elemChangesPeek.added.addElement(element4);
                }
                if (elementIndex2 != i5) {
                    Element element5 = elemChangesPeek.parent.getElement(elementIndex2);
                    this.pos = i2 + i3;
                    elemChangesPeek.removed.addElement(element5);
                    elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element5.getAttributes(), element5.getStartOffset(), this.pos));
                    elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element5.getAttributes(), this.pos, element5.getEndOffset()));
                }
            }
            return z2;
        }

        void endEdits(AbstractDocument.DefaultDocumentEvent defaultDocumentEvent) {
            int size = this.changes.size();
            for (int i2 = 0; i2 < size; i2++) {
                ElemChanges elemChangesElementAt = this.changes.elementAt(i2);
                Element[] elementArr = new Element[elemChangesElementAt.removed.size()];
                elemChangesElementAt.removed.copyInto(elementArr);
                Element[] elementArr2 = new Element[elemChangesElementAt.added.size()];
                elemChangesElementAt.added.copyInto(elementArr2);
                int i3 = elemChangesElementAt.index;
                ((AbstractDocument.BranchElement) elemChangesElementAt.parent).replace(i3, elementArr.length, elementArr2);
                defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(elemChangesElementAt.parent, i3, elementArr, elementArr2));
            }
            this.changes.removeAllElements();
            this.path.removeAllElements();
        }

        void beginEdits(int i2, int i3) {
            this.offset = i2;
            this.length = i3;
            this.endOffset = i2 + i3;
            this.pos = i2;
            if (this.changes == null) {
                this.changes = new Vector<>();
            } else {
                this.changes.removeAllElements();
            }
            if (this.path == null) {
                this.path = new Stack<>();
            } else {
                this.path.removeAllElements();
            }
            this.fracturedParent = null;
            this.fracturedChild = null;
            this.offsetLastIndexOnReplace = false;
            this.offsetLastIndex = false;
        }

        void push(Element element, int i2, boolean z2) {
            this.path.push(new ElemChanges(element, i2, z2));
        }

        void push(Element element, int i2) {
            push(element, i2, false);
        }

        void pop() {
            ElemChanges elemChangesPeek = this.path.peek();
            this.path.pop();
            if (elemChangesPeek.added.size() > 0 || elemChangesPeek.removed.size() > 0) {
                this.changes.addElement(elemChangesPeek);
            } else if (!this.path.isEmpty()) {
                Element element = elemChangesPeek.parent;
                if (element.getElementCount() == 0) {
                    this.path.peek().added.removeElement(element);
                }
            }
        }

        void advance(int i2) {
            this.pos += i2;
        }

        void insertElement(ElementSpec elementSpec) {
            ElemChanges elemChangesPeek = this.path.peek();
            switch (elementSpec.getType()) {
                case 1:
                    switch (elementSpec.getDirection()) {
                        case 5:
                            Element element = elemChangesPeek.parent.getElement(elemChangesPeek.index);
                            if (element.isLeaf()) {
                                if (elemChangesPeek.index + 1 < elemChangesPeek.parent.getElementCount()) {
                                    element = elemChangesPeek.parent.getElement(elemChangesPeek.index + 1);
                                } else {
                                    throw new StateInvariantError("Join next to leaf");
                                }
                            }
                            push(element, 0, true);
                            return;
                        case 7:
                            if (!this.createdFracture) {
                                fracture(this.path.size() - 1);
                            }
                            if (!elemChangesPeek.isFracture) {
                                push(this.fracturedChild, 0, true);
                                return;
                            } else {
                                push(elemChangesPeek.parent.getElement(0), 0, true);
                                return;
                            }
                        default:
                            Element elementCreateBranchElement = DefaultStyledDocument.this.createBranchElement(elemChangesPeek.parent, elementSpec.getAttributes());
                            elemChangesPeek.added.addElement(elementCreateBranchElement);
                            push(elementCreateBranchElement, 0);
                            return;
                    }
                case 2:
                    pop();
                    return;
                case 3:
                    int length = elementSpec.getLength();
                    if (elementSpec.getDirection() != 5) {
                        elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, elementSpec.getAttributes(), this.pos, this.pos + length));
                    } else if (!elemChangesPeek.isFracture) {
                        Element element2 = null;
                        if (this.insertPath != null) {
                            int length2 = this.insertPath.length - 1;
                            while (true) {
                                if (length2 >= 0) {
                                    if (this.insertPath[length2] != elemChangesPeek) {
                                        length2--;
                                    } else if (length2 != this.insertPath.length - 1) {
                                        element2 = elemChangesPeek.parent.getElement(elemChangesPeek.index);
                                    }
                                }
                            }
                        }
                        if (element2 == null) {
                            element2 = elemChangesPeek.parent.getElement(elemChangesPeek.index + 1);
                        }
                        elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element2.getAttributes(), this.pos, element2.getEndOffset()));
                        elemChangesPeek.removed.addElement(element2);
                    } else {
                        Element element3 = elemChangesPeek.parent.getElement(0);
                        elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element3.getAttributes(), this.pos, element3.getEndOffset()));
                        elemChangesPeek.removed.addElement(element3);
                    }
                    this.pos += length;
                    return;
                default:
                    return;
            }
        }

        boolean removeElements(Element element, int i2, int i3) {
            if (!element.isLeaf()) {
                int elementIndex = element.getElementIndex(i2);
                int elementIndex2 = element.getElementIndex(i3);
                push(element, elementIndex);
                ElemChanges elemChangesPeek = this.path.peek();
                if (elementIndex == elementIndex2) {
                    Element element2 = element.getElement(elementIndex);
                    if ((i2 <= element2.getStartOffset() && i3 >= element2.getEndOffset()) || removeElements(element2, i2, i3)) {
                        elemChangesPeek.removed.addElement(element2);
                    }
                } else {
                    Element element3 = element.getElement(elementIndex);
                    Element element4 = element.getElement(elementIndex2);
                    boolean z2 = i3 < element.getEndOffset();
                    if (z2 && canJoin(element3, element4)) {
                        for (int i4 = elementIndex; i4 <= elementIndex2; i4++) {
                            elemChangesPeek.removed.addElement(element.getElement(i4));
                        }
                        elemChangesPeek.added.addElement(join(element, element3, element4, i2, i3));
                    } else {
                        int i5 = elementIndex + 1;
                        int i6 = elementIndex2 - 1;
                        if (element3.getStartOffset() == i2 || (elementIndex == 0 && element3.getStartOffset() > i2 && element3.getEndOffset() <= i3)) {
                            element3 = null;
                            i5 = elementIndex;
                        }
                        if (!z2) {
                            element4 = null;
                            i6++;
                        } else if (element4.getStartOffset() == i3) {
                            element4 = null;
                        }
                        if (i5 <= i6) {
                            elemChangesPeek.index = i5;
                        }
                        for (int i7 = i5; i7 <= i6; i7++) {
                            elemChangesPeek.removed.addElement(element.getElement(i7));
                        }
                        if (element3 != null && removeElements(element3, i2, i3)) {
                            elemChangesPeek.removed.insertElementAt(element3, 0);
                            elemChangesPeek.index = elementIndex;
                        }
                        if (element4 != null && removeElements(element4, i2, i3)) {
                            elemChangesPeek.removed.addElement(element4);
                        }
                    }
                }
                pop();
                if (element.getElementCount() == elemChangesPeek.removed.size() - elemChangesPeek.added.size()) {
                    return true;
                }
                return false;
            }
            return false;
        }

        boolean canJoin(Element element, Element element2) {
            boolean zIsLeaf;
            if (element == null || element2 == null || (zIsLeaf = element.isLeaf()) != element2.isLeaf()) {
                return false;
            }
            if (zIsLeaf) {
                return element.getAttributes().isEqual(element2.getAttributes());
            }
            String name = element.getName();
            String name2 = element2.getName();
            if (name != null) {
                return name.equals(name2);
            }
            if (name2 != null) {
                return name2.equals(name);
            }
            return true;
        }

        Element join(Element element, Element element2, Element element3, int i2, int i3) {
            if (element2.isLeaf() && element3.isLeaf()) {
                return DefaultStyledDocument.this.createLeafElement(element, element2.getAttributes(), element2.getStartOffset(), element3.getEndOffset());
            }
            if (!element2.isLeaf() && !element3.isLeaf()) {
                Element elementCreateBranchElement = DefaultStyledDocument.this.createBranchElement(element, element2.getAttributes());
                int elementIndex = element2.getElementIndex(i2);
                int elementIndex2 = element3.getElementIndex(i3);
                Element element4 = element2.getElement(elementIndex);
                if (element4.getStartOffset() >= i2) {
                    element4 = null;
                }
                Element element5 = element3.getElement(elementIndex2);
                if (element5.getStartOffset() == i3) {
                    element5 = null;
                }
                Vector vector = new Vector();
                for (int i4 = 0; i4 < elementIndex; i4++) {
                    vector.addElement(clone(elementCreateBranchElement, element2.getElement(i4)));
                }
                if (canJoin(element4, element5)) {
                    vector.addElement(join(elementCreateBranchElement, element4, element5, i2, i3));
                } else {
                    if (element4 != null) {
                        vector.addElement(cloneAsNecessary(elementCreateBranchElement, element4, i2, i3));
                    }
                    if (element5 != null) {
                        vector.addElement(cloneAsNecessary(elementCreateBranchElement, element5, i2, i3));
                    }
                }
                int elementCount = element3.getElementCount();
                for (int i5 = element5 == null ? elementIndex2 : elementIndex2 + 1; i5 < elementCount; i5++) {
                    vector.addElement(clone(elementCreateBranchElement, element3.getElement(i5)));
                }
                Element[] elementArr = new Element[vector.size()];
                vector.copyInto(elementArr);
                ((AbstractDocument.BranchElement) elementCreateBranchElement).replace(0, 0, elementArr);
                return elementCreateBranchElement;
            }
            throw new StateInvariantError("No support to join leaf element with non-leaf element");
        }

        public Element clone(Element element, Element element2) {
            if (element2.isLeaf()) {
                return DefaultStyledDocument.this.createLeafElement(element, element2.getAttributes(), element2.getStartOffset(), element2.getEndOffset());
            }
            Element elementCreateBranchElement = DefaultStyledDocument.this.createBranchElement(element, element2.getAttributes());
            int elementCount = element2.getElementCount();
            Element[] elementArr = new Element[elementCount];
            for (int i2 = 0; i2 < elementCount; i2++) {
                elementArr[i2] = clone(elementCreateBranchElement, element2.getElement(i2));
            }
            ((AbstractDocument.BranchElement) elementCreateBranchElement).replace(0, 0, elementArr);
            return elementCreateBranchElement;
        }

        Element cloneAsNecessary(Element element, Element element2, int i2, int i3) {
            if (element2.isLeaf()) {
                return DefaultStyledDocument.this.createLeafElement(element, element2.getAttributes(), element2.getStartOffset(), element2.getEndOffset());
            }
            Element elementCreateBranchElement = DefaultStyledDocument.this.createBranchElement(element, element2.getAttributes());
            int elementCount = element2.getElementCount();
            ArrayList arrayList = new ArrayList(elementCount);
            for (int i4 = 0; i4 < elementCount; i4++) {
                Element element3 = element2.getElement(i4);
                if (element3.getStartOffset() < i2 || element3.getEndOffset() > i3) {
                    arrayList.add(cloneAsNecessary(elementCreateBranchElement, element3, i2, i3));
                }
            }
            ((AbstractDocument.BranchElement) elementCreateBranchElement).replace(0, 0, (Element[]) arrayList.toArray(new Element[arrayList.size()]));
            return elementCreateBranchElement;
        }

        void fracture(int i2) {
            int length = this.insertPath.length;
            int i3 = -1;
            boolean z2 = this.recreateLeafs;
            ElemChanges elemChanges = this.insertPath[length - 1];
            boolean z3 = elemChanges.index + 1 < elemChanges.parent.getElementCount();
            int i4 = z2 ? length : -1;
            int i5 = length - 1;
            this.createdFracture = true;
            for (int i6 = length - 2; i6 >= 0; i6--) {
                ElemChanges elemChanges2 = this.insertPath[i6];
                if (elemChanges2.added.size() > 0 || i6 == i2) {
                    i3 = i6;
                    if (!z2 && z3) {
                        z2 = true;
                        if (i4 == -1) {
                            i4 = i5 + 1;
                        }
                    }
                }
                if (!z3 && elemChanges2.index < elemChanges2.parent.getElementCount()) {
                    z3 = true;
                    i5 = i6;
                }
            }
            if (z2) {
                if (i3 == -1) {
                    i3 = length - 1;
                }
                fractureFrom(this.insertPath, i3, i4);
            }
        }

        void fractureFrom(ElemChanges[] elemChangesArr, int i2, int i3) {
            Element element;
            Element elementCreateBranchElement;
            Element element2;
            int i4;
            Element[] elementArr;
            ElemChanges elemChanges = elemChangesArr[i2];
            int length = elemChangesArr.length;
            if (i2 + 1 == length) {
                element = elemChanges.parent.getElement(elemChanges.index);
            } else {
                element = elemChanges.parent.getElement(elemChanges.index - 1);
            }
            if (element.isLeaf()) {
                elementCreateBranchElement = DefaultStyledDocument.this.createLeafElement(elemChanges.parent, element.getAttributes(), Math.max(this.endOffset, element.getStartOffset()), element.getEndOffset());
            } else {
                elementCreateBranchElement = DefaultStyledDocument.this.createBranchElement(elemChanges.parent, element.getAttributes());
            }
            this.fracturedParent = elemChanges.parent;
            this.fracturedChild = elementCreateBranchElement;
            while (true) {
                Element element3 = elementCreateBranchElement;
                i2++;
                if (i2 < i3) {
                    boolean z2 = i2 + 1 == i3;
                    boolean z3 = i2 + 1 == length;
                    ElemChanges elemChanges2 = elemChangesArr[i2];
                    if (z2) {
                        if (this.offsetLastIndex || !z3) {
                            element2 = null;
                        } else {
                            element2 = elemChanges2.parent.getElement(elemChanges2.index);
                        }
                    } else {
                        element2 = elemChanges2.parent.getElement(elemChanges2.index - 1);
                    }
                    if (element2 != null) {
                        if (element2.isLeaf()) {
                            elementCreateBranchElement = DefaultStyledDocument.this.createLeafElement(element3, element2.getAttributes(), Math.max(this.endOffset, element2.getStartOffset()), element2.getEndOffset());
                        } else {
                            elementCreateBranchElement = DefaultStyledDocument.this.createBranchElement(element3, element2.getAttributes());
                        }
                    } else {
                        elementCreateBranchElement = null;
                    }
                    int elementCount = elemChanges2.parent.getElementCount() - elemChanges2.index;
                    int i5 = 1;
                    if (elementCreateBranchElement == null) {
                        if (z3) {
                            elementCount--;
                            i4 = elemChanges2.index + 1;
                        } else {
                            i4 = elemChanges2.index;
                        }
                        i5 = 0;
                        elementArr = new Element[elementCount];
                    } else {
                        if (!z2) {
                            elementCount++;
                            i4 = elemChanges2.index;
                        } else {
                            i4 = elemChanges2.index + 1;
                        }
                        elementArr = new Element[elementCount];
                        elementArr[0] = elementCreateBranchElement;
                    }
                    for (int i6 = i5; i6 < elementCount; i6++) {
                        int i7 = i4;
                        i4++;
                        Element element4 = elemChanges2.parent.getElement(i7);
                        elementArr[i6] = recreateFracturedElement(element3, element4);
                        elemChanges2.removed.addElement(element4);
                    }
                    ((AbstractDocument.BranchElement) element3).replace(0, 0, elementArr);
                } else {
                    return;
                }
            }
        }

        Element recreateFracturedElement(Element element, Element element2) {
            if (element2.isLeaf()) {
                return DefaultStyledDocument.this.createLeafElement(element, element2.getAttributes(), Math.max(element2.getStartOffset(), this.endOffset), element2.getEndOffset());
            }
            Element elementCreateBranchElement = DefaultStyledDocument.this.createBranchElement(element, element2.getAttributes());
            int elementCount = element2.getElementCount();
            Element[] elementArr = new Element[elementCount];
            for (int i2 = 0; i2 < elementCount; i2++) {
                elementArr[i2] = recreateFracturedElement(elementCreateBranchElement, element2.getElement(i2));
            }
            ((AbstractDocument.BranchElement) elementCreateBranchElement).replace(0, 0, elementArr);
            return elementCreateBranchElement;
        }

        void fractureDeepestLeaf(ElementSpec[] elementSpecArr) {
            ElemChanges elemChangesPeek = this.path.peek();
            Element element = elemChangesPeek.parent.getElement(elemChangesPeek.index);
            if (this.offset != 0) {
                elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element.getAttributes(), element.getStartOffset(), this.offset));
            }
            elemChangesPeek.removed.addElement(element);
            if (element.getEndOffset() != this.endOffset) {
                this.recreateLeafs = true;
            } else {
                this.offsetLastIndex = true;
            }
        }

        void insertFirstContent(ElementSpec[] elementSpecArr) {
            Element elementCreateLeafElement;
            ElementSpec elementSpec = elementSpecArr[0];
            ElemChanges elemChangesPeek = this.path.peek();
            Element element = elemChangesPeek.parent.getElement(elemChangesPeek.index);
            int length = this.offset + elementSpec.getLength();
            boolean z2 = elementSpecArr.length == 1;
            switch (elementSpec.getDirection()) {
                case 4:
                    if (element.getEndOffset() != length && !z2) {
                        elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element.getAttributes(), element.getStartOffset(), length));
                        elemChangesPeek.removed.addElement(element);
                        if (element.getEndOffset() != this.endOffset) {
                            this.recreateLeafs = true;
                            break;
                        } else {
                            this.offsetLastIndex = true;
                            break;
                        }
                    } else {
                        this.offsetLastIndex = true;
                        this.offsetLastIndexOnReplace = true;
                        break;
                    }
                    break;
                case 5:
                    if (this.offset != 0) {
                        elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element.getAttributes(), element.getStartOffset(), this.offset));
                        Element element2 = elemChangesPeek.parent.getElement(elemChangesPeek.index + 1);
                        if (z2) {
                            elementCreateLeafElement = DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element2.getAttributes(), this.offset, element2.getEndOffset());
                        } else {
                            elementCreateLeafElement = DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element2.getAttributes(), this.offset, length);
                        }
                        elemChangesPeek.added.addElement(elementCreateLeafElement);
                        elemChangesPeek.removed.addElement(element);
                        elemChangesPeek.removed.addElement(element2);
                        break;
                    }
                    break;
                default:
                    if (element.getStartOffset() != this.offset) {
                        elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, element.getAttributes(), element.getStartOffset(), this.offset));
                    }
                    elemChangesPeek.removed.addElement(element);
                    elemChangesPeek.added.addElement(DefaultStyledDocument.this.createLeafElement(elemChangesPeek.parent, elementSpec.getAttributes(), this.offset, length));
                    if (element.getEndOffset() != this.endOffset) {
                        this.recreateLeafs = true;
                        break;
                    } else {
                        this.offsetLastIndex = true;
                        break;
                    }
            }
        }

        /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$ElementBuffer$ElemChanges.class */
        class ElemChanges {
            Element parent;
            int index;
            Vector<Element> added = new Vector<>();
            Vector<Element> removed = new Vector<>();
            boolean isFracture;

            ElemChanges(Element element, int i2, boolean z2) {
                this.parent = element;
                this.index = i2;
                this.isFracture = z2;
            }

            public String toString() {
                return "added: " + ((Object) this.added) + "\nremoved: " + ((Object) this.removed) + "\n";
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$AttributeUndoableEdit.class */
    public static class AttributeUndoableEdit extends AbstractUndoableEdit {
        protected AttributeSet newAttributes;
        protected AttributeSet copy;
        protected boolean isReplacing;
        protected Element element;

        public AttributeUndoableEdit(Element element, AttributeSet attributeSet, boolean z2) {
            this.element = element;
            this.newAttributes = attributeSet;
            this.isReplacing = z2;
            this.copy = element.getAttributes().copyAttributes();
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void redo() throws CannotRedoException {
            super.redo();
            MutableAttributeSet mutableAttributeSet = (MutableAttributeSet) this.element.getAttributes();
            if (this.isReplacing) {
                mutableAttributeSet.removeAttributes(mutableAttributeSet);
            }
            mutableAttributeSet.addAttributes(this.newAttributes);
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void undo() throws CannotUndoException {
            super.undo();
            MutableAttributeSet mutableAttributeSet = (MutableAttributeSet) this.element.getAttributes();
            mutableAttributeSet.removeAttributes(mutableAttributeSet);
            mutableAttributeSet.addAttributes(this.copy);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$StyleChangeUndoableEdit.class */
    static class StyleChangeUndoableEdit extends AbstractUndoableEdit {
        protected AbstractDocument.AbstractElement element;
        protected Style newStyle;
        protected AttributeSet oldStyle;

        public StyleChangeUndoableEdit(AbstractDocument.AbstractElement abstractElement, Style style) {
            this.element = abstractElement;
            this.newStyle = style;
            this.oldStyle = abstractElement.getResolveParent();
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void redo() throws CannotRedoException {
            super.redo();
            this.element.setResolveParent(this.newStyle);
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void undo() throws CannotUndoException {
            super.undo();
            this.element.setResolveParent(this.oldStyle);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$AbstractChangeHandler.class */
    static abstract class AbstractChangeHandler implements ChangeListener {
        private static final Map<Class, ReferenceQueue<DefaultStyledDocument>> queueMap = new HashMap();
        private DocReference doc;

        abstract void fireStateChanged(DefaultStyledDocument defaultStyledDocument, ChangeEvent changeEvent);

        /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$AbstractChangeHandler$DocReference.class */
        private class DocReference extends WeakReference<DefaultStyledDocument> {
            DocReference(DefaultStyledDocument defaultStyledDocument, ReferenceQueue<DefaultStyledDocument> referenceQueue) {
                super(defaultStyledDocument, referenceQueue);
            }

            ChangeListener getListener() {
                return AbstractChangeHandler.this;
            }
        }

        AbstractChangeHandler(DefaultStyledDocument defaultStyledDocument) {
            ReferenceQueue<DefaultStyledDocument> referenceQueue;
            Class<?> cls = getClass();
            synchronized (queueMap) {
                referenceQueue = queueMap.get(cls);
                if (referenceQueue == null) {
                    referenceQueue = new ReferenceQueue<>();
                    queueMap.put(cls, referenceQueue);
                }
            }
            this.doc = new DocReference(defaultStyledDocument, referenceQueue);
        }

        static List<ChangeListener> getStaleListeners(ChangeListener changeListener) {
            ArrayList arrayList = new ArrayList();
            ReferenceQueue<DefaultStyledDocument> referenceQueue = queueMap.get(changeListener.getClass());
            if (referenceQueue != null) {
                synchronized (referenceQueue) {
                    while (true) {
                        DocReference docReference = (DocReference) referenceQueue.poll();
                        if (docReference == null) {
                            break;
                        }
                        arrayList.add(docReference.getListener());
                    }
                }
            }
            return arrayList;
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            DefaultStyledDocument defaultStyledDocument = this.doc.get();
            if (defaultStyledDocument != null) {
                fireStateChanged(defaultStyledDocument, changeEvent);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$StyleChangeHandler.class */
    static class StyleChangeHandler extends AbstractChangeHandler {
        StyleChangeHandler(DefaultStyledDocument defaultStyledDocument) {
            super(defaultStyledDocument);
        }

        @Override // javax.swing.text.DefaultStyledDocument.AbstractChangeHandler
        void fireStateChanged(DefaultStyledDocument defaultStyledDocument, ChangeEvent changeEvent) {
            Object source = changeEvent.getSource();
            if (source instanceof Style) {
                defaultStyledDocument.styleChanged((Style) source);
            } else {
                defaultStyledDocument.styleChanged(null);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$StyleContextChangeHandler.class */
    static class StyleContextChangeHandler extends AbstractChangeHandler {
        StyleContextChangeHandler(DefaultStyledDocument defaultStyledDocument) {
            super(defaultStyledDocument);
        }

        @Override // javax.swing.text.DefaultStyledDocument.AbstractChangeHandler
        void fireStateChanged(DefaultStyledDocument defaultStyledDocument, ChangeEvent changeEvent) {
            defaultStyledDocument.updateStylesListeningTo();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/DefaultStyledDocument$ChangeUpdateRunnable.class */
    class ChangeUpdateRunnable implements Runnable {
        boolean isPending = false;

        ChangeUpdateRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this) {
                this.isPending = false;
            }
            try {
                DefaultStyledDocument.this.writeLock();
                AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(0, DefaultStyledDocument.this.getLength(), DocumentEvent.EventType.CHANGE);
                defaultDocumentEvent.end();
                DefaultStyledDocument.this.fireChangedUpdate(defaultDocumentEvent);
            } finally {
                DefaultStyledDocument.this.writeUnlock();
            }
        }
    }
}
