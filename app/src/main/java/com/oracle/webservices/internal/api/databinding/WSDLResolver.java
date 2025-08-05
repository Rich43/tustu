package com.oracle.webservices.internal.api.databinding;

import javax.xml.transform.Result;
import javax.xml.ws.Holder;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/WSDLResolver.class */
public interface WSDLResolver {
    Result getWSDL(String str);

    Result getAbstractWSDL(Holder<String> holder);

    Result getSchemaOutput(String str, Holder<String> holder);
}
