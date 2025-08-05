package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.ClassFactory;
import com.sun.xml.internal.bind.v2.model.core.Element;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeReferencePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.ElementBeanInfoImpl;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.WildcardLoader;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Iterator;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/SingleReferenceNodeProperty.class */
final class SingleReferenceNodeProperty<BeanT, ValueT> extends PropertyImpl<BeanT> {
    private final Accessor<BeanT, ValueT> acc;
    private final QNameMap<JaxBeanInfo> expectedElements;
    private final DomHandler domHandler;
    private final WildcardMode wcMode;

    public SingleReferenceNodeProperty(JAXBContextImpl context, RuntimeReferencePropertyInfo prop) {
        super(context, prop);
        this.expectedElements = new QNameMap<>();
        this.acc = prop.getAccessor().optimize(context);
        Iterator<? extends Element<Type, Class>> it = prop.getElements().iterator();
        while (it.hasNext()) {
            RuntimeElement e2 = (RuntimeElement) it.next();
            this.expectedElements.put(e2.getElementName(), (QName) context.getOrCreate(e2));
        }
        if (prop.getWildcard() != null) {
            this.domHandler = (DomHandler) ClassFactory.create(prop.getDOMHandler());
            this.wcMode = prop.getWildcard();
        } else {
            this.domHandler = null;
            this.wcMode = null;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void reset(BeanT bean) throws AccessorException {
        this.acc.set(bean, null);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public String getIdValue(BeanT beanT) {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public void serializeBody(BeanT o2, XMLSerializer w2, Object outerPeer) throws AccessorException, SAXException, XMLStreamException, IOException {
        ValueT v2 = this.acc.get(o2);
        if (v2 != null) {
            try {
                JaxBeanInfo bi2 = w2.grammar.getBeanInfo((Object) v2, true);
                if (bi2.jaxbType == Object.class && this.domHandler != null) {
                    w2.writeDom(v2, this.domHandler, o2, this.fieldName);
                } else {
                    bi2.serializeRoot(v2, w2);
                }
            } catch (JAXBException e2) {
                w2.reportError(this.fieldName, e2);
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
        for (QNameMap.Entry<JaxBeanInfo> n2 : this.expectedElements.entrySet()) {
            handlers.put(n2.nsUri, n2.localName, new ChildLoader(n2.getValue().getLoader(chain.context, true), this.acc));
        }
        if (this.domHandler != null) {
            handlers.put(CATCH_ALL, (QName) new ChildLoader(new WildcardLoader(this.domHandler, this.wcMode), this.acc));
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.REFERENCE;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        JaxBeanInfo bi2 = this.expectedElements.get(nsUri, localName);
        if (bi2 != null) {
            if (bi2 instanceof ElementBeanInfoImpl) {
                final ElementBeanInfoImpl ebi = (ElementBeanInfoImpl) bi2;
                return new Accessor<BeanT, Object>(ebi.expectedType) { // from class: com.sun.xml.internal.bind.v2.runtime.property.SingleReferenceNodeProperty.1
                    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
                    public Object get(BeanT bean) throws AccessorException {
                        Object obj = SingleReferenceNodeProperty.this.acc.get(bean);
                        if (obj instanceof JAXBElement) {
                            return ((JAXBElement) obj).getValue();
                        }
                        return obj;
                    }

                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
                    public void set(BeanT bean, Object value) throws AccessorException {
                        if (value != null) {
                            try {
                                value = ebi.createInstanceFromValue(value);
                            } catch (IllegalAccessException e2) {
                                throw new AccessorException(e2);
                            } catch (InstantiationException e3) {
                                throw new AccessorException(e3);
                            } catch (InvocationTargetException e4) {
                                throw new AccessorException(e4);
                            }
                        }
                        SingleReferenceNodeProperty.this.acc.set(bean, value);
                    }
                };
            }
            return this.acc;
        }
        return null;
    }
}
