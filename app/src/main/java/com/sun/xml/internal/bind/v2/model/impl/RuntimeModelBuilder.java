package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.WhiteSpaceProcessor;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElementRef;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet;
import com.sun.xml.internal.bind.v2.runtime.FilterTransducer;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.internal.bind.v2.runtime.InlineBinaryTransducer;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.internal.bind.v2.runtime.MimeTypedTransducer;
import com.sun.xml.internal.bind.v2.runtime.SchemaTypeTransducer;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import javax.activation.MimeType;
import javax.xml.namespace.QName;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeModelBuilder.class */
public class RuntimeModelBuilder extends ModelBuilder<Type, Class, Field, Method> {

    @Nullable
    public final JAXBContextImpl context;

    public RuntimeModelBuilder(JAXBContextImpl context, RuntimeAnnotationReader annotationReader, Map<Class, Class> subclassReplacements, String defaultNamespaceRemap) {
        super(annotationReader, Utils.REFLECTION_NAVIGATOR, subclassReplacements, defaultNamespaceRemap);
        this.context = context;
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilder
    public RuntimeNonElement getClassInfo(Class clazz, Locatable upstream) {
        return (RuntimeNonElement) super.getClassInfo((RuntimeModelBuilder) clazz, upstream);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilder
    public RuntimeNonElement getClassInfo(Class clazz, boolean searchForSuperClass, Locatable upstream) {
        return (RuntimeNonElement) super.getClassInfo((RuntimeModelBuilder) clazz, searchForSuperClass, upstream);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilder
    public RuntimeEnumLeafInfoImpl createEnumLeafInfo(Class clazz, Locatable upstream) {
        return new RuntimeEnumLeafInfoImpl(this, upstream, clazz);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilder
    public RuntimeClassInfoImpl createClassInfo(Class clazz, Locatable upstream) {
        return new RuntimeClassInfoImpl(this, upstream, clazz);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilder
    public RuntimeElementInfoImpl createElementInfo(RegistryInfoImpl<Type, Class, Field, Method> registryInfo, Method method) throws IllegalAnnotationException {
        return new RuntimeElementInfoImpl(this, registryInfo, method);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilder
    public RuntimeArrayInfoImpl createArrayInfo(Locatable upstream, Type arrayType) {
        return new RuntimeArrayInfoImpl(this, upstream, (Class) arrayType);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilder
    /* renamed from: createTypeInfoSet, reason: merged with bridge method [inline-methods] */
    public TypeInfoSetImpl<Type, Class, Field, Method> createTypeInfoSet2() {
        return new RuntimeTypeInfoSetImpl(this.reader);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ModelBuilder
    /* renamed from: link, reason: merged with bridge method [inline-methods] */
    public TypeInfoSet<Type, Class, Field, Method> link2() {
        return (RuntimeTypeInfoSet) super.link2();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement] */
    public static Transducer createTransducer(RuntimeNonElementRef ref) {
        Transducer t2 = ref.getTarget2().getTransducer();
        PropertyInfo<Type, Class> source2 = ref.getSource2();
        ID id = source2.id();
        if (id == ID.IDREF) {
            return RuntimeBuiltinLeafInfoImpl.STRING;
        }
        if (id == ID.ID) {
            t2 = new IDTransducerImpl(t2);
        }
        MimeType emt = source2.getExpectedMimeType();
        if (emt != null) {
            t2 = new MimeTypedTransducer(t2, emt);
        }
        if (source2.inlineBinaryData()) {
            t2 = new InlineBinaryTransducer(t2);
        }
        if (source2.getSchemaType() != null) {
            if (source2.getSchemaType().equals(createXSSimpleType())) {
                return RuntimeBuiltinLeafInfoImpl.STRING;
            }
            t2 = new SchemaTypeTransducer(t2, source2.getSchemaType());
        }
        return t2;
    }

    private static QName createXSSimpleType() {
        return new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_ANYSIMPLETYPE);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeModelBuilder$IDTransducerImpl.class */
    private static final class IDTransducerImpl<ValueT> extends FilterTransducer<ValueT> {
        public IDTransducerImpl(Transducer<ValueT> core) {
            super(core);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
        public ValueT parse(CharSequence charSequence) throws AccessorException, SAXException {
            String string = WhiteSpaceProcessor.trim(charSequence).toString();
            UnmarshallingContext.getInstance().addToIdTable(string);
            return (ValueT) this.core.parse(string);
        }
    }
}
