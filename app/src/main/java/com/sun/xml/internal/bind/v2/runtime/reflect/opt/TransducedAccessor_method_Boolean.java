package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.DatatypeConverterImpl;
import com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/TransducedAccessor_method_Boolean.class */
public final class TransducedAccessor_method_Boolean extends DefaultTransducedAccessor {
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public String print(Object o2) {
        return DatatypeConverterImpl._printBoolean(((Bean) o2).get_boolean());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public void parse(Object o2, CharSequence lexical) {
        ((Bean) o2).set_boolean(DatatypeConverterImpl._parseBoolean(lexical).booleanValue());
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public boolean hasValue(Object o2) {
        return true;
    }
}
