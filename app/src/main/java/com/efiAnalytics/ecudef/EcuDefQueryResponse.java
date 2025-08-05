package com.efianalytics.ecudef;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ecuDefQueryResponse", propOrder = {"ecuDefFound", "fileMd5Checksum", "knownFirmwares", "serialSignature"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/EcuDefQueryResponse.class */
public class EcuDefQueryResponse {
    protected boolean ecuDefFound;
    protected String fileMd5Checksum;

    @XmlElement(nillable = true)
    protected List<String> knownFirmwares;
    protected String serialSignature;

    public boolean isEcuDefFound() {
        return this.ecuDefFound;
    }

    public void setEcuDefFound(boolean value) {
        this.ecuDefFound = value;
    }

    public String getFileMd5Checksum() {
        return this.fileMd5Checksum;
    }

    public void setFileMd5Checksum(String value) {
        this.fileMd5Checksum = value;
    }

    public List<String> getKnownFirmwares() {
        if (this.knownFirmwares == null) {
            this.knownFirmwares = new ArrayList();
        }
        return this.knownFirmwares;
    }

    public String getSerialSignature() {
        return this.serialSignature;
    }

    public void setSerialSignature(String value) {
        this.serialSignature = value;
    }
}
