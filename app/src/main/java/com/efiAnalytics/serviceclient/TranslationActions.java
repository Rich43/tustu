package com.efianalytics.serviceclient;

import com.efianalytics.translation.MutationResult;
import com.efianalytics.translation.TranslationServices;
import com.efianalytics.translation.TranslationServicesService;
import com.efianalytics.translation.TranslationStaticProposed;
import com.efianalytics.translation.TranslationsStatic;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

/* loaded from: efiaServicesClient.jar:com/efianalytics/serviceclient/TranslationActions.class */
public class TranslationActions {
    public static List<TranslationStaticProposed> getAllTranslationProposals(String userId, String password, String targetLangCode) {
        TranslationServicesService service = new TranslationServicesService();
        TranslationServices port = service.getTranslationServicesPort();
        return port.getAllTranslationProposals(userId, password, targetLangCode);
    }

    public static MutationResult approveProposedTranslation(String userId, String password, String baseLangCode, String targetLangCode, String baseText) {
        TranslationServicesService service = new TranslationServicesService();
        TranslationServices port = service.getTranslationServicesPort();
        return port.approveProposedTranslation(userId, password, baseLangCode, targetLangCode, baseText);
    }

    public static MutationResult deleteTranslationProposal(String userId, String password, String baseLangCode, String targetLangCode, String baseText) {
        TranslationServicesService service = new TranslationServicesService();
        TranslationServices port = service.getTranslationServicesPort();
        return port.deleteTranslationProposal(userId, password, baseLangCode, targetLangCode, baseText);
    }

    public static MutationResult deleteTranslation(String appId, String userId, String password, String baseLangCode, String targetLangCode, String baseText) {
        TranslationServicesService service = new TranslationServicesService();
        TranslationServices port = service.getTranslationServicesPort();
        return port.deleteTranslation(appId, userId, password, baseLangCode, targetLangCode, baseText);
    }

    public static MutationResult addOrUpdateTranslation(String userId, String password, String appId, String uid, String regKey, String source, String status, String baseLangCode, String targetLangCode, String baseText, String translatedText) {
        TranslationServicesService service = new TranslationServicesService();
        TranslationServices port = service.getTranslationServicesPort();
        return port.addOrUpdateTranslation(userId, password, appId, uid, regKey, source, status, baseLangCode, targetLangCode, baseText, translatedText);
    }

    public static TranslationStaticProposed getTranslationProposal(String userId, String password, String baseText, String baseLangCode, String targetLangCode) {
        TranslationServicesService service = new TranslationServicesService();
        TranslationServices port = service.getTranslationServicesPort();
        return port.getTranslationProposal(userId, password, baseText, baseLangCode, targetLangCode);
    }

    public static MutationResult addUpdateProposedStaticTranslation(String userId, String password, String appId, String uid, String regKey, String baseLangCode, String targetLangCode, String baseText, String translatedText) {
        TranslationServicesService service = new TranslationServicesService();
        TranslationServices port = service.getTranslationServicesPort();
        return port.addUpdateProposedStaticTranslation(userId, password, appId, uid, regKey, baseLangCode, targetLangCode, baseText, translatedText);
    }

    public static String getTranslatedStaticText(String appId, String uid, String regKey, String baseText, String baseLangCode, String targetLangCode) {
        TranslationServicesService service = new TranslationServicesService();
        TranslationServices port = service.getTranslationServicesPort();
        return port.getTranslatedStaticText(appId, uid, regKey, baseText, baseLangCode, targetLangCode);
    }

    public static List<TranslationsStatic> getAllTranslationsSince(String appId, String uid, String regKey, String targetLangCode, XMLGregorianCalendar sinceDate) {
        TranslationServicesService service = new TranslationServicesService();
        TranslationServices port = service.getTranslationServicesPort();
        return port.getAllTranslationsSince(appId, uid, regKey, targetLangCode, sinceDate);
    }
}
