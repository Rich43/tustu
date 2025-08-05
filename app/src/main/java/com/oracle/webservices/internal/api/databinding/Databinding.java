package com.oracle.webservices.internal.api.databinding;

import com.oracle.webservices.internal.api.message.MessageContext;
import java.lang.reflect.Method;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceFeature;
import org.xml.sax.EntityResolver;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/Databinding.class */
public interface Databinding {

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/Databinding$Builder.class */
    public interface Builder {
        Builder targetNamespace(String str);

        Builder serviceName(QName qName);

        Builder portName(QName qName);

        Builder wsdlURL(URL url);

        Builder wsdlSource(Source source);

        Builder entityResolver(EntityResolver entityResolver);

        Builder classLoader(ClassLoader classLoader);

        Builder feature(WebServiceFeature... webServiceFeatureArr);

        Builder property(String str, Object obj);

        Databinding build();

        WSDLGenerator createWSDLGenerator();
    }

    JavaCallInfo createJavaCallInfo(Method method, Object[] objArr);

    MessageContext serializeRequest(JavaCallInfo javaCallInfo);

    JavaCallInfo deserializeResponse(MessageContext messageContext, JavaCallInfo javaCallInfo);

    JavaCallInfo deserializeRequest(MessageContext messageContext);

    MessageContext serializeResponse(JavaCallInfo javaCallInfo);
}
