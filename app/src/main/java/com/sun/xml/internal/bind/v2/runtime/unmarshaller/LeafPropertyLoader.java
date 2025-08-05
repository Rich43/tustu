package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/LeafPropertyLoader.class */
public class LeafPropertyLoader extends Loader {
    private final TransducedAccessor xacc;

    public LeafPropertyLoader(TransducedAccessor xacc) {
        super(true);
        this.xacc = xacc;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
        try {
            this.xacc.parse(state.getPrev().getTarget(), text);
        } catch (AccessorException e2) {
            handleGenericException(e2, true);
        } catch (RuntimeException e3) {
            handleParseConversionException(state, e3);
        }
    }
}
