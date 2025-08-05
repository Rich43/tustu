package com.oracle.webservices.internal.api.databinding;

import java.io.File;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/WSDLGenerator.class */
public interface WSDLGenerator {
    WSDLGenerator inlineSchema(boolean z2);

    WSDLGenerator property(String str, Object obj);

    void generate(WSDLResolver wSDLResolver);

    void generate(File file, String str);
}
