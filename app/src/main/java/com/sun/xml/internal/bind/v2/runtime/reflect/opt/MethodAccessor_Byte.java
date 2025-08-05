package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/MethodAccessor_Byte.class */
public class MethodAccessor_Byte extends Accessor {
    public MethodAccessor_Byte() {
        super(Byte.class);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Byte.valueOf(((Bean) bean).get_byte());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).set_byte(value == null ? Const.default_value_byte : ((Byte) value).byteValue());
    }
}
