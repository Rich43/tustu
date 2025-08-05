package com.sun.xml.internal.bind.v2.runtime.reflect;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/DefaultTransducedAccessor.class */
public abstract class DefaultTransducedAccessor<T> extends TransducedAccessor<T> {
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public abstract String print(T t2) throws AccessorException, SAXException;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public /* bridge */ /* synthetic */ CharSequence print(Object obj) throws AccessorException, SAXException {
        return print((DefaultTransducedAccessor<T>) obj);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public void writeLeafElement(XMLSerializer w2, Name tagName, T o2, String fieldName) throws AccessorException, XMLStreamException, SAXException, IOException {
        w2.leafElement(tagName, print((DefaultTransducedAccessor<T>) o2), fieldName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor
    public void writeText(XMLSerializer w2, T o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
        w2.text(print((DefaultTransducedAccessor<T>) o2), fieldName);
    }
}
