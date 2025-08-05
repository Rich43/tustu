package com.sun.org.apache.xml.internal.serialize;

import com.sun.org.apache.xerces.internal.dom.AbortException;
import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xerces.internal.dom.DOMErrorImpl;
import com.sun.org.apache.xerces.internal.dom.DOMLocatorImpl;
import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import com.sun.org.apache.xerces.internal.dom.DOMNormalizer;
import com.sun.org.apache.xerces.internal.dom.DOMStringListImpl;
import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XML11Char;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.MissingResourceException;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSSerializerFilter;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/DOMSerializerImpl.class */
public class DOMSerializerImpl implements LSSerializer, DOMConfiguration {
    private XML11Serializer xml11Serializer;
    private DOMStringList fRecognizedParameters;
    protected short features;
    protected static final short NAMESPACES = 1;
    protected static final short WELLFORMED = 2;
    protected static final short ENTITIES = 4;
    protected static final short CDATA = 8;
    protected static final short SPLITCDATA = 16;
    protected static final short COMMENTS = 32;
    protected static final short DISCARDDEFAULT = 64;
    protected static final short INFOSET = 128;
    protected static final short XMLDECL = 256;
    protected static final short NSDECL = 512;
    protected static final short DOM_ELEMENT_CONTENT_WHITESPACE = 1024;
    protected static final short PRETTY_PRINT = 2048;
    private DOMErrorHandler fErrorHandler = null;
    private final DOMErrorImpl fError = new DOMErrorImpl();
    private final DOMLocatorImpl fLocator = new DOMLocatorImpl();
    private XMLSerializer serializer = new XMLSerializer();

    public DOMSerializerImpl() {
        this.features = (short) 0;
        this.features = (short) (this.features | 1);
        this.features = (short) (this.features | 4);
        this.features = (short) (this.features | 32);
        this.features = (short) (this.features | 8);
        this.features = (short) (this.features | 16);
        this.features = (short) (this.features | 2);
        this.features = (short) (this.features | 512);
        this.features = (short) (this.features | 1024);
        this.features = (short) (this.features | 64);
        this.features = (short) (this.features | 256);
        initSerializer(this.serializer);
    }

    @Override // org.w3c.dom.ls.LSSerializer
    public DOMConfiguration getDomConfig() {
        return this;
    }

    @Override // org.w3c.dom.DOMConfiguration
    public void setParameter(String name, Object value) throws DOMException, MissingResourceException {
        if (!(value instanceof Boolean)) {
            if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER)) {
                if (value == null || (value instanceof DOMErrorHandler)) {
                    this.fErrorHandler = (DOMErrorHandler) value;
                    return;
                } else {
                    String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "TYPE_MISMATCH_ERR", new Object[]{name});
                    throw new DOMException((short) 17, msg);
                }
            }
            if (name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER) || name.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION) || name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE) || (name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS) && value != null)) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                throw new DOMException((short) 9, msg2);
            }
            String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[]{name});
            throw new DOMException((short) 8, msg3);
        }
        boolean state = ((Boolean) value).booleanValue();
        if (name.equalsIgnoreCase(Constants.DOM_INFOSET)) {
            if (state) {
                this.features = (short) (this.features & (-5));
                this.features = (short) (this.features & (-9));
                this.features = (short) (this.features | 1);
                this.features = (short) (this.features | 512);
                this.features = (short) (this.features | 2);
                this.features = (short) (this.features | 32);
                return;
            }
            return;
        }
        if (name.equalsIgnoreCase(Constants.DOM_XMLDECL)) {
            this.features = (short) (state ? this.features | 256 : this.features & (-257));
            return;
        }
        if (name.equalsIgnoreCase("namespaces")) {
            this.features = (short) (state ? this.features | 1 : this.features & (-2));
            this.serializer.fNamespaces = state;
            return;
        }
        if (name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA)) {
            this.features = (short) (state ? this.features | 16 : this.features & (-17));
            return;
        }
        if (name.equalsIgnoreCase(Constants.DOM_DISCARD_DEFAULT_CONTENT)) {
            this.features = (short) (state ? this.features | 64 : this.features & (-65));
            return;
        }
        if (name.equalsIgnoreCase(Constants.DOM_WELLFORMED)) {
            this.features = (short) (state ? this.features | 2 : this.features & (-3));
            return;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ENTITIES)) {
            this.features = (short) (state ? this.features | 4 : this.features & (-5));
            return;
        }
        if (name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS)) {
            this.features = (short) (state ? this.features | 8 : this.features & (-9));
            return;
        }
        if (name.equalsIgnoreCase(Constants.DOM_COMMENTS)) {
            this.features = (short) (state ? this.features | 32 : this.features & (-33));
            return;
        }
        if (name.equalsIgnoreCase(Constants.DOM_FORMAT_PRETTY_PRINT)) {
            this.features = (short) (state ? this.features | 2048 : this.features & (-2049));
            return;
        }
        if (name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_VALIDATE) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION)) {
            if (state) {
                String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                throw new DOMException((short) 9, msg4);
            }
        } else if (name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
            this.features = (short) (state ? this.features | 512 : this.features & (-513));
            this.serializer.fNamespacePrefixes = state;
        } else {
            if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE) || name.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS)) {
                if (!state) {
                    String msg5 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
                    throw new DOMException((short) 9, msg5);
                }
                return;
            }
            String msg6 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[]{name});
            throw new DOMException((short) 9, msg6);
        }
    }

    @Override // org.w3c.dom.DOMConfiguration
    public boolean canSetParameter(String name, Object state) {
        if (state == null) {
            return true;
        }
        if (!(state instanceof Boolean)) {
            if ((name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER) && state == null) || (state instanceof DOMErrorHandler)) {
                return true;
            }
            return false;
        }
        boolean value = ((Boolean) state).booleanValue();
        if (name.equalsIgnoreCase("namespaces") || name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA) || name.equalsIgnoreCase(Constants.DOM_DISCARD_DEFAULT_CONTENT) || name.equalsIgnoreCase(Constants.DOM_XMLDECL) || name.equalsIgnoreCase(Constants.DOM_WELLFORMED) || name.equalsIgnoreCase(Constants.DOM_INFOSET) || name.equalsIgnoreCase(Constants.DOM_ENTITIES) || name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS) || name.equalsIgnoreCase(Constants.DOM_COMMENTS) || name.equalsIgnoreCase(Constants.DOM_FORMAT_PRETTY_PRINT) || name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
            return true;
        }
        if (name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_VALIDATE) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION)) {
            return !value;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE) || name.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS)) {
            return value;
        }
        return false;
    }

    @Override // org.w3c.dom.DOMConfiguration
    public DOMStringList getParameterNames() {
        if (this.fRecognizedParameters == null) {
            ArrayList parameters = new ArrayList();
            parameters.add("namespaces");
            parameters.add(Constants.DOM_SPLIT_CDATA);
            parameters.add(Constants.DOM_DISCARD_DEFAULT_CONTENT);
            parameters.add(Constants.DOM_XMLDECL);
            parameters.add(Constants.DOM_CANONICAL_FORM);
            parameters.add(Constants.DOM_VALIDATE_IF_SCHEMA);
            parameters.add(Constants.DOM_VALIDATE);
            parameters.add(Constants.DOM_CHECK_CHAR_NORMALIZATION);
            parameters.add(Constants.DOM_DATATYPE_NORMALIZATION);
            parameters.add(Constants.DOM_FORMAT_PRETTY_PRINT);
            parameters.add(Constants.DOM_WELLFORMED);
            parameters.add(Constants.DOM_INFOSET);
            parameters.add(Constants.DOM_NAMESPACE_DECLARATIONS);
            parameters.add(Constants.DOM_ELEMENT_CONTENT_WHITESPACE);
            parameters.add(Constants.DOM_ENTITIES);
            parameters.add(Constants.DOM_CDATA_SECTIONS);
            parameters.add(Constants.DOM_COMMENTS);
            parameters.add(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS);
            parameters.add(Constants.DOM_ERROR_HANDLER);
            this.fRecognizedParameters = new DOMStringListImpl(parameters);
        }
        return this.fRecognizedParameters;
    }

    @Override // org.w3c.dom.DOMConfiguration
    public Object getParameter(String name) throws DOMException, MissingResourceException {
        if (name.equalsIgnoreCase(Constants.DOM_NORMALIZE_CHARACTERS)) {
            return null;
        }
        if (name.equalsIgnoreCase(Constants.DOM_COMMENTS)) {
            return (this.features & 32) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase("namespaces")) {
            return (this.features & 1) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_XMLDECL)) {
            return (this.features & 256) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_CDATA_SECTIONS)) {
            return (this.features & 8) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ENTITIES)) {
            return (this.features & 4) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_SPLIT_CDATA)) {
            return (this.features & 16) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_WELLFORMED)) {
            return (this.features & 2) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_NAMESPACE_DECLARATIONS)) {
            return (this.features & 512) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ELEMENT_CONTENT_WHITESPACE) || name.equalsIgnoreCase(Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS)) {
            return Boolean.TRUE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_DISCARD_DEFAULT_CONTENT)) {
            return (this.features & 64) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_FORMAT_PRETTY_PRINT)) {
            return (this.features & 2048) != 0 ? Boolean.TRUE : Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_INFOSET)) {
            if ((this.features & 4) == 0 && (this.features & 8) == 0 && (this.features & 1) != 0 && (this.features & 512) != 0 && (this.features & 2) != 0 && (this.features & 32) != 0) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_CANONICAL_FORM) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_CHECK_CHAR_NORMALIZATION) || name.equalsIgnoreCase(Constants.DOM_VALIDATE) || name.equalsIgnoreCase(Constants.DOM_VALIDATE_IF_SCHEMA) || name.equalsIgnoreCase(Constants.DOM_DATATYPE_NORMALIZATION)) {
            return Boolean.FALSE;
        }
        if (name.equalsIgnoreCase(Constants.DOM_ERROR_HANDLER)) {
            return this.fErrorHandler;
        }
        if (name.equalsIgnoreCase(Constants.DOM_RESOURCE_RESOLVER) || name.equalsIgnoreCase(Constants.DOM_SCHEMA_LOCATION) || name.equalsIgnoreCase(Constants.DOM_SCHEMA_TYPE)) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_SUPPORTED", new Object[]{name});
            throw new DOMException((short) 9, msg);
        }
        String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "FEATURE_NOT_FOUND", new Object[]{name});
        throw new DOMException((short) 8, msg2);
    }

    @Override // org.w3c.dom.ls.LSSerializer
    public String writeToString(Node wnode) throws DOMException, LSException {
        XMLSerializer ser;
        String ver = _getXmlVersion(wnode);
        if (ver != null && ver.equals(SerializerConstants.XMLVERSION11)) {
            if (this.xml11Serializer == null) {
                this.xml11Serializer = new XML11Serializer();
                initSerializer(this.xml11Serializer);
            }
            copySettings(this.serializer, this.xml11Serializer);
            ser = this.xml11Serializer;
        } else {
            ser = this.serializer;
        }
        StringWriter destination = new StringWriter();
        try {
            try {
                try {
                    try {
                        prepareForSerialization(ser, wnode);
                        ser._format.setEncoding("UTF-16");
                        ser.setOutputCharStream(destination);
                        if (wnode.getNodeType() == 9) {
                            ser.serialize((Document) wnode);
                        } else if (wnode.getNodeType() == 11) {
                            ser.serialize((DocumentFragment) wnode);
                        } else if (wnode.getNodeType() == 1) {
                            ser.serialize((Element) wnode);
                        } else if (wnode.getNodeType() == 3 || wnode.getNodeType() == 8 || wnode.getNodeType() == 5 || wnode.getNodeType() == 4 || wnode.getNodeType() == 7) {
                            ser.serialize(wnode);
                        } else {
                            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "unable-to-serialize-node", null);
                            if (ser.fDOMErrorHandler != null) {
                                DOMErrorImpl error = new DOMErrorImpl();
                                error.fType = "unable-to-serialize-node";
                                error.fMessage = msg;
                                error.fSeverity = (short) 3;
                                ser.fDOMErrorHandler.handleError(error);
                            }
                            throw new LSException((short) 82, msg);
                        }
                        ser.clearDocumentState();
                        return destination.toString();
                    } catch (AbortException e2) {
                        ser.clearDocumentState();
                        return null;
                    } catch (LSException lse) {
                        throw lse;
                    }
                } catch (IOException ioe) {
                    throw new DOMException((short) 2, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "STRING_TOO_LONG", new Object[]{ioe.getMessage()}));
                }
            } catch (RuntimeException e3) {
                throw ((LSException) DOMUtil.createLSException((short) 82, e3).fillInStackTrace());
            }
        } catch (Throwable th) {
            ser.clearDocumentState();
            throw th;
        }
    }

    @Override // org.w3c.dom.ls.LSSerializer
    public void setNewLine(String newLine) {
        this.serializer._format.setLineSeparator(newLine);
    }

    @Override // org.w3c.dom.ls.LSSerializer
    public String getNewLine() {
        return this.serializer._format.getLineSeparator();
    }

    @Override // org.w3c.dom.ls.LSSerializer
    public LSSerializerFilter getFilter() {
        return this.serializer.fDOMFilter;
    }

    @Override // org.w3c.dom.ls.LSSerializer
    public void setFilter(LSSerializerFilter filter) {
        this.serializer.fDOMFilter = filter;
    }

    private void initSerializer(XMLSerializer ser) {
        ser.fNSBinder = new NamespaceSupport();
        ser.fLocalNSBinder = new NamespaceSupport();
        ser.fSymbolTable = new SymbolTable();
    }

    private void copySettings(XMLSerializer src, XMLSerializer dest) {
        dest.fDOMErrorHandler = this.fErrorHandler;
        dest._format.setEncoding(src._format.getEncoding());
        dest._format.setLineSeparator(src._format.getLineSeparator());
        dest.fDOMFilter = src.fDOMFilter;
    }

    @Override // org.w3c.dom.ls.LSSerializer
    public boolean write(Node node, LSOutput destination) throws LSException {
        XMLSerializer ser;
        if (node == null) {
            return false;
        }
        String ver = _getXmlVersion(node);
        if (ver == null || !ver.equals(SerializerConstants.XMLVERSION11)) {
            ser = this.serializer;
        } else {
            if (this.xml11Serializer == null) {
                this.xml11Serializer = new XML11Serializer();
                initSerializer(this.xml11Serializer);
            }
            copySettings(this.serializer, this.xml11Serializer);
            ser = this.xml11Serializer;
        }
        String encoding = destination.getEncoding();
        String encoding2 = encoding;
        if (encoding == null) {
            encoding2 = _getInputEncoding(node);
            if (encoding2 == null) {
                encoding2 = _getXmlEncoding(node);
                if (encoding2 == null) {
                    encoding2 = "UTF-8";
                }
            }
        }
        try {
            try {
                try {
                    try {
                        prepareForSerialization(ser, node);
                        ser._format.setEncoding(encoding2);
                        OutputStream outputStream = destination.getByteStream();
                        Writer writer = destination.getCharacterStream();
                        String uri = destination.getSystemId();
                        if (writer != null) {
                            ser.setOutputCharStream(writer);
                        } else if (outputStream != null) {
                            ser.setOutputByteStream(outputStream);
                        } else {
                            if (uri == null) {
                                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "no-output-specified", null);
                                if (ser.fDOMErrorHandler != null) {
                                    DOMErrorImpl error = new DOMErrorImpl();
                                    error.fType = "no-output-specified";
                                    error.fMessage = msg;
                                    error.fSeverity = (short) 3;
                                    ser.fDOMErrorHandler.handleError(error);
                                }
                                throw new LSException((short) 82, msg);
                            }
                            ser.setOutputByteStream(XMLEntityManager.createOutputStream(uri));
                        }
                        if (node.getNodeType() == 9) {
                            ser.serialize((Document) node);
                        } else if (node.getNodeType() == 11) {
                            ser.serialize((DocumentFragment) node);
                        } else if (node.getNodeType() == 1) {
                            ser.serialize((Element) node);
                        } else {
                            if (node.getNodeType() != 3 && node.getNodeType() != 8 && node.getNodeType() != 5 && node.getNodeType() != 4 && node.getNodeType() != 7) {
                                ser.clearDocumentState();
                                return false;
                            }
                            ser.serialize(node);
                        }
                        ser.clearDocumentState();
                        return true;
                    } catch (AbortException e2) {
                        ser.clearDocumentState();
                        return false;
                    } catch (LSException lse) {
                        throw lse;
                    }
                } catch (UnsupportedEncodingException ue) {
                    if (ser.fDOMErrorHandler != null) {
                        DOMErrorImpl error2 = new DOMErrorImpl();
                        error2.fException = ue;
                        error2.fType = "unsupported-encoding";
                        error2.fMessage = ue.getMessage();
                        error2.fSeverity = (short) 3;
                        ser.fDOMErrorHandler.handleError(error2);
                    }
                    throw new LSException((short) 82, DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "unsupported-encoding", null));
                } catch (Exception e3) {
                    if (ser.fDOMErrorHandler != null) {
                        DOMErrorImpl error3 = new DOMErrorImpl();
                        error3.fException = e3;
                        error3.fMessage = e3.getMessage();
                        error3.fSeverity = (short) 2;
                        ser.fDOMErrorHandler.handleError(error3);
                    }
                    throw ((LSException) DOMUtil.createLSException((short) 82, e3).fillInStackTrace());
                }
            } catch (RuntimeException e4) {
                throw ((LSException) DOMUtil.createLSException((short) 82, e4).fillInStackTrace());
            }
        } catch (Throwable th) {
            ser.clearDocumentState();
            throw th;
        }
    }

    @Override // org.w3c.dom.ls.LSSerializer
    public boolean writeToURI(Node node, String URI) throws LSException {
        XMLSerializer ser;
        if (node == null) {
            return false;
        }
        String ver = _getXmlVersion(node);
        if (ver == null || !ver.equals(SerializerConstants.XMLVERSION11)) {
            ser = this.serializer;
        } else {
            if (this.xml11Serializer == null) {
                this.xml11Serializer = new XML11Serializer();
                initSerializer(this.xml11Serializer);
            }
            copySettings(this.serializer, this.xml11Serializer);
            ser = this.xml11Serializer;
        }
        String encoding = _getInputEncoding(node);
        if (encoding == null) {
            encoding = _getXmlEncoding(node);
            if (encoding == null) {
                encoding = "UTF-8";
            }
        }
        try {
            try {
                try {
                    prepareForSerialization(ser, node);
                    ser._format.setEncoding(encoding);
                    ser.setOutputByteStream(XMLEntityManager.createOutputStream(URI));
                    if (node.getNodeType() == 9) {
                        ser.serialize((Document) node);
                    } else if (node.getNodeType() == 11) {
                        ser.serialize((DocumentFragment) node);
                    } else if (node.getNodeType() == 1) {
                        ser.serialize((Element) node);
                    } else {
                        if (node.getNodeType() != 3 && node.getNodeType() != 8 && node.getNodeType() != 5 && node.getNodeType() != 4 && node.getNodeType() != 7) {
                            ser.clearDocumentState();
                            return false;
                        }
                        ser.serialize(node);
                    }
                    ser.clearDocumentState();
                    return true;
                } catch (AbortException e2) {
                    ser.clearDocumentState();
                    return false;
                } catch (Exception e3) {
                    if (ser.fDOMErrorHandler != null) {
                        DOMErrorImpl error = new DOMErrorImpl();
                        error.fException = e3;
                        error.fMessage = e3.getMessage();
                        error.fSeverity = (short) 2;
                        ser.fDOMErrorHandler.handleError(error);
                    }
                    throw ((LSException) DOMUtil.createLSException((short) 82, e3).fillInStackTrace());
                }
            } catch (LSException lse) {
                throw lse;
            } catch (RuntimeException e4) {
                throw ((LSException) DOMUtil.createLSException((short) 82, e4).fillInStackTrace());
            }
        } catch (Throwable th) {
            ser.clearDocumentState();
            throw th;
        }
    }

    private void prepareForSerialization(XMLSerializer ser, Node node) throws MissingResourceException {
        ser.reset();
        ser.features = this.features;
        ser.fDOMErrorHandler = this.fErrorHandler;
        ser.fNamespaces = (this.features & 1) != 0;
        ser.fNamespacePrefixes = (this.features & 512) != 0;
        ser._format.setIndenting((this.features & 2048) != 0);
        ser._format.setOmitComments((this.features & 32) == 0);
        ser._format.setOmitXMLDeclaration((this.features & 256) == 0);
        if ((this.features & 2) != 0) {
            boolean verifyNames = true;
            Document document = node.getNodeType() == 9 ? (Document) node : node.getOwnerDocument();
            try {
                java.lang.reflect.Method versionChanged = document.getClass().getMethod("isXMLVersionChanged()", new Class[0]);
                if (versionChanged != null) {
                    verifyNames = ((Boolean) versionChanged.invoke(document, (Object[]) null)).booleanValue();
                }
            } catch (Exception e2) {
            }
            if (node.getFirstChild() != null) {
                while (node != null) {
                    verify(node, verifyNames, false);
                    Node next = node.getFirstChild();
                    while (true) {
                        if (next == null) {
                            next = node.getNextSibling();
                            if (next == null) {
                                node = node.getParentNode();
                                if (node == node) {
                                    next = null;
                                    break;
                                }
                                next = node.getNextSibling();
                            }
                        }
                    }
                    node = next;
                }
                return;
            }
            verify(node, verifyNames, false);
        }
    }

    private void verify(Node node, boolean verifyNames, boolean xml11Version) throws MissingResourceException {
        boolean wellformed;
        boolean wellformed2;
        int type = node.getNodeType();
        this.fLocator.fRelatedNode = node;
        switch (type) {
            case 1:
                if (verifyNames) {
                    if ((this.features & 1) != 0) {
                        wellformed2 = CoreDocumentImpl.isValidQName(node.getPrefix(), node.getLocalName(), xml11Version);
                    } else {
                        wellformed2 = CoreDocumentImpl.isXMLName(node.getNodeName(), xml11Version);
                    }
                    if (!wellformed2 && this.fErrorHandler != null) {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[]{"Element", node.getNodeName()});
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short) 3, "wf-invalid-character-in-node-name");
                    }
                }
                NamedNodeMap attributes = node.hasAttributes() ? node.getAttributes() : null;
                if (attributes != null) {
                    for (int i2 = 0; i2 < attributes.getLength(); i2++) {
                        Attr attr = (Attr) attributes.item(i2);
                        this.fLocator.fRelatedNode = attr;
                        DOMNormalizer.isAttrValueWF(this.fErrorHandler, this.fError, this.fLocator, attributes, attr, attr.getValue(), xml11Version);
                        if (verifyNames) {
                            boolean wellformed3 = CoreDocumentImpl.isXMLName(attr.getNodeName(), xml11Version);
                            if (!wellformed3) {
                                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[]{"Attr", node.getNodeName()});
                                DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg2, (short) 3, "wf-invalid-character-in-node-name");
                            }
                        }
                    }
                    break;
                }
                break;
            case 3:
                DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), xml11Version);
                break;
            case 4:
                DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), xml11Version);
                break;
            case 5:
                if (verifyNames && (this.features & 4) != 0) {
                    CoreDocumentImpl.isXMLName(node.getNodeName(), xml11Version);
                    break;
                }
                break;
            case 7:
                ProcessingInstruction pinode = (ProcessingInstruction) node;
                String target = pinode.getTarget();
                if (verifyNames) {
                    if (xml11Version) {
                        wellformed = XML11Char.isXML11ValidName(target);
                    } else {
                        wellformed = XMLChar.isValidName(target);
                    }
                    if (!wellformed) {
                        String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[]{"Element", node.getNodeName()});
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg3, (short) 3, "wf-invalid-character-in-node-name");
                    }
                }
                DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, pinode.getData(), xml11Version);
                break;
            case 8:
                if ((this.features & 32) != 0) {
                    DOMNormalizer.isCommentWF(this.fErrorHandler, this.fError, this.fLocator, ((Comment) node).getData(), xml11Version);
                    break;
                }
                break;
        }
        this.fLocator.fRelatedNode = null;
    }

    private String _getXmlVersion(Node node) {
        Document doc = node.getNodeType() == 9 ? (Document) node : node.getOwnerDocument();
        if (doc == null || !DocumentMethods.fgDocumentMethodsAvailable) {
            return null;
        }
        try {
            return (String) DocumentMethods.fgDocumentGetXmlVersionMethod.invoke(doc, (Object[]) null);
        } catch (ThreadDeath td) {
            throw td;
        } catch (VirtualMachineError vme) {
            throw vme;
        } catch (Throwable th) {
            return null;
        }
    }

    private String _getInputEncoding(Node node) {
        Document doc = node.getNodeType() == 9 ? (Document) node : node.getOwnerDocument();
        if (doc == null || !DocumentMethods.fgDocumentMethodsAvailable) {
            return null;
        }
        try {
            return (String) DocumentMethods.fgDocumentGetInputEncodingMethod.invoke(doc, (Object[]) null);
        } catch (ThreadDeath td) {
            throw td;
        } catch (VirtualMachineError vme) {
            throw vme;
        } catch (Throwable th) {
            return null;
        }
    }

    private String _getXmlEncoding(Node node) {
        Document doc = node.getNodeType() == 9 ? (Document) node : node.getOwnerDocument();
        if (doc == null || !DocumentMethods.fgDocumentMethodsAvailable) {
            return null;
        }
        try {
            return (String) DocumentMethods.fgDocumentGetXmlEncodingMethod.invoke(doc, (Object[]) null);
        } catch (ThreadDeath td) {
            throw td;
        } catch (VirtualMachineError vme) {
            throw vme;
        } catch (Throwable th) {
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/DOMSerializerImpl$DocumentMethods.class */
    static class DocumentMethods {
        private static java.lang.reflect.Method fgDocumentGetXmlVersionMethod;
        private static java.lang.reflect.Method fgDocumentGetInputEncodingMethod;
        private static java.lang.reflect.Method fgDocumentGetXmlEncodingMethod;
        private static boolean fgDocumentMethodsAvailable;

        static {
            fgDocumentGetXmlVersionMethod = null;
            fgDocumentGetInputEncodingMethod = null;
            fgDocumentGetXmlEncodingMethod = null;
            fgDocumentMethodsAvailable = false;
            try {
                fgDocumentGetXmlVersionMethod = Document.class.getMethod("getXmlVersion", new Class[0]);
                fgDocumentGetInputEncodingMethod = Document.class.getMethod("getInputEncoding", new Class[0]);
                fgDocumentGetXmlEncodingMethod = Document.class.getMethod("getXmlEncoding", new Class[0]);
                fgDocumentMethodsAvailable = true;
            } catch (Exception e2) {
                fgDocumentGetXmlVersionMethod = null;
                fgDocumentGetInputEncodingMethod = null;
                fgDocumentGetXmlEncodingMethod = null;
                fgDocumentMethodsAvailable = false;
            }
        }

        private DocumentMethods() {
        }
    }
}
