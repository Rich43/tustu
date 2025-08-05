package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.DOMCache;
import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.runtime.output.TransletOutputHandlerFactory;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
import com.sun.org.apache.xml.internal.utils.XMLReaderManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import jdk.xml.internal.JdkXmlUtils;
import jdk.xml.internal.XMLSecurityManager;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/TransformerImpl.class */
public final class TransformerImpl extends Transformer implements DOMCache, ErrorListener {
    private static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
    private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
    private AbstractTranslet _translet;
    private String _method;
    private String _encoding;
    private String _sourceSystemId;
    private ErrorListener _errorListener;
    private URIResolver _uriResolver;
    private Properties _properties;
    private Properties _propertiesClone;
    private TransletOutputHandlerFactory _tohFactory;
    private DOM _dom;
    private int _indentNumber;
    private TransformerFactoryImpl _tfactory;
    private OutputStream _ostream;
    private XSLTCDTMManager _dtmManager;
    private XMLReaderManager _readerManager;
    private boolean _isIdentity;
    private boolean _isSecureProcessing;
    private boolean _overrideDefaultParser;
    private String _accessExternalDTD;
    private XMLSecurityManager _securityManager;
    private Map<String, Object> _parameters;

    /* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/TransformerImpl$MessageHandler.class */
    static class MessageHandler extends com.sun.org.apache.xalan.internal.xsltc.runtime.MessageHandler {
        private ErrorListener _errorListener;

        public MessageHandler(ErrorListener errorListener) {
            this._errorListener = errorListener;
        }

        @Override // com.sun.org.apache.xalan.internal.xsltc.runtime.MessageHandler
        public void displayMessage(String msg) {
            if (this._errorListener == null) {
                System.err.println(msg);
            } else {
                try {
                    this._errorListener.warning(new TransformerException(msg));
                } catch (TransformerException e2) {
                }
            }
        }
    }

    protected TransformerImpl(Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory) {
        this(null, outputProperties, indentNumber, tfactory);
        this._isIdentity = true;
    }

    protected TransformerImpl(Translet translet, Properties outputProperties, int indentNumber, TransformerFactoryImpl tfactory) {
        this._translet = null;
        this._method = null;
        this._encoding = null;
        this._sourceSystemId = null;
        this._errorListener = this;
        this._uriResolver = null;
        this._tohFactory = null;
        this._dom = null;
        this._tfactory = null;
        this._ostream = null;
        this._dtmManager = null;
        this._isIdentity = false;
        this._isSecureProcessing = false;
        this._accessExternalDTD = "all";
        this._parameters = null;
        this._translet = (AbstractTranslet) translet;
        this._properties = createOutputProperties(outputProperties);
        this._propertiesClone = (Properties) this._properties.clone();
        this._indentNumber = indentNumber;
        this._tfactory = tfactory;
        this._overrideDefaultParser = this._tfactory.overrideDefaultParser();
        this._accessExternalDTD = (String) this._tfactory.getAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD");
        this._securityManager = (XMLSecurityManager) this._tfactory.getAttribute("http://apache.org/xml/properties/security-manager");
        this._readerManager = XMLReaderManager.getInstance(this._overrideDefaultParser);
        this._readerManager.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", this._accessExternalDTD);
        this._readerManager.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", this._isSecureProcessing);
        this._readerManager.setProperty("http://apache.org/xml/properties/security-manager", this._securityManager);
    }

    public boolean isSecureProcessing() {
        return this._isSecureProcessing;
    }

    public void setSecureProcessing(boolean flag) {
        this._isSecureProcessing = flag;
        this._readerManager.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", this._isSecureProcessing);
    }

    public boolean overrideDefaultParser() {
        return this._overrideDefaultParser;
    }

    public void setOverrideDefaultParser(boolean flag) {
        this._overrideDefaultParser = flag;
    }

    protected AbstractTranslet getTranslet() {
        return this._translet;
    }

    public boolean isIdentity() {
        return this._isIdentity;
    }

    @Override // javax.xml.transform.Transformer
    public void transform(Source source, Result result) throws TransformerException {
        if (!this._isIdentity) {
            if (this._translet == null) {
                ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_NO_TRANSLET_ERR);
                throw new TransformerException(err.toString());
            }
            transferOutputProperties(this._translet);
        }
        SerializationHandler toHandler = getOutputHandler(result);
        if (toHandler == null) {
            ErrorMsg err2 = new ErrorMsg(ErrorMsg.JAXP_NO_HANDLER_ERR);
            throw new TransformerException(err2.toString());
        }
        if (this._uriResolver != null && !this._isIdentity) {
            this._translet.setDOMCache(this);
        }
        if (this._isIdentity) {
            transferOutputProperties(toHandler);
        }
        transform(source, toHandler, this._encoding);
        try {
            if (result instanceof DOMResult) {
                ((DOMResult) result).setNode(this._tohFactory.getNode());
            } else if (result instanceof StAXResult) {
                if (((StAXResult) result).getXMLEventWriter() != null) {
                    this._tohFactory.getXMLEventWriter().flush();
                } else if (((StAXResult) result).getXMLStreamWriter() != null) {
                    this._tohFactory.getXMLStreamWriter().flush();
                }
            }
        } catch (Exception e2) {
            System.out.println("Result writing error");
        }
    }

    public SerializationHandler getOutputHandler(Result result) throws TransformerException {
        this._method = (String) this._properties.get("method");
        this._encoding = this._properties.getProperty("encoding");
        this._tohFactory = TransletOutputHandlerFactory.newInstance(this._overrideDefaultParser);
        this._tohFactory.setEncoding(this._encoding);
        if (this._method != null) {
            this._tohFactory.setOutputMethod(this._method);
        }
        if (this._indentNumber >= 0) {
            this._tohFactory.setIndentNumber(this._indentNumber);
        }
        try {
            if (result instanceof SAXResult) {
                SAXResult target = (SAXResult) result;
                ContentHandler handler = target.getHandler();
                this._tohFactory.setHandler(handler);
                LexicalHandler lexicalHandler = target.getLexicalHandler();
                if (lexicalHandler != null) {
                    this._tohFactory.setLexicalHandler(lexicalHandler);
                }
                this._tohFactory.setOutputType(1);
                return this._tohFactory.getSerializationHandler();
            }
            if (result instanceof StAXResult) {
                if (((StAXResult) result).getXMLEventWriter() != null) {
                    this._tohFactory.setXMLEventWriter(((StAXResult) result).getXMLEventWriter());
                } else if (((StAXResult) result).getXMLStreamWriter() != null) {
                    this._tohFactory.setXMLStreamWriter(((StAXResult) result).getXMLStreamWriter());
                }
                this._tohFactory.setOutputType(3);
                return this._tohFactory.getSerializationHandler();
            }
            if (result instanceof DOMResult) {
                this._tohFactory.setNode(((DOMResult) result).getNode());
                this._tohFactory.setNextSibling(((DOMResult) result).getNextSibling());
                this._tohFactory.setOutputType(2);
                return this._tohFactory.getSerializationHandler();
            }
            if (result instanceof StreamResult) {
                StreamResult target2 = (StreamResult) result;
                this._tohFactory.setOutputType(0);
                Writer writer = target2.getWriter();
                if (writer != null) {
                    this._tohFactory.setWriter(writer);
                    return this._tohFactory.getSerializationHandler();
                }
                OutputStream ostream = target2.getOutputStream();
                if (ostream != null) {
                    this._tohFactory.setOutputStream(ostream);
                    return this._tohFactory.getSerializationHandler();
                }
                String systemId = result.getSystemId();
                if (systemId == null) {
                    ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_NO_RESULT_ERR);
                    throw new TransformerException(err.toString());
                }
                if (systemId.startsWith("file:")) {
                    try {
                        URI uri = new URI(systemId);
                        String host = uri.getHost();
                        String path = uri.getPath();
                        if (path == null) {
                            path = "";
                        }
                        systemId = host != null ? "file://" + host + path : "file://" + path;
                    } catch (Exception e2) {
                    }
                    URL url = new URL(systemId);
                    this._ostream = new FileOutputStream(url.getFile());
                    this._tohFactory.setOutputStream(this._ostream);
                    return this._tohFactory.getSerializationHandler();
                }
                if (systemId.startsWith("http:")) {
                    URL url2 = new URL(systemId);
                    URLConnection connection = url2.openConnection();
                    TransletOutputHandlerFactory transletOutputHandlerFactory = this._tohFactory;
                    OutputStream outputStream = connection.getOutputStream();
                    this._ostream = outputStream;
                    transletOutputHandlerFactory.setOutputStream(outputStream);
                    return this._tohFactory.getSerializationHandler();
                }
                TransletOutputHandlerFactory transletOutputHandlerFactory2 = this._tohFactory;
                FileOutputStream fileOutputStream = new FileOutputStream(new File(systemId));
                this._ostream = fileOutputStream;
                transletOutputHandlerFactory2.setOutputStream(fileOutputStream);
                return this._tohFactory.getSerializationHandler();
            }
            return null;
        } catch (UnknownServiceException e3) {
            throw new TransformerException(e3);
        } catch (IOException e4) {
            throw new TransformerException(e4);
        } catch (ParserConfigurationException e5) {
            throw new TransformerException(e5);
        }
    }

    protected void setDOM(DOM dom) {
        this._dom = dom;
    }

    private DOM getDOM(Source source) throws TransformerException {
        DOM dom;
        DTMWSFilter wsfilter;
        try {
            if (source != null) {
                if (this._translet != null && (this._translet instanceof StripFilter)) {
                    wsfilter = new DOMWSFilter(this._translet);
                } else {
                    wsfilter = null;
                }
                boolean hasIdCall = this._translet != null ? this._translet.hasIdCall() : false;
                if (this._dtmManager == null) {
                    this._dtmManager = this._tfactory.createNewDTMManagerInstance();
                    this._dtmManager.setOverrideDefaultParser(this._overrideDefaultParser);
                }
                dom = (DOM) this._dtmManager.getDTM(source, false, wsfilter, true, false, false, 0, hasIdCall);
            } else if (this._dom != null) {
                dom = this._dom;
                this._dom = null;
            } else {
                return null;
            }
            if (!this._isIdentity) {
                this._translet.prepassDocument(dom);
            }
            return dom;
        } catch (Exception e2) {
            if (this._errorListener != null) {
                postErrorToListener(e2.getMessage());
            }
            throw new TransformerException(e2);
        }
    }

    protected TransformerFactoryImpl getTransformerFactory() {
        return this._tfactory;
    }

    protected TransletOutputHandlerFactory getTransletOutputHandlerFactory() {
        return this._tohFactory;
    }

    private void transformIdentity(Source source, SerializationHandler handler) throws Exception {
        InputSource input;
        if (source != null) {
            this._sourceSystemId = source.getSystemId();
        }
        if (source instanceof StreamSource) {
            StreamSource stream = (StreamSource) source;
            InputStream streamInput = stream.getInputStream();
            Reader streamReader = stream.getReader();
            XMLReader reader = this._readerManager.getXMLReader();
            try {
                try {
                    reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
                    reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                } catch (Throwable th) {
                    this._readerManager.releaseXMLReader(reader);
                    throw th;
                }
            } catch (SAXException e2) {
            }
            reader.setContentHandler(handler);
            if (streamInput != null) {
                input = new InputSource(streamInput);
                input.setSystemId(this._sourceSystemId);
            } else if (streamReader != null) {
                input = new InputSource(streamReader);
                input.setSystemId(this._sourceSystemId);
            } else if (this._sourceSystemId != null) {
                input = new InputSource(this._sourceSystemId);
            } else {
                ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_NO_SOURCE_ERR);
                throw new TransformerException(err.toString());
            }
            reader.parse(input);
            this._readerManager.releaseXMLReader(reader);
            return;
        }
        if (source instanceof SAXSource) {
            SAXSource sax = (SAXSource) source;
            XMLReader reader2 = sax.getXMLReader();
            InputSource input2 = sax.getInputSource();
            boolean userReader = true;
            if (reader2 == null) {
                try {
                    reader2 = this._readerManager.getXMLReader();
                    userReader = false;
                } catch (Throwable th2) {
                    if (!userReader) {
                        this._readerManager.releaseXMLReader(reader2);
                    }
                    throw th2;
                }
            }
            try {
                reader2.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
                reader2.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            } catch (SAXException e3) {
            }
            reader2.setContentHandler(handler);
            reader2.parse(input2);
            if (!userReader) {
                this._readerManager.releaseXMLReader(reader2);
                return;
            }
            return;
        }
        if (!(source instanceof StAXSource)) {
            if (source instanceof DOMSource) {
                DOMSource domsrc = (DOMSource) source;
                new DOM2TO(domsrc.getNode(), handler).parse();
                return;
            } else if (source instanceof XSLTCSource) {
                DOM dom = ((XSLTCSource) source).getDOM(null, this._translet);
                ((SAXImpl) dom).copy(handler);
                return;
            } else {
                ErrorMsg err2 = new ErrorMsg(ErrorMsg.JAXP_NO_SOURCE_ERR);
                throw new TransformerException(err2.toString());
            }
        }
        StAXSource staxSource = (StAXSource) source;
        if (staxSource.getXMLEventReader() != null) {
            XMLEventReader xmlEventReader = staxSource.getXMLEventReader();
            StAXEvent2SAX staxevent2sax = new StAXEvent2SAX(xmlEventReader);
            staxevent2sax.setContentHandler(handler);
            staxevent2sax.parse();
            handler.flushPending();
            return;
        }
        if (staxSource.getXMLStreamReader() != null) {
            XMLStreamReader xmlStreamReader = staxSource.getXMLStreamReader();
            StAXStream2SAX staxStream2SAX = new StAXStream2SAX(xmlStreamReader);
            staxStream2SAX.setContentHandler(handler);
            staxStream2SAX.parse();
            handler.flushPending();
        }
    }

    private void transform(Source source, SerializationHandler handler, String encoding) throws TransformerException {
        try {
            try {
                try {
                    if (((source instanceof StreamSource) && source.getSystemId() == null && ((StreamSource) source).getInputStream() == null && ((StreamSource) source).getReader() == null) || (((source instanceof SAXSource) && ((SAXSource) source).getInputSource() == null && ((SAXSource) source).getXMLReader() == null) || ((source instanceof DOMSource) && ((DOMSource) source).getNode() == null))) {
                        DocumentBuilderFactory builderF = JdkXmlUtils.getDOMFactory(this._overrideDefaultParser);
                        DocumentBuilder builder = builderF.newDocumentBuilder();
                        String systemID = source.getSystemId();
                        source = new DOMSource(builder.newDocument());
                        if (systemID != null) {
                            source.setSystemId(systemID);
                        }
                    }
                    if (this._isIdentity) {
                        transformIdentity(source, handler);
                    } else {
                        this._translet.transform(getDOM(source), handler);
                    }
                    if (this._ostream != null) {
                        try {
                            this._ostream.close();
                        } catch (IOException e2) {
                        }
                        this._ostream = null;
                    }
                } catch (RuntimeException e3) {
                    if (this._errorListener != null) {
                        postErrorToListener(e3.getMessage());
                    }
                    throw new TransformerException(e3);
                } catch (Exception e4) {
                    if (this._errorListener != null) {
                        postErrorToListener(e4.getMessage());
                    }
                    throw new TransformerException(e4);
                }
            } catch (TransletException e5) {
                if (this._errorListener != null) {
                    postErrorToListener(e5.getMessage());
                }
                throw new TransformerException(e5);
            }
        } finally {
            this._dtmManager = null;
        }
    }

    @Override // javax.xml.transform.Transformer
    public ErrorListener getErrorListener() {
        return this._errorListener;
    }

    @Override // javax.xml.transform.Transformer
    public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
        if (listener == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.ERROR_LISTENER_NULL_ERR, "Transformer");
            throw new IllegalArgumentException(err.toString());
        }
        this._errorListener = listener;
        if (this._translet != null) {
            this._translet.setMessageHandler(new MessageHandler(this._errorListener));
        }
    }

    private void postErrorToListener(String message) {
        try {
            this._errorListener.error(new TransformerException(message));
        } catch (TransformerException e2) {
        }
    }

    private void postWarningToListener(String message) {
        try {
            this._errorListener.warning(new TransformerException(message));
        } catch (TransformerException e2) {
        }
    }

    @Override // javax.xml.transform.Transformer
    public Properties getOutputProperties() {
        return (Properties) this._properties.clone();
    }

    @Override // javax.xml.transform.Transformer
    public String getOutputProperty(String name) throws IllegalArgumentException {
        if (!validOutputProperty(name)) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_UNKNOWN_PROP_ERR, name);
            throw new IllegalArgumentException(err.toString());
        }
        return this._properties.getProperty(name);
    }

    @Override // javax.xml.transform.Transformer
    public void setOutputProperties(Properties properties) throws IllegalArgumentException {
        if (properties != null) {
            Enumeration names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement2();
                if (!isDefaultProperty(name, properties)) {
                    if (validOutputProperty(name)) {
                        this._properties.setProperty(name, properties.getProperty(name));
                    } else {
                        ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_UNKNOWN_PROP_ERR, name);
                        throw new IllegalArgumentException(err.toString());
                    }
                }
            }
            return;
        }
        this._properties = this._propertiesClone;
    }

    @Override // javax.xml.transform.Transformer
    public void setOutputProperty(String name, String value) throws IllegalArgumentException {
        if (!validOutputProperty(name)) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_UNKNOWN_PROP_ERR, name);
            throw new IllegalArgumentException(err.toString());
        }
        this._properties.setProperty(name, value);
    }

    private void transferOutputProperties(AbstractTranslet translet) {
        if (this._properties == null) {
            return;
        }
        Enumeration names = this._properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement2();
            String value = (String) this._properties.get(name);
            if (value != null) {
                if (name.equals("encoding")) {
                    translet._encoding = value;
                } else if (name.equals("method")) {
                    translet._method = value;
                } else if (name.equals("doctype-public")) {
                    translet._doctypePublic = value;
                } else if (name.equals("doctype-system")) {
                    translet._doctypeSystem = value;
                } else if (name.equals("media-type")) {
                    translet._mediaType = value;
                } else if (name.equals("standalone")) {
                    translet._standalone = value;
                } else if (name.equals("version")) {
                    translet._version = value;
                } else if (name.equals("omit-xml-declaration")) {
                    translet._omitHeader = value != null && value.toLowerCase().equals("yes");
                } else if (name.equals("indent")) {
                    translet._indent = value != null && value.toLowerCase().equals("yes");
                } else if (name.equals("{http://xml.apache.org/xslt}indent-amount")) {
                    if (value != null) {
                        translet._indentamount = Integer.parseInt(value);
                    }
                } else if (name.equals(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT)) {
                    if (value != null) {
                        translet._indentamount = Integer.parseInt(value);
                    }
                } else if (name.equals("cdata-section-elements")) {
                    if (value != null) {
                        translet._cdata = null;
                        StringTokenizer e2 = new StringTokenizer(value);
                        while (e2.hasMoreTokens()) {
                            translet.addCdataElement(e2.nextToken());
                        }
                    }
                } else if (name.equals(OutputPropertiesFactory.ORACLE_IS_STANDALONE) && value != null && value.equals("yes")) {
                    translet._isStandalone = true;
                }
            }
        }
    }

    public void transferOutputProperties(SerializationHandler handler) {
        String uri;
        String localName;
        if (this._properties == null) {
            return;
        }
        String doctypePublic = null;
        String doctypeSystem = null;
        Enumeration names = this._properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement2();
            String value = (String) this._properties.get(name);
            if (value != null) {
                if (name.equals("doctype-public")) {
                    doctypePublic = value;
                } else if (name.equals("doctype-system")) {
                    doctypeSystem = value;
                } else if (name.equals("media-type")) {
                    handler.setMediaType(value);
                } else if (name.equals("standalone")) {
                    handler.setStandalone(value);
                } else if (name.equals("version")) {
                    handler.setVersion(value);
                } else if (name.equals("omit-xml-declaration")) {
                    handler.setOmitXMLDeclaration(value != null && value.toLowerCase().equals("yes"));
                } else if (name.equals("indent")) {
                    handler.setIndent(value != null && value.toLowerCase().equals("yes"));
                } else if (name.equals("{http://xml.apache.org/xslt}indent-amount")) {
                    if (value != null) {
                        handler.setIndentAmount(Integer.parseInt(value));
                    }
                } else if (name.equals(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT)) {
                    if (value != null) {
                        handler.setIndentAmount(Integer.parseInt(value));
                    }
                } else if (name.equals(OutputPropertiesFactory.ORACLE_IS_STANDALONE)) {
                    if (value != null && value.equals("yes")) {
                        handler.setIsStandalone(true);
                    }
                } else if (name.equals("cdata-section-elements") && value != null) {
                    StringTokenizer e2 = new StringTokenizer(value);
                    ArrayList<String> uriAndLocalNames = null;
                    while (e2.hasMoreTokens()) {
                        String token = e2.nextToken();
                        int lastcolon = token.lastIndexOf(58);
                        if (lastcolon > 0) {
                            uri = token.substring(0, lastcolon);
                            localName = token.substring(lastcolon + 1);
                        } else {
                            uri = null;
                            localName = token;
                        }
                        if (uriAndLocalNames == null) {
                            uriAndLocalNames = new ArrayList<>();
                        }
                        uriAndLocalNames.add(uri);
                        uriAndLocalNames.add(localName);
                    }
                    handler.setCdataSectionElements(uriAndLocalNames);
                }
            }
        }
        if (doctypePublic != null || doctypeSystem != null) {
            handler.setDoctype(doctypeSystem, doctypePublic);
        }
    }

    private Properties createOutputProperties(Properties outputProperties) {
        Properties defaults = new Properties();
        setDefaults(defaults, "xml");
        Properties base = new Properties(defaults);
        if (outputProperties != null) {
            Enumeration names = outputProperties.propertyNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement2();
                base.setProperty(name, outputProperties.getProperty(name));
            }
        } else {
            base.setProperty("encoding", this._translet._encoding);
            if (this._translet._method != null) {
                base.setProperty("method", this._translet._method);
            }
        }
        String method = base.getProperty("method");
        if (method != null) {
            if (method.equals("html")) {
                setDefaults(defaults, "html");
            } else if (method.equals("text")) {
                setDefaults(defaults, "text");
            }
        }
        return base;
    }

    private void setDefaults(Properties props, String method) {
        Properties method_props = OutputPropertiesFactory.getDefaultMethodProperties(method);
        Enumeration names = method_props.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement2();
            props.setProperty(name, method_props.getProperty(name));
        }
    }

    private boolean validOutputProperty(String name) {
        return name.equals("encoding") || name.equals("method") || name.equals("indent") || name.equals("doctype-public") || name.equals("doctype-system") || name.equals("cdata-section-elements") || name.equals("media-type") || name.equals("omit-xml-declaration") || name.equals("standalone") || name.equals("version") || name.equals(OutputPropertiesFactory.ORACLE_IS_STANDALONE) || name.charAt(0) == '{';
    }

    private boolean isDefaultProperty(String name, Properties properties) {
        return properties.get(name) == null;
    }

    @Override // javax.xml.transform.Transformer
    public void setParameter(String name, Object value) {
        if (value == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_INVALID_SET_PARAM_VALUE, name);
            throw new IllegalArgumentException(err.toString());
        }
        if (this._isIdentity) {
            if (this._parameters == null) {
                this._parameters = new HashMap();
            }
            this._parameters.put(name, value);
            return;
        }
        this._translet.addParameter(name, value);
    }

    @Override // javax.xml.transform.Transformer
    public void clearParameters() {
        if (this._isIdentity && this._parameters != null) {
            this._parameters.clear();
        } else {
            this._translet.clearParameters();
        }
    }

    @Override // javax.xml.transform.Transformer
    public final Object getParameter(String name) {
        if (this._isIdentity) {
            if (this._parameters != null) {
                return this._parameters.get(name);
            }
            return null;
        }
        return this._translet.getParameter(name);
    }

    @Override // javax.xml.transform.Transformer
    public URIResolver getURIResolver() {
        return this._uriResolver;
    }

    @Override // javax.xml.transform.Transformer
    public void setURIResolver(URIResolver resolver) {
        this._uriResolver = resolver;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.DOMCache
    public DOM retrieveDocument(String baseURI, String href, Translet translet) {
        try {
            if (href.length() == 0) {
                href = baseURI;
            }
            Source resolvedSource = this._uriResolver.resolve(href, baseURI);
            if (resolvedSource == null) {
                AbstractTranslet t2 = (AbstractTranslet) translet;
                String systemId = SystemIDResolver.getAbsoluteURI(href, baseURI);
                String errMsg = null;
                try {
                    String accessError = SecuritySupport.checkAccess(systemId, t2.getAllowedProtocols(), "all");
                    if (accessError != null) {
                        ErrorMsg msg = new ErrorMsg(ErrorMsg.ACCESSING_XSLT_TARGET_ERR, SecuritySupport.sanitizePath(href), accessError);
                        errMsg = msg.toString();
                    }
                } catch (IOException ioe) {
                    errMsg = ioe.getMessage();
                }
                if (errMsg != null) {
                    t2.setAccessError(errMsg);
                    return null;
                }
                StreamSource streamSource = new StreamSource(systemId);
                return getDOM(streamSource);
            }
            return getDOM(resolvedSource);
        } catch (TransformerException e2) {
            if (this._errorListener != null) {
                postErrorToListener("File not found: " + e2.getMessage());
                return null;
            }
            return null;
        }
    }

    @Override // javax.xml.transform.ErrorListener
    public void error(TransformerException e2) throws TransformerException {
        Throwable wrapped = e2.getException();
        if (wrapped != null) {
            System.err.println(new ErrorMsg(ErrorMsg.ERROR_PLUS_WRAPPED_MSG, e2.getMessageAndLocation(), wrapped.getMessage()));
        } else {
            System.err.println(new ErrorMsg(ErrorMsg.ERROR_MSG, e2.getMessageAndLocation()));
        }
        throw e2;
    }

    @Override // javax.xml.transform.ErrorListener
    public void fatalError(TransformerException e2) throws TransformerException {
        Throwable wrapped = e2.getException();
        if (wrapped != null) {
            System.err.println(new ErrorMsg(ErrorMsg.FATAL_ERR_PLUS_WRAPPED_MSG, e2.getMessageAndLocation(), wrapped.getMessage()));
        } else {
            System.err.println(new ErrorMsg(ErrorMsg.FATAL_ERR_MSG, e2.getMessageAndLocation()));
        }
        throw e2;
    }

    @Override // javax.xml.transform.ErrorListener
    public void warning(TransformerException e2) throws TransformerException {
        Throwable wrapped = e2.getException();
        if (wrapped != null) {
            System.err.println(new ErrorMsg(ErrorMsg.WARNING_PLUS_WRAPPED_MSG, e2.getMessageAndLocation(), wrapped.getMessage()));
        } else {
            System.err.println(new ErrorMsg(ErrorMsg.WARNING_MSG, e2.getMessageAndLocation()));
        }
    }

    @Override // javax.xml.transform.Transformer
    public void reset() throws IllegalArgumentException {
        this._method = null;
        this._encoding = null;
        this._sourceSystemId = null;
        this._errorListener = this;
        this._uriResolver = null;
        this._dom = null;
        this._parameters = null;
        this._indentNumber = 0;
        setOutputProperties(null);
        this._tohFactory = null;
        this._ostream = null;
    }
}
