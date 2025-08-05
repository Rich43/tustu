package com.sun.xml.internal.fastinfoset.stax;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.Encoder;
import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.internal.fastinfoset.util.NamespaceContextImplementation;
import com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EmptyStackException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/StAXDocumentSerializer.class */
public class StAXDocumentSerializer extends Encoder implements XMLStreamWriter, LowLevelFastInfosetStreamWriter {
    protected StAXManager _manager;
    protected String _encoding;
    protected String _currentLocalName;
    protected String _currentUri;
    protected String _currentPrefix;
    protected boolean _inStartElement;
    protected boolean _isEmptyElement;
    protected String[] _attributesArray;
    protected int _attributesArrayIndex;
    protected boolean[] _nsSupportContextStack;
    protected int _stackCount;
    protected NamespaceContextImplementation _nsContext;
    protected String[] _namespacesArray;
    protected int _namespacesArrayIndex;

    public StAXDocumentSerializer() {
        super(true);
        this._inStartElement = false;
        this._isEmptyElement = false;
        this._attributesArray = new String[64];
        this._attributesArrayIndex = 0;
        this._nsSupportContextStack = new boolean[32];
        this._stackCount = -1;
        this._nsContext = new NamespaceContextImplementation();
        this._namespacesArray = new String[16];
        this._namespacesArrayIndex = 0;
        this._manager = new StAXManager(2);
    }

    public StAXDocumentSerializer(OutputStream outputStream) {
        super(true);
        this._inStartElement = false;
        this._isEmptyElement = false;
        this._attributesArray = new String[64];
        this._attributesArrayIndex = 0;
        this._nsSupportContextStack = new boolean[32];
        this._stackCount = -1;
        this._nsContext = new NamespaceContextImplementation();
        this._namespacesArray = new String[16];
        this._namespacesArrayIndex = 0;
        setOutputStream(outputStream);
        this._manager = new StAXManager(2);
    }

    public StAXDocumentSerializer(OutputStream outputStream, StAXManager manager) {
        super(true);
        this._inStartElement = false;
        this._isEmptyElement = false;
        this._attributesArray = new String[64];
        this._attributesArrayIndex = 0;
        this._nsSupportContextStack = new boolean[32];
        this._stackCount = -1;
        this._nsContext = new NamespaceContextImplementation();
        this._namespacesArray = new String[16];
        this._namespacesArrayIndex = 0;
        setOutputStream(outputStream);
        this._manager = manager;
    }

    @Override // com.sun.xml.internal.fastinfoset.Encoder, com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void reset() {
        super.reset();
        this._attributesArrayIndex = 0;
        this._namespacesArrayIndex = 0;
        this._nsContext.reset();
        this._stackCount = -1;
        this._currentPrefix = null;
        this._currentUri = null;
        this._currentLocalName = null;
        this._isEmptyElement = false;
        this._inStartElement = false;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument() throws XMLStreamException {
        writeStartDocument("finf", "1.0");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String version) throws XMLStreamException {
        writeStartDocument("finf", version);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        reset();
        try {
            encodeHeader(false);
            encodeInitialVocabulary();
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndDocument() throws XMLStreamException {
        while (this._stackCount >= 0) {
            try {
                writeEndElement();
                this._stackCount--;
            } catch (IOException e2) {
                throw new XMLStreamException(e2);
            }
        }
        encodeDocumentTermination();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void close() throws XMLStreamException {
        reset();
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void flush() throws XMLStreamException {
        try {
            this._s.flush();
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String localName) throws XMLStreamException {
        writeStartElement("", localName, "");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        writeStartElement("", localName, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        encodeTerminationAndCurrentElement(false);
        this._inStartElement = true;
        this._isEmptyElement = false;
        this._currentLocalName = localName;
        this._currentPrefix = prefix;
        this._currentUri = namespaceURI;
        this._stackCount++;
        if (this._stackCount == this._nsSupportContextStack.length) {
            boolean[] nsSupportContextStack = new boolean[this._stackCount * 2];
            System.arraycopy(this._nsSupportContextStack, 0, nsSupportContextStack, 0, this._nsSupportContextStack.length);
            this._nsSupportContextStack = nsSupportContextStack;
        }
        this._nsSupportContextStack[this._stackCount] = false;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String localName) throws XMLStreamException {
        writeEmptyElement("", localName, "");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        writeEmptyElement("", localName, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        encodeTerminationAndCurrentElement(false);
        this._inStartElement = true;
        this._isEmptyElement = true;
        this._currentLocalName = localName;
        this._currentPrefix = prefix;
        this._currentUri = namespaceURI;
        this._stackCount++;
        if (this._stackCount == this._nsSupportContextStack.length) {
            boolean[] nsSupportContextStack = new boolean[this._stackCount * 2];
            System.arraycopy(this._nsSupportContextStack, 0, nsSupportContextStack, 0, this._nsSupportContextStack.length);
            this._nsSupportContextStack = nsSupportContextStack;
        }
        this._nsSupportContextStack[this._stackCount] = false;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEndElement() throws XMLStreamException {
        if (this._inStartElement) {
            encodeTerminationAndCurrentElement(false);
        }
        try {
            encodeElementTermination();
            boolean[] zArr = this._nsSupportContextStack;
            int i2 = this._stackCount;
            this._stackCount = i2 - 1;
            if (zArr[i2]) {
                this._nsContext.popContext();
            }
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        } catch (EmptyStackException e3) {
            throw new XMLStreamException(e3);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String localName, String value) throws XMLStreamException {
        writeAttribute("", "", localName, value);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        String prefix = "";
        if (namespaceURI.length() > 0) {
            prefix = this._nsContext.getNonDefaultPrefix(namespaceURI);
            if (prefix == null || prefix.length() == 0) {
                if (namespaceURI == "http://www.w3.org/2000/xmlns/" || namespaceURI.equals("http://www.w3.org/2000/xmlns/")) {
                    return;
                } else {
                    throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.URIUnbound", new Object[]{namespaceURI}));
                }
            }
        }
        writeAttribute(prefix, namespaceURI, localName, value);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
        if (!this._inStartElement) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.attributeWritingNotAllowed"));
        }
        if (namespaceURI == "http://www.w3.org/2000/xmlns/" || namespaceURI.equals("http://www.w3.org/2000/xmlns/")) {
            return;
        }
        if (this._attributesArrayIndex == this._attributesArray.length) {
            String[] attributesArray = new String[this._attributesArrayIndex * 2];
            System.arraycopy(this._attributesArray, 0, attributesArray, 0, this._attributesArrayIndex);
            this._attributesArray = attributesArray;
        }
        String[] strArr = this._attributesArray;
        int i2 = this._attributesArrayIndex;
        this._attributesArrayIndex = i2 + 1;
        strArr[i2] = namespaceURI;
        String[] strArr2 = this._attributesArray;
        int i3 = this._attributesArrayIndex;
        this._attributesArrayIndex = i3 + 1;
        strArr2[i3] = prefix;
        String[] strArr3 = this._attributesArray;
        int i4 = this._attributesArrayIndex;
        this._attributesArrayIndex = i4 + 1;
        strArr3[i4] = localName;
        String[] strArr4 = this._attributesArray;
        int i5 = this._attributesArrayIndex;
        this._attributesArrayIndex = i5 + 1;
        strArr4[i5] = value;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        if (prefix == null || prefix.length() == 0 || prefix.equals("xmlns")) {
            writeDefaultNamespace(namespaceURI);
            return;
        }
        if (!this._inStartElement) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.attributeWritingNotAllowed"));
        }
        if (this._namespacesArrayIndex == this._namespacesArray.length) {
            String[] namespacesArray = new String[this._namespacesArrayIndex * 2];
            System.arraycopy(this._namespacesArray, 0, namespacesArray, 0, this._namespacesArrayIndex);
            this._namespacesArray = namespacesArray;
        }
        String[] strArr = this._namespacesArray;
        int i2 = this._namespacesArrayIndex;
        this._namespacesArrayIndex = i2 + 1;
        strArr[i2] = prefix;
        String[] strArr2 = this._namespacesArray;
        int i3 = this._namespacesArrayIndex;
        this._namespacesArrayIndex = i3 + 1;
        strArr2[i3] = namespaceURI;
        setPrefix(prefix, namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        if (!this._inStartElement) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.attributeWritingNotAllowed"));
        }
        if (this._namespacesArrayIndex == this._namespacesArray.length) {
            String[] namespacesArray = new String[this._namespacesArrayIndex * 2];
            System.arraycopy(this._namespacesArray, 0, namespacesArray, 0, this._namespacesArrayIndex);
            this._namespacesArray = namespacesArray;
        }
        String[] strArr = this._namespacesArray;
        int i2 = this._namespacesArrayIndex;
        this._namespacesArrayIndex = i2 + 1;
        strArr[i2] = "";
        String[] strArr2 = this._namespacesArray;
        int i3 = this._namespacesArrayIndex;
        this._namespacesArrayIndex = i3 + 1;
        strArr2[i3] = namespaceURI;
        setPrefix("", namespaceURI);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeComment(String data) throws XMLStreamException {
        try {
            if (getIgnoreComments()) {
                return;
            }
            encodeTerminationAndCurrentElement(true);
            encodeComment(data.toCharArray(), 0, data.length());
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target) throws XMLStreamException {
        writeProcessingInstruction(target, "");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        try {
            if (getIgnoreProcesingInstructions()) {
                return;
            }
            encodeTerminationAndCurrentElement(true);
            encodeProcessingInstruction(target, data);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCData(String text) throws XMLStreamException {
        try {
            int length = text.length();
            if (length == 0) {
                return;
            }
            if (length < this._charBuffer.length) {
                if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(text)) {
                    return;
                }
                encodeTerminationAndCurrentElement(true);
                text.getChars(0, length, this._charBuffer, 0);
                encodeCIIBuiltInAlgorithmDataAsCDATA(this._charBuffer, 0, length);
            } else {
                char[] ch = text.toCharArray();
                if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, 0, length)) {
                    return;
                }
                encodeTerminationAndCurrentElement(true);
                encodeCIIBuiltInAlgorithmDataAsCDATA(ch, 0, length);
            }
        } catch (Exception e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeDTD(String dtd) throws XMLStreamException {
        throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.notImplemented"));
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeEntityRef(String name) throws XMLStreamException {
        throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.notImplemented"));
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(String text) throws XMLStreamException {
        try {
            int length = text.length();
            if (length == 0) {
                return;
            }
            if (length < this._charBuffer.length) {
                if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(text)) {
                    return;
                }
                encodeTerminationAndCurrentElement(true);
                text.getChars(0, length, this._charBuffer, 0);
                encodeCharacters(this._charBuffer, 0, length);
            } else {
                char[] ch = text.toCharArray();
                if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, 0, length)) {
                    return;
                }
                encodeTerminationAndCurrentElement(true);
                encodeCharactersNoClone(ch, 0, length);
            }
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        if (len <= 0) {
            return;
        }
        try {
            if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(text, start, len)) {
                return;
            }
            encodeTerminationAndCurrentElement(true);
            encodeCharacters(text, start, len);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public String getPrefix(String uri) throws XMLStreamException {
        return this._nsContext.getPrefix(uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        if (this._stackCount > -1 && !this._nsSupportContextStack[this._stackCount]) {
            this._nsSupportContextStack[this._stackCount] = true;
            this._nsContext.pushContext();
        }
        this._nsContext.declarePrefix(prefix, uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setDefaultNamespace(String uri) throws XMLStreamException {
        setPrefix("", uri);
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
        throw new UnsupportedOperationException("setNamespaceContext");
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public NamespaceContext getNamespaceContext() {
        return this._nsContext;
    }

    @Override // javax.xml.stream.XMLStreamWriter
    public Object getProperty(String name) throws IllegalArgumentException {
        if (this._manager != null) {
            return this._manager.getProperty(name);
        }
        return null;
    }

    public void setManager(StAXManager manager) {
        this._manager = manager;
    }

    public void setEncoding(String encoding) {
        this._encoding = encoding;
    }

    public void writeOctets(byte[] b2, int start, int len) throws XMLStreamException {
        if (len == 0) {
            return;
        }
        try {
            encodeTerminationAndCurrentElement(true);
            encodeCIIOctetAlgorithmData(1, b2, start, len);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    protected void encodeTerminationAndCurrentElement(boolean terminateAfter) throws XMLStreamException {
        try {
            encodeTermination();
            if (this._inStartElement) {
                this._b = 0;
                if (this._attributesArrayIndex > 0) {
                    this._b |= 64;
                }
                if (this._namespacesArrayIndex > 0) {
                    write(this._b | 56);
                    int i2 = 0;
                    while (i2 < this._namespacesArrayIndex) {
                        int i3 = i2;
                        int i4 = i2 + 1;
                        i2 = i4 + 1;
                        encodeNamespaceAttribute(this._namespacesArray[i3], this._namespacesArray[i4]);
                    }
                    this._namespacesArrayIndex = 0;
                    write(240);
                    this._b = 0;
                }
                if (this._currentPrefix.length() == 0) {
                    if (this._currentUri.length() == 0) {
                        this._currentUri = this._nsContext.getNamespaceURI("");
                    } else {
                        String tmpPrefix = getPrefix(this._currentUri);
                        if (tmpPrefix != null) {
                            this._currentPrefix = tmpPrefix;
                        }
                    }
                }
                encodeElementQualifiedNameOnThirdBit(this._currentUri, this._currentPrefix, this._currentLocalName);
                int i5 = 0;
                while (i5 < this._attributesArrayIndex) {
                    int i6 = i5;
                    int i7 = i5 + 1;
                    String str = this._attributesArray[i6];
                    int i8 = i7 + 1;
                    String str2 = this._attributesArray[i7];
                    int i9 = i8 + 1;
                    encodeAttributeQualifiedNameOnSecondBit(str, str2, this._attributesArray[i8]);
                    String value = this._attributesArray[i9];
                    i5 = i9 + 1;
                    this._attributesArray[i9] = null;
                    boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
                    encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, false);
                    this._b = 240;
                    this._terminate = true;
                }
                this._attributesArrayIndex = 0;
                this._inStartElement = false;
                if (this._isEmptyElement) {
                    encodeElementTermination();
                    boolean[] zArr = this._nsSupportContextStack;
                    int i10 = this._stackCount;
                    this._stackCount = i10 - 1;
                    if (zArr[i10]) {
                        this._nsContext.popContext();
                    }
                    this._isEmptyElement = false;
                }
                if (terminateAfter) {
                    encodeTermination();
                }
            }
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void initiateLowLevelWriting() throws XMLStreamException {
        encodeTerminationAndCurrentElement(false);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final int getNextElementIndex() {
        return this._v.elementName.getNextIndex();
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final int getNextAttributeIndex() {
        return this._v.attributeName.getNextIndex();
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final int getLocalNameIndex() {
        return this._v.localName.getIndex();
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final int getNextLocalNameIndex() {
        return this._v.localName.getNextIndex();
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelTerminationAndMark() throws IOException {
        encodeTermination();
        mark();
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelStartElementIndexed(int type, int index) throws IOException {
        this._b = type;
        encodeNonZeroIntegerOnThirdBit(index);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final boolean writeLowLevelStartElement(int type, String prefix, String localName, String namespaceURI) throws IOException {
        boolean isIndexed = encodeElement(type, namespaceURI, prefix, localName);
        if (!isIndexed) {
            encodeLiteral(type | 60, namespaceURI, prefix, localName);
        }
        return isIndexed;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelStartNamespaces() throws IOException {
        write(56);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelNamespace(String prefix, String namespaceName) throws IOException {
        encodeNamespaceAttribute(prefix, namespaceName);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelEndNamespaces() throws IOException {
        write(240);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelStartAttributes() throws IOException {
        if (hasMark()) {
            byte[] bArr = this._octetBuffer;
            int i2 = this._markIndex;
            bArr[i2] = (byte) (bArr[i2] | 64);
            resetMark();
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelAttributeIndexed(int index) throws IOException {
        encodeNonZeroIntegerOnSecondBitFirstBitZero(index);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final boolean writeLowLevelAttribute(String prefix, String namespaceURI, String localName) throws IOException {
        boolean isIndexed = encodeAttribute(namespaceURI, prefix, localName);
        if (!isIndexed) {
            encodeLiteral(120, namespaceURI, prefix, localName);
        }
        return isIndexed;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelAttributeValue(String value) throws IOException {
        boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
        encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, false);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelStartNameLiteral(int type, String prefix, byte[] utf8LocalName, String namespaceURI) throws IOException {
        encodeLiteralHeader(type, namespaceURI, prefix);
        encodeNonZeroOctetStringLengthOnSecondBit(utf8LocalName.length);
        write(utf8LocalName, 0, utf8LocalName.length);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelStartNameLiteral(int type, String prefix, int localNameIndex, String namespaceURI) throws IOException {
        encodeLiteralHeader(type, namespaceURI, prefix);
        encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelEndStartElement() throws IOException {
        if (hasMark()) {
            resetMark();
        } else {
            this._b = 240;
            this._terminate = true;
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelEndElement() throws IOException {
        encodeElementTermination();
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelText(char[] text, int length) throws IOException {
        if (length == 0) {
            return;
        }
        encodeTermination();
        encodeCharacters(text, 0, length);
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelText(String text) throws IOException {
        int length = text.length();
        if (length == 0) {
            return;
        }
        encodeTermination();
        if (length < this._charBuffer.length) {
            text.getChars(0, length, this._charBuffer, 0);
            encodeCharacters(this._charBuffer, 0, length);
        } else {
            char[] ch = text.toCharArray();
            encodeCharactersNoClone(ch, 0, length);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.LowLevelFastInfosetStreamWriter
    public final void writeLowLevelOctets(byte[] octets, int length) throws IOException {
        if (length == 0) {
            return;
        }
        encodeTermination();
        encodeCIIOctetAlgorithmData(1, octets, 0, length);
    }

    private boolean encodeElement(int type, String namespaceURI, String prefix, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(localName);
        for (int i2 = 0; i2 < entry._valueIndex; i2++) {
            QualifiedName name = entry._value[i2];
            if ((prefix == name.prefix || prefix.equals(name.prefix)) && (namespaceURI == name.namespaceName || namespaceURI.equals(name.namespaceName))) {
                this._b = type;
                encodeNonZeroIntegerOnThirdBit(name.index);
                return true;
            }
        }
        entry.addQualifiedName(new QualifiedName(prefix, namespaceURI, localName, "", this._v.elementName.getNextIndex()));
        return false;
    }

    private boolean encodeAttribute(String namespaceURI, String prefix, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(localName);
        for (int i2 = 0; i2 < entry._valueIndex; i2++) {
            QualifiedName name = entry._value[i2];
            if ((prefix == name.prefix || prefix.equals(name.prefix)) && (namespaceURI == name.namespaceName || namespaceURI.equals(name.namespaceName))) {
                encodeNonZeroIntegerOnSecondBitFirstBitZero(name.index);
                return true;
            }
        }
        entry.addQualifiedName(new QualifiedName(prefix, namespaceURI, localName, "", this._v.attributeName.getNextIndex()));
        return false;
    }

    private void encodeLiteralHeader(int type, String namespaceURI, String prefix) throws IOException {
        if (namespaceURI != "") {
            int type2 = type | 1;
            if (prefix != "") {
                type2 |= 2;
            }
            write(type2);
            if (prefix != "") {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(this._v.prefix.get(prefix));
            }
            encodeNonZeroIntegerOnSecondBitFirstBitOne(this._v.namespaceName.get(namespaceURI));
            return;
        }
        write(type);
    }

    private void encodeLiteral(int type, String namespaceURI, String prefix, String localName) throws IOException {
        encodeLiteralHeader(type, namespaceURI, prefix);
        int localNameIndex = this._v.localName.obtainIndex(localName);
        if (localNameIndex == -1) {
            encodeNonEmptyOctetStringOnSecondBit(localName);
        } else {
            encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
        }
    }
}
