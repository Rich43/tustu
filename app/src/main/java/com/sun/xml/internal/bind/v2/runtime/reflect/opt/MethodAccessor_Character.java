package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/MethodAccessor_Character.class */
public class MethodAccessor_Character extends Accessor {
    public MethodAccessor_Character() {
        super(Character.class);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return Character.valueOf(((Bean) bean).get_char());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).set_char(value == null ? Const.default_value_char : ((Character) value).charValue());
    }
}
