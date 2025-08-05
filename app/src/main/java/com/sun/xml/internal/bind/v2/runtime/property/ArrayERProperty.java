package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import java.io.IOException;
import java.util.Collection;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ArrayERProperty.class */
abstract class ArrayERProperty<BeanT, ListT, ItemT> extends ArrayProperty<BeanT, ListT, ItemT> {
    protected final Name wrapperTagName;
    protected final boolean isWrapperNillable;

    protected abstract void serializeListBody(BeanT beant, XMLSerializer xMLSerializer, ListT listt) throws AccessorException, XMLStreamException, SAXException, IOException;

    protected abstract void createBodyUnmarshaller(UnmarshallerChain unmarshallerChain, QNameMap<ChildLoader> qNameMap);

    protected ArrayERProperty(JAXBContextImpl grammar, RuntimePropertyInfo prop, QName tagName, boolean isWrapperNillable) {
        super(grammar, prop);
        if (tagName == null) {
            this.wrapperTagName = null;
        } else {
            this.wrapperTagName = grammar.nameBuilder.createElementName(tagName);
        }
        this.isWrapperNillable = isWrapperNillable;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ArrayERProperty$ItemsLoader.class */
    private static final class ItemsLoader extends Loader {
        private final Accessor acc;
        private final Lister lister;
        private final QNameMap<ChildLoader> children;

        public ItemsLoader(Accessor acc, Lister lister, QNameMap<ChildLoader> children) {
            super(false);
            this.acc = acc;
            this.lister = lister;
            this.children = children;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
            UnmarshallingContext context = state.getContext();
            context.startScope(1);
            state.setTarget(state.getPrev().getTarget());
            context.getScope(0).start(this.acc, this.lister);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
            ChildLoader child = this.children.get(ea.uri, ea.local);
            if (child == null) {
                child = this.children.get(StructureLoaderBuilder.CATCH_ALL);
            }
            if (child == null) {
                super.childElement(state, ea);
            } else {
                state.setLoader(child.loader);
                state.setReceiver(child.receiver);
            }
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
            state.getContext().endScope(1);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
        public Collection<QName> getExpectedChildElements() {
            return this.children.keySet();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl, com.sun.xml.internal.bind.v2.runtime.property.Property
    public final void serializeBody(BeanT o2, XMLSerializer w2, Object outerPeer) throws AccessorException, XMLStreamException, SAXException, IOException {
        ListT list = this.acc.get(o2);
        if (list != null) {
            if (this.wrapperTagName != null) {
                w2.startElement(this.wrapperTagName, null);
                w2.endNamespaceDecls(list);
                w2.endAttributes();
            }
            serializeListBody(o2, w2, list);
            if (this.wrapperTagName != null) {
                w2.endElement();
                return;
            }
            return;
        }
        if (this.isWrapperNillable) {
            w2.startElement(this.wrapperTagName, null);
            w2.writeXsiNilTrue();
            w2.endElement();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder
    public final void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> loaders) {
        if (this.wrapperTagName != null) {
            UnmarshallerChain c2 = new UnmarshallerChain(chain.context);
            QNameMap<ChildLoader> m2 = new QNameMap<>();
            createBodyUnmarshaller(c2, m2);
            Loader loader = new ItemsLoader(this.acc, this.lister, m2);
            if (this.isWrapperNillable || chain.context.allNillable) {
                loader = new XsiNilLoader(loader);
            }
            loaders.put(this.wrapperTagName, (Name) new ChildLoader(loader, null));
            return;
        }
        createBodyUnmarshaller(chain, loaders);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/ArrayERProperty$ReceiverImpl.class */
    protected final class ReceiverImpl implements Receiver {
        private final int offset;

        protected ReceiverImpl(int offset) {
            this.offset = offset;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver
        public void receive(UnmarshallingContext.State state, Object o2) throws SAXException {
            state.getContext().getScope(this.offset).add(ArrayERProperty.this.acc, ArrayERProperty.this.lister, o2);
        }
    }
}
