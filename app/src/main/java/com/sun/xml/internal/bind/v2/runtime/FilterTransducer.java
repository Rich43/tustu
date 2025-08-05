package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.AccessorException;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/FilterTransducer.class */
public abstract class FilterTransducer<T> implements Transducer<T> {
    protected final Transducer<T> core;

    protected FilterTransducer(Transducer<T> core) {
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public final boolean isDefault() {
        return false;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public boolean useNamespace() {
        return this.core.useNamespace();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public void declareNamespace(T o2, XMLSerializer w2) throws AccessorException {
        this.core.declareNamespace(o2, w2);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    @NotNull
    public CharSequence print(@NotNull T o2) throws AccessorException {
        return this.core.print(o2);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public T parse(CharSequence lexical) throws AccessorException, SAXException {
        return this.core.parse(lexical);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeText(XMLSerializer w2, T o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
        this.core.writeText(w2, o2, fieldName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeLeafElement(XMLSerializer w2, Name tagName, T o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
        this.core.writeLeafElement(w2, tagName, o2, fieldName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.Transducer
    public QName getTypeName(T instance) {
        return null;
    }
}
