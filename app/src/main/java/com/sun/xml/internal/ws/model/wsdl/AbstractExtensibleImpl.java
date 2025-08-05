package com.sun.xml.internal.ws.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLObject;
import com.sun.xml.internal.ws.resources.UtilMessages;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceException;
import org.xml.sax.Locator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/AbstractExtensibleImpl.class */
abstract class AbstractExtensibleImpl extends AbstractObjectImpl implements WSDLExtensible {
    protected final Set<WSDLExtension> extensions;
    protected List<UnknownWSDLExtension> notUnderstoodExtensions;

    protected AbstractExtensibleImpl(XMLStreamReader xsr) {
        super(xsr);
        this.extensions = new HashSet();
        this.notUnderstoodExtensions = new ArrayList();
    }

    protected AbstractExtensibleImpl(String systemId, int lineNumber) {
        super(systemId, lineNumber);
        this.extensions = new HashSet();
        this.notUnderstoodExtensions = new ArrayList();
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public final Iterable<WSDLExtension> getExtensions() {
        return this.extensions;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public final <T extends WSDLExtension> Iterable<T> getExtensions(Class<T> type) {
        List<T> r2 = new ArrayList<>(this.extensions.size());
        for (WSDLExtension e2 : this.extensions) {
            if (type.isInstance(e2)) {
                r2.add(type.cast(e2));
            }
        }
        return r2;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public <T extends WSDLExtension> T getExtension(Class<T> type) {
        for (WSDLExtension e2 : this.extensions) {
            if (type.isInstance(e2)) {
                return type.cast(e2);
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public void addExtension(WSDLExtension ex) {
        if (ex == null) {
            throw new IllegalArgumentException();
        }
        this.extensions.add(ex);
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public List<? extends UnknownWSDLExtension> getNotUnderstoodExtensions() {
        return this.notUnderstoodExtensions;
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public void addNotUnderstoodExtension(QName extnEl, Locator locator) {
        this.notUnderstoodExtensions.add(new UnknownWSDLExtension(extnEl, locator));
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/model/wsdl/AbstractExtensibleImpl$UnknownWSDLExtension.class */
    protected static class UnknownWSDLExtension implements WSDLExtension, WSDLObject {
        private final QName extnEl;
        private final Locator locator;

        public UnknownWSDLExtension(QName extnEl, Locator locator) {
            this.extnEl = extnEl;
            this.locator = locator;
        }

        @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLExtension
        public QName getName() {
            return this.extnEl;
        }

        @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLObject
        @NotNull
        public Locator getLocation() {
            return this.locator;
        }

        public String toString() {
            return ((Object) this.extnEl) + " " + UtilMessages.UTIL_LOCATION(Integer.valueOf(this.locator.getLineNumber()), this.locator.getSystemId());
        }
    }

    @Override // com.sun.xml.internal.ws.api.model.wsdl.WSDLExtensible
    public boolean areRequiredExtensionsUnderstood() {
        if (this.notUnderstoodExtensions.size() != 0) {
            StringBuilder buf = new StringBuilder("Unknown WSDL extensibility elements:");
            for (UnknownWSDLExtension extn : this.notUnderstoodExtensions) {
                buf.append('\n').append(extn.toString());
            }
            throw new WebServiceException(buf.toString());
        }
        return true;
    }
}
