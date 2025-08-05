package com.sun.xml.internal.ws.api.wsdl.parser;

import com.sun.istack.internal.NotNull;
import java.util.List;
import javax.xml.transform.Source;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/wsdl/parser/ServiceDescriptor.class */
public abstract class ServiceDescriptor {
    @NotNull
    public abstract List<? extends Source> getWSDLs();

    @NotNull
    public abstract List<? extends Source> getSchemas();
}
