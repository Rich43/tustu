package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/FieldAccessor_Ref.class */
public class FieldAccessor_Ref extends Accessor {
    public FieldAccessor_Ref() {
        super(Ref.class);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public Object get(Object bean) {
        return ((Bean) bean).f_ref;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.Accessor
    public void set(Object bean, Object value) {
        ((Bean) bean).f_ref = (Ref) value;
    }
}
