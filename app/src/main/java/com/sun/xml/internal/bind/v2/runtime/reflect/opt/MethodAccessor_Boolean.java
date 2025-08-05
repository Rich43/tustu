package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/MethodAccessor_Boolean.class */
public class MethodAccessor_Boolean extends Accessor {
    public MethodAccessor_Boolean() {
        super(Boolean.class);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Boolean.valueOf(((Bean) bean).get_boolean());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).set_boolean(value == null ? Const.default_value_boolean : ((Boolean) value).booleanValue());
    }
}
