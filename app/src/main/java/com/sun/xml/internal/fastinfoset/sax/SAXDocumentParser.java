package com.sun.xml.internal.fastinfoset.sax;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.Decoder;
import com.sun.xml.internal.fastinfoset.DecoderStateTables;
import com.sun.xml.internal.fastinfoset.EncodingConstants;
import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmState;
import com.sun.xml.internal.fastinfoset.util.CharArray;
import com.sun.xml.internal.fastinfoset.util.CharArrayString;
import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser;
import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler;
import com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader;
import com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/sax/SAXDocumentParser.class */
public class SAXDocumentParser extends Decoder implements FastInfosetReader {
    private static final Logger logger = Logger.getLogger(SAXDocumentParser.class.getName());
    protected EntityResolver _entityResolver;
    protected DTDHandler _dtdHandler;
    protected ContentHandler _contentHandler;
    protected ErrorHandler _errorHandler;
    protected LexicalHandler _lexicalHandler;
    protected DeclHandler _declHandler;
    protected EncodingAlgorithmContentHandler _algorithmHandler;
    protected PrimitiveTypeContentHandler _primitiveHandler;
    protected AttributesHolder _attributes;
    protected int _namespacePrefixesIndex;
    protected boolean _namespacePrefixesFeature = false;
    protected BuiltInEncodingAlgorithmState builtInAlgorithmState = new BuiltInEncodingAlgorithmState();
    protected int[] _namespacePrefixes = new int[16];
    protected boolean _clearAttributes = false;

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/sax/SAXDocumentParser$LexicalHandlerImpl.class */
    private static final class LexicalHandlerImpl implements LexicalHandler {
        private LexicalHandlerImpl() {
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void comment(char[] ch, int start, int end) {
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void startDTD(String name, String publicId, String systemId) {
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void endDTD() {
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void startEntity(String name) {
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void endEntity(String name) {
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void startCDATA() {
        }

        @Override // org.xml.sax.ext.LexicalHandler
        public void endCDATA() {
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/sax/SAXDocumentParser$DeclHandlerImpl.class */
    private static final class DeclHandlerImpl implements DeclHandler {
        private DeclHandlerImpl() {
        }

        @Override // org.xml.sax.ext.DeclHandler
        public void elementDecl(String name, String model) throws SAXException {
        }

        @Override // org.xml.sax.ext.DeclHandler
        public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException {
        }

        @Override // org.xml.sax.ext.DeclHandler
        public void internalEntityDecl(String name, String value) throws SAXException {
        }

        @Override // org.xml.sax.ext.DeclHandler
        public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
        }
    }

    public SAXDocumentParser() {
        DefaultHandler handler = new DefaultHandler();
        this._attributes = new AttributesHolder(this._registeredEncodingAlgorithms);
        this._entityResolver = handler;
        this._dtdHandler = handler;
        this._contentHandler = handler;
        this._errorHandler = handler;
        this._lexicalHandler = new LexicalHandlerImpl();
        this._declHandler = new DeclHandlerImpl();
    }

    protected void resetOnError() {
        this._clearAttributes = false;
        this._attributes.clear();
        this._namespacePrefixesIndex = 0;
        if (this._v != null) {
            this._v.prefix.clearCompletely();
        }
        this._duplicateAttributeVerifier.clear();
    }

    @Override // org.xml.sax.XMLReader
    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http://xml.org/sax/features/namespaces")) {
            return true;
        }
        if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
            return this._namespacePrefixesFeature;
        }
        if (name.equals("http://xml.org/sax/features/string-interning") || name.equals(FastInfosetParser.STRING_INTERNING_PROPERTY)) {
            return getStringInterning();
        }
        throw new SAXNotRecognizedException(CommonResourceBundle.getInstance().getString("message.featureNotSupported") + name);
    }

    @Override // org.xml.sax.XMLReader
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http://xml.org/sax/features/namespaces")) {
            if (!value) {
                throw new SAXNotSupportedException(name + CallSiteDescriptor.TOKEN_DELIMITER + value);
            }
        } else if (name.equals("http://xml.org/sax/features/namespace-prefixes")) {
            this._namespacePrefixesFeature = value;
        } else {
            if (name.equals("http://xml.org/sax/features/string-interning") || name.equals(FastInfosetParser.STRING_INTERNING_PROPERTY)) {
                setStringInterning(value);
                return;
            }
            throw new SAXNotRecognizedException(CommonResourceBundle.getInstance().getString("message.featureNotSupported") + name);
        }
    }

    @Override // org.xml.sax.XMLReader
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http://xml.org/sax/properties/lexical-handler")) {
            return getLexicalHandler();
        }
        if (name.equals(Properties.DTD_DECLARATION_HANDLER_PROPERTY)) {
            return getDeclHandler();
        }
        if (name.equals("http://jvnet.org/fastinfoset/parser/properties/external-vocabularies")) {
            return getExternalVocabularies();
        }
        if (name.equals("http://jvnet.org/fastinfoset/parser/properties/registered-encoding-algorithms")) {
            return getRegisteredEncodingAlgorithms();
        }
        if (name.equals(FastInfosetReader.ENCODING_ALGORITHM_CONTENT_HANDLER_PROPERTY)) {
            return getEncodingAlgorithmContentHandler();
        }
        if (name.equals(FastInfosetReader.PRIMITIVE_TYPE_CONTENT_HANDLER_PROPERTY)) {
            return getPrimitiveTypeContentHandler();
        }
        throw new SAXNotRecognizedException(CommonResourceBundle.getInstance().getString("message.propertyNotRecognized", new Object[]{name}));
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
        if (name.equals(Properties.DTD_DECLARATION_HANDLER_PROPERTY)) {
            if (value instanceof DeclHandler) {
                setDeclHandler((DeclHandler) value);
                return;
            }
            throw new SAXNotSupportedException("http://xml.org/sax/properties/lexical-handler");
        }
        if (name.equals("http://jvnet.org/fastinfoset/parser/properties/external-vocabularies")) {
            if (value instanceof Map) {
                setExternalVocabularies((Map) value);
                return;
            }
            throw new SAXNotSupportedException("http://jvnet.org/fastinfoset/parser/properties/external-vocabularies");
        }
        if (name.equals("http://jvnet.org/fastinfoset/parser/properties/registered-encoding-algorithms")) {
            if (value instanceof Map) {
                setRegisteredEncodingAlgorithms((Map) value);
                return;
            }
            throw new SAXNotSupportedException("http://jvnet.org/fastinfoset/parser/properties/registered-encoding-algorithms");
        }
        if (name.equals(FastInfosetReader.ENCODING_ALGORITHM_CONTENT_HANDLER_PROPERTY)) {
            if (value instanceof EncodingAlgorithmContentHandler) {
                setEncodingAlgorithmContentHandler((EncodingAlgorithmContentHandler) value);
                return;
            }
            throw new SAXNotSupportedException(FastInfosetReader.ENCODING_ALGORITHM_CONTENT_HANDLER_PROPERTY);
        }
        if (name.equals(FastInfosetReader.PRIMITIVE_TYPE_CONTENT_HANDLER_PROPERTY)) {
            if (value instanceof PrimitiveTypeContentHandler) {
                setPrimitiveTypeContentHandler((PrimitiveTypeContentHandler) value);
                return;
            }
            throw new SAXNotSupportedException(FastInfosetReader.PRIMITIVE_TYPE_CONTENT_HANDLER_PROPERTY);
        }
        if (name.equals("http://jvnet.org/fastinfoset/parser/properties/buffer-size")) {
            if (value instanceof Integer) {
                setBufferSize(((Integer) value).intValue());
                return;
            }
            throw new SAXNotSupportedException("http://jvnet.org/fastinfoset/parser/properties/buffer-size");
        }
        throw new SAXNotRecognizedException(CommonResourceBundle.getInstance().getString("message.propertyNotRecognized", new Object[]{name}));
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

    @Override // org.xml.sax.XMLReader
    public void parse(InputSource input) throws SAXException, IOException {
        try {
            InputStream s2 = input.getByteStream();
            if (s2 == null) {
                String systemId = input.getSystemId();
                if (systemId == null) {
                    throw new SAXException(CommonResourceBundle.getInstance().getString("message.inputSource"));
                }
                parse(systemId);
            } else {
                parse(s2);
            }
        } catch (FastInfosetException e2) {
            logger.log(Level.FINE, "parsing error", (Throwable) e2);
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.XMLReader
    public void parse(String systemId) throws SAXException, IOException {
        try {
            parse(new URL(SystemIdResolver.getAbsoluteURI(systemId)).openStream());
        } catch (FastInfosetException e2) {
            logger.log(Level.FINE, "parsing error", (Throwable) e2);
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader
    public final void parse(InputStream s2) throws FastInfosetException, SAXException, IOException {
        setInputStream(s2);
        parse();
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader
    public void setLexicalHandler(LexicalHandler handler) {
        this._lexicalHandler = handler;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader
    public LexicalHandler getLexicalHandler() {
        return this._lexicalHandler;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader
    public void setDeclHandler(DeclHandler handler) {
        this._declHandler = handler;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader
    public DeclHandler getDeclHandler() {
        return this._declHandler;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader
    public void setEncodingAlgorithmContentHandler(EncodingAlgorithmContentHandler handler) {
        this._algorithmHandler = handler;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader
    public EncodingAlgorithmContentHandler getEncodingAlgorithmContentHandler() {
        return this._algorithmHandler;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader
    public void setPrimitiveTypeContentHandler(PrimitiveTypeContentHandler handler) {
        this._primitiveHandler = handler;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetReader
    public PrimitiveTypeContentHandler getPrimitiveTypeContentHandler() {
        return this._primitiveHandler;
    }

    public final void parse() throws FastInfosetException, IOException {
        if (this._octetBuffer.length < this._bufferSize) {
            this._octetBuffer = new byte[this._bufferSize];
        }
        try {
            reset();
            decodeHeader();
            if (this._parseFragments) {
                processDIIFragment();
            } else {
                processDII();
            }
        } catch (FastInfosetException e2) {
            try {
                this._errorHandler.fatalError(new SAXParseException(e2.getClass().getName(), null, e2));
            } catch (Exception e3) {
            }
            resetOnError();
            throw e2;
        } catch (IOException e4) {
            try {
                this._errorHandler.fatalError(new SAXParseException(e4.getClass().getName(), null, e4));
            } catch (Exception e5) {
            }
            resetOnError();
            throw e4;
        } catch (RuntimeException e6) {
            try {
                this._errorHandler.fatalError(new SAXParseException(e6.getClass().getName(), null, e6));
            } catch (Exception e7) {
            }
            resetOnError();
            throw new FastInfosetException(e6);
        }
    }

    protected final void processDII() throws FastInfosetException, IOException {
        try {
            this._contentHandler.startDocument();
            this._b = read();
            if (this._b > 0) {
                processDIIOptionalProperties();
            }
            boolean firstElementHasOccured = false;
            boolean documentTypeDeclarationOccured = false;
            while (true) {
                if (!this._terminate || !firstElementHasOccured) {
                    this._b = read();
                    switch (DecoderStateTables.DII(this._b)) {
                        case 0:
                            processEII(this._elementNameTable._array[this._b], false);
                            firstElementHasOccured = true;
                            continue;
                        case 1:
                            processEII(this._elementNameTable._array[this._b & 31], true);
                            firstElementHasOccured = true;
                            continue;
                        case 2:
                            processEII(decodeEIIIndexMedium(), (this._b & 64) > 0);
                            firstElementHasOccured = true;
                            continue;
                        case 3:
                            processEII(decodeEIIIndexLarge(), (this._b & 64) > 0);
                            firstElementHasOccured = true;
                            continue;
                        case 4:
                            processEIIWithNamespaces();
                            firstElementHasOccured = true;
                            continue;
                        case 5:
                            QualifiedName qn = decodeLiteralQualifiedName(this._b & 3, this._elementNameTable.getNext());
                            this._elementNameTable.add(qn);
                            processEII(qn, (this._b & 64) > 0);
                            firstElementHasOccured = true;
                            continue;
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 21:
                        default:
                            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingDII"));
                        case 18:
                            processCommentII();
                            continue;
                        case 19:
                            processProcessingII();
                            continue;
                        case 20:
                            if (documentTypeDeclarationOccured) {
                                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.secondOccurenceOfDTDII"));
                            }
                            documentTypeDeclarationOccured = true;
                            String strDecodeIdentifyingNonEmptyStringOnFirstBit = (this._b & 2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
                            String strDecodeIdentifyingNonEmptyStringOnFirstBit2 = (this._b & 1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
                            this._b = read();
                            while (this._b == 225) {
                                switch (decodeNonIdentifyingStringOnFirstBit()) {
                                    case 0:
                                        if (!this._addToTable) {
                                            break;
                                        } else {
                                            this._v.otherString.add(new CharArray(this._charBuffer, 0, this._charBufferLength, true));
                                            break;
                                        }
                                    case 2:
                                        throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
                                }
                                this._b = read();
                            }
                            if ((this._b & 240) != 240) {
                                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingInstructionIIsNotTerminatedCorrectly"));
                            }
                            if (this._b == 255) {
                                this._terminate = true;
                            }
                            if (this._notations != null) {
                                this._notations.clear();
                            }
                            if (this._unparsedEntities != null) {
                                this._unparsedEntities.clear();
                            } else {
                                continue;
                            }
                            break;
                        case 22:
                            break;
                        case 23:
                            this._doubleTerminate = true;
                            break;
                    }
                    this._terminate = true;
                } else {
                    while (!this._terminate) {
                        this._b = read();
                        switch (DecoderStateTables.DII(this._b)) {
                            case 18:
                                processCommentII();
                                continue;
                            case 19:
                                processProcessingII();
                                continue;
                            case 20:
                            case 21:
                            default:
                                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingDII"));
                            case 22:
                                break;
                            case 23:
                                this._doubleTerminate = true;
                                break;
                        }
                        this._terminate = true;
                    }
                    try {
                        this._contentHandler.endDocument();
                        return;
                    } catch (SAXException e2) {
                        throw new FastInfosetException("processDII", e2);
                    }
                }
            }
        } catch (SAXException e3) {
            throw new FastInfosetException("processDII", e3);
        }
    }

    protected final void processDIIFragment() throws FastInfosetException, IOException {
        try {
            this._contentHandler.startDocument();
            this._b = read();
            if (this._b > 0) {
                processDIIOptionalProperties();
            }
            while (!this._terminate) {
                this._b = read();
                switch (DecoderStateTables.EII(this._b)) {
                    case 0:
                        processEII(this._elementNameTable._array[this._b], false);
                        continue;
                    case 1:
                        processEII(this._elementNameTable._array[this._b & 31], true);
                        continue;
                    case 2:
                        processEII(decodeEIIIndexMedium(), (this._b & 64) > 0);
                        continue;
                    case 3:
                        processEII(decodeEIIIndexLarge(), (this._b & 64) > 0);
                        continue;
                    case 4:
                        processEIIWithNamespaces();
                        continue;
                    case 5:
                        QualifiedName qn = decodeLiteralQualifiedName(this._b & 3, this._elementNameTable.getNext());
                        this._elementNameTable.add(qn);
                        processEII(qn, (this._b & 64) > 0);
                        continue;
                    case 6:
                        this._octetBufferLength = (this._b & 1) + 1;
                        processUtf8CharacterString();
                        continue;
                    case 7:
                        this._octetBufferLength = read() + 3;
                        processUtf8CharacterString();
                        continue;
                    case 8:
                        this._octetBufferLength = ((read() << 24) | (read() << 16) | (read() << 8) | read()) + 259;
                        processUtf8CharacterString();
                        continue;
                    case 9:
                        this._octetBufferLength = (this._b & 1) + 1;
                        decodeUtf16StringAsCharBuffer();
                        if ((this._b & 16) > 0) {
                            this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                        }
                        try {
                            this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
                            continue;
                        } catch (SAXException e2) {
                            throw new FastInfosetException("processCII", e2);
                        }
                    case 10:
                        this._octetBufferLength = read() + 3;
                        decodeUtf16StringAsCharBuffer();
                        if ((this._b & 16) > 0) {
                            this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                        }
                        try {
                            this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
                            continue;
                        } catch (SAXException e3) {
                            throw new FastInfosetException("processCII", e3);
                        }
                    case 11:
                        this._octetBufferLength = ((read() << 24) | (read() << 16) | (read() << 8) | read()) + 259;
                        decodeUtf16StringAsCharBuffer();
                        if ((this._b & 16) > 0) {
                            this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                        }
                        try {
                            this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
                            continue;
                        } catch (SAXException e4) {
                            throw new FastInfosetException("processCII", e4);
                        }
                    case 12:
                        boolean addToTable = (this._b & 16) > 0;
                        this._identifier = (this._b & 2) << 6;
                        this._b = read();
                        this._identifier |= (this._b & 252) >> 2;
                        decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
                        decodeRestrictedAlphabetAsCharBuffer();
                        if (addToTable) {
                            this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                        }
                        try {
                            this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
                            continue;
                        } catch (SAXException e5) {
                            throw new FastInfosetException("processCII", e5);
                        }
                    case 13:
                        boolean addToTable2 = (this._b & 16) > 0;
                        this._identifier = (this._b & 2) << 6;
                        this._b = read();
                        this._identifier |= (this._b & 252) >> 2;
                        decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
                        processCIIEncodingAlgorithm(addToTable2);
                        continue;
                    case 14:
                        int index = this._b & 15;
                        try {
                            this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
                            continue;
                        } catch (SAXException e6) {
                            throw new FastInfosetException("processCII", e6);
                        }
                    case 15:
                        int index2 = (((this._b & 3) << 8) | read()) + 16;
                        try {
                            this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index2], this._characterContentChunkTable._length[index2]);
                            continue;
                        } catch (SAXException e7) {
                            throw new FastInfosetException("processCII", e7);
                        }
                    case 16:
                        int index3 = (((this._b & 3) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT;
                        try {
                            this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index3], this._characterContentChunkTable._length[index3]);
                            continue;
                        } catch (SAXException e8) {
                            throw new FastInfosetException("processCII", e8);
                        }
                    case 17:
                        int index4 = ((read() << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_4TH_BIT_LARGE_LIMIT;
                        try {
                            this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index4], this._characterContentChunkTable._length[index4]);
                            continue;
                        } catch (SAXException e9) {
                            throw new FastInfosetException("processCII", e9);
                        }
                    case 18:
                        processCommentII();
                        continue;
                    case 19:
                        processProcessingII();
                        continue;
                    case 20:
                    default:
                        throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
                    case 21:
                        String entity_reference_name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
                        String strDecodeIdentifyingNonEmptyStringOnFirstBit = (this._b & 2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
                        String strDecodeIdentifyingNonEmptyStringOnFirstBit2 = (this._b & 1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
                        try {
                            this._contentHandler.skippedEntity(entity_reference_name);
                            continue;
                        } catch (SAXException e10) {
                            throw new FastInfosetException("processUnexpandedEntityReferenceII", e10);
                        }
                    case 22:
                        break;
                    case 23:
                        this._doubleTerminate = true;
                        break;
                }
                this._terminate = true;
            }
            try {
                this._contentHandler.endDocument();
            } catch (SAXException e11) {
                throw new FastInfosetException("processDII", e11);
            }
        } catch (SAXException e12) {
            throw new FastInfosetException("processDII", e12);
        }
    }

    protected final void processDIIOptionalProperties() throws FastInfosetException, IOException {
        if (this._b == 32) {
            decodeInitialVocabulary();
            return;
        }
        if ((this._b & 64) > 0) {
            decodeAdditionalData();
        }
        if ((this._b & 32) > 0) {
            decodeInitialVocabulary();
        }
        if ((this._b & 16) > 0) {
            decodeNotations();
        }
        if ((this._b & 8) > 0) {
            decodeUnparsedEntities();
        }
        if ((this._b & 4) > 0) {
            decodeCharacterEncodingScheme();
        }
        if ((this._b & 2) > 0) {
            read();
        }
        if ((this._b & 1) > 0) {
            decodeVersion();
        }
    }

    protected final void processEII(QualifiedName name, boolean hasAttributes) throws FastInfosetException, IOException {
        if (this._prefixTable._currentInScope[name.prefixIndex] != name.namespaceNameIndex) {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameOfEIINotInScope"));
        }
        if (hasAttributes) {
            processAIIs();
        }
        try {
            this._contentHandler.startElement(name.namespaceName, name.localName, name.qName, this._attributes);
            if (this._clearAttributes) {
                this._attributes.clear();
                this._clearAttributes = false;
            }
            while (!this._terminate) {
                this._b = read();
                switch (DecoderStateTables.EII(this._b)) {
                    case 0:
                        processEII(this._elementNameTable._array[this._b], false);
                        continue;
                    case 1:
                        processEII(this._elementNameTable._array[this._b & 31], true);
                        continue;
                    case 2:
                        processEII(decodeEIIIndexMedium(), (this._b & 64) > 0);
                        continue;
                    case 3:
                        processEII(decodeEIIIndexLarge(), (this._b & 64) > 0);
                        continue;
                    case 4:
                        processEIIWithNamespaces();
                        continue;
                    case 5:
                        QualifiedName qn = decodeLiteralQualifiedName(this._b & 3, this._elementNameTable.getNext());
                        this._elementNameTable.add(qn);
                        processEII(qn, (this._b & 64) > 0);
                        continue;
                    case 6:
                        this._octetBufferLength = (this._b & 1) + 1;
                        processUtf8CharacterString();
                        continue;
                    case 7:
                        this._octetBufferLength = read() + 3;
                        processUtf8CharacterString();
                        continue;
                    case 8:
                        this._octetBufferLength = ((read() << 24) | (read() << 16) | (read() << 8) | read()) + 259;
                        processUtf8CharacterString();
                        continue;
                    case 9:
                        this._octetBufferLength = (this._b & 1) + 1;
                        decodeUtf16StringAsCharBuffer();
                        if ((this._b & 16) > 0) {
                            this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                        }
                        try {
                            this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
                            continue;
                        } catch (SAXException e2) {
                            throw new FastInfosetException("processCII", e2);
                        }
                    case 10:
                        this._octetBufferLength = read() + 3;
                        decodeUtf16StringAsCharBuffer();
                        if ((this._b & 16) > 0) {
                            this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                        }
                        try {
                            this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
                            continue;
                        } catch (SAXException e3) {
                            throw new FastInfosetException("processCII", e3);
                        }
                    case 11:
                        this._octetBufferLength = ((read() << 24) | (read() << 16) | (read() << 8) | read()) + 259;
                        decodeUtf16StringAsCharBuffer();
                        if ((this._b & 16) > 0) {
                            this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                        }
                        try {
                            this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
                            continue;
                        } catch (SAXException e4) {
                            throw new FastInfosetException("processCII", e4);
                        }
                    case 12:
                        boolean addToTable = (this._b & 16) > 0;
                        this._identifier = (this._b & 2) << 6;
                        this._b = read();
                        this._identifier |= (this._b & 252) >> 2;
                        decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
                        decodeRestrictedAlphabetAsCharBuffer();
                        if (addToTable) {
                            this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                        }
                        try {
                            this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
                            continue;
                        } catch (SAXException e5) {
                            throw new FastInfosetException("processCII", e5);
                        }
                    case 13:
                        boolean addToTable2 = (this._b & 16) > 0;
                        this._identifier = (this._b & 2) << 6;
                        this._b = read();
                        this._identifier |= (this._b & 252) >> 2;
                        decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
                        processCIIEncodingAlgorithm(addToTable2);
                        continue;
                    case 14:
                        int index = this._b & 15;
                        try {
                            this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index], this._characterContentChunkTable._length[index]);
                            continue;
                        } catch (SAXException e6) {
                            throw new FastInfosetException("processCII", e6);
                        }
                    case 15:
                        int index2 = (((this._b & 3) << 8) | read()) + 16;
                        try {
                            this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index2], this._characterContentChunkTable._length[index2]);
                            continue;
                        } catch (SAXException e7) {
                            throw new FastInfosetException("processCII", e7);
                        }
                    case 16:
                        int index3 = (((this._b & 3) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT;
                        try {
                            this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index3], this._characterContentChunkTable._length[index3]);
                            continue;
                        } catch (SAXException e8) {
                            throw new FastInfosetException("processCII", e8);
                        }
                    case 17:
                        int index4 = ((read() << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_4TH_BIT_LARGE_LIMIT;
                        try {
                            this._contentHandler.characters(this._characterContentChunkTable._array, this._characterContentChunkTable._offset[index4], this._characterContentChunkTable._length[index4]);
                            continue;
                        } catch (SAXException e9) {
                            throw new FastInfosetException("processCII", e9);
                        }
                    case 18:
                        processCommentII();
                        continue;
                    case 19:
                        processProcessingII();
                        continue;
                    case 20:
                    default:
                        throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
                    case 21:
                        String entity_reference_name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
                        String strDecodeIdentifyingNonEmptyStringOnFirstBit = (this._b & 2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
                        String strDecodeIdentifyingNonEmptyStringOnFirstBit2 = (this._b & 1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
                        try {
                            this._contentHandler.skippedEntity(entity_reference_name);
                            continue;
                        } catch (SAXException e10) {
                            throw new FastInfosetException("processUnexpandedEntityReferenceII", e10);
                        }
                    case 22:
                        break;
                    case 23:
                        this._doubleTerminate = true;
                        break;
                }
                this._terminate = true;
            }
            this._terminate = this._doubleTerminate;
            this._doubleTerminate = false;
            try {
                this._contentHandler.endElement(name.namespaceName, name.localName, name.qName);
            } catch (SAXException e11) {
                throw new FastInfosetException("processEII", e11);
            }
        } catch (SAXException e12) {
            logger.log(Level.FINE, "processEII error", (Throwable) e12);
            throw new FastInfosetException("processEII", e12);
        }
    }

    private final void processUtf8CharacterString() throws FastInfosetException, IOException {
        if ((this._b & 16) > 0) {
            this._characterContentChunkTable.ensureSize(this._octetBufferLength);
            int charactersOffset = this._characterContentChunkTable._arrayIndex;
            decodeUtf8StringAsCharBuffer(this._characterContentChunkTable._array, charactersOffset);
            this._characterContentChunkTable.add(this._charBufferLength);
            try {
                this._contentHandler.characters(this._characterContentChunkTable._array, charactersOffset, this._charBufferLength);
                return;
            } catch (SAXException e2) {
                throw new FastInfosetException("processCII", e2);
            }
        }
        decodeUtf8StringAsCharBuffer();
        try {
            this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
        } catch (SAXException e3) {
            throw new FastInfosetException("processCII", e3);
        }
    }

    protected final void processEIIWithNamespaces() throws FastInfosetException, IOException {
        boolean hasAttributes = (this._b & 64) > 0;
        this._clearAttributes = this._namespacePrefixesFeature;
        PrefixArray prefixArray = this._prefixTable;
        int i2 = prefixArray._declarationId + 1;
        prefixArray._declarationId = i2;
        if (i2 == Integer.MAX_VALUE) {
            this._prefixTable.clearDeclarationIds();
        }
        String prefix = "";
        String namespaceName = "";
        int start = this._namespacePrefixesIndex;
        int i3 = read();
        while (true) {
            int b2 = i3;
            if ((b2 & 252) == 204) {
                if (this._namespacePrefixesIndex == this._namespacePrefixes.length) {
                    int[] namespaceAIIs = new int[((this._namespacePrefixesIndex * 3) / 2) + 1];
                    System.arraycopy(this._namespacePrefixes, 0, namespaceAIIs, 0, this._namespacePrefixesIndex);
                    this._namespacePrefixes = namespaceAIIs;
                }
                switch (b2 & 3) {
                    case 0:
                        namespaceName = "";
                        prefix = "";
                        int[] iArr = this._namespacePrefixes;
                        int i4 = this._namespacePrefixesIndex;
                        this._namespacePrefixesIndex = i4 + 1;
                        iArr[i4] = -1;
                        this._prefixIndex = -1;
                        this._namespaceNameIndex = -1;
                        break;
                    case 1:
                        prefix = "";
                        namespaceName = decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(false);
                        int[] iArr2 = this._namespacePrefixes;
                        int i5 = this._namespacePrefixesIndex;
                        this._namespacePrefixesIndex = i5 + 1;
                        iArr2[i5] = -1;
                        this._prefixIndex = -1;
                        break;
                    case 2:
                        prefix = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(false);
                        namespaceName = "";
                        this._namespaceNameIndex = -1;
                        int[] iArr3 = this._namespacePrefixes;
                        int i6 = this._namespacePrefixesIndex;
                        this._namespacePrefixesIndex = i6 + 1;
                        iArr3[i6] = this._prefixIndex;
                        break;
                    case 3:
                        prefix = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(true);
                        namespaceName = decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(true);
                        int[] iArr4 = this._namespacePrefixes;
                        int i7 = this._namespacePrefixesIndex;
                        this._namespacePrefixesIndex = i7 + 1;
                        iArr4[i7] = this._prefixIndex;
                        break;
                }
                this._prefixTable.pushScope(this._prefixIndex, this._namespaceNameIndex);
                if (this._namespacePrefixesFeature) {
                    if (prefix != "") {
                        this._attributes.addAttribute(new QualifiedName("xmlns", "http://www.w3.org/2000/xmlns/", prefix), namespaceName);
                    } else {
                        this._attributes.addAttribute(EncodingConstants.DEFAULT_NAMESPACE_DECLARATION, namespaceName);
                    }
                }
                try {
                    this._contentHandler.startPrefixMapping(prefix, namespaceName);
                    i3 = read();
                } catch (SAXException e2) {
                    throw new IOException("processStartNamespaceAII");
                }
            } else {
                if (b2 != 240) {
                    throw new IOException(CommonResourceBundle.getInstance().getString("message.EIInamespaceNameNotTerminatedCorrectly"));
                }
                int end = this._namespacePrefixesIndex;
                this._b = read();
                switch (DecoderStateTables.EII(this._b)) {
                    case 0:
                        processEII(this._elementNameTable._array[this._b], hasAttributes);
                        break;
                    case 1:
                    case 4:
                    default:
                        throw new IOException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEIIAfterAIIs"));
                    case 2:
                        processEII(decodeEIIIndexMedium(), hasAttributes);
                        break;
                    case 3:
                        processEII(decodeEIIIndexLarge(), hasAttributes);
                        break;
                    case 5:
                        QualifiedName qn = decodeLiteralQualifiedName(this._b & 3, this._elementNameTable.getNext());
                        this._elementNameTable.add(qn);
                        processEII(qn, hasAttributes);
                        break;
                }
                try {
                    for (int i8 = end - 1; i8 >= start; i8--) {
                        int prefixIndex = this._namespacePrefixes[i8];
                        this._prefixTable.popScope(prefixIndex);
                        String prefix2 = prefixIndex > 0 ? this._prefixTable.get(prefixIndex - 1) : prefixIndex == -1 ? "" : "xml";
                        this._contentHandler.endPrefixMapping(prefix2);
                    }
                    this._namespacePrefixesIndex = start;
                    return;
                } catch (SAXException e3) {
                    throw new IOException("processStartNamespaceAII");
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00ee  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0204  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x02a0  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x02f1  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x036a  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0399  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x03a6 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected final void processAIIs() throws com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 985
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.fastinfoset.sax.SAXDocumentParser.processAIIs():void");
    }

    protected final void processCommentII() throws FastInfosetException, IOException {
        switch (decodeNonIdentifyingStringOnFirstBit()) {
            case 0:
                if (this._addToTable) {
                    this._v.otherString.add(new CharArray(this._charBuffer, 0, this._charBufferLength, true));
                }
                try {
                    this._lexicalHandler.comment(this._charBuffer, 0, this._charBufferLength);
                    return;
                } catch (SAXException e2) {
                    throw new FastInfosetException("processCommentII", e2);
                }
            case 1:
                CharArray ca = this._v.otherString.get(this._integer);
                try {
                    this._lexicalHandler.comment(ca.ch, ca.start, ca.length);
                    return;
                } catch (SAXException e3) {
                    throw new FastInfosetException("processCommentII", e3);
                }
            case 2:
                throw new IOException(CommonResourceBundle.getInstance().getString("message.commentIIAlgorithmNotSupported"));
            case 3:
                try {
                    this._lexicalHandler.comment(this._charBuffer, 0, 0);
                    return;
                } catch (SAXException e4) {
                    throw new FastInfosetException("processCommentII", e4);
                }
            default:
                return;
        }
    }

    protected final void processProcessingII() throws FastInfosetException, IOException {
        String target = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
        switch (decodeNonIdentifyingStringOnFirstBit()) {
            case 0:
                String data = new String(this._charBuffer, 0, this._charBufferLength);
                if (this._addToTable) {
                    this._v.otherString.add(new CharArrayString(data));
                }
                try {
                    this._contentHandler.processingInstruction(target, data);
                    return;
                } catch (SAXException e2) {
                    throw new FastInfosetException("processProcessingII", e2);
                }
            case 1:
                try {
                    this._contentHandler.processingInstruction(target, this._v.otherString.get(this._integer).toString());
                    return;
                } catch (SAXException e3) {
                    throw new FastInfosetException("processProcessingII", e3);
                }
            case 2:
                throw new IOException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
            case 3:
                try {
                    this._contentHandler.processingInstruction(target, "");
                    return;
                } catch (SAXException e4) {
                    throw new FastInfosetException("processProcessingII", e4);
                }
            default:
                return;
        }
    }

    protected final void processCIIEncodingAlgorithm(boolean addToTable) throws FastInfosetException, IOException {
        if (this._identifier < 9) {
            if (this._primitiveHandler != null) {
                processCIIBuiltInEncodingAlgorithmAsPrimitive();
            } else if (this._algorithmHandler != null) {
                Object array = processBuiltInEncodingAlgorithmAsObject();
                try {
                    this._algorithmHandler.object(null, this._identifier, array);
                } catch (SAXException e2) {
                    throw new FastInfosetException(e2);
                }
            } else {
                StringBuffer buffer = new StringBuffer();
                processBuiltInEncodingAlgorithmAsCharacters(buffer);
                try {
                    this._contentHandler.characters(buffer.toString().toCharArray(), 0, buffer.length());
                } catch (SAXException e3) {
                    throw new FastInfosetException(e3);
                }
            }
            if (addToTable) {
                StringBuffer buffer2 = new StringBuffer();
                processBuiltInEncodingAlgorithmAsCharacters(buffer2);
                this._characterContentChunkTable.add(buffer2.toString().toCharArray(), buffer2.length());
                return;
            }
            return;
        }
        if (this._identifier == 9) {
            this._octetBufferOffset -= this._octetBufferLength;
            decodeUtf8StringIntoCharBuffer();
            try {
                this._lexicalHandler.startCDATA();
                this._contentHandler.characters(this._charBuffer, 0, this._charBufferLength);
                this._lexicalHandler.endCDATA();
                if (addToTable) {
                    this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                    return;
                }
                return;
            } catch (SAXException e4) {
                throw new FastInfosetException(e4);
            }
        }
        if (this._identifier < 32 || this._algorithmHandler == null) {
            if (this._identifier >= 32) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmDataCannotBeReported"));
            }
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
        }
        String URI = this._v.encodingAlgorithm.get(this._identifier - 32);
        if (URI == null) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent", new Object[]{Integer.valueOf(this._identifier)}));
        }
        EncodingAlgorithm ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
        if (ea != null) {
            Object data = ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
            try {
                this._algorithmHandler.object(URI, this._identifier, data);
            } catch (SAXException e5) {
                throw new FastInfosetException(e5);
            }
        } else {
            try {
                this._algorithmHandler.octets(URI, this._identifier, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
            } catch (SAXException e6) {
                throw new FastInfosetException(e6);
            }
        }
        if (addToTable) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.addToTableNotSupported"));
        }
    }

    protected final void processCIIBuiltInEncodingAlgorithmAsPrimitive() throws FastInfosetException, IOException {
        try {
            switch (this._identifier) {
                case 0:
                case 1:
                    this._primitiveHandler.bytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                    break;
                case 2:
                    int length = BuiltInEncodingAlgorithmFactory.shortEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
                    if (length > this.builtInAlgorithmState.shortArray.length) {
                        short[] array = new short[((length * 3) / 2) + 1];
                        System.arraycopy(this.builtInAlgorithmState.shortArray, 0, array, 0, this.builtInAlgorithmState.shortArray.length);
                        this.builtInAlgorithmState.shortArray = array;
                    }
                    BuiltInEncodingAlgorithmFactory.shortEncodingAlgorithm.decodeFromBytesToShortArray(this.builtInAlgorithmState.shortArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                    this._primitiveHandler.shorts(this.builtInAlgorithmState.shortArray, 0, length);
                    break;
                case 3:
                    int length2 = BuiltInEncodingAlgorithmFactory.intEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
                    if (length2 > this.builtInAlgorithmState.intArray.length) {
                        int[] array2 = new int[((length2 * 3) / 2) + 1];
                        System.arraycopy(this.builtInAlgorithmState.intArray, 0, array2, 0, this.builtInAlgorithmState.intArray.length);
                        this.builtInAlgorithmState.intArray = array2;
                    }
                    BuiltInEncodingAlgorithmFactory.intEncodingAlgorithm.decodeFromBytesToIntArray(this.builtInAlgorithmState.intArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                    this._primitiveHandler.ints(this.builtInAlgorithmState.intArray, 0, length2);
                    break;
                case 4:
                    int length3 = BuiltInEncodingAlgorithmFactory.longEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
                    if (length3 > this.builtInAlgorithmState.longArray.length) {
                        long[] array3 = new long[((length3 * 3) / 2) + 1];
                        System.arraycopy(this.builtInAlgorithmState.longArray, 0, array3, 0, this.builtInAlgorithmState.longArray.length);
                        this.builtInAlgorithmState.longArray = array3;
                    }
                    BuiltInEncodingAlgorithmFactory.longEncodingAlgorithm.decodeFromBytesToLongArray(this.builtInAlgorithmState.longArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                    this._primitiveHandler.longs(this.builtInAlgorithmState.longArray, 0, length3);
                    break;
                case 5:
                    int length4 = BuiltInEncodingAlgorithmFactory.booleanEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength, this._octetBuffer[this._octetBufferStart] & 255);
                    if (length4 > this.builtInAlgorithmState.booleanArray.length) {
                        boolean[] array4 = new boolean[((length4 * 3) / 2) + 1];
                        System.arraycopy(this.builtInAlgorithmState.booleanArray, 0, array4, 0, this.builtInAlgorithmState.booleanArray.length);
                        this.builtInAlgorithmState.booleanArray = array4;
                    }
                    BuiltInEncodingAlgorithmFactory.booleanEncodingAlgorithm.decodeFromBytesToBooleanArray(this.builtInAlgorithmState.booleanArray, 0, length4, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                    this._primitiveHandler.booleans(this.builtInAlgorithmState.booleanArray, 0, length4);
                    break;
                case 6:
                    int length5 = BuiltInEncodingAlgorithmFactory.floatEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
                    if (length5 > this.builtInAlgorithmState.floatArray.length) {
                        float[] array5 = new float[((length5 * 3) / 2) + 1];
                        System.arraycopy(this.builtInAlgorithmState.floatArray, 0, array5, 0, this.builtInAlgorithmState.floatArray.length);
                        this.builtInAlgorithmState.floatArray = array5;
                    }
                    BuiltInEncodingAlgorithmFactory.floatEncodingAlgorithm.decodeFromBytesToFloatArray(this.builtInAlgorithmState.floatArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                    this._primitiveHandler.floats(this.builtInAlgorithmState.floatArray, 0, length5);
                    break;
                case 7:
                    int length6 = BuiltInEncodingAlgorithmFactory.doubleEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
                    if (length6 > this.builtInAlgorithmState.doubleArray.length) {
                        double[] array6 = new double[((length6 * 3) / 2) + 1];
                        System.arraycopy(this.builtInAlgorithmState.doubleArray, 0, array6, 0, this.builtInAlgorithmState.doubleArray.length);
                        this.builtInAlgorithmState.doubleArray = array6;
                    }
                    BuiltInEncodingAlgorithmFactory.doubleEncodingAlgorithm.decodeFromBytesToDoubleArray(this.builtInAlgorithmState.doubleArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                    this._primitiveHandler.doubles(this.builtInAlgorithmState.doubleArray, 0, length6);
                    break;
                case 8:
                    int length7 = BuiltInEncodingAlgorithmFactory.uuidEncodingAlgorithm.getPrimtiveLengthFromOctetLength(this._octetBufferLength);
                    if (length7 > this.builtInAlgorithmState.longArray.length) {
                        long[] array7 = new long[((length7 * 3) / 2) + 1];
                        System.arraycopy(this.builtInAlgorithmState.longArray, 0, array7, 0, this.builtInAlgorithmState.longArray.length);
                        this.builtInAlgorithmState.longArray = array7;
                    }
                    BuiltInEncodingAlgorithmFactory.uuidEncodingAlgorithm.decodeFromBytesToLongArray(this.builtInAlgorithmState.longArray, 0, this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                    this._primitiveHandler.uuids(this.builtInAlgorithmState.longArray, 0, length7);
                    break;
                case 9:
                    throw new UnsupportedOperationException("CDATA");
                default:
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.unsupportedAlgorithm", new Object[]{Integer.valueOf(this._identifier)}));
            }
        } catch (SAXException e2) {
            throw new FastInfosetException(e2);
        }
    }

    protected final void processAIIEncodingAlgorithm(QualifiedName name, boolean addToTable) throws FastInfosetException, IOException {
        if (this._identifier < 9) {
            if (this._primitiveHandler != null || this._algorithmHandler != null) {
                this._attributes.addAttributeWithAlgorithmData(name, null, this._identifier, processBuiltInEncodingAlgorithmAsObject());
            } else {
                StringBuffer buffer = new StringBuffer();
                processBuiltInEncodingAlgorithmAsCharacters(buffer);
                this._attributes.addAttribute(name, buffer.toString());
            }
        } else {
            if (this._identifier < 32 || this._algorithmHandler == null) {
                if (this._identifier >= 32) {
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmDataCannotBeReported"));
                }
                if (this._identifier == 9) {
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
                }
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
            }
            String URI = this._v.encodingAlgorithm.get(this._identifier - 32);
            if (URI == null) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent", new Object[]{Integer.valueOf(this._identifier)}));
            }
            EncodingAlgorithm ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
            if (ea != null) {
                this._attributes.addAttributeWithAlgorithmData(name, URI, this._identifier, ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength));
            } else {
                byte[] data = new byte[this._octetBufferLength];
                System.arraycopy(this._octetBuffer, this._octetBufferStart, data, 0, this._octetBufferLength);
                this._attributes.addAttributeWithAlgorithmData(name, URI, this._identifier, data);
            }
        }
        if (addToTable) {
            this._attributeValueTable.add(this._attributes.getValue(this._attributes.getIndex(name.qName)));
        }
    }

    protected final void processBuiltInEncodingAlgorithmAsCharacters(StringBuffer buffer) throws FastInfosetException, IOException {
        Object array = BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
        BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).convertToCharacters(array, buffer);
    }

    protected final Object processBuiltInEncodingAlgorithmAsObject() throws FastInfosetException, IOException {
        return BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
    }
}
