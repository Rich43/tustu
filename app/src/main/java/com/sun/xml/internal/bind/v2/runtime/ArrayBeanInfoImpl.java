package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.v2.model.runtime.RuntimeArrayInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/ArrayBeanInfoImpl.class */
final class ArrayBeanInfoImpl extends JaxBeanInfo {
    private final Class itemType;
    private final JaxBeanInfo itemBeanInfo;
    private Loader loader;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo] */
    public ArrayBeanInfoImpl(JAXBContextImpl jAXBContextImpl, RuntimeArrayInfo rai) {
        super(jAXBContextImpl, (RuntimeTypeInfo) rai, rai.getType2(), rai.getTypeName(), false, true, false);
        this.itemType = this.jaxbType.getComponentType();
        this.itemBeanInfo = jAXBContextImpl.getOrCreate((RuntimeTypeInfo) rai.getItemType2());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    protected void link(JAXBContextImpl grammar) {
        getLoader(grammar, false);
        super.link(grammar);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/ArrayBeanInfoImpl$ArrayLoader.class */
    private final class ArrayLoader extends Loader implements Receiver {
        private final Loader itemLoader;

        public ArrayLoader(JAXBContextImpl owner) {
            super(false);
            this.itemLoader = ArrayBeanInfoImpl.this.itemBeanInfo.getLoader(owner, true);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public void startElement(UnmarshallingContext.State state, TagName ea) {
            state.setTarget(new ArrayList());
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public void leaveElement(UnmarshallingContext.State state, TagName ea) {
            state.setTarget(ArrayBeanInfoImpl.this.toArray((List) state.getTarget()));
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
            if (ea.matches("", "item")) {
                state.setLoader(this.itemLoader);
                state.setReceiver(this);
            } else {
                super.childElement(state, ea);
            }
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public Collection<QName> getExpectedChildElements() {
            return Collections.singleton(new QName("", "item"));
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver
        public void receive(UnmarshallingContext.State state, Object o2) {
            ((List) state.getTarget()).add(o2);
        }
    }

    protected Object toArray(List list) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        int len = list.size();
        Object array = Array.newInstance((Class<?>) this.itemType, len);
        for (int i2 = 0; i2 < len; i2++) {
            Array.set(array, i2, list.get(i2));
        }
        return array;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(Object array, XMLSerializer target) throws XMLStreamException, SAXException, IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int len = Array.getLength(array);
        for (int i2 = 0; i2 < len; i2++) {
            Object item = Array.get(array, i2);
            target.startElement("", "item", null, null);
            if (item == null) {
                target.writeXsiNilTrue();
            } else {
                target.childAsXsiType(item, "arrayItem", this.itemBeanInfo, false);
            }
            target.endElement();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final String getElementNamespaceURI(Object array) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final String getElementLocalName(Object array) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final Object createInstance(UnmarshallingContext context) {
        return new ArrayList();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final boolean reset(Object array, UnmarshallingContext context) {
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final String getId(Object array, XMLSerializer target) {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final void serializeAttributes(Object array, XMLSerializer target) {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final void serializeRoot(Object array, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(array.getClass().getName()), null, null));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final void serializeURIs(Object array, XMLSerializer target) {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final Transducer getTransducer() {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        if (this.loader == null) {
            this.loader = new ArrayLoader(context);
        }
        return this.loader;
    }
}
