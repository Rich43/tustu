package com.sun.xml.internal.ws.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/BindingIDFactory.class */
public abstract class BindingIDFactory {
    @Nullable
    public abstract BindingID parse(@NotNull String str) throws WebServiceException;

    @Nullable
    public BindingID create(@NotNull String transport, @NotNull SOAPVersion soapVersion) throws WebServiceException {
        return null;
    }
}
