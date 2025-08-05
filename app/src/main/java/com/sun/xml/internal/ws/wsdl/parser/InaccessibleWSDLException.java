package com.sun.xml.internal.ws.wsdl.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/InaccessibleWSDLException.class */
public class InaccessibleWSDLException extends WebServiceException {
    private final List<Throwable> errors;
    private static final long serialVersionUID = 1;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !InaccessibleWSDLException.class.desiredAssertionStatus();
    }

    public InaccessibleWSDLException(List<Throwable> errors) {
        super(errors.size() + " counts of InaccessibleWSDLException.\n");
        if (!$assertionsDisabled && errors.isEmpty()) {
            throw new AssertionError((Object) "there must be at least one error");
        }
        this.errors = Collections.unmodifiableList(new ArrayList(errors));
    }

    @Override // java.lang.Throwable
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append('\n');
        for (Throwable error : this.errors) {
            sb.append(error.toString()).append('\n');
        }
        return sb.toString();
    }

    public List<Throwable> getErrors() {
        return this.errors;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/parser/InaccessibleWSDLException$Builder.class */
    public static class Builder implements ErrorHandler {
        private final List<Throwable> list = new ArrayList();

        @Override // com.sun.xml.internal.ws.wsdl.parser.ErrorHandler
        public void error(Throwable e2) {
            this.list.add(e2);
        }

        public void check() throws InaccessibleWSDLException {
            if (this.list.isEmpty()) {
            } else {
                throw new InaccessibleWSDLException(this.list);
            }
        }
    }
}
