package com.sun.xml.internal.ws.api.wsdl.parser;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.net.URI;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/wsdl/parser/MetaDataResolver.class */
public abstract class MetaDataResolver {
    @Nullable
    public abstract ServiceDescriptor resolve(@NotNull URI uri);
}
