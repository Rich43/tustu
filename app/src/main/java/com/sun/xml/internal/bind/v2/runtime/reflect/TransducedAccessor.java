package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.istack.internal.SAXException2;
import com.sun.xml.internal.bind.WhiteSpaceProcessor;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.model.impl.RuntimeModelBuilder;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElementRef;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.OptimizedTransducedAccessorFactory;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Patcher;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.util.concurrent.Callable;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/TransducedAccessor.class */
public abstract class TransducedAccessor<BeanT> {
    @Nullable
    public abstract CharSequence print(@NotNull BeanT beant) throws AccessorException, SAXException;

    public abstract void parse(BeanT beant, CharSequence charSequence) throws AccessorException, SAXException;

    public abstract boolean hasValue(BeanT beant) throws AccessorException;

    public abstract void writeLeafElement(XMLSerializer xMLSerializer, Name name, BeanT beant, String str) throws AccessorException, SAXException, XMLStreamException, IOException;

    public abstract void writeText(XMLSerializer xMLSerializer, BeanT beant, String str) throws AccessorException, SAXException, XMLStreamException, IOException;

    public boolean useNamespace() {
        return false;
    }

    public void declareNamespace(BeanT o2, XMLSerializer w2) throws AccessorException, SAXException {
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo] */
    public static <T> TransducedAccessor<T> get(JAXBContextImpl context, RuntimeNonElementRef ref) {
        TransducedAccessor xa;
        Transducer xducer = RuntimeModelBuilder.createTransducer(ref);
        ?? source = ref.getSource2();
        if (source.isCollection()) {
            return new ListTransducedAccessorImpl(xducer, source.getAccessor(), Lister.create(Utils.REFLECTION_NAVIGATOR.erasure(source.getRawType()), source.id(), source.getAdapter()));
        }
        if (source.id() == ID.IDREF) {
            return new IDREFTransducedAccessorImpl(source.getAccessor());
        }
        if (xducer.isDefault() && context != null && !context.fastBoot && (xa = OptimizedTransducedAccessorFactory.get(source)) != null) {
            return xa;
        }
        if (xducer.useNamespace()) {
            return new CompositeContextDependentTransducedAccessorImpl(context, xducer, source.getAccessor());
        }
        return new CompositeTransducedAccessorImpl(context, xducer, source.getAccessor());
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/TransducedAccessor$CompositeContextDependentTransducedAccessorImpl.class */
    static class CompositeContextDependentTransducedAccessorImpl<BeanT, ValueT> extends CompositeTransducedAccessorImpl<BeanT, ValueT> {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !TransducedAccessor.class.desiredAssertionStatus();
        }

        public CompositeContextDependentTransducedAccessorImpl(JAXBContextImpl context, Transducer<ValueT> xducer, Accessor<BeanT, ValueT> acc) {
            super(context, xducer, acc);
            if (!$assertionsDisabled && !xducer.useNamespace()) {
                throw new AssertionError();
            }
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public boolean useNamespace() {
            return true;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public void declareNamespace(BeanT bean, XMLSerializer w2) throws AccessorException {
            ValueT o2 = this.acc.get(bean);
            if (o2 != null) {
                this.xducer.declareNamespace(o2, w2);
            }
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor.CompositeTransducedAccessorImpl, com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public void writeLeafElement(XMLSerializer xMLSerializer, Name name, BeanT beant, String str) throws AccessorException, XMLStreamException, SAXException, IOException {
            xMLSerializer.startElement(name, null);
            declareNamespace(beant, xMLSerializer);
            xMLSerializer.endNamespaceDecls(null);
            xMLSerializer.endAttributes();
            this.xducer.writeText(xMLSerializer, this.acc.get(beant), str);
            xMLSerializer.endElement();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/TransducedAccessor$CompositeTransducedAccessorImpl.class */
    public static class CompositeTransducedAccessorImpl<BeanT, ValueT> extends TransducedAccessor<BeanT> {
        protected final Transducer<ValueT> xducer;
        protected final Accessor<BeanT, ValueT> acc;

        public CompositeTransducedAccessorImpl(JAXBContextImpl context, Transducer<ValueT> xducer, Accessor<BeanT, ValueT> acc) {
            this.xducer = xducer;
            this.acc = acc.optimize(context);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public CharSequence print(BeanT bean) throws AccessorException {
            ValueT o2 = this.acc.get(bean);
            if (o2 == null) {
                return null;
            }
            return this.xducer.print(o2);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public void parse(BeanT beant, CharSequence charSequence) throws AccessorException, SAXException {
            this.acc.set(beant, this.xducer.parse(charSequence));
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public boolean hasValue(BeanT bean) throws AccessorException {
            return this.acc.getUnadapted(bean) != null;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public void writeLeafElement(XMLSerializer xMLSerializer, Name name, BeanT beant, String str) throws AccessorException, SAXException, XMLStreamException, IOException {
            this.xducer.writeLeafElement(xMLSerializer, name, this.acc.get(beant), str);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public void writeText(XMLSerializer xMLSerializer, BeanT beant, String str) throws AccessorException, SAXException, XMLStreamException, IOException {
            this.xducer.writeText(xMLSerializer, this.acc.get(beant), str);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/TransducedAccessor$IDREFTransducedAccessorImpl.class */
    private static final class IDREFTransducedAccessorImpl<BeanT, TargetT> extends DefaultTransducedAccessor<BeanT> {
        private final Accessor<BeanT, TargetT> acc;
        private final Class<TargetT> targetType;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException, SAXException {
            return print((IDREFTransducedAccessorImpl<BeanT, TargetT>) obj);
        }

        public IDREFTransducedAccessorImpl(Accessor<BeanT, TargetT> acc) {
            this.acc = acc;
            this.targetType = acc.getValueType();
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public String print(BeanT bean) throws AccessorException, SAXException {
            TargetT target = this.acc.get(bean);
            if (target == null) {
                return null;
            }
            XMLSerializer w2 = XMLSerializer.getInstance();
            try {
                String id = w2.grammar.getBeanInfo((Object) target, true).getId(target, w2);
                if (id == null) {
                    w2.errorMissingId(target);
                }
                return id;
            } catch (JAXBException e2) {
                w2.reportError(null, e2);
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void assign(BeanT bean, TargetT t2, UnmarshallingContext context) throws AccessorException {
            if (!this.targetType.isInstance(t2)) {
                context.handleError(Messages.UNASSIGNABLE_TYPE.format(this.targetType, t2.getClass()));
            } else {
                this.acc.set(bean, t2);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public void parse(final BeanT bean, CharSequence lexical) throws AccessorException, SAXException {
            final String idref = WhiteSpaceProcessor.trim(lexical).toString();
            final UnmarshallingContext context = UnmarshallingContext.getInstance();
            final Callable callable = context.getObjectFromId(idref, this.acc.valueType);
            if (callable == null) {
                context.errorUnresolvedIDREF(bean, idref, context.getLocator());
                return;
            }
            try {
                Object objCall = callable.call();
                if (objCall != null) {
                    assign(bean, objCall, context);
                } else {
                    final LocatorEx loc = new LocatorEx.Snapshot(context.getLocator());
                    context.addPatcher(new Patcher() { // from class: com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor.IDREFTransducedAccessorImpl.1
                        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Patcher
                        public void run() throws SAXException {
                            try {
                                Object objCall2 = callable.call();
                                if (objCall2 != null) {
                                    IDREFTransducedAccessorImpl.this.assign(bean, objCall2, context);
                                } else {
                                    context.errorUnresolvedIDREF(bean, idref, loc);
                                }
                            } catch (AccessorException e2) {
                                context.handleError(e2);
                            } catch (RuntimeException e3) {
                                throw e3;
                            } catch (SAXException e4) {
                                throw e4;
                            } catch (Exception e5) {
                                throw new SAXException2(e5);
                            }
                        }
                    });
                }
            } catch (RuntimeException e2) {
                throw e2;
            } catch (SAXException e3) {
                throw e3;
            } catch (Exception e4) {
                throw new SAXException2(e4);
            }
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
        public boolean hasValue(BeanT bean) throws AccessorException {
            return this.acc.get(bean) != null;
        }
    }
}
