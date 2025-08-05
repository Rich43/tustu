package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import javax.xml.bind.annotation.DomHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/WildcardLoader.class */
public final class WildcardLoader extends ProxyLoader {
    private final DomLoader dom;
    private final WildcardMode mode;

    public WildcardLoader(DomHandler dom, WildcardMode mode) {
        this.dom = new DomLoader(dom);
        this.mode = mode;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.ProxyLoader
    protected Loader selectLoader(UnmarshallingContext.State state, TagName tag) throws SAXException {
        Loader l2;
        UnmarshallingContext context = state.getContext();
        if (this.mode.allowTypedObject && (l2 = context.selectRootLoader(state, tag)) != null) {
            return l2;
        }
        if (this.mode.allowDom) {
            return this.dom;
        }
        return Discarder.INSTANCE;
    }
}
