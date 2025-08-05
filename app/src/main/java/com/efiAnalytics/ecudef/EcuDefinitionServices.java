package com.efianalytics.ecudef;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@XmlSeeAlso({ObjectFactory.class})
@WebService(name = "EcuDefinitionServices", targetNamespace = "http://ecudef.efiAnalytics.com/")
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/EcuDefinitionServices.class */
public interface EcuDefinitionServices {
    @Action(input = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/findEcuDefForSerialSignatureRequest", output = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/findEcuDefForSerialSignatureResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findEcuDefForSerialSignature", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.FindEcuDefForSerialSignature")
    @ResponseWrapper(localName = "findEcuDefForSerialSignatureResponse", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.FindEcuDefForSerialSignatureResponse")
    @WebMethod
    EcuDefQueryResponse findEcuDefForSerialSignature(@WebParam(name = "serialSignature", targetNamespace = "") String str, @WebParam(name = "uid", targetNamespace = "") String str2);

    @Action(input = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/findEcuDefForQueryStringRequest", output = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/findEcuDefForQueryStringResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "findEcuDefForQueryString", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.FindEcuDefForQueryString")
    @ResponseWrapper(localName = "findEcuDefForQueryStringResponse", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.FindEcuDefForQueryStringResponse")
    @WebMethod
    EcuDefQueryResponse findEcuDefForQueryString(@WebParam(name = "queryString", targetNamespace = "") String str, @WebParam(name = "uid", targetNamespace = "") String str2);

    @Action(input = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/submitEcuDefFileRequest", output = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/submitEcuDefFileResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "submitEcuDefFile", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.SubmitEcuDefFile")
    @ResponseWrapper(localName = "submitEcuDefFileResponse", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.SubmitEcuDefFileResponse")
    @WebMethod
    EcuDefSubmissionResponse submitEcuDefFile(@WebParam(name = "uid", targetNamespace = "") String str, @WebParam(name = "serialSignature", targetNamespace = "") String str2, @WebParam(name = "firmwareVersion", targetNamespace = "") String str3, @WebParam(name = "fileData", targetNamespace = "") byte[] bArr, @WebParam(name = "fileFormat", targetNamespace = "") String str4, @WebParam(name = "md5Checksum", targetNamespace = "") byte[] bArr2, @WebParam(name = "appName", targetNamespace = "") String str5, @WebParam(name = "appEdition", targetNamespace = "") String str6, @WebParam(name = "userId", targetNamespace = "") String str7, @WebParam(name = "password", targetNamespace = "") String str8);

    @Action(input = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/getAllKnownFirmwaresRequest", output = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/getAllKnownFirmwaresResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getAllKnownFirmwares", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.GetAllKnownFirmwares")
    @ResponseWrapper(localName = "getAllKnownFirmwaresResponse", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.GetAllKnownFirmwaresResponse")
    @WebMethod
    List<FirmwareIdentifier> getAllKnownFirmwares(@WebParam(name = "uid", targetNamespace = "") String str, @WebParam(name = "appName", targetNamespace = "") String str2, @WebParam(name = "appEdition", targetNamespace = "") String str3);

    @Action(input = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/getEcuDefinitionRequest", output = "http://ecudef.efiAnalytics.com/EcuDefinitionServices/getEcuDefinitionResponse")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getEcuDefinition", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.GetEcuDefinition")
    @ResponseWrapper(localName = "getEcuDefinitionResponse", targetNamespace = "http://ecudef.efiAnalytics.com/", className = "com.efianalytics.ecudef.GetEcuDefinitionResponse")
    @WebMethod
    ClientEcuDefinitionFile getEcuDefinition(@WebParam(name = "uid", targetNamespace = "") String str, @WebParam(name = "serialSignature", targetNamespace = "") String str2, @WebParam(name = "fileFormat", targetNamespace = "") String str3);
}
