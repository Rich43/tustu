package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.DatatypeConverterImpl;
import com.sun.xml.internal.bind.v2.runtime.ClassBeanInfoImpl;
import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.util.Collection;
import javax.xml.namespace.QName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/LeafPropertyXsiLoader.class */
public final class LeafPropertyXsiLoader extends Loader {
    private final Loader defaultLoader;
    private final TransducedAccessor xacc;
    private final Accessor acc;

    public LeafPropertyXsiLoader(Loader defaultLoader, TransducedAccessor xacc, Accessor acc) {
        this.defaultLoader = defaultLoader;
        this.expectText = true;
        this.xacc = xacc;
        this.acc = acc;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader
    public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        Loader loader = selectLoader(state, ea);
        state.setLoader(loader);
        loader.startElement(state, ea);
    }

    protected Loader selectLoader(UnmarshallingContext.State state, TagName ea) throws SAXException {
        UnmarshallingContext context = state.getContext();
        Attributes atts = ea.atts;
        int idx = atts.getIndex("http://www.w3.org/2001/XMLSchema-instance", "type");
        if (idx >= 0) {
            String value = atts.getValue(idx);
            QName type = DatatypeConverterImpl._parseQName(value, context);
            if (type == null) {
                return this.defaultLoader;
            }
            JaxBeanInfo beanInfo = context.getJAXBContext().getGlobalType(type);
            if (beanInfo == null) {
                return this.defaultLoader;
            }
            try {
                ClassBeanInfoImpl cbii = (ClassBeanInfoImpl) beanInfo;
                if (null == cbii.getTransducer()) {
                    return this.defaultLoader;
                }
                return new LeafPropertyLoader(new TransducedAccessor.CompositeTransducedAccessorImpl(state.getContext().getJAXBContext(), cbii.getTransducer(), this.acc));
            } catch (ClassCastException e2) {
                return this.defaultLoader;
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
}
