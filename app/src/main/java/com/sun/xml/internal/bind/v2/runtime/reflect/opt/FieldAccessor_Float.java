package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/FieldAccessor_Float.class */
public class FieldAccessor_Float extends Accessor {
    public FieldAccessor_Float() {
        super(Float.class);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Float.valueOf(((Bean) bean).f_float);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).f_float = value == null ? Const.default_value_float : ((Float) value).floatValue();
    }
}
