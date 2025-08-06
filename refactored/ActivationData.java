package az;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

/**
 * Parsed activation information returned by the activation server.
 */
public class ActivationData {
    private String macAddress = "";
    private String hardwareId = "";
    private String motherboardId = "";
    private String deviceId = "";
    private String registrationKey = "";
    private String email = "";
    private int errorCode = 2;
    private String message = "";
    private int activationCount = 0;
    private Date renewalDate = new Date(0);
    private String rawData;

    public ActivationData() {
    }

    public ActivationData(String encoded) throws ActivationParseException {
        parse(encoded);
    }

    /**
     * Parse the Base64 encoded activation string produced by the server.
     */
    public final void parse(String encoded) throws ActivationParseException {
        this.rawData = encoded;
        Properties props = new Properties();
        try {
            byte[] decoded = Base64.getDecoder().decode(encoded);
            props.load(new ByteArrayInputStream(decoded));
            macAddress = props.getProperty("mac", "");
            hardwareId = props.getProperty("hId", "");
            motherboardId = props.getProperty("mId", "");
            deviceId = props.getProperty("dId", "");
            registrationKey = props.getProperty("rk", "");
            email = props.getProperty("em", "");
            try {
                errorCode = Integer.parseInt(props.getProperty("ec", "2"));
            } catch (NumberFormatException ignore) {
                errorCode = 2;
            }
            message = props.getProperty("msg", "");
            try {
                activationCount = Integer.parseInt(props.getProperty("actCount", "0"));
            } catch (NumberFormatException ignore) {
                activationCount = 0;
            }
            try {
                long renew = Long.parseLong(props.getProperty("renewDate", "0"));
                renewalDate = new Date(renew);
            } catch (NumberFormatException ignore) {
                renewalDate = new Date(0);
            }
        } catch (IllegalArgumentException | java.io.IOException e) {
            throw new ActivationParseException("Invalid activation data.", e);
        }
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public String getMotherboardId() {
        return motherboardId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getRegistrationKey() {
        return registrationKey;
    }

    public String getEmail() {
        return email;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public int getActivationCount() {
        return activationCount;
    }

    public Date getRenewalDate() {
        return renewalDate;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}
