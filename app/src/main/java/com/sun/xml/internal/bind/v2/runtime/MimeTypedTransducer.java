package com.sun.xml.internal.bind.v2.runtime;

import com.sun.xml.internal.bind.api.AccessorException;
import java.io.IOException;
import javax.activation.MimeType;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/MimeTypedTransducer.class */
public final class MimeTypedTransducer<V> extends FilterTransducer<V> {
    private final MimeType expectedMimeType;

    public MimeTypedTransducer(Transducer<V> core, MimeType expectedMimeType) {
        super(core);
        this.expectedMimeType = expectedMimeType;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
    public CharSequence print(V o2) throws AccessorException {
        XMLSerializer w2 = XMLSerializer.getInstance();
        MimeType old = w2.setExpectedMimeType(this.expectedMimeType);
        try {
            CharSequence charSequencePrint = this.core.print(o2);
            w2.setExpectedMimeType(old);
            return charSequencePrint;
        } catch (Throwable th) {
            w2.setExpectedMimeType(old);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeText(XMLSerializer w2, V o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
        MimeType old = w2.setExpectedMimeType(this.expectedMimeType);
        try {
            this.core.writeText(w2, o2, fieldName);
            w2.setExpectedMimeType(old);
        } catch (Throwable th) {
            w2.setExpectedMimeType(old);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeLeafElement(XMLSerializer w2, Name tagName, V o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
        MimeType old = w2.setExpectedMimeType(this.expectedMimeType);
        try {
            this.core.writeLeafElement(w2, tagName, o2, fieldName);
            w2.setExpectedMimeType(old);
        } catch (Throwable th) {
            w2.setExpectedMimeType(old);
            throw th;
        }
    }
}
