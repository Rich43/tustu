package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.AccessorFactory;
import com.sun.xml.internal.bind.AccessorFactoryImpl;
import com.sun.xml.internal.bind.InternalAccessorFactory;
import com.sun.xml.internal.bind.XmlAccessorFactory;
import com.sun.xml.internal.bind.annotation.XmlLocation;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.ClassFactory;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeValuePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.Location;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeClassInfoImpl.class */
class RuntimeClassInfoImpl extends ClassInfoImpl<Type, Class, Field, Method> implements RuntimeClassInfo, RuntimeElement {
    private Accessor<?, Locator> xmlLocationAccessor;
    private AccessorFactory accessorFactory;
    private boolean supressAccessorWarnings;
    private Accessor<?, Map<QName, String>> attributeWildcardAccessor;
    private boolean computedTransducer;
    private Transducer xducer;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !RuntimeClassInfoImpl.class.desiredAssertionStatus();
    }

    public RuntimeClassInfoImpl(RuntimeModelBuilder modelBuilder, Locatable upstream, Class clazz) {
        super(modelBuilder, upstream, clazz);
        this.supressAccessorWarnings = false;
        this.computedTransducer = false;
        this.xducer = null;
        this.accessorFactory = createAccessorFactory(clazz);
    }

    protected AccessorFactory createAccessorFactory(Class clazz) {
        XmlAccessorFactory factoryAnn;
        AccessorFactory accFactory = null;
        JAXBContextImpl context = ((RuntimeModelBuilder) this.builder).context;
        if (context != null) {
            this.supressAccessorWarnings = context.supressAccessorWarnings;
            if (context.xmlAccessorFactorySupport && (factoryAnn = findXmlAccessorFactoryAnnotation(clazz)) != null) {
                try {
                    accFactory = factoryAnn.value().newInstance();
                } catch (IllegalAccessException e2) {
                    this.builder.reportError(new IllegalAnnotationException(Messages.ACCESSORFACTORY_ACCESS_EXCEPTION.format(factoryAnn.getClass().getName(), nav().getClassName(clazz)), this));
                } catch (InstantiationException e3) {
                    this.builder.reportError(new IllegalAnnotationException(Messages.ACCESSORFACTORY_INSTANTIATION_EXCEPTION.format(factoryAnn.getClass().getName(), nav().getClassName(clazz)), this));
                }
            }
        }
        if (accFactory == null) {
            accFactory = AccessorFactoryImpl.getInstance();
        }
        return accFactory;
    }

    protected XmlAccessorFactory findXmlAccessorFactoryAnnotation(Class clazz) {
        XmlAccessorFactory factoryAnn = (XmlAccessorFactory) reader().getClassAnnotation(XmlAccessorFactory.class, clazz, this);
        if (factoryAnn == null) {
            factoryAnn = (XmlAccessorFactory) reader().getPackageAnnotation(XmlAccessorFactory.class, clazz, this);
        }
        return factoryAnn;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl, com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo
    public Method getFactoryMethod() {
        return super.getFactoryMethod();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl, com.sun.xml.internal.bind.v2.model.core.ClassInfo
    /* renamed from: getBaseClass, reason: merged with bridge method [inline-methods] */
    public final ClassInfo<Type, Class> getBaseClass2() {
        return (RuntimeClassInfoImpl) super.getBaseClass2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl
    protected ReferencePropertyInfoImpl<Type, Class, Field, Method> createReferenceProperty(PropertySeed<Type, Class, Field, Method> seed) {
        return new RuntimeReferencePropertyInfoImpl(this, seed);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl
    protected AttributePropertyInfoImpl<Type, Class, Field, Method> createAttributeProperty(PropertySeed<Type, Class, Field, Method> seed) {
        return new RuntimeAttributePropertyInfoImpl(this, seed);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl
    protected ValuePropertyInfoImpl<Type, Class, Field, Method> createValueProperty(PropertySeed<Type, Class, Field, Method> seed) {
        return new RuntimeValuePropertyInfoImpl(this, seed);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl
    protected ElementPropertyInfoImpl<Type, Class, Field, Method> createElementProperty(PropertySeed<Type, Class, Field, Method> seed) {
        return new RuntimeElementPropertyInfoImpl(this, seed);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl
    protected MapPropertyInfoImpl<Type, Class, Field, Method> createMapProperty(PropertySeed<Type, Class, Field, Method> seed) {
        return new RuntimeMapPropertyInfoImpl(this, seed);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl, com.sun.xml.internal.bind.v2.model.core.ClassInfo
    public List<? extends PropertyInfo<Type, Class>> getProperties() {
        return super.getProperties();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl, com.sun.xml.internal.bind.v2.model.core.ClassInfo
    /* renamed from: getProperty, reason: merged with bridge method [inline-methods] */
    public PropertyInfo<Type, Class> getProperty2(String name) {
        return (RuntimePropertyInfo) super.getProperty2(name);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl, com.sun.xml.internal.bind.v2.model.impl.TypeInfoImpl
    public void link() throws ClassInfoImpl.ConflictException {
        getTransducer();
        super.link();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo
    public <B> Accessor<B, Map<QName, String>> getAttributeWildcard() {
        ClassInfo<Type, Class> baseClass2 = this;
        while (true) {
            RuntimeClassInfoImpl runtimeClassInfoImpl = baseClass2;
            if (runtimeClassInfoImpl != 0) {
                if (runtimeClassInfoImpl.attributeWildcard == null) {
                    baseClass2 = runtimeClassInfoImpl.getBaseClass2();
                } else {
                    if (runtimeClassInfoImpl.attributeWildcardAccessor == null) {
                        runtimeClassInfoImpl.attributeWildcardAccessor = runtimeClassInfoImpl.createAttributeWildcardAccessor();
                    }
                    return (Accessor<B, Map<QName, String>>) runtimeClassInfoImpl.attributeWildcardAccessor;
                }
            } else {
                return null;
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement
    public Transducer getTransducer() {
        if (!this.computedTransducer) {
            this.computedTransducer = true;
            this.xducer = calcTransducer();
        }
        return this.xducer;
    }

    private Transducer calcTransducer() {
        RuntimeValuePropertyInfo valuep = null;
        if (hasAttributeWildcard()) {
            return null;
        }
        ClassInfo<Type, Class> baseClass2 = this;
        while (true) {
            ClassInfo<Type, Class> classInfo = baseClass2;
            if (classInfo != null) {
                Iterator<? extends PropertyInfo<Type, Class>> it = classInfo.getProperties().iterator();
                while (it.hasNext()) {
                    RuntimePropertyInfo pi = (RuntimePropertyInfo) it.next();
                    if (pi.kind() == PropertyKind.VALUE) {
                        valuep = (RuntimeValuePropertyInfo) pi;
                    } else {
                        return null;
                    }
                }
                baseClass2 = classInfo.getBaseClass2();
            } else {
                if (valuep == null || !valuep.getTarget2().isSimpleType()) {
                    return null;
                }
                return new TransducerImpl(getClazz(), TransducedAccessor.get(((RuntimeModelBuilder) this.builder).context, valuep));
            }
        }
    }

    private Accessor<?, Map<QName, String>> createAttributeWildcardAccessor() {
        if ($assertionsDisabled || this.attributeWildcard != null) {
            return ((RuntimePropertySeed) this.attributeWildcard).getAccessor();
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl
    public RuntimePropertySeed createFieldSeed(Field field) {
        Accessor acc;
        boolean readOnly = Modifier.isStatic(field.getModifiers());
        try {
            if (this.supressAccessorWarnings) {
                acc = ((InternalAccessorFactory) this.accessorFactory).createFieldAccessor((Class) this.clazz, field, readOnly, this.supressAccessorWarnings);
            } else {
                acc = this.accessorFactory.createFieldAccessor((Class) this.clazz, field, readOnly);
            }
        } catch (JAXBException e2) {
            this.builder.reportError(new IllegalAnnotationException(Messages.CUSTOM_ACCESSORFACTORY_FIELD_ERROR.format(nav().getClassName(this.clazz), e2.toString()), this));
            acc = Accessor.getErrorInstance();
        }
        return new RuntimePropertySeed(super.createFieldSeed((RuntimeClassInfoImpl) field), acc);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl
    public RuntimePropertySeed createAccessorSeed(Method getter, Method setter) {
        Accessor acc;
        try {
            acc = this.accessorFactory.createPropertyAccessor((Class) this.clazz, getter, setter);
        } catch (JAXBException e2) {
            this.builder.reportError(new IllegalAnnotationException(Messages.CUSTOM_ACCESSORFACTORY_PROPERTY_ERROR.format(nav().getClassName(this.clazz), e2.toString()), this));
            acc = Accessor.getErrorInstance();
        }
        return new RuntimePropertySeed(super.createAccessorSeed(getter, setter), acc);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl
    public void checkFieldXmlLocation(Field f2) {
        if (reader().hasFieldAnnotation(XmlLocation.class, f2)) {
            this.xmlLocationAccessor = new Accessor.FieldReflection(f2);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo
    public Accessor<?, Locator> getLocatorField() {
        return this.xmlLocationAccessor;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeClassInfoImpl$RuntimePropertySeed.class */
    static final class RuntimePropertySeed implements PropertySeed<Type, Class, Field, Method> {
        private final Accessor acc;
        private final PropertySeed<Type, Class, Field, Method> core;

        public RuntimePropertySeed(PropertySeed<Type, Class, Field, Method> core, Accessor acc) {
            this.core = core;
            this.acc = acc;
        }

        @Override // com.sun.xml.internal.bind.v2.model.impl.PropertySeed
        public String getName() {
            return this.core.getName();
        }

        @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource
        public <A extends Annotation> A readAnnotation(Class<A> cls) {
            return (A) this.core.readAnnotation(cls);
        }

        @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource
        public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
            return this.core.hasAnnotation(annotationType);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.sun.xml.internal.bind.v2.model.impl.PropertySeed
        public Type getRawType() {
            return this.core.getRawType();
        }

        @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
        public Location getLocation() {
            return this.core.getLocation();
        }

        @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
        public Locatable getUpstream() {
            return this.core.getUpstream();
        }

        public Accessor getAccessor() {
            return this.acc;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeClassInfoImpl$TransducerImpl.class */
    private static final class TransducerImpl<BeanT> implements Transducer<BeanT> {
        private final TransducedAccessor<BeanT> xacc;
        private final Class<BeanT> ownerClass;

        public TransducerImpl(Class<BeanT> ownerClass, TransducedAccessor<BeanT> xacc) {
            this.xacc = xacc;
            this.ownerClass = ownerClass;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public boolean useNamespace() {
            return this.xacc.useNamespace();
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public boolean isDefault() {
            return false;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public void declareNamespace(BeanT bean, XMLSerializer w2) throws AccessorException {
            try {
                this.xacc.declareNamespace(bean, w2);
            } catch (SAXException e2) {
                throw new AccessorException(e2);
            }
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        @NotNull
        public CharSequence print(BeanT o2) throws AccessorException {
            try {
                CharSequence value = this.xacc.print(o2);
                if (value == null) {
                    throw new AccessorException(Messages.THERE_MUST_BE_VALUE_IN_XMLVALUE.format(o2));
                }
                return value;
            } catch (SAXException e2) {
                throw new AccessorException(e2);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public BeanT parse(CharSequence charSequence) throws AccessorException, SAXException {
            Object objCreate;
            UnmarshallingContext unmarshallingContext = UnmarshallingContext.getInstance();
            if (unmarshallingContext != null) {
                objCreate = unmarshallingContext.createInstance((Class<?>) this.ownerClass);
            } else {
                objCreate = ClassFactory.create(this.ownerClass);
            }
            this.xacc.parse(objCreate, charSequence);
            return (BeanT) objCreate;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public void writeText(XMLSerializer w2, BeanT o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
            if (!this.xacc.hasValue(o2)) {
                throw new AccessorException(Messages.THERE_MUST_BE_VALUE_IN_XMLVALUE.format(o2));
            }
            this.xacc.writeText(w2, o2, fieldName);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public void writeLeafElement(XMLSerializer w2, Name tagName, BeanT o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
            if (!this.xacc.hasValue(o2)) {
                throw new AccessorException(Messages.THERE_MUST_BE_VALUE_IN_XMLVALUE.format(o2));
            }
            this.xacc.writeLeafElement(w2, tagName, o2, fieldName);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
        public QName getTypeName(BeanT instance) {
            return null;
        }
    }
}
