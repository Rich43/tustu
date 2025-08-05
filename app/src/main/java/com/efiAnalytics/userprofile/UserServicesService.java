package com.efianalytics.userprofile;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "UserServicesService", targetNamespace = "http://userprofile.efiAnalytics.com/", wsdlLocation = "https://www.shadowtuner.com/efiaServices/UserServices?wsdl")
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/UserServicesService.class */
public class UserServicesService extends Service {
    private static final WebServiceException USERSERVICESSERVICE_EXCEPTION;
    private static final QName USERSERVICESSERVICE_QNAME = new QName("http://userprofile.efiAnalytics.com/", "UserServicesService");
    private static final URL USERSERVICESSERVICE_WSDL_LOCATION = UserServicesService.class.getResource("https://www.shadowtuner.com/efiaServices/UserServices?wsdl");

    static {
        WebServiceException e2 = null;
        if (USERSERVICESSERVICE_WSDL_LOCATION == null) {
            e2 = new WebServiceException("Cannot find 'https://www.shadowtuner.com/efiaServices/UserServices?wsdl' wsdl. Place the resource correctly in the classpath.");
        }
        USERSERVICESSERVICE_EXCEPTION = e2;
    }

    public UserServicesService() {
        super(__getWsdlLocation(), USERSERVICESSERVICE_QNAME);
    }

    public UserServicesService(WebServiceFeature... features) {
        super(__getWsdlLocation(), USERSERVICESSERVICE_QNAME, features);
    }

    public UserServicesService(URL wsdlLocation) {
        super(wsdlLocation, USERSERVICESSERVICE_QNAME);
    }

    public UserServicesService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, USERSERVICESSERVICE_QNAME, features);
    }

    public UserServicesService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UserServicesService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    @WebEndpoint(name = "UserServicesPort")
    public UserServices getUserServicesPort() {
        return (UserServices) super.getPort(new QName("http://userprofile.efiAnalytics.com/", "UserServicesPort"), UserServices.class);
    }

    @WebEndpoint(name = "UserServicesPort")
    public UserServices getUserServicesPort(WebServiceFeature... features) {
        return (UserServices) super.getPort(new QName("http://userprofile.efiAnalytics.com/", "UserServicesPort"), UserServices.class, features);
    }

    private static URL __getWsdlLocation() {
        if (USERSERVICESSERVICE_EXCEPTION != null) {
            throw USERSERVICESSERVICE_EXCEPTION;
        }
        return USERSERVICESSERVICE_WSDL_LOCATION;
    }
}
