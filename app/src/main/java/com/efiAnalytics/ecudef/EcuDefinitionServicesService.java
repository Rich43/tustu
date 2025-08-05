package com.efianalytics.ecudef;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "EcuDefinitionServicesService", targetNamespace = "http://ecudef.efiAnalytics.com/", wsdlLocation = "http://services.efianalytics.com:80/efiaServices/EcuDefinitionServices?wsdl")
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/EcuDefinitionServicesService.class */
public class EcuDefinitionServicesService extends Service {
    private static final URL ECUDEFINITIONSERVICESSERVICE_WSDL_LOCATION;
    private static final WebServiceException ECUDEFINITIONSERVICESSERVICE_EXCEPTION;
    private static final QName ECUDEFINITIONSERVICESSERVICE_QNAME = new QName("http://ecudef.efiAnalytics.com/", "EcuDefinitionServicesService");

    static {
        URL url = null;
        WebServiceException e2 = null;
        try {
            url = new URL("http://services.efianalytics.com:80/efiaServices/EcuDefinitionServices?wsdl");
        } catch (MalformedURLException ex) {
            e2 = new WebServiceException(ex);
        }
        ECUDEFINITIONSERVICESSERVICE_WSDL_LOCATION = url;
        ECUDEFINITIONSERVICESSERVICE_EXCEPTION = e2;
    }

    public EcuDefinitionServicesService() {
        super(__getWsdlLocation(), ECUDEFINITIONSERVICESSERVICE_QNAME);
    }

    public EcuDefinitionServicesService(WebServiceFeature... features) {
        super(__getWsdlLocation(), ECUDEFINITIONSERVICESSERVICE_QNAME, features);
    }

    public EcuDefinitionServicesService(URL wsdlLocation) {
        super(wsdlLocation, ECUDEFINITIONSERVICESSERVICE_QNAME);
    }

    public EcuDefinitionServicesService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, ECUDEFINITIONSERVICESSERVICE_QNAME, features);
    }

    public EcuDefinitionServicesService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EcuDefinitionServicesService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    @WebEndpoint(name = "EcuDefinitionServicesPort")
    public EcuDefinitionServices getEcuDefinitionServicesPort() {
        return (EcuDefinitionServices) super.getPort(new QName("http://ecudef.efiAnalytics.com/", "EcuDefinitionServicesPort"), EcuDefinitionServices.class);
    }

    @WebEndpoint(name = "EcuDefinitionServicesPort")
    public EcuDefinitionServices getEcuDefinitionServicesPort(WebServiceFeature... features) {
        return (EcuDefinitionServices) super.getPort(new QName("http://ecudef.efiAnalytics.com/", "EcuDefinitionServicesPort"), EcuDefinitionServices.class, features);
    }

    private static URL __getWsdlLocation() {
        if (ECUDEFINITIONSERVICESSERVICE_EXCEPTION != null) {
            throw ECUDEFINITIONSERVICESSERVICE_EXCEPTION;
        }
        return ECUDEFINITIONSERVICESSERVICE_WSDL_LOCATION;
    }
}
