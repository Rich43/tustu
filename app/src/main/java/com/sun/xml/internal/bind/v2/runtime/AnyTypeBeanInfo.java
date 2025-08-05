package com.sun.xml.internal.bind.v2.runtime;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.DomLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiTypeLoader;
import java.io.IOException;
import javax.xml.bind.annotation.W3CDomHandler;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/AnyTypeBeanInfo.class */
final class AnyTypeBeanInfo extends JaxBeanInfo<Object> implements AttributeAccessor {
    private boolean nilIncluded;
    private static final W3CDomHandler domHandler = new W3CDomHandler();
    private static final DomLoader domLoader = new DomLoader(domHandler);
    private final XsiTypeLoader substLoader;

    public AnyTypeBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo anyTypeInfo) {
        super(grammar, anyTypeInfo, Object.class, new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_ANYTYPE), false, true, false);
        this.nilIncluded = false;
        this.substLoader = new XsiTypeLoader(this);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getElementNamespaceURI(Object element) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getElementLocalName(Object element) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public Object createInstance(UnmarshallingContext context) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public boolean reset(Object element, UnmarshallingContext context) {
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getId(Object element, XMLSerializer target) {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(Object element, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        NodeList childNodes = ((Element) element).getChildNodes();
        int len = childNodes.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            Node child = childNodes.item(i2);
            switch (child.getNodeType()) {
                case 1:
                    target.writeDom((Element) child, domHandler, null, null);
                    break;
                case 3:
                case 4:
                    target.text(child.getNodeValue(), (String) null);
                    break;
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeAttributes(Object element, XMLSerializer target) throws SAXException {
        NamedNodeMap al2 = ((Element) element).getAttributes();
        int len = al2.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            Attr a2 = (Attr) al2.item(i2);
            String uri = a2.getNamespaceURI();
            if (uri == null) {
                uri = "";
            }
            String local = a2.getLocalName();
            String name = a2.getName();
            if (local == null) {
                local = name;
            }
            if (uri.equals("http://www.w3.org/2001/XMLSchema-instance") && "nil".equals(local)) {
                this.isNilIncluded = true;
            }
            if (!name.startsWith("xmlns")) {
                target.attribute(uri, local, a2.getValue());
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeRoot(Object element, XMLSerializer target) throws SAXException {
        target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(element.getClass().getName()), null, null));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeURIs(Object element, XMLSerializer target) {
        NamedNodeMap al2 = ((Element) element).getAttributes();
        int len = al2.getLength();
        NamespaceContext2 context = target.getNamespaceContext();
        for (int i2 = 0; i2 < len; i2++) {
            Attr a2 = (Attr) al2.item(i2);
            if ("xmlns".equals(a2.getPrefix())) {
                context.force(a2.getValue(), a2.getLocalName());
            } else if ("xmlns".equals(a2.getName())) {
                if (element instanceof Element) {
                    context.declareNamespace(a2.getValue(), null, false);
                } else {
                    context.force(a2.getValue(), "");
                }
            } else {
                String nsUri = a2.getNamespaceURI();
                if (nsUri != null && nsUri.length() > 0) {
                    context.declareNamespace(nsUri, a2.getPrefix(), true);
                }
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public Transducer<Object> getTransducer() {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        if (typeSubstitutionCapable) {
            return this.substLoader;
        }
        return domLoader;
    }
}
