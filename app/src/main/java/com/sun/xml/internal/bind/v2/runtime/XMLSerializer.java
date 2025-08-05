package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.SAXException2;
import com.sun.xml.internal.bind.CycleRecoverable;
import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.internal.bind.util.ValidationEventLocatorExImpl;
import com.sun.xml.internal.bind.v2.runtime.output.MTOMXmlOutput;
import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
import com.sun.xml.internal.bind.v2.runtime.output.XmlOutput;
import com.sun.xml.internal.bind.v2.runtime.property.Property;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.IntData;
import com.sun.xml.internal.bind.v2.util.CollisionCheckStack;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.activation.MimeType;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.helpers.NotIdentifiableEventImpl;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/XMLSerializer.class */
public final class XMLSerializer extends Coordinator {
    public final JAXBContextImpl grammar;
    private XmlOutput out;
    public final NameList nameList;
    public final int[] knownUri2prefixIndexMap;
    private NamespaceContextImpl.Element nse;
    private final MarshallerImpl marshaller;
    private String schemaLocation;
    private String noNsSchemaLocation;
    private Transformer identityTransformer;
    private ContentHandlerAdaptor contentHandlerAdapter;
    private boolean fragment;
    private Base64Data base64Data;
    public AttachmentMarshaller attachmentMarshaller;
    private MimeType expectedMimeType;
    private boolean inlineBinaryFlag;
    private QName schemaType;
    ThreadLocal<Property> currentProperty = new ThreadLocal<>();
    private boolean textHasAlreadyPrinted = false;
    private boolean seenRoot = false;
    private final Set<Object> idReferencedObjects = new HashSet();
    private final Set<Object> objectsWithId = new HashSet();
    private final CollisionCheckStack<Object> cycleDetectionStack = new CollisionCheckStack<>();
    private final IntData intData = new IntData();
    private final NamespaceContextImpl nsContext = new NamespaceContextImpl(this);

    XMLSerializer(MarshallerImpl _owner) {
        this.marshaller = _owner;
        this.grammar = this.marshaller.context;
        this.nameList = this.marshaller.context.nameList;
        this.knownUri2prefixIndexMap = new int[this.nameList.namespaceURIs.length];
    }

    public Base64Data getCachedBase64DataInstance() {
        return new Base64Data();
    }

    private String getIdFromObject(Object identifiableObject) throws SAXException, JAXBException {
        return this.grammar.getBeanInfo(identifiableObject, true).getId(identifiableObject, this);
    }

    private void handleMissingObjectError(String fieldName) throws SAXException, XMLStreamException, IOException {
        reportMissingObjectError(fieldName);
        endNamespaceDecls(null);
        endAttributes();
    }

    public void reportError(ValidationEvent ve) throws SAXException {
        try {
            ValidationEventHandler handler = this.marshaller.getEventHandler();
            if (!handler.handleEvent(ve)) {
                if (ve.getLinkedException() instanceof Exception) {
                    throw new SAXException2((Exception) ve.getLinkedException());
                }
                throw new SAXException2(ve.getMessage());
            }
        } catch (JAXBException e2) {
            throw new SAXException2(e2);
        }
    }

    public final void reportError(String fieldName, Throwable t2) throws SAXException {
        ValidationEvent ve = new ValidationEventImpl(1, t2.getMessage(), getCurrentLocation(fieldName), t2);
        reportError(ve);
    }

    public void startElement(Name tagName, Object outerPeer) {
        startElement();
        this.nse.setTagName(tagName, outerPeer);
    }

    public void startElement(String nsUri, String localName, String preferredPrefix, Object outerPeer) {
        startElement();
        int idx = this.nsContext.declareNsUri(nsUri, preferredPrefix, false);
        this.nse.setTagName(idx, localName, outerPeer);
    }

    public void startElementForce(String nsUri, String localName, String forcedPrefix, Object outerPeer) {
        startElement();
        int idx = this.nsContext.force(nsUri, forcedPrefix);
        this.nse.setTagName(idx, localName, outerPeer);
    }

    public void endNamespaceDecls(Object innerPeer) throws XMLStreamException, IOException {
        this.nsContext.collectionMode = false;
        this.nse.startElement(this.out, innerPeer);
    }

    public void endAttributes() throws XMLStreamException, SAXException, IOException {
        if (!this.seenRoot) {
            this.seenRoot = true;
            if (this.schemaLocation != null || this.noNsSchemaLocation != null) {
                int p2 = this.nsContext.getPrefixIndex("http://www.w3.org/2001/XMLSchema-instance");
                if (this.schemaLocation != null) {
                    this.out.attribute(p2, "schemaLocation", this.schemaLocation);
                }
                if (this.noNsSchemaLocation != null) {
                    this.out.attribute(p2, "noNamespaceSchemaLocation", this.noNsSchemaLocation);
                }
            }
        }
        this.out.endStartTag();
    }

    public void endElement() throws SAXException, XMLStreamException, IOException {
        this.nse.endElement(this.out);
        this.nse = this.nse.pop();
        this.textHasAlreadyPrinted = false;
    }

    public void leafElement(Name tagName, String data, String fieldName) throws XMLStreamException, SAXException, IOException {
        if (this.seenRoot) {
            this.textHasAlreadyPrinted = false;
            this.nse = this.nse.push();
            this.out.beginStartTag(tagName);
            this.out.endStartTag();
            if (data != null) {
                try {
                    this.out.text(data, false);
                } catch (IllegalArgumentException e2) {
                    throw new IllegalArgumentException(Messages.ILLEGAL_CONTENT.format(fieldName, e2.getMessage()));
                }
            }
            this.out.endTag(tagName);
            this.nse = this.nse.pop();
            return;
        }
        startElement(tagName, null);
        endNamespaceDecls(null);
        endAttributes();
        try {
            this.out.text(data, false);
            endElement();
        } catch (IllegalArgumentException e3) {
            throw new IllegalArgumentException(Messages.ILLEGAL_CONTENT.format(fieldName, e3.getMessage()));
        }
    }

    public void leafElement(Name tagName, Pcdata data, String fieldName) throws XMLStreamException, SAXException, IOException {
        if (this.seenRoot) {
            this.textHasAlreadyPrinted = false;
            this.nse = this.nse.push();
            this.out.beginStartTag(tagName);
            this.out.endStartTag();
            if (data != null) {
                this.out.text(data, false);
            }
            this.out.endTag(tagName);
            this.nse = this.nse.pop();
            return;
        }
        startElement(tagName, null);
        endNamespaceDecls(null);
        endAttributes();
        this.out.text(data, false);
        endElement();
    }

    public void leafElement(Name tagName, int data, String fieldName) throws XMLStreamException, SAXException, IOException {
        this.intData.reset(data);
        leafElement(tagName, this.intData, fieldName);
    }

    public void text(String text, String fieldName) throws SAXException, XMLStreamException, IOException {
        if (text == null) {
            reportMissingObjectError(fieldName);
        } else {
            this.out.text(text, this.textHasAlreadyPrinted);
            this.textHasAlreadyPrinted = true;
        }
    }

    public void text(Pcdata text, String fieldName) throws SAXException, XMLStreamException, IOException {
        if (text == null) {
            reportMissingObjectError(fieldName);
        } else {
            this.out.text(text, this.textHasAlreadyPrinted);
            this.textHasAlreadyPrinted = true;
        }
    }

    public void attribute(String uri, String local, String value) throws SAXException {
        int prefix;
        if (uri.length() == 0) {
            prefix = -1;
        } else {
            prefix = this.nsContext.getPrefixIndex(uri);
        }
        try {
            this.out.attribute(prefix, local, value);
        } catch (IOException e2) {
            throw new SAXException2(e2);
        } catch (XMLStreamException e3) {
            throw new SAXException2(e3);
        }
    }

    public void attribute(Name name, CharSequence value) throws XMLStreamException, IOException {
        this.out.attribute(name, value.toString());
    }

    public NamespaceContext2 getNamespaceContext() {
        return this.nsContext;
    }

    public String onID(Object owner, String value) {
        this.objectsWithId.add(owner);
        return value;
    }

    public String onIDREF(Object obj) throws SAXException {
        try {
            String id = getIdFromObject(obj);
            this.idReferencedObjects.add(obj);
            if (id == null) {
                reportError(new NotIdentifiableEventImpl(1, Messages.NOT_IDENTIFIABLE.format(new Object[0]), new ValidationEventLocatorImpl(obj)));
            }
            return id;
        } catch (JAXBException e2) {
            reportError(null, e2);
            return null;
        }
    }

    public void childAsRoot(Object obj) throws SAXException, XMLStreamException, IOException, JAXBException {
        JaxBeanInfo beanInfo = this.grammar.getBeanInfo(obj, true);
        this.cycleDetectionStack.pushNocheck(obj);
        boolean lookForLifecycleMethods = beanInfo.lookForLifecycleMethods();
        if (lookForLifecycleMethods) {
            fireBeforeMarshalEvents(beanInfo, obj);
        }
        beanInfo.serializeRoot(obj, this);
        if (lookForLifecycleMethods) {
            fireAfterMarshalEvents(beanInfo, obj);
        }
        this.cycleDetectionStack.pop();
    }

    private Object pushObject(Object obj, String fieldName) throws SAXException {
        if (!this.cycleDetectionStack.push(obj)) {
            return obj;
        }
        if (obj instanceof CycleRecoverable) {
            Object obj2 = ((CycleRecoverable) obj).onCycleDetected(new CycleRecoverable.Context() { // from class: com.sun.xml.internal.bind.v2.runtime.XMLSerializer.1
                @Override // com.sun.xml.internal.bind.CycleRecoverable.Context
                public Marshaller getMarshaller() {
                    return XMLSerializer.this.marshaller;
                }
            });
            if (obj2 != null) {
                this.cycleDetectionStack.pop();
                return pushObject(obj2, fieldName);
            }
            return null;
        }
        reportError(new ValidationEventImpl(1, Messages.CYCLE_IN_MARSHALLER.format(this.cycleDetectionStack.getCycleString()), getCurrentLocation(fieldName), null));
        return null;
    }

    public final void childAsSoleContent(Object child, String fieldName) throws SAXException, XMLStreamException, IOException {
        if (child == null) {
            handleMissingObjectError(fieldName);
            return;
        }
        Object child2 = pushObject(child, fieldName);
        if (child2 == null) {
            endNamespaceDecls(null);
            endAttributes();
            this.cycleDetectionStack.pop();
        }
        try {
            JaxBeanInfo beanInfo = this.grammar.getBeanInfo(child2, true);
            boolean lookForLifecycleMethods = beanInfo.lookForLifecycleMethods();
            if (lookForLifecycleMethods) {
                fireBeforeMarshalEvents(beanInfo, child2);
            }
            beanInfo.serializeURIs(child2, this);
            endNamespaceDecls(child2);
            beanInfo.serializeAttributes(child2, this);
            endAttributes();
            beanInfo.serializeBody(child2, this);
            if (lookForLifecycleMethods) {
                fireAfterMarshalEvents(beanInfo, child2);
            }
            this.cycleDetectionStack.pop();
        } catch (JAXBException e2) {
            reportError(fieldName, e2);
            endNamespaceDecls(null);
            endAttributes();
            this.cycleDetectionStack.pop();
        }
    }

    public final void childAsXsiType(Object child, String fieldName, JaxBeanInfo expected, boolean nillable) throws SAXException, XMLStreamException, IOException {
        if (child == null) {
            handleMissingObjectError(fieldName);
            return;
        }
        Object child2 = pushObject(child, fieldName);
        if (child2 == null) {
            endNamespaceDecls(null);
            endAttributes();
            return;
        }
        boolean asExpected = child2.getClass() == expected.jaxbType;
        JaxBeanInfo actual = expected;
        QName actualTypeName = null;
        if (asExpected && actual.lookForLifecycleMethods()) {
            fireBeforeMarshalEvents(actual, child2);
        }
        if (!asExpected) {
            try {
                actual = this.grammar.getBeanInfo(child2, true);
                if (actual.lookForLifecycleMethods()) {
                    fireBeforeMarshalEvents(actual, child2);
                }
                if (actual == expected) {
                    asExpected = true;
                } else {
                    actualTypeName = actual.getTypeName(child2);
                    if (actualTypeName == null) {
                        reportError(new ValidationEventImpl(1, Messages.SUBSTITUTED_BY_ANONYMOUS_TYPE.format(expected.jaxbType.getName(), child2.getClass().getName(), actual.jaxbType.getName()), getCurrentLocation(fieldName)));
                    } else {
                        getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
                        getNamespaceContext().declareNamespace(actualTypeName.getNamespaceURI(), null, false);
                    }
                }
            } catch (JAXBException e2) {
                reportError(fieldName, e2);
                endNamespaceDecls(null);
                endAttributes();
                return;
            }
        }
        actual.serializeURIs(child2, this);
        if (nillable) {
            getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
        }
        endNamespaceDecls(child2);
        if (!asExpected) {
            attribute("http://www.w3.org/2001/XMLSchema-instance", "type", DatatypeConverter.printQName(actualTypeName, getNamespaceContext()));
        }
        actual.serializeAttributes(child2, this);
        boolean nilDefined = actual.isNilIncluded();
        if (nillable && !nilDefined) {
            attribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "true");
        }
        endAttributes();
        actual.serializeBody(child2, this);
        if (actual.lookForLifecycleMethods()) {
            fireAfterMarshalEvents(actual, child2);
        }
        this.cycleDetectionStack.pop();
    }

    private void fireAfterMarshalEvents(JaxBeanInfo beanInfo, Object currentTarget) {
        if (beanInfo.hasAfterMarshalMethod()) {
            Method m2 = beanInfo.getLifecycleMethods().afterMarshal;
            fireMarshalEvent(currentTarget, m2);
        }
        Marshaller.Listener externalListener = this.marshaller.getListener();
        if (externalListener != null) {
            externalListener.afterMarshal(currentTarget);
        }
    }

    private void fireBeforeMarshalEvents(JaxBeanInfo beanInfo, Object currentTarget) {
        if (beanInfo.hasBeforeMarshalMethod()) {
            Method m2 = beanInfo.getLifecycleMethods().beforeMarshal;
            fireMarshalEvent(currentTarget, m2);
        }
        Marshaller.Listener externalListener = this.marshaller.getListener();
        if (externalListener != null) {
            externalListener.beforeMarshal(currentTarget);
        }
    }

    private void fireMarshalEvent(Object target, Method m2) {
        try {
            m2.invoke(target, this.marshaller);
        } catch (Exception e2) {
            throw new IllegalStateException(e2);
        }
    }

    public void attWildcardAsURIs(Map<QName, String> attributes, String fieldName) {
        if (attributes == null) {
            return;
        }
        for (Map.Entry<QName, String> e2 : attributes.entrySet()) {
            QName n2 = e2.getKey();
            String nsUri = n2.getNamespaceURI();
            if (nsUri.length() > 0) {
                String p2 = n2.getPrefix();
                if (p2.length() == 0) {
                    p2 = null;
                }
                this.nsContext.declareNsUri(nsUri, p2, true);
            }
        }
    }

    public void attWildcardAsAttributes(Map<QName, String> attributes, String fieldName) throws SAXException {
        if (attributes == null) {
            return;
        }
        for (Map.Entry<QName, String> e2 : attributes.entrySet()) {
            QName n2 = e2.getKey();
            attribute(n2.getNamespaceURI(), n2.getLocalPart(), e2.getValue());
        }
    }

    public final void writeXsiNilTrue() throws XMLStreamException, SAXException, IOException {
        getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
        endNamespaceDecls(null);
        attribute("http://www.w3.org/2001/XMLSchema-instance", "nil", "true");
        endAttributes();
    }

    public <E> void writeDom(E element, DomHandler<E, ?> domHandler, Object parentBean, String fieldName) throws SAXException {
        Source source = domHandler.marshal(element, this);
        if (this.contentHandlerAdapter == null) {
            this.contentHandlerAdapter = new ContentHandlerAdaptor(this);
        }
        try {
            getIdentityTransformer().transform(source, new SAXResult(this.contentHandlerAdapter));
        } catch (TransformerException e2) {
            reportError(fieldName, e2);
        }
    }

    public Transformer getIdentityTransformer() {
        if (this.identityTransformer == null) {
            this.identityTransformer = JAXBContextImpl.createTransformer(this.grammar.disableSecurityProcessing);
        }
        return this.identityTransformer;
    }

    public void setPrefixMapper(NamespacePrefixMapper prefixMapper) {
        this.nsContext.setPrefixMapper(prefixMapper);
    }

    public void startDocument(XmlOutput out, boolean fragment, String schemaLocation, String noNsSchemaLocation) throws SAXException, XMLStreamException, IOException {
        pushCoordinator();
        this.nsContext.reset();
        this.nse = this.nsContext.getCurrent();
        if (this.attachmentMarshaller != null && this.attachmentMarshaller.isXOPPackage()) {
            out = new MTOMXmlOutput(out);
        }
        this.out = out;
        this.objectsWithId.clear();
        this.idReferencedObjects.clear();
        this.textHasAlreadyPrinted = false;
        this.seenRoot = false;
        this.schemaLocation = schemaLocation;
        this.noNsSchemaLocation = noNsSchemaLocation;
        this.fragment = fragment;
        this.inlineBinaryFlag = false;
        this.expectedMimeType = null;
        this.cycleDetectionStack.reset();
        out.startDocument(this, fragment, this.knownUri2prefixIndexMap, this.nsContext);
    }

    public void endDocument() throws SAXException, XMLStreamException, IOException {
        this.out.endDocument(this.fragment);
    }

    public void close() {
        this.out = null;
        clearCurrentProperty();
        popCoordinator();
    }

    public void addInscopeBinding(String nsUri, String prefix) {
        this.nsContext.put(nsUri, prefix);
    }

    public String getXMIMEContentType() {
        String v2 = this.grammar.getXMIMEContentType(this.cycleDetectionStack.peek());
        if (v2 != null) {
            return v2;
        }
        if (this.expectedMimeType != null) {
            return this.expectedMimeType.toString();
        }
        return null;
    }

    private void startElement() {
        this.nse = this.nse.push();
        if (!this.seenRoot) {
            if (this.grammar.getXmlNsSet() != null) {
                for (XmlNs xmlNs : this.grammar.getXmlNsSet()) {
                    this.nsContext.declareNsUri(xmlNs.namespaceURI(), xmlNs.prefix() == null ? "" : xmlNs.prefix(), xmlNs.prefix() != null);
                }
            }
            String[] knownUris = this.nameList.namespaceURIs;
            for (int i2 = 0; i2 < knownUris.length; i2++) {
                this.knownUri2prefixIndexMap[i2] = this.nsContext.declareNsUri(knownUris[i2], null, this.nameList.nsUriCannotBeDefaulted[i2]);
            }
            String[] uris = this.nsContext.getPrefixMapper().getPreDeclaredNamespaceUris();
            if (uris != null) {
                for (String uri : uris) {
                    if (uri != null) {
                        this.nsContext.declareNsUri(uri, null, false);
                    }
                }
            }
            String[] pairs = this.nsContext.getPrefixMapper().getPreDeclaredNamespaceUris2();
            if (pairs != null) {
                for (int i3 = 0; i3 < pairs.length; i3 += 2) {
                    String prefix = pairs[i3];
                    String nsUri = pairs[i3 + 1];
                    if (prefix != null && nsUri != null) {
                        this.nsContext.put(nsUri, prefix);
                    }
                }
            }
            if (this.schemaLocation != null || this.noNsSchemaLocation != null) {
                this.nsContext.declareNsUri("http://www.w3.org/2001/XMLSchema-instance", "xsi", true);
            }
        }
        this.nsContext.collectionMode = true;
        this.textHasAlreadyPrinted = false;
    }

    public MimeType setExpectedMimeType(MimeType expectedMimeType) {
        MimeType old = this.expectedMimeType;
        this.expectedMimeType = expectedMimeType;
        return old;
    }

    public boolean setInlineBinaryFlag(boolean value) {
        boolean old = this.inlineBinaryFlag;
        this.inlineBinaryFlag = value;
        return old;
    }

    public boolean getInlineBinaryFlag() {
        return this.inlineBinaryFlag;
    }

    public QName setSchemaType(QName st) {
        QName old = this.schemaType;
        this.schemaType = st;
        return old;
    }

    public QName getSchemaType() {
        return this.schemaType;
    }

    public void setObjectIdentityCycleDetection(boolean val) {
        this.cycleDetectionStack.setUseIdentity(val);
    }

    public boolean getObjectIdentityCycleDetection() {
        return this.cycleDetectionStack.getUseIdentity();
    }

    void reconcileID() throws SAXException {
        this.idReferencedObjects.removeAll(this.objectsWithId);
        for (Object idObj : this.idReferencedObjects) {
            try {
                String id = getIdFromObject(idObj);
                reportError(new NotIdentifiableEventImpl(1, Messages.DANGLING_IDREF.format(id), new ValidationEventLocatorImpl(idObj)));
            } catch (JAXBException e2) {
            }
        }
        this.idReferencedObjects.clear();
        this.objectsWithId.clear();
    }

    public boolean handleError(Exception e2) {
        return handleError(e2, this.cycleDetectionStack.peek(), null);
    }

    public boolean handleError(Exception e2, Object source, String fieldName) {
        return handleEvent(new ValidationEventImpl(1, e2.getMessage(), new ValidationEventLocatorExImpl(source, fieldName), e2));
    }

    @Override // javax.xml.bind.ValidationEventHandler
    public boolean handleEvent(ValidationEvent event) {
        try {
            return this.marshaller.getEventHandler().handleEvent(event);
        } catch (JAXBException e2) {
            throw new Error(e2);
        }
    }

    private void reportMissingObjectError(String fieldName) throws SAXException {
        reportError(new ValidationEventImpl(1, Messages.MISSING_OBJECT.format(fieldName), getCurrentLocation(fieldName), new NullPointerException()));
    }

    public void errorMissingId(Object obj) throws SAXException {
        reportError(new ValidationEventImpl(1, Messages.MISSING_ID.format(obj), new ValidationEventLocatorImpl(obj)));
    }

    public ValidationEventLocator getCurrentLocation(String fieldName) {
        return new ValidationEventLocatorExImpl(this.cycleDetectionStack.peek(), fieldName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Coordinator
    protected ValidationEventLocator getLocation() {
        return getCurrentLocation(null);
    }

    public Property getCurrentProperty() {
        return this.currentProperty.get();
    }

    public void clearCurrentProperty() {
        if (this.currentProperty != null) {
            this.currentProperty.remove();
        }
    }

    public static XMLSerializer getInstance() {
        return (XMLSerializer) Coordinator._getInstance();
    }
}
