package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/TextLoader.class */
public class TextLoader extends Loader {
    private final Transducer xducer;

    public TextLoader(Transducer xducer) {
        super(true);
        this.xducer = xducer;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
        try {
            state.setTarget(this.xducer.parse(text));
        } catch (AccessorException e2) {
            handleGenericException(e2, true);
        } catch (RuntimeException e3) {
            handleParseConversionException(state, e3);
        }
    }
}
