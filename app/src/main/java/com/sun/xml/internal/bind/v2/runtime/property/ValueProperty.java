package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeValuePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ValuePropertyLoader;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ValueProperty.class */
public final class ValueProperty<BeanT> extends PropertyImpl<BeanT> {
    private final TransducedAccessor<BeanT> xacc;
    private final Accessor<BeanT, ?> acc;

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ String getFieldName() {
        return super.getFieldName();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ void setHiddenByOverride(boolean z2) {
        super.setHiddenByOverride(z2);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ boolean isHiddenByOverride() {
        return super.isHiddenByOverride();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ void wrapUp() {
        super.wrapUp();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ Accessor getElementPropertyAccessor(String str, String str2) {
        return super.getElementPropertyAccessor(str, str2);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public /* bridge */ /* synthetic */ RuntimePropertyInfo getInfo() {
        return super.getInfo();
    }

    public ValueProperty(JAXBContextImpl context, RuntimeValuePropertyInfo prop) {
        super(context, prop);
        this.xacc = TransducedAccessor.get(context, prop);
        this.acc = prop.getAccessor();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public final void serializeBody(BeanT o2, XMLSerializer w2, Object outerPeer) throws AccessorException, SAXException, XMLStreamException, IOException {
        if (this.xacc.hasValue(o2)) {
            this.xacc.writeText(w2, o2, this.fieldName);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public void serializeURIs(BeanT o2, XMLSerializer w2) throws AccessorException, SAXException {
        this.xacc.declareNamespace(o2, w2);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public boolean hasSerializeURIAction() {
        return this.xacc.useNamespace();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chainElem, QNameMap<ChildLoader> handlers) {
        handlers.put(StructureLoaderBuilder.TEXT_HANDLER, (QName) new ChildLoader(new ValuePropertyLoader(this.xacc), null));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.VALUE;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void reset(BeanT o2) throws AccessorException {
        this.acc.set(o2, null);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public String getIdValue(BeanT bean) throws AccessorException, SAXException {
        return this.xacc.print(bean).toString();
    }
}
