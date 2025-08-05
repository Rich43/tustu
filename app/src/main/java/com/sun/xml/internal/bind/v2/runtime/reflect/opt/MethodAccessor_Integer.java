package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/MethodAccessor_Integer.class */
public class MethodAccessor_Integer extends Accessor {
    public MethodAccessor_Integer() {
        super(Integer.class);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Integer.valueOf(((Bean) bean).get_int());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).set_int(value == null ? Const.default_value_int : ((Integer) value).intValue());
    }
}
