package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/DefaultValueLoaderDecorator.class */
public final class DefaultValueLoaderDecorator extends Loader {

    /* renamed from: l, reason: collision with root package name */
    private final Loader f12072l;
    private final String defaultValue;

    public DefaultValueLoaderDecorator(Loader l2, String defaultValue) {
        this.f12072l = l2;
        this.defaultValue = defaultValue;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        if (state.getElementDefaultValue() == null) {
            state.setElementDefaultValue(this.defaultValue);
        }
        state.setLoader(this.f12072l);
        this.f12072l.startElement(state, ea);
    }
}
