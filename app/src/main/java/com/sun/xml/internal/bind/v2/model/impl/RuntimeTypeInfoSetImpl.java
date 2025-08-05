package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.internal.bind.v2.model.core.ArrayInfo;
import com.sun.xml.internal.bind.v2.model.core.BuiltinLeafInfo;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeTypeInfoSetImpl.class */
final class RuntimeTypeInfoSetImpl extends TypeInfoSetImpl<Type, Class, Field, Method> implements RuntimeTypeInfoSet {
    public RuntimeTypeInfoSetImpl(AnnotationReader<Type, Class, Field, Method> reader) {
        super(Utils.REFLECTION_NAVIGATOR, reader, RuntimeBuiltinLeafInfoImpl.LEAVES);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl
    /* renamed from: createAnyType, reason: merged with bridge method [inline-methods] */
    public NonElement<Type, Class> createAnyType2() {
        return RuntimeAnyTypeImpl.theInstance;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public RuntimeNonElement getTypeInfo(Type type) {
        return (RuntimeNonElement) super.getTypeInfo((RuntimeTypeInfoSetImpl) type);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    /* renamed from: getAnyTypeInfo */
    public NonElement<Type, Class> getAnyTypeInfo2() {
        return (RuntimeNonElement) super.getAnyTypeInfo2();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public RuntimeNonElement getClassInfo(Class clazz) {
        return (RuntimeNonElement) super.getClassInfo((RuntimeTypeInfoSetImpl) clazz);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<Class, ? extends ClassInfo<Type, Class>> beans() {
        return super.beans();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<Type, ? extends BuiltinLeafInfo<Type, Class>> builtins() {
        return super.builtins();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<Class, ? extends EnumLeafInfo<Type, Class>> enums() {
        return super.enums();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<? extends Type, ? extends ArrayInfo<Type, Class>> arrays() {
        return super.arrays();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public RuntimeElementInfoImpl getElementInfo(Class scope, QName name) {
        return (RuntimeElementInfoImpl) super.getElementInfo((RuntimeTypeInfoSetImpl) scope, name);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Map<QName, RuntimeElementInfoImpl> getElementMappings(Class scope) {
        return super.getElementMappings((RuntimeTypeInfoSetImpl) scope);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.TypeInfoSetImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    public Iterable<? extends ElementInfo<Type, Class>> getAllElements() {
        return super.getAllElements();
    }
}
