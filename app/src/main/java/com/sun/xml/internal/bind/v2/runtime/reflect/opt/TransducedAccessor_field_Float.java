package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.DatatypeConverterImpl;
import com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/TransducedAccessor_field_Float.class */
public final class TransducedAccessor_field_Float extends DefaultTransducedAccessor {
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public String print(Object o2) {
        return DatatypeConverterImpl._printFloat(((Bean) o2).f_float);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public void parse(Object o2, CharSequence lexical) {
        ((Bean) o2).f_float = DatatypeConverterImpl._parseFloat(lexical);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public boolean hasValue(Object o2) {
        return true;
    }
}
