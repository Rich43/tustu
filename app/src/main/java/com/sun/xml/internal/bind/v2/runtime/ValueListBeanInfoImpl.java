package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.FinalArrayList;
import com.sun.xml.internal.bind.WhiteSpaceProcessor;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/ValueListBeanInfoImpl.class */
final class ValueListBeanInfoImpl extends JaxBeanInfo {
    private final Class itemType;
    private final Transducer xducer;
    private final Loader loader;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ValueListBeanInfoImpl.class.desiredAssertionStatus();
    }

    public ValueListBeanInfoImpl(JAXBContextImpl owner, Class arrayType) throws JAXBException {
        super(owner, null, arrayType, false, true, false);
        this.loader = new Loader(true) { // from class: com.sun.xml.internal.bind.v2.runtime.ValueListBeanInfoImpl.1
            @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
            public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
                List<Object> r2 = new FinalArrayList<>();
                int idx = 0;
                int len = text.length();
                while (true) {
                    int p2 = idx;
                    while (p2 < len && !WhiteSpaceProcessor.isWhiteSpace(text.charAt(p2))) {
                        p2++;
                    }
                    CharSequence token = text.subSequence(idx, p2);
                    if (!token.equals("")) {
                        try {
                            r2.add(ValueListBeanInfoImpl.this.xducer.parse(token));
                        } catch (AccessorException e2) {
                            handleGenericException(e2, true);
                        }
                    }
                    if (p2 == len) {
                        break;
                    }
                    while (p2 < len && WhiteSpaceProcessor.isWhiteSpace(text.charAt(p2))) {
                        p2++;
                    }
                    if (p2 == len) {
                        break;
                    } else {
                        idx = p2;
                    }
                }
                state.setTarget(ValueListBeanInfoImpl.this.toArray(r2));
            }
        };
        this.itemType = this.jaxbType.getComponentType();
        this.xducer = owner.getBeanInfo((Class) arrayType.getComponentType(), true).getTransducer();
        if (!$assertionsDisabled && this.xducer == null) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object toArray(List list) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        int len = list.size();
        Object array = Array.newInstance((Class<?>) this.itemType, len);
        for (int i2 = 0; i2 < len; i2++) {
            Array.set(array, i2, list.get(i2));
        }
        return array;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(Object array, XMLSerializer target) throws SAXException, XMLStreamException, IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int len = Array.getLength(array);
        for (int i2 = 0; i2 < len; i2++) {
            Object item = Array.get(array, i2);
            try {
                this.xducer.writeText(target, item, "arrayItem");
            } catch (AccessorException e2) {
                target.reportError("arrayItem", e2);
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final void serializeURIs(Object array, XMLSerializer target) throws SAXException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.xducer.useNamespace()) {
            int len = Array.getLength(array);
            for (int i2 = 0; i2 < len; i2++) {
                Object item = Array.get(array, i2);
                try {
                    this.xducer.declareNamespace(item, target);
                } catch (AccessorException e2) {
                    target.reportError("arrayItem", e2);
                }
            }
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
        throw new UnsupportedOperationException();
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
    public final void serializeRoot(Object array, XMLSerializer target) throws SAXException {
        target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(array.getClass().getName()), null, null));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final Transducer getTransducer() {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        return this.loader;
    }
}
