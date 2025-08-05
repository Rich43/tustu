package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.AccessorException;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/InlineBinaryTransducer.class */
public class InlineBinaryTransducer<V> extends FilterTransducer<V> {
    public InlineBinaryTransducer(Transducer<V> core) {
        super(core);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
    @NotNull
    public CharSequence print(@NotNull V o2) throws AccessorException {
        XMLSerializer w2 = XMLSerializer.getInstance();
        boolean old = w2.setInlineBinaryFlag(true);
        try {
            CharSequence charSequencePrint = this.core.print(o2);
            w2.setInlineBinaryFlag(old);
            return charSequencePrint;
        } catch (Throwable th) {
            w2.setInlineBinaryFlag(old);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeText(XMLSerializer w2, V o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
        boolean old = w2.setInlineBinaryFlag(true);
        try {
            this.core.writeText(w2, o2, fieldName);
            w2.setInlineBinaryFlag(old);
        } catch (Throwable th) {
            w2.setInlineBinaryFlag(old);
            throw th;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.FilterTransducer, com.sun.xml.internal.bind.v2.runtime.Transducer
    public void writeLeafElement(XMLSerializer w2, Name tagName, V o2, String fieldName) throws AccessorException, SAXException, XMLStreamException, IOException {
        boolean old = w2.setInlineBinaryFlag(true);
        try {
            this.core.writeLeafElement(w2, tagName, o2, fieldName);
            w2.setInlineBinaryFlag(old);
        } catch (Throwable th) {
            w2.setInlineBinaryFlag(old);
            throw th;
        }
    }
}
