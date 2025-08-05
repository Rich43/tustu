package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ArrayElementNodeProperty.class */
final class ArrayElementNodeProperty<BeanT, ListT, ItemT> extends ArrayElementProperty<BeanT, ListT, ItemT> {
    public ArrayElementNodeProperty(JAXBContextImpl p2, RuntimeElementPropertyInfo prop) {
        super(p2, prop);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.ArrayElementProperty
    public void serializeItem(JaxBeanInfo expected, ItemT item, XMLSerializer w2) throws XMLStreamException, SAXException, IOException {
        if (item == null) {
            w2.writeXsiNilTrue();
        } else {
            w2.childAsXsiType(item, this.fieldName, expected, false);
        }
    }
}
