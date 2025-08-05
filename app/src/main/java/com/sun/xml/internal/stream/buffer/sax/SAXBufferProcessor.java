package com.sun.xml.internal.stream.buffer.sax;

import com.sun.xml.internal.stream.buffer.AbstractProcessor;
import com.sun.xml.internal.stream.buffer.AttributesHolder;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/sax/SAXBufferProcessor.class */
public class SAXBufferProcessor extends AbstractProcessor implements XMLReader {
    protected EntityResolver _entityResolver;
    protected DTDHandler _dtdHandler;
    protected ContentHandler _contentHandler;
    protected ErrorHandler _errorHandler;
    protected LexicalHandler _lexicalHandler;
    protected boolean _namespacePrefixesFeature;
    protected AttributesHolder _attributes;
    protected String[] _namespacePrefixes;
    protected int _namespacePrefixesIndex;
    protected int[] _namespaceAttributesStartingStack;
    protected int[] _namespaceAttributesStack;
    protected int _namespaceAttributesStackIndex;
    private static final DefaultWithLexicalHandler DEFAULT_LEXICAL_HANDLER = new DefaultWithLexicalHandler();

    public SAXBufferProcessor() {
        this._entityResolver = DEFAULT_LEXICAL_HANDLER;
        this._dtdHandler = DEFAULT_LEXICAL_HANDLER;
        this._contentHandler = DEFAULT_LEXICAL_HANDLER;
        this._errorHandler = DEFAULT_LEXICAL_HANDLER;
        this._lexicalHandler = DEFAULT_LEXICAL_HANDLER;
        this._namespacePrefixesFeature = false;
        this._attributes = new AttributesHolder();
        this._namespacePrefixes = new String[16];
        this._namespaceAttributesStartingStack = new int[16];
        this._namespaceAttributesStack = new int[16];
    }

    public SAXBufferProcessor(XMLStreamBuffer buffer) {
        this._entityResolver = DEFAULT_LEXICAL_HANDLER;
        this._dtdHandler = DEFAULT_LEXICAL_HANDLER;
        this._contentHandler = DEFAULT_LEXICAL_HANDLER;
        this._errorHandler = DEFAULT_LEXICAL_HANDLER;
        this._lexicalHandler = DEFAULT_LEXICAL_HANDLER;
        this._namespacePrefixesFeature = false;
        this._attributes = new AttributesHolder();
        this._namespacePrefixes = new String[16];
        this._namespaceAttributesStartingStack = new int[16];
        this._namespaceAttributesStack = new int[16];
        setXMLStreamBuffer(buffer);
    }

    public SAXBufferProcessor(XMLStreamBuffer buffer, boolean produceFragmentEvent) {
        this._entityResolver = DEFAULT_LEXICAL_HANDLER;
        this._dtdHandler = DEFAULT_LEXICAL_HANDLER;
        this._contentHandler = DEFAULT_LEXICAL_HANDLER;
        this._errorHandler = DEFAULT_LEXICAL_HANDLER;
        this._lexicalHandler = DEFAULT_LEXICAL_HANDLER;
        this._namespacePrefixesFeature = false;
        this._attributes = new AttributesHolder();
        this._namespacePrefixes = new String[16];
        this._namespaceAttributesStartingStack = new int[16];
        this._namespaceAttributesStack = new int[16];
        setXMLStreamBuffer(buffer, produceFragmentEvent);
    }

    @Override // org.xml.sax.XMLReader
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http://xml.org/sax/features/namespaces")) {
            return true;
        }
        if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
            return this._namespacePrefixesFeature;
        }
        if (name.equals(Features.EXTERNAL_GENERAL_ENTITIES) || name.equals(Features.EXTERNAL_PARAMETER_ENTITIES)) {
            return true;
        }
        if (name.equals("http://xml.org/sax/features/string-interning")) {
            return this._stringInterningFeature;
        }
        throw new SAXNotRecognizedException("Feature not supported: " + name);
    }

    @Override // org.xml.sax.XMLReader
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http://xml.org/sax/features/namespaces")) {
            if (!value) {
                throw new SAXNotSupportedException(name + CallSiteDescriptor.TOKEN_DELIMITER + value);
            }
        } else {
            if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
                this._namespacePrefixesFeature = value;
                return;
            }
            if (!name.equals(Features.EXTERNAL_GENERAL_ENTITIES) && !name.equals(Features.EXTERNAL_PARAMETER_ENTITIES)) {
                if (name.equals("http://xml.org/sax/features/string-interning")) {
                    if (value != this._stringInterningFeature) {
                        throw new SAXNotSupportedException(name + CallSiteDescriptor.TOKEN_DELIMITER + value);
                    }
                    return;
                }
                throw new SAXNotRecognizedException("Feature not supported: " + name);
            }
        }
    }

    @Override // org.xml.sax.XMLReader
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http://xml.org/sax/properties/lexical-handler")) {
            return getLexicalHandler();
        }
        throw new SAXNotRecognizedException("Property not recognized: " + name);
    }

    @Override // org.xml.sax.XMLReader
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http://xml.org/sax/properties/lexical-handler")) {
            if (value instanceof LexicalHandler) {
                setLexicalHandler((LexicalHandler) value);
                return;
            }
            throw new SAXNotSupportedException("http://xml.org/sax/properties/lexical-handler");
        }
        throw new SAXNotRecognizedException("Property not recognized: " + name);
    }

    @Override // org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver resolver) {
        this._entityResolver = resolver;
    }

    @Override // org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        return this._entityResolver;
    }

    @Override // org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler handler) {
        this._dtdHandler = handler;
    }

    @Override // org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        return this._dtdHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler handler) {
        this._contentHandler = handler;
    }

    @Override // org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return this._contentHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler handler) {
        this._errorHandler = handler;
    }

    @Override // org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        return this._errorHandler;
    }

    public void setLexicalHandler(LexicalHandler handler) {
        this._lexicalHandler = handler;
    }

    public LexicalHandler getLexicalHandler() {
        return this._lexicalHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void parse(InputSource input) throws SAXException, IOException {
        process();
    }

    @Override // org.xml.sax.XMLReader
    public void parse(String systemId) throws SAXException, IOException {
        process();
    }

    public final void process(XMLStreamBuffer buffer) throws SAXException {
        setXMLStreamBuffer(buffer);
        process();
    }

    public final void process(XMLStreamBuffer buffer, boolean produceFragmentEvent) throws SAXException {
        setXMLStreamBuffer(buffer);
        process();
    }

    public void setXMLStreamBuffer(XMLStreamBuffer buffer) {
        setBuffer(buffer);
    }

    public void setXMLStreamBuffer(XMLStreamBuffer buffer, boolean produceFragmentEvent) {
        if (!produceFragmentEvent && this._treeCount > 1) {
            throw new IllegalStateException("Can't write a forest to a full XML infoset");
        }
        setBuffer(buffer, produceFragmentEvent);
    }

    public final void process() throws SAXException {
        if (!this._fragmentMode) {
            LocatorImpl nullLocator = new LocatorImpl();
            nullLocator.setSystemId(this._buffer.getSystemId());
            nullLocator.setLineNumber(-1);
            nullLocator.setColumnNumber(-1);
            this._contentHandler.setDocumentLocator(nullLocator);
            this._contentHandler.startDocument();
        }
        while (this._treeCount > 0) {
            int item = readEiiState();
            switch (item) {
                case 1:
                    processDocument();
                    this._treeCount--;
                    break;
                case 2:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    throw reportFatalError("Illegal state for DIIs: " + item);
                case 3:
                    processElement(readStructureString(), readStructureString(), readStructureString(), isInscope());
                    this._treeCount--;
                    break;
                case 4:
                    String prefix = readStructureString();
                    String uri = readStructureString();
                    String localName = readStructureString();
                    processElement(uri, localName, getQName(prefix, localName), isInscope());
                    this._treeCount--;
                    break;
                case 5:
                    String uri2 = readStructureString();
                    String localName2 = readStructureString();
                    processElement(uri2, localName2, localName2, isInscope());
                    this._treeCount--;
                    break;
                case 6:
                    String localName3 = readStructureString();
                    processElement("", localName3, localName3, isInscope());
                    this._treeCount--;
                    break;
                case 12:
                    processCommentAsCharArraySmall();
                    break;
                case 13:
                    processCommentAsCharArrayMedium();
                    break;
                case 14:
                    processCommentAsCharArrayCopy();
                    break;
                case 15:
                    processComment(readContentString());
                    break;
                case 16:
                    processProcessingInstruction(readStructureString(), readStructureString());
                    break;
                case 17:
                    return;
            }
        }
        if (!this._fragmentMode) {
            this._contentHandler.endDocument();
        }
    }

    private void processCommentAsCharArraySmall() throws SAXException {
        int length = readStructure();
        int start = readContentCharactersBuffer(length);
        processComment(this._contentCharactersBuffer, start, length);
    }

    private SAXParseException reportFatalError(String msg) throws SAXException {
        SAXParseException spe = new SAXParseException(msg, null);
        if (this._errorHandler != null) {
            this._errorHandler.fatalError(spe);
        }
        return spe;
    }

    private boolean isInscope() {
        return this._buffer.getInscopeNamespaces().size() > 0;
    }

    private void processDocument() throws SAXException {
        while (true) {
            int item = readEiiState();
            switch (item) {
                case 3:
                    processElement(readStructureString(), readStructureString(), readStructureString(), isInscope());
                    break;
                case 4:
                    String prefix = readStructureString();
                    String uri = readStructureString();
                    String localName = readStructureString();
                    processElement(uri, localName, getQName(prefix, localName), isInscope());
                    break;
                case 5:
                    String uri2 = readStructureString();
                    String localName2 = readStructureString();
                    processElement(uri2, localName2, localName2, isInscope());
                    break;
                case 6:
                    String localName3 = readStructureString();
                    processElement("", localName3, localName3, isInscope());
                    break;
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    throw reportFatalError("Illegal state for child of DII: " + item);
                case 12:
                    processCommentAsCharArraySmall();
                    break;
                case 13:
                    processCommentAsCharArrayMedium();
                    break;
                case 14:
                    processCommentAsCharArrayCopy();
                    break;
                case 15:
                    processComment(readContentString());
                    break;
                case 16:
                    processProcessingInstruction(readStructureString(), readStructureString());
                    break;
                case 17:
                    return;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected void processElement(String uri, String localName, String qName, boolean inscope) throws SAXException {
        int item;
        boolean hasAttributes = false;
        boolean hasNamespaceAttributes = false;
        int item2 = peekStructure();
        Set<String> prefixSet = inscope ? new HashSet<>() : Collections.emptySet();
        if ((item2 & 240) == 64) {
            cacheNamespacePrefixStartingIndex();
            hasNamespaceAttributes = true;
            item2 = processNamespaceAttributes(item2, inscope, prefixSet);
        }
        if (inscope) {
            readInscopeNamespaces(prefixSet);
        }
        if ((item2 & 240) == 48) {
            hasAttributes = true;
            processAttributes(item2);
        }
        this._contentHandler.startElement(uri, localName, qName, this._attributes);
        if (hasAttributes) {
            this._attributes.clear();
        }
        do {
            item = readEiiState();
            switch (item) {
                case 3:
                    processElement(readStructureString(), readStructureString(), readStructureString(), false);
                    break;
                case 4:
                    String p2 = readStructureString();
                    String u2 = readStructureString();
                    String ln = readStructureString();
                    processElement(u2, ln, getQName(p2, ln), false);
                    break;
                case 5:
                    String u3 = readStructureString();
                    String ln2 = readStructureString();
                    processElement(u3, ln2, ln2, false);
                    break;
                case 6:
                    String ln3 = readStructureString();
                    processElement("", ln3, ln3, false);
                    break;
                case 7:
                    int length = readStructure();
                    int start = readContentCharactersBuffer(length);
                    this._contentHandler.characters(this._contentCharactersBuffer, start, length);
                    break;
                case 8:
                    int length2 = readStructure16();
                    int start2 = readContentCharactersBuffer(length2);
                    this._contentHandler.characters(this._contentCharactersBuffer, start2, length2);
                    break;
                case 9:
                    char[] ch = readContentCharactersCopy();
                    this._contentHandler.characters(ch, 0, ch.length);
                    break;
                case 10:
                    String s2 = readContentString();
                    this._contentHandler.characters(s2.toCharArray(), 0, s2.length());
                    break;
                case 11:
                    CharSequence c2 = (CharSequence) readContentObject();
                    String s3 = c2.toString();
                    this._contentHandler.characters(s3.toCharArray(), 0, s3.length());
                    break;
                case 12:
                    processCommentAsCharArraySmall();
                    break;
                case 13:
                    processCommentAsCharArrayMedium();
                    break;
                case 14:
                    processCommentAsCharArrayCopy();
                    break;
                case 16:
                    processProcessingInstruction(readStructureString(), readStructureString());
                    break;
                case 17:
                    break;
                case 104:
                    processComment(readContentString());
                    break;
                default:
                    throw reportFatalError("Illegal state for child of EII: " + item);
            }
        } while (item != 17);
        this._contentHandler.endElement(uri, localName, qName);
        if (hasNamespaceAttributes) {
            processEndPrefixMapping();
        }
    }

    private void readInscopeNamespaces(Set<String> prefixSet) throws SAXException {
        for (Map.Entry<String, String> e2 : this._buffer.getInscopeNamespaces().entrySet()) {
            String key = fixNull(e2.getKey());
            if (!prefixSet.contains(key)) {
                processNamespaceAttribute(key, e2.getValue());
            }
        }
    }

    private static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }

    private void processCommentAsCharArrayCopy() throws SAXException {
        char[] ch = readContentCharactersCopy();
        processComment(ch, 0, ch.length);
    }

    private void processCommentAsCharArrayMedium() throws SAXException {
        int length = readStructure16();
        int start = readContentCharactersBuffer(length);
        processComment(this._contentCharactersBuffer, start, length);
    }

    private void processEndPrefixMapping() throws SAXException {
        int[] iArr = this._namespaceAttributesStack;
        int i2 = this._namespaceAttributesStackIndex - 1;
        this._namespaceAttributesStackIndex = i2;
        int end = iArr[i2];
        int start = this._namespaceAttributesStackIndex >= 0 ? this._namespaceAttributesStartingStack[this._namespaceAttributesStackIndex] : 0;
        for (int i3 = end - 1; i3 >= start; i3--) {
            this._contentHandler.endPrefixMapping(this._namespacePrefixes[i3]);
        }
        this._namespacePrefixesIndex = start;
    }

    private int processNamespaceAttributes(int item, boolean collectPrefixes, Set<String> prefixSet) throws SAXException {
        do {
            switch (getNIIState(item)) {
                case 1:
                    processNamespaceAttribute("", "");
                    if (collectPrefixes) {
                        prefixSet.add("");
                        break;
                    }
                    break;
                case 2:
                    String prefix = readStructureString();
                    processNamespaceAttribute(prefix, "");
                    if (collectPrefixes) {
                        prefixSet.add(prefix);
                        break;
                    }
                    break;
                case 3:
                    String prefix2 = readStructureString();
                    processNamespaceAttribute(prefix2, readStructureString());
                    if (collectPrefixes) {
                        prefixSet.add(prefix2);
                        break;
                    }
                    break;
                case 4:
                    processNamespaceAttribute("", readStructureString());
                    if (collectPrefixes) {
                        prefixSet.add("");
                        break;
                    }
                    break;
                default:
                    throw reportFatalError("Illegal state: " + item);
            }
            readStructure();
            item = peekStructure();
        } while ((item & 240) == 64);
        cacheNamespacePrefixIndex();
        return item;
    }

    private void processAttributes(int item) throws SAXException {
        do {
            switch (getAIIState(item)) {
                case 1:
                    this._attributes.addAttributeWithQName(readStructureString(), readStructureString(), readStructureString(), readStructureString(), readContentString());
                    break;
                case 2:
                    String p2 = readStructureString();
                    String u2 = readStructureString();
                    String ln = readStructureString();
                    this._attributes.addAttributeWithQName(u2, ln, getQName(p2, ln), readStructureString(), readContentString());
                    break;
                case 3:
                    String u3 = readStructureString();
                    String ln2 = readStructureString();
                    this._attributes.addAttributeWithQName(u3, ln2, ln2, readStructureString(), readContentString());
                    break;
                case 4:
                    String ln3 = readStructureString();
                    this._attributes.addAttributeWithQName("", ln3, ln3, readStructureString(), readContentString());
                    break;
                default:
                    throw reportFatalError("Illegal state: " + item);
            }
            readStructure();
            item = peekStructure();
        } while ((item & 240) == 48);
    }

    private void processNamespaceAttribute(String prefix, String uri) throws SAXException {
        this._contentHandler.startPrefixMapping(prefix, uri);
        if (this._namespacePrefixesFeature) {
            if (prefix != "") {
                this._attributes.addAttributeWithQName("http://www.w3.org/2000/xmlns/", prefix, getQName("xmlns", prefix), "CDATA", uri);
            } else {
                this._attributes.addAttributeWithQName("http://www.w3.org/2000/xmlns/", "xmlns", "xmlns", "CDATA", uri);
            }
        }
        cacheNamespacePrefix(prefix);
    }

    private void cacheNamespacePrefix(String prefix) {
        if (this._namespacePrefixesIndex == this._namespacePrefixes.length) {
            String[] namespaceAttributes = new String[((this._namespacePrefixesIndex * 3) / 2) + 1];
            System.arraycopy(this._namespacePrefixes, 0, namespaceAttributes, 0, this._namespacePrefixesIndex);
            this._namespacePrefixes = namespaceAttributes;
        }
        String[] strArr = this._namespacePrefixes;
        int i2 = this._namespacePrefixesIndex;
        this._namespacePrefixesIndex = i2 + 1;
        strArr[i2] = prefix;
    }

    private void cacheNamespacePrefixIndex() {
        if (this._namespaceAttributesStackIndex == this._namespaceAttributesStack.length) {
            int[] namespaceAttributesStack = new int[((this._namespaceAttributesStackIndex * 3) / 2) + 1];
            System.arraycopy(this._namespaceAttributesStack, 0, namespaceAttributesStack, 0, this._namespaceAttributesStackIndex);
            this._namespaceAttributesStack = namespaceAttributesStack;
        }
        int[] iArr = this._namespaceAttributesStack;
        int i2 = this._namespaceAttributesStackIndex;
        this._namespaceAttributesStackIndex = i2 + 1;
        iArr[i2] = this._namespacePrefixesIndex;
    }

    private void cacheNamespacePrefixStartingIndex() {
        if (this._namespaceAttributesStackIndex == this._namespaceAttributesStartingStack.length) {
            int[] namespaceAttributesStart = new int[((this._namespaceAttributesStackIndex * 3) / 2) + 1];
            System.arraycopy(this._namespaceAttributesStartingStack, 0, namespaceAttributesStart, 0, this._namespaceAttributesStackIndex);
            this._namespaceAttributesStartingStack = namespaceAttributesStart;
        }
        this._namespaceAttributesStartingStack[this._namespaceAttributesStackIndex] = this._namespacePrefixesIndex;
    }

    private void processComment(String s2) throws SAXException {
        processComment(s2.toCharArray(), 0, s2.length());
    }

    private void processComment(char[] ch, int start, int length) throws SAXException {
        this._lexicalHandler.comment(ch, start, length);
    }

    private void processProcessingInstruction(String target, String data) throws SAXException {
        this._contentHandler.processingInstruction(target, data);
    }
}
