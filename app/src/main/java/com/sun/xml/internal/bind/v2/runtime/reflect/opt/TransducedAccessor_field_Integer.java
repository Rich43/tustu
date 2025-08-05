package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.DatatypeConverterImpl;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/TransducedAccessor_field_Integer.class */
public final class TransducedAccessor_field_Integer extends DefaultTransducedAccessor {
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public String print(Object o2) {
        return DatatypeConverterImpl._printInt(((Bean) o2).f_int);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public void parse(Object o2, CharSequence lexical) {
        ((Bean) o2).f_int = DatatypeConverterImpl._parseInt(lexical);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public boolean hasValue(Object o2) {
        return true;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor, com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public void writeLeafElement(XMLSerializer w2, Name tagName, Object o2, String fieldName) throws AccessorException, XMLStreamException, SAXException, IOException {
        w2.leafElement(tagName, ((Bean) o2).f_int, fieldName);
    }
}
