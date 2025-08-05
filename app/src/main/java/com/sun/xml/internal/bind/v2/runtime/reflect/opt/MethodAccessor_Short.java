package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/MethodAccessor_Short.class */
public class MethodAccessor_Short extends Accessor {
    public MethodAccessor_Short() {
        super(Short.class);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Short.valueOf(((Bean) bean).get_short());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).set_short(value == null ? Const.default_value_short : ((Short) value).shortValue());
    }
}
