package com.efianalytics.translation;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "TranslationServicesService", targetNamespace = "http://translation.efiAnalytics.com/", wsdlLocation = "https://www.shadowtuner.com/efiaServices/TranslationServices?wsdl")
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/TranslationServicesService.class */
public class TranslationServicesService extends Service {
    private static final WebServiceException TRANSLATIONSERVICESSERVICE_EXCEPTION;
    private static final QName TRANSLATIONSERVICESSERVICE_QNAME = new QName("http://translation.efiAnalytics.com/", "TranslationServicesService");
    private static final URL TRANSLATIONSERVICESSERVICE_WSDL_LOCATION = TranslationServicesService.class.getResource("https://www.shadowtuner.com/efiaServices/TranslationServices?wsdl");

    static {
        WebServiceException e2 = null;
        if (TRANSLATIONSERVICESSERVICE_WSDL_LOCATION == null) {
            e2 = new WebServiceException("Cannot find 'https://www.shadowtuner.com/efiaServices/TranslationServices?wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        TRANSLATIONSERVICESSERVICE_EXCEPTION = e2;
    }

    public TranslationServicesService() {
        super(__getWsdlLocation(), TRANSLATIONSERVICESSERVICE_QNAME);
    }

    public TranslationServicesService(WebServiceFeature... features) {
        super(__getWsdlLocation(), TRANSLATIONSERVICESSERVICE_QNAME, features);
    }

    public TranslationServicesService(URL wsdlLocation) {
        super(wsdlLocation, TRANSLATIONSERVICESSERVICE_QNAME);
    }

    public TranslationServicesService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, TRANSLATIONSERVICESSERVICE_QNAME, features);
    }

    public TranslationServicesService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TranslationServicesService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    @WebEndpoint(name = "TranslationServicesPort")
    public TranslationServices getTranslationServicesPort() {
        return (TranslationServices) super.getPort(new QName("http://translation.efiAnalytics.com/", "TranslationServicesPort"), TranslationServices.class);
    }

    @WebEndpoint(name = "TranslationServicesPort")
    public TranslationServices getTranslationServicesPort(WebServiceFeature... features) {
        return (TranslationServices) super.getPort(new QName("http://translation.efiAnalytics.com/", "TranslationServicesPort"), TranslationServices.class, features);
    }

    private static URL __getWsdlLocation() {
        if (TRANSLATIONSERVICESSERVICE_EXCEPTION != null) {
            throw TRANSLATIONSERVICESSERVICE_EXCEPTION;
        }
        return TRANSLATIONSERVICESSERVICE_WSDL_LOCATION;
    }
}
