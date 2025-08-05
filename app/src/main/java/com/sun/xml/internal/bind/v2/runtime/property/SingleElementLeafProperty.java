package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.DefaultValueLoaderDecorator;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LeafPropertyLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LeafPropertyXsiLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.io.IOException;
import java.lang.reflect.Modifier;
import javax.xml.bind.JAXBElement;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/SingleElementLeafProperty.class */
final class SingleElementLeafProperty<BeanT> extends PropertyImpl<BeanT> {
    private final Name tagName;
    private final boolean nillable;
    private final Accessor acc;
    private final String defaultValue;
    private final TransducedAccessor<BeanT> xacc;
    private final boolean improvedXsiTypeHandling;
    private final boolean idRef;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SingleElementLeafProperty.class.desiredAssertionStatus();
    }

    public SingleElementLeafProperty(JAXBContextImpl context, RuntimeElementPropertyInfo prop) {
        super(context, prop);
        RuntimeTypeRef ref = (RuntimeTypeRef) prop.getTypes().get(0);
        this.tagName = context.nameBuilder.createElementName(ref.getTagName());
        if (!$assertionsDisabled && this.tagName == null) {
            throw new AssertionError();
        }
        this.nillable = ref.isNillable();
        this.defaultValue = ref.getDefaultValue();
        this.acc = prop.getAccessor().optimize(context);
        this.xacc = TransducedAccessor.get(context, ref);
        if (!$assertionsDisabled && this.xacc == null) {
            throw new AssertionError();
        }
        this.improvedXsiTypeHandling = context.improvedXsiTypeHandling;
        this.idRef = ref.getSource2().id() == ID.IDREF;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public void reset(BeanT o2) throws AccessorException {
        this.acc.set(o2, null);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public String getIdValue(BeanT bean) throws AccessorException, SAXException {
        return this.xacc.print(bean).toString();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public void serializeBody(BeanT o2, XMLSerializer w2, Object outerPeer) throws AccessorException, SAXException, XMLStreamException, IOException {
        boolean hasValue = this.xacc.hasValue(o2);
        Object obj = null;
        try {
            obj = this.acc.getUnadapted(o2);
        } catch (AccessorException e2) {
        }
        Class valueType = this.acc.getValueType();
        if (xsiTypeNeeded(o2, w2, obj, valueType)) {
            w2.startElement(this.tagName, outerPeer);
            w2.childAsXsiType(obj, this.fieldName, w2.grammar.getBeanInfo(valueType), false);
            w2.endElement();
        } else if (hasValue) {
            this.xacc.writeLeafElement(w2, this.tagName, o2, this.fieldName);
        } else if (this.nillable) {
            w2.startElement(this.tagName, null);
            w2.writeXsiNilTrue();
            w2.endElement();
        }
    }

    private boolean xsiTypeNeeded(BeanT bean, XMLSerializer w2, Object value, Class valueTypeClass) {
        if (!this.improvedXsiTypeHandling || this.acc.isAdapted() || value == null || value.getClass().equals(valueTypeClass) || this.idRef || valueTypeClass.isPrimitive()) {
            return false;
        }
        return this.acc.isValueTypeAbstractable() || isNillableAbstract(bean, w2.grammar, value, valueTypeClass);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isNillableAbstract(BeanT beant, JAXBContextImpl context, Object value, Class valueTypeClass) {
        if (!this.nillable || valueTypeClass != Object.class || beant.getClass() != JAXBElement.class) {
            return false;
        }
        JAXBElement jaxbElement = (JAXBElement) beant;
        Class valueClass = value.getClass();
        Class declaredTypeClass = jaxbElement.getDeclaredType();
        if (declaredTypeClass.equals(valueClass) || !declaredTypeClass.isAssignableFrom(valueClass) || !Modifier.isAbstract(declaredTypeClass.getModifiers())) {
            return false;
        }
        return this.acc.isAbstractable(declaredTypeClass);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder
    public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
        Loader l2 = new LeafPropertyLoader(this.xacc);
        if (this.defaultValue != null) {
            l2 = new DefaultValueLoaderDecorator(l2, this.defaultValue);
        }
        if (this.nillable || chain.context.allNillable) {
            l2 = new XsiNilLoader.Single(l2, this.acc);
        }
        if (this.improvedXsiTypeHandling) {
            l2 = new LeafPropertyXsiLoader(l2, this.xacc, this.acc);
        }
        handlers.put(this.tagName, (Name) new ChildLoader(l2, null));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
    public PropertyKind getKind() {
        return PropertyKind.ELEMENT;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public Accessor getElementPropertyAccessor(String nsUri, String localName) {
        if (this.tagName.equals(nsUri, localName)) {
            return this.acc;
        }
        return null;
    }
}
