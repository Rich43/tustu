package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.v2.ClassFactory;
import com.sun.xml.internal.bind.v2.model.core.Element;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeReferencePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.property.ArrayERProperty;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.WildcardLoader;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Iterator;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ArrayReferenceNodeProperty.class */
class ArrayReferenceNodeProperty<BeanT, ListT, ItemT> extends ArrayERProperty<BeanT, ListT, ItemT> {
    private final QNameMap<JaxBeanInfo> expectedElements;
    private final boolean isMixed;
    private final DomHandler domHandler;
    private final WildcardMode wcMode;

    public ArrayReferenceNodeProperty(JAXBContextImpl p2, RuntimeReferencePropertyInfo prop) {
        super(p2, prop, prop.getXmlName(), prop.isCollectionNillable());
        this.expectedElements = new QNameMap<>();
        Iterator<? extends Element<Type, Class>> it = prop.getElements().iterator();
        while (it.hasNext()) {
            RuntimeElement e2 = (RuntimeElement) it.next();
            JaxBeanInfo bi2 = p2.getOrCreate(e2);
            this.expectedElements.put(e2.getElementName().getNamespaceURI(), e2.getElementName().getLocalPart(), bi2);
        }
        this.isMixed = prop.isMixed();
        if (prop.getWildcard() != null) {
            this.domHandler = (DomHandler) ClassFactory.create(prop.getDOMHandler());
            this.wcMode = prop.getWildcard();
        } else {
            this.domHandler = null;
            this.wcMode = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.property.ArrayERProperty
    protected final void serializeListBody(BeanT o2, XMLSerializer w2, ListT list) throws SAXException, XMLStreamException, IOException {
        ListIterator<ItemT> itr = this.lister.iterator(list, w2);
        while (itr.hasNext()) {
            try {
                Object next = itr.next();
                if (next != null) {
                    if (this.isMixed && next.getClass() == String.class) {
                        w2.text((String) next, (String) null);
                    } else {
                        JaxBeanInfo beanInfo = w2.grammar.getBeanInfo(next, true);
                        if (beanInfo.jaxbType == Object.class && this.domHandler != null) {
                            w2.writeDom(next, this.domHandler, o2, this.fieldName);
                        } else {
                            beanInfo.serializeRoot(next, w2);
                        }
                    }
                }
            } catch (JAXBException e2) {
                w2.reportError(this.fieldName, e2);
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.ArrayERProperty
    public void createBodyUnmarshaller(UnmarshallerChain chain, QNameMap<ChildLoader> loaders) {
        int offset = chain.allocateOffset();
        Receiver recv = new ArrayERProperty.ReceiverImpl(offset);
        for (QNameMap.Entry<JaxBeanInfo> n2 : this.expectedElements.entrySet()) {
            JaxBeanInfo beanInfo = n2.getValue();
            loaders.put(n2.nsUri, n2.localName, new ChildLoader(beanInfo.getLoader(chain.context, true), recv));
        }
        if (this.isMixed) {
            loaders.put(TEXT_HANDLER, (QName) new ChildLoader(new MixedTextLoader(recv), null));
        }
        if (this.domHandler != null) {
            loaders.put(CATCH_ALL, (QName) new ChildLoader(new WildcardLoader(this.domHandler, this.wcMode), recv));
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ArrayReferenceNodeProperty$MixedTextLoader.class */
    private static final class MixedTextLoader extends Loader {
        private final Receiver recv;

        public MixedTextLoader(Receiver recv) {
            super(true);
            this.recv = recv;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
            if (text.length() != 0) {
                this.recv.receive(state, text.toString());
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.REFERENCE;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        if (this.wrapperTagName != null) {
            if (this.wrapperTagName.equals(nsUri, localName)) {
                return this.acc;
            }
            return null;
        }
        if (this.expectedElements.containsKey(nsUri, localName)) {
            return this.acc;
        }
        return null;
    }
}
