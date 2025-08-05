package com.oracle.webservices.internal.api.databinding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

@WebServiceFeatureAnnotation(id = "", bean = DatabindingModeFeature.class)
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/DatabindingMode.class */
public @interface DatabindingMode {
    String value();
}
