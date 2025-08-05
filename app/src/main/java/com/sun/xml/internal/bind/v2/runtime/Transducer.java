package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.bind.api.AccessorException;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/Transducer.class */
public interface Transducer<ValueT> {
    boolean isDefault();

    boolean useNamespace();

    void declareNamespace(ValueT valuet, XMLSerializer xMLSerializer) throws AccessorException;

    @NotNull
    CharSequence print(@NotNull ValueT valuet) throws AccessorException;

    ValueT parse(CharSequence charSequence) throws AccessorException, SAXException;

    void writeText(XMLSerializer xMLSerializer, ValueT valuet, String str) throws AccessorException, SAXException, XMLStreamException, IOException;

    void writeLeafElement(XMLSerializer xMLSerializer, Name name, @NotNull ValueT valuet, String str) throws AccessorException, SAXException, XMLStreamException, IOException;

    QName getTypeName(@NotNull ValueT valuet);
}
