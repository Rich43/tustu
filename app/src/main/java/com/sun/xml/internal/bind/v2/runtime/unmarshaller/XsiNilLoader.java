package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.DatatypeConverterImpl;
import com.sun.xml.internal.bind.api.AccessorException;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.util.Collection;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/XsiNilLoader.class */
public class XsiNilLoader extends ProxyLoader {
    private final Loader defaultLoader;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XsiNilLoader.class.desiredAssertionStatus();
    }

    public XsiNilLoader(Loader defaultLoader) {
        this.defaultLoader = defaultLoader;
        if (!$assertionsDisabled && defaultLoader == null) {
            throw new AssertionError();
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.ProxyLoader
    protected Loader selectLoader(UnmarshallingContext.State state, TagName ea) throws SAXException {
        Boolean b2;
        int idx = ea.atts.getIndex("http://www.w3.org/2001/XMLSchema-instance", "nil");
        if (idx != -1 && (b2 = DatatypeConverterImpl._parseBoolean(ea.atts.getValue(idx))) != null && b2.booleanValue()) {
            onNil(state);
            boolean hasOtherAttributes = ea.atts.getLength() - 1 > 0;
            if (!hasOtherAttributes || !(state.getPrev().getTarget() instanceof JAXBElement)) {
                return Discarder.INSTANCE;
            }
        }
        return this.defaultLoader;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public Collection<QName> getExpectedChildElements() {
        return this.defaultLoader.getExpectedChildElements();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public Collection<QName> getExpectedAttributes() {
        return this.defaultLoader.getExpectedAttributes();
    }

    protected void onNil(UnmarshallingContext.State state) throws SAXException {
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/XsiNilLoader$Single.class */
    public static final class Single extends XsiNilLoader {
        private final Accessor acc;

        public Single(Loader l2, Accessor acc) {
            super(l2);
            this.acc = acc;
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader
        protected void onNil(UnmarshallingContext.State state) throws SAXException {
            try {
                this.acc.set(state.getPrev().getTarget(), null);
                state.getPrev().setNil(true);
            } catch (AccessorException e2) {
                handleGenericException(e2, true);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/XsiNilLoader$Array.class */
    public static final class Array extends XsiNilLoader {
        public Array(Loader core) {
            super(core);
        }

        @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader
        protected void onNil(UnmarshallingContext.State state) {
            state.setTarget(null);
        }
    }
}
