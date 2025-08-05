package com.efianalytics.serviceclient;

import com.efianalytics.ecudef.ClientEcuDefinitionFile;
import com.efianalytics.ecudef.EcuDefQueryResponse;
import com.efianalytics.ecudef.EcuDefSubmissionResponse;
import com.efianalytics.ecudef.EcuDefinitionServices;
import com.efianalytics.ecudef.EcuDefinitionServicesService;
import com.efianalytics.ecudef.FirmwareIdentifier;
import java.util.List;

/* loaded from: efiaServicesClient.jar:com/efianalytics/serviceclient/EcuDefinitionActions.class */
public class EcuDefinitionActions {
    public static EcuDefQueryResponse findEcuDefForSerialSignature(String serialSignature, String uid) {
        EcuDefinitionServicesService service = new EcuDefinitionServicesService();
        EcuDefinitionServices port = service.getEcuDefinitionServicesPort();
        return port.findEcuDefForSerialSignature(serialSignature, uid);
    }

    public static EcuDefSubmissionResponse submitEcuDefFile(String uid, String serialSignature, String firmwareVersion, byte[] fileData, String fileFormat, byte[] md5Checksum, String appName, String appEdition, String userId, String password) {
        EcuDefinitionServicesService service = new EcuDefinitionServicesService();
        EcuDefinitionServices port = service.getEcuDefinitionServicesPort();
        return port.submitEcuDefFile(uid, serialSignature, firmwareVersion, fileData, fileFormat, md5Checksum, appName, appEdition, userId, password);
    }

    public static List<FirmwareIdentifier> getAllKnownFirmwares(String uid, String appName, String appEdition) {
        EcuDefinitionServicesService service = new EcuDefinitionServicesService();
        EcuDefinitionServices port = service.getEcuDefinitionServicesPort();
        return port.getAllKnownFirmwares(uid, appName, appEdition);
    }

    public static ClientEcuDefinitionFile getEcuDefinition(String uid, String serialSignature, String fileFormat) {
        EcuDefinitionServicesService service = new EcuDefinitionServicesService();
        EcuDefinitionServices port = service.getEcuDefinitionServicesPort();
        return port.getEcuDefinition(uid, serialSignature, fileFormat);
    }
}
