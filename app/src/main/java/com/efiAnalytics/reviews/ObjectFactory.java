package com.efianalytics.reviews;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
/* loaded from: efiaServicesClient.jar:com/efianalytics/reviews/ObjectFactory.class */
public class ObjectFactory {
    private static final QName _SubmitUserReview_QNAME = new QName("http://reviews.efiAnalytics.com/", "submitUserReview");
    private static final QName _SubmitUserReviewResponse_QNAME = new QName("http://reviews.efiAnalytics.com/", "submitUserReviewResponse");

    public SubmitUserReview createSubmitUserReview() {
        return new SubmitUserReview();
    }

    public SubmitUserReviewResponse createSubmitUserReviewResponse() {
        return new SubmitUserReviewResponse();
    }

    @XmlElementDecl(namespace = "http://reviews.efiAnalytics.com/", name = "submitUserReview")
    public JAXBElement<SubmitUserReview> createSubmitUserReview(SubmitUserReview value) {
        return new JAXBElement<>(_SubmitUserReview_QNAME, SubmitUserReview.class, null, value);
    }

    @XmlElementDecl(namespace = "http://reviews.efiAnalytics.com/", name = "submitUserReviewResponse")
    public JAXBElement<SubmitUserReviewResponse> createSubmitUserReviewResponse(SubmitUserReviewResponse value) {
        return new JAXBElement<>(_SubmitUserReviewResponse_QNAME, SubmitUserReviewResponse.class, null, value);
    }
}
