package com.efianalytics.reviews;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@XmlSeeAlso({ObjectFactory.class})
@WebService(name = "UserReview", targetNamespace = "http://reviews.efiAnalytics.com/")
/* loaded from: efiaServicesClient.jar:com/efianalytics/reviews/UserReview.class */
public interface UserReview {
    @Action(input = "http://reviews.efiAnalytics.com/UserReview/submitUserReviewRequest", output = "http://reviews.efiAnalytics.com/UserReview/submitUserReviewResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "submitUserReview", targetNamespace = "http://reviews.efiAnalytics.com/", className = "com.efianalytics.reviews.SubmitUserReview")
    @ResponseWrapper(localName = "submitUserReviewResponse", targetNamespace = "http://reviews.efiAnalytics.com/", className = "com.efianalytics.reviews.SubmitUserReviewResponse")
    @WebMethod
    String submitUserReview(@WebParam(name = "uid", targetNamespace = "") String str, @WebParam(name = "email", targetNamespace = "") String str2, @WebParam(name = "user_id", targetNamespace = "") String str3, @WebParam(name = "app_name", targetNamespace = "") String str4, @WebParam(name = "app_edition", targetNamespace = "") String str5, @WebParam(name = "app_version", targetNamespace = "") String str6, @WebParam(name = "rating", targetNamespace = "") int i2, @WebParam(name = "review", targetNamespace = "") String str7);
}
