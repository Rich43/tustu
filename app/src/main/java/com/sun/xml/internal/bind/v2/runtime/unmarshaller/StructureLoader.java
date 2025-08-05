package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.ClassBeanInfoImpl;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.property.AttributeProperty;
import com.sun.xml.internal.bind.v2.runtime.property.Property;
import com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder;
import com.sun.xml.internal.bind.v2.runtime.property.UnmarshallerChain;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/StructureLoader.class */
public final class StructureLoader extends Loader {
    private final QNameMap<ChildLoader> childUnmarshallers;
    private ChildLoader catchAll;
    private ChildLoader textHandler;
    private QNameMap<TransducedAccessor> attUnmarshallers;
    private Accessor<Object, Map<QName, String>> attCatchAll;
    private final JaxBeanInfo beanInfo;
    private int frameSize;
    private static final QNameMap<TransducedAccessor> EMPTY;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StructureLoader.class.desiredAssertionStatus();
        EMPTY = new QNameMap<>();
    }

    public StructureLoader(ClassBeanInfoImpl beanInfo) {
        super(true);
        this.childUnmarshallers = new QNameMap<>();
        this.beanInfo = beanInfo;
    }

    public void init(JAXBContextImpl context, ClassBeanInfoImpl beanInfo, Accessor<?, Map<QName, String>> attWildcard) {
        UnmarshallerChain chain = new UnmarshallerChain(context);
        ClassBeanInfoImpl classBeanInfoImpl = beanInfo;
        while (true) {
            ClassBeanInfoImpl bi2 = classBeanInfoImpl;
            if (bi2 != null) {
                for (int i2 = bi2.properties.length - 1; i2 >= 0; i2--) {
                    Property p2 = bi2.properties[i2];
                    switch (p2.getKind()) {
                        case ATTRIBUTE:
                            if (this.attUnmarshallers == null) {
                                this.attUnmarshallers = new QNameMap<>();
                            }
                            AttributeProperty ap2 = (AttributeProperty) p2;
                            this.attUnmarshallers.put(ap2.attName.toQName(), (QName) ap2.xacc);
                            break;
                        case ELEMENT:
                        case REFERENCE:
                        case MAP:
                        case VALUE:
                            p2.buildChildElementUnmarshallers(chain, this.childUnmarshallers);
                            break;
                    }
                }
                classBeanInfoImpl = bi2.superClazz;
            } else {
                this.frameSize = chain.getScopeSize();
                this.textHandler = this.childUnmarshallers.get(StructureLoaderBuilder.TEXT_HANDLER);
                this.catchAll = this.childUnmarshallers.get(StructureLoaderBuilder.CATCH_ALL);
                if (attWildcard != null) {
                    this.attCatchAll = attWildcard;
                    if (this.attUnmarshallers == null) {
                        this.attUnmarshallers = EMPTY;
                        return;
                    }
                    return;
                }
                this.attCatchAll = null;
                return;
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        UnmarshallingContext context = state.getContext();
        if (!$assertionsDisabled && this.beanInfo.isImmutable()) {
            throw new AssertionError();
        }
        Object child = context.getInnerPeer();
        if (child != null && this.beanInfo.jaxbType != child.getClass()) {
            child = null;
        }
        if (child != null) {
            this.beanInfo.reset(child, context);
        }
        if (child == null) {
            child = context.createInstance(this.beanInfo);
        }
        context.recordInnerPeer(child);
        state.setTarget(child);
        fireBeforeUnmarshal(this.beanInfo, child, state);
        context.startScope(this.frameSize);
        if (this.attUnmarshallers != null) {
            Attributes atts = ea.atts;
            for (int i2 = 0; i2 < atts.getLength(); i2++) {
                String auri = atts.getURI(i2);
                String alocal = atts.getLocalName(i2);
                if ("".equals(alocal)) {
                    alocal = atts.getQName(i2);
                }
                String avalue = atts.getValue(i2);
                TransducedAccessor xacc = this.attUnmarshallers.get(auri, alocal);
                if (xacc != null) {
                    try {
                        xacc.parse(child, avalue);
                    } catch (AccessorException e2) {
                        handleGenericException(e2, true);
                    }
                } else {
                    if (this.attCatchAll != null) {
                        String qname = atts.getQName(i2);
                        if (!atts.getURI(i2).equals("http://www.w3.org/2001/XMLSchema-instance")) {
                            Object o2 = state.getTarget();
                            Map<QName, String> map = this.attCatchAll.get(o2);
                            if (map == null) {
                                if (this.attCatchAll.valueType.isAssignableFrom(HashMap.class)) {
                                    map = new HashMap();
                                    this.attCatchAll.set(o2, map);
                                } else {
                                    context.handleError(Messages.UNABLE_TO_CREATE_MAP.format(this.attCatchAll.valueType));
                                    return;
                                }
                            }
                            int idx = qname.indexOf(58);
                            String prefix = idx < 0 ? "" : qname.substring(0, idx);
                            map.put(new QName(auri, alocal, prefix), avalue);
                        }
                    }
                }
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void childElement(UnmarshallingContext.State state, TagName arg) throws SAXException {
        ChildLoader child = this.childUnmarshallers.get(arg.uri, arg.local);
        if (child == null) {
            child = this.catchAll;
            if (child == null) {
                super.childElement(state, arg);
                return;
            }
        }
        state.setLoader(child.loader);
        state.setReceiver(child.receiver);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public Collection<QName> getExpectedChildElements() {
        return this.childUnmarshallers.keySet();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public Collection<QName> getExpectedAttributes() {
        return this.attUnmarshallers.keySet();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
        if (this.textHandler != null) {
            this.textHandler.loader.text(state, text);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        state.getContext().endScope(this.frameSize);
        fireAfterUnmarshal(this.beanInfo, state.getTarget(), state.getPrev());
    }

    public JaxBeanInfo getBeanInfo() {
        return this.beanInfo;
    }
}
