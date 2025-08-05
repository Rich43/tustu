package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ArrayElementLeafProperty.class */
final class ArrayElementLeafProperty<BeanT, ListT, ItemT> extends ArrayElementProperty<BeanT, ListT, ItemT> {
    private final Transducer<ItemT> xducer;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ArrayElementLeafProperty.class.desiredAssertionStatus();
    }

    public ArrayElementLeafProperty(JAXBContextImpl p2, RuntimeElementPropertyInfo prop) {
        super(p2, prop);
        if (!$assertionsDisabled && prop.getTypes().size() != 1) {
            throw new AssertionError();
        }
        this.xducer = ((RuntimeTypeRef) prop.getTypes().get(0)).getTransducer();
        if (!$assertionsDisabled && this.xducer == null) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.ArrayElementProperty
    public void serializeItem(JaxBeanInfo bi2, ItemT item, XMLSerializer w2) throws AccessorException, XMLStreamException, SAXException, IOException {
        this.xducer.declareNamespace(item, w2);
        w2.endNamespaceDecls(item);
        w2.endAttributes();
        this.xducer.writeText(w2, item, this.fieldName);
    }
}
