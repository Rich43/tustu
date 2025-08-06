package az;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

/**
 * Represents the information needed to request an activation code from the
 * server.
 */
public class ActivationRequest {
    private String operatingSystem = "";
    private String hardwareId = "";
    private String motherboardId = "";
    private String deviceId = "";
    private String registrationKey = "";
    private String email = "";
    private String userId = "";

    /**
     * Serialise the request data to a Base64 encoded string understood by the
     * activation server.
     */
    public String toBase64() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("os", operatingSystem);
        properties.setProperty("dId", deviceId);
        properties.setProperty("hId", hardwareId);
        properties.setProperty("mId", motherboardId);
        properties.setProperty("regKey", registrationKey);
        properties.setProperty("email", email);
        properties.setProperty("uid", userId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        properties.store(out, "");
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem != null ? operatingSystem : "";
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        if (hardwareId != null) {
            this.hardwareId = hardwareId;
        }
    }

    public String getMotherboardId() {
        return motherboardId;
    }

    public void setMotherboardId(String motherboardId) {
        if (motherboardId != null) {
            this.motherboardId = motherboardId;
        }
    }

    public String getRegistrationKey() {
        return registrationKey;
    }

    public void setRegistrationKey(String registrationKey) {
        this.registrationKey = registrationKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        if (userId != null) {
            this.userId = userId;
        }
    }

    public void setDeviceId(String deviceId) {
        if (deviceId != null) {
            this.deviceId = deviceId;
        }
    }

    public String getDeviceId() {
        return deviceId;
    }
}
