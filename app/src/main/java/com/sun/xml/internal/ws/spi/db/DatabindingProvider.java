package com.sun.xml.internal.ws.spi.db;

import com.oracle.webservices.internal.api.databinding.Databinding;
import com.oracle.webservices.internal.api.databinding.WSDLGenerator;
import com.sun.xml.internal.ws.api.databinding.DatabindingConfig;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/DatabindingProvider.class */
public interface DatabindingProvider {
    boolean isFor(String str);

    void init(Map<String, Object> map);

    Databinding create(DatabindingConfig databindingConfig);

    WSDLGenerator wsdlGen(DatabindingConfig databindingConfig);
}
