package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/PropertyImpl.class */
abstract class PropertyImpl<BeanT> implements Property<BeanT> {
    protected final String fieldName;
    private RuntimePropertyInfo propertyInfo;
    private boolean hiddenByOverride = false;

    public PropertyImpl(JAXBContextImpl context, RuntimePropertyInfo prop) {
        this.propertyInfo = null;
        this.fieldName = prop.getName();
        if (context.retainPropertyInfo) {
            this.propertyInfo = prop;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public RuntimePropertyInfo getInfo() {
        return this.propertyInfo;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void serializeBody(BeanT o2, XMLSerializer w2, Object outerPeer) throws AccessorException, SAXException, XMLStreamException, IOException {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void serializeURIs(BeanT o2, XMLSerializer w2) throws AccessorException, SAXException {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public boolean hasSerializeURIAction() {
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void wrapUp() {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public boolean isHiddenByOverride() {
        return this.hiddenByOverride;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void setHiddenByOverride(boolean hidden) {
        this.hiddenByOverride = hidden;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public String getFieldName() {
        return this.fieldName;
    }
}
