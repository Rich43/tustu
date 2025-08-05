package com.efianalytics.translation;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/ObjectFactory.class */
public class ObjectFactory {
    private static final QName _GetTranslatedStaticText_QNAME = new QName("http://translation.efiAnalytics.com/", "getTranslatedStaticText");
    private static final QName _GetAllTranslationsSince_QNAME = new QName("http://translation.efiAnalytics.com/", "getAllTranslationsSince");
    private static final QName _DeleteTranslationResponse_QNAME = new QName("http://translation.efiAnalytics.com/", "deleteTranslationResponse");
    private static final QName _GetAllTranslationsSinceResponse_QNAME = new QName("http://translation.efiAnalytics.com/", "getAllTranslationsSinceResponse");
    private static final QName _AddUpdateProposedStaticTranslationResponse_QNAME = new QName("http://translation.efiAnalytics.com/", "addUpdateProposedStaticTranslationResponse");
    private static final QName _DeleteTranslationProposalResponse_QNAME = new QName("http://translation.efiAnalytics.com/", "deleteTranslationProposalResponse");
    private static final QName _DeleteTranslationProposal_QNAME = new QName("http://translation.efiAnalytics.com/", "deleteTranslationProposal");
    private static final QName _ApproveProposedTranslation_QNAME = new QName("http://translation.efiAnalytics.com/", "approveProposedTranslation");
    private static final QName _GetAllTranslationProposals_QNAME = new QName("http://translation.efiAnalytics.com/", "getAllTranslationProposals");
    private static final QName _GetAllTranslationProposalsResponse_QNAME = new QName("http://translation.efiAnalytics.com/", "getAllTranslationProposalsResponse");
    private static final QName _GetTranslationProposalResponse_QNAME = new QName("http://translation.efiAnalytics.com/", "getTranslationProposalResponse");
    private static final QName _ApproveProposedTranslationResponse_QNAME = new QName("http://translation.efiAnalytics.com/", "approveProposedTranslationResponse");
    private static final QName _AddOrUpdateTranslation_QNAME = new QName("http://translation.efiAnalytics.com/", "addOrUpdateTranslation");
    private static final QName _DeleteTranslation_QNAME = new QName("http://translation.efiAnalytics.com/", "deleteTranslation");
    private static final QName _GetTranslatedStaticTextResponse_QNAME = new QName("http://translation.efiAnalytics.com/", "getTranslatedStaticTextResponse");
    private static final QName _GetTranslationProposal_QNAME = new QName("http://translation.efiAnalytics.com/", "getTranslationProposal");
    private static final QName _AddOrUpdateTranslationResponse_QNAME = new QName("http://translation.efiAnalytics.com/", "addOrUpdateTranslationResponse");
    private static final QName _AddUpdateProposedStaticTranslation_QNAME = new QName("http://translation.efiAnalytics.com/", "addUpdateProposedStaticTranslation");

    public AddOrUpdateTranslation createAddOrUpdateTranslation() {
        return new AddOrUpdateTranslation();
    }

    public GetAllTranslationsSinceResponse createGetAllTranslationsSinceResponse() {
        return new GetAllTranslationsSinceResponse();
    }

    public TranslationsStatic createTranslationsStatic() {
        return new TranslationsStatic();
    }

    public ApproveProposedTranslationResponse createApproveProposedTranslationResponse() {
        return new ApproveProposedTranslationResponse();
    }

    public AddUpdateProposedStaticTranslation createAddUpdateProposedStaticTranslation() {
        return new AddUpdateProposedStaticTranslation();
    }

    public GetAllTranslationProposalsResponse createGetAllTranslationProposalsResponse() {
        return new GetAllTranslationProposalsResponse();
    }

    public GetAllTranslationProposals createGetAllTranslationProposals() {
        return new GetAllTranslationProposals();
    }

    public GetTranslatedStaticText createGetTranslatedStaticText() {
        return new GetTranslatedStaticText();
    }

    public DeleteTranslationProposal createDeleteTranslationProposal() {
        return new DeleteTranslationProposal();
    }

    public TranslationStaticProposed createTranslationStaticProposed() {
        return new TranslationStaticProposed();
    }

    public GetTranslatedStaticTextResponse createGetTranslatedStaticTextResponse() {
        return new GetTranslatedStaticTextResponse();
    }

    public TranslationStaticProposedPK createTranslationStaticProposedPK() {
        return new TranslationStaticProposedPK();
    }

    public AddUpdateProposedStaticTranslationResponse createAddUpdateProposedStaticTranslationResponse() {
        return new AddUpdateProposedStaticTranslationResponse();
    }

    public MutationResult createMutationResult() {
        return new MutationResult();
    }

    public GetAllTranslationsSince createGetAllTranslationsSince() {
        return new GetAllTranslationsSince();
    }

    public GetTranslationProposalResponse createGetTranslationProposalResponse() {
        return new GetTranslationProposalResponse();
    }

    public TranslationsStaticPK createTranslationsStaticPK() {
        return new TranslationsStaticPK();
    }

    public DeleteTranslationProposalResponse createDeleteTranslationProposalResponse() {
        return new DeleteTranslationProposalResponse();
    }

    public DeleteTranslationResponse createDeleteTranslationResponse() {
        return new DeleteTranslationResponse();
    }

    public ApproveProposedTranslation createApproveProposedTranslation() {
        return new ApproveProposedTranslation();
    }

    public GetTranslationProposal createGetTranslationProposal() {
        return new GetTranslationProposal();
    }

    public DeleteTranslation createDeleteTranslation() {
        return new DeleteTranslation();
    }

    public AddOrUpdateTranslationResponse createAddOrUpdateTranslationResponse() {
        return new AddOrUpdateTranslationResponse();
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "getTranslatedStaticText")
    public JAXBElement<GetTranslatedStaticText> createGetTranslatedStaticText(GetTranslatedStaticText value) {
        return new JAXBElement<>(_GetTranslatedStaticText_QNAME, GetTranslatedStaticText.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "getAllTranslationsSince")
    public JAXBElement<GetAllTranslationsSince> createGetAllTranslationsSince(GetAllTranslationsSince value) {
        return new JAXBElement<>(_GetAllTranslationsSince_QNAME, GetAllTranslationsSince.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "deleteTranslationResponse")
    public JAXBElement<DeleteTranslationResponse> createDeleteTranslationResponse(DeleteTranslationResponse value) {
        return new JAXBElement<>(_DeleteTranslationResponse_QNAME, DeleteTranslationResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "getAllTranslationsSinceResponse")
    public JAXBElement<GetAllTranslationsSinceResponse> createGetAllTranslationsSinceResponse(GetAllTranslationsSinceResponse value) {
        return new JAXBElement<>(_GetAllTranslationsSinceResponse_QNAME, GetAllTranslationsSinceResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "addUpdateProposedStaticTranslationResponse")
    public JAXBElement<AddUpdateProposedStaticTranslationResponse> createAddUpdateProposedStaticTranslationResponse(AddUpdateProposedStaticTranslationResponse value) {
        return new JAXBElement<>(_AddUpdateProposedStaticTranslationResponse_QNAME, AddUpdateProposedStaticTranslationResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "deleteTranslationProposalResponse")
    public JAXBElement<DeleteTranslationProposalResponse> createDeleteTranslationProposalResponse(DeleteTranslationProposalResponse value) {
        return new JAXBElement<>(_DeleteTranslationProposalResponse_QNAME, DeleteTranslationProposalResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "deleteTranslationProposal")
    public JAXBElement<DeleteTranslationProposal> createDeleteTranslationProposal(DeleteTranslationProposal value) {
        return new JAXBElement<>(_DeleteTranslationProposal_QNAME, DeleteTranslationProposal.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "approveProposedTranslation")
    public JAXBElement<ApproveProposedTranslation> createApproveProposedTranslation(ApproveProposedTranslation value) {
        return new JAXBElement<>(_ApproveProposedTranslation_QNAME, ApproveProposedTranslation.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "getAllTranslationProposals")
    public JAXBElement<GetAllTranslationProposals> createGetAllTranslationProposals(GetAllTranslationProposals value) {
        return new JAXBElement<>(_GetAllTranslationProposals_QNAME, GetAllTranslationProposals.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "getAllTranslationProposalsResponse")
    public JAXBElement<GetAllTranslationProposalsResponse> createGetAllTranslationProposalsResponse(GetAllTranslationProposalsResponse value) {
        return new JAXBElement<>(_GetAllTranslationProposalsResponse_QNAME, GetAllTranslationProposalsResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "getTranslationProposalResponse")
    public JAXBElement<GetTranslationProposalResponse> createGetTranslationProposalResponse(GetTranslationProposalResponse value) {
        return new JAXBElement<>(_GetTranslationProposalResponse_QNAME, GetTranslationProposalResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "approveProposedTranslationResponse")
    public JAXBElement<ApproveProposedTranslationResponse> createApproveProposedTranslationResponse(ApproveProposedTranslationResponse value) {
        return new JAXBElement<>(_ApproveProposedTranslationResponse_QNAME, ApproveProposedTranslationResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "addOrUpdateTranslation")
    public JAXBElement<AddOrUpdateTranslation> createAddOrUpdateTranslation(AddOrUpdateTranslation value) {
        return new JAXBElement<>(_AddOrUpdateTranslation_QNAME, AddOrUpdateTranslation.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "deleteTranslation")
    public JAXBElement<DeleteTranslation> createDeleteTranslation(DeleteTranslation value) {
        return new JAXBElement<>(_DeleteTranslation_QNAME, DeleteTranslation.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "getTranslatedStaticTextResponse")
    public JAXBElement<GetTranslatedStaticTextResponse> createGetTranslatedStaticTextResponse(GetTranslatedStaticTextResponse value) {
        return new JAXBElement<>(_GetTranslatedStaticTextResponse_QNAME, GetTranslatedStaticTextResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "getTranslationProposal")
    public JAXBElement<GetTranslationProposal> createGetTranslationProposal(GetTranslationProposal value) {
        return new JAXBElement<>(_GetTranslationProposal_QNAME, GetTranslationProposal.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "addOrUpdateTranslationResponse")
    public JAXBElement<AddOrUpdateTranslationResponse> createAddOrUpdateTranslationResponse(AddOrUpdateTranslationResponse value) {
        return new JAXBElement<>(_AddOrUpdateTranslationResponse_QNAME, AddOrUpdateTranslationResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://translation.efiAnalytics.com/", name = "addUpdateProposedStaticTranslation")
    public JAXBElement<AddUpdateProposedStaticTranslation> createAddUpdateProposedStaticTranslation(AddUpdateProposedStaticTranslation value) {
        return new JAXBElement<>(_AddUpdateProposedStaticTranslation_QNAME, AddUpdateProposedStaticTranslation.class, null, value);
    }
}
