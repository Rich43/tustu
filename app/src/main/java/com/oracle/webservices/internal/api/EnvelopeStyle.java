package com.oracle.webservices.internal.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

@WebServiceFeatureAnnotation(id = "", bean = EnvelopeStyleFeature.class)
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/oracle/webservices/internal/api/EnvelopeStyle.class */
public @interface EnvelopeStyle {
    Style[] style() default {Style.SOAP11};

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/EnvelopeStyle$Style.class */
    public enum Style {
        SOAP11(SOAPBinding.SOAP11HTTP_BINDING),
        SOAP12("http://www.w3.org/2003/05/soap/bindings/HTTP/"),
        XML(HTTPBinding.HTTP_BINDING);

        public final String bindingId;

        Style(String id) {
            this.bindingId = id;
        }

        public boolean isSOAP11() {
            return equals(SOAP11);
        }

        public boolean isSOAP12() {
            return equals(SOAP12);
        }

        public boolean isXML() {
            return equals(XML);
        }
    }
}
