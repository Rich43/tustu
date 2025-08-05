package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.ArrayInfo;
import com.sun.xml.internal.bind.v2.model.core.BuiltinLeafInfo;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeTypeInfoSet.class */
public interface RuntimeTypeInfoSet extends TypeInfoSet<Type, Class, Field, Method> {
    @Override // 
    Map<? extends Type, ? extends ArrayInfo<Type, Class>> arrays();

    @Override // 
    Map<Class, ? extends ClassInfo<Type, Class>> beans();

    @Override // 
    Map<Type, ? extends BuiltinLeafInfo<Type, Class>> builtins();

    @Override // 
    Map<Class, ? extends EnumLeafInfo<Type, Class>> enums();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // 
    RuntimeNonElement getTypeInfo(Type type);

    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfoSet, com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet
    /* renamed from: getAnyTypeInfo, reason: merged with bridge method [inline-methods] */
    NonElement<Type, Class> getAnyTypeInfo2();

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // 
    RuntimeNonElement getClassInfo(Class cls);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // 
    RuntimeElementInfo getElementInfo(Class cls, QName qName);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // 
    Map<QName, ? extends RuntimeElementInfo> getElementMappings(Class cls);

    @Override // 
    Iterable<? extends ElementInfo<Type, Class>> getAllElements();
}
