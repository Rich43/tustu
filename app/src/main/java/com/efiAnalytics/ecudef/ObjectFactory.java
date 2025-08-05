package com.efianalytics.ecudef;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/ObjectFactory.class */
public class ObjectFactory {
    private static final QName _FindEcuDefForQueryStringResponse_QNAME = new QName("http://ecudef.efiAnalytics.com/", "findEcuDefForQueryStringResponse");
    private static final QName _FindEcuDefForQueryString_QNAME = new QName("http://ecudef.efiAnalytics.com/", "findEcuDefForQueryString");
    private static final QName _GetAllKnownFirmwares_QNAME = new QName("http://ecudef.efiAnalytics.com/", "getAllKnownFirmwares");
    private static final QName _GetAllKnownFirmwaresResponse_QNAME = new QName("http://ecudef.efiAnalytics.com/", "getAllKnownFirmwaresResponse");
    private static final QName _GetEcuDefinition_QNAME = new QName("http://ecudef.efiAnalytics.com/", "getEcuDefinition");
    private static final QName _FindEcuDefForSerialSignatureResponse_QNAME = new QName("http://ecudef.efiAnalytics.com/", "findEcuDefForSerialSignatureResponse");
    private static final QName _GetEcuDefinitionResponse_QNAME = new QName("http://ecudef.efiAnalytics.com/", "getEcuDefinitionResponse");
    private static final QName _FindEcuDefForSerialSignature_QNAME = new QName("http://ecudef.efiAnalytics.com/", "findEcuDefForSerialSignature");
    private static final QName _SubmitEcuDefFileResponse_QNAME = new QName("http://ecudef.efiAnalytics.com/", "submitEcuDefFileResponse");
    private static final QName _SubmitEcuDefFile_QNAME = new QName("http://ecudef.efiAnalytics.com/", "submitEcuDefFile");
    private static final QName _SubmitEcuDefFileMd5Checksum_QNAME = new QName("", "md5Checksum");
    private static final QName _SubmitEcuDefFileFileData_QNAME = new QName("", "fileData");

    public ClientEcuDefinitionFile createClientEcuDefinitionFile() {
        return new ClientEcuDefinitionFile();
    }

    public FindEcuDefForSerialSignature createFindEcuDefForSerialSignature() {
        return new FindEcuDefForSerialSignature();
    }

    public EcuDefQueryResponse createEcuDefQueryResponse() {
        return new EcuDefQueryResponse();
    }

    public FindEcuDefForQueryString createFindEcuDefForQueryString() {
        return new FindEcuDefForQueryString();
    }

    public GetAllKnownFirmwares createGetAllKnownFirmwares() {
        return new GetAllKnownFirmwares();
    }

    public FindEcuDefForSerialSignatureResponse createFindEcuDefForSerialSignatureResponse() {
        return new FindEcuDefForSerialSignatureResponse();
    }

    public FindEcuDefForQueryStringResponse createFindEcuDefForQueryStringResponse() {
        return new FindEcuDefForQueryStringResponse();
    }

    public GetEcuDefinitionResponse createGetEcuDefinitionResponse() {
        return new GetEcuDefinitionResponse();
    }

    public SubmitEcuDefFile createSubmitEcuDefFile() {
        return new SubmitEcuDefFile();
    }

    public GetAllKnownFirmwaresResponse createGetAllKnownFirmwaresResponse() {
        return new GetAllKnownFirmwaresResponse();
    }

    public GetEcuDefinition createGetEcuDefinition() {
        return new GetEcuDefinition();
    }

    public SubmitEcuDefFileResponse createSubmitEcuDefFileResponse() {
        return new SubmitEcuDefFileResponse();
    }

    public EcuDefSubmissionResponse createEcuDefSubmissionResponse() {
        return new EcuDefSubmissionResponse();
    }

    public FirmwareIdentifier createFirmwareIdentifier() {
        return new FirmwareIdentifier();
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "findEcuDefForQueryStringResponse")
    public JAXBElement<FindEcuDefForQueryStringResponse> createFindEcuDefForQueryStringResponse(FindEcuDefForQueryStringResponse value) {
        return new JAXBElement<>(_FindEcuDefForQueryStringResponse_QNAME, FindEcuDefForQueryStringResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "findEcuDefForQueryString")
    public JAXBElement<FindEcuDefForQueryString> createFindEcuDefForQueryString(FindEcuDefForQueryString value) {
        return new JAXBElement<>(_FindEcuDefForQueryString_QNAME, FindEcuDefForQueryString.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "getAllKnownFirmwares")
    public JAXBElement<GetAllKnownFirmwares> createGetAllKnownFirmwares(GetAllKnownFirmwares value) {
        return new JAXBElement<>(_GetAllKnownFirmwares_QNAME, GetAllKnownFirmwares.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "getAllKnownFirmwaresResponse")
    public JAXBElement<GetAllKnownFirmwaresResponse> createGetAllKnownFirmwaresResponse(GetAllKnownFirmwaresResponse value) {
        return new JAXBElement<>(_GetAllKnownFirmwaresResponse_QNAME, GetAllKnownFirmwaresResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "getEcuDefinition")
    public JAXBElement<GetEcuDefinition> createGetEcuDefinition(GetEcuDefinition value) {
        return new JAXBElement<>(_GetEcuDefinition_QNAME, GetEcuDefinition.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "findEcuDefForSerialSignatureResponse")
    public JAXBElement<FindEcuDefForSerialSignatureResponse> createFindEcuDefForSerialSignatureResponse(FindEcuDefForSerialSignatureResponse value) {
        return new JAXBElement<>(_FindEcuDefForSerialSignatureResponse_QNAME, FindEcuDefForSerialSignatureResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "getEcuDefinitionResponse")
    public JAXBElement<GetEcuDefinitionResponse> createGetEcuDefinitionResponse(GetEcuDefinitionResponse value) {
        return new JAXBElement<>(_GetEcuDefinitionResponse_QNAME, GetEcuDefinitionResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "findEcuDefForSerialSignature")
    public JAXBElement<FindEcuDefForSerialSignature> createFindEcuDefForSerialSignature(FindEcuDefForSerialSignature value) {
        return new JAXBElement<>(_FindEcuDefForSerialSignature_QNAME, FindEcuDefForSerialSignature.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "submitEcuDefFileResponse")
    public JAXBElement<SubmitEcuDefFileResponse> createSubmitEcuDefFileResponse(SubmitEcuDefFileResponse value) {
        return new JAXBElement<>(_SubmitEcuDefFileResponse_QNAME, SubmitEcuDefFileResponse.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ecudef.efiAnalytics.com/", name = "submitEcuDefFile")
    public JAXBElement<SubmitEcuDefFile> createSubmitEcuDefFile(SubmitEcuDefFile value) {
        return new JAXBElement<>(_SubmitEcuDefFile_QNAME, SubmitEcuDefFile.class, null, value);
    }

    @XmlElementDecl(namespace = "", name = "md5Checksum", scope = SubmitEcuDefFile.class)
    public JAXBElement<byte[]> createSubmitEcuDefFileMd5Checksum(byte[] value) {
        return new JAXBElement<>(_SubmitEcuDefFileMd5Checksum_QNAME, byte[].class, SubmitEcuDefFile.class, value);
    }

    @XmlElementDecl(namespace = "", name = "fileData", scope = SubmitEcuDefFile.class)
    public JAXBElement<byte[]> createSubmitEcuDefFileFileData(byte[] value) {
        return new JAXBElement<>(_SubmitEcuDefFileFileData_QNAME, byte[].class, SubmitEcuDefFile.class, value);
    }
}
