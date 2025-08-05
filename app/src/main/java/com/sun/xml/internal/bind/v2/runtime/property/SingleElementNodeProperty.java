package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/SingleElementNodeProperty.class */
final class SingleElementNodeProperty<BeanT, ValueT> extends PropertyImpl<BeanT> {
    private final Accessor<BeanT, ValueT> acc;
    private final boolean nillable;
    private final QName[] acceptedElements;
    private final Map<Class, TagAndType> typeNames;
    private RuntimeElementPropertyInfo prop;
    private final Name nullTagName;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v18, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo] */
    public SingleElementNodeProperty(JAXBContextImpl jAXBContextImpl, RuntimeElementPropertyInfo prop) {
        super(jAXBContextImpl, prop);
        this.typeNames = new HashMap();
        this.acc = prop.getAccessor().optimize(jAXBContextImpl);
        this.prop = prop;
        QName nt = null;
        boolean nil = false;
        this.acceptedElements = new QName[prop.getTypes().size()];
        for (int i2 = 0; i2 < this.acceptedElements.length; i2++) {
            this.acceptedElements[i2] = ((RuntimeTypeRef) prop.getTypes().get(i2)).getTagName();
        }
        Iterator<? extends TypeRef<Type, Class>> it = prop.getTypes().iterator();
        while (it.hasNext()) {
            RuntimeTypeRef e2 = (RuntimeTypeRef) it.next();
            JaxBeanInfo beanInfo = jAXBContextImpl.getOrCreate((RuntimeTypeInfo) e2.getTarget2());
            if (nt == null) {
                nt = e2.getTagName();
            }
            this.typeNames.put(beanInfo.jaxbType, new TagAndType(jAXBContextImpl.nameBuilder.createElementName(e2.getTagName()), beanInfo));
            nil |= e2.isNillable();
        }
        this.nullTagName = jAXBContextImpl.nameBuilder.createElementName(nt);
        this.nillable = nil;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public void wrapUp() {
        super.wrapUp();
        this.prop = null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void reset(BeanT bean) throws AccessorException {
        this.acc.set(bean, null);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public String getIdValue(BeanT beanT) {
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public void serializeBody(BeanT beant, XMLSerializer w2, Object outerPeer) throws AccessorException, SAXException, XMLStreamException, IOException {
        ValueT v2 = this.acc.get(beant);
        if (v2 == null) {
            if (this.nillable) {
                w2.startElement(this.nullTagName, null);
                w2.writeXsiNilTrue();
                w2.endElement();
                return;
            }
            return;
        }
        Class vtype = v2.getClass();
        TagAndType tt = this.typeNames.get(vtype);
        if (tt == null) {
            Iterator<Map.Entry<Class, TagAndType>> it = this.typeNames.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<Class, TagAndType> e2 = it.next();
                if (e2.getKey().isAssignableFrom(vtype)) {
                    tt = e2.getValue();
                    break;
                }
            }
        }
        boolean addNilDecl = (beant instanceof JAXBElement) && ((JAXBElement) beant).isNil();
        if (tt == null) {
            w2.startElement(this.typeNames.values().iterator().next().tagName, null);
            w2.childAsXsiType(v2, this.fieldName, w2.grammar.getBeanInfo(Object.class), addNilDecl && this.nillable);
        } else {
            w2.startElement(tt.tagName, null);
            w2.childAsXsiType(v2, this.fieldName, tt.beanInfo, addNilDecl && this.nillable);
        }
        w2.endElement();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
        JAXBContextImpl context = chain.context;
        for (TypeRef<Type, Class> e2 : this.prop.getTypes()) {
            JaxBeanInfo bi2 = context.getOrCreate((RuntimeTypeInfo) e2.getTarget2());
            Loader l2 = bi2.getLoader(context, !Modifier.isFinal(bi2.jaxbType.getModifiers()));
            if (e2.getDefaultValue() != null) {
                l2 = new DefaultValueLoaderDecorator(l2, e2.getDefaultValue());
            }
            if (this.nillable || chain.context.allNillable) {
                l2 = new XsiNilLoader.Single(l2, this.acc);
            }
            handlers.put(e2.getTagName(), (QName) new ChildLoader(l2, this.acc));
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.ELEMENT;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        for (QName n2 : this.acceptedElements) {
            if (n2.getNamespaceURI().equals(nsUri) && n2.getLocalPart().equals(localName)) {
                return this.acc;
            }
        }
        return null;
    }
}
