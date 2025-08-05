package javax.swing.text;

import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.Bidi;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DocumentFilter;
import javax.swing.tree.TreeNode;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import sun.font.BidiUtils;
import sun.security.pkcs11.wrapper.Constants;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/AbstractDocument.class */
public abstract class AbstractDocument implements Document, Serializable {
    private transient int numReaders;
    private transient Thread currWriter;
    private transient int numWriters;
    private transient boolean notifyingListeners;
    private static Boolean defaultI18NProperty;
    private Dictionary<Object, Object> documentProperties;
    protected EventListenerList listenerList;
    private Content data;
    private AttributeContext context;
    private transient BranchElement bidiRoot;
    private DocumentFilter documentFilter;
    private transient DocumentFilter.FilterBypass filterBypass;
    private static final String BAD_LOCK_STATE = "document lock failure";
    protected static final String BAD_LOCATION = "document location failure";
    public static final String ParagraphElementName = "paragraph";
    public static final String ContentElementName = "content";
    public static final String SectionElementName = "section";
    public static final String BidiElementName = "bidi level";
    public static final String ElementNameAttribute = "$ename";
    static final String I18NProperty = "i18n";
    static final Object MultiByteProperty = "multiByte";
    static final String AsyncLoadPriority = "load priority";

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$AttributeContext.class */
    public interface AttributeContext {
        AttributeSet addAttribute(AttributeSet attributeSet, Object obj, Object obj2);

        AttributeSet addAttributes(AttributeSet attributeSet, AttributeSet attributeSet2);

        AttributeSet removeAttribute(AttributeSet attributeSet, Object obj);

        AttributeSet removeAttributes(AttributeSet attributeSet, Enumeration<?> enumeration);

        AttributeSet removeAttributes(AttributeSet attributeSet, AttributeSet attributeSet2);

        AttributeSet getEmptySet();

        void reclaim(AttributeSet attributeSet);
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$Content.class */
    public interface Content {
        Position createPosition(int i2) throws BadLocationException;

        int length();

        UndoableEdit insertString(int i2, String str) throws BadLocationException;

        UndoableEdit remove(int i2, int i3) throws BadLocationException;

        String getString(int i2, int i3) throws BadLocationException;

        void getChars(int i2, int i3, Segment segment) throws BadLocationException;
    }

    @Override // javax.swing.text.Document
    public abstract Element getDefaultRootElement();

    public abstract Element getParagraphElement(int i2);

    protected AbstractDocument(Content content) {
        this(content, StyleContext.getDefaultStyleContext());
    }

    protected AbstractDocument(Content content, AttributeContext attributeContext) {
        this.documentProperties = null;
        this.listenerList = new EventListenerList();
        this.data = content;
        this.context = attributeContext;
        this.bidiRoot = new BidiRootElement();
        if (defaultI18NProperty == null) {
            String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.swing.text.AbstractDocument.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public String run() {
                    return System.getProperty(AbstractDocument.I18NProperty);
                }
            });
            if (str != null) {
                defaultI18NProperty = Boolean.valueOf(str);
            } else {
                defaultI18NProperty = Boolean.FALSE;
            }
        }
        putProperty(I18NProperty, defaultI18NProperty);
        writeLock();
        try {
            this.bidiRoot.replace(0, 0, new Element[]{new BidiElement(this.bidiRoot, 0, 1, 0)});
            writeUnlock();
        } catch (Throwable th) {
            writeUnlock();
            throw th;
        }
    }

    public Dictionary<Object, Object> getDocumentProperties() {
        if (this.documentProperties == null) {
            this.documentProperties = new Hashtable(2);
        }
        return this.documentProperties;
    }

    public void setDocumentProperties(Dictionary<Object, Object> dictionary) {
        this.documentProperties = dictionary;
    }

    protected void fireInsertUpdate(DocumentEvent documentEvent) {
        this.notifyingListeners = true;
        try {
            Object[] listenerList = this.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == DocumentListener.class) {
                    ((DocumentListener) listenerList[length + 1]).insertUpdate(documentEvent);
                }
            }
        } finally {
            this.notifyingListeners = false;
        }
    }

    protected void fireChangedUpdate(DocumentEvent documentEvent) {
        this.notifyingListeners = true;
        try {
            Object[] listenerList = this.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == DocumentListener.class) {
                    ((DocumentListener) listenerList[length + 1]).changedUpdate(documentEvent);
                }
            }
        } finally {
            this.notifyingListeners = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fireRemoveUpdate(DocumentEvent documentEvent) {
        this.notifyingListeners = true;
        try {
            Object[] listenerList = this.listenerList.getListenerList();
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == DocumentListener.class) {
                    ((DocumentListener) listenerList[length + 1]).removeUpdate(documentEvent);
                }
            }
        } finally {
            this.notifyingListeners = false;
        }
    }

    protected void fireUndoableEditUpdate(UndoableEditEvent undoableEditEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == UndoableEditListener.class) {
                ((UndoableEditListener) listenerList[length + 1]).undoableEditHappened(undoableEditEvent);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }

    public int getAsynchronousLoadPriority() {
        Integer num = (Integer) getProperty(AsyncLoadPriority);
        if (num != null) {
            return num.intValue();
        }
        return -1;
    }

    public void setAsynchronousLoadPriority(int i2) {
        putProperty(AsyncLoadPriority, i2 >= 0 ? Integer.valueOf(i2) : null);
    }

    public void setDocumentFilter(DocumentFilter documentFilter) {
        this.documentFilter = documentFilter;
    }

    public DocumentFilter getDocumentFilter() {
        return this.documentFilter;
    }

    @Override // javax.swing.text.Document
    public void render(Runnable runnable) {
        readLock();
        try {
            runnable.run();
        } finally {
            readUnlock();
        }
    }

    @Override // javax.swing.text.Document
    public int getLength() {
        return this.data.length() - 1;
    }

    @Override // javax.swing.text.Document
    public void addDocumentListener(DocumentListener documentListener) {
        this.listenerList.add(DocumentListener.class, documentListener);
    }

    @Override // javax.swing.text.Document
    public void removeDocumentListener(DocumentListener documentListener) {
        this.listenerList.remove(DocumentListener.class, documentListener);
    }

    public DocumentListener[] getDocumentListeners() {
        return (DocumentListener[]) this.listenerList.getListeners(DocumentListener.class);
    }

    @Override // javax.swing.text.Document
    public void addUndoableEditListener(UndoableEditListener undoableEditListener) {
        this.listenerList.add(UndoableEditListener.class, undoableEditListener);
    }

    @Override // javax.swing.text.Document
    public void removeUndoableEditListener(UndoableEditListener undoableEditListener) {
        this.listenerList.remove(UndoableEditListener.class, undoableEditListener);
    }

    public UndoableEditListener[] getUndoableEditListeners() {
        return (UndoableEditListener[]) this.listenerList.getListeners(UndoableEditListener.class);
    }

    @Override // javax.swing.text.Document
    public final Object getProperty(Object obj) {
        return getDocumentProperties().get(obj);
    }

    @Override // javax.swing.text.Document
    public final void putProperty(Object obj, Object obj2) {
        if (obj2 != null) {
            getDocumentProperties().put(obj, obj2);
        } else {
            getDocumentProperties().remove(obj);
        }
        if (obj == TextAttribute.RUN_DIRECTION && Boolean.TRUE.equals(getProperty(I18NProperty))) {
            writeLock();
            try {
                updateBidi(new DefaultDocumentEvent(0, getLength(), DocumentEvent.EventType.INSERT));
                writeUnlock();
            } catch (Throwable th) {
                writeUnlock();
                throw th;
            }
        }
    }

    @Override // javax.swing.text.Document
    public void remove(int i2, int i3) throws BadLocationException {
        DocumentFilter documentFilter = getDocumentFilter();
        writeLock();
        try {
            if (documentFilter != null) {
                documentFilter.remove(getFilterBypass(), i2, i3);
            } else {
                handleRemove(i2, i3);
            }
            writeUnlock();
        } catch (Throwable th) {
            writeUnlock();
            throw th;
        }
    }

    void handleRemove(int i2, int i3) throws BadLocationException {
        if (i3 > 0) {
            if (i2 < 0 || i2 + i3 > getLength()) {
                throw new BadLocationException("Invalid remove", getLength() + 1);
            }
            DefaultDocumentEvent defaultDocumentEvent = new DefaultDocumentEvent(i2, i3, DocumentEvent.EventType.REMOVE);
            boolean zIsComposedTextElement = Utilities.isComposedTextElement(this, i2);
            removeUpdate(defaultDocumentEvent);
            UndoableEdit undoableEditRemove = this.data.remove(i2, i3);
            if (undoableEditRemove != null) {
                defaultDocumentEvent.addEdit(undoableEditRemove);
            }
            postRemoveUpdate(defaultDocumentEvent);
            defaultDocumentEvent.end();
            fireRemoveUpdate(defaultDocumentEvent);
            if (undoableEditRemove != null && !zIsComposedTextElement) {
                fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
            }
        }
    }

    public void replace(int i2, int i3, String str, AttributeSet attributeSet) throws BadLocationException {
        if (i3 == 0 && (str == null || str.length() == 0)) {
            return;
        }
        DocumentFilter documentFilter = getDocumentFilter();
        writeLock();
        try {
            if (documentFilter != null) {
                documentFilter.replace(getFilterBypass(), i2, i3, str, attributeSet);
            } else {
                if (i3 > 0) {
                    remove(i2, i3);
                }
                if (str != null && str.length() > 0) {
                    insertString(i2, str, attributeSet);
                }
            }
        } finally {
            writeUnlock();
        }
    }

    @Override // javax.swing.text.Document
    public void insertString(int i2, String str, AttributeSet attributeSet) throws BadLocationException {
        if (str == null || str.length() == 0) {
            return;
        }
        DocumentFilter documentFilter = getDocumentFilter();
        writeLock();
        try {
            if (documentFilter != null) {
                documentFilter.insertString(getFilterBypass(), i2, str, attributeSet);
            } else {
                handleInsertString(i2, str, attributeSet);
            }
            writeUnlock();
        } catch (Throwable th) {
            writeUnlock();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInsertString(int i2, String str, AttributeSet attributeSet) throws BadLocationException {
        if (str == null || str.length() == 0) {
            return;
        }
        UndoableEdit undoableEditInsertString = this.data.insertString(i2, str);
        DefaultDocumentEvent defaultDocumentEvent = new DefaultDocumentEvent(i2, str.length(), DocumentEvent.EventType.INSERT);
        if (undoableEditInsertString != null) {
            defaultDocumentEvent.addEdit(undoableEditInsertString);
        }
        if (getProperty(I18NProperty).equals(Boolean.FALSE)) {
            Object property = getProperty(TextAttribute.RUN_DIRECTION);
            if (property != null && property.equals(TextAttribute.RUN_DIRECTION_RTL)) {
                putProperty(I18NProperty, Boolean.TRUE);
            } else {
                char[] charArray = str.toCharArray();
                if (SwingUtilities2.isComplexLayout(charArray, 0, charArray.length)) {
                    putProperty(I18NProperty, Boolean.TRUE);
                }
            }
        }
        insertUpdate(defaultDocumentEvent, attributeSet);
        defaultDocumentEvent.end();
        fireInsertUpdate(defaultDocumentEvent);
        if (undoableEditInsertString != null) {
            if (attributeSet == null || !attributeSet.isDefined(StyleConstants.ComposedTextAttribute)) {
                fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
            }
        }
    }

    @Override // javax.swing.text.Document
    public String getText(int i2, int i3) throws BadLocationException {
        if (i3 < 0) {
            throw new BadLocationException("Length must be positive", i3);
        }
        return this.data.getString(i2, i3);
    }

    @Override // javax.swing.text.Document
    public void getText(int i2, int i3, Segment segment) throws BadLocationException {
        if (i3 < 0) {
            throw new BadLocationException("Length must be positive", i3);
        }
        this.data.getChars(i2, i3, segment);
    }

    @Override // javax.swing.text.Document
    public synchronized Position createPosition(int i2) throws BadLocationException {
        return this.data.createPosition(i2);
    }

    @Override // javax.swing.text.Document
    public final Position getStartPosition() {
        Position positionCreatePosition;
        try {
            positionCreatePosition = createPosition(0);
        } catch (BadLocationException e2) {
            positionCreatePosition = null;
        }
        return positionCreatePosition;
    }

    @Override // javax.swing.text.Document
    public final Position getEndPosition() {
        Position positionCreatePosition;
        try {
            positionCreatePosition = createPosition(this.data.length());
        } catch (BadLocationException e2) {
            positionCreatePosition = null;
        }
        return positionCreatePosition;
    }

    @Override // javax.swing.text.Document
    public Element[] getRootElements() {
        return new Element[]{getDefaultRootElement(), getBidiRootElement()};
    }

    private DocumentFilter.FilterBypass getFilterBypass() {
        if (this.filterBypass == null) {
            this.filterBypass = new DefaultFilterBypass();
        }
        return this.filterBypass;
    }

    public Element getBidiRootElement() {
        return this.bidiRoot;
    }

    static boolean isLeftToRight(Document document, int i2, int i3) {
        if (Boolean.TRUE.equals(document.getProperty(I18NProperty)) && (document instanceof AbstractDocument)) {
            Element bidiRootElement = ((AbstractDocument) document).getBidiRootElement();
            Element element = bidiRootElement.getElement(bidiRootElement.getElementIndex(i2));
            return element.getEndOffset() < i3 || StyleConstants.getBidiLevel(element.getAttributes()) % 2 == 0;
        }
        return true;
    }

    protected final AttributeContext getAttributeContext() {
        return this.context;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0055, code lost:
    
        putProperty(javax.swing.text.AbstractDocument.MultiByteProperty, java.lang.Boolean.TRUE);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void insertUpdate(javax.swing.text.AbstractDocument.DefaultDocumentEvent r6, javax.swing.text.AttributeSet r7) {
        /*
            r5 = this;
            r0 = r5
            java.lang.String r1 = "i18n"
            java.lang.Object r0 = r0.getProperty(r1)
            java.lang.Boolean r1 = java.lang.Boolean.TRUE
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L14
            r0 = r5
            r1 = r6
            r0.updateBidi(r1)
        L14:
            r0 = r6
            javax.swing.event.DocumentEvent$EventType r0 = javax.swing.text.AbstractDocument.DefaultDocumentEvent.access$100(r0)
            javax.swing.event.DocumentEvent$EventType r1 = javax.swing.event.DocumentEvent.EventType.INSERT
            if (r0 != r1) goto L74
            r0 = r6
            int r0 = r0.getLength()
            if (r0 <= 0) goto L74
            java.lang.Boolean r0 = java.lang.Boolean.TRUE
            r1 = r5
            java.lang.Object r2 = javax.swing.text.AbstractDocument.MultiByteProperty
            java.lang.Object r1 = r1.getProperty(r2)
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L74
            javax.swing.text.Segment r0 = javax.swing.text.SegmentCache.getSharedSegment()
            r8 = r0
            r0 = r5
            r1 = r6
            int r1 = r1.getOffset()     // Catch: javax.swing.text.BadLocationException -> L6e
            r2 = r6
            int r2 = r2.getLength()     // Catch: javax.swing.text.BadLocationException -> L6e
            r3 = r8
            r0.getText(r1, r2, r3)     // Catch: javax.swing.text.BadLocationException -> L6e
            r0 = r8
            char r0 = r0.first()     // Catch: javax.swing.text.BadLocationException -> L6e
        L4b:
            r0 = r8
            char r0 = r0.current()     // Catch: javax.swing.text.BadLocationException -> L6e
            r1 = 255(0xff, float:3.57E-43)
            if (r0 <= r1) goto L62
            r0 = r5
            java.lang.Object r1 = javax.swing.text.AbstractDocument.MultiByteProperty     // Catch: javax.swing.text.BadLocationException -> L6e
            java.lang.Boolean r2 = java.lang.Boolean.TRUE     // Catch: javax.swing.text.BadLocationException -> L6e
            r0.putProperty(r1, r2)     // Catch: javax.swing.text.BadLocationException -> L6e
            goto L6b
        L62:
            r0 = r8
            char r0 = r0.next()     // Catch: javax.swing.text.BadLocationException -> L6e
            r1 = 65535(0xffff, float:9.1834E-41)
            if (r0 != r1) goto L4b
        L6b:
            goto L70
        L6e:
            r9 = move-exception
        L70:
            r0 = r8
            javax.swing.text.SegmentCache.releaseSharedSegment(r0)
        L74:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.text.AbstractDocument.insertUpdate(javax.swing.text.AbstractDocument$DefaultDocumentEvent, javax.swing.text.AttributeSet):void");
    }

    protected void removeUpdate(DefaultDocumentEvent defaultDocumentEvent) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void postRemoveUpdate(DefaultDocumentEvent defaultDocumentEvent) {
        if (getProperty(I18NProperty).equals(Boolean.TRUE)) {
            updateBidi(defaultDocumentEvent);
        }
    }

    void updateBidi(DefaultDocumentEvent defaultDocumentEvent) {
        int startOffset;
        int endOffset;
        if (defaultDocumentEvent.type == DocumentEvent.EventType.INSERT || defaultDocumentEvent.type == DocumentEvent.EventType.CHANGE) {
            int offset = defaultDocumentEvent.getOffset();
            int length = offset + defaultDocumentEvent.getLength();
            startOffset = getParagraphElement(offset).getStartOffset();
            endOffset = getParagraphElement(length).getEndOffset();
        } else if (defaultDocumentEvent.type == DocumentEvent.EventType.REMOVE) {
            Element paragraphElement = getParagraphElement(defaultDocumentEvent.getOffset());
            startOffset = paragraphElement.getStartOffset();
            endOffset = paragraphElement.getEndOffset();
        } else {
            throw new Error("Internal error: unknown event type.");
        }
        byte[] bArrCalculateBidiLevels = calculateBidiLevels(startOffset, endOffset);
        Vector vector = new Vector();
        int startOffset2 = startOffset;
        int i2 = 0;
        if (startOffset2 > 0) {
            int elementIndex = this.bidiRoot.getElementIndex(startOffset - 1);
            i2 = elementIndex;
            Element element = this.bidiRoot.getElement(elementIndex);
            int bidiLevel = StyleConstants.getBidiLevel(element.getAttributes());
            if (bidiLevel == bArrCalculateBidiLevels[0]) {
                startOffset2 = element.getStartOffset();
            } else if (element.getEndOffset() > startOffset) {
                vector.addElement(new BidiElement(this.bidiRoot, element.getStartOffset(), startOffset, bidiLevel));
            } else {
                i2++;
            }
        }
        int i3 = 0;
        while (i3 < bArrCalculateBidiLevels.length && bArrCalculateBidiLevels[i3] == bArrCalculateBidiLevels[0]) {
            i3++;
        }
        int endOffset2 = endOffset;
        BidiElement bidiElement = null;
        int elementCount = this.bidiRoot.getElementCount() - 1;
        if (endOffset2 <= getLength()) {
            int elementIndex2 = this.bidiRoot.getElementIndex(endOffset);
            elementCount = elementIndex2;
            Element element2 = this.bidiRoot.getElement(elementIndex2);
            int bidiLevel2 = StyleConstants.getBidiLevel(element2.getAttributes());
            if (bidiLevel2 == bArrCalculateBidiLevels[bArrCalculateBidiLevels.length - 1]) {
                endOffset2 = element2.getEndOffset();
            } else if (element2.getStartOffset() < endOffset) {
                bidiElement = new BidiElement(this.bidiRoot, endOffset, element2.getEndOffset(), bidiLevel2);
            } else {
                elementCount--;
            }
        }
        int length2 = bArrCalculateBidiLevels.length;
        while (length2 > i3 && bArrCalculateBidiLevels[length2 - 1] == bArrCalculateBidiLevels[bArrCalculateBidiLevels.length - 1]) {
            length2--;
        }
        if (i3 == length2 && bArrCalculateBidiLevels[0] == bArrCalculateBidiLevels[bArrCalculateBidiLevels.length - 1]) {
            vector.addElement(new BidiElement(this.bidiRoot, startOffset2, endOffset2, bArrCalculateBidiLevels[0]));
        } else {
            vector.addElement(new BidiElement(this.bidiRoot, startOffset2, i3 + startOffset, bArrCalculateBidiLevels[0]));
            int i4 = i3;
            while (true) {
                int i5 = i4;
                if (i5 >= length2) {
                    break;
                }
                int i6 = i5;
                while (i6 < bArrCalculateBidiLevels.length && bArrCalculateBidiLevels[i6] == bArrCalculateBidiLevels[i5]) {
                    i6++;
                }
                vector.addElement(new BidiElement(this.bidiRoot, startOffset + i5, startOffset + i6, bArrCalculateBidiLevels[i5]));
                i4 = i6;
            }
            vector.addElement(new BidiElement(this.bidiRoot, length2 + startOffset, endOffset2, bArrCalculateBidiLevels[bArrCalculateBidiLevels.length - 1]));
        }
        if (bidiElement != null) {
            vector.addElement(bidiElement);
        }
        int i7 = 0;
        if (this.bidiRoot.getElementCount() > 0) {
            i7 = (elementCount - i2) + 1;
        }
        Element[] elementArr = new Element[i7];
        for (int i8 = 0; i8 < i7; i8++) {
            elementArr[i8] = this.bidiRoot.getElement(i2 + i8);
        }
        Element[] elementArr2 = new Element[vector.size()];
        vector.copyInto(elementArr2);
        defaultDocumentEvent.addEdit(new ElementEdit(this.bidiRoot, i2, elementArr, elementArr2));
        this.bidiRoot.replace(i2, elementArr.length, elementArr2);
    }

    private byte[] calculateBidiLevels(int i2, int i3) {
        byte[] bArr = new byte[i3 - i2];
        int length = 0;
        Boolean bool = null;
        Object property = getProperty(TextAttribute.RUN_DIRECTION);
        if (property instanceof Boolean) {
            bool = (Boolean) property;
        }
        int endOffset = i2;
        while (endOffset < i3) {
            Element paragraphElement = getParagraphElement(endOffset);
            int startOffset = paragraphElement.getStartOffset();
            int endOffset2 = paragraphElement.getEndOffset();
            Boolean bool2 = bool;
            Object attribute = paragraphElement.getAttributes().getAttribute(TextAttribute.RUN_DIRECTION);
            if (attribute instanceof Boolean) {
                bool2 = (Boolean) attribute;
            }
            Segment sharedSegment = SegmentCache.getSharedSegment();
            try {
                getText(startOffset, endOffset2 - startOffset, sharedSegment);
                int i4 = -2;
                if (bool2 != null) {
                    if (TextAttribute.RUN_DIRECTION_LTR.equals(bool2)) {
                        i4 = 0;
                    } else {
                        i4 = 1;
                    }
                }
                Bidi bidi = new Bidi(sharedSegment.array, sharedSegment.offset, null, 0, sharedSegment.count, i4);
                BidiUtils.getLevels(bidi, bArr, length);
                length += bidi.getLength();
                endOffset = paragraphElement.getEndOffset();
                SegmentCache.releaseSharedSegment(sharedSegment);
            } catch (BadLocationException e2) {
                throw new Error("Internal error: " + e2.toString());
            }
        }
        if (length != bArr.length) {
            throw new Error("levelsEnd assertion failed.");
        }
        return bArr;
    }

    public void dump(PrintStream printStream) {
        Element defaultRootElement = getDefaultRootElement();
        if (defaultRootElement instanceof AbstractElement) {
            ((AbstractElement) defaultRootElement).dump(printStream, 0);
        }
        this.bidiRoot.dump(printStream, 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Content getContent() {
        return this.data;
    }

    protected Element createLeafElement(Element element, AttributeSet attributeSet, int i2, int i3) {
        return new LeafElement(element, attributeSet, i2, i3);
    }

    protected Element createBranchElement(Element element, AttributeSet attributeSet) {
        return new BranchElement(element, attributeSet);
    }

    protected final synchronized Thread getCurrentWriter() {
        return this.currWriter;
    }

    protected final synchronized void writeLock() {
        while (true) {
            try {
                if (this.numReaders > 0 || this.currWriter != null) {
                    if (Thread.currentThread() == this.currWriter) {
                        if (this.notifyingListeners) {
                            throw new IllegalStateException("Attempt to mutate in notification");
                        }
                        this.numWriters++;
                        return;
                    }
                    wait();
                } else {
                    this.currWriter = Thread.currentThread();
                    this.numWriters = 1;
                    return;
                }
            } catch (InterruptedException e2) {
                throw new Error("Interrupted attempt to acquire write lock");
            }
        }
    }

    protected final synchronized void writeUnlock() {
        int i2 = this.numWriters - 1;
        this.numWriters = i2;
        if (i2 <= 0) {
            this.numWriters = 0;
            this.currWriter = null;
            notifyAll();
        }
    }

    public final synchronized void readLock() {
        while (this.currWriter != null) {
            try {
                if (this.currWriter == Thread.currentThread()) {
                    return;
                } else {
                    wait();
                }
            } catch (InterruptedException e2) {
                throw new Error("Interrupted attempt to acquire read lock");
            }
        }
        this.numReaders++;
    }

    public final synchronized void readUnlock() {
        if (this.currWriter == Thread.currentThread()) {
            return;
        }
        if (this.numReaders <= 0) {
            throw new StateInvariantError(BAD_LOCK_STATE);
        }
        this.numReaders--;
        notify();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.listenerList = new EventListenerList();
        this.bidiRoot = new BidiRootElement();
        try {
            writeLock();
            this.bidiRoot.replace(0, 0, new Element[]{new BidiElement(this.bidiRoot, 0, 1, 0)});
            objectInputStream.registerValidation(new ObjectInputValidation() { // from class: javax.swing.text.AbstractDocument.2
                @Override // java.io.ObjectInputValidation
                public void validateObject() {
                    try {
                        AbstractDocument.this.writeLock();
                        AbstractDocument.this.updateBidi(AbstractDocument.this.new DefaultDocumentEvent(0, AbstractDocument.this.getLength(), DocumentEvent.EventType.INSERT));
                    } finally {
                        AbstractDocument.this.writeUnlock();
                    }
                }
            }, 0);
        } finally {
            writeUnlock();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$AbstractElement.class */
    public abstract class AbstractElement implements Element, MutableAttributeSet, Serializable, TreeNode {
        private Element parent;
        private transient AttributeSet attributes;

        @Override // javax.swing.text.Element
        public abstract int getStartOffset();

        @Override // javax.swing.text.Element
        public abstract int getEndOffset();

        @Override // javax.swing.text.Element
        public abstract Element getElement(int i2);

        @Override // javax.swing.text.Element
        public abstract int getElementCount();

        @Override // javax.swing.text.Element
        public abstract int getElementIndex(int i2);

        @Override // javax.swing.text.Element, javax.swing.tree.TreeNode
        public abstract boolean isLeaf();

        @Override // javax.swing.tree.TreeNode
        public abstract boolean getAllowsChildren();

        @Override // javax.swing.tree.TreeNode
        public abstract Enumeration children();

        public AbstractElement(Element element, AttributeSet attributeSet) {
            this.parent = element;
            this.attributes = AbstractDocument.this.getAttributeContext().getEmptySet();
            if (attributeSet != null) {
                addAttributes(attributeSet);
            }
        }

        private final void indent(PrintWriter printWriter, int i2) {
            for (int i3 = 0; i3 < i2; i3++) {
                printWriter.print(Constants.INDENT);
            }
        }

        public void dump(PrintStream printStream, int i2) {
            PrintWriter printWriter;
            try {
                printWriter = new PrintWriter((Writer) new OutputStreamWriter(printStream, "JavaEsc"), true);
            } catch (UnsupportedEncodingException e2) {
                printWriter = new PrintWriter((OutputStream) printStream, true);
            }
            indent(printWriter, i2);
            if (getName() == null) {
                printWriter.print("<??");
            } else {
                printWriter.print("<" + getName());
            }
            if (getAttributeCount() > 0) {
                printWriter.println("");
                Enumeration<?> attributeNames = this.attributes.getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    Object objNextElement = attributeNames.nextElement2();
                    indent(printWriter, i2 + 1);
                    printWriter.println(objNextElement + "=" + getAttribute(objNextElement));
                }
                indent(printWriter, i2);
            }
            printWriter.println(">");
            if (isLeaf()) {
                indent(printWriter, i2 + 1);
                printWriter.print("[" + getStartOffset() + "," + getEndOffset() + "]");
                try {
                    String string = AbstractDocument.this.getContent().getString(getStartOffset(), getEndOffset() - getStartOffset());
                    if (string.length() > 40) {
                        string = string.substring(0, 40) + "...";
                    }
                    printWriter.println("[" + string + "]");
                    return;
                } catch (BadLocationException e3) {
                    return;
                }
            }
            int elementCount = getElementCount();
            for (int i3 = 0; i3 < elementCount; i3++) {
                ((AbstractElement) getElement(i3)).dump(printStream, i2 + 1);
            }
        }

        @Override // javax.swing.text.AttributeSet
        public int getAttributeCount() {
            return this.attributes.getAttributeCount();
        }

        @Override // javax.swing.text.AttributeSet
        public boolean isDefined(Object obj) {
            return this.attributes.isDefined(obj);
        }

        @Override // javax.swing.text.AttributeSet
        public boolean isEqual(AttributeSet attributeSet) {
            return this.attributes.isEqual(attributeSet);
        }

        @Override // javax.swing.text.AttributeSet
        public AttributeSet copyAttributes() {
            return this.attributes.copyAttributes();
        }

        @Override // javax.swing.text.AttributeSet
        public Object getAttribute(Object obj) {
            Object attribute = this.attributes.getAttribute(obj);
            if (attribute == null) {
                AttributeSet attributes = this.parent != null ? this.parent.getAttributes() : null;
                if (attributes != null) {
                    attribute = attributes.getAttribute(obj);
                }
            }
            return attribute;
        }

        @Override // javax.swing.text.AttributeSet
        public Enumeration<?> getAttributeNames() {
            return this.attributes.getAttributeNames();
        }

        @Override // javax.swing.text.AttributeSet
        public boolean containsAttribute(Object obj, Object obj2) {
            return this.attributes.containsAttribute(obj, obj2);
        }

        @Override // javax.swing.text.AttributeSet
        public boolean containsAttributes(AttributeSet attributeSet) {
            return this.attributes.containsAttributes(attributeSet);
        }

        @Override // javax.swing.text.AttributeSet
        public AttributeSet getResolveParent() {
            AttributeSet resolveParent = this.attributes.getResolveParent();
            if (resolveParent == null && this.parent != null) {
                resolveParent = this.parent.getAttributes();
            }
            return resolveParent;
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void addAttribute(Object obj, Object obj2) {
            checkForIllegalCast();
            this.attributes = AbstractDocument.this.getAttributeContext().addAttribute(this.attributes, obj, obj2);
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void addAttributes(AttributeSet attributeSet) {
            checkForIllegalCast();
            this.attributes = AbstractDocument.this.getAttributeContext().addAttributes(this.attributes, attributeSet);
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void removeAttribute(Object obj) {
            checkForIllegalCast();
            this.attributes = AbstractDocument.this.getAttributeContext().removeAttribute(this.attributes, obj);
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void removeAttributes(Enumeration<?> enumeration) {
            checkForIllegalCast();
            this.attributes = AbstractDocument.this.getAttributeContext().removeAttributes(this.attributes, enumeration);
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void removeAttributes(AttributeSet attributeSet) {
            checkForIllegalCast();
            AttributeContext attributeContext = AbstractDocument.this.getAttributeContext();
            if (attributeSet == this) {
                this.attributes = attributeContext.getEmptySet();
            } else {
                this.attributes = attributeContext.removeAttributes(this.attributes, attributeSet);
            }
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void setResolveParent(AttributeSet attributeSet) {
            checkForIllegalCast();
            AttributeContext attributeContext = AbstractDocument.this.getAttributeContext();
            if (attributeSet != null) {
                this.attributes = attributeContext.addAttribute(this.attributes, StyleConstants.ResolveAttribute, attributeSet);
            } else {
                this.attributes = attributeContext.removeAttribute(this.attributes, StyleConstants.ResolveAttribute);
            }
        }

        private final void checkForIllegalCast() {
            Thread currentWriter = AbstractDocument.this.getCurrentWriter();
            if (currentWriter == null || currentWriter != Thread.currentThread()) {
                throw new StateInvariantError("Illegal cast to MutableAttributeSet");
            }
        }

        @Override // javax.swing.text.Element
        public Document getDocument() {
            return AbstractDocument.this;
        }

        @Override // javax.swing.text.Element
        public Element getParentElement() {
            return this.parent;
        }

        @Override // javax.swing.text.Element
        public AttributeSet getAttributes() {
            return this;
        }

        @Override // javax.swing.text.Element
        public String getName() {
            if (this.attributes.isDefined(AbstractDocument.ElementNameAttribute)) {
                return (String) this.attributes.getAttribute(AbstractDocument.ElementNameAttribute);
            }
            return null;
        }

        @Override // javax.swing.tree.TreeNode
        public TreeNode getChildAt(int i2) {
            return (TreeNode) getElement(i2);
        }

        @Override // javax.swing.tree.TreeNode
        public int getChildCount() {
            return getElementCount();
        }

        @Override // javax.swing.tree.TreeNode
        public TreeNode getParent() {
            return (TreeNode) getParentElement();
        }

        @Override // javax.swing.tree.TreeNode
        public int getIndex(TreeNode treeNode) {
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                if (getChildAt(childCount) == treeNode) {
                    return childCount;
                }
            }
            return -1;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            StyleContext.writeAttributeSet(objectOutputStream, this.attributes);
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
            StyleContext.readAttributeSet(objectInputStream, simpleAttributeSet);
            this.attributes = AbstractDocument.this.getAttributeContext().addAttributes(SimpleAttributeSet.EMPTY, simpleAttributeSet);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$BranchElement.class */
    public class BranchElement extends AbstractElement {
        private AbstractElement[] children;
        private int nchildren;
        private int lastIndex;

        public BranchElement(Element element, AttributeSet attributeSet) {
            super(element, attributeSet);
            this.children = new AbstractElement[1];
            this.nchildren = 0;
            this.lastIndex = -1;
        }

        public Element positionToElement(int i2) {
            AbstractElement abstractElement = this.children[getElementIndex(i2)];
            int startOffset = abstractElement.getStartOffset();
            int endOffset = abstractElement.getEndOffset();
            if (i2 >= startOffset && i2 < endOffset) {
                return abstractElement;
            }
            return null;
        }

        public void replace(int i2, int i3, Element[] elementArr) {
            int length = elementArr.length - i3;
            int i4 = i2 + i3;
            int i5 = this.nchildren - i4;
            int i6 = i4 + length;
            if (this.nchildren + length >= this.children.length) {
                AbstractElement[] abstractElementArr = new AbstractElement[Math.max(2 * this.children.length, this.nchildren + length)];
                System.arraycopy(this.children, 0, abstractElementArr, 0, i2);
                System.arraycopy(elementArr, 0, abstractElementArr, i2, elementArr.length);
                System.arraycopy(this.children, i4, abstractElementArr, i6, i5);
                this.children = abstractElementArr;
            } else {
                System.arraycopy(this.children, i4, this.children, i6, i5);
                System.arraycopy(elementArr, 0, this.children, i2, elementArr.length);
            }
            this.nchildren += length;
        }

        public String toString() {
            return "BranchElement(" + getName() + ") " + getStartOffset() + "," + getEndOffset() + "\n";
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public String getName() {
            String name = super.getName();
            if (name == null) {
                name = AbstractDocument.ParagraphElementName;
            }
            return name;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public int getStartOffset() {
            return this.children[0].getStartOffset();
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public int getEndOffset() {
            return (this.nchildren > 0 ? this.children[this.nchildren - 1] : this.children[0]).getEndOffset();
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public Element getElement(int i2) {
            if (i2 < this.nchildren) {
                return this.children[i2];
            }
            return null;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public int getElementCount() {
            return this.nchildren;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public int getElementIndex(int i2) {
            int i3;
            int i4 = 0;
            int i5 = this.nchildren - 1;
            int i6 = 0;
            int startOffset = getStartOffset();
            if (this.nchildren == 0) {
                return 0;
            }
            if (i2 < getEndOffset()) {
                if (this.lastIndex >= 0 && this.lastIndex <= i5) {
                    AbstractElement abstractElement = this.children[this.lastIndex];
                    startOffset = abstractElement.getStartOffset();
                    int endOffset = abstractElement.getEndOffset();
                    if (i2 >= startOffset && i2 < endOffset) {
                        return this.lastIndex;
                    }
                    if (i2 < startOffset) {
                        i5 = this.lastIndex;
                    } else {
                        i4 = this.lastIndex;
                    }
                }
                while (i4 <= i5) {
                    i6 = i4 + ((i5 - i4) / 2);
                    AbstractElement abstractElement2 = this.children[i6];
                    startOffset = abstractElement2.getStartOffset();
                    int endOffset2 = abstractElement2.getEndOffset();
                    if (i2 >= startOffset && i2 < endOffset2) {
                        this.lastIndex = i6;
                        return i6;
                    }
                    if (i2 < startOffset) {
                        i5 = i6 - 1;
                    } else {
                        i4 = i6 + 1;
                    }
                }
                if (i2 < startOffset) {
                    i3 = i6;
                } else {
                    i3 = i6 + 1;
                }
                this.lastIndex = i3;
                return i3;
            }
            return this.nchildren - 1;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element, javax.swing.tree.TreeNode
        public boolean isLeaf() {
            return false;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.tree.TreeNode
        public boolean getAllowsChildren() {
            return true;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.tree.TreeNode
        public Enumeration children() {
            if (this.nchildren == 0) {
                return null;
            }
            Vector vector = new Vector(this.nchildren);
            for (int i2 = 0; i2 < this.nchildren; i2++) {
                vector.addElement(this.children[i2]);
            }
            return vector.elements();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$LeafElement.class */
    public class LeafElement extends AbstractElement {
        private transient Position p0;
        private transient Position p1;

        public LeafElement(Element element, AttributeSet attributeSet, int i2, int i3) {
            super(element, attributeSet);
            try {
                this.p0 = AbstractDocument.this.createPosition(i2);
                this.p1 = AbstractDocument.this.createPosition(i3);
            } catch (BadLocationException e2) {
                this.p0 = null;
                this.p1 = null;
                throw new StateInvariantError("Can't create Position references");
            }
        }

        public String toString() {
            return "LeafElement(" + getName() + ") " + ((Object) this.p0) + "," + ((Object) this.p1) + "\n";
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public int getStartOffset() {
            return this.p0.getOffset();
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public int getEndOffset() {
            return this.p1.getOffset();
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public String getName() {
            String name = super.getName();
            if (name == null) {
                name = AbstractDocument.ContentElementName;
            }
            return name;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public int getElementIndex(int i2) {
            return -1;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public Element getElement(int i2) {
            return null;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public int getElementCount() {
            return 0;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element, javax.swing.tree.TreeNode
        public boolean isLeaf() {
            return true;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.tree.TreeNode
        public boolean getAllowsChildren() {
            return false;
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.tree.TreeNode
        public Enumeration children() {
            return null;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeInt(this.p0.getOffset());
            objectOutputStream.writeInt(this.p1.getOffset());
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            int i2 = objectInputStream.readInt();
            int i3 = objectInputStream.readInt();
            try {
                this.p0 = AbstractDocument.this.createPosition(i2);
                this.p1 = AbstractDocument.this.createPosition(i3);
            } catch (BadLocationException e2) {
                this.p0 = null;
                this.p1 = null;
                throw new IOException("Can't restore Position references");
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$BidiRootElement.class */
    class BidiRootElement extends BranchElement {
        BidiRootElement() {
            super(null, null);
        }

        @Override // javax.swing.text.AbstractDocument.BranchElement, javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public String getName() {
            return "bidi root";
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$BidiElement.class */
    class BidiElement extends LeafElement {
        BidiElement(Element element, int i2, int i3, int i4) {
            super(element, new SimpleAttributeSet(), i2, i3);
            addAttribute(StyleConstants.BidiLevel, Integer.valueOf(i4));
        }

        @Override // javax.swing.text.AbstractDocument.LeafElement, javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public String getName() {
            return AbstractDocument.BidiElementName;
        }

        int getLevel() {
            Integer num = (Integer) getAttribute(StyleConstants.BidiLevel);
            if (num != null) {
                return num.intValue();
            }
            return 0;
        }

        boolean isLeftToRight() {
            return getLevel() % 2 == 0;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$DefaultDocumentEvent.class */
    public class DefaultDocumentEvent extends CompoundEdit implements DocumentEvent {
        private int offset;
        private int length;
        private Hashtable<Element, DocumentEvent.ElementChange> changeLookup;
        private DocumentEvent.EventType type;

        public DefaultDocumentEvent(int i2, int i3, DocumentEvent.EventType eventType) {
            this.offset = i2;
            this.length = i3;
            this.type = eventType;
        }

        @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit
        public String toString() {
            return this.edits.toString();
        }

        @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public boolean addEdit(UndoableEdit undoableEdit) {
            if (this.changeLookup == null && this.edits.size() > 10) {
                this.changeLookup = new Hashtable<>();
                int size = this.edits.size();
                for (int i2 = 0; i2 < size; i2++) {
                    UndoableEdit undoableEditElementAt = this.edits.elementAt(i2);
                    if (undoableEditElementAt instanceof DocumentEvent.ElementChange) {
                        DocumentEvent.ElementChange elementChange = (DocumentEvent.ElementChange) undoableEditElementAt;
                        this.changeLookup.put(elementChange.getElement(), elementChange);
                    }
                }
            }
            if (this.changeLookup != null && (undoableEdit instanceof DocumentEvent.ElementChange)) {
                DocumentEvent.ElementChange elementChange2 = (DocumentEvent.ElementChange) undoableEdit;
                this.changeLookup.put(elementChange2.getElement(), elementChange2);
            }
            return super.addEdit(undoableEdit);
        }

        @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void redo() throws CannotRedoException {
            AbstractDocument.this.writeLock();
            try {
                super.redo();
                UndoRedoDocumentEvent undoRedoDocumentEvent = AbstractDocument.this.new UndoRedoDocumentEvent(this, false);
                if (this.type == DocumentEvent.EventType.INSERT) {
                    AbstractDocument.this.fireInsertUpdate(undoRedoDocumentEvent);
                } else if (this.type == DocumentEvent.EventType.REMOVE) {
                    AbstractDocument.this.fireRemoveUpdate(undoRedoDocumentEvent);
                } else {
                    AbstractDocument.this.fireChangedUpdate(undoRedoDocumentEvent);
                }
            } finally {
                AbstractDocument.this.writeUnlock();
            }
        }

        @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void undo() throws CannotUndoException {
            AbstractDocument.this.writeLock();
            try {
                super.undo();
                UndoRedoDocumentEvent undoRedoDocumentEvent = AbstractDocument.this.new UndoRedoDocumentEvent(this, true);
                if (this.type == DocumentEvent.EventType.REMOVE) {
                    AbstractDocument.this.fireInsertUpdate(undoRedoDocumentEvent);
                } else if (this.type == DocumentEvent.EventType.INSERT) {
                    AbstractDocument.this.fireRemoveUpdate(undoRedoDocumentEvent);
                } else {
                    AbstractDocument.this.fireChangedUpdate(undoRedoDocumentEvent);
                }
            } finally {
                AbstractDocument.this.writeUnlock();
            }
        }

        @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public boolean isSignificant() {
            return true;
        }

        @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public String getPresentationName() {
            DocumentEvent.EventType type = getType();
            if (type == DocumentEvent.EventType.INSERT) {
                return UIManager.getString("AbstractDocument.additionText");
            }
            if (type == DocumentEvent.EventType.REMOVE) {
                return UIManager.getString("AbstractDocument.deletionText");
            }
            return UIManager.getString("AbstractDocument.styleChangeText");
        }

        @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public String getUndoPresentationName() {
            return UIManager.getString("AbstractDocument.undoText") + " " + getPresentationName();
        }

        @Override // javax.swing.undo.CompoundEdit, javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public String getRedoPresentationName() {
            return UIManager.getString("AbstractDocument.redoText") + " " + getPresentationName();
        }

        @Override // javax.swing.event.DocumentEvent
        public DocumentEvent.EventType getType() {
            return this.type;
        }

        @Override // javax.swing.event.DocumentEvent
        public int getOffset() {
            return this.offset;
        }

        @Override // javax.swing.event.DocumentEvent
        public int getLength() {
            return this.length;
        }

        @Override // javax.swing.event.DocumentEvent
        public Document getDocument() {
            return AbstractDocument.this;
        }

        @Override // javax.swing.event.DocumentEvent
        public DocumentEvent.ElementChange getChange(Element element) {
            if (this.changeLookup != null) {
                return this.changeLookup.get(element);
            }
            int size = this.edits.size();
            for (int i2 = 0; i2 < size; i2++) {
                UndoableEdit undoableEditElementAt = this.edits.elementAt(i2);
                if (undoableEditElementAt instanceof DocumentEvent.ElementChange) {
                    DocumentEvent.ElementChange elementChange = (DocumentEvent.ElementChange) undoableEditElementAt;
                    if (element.equals(elementChange.getElement())) {
                        return elementChange;
                    }
                }
            }
            return null;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$UndoRedoDocumentEvent.class */
    class UndoRedoDocumentEvent implements DocumentEvent {
        private DefaultDocumentEvent src;
        private DocumentEvent.EventType type;

        public UndoRedoDocumentEvent(DefaultDocumentEvent defaultDocumentEvent, boolean z2) {
            this.src = null;
            this.type = null;
            this.src = defaultDocumentEvent;
            if (z2) {
                if (defaultDocumentEvent.getType().equals(DocumentEvent.EventType.INSERT)) {
                    this.type = DocumentEvent.EventType.REMOVE;
                    return;
                } else if (defaultDocumentEvent.getType().equals(DocumentEvent.EventType.REMOVE)) {
                    this.type = DocumentEvent.EventType.INSERT;
                    return;
                } else {
                    this.type = defaultDocumentEvent.getType();
                    return;
                }
            }
            this.type = defaultDocumentEvent.getType();
        }

        public DefaultDocumentEvent getSource() {
            return this.src;
        }

        @Override // javax.swing.event.DocumentEvent
        public int getOffset() {
            return this.src.getOffset();
        }

        @Override // javax.swing.event.DocumentEvent
        public int getLength() {
            return this.src.getLength();
        }

        @Override // javax.swing.event.DocumentEvent
        public Document getDocument() {
            return this.src.getDocument();
        }

        @Override // javax.swing.event.DocumentEvent
        public DocumentEvent.EventType getType() {
            return this.type;
        }

        @Override // javax.swing.event.DocumentEvent
        public DocumentEvent.ElementChange getChange(Element element) {
            return this.src.getChange(element);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$ElementEdit.class */
    public static class ElementEdit extends AbstractUndoableEdit implements DocumentEvent.ElementChange {

        /* renamed from: e, reason: collision with root package name */
        private Element f12831e;
        private int index;
        private Element[] removed;
        private Element[] added;

        public ElementEdit(Element element, int i2, Element[] elementArr, Element[] elementArr2) {
            this.f12831e = element;
            this.index = i2;
            this.removed = elementArr;
            this.added = elementArr2;
        }

        @Override // javax.swing.event.DocumentEvent.ElementChange
        public Element getElement() {
            return this.f12831e;
        }

        @Override // javax.swing.event.DocumentEvent.ElementChange
        public int getIndex() {
            return this.index;
        }

        @Override // javax.swing.event.DocumentEvent.ElementChange
        public Element[] getChildrenRemoved() {
            return this.removed;
        }

        @Override // javax.swing.event.DocumentEvent.ElementChange
        public Element[] getChildrenAdded() {
            return this.added;
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void redo() throws CannotRedoException {
            super.redo();
            Element[] elementArr = this.removed;
            this.removed = this.added;
            this.added = elementArr;
            ((BranchElement) this.f12831e).replace(this.index, this.removed.length, this.added);
        }

        @Override // javax.swing.undo.AbstractUndoableEdit, javax.swing.undo.UndoableEdit
        public void undo() throws CannotUndoException {
            super.undo();
            ((BranchElement) this.f12831e).replace(this.index, this.added.length, this.removed);
            Element[] elementArr = this.removed;
            this.removed = this.added;
            this.added = elementArr;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/AbstractDocument$DefaultFilterBypass.class */
    private class DefaultFilterBypass extends DocumentFilter.FilterBypass {
        private DefaultFilterBypass() {
        }

        @Override // javax.swing.text.DocumentFilter.FilterBypass
        public Document getDocument() {
            return AbstractDocument.this;
        }

        @Override // javax.swing.text.DocumentFilter.FilterBypass
        public void remove(int i2, int i3) throws BadLocationException {
            AbstractDocument.this.handleRemove(i2, i3);
        }

        @Override // javax.swing.text.DocumentFilter.FilterBypass
        public void insertString(int i2, String str, AttributeSet attributeSet) throws BadLocationException {
            AbstractDocument.this.handleInsertString(i2, str, attributeSet);
        }

        @Override // javax.swing.text.DocumentFilter.FilterBypass
        public void replace(int i2, int i3, String str, AttributeSet attributeSet) throws BadLocationException {
            AbstractDocument.this.handleRemove(i2, i3);
            AbstractDocument.this.handleInsertString(i2, str, attributeSet);
        }
    }
}
