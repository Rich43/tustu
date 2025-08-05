package javax.swing.text.html;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.swing.ButtonGroup;
import javax.swing.DefaultButtonModel;
import javax.swing.JToggleButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.GapContent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoableEdit;
import org.icepdf.core.util.PdfOps;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/html/HTMLDocument.class */
public class HTMLDocument extends DefaultStyledDocument {
    private boolean frameDocument;
    private boolean preservesUnknownTags;
    private HashMap<String, ButtonGroup> radioButtonGroupsMap;
    static final String TokenThreshold = "token threshold";
    private static final int MaxThreshold = 10000;
    private static final int StepThreshold = 5;
    public static final String AdditionalComments = "AdditionalComments";
    static final String StyleType = "StyleType";
    URL base;
    boolean hasBaseTag;
    private String baseTarget;
    private HTMLEditorKit.Parser parser;
    private static char[] NEWLINE;
    private boolean insertInBody;
    private static final String I18NProperty = "i18n";
    static String MAP_PROPERTY = "__MAP__";
    private static AttributeSet contentAttributeSet = new SimpleAttributeSet();

    /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$Iterator.class */
    public static abstract class Iterator {
        public abstract AttributeSet getAttributes();

        public abstract int getStartOffset();

        public abstract int getEndOffset();

        public abstract void next();

        public abstract boolean isValid();

        public abstract HTML.Tag getTag();
    }

    public HTMLDocument() {
        this(new GapContent(4096), new StyleSheet());
    }

    public HTMLDocument(StyleSheet styleSheet) {
        this(new GapContent(4096), styleSheet);
    }

    public HTMLDocument(AbstractDocument.Content content, StyleSheet styleSheet) {
        super(content, styleSheet);
        this.frameDocument = false;
        this.preservesUnknownTags = true;
        this.hasBaseTag = false;
        this.baseTarget = null;
        this.insertInBody = false;
    }

    public HTMLEditorKit.ParserCallback getReader(int i2) {
        Object property = getProperty(Document.StreamDescriptionProperty);
        if (property instanceof URL) {
            setBase((URL) property);
        }
        return new HTMLReader(this, i2);
    }

    public HTMLEditorKit.ParserCallback getReader(int i2, int i3, int i4, HTML.Tag tag) {
        return getReader(i2, i3, i4, tag, true);
    }

    HTMLEditorKit.ParserCallback getReader(int i2, int i3, int i4, HTML.Tag tag, boolean z2) {
        Object property = getProperty(Document.StreamDescriptionProperty);
        if (property instanceof URL) {
            setBase((URL) property);
        }
        return new HTMLReader(i2, i3, i4, tag, z2, false, true);
    }

    public URL getBase() {
        return this.base;
    }

    public void setBase(URL url) {
        this.base = url;
        getStyleSheet().setBase(url);
    }

    @Override // javax.swing.text.DefaultStyledDocument
    protected void insert(int i2, DefaultStyledDocument.ElementSpec[] elementSpecArr) throws BadLocationException {
        super.insert(i2, elementSpecArr);
    }

    @Override // javax.swing.text.DefaultStyledDocument, javax.swing.text.AbstractDocument
    protected void insertUpdate(AbstractDocument.DefaultDocumentEvent defaultDocumentEvent, AttributeSet attributeSet) {
        if (attributeSet == null) {
            attributeSet = contentAttributeSet;
        } else if (attributeSet.isDefined(StyleConstants.ComposedTextAttribute)) {
            ((MutableAttributeSet) attributeSet).addAttributes(contentAttributeSet);
        }
        if (attributeSet.isDefined(SwingUtilities2.IMPLIED_CR)) {
            ((MutableAttributeSet) attributeSet).removeAttribute(SwingUtilities2.IMPLIED_CR);
        }
        super.insertUpdate(defaultDocumentEvent, attributeSet);
    }

    @Override // javax.swing.text.DefaultStyledDocument
    protected void create(DefaultStyledDocument.ElementSpec[] elementSpecArr) {
        super.create(elementSpecArr);
    }

    @Override // javax.swing.text.DefaultStyledDocument, javax.swing.text.StyledDocument
    public void setParagraphAttributes(int i2, int i3, AttributeSet attributeSet, boolean z2) {
        try {
            writeLock();
            int iMin = Math.min(i2 + i3, getLength());
            int startOffset = getParagraphElement(i2).getStartOffset();
            AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(startOffset, Math.max(0, getParagraphElement(iMin).getEndOffset() - startOffset), DocumentEvent.EventType.CHANGE);
            AttributeSet attributeSetCopyAttributes = attributeSet.copyAttributes();
            int endOffset = Integer.MAX_VALUE;
            int i4 = startOffset;
            while (i4 <= iMin) {
                Element paragraphElement = getParagraphElement(i4);
                if (endOffset == paragraphElement.getEndOffset()) {
                    endOffset++;
                } else {
                    endOffset = paragraphElement.getEndOffset();
                }
                MutableAttributeSet mutableAttributeSet = (MutableAttributeSet) paragraphElement.getAttributes();
                defaultDocumentEvent.addEdit(new DefaultStyledDocument.AttributeUndoableEdit(paragraphElement, attributeSetCopyAttributes, z2));
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

    public StyleSheet getStyleSheet() {
        return (StyleSheet) getAttributeContext();
    }

    public Iterator getIterator(HTML.Tag tag) {
        if (tag.isBlock()) {
            return null;
        }
        return new LeafIterator(tag, this);
    }

    @Override // javax.swing.text.AbstractDocument
    protected Element createLeafElement(Element element, AttributeSet attributeSet, int i2, int i3) {
        return new RunElement(element, attributeSet, i2, i3);
    }

    @Override // javax.swing.text.AbstractDocument
    protected Element createBranchElement(Element element, AttributeSet attributeSet) {
        return new BlockElement(element, attributeSet);
    }

    @Override // javax.swing.text.DefaultStyledDocument
    protected AbstractDocument.AbstractElement createDefaultRoot() {
        writeLock();
        MutableAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        simpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.HTML);
        BlockElement blockElement = new BlockElement(null, simpleAttributeSet.copyAttributes());
        simpleAttributeSet.removeAttributes(simpleAttributeSet);
        simpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.BODY);
        BlockElement blockElement2 = new BlockElement(blockElement, simpleAttributeSet.copyAttributes());
        simpleAttributeSet.removeAttributes(simpleAttributeSet);
        simpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.f12849P);
        getStyleSheet().addCSSAttributeFromHTML(simpleAttributeSet, CSS.Attribute.MARGIN_TOP, "0");
        BlockElement blockElement3 = new BlockElement(blockElement2, simpleAttributeSet.copyAttributes());
        simpleAttributeSet.removeAttributes(simpleAttributeSet);
        simpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
        Element[] elementArr = {new RunElement(blockElement3, simpleAttributeSet, 0, 1)};
        blockElement3.replace(0, 0, elementArr);
        elementArr[0] = blockElement3;
        blockElement2.replace(0, 0, elementArr);
        elementArr[0] = blockElement2;
        blockElement.replace(0, 0, elementArr);
        writeUnlock();
        return blockElement;
    }

    public void setTokenThreshold(int i2) {
        putProperty(TokenThreshold, new Integer(i2));
    }

    public int getTokenThreshold() {
        Integer num = (Integer) getProperty(TokenThreshold);
        if (num != null) {
            return num.intValue();
        }
        return Integer.MAX_VALUE;
    }

    public void setPreservesUnknownTags(boolean z2) {
        this.preservesUnknownTags = z2;
    }

    public boolean getPreservesUnknownTags() {
        return this.preservesUnknownTags;
    }

    public void processHTMLFrameHyperlinkEvent(HTMLFrameHyperlinkEvent hTMLFrameHyperlinkEvent) {
        String target = hTMLFrameHyperlinkEvent.getTarget();
        Element sourceElement = hTMLFrameHyperlinkEvent.getSourceElement();
        String string = hTMLFrameHyperlinkEvent.getURL().toString();
        if (target.equals("_self")) {
            updateFrame(sourceElement, string);
            return;
        }
        if (target.equals("_parent")) {
            updateFrameSet(sourceElement.getParentElement(), string);
            return;
        }
        Element elementFindFrame = findFrame(target);
        if (elementFindFrame != null) {
            updateFrame(elementFindFrame, string);
        }
    }

    private Element findFrame(String str) {
        Element next;
        String str2;
        ElementIterator elementIterator = new ElementIterator(this);
        while (true) {
            next = elementIterator.next();
            if (next == null) {
                break;
            }
            AttributeSet attributes = next.getAttributes();
            if (matchNameAttribute(attributes, HTML.Tag.FRAME) && (str2 = (String) attributes.getAttribute(HTML.Attribute.NAME)) != null && str2.equals(str)) {
                break;
            }
        }
        return next;
    }

    static boolean matchNameAttribute(AttributeSet attributeSet, HTML.Tag tag) {
        Object attribute = attributeSet.getAttribute(StyleConstants.NameAttribute);
        if ((attribute instanceof HTML.Tag) && ((HTML.Tag) attribute) == tag) {
            return true;
        }
        return false;
    }

    private void updateFrameSet(Element element, String str) {
        try {
            element.getStartOffset();
            Math.min(getLength(), element.getEndOffset());
            String str2 = "<frame";
            if (str != null) {
                str2 = str2 + " src=\"" + str + PdfOps.DOUBLE_QUOTE__TOKEN;
            }
            installParserIfNecessary();
            setOuterHTML(element, str2 + ">");
        } catch (IOException e2) {
        } catch (BadLocationException e3) {
        }
    }

    private void updateFrame(Element element, String str) {
        try {
            writeLock();
            AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(element.getStartOffset(), 1, DocumentEvent.EventType.CHANGE);
            AttributeSet attributeSetCopyAttributes = element.getAttributes().copyAttributes();
            MutableAttributeSet mutableAttributeSet = (MutableAttributeSet) element.getAttributes();
            defaultDocumentEvent.addEdit(new DefaultStyledDocument.AttributeUndoableEdit(element, attributeSetCopyAttributes, false));
            mutableAttributeSet.removeAttribute(HTML.Attribute.SRC);
            mutableAttributeSet.addAttribute(HTML.Attribute.SRC, str);
            defaultDocumentEvent.end();
            fireChangedUpdate(defaultDocumentEvent);
            fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
            writeUnlock();
        } catch (Throwable th) {
            writeUnlock();
            throw th;
        }
    }

    boolean isFrameDocument() {
        return this.frameDocument;
    }

    void setFrameDocumentState(boolean z2) {
        this.frameDocument = z2;
    }

    void addMap(Map map) {
        String name = map.getName();
        if (name != null) {
            Object property = getProperty(MAP_PROPERTY);
            if (property == null) {
                property = new Hashtable(11);
                putProperty(MAP_PROPERTY, property);
            }
            if (property instanceof Hashtable) {
                ((Hashtable) property).put(FXMLLoader.CONTROLLER_METHOD_PREFIX + name, map);
            }
        }
    }

    void removeMap(Map map) {
        String name = map.getName();
        if (name != null) {
            Object property = getProperty(MAP_PROPERTY);
            if (property instanceof Hashtable) {
                ((Hashtable) property).remove(FXMLLoader.CONTROLLER_METHOD_PREFIX + name);
            }
        }
    }

    Map getMap(String str) {
        Object property;
        if (str != null && (property = getProperty(MAP_PROPERTY)) != null && (property instanceof Hashtable)) {
            return (Map) ((Hashtable) property).get(str);
        }
        return null;
    }

    Enumeration getMaps() {
        Object property = getProperty(MAP_PROPERTY);
        if (property instanceof Hashtable) {
            return ((Hashtable) property).elements();
        }
        return null;
    }

    void setDefaultStyleSheetType(String str) {
        putProperty(StyleType, str);
    }

    String getDefaultStyleSheetType() {
        String str = (String) getProperty(StyleType);
        if (str == null) {
            return "text/css";
        }
        return str;
    }

    public void setParser(HTMLEditorKit.Parser parser) {
        this.parser = parser;
        putProperty("__PARSER__", null);
    }

    public HTMLEditorKit.Parser getParser() {
        Object property = getProperty("__PARSER__");
        if (property instanceof HTMLEditorKit.Parser) {
            return (HTMLEditorKit.Parser) property;
        }
        return this.parser;
    }

    public void setInnerHTML(Element element, String str) throws IOException, BadLocationException {
        verifyParser();
        if (element != null && element.isLeaf()) {
            throw new IllegalArgumentException("Can not set inner HTML of a leaf");
        }
        if (element != null && str != null) {
            int elementCount = element.getElementCount();
            element.getStartOffset();
            insertHTML(element, element.getStartOffset(), str, true);
            if (element.getElementCount() > elementCount) {
                removeElements(element, element.getElementCount() - elementCount, elementCount);
            }
        }
    }

    public void setOuterHTML(Element element, String str) throws IOException, BadLocationException {
        verifyParser();
        if (element != null && element.getParentElement() != null && str != null) {
            int startOffset = element.getStartOffset();
            int endOffset = element.getEndOffset();
            int length = getLength();
            boolean z2 = !element.isLeaf();
            if (!z2 && (endOffset > length || getText(endOffset - 1, 1).charAt(0) == NEWLINE[0])) {
                z2 = true;
            }
            Element parentElement = element.getParentElement();
            int elementCount = parentElement.getElementCount();
            insertHTML(parentElement, startOffset, str, z2);
            int length2 = getLength();
            if (elementCount != parentElement.getElementCount()) {
                removeElements(parentElement, parentElement.getElementIndex((startOffset + length2) - length), 1);
            }
        }
    }

    public void insertAfterStart(Element element, String str) throws IOException, BadLocationException {
        verifyParser();
        if (element == null || str == null) {
            return;
        }
        if (element.isLeaf()) {
            throw new IllegalArgumentException("Can not insert HTML after start of a leaf");
        }
        insertHTML(element, element.getStartOffset(), str, false);
    }

    public void insertBeforeEnd(Element element, String str) throws IOException, BadLocationException {
        verifyParser();
        if (element != null && element.isLeaf()) {
            throw new IllegalArgumentException("Can not set inner HTML before end of leaf");
        }
        if (element != null) {
            int endOffset = element.getEndOffset();
            if (element.getElement(element.getElementIndex(endOffset - 1)).isLeaf() && getText(endOffset - 1, 1).charAt(0) == NEWLINE[0]) {
                endOffset--;
            }
            insertHTML(element, endOffset, str, false);
        }
    }

    public void insertBeforeStart(Element element, String str) throws IOException, BadLocationException {
        Element parentElement;
        verifyParser();
        if (element != null && (parentElement = element.getParentElement()) != null) {
            insertHTML(parentElement, element.getStartOffset(), str, false);
        }
    }

    public void insertAfterEnd(Element element, String str) throws IOException, BadLocationException {
        Element parentElement;
        verifyParser();
        if (element != null && (parentElement = element.getParentElement()) != null) {
            if (HTML.Tag.BODY.name.equals(parentElement.getName())) {
                this.insertInBody = true;
            }
            int endOffset = element.getEndOffset();
            if (endOffset > getLength() + 1) {
                endOffset--;
            } else if (element.isLeaf() && getText(endOffset - 1, 1).charAt(0) == NEWLINE[0]) {
                endOffset--;
            }
            insertHTML(parentElement, endOffset, str, false);
            if (this.insertInBody) {
                this.insertInBody = false;
            }
        }
    }

    public Element getElement(String str) {
        if (str == null) {
            return null;
        }
        return getElement(getDefaultRootElement(), HTML.Attribute.ID, str, true);
    }

    public Element getElement(Element element, Object obj, Object obj2) {
        return getElement(element, obj, obj2, true);
    }

    private Element getElement(Element element, Object obj, Object obj2, boolean z2) {
        Enumeration<?> attributeNames;
        AttributeSet attributes = element.getAttributes();
        if (attributes != null && attributes.isDefined(obj) && obj2.equals(attributes.getAttribute(obj))) {
            return element;
        }
        if (!element.isLeaf()) {
            int elementCount = element.getElementCount();
            for (int i2 = 0; i2 < elementCount; i2++) {
                Element element2 = getElement(element.getElement(i2), obj, obj2, z2);
                if (element2 != null) {
                    return element2;
                }
            }
            return null;
        }
        if (z2 && attributes != null && (attributeNames = attributes.getAttributeNames()) != null) {
            while (attributeNames.hasMoreElements()) {
                Object objNextElement = attributeNames.nextElement2();
                if ((objNextElement instanceof HTML.Tag) && (attributes.getAttribute(objNextElement) instanceof AttributeSet)) {
                    AttributeSet attributeSet = (AttributeSet) attributes.getAttribute(objNextElement);
                    if (attributeSet.isDefined(obj) && obj2.equals(attributeSet.getAttribute(obj))) {
                        return element;
                    }
                }
            }
            return null;
        }
        return null;
    }

    private void verifyParser() {
        if (getParser() == null) {
            throw new IllegalStateException("No HTMLEditorKit.Parser");
        }
    }

    private void installParserIfNecessary() {
        if (getParser() == null) {
            setParser(new HTMLEditorKit().getParser());
        }
    }

    private void insertHTML(Element element, int i2, String str, boolean z2) throws IOException, BadLocationException {
        HTMLEditorKit.Parser parser;
        if (element != null && str != null && (parser = getParser()) != null) {
            int iMax = Math.max(0, i2 - 1);
            Element characterElement = getCharacterElement(iMax);
            Element parentElement = element;
            int i3 = 0;
            int i4 = 0;
            if (element.getStartOffset() > iMax) {
                while (parentElement != null && parentElement.getStartOffset() > iMax) {
                    parentElement = parentElement.getParentElement();
                    i4++;
                }
                if (parentElement == null) {
                    throw new BadLocationException("No common parent", i2);
                }
            }
            while (characterElement != null && characterElement != parentElement) {
                i3++;
                characterElement = characterElement.getParentElement();
            }
            if (characterElement != null) {
                HTMLReader hTMLReader = new HTMLReader(i2, i3 - 1, i4, null, false, true, z2);
                parser.parse(new StringReader(str), hTMLReader, true);
                hTMLReader.flush();
            }
        }
    }

    private void removeElements(Element element, int i2, int i3) throws BadLocationException {
        writeLock();
        try {
            int startOffset = element.getElement(i2).getStartOffset();
            int endOffset = element.getElement((i2 + i3) - 1).getEndOffset();
            if (endOffset > getLength()) {
                removeElementsAtEnd(element, i2, i3, startOffset, endOffset);
            } else {
                removeElements(element, i2, i3, startOffset, endOffset);
            }
        } finally {
            writeUnlock();
        }
    }

    private void removeElementsAtEnd(Element element, int i2, int i3, int i4, int i5) throws BadLocationException {
        Element element2;
        boolean zIsLeaf = element.getElement(i2 - 1).isLeaf();
        AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(i4 - 1, (i5 - i4) + 1, DocumentEvent.EventType.REMOVE);
        if (zIsLeaf) {
            int i6 = i2 - 1;
            if (getCharacterElement(getLength()).getParentElement() != element) {
                replace(defaultDocumentEvent, element, i6, i3 + 1, i4, i5, true, true);
            } else {
                replace(defaultDocumentEvent, element, i6, i3, i4, i5, true, false);
            }
        } else {
            Element element3 = element.getElement(i2 - 1);
            while (true) {
                element2 = element3;
                if (element2.isLeaf()) {
                    break;
                } else {
                    element3 = element2.getElement(element2.getElementCount() - 1);
                }
            }
            Element parentElement = element2.getParentElement();
            replace(defaultDocumentEvent, element, i2, i3, i4, i5, false, false);
            replace(defaultDocumentEvent, parentElement, parentElement.getElementCount() - 1, 1, i4, i5, true, true);
        }
        postRemoveUpdate(defaultDocumentEvent);
        defaultDocumentEvent.end();
        fireRemoveUpdate(defaultDocumentEvent);
        fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
    }

    private void replace(AbstractDocument.DefaultDocumentEvent defaultDocumentEvent, Element element, int i2, int i3, int i4, int i5, boolean z2, boolean z3) throws BadLocationException {
        Element[] elementArr;
        UndoableEdit undoableEditRemove;
        AttributeSet attributes = element.getElement(i2).getAttributes();
        Element[] elementArr2 = new Element[i3];
        for (int i6 = 0; i6 < i3; i6++) {
            elementArr2[i6] = element.getElement(i6 + i2);
        }
        if (z2 && (undoableEditRemove = getContent().remove(i4 - 1, i5 - i4)) != null) {
            defaultDocumentEvent.addEdit(undoableEditRemove);
        }
        if (z3) {
            elementArr = new Element[]{createLeafElement(element, attributes, i4 - 1, i4)};
        } else {
            elementArr = new Element[0];
        }
        defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(element, i2, elementArr2, elementArr));
        ((AbstractDocument.BranchElement) element).replace(i2, elementArr2.length, elementArr);
    }

    private void removeElements(Element element, int i2, int i3, int i4, int i5) throws BadLocationException {
        Element[] elementArr = new Element[i3];
        Element[] elementArr2 = new Element[0];
        for (int i6 = 0; i6 < i3; i6++) {
            elementArr[i6] = element.getElement(i6 + i2);
        }
        AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(i4, i5 - i4, DocumentEvent.EventType.REMOVE);
        ((AbstractDocument.BranchElement) element).replace(i2, elementArr.length, elementArr2);
        defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(element, i2, elementArr, elementArr2));
        UndoableEdit undoableEditRemove = getContent().remove(i4, i5 - i4);
        if (undoableEditRemove != null) {
            defaultDocumentEvent.addEdit(undoableEditRemove);
        }
        postRemoveUpdate(defaultDocumentEvent);
        defaultDocumentEvent.end();
        fireRemoveUpdate(defaultDocumentEvent);
        if (undoableEditRemove != null) {
            fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
        }
    }

    void obtainLock() {
        writeLock();
    }

    void releaseLock() {
        writeUnlock();
    }

    @Override // javax.swing.text.AbstractDocument
    protected void fireChangedUpdate(DocumentEvent documentEvent) {
        super.fireChangedUpdate(documentEvent);
    }

    @Override // javax.swing.text.AbstractDocument
    protected void fireUndoableEditUpdate(UndoableEditEvent undoableEditEvent) {
        super.fireUndoableEditUpdate(undoableEditEvent);
    }

    boolean hasBaseTag() {
        return this.hasBaseTag;
    }

    String getBaseTarget() {
        return this.baseTarget;
    }

    static {
        ((MutableAttributeSet) contentAttributeSet).addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
        NEWLINE = new char[1];
        NEWLINE[0] = '\n';
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$LeafIterator.class */
    static class LeafIterator extends Iterator {
        private int endOffset = 0;
        private HTML.Tag tag;
        private ElementIterator pos;

        LeafIterator(HTML.Tag tag, Document document) {
            this.tag = tag;
            this.pos = new ElementIterator(document);
            next();
        }

        @Override // javax.swing.text.html.HTMLDocument.Iterator
        public AttributeSet getAttributes() {
            Element elementCurrent = this.pos.current();
            if (elementCurrent != null) {
                AttributeSet attributes = (AttributeSet) elementCurrent.getAttributes().getAttribute(this.tag);
                if (attributes == null) {
                    attributes = elementCurrent.getAttributes();
                }
                return attributes;
            }
            return null;
        }

        @Override // javax.swing.text.html.HTMLDocument.Iterator
        public int getStartOffset() {
            Element elementCurrent = this.pos.current();
            if (elementCurrent != null) {
                return elementCurrent.getStartOffset();
            }
            return -1;
        }

        @Override // javax.swing.text.html.HTMLDocument.Iterator
        public int getEndOffset() {
            return this.endOffset;
        }

        @Override // javax.swing.text.html.HTMLDocument.Iterator
        public void next() {
            nextLeaf(this.pos);
            while (isValid()) {
                if (this.pos.current().getStartOffset() >= this.endOffset) {
                    AttributeSet attributes = this.pos.current().getAttributes();
                    if (attributes.isDefined(this.tag) || attributes.getAttribute(StyleConstants.NameAttribute) == this.tag) {
                        setEndOffset();
                        return;
                    }
                }
                nextLeaf(this.pos);
            }
        }

        @Override // javax.swing.text.html.HTMLDocument.Iterator
        public HTML.Tag getTag() {
            return this.tag;
        }

        @Override // javax.swing.text.html.HTMLDocument.Iterator
        public boolean isValid() {
            return this.pos.current() != null;
        }

        void nextLeaf(ElementIterator elementIterator) {
            elementIterator.next();
            while (elementIterator.current() != null && !elementIterator.current().isLeaf()) {
                elementIterator.next();
            }
        }

        void setEndOffset() {
            AttributeSet attributes = getAttributes();
            this.endOffset = this.pos.current().getEndOffset();
            ElementIterator elementIterator = (ElementIterator) this.pos.clone();
            nextLeaf(elementIterator);
            while (elementIterator.current() != null) {
                Element elementCurrent = elementIterator.current();
                AttributeSet attributeSet = (AttributeSet) elementCurrent.getAttributes().getAttribute(this.tag);
                if (attributeSet != null && attributeSet.equals(attributes)) {
                    this.endOffset = elementCurrent.getEndOffset();
                    nextLeaf(elementIterator);
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader.class */
    public class HTMLReader extends HTMLEditorKit.ParserCallback {
        private boolean receivedEndHTML;
        private int flushCount;
        private boolean insertAfterImplied;
        private boolean wantsTrailingNewline;
        int threshold;
        int offset;
        boolean inParagraph;
        boolean impliedP;
        boolean inPre;
        boolean inTextArea;
        TextAreaDocument textAreaDocument;
        boolean inTitle;
        boolean lastWasNewline;
        boolean emptyAnchor;
        boolean midInsert;
        boolean inBody;
        HTML.Tag insertTag;
        boolean insertInsertTag;
        boolean foundInsertTag;
        int insertTagDepthDelta;
        int popDepth;
        int pushDepth;
        Map lastMap;
        boolean inStyle;
        String defaultStyle;
        Vector<Object> styles;
        boolean inHead;
        boolean isStyleCSS;
        boolean emptyDocument;
        AttributeSet styleAttributes;
        Option option;
        protected Vector<DefaultStyledDocument.ElementSpec> parseBuffer;
        protected MutableAttributeSet charAttr;
        Stack<AttributeSet> charAttrStack;
        Hashtable<HTML.Tag, TagAction> tagMap;
        int inBlock;
        private HTML.Tag nextTagAfterPImplied;

        public HTMLReader(HTMLDocument hTMLDocument, int i2) {
            this(hTMLDocument, i2, 0, 0, null);
        }

        public HTMLReader(HTMLDocument hTMLDocument, int i2, int i3, int i4, HTML.Tag tag) {
            this(i2, i3, i4, tag, true, false, true);
        }

        HTMLReader(int i2, int i3, int i4, HTML.Tag tag, boolean z2, boolean z3, boolean z4) {
            HTML.Tag tag2;
            this.inParagraph = false;
            this.impliedP = false;
            this.inPre = false;
            this.inTextArea = false;
            this.textAreaDocument = null;
            this.inTitle = false;
            this.lastWasNewline = true;
            this.inStyle = false;
            this.inHead = false;
            this.parseBuffer = new Vector<>();
            this.charAttr = new TaggedAttributeSet();
            this.charAttrStack = new Stack<>();
            this.inBlock = 0;
            this.nextTagAfterPImplied = null;
            this.emptyDocument = HTMLDocument.this.getLength() == 0;
            this.isStyleCSS = "text/css".equals(HTMLDocument.this.getDefaultStyleSheetType());
            this.offset = i2;
            this.threshold = HTMLDocument.this.getTokenThreshold();
            this.tagMap = new Hashtable<>(57);
            new TagAction();
            BlockAction blockAction = new BlockAction();
            ParagraphAction paragraphAction = new ParagraphAction();
            CharacterAction characterAction = new CharacterAction();
            SpecialAction specialAction = new SpecialAction();
            FormAction formAction = new FormAction();
            HiddenAction hiddenAction = new HiddenAction();
            ConvertAction convertAction = new ConvertAction();
            this.tagMap.put(HTML.Tag.f12846A, new AnchorAction());
            this.tagMap.put(HTML.Tag.ADDRESS, characterAction);
            this.tagMap.put(HTML.Tag.APPLET, hiddenAction);
            this.tagMap.put(HTML.Tag.AREA, new AreaAction());
            this.tagMap.put(HTML.Tag.f12847B, convertAction);
            this.tagMap.put(HTML.Tag.BASE, new BaseAction());
            this.tagMap.put(HTML.Tag.BASEFONT, characterAction);
            this.tagMap.put(HTML.Tag.BIG, characterAction);
            this.tagMap.put(HTML.Tag.BLOCKQUOTE, blockAction);
            this.tagMap.put(HTML.Tag.BODY, blockAction);
            this.tagMap.put(HTML.Tag.BR, specialAction);
            this.tagMap.put(HTML.Tag.CAPTION, blockAction);
            this.tagMap.put(HTML.Tag.CENTER, blockAction);
            this.tagMap.put(HTML.Tag.CITE, characterAction);
            this.tagMap.put(HTML.Tag.CODE, characterAction);
            this.tagMap.put(HTML.Tag.DD, blockAction);
            this.tagMap.put(HTML.Tag.DFN, characterAction);
            this.tagMap.put(HTML.Tag.DIR, blockAction);
            this.tagMap.put(HTML.Tag.DIV, blockAction);
            this.tagMap.put(HTML.Tag.DL, blockAction);
            this.tagMap.put(HTML.Tag.DT, paragraphAction);
            this.tagMap.put(HTML.Tag.EM, characterAction);
            this.tagMap.put(HTML.Tag.FONT, convertAction);
            this.tagMap.put(HTML.Tag.FORM, new FormTagAction());
            this.tagMap.put(HTML.Tag.FRAME, specialAction);
            this.tagMap.put(HTML.Tag.FRAMESET, blockAction);
            this.tagMap.put(HTML.Tag.H1, paragraphAction);
            this.tagMap.put(HTML.Tag.H2, paragraphAction);
            this.tagMap.put(HTML.Tag.H3, paragraphAction);
            this.tagMap.put(HTML.Tag.H4, paragraphAction);
            this.tagMap.put(HTML.Tag.H5, paragraphAction);
            this.tagMap.put(HTML.Tag.H6, paragraphAction);
            this.tagMap.put(HTML.Tag.HEAD, new HeadAction());
            this.tagMap.put(HTML.Tag.HR, specialAction);
            this.tagMap.put(HTML.Tag.HTML, blockAction);
            this.tagMap.put(HTML.Tag.f12848I, convertAction);
            this.tagMap.put(HTML.Tag.IMG, specialAction);
            this.tagMap.put(HTML.Tag.INPUT, formAction);
            this.tagMap.put(HTML.Tag.ISINDEX, new IsindexAction());
            this.tagMap.put(HTML.Tag.KBD, characterAction);
            this.tagMap.put(HTML.Tag.LI, blockAction);
            this.tagMap.put(HTML.Tag.LINK, new LinkAction());
            this.tagMap.put(HTML.Tag.MAP, new MapAction());
            this.tagMap.put(HTML.Tag.MENU, blockAction);
            this.tagMap.put(HTML.Tag.META, new MetaAction());
            this.tagMap.put(HTML.Tag.NOBR, characterAction);
            this.tagMap.put(HTML.Tag.NOFRAMES, blockAction);
            this.tagMap.put(HTML.Tag.OBJECT, specialAction);
            this.tagMap.put(HTML.Tag.OL, blockAction);
            this.tagMap.put(HTML.Tag.OPTION, formAction);
            this.tagMap.put(HTML.Tag.f12849P, paragraphAction);
            this.tagMap.put(HTML.Tag.PARAM, new ObjectAction());
            this.tagMap.put(HTML.Tag.PRE, new PreAction());
            this.tagMap.put(HTML.Tag.SAMP, characterAction);
            this.tagMap.put(HTML.Tag.SCRIPT, hiddenAction);
            this.tagMap.put(HTML.Tag.SELECT, formAction);
            this.tagMap.put(HTML.Tag.SMALL, characterAction);
            this.tagMap.put(HTML.Tag.SPAN, characterAction);
            this.tagMap.put(HTML.Tag.STRIKE, convertAction);
            this.tagMap.put(HTML.Tag.f12850S, characterAction);
            this.tagMap.put(HTML.Tag.STRONG, characterAction);
            this.tagMap.put(HTML.Tag.STYLE, new StyleAction());
            this.tagMap.put(HTML.Tag.SUB, convertAction);
            this.tagMap.put(HTML.Tag.SUP, convertAction);
            this.tagMap.put(HTML.Tag.TABLE, blockAction);
            this.tagMap.put(HTML.Tag.TD, blockAction);
            this.tagMap.put(HTML.Tag.TEXTAREA, formAction);
            this.tagMap.put(HTML.Tag.TH, blockAction);
            this.tagMap.put(HTML.Tag.TITLE, new TitleAction());
            this.tagMap.put(HTML.Tag.TR, blockAction);
            this.tagMap.put(HTML.Tag.TT, characterAction);
            this.tagMap.put(HTML.Tag.f12851U, convertAction);
            this.tagMap.put(HTML.Tag.UL, blockAction);
            this.tagMap.put(HTML.Tag.VAR, characterAction);
            if (tag != null) {
                this.insertTag = tag;
                this.popDepth = i3;
                this.pushDepth = i4;
                this.insertInsertTag = z2;
                this.foundInsertTag = false;
            } else {
                this.foundInsertTag = true;
            }
            if (z3) {
                this.popDepth = i3;
                this.pushDepth = i4;
                this.insertAfterImplied = true;
                this.foundInsertTag = false;
                this.midInsert = false;
                this.insertInsertTag = true;
                this.wantsTrailingNewline = z4;
            } else {
                this.midInsert = !this.emptyDocument && tag == null;
                if (this.midInsert) {
                    generateEndsSpecsForMidInsert();
                }
            }
            if (!this.emptyDocument && !this.midInsert) {
                Element characterElement = HTMLDocument.this.getCharacterElement(Math.max(this.offset - 1, 0));
                for (int i5 = 0; i5 <= this.popDepth; i5++) {
                    characterElement = characterElement.getParentElement();
                }
                for (int i6 = 0; i6 < this.pushDepth; i6++) {
                    characterElement = characterElement.getElement(characterElement.getElementIndex(this.offset));
                }
                AttributeSet attributes = characterElement.getAttributes();
                if (attributes != null && (tag2 = (HTML.Tag) attributes.getAttribute(StyleConstants.NameAttribute)) != null) {
                    this.inParagraph = tag2.isParagraph();
                }
            }
        }

        private void generateEndsSpecsForMidInsert() {
            int iHeightToElementWithName = heightToElementWithName(HTML.Tag.BODY, Math.max(0, this.offset - 1));
            boolean z2 = false;
            if (iHeightToElementWithName == -1 && this.offset > 0) {
                iHeightToElementWithName = heightToElementWithName(HTML.Tag.BODY, this.offset);
                if (iHeightToElementWithName != -1) {
                    iHeightToElementWithName = depthTo(this.offset - 1) - 1;
                    z2 = true;
                }
            }
            if (iHeightToElementWithName == -1) {
                throw new RuntimeException("Must insert new content into body element-");
            }
            if (iHeightToElementWithName != -1) {
                if (!z2) {
                    try {
                        if (this.offset > 0 && !HTMLDocument.this.getText(this.offset - 1, 1).equals("\n")) {
                            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                            simpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
                            this.parseBuffer.addElement(new DefaultStyledDocument.ElementSpec(simpleAttributeSet, (short) 3, HTMLDocument.NEWLINE, 0, 1));
                        }
                    } catch (BadLocationException e2) {
                    }
                }
                while (true) {
                    int i2 = iHeightToElementWithName;
                    iHeightToElementWithName--;
                    if (i2 <= 0) {
                        break;
                    } else {
                        this.parseBuffer.addElement(new DefaultStyledDocument.ElementSpec(null, (short) 2));
                    }
                }
                if (z2) {
                    DefaultStyledDocument.ElementSpec elementSpec = new DefaultStyledDocument.ElementSpec(null, (short) 1);
                    elementSpec.setDirection((short) 5);
                    this.parseBuffer.addElement(elementSpec);
                }
            }
        }

        private int depthTo(int i2) {
            Element defaultRootElement = HTMLDocument.this.getDefaultRootElement();
            int i3 = 0;
            while (!defaultRootElement.isLeaf()) {
                i3++;
                defaultRootElement = defaultRootElement.getElement(defaultRootElement.getElementIndex(i2));
            }
            return i3;
        }

        private int heightToElementWithName(Object obj, int i2) {
            Element parentElement = HTMLDocument.this.getCharacterElement(i2).getParentElement();
            int i3 = 0;
            while (parentElement != null && parentElement.getAttributes().getAttribute(StyleConstants.NameAttribute) != obj) {
                i3++;
                parentElement = parentElement.getParentElement();
            }
            if (parentElement == null) {
                return -1;
            }
            return i3;
        }

        private void adjustEndElement() {
            int length = HTMLDocument.this.getLength();
            if (length == 0) {
                return;
            }
            HTMLDocument.this.obtainLock();
            try {
                Element[] pathTo = getPathTo(length - 1);
                int length2 = pathTo.length;
                if (length2 > 1 && pathTo[1].getAttributes().getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY && pathTo[1].getEndOffset() == length) {
                    String text = HTMLDocument.this.getText(length - 1, 1);
                    Element[] elementArr = new Element[0];
                    int elementIndex = pathTo[0].getElementIndex(length);
                    Element[] elementArr2 = {pathTo[0].getElement(elementIndex)};
                    ((AbstractDocument.BranchElement) pathTo[0]).replace(elementIndex, 1, elementArr);
                    AbstractDocument.ElementEdit elementEdit = new AbstractDocument.ElementEdit(pathTo[0], elementIndex, elementArr2, elementArr);
                    SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                    simpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
                    simpleAttributeSet.addAttribute(SwingUtilities2.IMPLIED_CR, Boolean.TRUE);
                    Element[] elementArr3 = {HTMLDocument.this.createLeafElement(pathTo[length2 - 1], simpleAttributeSet, length, length + 1)};
                    int elementCount = pathTo[length2 - 1].getElementCount();
                    ((AbstractDocument.BranchElement) pathTo[length2 - 1]).replace(elementCount, 0, elementArr3);
                    AbstractDocument.DefaultDocumentEvent defaultDocumentEvent = new AbstractDocument.DefaultDocumentEvent(length, 1, DocumentEvent.EventType.CHANGE);
                    defaultDocumentEvent.addEdit(new AbstractDocument.ElementEdit(pathTo[length2 - 1], elementCount, new Element[0], elementArr3));
                    defaultDocumentEvent.addEdit(elementEdit);
                    defaultDocumentEvent.end();
                    HTMLDocument.this.fireChangedUpdate(defaultDocumentEvent);
                    HTMLDocument.this.fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent));
                    if (text.equals("\n")) {
                        AbstractDocument.DefaultDocumentEvent defaultDocumentEvent2 = new AbstractDocument.DefaultDocumentEvent(length - 1, 1, DocumentEvent.EventType.REMOVE);
                        HTMLDocument.this.removeUpdate(defaultDocumentEvent2);
                        UndoableEdit undoableEditRemove = HTMLDocument.this.getContent().remove(length - 1, 1);
                        if (undoableEditRemove != null) {
                            defaultDocumentEvent2.addEdit(undoableEditRemove);
                        }
                        HTMLDocument.this.postRemoveUpdate(defaultDocumentEvent2);
                        defaultDocumentEvent2.end();
                        HTMLDocument.this.fireRemoveUpdate(defaultDocumentEvent2);
                        HTMLDocument.this.fireUndoableEditUpdate(new UndoableEditEvent(this, defaultDocumentEvent2));
                    }
                }
                HTMLDocument.this.releaseLock();
            } catch (BadLocationException e2) {
                HTMLDocument.this.releaseLock();
            } catch (Throwable th) {
                HTMLDocument.this.releaseLock();
                throw th;
            }
        }

        private Element[] getPathTo(int i2) {
            Stack stack = new Stack();
            Element defaultRootElement = HTMLDocument.this.getDefaultRootElement();
            while (true) {
                Element element = defaultRootElement;
                if (!element.isLeaf()) {
                    stack.push(element);
                    defaultRootElement = element.getElement(element.getElementIndex(i2));
                } else {
                    Element[] elementArr = new Element[stack.size()];
                    stack.copyInto(elementArr);
                    return elementArr;
                }
            }
        }

        @Override // javax.swing.text.html.HTMLEditorKit.ParserCallback
        public void flush() throws BadLocationException {
            if (this.emptyDocument && !this.insertAfterImplied) {
                if (HTMLDocument.this.getLength() > 0 || this.parseBuffer.size() > 0) {
                    flushBuffer(true);
                    adjustEndElement();
                    return;
                }
                return;
            }
            flushBuffer(true);
        }

        @Override // javax.swing.text.html.HTMLEditorKit.ParserCallback
        public void handleText(char[] cArr, int i2) {
            Object property;
            if (this.receivedEndHTML) {
                return;
            }
            if (this.midInsert && !this.inBody) {
                return;
            }
            if (HTMLDocument.this.getProperty(HTMLDocument.I18NProperty).equals(Boolean.FALSE) && (((property = HTMLDocument.this.getProperty(TextAttribute.RUN_DIRECTION)) != null && property.equals(TextAttribute.RUN_DIRECTION_RTL)) || SwingUtilities2.isComplexLayout(cArr, 0, cArr.length))) {
                HTMLDocument.this.putProperty(HTMLDocument.I18NProperty, Boolean.TRUE);
            }
            if (this.inTextArea) {
                textAreaContent(cArr);
                return;
            }
            if (this.inPre) {
                preContent(cArr);
                return;
            }
            if (this.inTitle) {
                HTMLDocument.this.putProperty("title", new String(cArr));
                return;
            }
            if (this.option != null) {
                this.option.setLabel(new String(cArr));
                return;
            }
            if (this.inStyle) {
                if (this.styles != null) {
                    this.styles.addElement(new String(cArr));
                }
            } else if (this.inBlock > 0) {
                if (!this.foundInsertTag && this.insertAfterImplied) {
                    foundInsertTag(false);
                    this.foundInsertTag = true;
                    boolean z2 = !HTMLDocument.this.insertInBody;
                    this.impliedP = z2;
                    this.inParagraph = z2;
                }
                if (cArr.length >= 1) {
                    addContent(cArr, 0, cArr.length);
                }
            }
        }

        @Override // javax.swing.text.html.HTMLEditorKit.ParserCallback
        public void handleStartTag(HTML.Tag tag, MutableAttributeSet mutableAttributeSet, int i2) {
            if (this.receivedEndHTML) {
                return;
            }
            if (this.midInsert && !this.inBody) {
                if (tag == HTML.Tag.BODY) {
                    this.inBody = true;
                    this.inBlock++;
                    return;
                }
                return;
            }
            if (!this.inBody && tag == HTML.Tag.BODY) {
                this.inBody = true;
            }
            if (this.isStyleCSS && mutableAttributeSet.isDefined(HTML.Attribute.STYLE)) {
                String str = (String) mutableAttributeSet.getAttribute(HTML.Attribute.STYLE);
                mutableAttributeSet.removeAttribute(HTML.Attribute.STYLE);
                this.styleAttributes = HTMLDocument.this.getStyleSheet().getDeclaration(str);
                mutableAttributeSet.addAttributes(this.styleAttributes);
            } else {
                this.styleAttributes = null;
            }
            TagAction tagAction = this.tagMap.get(tag);
            if (tagAction != null) {
                tagAction.start(tag, mutableAttributeSet);
            }
        }

        @Override // javax.swing.text.html.HTMLEditorKit.ParserCallback
        public void handleComment(char[] cArr, int i2) {
            if (this.receivedEndHTML) {
                addExternalComment(new String(cArr));
                return;
            }
            if (this.inStyle) {
                if (this.styles != null) {
                    this.styles.addElement(new String(cArr));
                }
            } else if (HTMLDocument.this.getPreservesUnknownTags()) {
                if (this.inBlock == 0 && (this.foundInsertTag || this.insertTag != HTML.Tag.COMMENT)) {
                    addExternalComment(new String(cArr));
                    return;
                } else {
                    SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                    simpleAttributeSet.addAttribute(HTML.Attribute.COMMENT, new String(cArr));
                    addSpecialElement(HTML.Tag.COMMENT, simpleAttributeSet);
                }
            }
            TagAction tagAction = this.tagMap.get(HTML.Tag.COMMENT);
            if (tagAction != null) {
                tagAction.start(HTML.Tag.COMMENT, new SimpleAttributeSet());
                tagAction.end(HTML.Tag.COMMENT);
            }
        }

        private void addExternalComment(String str) {
            Object property = HTMLDocument.this.getProperty(HTMLDocument.AdditionalComments);
            if (property != null && !(property instanceof Vector)) {
                return;
            }
            if (property == null) {
                property = new Vector();
                HTMLDocument.this.putProperty(HTMLDocument.AdditionalComments, property);
            }
            ((Vector) property).addElement(str);
        }

        @Override // javax.swing.text.html.HTMLEditorKit.ParserCallback
        public void handleEndTag(HTML.Tag tag, int i2) {
            if (this.receivedEndHTML) {
                return;
            }
            if (this.midInsert && !this.inBody) {
                return;
            }
            if (tag == HTML.Tag.HTML) {
                this.receivedEndHTML = true;
            }
            if (tag == HTML.Tag.BODY) {
                this.inBody = false;
                if (this.midInsert) {
                    this.inBlock--;
                }
            }
            TagAction tagAction = this.tagMap.get(tag);
            if (tagAction != null) {
                tagAction.end(tag);
            }
        }

        @Override // javax.swing.text.html.HTMLEditorKit.ParserCallback
        public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet mutableAttributeSet, int i2) {
            if (this.receivedEndHTML) {
                return;
            }
            if (this.midInsert && !this.inBody) {
                return;
            }
            if (this.isStyleCSS && mutableAttributeSet.isDefined(HTML.Attribute.STYLE)) {
                String str = (String) mutableAttributeSet.getAttribute(HTML.Attribute.STYLE);
                mutableAttributeSet.removeAttribute(HTML.Attribute.STYLE);
                this.styleAttributes = HTMLDocument.this.getStyleSheet().getDeclaration(str);
                mutableAttributeSet.addAttributes(this.styleAttributes);
            } else {
                this.styleAttributes = null;
            }
            TagAction tagAction = this.tagMap.get(tag);
            if (tagAction != null) {
                tagAction.start(tag, mutableAttributeSet);
                tagAction.end(tag);
            } else if (HTMLDocument.this.getPreservesUnknownTags()) {
                addSpecialElement(tag, mutableAttributeSet);
            }
        }

        @Override // javax.swing.text.html.HTMLEditorKit.ParserCallback
        public void handleEndOfLineString(String str) {
            if (this.emptyDocument && str != null) {
                HTMLDocument.this.putProperty(DefaultEditorKit.EndOfLineStringProperty, str);
            }
        }

        protected void registerTag(HTML.Tag tag, TagAction tagAction) {
            this.tagMap.put(tag, tagAction);
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$TagAction.class */
        public class TagAction {
            public TagAction() {
            }

            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
            }

            public void end(HTML.Tag tag) {
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$BlockAction.class */
        public class BlockAction extends TagAction {
            public BlockAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.blockOpen(tag, mutableAttributeSet);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                HTMLReader.this.blockClose(tag);
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$FormTagAction.class */
        private class FormTagAction extends BlockAction {
            private FormTagAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                super.start(tag, mutableAttributeSet);
                HTMLDocument.this.radioButtonGroupsMap = new HashMap();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                super.end(tag);
                HTMLDocument.this.radioButtonGroupsMap = null;
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$ParagraphAction.class */
        public class ParagraphAction extends BlockAction {
            public ParagraphAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                super.start(tag, mutableAttributeSet);
                HTMLReader.this.inParagraph = true;
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                super.end(tag);
                HTMLReader.this.inParagraph = false;
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$SpecialAction.class */
        public class SpecialAction extends TagAction {
            public SpecialAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.addSpecialElement(tag, mutableAttributeSet);
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$IsindexAction.class */
        public class IsindexAction extends TagAction {
            public IsindexAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.blockOpen(HTML.Tag.IMPLIED, new SimpleAttributeSet());
                HTMLReader.this.addSpecialElement(tag, mutableAttributeSet);
                HTMLReader.this.blockClose(HTML.Tag.IMPLIED);
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$HiddenAction.class */
        public class HiddenAction extends TagAction {
            public HiddenAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.addSpecialElement(tag, mutableAttributeSet);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                if (!isEmpty(tag)) {
                    SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                    simpleAttributeSet.addAttribute(HTML.Attribute.ENDTAG, "true");
                    HTMLReader.this.addSpecialElement(tag, simpleAttributeSet);
                }
            }

            boolean isEmpty(HTML.Tag tag) {
                if (tag == HTML.Tag.APPLET || tag == HTML.Tag.SCRIPT) {
                    return false;
                }
                return true;
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$MetaAction.class */
        class MetaAction extends HiddenAction {
            MetaAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                Object attribute = mutableAttributeSet.getAttribute(HTML.Attribute.HTTPEQUIV);
                if (attribute != null) {
                    String lowerCase = ((String) attribute).toLowerCase();
                    if (lowerCase.equals("content-style-type")) {
                        HTMLDocument.this.setDefaultStyleSheetType((String) mutableAttributeSet.getAttribute(HTML.Attribute.CONTENT));
                        HTMLReader.this.isStyleCSS = "text/css".equals(HTMLDocument.this.getDefaultStyleSheetType());
                    } else if (lowerCase.equals("default-style")) {
                        HTMLReader.this.defaultStyle = (String) mutableAttributeSet.getAttribute(HTML.Attribute.CONTENT);
                    }
                }
                super.start(tag, mutableAttributeSet);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction
            boolean isEmpty(HTML.Tag tag) {
                return true;
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$HeadAction.class */
        class HeadAction extends BlockAction {
            HeadAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.inHead = true;
                if ((HTMLReader.this.insertTag != null || HTMLReader.this.insertAfterImplied) && HTMLReader.this.insertTag != HTML.Tag.HEAD) {
                    if (HTMLReader.this.insertAfterImplied) {
                        if (!HTMLReader.this.foundInsertTag && mutableAttributeSet.isDefined(HTMLEditorKit.ParserCallback.IMPLIED)) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                super.start(tag, mutableAttributeSet);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                HTMLReader hTMLReader = HTMLReader.this;
                HTMLReader.this.inStyle = false;
                hTMLReader.inHead = false;
                if (HTMLReader.this.styles != null) {
                    boolean z2 = HTMLReader.this.isStyleCSS;
                    int i2 = 0;
                    int size = HTMLReader.this.styles.size();
                    while (i2 < size) {
                        if (HTMLReader.this.styles.elementAt(i2) == HTML.Tag.LINK) {
                            int i3 = i2 + 1;
                            handleLink((AttributeSet) HTMLReader.this.styles.elementAt(i3));
                            i2 = i3 + 1;
                        } else {
                            i2++;
                            String str = (String) HTMLReader.this.styles.elementAt(i2);
                            boolean zEquals = str == null ? z2 : str.equals("text/css");
                            while (true) {
                                i2++;
                                if (i2 >= size || !(HTMLReader.this.styles.elementAt(i2) instanceof String)) {
                                    break;
                                } else if (zEquals) {
                                    HTMLReader.this.addCSSRules((String) HTMLReader.this.styles.elementAt(i2));
                                }
                            }
                        }
                    }
                }
                if ((HTMLReader.this.insertTag == null && !HTMLReader.this.insertAfterImplied) || HTMLReader.this.insertTag == HTML.Tag.HEAD || (HTMLReader.this.insertAfterImplied && HTMLReader.this.foundInsertTag)) {
                    super.end(tag);
                }
            }

            boolean isEmpty(HTML.Tag tag) {
                return false;
            }

            private void handleLink(AttributeSet attributeSet) {
                String lowerCase;
                String defaultStyleSheetType = (String) attributeSet.getAttribute(HTML.Attribute.TYPE);
                if (defaultStyleSheetType == null) {
                    defaultStyleSheetType = HTMLDocument.this.getDefaultStyleSheetType();
                }
                if (defaultStyleSheetType.equals("text/css")) {
                    String str = (String) attributeSet.getAttribute(HTML.Attribute.REL);
                    String str2 = (String) attributeSet.getAttribute(HTML.Attribute.TITLE);
                    String str3 = (String) attributeSet.getAttribute(HTML.Attribute.MEDIA);
                    if (str3 == null) {
                        lowerCase = "all";
                    } else {
                        lowerCase = str3.toLowerCase();
                    }
                    if (str != null) {
                        String lowerCase2 = str.toLowerCase();
                        if (lowerCase.indexOf("all") != -1 || lowerCase.indexOf("screen") != -1) {
                            if (lowerCase2.equals(Constants.ELEMNAME_STYLESHEET_STRING) || (lowerCase2.equals("alternate stylesheet") && str2.equals(HTMLReader.this.defaultStyle))) {
                                HTMLReader.this.linkCSSStyleSheet((String) attributeSet.getAttribute(HTML.Attribute.HREF));
                            }
                        }
                    }
                }
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$LinkAction.class */
        class LinkAction extends HiddenAction {
            LinkAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                String str = (String) mutableAttributeSet.getAttribute(HTML.Attribute.REL);
                if (str != null) {
                    String lowerCase = str.toLowerCase();
                    if (lowerCase.equals(Constants.ELEMNAME_STYLESHEET_STRING) || lowerCase.equals("alternate stylesheet")) {
                        if (HTMLReader.this.styles == null) {
                            HTMLReader.this.styles = new Vector<>(3);
                        }
                        HTMLReader.this.styles.addElement(tag);
                        HTMLReader.this.styles.addElement(mutableAttributeSet.copyAttributes());
                    }
                }
                super.start(tag, mutableAttributeSet);
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$MapAction.class */
        class MapAction extends TagAction {
            MapAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.lastMap = new Map((String) mutableAttributeSet.getAttribute(HTML.Attribute.NAME));
                HTMLDocument.this.addMap(HTMLReader.this.lastMap);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$AreaAction.class */
        class AreaAction extends TagAction {
            AreaAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                if (HTMLReader.this.lastMap != null) {
                    HTMLReader.this.lastMap.addArea(mutableAttributeSet.copyAttributes());
                }
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$StyleAction.class */
        class StyleAction extends TagAction {
            StyleAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                if (HTMLReader.this.inHead) {
                    if (HTMLReader.this.styles == null) {
                        HTMLReader.this.styles = new Vector<>(3);
                    }
                    HTMLReader.this.styles.addElement(tag);
                    HTMLReader.this.styles.addElement(mutableAttributeSet.getAttribute(HTML.Attribute.TYPE));
                    HTMLReader.this.inStyle = true;
                }
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                HTMLReader.this.inStyle = false;
            }

            boolean isEmpty(HTML.Tag tag) {
                return false;
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$PreAction.class */
        public class PreAction extends BlockAction {
            public PreAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.inPre = true;
                HTMLReader.this.blockOpen(tag, mutableAttributeSet);
                mutableAttributeSet.addAttribute(CSS.Attribute.WHITE_SPACE, "pre");
                HTMLReader.this.blockOpen(HTML.Tag.IMPLIED, mutableAttributeSet);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                HTMLReader.this.blockClose(HTML.Tag.IMPLIED);
                HTMLReader.this.inPre = false;
                HTMLReader.this.blockClose(tag);
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$CharacterAction.class */
        public class CharacterAction extends TagAction {
            public CharacterAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.pushCharacterStyle();
                if (!HTMLReader.this.foundInsertTag) {
                    boolean zCanInsertTag = HTMLReader.this.canInsertTag(tag, mutableAttributeSet, false);
                    if (HTMLReader.this.foundInsertTag && !HTMLReader.this.inParagraph) {
                        HTMLReader hTMLReader = HTMLReader.this;
                        HTMLReader.this.impliedP = true;
                        hTMLReader.inParagraph = true;
                    }
                    if (!zCanInsertTag) {
                        return;
                    }
                }
                if (mutableAttributeSet.isDefined(HTMLEditorKit.ParserCallback.IMPLIED)) {
                    mutableAttributeSet.removeAttribute(HTMLEditorKit.ParserCallback.IMPLIED);
                }
                HTMLReader.this.charAttr.addAttribute(tag, mutableAttributeSet.copyAttributes());
                if (HTMLReader.this.styleAttributes != null) {
                    HTMLReader.this.charAttr.addAttributes(HTMLReader.this.styleAttributes);
                }
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                HTMLReader.this.popCharacterStyle();
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$ConvertAction.class */
        class ConvertAction extends TagAction {
            ConvertAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.pushCharacterStyle();
                if (!HTMLReader.this.foundInsertTag) {
                    boolean zCanInsertTag = HTMLReader.this.canInsertTag(tag, mutableAttributeSet, false);
                    if (HTMLReader.this.foundInsertTag && !HTMLReader.this.inParagraph) {
                        HTMLReader hTMLReader = HTMLReader.this;
                        HTMLReader.this.impliedP = true;
                        hTMLReader.inParagraph = true;
                    }
                    if (!zCanInsertTag) {
                        return;
                    }
                }
                if (mutableAttributeSet.isDefined(HTMLEditorKit.ParserCallback.IMPLIED)) {
                    mutableAttributeSet.removeAttribute(HTMLEditorKit.ParserCallback.IMPLIED);
                }
                if (HTMLReader.this.styleAttributes != null) {
                    HTMLReader.this.charAttr.addAttributes(HTMLReader.this.styleAttributes);
                }
                HTMLReader.this.charAttr.addAttribute(tag, mutableAttributeSet.copyAttributes());
                StyleSheet styleSheet = HTMLDocument.this.getStyleSheet();
                if (tag == HTML.Tag.f12847B) {
                    styleSheet.addCSSAttribute(HTMLReader.this.charAttr, CSS.Attribute.FONT_WEIGHT, "bold");
                    return;
                }
                if (tag == HTML.Tag.f12848I) {
                    styleSheet.addCSSAttribute(HTMLReader.this.charAttr, CSS.Attribute.FONT_STYLE, "italic");
                    return;
                }
                if (tag == HTML.Tag.f12851U) {
                    Object attribute = HTMLReader.this.charAttr.getAttribute(CSS.Attribute.TEXT_DECORATION);
                    styleSheet.addCSSAttribute(HTMLReader.this.charAttr, CSS.Attribute.TEXT_DECORATION, attribute != null ? "underline," + attribute.toString() : "underline");
                    return;
                }
                if (tag == HTML.Tag.STRIKE) {
                    Object attribute2 = HTMLReader.this.charAttr.getAttribute(CSS.Attribute.TEXT_DECORATION);
                    styleSheet.addCSSAttribute(HTMLReader.this.charAttr, CSS.Attribute.TEXT_DECORATION, attribute2 != null ? "line-through," + attribute2.toString() : "line-through");
                    return;
                }
                if (tag == HTML.Tag.SUP) {
                    Object attribute3 = HTMLReader.this.charAttr.getAttribute(CSS.Attribute.VERTICAL_ALIGN);
                    styleSheet.addCSSAttribute(HTMLReader.this.charAttr, CSS.Attribute.VERTICAL_ALIGN, attribute3 != null ? "sup," + attribute3.toString() : "sup");
                    return;
                }
                if (tag == HTML.Tag.SUB) {
                    Object attribute4 = HTMLReader.this.charAttr.getAttribute(CSS.Attribute.VERTICAL_ALIGN);
                    styleSheet.addCSSAttribute(HTMLReader.this.charAttr, CSS.Attribute.VERTICAL_ALIGN, attribute4 != null ? "sub," + attribute4.toString() : "sub");
                } else if (tag == HTML.Tag.FONT) {
                    String str = (String) mutableAttributeSet.getAttribute(HTML.Attribute.COLOR);
                    if (str != null) {
                        styleSheet.addCSSAttribute(HTMLReader.this.charAttr, CSS.Attribute.COLOR, str);
                    }
                    String str2 = (String) mutableAttributeSet.getAttribute(HTML.Attribute.FACE);
                    if (str2 != null) {
                        styleSheet.addCSSAttribute(HTMLReader.this.charAttr, CSS.Attribute.FONT_FAMILY, str2);
                    }
                    String str3 = (String) mutableAttributeSet.getAttribute(HTML.Attribute.SIZE);
                    if (str3 != null) {
                        styleSheet.addCSSAttributeFromHTML(HTMLReader.this.charAttr, CSS.Attribute.FONT_SIZE, str3);
                    }
                }
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                HTMLReader.this.popCharacterStyle();
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$AnchorAction.class */
        class AnchorAction extends CharacterAction {
            AnchorAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.CharacterAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.emptyAnchor = true;
                super.start(tag, mutableAttributeSet);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.CharacterAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                if (HTMLReader.this.emptyAnchor) {
                    HTMLReader.this.addContent(new char[]{'\n'}, 0, 1);
                }
                super.end(tag);
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$TitleAction.class */
        class TitleAction extends HiddenAction {
            TitleAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                HTMLReader.this.inTitle = true;
                super.start(tag, mutableAttributeSet);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                HTMLReader.this.inTitle = false;
                super.end(tag);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction
            boolean isEmpty(HTML.Tag tag) {
                return false;
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$BaseAction.class */
        class BaseAction extends TagAction {
            BaseAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                String str = (String) mutableAttributeSet.getAttribute(HTML.Attribute.HREF);
                if (str != null) {
                    try {
                        HTMLDocument.this.setBase(new URL(HTMLDocument.this.base, str));
                        HTMLDocument.this.hasBaseTag = true;
                    } catch (MalformedURLException e2) {
                    }
                }
                HTMLDocument.this.baseTarget = (String) mutableAttributeSet.getAttribute(HTML.Attribute.TARGET);
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$ObjectAction.class */
        class ObjectAction extends SpecialAction {
            ObjectAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                if (tag == HTML.Tag.PARAM) {
                    addParameter(mutableAttributeSet);
                } else {
                    super.start(tag, mutableAttributeSet);
                }
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                if (tag != HTML.Tag.PARAM) {
                    super.end(tag);
                }
            }

            void addParameter(AttributeSet attributeSet) {
                String str = (String) attributeSet.getAttribute(HTML.Attribute.NAME);
                String str2 = (String) attributeSet.getAttribute(HTML.Attribute.VALUE);
                if (str != null && str2 != null) {
                    ((MutableAttributeSet) HTMLReader.this.parseBuffer.lastElement().getAttributes()).addAttribute(str, str2);
                }
            }
        }

        /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$HTMLReader$FormAction.class */
        public class FormAction extends SpecialAction {
            Object selectModel;
            int optionCount;

            public FormAction() {
                super();
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction, javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void start(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
                if (tag == HTML.Tag.INPUT) {
                    String str = (String) mutableAttributeSet.getAttribute(HTML.Attribute.TYPE);
                    if (str == null) {
                        str = "text";
                        mutableAttributeSet.addAttribute(HTML.Attribute.TYPE, "text");
                    }
                    setModel(str, mutableAttributeSet);
                } else if (tag == HTML.Tag.TEXTAREA) {
                    HTMLReader.this.inTextArea = true;
                    HTMLReader.this.textAreaDocument = new TextAreaDocument();
                    mutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, HTMLReader.this.textAreaDocument);
                } else if (tag == HTML.Tag.SELECT) {
                    int integerAttributeValue = HTML.getIntegerAttributeValue(mutableAttributeSet, HTML.Attribute.SIZE, 1);
                    boolean z2 = mutableAttributeSet.getAttribute(HTML.Attribute.MULTIPLE) != null;
                    if (integerAttributeValue > 1 || z2) {
                        OptionListModel optionListModel = new OptionListModel();
                        if (z2) {
                            optionListModel.setSelectionMode(2);
                        }
                        this.selectModel = optionListModel;
                    } else {
                        this.selectModel = new OptionComboBoxModel();
                    }
                    mutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, this.selectModel);
                }
                if (tag == HTML.Tag.OPTION) {
                    HTMLReader.this.option = new Option(mutableAttributeSet);
                    if (this.selectModel instanceof OptionListModel) {
                        OptionListModel optionListModel2 = (OptionListModel) this.selectModel;
                        optionListModel2.addElement(HTMLReader.this.option);
                        if (HTMLReader.this.option.isSelected()) {
                            optionListModel2.addSelectionInterval(this.optionCount, this.optionCount);
                            optionListModel2.setInitialSelection(this.optionCount);
                        }
                    } else if (this.selectModel instanceof OptionComboBoxModel) {
                        OptionComboBoxModel optionComboBoxModel = (OptionComboBoxModel) this.selectModel;
                        optionComboBoxModel.addElement(HTMLReader.this.option);
                        if (HTMLReader.this.option.isSelected()) {
                            optionComboBoxModel.setSelectedItem(HTMLReader.this.option);
                            optionComboBoxModel.setInitialSelection(HTMLReader.this.option);
                        }
                    }
                    this.optionCount++;
                    return;
                }
                super.start(tag, mutableAttributeSet);
            }

            @Override // javax.swing.text.html.HTMLDocument.HTMLReader.TagAction
            public void end(HTML.Tag tag) {
                if (tag == HTML.Tag.OPTION) {
                    HTMLReader.this.option = null;
                    return;
                }
                if (tag == HTML.Tag.SELECT) {
                    this.selectModel = null;
                    this.optionCount = 0;
                } else if (tag == HTML.Tag.TEXTAREA) {
                    HTMLReader.this.inTextArea = false;
                    HTMLReader.this.textAreaDocument.storeInitialText();
                }
                super.end(tag);
            }

            void setModel(String str, MutableAttributeSet mutableAttributeSet) {
                PlainDocument plainDocument;
                if (str.equals("submit") || str.equals(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET) || str.equals(MetadataParser.IMAGE_TAG_NAME)) {
                    mutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, new DefaultButtonModel());
                    return;
                }
                if (str.equals("text") || str.equals("password")) {
                    int integerAttributeValue = HTML.getIntegerAttributeValue(mutableAttributeSet, HTML.Attribute.MAXLENGTH, -1);
                    if (integerAttributeValue > 0) {
                        plainDocument = new FixedLengthDocument(integerAttributeValue);
                    } else {
                        plainDocument = new PlainDocument();
                    }
                    try {
                        plainDocument.insertString(0, (String) mutableAttributeSet.getAttribute(HTML.Attribute.VALUE), null);
                    } catch (BadLocationException e2) {
                    }
                    mutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, plainDocument);
                    return;
                }
                if (str.equals(DeploymentDescriptorParser.ATTR_FILE)) {
                    mutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, new PlainDocument());
                    return;
                }
                if (str.equals("checkbox") || str.equals("radio")) {
                    JToggleButton.ToggleButtonModel toggleButtonModel = new JToggleButton.ToggleButtonModel();
                    if (str.equals("radio")) {
                        String str2 = (String) mutableAttributeSet.getAttribute(HTML.Attribute.NAME);
                        if (HTMLDocument.this.radioButtonGroupsMap == null) {
                            HTMLDocument.this.radioButtonGroupsMap = new HashMap();
                        }
                        ButtonGroup buttonGroup = (ButtonGroup) HTMLDocument.this.radioButtonGroupsMap.get(str2);
                        if (buttonGroup == null) {
                            buttonGroup = new ButtonGroup();
                            HTMLDocument.this.radioButtonGroupsMap.put(str2, buttonGroup);
                        }
                        toggleButtonModel.setGroup(buttonGroup);
                    }
                    toggleButtonModel.setSelected(mutableAttributeSet.getAttribute(HTML.Attribute.CHECKED) != null);
                    mutableAttributeSet.addAttribute(StyleConstants.ModelAttribute, toggleButtonModel);
                }
            }
        }

        protected void pushCharacterStyle() {
            this.charAttrStack.push(this.charAttr.copyAttributes());
        }

        protected void popCharacterStyle() {
            if (!this.charAttrStack.empty()) {
                this.charAttr = (MutableAttributeSet) this.charAttrStack.peek();
                this.charAttrStack.pop();
            }
        }

        protected void textAreaContent(char[] cArr) {
            try {
                this.textAreaDocument.insertString(this.textAreaDocument.getLength(), new String(cArr), null);
            } catch (BadLocationException e2) {
            }
        }

        protected void preContent(char[] cArr) {
            int i2 = 0;
            for (int i3 = 0; i3 < cArr.length; i3++) {
                if (cArr[i3] == '\n') {
                    addContent(cArr, i2, (i3 - i2) + 1);
                    blockClose(HTML.Tag.IMPLIED);
                    SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                    simpleAttributeSet.addAttribute(CSS.Attribute.WHITE_SPACE, "pre");
                    blockOpen(HTML.Tag.IMPLIED, simpleAttributeSet);
                    i2 = i3 + 1;
                }
            }
            if (i2 < cArr.length) {
                addContent(cArr, i2, cArr.length - i2);
            }
        }

        protected void blockOpen(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
            if (this.impliedP) {
                blockClose(HTML.Tag.IMPLIED);
            }
            this.inBlock++;
            if (!canInsertTag(tag, mutableAttributeSet, true)) {
                return;
            }
            if (mutableAttributeSet.isDefined(IMPLIED)) {
                mutableAttributeSet.removeAttribute(IMPLIED);
            }
            this.lastWasNewline = false;
            mutableAttributeSet.addAttribute(StyleConstants.NameAttribute, tag);
            this.parseBuffer.addElement(new DefaultStyledDocument.ElementSpec(mutableAttributeSet.copyAttributes(), (short) 1));
        }

        protected void blockClose(HTML.Tag tag) {
            this.inBlock--;
            if (!this.foundInsertTag) {
                return;
            }
            if (!this.lastWasNewline) {
                pushCharacterStyle();
                this.charAttr.addAttribute(SwingUtilities2.IMPLIED_CR, Boolean.TRUE);
                addContent(HTMLDocument.NEWLINE, 0, 1, true);
                popCharacterStyle();
                this.lastWasNewline = true;
            }
            if (this.impliedP) {
                this.impliedP = false;
                this.inParagraph = false;
                if (tag != HTML.Tag.IMPLIED) {
                    blockClose(HTML.Tag.IMPLIED);
                }
            }
            DefaultStyledDocument.ElementSpec elementSpecLastElement = this.parseBuffer.size() > 0 ? this.parseBuffer.lastElement() : null;
            if (elementSpecLastElement != null && elementSpecLastElement.getType() == 1) {
                addContent(new char[]{' '}, 0, 1);
            }
            this.parseBuffer.addElement(new DefaultStyledDocument.ElementSpec(null, (short) 2));
        }

        protected void addContent(char[] cArr, int i2, int i3) {
            addContent(cArr, i2, i3, true);
        }

        protected void addContent(char[] cArr, int i2, int i3, boolean z2) {
            if (!this.foundInsertTag) {
                return;
            }
            if (z2 && !this.inParagraph && !this.inPre) {
                blockOpen(HTML.Tag.IMPLIED, new SimpleAttributeSet());
                this.inParagraph = true;
                this.impliedP = true;
            }
            this.emptyAnchor = false;
            this.charAttr.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
            this.parseBuffer.addElement(new DefaultStyledDocument.ElementSpec(this.charAttr.copyAttributes(), (short) 3, cArr, i2, i3));
            if (this.parseBuffer.size() > this.threshold) {
                if (this.threshold <= 10000) {
                    this.threshold *= 5;
                }
                try {
                    flushBuffer(false);
                } catch (BadLocationException e2) {
                }
            }
            if (i3 > 0) {
                this.lastWasNewline = cArr[(i2 + i3) - 1] == '\n';
            }
        }

        protected void addSpecialElement(HTML.Tag tag, MutableAttributeSet mutableAttributeSet) {
            if (tag != HTML.Tag.FRAME && !this.inParagraph && !this.inPre) {
                this.nextTagAfterPImplied = tag;
                blockOpen(HTML.Tag.IMPLIED, new SimpleAttributeSet());
                this.nextTagAfterPImplied = null;
                this.inParagraph = true;
                this.impliedP = true;
            }
            if (!canInsertTag(tag, mutableAttributeSet, tag.isBlock())) {
                return;
            }
            if (mutableAttributeSet.isDefined(IMPLIED)) {
                mutableAttributeSet.removeAttribute(IMPLIED);
            }
            this.emptyAnchor = false;
            mutableAttributeSet.addAttributes(this.charAttr);
            mutableAttributeSet.addAttribute(StyleConstants.NameAttribute, tag);
            this.parseBuffer.addElement(new DefaultStyledDocument.ElementSpec(mutableAttributeSet.copyAttributes(), (short) 3, new char[]{' '}, 0, 1));
            if (tag == HTML.Tag.FRAME) {
                this.lastWasNewline = true;
            }
        }

        void flushBuffer(boolean z2) throws BadLocationException {
            int length = HTMLDocument.this.getLength();
            int size = this.parseBuffer.size();
            if (z2 && ((this.insertTag != null || this.insertAfterImplied) && size > 0)) {
                adjustEndSpecsForPartialInsert();
                size = this.parseBuffer.size();
            }
            DefaultStyledDocument.ElementSpec[] elementSpecArr = new DefaultStyledDocument.ElementSpec[size];
            this.parseBuffer.copyInto(elementSpecArr);
            if (length == 0 && this.insertTag == null && !this.insertAfterImplied) {
                HTMLDocument.this.create(elementSpecArr);
            } else {
                HTMLDocument.this.insert(this.offset, elementSpecArr);
            }
            this.parseBuffer.removeAllElements();
            this.offset += HTMLDocument.this.getLength() - length;
            this.flushCount++;
        }

        private void adjustEndSpecsForPartialInsert() {
            int size = this.parseBuffer.size();
            if (this.insertTagDepthDelta < 0) {
                for (int i2 = this.insertTagDepthDelta; i2 < 0 && size >= 0 && this.parseBuffer.elementAt(size - 1).getType() == 2; i2++) {
                    size--;
                    this.parseBuffer.removeElementAt(size);
                }
            }
            if (this.flushCount == 0 && (!this.insertAfterImplied || !this.wantsTrailingNewline)) {
                int i3 = 0;
                if (this.pushDepth > 0 && this.parseBuffer.elementAt(0).getType() == 3) {
                    i3 = 0 + 1;
                }
                int i4 = i3 + this.popDepth + this.pushDepth;
                int i5 = 0;
                while (i4 < size && this.parseBuffer.elementAt(i4).getType() == 3) {
                    i4++;
                    i5++;
                }
                if (i5 > 1) {
                    while (i4 < size && this.parseBuffer.elementAt(i4).getType() == 2) {
                        i4++;
                    }
                    if (i4 == size) {
                        char[] array = this.parseBuffer.elementAt((i4 + i5) - 1).getArray();
                        if (array.length == 1 && array[0] == HTMLDocument.NEWLINE[0]) {
                            int i6 = (i4 + i5) - 1;
                            while (size > i6) {
                                size--;
                                this.parseBuffer.removeElementAt(size);
                            }
                        }
                    }
                }
            }
            if (this.wantsTrailingNewline) {
                for (int size2 = this.parseBuffer.size() - 1; size2 >= 0; size2--) {
                    DefaultStyledDocument.ElementSpec elementSpecElementAt = this.parseBuffer.elementAt(size2);
                    if (elementSpecElementAt.getType() == 3) {
                        if (elementSpecElementAt.getArray()[elementSpecElementAt.getLength() - 1] != '\n') {
                            SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
                            simpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
                            this.parseBuffer.insertElementAt(new DefaultStyledDocument.ElementSpec(simpleAttributeSet, (short) 3, HTMLDocument.NEWLINE, 0, 1), size2 + 1);
                            return;
                        }
                        return;
                    }
                }
            }
        }

        void addCSSRules(String str) {
            HTMLDocument.this.getStyleSheet().addRule(str);
        }

        void linkCSSStyleSheet(String str) {
            URL url;
            try {
                url = new URL(HTMLDocument.this.base, str);
            } catch (MalformedURLException e2) {
                try {
                    url = new URL(str);
                } catch (MalformedURLException e3) {
                    url = null;
                }
            }
            if (url != null) {
                HTMLDocument.this.getStyleSheet().importStyleSheet(url);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean canInsertTag(HTML.Tag tag, AttributeSet attributeSet, boolean z2) {
            if (!this.foundInsertTag) {
                if (((tag != HTML.Tag.IMPLIED || this.inParagraph || this.inPre) ? false : true) && this.nextTagAfterPImplied != null) {
                    if (this.insertTag != null && (!isInsertTag(this.nextTagAfterPImplied) || !this.insertInsertTag)) {
                        return false;
                    }
                } else if (this.insertTag == null || isInsertTag(tag)) {
                    if (this.insertAfterImplied && (attributeSet == null || attributeSet.isDefined(IMPLIED) || tag == HTML.Tag.IMPLIED)) {
                        return false;
                    }
                } else {
                    return false;
                }
                foundInsertTag(z2);
                if (!this.insertInsertTag) {
                    return false;
                }
                return true;
            }
            return true;
        }

        private boolean isInsertTag(HTML.Tag tag) {
            return this.insertTag == tag;
        }

        private void foundInsertTag(boolean z2) {
            HTML.Tag tag;
            this.foundInsertTag = true;
            if (!this.insertAfterImplied && (this.popDepth > 0 || this.pushDepth > 0)) {
                try {
                    if (this.offset == 0 || !HTMLDocument.this.getText(this.offset - 1, 1).equals("\n")) {
                        SimpleAttributeSet simpleAttributeSet = null;
                        boolean z3 = true;
                        if (this.offset != 0) {
                            AttributeSet attributes = HTMLDocument.this.getCharacterElement(this.offset - 1).getAttributes();
                            if (attributes.isDefined(StyleConstants.ComposedTextAttribute)) {
                                z3 = false;
                            } else {
                                Object attribute = attributes.getAttribute(StyleConstants.NameAttribute);
                                if ((attribute instanceof HTML.Tag) && ((tag = (HTML.Tag) attribute) == HTML.Tag.IMG || tag == HTML.Tag.HR || tag == HTML.Tag.COMMENT || (tag instanceof HTML.UnknownTag))) {
                                    z3 = false;
                                }
                            }
                        }
                        if (!z3) {
                            simpleAttributeSet = new SimpleAttributeSet();
                            simpleAttributeSet.addAttribute(StyleConstants.NameAttribute, HTML.Tag.CONTENT);
                        }
                        DefaultStyledDocument.ElementSpec elementSpec = new DefaultStyledDocument.ElementSpec(simpleAttributeSet, (short) 3, HTMLDocument.NEWLINE, 0, HTMLDocument.NEWLINE.length);
                        if (z3) {
                            elementSpec.setDirection((short) 4);
                        }
                        this.parseBuffer.addElement(elementSpec);
                    }
                } catch (BadLocationException e2) {
                }
            }
            for (int i2 = 0; i2 < this.popDepth; i2++) {
                this.parseBuffer.addElement(new DefaultStyledDocument.ElementSpec(null, (short) 2));
            }
            for (int i3 = 0; i3 < this.pushDepth; i3++) {
                DefaultStyledDocument.ElementSpec elementSpec2 = new DefaultStyledDocument.ElementSpec(null, (short) 1);
                elementSpec2.setDirection((short) 5);
                this.parseBuffer.addElement(elementSpec2);
            }
            this.insertTagDepthDelta = ((depthTo(Math.max(0, this.offset - 1)) - this.popDepth) + this.pushDepth) - this.inBlock;
            if (z2) {
                this.insertTagDepthDelta++;
                return;
            }
            this.insertTagDepthDelta--;
            this.inParagraph = true;
            this.lastWasNewline = false;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$TaggedAttributeSet.class */
    static class TaggedAttributeSet extends SimpleAttributeSet {
        TaggedAttributeSet() {
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$RunElement.class */
    public class RunElement extends AbstractDocument.LeafElement {
        public RunElement(Element element, AttributeSet attributeSet, int i2, int i3) {
            super(element, attributeSet, i2, i3);
        }

        @Override // javax.swing.text.AbstractDocument.LeafElement, javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public String getName() {
            Object attribute = getAttribute(StyleConstants.NameAttribute);
            if (attribute != null) {
                return attribute.toString();
            }
            return super.getName();
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.AttributeSet
        public AttributeSet getResolveParent() {
            return null;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$BlockElement.class */
    public class BlockElement extends AbstractDocument.BranchElement {
        public BlockElement(Element element, AttributeSet attributeSet) {
            super(element, attributeSet);
        }

        @Override // javax.swing.text.AbstractDocument.BranchElement, javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.Element
        public String getName() {
            Object attribute = getAttribute(StyleConstants.NameAttribute);
            if (attribute != null) {
                return attribute.toString();
            }
            return super.getName();
        }

        @Override // javax.swing.text.AbstractDocument.AbstractElement, javax.swing.text.AttributeSet
        public AttributeSet getResolveParent() {
            return null;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/HTMLDocument$FixedLengthDocument.class */
    private static class FixedLengthDocument extends PlainDocument {
        private int maxLength;

        public FixedLengthDocument(int i2) {
            this.maxLength = i2;
        }

        @Override // javax.swing.text.PlainDocument, javax.swing.text.AbstractDocument, javax.swing.text.Document
        public void insertString(int i2, String str, AttributeSet attributeSet) throws BadLocationException {
            if (str != null && str.length() + getLength() <= this.maxLength) {
                super.insertString(i2, str, attributeSet);
            }
        }
    }
}
