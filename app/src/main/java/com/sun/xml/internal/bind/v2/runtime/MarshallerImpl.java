package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.internal.bind.marshaller.DataWriter;
import com.sun.xml.internal.bind.marshaller.DumbEscapeHandler;
import com.sun.xml.internal.bind.marshaller.MinimumEscapeHandler;
import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
import com.sun.xml.internal.bind.marshaller.SAX2DOMEx;
import com.sun.xml.internal.bind.marshaller.XMLWriter;
import com.sun.xml.internal.bind.v2.runtime.output.C14nXmlOutput;
import com.sun.xml.internal.bind.v2.runtime.output.Encoded;
import com.sun.xml.internal.bind.v2.runtime.output.ForkXmlOutput;
import com.sun.xml.internal.bind.v2.runtime.output.IndentingUTF8XmlOutput;
import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput;
import com.sun.xml.internal.bind.v2.runtime.output.XMLEventWriterOutput;
import com.sun.xml.internal.bind.v2.runtime.output.XMLStreamWriterOutput;
import com.sun.xml.internal.bind.v2.runtime.output.XmlOutput;
import com.sun.xml.internal.bind.v2.util.FatalAdapter;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.helpers.AbstractMarshallerImpl;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.ValidatorHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/MarshallerImpl.class */
public final class MarshallerImpl extends AbstractMarshallerImpl implements ValidationEventHandler {
    final JAXBContextImpl context;
    private Schema schema;
    private boolean c14nSupport;
    private Flushable toBeFlushed;
    private Closeable toBeClosed;
    protected static final String INDENT_STRING = "com.sun.xml.internal.bind.indentString";
    protected static final String PREFIX_MAPPER = "com.sun.xml.internal.bind.namespacePrefixMapper";
    protected static final String ENCODING_HANDLER = "com.sun.xml.internal.bind.characterEscapeHandler";
    protected static final String ENCODING_HANDLER2 = "com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler";
    protected static final String XMLDECLARATION = "com.sun.xml.internal.bind.xmlDeclaration";
    protected static final String XML_HEADERS = "com.sun.xml.internal.bind.xmlHeaders";
    protected static final String C14N = "com.sun.xml.internal.bind.c14n";
    protected static final String OBJECT_IDENTITY_CYCLE_DETECTION = "com.sun.xml.internal.bind.objectIdentitityCycleDetection";
    static final /* synthetic */ boolean $assertionsDisabled;
    private String indent = "    ";
    private NamespacePrefixMapper prefixMapper = null;
    private CharacterEscapeHandler escapeHandler = null;
    private String header = null;
    private Marshaller.Listener externalListener = null;
    protected final XMLSerializer serializer = new XMLSerializer(this);

    static {
        $assertionsDisabled = !MarshallerImpl.class.desiredAssertionStatus();
    }

    public MarshallerImpl(JAXBContextImpl c2, AssociationMap assoc) {
        this.context = c2;
        this.c14nSupport = this.context.c14nSupport;
        try {
            setEventHandler(this);
        } catch (JAXBException e2) {
            throw new AssertionError(e2);
        }
    }

    public JAXBContextImpl getContext() {
        return this.context;
    }

    public void marshal(Object obj, OutputStream out, NamespaceContext inscopeNamespace) throws JAXBException {
        write(obj, createWriter(out), new StAXPostInitAction(inscopeNamespace, this.serializer));
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public void marshal(Object obj, XMLStreamWriter writer) throws JAXBException {
        write(obj, XMLStreamWriterOutput.create(writer, this.context, this.escapeHandler), new StAXPostInitAction(writer, this.serializer));
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public void marshal(Object obj, XMLEventWriter writer) throws JAXBException {
        write(obj, new XMLEventWriterOutput(writer), new StAXPostInitAction(writer, this.serializer));
    }

    public void marshal(Object obj, XmlOutput output) throws JAXBException {
        write(obj, output, null);
    }

    final XmlOutput createXmlOutput(Result result) throws JAXBException {
        if (result instanceof SAXResult) {
            return new SAXOutput(((SAXResult) result).getHandler());
        }
        if (result instanceof DOMResult) {
            Node node = ((DOMResult) result).getNode();
            if (node == null) {
                Document doc = JAXBContextImpl.createDom(getContext().disableSecurityProcessing);
                ((DOMResult) result).setNode(doc);
                return new SAXOutput(new SAX2DOMEx(doc));
            }
            return new SAXOutput(new SAX2DOMEx(node));
        }
        if (result instanceof StreamResult) {
            StreamResult sr = (StreamResult) result;
            if (sr.getWriter() != null) {
                return createWriter(sr.getWriter());
            }
            if (sr.getOutputStream() != null) {
                return createWriter(sr.getOutputStream());
            }
            if (sr.getSystemId() != null) {
                String fileURL = sr.getSystemId();
                try {
                    fileURL = new URI(fileURL).getPath();
                } catch (URISyntaxException e2) {
                }
                try {
                    FileOutputStream fos = new FileOutputStream(fileURL);
                    if (!$assertionsDisabled && this.toBeClosed != null) {
                        throw new AssertionError();
                    }
                    this.toBeClosed = fos;
                    return createWriter(fos);
                } catch (IOException e3) {
                    throw new MarshalException(e3);
                }
            }
        }
        throw new MarshalException(Messages.UNSUPPORTED_RESULT.format(new Object[0]));
    }

    final Runnable createPostInitAction(Result result) {
        if (result instanceof DOMResult) {
            Node node = ((DOMResult) result).getNode();
            return new DomPostInitAction(node, this.serializer);
        }
        return null;
    }

    @Override // javax.xml.bind.Marshaller
    public void marshal(Object target, Result result) throws JAXBException {
        write(target, createXmlOutput(result), createPostInitAction(result));
    }

    protected final <T> void write(Name rootTagName, JaxBeanInfo<T> bi2, T obj, XmlOutput out, Runnable postInitAction) throws JAXBException {
        try {
            try {
                try {
                    prewrite(out, true, postInitAction);
                    this.serializer.startElement(rootTagName, null);
                    if (bi2.jaxbType == Void.class || bi2.jaxbType == Void.TYPE) {
                        this.serializer.endNamespaceDecls(null);
                        this.serializer.endAttributes();
                    } else if (obj == null) {
                        this.serializer.writeXsiNilTrue();
                    } else {
                        this.serializer.childAsXsiType(obj, "root", bi2, false);
                    }
                    this.serializer.endElement();
                    postwrite();
                    this.serializer.close();
                } catch (IOException e2) {
                    throw new MarshalException(e2);
                } catch (XMLStreamException e3) {
                    throw new MarshalException(e3);
                } catch (SAXException e4) {
                    throw new MarshalException(e4);
                }
            } catch (Throwable th) {
                this.serializer.close();
                throw th;
            }
        } finally {
            cleanUp();
        }
    }

    private void write(Object obj, XmlOutput out, Runnable postInitAction) throws JAXBException {
        try {
            if (obj == null) {
                throw new IllegalArgumentException(Messages.NOT_MARSHALLABLE.format(new Object[0]));
            }
            if (this.schema != null) {
                ValidatorHandler validator = this.schema.newValidatorHandler();
                validator.setErrorHandler(new FatalAdapter(this.serializer));
                XMLFilterImpl f2 = new XMLFilterImpl() { // from class: com.sun.xml.internal.bind.v2.runtime.MarshallerImpl.1
                    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
                    public void startPrefixMapping(String prefix, String uri) throws SAXException {
                        super.startPrefixMapping(prefix.intern(), uri.intern());
                    }
                };
                f2.setContentHandler(validator);
                out = new ForkXmlOutput(new SAXOutput(f2) { // from class: com.sun.xml.internal.bind.v2.runtime.MarshallerImpl.2
                    @Override // com.sun.xml.internal.bind.v2.runtime.output.SAXOutput, com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
                    public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, XMLStreamException, IOException {
                        super.startDocument(serializer, false, nsUriIndex2prefixIndex, nsContext);
                    }

                    @Override // com.sun.xml.internal.bind.v2.runtime.output.SAXOutput, com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
                    public void endDocument(boolean fragment) throws SAXException, XMLStreamException, IOException {
                        super.endDocument(false);
                    }
                }, out);
            }
            try {
                try {
                    prewrite(out, isFragment(), postInitAction);
                    this.serializer.childAsRoot(obj);
                    postwrite();
                    this.serializer.close();
                } catch (IOException e2) {
                    throw new MarshalException(e2);
                } catch (XMLStreamException e3) {
                    throw new MarshalException(e3);
                } catch (SAXException e4) {
                    throw new MarshalException(e4);
                }
            } catch (Throwable th) {
                this.serializer.close();
                throw th;
            }
        } finally {
            cleanUp();
        }
    }

    private void cleanUp() {
        if (this.toBeFlushed != null) {
            try {
                this.toBeFlushed.flush();
            } catch (IOException e2) {
            }
        }
        if (this.toBeClosed != null) {
            try {
                this.toBeClosed.close();
            } catch (IOException e3) {
            }
        }
        this.toBeFlushed = null;
        this.toBeClosed = null;
    }

    private void prewrite(XmlOutput out, boolean fragment, Runnable postInitAction) throws SAXException, XMLStreamException, IOException {
        String[] decls;
        this.serializer.startDocument(out, fragment, getSchemaLocation(), getNoNSSchemaLocation());
        if (postInitAction != null) {
            postInitAction.run();
        }
        if (this.prefixMapper != null && (decls = this.prefixMapper.getContextualNamespaceDecls()) != null) {
            for (int i2 = 0; i2 < decls.length; i2 += 2) {
                String prefix = decls[i2];
                String nsUri = decls[i2 + 1];
                if (nsUri != null && prefix != null) {
                    this.serializer.addInscopeBinding(nsUri, prefix);
                }
            }
        }
        this.serializer.setPrefixMapper(this.prefixMapper);
    }

    private void postwrite() throws SAXException, XMLStreamException, IOException {
        this.serializer.endDocument();
        this.serializer.reconcileID();
    }

    CharacterEscapeHandler getEscapeHandler() {
        return this.escapeHandler;
    }

    protected CharacterEscapeHandler createEscapeHandler(String encoding) {
        if (this.escapeHandler != null) {
            return this.escapeHandler;
        }
        if (encoding.startsWith("UTF")) {
            return MinimumEscapeHandler.theInstance;
        }
        try {
            return new NioEscapeHandler(getJavaEncoding(encoding));
        } catch (Throwable th) {
            return DumbEscapeHandler.theInstance;
        }
    }

    public XmlOutput createWriter(Writer w2, String encoding) {
        XMLWriter xw;
        if (!(w2 instanceof BufferedWriter)) {
            w2 = new BufferedWriter(w2);
        }
        if (!$assertionsDisabled && this.toBeFlushed != null) {
            throw new AssertionError();
        }
        this.toBeFlushed = w2;
        CharacterEscapeHandler ceh = createEscapeHandler(encoding);
        if (isFormattedOutput()) {
            DataWriter d2 = new DataWriter(w2, encoding, ceh);
            d2.setIndentStep(this.indent);
            xw = d2;
        } else {
            xw = new XMLWriter(w2, encoding, ceh);
        }
        xw.setXmlDecl(!isFragment());
        xw.setHeader(this.header);
        return new SAXOutput(xw);
    }

    public XmlOutput createWriter(Writer w2) {
        return createWriter(w2, getEncoding());
    }

    public XmlOutput createWriter(OutputStream os) throws JAXBException {
        return createWriter(os, getEncoding());
    }

    public XmlOutput createWriter(OutputStream os, String encoding) throws JAXBException {
        UTF8XmlOutput out;
        if (encoding.equals("UTF-8")) {
            Encoded[] table = this.context.getUTF8NameTable();
            if (isFormattedOutput()) {
                out = new IndentingUTF8XmlOutput(os, this.indent, table, this.escapeHandler);
            } else if (this.c14nSupport) {
                out = new C14nXmlOutput(os, table, this.context.c14nSupport, this.escapeHandler);
            } else {
                out = new UTF8XmlOutput(os, table, this.escapeHandler);
            }
            if (this.header != null) {
                out.setHeader(this.header);
            }
            return out;
        }
        try {
            return createWriter(new OutputStreamWriter(os, getJavaEncoding(encoding)), encoding);
        } catch (UnsupportedEncodingException e2) {
            throw new MarshalException(Messages.UNSUPPORTED_ENCODING.format(encoding), e2);
        }
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public Object getProperty(String name) throws PropertyException {
        if (INDENT_STRING.equals(name)) {
            return this.indent;
        }
        if (ENCODING_HANDLER.equals(name) || ENCODING_HANDLER2.equals(name)) {
            return this.escapeHandler;
        }
        if (PREFIX_MAPPER.equals(name)) {
            return this.prefixMapper;
        }
        if (XMLDECLARATION.equals(name)) {
            return Boolean.valueOf(!isFragment());
        }
        if (XML_HEADERS.equals(name)) {
            return this.header;
        }
        if ("com.sun.xml.internal.bind.c14n".equals(name)) {
            return Boolean.valueOf(this.c14nSupport);
        }
        if (OBJECT_IDENTITY_CYCLE_DETECTION.equals(name)) {
            return Boolean.valueOf(this.serializer.getObjectIdentityCycleDetection());
        }
        return super.getProperty(name);
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public void setProperty(String name, Object value) throws PropertyException {
        if (INDENT_STRING.equals(name)) {
            checkString(name, value);
            this.indent = (String) value;
            return;
        }
        if (ENCODING_HANDLER.equals(name) || ENCODING_HANDLER2.equals(name)) {
            if (!(value instanceof CharacterEscapeHandler)) {
                throw new PropertyException(Messages.MUST_BE_X.format(name, CharacterEscapeHandler.class.getName(), value.getClass().getName()));
            }
            this.escapeHandler = (CharacterEscapeHandler) value;
            return;
        }
        if (PREFIX_MAPPER.equals(name)) {
            if (!(value instanceof NamespacePrefixMapper)) {
                throw new PropertyException(Messages.MUST_BE_X.format(name, NamespacePrefixMapper.class.getName(), value.getClass().getName()));
            }
            this.prefixMapper = (NamespacePrefixMapper) value;
            return;
        }
        if (XMLDECLARATION.equals(name)) {
            checkBoolean(name, value);
            super.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.valueOf(!((Boolean) value).booleanValue()));
            return;
        }
        if (XML_HEADERS.equals(name)) {
            checkString(name, value);
            this.header = (String) value;
        } else if ("com.sun.xml.internal.bind.c14n".equals(name)) {
            checkBoolean(name, value);
            this.c14nSupport = ((Boolean) value).booleanValue();
        } else if (OBJECT_IDENTITY_CYCLE_DETECTION.equals(name)) {
            checkBoolean(name, value);
            this.serializer.setObjectIdentityCycleDetection(((Boolean) value).booleanValue());
        } else {
            super.setProperty(name, value);
        }
    }

    private void checkBoolean(String name, Object value) throws PropertyException {
        if (!(value instanceof Boolean)) {
            throw new PropertyException(Messages.MUST_BE_X.format(name, Boolean.class.getName(), value.getClass().getName()));
        }
    }

    private void checkString(String name, Object value) throws PropertyException {
        if (!(value instanceof String)) {
            throw new PropertyException(Messages.MUST_BE_X.format(name, String.class.getName(), value.getClass().getName()));
        }
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public <A extends XmlAdapter> void setAdapter(Class<A> type, A adapter) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        this.serializer.putAdapter(type, adapter);
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public <A extends XmlAdapter> A getAdapter(Class<A> cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        if (this.serializer.containsAdapter(cls)) {
            return (A) this.serializer.getAdapter(cls);
        }
        return null;
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public void setAttachmentMarshaller(AttachmentMarshaller am2) {
        this.serializer.attachmentMarshaller = am2;
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public AttachmentMarshaller getAttachmentMarshaller() {
        return this.serializer.attachmentMarshaller;
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public Schema getSchema() {
        return this.schema;
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public void setSchema(Schema s2) {
        this.schema = s2;
    }

    @Override // javax.xml.bind.ValidationEventHandler
    public boolean handleEvent(ValidationEvent event) {
        return false;
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public Marshaller.Listener getListener() {
        return this.externalListener;
    }

    @Override // javax.xml.bind.helpers.AbstractMarshallerImpl, javax.xml.bind.Marshaller
    public void setListener(Marshaller.Listener listener) {
        this.externalListener = listener;
    }
}
