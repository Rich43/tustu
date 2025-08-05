package com.efianalytics.reviews;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "UserReviewService", targetNamespace = "http://reviews.efiAnalytics.com/", wsdlLocation = "http://services.efianalytics.com:80/efiaServices/UserReview?wsdl")
/* loaded from: efiaServicesClient.jar:com/efianalytics/reviews/UserReviewService.class */
public class UserReviewService extends Service {
    private static final URL USERREVIEWSERVICE_WSDL_LOCATION;
    private static final WebServiceException USERREVIEWSERVICE_EXCEPTION;
    private static final QName USERREVIEWSERVICE_QNAME = new QName("http://reviews.efiAnalytics.com/", "UserReviewService");

    static {
        URL url = null;
        WebServiceException e2 = null;
        try {
            url = new URL("http://services.efianalytics.com:80/efiaServices/UserReview?wsdl");
        } catch (MalformedURLException ex) {
            e2 = new WebServiceException(ex);
        }
        USERREVIEWSERVICE_WSDL_LOCATION = url;
        USERREVIEWSERVICE_EXCEPTION = e2;
    }

    public UserReviewService() {
        super(__getWsdlLocation(), USERREVIEWSERVICE_QNAME);
    }

    public UserReviewService(WebServiceFeature... features) {
        super(__getWsdlLocation(), USERREVIEWSERVICE_QNAME, features);
    }

    public UserReviewService(URL wsdlLocation) {
        super(wsdlLocation, USERREVIEWSERVICE_QNAME);
    }

    public UserReviewService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, USERREVIEWSERVICE_QNAME, features);
    }

    public UserReviewService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public UserReviewService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    @WebEndpoint(name = "UserReviewPort")
    public UserReview getUserReviewPort() {
        return (UserReview) super.getPort(new QName("http://reviews.efiAnalytics.com/", "UserReviewPort"), UserReview.class);
    }

    @WebEndpoint(name = "UserReviewPort")
    public UserReview getUserReviewPort(WebServiceFeature... features) {
        return (UserReview) super.getPort(new QName("http://reviews.efiAnalytics.com/", "UserReviewPort"), UserReview.class, features);
    }

    private static URL __getWsdlLocation() {
        if (USERREVIEWSERVICE_EXCEPTION != null) {
            throw USERREVIEWSERVICE_EXCEPTION;
        }
        return USERREVIEWSERVICE_WSDL_LOCATION;
    }
}
