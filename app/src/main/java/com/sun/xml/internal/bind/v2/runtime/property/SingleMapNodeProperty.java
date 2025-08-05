package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.ClassFactory;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeMapPropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/SingleMapNodeProperty.class */
final class SingleMapNodeProperty<BeanT, ValueT extends Map> extends PropertyImpl<BeanT> {
    private final Accessor<BeanT, ValueT> acc;
    private final Name tagName;
    private final Name entryTag;
    private final Name keyTag;
    private final Name valueTag;
    private final boolean nillable;
    private JaxBeanInfo keyBeanInfo;
    private JaxBeanInfo valueBeanInfo;
    private final Class<? extends ValueT> mapImplClass;
    private Loader keyLoader;
    private Loader valueLoader;
    private final Loader itemsLoader;
    private final Loader entryLoader;
    private static final Class[] knownImplClasses = {HashMap.class, TreeMap.class, LinkedHashMap.class};
    private static final Receiver keyReceiver = new ReceiverImpl(0);
    private static final Receiver valueReceiver = new ReceiverImpl(1);

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v10, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo] */
    /* JADX WARN: Type inference failed for: r2v12, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo] */
    public SingleMapNodeProperty(JAXBContextImpl jAXBContextImpl, RuntimeMapPropertyInfo prop) {
        super(jAXBContextImpl, prop);
        this.itemsLoader = new Loader(false) { // from class: com.sun.xml.internal.bind.v2.runtime.property.SingleMapNodeProperty.1
            private ThreadLocal<BeanT> target = new ThreadLocal<>();
            private ThreadLocal<ValueT> map = new ThreadLocal<>();
            private int depthCounter = 0;

            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
            public void startElement(UnmarshallingContext.State state, TagName tagName) throws SAXException {
                try {
                    this.target.set(state.getPrev().getTarget());
                    this.map.set(SingleMapNodeProperty.this.acc.get(this.target.get()));
                    this.depthCounter++;
                    if (this.map.get() == null) {
                        this.map.set(ClassFactory.create(SingleMapNodeProperty.this.mapImplClass));
                    }
                    this.map.get().clear();
                    state.setTarget(this.map.get());
                } catch (AccessorException e2) {
                    handleGenericException(e2, true);
                    state.setTarget(new HashMap());
                }
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
            public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
                super.leaveElement(state, ea);
                try {
                    SingleMapNodeProperty.this.acc.set(this.target.get(), this.map.get());
                    int i2 = this.depthCounter - 1;
                    this.depthCounter = i2;
                    if (i2 == 0) {
                        this.target.remove();
                        this.map.remove();
                    }
                } catch (AccessorException ex) {
                    handleGenericException(ex, true);
                }
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
            public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
                if (ea.matches(SingleMapNodeProperty.this.entryTag)) {
                    state.setLoader(SingleMapNodeProperty.this.entryLoader);
                } else {
                    super.childElement(state, ea);
                }
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
            public Collection<QName> getExpectedChildElements() {
                return Collections.singleton(SingleMapNodeProperty.this.entryTag.toQName());
            }
        };
        this.entryLoader = new Loader(false) { // from class: com.sun.xml.internal.bind.v2.runtime.property.SingleMapNodeProperty.2
            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
            public void startElement(UnmarshallingContext.State state, TagName ea) {
                state.setTarget(new Object[2]);
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
            public void leaveElement(UnmarshallingContext.State state, TagName ea) {
                Object[] keyValue = (Object[]) state.getTarget();
                Map map = (Map) state.getPrev().getTarget();
                map.put(keyValue[0], keyValue[1]);
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
            public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
                if (ea.matches(SingleMapNodeProperty.this.keyTag)) {
                    state.setLoader(SingleMapNodeProperty.this.keyLoader);
                    state.setReceiver(SingleMapNodeProperty.keyReceiver);
                } else if (ea.matches(SingleMapNodeProperty.this.valueTag)) {
                    state.setLoader(SingleMapNodeProperty.this.valueLoader);
                    state.setReceiver(SingleMapNodeProperty.valueReceiver);
                } else {
                    super.childElement(state, ea);
                }
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
            public Collection<QName> getExpectedChildElements() {
                return Arrays.asList(SingleMapNodeProperty.this.keyTag.toQName(), SingleMapNodeProperty.this.valueTag.toQName());
            }
        };
        this.acc = prop.getAccessor().optimize(jAXBContextImpl);
        this.tagName = jAXBContextImpl.nameBuilder.createElementName(prop.getXmlName());
        this.entryTag = jAXBContextImpl.nameBuilder.createElementName("", "entry");
        this.keyTag = jAXBContextImpl.nameBuilder.createElementName("", "key");
        this.valueTag = jAXBContextImpl.nameBuilder.createElementName("", "value");
        this.nillable = prop.isCollectionNillable();
        this.keyBeanInfo = jAXBContextImpl.getOrCreate((RuntimeTypeInfo) prop.getKeyType2());
        this.valueBeanInfo = jAXBContextImpl.getOrCreate((RuntimeTypeInfo) prop.getValueType2());
        Class<ValueT> sig = (Class) Utils.REFLECTION_NAVIGATOR.erasure(prop.getRawType());
        this.mapImplClass = ClassFactory.inferImplClass(sig, knownImplClasses);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void reset(BeanT bean) throws AccessorException {
        this.acc.set(bean, null);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public String getIdValue(BeanT bean) {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.MAP;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
        this.keyLoader = this.keyBeanInfo.getLoader(chain.context, true);
        this.valueLoader = this.valueBeanInfo.getLoader(chain.context, true);
        handlers.put(this.tagName, (Name) new ChildLoader(this.itemsLoader, null));
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/SingleMapNodeProperty$ReceiverImpl.class */
    private static final class ReceiverImpl implements Receiver {
        private final int index;

        public ReceiverImpl(int index) {
            this.index = index;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver
        public void receive(UnmarshallingContext.State state, Object o2) {
            ((Object[]) state.getTarget())[this.index] = o2;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public void serializeBody(BeanT o2, XMLSerializer w2, Object outerPeer) throws AccessorException, XMLStreamException, SAXException, IOException {
        ValueT v2 = this.acc.get(o2);
        if (v2 != null) {
            bareStartTag(w2, this.tagName, v2);
            for (Map.Entry e2 : v2.entrySet()) {
                bareStartTag(w2, this.entryTag, null);
                Object key = e2.getKey();
                if (key != null) {
                    w2.startElement(this.keyTag, key);
                    w2.childAsXsiType(key, this.fieldName, this.keyBeanInfo, false);
                    w2.endElement();
                }
                Object value = e2.getValue();
                if (value != null) {
                    w2.startElement(this.valueTag, value);
                    w2.childAsXsiType(value, this.fieldName, this.valueBeanInfo, false);
                    w2.endElement();
                }
                w2.endElement();
            }
            w2.endElement();
            return;
        }
        if (this.nillable) {
            w2.startElement(this.tagName, null);
            w2.writeXsiNilTrue();
            w2.endElement();
        }
    }

    private void bareStartTag(XMLSerializer w2, Name tagName, Object peer) throws XMLStreamException, SAXException, IOException {
        w2.startElement(tagName, peer);
        w2.endNamespaceDecls(peer);
        w2.endAttributes();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        if (this.tagName.equals(nsUri, localName)) {
            return this.acc;
        }
        return null;
    }
}
