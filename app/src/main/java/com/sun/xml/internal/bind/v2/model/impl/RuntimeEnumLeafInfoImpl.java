package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.model.annotation.FieldLocatable;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeEnumLeafInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import java.lang.Enum;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeEnumLeafInfoImpl.class */
final class RuntimeEnumLeafInfoImpl<T extends Enum<T>, B> extends EnumLeafInfoImpl<Type, Class, Field, Method> implements RuntimeEnumLeafInfo, Transducer<T> {
    private final Transducer<B> baseXducer;
    private final Map<B, T> parseMap;
    private final Map<T, B> printMap;

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo, com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement
    public Transducer<T> getTransducer() {
        return this;
    }

    RuntimeEnumLeafInfoImpl(RuntimeModelBuilder builder, Locatable upstream, Class<T> enumType) {
        super(builder, upstream, enumType, enumType);
        this.parseMap = new HashMap();
        this.printMap = new EnumMap(enumType);
        this.baseXducer = ((RuntimeNonElement) this.baseType).getTransducer();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.EnumLeafInfoImpl
    public RuntimeEnumConstantImpl createEnumConstant(String str, String str2, Field field, EnumConstantImpl<Type, Class, Field, Method> enumConstantImpl) {
        try {
            try {
                field.setAccessible(true);
            } catch (IllegalAccessException e2) {
                throw new IllegalAccessError(e2.getMessage());
            }
        } catch (SecurityException e3) {
        }
        Enum r0 = (Enum) field.get(null);
        B b2 = null;
        try {
            b2 = this.baseXducer.parse(str2);
        } catch (Exception e4) {
            this.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ENUM_VALUE.format(str2, ((Type) this.baseType.getType2()).toString()), e4, new FieldLocatable(this, field, nav())));
        }
        this.parseMap.put(b2, r0);
        this.printMap.put(r0, b2);
        return new RuntimeEnumConstantImpl(this, str, str2, enumConstantImpl);
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo
    public QName[] getTypeNames() {
        return new QName[]{getTypeName()};
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public boolean isDefault() {
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.model.impl.EnumLeafInfoImpl, com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo
    public Class getClazz() {
        return (Class) this.clazz;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public boolean useNamespace() {
        return this.baseXducer.useNamespace();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public void declareNamespace(T t2, XMLSerializer xMLSerializer) throws AccessorException {
        this.baseXducer.declareNamespace(this.printMap.get(t2), xMLSerializer);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public CharSequence print(T t2) throws AccessorException {
        return this.baseXducer.print(this.printMap.get(t2));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public T parse(CharSequence lexical) throws AccessorException, SAXException {
        Object objTrim = this.baseXducer.parse(lexical);
        if (this.tokenStringType) {
            objTrim = ((String) objTrim).trim();
        }
        return this.parseMap.get(objTrim);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeText(XMLSerializer xMLSerializer, T t2, String str) throws AccessorException, SAXException, XMLStreamException, IOException {
        this.baseXducer.writeText(xMLSerializer, this.printMap.get(t2), str);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeLeafElement(XMLSerializer xMLSerializer, Name name, T t2, String str) throws AccessorException, SAXException, XMLStreamException, IOException {
        this.baseXducer.writeLeafElement(xMLSerializer, name, this.printMap.get(t2), str);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public QName getTypeName(T instance) {
        return null;
    }
}
