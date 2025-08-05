package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.api.AccessorException;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/SchemaTypeTransducer.class */
public class SchemaTypeTransducer<V> extends FilterTransducer<V> {
    private final QName schemaType;

    public SchemaTypeTransducer(Transducer<V> core, QName schemaType) {
        super(core);
        this.schemaType = schemaType;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
    public CharSequence print(V o2) throws AccessorException {
        XMLSerializer w2 = XMLSerializer.getInstance();
        QName old = w2.setSchemaType(this.schemaType);
        try {
            CharSequence charSequencePrint = this.core.print(o2);
            w2.setSchemaType(old);
            return charSequencePrint;
        } catch (Throwable th) {
            w2.setSchemaType(old);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeText(XMLSerializer w2, V o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
        QName old = w2.setSchemaType(this.schemaType);
        try {
            this.core.writeText(w2, o2, fieldName);
            w2.setSchemaType(old);
        } catch (Throwable th) {
            w2.setSchemaType(old);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeLeafElement(XMLSerializer w2, Name tagName, V o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
        QName old = w2.setSchemaType(this.schemaType);
        try {
            this.core.writeLeafElement(w2, tagName, o2, fieldName);
            w2.setSchemaType(old);
        } catch (Throwable th) {
            w2.setSchemaType(old);
            throw th;
        }
    }
}
