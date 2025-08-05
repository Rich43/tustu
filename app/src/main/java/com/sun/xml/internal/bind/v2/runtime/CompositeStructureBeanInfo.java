package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.api.CompositeStructure;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/CompositeStructureBeanInfo.class */
public class CompositeStructureBeanInfo extends JaxBeanInfo<CompositeStructure> {
    public CompositeStructureBeanInfo(JAXBContextImpl context) {
        super(context, null, CompositeStructure.class, false, true, false);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getElementNamespaceURI(CompositeStructure o2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getElementLocalName(CompositeStructure o2) {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public CompositeStructure createInstance(UnmarshallingContext context) throws IllegalAccessException, SAXException, InstantiationException, InvocationTargetException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public boolean reset(CompositeStructure o2, UnmarshallingContext context) throws SAXException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getId(CompositeStructure o2, XMLSerializer target) throws SAXException {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeRoot(CompositeStructure o2, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(o2.getClass().getName()), null, null));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeURIs(CompositeStructure o2, XMLSerializer target) throws SAXException {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeAttributes(CompositeStructure o2, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(CompositeStructure o2, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        int len = o2.bridges.length;
        for (int i2 = 0; i2 < len; i2++) {
            Object value = o2.values[i2];
            InternalBridge bi2 = (InternalBridge) o2.bridges[i2];
            bi2.marshal((InternalBridge) value, target);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public Transducer<CompositeStructure> getTransducer() {
        return null;
    }
}
