package javax.swing.text;

import java.util.Vector;
import javax.swing.text.AbstractDocument;

/* loaded from: rt.jar:javax/swing/text/PlainDocument.class */
public class PlainDocument extends AbstractDocument {
    public static final String tabSizeAttribute = "tabSize";
    public static final String lineLimitAttribute = "lineLimit";
    private AbstractDocument.AbstractElement defaultRoot;
    private Vector<Element> added;
    private Vector<Element> removed;

    /* renamed from: s, reason: collision with root package name */
    private transient Segment f12840s;

    public PlainDocument() {
        this(new GapContent());
    }

    public PlainDocument(AbstractDocument.Content content) {
        super(content);
        this.added = new Vector<>();
        this.removed = new Vector<>();
        putProperty(tabSizeAttribute, 8);
        this.defaultRoot = createDefaultRoot();
    }

    @Override // javax.swing.text.AbstractDocument, javax.swing.text.Document
    public void insertString(int i2, String str, AttributeSet attributeSet) throws BadLocationException {
        Object property = getProperty("filterNewlines");
        if ((property instanceof Boolean) && property.equals(Boolean.TRUE) && str != null && str.indexOf(10) >= 0) {
            StringBuilder sb = new StringBuilder(str);
            int length = sb.length();
            for (int i3 = 0; i3 < length; i3++) {
                if (sb.charAt(i3) == '\n') {
                    sb.setCharAt(i3, ' ');
                }
            }
            str = sb.toString();
        }
        super.insertString(i2, str, attributeSet);
    }

    @Override // javax.swing.text.AbstractDocument, javax.swing.text.Document
    public Element getDefaultRootElement() {
        return this.defaultRoot;
    }

    protected AbstractDocument.AbstractElement createDefaultRoot() {
        AbstractDocument.BranchElement branchElement = (AbstractDocument.BranchElement) createBranchElement(null, null);
        branchElement.replace(0, 0, new Element[]{createLeafElement(branchElement, null, 0, 1)});
        return branchElement;
    }

    @Override // javax.swing.text.AbstractDocument, javax.swing.text.StyledDocument
    public Element getParagraphElement(int i2) {
        Element defaultRootElement = getDefaultRootElement();
        return defaultRootElement.getElement(defaultRootElement.getElementIndex(i2));
    }

    @Override // javax.swing.text.AbstractDocument
    protected void insertUpdate(AbstractDocument.DefaultDocumentEvent defaultDocumentEvent, AttributeSet attributeSet) {
        this.removed.removeAllElements();
        this.added.removeAllElements();
        AbstractDocument.BranchElement branchElement = (AbstractDocument.BranchElement) getDefaultRootElement();
        int offset = defaultDocumentEvent.getOffset();
        int length = defaultDocumentEvent.getLength();
        if (offset > 0) {
            offset--;
            length++;
        }
        int elementIndex = branchElement.getElementIndex(offset);
        Element element = branchElement.getElement(elementIndex);
        int startOffset = element.getStartOffset();
        int endOffset = element.getEndOffset();
        int i2 = startOffset;
        try {
            if (this.f12840s == null) {
                this.f12840s = new Segment();
            }
            getContent().getChars(offset, length, this.f12840s);
            boolean z2 = false;
            for (int i3 = 0; i3 < length; i3++) {
                if (this.f12840s.array[this.f12840s.offset + i3] == '\n') {
                    int i4 = offset + i3 + 1;
                    this.added.addElement(createLeafElement(branchElement, null, i2, i4));
                    i2 = i4;
                    z2 = true;
                }
            }
            if (z2) {
                this.removed.addElement(element);
                if (offset + length == endOffset && i2 != endOffset && elementIndex + 1 < branchElement.getElementCount()) {
                    Element element2 = branchElement.getElement(elementIndex + 1);
                    this.removed.addElement(element2);
                    endOffset = element2.getEndOffset();
                }
                if (i2 < endOffset) {
                    this.added.addElement(createLeafElement(branchElement, null, i2, endOffset));
                }
                Element[] elementArr = new Element[this.added.size()];
                this.added.copyInto(elementArr);
                Element[] elementArr2 = new Element[this.removed.size()];
                this.removed.copyInto(elementArr2);
                defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(branchElement, elementIndex, elementArr2, elementArr));
                branchElement.replace(elementIndex, elementArr2.length, elementArr);
            }
            if (Utilities.isComposedTextAttributeDefined(attributeSet)) {
                insertComposedTextUpdate(defaultDocumentEvent, attributeSet);
            }
            super.insertUpdate(defaultDocumentEvent, attributeSet);
        } catch (BadLocationException e2) {
            throw new Error("Internal error: " + e2.toString());
        }
    }

    @Override // javax.swing.text.AbstractDocument
    protected void removeUpdate(AbstractDocument.DefaultDocumentEvent defaultDocumentEvent) {
        this.removed.removeAllElements();
        AbstractDocument.BranchElement branchElement = (AbstractDocument.BranchElement) getDefaultRootElement();
        int offset = defaultDocumentEvent.getOffset();
        int length = defaultDocumentEvent.getLength();
        int elementIndex = branchElement.getElementIndex(offset);
        int elementIndex2 = branchElement.getElementIndex(offset + length);
        if (elementIndex != elementIndex2) {
            for (int i2 = elementIndex; i2 <= elementIndex2; i2++) {
                this.removed.addElement(branchElement.getElement(i2));
            }
            Element[] elementArr = {createLeafElement(branchElement, null, branchElement.getElement(elementIndex).getStartOffset(), branchElement.getElement(elementIndex2).getEndOffset())};
            Element[] elementArr2 = new Element[this.removed.size()];
            this.removed.copyInto(elementArr2);
            defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(branchElement, elementIndex, elementArr2, elementArr));
            branchElement.replace(elementIndex, elementArr2.length, elementArr);
        } else {
            Element element = branchElement.getElement(elementIndex);
            if (!element.isLeaf() && Utilities.isComposedTextElement(element.getElement(element.getElementIndex(offset)))) {
                Element[] elementArr3 = {createLeafElement(branchElement, null, element.getStartOffset(), element.getEndOffset())};
                defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(branchElement, elementIndex, new Element[]{element}, elementArr3));
                branchElement.replace(elementIndex, 1, elementArr3);
            }
        }
        super.removeUpdate(defaultDocumentEvent);
    }

    private void insertComposedTextUpdate(AbstractDocument.DefaultDocumentEvent defaultDocumentEvent, AttributeSet attributeSet) {
        this.added.removeAllElements();
        AbstractDocument.BranchElement branchElement = (AbstractDocument.BranchElement) getDefaultRootElement();
        int offset = defaultDocumentEvent.getOffset();
        int length = defaultDocumentEvent.getLength();
        int elementIndex = branchElement.getElementIndex(offset);
        Element element = branchElement.getElement(elementIndex);
        int startOffset = element.getStartOffset();
        int endOffset = element.getEndOffset();
        AbstractDocument.BranchElement[] branchElementArr = {(AbstractDocument.BranchElement) createBranchElement(branchElement, null)};
        Element[] elementArr = {element};
        if (startOffset != offset) {
            this.added.addElement(createLeafElement(branchElementArr[0], null, startOffset, offset));
        }
        this.added.addElement(createLeafElement(branchElementArr[0], attributeSet, offset, offset + length));
        if (endOffset != offset + length) {
            this.added.addElement(createLeafElement(branchElementArr[0], null, offset + length, endOffset));
        }
        Element[] elementArr2 = new Element[this.added.size()];
        this.added.copyInto(elementArr2);
        defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(branchElement, elementIndex, elementArr, branchElementArr));
        branchElementArr[0].replace(0, 0, elementArr2);
        branchElement.replace(elementIndex, 1, branchElementArr);
    }
}
