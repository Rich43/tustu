package com.efianalytics.translation;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@XmlSeeAlso({ObjectFactory.class})
@WebService(name = "TranslationServices", targetNamespace = "http://translation.efiAnalytics.com/")
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/TranslationServices.class */
public interface TranslationServices {
    @Action(input = "http://translation.efiAnalytics.com/TranslationServices/getTranslatedStaticTextRequest", output = "http://translation.efiAnalytics.com/TranslationServices/getTranslatedStaticTextResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTranslatedStaticText", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.GetTranslatedStaticText")
    @ResponseWrapper(localName = "getTranslatedStaticTextResponse", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.GetTranslatedStaticTextResponse")
    @WebMethod
    String getTranslatedStaticText(@WebParam(name = "appId", targetNamespace = "") String str, @WebParam(name = "uid", targetNamespace = "") String str2, @WebParam(name = "regKey", targetNamespace = "") String str3, @WebParam(name = "baseText", targetNamespace = "") String str4, @WebParam(name = "baseLangCode", targetNamespace = "") String str5, @WebParam(name = "targetLangCode", targetNamespace = "") String str6);

    @Action(input = "http://translation.efiAnalytics.com/TranslationServices/addUpdateProposedStaticTranslationRequest", output = "http://translation.efiAnalytics.com/TranslationServices/addUpdateProposedStaticTranslationResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addUpdateProposedStaticTranslation", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.AddUpdateProposedStaticTranslation")
    @ResponseWrapper(localName = "addUpdateProposedStaticTranslationResponse", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.AddUpdateProposedStaticTranslationResponse")
    @WebMethod
    MutationResult addUpdateProposedStaticTranslation(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2, @WebParam(name = "appId", targetNamespace = "") String str3, @WebParam(name = "uid", targetNamespace = "") String str4, @WebParam(name = "regKey", targetNamespace = "") String str5, @WebParam(name = "baseLangCode", targetNamespace = "") String str6, @WebParam(name = "targetLangCode", targetNamespace = "") String str7, @WebParam(name = "baseText", targetNamespace = "") String str8, @WebParam(name = "translatedText", targetNamespace = "") String str9);

    @Action(input = "http://translation.efiAnalytics.com/TranslationServices/getAllTranslationProposalsRequest", output = "http://translation.efiAnalytics.com/TranslationServices/getAllTranslationProposalsResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllTranslationProposals", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.GetAllTranslationProposals")
    @ResponseWrapper(localName = "getAllTranslationProposalsResponse", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.GetAllTranslationProposalsResponse")
    @WebMethod
    List<TranslationStaticProposed> getAllTranslationProposals(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2, @WebParam(name = "targetLangCode", targetNamespace = "") String str3);

    @Action(input = "http://translation.efiAnalytics.com/TranslationServices/getTranslationProposalRequest", output = "http://translation.efiAnalytics.com/TranslationServices/getTranslationProposalResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getTranslationProposal", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.GetTranslationProposal")
    @ResponseWrapper(localName = "getTranslationProposalResponse", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.GetTranslationProposalResponse")
    @WebMethod
    TranslationStaticProposed getTranslationProposal(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2, @WebParam(name = "baseText", targetNamespace = "") String str3, @WebParam(name = "baseLangCode", targetNamespace = "") String str4, @WebParam(name = "targetLangCode", targetNamespace = "") String str5);

    @Action(input = "http://translation.efiAnalytics.com/TranslationServices/approveProposedTranslationRequest", output = "http://translation.efiAnalytics.com/TranslationServices/approveProposedTranslationResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "approveProposedTranslation", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.ApproveProposedTranslation")
    @ResponseWrapper(localName = "approveProposedTranslationResponse", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.ApproveProposedTranslationResponse")
    @WebMethod
    MutationResult approveProposedTranslation(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2, @WebParam(name = "baseText", targetNamespace = "") String str3, @WebParam(name = "baseLangCode", targetNamespace = "") String str4, @WebParam(name = "targetLangCode", targetNamespace = "") String str5);

    @Action(input = "http://translation.efiAnalytics.com/TranslationServices/deleteTranslationProposalRequest", output = "http://translation.efiAnalytics.com/TranslationServices/deleteTranslationProposalResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deleteTranslationProposal", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.DeleteTranslationProposal")
    @ResponseWrapper(localName = "deleteTranslationProposalResponse", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.DeleteTranslationProposalResponse")
    @WebMethod
    MutationResult deleteTranslationProposal(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2, @WebParam(name = "baseText", targetNamespace = "") String str3, @WebParam(name = "baseLangCode", targetNamespace = "") String str4, @WebParam(name = "targetLangCode", targetNamespace = "") String str5);

    @Action(input = "http://translation.efiAnalytics.com/TranslationServices/deleteTranslationRequest", output = "http://translation.efiAnalytics.com/TranslationServices/deleteTranslationResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deleteTranslation", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.DeleteTranslation")
    @ResponseWrapper(localName = "deleteTranslationResponse", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.DeleteTranslationResponse")
    @WebMethod
    MutationResult deleteTranslation(@WebParam(name = "appId", targetNamespace = "") String str, @WebParam(name = "userId", targetNamespace = "") String str2, @WebParam(name = "password", targetNamespace = "") String str3, @WebParam(name = "baseLangCode", targetNamespace = "") String str4, @WebParam(name = "targetLangCode", targetNamespace = "") String str5, @WebParam(name = "baseText", targetNamespace = "") String str6);

    @Action(input = "http://translation.efiAnalytics.com/TranslationServices/addOrUpdateTranslationRequest", output = "http://translation.efiAnalytics.com/TranslationServices/addOrUpdateTranslationResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addOrUpdateTranslation", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.AddOrUpdateTranslation")
    @ResponseWrapper(localName = "addOrUpdateTranslationResponse", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.AddOrUpdateTranslationResponse")
    @WebMethod
    MutationResult addOrUpdateTranslation(@WebParam(name = "userId", targetNamespace = "") String str, @WebParam(name = "password", targetNamespace = "") String str2, @WebParam(name = "appId", targetNamespace = "") String str3, @WebParam(name = "uid", targetNamespace = "") String str4, @WebParam(name = "regKey", targetNamespace = "") String str5, @WebParam(name = "source", targetNamespace = "") String str6, @WebParam(name = "status", targetNamespace = "") String str7, @WebParam(name = "baseLangCode", targetNamespace = "") String str8, @WebParam(name = "targetLangCode", targetNamespace = "") String str9, @WebParam(name = "baseText", targetNamespace = "") String str10, @WebParam(name = "translatedText", targetNamespace = "") String str11);

    @Action(input = "http://translation.efiAnalytics.com/TranslationServices/getAllTranslationsSinceRequest", output = "http://translation.efiAnalytics.com/TranslationServices/getAllTranslationsSinceResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllTranslationsSince", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.GetAllTranslationsSince")
    @ResponseWrapper(localName = "getAllTranslationsSinceResponse", targetNamespace = "http://translation.efiAnalytics.com/", className = "com.efianalytics.translation.GetAllTranslationsSinceResponse")
    @WebMethod
    List<TranslationsStatic> getAllTranslationsSince(@WebParam(name = "appId", targetNamespace = "") String str, @WebParam(name = "uid", targetNamespace = "") String str2, @WebParam(name = "regKey", targetNamespace = "") String str3, @WebParam(name = "targetLangCode", targetNamespace = "") String str4, @WebParam(name = "sinceDate", targetNamespace = "") XMLGregorianCalendar xMLGregorianCalendar);
}
