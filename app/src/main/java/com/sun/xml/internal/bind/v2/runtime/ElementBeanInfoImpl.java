package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.property.Property;
import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
import com.sun.xml.internal.bind.v2.runtime.property.UnmarshallerChain;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Discarder;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Intercepter;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/ElementBeanInfoImpl.class */
public final class ElementBeanInfoImpl extends JaxBeanInfo<JAXBElement> {
    private Loader loader;
    private final Property property;
    private final QName tagName;
    public final Class expectedType;
    private final Class scope;
    private final Constructor<? extends JAXBElement> constructor;

    /* JADX WARN: Type inference failed for: r2v2, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo, com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo] */
    ElementBeanInfoImpl(JAXBContextImpl grammar, RuntimeElementInfo rei) {
        super(grammar, rei, rei.getType2(), true, false, true);
        this.property = PropertyFactory.create(grammar, rei.getProperty2());
        this.tagName = rei.getElementName();
        this.expectedType = (Class) Utils.REFLECTION_NAVIGATOR.erasure(rei.getContentInMemoryType());
        this.scope = rei.getScope2() == null ? JAXBElement.GlobalScope.class : rei.getScope2().getClazz();
        Class type = (Class) Utils.REFLECTION_NAVIGATOR.erasure(rei.getType2());
        if (type == JAXBElement.class) {
            this.constructor = null;
            return;
        }
        try {
            this.constructor = type.getConstructor(this.expectedType);
        } catch (NoSuchMethodException e2) {
            NoSuchMethodError x2 = new NoSuchMethodError("Failed to find the constructor for " + ((Object) type) + " with " + ((Object) this.expectedType));
            x2.initCause(e2);
            throw x2;
        }
    }

    protected ElementBeanInfoImpl(final JAXBContextImpl grammar) {
        super(grammar, null, JAXBElement.class, true, false, true);
        this.tagName = null;
        this.expectedType = null;
        this.scope = null;
        this.constructor = null;
        this.property = new Property<JAXBElement>() { // from class: com.sun.xml.internal.bind.v2.runtime.ElementBeanInfoImpl.1
            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public void reset(JAXBElement o2) {
                throw new UnsupportedOperationException();
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public void serializeBody(JAXBElement e2, XMLSerializer target, Object outerPeer) throws XMLStreamException, SAXException, IOException {
                Class scope = e2.getScope();
                if (e2.isGlobalScope()) {
                    scope = null;
                }
                QName n2 = e2.getName();
                ElementBeanInfoImpl bi2 = grammar.getElement(scope, n2);
                if (bi2 != null) {
                    try {
                        bi2.property.serializeBody(e2, target, e2);
                        return;
                    } catch (AccessorException x2) {
                        target.reportError(null, x2);
                        return;
                    }
                }
                try {
                    JaxBeanInfo tbi = grammar.getBeanInfo(e2.getDeclaredType(), true);
                    Object value = e2.getValue();
                    target.startElement(n2.getNamespaceURI(), n2.getLocalPart(), n2.getPrefix(), null);
                    if (value == null) {
                        target.writeXsiNilTrue();
                    } else {
                        target.childAsXsiType(value, "value", tbi, false);
                    }
                    target.endElement();
                } catch (JAXBException x3) {
                    target.reportError(null, x3);
                }
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public void serializeURIs(JAXBElement o2, XMLSerializer target) {
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public boolean hasSerializeURIAction() {
                return false;
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public String getIdValue(JAXBElement o2) {
                return null;
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public PropertyKind getKind() {
                return PropertyKind.ELEMENT;
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder
            public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public Accessor getElementPropertyAccessor(String nsUri, String localName) {
                throw new UnsupportedOperationException();
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public void wrapUp() {
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public RuntimePropertyInfo getInfo() {
                return ElementBeanInfoImpl.this.property.getInfo();
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public boolean isHiddenByOverride() {
                return false;
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public void setHiddenByOverride(boolean hidden) {
                throw new UnsupportedOperationException("Not supported on jaxbelements.");
            }

            @Override // com.sun.xml.internal.bind.v2.runtime.property.Property
            public String getFieldName() {
                return null;
            }
        };
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/ElementBeanInfoImpl$IntercepterLoader.class */
    private final class IntercepterLoader extends Loader implements Intercepter {
        private final Loader core;

        public IntercepterLoader(Loader core) {
            this.core = core;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public final void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
            state.setLoader(this.core);
            state.setIntercepter(this);
            UnmarshallingContext context = state.getContext();
            Object child = context.getOuterPeer();
            if (child != null && ElementBeanInfoImpl.this.jaxbType != child.getClass()) {
                child = null;
            }
            if (child != null) {
                ElementBeanInfoImpl.this.reset((JAXBElement) child, context);
            }
            if (child == null) {
                child = context.createInstance(ElementBeanInfoImpl.this);
            }
            fireBeforeUnmarshal(ElementBeanInfoImpl.this, child, state);
            context.recordOuterPeer(child);
            UnmarshallingContext.State p2 = state.getPrev();
            p2.setBackup(p2.getTarget());
            p2.setTarget(child);
            this.core.startElement(state, ea);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Intercepter
        public Object intercept(UnmarshallingContext.State state, Object o2) throws SAXException {
            JAXBElement e2 = (JAXBElement) state.getTarget();
            state.setTarget(state.getBackup());
            state.setBackup(null);
            if (state.isNil()) {
                e2.setNil(true);
                state.setNil(false);
            }
            if (o2 != null) {
                e2.setValue(o2);
            }
            fireAfterUnmarshal(ElementBeanInfoImpl.this, e2, state);
            return e2;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getElementNamespaceURI(JAXBElement e2) {
        return e2.getName().getNamespaceURI();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getElementLocalName(JAXBElement e2) {
        return e2.getName().getLocalPart();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
        if (this.loader == null) {
            UnmarshallerChain c2 = new UnmarshallerChain(context);
            QNameMap<ChildLoader> result = new QNameMap<>();
            this.property.buildChildElementUnmarshallers(c2, result);
            if (result.size() == 1) {
                this.loader = new IntercepterLoader(result.getOne().getValue().loader);
            } else {
                this.loader = Discarder.INSTANCE;
            }
        }
        return this.loader;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final JAXBElement createInstance(UnmarshallingContext context) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return createInstanceFromValue(null);
    }

    public final JAXBElement createInstanceFromValue(Object o2) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return this.constructor == null ? new JAXBElement(this.tagName, this.expectedType, this.scope, o2) : this.constructor.newInstance(o2);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public boolean reset(JAXBElement e2, UnmarshallingContext context) {
        e2.setValue(null);
        return true;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public String getId(JAXBElement e2, XMLSerializer target) {
        Object o2 = e2.getValue();
        if (o2 instanceof String) {
            return (String) o2;
        }
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeBody(JAXBElement element, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        try {
            this.property.serializeBody(element, target, null);
        } catch (AccessorException x2) {
            target.reportError(null, x2);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeRoot(JAXBElement e2, XMLSerializer target) throws SAXException, XMLStreamException, IOException {
        serializeBody(e2, target);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeAttributes(JAXBElement e2, XMLSerializer target) {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void serializeURIs(JAXBElement e2, XMLSerializer target) {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public final Transducer<JAXBElement> getTransducer() {
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void wrapUp() {
        super.wrapUp();
        this.property.wrapUp();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo
    public void link(JAXBContextImpl grammar) {
        super.link(grammar);
        getLoader(grammar, true);
    }
}
