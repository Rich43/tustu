package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/FieldAccessor_Boolean.class */
public class FieldAccessor_Boolean extends Accessor {
    public FieldAccessor_Boolean() {
        super(Boolean.class);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Boolean.valueOf(((Bean) bean).f_boolean);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).f_boolean = value == null ? Const.default_value_boolean : ((Boolean) value).booleanValue();
    }
}
