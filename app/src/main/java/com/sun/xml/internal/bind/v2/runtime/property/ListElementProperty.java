package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.ListTransducedAccessorImpl;
import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LeafPropertyLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ListElementProperty.class */
final class ListElementProperty<BeanT, ListT, ItemT> extends ArrayProperty<BeanT, ListT, ItemT> {
    private final Name tagName;
    private final String defaultValue;
    private final TransducedAccessor<BeanT> xacc;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ListElementProperty.class.desiredAssertionStatus();
    }

    public ListElementProperty(JAXBContextImpl grammar, RuntimeElementPropertyInfo prop) {
        super(grammar, prop);
        if (!$assertionsDisabled && !prop.isValueList()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && prop.getTypes().size() != 1) {
            throw new AssertionError();
        }
        RuntimeTypeRef ref = (RuntimeTypeRef) prop.getTypes().get(0);
        this.tagName = grammar.nameBuilder.createElementName(ref.getTagName());
        this.defaultValue = ref.getDefaultValue();
        Transducer xducer = ref.getTransducer();
        this.xacc = new ListTransducedAccessorImpl(xducer, this.acc, this.lister);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.ELEMENT;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
        Loader l2 = new LeafPropertyLoader(this.xacc);
        handlers.put(this.tagName, (Name) new ChildLoader(new DefaultValueLoaderDecorator(l2, this.defaultValue), null));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public void serializeBody(BeanT o2, XMLSerializer w2, Object outerPeer) throws AccessorException, SAXException, XMLStreamException, IOException {
        ListT list = this.acc.get(o2);
        if (list != null) {
            if (this.xacc.useNamespace()) {
                w2.startElement(this.tagName, null);
                this.xacc.declareNamespace(o2, w2);
                w2.endNamespaceDecls(list);
                w2.endAttributes();
                this.xacc.writeText(w2, o2, this.fieldName);
                w2.endElement();
                return;
            }
            this.xacc.writeLeafElement(w2, this.tagName, o2, this.fieldName);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        if (this.tagName != null && this.tagName.equals(nsUri, localName)) {
            return this.acc;
        }
        return null;
    }
}
