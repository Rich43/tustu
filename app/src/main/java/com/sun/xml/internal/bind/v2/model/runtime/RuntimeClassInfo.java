package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeClassInfo.class */
public interface RuntimeClassInfo extends ClassInfo<Type, Class>, RuntimeNonElement {
    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    /* renamed from: getBaseClass */
    ClassInfo<Type, Class> getBaseClass2();

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    List<? extends PropertyInfo<Type, Class>> getProperties();

    @Override // com.sun.xml.internal.bind.v2.model.core.ClassInfo
    /* renamed from: getProperty */
    PropertyInfo<Type, Class> getProperty2(String str);

    Method getFactoryMethod();

    <BeanT> Accessor<BeanT, Map<QName, String>> getAttributeWildcard();

    <BeanT> Accessor<BeanT, Locator> getLocatorField();
}
