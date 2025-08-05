package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.unmarshaller.InfosetScanner;
import com.sun.xml.internal.bind.v2.runtime.AssociationMap;
import com.sun.xml.internal.bind.v2.runtime.output.DOMOutput;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.SAXConnector;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/BinderImpl.class */
public class BinderImpl<XmlNode> extends Binder<XmlNode> {
    private final JAXBContextImpl context;
    private UnmarshallerImpl unmarshaller;
    private MarshallerImpl marshaller;
    private final InfosetScanner<XmlNode> scanner;
    private final AssociationMap<XmlNode> assoc = new AssociationMap<>();

    BinderImpl(JAXBContextImpl _context, InfosetScanner<XmlNode> scanner) {
        this.context = _context;
        this.scanner = scanner;
    }

    private UnmarshallerImpl getUnmarshaller() {
        if (this.unmarshaller == null) {
            this.unmarshaller = new UnmarshallerImpl(this.context, this.assoc);
        }
        return this.unmarshaller;
    }

    private MarshallerImpl getMarshaller() {
        if (this.marshaller == null) {
            this.marshaller = new MarshallerImpl(this.context, this.assoc);
        }
        return this.marshaller;
    }

    @Override // javax.xml.bind.Binder
    public void marshal(Object jaxbObject, XmlNode xmlNode) throws JAXBException {
        if (xmlNode == null || jaxbObject == null) {
            throw new IllegalArgumentException();
        }
        getMarshaller().marshal(jaxbObject, createOutput(xmlNode));
    }

    private DOMOutput createOutput(XmlNode xmlNode) {
        return new DOMOutput((Node) xmlNode, this.assoc);
    }

    @Override // javax.xml.bind.Binder
    public Object updateJAXB(XmlNode xmlNode) throws JAXBException {
        return associativeUnmarshal(xmlNode, true, null);
    }

    @Override // javax.xml.bind.Binder
    public Object unmarshal(XmlNode xmlNode) throws JAXBException {
        return associativeUnmarshal(xmlNode, false, null);
    }

    @Override // javax.xml.bind.Binder
    public <T> JAXBElement<T> unmarshal(XmlNode xmlNode, Class<T> expectedType) throws JAXBException {
        if (expectedType == null) {
            throw new IllegalArgumentException();
        }
        return (JAXBElement) associativeUnmarshal(xmlNode, true, expectedType);
    }

    @Override // javax.xml.bind.Binder
    public void setSchema(Schema schema) {
        getMarshaller().setSchema(schema);
        getUnmarshaller().setSchema(schema);
    }

    @Override // javax.xml.bind.Binder
    public Schema getSchema() {
        return getUnmarshaller().getSchema();
    }

    private Object associativeUnmarshal(XmlNode xmlNode, boolean inplace, Class expectedType) throws JAXBException {
        if (xmlNode == null) {
            throw new IllegalArgumentException();
        }
        JaxBeanInfo bi2 = null;
        if (expectedType != null) {
            bi2 = this.context.getBeanInfo(expectedType, true);
        }
        InterningXmlVisitor handler = new InterningXmlVisitor(getUnmarshaller().createUnmarshallerHandler(this.scanner, inplace, bi2));
        this.scanner.setContentHandler(new SAXConnector(handler, this.scanner.getLocator()));
        try {
            this.scanner.scan(xmlNode);
            return handler.getContext().getResult();
        } catch (SAXException e2) {
            throw this.unmarshaller.createUnmarshalException(e2);
        }
    }

    @Override // javax.xml.bind.Binder
    public XmlNode getXMLNode(Object jaxbObject) {
        if (jaxbObject == null) {
            throw new IllegalArgumentException();
        }
        AssociationMap.Entry<XmlNode> e2 = this.assoc.byPeer(jaxbObject);
        if (e2 == null) {
            return null;
        }
        return e2.element();
    }

    @Override // javax.xml.bind.Binder
    public Object getJAXBNode(XmlNode xmlNode) {
        if (xmlNode == null) {
            throw new IllegalArgumentException();
        }
        AssociationMap.Entry e2 = this.assoc.byElement(xmlNode);
        if (e2 == null) {
            return null;
        }
        return e2.outer() != null ? e2.outer() : e2.inner();
    }

    @Override // javax.xml.bind.Binder
    public XmlNode updateXML(Object obj) throws JAXBException {
        return updateXML(obj, getXMLNode(obj));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19, types: [XmlNode, org.w3c.dom.Node] */
    @Override // javax.xml.bind.Binder
    public XmlNode updateXML(Object obj, XmlNode xmlnode) throws DOMException, JAXBException {
        if (obj == null || xmlnode == null) {
            throw new IllegalArgumentException();
        }
        Element element = (Element) xmlnode;
        Node nextSibling = element.getNextSibling();
        Node parentNode = element.getParentNode();
        parentNode.removeChild(element);
        JaxBeanInfo beanInfo = this.context.getBeanInfo(obj, true);
        if (!beanInfo.isElement()) {
            obj = new JAXBElement(new QName(element.getNamespaceURI(), element.getLocalName()), beanInfo.jaxbType, obj);
        }
        getMarshaller().marshal(obj, parentNode);
        ?? r0 = (XmlNode) parentNode.getLastChild();
        parentNode.removeChild(r0);
        parentNode.insertBefore(r0, nextSibling);
        return r0;
    }

    @Override // javax.xml.bind.Binder
    public void setEventHandler(ValidationEventHandler handler) throws JAXBException {
        getUnmarshaller().setEventHandler(handler);
        getMarshaller().setEventHandler(handler);
    }

    @Override // javax.xml.bind.Binder
    public ValidationEventHandler getEventHandler() {
        return getUnmarshaller().getEventHandler();
    }

    @Override // javax.xml.bind.Binder
    public Object getProperty(String name) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException(Messages.NULL_PROPERTY_NAME.format(new Object[0]));
        }
        if (excludeProperty(name)) {
            throw new PropertyException(name);
        }
        try {
            Object prop = getMarshaller().getProperty(name);
            return prop;
        } catch (PropertyException e2) {
            try {
                Object prop2 = getUnmarshaller().getProperty(name);
                return prop2;
            } catch (PropertyException p2) {
                p2.setStackTrace(Thread.currentThread().getStackTrace());
                throw p2;
            }
        }
    }

    @Override // javax.xml.bind.Binder
    public void setProperty(String name, Object value) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException(Messages.NULL_PROPERTY_NAME.format(new Object[0]));
        }
        if (excludeProperty(name)) {
            throw new PropertyException(name, value);
        }
        try {
            getMarshaller().setProperty(name, value);
        } catch (PropertyException e2) {
            try {
                getUnmarshaller().setProperty(name, value);
            } catch (PropertyException p2) {
                p2.setStackTrace(Thread.currentThread().getStackTrace());
                throw p2;
            }
        }
    }

    private boolean excludeProperty(String name) {
        return name.equals("com.sun.xml.internal.bind.characterEscapeHandler") || name.equals("com.sun.xml.internal.bind.xmlDeclaration") || name.equals("com.sun.xml.internal.bind.xmlHeaders");
    }
}
