package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/FieldAccessor_Long.class */
public class FieldAccessor_Long extends Accessor {
    public FieldAccessor_Long() {
        super(Long.class);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Long.valueOf(((Bean) bean).f_long);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).f_long = value == null ? Const.default_value_long : ((Long) value).longValue();
    }
}
