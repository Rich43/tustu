package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TextLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiTypeLoader;
import java.io.IOException;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/LeafBeanInfoImpl.class */
final class LeafBeanInfoImpl<BeanT> extends JaxBeanInfo<BeanT> {
    private final Loader loader;
    private final Loader loaderWithSubst;
    private final Transducer<BeanT> xducer;
    private final Name tagName;

    public LeafBeanInfoImpl(JAXBContextImpl grammar, RuntimeLeafInfo li) {
        super(grammar, (RuntimeTypeInfo) li, li.getClazz(), li.getTypeNames(), li.isElement(), true, false);
        this.xducer = li.getTransducer();
        this.loader = new TextLoader(this.xducer);
        this.loaderWithSubst = new XsiTypeLoader(this);
        if (isElement()) {
            this.tagName = grammar.nameBuilder.createElementName(li.getElementName());
        } else {
            this.tagName = null;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public QName getTypeName(BeanT instance) {
        QName tn = this.xducer.getTypeName(instance);
        return tn != null ? tn : super.getTypeName(instance);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final String getElementNamespaceURI(BeanT t2) {
        return this.tagName.nsUri;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final String getElementLocalName(BeanT t2) {
        return this.tagName.localName;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public BeanT createInstance(UnmarshallingContext context) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final boolean reset(BeanT bean, UnmarshallingContext context) {
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final String getId(BeanT bean, XMLSerializer target) {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final void serializeBody(BeanT bean, XMLSerializer w2) throws SAXException, XMLStreamException, IOException {
        try {
            this.xducer.writeText(w2, bean, null);
        } catch (AccessorException e2) {
            w2.reportError(null, e2);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final void serializeAttributes(BeanT bean, XMLSerializer target) {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final void serializeRoot(BeanT bean, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        if (this.tagName == null) {
            target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(bean.getClass().getName()), null, null));
            return;
        }
        target.startElement(this.tagName, bean);
        target.childAsSoleContent(bean, null);
        target.endElement();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final void serializeURIs(BeanT bean, XMLSerializer target) throws SAXException {
        if (this.xducer.useNamespace()) {
            try {
                this.xducer.declareNamespace(bean, target);
            } catch (AccessorException e2) {
                target.reportError(null, e2);
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        if (typeSubstitutionCapable) {
            return this.loaderWithSubst;
        }
        return this.loader;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public Transducer<BeanT> getTransducer() {
        return this.xducer;
    }
}
